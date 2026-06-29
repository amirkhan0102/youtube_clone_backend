package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.tag.TagResponseDto;
import dasturlash.uz.youtube.dto.videotag.VideoTagDTO;
import dasturlash.uz.youtube.dto.videotag.VideoTagFullInfoDTO;
import dasturlash.uz.youtube.entity.ChannelEntity;
import dasturlash.uz.youtube.entity.TagEntity;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.entity.VideoTagEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.exception.ItemNotFoundException;
import dasturlash.uz.youtube.repository.TagRepository;
import dasturlash.uz.youtube.repository.VideoRepository;
import dasturlash.uz.youtube.repository.VideoTagRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoTagService {

    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private VideoRepository videoRepository;

    public String addTagToVideo(VideoTagDTO dto) {
        VideoEntity video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Video not found!"));

        checkOwner(video, SecurityUtil.getProfileId());

        TagEntity tag = tagRepository.findById(dto.getTagId())
                .orElseThrow(() -> new AppBadException("Tag not found!"));

        if (videoTagRepository.existsByVideoIdAndTagId(dto.getVideoId(), dto.getTagId())) {
            throw new AppBadException("Video tag already exists!");

        }
        VideoTagEntity videoTag = new VideoTagEntity();
        videoTag.setVideoId(video.getId());
        videoTag.setTagId(tag.getId());
        videoTagRepository.save(videoTag);

        return "Successfully added";
    }

    public String deleteTagFromVideo(VideoTagDTO dto) {
        VideoEntity video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new AppBadException("Video not found"));

        checkOwner(video, SecurityUtil.getProfileId());

        Optional<VideoTagEntity> optional = videoTagRepository.findByVideoIdAndTagId(dto.getVideoId(), dto.getTagId());
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Video tag not found");
        }

        videoTagRepository.delete(optional.get());

        return "Successfully deleted";
    }

    public List<VideoTagFullInfoDTO> getTagListByVideoId(String videoId) {
        videoRepository.findById(videoId).orElseThrow(() -> new AppBadException("video not found!"));

        List<VideoTagEntity> videoTags = videoTagRepository.findAllByVideoId(videoId);

        List<VideoTagFullInfoDTO> response = new ArrayList<>();
        for (VideoTagEntity videoTag : videoTags) {
            VideoTagFullInfoDTO dto = new VideoTagFullInfoDTO();
            dto.setId(videoTag.getId());
            dto.setVideoId(videoTag.getVideoId());
            dto.setCreatedDate(videoTag.getCreatedDate());
            dto.setTagInfo(new TagResponseDto(videoTag.getTagId(), videoTag.getTag().getName(), null));

            response.add(dto);
        }

        return response;
    }

    private void checkOwner(VideoEntity videoEntity, Integer currentProfileId) {
        ChannelEntity channel = videoEntity.getChannel();

        if (channel == null) {
            throw new AppBadException("Channel not found!");
        }
        if (!channel.getProfileId().equals(currentProfileId)) {
            throw new AppBadException("You are not owner of this video!");
        }
    }

    public List<TagResponseDto> geTagsByVideoId(String videoId) {
        return videoTagRepository.getVideoTagEntitiesByVideoId(videoId)
                .stream()
                .map(vt -> new TagResponseDto(vt.getTagId(), vt.getTag().getName(), null))
                .toList();
    }
}
