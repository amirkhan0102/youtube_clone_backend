package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.dto.attach.AttachShortInfoDTO;
import dasturlash.uz.youtube.dto.channel.ChannelShortInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoShortInfoDTO {

    private String id;
    private String title;
    private AttachShortInfoDTO preview;
    private LocalDateTime publishedDate;
    private ChannelShortInfoDTO channel;
    private Long viewCount;
    private Long duration;
}
