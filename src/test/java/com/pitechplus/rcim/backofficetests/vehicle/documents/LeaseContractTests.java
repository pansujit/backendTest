package com.pitechplus.rcim.backofficetests.vehicle.documents;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.vehicle.LeaseContractDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
public class LeaseContractTests extends BackendAbstract {
    private UUID vehicleId;

    @BeforeMethod
    public void addVehicle() {
        vehicleId = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(),
                DtoBuilders.buildVehicleCreate(rcimTestData)).getBody().getId();
    }

    @Test(description = "This test verifies that create one lease contract works accordingly.")
    @TestInfo(expectedResult = "Service responds with created lease contract with correct information which was given on request.")
    public void createLeaseContractTest() {
        LeaseContractDto leaseContractCreate = DtoBuilders.buildLeaseContract(0, 12);
        LeaseContractDto createdLeaseContract = vehicleService.createLeaseContract(rcimTestData.getSuperAdminToken(),
                vehicleId, leaseContractCreate).getBody();
        assertThat("Lease contract does not have an id!", createdLeaseContract.getId(), is(notNullValue()));
        assertThat("Lease contract not created accordingly", createdLeaseContract, is(leaseContractCreate));
    }

    @Test(description = "This test verifies that multiple lease contracts can be created for same vehicle.")
    @TestInfo(expectedResult = "All lease contracts are created correctly with information which was given on create requests.")
    public void createMultipleLeaseContractsTest() {
        Set<LeaseContractDto> addedLeaseContracts = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(2, 5); i++) {
            LeaseContractDto leaseContractCreate = DtoBuilders.buildLeaseContract(0, 12);
            vehicleService.createLeaseContract(rcimTestData.getSuperAdminToken(), vehicleId, leaseContractCreate).getBody();
            addedLeaseContracts.add(leaseContractCreate);
        }
        Set<LeaseContractDto> vehicleLeaseContracts = new HashSet<>(Arrays.asList(vehicleService.getLeaseContracts(rcimTestData.getSuperAdminToken(),
                vehicleId).getBody()));
        assertThat("Lease contracts were not all created accordingly!!", vehicleLeaseContracts, is(addedLeaseContracts));
    }
}
