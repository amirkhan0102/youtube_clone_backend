package dasturlash.uz.youtube.repository;

import dasturlash.uz.youtube.entity.VideoLikeEntity;
import dasturlash.uz.youtube.enums.EmotionEnum;
import org.springframework.data.repository.CrudRepository;

public interface VideoLikeRepository extends CrudRepository<VideoLikeEntity, Integer> {
    boolean existsByProfileIdAndVideoIdAndEmotion(Integer profileId, String videoId, EmotionEnum emotion);

}
