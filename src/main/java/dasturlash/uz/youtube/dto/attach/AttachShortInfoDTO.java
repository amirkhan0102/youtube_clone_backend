package dasturlash.uz.youtube.dto.attach;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachShortInfoDTO {
    String id;
    String url;

    public AttachShortInfoDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
