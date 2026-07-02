package dasturlash.uz.youtube.dto.playlist;

import lombok.*;

@Getter @Setter
@Builder
public class ChannelOwnerInfo {
    private String id;
    private String name;
    private AttachInfo photo;
    private ProfileShortInfo profile;
}