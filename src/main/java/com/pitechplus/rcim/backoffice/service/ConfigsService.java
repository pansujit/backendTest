package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.common.FileDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.CategoryDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.ColorDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleVersionDto;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dgliga on 26.09.2017.
 */

@Component("configs-controller")
public class ConfigsService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${templates}")
    private String templates;

    @Value("${categories}")
    private String categories;

    @Value("${versions}")
    private String versions;

    @Value("${colors}")
    private String colors;

    @Value("${files}")
    private String files;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);


    public List<String> getTemplates(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity entity = new HttpEntity(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get group templates")
                .url(gwAdminBaseUrl + templates)
                .httpMethod(HttpMethod.GET)
                .requestBody(entity)
                .response(String[].class)
                .build();
        ResponseEntity<String[]> responseEntity = restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
        return Arrays.asList(responseEntity.getBody());
    }

    public List<CategoryDto> getCategories(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity categoryHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Categories")
                .url(gwAdminBaseUrl + categories)
                .httpMethod(HttpMethod.GET)
                .requestBody(categoryHttpEntity)
                .response(CategoryDto[].class)
                .build();
        ResponseEntity<CategoryDto[]> responseEntity = restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
        return Arrays.asList(responseEntity.getBody());
    }

    public List<VehicleVersionDto> getVersions(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity versionsHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Versions")
                .url(gwAdminBaseUrl + versions)
                .httpMethod(HttpMethod.GET)
                .requestBody(versionsHttpEntity)
                .response(VehicleVersionDto[].class)
                .build();
        ResponseEntity<VehicleVersionDto[]> responseEntity = restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
        return Arrays.asList(responseEntity.getBody());
    }

    public List<ColorDto> getColors(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity colorsHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Colors")
                .url(gwAdminBaseUrl + colors)
                .httpMethod(HttpMethod.GET)
                .requestBody(colorsHttpEntity)
                .response(ColorDto[].class)
                .build();
        ResponseEntity<ColorDto[]> responseEntity = restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
        return Arrays.asList(responseEntity.getBody());
    }

    public ResponseEntity<FileDto> createFile(String xAuthToken, FileDto fileDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<FileDto> fileDtoHttpEntity = new HttpEntity<>(fileDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create file")
                .url(gwAdminBaseUrl + files)
                .httpMethod(HttpMethod.POST)
                .requestBody(fileDtoHttpEntity)
                .response(FileDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

}
