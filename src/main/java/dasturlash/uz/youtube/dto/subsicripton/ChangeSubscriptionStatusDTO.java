package dasturlash.uz.youtube.dto.subsicripton;

import dasturlash.uz.youtube.enums.SubscriptionStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class ChangeSubscriptionStatusDTO {
    @NotBlank
    private String channelId;
    @NotNull
    private SubscriptionStatusEnum status;
}