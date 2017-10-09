package com.pitechplus.rcim.backofficetests.backuser.suspend;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.BackendAbstract;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.NO_BACK_USER_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 02.03.2017.
 */
public class SuspendBackUsersNegativeTests extends BackendAbstract {


    @Test(description = "This test verifies that calling service to suspend back user with invalid X-AUTH-TOKEN triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void suspendBackUserInvalidXAuthTokenTest() throws IOException {
        try {
            backUserService.suspendOrAllowBackUser("Invalid", new UUID(1, 20), true);
            Assert.fail("Suspend back user with invalid token worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that calling service to suspend back user with invalid id triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found and developerMessage: " + NO_BACK_USER_FOUND + "{invalidId}")
    public void suspendBackUserWithInvalidIdTest() throws IOException {
        UUID invalidBackUserId = new UUID(1, 10);
        try {
            backUserService.getSingleBackUser(rcimTestData.getAdminToken(), invalidBackUserId);
            Assert.fail("Suspend back user with invalid back user id worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            NO_BACK_USER_FOUND + invalidBackUserId.toString(), null));
        }
    }

}
