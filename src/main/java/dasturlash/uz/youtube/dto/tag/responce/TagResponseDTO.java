package dasturlash.uz.youtube.dto.tag.responce;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TagResponseDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
}
