package com.pitechplus.rcim.backoffice.dto.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 31.05.2017.
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CompanyConfiguration {

    UUID id;
    String name;
    String vatCode;
    Float vatRate;
    HotlineDto hotline;
    Boolean payBackEnabled;
    Float payBackRate;
    Boolean allowsGroups;

}
