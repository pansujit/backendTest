package com.pitechplus.rcim.backoffice.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Created by dgliga on 11.09.2017.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DamageReportContextDto {

    BookingDto booking;
    List<ReportRemarkDto> reportRemarks;
    DamageReportDetailsDto startDamageReport;
    DamageReportDetailsDto endDamageReport;

}
