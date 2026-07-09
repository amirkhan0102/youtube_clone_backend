package dasturlash.uz.youtube.service;

import dasturlash.uz.youtube.dto.playlist.AttachInfo;
import dasturlash.uz.youtube.dto.playlist.ProfileShortInfo;
import dasturlash.uz.youtube.dto.report.CreateReportDTO;
import dasturlash.uz.youtube.dto.report.ReportInfo;
import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.entity.ReportEntity;
import dasturlash.uz.youtube.exception.AppBadException;
import dasturlash.uz.youtube.repository.ProfileRepository;
import dasturlash.uz.youtube.repository.ReportRepository;
import dasturlash.uz.youtube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ProfileRepository profileRepository;

    @Value("${server.url}")
    private String serverUrl;

    // 1. Create Report (USER)
    public ReportInfo create(CreateReportDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();

        ProfileEntity profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new AppBadException("Profile not found"));

        ReportEntity entity = new ReportEntity();
        entity.setProfile(profile);
        entity.setEntityId(dto.getEntityId());
        entity.setType(dto.getType());
        entity.setContent(dto.getContent());
        reportRepository.save(entity);

        return toInfo(entity);
    }

    // 2. Report List Pagination (ADMIN)
    public Page<ReportInfo> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return reportRepository.findAll(pageable).map(this::toInfo);
    }

    // 3. Delete Report (ADMIN)
    public Boolean delete(Long id) {
        ReportEntity entity = reportRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Report not found"));
        reportRepository.delete(entity);
        return true;
    }

    // 4. Report List By UserId (ADMIN)
    public Page<ReportInfo> listByUserId(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return reportRepository.findAllByProfileId(userId, pageable).map(this::toInfo);
    }

    private ReportInfo toInfo(ReportEntity entity) {
        ProfileEntity profile = entity.getProfile();
        return ReportInfo.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .entityId(entity.getEntityId())
                .type(entity.getType())
                .profile(ProfileShortInfo.builder()
                        .id(profile.getId())
                        .name(profile.getName())
                        .surname(profile.getSurname())
                        .photo(profile.getPhotoId() != null
                                ? AttachInfo.builder()
                                .id(profile.getPhotoId())
                                .url(serverUrl + "/attach/open/" + profile.getPhotoId())
                                .build()
                                : null)
                        .build())
                .build();
    }
}