package com.pitechplus.rcim.backoffice.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Created by dgliga on 04.09.2017.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DamageReportCreateDto {

    DamageReportType type;
    Integer externalCleanliness;
    Integer internalCleanliness;
    String cleanlinessComment;
    Set<DamageDto> reports;

}
