package dasturlash.uz.youtube.dto.videolike;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeFullDTO {
    private Long likeCount;
    private Long dislikeCount;
    private Boolean isUserLiked;
    private Boolean isUserDisliked;
}
