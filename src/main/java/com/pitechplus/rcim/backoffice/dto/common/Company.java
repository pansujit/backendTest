package com.pitechplus.rcim.backoffice.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 27.02.2017.
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode(exclude = "name")
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {

    UUID id;
    String name;
}
