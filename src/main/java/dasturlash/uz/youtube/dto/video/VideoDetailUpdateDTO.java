package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.enums.VideoStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoDetailUpdateDTO {

    @NotBlank(message = "Title should not be empty")
    private String title;

    private String description;

    @NotNull(message = "Category should not be empty")
    private Integer categoryId;

    @NotBlank(message = "Preview ID should not be empty")
    private String previewAttachId;

    @NotNull(message = "Status should not be empty")
    private VideoStatusEnum status;
}
