package com.pitechplus.rcim.nissan.be.nissanutils.nissanmappers;

import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.ReviewStatus;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberUpdateDto;

/**
 * Created by dgliga  on 09.08.2017.
 */
public class MemberMapper {
    /**
     * This method is used to create a memberCreateDto object with valid data
     *
     * @param memberCreateDto -what member was sent on the request
     * @return memberCreateDto object with all fields having valid values
     */
    public static MemberDto mapMemberCreateDtoToMemberDto(MemberCreateDto memberCreateDto, SuperCompanyDto superCompanyDto) {
        return MemberDto.builder()
                .login(memberCreateDto.getLogin())
                .firstName(memberCreateDto.getFirstName())
                .lastName(memberCreateDto.getLastName())
                .phoneNumber(memberCreateDto.getPhoneNumber())
                .birthDate(memberCreateDto.getBirthDate())
                .address(memberCreateDto.getAddress())
                .vip(memberCreateDto.getVip())
                .enabled(false)
                .suspended(false)
                .commercialOffers(false)
                .professional(false)
                .validated(memberCreateDto.getDrivingLicence().getValidated())
                .profileReviewStatus(memberCreateDto.getDrivingLicence().getStatus())
                .drivingLicence(memberCreateDto.getDrivingLicence())
                .locale(memberCreateDto.getLocale())
                .company(superCompanyDto)
                .technician(false).build();
    }


    /**
     * This method maps database information from a MemberUpdateDto to MemberDto object
     *
     * @param memberUpdateDto object which is used to update a back office user
     * @return BackUser object with information from the request
     */

    public static MemberDto mapMemberUpdateDtoToMemberDto(MemberUpdateDto memberUpdateDto, MemberDto memberDto) {
        return MemberDto.builder()
                .id(memberDto.getId())
                .civility(memberUpdateDto.getCivility())
                .login(memberDto.getLogin())
                .firstName(memberUpdateDto.getFirstName())
                .lastName(memberUpdateDto.getLastName())
                .phoneNumber(memberUpdateDto.getPhoneNumber())
                .secondaryPhoneNumber(memberUpdateDto.getSecondaryPhoneNumber())
                .emergencyPhoneNumber(memberUpdateDto.getEmergencyPhoneNumber())
                .birthDate(memberUpdateDto.getBirthDate())
                .birthPlace(memberUpdateDto.getBirthPlace())
                .address(memberUpdateDto.getAddress())
                .vip(memberUpdateDto.getVip())
                .enabled(true)         //can't be updated
                .suspended(false)      //can't be updated
                .commercialOffers(memberUpdateDto.getCommercialOffers())
                .professional(false)  //can't be updated
                .validated(false)     //can't be updated
                .avatar(memberUpdateDto.getAvatar())
                .drivingLicence(memberUpdateDto.getDrivingLicence())
                .identityDocument(memberUpdateDto.getIdentityDocument())
                .employerCertificate(memberUpdateDto.getEmployerCertificate())
                .lastTaxNotice(memberUpdateDto.getLastTaxNotice())
                .lastPaySlip(memberUpdateDto.getLastPaySlip())
                .locale(memberUpdateDto.getLocale())
                .company(memberDto.getCompany())
                .technician(memberUpdateDto.getTechnician())
                .sex(memberUpdateDto.getSex())
                .maritalStatus(memberUpdateDto.getMaritalStatus())
                .profession(memberUpdateDto.getProfession())
                .description(memberUpdateDto.getDescription())
                .profileReviewStatus(ReviewStatus.TO_REVIEW).build();

        // GroupMembershipViewDto groupMembership;
        // List<CommentDto> comments
        // EnterpriseDto enterprise;
     }
}
