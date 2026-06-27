package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.entity.EmailHistoryEntity;
import dasturlash.uz.youtube.entity.EmailVerificationEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.EmailHistoryRepository;
import dasturlash.uz.youtube.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {

    private final EmailHistoryRepository emailHistoryRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    // Verification token saqlash
    public void create(String email, String token) {
            EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setEmail(email);
        entity.setToken(token);
        entity.setVerified(false);
        emailVerificationRepository.save(entity);
    }

    // Token mavjudligini tekshirish
    public boolean exists(String email, String token) {
        return emailVerificationRepository
                .existsByEmailAndTokenAndVerifiedFalse(email, token);
    }

    // Tokenni verified qilish
    public void markAsVerified(String token) {
        EmailVerificationEntity entity = emailVerificationRepository
                .findByToken(token)
                .orElseThrow(() -> new AppBadException("Token not found"));
        entity.setVerified(true);
        emailVerificationRepository.save(entity);
    }

    // Eski tokenlarni bekor qilish
    public void invalidateAllTokens(String email) {
        emailVerificationRepository.invalidateAllTokens(email);
    }

    // Email history ga log yozish
    public void saveHistory(String toEmail, String title, String message) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setToEmail(toEmail);
        entity.setTitle(title);
        entity.setMessage(message);
        emailHistoryRepository.save(entity);
    }
}