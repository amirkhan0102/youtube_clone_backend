package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class VideoFullInfo {
    private String id;
    private String title;
    private String description;
    private AttachInfo previewAttach;
    private VideoAttachInfo attach;
    private CategoryShortInfo category;
    private List<TagShortInfo> tagList;
    private LocalDateTime publishedDate;
    private ChannelShortPhotoInfo channel;
    private Long viewCount;
    private Long sharedCount;
    private LikeInfo like;
    private Double duration;
}