package dasturlash.uz.youtube.dto.playlistvideo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeletePlaylistVideoDTO {
    @NotNull
    private Long playlistId;
    @NotBlank
    private String videoId;
}