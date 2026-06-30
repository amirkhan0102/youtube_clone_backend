package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.commentLike.request.CommentLikeReqDto;
import dasturlash.uz.youtube.dto.commentLike.response.CommentLikeInfoResDto;
import dasturlash.uz.youtube.dto.commentLike.response.CommentLikeResDto;
import dasturlash.uz.youtube.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment-like")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/create")
    public ResponseEntity<CommentLikeResDto> create(@RequestBody CommentLikeReqDto dto) {
        return ResponseEntity.ok(commentLikeService.create(dto));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Boolean> remove(@RequestBody CommentLikeReqDto dto) {
        return ResponseEntity.ok(commentLikeService.remove(dto));
    }

    @GetMapping("/list/user-liked")
    public ResponseEntity<List<CommentLikeInfoResDto>> userLikedList() {
        return ResponseEntity.ok(commentLikeService.userLikedList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list/user-liked/{profileId}")
    public ResponseEntity<List<CommentLikeInfoResDto>> userLikedByPrId(@PathVariable Integer profileId) {
        return ResponseEntity.ok(commentLikeService.userLikedListByPrId(profileId));
    }
}
