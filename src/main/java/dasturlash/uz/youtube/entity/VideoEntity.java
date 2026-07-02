package dasturlash.uz.youtube.entity;

import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.enums.VideoTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_attach_id")
    private AttachEntity previewAttach;

    @Column(name = "preview_attach_id", insertable = false, updatable = false)
    private String previewAttachId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", nullable = false)
    private AttachEntity attach;

    @Column(name = "attach_id", insertable = false, updatable = false)
    private String attachId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private ChannelEntity channel;

    @Column(name = "channel_id", insertable = false, updatable = false)
    private String channelId;

    @PrePersist
    public void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
        this.createdDate = LocalDateTime.now();
    }
}