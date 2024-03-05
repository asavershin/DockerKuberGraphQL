package asavershin.note.security;

import asavershin.note.dto.AuthenticationRequest;
import asavershin.note.dto.AuthenticationResponse;
import asavershin.note.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    void register(UserEntity userEntity);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(HttpServletRequest request);
}
