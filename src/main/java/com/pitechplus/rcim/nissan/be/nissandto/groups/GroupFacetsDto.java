package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Created by dgliga  on 07.09.2017.
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class GroupFacetsDto {
    Set<String> publicIds;
    Set<String> siteNames;
    Set<String> plateNumbers;
    Set<String> groupSizes;
    Set<String> groupStates;
    Set<String> startDates;
    Set<String> endDates;
    Set<String> companyIds;
    Set<String> subCompanyIds;

}
