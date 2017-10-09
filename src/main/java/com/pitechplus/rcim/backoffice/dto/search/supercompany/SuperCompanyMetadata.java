package com.pitechplus.rcim.backoffice.dto.search.supercompany;

import com.pitechplus.rcim.backoffice.dto.pagination.PageInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 13.06.2017.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
class SuperCompanyMetadata {

    PageInfo paginationInfo;
    SuperCompanyFacets facets;

}
