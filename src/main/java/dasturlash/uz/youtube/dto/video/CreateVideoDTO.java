package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.enums.VideoTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVideoDTO {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String attachId;

    private String previewAttachId;

    @NotNull
    private Integer categoryId;

    @NotBlank
    private String channelId;

    @NotNull
    private VideoTypeEnum type;
}