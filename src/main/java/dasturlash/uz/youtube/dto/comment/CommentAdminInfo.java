package dasturlash.uz.youtube.dto.comment;

import dasturlash.uz.youtube.dto.playlist.ProfileShortInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class CommentAdminInfo {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private Long likeCount;
    private Long dislikeCount;
    private VideoShortForComment video;
    private ProfileShortInfo profile;
}