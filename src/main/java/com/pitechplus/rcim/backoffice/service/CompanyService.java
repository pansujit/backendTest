package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.common.Company;
import com.pitechplus.rcim.backoffice.dto.company.CompanyCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.CompanyDto;
import com.pitechplus.rcim.backoffice.dto.search.companyconfig.ConfigurationQueryDto;
import com.pitechplus.rcim.backoffice.dto.search.companyconfig.SearchCompanyConfigurations;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearch;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearchResult;
import com.pitechplus.rcim.backoffice.dto.supercompany.*;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by dgliga on 26.09.2017.
 */

@Component("company-controller")
public class CompanyService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

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

    @Value("${get.current.contract}")
    private String getCurrentContract;

    @Value("${create.super.company.configuration}")
    private String createSuperCompanyConfiguration;


    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

    public List<Company> getCompanies(String xAuthToken, UUID superCompanyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity entity = new HttpEntity(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get companies for super company: " + superCompanyId.toString())
                .url(gwAdminBaseUrl + getCompanies)
                .httpMethod(HttpMethod.GET)
                .requestBody(entity)
                .response(Company[].class)
                .uriVariables(superCompanyId)
                .build();
        ResponseEntity<Company[]> responseEntity = restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
        return Arrays.asList(responseEntity.getBody());
    }

    public ResponseEntity<SearchCompanyConfigurations> searchCompanyConfigurations(String xAuthToken, ConfigurationQueryDto searchQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<ConfigurationQueryDto> companyConfigsSearch = new HttpEntity<>(searchQuery, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Search company configurations")
                .url(gwAdminBaseUrl + searchCompanyConfigurations)
                .httpMethod(HttpMethod.POST)
                .requestBody(companyConfigsSearch)
                .response(SearchCompanyConfigurations.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SuperCompanyDto> createSuperCompany(String xAuthToken, SuperCompanyCreate superCompanyCreate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<SuperCompanyCreate> superCompanyCreateHttpEntity = new HttpEntity<>(superCompanyCreate, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create Super Company")
                .url(gwAdminBaseUrl + superCompany)
                .httpMethod(HttpMethod.POST)
                .requestBody(superCompanyCreateHttpEntity)
                .response(SuperCompanyDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SuperCompanyDto[]> getSuperCompanies(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity superCompanyGetHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Super Companies")
                .url(gwAdminBaseUrl + superCompany)
                .httpMethod(HttpMethod.GET)
                .requestBody(superCompanyGetHttpEntity)
                .response(SuperCompanyDto[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SuperCompanyDto> getSuperCompany(String xAuthToken, UUID companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity superCompanyGetHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Super Company with id: " + companyId.toString())
                .url(gwAdminBaseUrl + getSuperCompany)
                .httpMethod(HttpMethod.GET)
                .requestBody(superCompanyGetHttpEntity)
                .response(SuperCompanyDto.class)
                .uriVariables(companyId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SuperCompanyDto> updateSuperCompany(String xAuthToken, UUID superCompanyId,
                                                              SuperCompanyCreate updateSuperCompany) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<SuperCompanyCreate> superCompanyCreateHttpEntity = new HttpEntity<>(updateSuperCompany, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Update Super Company with id: " + superCompanyId.toString())
                .url(gwAdminBaseUrl + getSuperCompany)
                .httpMethod(HttpMethod.PUT)
                .requestBody(superCompanyCreateHttpEntity)
                .response(SuperCompanyDto.class)
                .sleepTimeAfterRequestInSec(2)
                .uriVariables(superCompanyId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SuperCompanySearchResult> searchSuperCompanies(String xAuthToken, SuperCompanySearch superCompanySearch) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity searchSuperCompany = new HttpEntity<>(superCompanySearch, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Search super companies")
                .url(gwAdminBaseUrl + searchSuperCompanies)
                .httpMethod(HttpMethod.POST)
                .requestBody(searchSuperCompany)
                .response(SuperCompanySearchResult.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SuperCompanyConfigurationDto> createSuperCompanyConfiguration(String xAuthToken, SuperCompanyConfigurationEditDto superCompanyConfigurationEdit) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity superCompanyConfigurationDtoHttpEntity = new HttpEntity<>(superCompanyConfigurationEdit, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create super company configuration.")
                .url(gwAdminBaseUrl + createSuperCompanyConfiguration)
                .httpMethod(HttpMethod.POST)
                .requestBody(superCompanyConfigurationDtoHttpEntity)
                .response(SuperCompanyConfigurationDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<CompanyDto> createCompany(String xAuthToken, UUID superCompanyId, CompanyCreateDto companyCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<CompanyCreateDto> superCompanyCreateHttpEntity = new HttpEntity<>(companyCreateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create Company")
                .url(gwAdminBaseUrl + company)
                .httpMethod(HttpMethod.POST)
                .requestBody(superCompanyCreateHttpEntity)
                .response(CompanyDto.class)
                .uriVariables(superCompanyId)
                .sleepTimeAfterRequestInSec(2)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<CompanyDto> updateCompany(String xAuthToken, UUID companyId, CompanyCreateDto companyUpdateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<CompanyCreateDto> updateCompanyHttp = new HttpEntity<>(companyUpdateDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Update Company")
                .url(gwAdminBaseUrl + viewUpdateCompany)
                .httpMethod(HttpMethod.PUT)
                .requestBody(updateCompanyHttp)
                .response(CompanyDto.class)
                .uriVariables(companyId)
                .sleepTimeAfterRequestInSec(2)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<CompanyDto> getCompany(String xAuthToken, UUID subCompanyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity companyHttp = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Sub-Company: " + subCompanyId.toString())
                .url(gwAdminBaseUrl + getSubCompany)
                .httpMethod(HttpMethod.GET)
                .requestBody(companyHttp)
                .response(CompanyDto.class)
                .uriVariables(subCompanyId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<ContractDto> createContract(String xAuthToken, UUID companyId, ContractDto contractDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<ContractDto> contractDtoHttpEntity = new HttpEntity<>(contractDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create Contract for company with id: " + companyId)
                .url(gwAdminBaseUrl + companyContract)
                .httpMethod(HttpMethod.POST)
                .requestBody(contractDtoHttpEntity)
                .response(ContractDto.class)
                .uriVariables(companyId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<ContractDto[]> getCompanyContracts(String xAuthToken, UUID companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity contractDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Contracts for company with id: " + companyId)
                .url(gwAdminBaseUrl + companyContract)
                .httpMethod(HttpMethod.GET)
                .requestBody(contractDtoHttpEntity)
                .response(ContractDto[].class)
                .uriVariables(companyId)
                .sleepTimeAfterRequestInSec(2)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<ContractDto> getCurrentContract(String xAuthToken, UUID companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity contractDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Current Contract for company with id: " + companyId)
                .url(gwAdminBaseUrl + getCurrentContract)
                .httpMethod(HttpMethod.GET)
                .requestBody(contractDtoHttpEntity)
                .response(ContractDto.class)
                .uriVariables(companyId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
}
