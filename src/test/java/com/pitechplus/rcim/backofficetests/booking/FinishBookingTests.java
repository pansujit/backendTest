package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.BookingState;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
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

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;
import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FinishBookingTests extends BackendAbstract {

    private UUID bookingId;

    @BeforeClass
    public void startBooking() {
        bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                        rcimTestData.getAutomationParking())).getBody().getId();
        String memberToken = extractXAuthTokenFromResponse(mobileService.authUser(new Login(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getMemberPassword())));
        mobileService.startBooking(memberToken, bookingId);
    }

    @Test
    public void finishBookingTest() {
        String technicalComment = "This is the will of " + PersonalInfoGenerator.generateOrcName();
        BookingDto bookingAfterFinish = bookingService.finishBooking(rcimTestData.getSuperAdminToken(), bookingId,
                technicalComment).getBody();
        assertThat("Booking status not changed to Completed!", bookingAfterFinish.getStatus(),
                is(BookingStatusType.COMPLETED));
        assertThat("Booking state not changed to Past!", bookingAfterFinish.getState(), is(BookingState.PAST));
        assertThat("Technical comment not saved!", bookingAfterFinish.getTechnicalComment(), is(technicalComment));
    }

    @Test(description = "This test verifies that request finish booking with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        try {
            bookingService.finishBooking(rcimTestData.getSuperAdminToken() + "INVALID", bookingId, "a comment");
            Assert.fail("Create Booking worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request finish booking with invalid booking id triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developerMessage: No booking found for id {bookingId}")
    public void invalidBookingIdTest() throws IOException {
        UUID invalidId = UUID.randomUUID();
        try {
            bookingService.finishBooking(rcimTestData.getSuperAdminToken(), invalidId, "a comment");
            Assert.fail("Finish Booking worked with invalid booking id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No booking found for id " + invalidId, null));
        }
    }

    @Test(dependsOnMethods = "finishBookingTest")
    public void finishAlreadyFinishedBookingTest() throws IOException {
        try {
            bookingService.finishBooking(rcimTestData.getSuperAdminToken(), bookingId, "a comment");
            Assert.fail("Finish Booking worked for already finished booking!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "The booking is already finished", null));
        }
    }

}
