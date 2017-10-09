package com.pitechplus.rcim.backofficetests.supercompany.contract;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.supercompany.ContractDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
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
 * Created by dgliga on 05.07.2017.
 */
public class ContractCreateTests extends BackendAbstract {

    private UUID superCompanyId;

    @BeforeMethod(description = "This method creates a super company which we need in order to create contracts.")
    public void addSuperCompany() {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        superCompanyId = companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate).getBody().getId();
    }

    @Test(description = "This test verifies that valid request to create contract for a super company works accordingly.")
    @TestInfo(expectedResult = "Response from create contract request returns correct information which reflect the request " +
            "made and an id is assigned to the contract.")
    public void createOneContractTest() {
        ContractDto contractDto = DtoBuilders.buildContractDto(0, 1);
        ContractDto createdContract = companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId,
                contractDto).getBody();
        assertThat("Response from create contract does not reflect request made!", createdContract, is(contractDto));
        assertThat("Contract created does not have an id!", createdContract.getId(), is(notNullValue()));
    }

    @Test(description = "This test verifies that valid multiple requests to create a contract for a super company works accordingly.")
    @TestInfo(expectedResult = "All contracts created are attached to the company with correct information.")
    public void createMultipleContractsTest() {
        Set<ContractDto> contractDtos = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(2, 5); i++) {
            ContractDto contractDto = DtoBuilders.buildContractDto(i, i);
            companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, contractDto);
            contractDtos.add(contractDto);
        }
        Set<ContractDto> companyContracts = new HashSet<>(Arrays.asList(companyService.getCompanyContracts(rcimTestData.getSuperAdminToken(),
                superCompanyId).getBody()));
        assertThat("Contracts were not all created accordingly!!", companyContracts, is(contractDtos));
    }

}
