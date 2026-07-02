package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.attach.AttachShortInfoDTO;
import dasturlash.uz.youtube.dto.channel.ChannelShortInfoDTO;
import dasturlash.uz.youtube.dto.playlist.PlaylistShortDTO;
import dasturlash.uz.youtube.dto.profile.ProfileShortInfoDTO;
import dasturlash.uz.youtube.dto.video.*;
import dasturlash.uz.youtube.dto.videolike.LikeFullDTO;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.enums.EmotionEnum;
import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.exception.ItemNotFoundException;
import dasturlash.uz.youtube.mapper.VideoAdminShortInfoMapper;
import dasturlash.uz.youtube.mapper.VideoFullInfoMapper;
import dasturlash.uz.youtube.mapper.VideoPlaylistInfoMapper;
import dasturlash.uz.youtube.mapper.VideoShortInfoMapper;
import dasturlash.uz.youtube.repository.VideoRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private VideoTagService videoTagService;
    @Autowired
    private VideoLikeService videoLikeService;

    public String create(VideoCreateDTO dto) {
        isProfileChannelOwner(dto.getChannelId());

        if (attachService.findById(dto.getPreviewAttachId()) == null) {
            throw new ItemNotFoundException("Preview attach not found");
        }

        if (attachService.findById(dto.getAttachId()) == null) {
            throw new ItemNotFoundException("Video not found");
        }

        VideoEntity video = new VideoEntity();

        dtoToEntity(dto, video);

        videoRepository.save(video);

        return "Successfully uploaded";

    }

    private void dtoToEntity(VideoCreateDTO dto, VideoEntity video) {
        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setPreviewAttachId(dto.getPreviewAttachId());
        video.setAttachId(dto.getAttachId());
        video.setCategoryId(dto.getCategoryId());
        video.setChannelId(dto.getChannelId());
        video.setType(dto.getType());
        video.setStatus(dto.getStatus());
        if (dto.getStatus().equals(VideoStatusEnum.PRIVATE)) video.setPublishedDate(null);
        video.setViewCount(0L);
        video.setSharedCount(0L);
        video.setLikeCount(0L);
        video.setDislikeCount(0L);
    }

    public String updateDetail(String videoId, VideoDetailUpdateDTO dto) {
        VideoEntity video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ItemNotFoundException("Video not found"));

        isProfileChannelOwner(video.getChannelId());

        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setStatus(dto.getStatus());
        video.setCategoryId(dto.getCategoryId());
        if (!video.getPreviewAttachId().equals(dto.getPreviewAttachId())) {
            boolean ok = attachService.delete(video.getAttachId());
            if (!ok) {
                throw new AppBadException("Something went wrong");
            }
            video.setPreviewAttachId(dto.getPreviewAttachId());
        }

        videoRepository.save(video);

        return "Successfully updated";
    }

    public String updateStatus(VideoStatusUpdateDTO dto) {
        int effRow = videoRepository.changeVideoStatus(dto.getVideoId(), dto.getStatus(), SecurityUtil.getProfileId());

        if (effRow > 0) {
            return "Successfully changed";
        }
        throw new AppBadException("Video not found or this video is not yours");
    }

    private void isProfileChannelOwner(String channelId) {
        if (!channelService.isProfileChannelOwner(SecurityUtil.getProfileId(), channelId)) {
            throw new AppBadException("This video is not yours!");
        }
    }

    public Integer increaseViewCount(String videoId) {
        int effRow = videoRepository.increaseViewCount(videoId);

        if (effRow > 0) {
            return videoRepository.findViewCountById(videoId);
        }
        throw new AppBadException("Something went wrong");
    }

    public Page<VideoShortInfoDTO> getVideosByCategory(Integer categoryId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<VideoShortInfoMapper> pages = videoRepository.getVideosByCategory(categoryId, pageable);

        List<VideoShortInfoDTO> response = pages.stream()
                .map(this::mapperToShortInfoDto)
                .toList();

        return new PageImpl<>(response, pageable, pages.getTotalElements());
    }

    private VideoShortInfoDTO mapperToShortInfoDto(VideoShortInfoMapper mapper) {
        VideoShortInfoDTO dto = new VideoShortInfoDTO();
        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setPublishedDate(mapper.getPublishedDate());
        dto.setDuration(mapper.getDuration());
        dto.setViewCount(mapper.getViewCount());

        if (mapper.getPreview() != null) {
            dto.setPreview(new AttachShortInfoDTO(
                    mapper.getPreview().getId(),
                    attachService.openURL(mapper.getPreview().getId())
            ));
        }

        if (mapper.getChannel() != null) {
            dto.setChannel(new ChannelShortInfoDTO(
                    mapper.getChannel().getId(),
                    mapper.getChannel().getName(),
                    attachService.openURL(mapper.getChannel().getPhotoId())
            ));
        }

        return dto;
    }

    public Page<VideoShortInfoDTO> search(VideoSearchDTO dto, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoShortInfoMapper> pages = videoRepository.searchByTitle("%" + dto.getSearch() + "%", pageable);

        List<VideoShortInfoDTO> response = pages.stream()
                .map(this::mapperToShortInfoDto)
                .toList();
        return new PageImpl<>(response, pageable, pages.getTotalElements());
    }

    public Page<VideoShortInfoDTO> getVideosByTag(Integer tagId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<VideoShortInfoMapper> pages = videoRepository.getVideosByTag(tagId, pageable);

        List<VideoShortInfoDTO> response = pages.stream()
                .map(this::mapperToShortInfoDto)
                .toList();

        return new PageImpl<>(response, pageable, pages.getTotalElements());
    }

    public VideoFullInfoDTO getById(String videoId) {
        VideoFullInfoMapper mapper = videoRepository.getByIdWithFull(videoId, SecurityUtil.getProfileId());
        if(mapper == null){
            throw new ItemNotFoundException("Video not found");
        }
        return mapperToFullInfoDto(mapper) ;
    }

    private VideoFullInfoDTO mapperToFullInfoDto(VideoFullInfoMapper mapper) {
        VideoFullInfoDTO dto = new VideoFullInfoDTO();
        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setDescription(mapper.getDescription());
        dto.setViewCount(mapper.getViewCount());
        dto.setSharedCount(mapper.getSharedCount());

        dto.setTagList(videoTagService.geTagsByVideoId(mapper.getId()));

        if(mapper.getPreview() != null){
            dto.setPreview(new AttachShortInfoDTO(
                    mapper.getPreview().getId(),
                    attachService.openURL(mapper.getPreview().getId())
            ));
        }

        if(mapper.getVideo() != null){
            dto.setDuration(mapper.getVideo().getDuration());

            dto.setVideo(new AttachShortInfoDTO(
                    mapper.getVideo().getId(),
                    attachService.openURL(mapper.getVideo().getId())
            ));
        }

        if(mapper.getChannel() != null){
            dto.setChannel(new ChannelShortInfoDTO(
                    mapper.getChannel().getId(),
                    mapper.getChannel().getName(),
                    attachService.openURL(mapper.getChannel().getPhotoId())
            ));
        }

        dto.setLikeInfo(new LikeFullDTO(
                mapper.getLikeCount(),
                mapper.getDislikeCount(),
                videoLikeService.isUserLikedVideo(SecurityUtil.getProfileId(), dto.getId(), EmotionEnum.LIKE),
                videoLikeService.isUserLikedVideo(SecurityUtil.getProfileId(), dto.getId(), EmotionEnum.DISLIKE)
        ));

        return dto;
    }

    public Page<VideoAdminShortInfoDTO> getList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoAdminShortInfoMapper> pages = videoRepository.getList(pageable);

        List<VideoAdminShortInfoDTO> response = pages.stream()
                .map(this::adminMapperToDto)
                .toList();

        return new PageImpl<>(response, pageable, pages.getTotalElements());
    }

    private VideoAdminShortInfoDTO adminMapperToDto(VideoAdminShortInfoMapper mapper) {
        VideoShortInfoDTO vDto = new VideoShortInfoDTO();
        vDto.setId(mapper.getId());
        vDto.setTitle(mapper.getTitle());
        vDto.setPublishedDate(mapper.getPublishedDate());
        vDto.setDuration(mapper.getDuration());
        vDto.setViewCount(mapper.getViewCount());

        if (mapper.getPreview() != null) {
            vDto.setPreview(new AttachShortInfoDTO(
                    mapper.getPreview().getId(),
                    attachService.openURL(mapper.getPreview().getId())
            ));
        }
        if (mapper.getChannel() != null) {
            vDto.setChannel(new ChannelShortInfoDTO(
                    mapper.getChannel().getId(),
                    mapper.getChannel().getName(),
                    attachService.openURL(mapper.getChannel().getPhotoId())
            ));
        }

        ProfileShortInfoDTO pDto = null;
        if (mapper.getProfileId() != null) {
            pDto = new ProfileShortInfoDTO();
            pDto.setId(mapper.getProfileId());
            pDto.setName(mapper.getProfileName());
            pDto.setSurname(mapper.getProfileSurname());
        }

        PlaylistShortDTO plDto = null;
        if (mapper.getPlaylistId() != null) {
            plDto = new PlaylistShortDTO();
            plDto.setId(mapper.getPlaylistId());
            plDto.setName(mapper.getPlaylistName());
        }

        return new VideoAdminShortInfoDTO(vDto, pDto, plDto);
    }
    public Page<VideoPlaylistInfoDTO> getChannelVideos(String channelId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoPlaylistInfoMapper> pages = videoRepository.getChannelVideos(channelId, pageable);

        List<VideoPlaylistInfoDTO> response = pages.stream()
                .map(this::playlistMapperToDto)
                .toList();

        return new PageImpl<>(response, pageable, pages.getTotalElements());
    }

    private VideoPlaylistInfoDTO playlistMapperToDto(VideoPlaylistInfoMapper mapper) {
        VideoPlaylistInfoDTO dto = new VideoPlaylistInfoDTO();
        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setViewCount(mapper.getViewCount());
        dto.setPublishedDate(mapper.getPublishedDate());
        dto.setDuration(mapper.getDuration());

        if(mapper.getPreview() != null){
            dto.setPreview(new AttachShortInfoDTO(
                    mapper.getPreview().getId(),
                    attachService.openURL(mapper.getPreview().getId())
            ));
        }

        return dto;
    }

    public VideoEntity get(String id) {
        return videoRepository.findByIdAndVisibleIsTrue(id).orElseThrow(() -> {
            throw new AppBadException("Video not found");
        });
    }
}
