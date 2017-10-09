package com.pitechplus.rcim.backoffice.dto.search.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.*;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

/**
 * Created by dgliga on 24.07.2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchVehicleRequestDto {

    String plateNumber;
    List<FuelType> fuelType;
    List<TransmissionType> transmissionType;
    List<String> category;
    String brand;
    String model;
    String version;
    String color;
    UUID companyId;
    UUID subCompanyId;
    List<ServiceLevelType> serviceLevelType;
    List<StatusType> vehicleStatus;
    List<AccessoryType> accessories;
    List<SystemType> systemType;
    List<VehicleType> type;
    List<VehicleCleanlinessStatus> cleanliness;
    Page page;
    SorterVehicleDto sort;

}
