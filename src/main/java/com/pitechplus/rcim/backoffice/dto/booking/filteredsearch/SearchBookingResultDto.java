package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BookingType;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 21.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchBookingResultDto {

    BookingType type;
    DateLocationDto start;
    DateLocationDto end;
    VehicleDto vehicle;
    String comment;
    Integer reservedSeats;
    CarSharingInfoDto carSharingInfo;
    RideSharingInfoDto rideSharingInfo;
    ShuttleInfoDto shuttleInfo;

}
