package com.pitechplus.rcim.backofficetests.supercompany.contract;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.supercompany.ContractDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
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
 * Created by dgliga on 05.07.2017.
 */
public class GetCurrentContractTests extends BackendAbstract {
    private UUID superCompanyId;
    private ContractDto contractDto;

    @BeforeClass(description = "This method creates a contract for a super company which we need in order to get current contract.")
    public void addContractToSuperCompany() {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        superCompanyId = companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate).getBody().getId();
        contractDto = DtoBuilders.buildContractDto(0, 1);
        companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, contractDto);
    }

    @Test(description = "This test method verifies that valid call to get current contract of a super company works accordingly.")
    @TestInfo(expectedResult = "Service responds with current contract of company with correct details")
    public void getCurrentContractTest() {
        ContractDto currentContract = companyService.getCurrentContract(rcimTestData.getSuperAdminToken(),
                superCompanyId).getBody();
        assertThat("Response from get current contract did not work!", currentContract, is(contractDto));
    }

    @Test(description = "This test verifies that get current contract for company call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void getCurrentContractInvalidXAuthTest() throws IOException {
        try {
            companyService.getCurrentContract(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(), superCompanyId);
            Assert.fail("Current Contract was retrieved with invalid X-AUTH-TOKEN");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that get current contract for company call with invalid company id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developerMessage: HTTP 404 Not Found")
    public void getCurrentContractInvalidCompanyIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            companyService.getCurrentContract(rcimTestData.getSuperAdminToken(), invalidId);
            Assert.fail("Contract was created for invalid company id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "HTTP 404 Not Found", null));
        }
    }
}
