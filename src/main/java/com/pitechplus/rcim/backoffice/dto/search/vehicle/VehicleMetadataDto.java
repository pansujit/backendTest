package com.pitechplus.rcim.backoffice.dto.search.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 24.07.2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class VehicleMetadataDto {

    SearchVehicleFacetsDto facets;
    PageInfo paginationInfo;

}
