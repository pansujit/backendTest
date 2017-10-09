package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.BookingState;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.dto.booking.BookingCreateDto;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 28.08.2017.
 */
public class CancelBookingTests extends BackendAbstract {

    @Test(description = "This test verifies that cancel booking request works accordingly")
    @TestInfo(expectedResult = "Booking is cancelled and status changes to CANCELLED and state to PAST.")
    public void cancelBookingTest() {
        BookingDto createdBooking = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                        rcimTestData.getAutomationParking())).getBody();
        BookingDto cancelledBooking = bookingService.cancelBooking(rcimTestData.getSuperAdminToken(),
                createdBooking.getId()).getBody();
        assertThat("Booking status not changed to CANCELLED!", cancelledBooking.getStatus(), is(BookingStatusType.CANCELED));
        assertThat("Booking state not changed to PAST!", cancelledBooking.getState(), is(BookingState.PAST));
    }


    @Test(description = "This test verifies that request cancel booking with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        BookingCreateDto bookingCreateDto = buildCreateBooking(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getAutomationVehicle(), rcimTestData.getAutomationParking());
        UUID bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                bookingCreateDto).getBody().getId();
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingId);
        try {
            bookingService.cancelBooking(rcimTestData.getSuperAdminToken() + "INVALID", bookingId);
            Assert.fail("Cancel Booking worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request cancel booking with invalid booking id triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developerMessage: No booking found for id {bookingId}")
    public void invalidBookingIdTest() throws IOException {
        UUID invalidId = UUID.randomUUID();
        try {
            bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), invalidId);
            Assert.fail("Cancel Booking worked with invalid booking id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No booking found for id " + invalidId, null));
        }
    }

    @Test(description = "This test verifies that request tto cancel booking which was already cancelled triggers correct " +
            "response error from server,")
    @TestInfo(expectedResult = "Server responds with 422 Unprocessable Entity with developerMessage: Not allowed to cancel this booking")
    public void cancelAlreadyCancelledBookingTest() throws IOException {
        BookingDto createdBooking = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                        rcimTestData.getAutomationParking())).getBody();
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), createdBooking.getId());
        try {
            bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), createdBooking.getId());
            Assert.fail("Cancel Booking worked with invalid booking id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "Not allowed to cancel this booking", null));
        }
    }

}
