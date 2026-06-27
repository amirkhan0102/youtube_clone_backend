package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.ChannelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity,String> {
    Page<ChannelEntity> findAll(Pageable pageable);
    List<ChannelEntity> findAllByProfileId(Integer profileId);

}
