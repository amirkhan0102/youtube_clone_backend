package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.commentlike.CommentLikeDTO;
import dasturlash.uz.youtube.dto.commentlike.CommentLikeInfo;
import dasturlash.uz.youtube.service.CommentLikeService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment-like")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 1. Create Like
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<String> createLike(@Valid @RequestBody CommentLikeDTO dto) {
        return ResponseEntity.ok(commentLikeService.createLike(dto));
    }

    // 2. Remove Like
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> removeLike(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentLikeService.removeLike(commentId));
    }

    // 3. My Liked Comments (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<Page<CommentLikeInfo>> myLikedComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentLikeService.myLikedComments(page, size));
    }

    // 4. By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Page<CommentLikeInfo>> getLikedByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentLikeService.getLikedByUserId(userId, page, size));
    }
}