package com.pitechplus.rcim.backoffice.dto.report.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportDeclaredStatus;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportStatus;
import com.pitechplus.rcim.backoffice.data.enums.DamageStatus;
import com.pitechplus.rcim.backoffice.data.enums.DamageType;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 22.09.2017.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DamageQueryDto {

    String companyId;
    String subCompanyId;
    String bookingId;
    String plateNumber;
    DamageType damageType;
    String startDate;
    String endDate;
    Page page;
    DamageStatus status;
    DamageSorterDto sort;


}
