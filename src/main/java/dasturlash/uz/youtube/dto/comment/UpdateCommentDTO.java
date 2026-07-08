package dasturlash.uz.youtube.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class UpdateCommentDTO {
    @NotBlank
    private String content;
}