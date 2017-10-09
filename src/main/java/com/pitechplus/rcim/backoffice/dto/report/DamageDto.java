package com.pitechplus.rcim.backoffice.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pitechplus.rcim.backoffice.data.enums.DamageArea;
import com.pitechplus.rcim.backoffice.data.enums.DamageStatus;
import com.pitechplus.rcim.backoffice.data.enums.DamageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 04.09.2017.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "createdDate"})
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DamageDto {

    UUID id;
    String createdDate;
    DamageArea area;
    DamageStatus currentStatus;
    DamageType type;
    String comment;
    String vehicleId;
    UUID fileId;

}
