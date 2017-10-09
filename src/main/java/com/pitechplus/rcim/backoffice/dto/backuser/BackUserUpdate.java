package com.pitechplus.rcim.backoffice.dto.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by dgliga on 28.02.2017.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BackUserUpdate {

    String firstName;
    String lastName;
    PhoneNumber phoneNumber;
    PhoneNumber secondaryPhoneNumber;
    String role;
    Address address;
    Locale locale;
    UUID companyId;
}
