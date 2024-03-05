package asavershin.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userId;

    @NotEmpty(message = "Не заполнено имя")
    @Size(min = 1, max = 20, message = "Недопустимая длина имени")
    private String userFirstname;

    @NotEmpty(message = "Не заполнена фамилия")
    @Size(min = 1, max = 20, message = "Недопустимая длина фамилии")
    private String userLastname;

    @NotEmpty(message = "Не заполнен email")
    @Email(message = "Некорректная почта")
    private String userEmail;

    @NotEmpty(message = "Не заполнен пароль")
    @Size(min = 8, message = "Длина пароля должна быть не менее 8")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String userPassword;
}
