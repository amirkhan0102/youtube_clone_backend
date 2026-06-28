package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.EmailHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Long> {
    Page<EmailHistoryEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<EmailHistoryEntity> findByToEmailOrderByCreatedDateDesc(String email, Pageable pageable);
    Page<EmailHistoryEntity> findByToEmailAndCreatedDateBetweenOrderByCreatedDateDesc(
            String email, LocalDateTime from, LocalDateTime to, Pageable pageable);
}