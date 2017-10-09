package com.pitechplus.rcim.backoffice.dto.search.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

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
public class BackUserSearch {

    String lastName;
    String firstName;
    String email;
    UUID companyId;
    BackOfficeRole role;
    Boolean enabled;
    Page page;
    BackUserSorter sort;

}
