package com.pitechplus.rcim.backoffice.dto.pagination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PageInfo {

    Integer currentPage;
    Integer pageSize;
    Integer totalPages;
    Integer totalResults;

}
