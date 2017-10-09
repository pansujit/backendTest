package com.pitechplus.rcim.backoffice.dto.search.companyconfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.supercompany.CompanyConfiguration;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Created by dgliga on 31.05.2017.
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SearchCompanyConfigurations {

    List<CompanyConfiguration> results;
    ConfigurationMetaData metadata;

}
