package com.pitechplus.rcim.backofficetests.parking.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 17.07.2017.
 */
public class GetParkingTests extends BackendAbstract {

    private ParkingDto addedParking;

    @BeforeClass(description = "Create a parking for a site")
    public void createParking() {
        addedParking = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                DtoBuilders.buildParkingCreate()).getBody();
    }

    @Test(description = "This test verifies that retrieve single parking by id works accordingly.")
    @TestInfo(expectedResult = "Expected parking is retrieved with correct information.")
    public void getParkingTest() {
        ParkingDto retrievedParking = parkingService.getParking(rcimTestData.getSuperAdminToken(), addedParking.getId()).getBody();
        assertThat("Information given on response to get parking does not bring correct information!", retrievedParking,
                PropertyValuesMatcher.samePropertyValuesAs(addedParking));
    }

    @Test(description = "This test verifies that retrieve parking call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void getParkingInvalidXAuthTest() throws IOException {
        try {
            parkingService.getParking(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(), addedParking.getId());
            Assert.fail("Parking retrieved with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that retrieve parking call with invalid parking id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developer message: HTTP 404 Not Found")
    public void getParkingInvalidParkingIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            parkingService.getParking(rcimTestData.getSuperAdminToken(), invalidId);
            Assert.fail("Parking retrieved with invalid parking id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Parking not found for id " + invalidId, null));
        }
    }
}
