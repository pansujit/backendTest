package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.booking.BookingCreateDto;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.dto.booking.FilteredSearchDto;
import com.pitechplus.rcim.backoffice.dto.booking.SearchBookingResponseDto;
import com.pitechplus.rcim.backoffice.dto.booking.search.BookingQueryDto;
import com.pitechplus.rcim.backoffice.dto.booking.search.BookingResultDto;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by dgliga on 26.09.2017.
 */

@Component("booking-controller")
public class BookingService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${booking.search.filtered}")
    private String searchFiltered;

    @Value("${bookings}")
    private String bookings;

    @Value("${cancel.booking}")
    private String cancelBooking;

    @Value("${get.booking}")
    private String getBooking;

    @Value("${replace.booking}")
    private String replaceBooking;

    @Value("${search.bookings}")
    private String searchBookings;

    @Value("${finish.booking}")
    private String finishBooking;

    @Value("${shortened.extend.booking}")
    private String shortenedExtendBooking;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);


    public ResponseEntity<SearchBookingResponseDto> searchFilteredBookings(String xAuthToken, FilteredSearchDto filteredSearchDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity searchFilteredHttpEntity = new HttpEntity<>(filteredSearchDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Filtered search for bookings for member: " + filteredSearchDto.getMemberLogin())
                .url(gwAdminBaseUrl + searchFiltered)
                .httpMethod(HttpMethod.POST)
                .requestBody(searchFilteredHttpEntity)
                .response(SearchBookingResponseDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BookingDto> createBooking(String xAuthToken, BookingCreateDto bookingCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<BookingCreateDto> bookingCreateDtoHttpEntity = new HttpEntity<>(bookingCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create booking for member: " + bookingCreateDto.getMemberLogin())
                .url(gwAdminBaseUrl + bookings)
                .httpMethod(HttpMethod.POST)
                .requestBody(bookingCreateDtoHttpEntity)
                .response(BookingDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BookingDto> cancelBooking(String xAuthToken, UUID bookingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity cancelBookingHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Cancel booking with id: " + bookingId)
                .url(gwAdminBaseUrl + cancelBooking)
                .httpMethod(HttpMethod.POST)
                .uriVariables(bookingId)
                .requestBody(cancelBookingHttpEntity)
                .response(BookingDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BookingDto> getBooking(String xAuthToken, UUID bookingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity getBookingHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get booking with id: " + bookingId)
                .url(gwAdminBaseUrl + getBooking)
                .httpMethod(HttpMethod.GET)
                .uriVariables(bookingId)
                .requestBody(getBookingHttpEntity)
                .response(BookingDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BookingDto> replaceBooking(String xAuthToken, UUID bookingId, BookingCreateDto bookingCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<BookingCreateDto> replaceBookingHttpEntity = new HttpEntity<>(bookingCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Replace booking with id: " + bookingId)
                .url(gwAdminBaseUrl + replaceBooking)
                .httpMethod(HttpMethod.POST)
                .uriVariables(bookingId)
                .requestBody(replaceBookingHttpEntity)
                .response(BookingDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BookingResultDto> searchBookings(String xAuthToken, BookingQueryDto bookingQueryDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<BookingQueryDto> searchBookingHttpEntity = new HttpEntity<>(bookingQueryDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Search Bookings")
                .url(gwAdminBaseUrl + searchBookings)
                .httpMethod(HttpMethod.POST)
                .requestBody(searchBookingHttpEntity)
                .response(BookingResultDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BookingDto> finishBooking(String xAuthToken, UUID bookingId, String technicalComment) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity finishBookingHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Replace booking with id: " + bookingId)
                .url(gwAdminBaseUrl + finishBooking)
                .httpMethod(HttpMethod.POST)
                .uriVariables(bookingId, technicalComment)
                .requestBody(finishBookingHttpEntity)
                .response(BookingDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BookingDto> shortenedExtendBooking(String xAuthToken, UUID bookingId, String newDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity extendShortenBooking = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Shorten or extend booking with id: " + bookingId)
                .url(gwAdminBaseUrl + shortenedExtendBooking)
                .httpMethod(HttpMethod.PUT)
                .uriVariables(bookingId, newDate)
                .requestBody(extendShortenBooking)
                .response(BookingDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
}
