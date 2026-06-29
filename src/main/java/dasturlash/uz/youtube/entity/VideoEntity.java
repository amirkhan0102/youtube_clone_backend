package dasturlash.uz.youtube.entity;

import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.enums.VideoTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "video")
@Getter
@Setter
public class VideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @OneToOne
    @JoinColumn(name = "preview_attach_id", insertable = false, updatable = false)
    private AttachEntity previewPhoto;

    @Column(name = "title")
    private String title;

    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",updatable = false, insertable = false)
    private CategoryEntity category;

    @Column(name = "attach_id")
    private String attachId;
    @OneToOne
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime  createdDate;

    @CreationTimestamp
    @Column(name = "published_date")
    private LocalDateTime  publishedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VideoStatusEnum status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VideoTypeEnum type;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "shared_count")
    private Long sharedCount;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "dislike_count")
    private Long dislikeCount;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
}