package com.pitechplus.rcim.backoffice.dto.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.Company;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by dgliga on 27.02.2017.
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BackUser {

    UUID id;
    String login;
    String password;
    String firstName;
    String lastName;
    PhoneNumber phoneNumber;
    PhoneNumber secondaryPhoneNumber;
    String role;
    Address address;
    Locale locale;
    Company company;
    Boolean enabled;
    Boolean suspended;
}
