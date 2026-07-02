package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.notification.NotificationFilterDTO;
import dasturlash.uz.youtube.dto.notification.NotificationRequestDTO;
import dasturlash.uz.youtube.dto.notification.NotificationResponseDTO;
import dasturlash.uz.youtube.service.NotificationService;
import dasturlash.uz.youtube.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> send(@RequestBody NotificationRequestDTO dto){
        return ResponseEntity.ok(notificationService.send(dto));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<NotificationResponseDTO>> filter(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestBody NotificationFilterDTO dto
    ){
        return ResponseEntity.ok(notificationService.filter(dto, PageUtil.page(page),size));
    }
}
