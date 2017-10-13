package com.pitechplus.rcim.backoffice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.dto.common.Company;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardCreateDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardDto;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
@Component("smartcard-controller")
public class SmartCardService {
	
    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;
    
    @Value("${create.smartcard}")
    private String createSmartCard;
    
    @Value("${get.companies}")
    private String getCompanies;

    @Value("${search.company.configurations}")
    private String searchCompanyConfigurations;

    @Value("${super.company}")
    private String superCompany;

    @Value("${get.super.company}")
    private String getSuperCompany;

    @Value("${search.super.companies}")
    private String searchSuperCompanies;

    @Value("${company}")
    private String company;

    @Value("${company.view.update}")
    private String viewUpdateCompany;

    @Value("${get.sub.company}")
    private String getSubCompany;

    @Value("${company.contract}")
    private String companyContract;


    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);
    
    public ResponseEntity<SmartCardDto> createANewSmartCard(String xAuthToken, SmartCardCreateDto smartCardCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<SmartCardCreateDto> smartcardCreate = new HttpEntity<>(smartCardCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: createANewSmartCard: ")
                .url(gwAdminBaseUrl + createSmartCard)
                .httpMethod(HttpMethod.POST)
                .requestBody(smartcardCreate)
                .response(SmartCardDto.class)
                .build();
       return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
       
    }

}
