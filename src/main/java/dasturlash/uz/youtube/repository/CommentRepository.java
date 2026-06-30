package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {

    CommentEntity getById(Integer id);

    @Query("FROM CommentEntity where id =:id AND visible IS TRUE ")
    Optional<CommentEntity> findByIdAndVisibleTrue(Integer id);

    @Query("FROM CommentEntity WHERE visible IS TRUE ")
    Page<CommentEntity> findAllVisibleTrue(Pageable pageable);

    @Query("FROM CommentEntity WHERE profileId =:profileId AND visible IS TRUE ")
    Optional<List<CommentEntity>> findByProfileIdAndVisibleTrue(Integer profileId);

    @Query("SELECT c1 FROM CommentEntity AS c1 " +
            "WHERE c1.id = (" +
            "SELECT c2.replyId FROM CommentEntity AS c2 " +
            "WHERE c2.id=:commentId " +
            "AND c2.visible IS TRUE) ")
    Optional<List<CommentEntity>> getRepliesById(Integer commentId);
}
