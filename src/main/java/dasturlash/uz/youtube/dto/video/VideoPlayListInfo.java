package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VideoPlayListInfo {
    private String id;
    private String title;
    private AttachInfo previewAttach;
    private Long viewCount;
    private LocalDateTime publishedDate;
    private Double duration;
}