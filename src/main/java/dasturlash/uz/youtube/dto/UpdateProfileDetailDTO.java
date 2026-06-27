package dasturlash.uz.youtube.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileDetailDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50)
    private String surname;
}