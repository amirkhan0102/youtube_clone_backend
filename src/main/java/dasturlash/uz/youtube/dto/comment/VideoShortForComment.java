package dasturlash.uz.youtube.dto.comment;

import lombok.*;

@Getter @Setter
@Builder
public class VideoShortForComment {
    private String id;
    private String title;
    private String previewAttachId;
    private Double duration;
}