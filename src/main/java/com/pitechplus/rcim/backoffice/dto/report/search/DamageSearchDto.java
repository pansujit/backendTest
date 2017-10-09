package com.pitechplus.rcim.backoffice.dto.report.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.DamageArea;
import com.pitechplus.rcim.backoffice.data.enums.DamageStatus;
import com.pitechplus.rcim.backoffice.data.enums.DamageType;
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
@EqualsAndHashCode(exclude = {"id", "creationDate"})
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DamageSearchDto {

    UUID id;
    String companyId;
    String bookingId;
    UUID fileId;
    String vehicleId;
    DamageArea area;
    DamageType type;
    DamageStatus status;
    String vehicleBrandModel;
    String vehiclePlateNumber;
    String creationDate;

}
