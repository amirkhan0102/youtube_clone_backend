package dasturlash.uz.youtube.service;


import dasturlash.uz.youtube.dto.video.TagShortInfo;
import dasturlash.uz.youtube.dto.videotag.CreateVideoTagDTO;
import dasturlash.uz.youtube.dto.videotag.VideoTagDTO;
import dasturlash.uz.youtube.entity.TagEntity;
import dasturlash.uz.youtube.entity.VideoEntity;
import dasturlash.uz.youtube.entity.VideoTagEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.TagRepository;
import dasturlash.uz.youtube.repository.VideoRepository;
import dasturlash.uz.youtube.repository.VideoTagRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoTagService {

    private final VideoTagRepository videoTagRepository;
    private final VideoRepository videoRepository;
    private final TagRepository tagRepository;


    //1 add Tag to video

    public VideoTagDTO addTag(CreateVideoTagDTO dto){
        Integer profileId= SecurityUtil.getProfileId();

        VideoEntity video = videoRepository.findById(dto.getVideId())
                .orElseThrow(()-> new AppBadException("Video Not Found"));

        if(!video.getChannel().getProfileId().equals(profileId)) {
            throw  new AppBadException("Access denied: not video owner");
        }
        if(videoTagRepository.existsByVideoIdAndTagId(dto.getVideId(), dto.getTagId())) {
            throw  new AppBadException("Access denied: tag already exists");
        }

        TagEntity tag = tagRepository.findById(dto.getTagId())
                .orElseThrow(()-> new AppBadException("Tag Not Found"));

        VideoTagEntity entity = new VideoTagEntity();
        entity.setVideo(video);
        entity.setTag(tag);
        videoTagRepository.save(entity);

        return toDTO(entity);

    }



    // 2 Delete tag from video(Owner or User)

    public Boolean deleteTag(CreateVideoTagDTO dto){
        Integer profileId= SecurityUtil.getProfileId();


     VideoEntity video =   videoRepository.findById(dto.getVideId())
             .orElseThrow(()-> new AppBadException("Video Not Found"));


        if (!video.getChannel().getProfileId().equals(profileId)) {
            throw new AppBadException("Access denied: not video owner");
        }

        VideoTagEntity entity = videoTagRepository
                .findByVideoIdAndTagId(dto.getVideId(), dto.getTagId())
                .orElseThrow(() -> new AppBadException("Tag not found in this video"));

        videoTagRepository.delete(entity);
        return true;


    }


    //3 get video tag list by videoId
    public List<VideoTagDTO> getByVideoId(String videoId){
        return videoTagRepository.findAllByVideoId(videoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }


    private VideoTagDTO toDTO(VideoTagEntity entity) {
        return VideoTagDTO.builder()
                .id(entity.getId())
                .videoId(entity.getVideoId())
                .tag(TagShortInfo.builder()
                        .id(entity.getTag().getId())
                        .name(entity.getTag().getName())
                        .build())
                .createdDate(entity.getCreatedDate())
                .build();
    }


}
