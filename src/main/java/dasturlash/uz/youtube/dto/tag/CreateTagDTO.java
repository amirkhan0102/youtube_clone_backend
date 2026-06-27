package dasturlash.uz.youtube.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CreateTagDTO {
    @NotBlank(message = "Name is required")
    private String name;
}