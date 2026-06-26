package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.config.CustomUserDetails;
import dasturlash.uz.youtube.dto.auth.LoginDTO;
import dasturlash.uz.youtube.dto.auth.RegisterDTO;
import dasturlash.uz.youtube.dto.auth.VerificationDTO;
import dasturlash.uz.youtube.dto.profile.ProfileResponseDTO;
import dasturlash.uz.youtube.dto.security.JwtDTO;
import dasturlash.uz.youtube.entity.EmailHistoryEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.VerificationAttemptEntity;
import dasturlash.uz.youtube.enums.ProfileRoleEnum;
import dasturlash.uz.youtube.enums.ProfileStatusEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.exception.ItemNotFoundException;
import dasturlash.uz.youtube.repository.EmailHistoryRepository;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.VerificationAttemptRepository;
import dasturlash.uz.youtube.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private VerificationAttemptRepository attemptRepository;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    public String register(RegisterDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if(optional.isPresent()){
            if(optional.get().getStatus().equals(ProfileStatusEnum.IN_REGISTRATION)){
                profileRepository.delete(optional.get());
            }
            else {
                throw new AppBadException("User already exists");
            }
        }
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setEmail(dto.getEmail());
        profile.setPassword(passwordEncoder.encode(dto.getPassword()));
        profile.setRole(ProfileRoleEnum.ROLE_USER);
        profile.setStatus(ProfileStatusEnum.IN_REGISTRATION);

        profileRepository.save(profile);

        mailSenderService.verificationCode(profile.getEmail());

        VerificationAttemptEntity attempt = new VerificationAttemptEntity();
        attempt.setEmail(dto.getEmail());
        attempt.setAttemptCount(0);
        attempt.setLastAttempt(LocalDateTime.now());
        attempt.setLastResendTime(LocalDateTime.now());
        attemptRepository.save(attempt);

        return "Code sent to your email. Please check email";
    }

    @Transactional
    public String verify(VerificationDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        VerificationAttemptEntity attempt = attemptRepository.findByEmail(dto.getEmail());
        if(attempt == null){
            VerificationAttemptEntity verificationAttempt = new VerificationAttemptEntity();
            verificationAttempt.setEmail(dto.getEmail());
            verificationAttempt.setAttemptCount(0);
            attempt = attemptRepository.save(verificationAttempt);
        }

        if(attempt.getAttemptCount() >= 5 && attempt.getLastAttempt() != null){
            LocalDateTime expiryTime = attempt.getLastAttempt().plusMinutes(2);
            if(expiryTime.isAfter(now)){
                throw new AppBadException("Too many attempt. Please wait 2 minutes");
            }
            else{
                attempt.setAttemptCount(0);
            }
        }

        EmailHistoryEntity lastCode = emailHistoryRepository.findTopByToEmailOrderByCreatedDateDesc(dto.getEmail());
        if(lastCode != null && lastCode.getCreatedDate().plusMinutes(2).isBefore(now)){
            throw new AppBadException("Code is expired. Please click resend to get new code");
        }
        if(lastCode != null && !lastCode.getCode().equals(dto.getCode())){
            attempt.setAttemptCount(attempt.getAttemptCount()+1);
            attempt.setLastAttempt(now);
            attemptRepository.save(attempt);

            int remaining = 5 - attempt.getAttemptCount();
            throw new AppBadException("Wrong code: " + (remaining > 0 ? remaining + "-attempt left" : "Please try 2 minutes later"));
        }

        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if(optional.isEmpty()) {
            throw new ItemNotFoundException("User not found");
        }
        ProfileEntity profile = optional.get();
        profile.setStatus(ProfileStatusEnum.ACTIVE);
        profileRepository.save(profile);

        attemptRepository.delete(attempt);

        return "Successfully activated. You can login by now";
    }

    public ProfileResponseDTO login(LoginDTO login) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
            );

            if (authentication.isAuthenticated()) {

                if (authentication.getPrincipal() instanceof CustomUserDetails user) {
                    String jwt = JwtUtil.encode(new JwtDTO(
                            user.getId(),
                            user.getEmail(),
                            user.getRole().name()
                    ));

                    ProfileResponseDTO response = new ProfileResponseDTO();
                    response.setName(user.getName());
                    response.setSurname(user.getSurname());
                    response.setUsername(user.getUsername());
                    response.setRole(user.getRole());
                    response.setJwt(jwt);
                    return response;
                }
            }
        } catch (BadCredentialsException e) {
            throw new AppBadException("Username or password wrong");
        } catch (DisabledException e) {
            throw new AppBadException("This user is not active");
        }
        throw new AppBadException("Username or password wrong");
    }
}
