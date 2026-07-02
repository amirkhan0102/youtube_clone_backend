package dasturlash.uz.youtube.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationFilterDTO {

    private Integer profileId;
    private String channelId;
    private String videoId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean isProfileReceived;
}
