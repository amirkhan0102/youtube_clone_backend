package dasturlash.uz.youtube.entity;


import dasturlash.uz.youtube.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Column(name = "photo_id")
    private String photoId; //

    @Column(name = "visible", nullable = false)
    private Boolean visible = true;


    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;


    @OneToMany(mappedBy = "profile")
    private List<ProfileRoleEntity> roleList;

}
