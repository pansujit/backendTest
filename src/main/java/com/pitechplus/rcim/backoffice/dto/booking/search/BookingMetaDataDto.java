package com.pitechplus.rcim.backoffice.dto.booking.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 30.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingMetaDataDto {

    SearchBookingFacetsDto facets;
    PageInfo paginationInfo;

}
