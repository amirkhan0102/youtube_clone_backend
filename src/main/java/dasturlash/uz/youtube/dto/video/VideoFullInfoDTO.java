package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.dto.attach.AttachShortInfoDTO;
import dasturlash.uz.youtube.dto.category.CategoryShortDTO;
import dasturlash.uz.youtube.dto.channel.ChannelShortInfoDTO;
import dasturlash.uz.youtube.dto.tag.TagResponseDto;
import dasturlash.uz.youtube.dto.videolike.LikeFullDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoFullInfoDTO {

    private String id;
    private String title;
    private String description;
    private Long viewCount;
    private Long sharedCount;
    private Double duration;

    private AttachShortInfoDTO preview;

    private AttachShortInfoDTO video;

    private CategoryShortDTO category;

    private List<TagResponseDto> tagList;

    private ChannelShortInfoDTO channel;

    private LikeFullDTO likeInfo;
}
