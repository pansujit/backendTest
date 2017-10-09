package com.pitechplus.rcim.backofficetests.vehicle.search;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.custommatchers.SoftAssert;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.*;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.search.vehicle.SearchVehicleRequestDto;
import com.pitechplus.rcim.backoffice.dto.search.vehicle.SearchVehicleResponseDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.*;

import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.randomEnum;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by dgliga on 21.07.2017.
 */
public class SearchVehicleTest extends BackendAbstract {

    private int numberOfVehicles;
    private SoftAssert softAssert;
    private VehicleDto vehicleCreated;

    @BeforeClass
    public void createVehicleAndGetAll() {
        vehicleCreated = vehicleService.createVehicle(rcimTestData.getSuperAdminToken(),
                DtoBuilders.buildVehicleCreate(rcimTestData)).getBody();
        Set<VehicleDto> vehicleDtos = new HashSet<>(Arrays.asList(vehicleService.getVehicles(rcimTestData.getSuperAdminToken())
                .getBody()));
        numberOfVehicles = vehicleDtos.size();
        softAssert = new SoftAssert();
    }


    @Test(description = "This test verifies that search vehicle by plate number works accordingly.")
    @TestInfo(expectedResult = "Search returns vehicle with correct information.")
    public void searchVehicleByPlateNumberTest() {
        SearchVehicleRequestDto byRegistrationNumber = SearchVehicleRequestDto.builder()
                .page(new Page(1, 1))
                .plateNumber(vehicleCreated.getRegistrationNumber()).build();
        SearchVehicleResponseDto searchResult = vehicleService.searchVehicles(rcimTestData.getSuperAdminToken(),
                byRegistrationNumber).getBody();
        assertThat("Search by plate number did not return correct information!", searchResult.getResults().iterator().next(),
                is(vehicleCreated));
    }

    @Test(dataProvider = "searchByEnum", description = "This test verifies that search by an enum attribute of the vehicle entity " +
            "works accordingly.")
    @TestInfo(expectedResult = "Vehicles which have the attribute by which search is performed are retrieved.")
    public <T extends Enum<T>> void searchByAttributeTest(String fieldSearchBy, SearchVehicleRequestDto searchRequest, T enumValue)
            throws NoSuchFieldException {
        SearchVehicleResponseDto searchResult = vehicleService.searchVehicles(rcimTestData.getSuperAdminToken(),
                searchRequest).getBody();
        assertThat("Search did not return any results!", searchResult.getResults().size(), is(greaterThan(0)));
        for (VehicleDto vehicleDto : searchResult.getResults()) {
            Field field = vehicleDto.getClass().getDeclaredField(fieldSearchBy);
            field.setAccessible(true);
            try {
                softAssert.assertTrue(field.get(vehicleDto).toString().equals(enumValue.toString()),
                        "Vehicle returned did not have the fuel type by which search was made!" + vehicleDto);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        softAssert.assertAll();
    }

    @Test(dataProvider = "searchByIds", description = "This test verifies that filtered search vehicles by company / subCompany id " +
            "works accordingly")
    @TestInfo(expectedResult = "Search result returns vehicle for the company / subCompany id for which filtered search was performed.")
    public void searchByIdsTest(SearchVehicleRequestDto searchRequest) {
        SearchVehicleResponseDto searchResult = vehicleService.searchVehicles(rcimTestData.getSuperAdminToken(),
                searchRequest).getBody();
        assertThat("More than one vehicle was returned by filtered search!", searchResult.getResults().size(), is(not(greaterThan(1))));
        assertThat("Search by id did not return correct information!", searchResult.getResults().iterator().next(),
                is(vehicleCreated));
    }

    @DataProvider
    private Object[][] searchByEnum() {
        List<FuelType> randomFuelType = Collections.singletonList(randomEnum(FuelType.class));
        List<TransmissionType> randomTransmissionType = Collections.singletonList(randomEnum(TransmissionType.class));
        List<StatusType> randomStatusType = Collections.singletonList(randomEnum(StatusType.class));
        List<SystemType> randomSystemType = Collections.singletonList(randomEnum(SystemType.class));
        List<VehicleType> randomVehicleType = Collections.singletonList(randomEnum(VehicleType.class));

        SearchVehicleRequestDto byFuelType = SearchVehicleRequestDto.builder()
                .page(new Page(1, numberOfVehicles))
                .fuelType(randomFuelType).build();
        SearchVehicleRequestDto byTransmissionType = SearchVehicleRequestDto.builder()
                .page(new Page(1, numberOfVehicles))
                .transmissionType(randomTransmissionType).build();
        SearchVehicleRequestDto byStatusType = SearchVehicleRequestDto.builder()
                .page(new Page(1, numberOfVehicles))
                .vehicleStatus(randomStatusType).build();
        SearchVehicleRequestDto bySystemType = SearchVehicleRequestDto.builder()
                .page(new Page(1, numberOfVehicles))
                .systemType(randomSystemType).build();
        SearchVehicleRequestDto byVehicleType = SearchVehicleRequestDto.builder()
                .page(new Page(1, numberOfVehicles))
                .type(randomVehicleType).build();


        return new Object[][]{
                {FuelType.getFieldNameInClass(), byFuelType, randomFuelType.get(0)},
                {TransmissionType.getFieldNameInClass(), byTransmissionType, randomTransmissionType.get(0)},
                {StatusType.getFieldNameInClass(), byStatusType, randomStatusType.get(0)},
                {SystemType.getFieldNameInClass(), bySystemType, randomSystemType.get(0)},
                {VehicleType.getFieldNameInClass(), byVehicleType, randomVehicleType.get(0)},
        };
    }

    @DataProvider
    private Object[][] searchByIds() {
        SearchVehicleRequestDto searchByCompanyId = SearchVehicleRequestDto.builder()
                .page(new Page(1, numberOfVehicles))
                .companyId(rcimTestData.getSuperCompanyDto().getId()).build();
        SearchVehicleRequestDto searchBySubCompanyId = SearchVehicleRequestDto.builder()
                .page(new Page(1, numberOfVehicles))
                .subCompanyId(rcimTestData.getCompanyDto().getId()).build();


        return new Object[][]{
                {searchByCompanyId},
                {searchBySubCompanyId}
        };
    }
}
