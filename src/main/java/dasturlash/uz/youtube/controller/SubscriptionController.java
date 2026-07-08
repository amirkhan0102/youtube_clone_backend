package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.subsicripton.*;
import dasturlash.uz.youtube.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 1. Create (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<SubscriptionInfo> create(
            @Valid @RequestBody CreateSubscriptionDTO dto) {
        return ResponseEntity.ok(subscriptionService.create(dto));
    }

    // 2. Change Status (USER)
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/status")
    public ResponseEntity<SubscriptionInfo> changeStatus(
            @Valid @RequestBody ChangeSubscriptionStatusDTO dto) {
        return ResponseEntity.ok(subscriptionService.changeStatus(dto));
    }

    // 3. Change Notification Type (USER)
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/notification-type")
    public ResponseEntity<SubscriptionInfo> changeNotificationType(
            @Valid @RequestBody ChangeNotificationTypeDTO dto) {
        return ResponseEntity.ok(subscriptionService.changeNotificationType(dto));
    }

    // 4. My Subscriptions (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<SubscriptionInfo>> mySubscriptions() {
        return ResponseEntity.ok(subscriptionService.mySubscriptions());
    }

    // 5. By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<SubscriptionAdminInfo>> getByUserId(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(subscriptionService.getByUserId(userId));
    }
}