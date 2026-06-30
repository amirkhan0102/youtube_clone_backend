package dasturlash.uz.youtube.dto.commentLike.response;

import dasturlash.uz.youtube.enums.EmotionEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentLikeInfoResDto {
    private Integer id;
    private Integer profileId;
    private Integer commentId;
    private EmotionEnum emotion;
    private LocalDateTime createdDate;
}
