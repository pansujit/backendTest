package com.pitechplus.rcim.backoffice.dto.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.BookingMetadataDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.SearchBookingResultDto;
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
public class SearchBookingResponseDto {

    Set<SearchBookingResultDto> results;
    BookingMetadataDto metadata;
}
