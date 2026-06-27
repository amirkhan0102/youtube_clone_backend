package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.auth.AuthResponseDTO;
import dasturlash.uz.youtube.dto.auth.LoginDTO;
import dasturlash.uz.youtube.dto.auth.RegistrationDTO;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.ProfileRoleEntity;
import dasturlash.uz.youtube.enums.ProfileRoleEnum;
import dasturlash.uz.youtube.enums.ProfileStatus;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.ProfileRoleRepository;
import dasturlash.uz.youtube.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final ProfileRoleRepository profileRoleRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final EmailHistoryService emailHistoryService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Value("${app.url}")
    private String appUrl;


    @Transactional
    public String registration(RegistrationDTO dto) {

        Optional<ProfileEntity> optional =
                profileRepository.findByEmailAndVisibleTrue(dto.getEmail());

        if (optional.isPresent()) {

            ProfileEntity profile = optional.get();

            if (profile.getStatus() == ProfileStatus.ACTIVE) {
                throw new AppBadException("Email already exists");
            }

            if (profile.getStatus() == ProfileStatus.NOT_ACTIVE) {

                emailHistoryService.invalidateAllTokens(profile.getEmail());

                String token = jwtUtil.createVerificationToken(profile.getEmail());

                emailHistoryService.create(profile.getEmail(), token);

                emailService.sendRegistrationEmail(
                        profile.getEmail(),
                        profile.getName(),
                        buildVerificationLink(token)
                );

                return "Verification email sent again.";
            }
        }

        ProfileEntity profile = createProfile(dto);

        saveUserRole(profile);

        emailHistoryService.invalidateAllTokens(profile.getEmail());

        String token = jwtUtil.createVerificationToken(profile.getEmail());

        emailHistoryService.create(profile.getEmail(), token);

        emailService.sendRegistrationEmail(
                profile.getEmail(),
                profile.getName(),
                buildVerificationLink(token)
        );

        return buildVerificationLink(token);
    }

    private void checkEmail(String email) {
        if (profileRepository.existsByEmailAndVisibleTrue(email)) {
            throw new AppBadException("Email already exists");
        }
    }



    @Transactional
    public String verifyEmail(String token) {

        String email;

        try {
            email = jwtUtil.getEmail(token);
        } catch (Exception e) {
            throw new AppBadException("Verification link is invalid or expired.");
        }

        ProfileEntity profile = profileRepository
                .findByEmailAndVisibleTrue(email)
                .orElseThrow(() -> new AppBadException("Profile not found."));

        if (profile.getStatus() == ProfileStatus.ACTIVE) {
            return "Email already verified.";
        }

        if (!emailHistoryService.exists(email, token)) {
            throw new AppBadException("Verification link is invalid.");
        }

        profileRepository.updateStatus(email, ProfileStatus.ACTIVE);

        emailHistoryService.markAsVerified(token);

        return "Email verified successfully.";
    }


    public AuthResponseDTO login(LoginDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (DisabledException e) {
            throw new AppBadException("Email not verified. Please check your inbox.");
        } catch (BadCredentialsException e) {
            throw new AppBadException("Email or password is incorrect.");
        }

        ProfileEntity profile = profileRepository.findByEmailWithRoles(dto.getEmail())
                .orElseThrow(() -> new AppBadException("Profile not found"));

        String token = jwtUtil.createAccessToken(profile);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .build();
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


    private String buildVerificationLink(String token) {
        return appUrl + "/auth/verification/" + token;
    }

}