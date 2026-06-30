package dasturlash.uz.youtube.util;

import dasturlash.uz.youtube.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

@Component
public class SecurityUtil {

    public static Integer getProfileId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getId();
    }

    public static String getProfileEmail() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    public static List<String> getCurrentProfileRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

//        Collections<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
        return roles;
    }
}