package dasturlash.uz.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "attach")
@Getter
@Setter
public class AttachEntity {

    @Id
    private String id;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "size")
    private Long size;

    @Column(name = "type")
    private String type;

    @Column(name = "path")
    private String path;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "visible")
    private Boolean visible = true;


//JPA (Java Persistence API) tizimidagi maxsus metod bo'lib,
// ob'ekt ma'lumotlar bazasiga saqlanishidan oldin unga avtomatik ravishda noyob
// (unikal) ID biriktirish uchun ishlatiladi.
    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
    }
}