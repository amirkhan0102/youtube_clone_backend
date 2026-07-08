package dasturlash.uz.youtube.dto.commentlike;

import dasturlash.uz.youtube.enums.LikeTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentLikeInfo {
    private Long id;
    private Integer profileId;
    private Long commentId;
    private LocalDateTime createdDate;
    private LikeTypeEnum type;
}