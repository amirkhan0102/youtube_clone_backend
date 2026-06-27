package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.ChangePasswordDTO;
import dasturlash.uz.youtube.dto.CreateProfileDTO;
import dasturlash.uz.youtube.dto.UpdateProfileDetailDTO;
import dasturlash.uz.youtube.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // 1. Change Password (USER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        return ResponseEntity.ok(profileService.changePassword(dto));
    }

    // 2. Update Email — keyinroq (Attach dan keyin)

    // 3. Update Profile Detail (USER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/detail")
    public ResponseEntity<ProfileDetailDTO> updateDetail(
            @Valid @RequestBody UpdateProfileDetailDTO dto) {
        return ResponseEntity.ok(profileService.updateDetail(dto));
    }

    // 4. Update Photo — keyinroq (Attach dan keyin)

    // 5. Get Profile Detail (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/detail")
    public ResponseEntity<ProfileDetailDTO> getDetail() {
        return ResponseEntity.ok(profileService.getDetail());
    }

    // 6. Create Profile (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<ProfileDetailDTO> create(@Valid @RequestBody CreateProfileDTO dto) {
        return ResponseEntity.ok(profileService.create(dto));
    }
}