package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.playlistvideo.CreatePlaylistVideoDTO;
import dasturlash.uz.youtube.dto.playlistvideo.DeletePlaylistVideoDTO;
import dasturlash.uz.youtube.dto.playlistvideo.PlaylistVideoInfo;
import dasturlash.uz.youtube.dto.playlistvideo.UpdatePlaylistVideoDTO;
import dasturlash.uz.youtube.service.PlaylistVideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist-video")
@RequiredArgsConstructor
public class PlaylistVideoController {

    private final PlaylistVideoService playlistVideoService;

    // 1. Create (User and Owner)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<PlaylistVideoInfo> create(
            @Valid @RequestBody CreatePlaylistVideoDTO dto) {
        return ResponseEntity.ok(playlistVideoService.create(dto));
    }

    // 2. Update
    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public ResponseEntity<PlaylistVideoInfo> update(
            @Valid @RequestBody UpdatePlaylistVideoDTO dto) {
        return ResponseEntity.ok(playlistVideoService.update(dto));
    }

    // 3. Delete
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<Boolean> delete(@Valid @RequestBody DeletePlaylistVideoDTO dto) {
        return ResponseEntity.ok(playlistVideoService.delete(dto));
    }

    // 4. Get by playlistId
    @GetMapping("/{playlistId}")
    public ResponseEntity<List<PlaylistVideoInfo>> getByPlaylistId(
            @PathVariable Long playlistId) {
        return ResponseEntity.ok(playlistVideoService.getByPlaylistId(playlistId));
    }
}