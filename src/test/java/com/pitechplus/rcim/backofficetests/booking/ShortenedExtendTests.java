package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;
import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ShortenedExtendTests extends BackendAbstract {

    private UUID bookingId;

    @BeforeMethod
    public void startBooking() {
        bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                        rcimTestData.getAutomationParking())).getBody().getId();
        String memberToken = extractXAuthTokenFromResponse(mobileService.authUser(new Login(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getMemberPassword())));
        mobileService.startBooking(memberToken, bookingId);
    }

    @Test(description = "This test verifies that shorten / extend booking request works accordingly when new date exceeds " +
            "the initial end date of the booking.")
    @TestInfo(expectedResult = "The new end date of the booking is updated according to the request which was mde.")
    public void extendBookingTest() {
        String newDate = LocalDateTime.now().minusHours(1).plusMinutes(100).withNano(0) +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()).getId();
        BookingDto bookingAfterUpdate = bookingService.shortenedExtendBooking(rcimTestData.getSuperAdminToken(),
                bookingId, newDate).getBody();
        assertThat("New End Date was not updated!", bookingAfterUpdate.getEnd().getDate(), is(newDate));
        assertThat("Booking status changed from in progress!", bookingAfterUpdate.getStatus(),
                is(BookingStatusType.IN_PROGRESS));
    }

    @Test(description = "This test verifies that shorten / extend booking request works accordingly when new date falls behind " +
            "the initial end date of the booking.")
    @TestInfo(expectedResult = "The new end date of the booking is updated according to the request which was mde.")
    public void shortenBookingTest() {
        String newDate = LocalDateTime.now().minusHours(1).plusMinutes(10).plusHours(2).withNano(0) +
                ZoneId.of(rcimTestData.getAutomationParking().getSite().getZoneId()).getRules().getOffset(Instant.now()).getId();
        BookingDto bookingAfterUpdate = bookingService.shortenedExtendBooking(rcimTestData.getSuperAdminToken(),
                bookingId, newDate).getBody();
        assertThat("New End Date was not updated!", bookingAfterUpdate.getEnd().getDate(), is(newDate));
        assertThat("Booking status changed from in progress!", bookingAfterUpdate.getStatus(),
                is(BookingStatusType.IN_PROGRESS));
    }

    @AfterMethod
    public void cancelBooking() {
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingId);
    }
}
