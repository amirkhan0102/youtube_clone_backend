package dasturlash.uz.youtube.dto.tag;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TagDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
}
