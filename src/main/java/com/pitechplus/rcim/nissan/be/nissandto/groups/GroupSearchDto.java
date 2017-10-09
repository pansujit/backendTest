package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class GroupSearchDto {

    UUID id;
    String publicId;
    Integer groupSize;
    String groupAddress;
    String companyName;
    UUID siteId;
    String siteName;
    String startDate;
    String endDate;
    UUID vehicleId;
    String plateNumber;
    GroupStateDto groupState;
    Boolean invoicingSuspended;

}
