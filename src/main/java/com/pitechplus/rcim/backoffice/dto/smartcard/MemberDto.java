package com.pitechplus.rcim.backoffice.dto.smartcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.Civility;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.MaritalStatus;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.ReviewStatus;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.SexDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipViewDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.CommentDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.DocumentaryEvidenceDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.DrivingLicenceDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.EnterpriseDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberAvatarDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
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
@EqualsAndHashCode(exclude= "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberDto {

    UUID id;
    Civility civility;
    String login;
    String firstname;
    String lastname;
    PhoneNumber phoneNumber;
    PhoneNumber secondaryPhoneNumber;
    PhoneNumber emergencyPhoneNumber;
    String birthDate;
    String birthPlace;
    Address address;
    Boolean vip;
    Boolean enabled;
    Boolean suspended;
    Boolean commercialOffers;
    Boolean professional;
    Boolean validated;
    MemberAvatarDto avatar;
    ReviewStatus profileReviewStatus;
    DrivingLicenceDto drivingLicence;
    DocumentaryEvidenceDto identityDocument;
    DocumentaryEvidenceDto employerCertificate;
    DocumentaryEvidenceDto lastTaxNotice;
    DocumentaryEvidenceDto lastPaySlip;
    Locale locale;
    SuperCompanyDto company;
    EnterpriseDto enterprise;
    Boolean technician;
    SexDto sex;
    MaritalStatus maritalStatus;
    String profession;
    String description;
    GroupMembershipViewDto groupMembership;
    List<CommentDto> comments;
}
