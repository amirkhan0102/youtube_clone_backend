package dasturlash.uz.youtube.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponseDTO {
    private Integer id;
    private NotificationProfileDTO notificationProfile;
    private NotificationChannelDTO notificationChannel;
    private NotificationVideoDTO notificationVideo;
    private String message;
    private LocalDateTime createdDate;

    public NotificationResponseDTO(Integer id, NotificationProfileDTO notificationProfile,
                                   NotificationChannelDTO notificationChannel,
                                   NotificationVideoDTO notificationVideo,
                                   String message, LocalDateTime createdDate) {
        this.id = id;
        this.notificationProfile = notificationProfile;
        this.notificationChannel = notificationChannel;
        this.notificationVideo = notificationVideo;
        this.message = message;
        this.createdDate = createdDate;
    }
}
