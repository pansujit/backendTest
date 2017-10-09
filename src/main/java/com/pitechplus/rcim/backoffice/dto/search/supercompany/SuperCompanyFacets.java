package com.pitechplus.rcim.backoffice.dto.search.supercompany;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Created by dgliga on 13.06.2017.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
class SuperCompanyFacets {

    Set<String> names;

}
