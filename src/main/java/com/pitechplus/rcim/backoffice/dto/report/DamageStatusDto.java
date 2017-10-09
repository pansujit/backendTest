package com.pitechplus.rcim.backoffice.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.DamageStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 12.09.2017.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(exclude = "date")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DamageStatusDto {

    String date;
    DamageStatus type;

}
