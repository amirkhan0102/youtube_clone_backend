package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    boolean existsByName(String name);
    List<TagEntity> findAllByVisibleTrue();
}