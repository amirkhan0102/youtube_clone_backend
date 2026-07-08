package dasturlash.uz.youtube.dto.videolike;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VideoLikeInfo {

    private Long id;

    private VideoInLikeInfo video;




}
