package dasturlash.uz.youtube.dto.video;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeInfo {
    private Long likeCount;
    private Long dislikeCount;
    private Boolean isUserLiked;
    private Boolean isUserDisliked;
}