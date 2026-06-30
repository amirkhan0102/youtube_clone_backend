package dasturlash.uz.youtube.dto.comment.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentReqDto {
    private String videoId;
    private Integer replyId;
    private String content;
}
