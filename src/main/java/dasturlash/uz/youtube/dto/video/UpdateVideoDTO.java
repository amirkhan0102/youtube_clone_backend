package dasturlash.uz.youtube.dto.video;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVideoDTO {
    @NotBlank
    private String title;
    private String description;
    private Integer categoryId;
}