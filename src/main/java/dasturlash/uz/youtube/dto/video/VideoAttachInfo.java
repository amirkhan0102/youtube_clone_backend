package dasturlash.uz.youtube.dto.video;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoAttachInfo {
    private String id;
    private String url;
    private Double duration;
}