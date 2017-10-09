package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Created by dgliga on 21.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CostDto {

    BigDecimal pricePerKm;
    BigDecimal estimatedPriceForDuration;
    BigDecimal lateFeePerHour;
    BigDecimal lateFixedFee;
    BigDecimal distancePrice;
    BigDecimal durationPrice;
    BigDecimal latePrice;
    BigDecimal totalPriceExcludingTaxes;
    BigDecimal taxesPrice;
    BigDecimal totalPriceIncludingTaxes;

}
