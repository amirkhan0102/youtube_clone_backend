package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.enums.VideoStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeVideoStatusDTO {
    @NotNull
    private VideoStatusEnum status;
}