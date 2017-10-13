package com.pitechplus.rcim.backofficetests.smartcard.create;



import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;

import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardCreateDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanmappers.MemberMapper;
import com.rits.cloning.Cloner;

public class CreateSmartCardTest extends BackendAbstract{
	private String login;
	private String superCompanyId;
	@BeforeClass
	public void creatememberForSmartCard() {
        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                (nissanUtils.createArrayOfValidFiles(1)).get(0));
        MemberDto responseMemberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                memberCreateDto).getBody();
        login= responseMemberDto.getLogin();
        superCompanyId= responseMemberDto.getCompany().getId().toString();
	}
	
    @Test(description = "This test method verifies creation of a smartcard")
    @TestInfo(expectedResult = "a smartcard will be created with given information")
    public void addSmartCardTest() {
        SmartCardCreateDto smartCardCreateDto = 
        		DtoBuilders.buildSmartCardCreate(superCompanyId,login);
        SmartCardDto smartcardDto=smartCardService.createANewSmartCard(rcimTestData.getSuperAdminToken(), 
        		smartCardCreateDto).getBody(); 
        Cloner cloner= new Cloner();
        SmartCardDto x=cloner.deepClone(smartcardDto);
       assertThat("The build created smartcard has different response than what we created !", smartcardDto,
               is(( DtoBuilders.buildExpectedCreateSmartDto(x, superCompanyId,login))));
    }
	
	

}
