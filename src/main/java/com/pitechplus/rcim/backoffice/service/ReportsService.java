package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.report.DamageDetailsDto;
import com.pitechplus.rcim.backoffice.dto.report.DamageReportContextDto;
import com.pitechplus.rcim.backoffice.dto.report.search.DamageQueryDto;
import com.pitechplus.rcim.backoffice.dto.report.search.DamageResultDto;
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

@Component("reports-controller")
public class ReportsService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${get.damage.report.context}")
    private String getDamageReportContext;

    @Value("${get.damage.details}")
    private String getDamageDetails;

    @Value("${search.damages.reports}")
    private String searchDamagesReports;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

    public ResponseEntity<DamageReportContextDto> getDamageContextForBooking(String xAuthToken, UUID bookingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity getDamageReportContextHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Damage Report Context for booking with id: " + bookingId)
                .url(gwAdminBaseUrl + getDamageReportContext)
                .httpMethod(HttpMethod.GET)
                .uriVariables(bookingId)
                .requestBody(getDamageReportContextHttpEntity)
                .response(DamageReportContextDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<DamageDetailsDto> getDamageDetails(String xAuthToken, UUID damageId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity getDamageReportContextHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Damage Details for damage with id: " + damageId)
                .url(gwAdminBaseUrl + getDamageDetails)
                .httpMethod(HttpMethod.GET)
                .uriVariables(damageId)
                .requestBody(getDamageReportContextHttpEntity)
                .response(DamageDetailsDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<DamageResultDto> searchDamages(String xAuthToken, DamageQueryDto damageQueryDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<DamageQueryDto> damageReportQueryDtoHttpEntity = new HttpEntity<>(damageQueryDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Search damage reports")
                .url(gwAdminBaseUrl + searchDamagesReports)
                .httpMethod(HttpMethod.POST)
                .requestBody(damageReportQueryDtoHttpEntity)
                .response(DamageResultDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
}
