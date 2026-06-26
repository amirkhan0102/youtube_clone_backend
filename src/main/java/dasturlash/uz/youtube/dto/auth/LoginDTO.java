package dasturlash.uz.youtube.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "Email should not be empty")
    private String email;
    @NotBlank(message = "Password should not be empty!")
    @Size(min = 5, max = 30, message = "Password size should be min 5 and max 30 length!")
    private String password;

}
