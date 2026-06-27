package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Integer> {

    @Query("SELECT COUNT(eh) FROM EmailHistoryEntity eh " +
            " WHERE eh.toEmail = ?1 AND eh.createdDate >= ?2 ")
    int countByToEmailAfter(String toAccount, LocalDateTime from);

    EmailHistoryEntity findTopByToEmailOrderByCreatedDateDesc(String email);

}