package dasturlash.uz.youtube.dto.videotag;

import dasturlash.uz.youtube.dto.tag.TagResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoTagFullInfoDTO {
    private Integer id;
    private String videoId;
    private LocalDateTime createdDate;
    private TagResponseDto tagInfo;
}
