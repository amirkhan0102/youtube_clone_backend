package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.PlaylistEntity;
import dasturlash.uz.youtube.enums.PlaylistStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayListRepository extends JpaRepository<PlaylistEntity, Long> {

    Page<PlaylistEntity> findAll(Pageable pageable);

    Page<PlaylistEntity> findAllByChannel_Profile_IdOrderByOrderNumDesc(
            Integer profileId, Pageable pageable);

    List<PlaylistEntity> findAllByChannel_Profile_IdOrderByOrderNumDesc(Integer profileId);

    List<PlaylistEntity> findAllByChannelIdAndStatusOrderByOrderNumDesc(
            String channelId, PlaylistStatusEnum status);

}
