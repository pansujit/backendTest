package com.pitechplus.rcim.backofficetests.supercompany.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 07.06.2017.
 */
public class GetAllSuperCompaniesTests extends BackendAbstract {

    @Test(description = "This test verifies that valid call for service get all super companies works accordingly.")
    @TestInfo(expectedResult = "Super Companies retrieved from server by an admin is greater than 0.")
    public void getAllSuperCompaniesTest() {
        Set<SuperCompanyDto> superCompanies =
                new HashSet<>(Arrays.asList(companyService.getSuperCompanies(rcimTestData.getAdminToken()).getBody()));
        assertThat("Get All back users service did not bring any super company!", superCompanies.size(), is(greaterThan(0)));
    }

    @Test(description = "This test verifies that calling service to get all super-companies with invalid X-AUTH-TOKEN triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void getAlSuperCompaniesWithInvalidXAuthTest() throws IOException {
        try {
            companyService.getSuperCompanies(RandomStringUtils.random(6));
            Assert.fail("Get Super Companies worked with invalid token!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

}
