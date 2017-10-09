package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.StatusPayment;
import com.pitechplus.rcim.backoffice.data.enums.UsageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 21.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "cost")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarSharingInfoDto {

    UsageType usageType;
    StatusPayment statusPayment;
    Integer availableSeats;
    Integer lateDuration;
    CostDto cost;
    Double distanceTravelled;
    Boolean openToRideSharing;
    PickUpDto pickUp;
    String invoiceNumber;
    String costCenter;
    Boolean delayed;
    Boolean failed;
    String effectiveStartDate;
    String effectiveEndDate;
    VehicleStatusDataDto endVehicleStatusData;
    VehicleStatusDataDto startVehicleStatusData;
    Boolean freeOfCharges;

}
