package dasturlash.uz.youtube.mapper;

import dasturlash.uz.youtube.entity.AttachEntity;
import dasturlash.uz.youtube.entity.CategoryEntity;
import dasturlash.uz.youtube.entity.ChannelEntity;

public interface VideoFullInfoMapper {
    String getId();
    String getTitle();
    String getDescription();
    Long getViewCount();
    Long getSharedCount();
    Long getLikeCount();
    Long getDislikeCount();

    AttachEntity getPreview();

    AttachEntity getVideo();

    CategoryEntity getCategory();

    ChannelEntity getChannel();
}
