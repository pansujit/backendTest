package com.pitechplus.rcim.backofficetests.report;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.DamageReportType;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.dto.report.DamageDetailsDto;
import com.pitechplus.rcim.backoffice.dto.report.DamageDto;
import com.pitechplus.rcim.backoffice.dto.report.DamageReportContextDto;
import com.pitechplus.rcim.backoffice.dto.report.DamageReportCreateDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;
import static com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders.buildCreateBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 12.09.2017.
 */
public class GetDamageReportDetailsTests extends BackendAbstract {

    private DamageDto damageDto;
    private UUID bookingId;

    @BeforeClass
    public void createDamageReportForBooking() {
        //create booking and start it
        bookingId = bookingService.createBooking(rcimTestData.getSuperAdminToken(),
                buildCreateBooking(rcimTestData.getMemberDto().getLogin(), rcimTestData.getAutomationVehicle(),
                        rcimTestData.getAutomationParking())).getBody().getId();
        String memberToken = extractXAuthTokenFromResponse(mobileService.authUser(new Login(rcimTestData.getMemberDto().getLogin(),
                rcimTestData.getMemberPassword())));
        mobileService.startBooking(memberToken, bookingId);

        //create damage report for booking created
        DamageReportCreateDto damageReportCreateDto = DtoBuilders.buildDamageReportCreate(DamageReportType.START_BOOKING,
                Collections.singletonList(configsService.createFile(rcimTestData.getSuperAdminToken(),
                        DtoBuilders.buildFile()).getBody().getId()));
        mobileService.createDamageReport(memberToken, bookingId, damageReportCreateDto);
        damageDto = damageReportCreateDto.getReports().iterator().next();
    }

    @Test(description = "This test verifies that get Damage Report Details works accordingly.")
    @TestInfo(expectedResult = "Damage Report Details retrieved have the correct information expected.")
    public void getDamageReportDetailsTest() {
        DamageReportContextDto damageReportContextDto = reportsService.getDamageContextForBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody();
        DamageDetailsDto damageDetailsDto = reportsService.getDamageDetails(rcimTestData.getSuperAdminToken(),
                damageReportContextDto.getStartDamageReport().getReports().iterator().next().getId()).getBody();
        DamageDetailsDto expectedDamageDetails = DtoBuilders.buildExpectedDamageDetails(damageDto, bookingId,
                rcimTestData.getAutomationVehicle());
        assertThat("Get Damage Details did not retrieve correct info!", damageDetailsDto, is(expectedDamageDetails));
        assertThat("Created date is not today for Damage Details!", damageDetailsDto.getCreatedDate().contains(LocalDate.now()
                .toString()));
        assertThat("Created date is not today for Damage Details!", damageDetailsDto.getStatuses().iterator().next().getDate()
                .contains(LocalDate.now().toString()));
        assertThat("Created date is not today for Damage Details!", damageDetailsDto.getCurrentStatus().getDate()
                .contains(LocalDate.now().toString()));
    }

    @Test(description = "This test verifies that retrieve damage report details call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        DamageReportContextDto damageReportContextDto = reportsService.getDamageContextForBooking(rcimTestData.getSuperAdminToken(),
                bookingId).getBody();

        try {
            reportsService.getDamageDetails(rcimTestData.getSuperAdminToken(),
                    damageReportContextDto.getStartDamageReport().getReports().iterator().next().getId()).getBody();
            Assert.fail("Create Damage Report worked with invalid X-AUTH-TOKEN!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @AfterClass
    public void cancelBooking() {
        bookingService.cancelBooking(rcimTestData.getSuperAdminToken(), bookingId);
    }

}
