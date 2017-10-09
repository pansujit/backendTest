package com.pitechplus.rcim.backoffice.dto.booking.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BookingState;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.data.enums.BookingType;
import com.pitechplus.rcim.backoffice.data.enums.StatusPayment;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

/**
 * Created by dgliga on 30.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingQueryDto {

    String memberFirstName;
    String memberLastName;
    String startDate;
    String endDate;
    UUID groupId;
    String vehicleBrand;
    String vehicleModel;
    UUID companyId;
    UUID subCompanyId;
    List<BookingState> states;
    BookingStatusType status;
    BookingType type;
    StatusPayment paymentStatus;
    String invoiceNumber;
    Boolean delayed;
    String id;
    Boolean failed;
    Page page;
    BookingSorterDto sort;

}
