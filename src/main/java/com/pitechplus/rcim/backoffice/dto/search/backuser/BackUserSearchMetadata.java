package com.pitechplus.rcim.backoffice.dto.search.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 13.03.2017.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BackUserSearchMetadata {

    BackUserSearchFacets facets;
    PageInfo paginationInfo;

}
