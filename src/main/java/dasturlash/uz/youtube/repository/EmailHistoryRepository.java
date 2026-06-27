package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Long> {
}