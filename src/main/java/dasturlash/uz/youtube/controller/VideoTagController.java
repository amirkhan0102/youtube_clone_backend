package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.videotag.CreateVideoTagDTO;
import dasturlash.uz.youtube.dto.videotag.VideoTagDTO;
import dasturlash.uz.youtube.service.VideoTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/video-tag")
@RequiredArgsConstructor
public class VideoTagController {

    private final VideoTagService videoTagService;

    // 1. Add tag
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<VideoTagDTO> addTag(@Valid @RequestBody CreateVideoTagDTO dto) {
        return ResponseEntity.ok(videoTagService.addTag(dto));
    }

    // 2. Delete tag
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteTag(@Valid @RequestBody CreateVideoTagDTO dto) {
        return ResponseEntity.ok(videoTagService.deleteTag(dto));
    }

    // 3. Get by videoId
    @GetMapping("/{videoId}")
    public ResponseEntity<List<VideoTagDTO>> getByVideoId(@PathVariable String videoId) {
        return ResponseEntity.ok(videoTagService.getByVideoId(videoId));
    }
}