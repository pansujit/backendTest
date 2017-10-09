package com.pitechplus.rcim.backoffice.dto.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.UsageType;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 06.07.2017.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SiteDto {

    UUID id;
    Integer timeUnitOfBooking;
    Integer minDurationOfBooking;
    Integer maxDurationOfBooking;
    Integer reservationBuffer;
    Integer maxStartDayBooking;
    Integer plannedUsageAvailability;
    Integer immediateUsageCarAvailability;
    Integer periodBeforeCantNoMoreUpdateStartDate;
    Integer periodBeforeCantNoMoreUpdateEndDate;
    Integer periodBeforeCantNoMoreCancel;
    Integer carPriorReservation;
    Integer automaticNoShowOneWay;
    Integer automaticNoShowRoundTrip;
    String name;
    Address address;
    UUID subCompanyId;
    String zoneId;
    Boolean automaticShortening;
    Boolean automaticExtension;
    Boolean chargeExpiredBooking;
    Boolean spontaneousBookingEnabled;
    UsageType spontaneousBookingUsage;
    Boolean smartcardFishingEnabled;
    Boolean smartcardEnabled;
}
