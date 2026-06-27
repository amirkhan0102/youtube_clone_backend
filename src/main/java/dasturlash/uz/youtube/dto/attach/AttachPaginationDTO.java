package dasturlash.uz.youtube.dto.attach;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttachPaginationDTO {
    private String id;
    private String originName;
    private Long size;
    private String url;

}
