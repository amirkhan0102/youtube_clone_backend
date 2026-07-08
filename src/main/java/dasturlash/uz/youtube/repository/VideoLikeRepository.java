package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.VideoLikeEntity;
import dasturlash.uz.youtube.enums.LikeTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoLikeRepository extends JpaRepository<VideoLikeEntity, Long> {
    Optional<VideoLikeEntity> findByProfileIdAndVideoId(Integer profileId, String videoId);


    Page<VideoLikeEntity> findAllByProfileIdAndTypeOrderByCreatedDateDesc(
            Integer profileId, LikeTypeEnum type, Pageable pageable);


}