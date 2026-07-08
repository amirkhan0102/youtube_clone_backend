package dasturlash.uz.youtube.dto.comment;

import dasturlash.uz.youtube.dto.playlist.ProfileShortInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentInfo {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private Long likeCount;
    private Long dislikeCount;
    private ProfileShortInfo profile;
}
