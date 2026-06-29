package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.enums.EmotionEnum;
import dasturlash.uz.youtube.repository.VideoLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoLikeService {
    @Autowired
    private VideoLikeRepository videoLikeRepository;

    public Boolean isUserLikedVideo(Integer profileId, String videoId, EmotionEnum emotion) {
        return videoLikeRepository.existsByProfileIdAndVideoIdAndEmotion(profileId, videoId, emotion);
    }
}
