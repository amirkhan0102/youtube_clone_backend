package dasturlash.uz.youtube.dto.playlist;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PlayListShortInfo {


    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private ChannelShortInfo channel;
    private Long videoCount;
    private List<VideoMiniInfo> videoList;

}
