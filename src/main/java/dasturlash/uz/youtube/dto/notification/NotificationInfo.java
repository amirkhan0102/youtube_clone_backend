package dasturlash.uz.youtube.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationInfo {
    private Long id;
    private ProfileMiniInfo profile;
    private ChannelMiniInfo channel;
    private VideoMiniForNotification video;
    private String message;
    private LocalDateTime createdDate;
}