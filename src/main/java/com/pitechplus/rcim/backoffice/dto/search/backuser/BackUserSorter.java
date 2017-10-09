package com.pitechplus.rcim.backoffice.dto.search.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BackUserSearchProperty;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class BackUserSorter {

    BackUserSearchProperty property;
    SortDirection direction;

}
