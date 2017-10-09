package com.pitechplus.rcim.backofficetests.vehicle.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleCreate;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by dgliga on 19.07.2017.
 */
public class CreateVehicleTests extends BackendAbstract {

    @Test(description = "This test verifies that create vehicle service works accordingly for valid request.")
    @TestInfo(expectedResult = "Vehicle is created and service responds with vehicle information as given on request.")
    public void createVehicleTest() {
        VehicleCreate vehicleCreate = DtoBuilders.buildVehicleCreate(rcimTestData);
        VehicleDto addedVehicle = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(), vehicleCreate).getBody();
        assertThat("Vehicle added does not have an id!", addedVehicle.getId(), is(notNullValue()));
        assertThat("Information given on response to add vehicle does not reflect request !", addedVehicle,
                is(DtoBuilders.buildExpectedVehicleDto(vehicleCreate, rcimTestData)));
    }


}
