package com.pitechplus.rcim.backoffice.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportStatus;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by dgliga on 11.09.2017.
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = {"id", "createdDate"})
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DamageReportDetailsDto {

    UUID id;
    String createdDate;
    Integer externalCleanliness;
    Integer internalCleanliness;
    String cleanlinessComment;
    Set<DamageDto> reports;
    Set<ReportRemarkDto> reportRemarks;
    DamageReportType type;
    DamageReportStatus status;

}
