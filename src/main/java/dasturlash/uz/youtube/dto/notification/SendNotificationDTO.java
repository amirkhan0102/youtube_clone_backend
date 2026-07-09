package dasturlash.uz.youtube.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendNotificationDTO {
    @NotNull
    private Integer profileId;
    @NotBlank
    private String channelId;
    @NotBlank
    private String videoId;
    @NotBlank
    private String message;
}