package com.pitechplus.rcim.backoffice.dto.search.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.dto.common.Company;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Created by dgliga on 08.03.2017.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BackUserSearchFacets {

    Set<String> lastNames;
    Set<String> firstNames;
    Set<String> emails;
    Set<BackOfficeRole> roles;
    Set<Boolean> enabled;
    Set<Company> companies;

}
