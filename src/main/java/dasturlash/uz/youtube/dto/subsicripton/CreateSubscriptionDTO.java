package dasturlash.uz.youtube.dto.subsicripton;

import dasturlash.uz.youtube.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class CreateSubscriptionDTO {
    @NotBlank
    private String channelId;
    @NotNull
    private NotificationType notificationType;
}
