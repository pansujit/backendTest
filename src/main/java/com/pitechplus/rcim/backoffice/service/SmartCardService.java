package com.pitechplus.rcim.backoffice.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.smartcard.SearchResultDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardCreateDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardUpdateDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartcardQueryDto;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
@Component("smartcard-controller")
public class SmartCardService {
	
    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;
    
    @Value("${super.company}")
    private String superCompany;

    @Value("${get.super.company}")
    private String getSuperCompany;
    
    @Value("${create.smartcard}")
    private String createSmartCard;

   @Value("${search.smartcard.card.id}")
   private String searchSmartCardById;
   
   @Value("${search.smartcard}")
   private String searchSmartCards;
   
   @Value("${search.smartcard.user.login}")
   private String searchSmartCardUserLogin;
   
   @Value("${search.smartcard.smartcard.id}")
   private String searchSmartCardBySmartCardId;
   
   @Value("${unlink.smartcard}")
   private String unlinkSmartCard;
   
   @Value("${link.smartcard}")
   private String linkSmartCard;
   
   
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

    
    public ResponseEntity<SmartCardDto[]> getAllSmartCard(String xAuthToken) {
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity smartcardCreate = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: createANewSmartCard: ")
                .url(gwAdminBaseUrl + createSmartCard)
                .httpMethod(HttpMethod.GET)
                .requestBody(smartcardCreate)
                .response(SmartCardDto[].class)
                .build();
        
       return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
       
    }
    public ResponseEntity<SmartCardDto> unlinkSmartCardFromUser(String xAuthToken,String smartcardId) {;
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-AUTH-TOKEN", xAuthToken);
    headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
    HttpEntity unlinkSmartcard = new HttpEntity<>(headers);
    RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
            .requestDescription("Call service: Unlinking Smartcard : "+ smartcardId)
            .url(gwAdminBaseUrl + unlinkSmartCard)
            .httpMethod(HttpMethod.POST)
            .requestBody(unlinkSmartcard)
            .uriVariables(smartcardId)
            .response(SmartCardDto.class)
            .build();
    
   return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
   
}
    public ResponseEntity<SmartCardDto> linkSmartCardToUser(String xAuthToken,String smartcardId,String login) {;
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-AUTH-TOKEN", xAuthToken);
    headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
    HttpEntity linkSmartcard = new HttpEntity<>(headers);
    RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
            .requestDescription("Call service: Unlinking Smartcard : "+ smartcardId)
            .url(gwAdminBaseUrl + linkSmartCard)
            .httpMethod(HttpMethod.POST)
            .requestBody(linkSmartcard)
            .uriVariables(smartcardId,login)
            .response(SmartCardDto.class)
            .build();
    
   return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
   
}
    
    
    public ResponseEntity<SmartCardDto> getSmartCardById(String xAuthToken,String cardId) {;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
       
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity smartcardSearch = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Smartcard searching for id: "+ cardId)
                .url(gwAdminBaseUrl + searchSmartCardById)
                .httpMethod(HttpMethod.GET)
                .requestBody(smartcardSearch)
                .uriVariables(cardId)
                .response(SmartCardDto.class)
                .build();
        
       return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
       
    }
    public ResponseEntity<SmartCardDto> getSmartCardByUserLogin(String xAuthToken,String userLogin) {;
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-AUTH-TOKEN", xAuthToken);
    headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
    HttpEntity smartcardSearch = new HttpEntity<>(headers);
    RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
            .requestDescription("Call service: createANewSmartCard: ")
            .url(gwAdminBaseUrl + searchSmartCardUserLogin)
            .httpMethod(HttpMethod.GET)
            .requestBody(smartcardSearch)
            .uriVariables(userLogin)
            .response(SmartCardDto.class)
            .build();
    
   return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
   
}
    public ResponseEntity<SmartCardDto> getSmartCardBySmartcardId(String xAuthToken,String smartcardId) {;
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-AUTH-TOKEN", xAuthToken);
    headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
    HttpEntity smartcardSearch = new HttpEntity<>(headers);
    RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
            .requestDescription("Call service: createANewSmartCard: ")
            .url(gwAdminBaseUrl + searchSmartCardBySmartCardId)
            .httpMethod(HttpMethod.GET)
            .requestBody(smartcardSearch)
            .uriVariables(smartcardId)
            .response(SmartCardDto.class)
            .build();
    
   return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
   
}
    public ResponseEntity<SmartCardDto> updateSmartCard(String xAuthToken,String smartcardId,
    		SmartCardUpdateDto smartCardUpdateDto) {;
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-AUTH-TOKEN", xAuthToken);
    headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
    HttpEntity smartcardUpdate = new HttpEntity<>(smartCardUpdateDto,headers);
    RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
            .requestDescription("Call service: update SmartCard of given Id :"+ smartcardId)
            .url(gwAdminBaseUrl + searchSmartCardBySmartCardId)
            .httpMethod(HttpMethod.PUT)
            .requestBody(smartcardUpdate)
            .uriVariables(smartcardId)
            .response(SmartCardDto.class)
            .build();
    
   return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
   
}
      
    public ResponseEntity<SearchResultDto> searchSmartCard(String xAuthToken, SmartcardQueryDto smartcardQueryDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<SmartcardQueryDto> searchSmartcard = new HttpEntity<>(smartcardQueryDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Searching for smartcards ")
                .url(gwAdminBaseUrl + searchSmartCards)
                .httpMethod(HttpMethod.POST)
                .requestBody(searchSmartcard).sleepTimeAfterRequestInSec(2)
                .response(SearchResultDto.class)
                .build();
       return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
       
    }
    
    

}
