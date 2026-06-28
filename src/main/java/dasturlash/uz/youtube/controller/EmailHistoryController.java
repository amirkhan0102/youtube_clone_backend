package dasturlash.uz.youtube.controller;


import dasturlash.uz.youtube.dto.email_history.EmailHistoryDTO;
import dasturlash.uz.youtube.service.EmailHistoryAdminService;
import dasturlash.uz.youtube.service.EmailHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/email-history")
@RequiredArgsConstructor
public class EmailHistoryController {


    private final EmailHistoryAdminService emailHistoryAdminService;
    // 1 ALL pagination

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<EmailHistoryDTO>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(emailHistoryAdminService.pagination(page, size));
    }

    //2 BY EMAIL PAGINATION

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-email")
    public ResponseEntity<Page<EmailHistoryDTO>> byEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(emailHistoryAdminService.paginationByEmail(email, page, size));
    }


    // 3 FILTER
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Page<EmailHistoryDTO>> filter(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(emailHistoryAdminService.filter(email, from, to, page, size));
    }

}
