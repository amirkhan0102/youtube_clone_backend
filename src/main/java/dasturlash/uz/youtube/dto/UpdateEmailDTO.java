package dasturlash.uz.youtube.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class UpdateEmailDTO {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String newEmail;
}