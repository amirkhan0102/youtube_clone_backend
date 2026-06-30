package dasturlash.uz.youtube.dto.comment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCustomVideoInfoResDto {
    private String id;
    @JsonProperty(namespace = "preview_attach_id")
    private String previewAttachId;
    private String title;
    private Double duration;
}
