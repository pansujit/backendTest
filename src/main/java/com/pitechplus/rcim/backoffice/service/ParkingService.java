package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.company.ParkingCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
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

@Component("parking-controller")
public class ParkingService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${parkings}")
    private String parkings;

    @Value("${view.update.parking}")
    private String viewUpdateParking;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

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

    public ResponseEntity<ParkingDto> getParking(String xAuthToken, UUID parkingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity parkingCreateDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Parking with id: " + parkingId)
                .url(gwAdminBaseUrl + viewUpdateParking)
                .httpMethod(HttpMethod.GET)
                .requestBody(parkingCreateDtoHttpEntity)
                .response(ParkingDto.class)
                .uriVariables(parkingId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<ParkingDto> updateParking(String xAuthToken, UUID parkingId, ParkingCreateDto parkingCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<ParkingCreateDto> parkingCreateDtoHttpEntity = new HttpEntity<>(parkingCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Parking with id: " + parkingId)
                .url(gwAdminBaseUrl + viewUpdateParking)
                .httpMethod(HttpMethod.PUT)
                .requestBody(parkingCreateDtoHttpEntity)
                .response(ParkingDto.class)
                .uriVariables(parkingId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
}
