package dasturlash.uz.youtube.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponseDTO {

    private String token;

    private String tokenType;

}