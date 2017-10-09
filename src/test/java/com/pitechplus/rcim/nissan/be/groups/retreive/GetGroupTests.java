package com.pitechplus.rcim.nissan.be.groups.retreive;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupViewDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga  on 01.09.2017.
 */
public class GetGroupTests extends BackendAbstract {

    private GroupViewDto groupDto;


    @BeforeClass
    public void createGroup() {
        ParkingDto parkingDto = parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                DtoBuilders.buildParkingCreate()).getBody();
        parkingDto.getSite().setAddress(null);
        parkingDto.getSite().setSpontaneousBookingEnabled(null);
        parkingDto.getSite().setSpontaneousBookingUsage(null);
        parkingDto.getSite().setSmartcardFishingEnabled(null);
        parkingDto.getSite().setSmartcardEnabled(null);
        groupDto = nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(1, 0, rcimTestData),
                        rcimTestData, parkingDto)).getBody();
        groupDto.setParking(parkingDto);


    }

    @Test(description = "This test verifies that valid call to get group with single member works accordingly.")
    @TestInfo(expectedResult = "Member retrieved from server has all the correct information from creation.")
    public void getGroupTest() {
        GroupViewDto responseGroup = nissanBeServices.getGroup(rcimTestData.getAdminToken(), groupDto.getId()).getBody();
        assertThat("Get a single member service did not respond with the correct information !",
                responseGroup, samePropertyValuesAs(groupDto));
    }

}

