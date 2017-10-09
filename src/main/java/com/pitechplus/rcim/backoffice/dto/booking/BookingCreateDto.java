package com.pitechplus.rcim.backoffice.dto.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BookingType;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.CarSharingInfoDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.DateLocationDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 24.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreateDto {

    CarSharingInfoDto carSharingInfo;
    DateLocationDto end;
    DateLocationDto start;
    String memberLogin;
    BookingType type;
    VehicleDto vehicle;
    Integer reservedSeats;
}
