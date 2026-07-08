package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.comment.CommentAdminInfo;
import dasturlash.uz.youtube.dto.comment.CommentInfo;
import dasturlash.uz.youtube.dto.comment.CreateCommentDTO;
import dasturlash.uz.youtube.dto.comment.UpdateCommentDTO;
import dasturlash.uz.youtube.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. Create (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<CommentInfo> create(@Valid @RequestBody CreateCommentDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    // 2. Update (USER AND OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentInfo> update(@PathVariable Long id,
                                              @Valid @RequestBody UpdateCommentDTO dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    // 3. Delete (USER AND OWNER, ADMIN)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.delete(id));
    }

    // 4. Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<CommentAdminInfo>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.pagination(page, size));
    }

    // 5. By profileId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-profile/{profileId}")
    public ResponseEntity<Page<CommentAdminInfo>> listByProfileId(
            @PathVariable Integer profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.listByProfileId(profileId, page, size));
    }

    // 6. My Comments (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<Page<CommentAdminInfo>> myComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.myComments(page, size));
    }

    // 7. By videoId
    @GetMapping("/by-video/{videoId}")
    public ResponseEntity<Page<CommentInfo>> listByVideoId(
            @PathVariable String videoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.listByVideoId(videoId, page, size));
    }

    // 8. Replies by commentId
    @GetMapping("/replies/{commentId}")
    public ResponseEntity<Page<CommentInfo>> getReplies(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getReplies(commentId, page, size));
    }
}