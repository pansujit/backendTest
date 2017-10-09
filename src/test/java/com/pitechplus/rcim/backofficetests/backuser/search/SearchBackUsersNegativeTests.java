package com.pitechplus.rcim.backofficetests.backuser.search;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.data.enums.ValidationError;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearch;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchResponse;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 17.03.2017.
 */
public class SearchBackUsersNegativeTests extends BackendAbstract {

    @Test(description = "This test verifies that calling service search back user with invalid X-AUTH-TOKEN triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidXAuthTest() throws IOException {
        try {
            backUserService.searchBackUser("Invalid X-AUTH-TOKEN", new BackUserSearch());
            Assert.fail("Search back user with invalid token worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that calling service search back user with missing mandatory field ( Page ) triggers " +
            "correct error response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validation error: Page may not be null")
    public void missingMandatoryFieldTest() throws IOException {
        try {
            backUserService.searchBackUser(rcimTestData.getAdminToken(), new BackUserSearch());
            Assert.fail("Search back user without mandatory fields in request worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_SEARCH,
                                    ValidationError.PAGE_MAY_NOT_BE_NULL)));
        }
    }

    @Test(description = "This test verifies that calling search back user with invalid values ( data which is not present in " +
            "database about back users ) triggers correct response from server.", dataProvider = "backUserInvalidSearchByOneField")
    @TestInfo(expectedResult = "Server responds with 0 results after search was made.")
    public void invalidSearchByOneFieldTest(BackUserSearch backUserSearch) {
        ResponseEntity<BackUserSearchResponse> searchResponse = backUserService.searchBackUser(rcimTestData.getAdminToken(),
                backUserSearch);
        assertThat(searchResponse.getBody().getResults().size(), is(0));
    }

    @Test(description = "This test verifies that calling search back user with invalid page number and size triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request and validationErrors: pageSize must be greater or equal to 0, " +
            "pageNumber must be greater or equal to 0.")
    public void invalidPageNumberAndSizeTest() throws IOException {
        BackUserSearch invalidPageSearch = BackUserSearch.builder()
                .page(Page.builder().number(-NumberGenerator.randInt(0, 1000)).size(-NumberGenerator.randInt(0, 1000)).build())
                .build();
        try {
            backUserService.searchBackUser(rcimTestData.getAdminToken(), invalidPageSearch);
            Assert.fail("Search back user with invalid Pagination worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BACK_USER_SEARCH,
                                    ValidationError.PAGE_SIZE_INVALID, ValidationError.PAGE_NUMBER_INVALID)));
        }
    }


    /**
     * This data provider returns Back User Search objects for test which searches by invalid data ( data not present in db
     * for a back user )
     *
     * @return BackUserSearch objects for search request
     */
    @DataProvider
    private Object[][] backUserInvalidSearchByOneField() {
        BackUser addedBackUser = backUserService.createBackUser(rcimTestData.getAdminToken(),
                DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                        rcimTestData.getAdminSuperCompanyId())).getBody();
        Page oneResultPage = Page.builder()
                .number(1)
                .size(1)
                .build();
        BackUserSearch searchByInvalidLastName = BackUserSearch.builder()
                .lastName(addedBackUser.getLastName() + " invalid")
                .page(oneResultPage)
                .build();
        BackUserSearch searchByInvalidFirstName = BackUserSearch.builder()
                .firstName(addedBackUser.getFirstName() + " invalid")
                .page(oneResultPage)
                .build();
        BackUserSearch searchByInvalidEmail = BackUserSearch.builder()
                .email(addedBackUser.getLogin() + " invalid")
                .page(oneResultPage)
                .build();

        return new Object[][]{
                {searchByInvalidLastName},
                {searchByInvalidFirstName},
                {searchByInvalidEmail},
        };
    }
}
