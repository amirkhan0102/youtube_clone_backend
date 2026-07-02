package dasturlash.uz.youtube.dto.playlistvideo;

import dasturlash.uz.youtube.dto.playlist.ChannelShortInfo;
import lombok.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class PlaylistVideoInfo {
    private Long playlistId;
    private VideoInPlaylistInfo video;
    private ChannelShortInfo channel;
    private LocalDateTime createdDate;
    private Integer orderNum;
}