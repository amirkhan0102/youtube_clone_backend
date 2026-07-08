package dasturlash.uz.youtube.dto.videotag;


import dasturlash.uz.youtube.dto.video.TagShortInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VideoTagDTO {
    private Long id;
    private String videoId;
    private TagShortInfo tag;
    private LocalDateTime createdDate;
}
