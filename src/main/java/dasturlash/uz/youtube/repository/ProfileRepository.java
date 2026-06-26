package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmailAndVisibleTrue(String email);

    boolean existsByEmailAndVisibleTrue(String email);

    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer id);

    @Transactional
    @Modifying
    @Query("""
            update ProfileEntity p
            set p.status = ?2
            where p.email = ?1
            """)
    int updateStatusByEmail(String email, ProfileStatus status);
}