package dasturlash.uz.youtube.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "playlist_video")
@Entity
public class PlaylistVideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "playlist_id")
    private Integer playlistId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id",  insertable = false, updatable = false)
    private PlaylistEntity playlist;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime  createdDate;

    @Column(name = "order_num")
    private Integer orderNumber;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;





}
