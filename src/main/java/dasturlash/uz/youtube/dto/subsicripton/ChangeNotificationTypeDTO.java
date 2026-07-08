package dasturlash.uz.youtube.dto.subsicripton;

import dasturlash.uz.youtube.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeNotificationTypeDTO {
    @NotBlank
    private String channelId;
    @NotNull
    private NotificationType notificationType;
}