package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.custommatchers.SoftAssert;
import com.pitechplus.qautils.selenium.Waits;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.BookingStatusType;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.dto.booking.search.BookingQueryDto;
import com.pitechplus.rcim.backoffice.dto.booking.search.BookingResultDto;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.joda.time.LocalDate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 30.08.2017.
 */
public class SearchBookingsTests extends BackendAbstract {

    private BookingDto createdBooking;
    private MemberDto memberDto;
    private SoftAssert softAssert;

    @BeforeClass
    public void createBooking() {

        memberDto = rcimTestData.createEnabledMember();
        createdBooking = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                buildCreateBooking(memberDto.getLogin(), rcimTestData.getAutomationVehicle(),
                        rcimTestData.getAutomationParking())).getBody();
        Waits.sleep(2);//for Booking to be properly created
        createdBooking.setMember(memberDto);
        createdBooking.getMember().setDrivingLicence(null);//on search bookings member documents are not retrieved
        softAssert = new SoftAssert();
    }

    @Test(description = "This test verifies that booking search for first page ( as shown in admin app also ) works accordingly")
    @TestInfo(expectedResult = "Bookings are retrieved according to the size sent on request.")
    public void searchFirstPageBookingsTest() {
        BookingQueryDto bookingQueryDto = BookingQueryDto.builder()
                .page(Page.builder().size(50).number(1).build()).build();
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                bookingQueryDto).getBody();
        assertThat("First Page not retrieved correctly!", bookingResultDto.getResults().size(),
                is(bookingQueryDto.getPage().getSize()));
    }

    @Test(dataProvider = "searchByNames", description = "This test verifies that search bookings request by member names " +
            "(first name and last name) works accordingly.")
    @TestInfo(expectedResult = "Search result contains booking of member for which search was performed.")
    public void searchByMemberNamesTest(BookingQueryDto bookingQueryDto) {
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                bookingQueryDto).getBody();
        assertThat("Search returned more than one result!", bookingResultDto.getResults().size(), is(1));
        assertThat("Booking returned not correct!", bookingResultDto.getResults().iterator().next(),
                SamePropertyValuesAs.samePropertyValuesAs(createdBooking));
    }

    @Test(dataProvider = "searchBySpecificFields", description = "This test verifies that search bookings request by fields: " +
            "booking id, booking functional id, start date and end date works accordingly.")
    @TestInfo(expectedResult = "Search results contain the booking of the member for which teh search was performed.")
    public void searchByFieldsTest(BookingQueryDto bookingQueryDto) {
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                bookingQueryDto).getBody();
        assertThat("Booking returned not correct!", bookingResultDto.getResults(), hasItem(createdBooking));
    }

    @Test(description = "This test verifies that search bookings request by car model works accordingly.")
    @TestInfo(expectedResult = "Search result contains bookings for which the cars have the model which was sent on request " +
            "to search for bookings.")
    public void searchByCarModelTest() {
        String model = createdBooking.getVehicle().getVersion().getModel().getName();
        BookingQueryDto byCarModel = BookingQueryDto.builder()
                .vehicleModel(model)
                .page(Page.builder().number(1).size(50).build()).build();
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                byCarModel).getBody();
        for (BookingDto bookingDto : bookingResultDto.getResults()) {
            softAssert.assertTrue(bookingDto.getVehicle().getVersion().getModel().getName().equals(model),
                    "Car model returned by search is not correct! Booking id: " + bookingDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(description = "This test verifies that search bookings request by car brand works accordingly.")
    @TestInfo(expectedResult = "Search result contains bookings for which the cars have the brand which was sent on request " +
            "to search for bookings.")
    public void searchByCarBrandTest() {
        String brand = createdBooking.getVehicle().getVersion().getModel().getBrand().getName();
        BookingQueryDto byCarModel = BookingQueryDto.builder()
                .vehicleBrand(brand)
                .page(Page.builder().number(1).size(50).build()).build();
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                byCarModel).getBody();
        for (BookingDto bookingDto : bookingResultDto.getResults()) {
            softAssert.assertTrue(bookingDto.getVehicle().getVersion().getModel().getBrand().getName().equals(brand),
                    "Car brand returned by search is not correct! Booking id: " + bookingDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(description = "This test verifies that search bookings request by company id works accordingly.")
    @TestInfo(expectedResult = "Search response contains bookings which were made under the company for which the search " +
            "request was made.")
    public void searchByCompanyIdTest() {
        BookingQueryDto byCompanyId = BookingQueryDto.builder()
                .companyId(createdBooking.getVehicle().getCompany().getId())
                .page(Page.builder().number(1).size(50).build()).build();
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                byCompanyId).getBody();
        for (BookingDto bookingDto : bookingResultDto.getResults()) {
            softAssert.assertTrue(bookingDto.getVehicle().getCompany().getId().equals(byCompanyId.getCompanyId()),
                    "Booking returned by search did not have the correct company id! Booking id:" + bookingDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(description = "This test verifies that search bookings request by company id and sub company id works accordingly.")
    @TestInfo(expectedResult = "Search response contains bookings which were made under the company id and sub company id " +
            "for which the search request was made.")
    public void searchByCompanyIdAndSubCompanyIdTest() {
        BookingQueryDto byCoAndSubCoIds = BookingQueryDto.builder()
                .companyId(createdBooking.getVehicle().getCompany().getId())
                .subCompanyId(createdBooking.getStart().getParking().getSite().getSubCompanyId())
                .page(Page.builder().number(1).size(50).build()).build();
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                byCoAndSubCoIds).getBody();
        for (BookingDto bookingDto : bookingResultDto.getResults()) {
            softAssert.assertTrue(bookingDto.getVehicle().getCompany().getId().equals(byCoAndSubCoIds.getCompanyId()) ||
                            bookingDto.getStart().getParking().getSite().getSubCompanyId().equals(byCoAndSubCoIds.getSubCompanyId()),
                    "Booking returned by search did not have the correct company id or subCompany id! Booking id:" + bookingDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(dataProvider = "bookingStatus", description = "This test verifies that search bookings request by booking status type " +
            "works accordingly.")
    @TestInfo(expectedResult = "Search response contains bookings which have the booking status type as specified in the " +
            "search bookings request.")
    public void searchByBookingStatusTypeTest(BookingStatusType bookingStatusType) {
        BookingQueryDto byStatus = BookingQueryDto.builder()
                .status(bookingStatusType)
                .page(Page.builder().number(1).size(50).build()).build();
        BookingResultDto bookingResultDto = bookingService.searchBookings(rcimTestData.getSuperAdminToken(),
                byStatus).getBody();
        for (BookingDto bookingDto : bookingResultDto.getResults()) {
            softAssert.assertTrue(bookingDto.getStatus().equals(bookingStatusType),
                    "Booking returned by search did not have the correct status! Booking id:" + bookingDto.getId());
        }
        softAssert.assertAll();
    }


    @DataProvider
    private Object[][] searchByNames() {
        Page page = Page.builder().number(1).size(50).build();
        BookingQueryDto searchByMemberFirstName = BookingQueryDto.builder()
                .memberFirstName(memberDto.getFirstName())
                .page(page).build();
        BookingQueryDto searchByMemberLastName = BookingQueryDto.builder()
                .memberLastName(memberDto.getLastName())
                .page(page).build();

        return new Object[][]{
                {searchByMemberFirstName},
                {searchByMemberLastName}
        };
    }

    @DataProvider
    private Object[][] searchBySpecificFields() {
        Page page = Page.builder().number(1).size(100).build();
        BookingQueryDto searchById = BookingQueryDto.builder()
                .id(createdBooking.getId().toString())
                .page(page).build();
        BookingQueryDto searchByFunctionalId = BookingQueryDto.builder()
                .id(createdBooking.getFunctionalId())
                .page(page).build();
        BookingQueryDto searchByStartDate = BookingQueryDto.builder()
                .startDate(new LocalDate().toString())
                .page(page).build();
        BookingQueryDto searchByEndDate = BookingQueryDto.builder()
                .endDate(new LocalDate().toString())
                .page(page).build();
        return new Object[][]{
                {searchById},
                {searchByFunctionalId},
                {searchByStartDate},
                {searchByEndDate},
        };
    }

    @DataProvider
    private Object[][] bookingStatus() {
        return new Object[][]{
                {BookingStatusType.SCHEDULED},
                {BookingStatusType.CANCELED},
                {BookingStatusType.COMPLETED},
                {BookingStatusType.IN_PROGRESS},
        };
    }

    @AfterClass
    public void cancelBooking() {
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), createdBooking.getId());
    }
}
