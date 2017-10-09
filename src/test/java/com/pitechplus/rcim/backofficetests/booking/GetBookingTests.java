package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.booking.BookingCreateDto;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
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
 * Created by dgliga on 25.08.2017.
 */
public class GetBookingTests extends BackendAbstract {


    @Test(description = "This test verifies that request to get booking works accordingly.")
    @TestInfo(expectedResult = "The booking which belongs to the id given on request is retrieved with correct information.")
    public void getBookingTest() {
        BookingCreateDto bookingCreateDto = buildCreateBooking(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getAutomationVehicle(), rcimTestData.getAutomationParking());
        UUID bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                bookingCreateDto).getBody().getId();
        BookingDto bookingDto = bookingService.getBooking(rcimTestData.getSuperAdminToken(), bookingId).getBody();
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingDto.getId());
        BookingDto expectedBookingDto = DtoBuilders.buildExpectedBooking(bookingCreateDto, rcimTestData.getAutomationVehicle());
        MemberDto memberDto = rcimTestData.getMemberDto();
        memberDto.setDrivingLicence(null);
        expectedBookingDto.setMember(rcimTestData.getMemberDto());
        assertThat("Booking retrieved is incorrect!", bookingDto, is(expectedBookingDto));
    }

    @Test(description = "This test verifies that request get booking with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        BookingCreateDto bookingCreateDto = buildCreateBooking(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getAutomationVehicle(), rcimTestData.getAutomationParking());
        UUID bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                bookingCreateDto).getBody().getId();
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingId);
        try {
            bookingService.getBooking(rcimTestData.getSuperAdminToken() + "INVALID", bookingId);
            Assert.fail("Get Booking worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request get booking with invalid booking id triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developerMessage: No booking found for id {bookingId}")
    public void invalidBookingIdTest() throws IOException {
        UUID invalidId = UUID.randomUUID();
        try {
            bookingService.getBooking(rcimTestData.getSuperAdminToken(), invalidId);
            Assert.fail("Get Booking worked with invalid booking id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No booking found for id " + invalidId, null));
        }
    }
}
