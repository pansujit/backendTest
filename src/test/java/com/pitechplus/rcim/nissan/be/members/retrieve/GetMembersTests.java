package com.pitechplus.rcim.nissan.be.members.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga  on 27.07.2017.
 */
public class GetMembersTests extends BackendAbstract {

    private MemberDto memberDto;

    @BeforeClass
    public void createMember() {
        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        memberDto = nissanBeServices.createMember(rcimTestData.getAdminToken(), memberCreateDto).getBody();
    }

    @Test(description = "This test verifies that valid call to get single member works accordingly.")
    @TestInfo(expectedResult = "Member retrieved from server has all the correct information from creation.")
    public void getMemberTest() {
        MemberDto responseMember = nissanBeServices.getMember(rcimTestData.getAdminToken(), memberDto.getId()).getBody();
        assertThat("Get a single member service did not respond with the correct information !",
                responseMember, samePropertyValuesAs(memberDto));
    }

}
