package dasturlash.uz.youtube.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CreateCommentDTO {
    @NotBlank
    private String videoId;
    @NotBlank
    private String content;
    private Long replyId;
}