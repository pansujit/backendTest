package com.pitechplus.rcim.backofficetests.booking;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.data.enums.UsageType;
import com.pitechplus.rcim.backoffice.dto.booking.FilteredSearchDto;
import com.pitechplus.rcim.backoffice.dto.booking.SearchBookingResponseDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.SearchBookingResultDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.rits.cloning.Cloner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.pitechplus.qautils.randomgenerators.NumberGenerator.randInt;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.MEMBER_LOGIN_MAY_NOT_BE_EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 21.08.2017.
 */
public class SearchFilteredTests extends BackendAbstract {


    @Test(description = "This test verifies that valid filtered search for a booking works accordingly.")
    @TestInfo(expectedResult = "Search filtered booking returns correct results with vehicle available for the search " +
            "which was performed in order to create a booking.")
    public void filteredSearchTest() {
        FilteredSearchDto filteredSearchDto = DtoBuilders.buildFilteredSearch(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationSiteId());
        SearchBookingResponseDto searchBookingResponseDto = bookingService.searchFilteredBookings(rcimTestData.getSuperAdminToken(),
                filteredSearchDto).getBody();
        Set<SearchBookingResultDto> expectedSearchResult = new HashSet<>();
        expectedSearchResult.add(DtoBuilders.buildExpectedSearchBookingResult(filteredSearchDto, rcimTestData.getAutomationParking(),
                rcimTestData.getAutomationVehicle(), UsageType.PRIVATE));
        expectedSearchResult.add(DtoBuilders.buildExpectedSearchBookingResult(filteredSearchDto, rcimTestData.getAutomationParking(),
                rcimTestData.getAutomationVehicle(), UsageType.BUSINESS));
        assertThat("Search filtered booking response was not correct!", searchBookingResponseDto.getResults(),
                is(expectedSearchResult));
    }

    @Test(description = "This test verifies that request filtered search bookings with invalid X-AUTH-TOKEN triggers correct error " +
            "response from server.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        try {
            bookingService.searchFilteredBookings(rcimTestData.getSuperAdminToken() + "Invalid",
                    DtoBuilders.buildFilteredSearch(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationSiteId()));
            Assert.fail("Filtered search worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(dataProvider = "searchFilteredInvalidFields", description = "This test verifies that call with invalid field values " +
            "returns no search results.")
    @TestInfo(expectedResult = "Search returns no results for invalid value parameters.")
    public void invalidFieldTest(FilteredSearchDto filteredSearchDto) throws IOException {
        SearchBookingResponseDto searchBookingResponseDto = bookingService.searchFilteredBookings(rcimTestData.getSuperAdminToken(),
                filteredSearchDto).getBody();
        assertThat("Search returned results for invalid member login!", searchBookingResponseDto.getResults().size(), is(0));
    }

    @Test(description = "This test verifies that performing search with invalid number of passengers ( o or negative ) triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: At least one passenger is required")
    public void invalidPassengerNumberTest() throws IOException {
        FilteredSearchDto filteredSearchDto = DtoBuilders.buildFilteredSearch(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationSiteId());
        filteredSearchDto.setPassengers(-randInt(0, 5));
        try {
            bookingService.searchFilteredBookings(rcimTestData.getSuperAdminToken(), filteredSearchDto);
            Assert.fail("Filtered search worked with less than 1 passenger!!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "At least one passenger is required", null));
        }
    }

    @Test(dataProvider = "missingMandatoryField", description = "This test verifies that calling  filtered search for bookings " +
            "with missing mandatory field triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage depending on which field is missing out " +
            "of the following: start date, start site id or passengers.")
    public void missingMandatoryFieldTest(FilteredSearchDto filteredSearchDto, String message) throws IOException {
        try {
            bookingService.searchFilteredBookings(rcimTestData.getSuperAdminToken(), filteredSearchDto);
            Assert.fail("Filtered search worked with one missing mandatory field!!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            message, null));
        }
    }

    @Test(description = "This test verifies that calling filtered search with no member login triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationError: memberLogin in searchBookingDto may not be empty.")
    public void noMemberLoginTest() throws IOException {
        FilteredSearchDto filteredSearchDto = DtoBuilders.buildFilteredSearch(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationSiteId());
        filteredSearchDto.setMemberLogin(null);
        try {
            bookingService.searchFilteredBookings(rcimTestData.getSuperAdminToken(), filteredSearchDto);
            Assert.fail("Filtered search worked with less than 1 passenger!!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SEARCH_BOOKING, MEMBER_LOGIN_MAY_NOT_BE_EMPTY)));
        }
    }

    @Test(description = "This test verifies that calling filtered search with duration which exceeds booking max duration " +
            "of the site works accordingly")
    @TestInfo(expectedResult = "Search returns no result since such a booking is not possible.")
    public void searchExceedsMaxBookingDurationTest() {
        FilteredSearchDto filteredSearchDto = DtoBuilders.buildFilteredSearch(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationSiteId());
        filteredSearchDto.setEndDate(LocalDateTime.now().plusMinutes(rcimTestData.getAutomationParking().getSite().getMaxDurationOfBooking()).plusMonths(1).toString());
        SearchBookingResponseDto searchBookingResponseDto = bookingService.searchFilteredBookings(rcimTestData.getSuperAdminToken(),
                filteredSearchDto).getBody();
        assertThat("Search returned results for period which exceeds max booking duration!",
                searchBookingResponseDto.getResults().size(), is(0));
    }

    @DataProvider
    private Object[][] searchFilteredInvalidFields() {
        Cloner cloningMachine = new Cloner();

        //create object with all fields
        FilteredSearchDto filteredSearchDto = DtoBuilders.buildFilteredSearch(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationSiteId());
        FilteredSearchDto invalidMember = cloningMachine.deepClone(filteredSearchDto);
        invalidMember.setMemberLogin(rcimTestData.getMemberDto().getLogin() + "INVALID");
        FilteredSearchDto invalidStartSiteId = cloningMachine.deepClone(filteredSearchDto);
        invalidStartSiteId.setStartSiteId(UUID.randomUUID().toString());
        FilteredSearchDto invalidEndSiteId = cloningMachine.deepClone(filteredSearchDto);
        invalidEndSiteId.setEndSiteId(UUID.randomUUID().toString());
        FilteredSearchDto invalidPassengers = cloningMachine.deepClone(filteredSearchDto);
        invalidPassengers.setPassengers(randInt(5, 100));
        FilteredSearchDto invalidDates = cloningMachine.deepClone(filteredSearchDto);
        invalidDates.setEndDate(LocalDateTime.now().minusMinutes(10).withSecond(0).withNano(0).toString());
        return new Object[][]{
                {invalidMember}, {invalidStartSiteId}, {invalidEndSiteId}, {invalidPassengers}, {invalidDates}
        };
    }

    @DataProvider
    private Object[][] missingMandatoryField() {
        Cloner cloningMachine = new Cloner();

        //create object with all fields
        FilteredSearchDto filteredSearchDto = DtoBuilders.buildFilteredSearch(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationSiteId());
        FilteredSearchDto noStartDate = cloningMachine.deepClone(filteredSearchDto);
        noStartDate.setStartDate(null);
        FilteredSearchDto noStartSiteId = cloningMachine.deepClone(filteredSearchDto);
        noStartSiteId.setStartSiteId(null);
        FilteredSearchDto noPassengers = cloningMachine.deepClone(filteredSearchDto);
        noPassengers.setPassengers(null);

        return new Object[][]{
                {noStartDate, "Start date is required"},
                {noStartSiteId, "Start location is required"},
                {noPassengers, "At least one passenger is required"}
        };
    }


}
