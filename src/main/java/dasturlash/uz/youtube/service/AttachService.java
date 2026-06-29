package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.attach.AttachPaginationDTO;
import dasturlash.uz.youtube.dto.attach.AttachResponseDTO;
import dasturlash.uz.youtube.entity.AttachEntity;
import dasturlash.uz.youtube.exception.ItemNotFoundException;
import dasturlash.uz.youtube.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttachService {

    private final AttachRepository attachRepository;

    @Value("${attach.upload.folder}")
    private String uploadFolder;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.url}")
    private String attachUrl;

    // 1. Upload
    public AttachResponseDTO upload(MultipartFile file) {
        try {
            String originName = file.getOriginalFilename();
            String extension = getExtension(originName);
            String folder = getDateFolder();

            // Papka yaratish
            File dir = new File(uploadFolder + folder);
            if (!dir.exists()) dir.mkdirs();

            // Entity yaratish
            AttachEntity entity = new AttachEntity();
            entity.setOriginName(originName);
            entity.setSize(file.getSize());
            entity.setType(extension);
            entity.setPath(folder + "/" + entity.getId() + "." + extension);
            entity.setVisible(true);

            // Faylni saqlash
            File dest = new File(uploadFolder + entity.getPath());
            file.transferTo(dest);

            attachRepository.save(entity);

            return toResponseDTO(entity);

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    // 2. Open (brauzerda ko'rish)
    public byte[] open(String id) {
        AttachEntity entity = getById(id);
        try {
            Path path = Paths.get(uploadFolder + entity.getPath());
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }

    // Content type aniqlash
    public String getContentType(String id) {
        AttachEntity entity = getById(id);
        String type = entity.getType().toLowerCase();
        return switch (type) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "mp4" -> "video/mp4";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }

    // 3. Download
    public Resource download(String id) {
        AttachEntity entity = getById(id);
        try {
            Path path = Paths.get(uploadFolder + entity.getPath());
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }
            return resource;
        } catch (Exception e) {
            throw new RuntimeException("File download failed");
        }
    }

    public String getOriginalName(String id) {
        return getById(id).getOriginName();
    }

    // 4. Pagination (ADMIN)
    public Page<AttachPaginationDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return attachRepository.findAllByVisibleTrue(pageable)
                .map(this::toPaginationDTO);
    }

    // 5. Delete (ADMIN)
    public Boolean delete(String id) {
        AttachEntity entity = getById(id);

        // DB dan o'chirish (soft delete)
        entity.setVisible(false);
        attachRepository.save(entity);

        // Diskdan o'chirish
        File file = new File(uploadFolder + entity.getPath());
        if (file.exists()) file.delete();

        return true;
    }

    // Helper metodlar
    public AttachEntity getById(String id) {
        return attachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attach not found: " + id));
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getDateFolder() {
        LocalDate now = LocalDate.now();
        return now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth();
    }

    private AttachResponseDTO toResponseDTO(AttachEntity entity) {
        return AttachResponseDTO.builder()
                .id(entity.getId())
                .originName(entity.getOriginName())
                .size(entity.getSize())
                .type(entity.getType())
                .url(serverUrl + "/attach/open/" + entity.getId())
                .duration(entity.getDuration())
                .build();
    }

    private AttachPaginationDTO toPaginationDTO(AttachEntity entity) {
        return AttachPaginationDTO.builder()
                .id(entity.getId())
                .originName(entity.getOriginName())
                .size(entity.getSize())
                .url(serverUrl + "/attach/open/" + entity.getId())
                .build();
    }

    public AttachEntity findById(String attachId) {
        return attachRepository.findById(attachId)
                .orElseThrow(() -> new ItemNotFoundException("Attach not found"));
    }

    public String openURL(String fileName) {
        return attachUrl + "/api/v1/attach/open/" + fileName;
    }
}