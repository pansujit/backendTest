package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupPropertyDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class GroupSorterDto {

    GroupPropertyDto property;
    SortDirection direction;

}
