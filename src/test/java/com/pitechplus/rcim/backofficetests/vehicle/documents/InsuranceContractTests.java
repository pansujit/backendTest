package com.pitechplus.rcim.backofficetests.vehicle.documents;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.vehicle.InsuranceContractDto;
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
public class InsuranceContractTests extends BackendAbstract {

    private UUID vehicleId;

    @BeforeMethod
    public void addVehicle() {
        vehicleId = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(),
                DtoBuilders.buildVehicleCreate(rcimTestData)).getBody().getId();
    }

    @Test(description = "This test verifies that create one insurance contract works accordingly.")
    @TestInfo(expectedResult = "Service responds with created insurance contract with correct information which was given on request.")
    public void createOneInsuranceContractTest() {
        UUID fileId = configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId();
        InsuranceContractDto insuranceContractCreate = DtoBuilders.buildInsuranceContract(0, 12, fileId);
        InsuranceContractDto createdInsuranceContract = vehicleService.createInsuranceContract(rcimTestData.getSuperAdminToken(),
                vehicleId, insuranceContractCreate).getBody();
        assertThat("Insurance contract does not have an id!", createdInsuranceContract.getId(), is(notNullValue()));
        assertThat("Insurance contract not created accordingly", createdInsuranceContract, is(insuranceContractCreate));
    }

    @Test(description = "This test verifies that multiple insurance contracts can be created for same vehicle.")
    @TestInfo(expectedResult = "All insurance contracts are created correctly with information which was given on create requests.")
    public void createMultipleInsuranceContractsTest() {
        Set<InsuranceContractDto> addedInsuranceContracts = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(2, 5); i++) {
            UUID fileId = configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId();
            InsuranceContractDto insuranceContractCreate = DtoBuilders.buildInsuranceContract(0, 12, fileId);
            vehicleService.createInsuranceContract(rcimTestData.getSuperAdminToken(), vehicleId, insuranceContractCreate).getBody();
            addedInsuranceContracts.add(insuranceContractCreate);
        }
        Set<InsuranceContractDto> vehicleInsuranceContracts = new HashSet<>(Arrays.asList(vehicleService.getInsuranceContracts(rcimTestData.getSuperAdminToken(),
                vehicleId).getBody()));
        assertThat("Insurance contracts were not all created accordingly!!", vehicleInsuranceContracts, is(addedInsuranceContracts));
    }

}
