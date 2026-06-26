package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.auth.RegistrationDTO;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.ProfileRoleEntity;
import dasturlash.uz.youtube.enums.ProfileRoleEnum;
import dasturlash.uz.youtube.enums.ProfileStatus;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.ProfileRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;

//    @Autowired
private final PasswordEncoder passwordEncoder;


    @Transactional
    public void registration(RegistrationDTO dto) {

        checkEmail(dto.getEmail());

        ProfileEntity profile = createProfile(dto);

        saveUserRole(profile);

        // TODO Email Verification
    }

    private void checkEmail(String email) {
        if (profileRepository.existsByEmailAndVisibleTrue(email)) {
            throw new AppBadException("Email already exists");
        }
    }

    private ProfileEntity createProfile(RegistrationDTO dto) {

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        entity.setVisible(true);

        return profileRepository.save(entity);
    }

    private void saveUserRole(ProfileEntity profile) {

        ProfileRoleEntity role = new ProfileRoleEntity();
        role.setProfileId(profile.getId());
        role.setRoles(ProfileRoleEnum.USER);

        profileRoleRepository.save(role);
    }

}