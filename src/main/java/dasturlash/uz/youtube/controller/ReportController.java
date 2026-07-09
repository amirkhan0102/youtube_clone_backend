package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.report.CreateReportDTO;
import dasturlash.uz.youtube.dto.report.ReportInfo;
import dasturlash.uz.youtube.service.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 1. Create (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ReportInfo> create(@Valid @RequestBody CreateReportDTO dto) {
        return ResponseEntity.ok(reportService.create(dto));
    }

    // 2. Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<ReportInfo>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reportService.pagination(page, size));
    }

    // 3. Delete (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.delete(id));
    }

    // 4. By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Page<ReportInfo>> listByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reportService.listByUserId(userId, page, size));
    }
}