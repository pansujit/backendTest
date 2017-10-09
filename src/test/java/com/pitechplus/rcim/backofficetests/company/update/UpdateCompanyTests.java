package com.pitechplus.rcim.backofficetests.company.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.company.CompanyCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.CompanyDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 04.07.2017.
 */
public class UpdateCompanyTests extends BackendAbstract {

    private UUID companyId;

    @BeforeClass(description = "This method creates a company needed in order to call update company service.")
    public void createCompany() {
        companyId = companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(),
                DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId())).getBody().getId();
    }

    @Test(description = "This test verifies that company can be updated.")
    @TestInfo(expectedResult = "Company after update contains correct information given on request to update.")
    public void updateCompanyTest() {
        CompanyCreateDto companyUpdate = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        CompanyDto companyAfterUpdate = companyService.updateCompany(rcimTestData.getSuperAdminToken(), companyId,
                companyUpdate).getBody();
        assertThat("Information given on response to update company does not reflect request !", companyAfterUpdate,
                is((DtoBuilders.buildExpectedCompanyDto(companyUpdate, rcimTestData.getSuperCompanyDto().getId()))));
    }

}
