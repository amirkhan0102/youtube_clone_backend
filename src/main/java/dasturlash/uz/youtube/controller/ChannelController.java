package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.channel.ChannelDTO;
import dasturlash.uz.youtube.dto.channel.CreateChannelDTO;
import dasturlash.uz.youtube.dto.channel.UpdateChannelDTO;
import dasturlash.uz.youtube.enums.ChannelStatusEnum;
import dasturlash.uz.youtube.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    // 1. Create (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ChannelDTO> create(@Valid @RequestBody CreateChannelDTO dto) {
        return ResponseEntity.ok(channelService.create(dto));
    }

    // 2. Update (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelDTO> update(@PathVariable String channelId,
                                             @Valid @RequestBody UpdateChannelDTO dto) {
        return ResponseEntity.ok(channelService.update(channelId, dto));
    }

    // 3. Update Photo (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{channelId}/photo")
    public ResponseEntity<ChannelDTO> updatePhoto(@PathVariable String channelId,
                                                  @RequestParam String attachId) {
        return ResponseEntity.ok(channelService.updatePhoto(channelId, attachId));
    }

    // 4. Update Banner (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{channelId}/banner")
    public ResponseEntity<ChannelDTO> updateBanner(@PathVariable String channelId,
                                                   @RequestParam String attachId) {
        return ResponseEntity.ok(channelService.updateBanner(channelId, attachId));
    }

    // 5. Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<ChannelDTO>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(channelService.pagination(page, size));
    }

    // 6. Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<ChannelDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(channelService.getById(id));
    }

    // 7. Change Status (ADMIN, USER and OWNER)
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{channelId}/status")
    public ResponseEntity<ChannelDTO> changeStatus(@PathVariable String channelId,
                                                   @RequestParam ChannelStatusEnum status) {
        return ResponseEntity.ok(channelService.changeStatus(channelId, status));
    }

    // 8. My Channels (USER)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<ChannelDTO>> myChannels() {
        return ResponseEntity.ok(channelService.myChannels());
    }
}