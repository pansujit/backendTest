package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.dto.booking.BookingCreateDto;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.rits.cloning.Cloner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 28.08.2017.
 */
public class ReplaceBookingTests extends BackendAbstract {

    private UUID bookingId;
    private UUID newBookingId;

    @BeforeMethod
    public void createBooking() {
        BookingCreateDto bookingCreateDto = buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                rcimTestData.getAutomationParking());
        bookingCreateDto.getStart().setDate(LocalDateTime.now().plusMinutes(30).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        bookingCreateDto.getEnd().setDate(LocalDateTime.now().plusMinutes(90).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(), bookingCreateDto).getBody().getId();
    }

    @Test(dataProvider = "replaceBookings", description = "This test verifies that request to replace booking works accordingly.")
    @TestInfo(expectedResult = "Initial Booking is Canceled and new Booking is created with the information provided in " +
            "the replace booking request.")
    public void replaceBookingTest(BookingCreateDto bookingCreateDto) {
        BookingDto bookingAfterReplace = bookingService.replaceBooking(rcimTestData.getSuperAdminToken(), bookingId,
                bookingCreateDto).getBody();
        newBookingId = bookingAfterReplace.getId();
        assertThat("Initial booking was not cancelled!", bookingService.getBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody().getStatus(), is(BookingStatusType.CANCELED));
        assertThat("Booking was not replaced correctly!", bookingAfterReplace,
                is(DtoBuilders.buildExpectedBooking(bookingCreateDto, rcimTestData.getAutomationVehicle())));
    }

    @AfterMethod
    public void cancelBooking() {
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), newBookingId);
    }

    @DataProvider
    private Object[][] replaceBookings() {
        Cloner cloningMachine = new Cloner();
        BookingCreateDto earlierBooking = buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                rcimTestData.getAutomationParking());
        earlierBooking.getStart().setDate(LocalDateTime.now().plusMinutes(5).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        earlierBooking.getEnd().setDate(LocalDateTime.now().plusMinutes(65).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        BookingCreateDto laterBooking = cloningMachine.deepClone(earlierBooking);
        laterBooking.getStart().setDate(LocalDateTime.now().plusMinutes(35).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));
        laterBooking.getEnd().setDate(LocalDateTime.now().plusMinutes(100).withNano(0).toString() +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()));

        return new Object[][]{
                {earlierBooking},
                {laterBooking}
        };
    }
}
