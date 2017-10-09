package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Created by dgliga on 22.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleStatusDataDto {

    BigDecimal mileage;
    BigDecimal fuel;
    Float latitude;
    Float longitude;
    String gpsTimestamp;

}
