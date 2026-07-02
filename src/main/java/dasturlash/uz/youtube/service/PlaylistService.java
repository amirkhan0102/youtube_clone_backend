package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.playlist.*;
import dasturlash.uz.youtube.entity.ChannelEntity;
import dasturlash.uz.youtube.entity.PlaylistEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.enums.PlaylistStatusEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ChannelRepository;
import dasturlash.uz.youtube.repository.PlayListRepository;
import dasturlash.uz.youtube.repository.PlaylistVideoRepository;
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
public class PlaylistService {

    private final PlayListRepository playlistRepository;
    private final ChannelRepository channelRepository;
    private final PlaylistVideoRepository playlistVideoRepository;

    @Value("${server.url}")
    private String serverUrl;

    // 1. Create Playlist (USER)
    public PlaylistInfo create(CreatePlaylistDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        ChannelEntity channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new AppBadException("Channel not found"));

        if (!channel.getProfileId().equals(profileId)) {
            throw new AppBadException("Access denied: not channel owner");
        }

        PlaylistEntity entity = new PlaylistEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(PlaylistStatusEnum.PRIVATE);
        entity.setOrderNum(0);
        entity.setChannel(channel);
        playlistRepository.save(entity);

        return toInfo(entity);
    }

    // 2. Update Playlist (USER and OWNER)
    public PlaylistInfo update(Long id, UpdatePlaylistDTO dto) {
        PlaylistEntity entity = getOwnedPlaylist(id);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getOrderNum() != null) {
            entity.setOrderNum(dto.getOrderNum());
        }
        playlistRepository.save(entity);
        return toInfo(entity);
    }

    // 3. Change Status (USER and OWNER)
    public PlaylistInfo changeStatus(Long id, PlaylistStatusEnum status) {
        PlaylistEntity entity = getOwnedPlaylist(id);
        entity.setStatus(status);
        playlistRepository.save(entity);
        return toInfo(entity);
    }

    // 4. Delete (USER and OWNER, ADMIN)
    public Boolean delete(Long id) {
        PlaylistEntity entity = getPlaylistById(id);

        Integer profileId = SecurityUtil.getProfileId();
        boolean isOwner = entity.getChannel().getProfileId().equals(profileId);
        boolean isAdmin = isAdmin();

        if (!isOwner && !isAdmin) {
            throw new AppBadException("Access denied");
        }

        playlistRepository.delete(entity);
        return true;
    }

    // 5. Playlist Pagination (ADMIN)
    public Page<PlaylistInfo> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return playlistRepository.findAll(pageable).map(this::toInfo);
    }

    // 6. Playlist List By UserId (ADMIN)
    public List<PlaylistInfo> listByUserId(Integer userId) {
        return playlistRepository
                .findAllByChannel_Profile_IdOrderByOrderNumDesc(userId)
                .stream()
                .map(this::toInfo)
                .toList();
    }

    // 7. Get User Playlist (murojat qilgan user)
    public List<PlayListShortInfo> myPlaylists() {
        Integer profileId = SecurityUtil.getProfileId();
        return playlistRepository
                .findAllByChannel_Profile_IdOrderByOrderNumDesc(profileId)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    // 8. Get Channel Playlist By ChannelKey (only Public)
    public List<PlayListShortInfo> getChannelPlaylists(String channelId) {
        return playlistRepository
                .findAllByChannelIdAndStatusOrderByOrderNumDesc(
                        channelId, PlaylistStatusEnum.PUBLIC)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    // 9. Get Playlist by id
    public PlaylistDetailInfo getDetail(Long id) {
        PlaylistEntity entity = getPlaylistById(id);

        Long videoCount = playlistVideoRepository.countByPlaylistId(id);
        Long totalViewCount = playlistVideoRepository.sumViewCountByPlaylistId(id);
        LocalDateTime lastUpdate = playlistVideoRepository
                .findLastUpdateDateByPlaylistId(id);

        return PlaylistDetailInfo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .videoCount(videoCount != null ? videoCount : 0L)
                .totalViewCount(totalViewCount != null ? totalViewCount : 0L)
                .lastUpdateDate(lastUpdate)
                .build();
    }

    // Helper lar
    private PlaylistEntity getPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Playlist not found"));
    }

    private PlaylistEntity getOwnedPlaylist(Long id) {
        Integer profileId = SecurityUtil.getProfileId();
        PlaylistEntity entity = getPlaylistById(id);
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

    private PlaylistInfo toInfo(PlaylistEntity entity) {
        ChannelEntity channel = entity.getChannel();
        ProfileEntity profile = channel.getProfile();

        return PlaylistInfo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .orderNum(entity.getOrderNum())
                .channel(ChannelOwnerInfo.builder()
                        .id(channel.getId())
                        .name(channel.getName())
                        .photo(channel.getPhotoId() != null
                                ? AttachInfo.builder()
                                .id(channel.getPhotoId())
                                .url(serverUrl + "/attach/open/" + channel.getPhotoId())
                                .build()
                                : null)
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
                        .build())
                .build();
    }

    private PlayListShortInfo toShortInfo(PlaylistEntity entity) {
        Long videoCount = playlistVideoRepository.countByPlaylistId(entity.getId());
        List<VideoMiniInfo> firstTwo = playlistVideoRepository
                .findVideosByPlaylistId(entity.getId(), PageRequest.of(0, 2));


        return PlayListShortInfo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .channel(ChannelShortInfo.builder()
                        .id(entity.getChannel().getId())
                        .name(entity.getChannel().getName())
                        .build())
                .videoCount(videoCount != null ? videoCount : 0L)
                .videoList(firstTwo)
                .build();
    }
}