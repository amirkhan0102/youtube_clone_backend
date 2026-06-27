package dasturlash.uz.youtube.dto.channel;

import dasturlash.uz.youtube.enums.ChannelStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChannelDTO {
    private String id;
    private String name;
    private String description;
    private String photoUrl;
    private String bannerUrl;
    private ChannelStatusEnum status;
}