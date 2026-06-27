package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.AttachEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, String> {
    Page<AttachEntity> findAllByVisibleTrue(Pageable pageable);
}
