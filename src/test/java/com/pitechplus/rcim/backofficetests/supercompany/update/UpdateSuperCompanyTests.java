package com.pitechplus.rcim.backofficetests.supercompany.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.search.companyconfig.ConfigurationQueryDto;
import com.pitechplus.rcim.backoffice.dto.search.companyconfig.SearchCompanyConfigurations;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static com.pitechplus.rcim.backoffice.utils.mappers.CompanyMapper.mapSuperCompanyDtoToCreate;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 07.06.2017.
 */
public class UpdateSuperCompanyTests extends BackendAbstract {

    private UUID superCompanyId;

    @BeforeClass
    public void addSuperCompany() {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        superCompanyId = companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate).getBody().getId();
    }

    @Test(description = "This test verifies that super company can be updated.")
    @TestInfo(expectedResult = "Super company after update contains correct information given on request to update.")
    public void updateSuperCompanyTest() {
        List<String> templates = configsService.getTemplates(rcimTestData.getAdminToken());
        String randomTemplate = templates.get(NumberGenerator.randInt(0, templates.size() - 1));
        SearchCompanyConfigurations searchCompanyConfigurations = companyService.searchCompanyConfigurations(rcimTestData.getAdminToken(),
                new ConfigurationQueryDto(new Page(0, 30))).getBody();
        UUID randomConfigId = searchCompanyConfigurations.getResults().get(NumberGenerator.randInt(0,
                searchCompanyConfigurations.getResults().size() - 1)).getId();
        SuperCompanyCreate updateSuperCompany = DtoBuilders.buildSuperCompanyCreate(randomConfigId, randomTemplate);
        SuperCompanyDto updateSuperCompanyResponse = companyService.updateSuperCompany(rcimTestData.getSuperAdminToken(),
                superCompanyId, updateSuperCompany).getBody();
        assertThat("Information given on response to update super company does not reflect request !",
                mapSuperCompanyDtoToCreate(updateSuperCompanyResponse), samePropertyValuesAs((updateSuperCompany)));
    }

}
