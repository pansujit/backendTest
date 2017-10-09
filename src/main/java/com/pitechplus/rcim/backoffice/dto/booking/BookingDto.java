package com.pitechplus.rcim.backoffice.dto.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BookingState;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.data.enums.BookingType;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.CarSharingInfoDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.DateLocationDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.RideSharingInfoDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.ShuttleInfoDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by dgliga on 24.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id", "functionalId"})
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    UUID id;
    String functionalId;
    BookingType type;
    DateLocationDto start;
    DateLocationDto end;
    VehicleDto vehicle;
    BookingStatusType status;
    String comment;
    String technicalComment;
    MemberDto member;
    BookingState state;
    Integer reservedSeats;
    CarSharingInfoDto carSharingInfo;
    RideSharingInfoDto rideSharingInfo;
    ShuttleInfoDto shuttleInfo;
    GroupSharingInfoDto groupSharingInfo;
    BigDecimal remainingAmountToCharge;
    Boolean paymentSettlingAllowed;

}
