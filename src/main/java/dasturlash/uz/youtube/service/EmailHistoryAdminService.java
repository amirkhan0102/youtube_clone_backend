package dasturlash.uz.youtube.service;


import dasturlash.uz.youtube.dto.email_history.EmailHistoryDTO;
import dasturlash.uz.youtube.entity.EmailHistoryEntity;
import dasturlash.uz.youtube.repository.EmailHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailHistoryAdminService {

    private final EmailHistoryRepository emailHistoryRepository;


    // 1 get all pagination ADMIN only

    public Page<EmailHistoryDTO> pagination(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        return emailHistoryRepository.findAllByOrderByCreatedDateDesc(pageable)
                .map(this::toDTO);
    }


    // 2 Get By Email Pagination

    public Page<EmailHistoryDTO> paginationByEmail(String email, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);

        return emailHistoryRepository
                .findByToEmailOrderByCreatedDateDesc(email, pageable)
                .map(this::toDTO);
    }


    // 3  FILTER BY EMAIL AND DATE

    public Page<EmailHistoryDTO> filter(String email,
                                        LocalDateTime from,
                                        LocalDateTime to,
                                        int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return emailHistoryRepository.findByToEmailAndCreatedDateBetweenOrderByCreatedDateDesc(email, from, to, pageable)
                .map(this::toDTO);
    }


    private EmailHistoryDTO toDTO(EmailHistoryEntity entity) {
        return EmailHistoryDTO.builder()
                .id(entity.getId())
                .toEmail(entity.getToEmail())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .createdDate(entity.getCreatedDate())
                .build();
    }


}
