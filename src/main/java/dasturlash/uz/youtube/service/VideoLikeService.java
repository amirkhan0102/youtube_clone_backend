package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import dasturlash.uz.youtube.dto.playlist.ChannelShortInfo;
import dasturlash.uz.youtube.dto.videolike.VideoInLikeInfo;
import dasturlash.uz.youtube.dto.videolike.VideoLikeDTO;
import dasturlash.uz.youtube.dto.videolike.VideoLikeInfo;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.entity.VideoLikeEntity;
import dasturlash.uz.youtube.enums.LikeTypeEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.VideoLikeRepository;
import dasturlash.uz.youtube.repository.VideoRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoLikeService {

    private final VideoLikeRepository videoLikeRepository;
    private final VideoRepository videoRepository;
    private final ProfileRepository profileRepository;   // ← Qo'shildi

    @Value("${server.url}")
    private String serverUrl;

    // 1. Create / Toggle Like or Dislike
    public String createLike(VideoLikeDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        VideoEntity video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Video not found"));

        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException("Profile not found"));

        Optional<VideoLikeEntity> existingOpt = videoLikeRepository
                .findByProfileIdAndVideoId(profileId, dto.getVideoId());

        if (existingOpt.isPresent()) {
            VideoLikeEntity entity = existingOpt.get();

            if (entity.getType() == dto.getType()) {
                // Remove like/dislike
                updateVideoLikeCount(video, entity.getType(), -1);
                videoLikeRepository.delete(entity);
                return "Removed";
            } else {
                // Change like <-> dislike
                updateVideoLikeCount(video, entity.getType(), -1);
                entity.setType(dto.getType());
                videoLikeRepository.save(entity);
                updateVideoLikeCount(video, dto.getType(), 1);
                return "Changed to " + dto.getType();
            }
        }

        // Create new
        VideoLikeEntity entity = new VideoLikeEntity();
        entity.setProfile(profile);           // ← To'g'rilandi
        entity.setVideo(video);
        entity.setType(dto.getType());
        entity.setCreatedDate(LocalDateTime.now());

        videoLikeRepository.save(entity);
        updateVideoLikeCount(video, dto.getType(), 1);

        return dto.getType().name();
    }

    // 2. Remove Like
    public Boolean removeLike(String videoId) {
        Integer profileId = SecurityUtil.getProfileId();

        VideoLikeEntity entity = videoLikeRepository
                .findByProfileIdAndVideoId(profileId, videoId)
                .orElseThrow(() -> new AppBadException("Like not found"));

        VideoEntity video = entity.getVideo();
        updateVideoLikeCount(video, entity.getType(), -1);
        videoLikeRepository.delete(entity);
        return true;
    }

    // 3. My Liked Videos
    public Page<VideoLikeInfo> myLikedVideos(int page, int size) {
        Integer profileId = SecurityUtil.getProfileId();
        Pageable pageable = PageRequest.of(page, size);

        return videoLikeRepository
                .findAllByProfileIdAndTypeOrderByCreatedDateDesc(
                        profileId, LikeTypeEnum.LIKE, pageable)
                .map(this::toInfo);
    }

    // 4. Admin - User Liked Videos
    public Page<VideoLikeInfo> getLikedByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoLikeRepository
                .findAllByProfileIdAndTypeOrderByCreatedDateDesc(
                        userId, LikeTypeEnum.LIKE, pageable)
                .map(this::toInfo);
    }

    private void updateVideoLikeCount(VideoEntity video, LikeTypeEnum type, int delta) {
        if (type == LikeTypeEnum.LIKE) {
            video.setLikeCount(video.getLikeCount() + delta);
        } else {
            video.setDislikeCount(video.getDislikeCount() + delta);
        }
        videoRepository.save(video);
    }

    private VideoLikeInfo toInfo(VideoLikeEntity entity) {
        VideoEntity video = entity.getVideo();
        return VideoLikeInfo.builder()
                .id(entity.getId())
                .video(VideoInLikeInfo.builder()
                        .id(video.getId())
                        .name(video.getTitle())
                        .channel(ChannelShortInfo.builder()
                                .id(video.getChannel().getId())
                                .name(video.getChannel().getName())
                                .build())
                        .duration(video.getAttach() != null ? video.getAttach().getDuration() : null)
                        .previewAttach(video.getPreviewAttachId() != null
                                ? AttachInfo.builder()
                                .id(video.getPreviewAttachId())
                                .url(serverUrl + "/attach/open/" + video.getPreviewAttachId())
                                .build()
                                : null)
                        .build())
                .build();
    }
}