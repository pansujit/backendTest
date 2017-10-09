package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.ContractState;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.ContractType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by dgliga  on 21.07.2017.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupMembershipContractDto {

    UUID id;
    String publicId;
    BigDecimal pricePerMonth;
    Integer contractDuration;
    Integer allocatedDistance;
    Integer allocatedTime;
    UUID contractFileId;
    String contractUrl;
    ContractState state;
    ContractType type;
    String startDate;
    String endDate;

}
