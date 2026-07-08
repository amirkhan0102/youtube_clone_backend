package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.CommentLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Long> {

    Optional<CommentLikeEntity> findByProfileIdAndCommentId(Integer profileId, Long commentId);

    Page<CommentLikeEntity> findAllByProfileIdOrderByCreatedDateDesc(Integer profileId, Pageable pageable);


}
