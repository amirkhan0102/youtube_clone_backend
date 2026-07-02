package dasturlash.uz.youtube.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDTO {
    private Integer profileId;
    private String channelId;
    private String videoId;
    private String message;
}
