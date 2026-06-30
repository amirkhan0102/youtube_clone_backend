package dasturlash.uz.youtube.dto.commentLike.response;

import dasturlash.uz.youtube.enums.EmotionEnum;
import dasturlash.uz.youtube.enums.GeneralLikeStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentLikeResDto {
    private Integer id;
    private Integer profileId;
    private Integer commentId;
    private EmotionEnum emotion;
    private GeneralLikeStatusEnum status;
    private LocalDateTime createdDate;
}
