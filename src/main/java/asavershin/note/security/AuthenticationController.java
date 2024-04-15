package asavershin.note.security;

import asavershin.note.dto.AuthenticationRequest;
import asavershin.note.dto.AuthenticationResponse;
import asavershin.note.dto.UserDTO;
import asavershin.note.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "auth", description = "Аутентификация")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Operation(description = "Регистрация")
    public void register(
            @RequestBody @Valid UserDTO request
    ) {
        service.register(userMapper.toEntity(request));
    }
    @PostMapping("/login")
    @Operation(description = "Аутентификация")
    public AuthenticationResponse authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return service.authenticate(request);
    }

    @PostMapping("/refresh-token")
    @Operation(description = "Использовать рефреш токен")
    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        return service.refreshToken(request);
    }
}
