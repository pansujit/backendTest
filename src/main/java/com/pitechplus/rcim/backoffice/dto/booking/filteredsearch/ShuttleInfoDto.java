package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

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
public class ShuttleInfoDto {

    ShuttleCompanyDto shuttleCompany;
    BigDecimal price;
    Integer duration;
    UUID routeId;

}
