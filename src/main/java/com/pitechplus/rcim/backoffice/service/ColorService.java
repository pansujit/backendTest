package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.color.ColorCreateDto;
import com.pitechplus.rcim.backoffice.dto.color.ColorDto;
import com.pitechplus.rcim.backoffice.dto.color.ColorUpdateDto;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * Created by dgliga on 26.09.2017.
 */

@Component("color-controller")
public class ColorService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;
    
    @Value("${color}")
    private String color;
    
    @Value("${single.color}")
    private String getUpdateColor;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

    public ResponseEntity<ColorDto> postColor(String xAuthToken, ColorCreateDto colorCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity colorCreateHttpEntity = new HttpEntity<>(colorCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create color")
                .url(gwAdminBaseUrl + color)
                .httpMethod(HttpMethod.POST)
                .requestBody(colorCreateHttpEntity)
                .sleepTimeAfterRequestInSec(2)
                .response(ColorDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
    
    public ResponseEntity<ColorDto[]> getAllColor(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity colorCreateHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get all colors")
                .url(gwAdminBaseUrl + color)
                .httpMethod(HttpMethod.GET)
                .requestBody(colorCreateHttpEntity)
                .sleepTimeAfterRequestInSec(2)
                .response(ColorDto[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
    
    public ResponseEntity<ColorDto> getSingleColor(String xAuthToken,String colorId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity colorCreateHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: get a color :" + colorId)
                .url(gwAdminBaseUrl + getUpdateColor)
                .httpMethod(HttpMethod.GET)
                .requestBody(colorCreateHttpEntity)
                .uriVariables(colorId)
                .response(ColorDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
    
    public ResponseEntity<ColorDto> updateSingleColor(String xAuthToken,String colorId,ColorUpdateDto colorUpdateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity updateColorHttpEntity = new HttpEntity<>(colorUpdateDto,headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: update a color :"+ colorId)
                .url(gwAdminBaseUrl + getUpdateColor)
                .httpMethod(HttpMethod.GET)
                .requestBody(updateColorHttpEntity)
                .uriVariables(colorId)
                .response(ColorDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    

}
