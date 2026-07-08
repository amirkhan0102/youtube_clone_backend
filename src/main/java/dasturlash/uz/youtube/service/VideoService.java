package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import dasturlash.uz.youtube.dto.video.*;
import dasturlash.uz.youtube.entity.*;
import dasturlash.uz.youtube.enums.LikeTypeEnum;
import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.*;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    private final AttachService attachService;
    private final VideoTagRepository videoTagRepository;
    private final VideoLikeRepository videoLikeRepository;

    @Value("${server.url}")
    private String serverUrl;

    // 1. Create Video (USER)
    public VideoFullInfo create(CreateVideoDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        ChannelEntity channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new AppBadException("Channel not found"));

        if (!channel.getProfileId().equals(profileId)) {
            throw new AppBadException("Access denied: not channel owner");
        }

        AttachEntity attach = attachService.getById(dto.getAttachId());
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new AppBadException("Category not found"));

        VideoEntity entity = new VideoEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setAttach(attach);
        entity.setCategory(category);
        entity.setChannel(channel);
        entity.setType(dto.getType());
        entity.setStatus(VideoStatusEnum.PRIVATE);
        entity.setPublishedDate(LocalDateTime.now());

        if (dto.getPreviewAttachId() != null) {
            entity.setPreviewAttach(attachService.getById(dto.getPreviewAttachId()));
        }

        videoRepository.save(entity);
        return toFullInfo(entity, profileId);
    }

    // 2. Update Video Detail (USER and OWNER)
    public VideoFullInfo update(String id, UpdateVideoDTO dto) {
        VideoEntity entity = getOwnedVideo(id);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        if (dto.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new AppBadException("Category not found"));
            entity.setCategory(category);
        }

        videoRepository.save(entity);
        return toFullInfo(entity, SecurityUtil.getProfileId());
    }

    // 3. Change Video Status (USER and OWNER)
    public VideoFullInfo changeStatus(String id, VideoStatusEnum status) {
        VideoEntity entity = getOwnedVideo(id);
        entity.setStatus(status);
        videoRepository.save(entity);
        return toFullInfo(entity, SecurityUtil.getProfileId());
    }

    // 4. Increase view count by id
    public void increaseView(String id) {
        videoRepository.incrementViewCount(id);
    }

    // 5. Pagination by CategoryId
    public Page<VideoShortInfo> paginationByCategory(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return videoRepository
                .findAllByCategory_IdAndStatus(categoryId, VideoStatusEnum.PUBLIC, pageable)
                .map(this::toShortInfo);
    }

    // 6. Search by Title
    public Page<VideoShortInfo> searchByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return videoRepository
                .findAllByTitleContainingIgnoreCaseAndStatus(title, VideoStatusEnum.PUBLIC, pageable)
                .map(this::toShortInfo);
    }

    // 7. Get by tag_id with pagination
    public Page<VideoShortInfo> getByTagId(Integer tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoTagEntity> videoTags = videoTagRepository.findAllByTagId(tagId, pageable);
        return videoTags.map(vt -> toShortInfo(vt.getVideo()));
    }

    // 8. Get Video By id
    public VideoFullInfo getById(String id) {
        VideoEntity entity = getVideoById(id);
        Integer profileId = getCurrentProfileIdOrNull();

        if (entity.getStatus() == VideoStatusEnum.PRIVATE) {
            if (profileId == null) {
                throw new AppBadException("Access denied");
            }
            boolean isOwner = entity.getChannel().getProfileId().equals(profileId);
            boolean isAdmin = isAdmin();
            if (!isOwner && !isAdmin) {
                throw new AppBadException("Access denied");
            }
        }

        return toFullInfo(entity, profileId);
    }

    // 9. Get Video List Pagination (ADMIN)
    public Page<VideoShortInfo> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return videoRepository.findAll(pageable).map(this::toShortInfo);
    }

    // 10. Get Channel Video List Pagination
    public Page<VideoPlayListInfo> getChannelVideos(String channelId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return videoRepository
                .findAllByChannelIdOrderByCreatedDateDesc(channelId, pageable)
                .map(this::toPlayListInfo);
    }

    // Helper lar
    public VideoEntity getVideoById(String id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Video not found"));
    }

    private VideoEntity getOwnedVideo(String id) {
        Integer profileId = SecurityUtil.getProfileId();
        VideoEntity entity = getVideoById(id);
        if (!entity.getChannel().getProfileId().equals(profileId)) {
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

    private Integer getCurrentProfileIdOrNull() {
        try {
            return SecurityUtil.getProfileId();
        } catch (Exception e) {
            return null;
        }
    }

    private AttachInfo toAttachInfo(String attachId) {
        if (attachId == null) return null;
        return AttachInfo.builder()
                .id(attachId)
                .url(serverUrl + "/attach/open/" + attachId)
                .build();
    }

    private VideoFullInfo toFullInfo(VideoEntity entity, Integer profileId) {
        List<TagShortInfo> tags = videoTagRepository.findAllByVideoId(entity.getId())
                .stream()
                .map(vt -> TagShortInfo.builder()
                        .id(vt.getTag().getId())
                        .name(vt.getTag().getName())
                        .build())
                .toList();

        Boolean isLiked = false;
        Boolean isDisliked = false;
        if (profileId != null) {
            var likeOpt = videoLikeRepository.findByProfileIdAndVideoId(profileId, entity.getId());
            if (likeOpt.isPresent()) {
                isLiked = likeOpt.get().getType() == LikeTypeEnum.LIKE;
                isDisliked = likeOpt.get().getType() == LikeTypeEnum.DISLIKE;
            }
        }

        return VideoFullInfo.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .previewAttach(toAttachInfo(entity.getPreviewAttachId()))
                .attach(VideoAttachInfo.builder()
                        .id(entity.getAttachId())
                        .url(serverUrl + "/attach/open/" + entity.getAttachId())
                        .duration(entity.getAttach().getDuration())
                        .build())
                .category(entity.getCategory() != null
                        ? CategoryShortInfo.builder()
                        .id(entity.getCategory().getId())
                        .name(entity.getCategory().getName())
                        .build()
                        : null)
                .tagList(tags)
                .publishedDate(entity.getPublishedDate())
                .channel(ChannelShortPhotoInfo.builder()
                        .id(entity.getChannel().getId())
                        .name(entity.getChannel().getName())
                        .photo(toAttachInfo(entity.getChannel().getPhotoId()))
                        .build())
                .viewCount(entity.getViewCount())
                .sharedCount(entity.getSharedCount())
                .like(LikeInfo.builder()
                        .likeCount(entity.getLikeCount())
                        .dislikeCount(entity.getDislikeCount())
                        .isUserLiked(isLiked)
                        .isUserDisliked(isDisliked)
                        .build())
                .duration(entity.getAttach().getDuration())
                .build();
    }

    private VideoShortInfo toShortInfo(VideoEntity entity) {
        return VideoShortInfo.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .previewAttach(toAttachInfo(entity.getPreviewAttachId()))
                .publishedDate(entity.getPublishedDate())
                .channel(ChannelShortPhotoInfo.builder()
                        .id(entity.getChannel().getId())
                        .name(entity.getChannel().getName())
                        .photo(toAttachInfo(entity.getChannel().getPhotoId()))
                        .build())
                .viewCount(entity.getViewCount())
                .duration(entity.getAttach().getDuration())
                .build();
    }

    private VideoPlayListInfo toPlayListInfo(VideoEntity entity) {
        return VideoPlayListInfo.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .previewAttach(toAttachInfo(entity.getPreviewAttachId()))
                .viewCount(entity.getViewCount())
                .publishedDate(entity.getPublishedDate())
                .duration(entity.getAttach().getDuration())
                .build();
    }
}