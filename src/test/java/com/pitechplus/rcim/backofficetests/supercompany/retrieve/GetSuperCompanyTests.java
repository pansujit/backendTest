package com.pitechplus.rcim.backofficetests.supercompany.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
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

import static com.pitechplus.qautils.randomgenerators.NumberGenerator.randInt;
import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static com.pitechplus.rcim.backoffice.utils.mappers.CompanyMapper.mapSuperCompanyDtoToCreate;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 07.06.2017.
 */
public class GetSuperCompanyTests extends BackendAbstract {

    private UUID superCompanyId;
    private SuperCompanyCreate superCompanyCreate;

    @BeforeClass
    public void addSuperCompany() {
        superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(), rcimTestData.getGroupTemplate());
        superCompanyId = companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate).getBody().getId();
    }

    @Test(description = "This test verifies that valid call to get single super company works accordingly.")
    @TestInfo(expectedResult = "Super Company retrieved from server has all the correct information from creation.")
    public void getSuperCompanyTest() {
        SuperCompanyDto responseSuperCompany = companyService.getSuperCompany(rcimTestData.getAdminToken(), superCompanyId).getBody();
        assertThat("Get Super Company service did not respond with the correct information !",
                mapSuperCompanyDtoToCreate(responseSuperCompany), samePropertyValuesAs((superCompanyCreate)));
    }

    @Test(description = "This test verifies that calling service to get super company with invalid X-AUTH-TOKEN triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void getSuperCompanyWithInvalidXAuthTest() throws IOException {
        try {
            companyService.getSuperCompany(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(), superCompanyId);
            Assert.fail("Get Back Office user worked with invalid token!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that calling service to get super company with invalid id triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found")
    public void getSuperCompanyWithInvalidIdTest() throws IOException {
        UUID invalidSuperCompanyId = new UUID(randInt(0, 10), randInt(0, 10));
        try {
            companyService.getSuperCompany(rcimTestData.getAdminToken(), invalidSuperCompanyId);
            Assert.fail("Get Back Office user worked with invalid back user id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), null, null));
        }
    }
}
