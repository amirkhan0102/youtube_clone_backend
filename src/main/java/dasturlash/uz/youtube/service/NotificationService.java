package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.notification.*;
import dasturlash.uz.youtube.entity.ChannelEntity;
import dasturlash.uz.youtube.entity.NotificationEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ChannelRepository;
import dasturlash.uz.youtube.repository.NotificationRepository;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProfileRepository profileRepository;
    private final ChannelRepository channelRepository;
    private final VideoRepository videoRepository;

    // 1. Filter with pagination
    public Page<NotificationInfo> filter(NotificationFilterDTO dto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.filter(
                dto.getProfileId(),
                dto.getChannelId(),
                dto.getVideoId(),
                dto.getDateFrom(),
                dto.getDateTo(),
                pageable
        ).map(this::toInfo);
    }

    // 2. Send Notification
    public NotificationInfo send(SendNotificationDTO dto) {
        ProfileEntity profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new AppBadException("Profile not found"));

        ChannelEntity channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new AppBadException("Channel not found"));

        VideoEntity video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Video not found"));

        NotificationEntity entity = new NotificationEntity();
        entity.setProfile(profile);
        entity.setChannel(channel);
        entity.setVideo(video);
        entity.setMessage(dto.getMessage());
        notificationRepository.save(entity);

        return toInfo(entity);
    }

    private NotificationInfo toInfo(NotificationEntity entity) {
        return NotificationInfo.builder()
                .id(entity.getId())
                .profile(ProfileMiniInfo.builder()
                        .id(entity.getProfile().getId())
                        .name(entity.getProfile().getName())
                        .build())
                .channel(ChannelMiniInfo.builder()
                        .id(entity.getChannel().getId())
                        .name(entity.getChannel().getName())
                        .build())
                .video(VideoMiniForNotification.builder()
                        .id(entity.getVideo().getId())
                        .title(entity.getVideo().getTitle())
                        .build())
                .message(entity.getMessage())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}