package com.pitechplus.rcim.nissan.be.groups.add;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupViewDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by dgliga  on 15.09.2017.
 */
public class AddNewMemberTest extends BackendAbstract {

    private ParkingDto parkingDto;

    @BeforeClass
    public void createGroup() {
        parkingDto = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                DtoBuilders.buildParkingCreate()).getBody();
    }

    @Test(description = "This test verifies that a new member can be added to an existing group")
    @TestInfo(expectedResult = "The member was added to the group.")
    public void addNewMemberToGroupTest() {
        GroupViewDto group = nissanBeServices.getGroup(rcimTestData.getAdminToken(),
                nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                        NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(1,
                                0, rcimTestData), rcimTestData, parkingDto)).getBody().getId()).getBody();
        MemberDto memberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getSuperCompanyDto().getId().toString(),
                        configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody()
                                .getId())).getBody();
        GroupMembershipDto groupMembershipDto = NissanDtoBuilders.buildGroupMembershipDto(memberDto,
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        nissanBeServices.saveGroupMembership(rcimTestData.getAdminToken(), group.getId(), groupMembershipDto);
        //todo: where are the asserts ?
    }
}
