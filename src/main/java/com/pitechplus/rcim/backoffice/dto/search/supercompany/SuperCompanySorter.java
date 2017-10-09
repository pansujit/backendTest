package com.pitechplus.rcim.backoffice.dto.search.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.SuperCompanySearchProperty;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 13.06.2017.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class SuperCompanySorter {

    SortDirection direction;
    SuperCompanySearchProperty property;

}
