package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 21.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class BookingMetadataDto {

    BookingFacetsDto facets;
    PageInfo paginationInfo;
}
