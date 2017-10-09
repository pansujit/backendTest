package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupStateDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

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

public class GroupQueryDto {

    String publicId;
    String memberName;
    String siteName;
    String plateNumber;
    Integer groupSize;
    GroupStateDto groupState;
    String startDate;
    UUID companyId;
    UUID subCompanyId;
    String endDate;
    Boolean invoicingSuspended;
    Page page;
    GroupSorterDto sort;
}



