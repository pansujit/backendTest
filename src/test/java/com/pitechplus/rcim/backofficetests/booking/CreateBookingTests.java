package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.booking.BookingCreateDto;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.rits.cloning.Cloner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 24.08.2017.
 */
public class CreateBookingTests extends BackendAbstract {

    private BookingCreateDto bookingCreateDto;
    private Cloner cloningMachine;

    @BeforeClass
    public void prepareBookingCreate() {
    	// the start time of booking has been modified to work 
        bookingCreateDto = buildCreateBooking(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getAutomationVehicle(), rcimTestData.getAutomationParking());
        cloningMachine = new Cloner();
    }
    @Test
    public void createBookingTest() {
        BookingDto bookingDto = bookingService.createBooking(rcimTestData.getSuperAdminToken(), bookingCreateDto).getBody();
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingDto.getId());
        assertThat("Booking was not created correctly!", bookingDto,
                is(DtoBuilders.buildExpectedBooking(bookingCreateDto, rcimTestData.getAutomationVehicle())));
    }

    @Test(description = "This test verifies that request create booking with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        try {
            bookingService.createBooking(rcimTestData.getSuperAdminToken() + "INVALID", bookingCreateDto);
            Assert.fail("Create Booking worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to create booking with missing mandatory fields triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors for fields type, start and reserved seats.")
    public void missingMandatoryFieldsTest() throws IOException {
        try {
            bookingService.createBooking(rcimTestData.getSuperAdminToken(), new BookingCreateDto());
            Assert.fail("Create Booking worked with missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.BOOKING_CREATE, TYPE_MAY_NOT_BE_NULL,
                                    START_MAY_NOT_BE_NULL, RESERVED_SEATS_MAY_NOT_BE_NULL)));
        }
    }

    @Test(description = "This test verifies that request to create booking with invalid member login triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: No member found for email: {invalidEmail}")
    public void invalidMemberLoginTest() throws IOException {
        BookingCreateDto invalidMemberLogin = cloningMachine.deepClone(bookingCreateDto);
        invalidMemberLogin.setMemberLogin(bookingCreateDto.getMemberLogin() + "INVALID");
        try {
            bookingService.createBooking(rcimTestData.getSuperAdminToken(), invalidMemberLogin);
            Assert.fail("Create Booking worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No member found for email: " + invalidMemberLogin.getMemberLogin(), null));
        }
    }


}
