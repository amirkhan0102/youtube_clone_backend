package dasturlash.uz.youtube.dto;

import lombok.*;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileDetailDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String photoUrl;
}