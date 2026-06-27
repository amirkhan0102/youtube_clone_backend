package dasturlash.uz.youtube.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CategoryDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
}