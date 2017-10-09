package com.pitechplus.rcim.nissan.be.nissandto.members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by dgliga on 21.07.2017.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberCreateDto {

    String login;
    String password;
    String firstName;
    String lastName;
    PhoneNumber phoneNumber;
    String birthDate;
    Address address;
    Boolean vip;
    DrivingLicenceDto drivingLicence;
    Locale locale;
    UUID companyId;
}

