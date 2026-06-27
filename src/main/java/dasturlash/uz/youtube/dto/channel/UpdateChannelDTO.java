package dasturlash.uz.youtube.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class UpdateChannelDTO {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}