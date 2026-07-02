package dasturlash.uz.youtube.dto.playlistvideo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoMiniInfo {
    private String id;
    private String name;
    private Double duration;
}