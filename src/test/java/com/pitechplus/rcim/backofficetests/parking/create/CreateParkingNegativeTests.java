package com.pitechplus.rcim.backofficetests.parking.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.company.ParkingCreateDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.COORDINATES_MAY_NOT_BE_NULL;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.NAME_MAY_NOT_BE_EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 14.07.2017.
 */
public class CreateParkingNegativeTests extends BackendAbstract {

    private ParkingCreateDto parkingCreateDto;

    @BeforeClass(description = "Create a site for a company")
    public void createParkingObject() {
        parkingCreateDto = DtoBuilders.buildParkingCreate();
    }

    @Test(description = "This test verifies that create parking call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        try {
            parkingService.createParking(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(),
                    rcimTestData.getSiteDto().getId(), parkingCreateDto);
            Assert.fail("Parking created with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that create parking call with invalid side id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developer message: HTTP 400 Bad Request")
    public void invalidSiteIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            parkingService.createParking(rcimTestData.getSuperAdminToken(), invalidId, parkingCreateDto);
            Assert.fail("Parking created with invalid site id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Site not found for id " + invalidId, null));
        }
    }

    @Test(description = "This test verifies that create parking call with missing mandatory fields triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- coordinates and name")
    public void missingAllMandatoryFieldsTest() throws IOException {
        try {
            parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(), new ParkingCreateDto());
            Assert.fail("Parking created with missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.PARKING_CREATE,
                                    COORDINATES_MAY_NOT_BE_NULL, NAME_MAY_NOT_BE_EMPTY)));
        }
    }

    @Test(description = "This test verifies that create parking call with duplicate name for same company triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: A parking already exists with the name")
    public void duplicateNameTest() throws IOException {
        parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(), parkingCreateDto);
        try {
            parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(), parkingCreateDto);
            Assert.fail("Parking created with duplicate name!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "A parking already exists with the name", null));
        }
    }

}
