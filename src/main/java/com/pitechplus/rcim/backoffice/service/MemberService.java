package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by dgliga on 26.09.2017.
 */

@Component("member-controller")
public class MemberService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${approve.document}")
    private String approveDocument;

    @Value("${approve.member.profile}")
    private String approveMemberProfile;

    @Value("${enable.member}")
    private String enableMember;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

    public void approveDocument(String xAuthToken, UUID documentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity approveDocHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Approve Document: " + documentId)
                .url(gwAdminBaseUrl + approveDocument)
                .httpMethod(HttpMethod.PUT)
                .uriVariables(documentId)
                .requestBody(approveDocHttpEntity)
                .build();
        restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public void approveMemberProfile(String xAuthToken, UUID memberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity approveDocHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Approve Profile for member with id: " + memberId)
                .url(gwAdminBaseUrl + approveMemberProfile)
                .httpMethod(HttpMethod.PUT)
                .uriVariables(memberId)
                .requestBody(approveDocHttpEntity)
                .build();
        restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public void enableMember(String xAuthToken, String login, Boolean enable) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity enableHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Enable member with login: " + login)
                .url(gwAdminBaseUrl + enableMember)
                .httpMethod(HttpMethod.POST)
                .uriVariables(login, enable)
                .requestBody(enableHttpEntity)
                .build();
        restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }
}
