package com.pitechplus.rcim.backofficetests.site.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.company.SiteDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by dgliga on 06.07.2017.
 */
public class CreateSiteTests extends BackendAbstract {

    @Test(description = "This test method verifies that valid call to create one site for a company works accordingly")
    @TestInfo(expectedResult = "Site is created and assigned an id, with correct information reflecting create request.")
    public void createOneSiteTest() {
        UUID companyId = companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(),
                DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId())).getBody().getId();
        SiteDto createSiteDto = DtoBuilders.buildSiteDto(companyId);
        SiteDto addedSiteDto = siteService.createSite(rcimTestData.getSuperAdminToken(), companyId, createSiteDto).getBody();
        assertThat("Information given on response to create site does not reflect request made !", addedSiteDto, is((createSiteDto)));
        assertThat("Contract created does not have an id!", addedSiteDto.getId(), is(notNullValue()));

    }

    @Test(description = "This test verifies that creating multiple sites for a company works accordingly.")
    @TestInfo(expectedResult = "Using get Sites service check that sites created are the ones retrieved on the service.")
    public void createMultipleSitesTest() {
        UUID companyId = companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(),
                DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId())).getBody().getId();

        Set<SiteDto> siteDtos = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(2, 5); i++) {
            SiteDto createSiteDto = DtoBuilders.buildSiteDto(companyId);
            siteService.createSite(rcimTestData.getSuperAdminToken(), companyId, createSiteDto);
            siteDtos.add(createSiteDto);
        }
        Set<SiteDto> companySites = new HashSet<>(Arrays.asList(siteService.getCompanySites(rcimTestData.getSuperAdminToken(),
                companyId).getBody()));
        assertThat("Contracts were not all created accordingly!!", companySites, is(siteDtos));
    }
}
