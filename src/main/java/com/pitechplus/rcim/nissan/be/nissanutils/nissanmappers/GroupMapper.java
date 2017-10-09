package com.pitechplus.rcim.nissan.be.nissanutils.nissanmappers;

import com.pitechplus.rcim.backoffice.dto.company.CompanyDto;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupStateDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipViewDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupViewDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;

import java.time.LocalDate;
import java.util.HashSet;

/**
 * Created by dgliga  on 18.08.2017.
 */
public class GroupMapper {
    /**
     * This method is used to create a memberCreateDto object with valid data
     *
     * @param groupCreateDto -what member was sent on the request
     * @return GroupViewDto object with all fields having valid values
     */
    public static GroupViewDto mapGroupCreateDtoToGroupViewDto(GroupCreateDto groupCreateDto, CompanyDto companyDto, HashSet<GroupMembershipViewDto> groupMembershipDto) {
        companyDto.setAgencyCode(null);
        companyDto.setParentCompanyId(null);
        return GroupViewDto.builder()
                .isPrivate(null)
                .company(companyDto)
                .invoicingSuspended(false)
                .publicId(groupCreateDto.getPublicId())
                .state(GroupStateDto.INACTIVE)
                .groupMemberships(groupMembershipDto)
                .parkingId(groupCreateDto.getParkingId())
                .build();

    }

    /**
     * This method is used to create a groupMembershipViewDto object with valid data
     *
     * @param groupMembershipDto -what member was sent on the request
     * @return memberCreateDto object with all fields having valid values
     */
    public static GroupMembershipViewDto mapGroupMembershipDtoToGroupMembershipViewDto(GroupMembershipDto groupMembershipDto, MemberDto memberDto, GroupViewDto groupViewDto) {

        return GroupMembershipViewDto.builder()
                .owner(groupMembershipDto.getOwner())
                .memberId(memberDto.getId())
                .email(memberDto.getLogin())
                .memberFirstName(memberDto.getFirstName())
                .memberLastName(memberDto.getLastName())
                .memberDescription(memberDto.getDescription())
                .memberPhoneNumber(memberDto.getPhoneNumber())
                .memberAge(LocalDate.now().getYear() - LocalDate.parse(memberDto.getBirthDate()).getYear())
                .groupMembershipContract(groupMembershipDto.getGroupMembershipContract())
                .groupPublicId(groupViewDto.getPublicId())
                .groupId(groupViewDto.getId())
                .build();


    }

   /* public static GroupMembershipDto mapMemberDtoToGroupMembershipDto (MemberDto memberDto){
        return GroupMembershipDto.builder()
                .
    }*/

}
