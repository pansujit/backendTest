package com.pitechplus.rcim.nissan.be.members.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.rits.cloning.Cloner;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga  on 27.07.2017.
 */
public class AddMembersNegativeTests extends BackendAbstract {


    @Test(description = "This test verifies that create member call with invalid  X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {

        try {
            nissanBeServices.createMember(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(),
                    NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getSuperCompanyDto().getId().toString(),
                            configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId()));
            Assert.fail("Member created with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }


    @Test(description = "This test verifies that request to create a new member with all missing mandatory fields triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- superCompanyId, email, password, name, address, phoneNumber and locale")
    public void missingMandatoryFieldsTest() throws IOException {
        try {
            nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), new MemberCreateDto());
            Assert.fail("Member created with missing mandatory fields");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                                    COMPANYID_MAY_NOT_BE_NULL, LOGIN_MAY_NOT_BE_EMPTY, PASSWORD_IS_NOT_PROPERLY_FORMED, ADDRESS_MAY_NOT_BE_NULL,
                                    FIRST_NAME_MAY_NOT_BE_EMPTY, LAST_NAME_MAY_NOT_BE_EMPTY, PHONE_NUMBER_MAY_NOT_BE_NULL, LOCALE_MAY_NOT_BE_NULL)));
        }
    }

    @Test(dataProvider = "memberCreateMissingMandatoryField", description = "This test verifies that request to create a new" +
            " member with one missing mandatory field triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- superCompanyId, email, password, name, address, phoneNumber and locale")
    public void oneMissingMandatoryFieldTest(MemberCreateDto memberCreateDto, Set<String> validationErrors) throws IOException {
        try {
            nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto);
            Assert.fail("Member created with one missing mandatory field");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null, validationErrors));
        }
    }

    @Test(description = "This test verifies that two members cannot be created with the same email address.")
    @TestInfo(expectedResult = "Server responds with 422 Unprocessable Entity error.")
    public void existingEmailAddressTest() throws IOException {

        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto);

        Cloner cloningMachine = new Cloner();
        MemberCreateDto sameEmailAddress = cloningMachine.deepClone(memberCreateDto);
        sameEmailAddress.setLogin(memberCreateDto.getLogin());

        try {
            nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto);
            Assert.fail("Member created with an existing email address");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw the correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "The user login already exists", null));
        }
    }

    @Test(dataProvider = "invalidEmailFormat", description = "This test verifies that a member cannot be created with a wrong format email address.")
    @TestInfo(expectedResult = " Server responds with 400 Bad Request error.")
    public void invalidEmailFormatTest(String login) throws IOException {
        try {
            MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                    configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
            memberCreateDto.setLogin(login);
            nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto);
            Assert.fail("Member created with an email with wrong format");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE, EMAIL_WRONG_FORMAT)));
        }
    }


    @Test(dataProvider = "invalidPasswordFormat", description = "This test verifies that a member cannot be created with a password with wrong format.")
    @TestInfo(expectedResult = " Server responds with 400 Bad Request error.")
    public void invalidPasswordFormat(String password) throws IOException {
        try {
            MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                    configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
            memberCreateDto.setPassword(password);
            nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto);
            Assert.fail("Member created with a password with wrong format");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE, PASSWORD_IS_NOT_PROPERLY_FORMED)));
        }
    }

    @Test(description = "This test verifies that a member cannot be created if there was no superCompany with the Id introduced.")
    @TestInfo(expectedResult = " Server responds with 400 Bad Request error.")
    public void incorrectCompanyIdTest() throws IOException {

        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        memberCreateDto.setCompanyId(UUID.randomUUID());

        try {
            nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto);
            Assert.fail("Member created with an unexisting companiID");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "No company found for id " + memberCreateDto.getCompanyId(), null));
        }
    }


    @Test(description = "This test verifies that request to create company with invalid phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void invalidPhoneNumberTest() throws IOException {
        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        memberCreateDto.getPhoneNumber().setNationalNumber(RandomStringUtils.random(5));
        try {
            nissanBeServices.createMember(rcimTestData.getAdminToken(), memberCreateDto);
            Assert.fail("Member created with invalid phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE, PHONE_NUMBER_IS_INVALID)));
        }
    }


    @DataProvider
    Object[][] memberCreateMissingMandatoryField() {
        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        Cloner cloningMachine = new Cloner();
        MemberCreateDto noCompanyId = cloningMachine.deepClone(memberCreateDto);
        noCompanyId.setCompanyId(null);

        MemberCreateDto noEmailAddress = cloningMachine.deepClone(memberCreateDto);
        noEmailAddress.setLogin(null);

        MemberCreateDto noPassword = cloningMachine.deepClone(memberCreateDto);
        noPassword.setPassword(null);

        MemberCreateDto noAddress = cloningMachine.deepClone(memberCreateDto);
        noAddress.setAddress(null);

        MemberCreateDto noLastName = cloningMachine.deepClone(memberCreateDto);
        noLastName.setLastName(null);

        MemberCreateDto noFirstName = cloningMachine.deepClone(memberCreateDto);
        noFirstName.setFirstName(null);

        MemberCreateDto noPhoneNumber = cloningMachine.deepClone(memberCreateDto);
        noPhoneNumber.setPhoneNumber(null);

        MemberCreateDto noLocale = cloningMachine.deepClone(memberCreateDto);
        noLocale.setLocale(null);
        return new Object[][]{
                {noCompanyId, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        COMPANYID_MAY_NOT_BE_NULL)},
                {noEmailAddress, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        LOGIN_MAY_NOT_BE_EMPTY)},
                {noPassword, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        PASSWORD_IS_NOT_PROPERLY_FORMED)},
                {noAddress, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        ADDRESS_MAY_NOT_BE_NULL)},
                {noLastName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        LAST_NAME_MAY_NOT_BE_EMPTY)},
                {noFirstName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        FIRST_NAME_MAY_NOT_BE_EMPTY)},
                {noPhoneNumber, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        PHONE_NUMBER_MAY_NOT_BE_NULL)},
                {noLocale, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.MEMBER_CREATE,
                        LOCALE_MAY_NOT_BE_NULL)}
        };
    }


    @DataProvider
    Object[][] invalidEmailFormat() {

        return new Object[][]{
                {"nissanTest.gmail.com"},
                {"nissa@nTe@st@gmail.com"},
                {"ab(c)d,e:f;g<h>i[jk]l@example.com"},
                {"nissan@"},
                {"@gmail.com"},
                {"nissanTesting@gmail..com"},
                {"nissantesting@g mail.com"},
                {"nissantes ting@gmail.com"},
                {"nissantesting@gmail.com."}
        };
    }


    @DataProvider
    Object[][] invalidPasswordFormat() {

        return new Object[][]{
                {"11111111"},
                {"AAAAAAAA"},
                {"aaaaaaaa"},
                {"1111aaaa"},
                {"1234AAAA"},
        };
    }
}