package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.TagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<TagEntity, Integer> {

    @Query("from TagEntity order by createdDate desc")
    List<TagEntity> getAllByOrderByCreatedDateDesc();
}
