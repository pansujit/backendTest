package com.pitechplus.rcim.nissan.be.nissandto.members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.Civility;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.MaritalStatus;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.SexDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;

/**
 * Created by dgliga  on 07.08.2017.
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberUpdateDto {

    Civility civility;
    String firstName;
    String lastName;
    PhoneNumber phoneNumber;
    PhoneNumber secondaryPhoneNumber;
    PhoneNumber emergencyPhoneNumber;
    String birthDate;
    String birthPlace;
    Boolean commercialOffers;
    Address address;
    Boolean vip;
    MemberAvatarDto avatar;
    DrivingLicenceDto drivingLicence;
    DocumentaryEvidenceDto identityDocument;
    DocumentaryEvidenceDto employerCertificate;
    DocumentaryEvidenceDto lastTaxNotice;
    DocumentaryEvidenceDto lastPaySlip;
    Locale locale;
    Boolean technician;
    SexDto sex;
    MaritalStatus maritalStatus;
    String profession;
    String description;

}
