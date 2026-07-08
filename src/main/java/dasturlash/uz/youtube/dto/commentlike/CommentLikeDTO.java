package dasturlash.uz.youtube.dto.commentlike;


import dasturlash.uz.youtube.enums.LikeTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentLikeDTO {

    @NotNull
    private Long commentId;

    @NotNull
    private LikeTypeEnum type;


}
