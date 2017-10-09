package com.pitechplus.rcim.backoffice.dto.report.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

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
class DamageFacetsDto {

    Set<String> companyIds;
    Set<String> plateNumbers;
    Set<String> damageTypes;
    Set<String> creationDates;
    Set<String> subCompanyIds;
    Set<String> bookingIds;

}
