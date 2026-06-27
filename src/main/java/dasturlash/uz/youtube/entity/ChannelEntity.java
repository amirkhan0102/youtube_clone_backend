package dasturlash.uz.youtube.entity;

import dasturlash.uz.youtube.enums.ChannelStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "channel")
@Getter
@Setter
public class ChannelEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChannelStatusEnum status;

    // Profile join
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;

    @Column(name = "profile_id", insertable = false, updatable = false)
    private Integer profileId;

    // Photo join
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private AttachEntity photo;

    @Column(name = "photo_id", insertable = false, updatable = false)
    private String photoId; // ← qo'shildi

    // Banner join
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private AttachEntity bannerAttach;

    @Column(name = "banner_id", insertable = false, updatable = false)
    private String banner; // ← qo'shildi
}