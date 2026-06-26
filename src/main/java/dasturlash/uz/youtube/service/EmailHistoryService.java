package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.auth.MailMessageDTO;
import dasturlash.uz.youtube.entity.EmailHistoryEntity;
import dasturlash.uz.youtube.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailHistoryService {

    @Autowired
    private EmailHistoryRepository historyRepository;

    public void create(MailMessageDTO dto, String code) {
        EmailHistoryEntity history = new EmailHistoryEntity();
        history.setToEmail(dto.getToAccount());
        history.setTitle(dto.getSubject());
        history.setMessage(dto.getBody());
        history.setCode(code);

        historyRepository.save(history);
    }

    public int getCountAfter(String toAccount, LocalDateTime from) {
        return historyRepository.countByToEmailAfter(toAccount, from);
    }

}
