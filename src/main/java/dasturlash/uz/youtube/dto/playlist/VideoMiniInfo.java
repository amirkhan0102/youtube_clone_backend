package dasturlash.uz.youtube.dto.playlist;

import lombok.*;

@Getter @Setter
@Builder
public class VideoMiniInfo {
    private String id;
    private String name;
    private Double duration;
}