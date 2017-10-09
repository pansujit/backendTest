package com.pitechplus.rcim.backoffice.dto.booking.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.AccessoryType;
import com.pitechplus.rcim.backoffice.data.enums.FuelType;
import com.pitechplus.rcim.backoffice.data.enums.TransmissionType;
import com.pitechplus.rcim.backoffice.data.enums.UsageType;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Created by dgliga on 30.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SearchBookingFacetsDto {

    Set<AccessoryType> accessories;
    Set<UsageType> usageTypes;
    Set<FuelType> fuelTypes;
    Set<TransmissionType> transmissionTypes;
    Set<String> categories;
    Set<ParkingDto> startParkings;

}
