package com.pitechplus.rcim.nissan.be.groups.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipViewDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupViewDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanmappers.GroupMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga  on 18.08.2017.
 */


public class CreateGroupsTests extends BackendAbstract {

    private ParkingDto parkingDto;

    @BeforeClass
    public void createGroup() {
        parkingDto = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                DtoBuilders.buildParkingCreate()).getBody();
    }

    @Test(description = "This test verifies that a group can be created")
    @TestInfo(expectedResult = "Group created with correct information which was given on request.")
    public void createGroupTest() {
        GroupCreateDto groupCreateDto = NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(5, 0, rcimTestData),
                rcimTestData, parkingDto);
        GroupViewDto responseGroupDto = nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(), groupCreateDto).getBody();
        GroupViewDto expectedGroupDto = GroupMapper.mapGroupCreateDtoToGroupViewDto(groupCreateDto, rcimTestData.getCompanyDto(),
                createGroupMembersViewDto(groupCreateDto.getGroupMemberships(), responseGroupDto));
        assertThat("Information given on response to create group does not reflect request !", responseGroupDto, is(expectedGroupDto));
    }


    private HashSet<GroupMembershipViewDto> createGroupMembersViewDto(Set<GroupMembershipDto> groupMembersList, GroupViewDto responseGroupDto) {

        HashSet<GroupMembershipViewDto> groupMembershipViewDto = new HashSet<>();
        for (GroupMembershipDto member : groupMembersList) {
            GroupMembershipViewDto groupMemberView = GroupMapper.mapGroupMembershipDtoToGroupMembershipViewDto(member,
                    nissanBeServices.getMember(rcimTestData.getSuperAdminToken(), member.getMemberId()).getBody(), responseGroupDto);
            groupMembershipViewDto.add(groupMemberView);
        }
        return groupMembershipViewDto;
    }
}
