package dasturlash.uz.youtube.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoMiniForNotification {
    private String id;
    private String title;
}