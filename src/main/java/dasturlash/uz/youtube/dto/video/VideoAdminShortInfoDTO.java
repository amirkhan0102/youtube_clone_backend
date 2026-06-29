package dasturlash.uz.youtube.dto.video;

import dasturlash.uz.youtube.dto.playlist.PlaylistShortDTO;
import dasturlash.uz.youtube.dto.profile.ProfileShortInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoAdminShortInfoDTO {
    private VideoShortInfoDTO shortInfo;
    private ProfileShortInfoDTO profileInfo;
    private PlaylistShortDTO playlistInfo;
}
