package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChannelShortPhotoInfo {
    private String id;
    private String name;
    private AttachInfo photo;
}