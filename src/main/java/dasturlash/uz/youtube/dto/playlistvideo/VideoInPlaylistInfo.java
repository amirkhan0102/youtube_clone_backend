package dasturlash.uz.youtube.dto.playlistvideo;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoInPlaylistInfo {
    private String id;
    private AttachInfo previewAttach;
    private String title;
    private Double duration;
}