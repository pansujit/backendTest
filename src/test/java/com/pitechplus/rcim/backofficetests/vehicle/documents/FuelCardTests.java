package com.pitechplus.rcim.backofficetests.vehicle.documents;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.vehicle.FuelCardDto;
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
public class FuelCardTests extends BackendAbstract {

    private UUID vehicleId;

    @BeforeMethod
    public void addVehicle() {
        vehicleId = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(),
                DtoBuilders.buildVehicleCreate(rcimTestData)).getBody().getId();
    }

    @Test(description = "This test verifies that create one fuel card works accordingly.")
    @TestInfo(expectedResult = "Service responds with created fuel card with correct information which was given on request.")
    public void createOneFuelCardTest() {
        FuelCardDto fuelCardToCreate = DtoBuilders.buildFuelCard(0, 12);
        FuelCardDto createdFuelCard = vehicleService.createFuelCardForVehicle(rcimTestData.getSuperAdminToken(), vehicleId,
                fuelCardToCreate).getBody();
        assertThat("Fuel card does not have an id!", createdFuelCard.getId(), is(notNullValue()));
        assertThat("Fuel card not created accordingly", createdFuelCard, is(fuelCardToCreate));
    }

    @Test(description = "This test verifies that multiple fuel cards can be created for same vehicle.")
    @TestInfo(expectedResult = "All fuel cards are created correctly with information which was given on create requests.")
    public void createMultipleFuelCardsTest() {
        Set<FuelCardDto> addedFuelCards = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(2, 5); i++) {
            FuelCardDto fuelCardToCreate = DtoBuilders.buildFuelCard(i, i + 1);
            vehicleService.createFuelCardForVehicle(rcimTestData.getSuperAdminToken(), vehicleId,
                    fuelCardToCreate);
            addedFuelCards.add(fuelCardToCreate);
        }
        Set<FuelCardDto> vehicleFuelCards = new HashSet<>(Arrays.asList(vehicleService.getFuelCardsForVehicle(rcimTestData.getSuperAdminToken(),
                vehicleId).getBody()));
        assertThat("fuel Cards were not all created accordingly!!", vehicleFuelCards, is(addedFuelCards));
    }

    @Test(description = "This test verifies that fuel card for a vehicle can not be created in period which overlaps another " +
            "fuel card for the same vehicle.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: A fuel card already exists " +
            "for this date interval.")
    public void createSamePeriodTest() throws IOException {
        FuelCardDto firstFuelCard = DtoBuilders.buildFuelCard(0, 2);
        FuelCardDto secondFuelCard = DtoBuilders.buildFuelCard(1, 3);
        vehicleService.createFuelCardForVehicle(rcimTestData.getSuperAdminToken(), vehicleId, firstFuelCard);
        try {
            vehicleService.createFuelCardForVehicle(rcimTestData.getSuperAdminToken(), vehicleId, secondFuelCard);
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "A fuel card already exists for this date interval.", null));
        }
    }
}
