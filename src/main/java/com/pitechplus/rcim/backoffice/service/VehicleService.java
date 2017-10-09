package com.pitechplus.rcim.backoffice.service;

import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.search.vehicle.SearchVehicleRequestDto;
import com.pitechplus.rcim.backoffice.dto.search.vehicle.SearchVehicleResponseDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.*;
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

@Component("vehicle-controller")
public class VehicleService {

    @Value("${services.gw.admin}")
    private String gwAdminBaseUrl;

    @Value("${vehicles}")
    private String vehicles;

    @Value("${view.update.vehicle}")
    private String viewUpdateVehicle;

    @Value("${vehicle.search}")
    private String searchVehicles;

    @Value("${vehicle.autolib}")
    private String vehicleAutolib;

    @Value("${vehicle.fuel.card}")
    private String vehicleFuelCard;

    @Value("${vehicle.insurance.contract}")
    private String vehicleInsurance;

    @Value("${vehicle.lease.contract}")
    private String vehicleLease;

    private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);

    public ResponseEntity<VehicleDto> createVehicle(String xAuthToken, VehicleCreate vehicleCreate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<VehicleCreate> vehicleCreateHttpEntity = new HttpEntity<>(vehicleCreate, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create Vehicle")
                .url(gwAdminBaseUrl + vehicles)
                .httpMethod(HttpMethod.POST)
                .requestBody(vehicleCreateHttpEntity)
                .sleepTimeAfterRequestInSec(2)
                .response(VehicleDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<VehicleDto[]> getVehicles(String xAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity getVehiclesHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Vehicles")
                .url(gwAdminBaseUrl + vehicles)
                .httpMethod(HttpMethod.GET)
                .requestBody(getVehiclesHttpEntity)
                .response(VehicleDto[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<VehicleDto> updateVehicle(String xAuthToken, UUID vehicleId, VehicleCreate vehicleCreate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<VehicleCreate> vehicleCreateHttpEntity = new HttpEntity<>(vehicleCreate, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Update Vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + viewUpdateVehicle)
                .httpMethod(HttpMethod.PUT)
                .requestBody(vehicleCreateHttpEntity)
                .uriVariables(vehicleId)
                .response(VehicleDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<VehicleDto> getVehicle(String xAuthToken, UUID vehicleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<VehicleCreate> getVehicleHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get Vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + viewUpdateVehicle)
                .httpMethod(HttpMethod.GET)
                .requestBody(getVehicleHttpEntity)
                .uriVariables(vehicleId)
                .response(VehicleDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<SearchVehicleResponseDto> searchVehicles(String xAuthToken, SearchVehicleRequestDto searchVehicleRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<SearchVehicleRequestDto> searchVehiclesHttpEntity = new HttpEntity<>(searchVehicleRequestDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Search Vehicles")
                .url(gwAdminBaseUrl + searchVehicles)
                .httpMethod(HttpMethod.POST)
                .requestBody(searchVehiclesHttpEntity)
                .response(SearchVehicleResponseDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<AutolibCardDto> createAutolibCardForVehicle(String xAuthToken, UUID vehicleId, AutolibCardDto autolibCardDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<AutolibCardDto> autolibCardDtoHttpEntity = new HttpEntity<>(autolibCardDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create autolib card for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleAutolib)
                .httpMethod(HttpMethod.POST)
                .uriVariables(vehicleId)
                .requestBody(autolibCardDtoHttpEntity)
                .response(AutolibCardDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<AutolibCardDto[]> getAutoLibsForVehicle(String xAuthToken, UUID vehicleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<AutolibCardDto> autolibCardDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get autolib cards for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleAutolib)
                .httpMethod(HttpMethod.GET)
                .uriVariables(vehicleId)
                .requestBody(autolibCardDtoHttpEntity)
                .response(AutolibCardDto[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<FuelCardDto> createFuelCardForVehicle(String xAuthToken, UUID vehicleId, FuelCardDto fuelCardDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<FuelCardDto> fuelCardDtoHttpEntity = new HttpEntity<>(fuelCardDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create fuel card for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleFuelCard)
                .httpMethod(HttpMethod.POST)
                .uriVariables(vehicleId)
                .requestBody(fuelCardDtoHttpEntity)
                .response(FuelCardDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<FuelCardDto[]> getFuelCardsForVehicle(String xAuthToken, UUID vehicleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<FuelCardDto> fuelCardDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get fuel cards for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleFuelCard)
                .httpMethod(HttpMethod.GET)
                .uriVariables(vehicleId)
                .requestBody(fuelCardDtoHttpEntity)
                .response(FuelCardDto[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<InsuranceContractDto> createInsuranceContract(String xAuthToken, UUID vehicleId,
                                                                        InsuranceContractDto insuranceContractDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<InsuranceContractDto> insuranceContractDtoHttpEntity = new HttpEntity<>(insuranceContractDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create insurance contract for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleInsurance)
                .httpMethod(HttpMethod.POST)
                .uriVariables(vehicleId)
                .requestBody(insuranceContractDtoHttpEntity)
                .response(InsuranceContractDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<InsuranceContractDto[]> getInsuranceContracts(String xAuthToken, UUID vehicleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<InsuranceContractDto> insuranceContractDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get insurance contracts for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleInsurance)
                .httpMethod(HttpMethod.GET)
                .uriVariables(vehicleId)
                .requestBody(insuranceContractDtoHttpEntity)
                .response(InsuranceContractDto[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<LeaseContractDto> createLeaseContract(String xAuthToken, UUID vehicleId,
                                                                LeaseContractDto leaseContractDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity<LeaseContractDto> leaseContractDtoHttpEntity = new HttpEntity<>(leaseContractDto, headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Create lease contract for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleLease)
                .httpMethod(HttpMethod.POST)
                .uriVariables(vehicleId)
                .requestBody(leaseContractDtoHttpEntity)
                .response(LeaseContractDto.class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

    public ResponseEntity<LeaseContractDto[]> getLeaseContracts(String xAuthToken, UUID vehicleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-AUTH-TOKEN", xAuthToken);
        headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
        HttpEntity leaseContractDtoHttpEntity = new HttpEntity<>(headers);
        RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
                .requestDescription("Call service: Get lease contracts for vehicle with id: " + vehicleId)
                .url(gwAdminBaseUrl + vehicleLease)
                .httpMethod(HttpMethod.GET)
                .uriVariables(vehicleId)
                .requestBody(leaseContractDtoHttpEntity)
                .response(LeaseContractDto[].class)
                .build();
        return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
    }

}
