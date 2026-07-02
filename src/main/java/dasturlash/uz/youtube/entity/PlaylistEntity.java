package dasturlash.uz.youtube.entity;

import dasturlash.uz.youtube.enums.PlaylistStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "playlist")
@Getter
@Setter
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlaylistStatusEnum status;

    @Column(name = "order_num")
    private Integer orderNum;

    // Channel bilan join
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private ChannelEntity channel;

    @Column(name = "channel_id", insertable = false, updatable = false)
    private String channelId;


}