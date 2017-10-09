package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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

public class GroupResultDto {

    List<GroupSearchDto> results;
    GroupFacetsDto facets;
    PageInfoDto pageInfo;
}
