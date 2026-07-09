package dasturlash.uz.youtube.dto.report;

import dasturlash.uz.youtube.dto.playlist.ProfileShortInfo;
import dasturlash.uz.youtube.enums.ReportTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
public class ReportInfo {
    private Long id;
    private ProfileShortInfo profile;
    private String content;
    private String entityId;
    private ReportTypeEnum type;
}