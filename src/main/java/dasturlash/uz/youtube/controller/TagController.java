package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.tag.CreateTagDTO;
import dasturlash.uz.youtube.dto.tag.TagDTO;
import dasturlash.uz.youtube.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // 1. Create
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TagDTO> create(@Valid @RequestBody CreateTagDTO dto) {
        return ResponseEntity.ok(tagService.create(dto));
    }

    // 2. Update (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> update(@PathVariable Integer id,
                                         @Valid @RequestBody CreateTagDTO dto) {
        return ResponseEntity.ok(tagService.update(id, dto));
    }

    // 3. Delete (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(tagService.delete(id));
    }

    // 4. List
    @GetMapping
    public ResponseEntity<List<TagDTO>> list() {
        return ResponseEntity.ok(tagService.list());
    }
}