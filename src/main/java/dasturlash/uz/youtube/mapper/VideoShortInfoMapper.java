package dasturlash.uz.youtube.mapper;

import dasturlash.uz.youtube.entity.AttachEntity;
import dasturlash.uz.youtube.entity.ChannelEntity;

import java.time.LocalDateTime;

public interface VideoShortInfoMapper {
    String getId();
    String getTitle();
    Long getViewCount();
    LocalDateTime getPublishedDate();
    Long getDuration();
    AttachEntity getPreview();
    ChannelEntity getChannel();
}
