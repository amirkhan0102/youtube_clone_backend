package dasturlash.uz.youtube.dto.attach;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AttachResponseDTO {


    private String id;
    private String originName;
    private Long size;
    private String type;
    private String url;
    private Double duration;


}
