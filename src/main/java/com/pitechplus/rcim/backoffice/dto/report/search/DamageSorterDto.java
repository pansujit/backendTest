package com.pitechplus.rcim.backoffice.dto.report.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportProperty;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 25.09.2017.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
class DamageSorterDto {

    DamageReportProperty property;
    SortDirection direction;

}
