package com.pitechplus.rcim.backofficetests.company.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.backoffice.dto.company.CompanyCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.CompanyDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.BackendAbstract;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 30.06.2017.
 */
public class AddCompanyTests extends BackendAbstract {

    @Test(description = "This test method verifies that valid call to add company works accordingly")
    @TestInfo(expectedResult = "Company created with correct information which was given on request.")
    public void addCompanyTest() {
        CompanyCreateDto companyCreateDto = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        CompanyDto addedCompany = companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(),
                companyCreateDto).getBody();
        assertThat("Information given on response to add company does not reflect request !", addedCompany,
                is((DtoBuilders.buildExpectedCompanyDto(companyCreateDto, rcimTestData.getSuperCompanyDto().getId()))));
    }
}
