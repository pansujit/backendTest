package com.pitechplus.rcim.backofficetests.vehicle.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleCreate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.rits.cloning.Cloner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 20.07.2017.
 */
public class CreateVehicleNegativeTests extends BackendAbstract {

    private VehicleCreate vehicleCreate;
    private Cloner cloningMachine;

    @BeforeClass
    public void createVehicleObject() {
        vehicleCreate = DtoBuilders.buildVehicleCreate(rcimTestData);
        cloningMachine = new Cloner();
    }

    @Test(description = "This test verifies that create vehicle call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        try {
            vehicleService.createVehicle(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(), vehicleCreate);
            Assert.fail("Vehicle created with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that create vehicle call with invalid company id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: The sub company and the super company don't match")
    public void invalidCompanyIdTest() throws IOException {
        VehicleCreate invalidCompanyId = cloningMachine.deepClone(vehicleCreate);
        invalidCompanyId.setCompanyId(UUID.randomUUID());
        try {
            vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), invalidCompanyId);
            Assert.fail("Vehicle created with invalid company id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "Company with id " + invalidCompanyId.getCompanyId() + " does not exist.", null));
        }
    }

    @Test(description = "This test verifies that create vehicle call with invalid category id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: HTTP 400 Bad Request")
    public void invalidCategoryIdTest() throws IOException {
        VehicleCreate invalidCategoryId = cloningMachine.deepClone(vehicleCreate);
        invalidCategoryId.setCategoryId(UUID.randomUUID());
        try {
            vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), invalidCategoryId);
            Assert.fail("Vehicle created with invalid category id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "No category found for id: " + invalidCategoryId.getCategoryId(), null));
        }
    }

    @Test(description = "This test verifies that create vehicle call with invalid version id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: HTTP 400 Bad Request")
    public void invalidVersionIdTest() throws IOException {
        VehicleCreate invalidVersionId = cloningMachine.deepClone(vehicleCreate);
        invalidVersionId.setVersionId(UUID.randomUUID());
        try {
            vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), invalidVersionId);
            Assert.fail("Vehicle created with invalid version id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "No version found for id: " + invalidVersionId.getVersionId(), null));
        }
    }

    @Test(description = "This test verifies that create vehicle call with invalid color id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: HTTP 400 Bad Request")
    public void invalidColorIdTest() throws IOException {
        VehicleCreate invalidColorId = cloningMachine.deepClone(vehicleCreate);
        invalidColorId.setColorId(UUID.randomUUID());
        try {
            vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), invalidColorId);
            Assert.fail("Vehicle created with invalid color id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "No color found for id: " + invalidColorId.getColorId(), null));
        }
    }

    @Test(description = "This test verifies that create vehicle call with invalid registration document id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: HTTP 400 Bad Request")
    public void invalidRegistrationDocumentIdTest() throws IOException {
        VehicleCreate invalidRegDocId = cloningMachine.deepClone(vehicleCreate);
        invalidRegDocId.setRegistrationDocumentId(UUID.randomUUID());
        try {
            vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), invalidRegDocId);
            Assert.fail("Vehicle created with invalid registration document id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "One of these files is not valid", null));
        }
    }

    @Test(description = "This test verifies that create vehicle call with duplicate vin triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: An other vehicle already " +
            "exists with the same VIN number")
    public void duplicateVinTest() throws IOException {
        VehicleCreate validVehicle = DtoBuilders.buildVehicleCreate(rcimTestData);
        vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), validVehicle);
        VehicleCreate duplicateVin = cloningMachine.deepClone(vehicleCreate);
        duplicateVin.setVin(validVehicle.getVin());
        try {
            vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), duplicateVin);
            Assert.fail("Vehicle created with duplicate vin!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "An other vehicle already exists with the same VIN number", null));
        }
    }

}
