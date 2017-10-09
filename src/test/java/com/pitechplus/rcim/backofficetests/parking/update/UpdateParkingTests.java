package com.pitechplus.rcim.backofficetests.parking.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.company.ParkingCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 17.07.2017.
 */
public class UpdateParkingTests extends BackendAbstract {

    private UUID parkingId;
    private ParkingCreateDto updateParking;

    @BeforeClass(description = "Create a site for a company")
    public void createParking() {
        parkingId = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                DtoBuilders.buildParkingCreate()).getBody().getId();
        updateParking = DtoBuilders.buildParkingCreate();
    }

    @Test(description = "This test verifies that valid call to update parking works accordingly")
    @TestInfo(expectedResult = "Parking is updated with new information according to request made.")
    public void updateParkingTest() {
        ParkingDto parkingAfterUpdate = parkingService.updateParking(rcimTestData.getSuperAdminToken(), parkingId,
                updateParking).getBody();
        assertThat("Information given on response to update parking does not reflect request !", parkingAfterUpdate,
                is((DtoBuilders.buildExpectedParkingDto(updateParking, rcimTestData.getSiteDto()))));
    }

    @Test(description = "This test verifies that update parking call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidXAuthTest() throws IOException {
        try {
            parkingService.updateParking(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(),
                    parkingId, updateParking);
            Assert.fail("Parking updated with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that update parking call with invalid parking id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developer message: No parking found")
    public void invalidParkingIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            parkingService.updateParking(rcimTestData.getSuperAdminToken(), invalidId, updateParking);
            Assert.fail("Parking updated with invalid parking id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Parking not found for id " + invalidId, null));
        }
    }

    @Test(description = "This test verifies that update parking call with missing mandatory fields triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- coordinates and name")
    public void missingAllMandatoryFieldsTest() throws IOException {
        try {
            parkingService.updateParking(rcimTestData.getSuperAdminToken(), parkingId, new ParkingCreateDto());
            Assert.fail("Parking updated with missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.PARKING_UPDATE,
                                    COORDINATES_MAY_NOT_BE_NULL, NAME_MAY_NOT_BE_EMPTY)));
        }
    }

    @Test(description = "This test verifies that update parking call with duplicate name for same company triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: A parking already exists with the name")
    public void updateDuplicateNameTest() throws IOException {
        ParkingCreateDto firstParking = DtoBuilders.buildParkingCreate();
        parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(), firstParking);
        UUID secondParkingId = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                DtoBuilders.buildParkingCreate()).getBody().getId();
        ParkingCreateDto updateSameName = DtoBuilders.buildParkingCreate();
        updateSameName.setName(firstParking.getName());

        try {
            parkingService.updateParking(rcimTestData.getSuperAdminToken(), secondParkingId, updateSameName);
            Assert.fail("Parking updated with duplicate name!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "A parking already exists with the name", null));
        }
    }

}
