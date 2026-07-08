package dasturlash.uz.youtube.dto.videolike;

import dasturlash.uz.youtube.enums.LikeTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VideoLikeDTO {
    @NotBlank
    private String videoId;

    @NotNull
    private LikeTypeEnum type;
}
