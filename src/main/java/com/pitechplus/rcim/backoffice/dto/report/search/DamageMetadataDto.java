package com.pitechplus.rcim.backoffice.dto.report.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 22.09.2017.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
class DamageMetadataDto {

    DamageFacetsDto facets;
    PageInfo paginationInfo;

}
