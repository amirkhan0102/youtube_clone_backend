package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.tag.CreateTagDTO;
import dasturlash.uz.youtube.dto.tag.TagDTO;
import dasturlash.uz.youtube.entity.TagEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    // 1. Create
    public TagDTO create(CreateTagDTO dto) {
        if (tagRepository.existsByName(dto.getName())) {
            throw new AppBadException("Tag already exists");
        }
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        tagRepository.save(entity);
        return toDTO(entity);
    }

    // 2. Update (ADMIN)
    public TagDTO update(Integer id, CreateTagDTO dto) {
        TagEntity entity = getById(id);
        entity.setName(dto.getName());
        tagRepository.save(entity);
        return toDTO(entity);
    }

    // 3. Delete (ADMIN)
    public Boolean delete(Integer id) {
        TagEntity entity = getById(id);
        entity.setVisible(false);
        tagRepository.save(entity);
        return true;
    }

    // 4. List
    public List<TagDTO> list() {
        return tagRepository.findAllByVisibleTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TagEntity getById(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Tag not found"));
    }

    private TagDTO toDTO(TagEntity entity) {
        return TagDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}