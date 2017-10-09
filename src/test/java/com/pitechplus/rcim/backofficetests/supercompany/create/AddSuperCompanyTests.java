package com.pitechplus.rcim.backofficetests.supercompany.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.Test;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static com.pitechplus.rcim.backoffice.utils.mappers.CompanyMapper.mapSuperCompanyDtoToCreate;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 31.05.2017.
 */
public class AddSuperCompanyTests extends BackendAbstract {

    @Test(description = "This test verifies that call to create super company works accordingly.")
    @TestInfo(expectedResult = "Super company created with correct information which was given on request.")
    public void createSuperCompanyTest() {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        SuperCompanyDto superCompanyDto = companyService.createSuperCompany(rcimTestData.getAdminToken(),
                superCompanyCreate).getBody();
        assertThat("Information given on response to add super company does not reflect request !",
                mapSuperCompanyDtoToCreate(superCompanyDto), samePropertyValuesAs((superCompanyCreate)));
    }
}
