package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.dto.playlist.VideoMiniInfo;
import dasturlash.uz.youtube.entity.PlaylistVideoEntity;
import dasturlash.uz.youtube.enums.VideoStatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideoEntity, Long> {

    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(Long playlistId, String videoId);

    List<PlaylistVideoEntity> findAllByPlaylistIdAndVideo_StatusOrderByOrderNumDesc(
            Long playlistId, VideoStatusEnum status);

    @Query("SELECT COUNT(pv) FROM PlaylistVideoEntity pv WHERE pv.playlist.id = :playlistId")
    Long countByPlaylistId(@Param("playlistId") Long playlistId);

    @Query("""
        SELECT COALESCE(SUM(pv.video.viewCount), 0)
        FROM PlaylistVideoEntity pv
        WHERE pv.playlist.id = :playlistId
        """)
    Long sumViewCountByPlaylistId(@Param("playlistId") Long playlistId);

    @Query("""
        SELECT MAX(pv.createdDate)
        FROM PlaylistVideoEntity pv
        WHERE pv.playlist.id = :playlistId
        """)
    LocalDateTime findLastUpdateDateByPlaylistId(@Param("playlistId") Long playlistId);

    @Query("""
        SELECT new dasturlash.uz.youtube.dto.playlist.VideoMiniInfo(
            pv.video.id, pv.video.title, pv.video.attach.duration)
        FROM PlaylistVideoEntity pv
        WHERE pv.playlist.id = :playlistId
        ORDER BY pv.orderNum DESC
        """)
    List<VideoMiniInfo> findVideosByPlaylistId(
            @Param("playlistId") Long playlistId, Pageable pageable);
}