package dasturlash.uz.youtube.dto.subsicripton;

import dasturlash.uz.youtube.dto.video.ChannelShortPhotoInfo;
import dasturlash.uz.youtube.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SubscriptionAdminInfo {
    private Long id;
    private ChannelShortPhotoInfo channel;
    private NotificationType notificationType;
    private LocalDateTime createdDate;
}