package com.pitechplus.rcim.backofficetests.parking.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.company.ParkingCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 13.07.2017.
 */
public class CreateParkingTests extends BackendAbstract {

    @Test(description = "This test verifies that valid call to create parking for a site works accordingly.")
    @TestInfo(expectedResult = "Parking is created with information which reflects the request made.")
    public void createParkingTest() {
        ParkingCreateDto parkingCreateDto = DtoBuilders.buildParkingCreate();
        ParkingDto addedParking = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                parkingCreateDto).getBody();
        assertThat("Information given on response to add parking does not reflect request !", addedParking,
                is((DtoBuilders.buildExpectedParkingDto(parkingCreateDto, rcimTestData.getSiteDto()))));
    }
}
