package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.playlist.*;
import dasturlash.uz.youtube.enums.PlaylistStatusEnum;
import dasturlash.uz.youtube.service.PlaylistService;
import jakarta.validation.*;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    // 1. Create (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<PlaylistInfo> create(@Valid @RequestBody CreatePlaylistDTO dto) {
        return ResponseEntity.ok(playlistService.create(dto));
    }

    // 2. Update (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistInfo> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdatePlaylistDTO dto) {
        return ResponseEntity.ok(playlistService.update(id, dto));
    }

    // 3. Change Status (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PlaylistInfo> changeStatus(@PathVariable Long id,
                                                     @RequestParam PlaylistStatusEnum status) {
        return ResponseEntity.ok(playlistService.changeStatus(id, status));
    }

    // 4. Delete (USER and OWNER, ADMIN)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.delete(id));
    }

    // 5. Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<PlaylistInfo>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(playlistService.pagination(page, size));
    }

    // 6. List By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<PlaylistInfo>> listByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(playlistService.listByUserId(userId));
    }

    // 7. My Playlists (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<PlayListShortInfo>> myPlaylists() {
        return ResponseEntity.ok(playlistService.myPlaylists());
    }

    // 8. Get Channel Playlists (Public)
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<PlayListShortInfo>> getChannelPlaylists(
            @PathVariable String channelId) {
        return ResponseEntity.ok(playlistService.getChannelPlaylists(channelId));
    }

    // 9. Get Playlist Detail
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDetailInfo> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getDetail(id));
    }
}