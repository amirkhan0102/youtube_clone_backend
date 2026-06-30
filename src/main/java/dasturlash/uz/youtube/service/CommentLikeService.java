package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.commentLike.request.CommentLikeReqDto;
import dasturlash.uz.youtube.dto.commentLike.response.CommentLikeInfoResDto;
import dasturlash.uz.youtube.dto.commentLike.response.CommentLikeResDto;
import dasturlash.uz.youtube.entity.CommentLikeEntity;
import dasturlash.uz.youtube.enums.GeneralLikeStatusEnum;
import dasturlash.uz.youtube.exception.ItemNotFoundException;
import dasturlash.uz.youtube.repository.CommentLikeRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentLikeService {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public CommentLikeResDto create(CommentLikeReqDto dto) {
        Integer profileId = SecurityUtil.getProfileId();
        Optional<CommentLikeEntity> optional = commentLikeRepository.findByCommentIdAndProfileId(profileId, dto.getCommentId());

        if (optional.isPresent()) {
            CommentLikeEntity entity = optional.get();
            if (entity.getEmotionEnum().equals(dto.getEmotion())) {
                commentLikeRepository.delete(entity);

                return toDto(entity, GeneralLikeStatusEnum.DELETED);
            } else {
                entity.setEmotionEnum(dto.getEmotion());
                commentLikeRepository.save(entity);

                return  toDto(entity, GeneralLikeStatusEnum.UPDATED);
            }
        }

        CommentLikeEntity entity = new CommentLikeEntity();
        entity.setCommentId(dto.getCommentId());
        entity.setProfileId(profileId);
        entity.setEmotionEnum(dto.getEmotion());
        entity.setCreatedDate(LocalDateTime.now());

        commentLikeRepository.save(entity);

        return toDto(entity, GeneralLikeStatusEnum.CREATED);
    }

    public CommentLikeResDto toDto(CommentLikeEntity entity, GeneralLikeStatusEnum status) {
        CommentLikeResDto response = new CommentLikeResDto();
        response.setId(entity.getId());
        response.setCommentId(entity.getCommentId());
        response.setProfileId(entity.getProfileId());
        response.setEmotion(entity.getEmotionEnum());
        response.setCreatedDate(entity.getCreatedDate());
        response.setStatus(status);
        return response;
    }

    public Boolean remove(CommentLikeReqDto dto) {
        CommentLikeEntity entity = commentLikeRepository.findByCommentIdAndProfileId(dto.getCommentId(), SecurityUtil.getProfileId())
                .orElseThrow(() -> new ItemNotFoundException("Comment Not Found"));
        commentLikeRepository.delete(entity);
        return Boolean.TRUE;
    }

    public List<CommentLikeInfoResDto> userLikedList() {
        List<CommentLikeEntity> entities = commentLikeRepository.findByProfileId(SecurityUtil.getProfileId())
                .orElseThrow(() -> new ItemNotFoundException("Comment Not Found"));

        List<CommentLikeInfoResDto> response = new LinkedList<>();

        entities.forEach(entity -> {
            response.add(toInfoDto(entity));
        });

        return response;
    }

    public CommentLikeInfoResDto toInfoDto(CommentLikeEntity entity) {
        CommentLikeInfoResDto response = new CommentLikeInfoResDto();
        response.setId(entity.getId());
        response.setCommentId(entity.getCommentId());
        response.setProfileId(entity.getProfileId());
        response.setEmotion(entity.getEmotionEnum());
        response.setCreatedDate(entity.getCreatedDate());
        return response;
    }

    public List<CommentLikeInfoResDto> userLikedListByPrId(Integer profileId) {
        List<CommentLikeEntity> entities = commentLikeRepository.findByProfileId(profileId)
                .orElseThrow(() -> new ItemNotFoundException("Comment Not Found"));

        List<CommentLikeInfoResDto> response = new LinkedList<>();

        entities.forEach(entity -> {
            response.add(toInfoDto(entity));
        });

        return response;
    }

}

