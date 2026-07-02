package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.VideoTagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoTagRepository extends JpaRepository<VideoTagEntity, Long> {
    List<VideoTagEntity> findAllByVideoId(String videoId);

    @Query("SELECT vt FROM VideoTagEntity vt JOIN FETCH vt.tag WHERE vt.video.id IN :videoIds")
    List<VideoTagEntity> findAllByVideoIds(@Param("videoIds") List<String> videoIds);

    Page<VideoTagEntity> findAllByTagId(Integer tagId, Pageable pageable);
}