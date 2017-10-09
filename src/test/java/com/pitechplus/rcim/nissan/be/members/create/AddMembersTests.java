package com.pitechplus.rcim.nissan.be.members.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanmappers.MemberMapper;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga  on 24.07.2017.
 */
public class AddMembersTests extends BackendAbstract {

    @Test(description = "This test verifies that call to create a member works accordingly.")
    @TestInfo(expectedResult = "Member created with correct information which was given on request.")
    public void createMemberTest() {

        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                (nissanUtils.createArrayOfValidFiles(1)).get(0));
        MemberDto expectedMemberDto = MemberMapper.mapMemberCreateDtoToMemberDto(memberCreateDto, companyService
                .getSuperCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getAdminSuperCompanyId()).getBody());
        MemberDto responseMemberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                memberCreateDto).getBody();

        assertThat("Information given on response to add member does not reflect request !",
                responseMemberDto, is(expectedMemberDto));
    }


}

