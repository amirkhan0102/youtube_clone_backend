package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.tag.request.TagRequestDTO;
import dasturlash.uz.youtube.dto.tag.request.TagUpdateRequestDTO;
import dasturlash.uz.youtube.dto.tag.responce.TagResponseDTO;
import dasturlash.uz.youtube.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<TagResponseDTO> create(@RequestBody TagRequestDTO dto) {
        return ResponseEntity.ok(tagService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<TagResponseDTO> update(@RequestBody TagUpdateRequestDTO dto) {
        return ResponseEntity.ok(tagService.update(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(tagService.delete(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<TagResponseDTO>> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }
}
