package com.pitechplus.rcim.backoffice.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.UserType;
import com.pitechplus.rcim.backoffice.data.enums.VehicleCleanlinessStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 18.07.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class VehicleCleanlinessDto {

    String createdDate;
    VehicleCleanlinessStatus status;
    UserType userType;
    String userFirstName;
    String userLastName;
    UUID reporterId;

}
