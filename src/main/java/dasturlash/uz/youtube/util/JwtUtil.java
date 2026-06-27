package dasturlash.uz.youtube.util;

import dasturlash.uz.youtube.entity.ProfileEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.validity}")
    private Long tokenValidity;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Verification token (email uchun)
    public String createVerificationToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Access token (login uchun) — role va id qo'shildi
    public String createAccessToken(ProfileEntity profile) {
        List<String> roles = profile.getRoleList()
                .stream()
                .map(r -> r.getRoles().name())
                .toList();

        return Jwts.builder()
                .subject(profile.getEmail())
                .claim("id", profile.getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Email olish
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Id olish
    public Integer getId(String token) {
        return getClaims(token).get("id", Integer.class);
    }

    // Roles olish
    public List<String> getRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}