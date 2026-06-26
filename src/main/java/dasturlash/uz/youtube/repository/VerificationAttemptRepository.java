package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.VerificationAttemptEntity;
import org.springframework.data.repository.CrudRepository;

public interface VerificationAttemptRepository extends CrudRepository<VerificationAttemptEntity, Integer> {
    VerificationAttemptEntity findByEmail(String email);
}
