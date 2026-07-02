package dasturlash.uz.youtube.dto.playlist;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
public class PlaylistDetailInfo {
    private Long id;
    private String name;
    private Long videoCount;
    private Long totalViewCount;
    private LocalDateTime lastUpdateDate;
}