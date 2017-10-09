package com.pitechplus.rcim.backoffice.dto.search.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Created by dgliga on 24.07.2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SearchVehicleFacetsDto {

    Set<String> plateNumbers;
    Set<FuelType> fuelTypes;
    Set<TransmissionType> transmissionTypes;
    Set<String> categories;
    Set<String> brands;
    Set<String> models;
    Set<String> versions;
    Set<String> colors;
    Set<ServiceLevelType> serviceLevelTypes;
    Set<StatusType> statuses;
    Set<AccessoryType> accessories;
    Set<SystemType> systemTypes;
    Set<VehicleType> types;
    Set<VehicleCleanlinessStatus> cleanlinesses;

}
