package dasturlash.uz.youtube.dto.comment.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCustomProfileResDto {
    private Integer id;
    private String name;
    private String surname;
    private CommentCustomPhotoResDto photo;
}
