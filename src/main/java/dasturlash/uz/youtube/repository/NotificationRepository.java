package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.dto.notification.NotificationResponseDTO;
import dasturlash.uz.youtube.entity.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NotificationRepository extends CrudRepository<NotificationEntity, Integer> {
    Optional<NotificationEntity> findNotificationById(Integer id);
}
