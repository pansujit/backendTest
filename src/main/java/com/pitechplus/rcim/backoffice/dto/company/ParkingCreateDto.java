package com.pitechplus.rcim.backoffice.dto.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.Coordinates;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 13.07.2017.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ParkingCreateDto {

    String additionalInformation;
    Address address;
    Boolean alwaysOpen;
    Coordinates coordinates;
    Boolean disabledAccess;
    Boolean electricCharging;
    String groupId;
    Boolean gsmConnection;
    String name;
    Boolean privateAccess;
    Integer radius;
}
