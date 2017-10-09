package com.pitechplus.rcim.backofficetests.vehicle.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by dgliga on 19.07.2017.
 */
public class GetVehiclesTests extends BackendAbstract {

    private VehicleDto vehicleCreated;

    @BeforeClass
    public void addVehicle() {
        vehicleCreated = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(),
                DtoBuilders.buildVehicleCreate(rcimTestData)).getBody();
    }

    @Test(description = "This test verifies that service to get all vehicles works accordingly.")
    @TestInfo(expectedResult = "Vehicle entities are returned and the one created by us is included in the list.")
    public void getAllVehiclesTest() {
        Set<VehicleDto> vehicleDtos = new HashSet<>(Arrays.asList(vehicleService.getVehicles(rcimTestData.getSuperAdminToken())
                .getBody()));
        assertThat("No Vehicle was retrieved!", vehicleDtos.size(), is(greaterThan(0)));
        assertThat("Vehicle Created was not in the list!", vehicleDtos, hasItem(vehicleCreated));
    }

    @Test(description = "This test verifies that service to get vehicle works accordingly.")
    @TestInfo(expectedResult = "Vehicle returned is the one expected with correct information.")
    public void getVehicleTest() {
        VehicleDto serverVehicle = vehicleService.getVehicle(rcimTestData.getSuperAdminToken(), vehicleCreated.getId()).getBody();
        assertThat("Vehicle retrieved is not the expected one!", serverVehicle, is(vehicleCreated));
    }
}
