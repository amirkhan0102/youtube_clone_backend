package dasturlash.uz.youtube.config;

import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        ProfileEntity profile = profileRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AppBadException("Email not found"));

        return new CustomUserDetails(profile);
    }
}