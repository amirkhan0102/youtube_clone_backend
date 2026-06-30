package dasturlash.uz.youtube.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "comment")
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "reply_id")
    private Integer replyId;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "dislike_count")
    private Long dislikeCount;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;


}
