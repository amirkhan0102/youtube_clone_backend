package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.videolike.VideoLikeDTO;
import dasturlash.uz.youtube.dto.videolike.VideoLikeInfo;
import dasturlash.uz.youtube.service.VideoLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video-like")
@RequiredArgsConstructor
public class VideoLikeController {

    private final VideoLikeService videoLikeService;

    // 1. Create Like/Dislike
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<String> createLike(@Valid @RequestBody VideoLikeDTO dto) {
        return ResponseEntity.ok(videoLikeService.createLike(dto));
    }

    // 2. Remove Like
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Boolean> removeLike(@PathVariable String videoId) {
        return ResponseEntity.ok(videoLikeService.removeLike(videoId));
    }

    // 3. My Liked Videos (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<Page<VideoLikeInfo>> myLikedVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoLikeService.myLikedVideos(page, size));
    }

    // 4. By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Page<VideoLikeInfo>> getLikedByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoLikeService.getLikedByUserId(userId, page, size));
    }
}