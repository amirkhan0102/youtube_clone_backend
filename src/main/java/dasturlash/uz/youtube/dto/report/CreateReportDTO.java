package dasturlash.uz.youtube.dto.report;

import dasturlash.uz.youtube.enums.ReportTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReportDTO {
    @NotBlank
    private String entityId;
    @NotNull
    private ReportTypeEnum type;
    private String content;
}
