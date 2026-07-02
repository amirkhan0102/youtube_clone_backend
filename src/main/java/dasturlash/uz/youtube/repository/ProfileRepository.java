package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmailAndVisibleTrue(String email);

    boolean existsByEmailAndVisibleTrue(String email);


    @Modifying
    @Transactional
    @Query("""
       update ProfileEntity p
       set p.status = :status
       where p.email = :email
       """)
    void updateStatus(@Param("email") String email,
                      @Param("status") ProfileStatus status);


    @Query("""
    SELECT DISTINCT p
    FROM ProfileEntity p
    LEFT JOIN FETCH p.roleList
    WHERE p.email = :email
      AND p.visible = true
""")
    Optional<ProfileEntity> findByEmailWithRoles(@Param("email") String email);


    @Query("from ProfileEntity where id=:id and status='ACTIVE'")
    Optional<ProfileEntity> findByIdAndStatusIsActive(Integer id);
}