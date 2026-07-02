package dasturlash.uz.youtube.dto.playlist;

import lombok.*;

@Getter @Setter
@Builder
public class ProfileShortInfo {
    private Integer id;
    private String name;
    private String surname;
    private AttachInfo photo;
}