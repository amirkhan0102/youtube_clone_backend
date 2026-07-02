package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class VideoShortInfo {
    private String id;
    private String title;
    private AttachInfo previewAttach;
    private LocalDateTime publishedDate;
    private ChannelShortPhotoInfo channel;
    private Long viewCount;
    private Double duration;
}