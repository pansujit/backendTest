package com.pitechplus.rcim.backoffice.dto.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga  on 29.08.2017.
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuperCompanyConfigurationDto {

    UUID id;
    SuperCompanyConfigurationDto parent;
    String name;
    String vatCode;
    Float vatRate;
    HotlineDto hotline;
    Boolean payBackEnabled;
    Float payBackRate;
    Boolean allowsGroups;

}
