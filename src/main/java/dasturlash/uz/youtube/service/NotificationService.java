package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.notification.*;
import dasturlash.uz.youtube.entity.ChannelEntity;
import dasturlash.uz.youtube.entity.NotificationEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.repository.custom.CustomNotificationRepository;
import dasturlash.uz.youtube.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private CustomNotificationRepository customNotificationRepository;

    @Autowired
    private  ProfileService profileService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private VideoService videoService;

    public NotificationResponseDTO send(NotificationRequestDTO dto) {
        ProfileEntity profile = profileService.get(dto.getProfileId());
        ChannelEntity channel = channelService.get(dto.getChannelId());
        VideoEntity video = videoService.get(dto.getVideoId());

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setProfileId(profile.getId());
        notificationEntity.setChannelId(channel.getId());
        notificationEntity.setVideoId(video.getId());
        notificationEntity.setMessage(dto.getMessage());
        notificationEntity.setIsProfileReceived(false);

        notificationRepository.save(notificationEntity);

        return toResponse(notificationEntity, profile,channel,video);
    }

    public Page<NotificationResponseDTO> filter(NotificationFilterDTO dto, Integer page, Integer size) {
        Page<NotificationEntity> pages = customNotificationRepository.filter(dto,page, size);

        return new PageImpl<>(
                pages.stream().map(this::toResponse).toList(),
                PageRequest.of(page,size),
                pages.getTotalElements()
        );
    }


    private NotificationResponseDTO toResponse(NotificationEntity notification, ProfileEntity profile,
                                               ChannelEntity channel, VideoEntity video) {
        return new NotificationResponseDTO(
                notification.getId(),
                new NotificationProfileDTO(profile.getId(), profile.getName()),
                new NotificationChannelDTO(channel.getId(), channel.getName()),
                new NotificationVideoDTO(video.getId(), video.getTitle()),
                notification.getMessage(),
                notification.getCreatedDate()
        );
    }

    private NotificationResponseDTO toResponse(NotificationEntity notification){
        ProfileEntity profile = profileService.get(notification.getProfileId());
        ChannelEntity channel = channelService.get(notification.getChannelId());
        VideoEntity video = videoService.get(notification.getVideoId());

        return  toResponse(notification,profile,channel,video);
    }
}
