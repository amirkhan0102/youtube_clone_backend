package dasturlash.uz.youtube.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChannelDTO {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}