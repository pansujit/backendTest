package com.pitechplus.rcim.backoffice.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.DamageArea;
import com.pitechplus.rcim.backoffice.data.enums.DamageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

/**
 * Created by dgliga on 12.09.2017.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "createdDate"})
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DamageDetailsDto {

    UUID id;
    DamageVehicleDto vehicle;
    String createdDate;
    DamageStatusDto currentStatus;
    DamageArea area;
    DamageType type;
    UUID fileId;
    UUID bookingId;
    Set<DamageStatusDto> statuses;

}
