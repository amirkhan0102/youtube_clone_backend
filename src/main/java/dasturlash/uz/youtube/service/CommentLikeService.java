package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.commentlike.CommentLikeDTO;
import dasturlash.uz.youtube.dto.commentlike.CommentLikeInfo;
import dasturlash.uz.youtube.entity.CommentEntity;
import dasturlash.uz.youtube.entity.CommentLikeEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.enums.LikeTypeEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.CommentLikeRepository;
import dasturlash.uz.youtube.repository.CommentRepository;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;

    // 1. Create / Toggle Like
    public String createLike(CommentLikeDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        CommentEntity comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new AppBadException("Comment not found"));

        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException("Profile not found"));

        Optional<CommentLikeEntity> existing =
                commentLikeRepository.findByProfileIdAndCommentId(profileId, dto.getCommentId());

        if (existing.isPresent()) {
            CommentLikeEntity entity = existing.get();

            if (entity.getType() == dto.getType()) {
                // Remove
                updateCommentLikeCount(comment, entity.getType(), -1);
                commentLikeRepository.delete(entity);
                return "Removed";
            } else {
                // Change Like <-> Dislike
                updateCommentLikeCount(comment, entity.getType(), -1);
                entity.setType(dto.getType());
                commentLikeRepository.save(entity);
                updateCommentLikeCount(comment, dto.getType(), 1);
                return "Changed to " + dto.getType();
            }
        }

        // Create new
        CommentLikeEntity entity = new CommentLikeEntity();
        entity.setProfile(profile);
        entity.setComment(comment);
        entity.setType(dto.getType());
        entity.setCreatedDate(LocalDateTime.now());

        commentLikeRepository.save(entity);
        updateCommentLikeCount(comment, dto.getType(), 1);

        return dto.getType().name();
    }

    // 2. Remove Like
    public Boolean removeLike(Long commentId) {
        Integer profileId = SecurityUtil.getProfileId();

        CommentLikeEntity entity = commentLikeRepository
                .findByProfileIdAndCommentId(profileId, commentId)
                .orElseThrow(() -> new AppBadException("Like not found"));

        CommentEntity comment = entity.getComment();
        updateCommentLikeCount(comment, entity.getType(), -1);
        commentLikeRepository.delete(entity);
        return true;
    }

    // 3. My Liked Comments
    public Page<CommentLikeInfo> myLikedComments(int page, int size) {
        Integer profileId = SecurityUtil.getProfileId();
        Pageable pageable = PageRequest.of(page, size);

        return commentLikeRepository
                .findAllByProfileIdOrderByCreatedDateDesc(profileId, pageable)
                .map(this::toInfo);
    }

    // 4. Admin - By UserId
    public Page<CommentLikeInfo> getLikedByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentLikeRepository
                .findAllByProfileIdOrderByCreatedDateDesc(userId, pageable)
                .map(this::toInfo);
    }

    private void updateCommentLikeCount(CommentEntity comment, LikeTypeEnum type, int delta) {
        if (type == LikeTypeEnum.LIKE) {
            comment.setLikeCount(comment.getLikeCount() + delta);
        } else {
            comment.setDislikeCount(comment.getDislikeCount() + delta);
        }
        commentRepository.save(comment);
    }

    private CommentLikeInfo toInfo(CommentLikeEntity entity) {
        return CommentLikeInfo.builder()
                .id(entity.getId())
                .profileId(entity.getProfile().getId())
                .commentId(entity.getComment().getId())
                .createdDate(entity.getCreatedDate())
                .type(entity.getType())
                .build();
    }
}