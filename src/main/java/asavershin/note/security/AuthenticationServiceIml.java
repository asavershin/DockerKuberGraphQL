package asavershin.note.security;

import asavershin.note.dto.AuthenticationRequest;
import asavershin.note.dto.AuthenticationResponse;
import asavershin.note.entities.UserEntity;
import asavershin.note.entities.UserRole;
import asavershin.note.exceptions.AuthException;
import asavershin.note.repositories.UserRepository;
import asavershin.note.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceIml implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public void register(UserEntity user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.setUserRole(UserRole.USER);
        if (userRepository.existByUserEmail(user.getUserEmail())) {
            throw new AuthException("User exists with email:" + user.getUserEmail());
        }
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userDetailsService.loadUserByUsername(request.getEmail());

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenRepository.saveRefreshToken(user.getUsername(), refreshToken, jwtProperties.getRefreshExpiration());
        tokenRepository.saveAccessToken(user.getUsername(), jwtToken, jwtProperties.getAccessExpiration());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userRoles(user.getAuthorities().stream().map(Objects::toString).toList())
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException("Token is empty");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            throw new AuthException("Token is empty");
        }
        var user = userDetailsService.loadUserByUsername(userEmail);
        var previousToken = tokenRepository.getRefreshToken(userEmail);

        if (!jwtService.isTokenValid(refreshToken, user) || previousToken == null || !previousToken.equals(refreshToken)) {
            tokenRepository.deleteAllTokensByUserEmail(user.getUsername());
            throw new AuthException("Your token is not valid");
        }

        var accessToken = jwtService.generateToken(user);
        tokenRepository.saveRefreshToken(user.getUsername(), refreshToken, jwtProperties.getRefreshExpiration());
        tokenRepository.saveAccessToken(user.getUsername(), accessToken, jwtProperties.getAccessExpiration());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userRoles(user.getAuthorities().stream().map(Object::toString).toList())
                .build();

    }
}

