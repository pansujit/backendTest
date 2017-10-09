package com.pitechplus.rcim.backofficetests.vehicle.documents;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.vehicle.AutolibCardDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by dgliga on 17.08.2017.
 */
public class AutolibTests extends BackendAbstract {
    private UUID vehicleId;

    @BeforeMethod
    public void addVehicle() {
        vehicleId = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(),
                DtoBuilders.buildVehicleCreate(rcimTestData)).getBody().getId();
    }

    @Test(description = "This test verifies that create one auto lib card works accordingly.")
    @TestInfo(expectedResult = "Service responds with created auto lib card with correct information which was given on request.")
    public void createOneAutolibTest() {
        AutolibCardDto autolibToCreate = DtoBuilders.buildAutolibCard(0, 12);
        AutolibCardDto autolibCreated = vehicleService.createAutolibCardForVehicle(rcimTestData.getSuperAdminToken(),
                vehicleId, autolibToCreate).getBody();
        assertThat("Autolib card does not have an id!", autolibCreated.getId(), is(notNullValue()));
        assertThat("Autolib card not created accordingly", autolibCreated, is(autolibToCreate));
    }

    @Test(description = "This test verifies that multiple auto lib contracts can be created for same vehicle.")
    @TestInfo(expectedResult = "All auto lib cards are created correctly with information which was given on create requests.")
    public void createMultipleAutolibTest() {
        Set<AutolibCardDto> addedAutoLibCards = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(2, 5); i++) {
            AutolibCardDto autolibCard = DtoBuilders.buildAutolibCard(i, i + 1);
            vehicleService.createAutolibCardForVehicle(rcimTestData.getSuperAdminToken(), vehicleId, autolibCard);
            addedAutoLibCards.add(autolibCard);
        }
        Set<AutolibCardDto> vehicleAutoLibs = new HashSet<>(Arrays.asList(vehicleService.getAutoLibsForVehicle(rcimTestData.getSuperAdminToken(),
                vehicleId).getBody()));
        assertThat("Auto lib cards were not all created accordingly!!", vehicleAutoLibs, is(addedAutoLibCards));
    }

    @Test(description = "This test verifies that auto lib card for a vehicle can not be created in period which overlaps another " +
            "auto lib card for the same vehicle.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: An autolib card already exists " +
            "for this date interval.")
    public void createSamePeriodTest() throws IOException {
        AutolibCardDto firstAutolib = DtoBuilders.buildAutolibCard(0, 2);
        AutolibCardDto secondAutolib = DtoBuilders.buildAutolibCard(1, 3);
        vehicleService.createAutolibCardForVehicle(rcimTestData.getSuperAdminToken(), vehicleId, firstAutolib);
        try {
            vehicleService.createAutolibCardForVehicle(rcimTestData.getSuperAdminToken(), vehicleId, secondAutolib);
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "An autolib card already exists for this date interval.", null));
        }
    }


}
