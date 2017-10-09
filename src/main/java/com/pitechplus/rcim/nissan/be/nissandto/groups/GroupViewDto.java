package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.company.CompanyDto;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupStateDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * Created by dgliga  on 03.08.2017.
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
public class GroupViewDto {

    UUID id;
    CompanyDto company;
    Boolean isPrivate;
    Boolean invoicingSuspended;
    String publicId;
    LocalDate startDate;
    LocalDate endDate;
    GroupStateDto state;
    Set<GroupMembershipViewDto> groupMemberships;
    UUID parkingId;
    ParkingDto parking;
    VehicleDto vehicle;

}
