package com.pitechplus.rcim.backofficetests.site.retrieve;

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
 * Created by dgliga on 06.07.2017.
 */
public class GetSingleSiteTests extends BackendAbstract {

    private SiteDto addedSiteDto;

    @BeforeClass(description = "Create a site for a company")
    public void createSiteForCompany() {
        addedSiteDto = siteService.createSite(rcimTestData.getSuperAdminToken(), rcimTestData.getCompanyDto().getId(),
                DtoBuilders.buildSiteDto(rcimTestData.getCompanyDto().getId())).getBody();
    }

    @Test(description = "This test verifies that valid call to get single site for company works accordingly.")
    @TestInfo(expectedResult = "Site retrieved is the one expected with correct information")
    public void getSingleSiteTest() {
        SiteDto siteRetrieved = siteService.getSite(rcimTestData.getSuperAdminToken(), addedSiteDto.getId()).getBody();
        assertThat("Site retrieved is not the expected one !", siteRetrieved, is((addedSiteDto)));
    }

    @Test(description = "This test verifies that request to get site with invalid X-AUTH-TOKEN triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void getSingleSiteInvalidXAuthTest() throws IOException {
        try {
            siteService.getSite(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(), addedSiteDto.getId());
            Assert.fail("Get site retrieved with invalid X-AUTH-TOKEN");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to get site with invalid site id triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developer message: HTTP 404 Not Found")
    public void getSingleSiteInvalidIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            siteService.getSite(rcimTestData.getSuperAdminToken(), invalidId);
            Assert.fail("Get site retrieved with invalid site id");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            null, null));
        }
    }

}
