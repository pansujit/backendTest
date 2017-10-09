package com.pitechplus.rcim.backofficetests.backuser.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.data.enums.ValidationError;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserUpdate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.NO_BACK_USER_FOUND;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.PHONE_NUMBER_IS_INVALID;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.SECONDARY_PHONE_NUMBER_IS_INVALID;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 28.02.2017.
 */
public class UpdateBackUserNegativeTests extends BackendAbstract {

    private UUID backProfileId;

    @BeforeClass
    public void addBackUser() {
        BackUserCreate backUserCreate = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        ResponseEntity<BackUser> backUserResponseEntity = backUserService.createBackUser(rcimTestData.getAdminToken(),
                backUserCreate);
        backProfileId = backUserResponseEntity.getBody().getId();
    }

    @Test(description = "This test verifies that request to update a back office user with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void updateBackUserInvalidXAuthTokenTest() throws IOException {
        BackUserUpdate backUserUpdate = DtoBuilders.buildBackUserUpdate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        try {
            backUserService.updateBackUser("Invalid X-AUTH-TOKEN", backProfileId, backUserUpdate);
            Assert.fail("User in back office was updated with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to update a back office user with all missing mandatory fields " +
            "triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- firstName, lastName, address, locale, phoneNumber")
    public void updateBackUserWithAllMissingMandatoryFieldsTest() throws IOException {
        try {
            backUserService.updateBackUser(rcimTestData.getAdminToken(), backProfileId, new BackUserUpdate());
            Assert.fail("User in back office was updated with all missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_UPDATE_DTO,
                                    ValidationError.FIRST_NAME_MAY_NOT_BE_EMPTY, ValidationError.PHONE_NUMBER_MAY_NOT_BE_NULL,
                                    ValidationError.ADDRESS_MAY_NOT_BE_NULL, ValidationError.LAST_NAME_MAY_NOT_BE_EMPTY,
                                    ValidationError.LOCALE_MAY_NOT_BE_NULL)));
        }
    }

    @Test(description = "This test verifies that calling update back user service with invalid back user id triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found and developerMessage: " + NO_BACK_USER_FOUND + "{invalidId}")
    public void updateBackUserInvalidBackUserIdTest() throws IOException {
        UUID invalidId = new UUID(1, 10);
        BackUserUpdate backUserUpdate = DtoBuilders.buildBackUserUpdate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        try {
            backUserService.updateBackUser(rcimTestData.getAdminToken(), invalidId, backUserUpdate);
            Assert.fail("User in back office was updated with invalid back user id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            NO_BACK_USER_FOUND + invalidId.toString(), null));
        }
    }

    @Test(description = "This test verifies that call to update back user with invalid phone number triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void updateBackUserInvalidPhoneNumberTest() throws IOException {
        BackUserUpdate backUserUpdate = DtoBuilders.buildBackUserUpdate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        backUserUpdate.getPhoneNumber().setNationalNumber("invalid");
        try {
            backUserService.updateBackUser(rcimTestData.getAdminToken(), backProfileId, backUserUpdate);
            Assert.fail("User in back office was added with invalid Phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_UPDATE_DTO, PHONE_NUMBER_IS_INVALID)));
        }
    }

    @Test(description = "This test verifies that call to update back user with invalid secondary phone number triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid secondaryPhoneNumber")
    public void updateBackUserInvalidSecondaryPhoneNumberTest() throws IOException {
        BackUserUpdate backUserUpdate = DtoBuilders.buildBackUserUpdate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        backUserUpdate.getSecondaryPhoneNumber().setNationalNumber("invalid");
        try {
            backUserService.updateBackUser(rcimTestData.getAdminToken(), backProfileId, backUserUpdate);
            Assert.fail("User in back office was added with invalid secondary Phone Number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_UPDATE_DTO, SECONDARY_PHONE_NUMBER_IS_INVALID)));
        }
    }

    @Test(description = "This test verifies that call to update back user with invalid company id triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: No company found for this id")
    public void updateBackUserInvalidCompanyIdTest() throws IOException {
        BackUserUpdate backUserUpdate = DtoBuilders.buildBackUserUpdate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        UUID invalidCompanyId = new UUID(1, 10);
        backUserUpdate.setCompanyId(invalidCompanyId);
        try {
            backUserService.updateBackUser(rcimTestData.getAdminToken(), backProfileId, backUserUpdate);
            Assert.fail("User in back office was added with invalid company id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "No company found for this id", null));
        }
    }

}
