package dasturlash.uz.youtube.config;

import dasturlash.uz.youtube.enums.ProfileRoleEnum;
import dasturlash.uz.youtube.enums.ProfileStatusEnum;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private Integer id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private ProfileStatusEnum status;
    private ProfileRoleEnum role;

    public CustomUserDetails(Integer id, String username, String password,
                             String name, String surname,
                             ProfileStatusEnum status,
                             ProfileRoleEnum role) {
        this.id = id;
        this.email = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.role = role;
    }

    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == ProfileStatusEnum.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
