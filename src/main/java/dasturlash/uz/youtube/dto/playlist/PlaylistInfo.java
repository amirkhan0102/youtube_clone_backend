package dasturlash.uz.youtube.dto.playlist;

import dasturlash.uz.youtube.enums.PlaylistStatusEnum;
import lombok.*;

@Getter
@Setter @Builder
public class PlaylistInfo {
    private Long id;
    private String name;
    private String description;
    private PlaylistStatusEnum status;
    private Integer orderNum;
    private ChannelOwnerInfo channel;
}