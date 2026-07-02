package dasturlash.uz.youtube.dto.playlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
public class CreatePlaylistDTO {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private String channelId;
}