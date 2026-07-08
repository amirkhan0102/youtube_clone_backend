package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<CommentEntity, Long> {


    Page<CommentEntity> findAll(Pageable pageable);

    Page<CommentEntity> findAllByProfileId(Integer profileId, Pageable pageable);

    Page<CommentEntity> findAllByVideoIdOrderByIdDesc(String videoId, Pageable pageable);

    Page<CommentEntity> findAllByReplyIdOrderByIdDesc(Long replyId, Pageable pageable);



}
