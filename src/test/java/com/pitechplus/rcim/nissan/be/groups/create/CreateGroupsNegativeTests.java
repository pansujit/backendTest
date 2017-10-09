package com.pitechplus.rcim.nissan.be.groups.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga  on 31.08.2017.
 */
public class CreateGroupsNegativeTests extends BackendAbstract {

    private ParkingDto parkingDto;

    @BeforeMethod
    public void createGroup() {
        parkingDto = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                DtoBuilders.buildParkingCreate()).getBody();
    }

    @Test(description = "This test verifies that create group call with invalid  X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidIdTokenTest() throws IOException {
        try {
            nissanBeServices.createGroup(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(),
                    NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(1, 0, rcimTestData), rcimTestData,parkingDto)).getBody();
            Assert.fail("Group created with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that a group can not be created without members")
    @TestInfo(expectedResult = "The server with 500 Internal Server Error  with message: Request processing failed.")
    public void noMembersTest() throws IOException {
        try {
            nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(), NissanDtoBuilders.buildGroupCreateDto(new HashSet<>(), rcimTestData,parkingDto)).getBody();
            Assert.fail("Group created without members!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                            null, null));
        }
    }


    @Test(description = "This test verifies that a group can not be created with two owners - both have the value TRUE at the OWNER paramenter ")
    @TestInfo(expectedResult = "The server with 422 Unprocessable entity" + ErrorMessages.TWO_GROUP_OWNERS)
    public void twoMembersOwnerTest() throws IOException {
        try {
            Set<GroupMembershipDto> groupMembershipDto = nissanUtils.createListOfMembersOfTheSameCompany(2, 0, rcimTestData);
            for (GroupMembershipDto member : groupMembershipDto)
                if (!member.getOwner()) {
                    member.setOwner(true);
                    break;
                }

            nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(), NissanDtoBuilders.buildGroupCreateDto(groupMembershipDto, rcimTestData, parkingDto));

            Assert.fail("Group created with two members as owner!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "A group cannot have more than one owner", null));
        }
    }

/*    @Test(description = "This test verifies that a group can not be created if a member is added twice")
    @TestInfo(expectedResult = "The server with 422 Unprocessable entity" + ErrorMessages.MEMBER_ADDED_MULTIPLE_TIMES)
    public void sameTwoMembersTest() throws IOException {
        try {
            HashSet<GroupMembershipDto> groupMembershipDto = nissanUtils.createListOfMembersOfTheSameCompany(2, 0, rcimTestData);

            nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(), NissanDtoBuilders.buildGroupCreateDto(groupMembershipDto, rcimTestData));

            Assert.fail("Group created even if a member is added twice!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "A group cannot contain the same member multiple times", null));
        }
    }*/


    @Test(description = "This test verifies that a group can not be created using an existing publicId.")
    @TestInfo(expectedResult = "The server with 422 Unprocessable entity" + ErrorMessages.EXISTING_PUBLICID)
    public void creatGroupWithAnExistingPublicId() throws IOException {
        GroupCreateDto groupCreateDto = null;
        try {
            groupCreateDto = NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(2, 0, rcimTestData), rcimTestData, parkingDto);
            groupCreateDto.setPublicId(nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(), NissanDtoBuilders
                    .buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(2, 0, rcimTestData), rcimTestData,parkingDto))
                    .getBody().getPublicId());
            nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(), groupCreateDto);
            Assert.fail("Group created even if there is another group created with the same PublicId");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "Group public id already exists: " + groupCreateDto.getPublicId(), null));
        }
    }


    @Test(description = "This test verifies that a group can not be created with more than 5 members")
    @TestInfo(expectedResult = "The server with 422 Unprocessable entity" + ErrorMessages.GROUP_LIMIT_REACHED)
    public void moreThanFiveMembersTest() throws IOException {
        try {
            nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                    NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(6, 0, rcimTestData), rcimTestData, parkingDto)).getBody();
            Assert.fail("Group created with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "Group membership limit reached", null));
        }
    }

}