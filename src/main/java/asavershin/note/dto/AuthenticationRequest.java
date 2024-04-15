package asavershin.note.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotEmpty(message = "Не заполнен email")
    @Email(message = "Некорректная почта")
    private String email;

    @NotEmpty(message = "Не заполнен пароль")
    @Size(min = 8, message = "Длина пароля должна быть не менее 8")
    String password;
}
