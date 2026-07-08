package dasturlash.uz.youtube.dto.subsicripton;

import dasturlash.uz.youtube.dto.video.ChannelShortPhotoInfo;
import dasturlash.uz.youtube.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubscriptionInfo {
    private Long id;
    private ChannelShortPhotoInfo channel;
    private NotificationType notificationType;
}