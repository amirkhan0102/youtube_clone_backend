package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("""
        SELECT n FROM NotificationEntity n
        WHERE (:profileId IS NULL OR n.profileId = :profileId)
        AND (:channelId IS NULL OR n.channelId = :channelId)
        AND (:videoId IS NULL OR n.videoId = :videoId)
        AND (:dateFrom IS NULL OR n.createdDate >= :dateFrom)
        AND (:dateTo IS NULL OR n.createdDate <= :dateTo)
        ORDER BY n.createdDate DESC
        """)
    Page<NotificationEntity> filter(
            @Param("profileId") Integer profileId,
            @Param("channelId") String channelId,
            @Param("videoId") String videoId,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable);
}