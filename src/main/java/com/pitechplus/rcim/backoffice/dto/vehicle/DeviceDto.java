package com.pitechplus.rcim.backoffice.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 18.07.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceDto {

    private String serialNumber;
    private String installationDate;
    private String removalDate;

}
