package com.pitechplus.rcim.backoffice.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Created by dgliga on 11.09.2017.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
class ReportRemarkDto {


    UUID id;
    OffsetDateTime createdDate;
    String comment;
    UUID reporterId;

}
