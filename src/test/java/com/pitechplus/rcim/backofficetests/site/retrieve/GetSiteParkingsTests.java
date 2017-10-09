package com.pitechplus.rcim.backofficetests.site.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.dto.company.ParkingCreateDto;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.dto.company.SiteDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 17.07.2017.
 */
public class GetSiteParkingsTests extends BackendAbstract {

    private SiteDto siteDto;

    @BeforeClass(description = "Create a site for a company")
    public void createSiteForCompany() {
        siteDto = siteService.createSite(rcimTestData.getSuperAdminToken(), rcimTestData.getCompanyDto().getId(),
                DtoBuilders.buildSiteDto(rcimTestData.getCompanyDto().getId())).getBody();
    }

    @Test(description = "This test verifies that retrieve parkings for site with multiple parkings works accordingly.")
    @TestInfo(expectedResult = "All parkings are retrieved for site with correct information.")
    public void getMultipleParkingsForSiteTest() {
        Set<ParkingDto> parkingDtos = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(2, 5); i++) {
            ParkingCreateDto parkingCreateDto = DtoBuilders.buildParkingCreate();
            siteService.createParking(rcimTestData.getSuperAdminToken(), siteDto.getId(), parkingCreateDto);
            parkingDtos.add(DtoBuilders.buildExpectedParkingDto(parkingCreateDto, siteDto));
        }
        Set<ParkingDto> siteParkings = new HashSet<>(Arrays.asList(siteService.getSiteParkings(rcimTestData.getSuperAdminToken(),
                siteDto.getId()).getBody()));
        assertThat("Not all parkings retrieved for site!!", siteParkings, is(parkingDtos));
    }

    @Test(description = "This test verifies that retrieve parkings for site with one parking works accordingly.")
    @TestInfo(expectedResult = "Only one parking is retrieved with correct information.")
    public void getParkingsForSiteWithOneParkingTest() {
        SiteDto siteWithOneParking = siteService.createSite(rcimTestData.getSuperAdminToken(), rcimTestData.getCompanyDto().getId(),
                DtoBuilders.buildSiteDto(rcimTestData.getCompanyDto().getId())).getBody();
        ParkingCreateDto parkingCreateDto = DtoBuilders.buildParkingCreate();
        parkingService.createParking(rcimTestData.getSuperAdminToken(), siteWithOneParking.getId(), parkingCreateDto);
        Set<ParkingDto> siteParkings = new HashSet<>(Arrays.asList(siteService.getSiteParkings(rcimTestData.getSuperAdminToken(),
                siteWithOneParking.getId()).getBody()));
        assertThat("More than one parking was retrieved with site with one parking!", siteParkings.size(), is(1));
        assertThat("Parking not retrieved for site with one parking!!", siteParkings.iterator().next(),
                is(DtoBuilders.buildExpectedParkingDto(parkingCreateDto, siteWithOneParking)));
    }

    @Test(description = "This test verifies that retrieve parkings for site with none works accordingly.")
    @TestInfo(expectedResult = "No parking is retrieved, response is an empty array.")
    public void getParkingsForSiteWithNoneTest() {
        SiteDto siteWithNoParkings = siteService.createSite(rcimTestData.getSuperAdminToken(), rcimTestData.getCompanyDto().getId(),
                DtoBuilders.buildSiteDto(rcimTestData.getCompanyDto().getId())).getBody();
        ParkingDto[] siteParkings = siteService.getSiteParkings(rcimTestData.getSuperAdminToken(),
                siteWithNoParkings.getId()).getBody();
        assertThat("More than one parking was retrieved for site with one parking!", siteParkings.length, is(0));
    }

    @Test(description = "This test verifies that retrieve parkings for site call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void getParkingsInvalidXAuthTest() throws IOException {
        try {
            siteService.getSiteParkings(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(), siteDto.getId());
            Assert.fail("Parkings retrieved for site with invalid X-AUTH-TOKEN");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that retrieve parkings for invalid site id works accordingly.")
    @TestInfo(expectedResult = "No parking is retrieved, response is an empty array.")
    public void getParkingsInvalidSiteIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        ParkingDto[] siteParkings = siteService.getSiteParkings(rcimTestData.getSuperAdminToken(),
                invalidId).getBody();
        assertThat("More than one parking was retrieved for invalid site id with one parking!", siteParkings.length, is(0));
    }
}
