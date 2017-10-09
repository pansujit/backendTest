package com.pitechplus.rcim.nissan.be.members.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.nissan.be.nissandto.members.DrivingLicenceDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberUpdateDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanmappers.MemberMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga  on 27.07.2017.
 */
public class UpdateMembersTests extends BackendAbstract {

    private MemberDto memberDto;
    private MemberUpdateDto memberUpdateDto;


    @BeforeClass(description = "The methods creates a member needed in order to call update member service")
    public void createMember() {
        memberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getSuperCompanyDto().getId().toString(),
                        (nissanUtils.createArrayOfValidFiles(1).get(0)))).getBody();
        DrivingLicenceDto drivingLicenceDto = NissanDtoBuilders.buildDrivingLicenceDto((nissanUtils.createArrayOfValidFiles(1)).get(0));
        memberUpdateDto = NissanDtoBuilders.buildMemberUpdateDto(nissanUtils.createArrayOfValidFiles(6), drivingLicenceDto);
    }

    @Test(description = "This test verifies that an existing member can be updated.")
    @TestInfo(expectedResult = "Member after update contains correct information given on request to update.")
    public void updateMemberTest() {

        MemberDto memberAfterUpdate = nissanBeServices.updateMember(rcimTestData.getSuperAdminToken(), memberDto.getId(),
                memberUpdateDto).getBody();
        assertThat("Information given on response to update member does not reflect request !", memberAfterUpdate,
                samePropertyValuesAs((MemberMapper.mapMemberUpdateDtoToMemberDto(memberUpdateDto, memberDto))));
    }


}

