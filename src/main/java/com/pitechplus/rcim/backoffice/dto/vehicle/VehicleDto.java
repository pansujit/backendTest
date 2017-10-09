package com.pitechplus.rcim.backoffice.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.*;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

/**
 * Created by dgliga on 18.07.2017.
 */

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleDto {

    UUID id;
    String vin;
    String registrationNumber;
    String registrationDate;
    String pictureUrl;
    TransmissionType transmissionType;
    FuelType fuelType;
    FuelLevelDto fuelLevel;
    Integer mileage;
    Integer seats;
    VehicleVersionDto version;
    CategoryDto category;
    CompanyId company;
    ServiceLevelType serviceLevelType;
    StatusType statusType;
    String colorCode;
    ColorDto color;
    VehiclePositionDto lastPosition;
    Set<AccessoryType> accessories;
    SystemType systemType;
    DeviceDto device;
    Boolean ownedByRci;
    Integer doorsNumber;
    VehicleType type;
    UUID registrationDocumentId;
    VehicleCleanlinessDto cleanliness;

}
