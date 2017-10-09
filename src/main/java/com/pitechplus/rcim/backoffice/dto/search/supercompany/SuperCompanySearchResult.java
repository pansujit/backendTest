package com.pitechplus.rcim.backoffice.dto.search.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

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
public class SuperCompanySearchResult {

    List<SuperCompanyDto> results;
    SuperCompanyMetadata metadata;

}
