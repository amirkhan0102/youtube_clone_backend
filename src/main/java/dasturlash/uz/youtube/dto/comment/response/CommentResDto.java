package dasturlash.uz.youtube.dto.comment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResDto {
    private Integer id;
    private String content;
    @JsonProperty(namespace = "like_count")
    private Long likeCount;
    @JsonProperty(namespace = "dislike_count")
    private Long dislikeCount;
    @JsonProperty(namespace = "created_date")
    private LocalDateTime createdDate;
    private CommentCustomVideoInfoResDto video;
}
