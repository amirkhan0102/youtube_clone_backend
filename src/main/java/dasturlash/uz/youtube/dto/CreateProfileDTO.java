package dasturlash.uz.youtube.dto;

import dasturlash.uz.youtube.enums.ProfileRoleEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProfileDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50)
    private String surname;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20)
    private String password;

    private ProfileRoleEnum role; // ADMIN yoki MODERATOR
}