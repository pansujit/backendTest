package com.pitechplus.rcim.backoffice.dto.search.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 13.06.2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuperCompanySearch {

    Page page;
    SuperCompanySorter sort;

}
