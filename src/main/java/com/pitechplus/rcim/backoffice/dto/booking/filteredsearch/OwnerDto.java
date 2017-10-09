package com.pitechplus.rcim.backoffice.dto.booking.filteredsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
class OwnerDto {

    String firstname;
    String lastname;
    PhoneNumber phoneNumber;
    String email;

}
