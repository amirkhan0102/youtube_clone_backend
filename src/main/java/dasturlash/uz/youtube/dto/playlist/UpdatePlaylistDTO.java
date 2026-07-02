package dasturlash.uz.youtube.dto.playlist;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
public class UpdatePlaylistDTO {
    @NotBlank
    private String name;
    private String description;
    private Integer orderNum;
}