package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.enums.VideoTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoCreateDTO {

    @NotBlank(message = "Title should not be empty")
    private String title;

    private String description;

    @NotBlank(message = "Preview ID should not be empty")
    private String previewAttachId;

    @NotBlank(message = "Video should not be empty")
    private String attachId;

    @NotNull(message = "Category should be chosen")
    private Integer categoryId;

    @NotNull(message = "Channel ID should not be empty")
    private String channelId;

    @NotNull(message = "Video should not be empty")
    private VideoTypeEnum type;

    @NotNull(message = "Status should not be empty")
    private VideoStatusEnum status;

}
