package dasturlash.uz.youtube.entity;

import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.enums.VideoTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "video")
@Getter
@Setter
public class VideoEntity {

    @Id
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VideoStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VideoTypeEnum type;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "shared_count")
    private Long sharedCount = 0L;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "dislike_count")
    private Long dislikeCount = 0L;

    // Preview Attach
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_attach_id")
    private AttachEntity previewAttach;

    // Video Attach
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", nullable = false)
    private AttachEntity attach;

    // Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    // Channel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private ChannelEntity channel;
}