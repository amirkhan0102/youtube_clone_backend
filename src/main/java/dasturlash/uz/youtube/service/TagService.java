package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.tag.request.TagRequestDTO;
import dasturlash.uz.youtube.dto.tag.request.TagUpdateRequestDTO;
import dasturlash.uz.youtube.dto.tag.responce.TagResponseDTO;
import dasturlash.uz.youtube.entity.TagEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagResponseDTO create(TagRequestDTO dto){
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        entity.setCreatedDate(LocalDateTime.now());
        return toDtoFromEntity(entity);
    }

    public TagResponseDTO toDtoFromEntity(TagEntity entity){
        TagResponseDTO dto = new TagResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public TagResponseDTO update(TagUpdateRequestDTO dto) {
        Optional<TagEntity> optional = tagRepository.findById(dto.getId());
        if (optional.isEmpty()){
            throw new AppBadException("Tag not found");
        }
        TagEntity entity = optional.get();
        entity.setName(dto.getName());
        entity.setCreatedDate(LocalDateTime.now());
        tagRepository.save(entity);
        return toDtoFromEntity(entity);
    }

    public Boolean delete(Integer id) {
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (optional.isEmpty()){
            throw new AppBadException("Tag not found");
        }
        return Boolean.TRUE;
    }

    public List<TagResponseDTO> getAll() {
        Iterable<TagEntity> iterable = tagRepository.getAllByOrderByCreatedDateDesc();
        List<TagResponseDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity -> dtoList.add(toDtoFromEntity(entity)));
        return dtoList;
    }
}
