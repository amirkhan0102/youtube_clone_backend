package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.category.CategoryDTO;
import dasturlash.uz.youtube.dto.category.CreateCategoryDTO;
import dasturlash.uz.youtube.entity.CategoryEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 1. Create (ADMIN)
    public CategoryDTO create(CreateCategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new AppBadException("Category already exists");
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        categoryRepository.save(entity);
        return toDTO(entity);
    }

    // 2. Update (ADMIN)
    public CategoryDTO update(Integer id, CreateCategoryDTO dto) {
        CategoryEntity entity = getById(id);
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        return toDTO(entity);
    }

    // 3. Delete (ADMIN)
    public Boolean delete(Integer id) {
        CategoryEntity entity = getById(id);
        entity.setVisible(false);
        categoryRepository.save(entity);
        return true;
    }

    // 4. List
    public List<CategoryDTO> list() {
        return categoryRepository.findAllByVisibleTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CategoryEntity getById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Category not found"));
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        return CategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}