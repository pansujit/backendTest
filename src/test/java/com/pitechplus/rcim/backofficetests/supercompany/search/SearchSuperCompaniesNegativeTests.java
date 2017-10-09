package com.pitechplus.rcim.backofficetests.supercompany.search;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.data.enums.SuperCompanySearchProperty;
import com.pitechplus.rcim.backoffice.data.enums.ValidationError;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearch;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySorter;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 27.06.2017.
 */
public class SearchSuperCompaniesNegativeTests extends BackendAbstract {

    @Test(description = "This test verifies that calling service search super companies with invalid X-AUTH-TOKEN triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidXAuthTest() throws IOException {
        try {
            companyService.searchSuperCompanies("Invalid X-AUTH-TOKEN", new SuperCompanySearch());
            Assert.fail("Search super companies with invalid token worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }


    @Test(description = "This test verifies that calling search super companies with invalid page number and size triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request and validationErrors: pageSize must be greater or equal to 0, " +
            "pageNumber must be greater or equal to 0.")
    public void invalidPageNumberAndSizeTest() throws IOException {
        SuperCompanySearch invalidPageSearch = SuperCompanySearch.builder()
                .page(Page.builder().number(-NumberGenerator.randInt(0, 1000)).size(-NumberGenerator.randInt(0, 1000)).build())
                .sort(new SuperCompanySorter(SortDirection.ASC, SuperCompanySearchProperty.NAME)).build();
        try {
            companyService.searchSuperCompanies(rcimTestData.getAdminToken(), invalidPageSearch);
            Assert.fail("Search super companies with invalid Pagination worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_SEARCH,
                                    ValidationError.PAGE_SIZE_INVALID, ValidationError.PAGE_NUMBER_INVALID)));
        }
    }

    @Test(description = "This test verifies that calling service search super companies with missing mandatory field ( Page ) triggers " +
            "correct error response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validation error: Page may not be null")
    public void missingMandatoryFieldTest() throws IOException {
        try {
            companyService.searchSuperCompanies(rcimTestData.getAdminToken(), new SuperCompanySearch());
            Assert.fail("Search super company without mandatory fields in request worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_SEARCH,
                                    ValidationError.PAGE_MAY_NOT_BE_NULL)));
        }
    }
}
