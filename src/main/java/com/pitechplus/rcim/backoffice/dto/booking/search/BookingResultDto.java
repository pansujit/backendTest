package com.pitechplus.rcim.backoffice.dto.booking.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.BookingMetadataDto;
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
public class BookingResultDto {

    Set<BookingDto> results;
    BookingMetadataDto metadata;
}
