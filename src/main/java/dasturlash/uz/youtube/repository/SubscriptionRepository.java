package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.SubscriptionEntity;
import dasturlash.uz.youtube.enums.SubscriptionStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByProfileIdAndChannelId(Integer profileId, String channelId);

    List<SubscriptionEntity> findAllByProfileIdAndStatus(
            Integer profileId, SubscriptionStatusEnum status);

    List<SubscriptionEntity> findAllByChannelIdAndStatus(
            String channelId, SubscriptionStatusEnum status);
}