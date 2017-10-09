package com.pitechplus.rcim.backofficetests.vehicle.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleCreate;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 21.07.2017.
 */
public class UpdateVehicleTests extends BackendAbstract {

    private UUID vehicleId;

    @BeforeClass
    public void addVehicle() {
        vehicleId = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(),
                DtoBuilders.buildVehicleCreate(rcimTestData)).getBody().getId();
    }

    @Test(description = "This test verifies that update vehicle works accordingly.")
    @TestInfo(expectedResult = "Vehicle is updated with correct information from request body.")
    public void updateVehicleTest() {
        VehicleCreate updateVehicle = DtoBuilders.buildVehicleCreate(rcimTestData);
        VehicleDto serverVehicle = vehicleService.updateVehicle(rcimTestData.getSuperAdminToken(), vehicleId, updateVehicle).getBody();
        assertThat("Information given on response to update vehicle does not reflect request !", serverVehicle,
                is(DtoBuilders.buildExpectedVehicleDto(updateVehicle, rcimTestData)));
    }
}
