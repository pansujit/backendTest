package com.pitechplus.rcim.backoffice.dto.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Created by dgliga on 27.02.2017.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractDto {

    UUID id;
    String name;
    String reference;
    String contractStart;
    String contractEnd;
    Boolean shuttle;
    Boolean rideSharing;
    Boolean privateCarSharing;
    Boolean businessCarSharing;

}
