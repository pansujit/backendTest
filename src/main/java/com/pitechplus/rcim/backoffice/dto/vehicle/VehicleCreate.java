package com.pitechplus.rcim.backoffice.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

/**
 * Created by dgliga on 18.07.2017.
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleCreate {

    String vin;
    String registrationNumber;
    String registrationDate;
    String pictureUrl;
    TransmissionType transmissionType;
    FuelType fuelType;
    Integer mileage;
    Integer seats;
    UUID categoryId;
    UUID versionId;
    UUID companyId;
    UUID colorId;
    ServiceLevelType serviceLevelType;
    VehiclePositionDto lastPosition;
    Set<AccessoryType> accessories;
    SystemType systemType;
    DeviceDto device;
    Boolean ownedByRci;
    Integer doorsNumber;
    VehicleType type;
    UUID registrationDocumentId;

}
