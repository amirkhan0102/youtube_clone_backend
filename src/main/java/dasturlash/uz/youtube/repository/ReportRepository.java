package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    Page<ReportEntity> findAll(Pageable pageable);

    Page<ReportEntity> findAllByProfileId(Integer profileId, Pageable pageable);

}