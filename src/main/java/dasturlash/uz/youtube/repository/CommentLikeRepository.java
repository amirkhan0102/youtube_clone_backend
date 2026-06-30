package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.CommentLikeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, Integer> {

    Optional<CommentLikeEntity> findByCommentIdAndProfileId(Integer commentId, Integer profileId);

    Optional<List<CommentLikeEntity>> findByProfileId(Integer profileId);
}
