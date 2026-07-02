package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.enums.VideoStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, String> {

    Page<VideoEntity> findAllByCategory_IdAndStatus(
            Integer categoryId, VideoStatusEnum status, Pageable pageable);

    Page<VideoEntity> findAllByTitleContainingIgnoreCaseAndStatus(
            String title, VideoStatusEnum status, Pageable pageable);

    Page<VideoEntity> findAllByChannelIdOrderByCreatedDateDesc(
            String channelId, Pageable pageable);

    Page<VideoEntity> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE VideoEntity v SET v.viewCount = v.viewCount + 1 WHERE v.id = :id")
    void incrementViewCount(@Param("id") String id);
}