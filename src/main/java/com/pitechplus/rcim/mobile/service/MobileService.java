package com.pitechplus.rcim.mobile.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.dto.report.DamageReportCreateDto;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("mobile-services")
public class MobileService {

    @Value("${services.gw.mobile}")
    private String gwMobileBaseUrl;

    @Value("${mobile.auth}")
    private String authUser;

    @Value("${start.booking}")
    private String startBooking;

    @Value("${damage.report}")
    private String damageReport;


    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);


    public ResponseEntity<MemberDto> authUser(Login login) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-app-origin", ApplicationType.GLIDE_MOBILE.toString());
        headers.set("Content-Type", " application/json");
        HttpEntity loginDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Auth Mobile User: " + login.getLogin())
                .url(gwMobileBaseUrl + authUser)
                .httpMethod(HttpMethod.POST)
                .uriVariables(login.getLogin(), login.getPassword())
                .requestBody(loginDtoHttpEntity)
                .response(MemberDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public void startBooking(String xAuthToken, UUID bookingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("Content-Type", " application/json");
        headers.set("x-app-origin", ApplicationType.GLIDE_MOBILE.toString());
        HttpEntity startBookingHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Start booking with id: " + bookingId)
                .url(gwMobileBaseUrl + startBooking)
                .httpMethod(HttpMethod.POST)
                .uriVariables(bookingId)
                .requestBody(startBookingHttpEntity)
                .build();
        restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<Boolean> createDamageReport(String xAuthToken, UUID bookingId, DamageReportCreateDto damageReportCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("Content-Type", " application/json");
        headers.set("x-app-origin", ApplicationType.GLIDE_MOBILE.toString());
        HttpEntity<DamageReportCreateDto> damageReportCreateHttpEntity = new HttpEntity<>(damageReportCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create damage report for booking with id: " + bookingId)
                .url(gwMobileBaseUrl + damageReport)
                .httpMethod(HttpMethod.POST)
                .uriVariables(bookingId)
                .response(Boolean.class)
                .requestBody(damageReportCreateHttpEntity)
                .sleepTimeAfterRequestInSec(5)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

}
