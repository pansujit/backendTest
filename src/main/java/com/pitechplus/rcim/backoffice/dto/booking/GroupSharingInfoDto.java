package com.pitechplus.rcim.backoffice.dto.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.GroupSharingType;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.VehicleStatusDataDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 24.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class GroupSharingInfoDto {

    UUID groupId;
    UUID siteId;
    GroupSharingType type;
    MembershipScheduleSlotDto scheduleSlot;
    String effectiveStartDate;
    String effectiveEndDate;
    VehicleStatusDataDto startVehicleStatusData;
    VehicleStatusDataDto endVehicleStatusData;
    Boolean delayed;

}
