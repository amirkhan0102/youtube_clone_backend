package dasturlash.uz.youtube.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailTemplateService emailTemplateService;
    private final EmailHistoryService emailHistoryService;

    public void sendRegistrationEmail(String toEmail,
                                      String name,
                                      String verificationLink) {

        String subject = "Verify your email";
        String html = emailTemplateService.buildRegistrationTemplate(name, verificationLink);

        sendMimeEmail(toEmail, subject, html);

        // History ga saqlash
        emailHistoryService.saveHistory(toEmail, subject, verificationLink);
    }

    private void sendMimeEmail(String toEmail, String subject, String html) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(html, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}