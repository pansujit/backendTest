package com.pitechplus.rcim.nissan.be.members.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberUpdateDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.rits.cloning.Cloner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga  on 11.08.2017.
 */
public class UpdateMembersNegativeTests extends BackendAbstract {
    private MemberDto memberDto;
    private MemberUpdateDto memberUpdateDto;

    @BeforeClass(description = "This method creates a company and prepares object used for update company and cloning object.")
    public void createMember() {
        memberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                        (nissanUtils.createArrayOfValidFiles(1)).get(0))).getBody();

        UUID[] idFile = new UUID[6];

        for (int i = 0; i < 6; i++) {
            idFile[i] = configsService.createFile(extractXAuthTokenFromResponse(backUserService.authUser
                    (new Login(boSuperAdminUsername, boSuperAdminPassword))), DtoBuilders.buildFile()).getBody().getId();
            memberUpdateDto = NissanDtoBuilders.buildMemberUpdateDto(nissanUtils.createArrayOfValidFiles(6),
                    NissanDtoBuilders.buildDrivingLicenceDto((nissanUtils.createArrayOfValidFiles(1)).get(0)));
        }
    }

    @Test(description = "This test verifies that update member call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void updateCompanyInvalidXAuthTest() throws IOException {
        try {
            nissanBeServices.updateMember("Invalid X-AUTH-TOKEN", memberDto.getId(), memberUpdateDto);
            Assert.fail("Member was updated with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to update an existing member with all missing mandatory fields triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n-  name, address, phoneNumber and locale")
    public void memberUpdateWithMissingMandatoryFieldsTest() throws IOException {
        try {
            nissanBeServices.updateMember(rcimTestData.getSuperAdminToken(), memberDto.getId(), new MemberUpdateDto());
            Assert.fail("Member update with missing mandatory fields");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_UPDATE,
                                    ADDRESS_MAY_NOT_BE_NULL, FIRST_NAME_MAY_NOT_BE_EMPTY, LAST_NAME_MAY_NOT_BE_EMPTY,
                                    PHONE_NUMBER_MAY_NOT_BE_NULL, LOCALE_MAY_NOT_BE_NULL)));
        }
    }

    @Test(dataProvider = "memberUpdateMissingMandatoryField", description = "This test verifies that request to update an existing" +
            " member with one missing mandatory field triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- name, address, phoneNumber and locale")
    public void memberUpdateWithOneMissingMandatoryFieldTest(MemberUpdateDto memberUpdateDto, Set<String> validationErrors) throws IOException {
        try {
            nissanBeServices.updateMember(rcimTestData.getSuperAdminToken(), memberDto.getId(), memberUpdateDto);
            Assert.fail("Member created with one missing mandatory field");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null, validationErrors));
        }
    }

    @Test(description = "This test verifies if an invalid memberId is introduced the update request triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 404 Bad request with the following message: No member found for the given id")
    public void memberUpdateWithInvalidMemberIdTest() throws IOException {
        UUID invalidId = UUID.randomUUID();
        try {
            nissanBeServices.updateMember(rcimTestData.getSuperAdminToken(), invalidId, memberUpdateDto);
            Assert.fail("User is not warned that there is no member with the introduced Id");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), "No member found for id " + invalidId, null));
        }
    }


    @DataProvider
    Object[][] memberUpdateMissingMandatoryField() {
        MemberUpdateDto memberUpdateDto = NissanDtoBuilders.buildMemberUpdateDto(nissanUtils.createArrayOfValidFiles(6),
                NissanDtoBuilders.buildDrivingLicenceDto(nissanUtils.createArrayOfValidFiles(1).get(0)));
        Cloner cloningMachine = new Cloner();

        MemberUpdateDto noAddress = cloningMachine.deepClone(memberUpdateDto);
        noAddress.setAddress(null);

        MemberUpdateDto noLastName = cloningMachine.deepClone(memberUpdateDto);
        noLastName.setLastName(null);

        MemberUpdateDto noFirstName = cloningMachine.deepClone(memberUpdateDto);
        noFirstName.setFirstName(null);

        MemberUpdateDto noPhoneNumber = cloningMachine.deepClone(memberUpdateDto);
        noPhoneNumber.setPhoneNumber(null);

        MemberUpdateDto noLocale = cloningMachine.deepClone(memberUpdateDto);
        noLocale.setLocale(null);
        return new Object[][]{
                {noAddress, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_UPDATE,
                        ADDRESS_MAY_NOT_BE_NULL)},
                {noLastName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_UPDATE,
                        LAST_NAME_MAY_NOT_BE_EMPTY)},
                {noFirstName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_UPDATE,
                        FIRST_NAME_MAY_NOT_BE_EMPTY)},
                {noPhoneNumber, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_UPDATE,
                        PHONE_NUMBER_MAY_NOT_BE_NULL)},
                {noLocale, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_UPDATE,
                        LOCALE_MAY_NOT_BE_NULL)}
        };
    }
}
