package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import dasturlash.uz.youtube.dto.playlist.ChannelShortInfo;
import dasturlash.uz.youtube.dto.playlistvideo.*;
import dasturlash.uz.youtube.entity.PlaylistEntity;
import dasturlash.uz.youtube.entity.PlaylistVideoEntity;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.PlayListRepository;
import dasturlash.uz.youtube.repository.PlaylistVideoRepository;
import dasturlash.uz.youtube.repository.VideoRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistVideoService {

    private final PlaylistVideoRepository playlistVideoRepository;
    private final PlayListRepository playlistRepository;
    private final VideoRepository videoRepository;

    @Value("${server.url}")
    private String serverUrl;

    // 1. Create
    public PlaylistVideoInfo create(CreatePlaylistVideoDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        PlaylistEntity playlist = getOwnedPlaylist(dto.getPlaylistId(), profileId);
        VideoEntity video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Video not found"));

        PlaylistVideoEntity entity = new PlaylistVideoEntity();
        entity.setPlaylist(playlist);
        entity.setVideo(video);
        entity.setOrderNum(dto.getOrderNum() != null ? dto.getOrderNum() : 0);
        playlistVideoRepository.save(entity);

        return toInfo(entity);
    }

    // 2. Update
    public PlaylistVideoInfo update(UpdatePlaylistVideoDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();
        getOwnedPlaylist(dto.getPlaylistId(), profileId);

        PlaylistVideoEntity entity = playlistVideoRepository
                .findByPlaylistIdAndVideoId(dto.getPlaylistId(), dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Playlist video not found"));

        entity.setOrderNum(dto.getOrderNum());
        playlistVideoRepository.save(entity);
        return toInfo(entity);
    }

    // 3. Delete
    public Boolean delete(DeletePlaylistVideoDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();
        getOwnedPlaylist(dto.getPlaylistId(), profileId);

        PlaylistVideoEntity entity = playlistVideoRepository
                .findByPlaylistIdAndVideoId(dto.getPlaylistId(), dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Playlist video not found"));

        playlistVideoRepository.delete(entity);
        return true;
    }

    // 4. Get Video list by playListId (only published)
    public List<PlaylistVideoInfo> getByPlaylistId(Long playlistId) {
        return playlistVideoRepository
                .findAllByPlaylistIdAndVideo_StatusOrderByOrderNumDesc(
                        playlistId, VideoStatusEnum.PUBLIC)
                .stream()
                .map(this::toInfo)
                .toList();
    }

    private PlaylistEntity getOwnedPlaylist(Long playlistId, Integer profileId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppBadException("Playlist not found"));
        if (!playlist.getChannel().getProfileId().equals(profileId)) {
            throw new AppBadException("Access denied");
        }
        return playlist;
    }

    private PlaylistVideoInfo toInfo(PlaylistVideoEntity entity) {
        VideoEntity video = entity.getVideo();
        return PlaylistVideoInfo.builder()
                .playlistId(entity.getPlaylist().getId())
                .video(VideoInPlaylistInfo.builder()
                        .id(video.getId())
                        .previewAttach(video.getPreviewAttachId() != null
                                ? AttachInfo.builder()
                                .id(video.getPreviewAttachId())
                                .url(serverUrl + "/attach/open/" + video.getPreviewAttachId())
                                .build()
                                : null)
                        .title(video.getTitle())
                        .duration(video.getAttach().getDuration())
                        .build())
                .channel(ChannelShortInfo.builder()
                        .id(entity.getPlaylist().getChannel().getId())
                        .name(entity.getPlaylist().getChannel().getName())
                        .build())
                .createdDate(entity.getCreatedDate())
                .orderNum(entity.getOrderNum())
                .build();
    }
}