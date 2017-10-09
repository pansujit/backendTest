package com.pitechplus.rcim.nissan.be.groups.add;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupViewDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga  on 15.09.2017.
 */
public class AddNewMemberNegativeTests extends BackendAbstract {

    private ParkingDto parkingDto;

    @BeforeMethod
    public void createGroup() {
        parkingDto = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(), DtoBuilders.buildParkingCreate()).getBody();
    }

    @Test(description = "This test verifies that if a member is not created on the superCompany of the group , this one can not be added as a new member to the existing group")
    @TestInfo(expectedResult = "The member was not added to the group.")
    public void differentSuperCompanyTest() throws IOException {
        GroupViewDto responseGroup = nissanBeServices.getGroup(rcimTestData.getAdminToken(),
                nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(1, 0, rcimTestData),
                        rcimTestData, parkingDto)).getBody().getId()).getBody();
        MemberDto memberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                        configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId())).getBody();
        GroupMembershipDto groupMembershipDto = NissanDtoBuilders.buildGroupMembershipDto(memberDto,
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        try {
            nissanBeServices.saveGroupMembership(rcimTestData.getAdminToken(), responseGroup.getId(), groupMembershipDto);
            Assert.fail("Even if the superCompany of the member is not the same with the superCompany of  the group, the member was added to the group.");
        } catch (HttpStatusCodeException exception) {
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "Member " + groupMembershipDto.getMemberId() + " is not in group supercompany", null));
        }
    }

    @Test(description = "This verifies that to a group that reached the limit a new member can not be added.")
    @TestInfo(expectedResult = "The member was not added to the group.")
    public void groupLimitReached() throws IOException {
        try {
            GroupViewDto responseGroup = nissanBeServices.getGroup(rcimTestData.getAdminToken(), nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                    NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(5, 0, rcimTestData),
                            rcimTestData, parkingDto)).getBody().getId()).getBody();
            MemberDto memberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                    NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                            configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId())).getBody();
            GroupMembershipDto groupMembershipDto = NissanDtoBuilders.buildGroupMembershipDto(memberDto, configsService.createFile(rcimTestData.getSuperAdminToken(),
                    DtoBuilders.buildFile()).getBody().getId());
            nissanBeServices.saveGroupMembership(rcimTestData.getAdminToken(), responseGroup.getId(), groupMembershipDto);
            Assert.fail("Even if the superCompany of the member is not the same with the superCompany of  the group, the member was added to the group.");
        } catch (HttpStatusCodeException exception) {

            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "Group membership limit reached", null));
        }
    }

    @Test(description = "This test verifies that to a group that already has an owner, another owner member can not be added.")
    @TestInfo(expectedResult = "The member was not added to the group.")
    public void anOwnerAlreadyExists() throws IOException {
        try {
            GroupViewDto responseGroup = nissanBeServices.getGroup(rcimTestData.getAdminToken(), nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                    NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(2,
                            0, rcimTestData), rcimTestData, parkingDto)).getBody().getId()).getBody();
            MemberDto memberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                    NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(), nissanUtils.createArrayOfValidFiles(1).get(0))).getBody();
            GroupMembershipDto groupMembershipDto = NissanDtoBuilders.buildGroupMembershipDto(memberDto,
                    configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
            groupMembershipDto.setOwner(true);
            nissanBeServices.saveGroupMembership(rcimTestData.getAdminToken(), responseGroup.getId(), groupMembershipDto);
            Assert.fail("Even if the group already has an owner another owner member was added to the group.");
        } catch (HttpStatusCodeException exception) {
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "A group cannot have more than one owner", null));
        }
    }

    @Test(description = "This test verifies that a member can not be added to a group if he is already in that group.")
    @TestInfo(expectedResult = "The member was not added to the group.")
    public void memberAddedTwice() throws IOException {
        try {
            GroupViewDto responseGroup = nissanBeServices.getGroup(rcimTestData.getAdminToken(), nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                    NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(2,
                            0, rcimTestData), rcimTestData, parkingDto)).getBody().getId()).getBody();
            MemberDto memberDto = nissanBeServices.getMember(rcimTestData.getSuperAdminToken(), responseGroup.getGroupMemberships().iterator().next().getMemberId()).getBody();
            GroupMembershipDto groupMembershipDto = NissanDtoBuilders.buildGroupMembershipDto(memberDto,
                    configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
            nissanBeServices.saveGroupMembership(rcimTestData.getAdminToken(), responseGroup.getId(), groupMembershipDto);
            Assert.fail("A member can not be added again into a group in which he is already in.");
        } catch (HttpStatusCodeException exception) {
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "A group cannot contain the same member multiple times", null));
        }
    }
}
