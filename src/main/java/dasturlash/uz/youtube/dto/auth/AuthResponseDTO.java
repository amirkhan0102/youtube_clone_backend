package dasturlash.uz.youtube.dto.auth;


import dasturlash.uz.youtube.entity.ProfileRoleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private ProfileRoleEntity role;
}
