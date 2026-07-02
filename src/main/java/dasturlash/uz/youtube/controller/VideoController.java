package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.video.*;
import dasturlash.uz.youtube.enums.VideoStatusEnum;
import dasturlash.uz.youtube.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    // 1. Create (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<VideoFullInfo> create(@Valid @RequestBody CreateVideoDTO dto) {
        return ResponseEntity.ok(videoService.create(dto));
    }

    // 2. Update Detail (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<VideoFullInfo> update(@PathVariable String id,
                                                @Valid @RequestBody UpdateVideoDTO dto) {
        return ResponseEntity.ok(videoService.update(id, dto));
    }

    // 3. Change Status (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<VideoFullInfo> changeStatus(@PathVariable String id,
                                                      @RequestParam VideoStatusEnum status) {
        return ResponseEntity.ok(videoService.changeStatus(id, status));
    }

    // 4. Increase view count
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> increaseView(@PathVariable String id) {
        videoService.increaseView(id);
        return ResponseEntity.ok().build();
    }

    // 5. Pagination by CategoryId
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<VideoShortInfo>> paginationByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.paginationByCategory(categoryId, page, size));
    }

    // 6. Search by Title
    @GetMapping("/search")
    public ResponseEntity<Page<VideoShortInfo>> search(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.searchByTitle(title, page, size));
    }

    // 7. By tag_id pagination
    @GetMapping("/by-tag/{tagId}")
    public ResponseEntity<Page<VideoShortInfo>> byTag(
            @PathVariable Integer tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.getByTagId(tagId, page, size));
    }

    // 8. Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<VideoFullInfo> getById(@PathVariable String id) {
        return ResponseEntity.ok(videoService.getById(id));
    }

    // 9. List Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<VideoShortInfo>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.pagination(page, size));
    }

    // 10. Channel Video List
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<Page<VideoPlayListInfo>> channelVideos(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.getChannelVideos(channelId, page, size));
    }
}