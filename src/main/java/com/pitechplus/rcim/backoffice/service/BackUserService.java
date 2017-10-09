package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserUpdate;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearch;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchResponse;
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

@Component("back-user-controller")
public class BackUserService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${backusers}")
    private String backUsers;

    @Value("${auth.user}")
    private String authUser;

    @Value("${single.back.user}")
    private String singleBackUser;

    @Value("${suspend.back.user}")
    private String suspendBackUser;

    @Value("${search.back.user}")
    private String searchBackUser;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

    public ResponseEntity<BackUser> createBackUser(String xAuthToken, BackUserCreate backUserCreate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<BackUserCreate> boUserHttpEntity = new HttpEntity<>(backUserCreate, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create Back User")
                .url(gwAdminBaseUrl + backUsers)
                .httpMethod(HttpMethod.POST)
                .requestBody(boUserHttpEntity)
                .response(BackUser.class)
                .sleepTimeAfterRequestInSec(2)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BackUser> authUser(Login login) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<Login> loginDtoHttpEntity = new HttpEntity<>(login, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Auth Back User: " + login.getLogin())
                .url(gwAdminBaseUrl + authUser)
                .httpMethod(HttpMethod.POST)
                .requestBody(loginDtoHttpEntity)
                .response(BackUser.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BackUser> getSingleBackUser(String xAuthToken, UUID backProfileId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity entity = new HttpEntity(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Single Back User with id: " + backProfileId.toString())
                .url(gwAdminBaseUrl + singleBackUser)
                .httpMethod(HttpMethod.GET)
                .requestBody(entity)
                .response(BackUser.class)
                .uriVariables(backProfileId)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BackUser[]> getBackUsers(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity entity = new HttpEntity(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get All Back users")
                .url(gwAdminBaseUrl + backUsers)
                .httpMethod(HttpMethod.GET)
                .requestBody(entity)
                .response(BackUser[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BackUser> updateBackUser(String xAuthToken, UUID backUserId, BackUserUpdate backUserUpdate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<BackUserUpdate> backUserUpdateHttpEntity = new HttpEntity<>(backUserUpdate, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Update Back User: " + backUserId.toString())
                .url(gwAdminBaseUrl + singleBackUser)
                .httpMethod(HttpMethod.PUT)
                .requestBody(backUserUpdateHttpEntity)
                .uriVariables(backUserId)
                .response(BackUser.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BackUser> suspendOrAllowBackUser(String xAuthToken, UUID backUserId, boolean suspend) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity suspendEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Suspend Back User: " + backUserId.toString() + " with value: " + suspend)
                .url(gwAdminBaseUrl + suspendBackUser)
                .httpMethod(HttpMethod.PUT)
                .requestBody(suspendEntity)
                .uriVariables(backUserId, suspend)
                .response(BackUser.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<BackUserSearchResponse> searchBackUser(String xAuthToken, BackUserSearch backUserSearch) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<BackUserSearch> backUserSearchHttpEntity = new HttpEntity<>(backUserSearch, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Search Back User")
                .url(gwAdminBaseUrl + searchBackUser)
                .httpMethod(HttpMethod.POST)
                .requestBody(backUserSearchHttpEntity)
                .response(BackUserSearchResponse.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
}
