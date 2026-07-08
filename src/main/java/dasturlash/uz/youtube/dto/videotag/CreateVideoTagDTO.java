package dasturlash.uz.youtube.dto.videotag;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVideoTagDTO {

    @NotBlank
    private String videId;
    @NotNull
    private Integer tagId;
}
