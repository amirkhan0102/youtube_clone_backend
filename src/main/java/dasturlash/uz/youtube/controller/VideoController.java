package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.video.*;
import dasturlash.uz.youtube.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody VideoCreateDTO dto) {
        return ResponseEntity.ok(videoService.create(dto));
    }

    @PutMapping("/update/detail/{videoId}")
    public ResponseEntity<String> updateDetail(@PathVariable String videoId,
                                               @Valid @RequestBody VideoDetailUpdateDTO dto) {
        return ResponseEntity.ok(videoService.updateDetail(videoId, dto));
    }

    @PutMapping("/update/status")
    public ResponseEntity<String> updateStatus(@Valid @RequestBody VideoStatusUpdateDTO dto) {
        return ResponseEntity.ok(videoService.updateStatus(dto));
    }

    @PutMapping("/increase/view-count/{videoId}")
    public ResponseEntity<Integer> increaseViewCount(@PathVariable String videoId) {
        return ResponseEntity.ok(videoService.increaseViewCount(videoId));
    }

    @GetMapping("/by-category-id/{categoryId}")
    public ResponseEntity<Page<VideoShortInfoDTO>> getByCategory(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                 @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                                 @PathVariable Integer categoryId) {
        return ResponseEntity.ok(videoService.getVideosByCategory(categoryId, page - 1, size));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<VideoShortInfoDTO>> search(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                          @RequestBody VideoSearchDTO dto) {
        return ResponseEntity.ok(videoService.search(dto, page - 1, size));
    }

    @PostMapping("/by-tag-id/{tagId}")
    public ResponseEntity<Page<VideoShortInfoDTO>> videosByTag(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                               @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                               @PathVariable Integer tagId) {
        return ResponseEntity.ok(videoService.getVideosByTag(tagId, page - 1, size));
    }

    @GetMapping("/get/by-id/{videoId}")
    public ResponseEntity<VideoFullInfoDTO> getById(@PathVariable String videoId) {
        return ResponseEntity.ok(videoService.getById(videoId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("get/list")
    public ResponseEntity<Page<VideoAdminShortInfoDTO>> getList(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(videoService.getList(page - 1, size));
    }

    @GetMapping("/get/channel-videos/{channelId}")
    public ResponseEntity<Page<VideoPlaylistInfoDTO>> channelVideos(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                                    @PathVariable String channelId) {
        return ResponseEntity.ok(videoService.getChannelVideos(channelId, page-1, size));
    }

}
