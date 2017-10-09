package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.company.ParkingCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.dto.company.SiteDto;
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

@Component("site-controller")
public class SiteService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${sites}")
    private String sites;

    @Value("${view.update.site}")
    private String viewUpdateSite;

    @Value("${parkings}")
    private String parkings;

    @Value("${get.site.parkings}")
    private String getSiteParkings;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

    public ResponseEntity<SiteDto> createSite(String xAuthToken, UUID subCompanyId, SiteDto siteDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<SiteDto> siteDtoHttpEntity = new HttpEntity<>(siteDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create Site for company with id: " + subCompanyId)
                .url(gwAdminBaseUrl + sites)
                .httpMethod(HttpMethod.POST)
                .requestBody(siteDtoHttpEntity)
                .response(SiteDto.class)
                .uriVariables(subCompanyId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SiteDto[]> getCompanySites(String xAuthToken, UUID subCompanyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity siteDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Sites for company with id: " + subCompanyId)
                .url(gwAdminBaseUrl + sites)
                .httpMethod(HttpMethod.GET)
                .requestBody(siteDtoHttpEntity)
                .response(SiteDto[].class)
                .uriVariables(subCompanyId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SiteDto> getSite(String xAuthToken, UUID siteId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity siteDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Sites with id: " + siteId)
                .url(gwAdminBaseUrl + viewUpdateSite)
                .httpMethod(HttpMethod.GET)
                .requestBody(siteDtoHttpEntity)
                .response(SiteDto.class)
                .uriVariables(siteId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SiteDto> updateSite(String xAuthToken, UUID siteId, SiteDto updateSiteDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<SiteDto> siteDtoHttpEntity = new HttpEntity<>(updateSiteDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Update site with id: " + siteId)
                .url(gwAdminBaseUrl + viewUpdateSite)
                .httpMethod(HttpMethod.PUT)
                .requestBody(siteDtoHttpEntity)
                .response(SiteDto.class)
                .uriVariables(siteId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<ParkingDto> createParking(String xAuthToken, UUID siteId, ParkingCreateDto parkingCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<ParkingCreateDto> parkingCreateDtoHttpEntity = new HttpEntity<>(parkingCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create Parking for site with id: " + siteId)
                .url(gwAdminBaseUrl + parkings)
                .httpMethod(HttpMethod.POST)
                .requestBody(parkingCreateDtoHttpEntity)
                .response(ParkingDto.class)
                .uriVariables(siteId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<ParkingDto[]> getSiteParkings(String xAuthToken, UUID siteId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity siteDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Parkings for site with id: " + siteId)
                .url(gwAdminBaseUrl + getSiteParkings)
                .httpMethod(HttpMethod.GET)
                .requestBody(siteDtoHttpEntity)
                .response(ParkingDto[].class)
                .uriVariables(siteId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
}
