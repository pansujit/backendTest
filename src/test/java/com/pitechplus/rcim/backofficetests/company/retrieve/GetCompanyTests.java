package com.pitechplus.rcim.backofficetests.company.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.company.CompanyCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.CompanyDto;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 05.07.2017.
 */
public class GetCompanyTests extends BackendAbstract {
    private CompanyCreateDto createdCompany;
    private UUID companyId;

    @BeforeClass(description = "This method creates a company needed in order to call get company service.")
    public void createCompany() {
        createdCompany = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        companyId = companyService.createCompany(rcimTestData.getAdminToken(), rcimTestData.getAdminSuperCompanyId(),
                createdCompany).getBody().getId();
    }

    @Test(description = "This test verifies that valid call to service get company by id works accordingly.")
    @TestInfo(expectedResult = "Details of company retrieved are correct.")
    public void getCompanyTest() {
        CompanyDto companyFromServer = companyService.getCompany(rcimTestData.getAdminToken(), companyId).getBody();
        assertThat("Information given on response to get company does not reflect request !", companyFromServer,
                is((DtoBuilders.buildExpectedCompanyDto(createdCompany, rcimTestData.getAdminSuperCompanyId()))));
    }

    @Test(description = "This test verifies that calling service to get company with invalid X-AUTH-TOKEN triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void getCompanyInvalidXAuthTest() throws IOException {
        try {
            companyService.getCompany(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateName(5), companyId);
            Assert.fail("Get company with invalid X-AUTH-TOKEN worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to get company with invalid company id triggers correct error " +
            "from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: No sub-company found for id  {id}")
    public void getCompanyInvalidIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            companyService.getCompany(rcimTestData.getSuperAdminToken(), invalidId);
            Assert.fail("Get company with invalid company id worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No sub-company found for id " + invalidId, null));
        }
    }
}
