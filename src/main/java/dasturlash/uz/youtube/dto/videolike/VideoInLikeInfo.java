package dasturlash.uz.youtube.dto.videolike;


import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import dasturlash.uz.youtube.dto.playlist.ChannelShortInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoInLikeInfo {

    private String id;
    private String name;
    private ChannelShortInfo channel;
    private Double duration;
    private AttachInfo previewAttach;


}
