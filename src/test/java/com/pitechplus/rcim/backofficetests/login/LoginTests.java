package com.pitechplus.rcim.backofficetests.login;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by dgliga on 08.05.2017.
 */
public class LoginTests extends BackendAbstract {

    @Test(description = "This test verifies that valid login in back office works accordingly.")
    @TestInfo(expectedResult = "Login service responds with 200 OK and x-auth-token is generated for logged in user.")
    public void validLoginTest() {
        ResponseEntity<BackUser> login = backUserService.authUser(new Login(boSuperAdminUsername, boSuperAdminPassword));
        assertThat("Login status is not ok!", login.getStatusCode(), is(HttpStatus.OK));
        assertThat("X-AUTH-TOKEN was not generated for valid login!", extractXAuthTokenFromResponse(login), is(notNullValue()));
    }

    @Test(description = "This test verifies that login with invalid username does not work.")
    @TestInfo(expectedResult = "Login service responds with 401 Unauthorized")
    public void invalidUsernameTest() throws IOException {
        try {
            backUserService.authUser(new Login(PersonalInfoGenerator.generateFullName().replaceAll("\\s", "") + "@test.ro", boSuperAdminPassword));
            Assert.fail("Login with invalid username worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                            null, null));
        }
    }

    @Test(description = "This test verifies that login with invalid password does not work.")
    @TestInfo(expectedResult = "Login service responds with 401 Unauthorized")
    public void invalidPasswordTest() throws IOException {
        try {
            backUserService.authUser(new Login(boSuperAdminUsername, PersonalInfoGenerator.generateName(6) + "1A@"));
            Assert.fail("Login with invalid username worked!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                            null, null));
        }
    }
}
