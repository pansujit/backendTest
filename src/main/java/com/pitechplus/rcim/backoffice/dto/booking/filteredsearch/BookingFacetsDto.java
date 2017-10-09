package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.data.enums.BookingType;
import com.pitechplus.rcim.backoffice.data.enums.StatusPayment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Created by dgliga on 21.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookingFacetsDto {

    Set<String> memberFirstNames;
    Set<String> memberLastNames;
    Set<String> startDates;
    Set<String> vehicleBrands;
    Set<String> vehicleModels;
    Set<BookingStatusType> statuses;
    Set<BookingType> types;
    Set<StatusPayment> paymentStatuses;
    Set<String> invoiceNumbers;
    Set<Boolean> delayedValues;
    Set<String> ids;
    Set<Boolean> failedValues;

}
