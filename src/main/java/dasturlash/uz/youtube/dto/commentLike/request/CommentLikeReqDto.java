package dasturlash.uz.youtube.dto.commentLike.request;

import dasturlash.uz.youtube.enums.EmotionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLikeReqDto {
    private Integer commentId;
    private EmotionEnum emotion;
}
