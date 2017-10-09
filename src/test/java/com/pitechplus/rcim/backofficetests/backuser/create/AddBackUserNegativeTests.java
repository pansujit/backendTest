package com.pitechplus.rcim.backofficetests.backuser.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.COUNTRY_IS_INVALID;
import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.ROLE_IS_INVALID;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 21.02.2017.
 */
public class AddBackUserNegativeTests extends BackendAbstract {


    @Test(description = "This test verifies that request to add a back office user with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void addBackOfficeUserWithInvalidXAuthTest() throws IOException {
        BackUserCreate addBoUserDto = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        try {
            backUserService.createBackUser("Invalid xAuth", addBoUserDto);
            Assert.fail("User in back office was added with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to add a back office user with all missing mandatory fields triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- login, firstName, lastName, address, locale, phoneNumber")
    public void addBackOfficeUserWithAllMissingMandatoryFieldsTest() throws IOException {
        try {
            backUserService.createBackUser(rcimTestData.getAdminToken(), new BackUserCreate());
            Assert.fail("User in back office was added with missing mandatory fields");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, LOGIN_MAY_NOT_BE_EMPTY,
                                    FIRST_NAME_MAY_NOT_BE_EMPTY, LAST_NAME_MAY_NOT_BE_EMPTY, ADDRESS_MAY_NOT_BE_NULL, LOCALE_MAY_NOT_BE_NULL,
                                    PHONE_NUMBER_MAY_NOT_BE_NULL)));
        }
    }

    @Test(dataProvider = "backUserCreateMissingMandatoryFields", description = "This test verifies that request to add a back office " +
            "user with one missing mandatory field triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors: depending on which field is missing " +
            "the correct error/errors is/are checked.")
    public void addBackOfficeUserWithMissingMandatoryFieldTest(BackUserCreate boUserDto, Set<String> validationErrors) throws IOException {
        try {
            backUserService.createBackUser(rcimTestData.getAdminToken(), boUserDto);
            Assert.fail("User in back office was added with missing mandatory field");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, validationErrors));
        }
    }

    @Test(description = "This test verifies that request to add a back office user with invalid phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void addBackOfficeUserWithInvalidPhoneNumberTest() throws IOException {
        BackUserCreate boUserDto = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        boUserDto.getPhoneNumber().setNationalNumber("invalid");
        try {
            backUserService.createBackUser(rcimTestData.getAdminToken(), boUserDto);
            Assert.fail("User in back office was added with missing mandatory fields");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, PHONE_NUMBER_IS_INVALID)));
        }
    }

    //todo: enable test when/if issue is fixed
    @Test(enabled = false, description = "This test verifies that request to add a back office user with invalid country triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid country")
    public void addBackOfficeUserWithInvalidCountryTest() throws IOException {
        BackUserCreate boUserDto = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        boUserDto.getAddress().setCountry("Invalid");
        try {
            backUserService.createBackUser(rcimTestData.getAdminToken(), boUserDto);
            Assert.fail("User in back office was added with invalid country!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            new HashSet<>(Collections.singletonList(COUNTRY_IS_INVALID))));
        }
    }

    //todo: enable test when/if issue is fixed
    @Test(enabled = false, description = "This test verifies that request to add a back office user with invalid role triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid role")
    public void addBackOfficeUserWithInvalidRoleTest() throws IOException {
        BackUserCreate boUserDto = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_INVALID, rcimTestData.getAdminSuperCompanyId());
        try {
            backUserService.createBackUser(rcimTestData.getAdminToken(), boUserDto);
            Assert.fail("User in back office was added with invalid country!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            new HashSet<>(Collections.singletonList(ROLE_IS_INVALID))));
        }
    }

    @Test(description = "This test verifies that request to add a back office user with login which already exists triggers " +
            "correct error response from server.")
    @TestInfo(expectedResult = "Server responds with 422 Unprocessable Entity with developerMessage: The user login already exists")
    public void addBackUserWithAlreadyUsedLoginTest() throws IOException {
        BackUserCreate boUserDto = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        backUserService.createBackUser(rcimTestData.getAdminToken(), boUserDto);
        try {
            backUserService.createBackUser(rcimTestData.getAdminToken(), boUserDto.clone());
            Assert.fail("User in back office was added with login previously used!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "The user login already exists", null));
        }
    }


    /**
     * This data provider creates BackUserCreate objects, each with one missing mandatory field
     *
     * @return BackUserCreate objects
     */
    @DataProvider
    public Object[][] backUserCreateMissingMandatoryFields() {
        //create object with all fields
        BackUserCreate boUserDto = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        BackUserCreate noFirstName = boUserDto.clone();
        noFirstName.setFirstName(null);
        BackUserCreate noLastName = boUserDto.clone();
        noLastName.setLastName(null);
        BackUserCreate noLogin = boUserDto.clone();
        noLogin.setLogin(null);
        BackUserCreate noAddress = boUserDto.clone();
        noAddress.setAddress(null);
        BackUserCreate noPhone = boUserDto.clone();
        noPhone.setPhoneNumber(null);
        BackUserCreate noLocale = boUserDto.clone();
        noLocale.setLocale(null);
        BackUserCreate noCity = boUserDto.clone();
        noCity.getAddress().setCity(null);
        BackUserCreate noCountry = boUserDto.clone();
        noCountry.getAddress().setCountry(null);
        BackUserCreate noFormattedAddress = boUserDto.clone();
        noFormattedAddress.getAddress().setFormattedAddress(null);
        BackUserCreate noPostalCode = boUserDto.clone();
        noPostalCode.getAddress().setPostalCode(null);
        BackUserCreate noStreetName = boUserDto.clone();
        noStreetName.getAddress().setStreetName(null);
        BackUserCreate noStreetNumber = boUserDto.clone();
        noStreetNumber.getAddress().setStreetNumber(null);
        BackUserCreate noNationalNumber = boUserDto.clone();
        noNationalNumber.getPhoneNumber().setNationalNumber(null);
        BackUserCreate noCountryCode = boUserDto.clone();
        noCountryCode.getPhoneNumber().setCountryCode(null);


        return new Object[][]{
                {noFirstName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, FIRST_NAME_MAY_NOT_BE_EMPTY)},
                {noLastName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, LAST_NAME_MAY_NOT_BE_EMPTY)},
                {noLogin, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, LOGIN_MAY_NOT_BE_EMPTY)},
                {noAddress, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, ADDRESS_MAY_NOT_BE_NULL)},
                {noPhone, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, PHONE_NUMBER_MAY_NOT_BE_NULL)},
                {noLocale, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, LOCALE_MAY_NOT_BE_NULL)},
                {noFormattedAddress, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, FORMATTED_ADDRESS_MAY_NOT_BE_EMPTY)},
                {noNationalNumber, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, PHONE_NUMBER_IS_INVALID,
                        NATIONAL_NUMBER_MAY_NOT_BE_EMPTY)},
                {noCountryCode, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_CREATE_DTO, PHONE_NUMBER_IS_INVALID,
                        COUNTRY_CODE_MAY_NOT_BE_EMPTY)}
        };
    }

}
