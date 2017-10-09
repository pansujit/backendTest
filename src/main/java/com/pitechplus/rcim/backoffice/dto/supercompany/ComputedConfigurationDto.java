package com.pitechplus.rcim.backoffice.dto.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 08.05.2017.
 */
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
class ComputedConfigurationDto {

    String vatCode;
    Float vatRate;
    HotlineDto hotline;

}