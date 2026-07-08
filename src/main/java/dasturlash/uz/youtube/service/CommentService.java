package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.comment.*;
import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import dasturlash.uz.youtube.dto.playlist.ProfileShortInfo;
import dasturlash.uz.youtube.entity.CommentEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.CommentRepository;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.VideoRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final ProfileRepository profileRepository;   // ← Added

    @Value("${server.url}")
    private String serverUrl;

    // 1. Create Comment
    public CommentInfo create(CreateCommentDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        VideoEntity video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Video not found"));

        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException("Profile not found"));

        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setVideo(video);
        entity.setProfile(profile);
        entity.setLikeCount(0L);
        entity.setDislikeCount(0L);
        entity.setCreatedDate(LocalDateTime.now());

        if (dto.getReplyId() != null) {
            CommentEntity reply = commentRepository.findById(dto.getReplyId())
                    .orElseThrow(() -> new AppBadException("Comment not found"));
            entity.setReply(reply);
        }

        commentRepository.save(entity);
        return toInfo(entity);
    }

    // 2. Update Comment
    public CommentInfo update(Long id, UpdateCommentDTO dto) {
        CommentEntity entity = getOwnedComment(id);
        entity.setContent(dto.getContent());
        commentRepository.save(entity);
        return toInfo(entity);
    }

    // 3. Delete Comment
    public Boolean delete(Long id) {
        CommentEntity entity = getCommentById(id);
        Integer profileId = SecurityUtil.getProfileId();

        boolean isOwner = entity.getProfile().getId().equals(profileId);  // ← Fixed
        boolean isAdmin = isAdmin();

        if (!isOwner && !isAdmin) {
            throw new AppBadException("Access denied");
        }

        commentRepository.delete(entity);
        return true;
    }

    // 4. Pagination (ADMIN)
    public Page<CommentAdminInfo> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return commentRepository.findAll(pageable).map(this::toAdminInfo);
    }

    // 5. List by profileId (ADMIN)
    public Page<CommentAdminInfo> listByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return commentRepository.findAllByProfileId(profileId, pageable)
                .map(this::toAdminInfo);
    }

    // 6. My Comments
    public Page<CommentAdminInfo> myComments(int page, int size) {
        Integer profileId = SecurityUtil.getProfileId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return commentRepository.findAllByProfileId(profileId, pageable)
                .map(this::toAdminInfo);
    }

    // 7. List by Video
    public Page<CommentInfo> listByVideoId(String videoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return commentRepository.findAllByVideoIdOrderByIdDesc(videoId, pageable)
                .map(this::toInfo);
    }

    // 8. Get Replies
    public Page<CommentInfo> getReplies(Long commentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return commentRepository.findAllByReplyIdOrderByIdDesc(commentId, pageable)
                .map(this::toInfo);
    }

    private CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Comment not found"));
    }

    private CommentEntity getOwnedComment(Long id) {
        CommentEntity entity = getCommentById(id);
        Integer currentProfileId = SecurityUtil.getProfileId();

        if (!entity.getProfile().getId().equals(currentProfileId)) {   // ← Fixed
            throw new AppBadException("Access denied");
        }
        return entity;
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private CommentInfo toInfo(CommentEntity entity) {
        ProfileEntity profile = entity.getProfile();
        return CommentInfo.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createdDate(entity.getCreatedDate())
                .likeCount(entity.getLikeCount())
                .dislikeCount(entity.getDislikeCount())
                .profile(ProfileShortInfo.builder()
                        .id(profile.getId())
                        .name(profile.getName())
                        .surname(profile.getSurname())
                        .photo(profile.getPhotoId() != null
                                ? AttachInfo.builder()
                                .id(profile.getPhotoId())
                                .url(serverUrl + "/attach/open/" + profile.getPhotoId())
                                .build()
                                : null)
                        .build())
                .build();
    }

    private CommentAdminInfo toAdminInfo(CommentEntity entity) {
        VideoEntity video = entity.getVideo();
        ProfileEntity profile = entity.getProfile();

        return CommentAdminInfo.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createdDate(entity.getCreatedDate())
                .likeCount(entity.getLikeCount())
                .dislikeCount(entity.getDislikeCount())
                .profile(ProfileShortInfo.builder()
                        .id(profile.getId())
                        .name(profile.getName())
                        .surname(profile.getSurname())
                        .photo(profile.getPhotoId() != null
                                ? AttachInfo.builder()
                                .id(profile.getPhotoId())
                                .url(serverUrl + "/attach/open/" + profile.getPhotoId())
                                .build()
                                : null)
                        .build())
                .video(VideoShortForComment.builder()
                        .id(video.getId())
                        .title(video.getTitle())
                        .previewAttachId(video.getPreviewAttachId())
                        .duration(video.getAttach().getDuration())
                        .build())
                .build();
    }
}