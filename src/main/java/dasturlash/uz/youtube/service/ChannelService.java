package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.channel.ChannelDTO;
import dasturlash.uz.youtube.dto.channel.CreateChannelDTO;
import dasturlash.uz.youtube.dto.channel.UpdateChannelDTO;
import dasturlash.uz.youtube.entity.AttachEntity;
import dasturlash.uz.youtube.entity.ChannelEntity;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.enums.ChannelStatusEnum;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ChannelRepository;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final AttachService attachService;
    private final ProfileRepository profileRepository;

    @Value("${server.url}")
    private String serverUrl;

    // 1. Create Channel (USER)
    public ChannelDTO create(CreateChannelDTO dto) {

        Integer profileId = SecurityUtil.getProfileId();

        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException("Profile not found"));

        ChannelEntity entity = new ChannelEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(ChannelStatusEnum.ACTIVE);

        entity.setProfile(profile);

        channelRepository.save(entity);

        return toDTO(entity);
    }

    // 2. Update Channel (USER and OWNER)
    public ChannelDTO update(String channelId, UpdateChannelDTO dto) {
        ChannelEntity entity = getByIdAndCheckOwner(channelId);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        channelRepository.save(entity);
        return toDTO(entity);
    }

    // 3. Update Channel Photo
    public ChannelDTO updatePhoto(String channelId, String attachId) {
        ChannelEntity entity = getByIdAndCheckOwner(channelId);
        AttachEntity attach = attachService.getById(attachId);
        entity.setPhoto(attach); // ← setPhotoId emas
        channelRepository.save(entity);
        return toDTO(entity);
    }

    // 4. Update Channel Banner
    public ChannelDTO updateBanner(String channelId, String attachId) {
        ChannelEntity entity = getByIdAndCheckOwner(channelId);
        AttachEntity attach = attachService.getById(attachId);
        entity.setBannerAttach(attach); // ← setBanner emas
        channelRepository.save(entity);
        return toDTO(entity);
    }

    // 5. Channel Pagination (ADMIN)
    public Page<ChannelDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return channelRepository.findAll(pageable).map(this::toDTO);
    }

    // 6. Get Channel By Id
    public ChannelDTO getById(String id) {
        return toDTO(getChannelById(id));
    }

    // 7. Change Channel Status (ADMIN, USER and OWNER)
    public ChannelDTO changeStatus(String channelId, ChannelStatusEnum status) {
        Integer profileId = SecurityUtil.getProfileId();
        ChannelEntity entity = getChannelById(channelId);

        // OWNER yoki ADMIN tekshirish
        boolean isOwner = entity.getProfileId().equals(profileId);
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AppBadException("Access denied");
        }

        entity.setStatus(status);
        channelRepository.save(entity);
        return toDTO(entity);
    }

    // 8. User Channel List (murojat qilgan user)
    public List<ChannelDTO> myChannels() {
        Integer profileId = SecurityUtil.getProfileId();
        return channelRepository.findAllByProfileId(profileId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Helper
    private ChannelEntity getChannelById(String id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Channel not found"));
    }

    private ChannelEntity getByIdAndCheckOwner(String channelId) {
        Integer profileId = SecurityUtil.getProfileId();
        ChannelEntity entity = getChannelById(channelId);
        if (!entity.getProfileId().equals(profileId)) {
            throw new AppBadException("Access denied");
        }
        return entity;
    }

    private ChannelDTO toDTO(ChannelEntity entity) {
        return ChannelDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .photoUrl(entity.getPhotoId() != null
                        ? serverUrl + "/attach/open/" + entity.getPhotoId()
                        : null)
                .bannerUrl(entity.getBanner() != null
                        ? serverUrl + "/attach/open/" + entity.getBanner()
                        : null)
                .build();
    }

    public boolean isProfileChannelOwner(Integer profileId, String channelId) {
        return channelRepository.findByIdAndProfileId(channelId, profileId) != null;
    }
}