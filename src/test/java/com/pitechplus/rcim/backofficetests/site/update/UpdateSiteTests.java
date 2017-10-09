package com.pitechplus.rcim.backofficetests.site.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.company.SiteDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 11.07.2017.
 */
public class UpdateSiteTests extends BackendAbstract {

    private UUID siteId;
    private SiteDto updateSiteDto;

    @BeforeClass(description = "Create a site for a company")
    public void createSiteForCompany() {
        siteId = siteService.createSite(rcimTestData.getSuperAdminToken(), rcimTestData.getCompanyDto().getId(),
                DtoBuilders.buildSiteDto(rcimTestData.getCompanyDto().getId())).getBody().getId();
        updateSiteDto = DtoBuilders.buildSiteDto(rcimTestData.getCompanyDto().getId());
    }

    @Test(description = "This test verifies that valid request to update site works accordingly!")
    @TestInfo(expectedResult = "Site is updated and service responds with updated site object according to request made.")
    public void updateSiteTest() {
        SiteDto siteAfterUpdate = siteService.updateSite(rcimTestData.getSuperAdminToken(), siteId, updateSiteDto).getBody();
        assertThat("Information given on response to update site does not reflect request made !", siteAfterUpdate, is((updateSiteDto)));
    }

    @Test(description = "This test verifies that request to update site with invalid X-AUTH-TOKEN triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void updateSiteInvalidXAuthTest() throws IOException {
        try {
            siteService.updateSite(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(), siteId, updateSiteDto);
            Assert.fail("Update site worked with invalid X-AUTH-TOKEN");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to update site with invalid site id triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developer message: HTTP 404 Not Found")
    public void updateSiteInvalidSiteIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            siteService.updateSite(rcimTestData.getSuperAdminToken(), invalidId, updateSiteDto);
            Assert.fail("Update site worked with invalid site id");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            null, null));
        }
    }
}
