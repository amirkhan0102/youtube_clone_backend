package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.comment.request.CommentReqDto;
import dasturlash.uz.youtube.dto.comment.response.*;
import dasturlash.uz.youtube.entity.CommentEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.exception.ItemNotFoundException;
import dasturlash.uz.youtube.repository.CommentRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentResDto create(CommentReqDto dto) {
        CommentEntity entity = new CommentEntity();
        entity.setProfileId(SecurityUtil.getProfileId());
        entity.setVideoId(dto.getVideoId());
        entity.setContent(dto.getContent());
        if (dto.getReplyId() != null) {
            entity.setReplyId(dto.getReplyId());
        }
        entity.setLikeCount(0L);
        entity.setDislikeCount(0L);
        entity.setCreatedDate(LocalDateTime.now());

        commentRepository.save(entity);

        return toDtoFromEntity(entity);
    }

    public CommentResDto toDtoFromEntity(CommentEntity entity) {
        CommentResDto dto = new CommentResDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setLikeCount(entity.getLikeCount());
        dto.setDislikeCount(entity.getDislikeCount());
        dto.setCreatedDate(entity.getCreatedDate());

        // SET VIDEO DTO
        CommentCustomVideoInfoResDto video = new CommentCustomVideoInfoResDto();
        video.setId(entity.getVideoId());
        video.setPreviewAttachId(entity.getVideo().getPreviewAttachId());
        video.setTitle(entity.getVideo().getTitle());
        video.setDuration(entity.getVideo().getAttach().getDuration());

        // SET VIDEO FOR COMMENT RESPONSE
        dto.setVideo(video);

        return dto;
    }

    public CommentResDto update(Integer id, CommentReqDto dto) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Comment not found");
        }

        CommentEntity entity = optional.get();
        if (!SecurityUtil.getProfileId().equals(entity.getProfileId())) {
            throw new AppBadException("Not Allow To Edit Comment");
        }
        entity.setContent(dto.getContent());
        if (dto.getReplyId() != null) {
            entity.setReplyId(dto.getReplyId());
        }
        entity.setVideoId(dto.getVideoId());

        commentRepository.save(entity);
        return toDtoFromEntity(entity);
    }

    public CommentResDto delete(Integer id) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Comment not found");
        }

        CommentEntity entity = optional.get();
        if (!SecurityUtil.getCurrentProfileRoles().getFirst().equals("ROLE_ADMIN")) {
            if (!SecurityUtil.getProfileId().equals(entity.getProfileId())) {
                throw new AppBadException("Not Allowed To Delete This Comment");
            }
        }

        // DELETE IT BY SETTING VISIBLE FALSE
        entity.setVisible(Boolean.FALSE);
        return toDtoFromEntity(entity);
    }

    public PageImpl<CommentResDto> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        Page<CommentEntity> result = commentRepository.findAllVisibleTrue(pageable);

        return toPagination(pageable, result);
    }

    public PageImpl<CommentResDto> toPagination(Pageable pageable, Page<CommentEntity> result) {
        List<CommentResDto> dtoList = new LinkedList<>();

        List<CommentEntity> entities = result.getContent();
        entities.forEach(entity -> {
            dtoList.add(toDtoFromEntity(entity));
        });

        return  new PageImpl<>(dtoList, pageable, entities.size());
    }

    public List<CommentResDto> listByProfileId(Integer profileId) {
        List<CommentResDto> dtoList = new LinkedList<>();
        entitiesByProfileId(profileId).forEach(entity -> {
            dtoList.add(toDtoFromEntity(entity));
        });

        return dtoList;
    }


    public List<CommentResDto> listByCurrentProfile() {
        List<CommentResDto> dtoList = new LinkedList<>();
        entitiesByProfileId(SecurityUtil.getProfileId()).forEach(entity -> {
            dtoList.add(toDtoFromEntity(entity));
        });

        return dtoList;
    }

    public List<CommentEntity> entitiesByProfileId(Integer profileId) {
        Optional<List<CommentEntity>> optional = commentRepository.findByProfileIdAndVisibleTrue(profileId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Comment not found");
        }
        return  optional.get();
    }


    public List<CommentInfoResDto> replyListById(Integer commentId) {
        Optional<List<CommentEntity>> optional = commentRepository.getRepliesById(commentId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Comment not found");
        }
        List<CommentInfoResDto> response = new LinkedList<>();

        List<CommentEntity> entities = optional.get();
        entities.forEach(entity -> {
            response.add(toCommentInfoDto(entity));
        });

        return response;
    }

    public CommentInfoResDto toCommentInfoDto(CommentEntity entity) {
        CommentInfoResDto response = new CommentInfoResDto();
        response.setId(entity.getId());
        response.setCreatedDate(entity.getCreatedDate());
        response.setContent(entity.getContent());
        response.setLikeCount(entity.getLikeCount());
        response.setDislikeCount(entity.getDislikeCount());

        // SET PROFILE FOR RESPONSE MAIN DTO
        response.setProfile(toProfileInfoDto(entity));

        return response;
    }

    public CommentCustomProfileResDto toProfileInfoDto(CommentEntity entity) {
        CommentCustomProfileResDto profile = new CommentCustomProfileResDto();
        profile.setId(entity.getProfileId());
        profile.setName(entity.getProfile().getName());
        profile.setSurname(entity.getProfile().getSurname());

        // SET PHOTO DTO
        CommentCustomPhotoResDto photo = new CommentCustomPhotoResDto();
        photo.setId(entity.getProfile().getPhotoId());
        photo.setUrl(entity.getProfile().getPhoto().getPath());


        // SET PHOTO FOR PROFILE DTO
        profile.setPhoto(photo);

        return profile;
    }
}
