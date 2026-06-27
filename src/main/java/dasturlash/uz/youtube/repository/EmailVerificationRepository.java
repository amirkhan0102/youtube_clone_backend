package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.EmailVerificationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {

    boolean existsByEmailAndTokenAndVerifiedFalse(String email, String token);

    Optional<EmailVerificationEntity> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE EmailVerificationEntity e SET e.verified = true WHERE e.email = :email AND e.verified = false")
    void invalidateAllTokens(@Param("email") String email);
}