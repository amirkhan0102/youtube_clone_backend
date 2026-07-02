package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.ChannelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity,String> {
    Page<ChannelEntity> findAll(Pageable pageable);
    List<ChannelEntity> findAllByProfileId(Integer profileId);

    @Query("from ChannelEntity where id=:channelId and profileId=:prfile_id")
    ChannelEntity findByIdAndProfileId(@Param("channelId") String channelId, @Param("profile_id") Integer profile_id);

    @Query("from ChannelEntity where id=:entityId and status='ACTIVE' and visible=true")
    Optional<ChannelEntity> findByIdAndVisibleIsTrueAndStatusActive(@Param("entityId") String entityId);

}
