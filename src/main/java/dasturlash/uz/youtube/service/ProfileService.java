package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.ChangePasswordDTO;
import dasturlash.uz.youtube.dto.CreateProfileDTO;
import dasturlash.uz.youtube.dto.ProfileDetailDTO;
import dasturlash.uz.youtube.dto.UpdateProfileDetailDTO;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.ProfileRoleEntity;
import dasturlash.uz.youtube.enums.ProfileRoleEnum;
import dasturlash.uz.youtube.enums.ProfileStatus;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.ProfileRoleRepository;
import dasturlash.uz.youtube.util.JwtUtil;
import dasturlash.uz.youtube.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailHistoryService emailHistoryService;
    private final JwtUtil jwtUtil;

    @Value("${app.url}")
    private String serverUrl;

    // 1. Change Password
    public String changePassword(ChangePasswordDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        ProfileEntity profile = getById(profileId);

        if (!passwordEncoder.matches(dto.getOldPassword(), profile.getPassword())) {
            throw new AppBadException("Old password is incorrect");
        }

        if (dto.getOldPassword().equals(dto.getNewPassword())) {
            throw new AppBadException("New password must be different from old password");
        }

        profile.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        profileRepository.save(profile);

        return "Password changed successfully";
    }

    // 2. Update Email — Attach dan keyin qilinadi
    // ...

    // 3. Update Profile Detail
    public ProfileDetailDTO updateDetail(UpdateProfileDetailDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        ProfileEntity profile = getById(profileId);

        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profileRepository.save(profile);

        return toDTO(profile);
    }

    // 4. Update Photo — Attach API dan keyin qilinadi
    // ...

    // 5. Get Profile Detail
    public ProfileDetailDTO getDetail() {
        Integer profileId = SecurityUtil.getProfileId();
        ProfileEntity profile = getById(profileId);
        return toDTO(profile);
    }

    // 6. Create Profile (ADMIN)
    @Transactional
    public ProfileDetailDTO create(CreateProfileDTO dto) {
        // Email tekshirish
        if (profileRepository.existsByEmailAndVisibleTrue(dto.getEmail())) {
            throw new AppBadException("Email already exists");
        }

        // Profile yaratish
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setEmail(dto.getEmail());
        profile.setPassword(passwordEncoder.encode(dto.getPassword()));
        profile.setStatus(ProfileStatus.ACTIVE); // ADMIN yaratsa — to'g'ridan ACTIVE
        profile.setVisible(true);
        profileRepository.save(profile);

        // Role saqlash
        ProfileRoleEntity role = new ProfileRoleEntity();
        role.setProfileId(profile.getId());
        role.setRoles(dto.getRole() != null ? dto.getRole() : ProfileRoleEnum.USER);
        profileRoleRepository.save(role);

        return toDTO(profile);
    }

    // Helper
    private ProfileEntity getById(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Profile not found"));
    }

    private ProfileDetailDTO toDTO(ProfileEntity profile) {
        return ProfileDetailDTO.builder()
                .id(profile.getId())
                .name(profile.getName())
                .surname(profile.getSurname())
                .email(profile.getEmail())
                .photoUrl(profile.getPhotoId() != null
                        ? serverUrl + "/attach/open/" + profile.getPhotoId()
                        : null)
                .build();
    }
}