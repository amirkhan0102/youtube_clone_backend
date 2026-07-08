package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import dasturlash.uz.youtube.dto.subsicripton.*;
import dasturlash.uz.youtube.dto.video.ChannelShortPhotoInfo;
import dasturlash.uz.youtube.entity.ChannelEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.SubscriptionEntity;
import dasturlash.uz.youtube.enums.SubscriptionStatusEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ChannelRepository;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.SubscriptionRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ChannelRepository channelRepository;
    private final ProfileRepository profileRepository;

    @Value("${server.url}")
    private String serverUrl;

    public SubscriptionInfo create(CreateSubscriptionDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        ChannelEntity channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new AppBadException("Channel not found"));

        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException("Profile not found"));

        var existingOpt = subscriptionRepository.findByProfileIdAndChannelId(profileId, dto.getChannelId());

        if (existingOpt.isPresent()) {
            SubscriptionEntity entity = existingOpt.get();
            entity.setStatus(SubscriptionStatusEnum.ACTIVE);
            entity.setNotificationType(dto.getNotificationType());  // ← To'g'rilandi
            subscriptionRepository.save(entity);
            return toInfo(entity);
        }

        // Create new
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setProfile(profile);
        entity.setChannel(channel);
        entity.setStatus(SubscriptionStatusEnum.ACTIVE);
        entity.setNotificationType(dto.getNotificationType());   // ← To'g'rilandi
        entity.setCreatedDate(LocalDateTime.now());

        subscriptionRepository.save(entity);
        return toInfo(entity);
    }

    public SubscriptionInfo changeStatus(ChangeSubscriptionStatusDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        SubscriptionEntity entity = subscriptionRepository
                .findByProfileIdAndChannelId(profileId, dto.getChannelId())
                .orElseThrow(() -> new AppBadException("Subscription not found"));

        if (dto.getStatus() == SubscriptionStatusEnum.BLOCK) {
            entity.setUnsubscribeDate(LocalDateTime.now());
        } else if (dto.getStatus() == SubscriptionStatusEnum.ACTIVE) {
            entity.setUnsubscribeDate(null);
        }

        entity.setStatus(dto.getStatus());
        subscriptionRepository.save(entity);
        return toInfo(entity);
    }

    public SubscriptionInfo changeNotificationType(ChangeNotificationTypeDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        SubscriptionEntity entity = subscriptionRepository
                .findByProfileIdAndChannelId(profileId, dto.getChannelId())
                .orElseThrow(() -> new AppBadException("Subscription not found"));

        entity.setNotificationType(dto.getNotificationType());   // ← To'g'rilandi
        subscriptionRepository.save(entity);
        return toInfo(entity);
    }

    public List<SubscriptionInfo> mySubscriptions() {
        Integer profileId = SecurityUtil.getProfileId();
        return subscriptionRepository
                .findAllByProfileIdAndStatus(profileId, SubscriptionStatusEnum.ACTIVE)
                .stream()
                .map(this::toInfo)
                .toList();
    }

    public List<SubscriptionAdminInfo> getByUserId(Integer userId) {
        return subscriptionRepository
                .findAllByProfileIdAndStatus(userId, SubscriptionStatusEnum.ACTIVE)
                .stream()
                .map(this::toAdminInfo)
                .toList();
    }

    private SubscriptionInfo toInfo(SubscriptionEntity entity) {
        ChannelEntity channel = entity.getChannel();
        return SubscriptionInfo.builder()
                .id(entity.getId())
                .channel(ChannelShortPhotoInfo.builder()
                        .id(channel.getId())
                        .name(channel.getName())
                        .photo(channel.getPhotoId() != null
                                ? AttachInfo.builder()
                                .id(channel.getPhotoId())
                                .url(serverUrl + "/attach/open/" + channel.getPhotoId())
                                .build()
                                : null)
                        .build())
                .notificationType(entity.getNotificationType())
                .build();
    }

    private SubscriptionAdminInfo toAdminInfo(SubscriptionEntity entity) {
        ChannelEntity channel = entity.getChannel();
        return SubscriptionAdminInfo.builder()
                .id(entity.getId())
                .channel(ChannelShortPhotoInfo.builder()
                        .id(channel.getId())
                        .name(channel.getName())
                        .photo(channel.getPhotoId() != null
                                ? AttachInfo.builder()
                                .id(channel.getPhotoId())
                                .url(serverUrl + "/attach/open/" + channel.getPhotoId())
                                .build()
                                : null)
                        .build())
                .notificationType(entity.getNotificationType())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}