package dasturlash.uz.youtube.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    /**
     * Hozircha ochiq API lar.
     * Keyinchalik yangi endpointlar qo'shilsa shu yerga qo'shiladi.
     */
    public static final String[] OPEN_API = {
            "/auth/registration",
            "/auth/login",
            "/error"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(OPEN_API).permitAll()
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}