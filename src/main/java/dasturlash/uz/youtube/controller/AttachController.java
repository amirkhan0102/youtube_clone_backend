package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.attach.AttachPaginationDTO;
import dasturlash.uz.youtube.dto.attach.AttachResponseDTO;
import dasturlash.uz.youtube.service.AttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    // 1. Upload
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public ResponseEntity<AttachResponseDTO> upload(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }

    // 2. Open (brauzerda ko'rish)
    @GetMapping("/open/{id}")
    public ResponseEntity<byte[]> open(@PathVariable String id) {
        byte[] data = attachService.open(id);
        String contentType = attachService.getContentType(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(data);
    }

    // 3. Download
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        Resource resource = attachService.download(id);
        String originalName = attachService.getOriginalName(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + originalName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
    }

    // 4. Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<AttachPaginationDTO>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(attachService.pagination(page, size));
    }

    // 5. Delete (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(attachService.delete(id));
    }
}