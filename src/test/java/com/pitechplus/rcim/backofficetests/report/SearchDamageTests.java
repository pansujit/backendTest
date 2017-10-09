package com.pitechplus.rcim.backofficetests.report;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.custommatchers.SoftAssert;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportType;
import com.pitechplus.rcim.backoffice.data.enums.DamageStatus;
import com.pitechplus.rcim.backoffice.data.enums.DamageType;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.report.DamageReportContextDto;
import com.pitechplus.rcim.backoffice.dto.report.DamageReportCreateDto;
import com.pitechplus.rcim.backoffice.dto.report.search.DamageQueryDto;
import com.pitechplus.rcim.backoffice.dto.report.search.DamageResultDto;
import com.pitechplus.rcim.backoffice.dto.report.search.DamageSearchDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.*;

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;
import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 25.09.2017.
 */
public class SearchDamageTests extends BackendAbstract {


    private BookingDto bookingDto;
    private SoftAssert softAssert;

    @BeforeClass
    public void createDamageReportForBooking() {
        //create booking and start it
        bookingDto = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                        rcimTestData.getAutomationParking())).getBody();
        String memberToken = extractXAuthTokenFromResponse(mobileService.authUser(new Login(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getMemberPassword())));
        mobileService.startBooking(memberToken, bookingDto.getId());

        //create damage report for booking created
        DamageReportCreateDto damageReportCreateDto = DtoBuilders.buildDamageReportCreate(DamageReportType.START_BOOKING,
                Collections.singletonList(configsService.createFile(rcimTestData.getSuperAdminToken(),
                        DtoBuilders.buildFile()).getBody().getId()));
        mobileService.createDamageReport(memberToken, bookingDto.getId(), damageReportCreateDto);
        softAssert = new SoftAssert();
    }


    @Test(description = "This test verifies that search damage report by booking id works accordingly.")
    @TestInfo(expectedResult = "Result returned contains the expected damage report of the booking by which the search was made.")
    public void searchByBookingIdTest() {
        DamageQueryDto damageQueryDto = DamageQueryDto.builder()
                .page(new Page(1, 100))
                .companyId(rcimTestData.getBookingSuperCompanyId())
                .bookingId(bookingDto.getId().toString()).build();
        DamageReportContextDto damageReportContextDto = reportsService.getDamageContextForBooking(rcimTestData.getSuperAdminToken(),
                bookingDto.getId()).getBody();
        DamageSearchDto expectedSearch = DtoBuilders.buildExpectedDamageResult(rcimTestData, damageReportContextDto);
        DamageResultDto damageResultDto = reportsService.searchDamages(rcimTestData.getSuperAdminToken(),
                damageQueryDto).getBody();
        assertThat(damageResultDto.getResults().size(), is(1));
        assertThat(damageResultDto.getResults().get(0), is(expectedSearch));
        assertThat("Created date is not today for Damage Details!", damageResultDto.getResults().iterator().next().getCreationDate()
                .contains(LocalDate.now().toString()));
    }

    @Test(description = "This test verifies that search damage report by plate number works accordingly.")
    @TestInfo(expectedResult = "All the damage reports retrieved belong to the vehicle with the plate number by which the " +
            "search was made.")
    public void searchByPlateNumberTest() {
        DamageQueryDto damageQueryDto = DamageQueryDto.builder()
                .page(new Page(1, 100))
                .companyId(rcimTestData.getBookingSuperCompanyId())
                .plateNumber(rcimTestData.getAutomationVehicle().getRegistrationNumber()).build();
        DamageResultDto damageResultDto = reportsService.searchDamages(rcimTestData.getSuperAdminToken(),
                damageQueryDto).getBody();
        assertThat(damageResultDto.getResults().size(), is(greaterThan(0)));
        for (DamageSearchDto damageSearchDto : damageResultDto.getResults()) {
            softAssert.assertTrue(damageSearchDto.getVehiclePlateNumber().equals(rcimTestData.getAutomationVehicle().getRegistrationNumber()));
        }
    }

    @Test(description = "This test verifies that search damage report by damage status works accordingly.",
            dataProvider = "damageStatus")
    @TestInfo(expectedResult = "All the damage reports retrieved have the damage status by which the search was made.")
    public void searchByStatusTest(DamageStatus damageStatus) {
        DamageQueryDto damageQueryDto = DamageQueryDto.builder()
                .page(new Page(1, 50))
                .companyId(rcimTestData.getBookingSuperCompanyId())
                .status(damageStatus).build();
        DamageResultDto damageResultDto = reportsService.searchDamages(rcimTestData.getSuperAdminToken(),
                damageQueryDto).getBody();
        assertThat(damageResultDto.getResults().size(), is(greaterThan(0)));
        for (DamageSearchDto damageSearchDto : damageResultDto.getResults()) {
            softAssert.assertTrue(damageSearchDto.getStatus().equals(damageStatus));
        }
    }

    @Test(description = "This test verifies that search damage report by damage type works accordingly.",
            dataProvider = "damageType")
    @TestInfo(expectedResult = "All the damage reports retrieved have the damage type by which the search was made.")
    public void searchByDamageTypeTest(DamageType damageType) {
        DamageQueryDto damageQueryDto = DamageQueryDto.builder()
                .page(new Page(1, 50))
                .companyId(rcimTestData.getBookingSuperCompanyId())
                .damageType(damageType).build();
        DamageResultDto damageResultDto = reportsService.searchDamages(rcimTestData.getSuperAdminToken(),
                damageQueryDto).getBody();
        assertThat(damageResultDto.getResults().size(), is(greaterThan(0)));
        for (DamageSearchDto damageSearchDto : damageResultDto.getResults()) {
            softAssert.assertTrue(damageSearchDto.getType().equals(damageType));
        }
    }

    @AfterClass
    public void cancelBooking() {
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingDto.getId());
    }

    @DataProvider
    public static Iterator<Object[]> damageStatus() {
        List<DamageStatus> damageStatuses = Arrays.asList(DamageStatus.values());
        Collection<Object[]> damageTypesValues = new ArrayList<Object[]>() {
            {
                for (DamageStatus damageStatus : damageStatuses) {
                    add(new Object[]{damageStatus});
                }
            }
        };
        return damageTypesValues.iterator();
    }


    @DataProvider
    public Iterator<Object[]> damageType() {
        List<DamageType> damageTypes = Arrays.asList(DamageType.values());
        Collection<Object[]> damageTypesValues = new ArrayList<Object[]>() {
            {
                for (DamageType damageType : damageTypes) {
                    add(new Object[]{damageType});
                }
            }
        };
        return damageTypesValues.iterator();
    }
}
