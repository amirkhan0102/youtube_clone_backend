package dasturlash.uz.youtube.dto.email_history;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EmailHistoryDTO {
    private Long id;
    private String toEmail;
    private String title;
    private String message;
    private LocalDateTime createdDate;
}