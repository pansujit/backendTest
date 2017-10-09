package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.selenium.Waits;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.booking.BookingCreateDto;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 29.08.2017.
 */

public class ReplaceBookingNegativeTests extends BackendAbstract {

    private UUID bookingId;

    @BeforeClass
    public void createBooking() {
        BookingCreateDto bookingCreateDto = buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                rcimTestData.getAutomationParking());
        bookingCreateDto.getStart().setDate(LocalDateTime.now().plusMinutes(30).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        bookingCreateDto.getEnd().setDate(LocalDateTime.now().plusMinutes(90).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(), bookingCreateDto).getBody().getId();
    }

    @Test(description = "This test verifies that request replace booking with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        try {
            bookingService.replaceBooking(rcimTestData.getSuperAdminToken() + "INVALID", bookingId, new BookingCreateDto());
            Assert.fail("Replace Booking worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
        //check initial booking not affected
        assertThat("Initial booking was cancelled!", bookingService.getBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody().getStatus(), is(BookingStatusType.SCHEDULED));
    }

    @Test(description = "This test verifies that request replace booking with invalid booking id triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developerMessage: No booking found for id {bookingId}")
    public void invalidBookingIdTest() throws IOException {
        UUID invalidId = UUID.randomUUID();
        try {
            bookingService.replaceBooking(rcimTestData.getSuperAdminToken(), invalidId,
                    buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(), rcimTestData.getAutomationParking()));
            Assert.fail("Replace Booking worked with invalid booking id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No booking found for id " + invalidId, null));
        }
        //check initial booking not affected
        assertThat("Initial booking was cancelled!", bookingService.getBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody().getStatus(), is(BookingStatusType.SCHEDULED));
    }

    @Test(description = "This test verifies that request to replace booking with missing mandatory fields triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors for fields type, start and reserved seats.")
    public void missingMandatoryFieldsTest() throws IOException {
        try {
            bookingService.replaceBooking(rcimTestData.getSuperAdminToken(), bookingId, new BookingCreateDto());
            Assert.fail("Replace Booking worked with missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            Waits.sleep(5);//in order for async job to be done
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BOOKING_CREATE, TYPE_MAY_NOT_BE_NULL,
                                    START_MAY_NOT_BE_NULL, RESERVED_SEATS_MAY_NOT_BE_NULL)));
        }
        //check initial booking not affected
        assertThat("Initial booking was cancelled!", bookingService.getBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody().getStatus(), is(BookingStatusType.SCHEDULED));
    }

    @Test(description = "This test verifies that request to replace booking with start date in the past triggers correct " +
            "error response from server.")
    @TestInfo(expectedResult = "Server responds with 422 Unprocessable Entity with developerMessage: Booking has an invalid start date.")
    public void replaceBookingWithPastStartDateTest() throws IOException {
        BookingCreateDto pastStartDate = buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                rcimTestData.getAutomationParking());
        pastStartDate.getStart().setDate(LocalDateTime.now().minusHours(2).withSecond(0).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        try {
            bookingService.replaceBooking(rcimTestData.getSuperAdminToken(), bookingId, pastStartDate);
            Assert.fail("Replace booking worked with start date in the past!");
        } catch (HttpStatusCodeException exception) {
            Waits.sleep(5);//in order for async job to be done
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "Booking has an invalid start date", null));
        }
        //check initial booking not affected
        assertThat("Initial booking was cancelled!", bookingService.getBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody().getStatus(), is(BookingStatusType.SCHEDULED));
    }

    @Test(description = "This test verifies that request to replace booking which overlaps another existing booking triggers " +
            "correct error response from server.")
    @TestInfo(expectedResult = "Server responds with 422 Unprocessable Entity with developerMessage: A user already has a " +
            "booking in the same period for a member")
    public void overlapExistingBookingTest() throws IOException {
        BookingCreateDto futureBooking = buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                rcimTestData.getAutomationParking());
        futureBooking.getStart().setDate(LocalDateTime.now().plusHours(4).withSecond(0).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        futureBooking.getEnd().setDate(LocalDateTime.now().plusHours(6).withSecond(0).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        UUID bookingOverlappedId = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                futureBooking).getBody().getId();
        BookingCreateDto overlappingBooking = buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                rcimTestData.getAutomationParking());
        overlappingBooking.getStart().setDate(LocalDateTime.now().plusHours(3).withSecond(0).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        overlappingBooking.getEnd().setDate(LocalDateTime.now().plusHours(5).withSecond(0).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));

        try {
            bookingService.replaceBooking(rcimTestData.getSuperAdminToken(), bookingId, overlappingBooking);
            Assert.fail("Replace booking worked with overlapping dates as another booking!");
        } catch (HttpStatusCodeException exception) {
            Waits.sleep(5);//in order for async job to be done
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                            "A user already has a booking in the same period for a member", null));
        }
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingOverlappedId);

        //check initial booking not affected
        assertThat("Initial booking was cancelled!", bookingService.getBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody().getStatus(), is(BookingStatusType.SCHEDULED));
    }

    @AfterClass
    public void cancelBooking() {
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingId);
    }

}
