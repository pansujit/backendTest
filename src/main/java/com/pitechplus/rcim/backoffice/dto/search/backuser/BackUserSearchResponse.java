package com.pitechplus.rcim.backoffice.dto.search.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
public class BackUserSearchResponse {

    List<BackUser> results;
    BackUserSearchMetadata metadata;

}
