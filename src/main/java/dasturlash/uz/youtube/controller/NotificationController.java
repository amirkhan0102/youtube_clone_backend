package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.notification.NotificationFilterDTO;
import dasturlash.uz.youtube.dto.notification.NotificationInfo;
import dasturlash.uz.youtube.dto.notification.SendNotificationDTO;
import dasturlash.uz.youtube.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 1. Filter with pagination
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<Page<NotificationInfo>> filter(
            @RequestBody NotificationFilterDTO dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(notificationService.filter(dto, page, size));
    }

    // 2. Send Notification
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send")
    public ResponseEntity<NotificationInfo> send(
            @Valid @RequestBody SendNotificationDTO dto) {
        return ResponseEntity.ok(notificationService.send(dto));
    }
}