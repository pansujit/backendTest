package com.pitechplus.rcim.backofficetests.smartcard;

import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.SmartCardSortProperty;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.smartcard.SearchResultDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardCreateDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardQuerySort;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartcardQueryDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.SortingMatchers;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;

public class SearchSmartCardsTest extends BackendAbstract {
	private String login;
	private String superCompanyId;
	SmartCardDto smartcardDto;
	
	@BeforeClass
	public void creatememberForSmartCard() {
		MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(
				rcimTestData.getAdminSuperCompanyId().toString(), (nissanUtils.createArrayOfValidFiles(1)).get(0));
		MemberDto responseMemberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto)
				.getBody();
		login = responseMemberDto.getLogin();
		superCompanyId = responseMemberDto.getCompany().getId().toString();
		SmartCardCreateDto smartCardCreateDto = DtoBuilders.buildSmartCardCreate(superCompanyId, login);
	    smartcardDto = smartCardService.createANewSmartCard(rcimTestData.getSuperAdminToken(), smartCardCreateDto).getBody();
	    try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test(description="This test verifies get all smartcard method")
	@TestInfo(expectedResult="All the smartcards should be displayed")
	public void getallSmartCardsTest() throws JsonParseException, JsonMappingException, IOException {
		SmartCardDto[] x=smartCardService.getAllSmartCard(rcimTestData.getSuperAdminToken()).getBody();
		List<SmartCardDto> y=Arrays.asList(x);
		assertThat("The created new item is in not in the list",y,hasItem(smartcardDto));
	}
	
	@Test(description="This test verifies search smart card by its cardId")
	@TestInfo(expectedResult="Only the smartcard with the given cardid should be displayed")
	public void searchSmartCardBycardId() {
		ResponseEntity<SmartCardDto> x=smartCardService.getSmartCardById(rcimTestData.getSuperAdminToken(), smartcardDto.getCardId());
		assertThat("this is failed test",x.getBody(), is(smartcardDto));
		
	}
	@Test(description="This test verifies search smartcard by its user login")
	@TestInfo(expectedResult="Only the smartcard with the given login/email should be displayed")
	public void searchSmartCardByUserLogin() {
		ResponseEntity<SmartCardDto> x=smartCardService.getSmartCardById(rcimTestData.getSuperAdminToken(), smartcardDto.getUser().getLogin());
		assertThat("this is failed test",x.getBody(), is(smartcardDto));
		
	}
	@Test(description="This test verifies search smartcard by its smart card Id")
	@TestInfo(expectedResult="Only the smartcard with the given smartcard Id should be displayed")
	public void searchSmartCardBySmartCardId() {
		ResponseEntity<SmartCardDto> x=smartCardService.getSmartCardById(rcimTestData.getSuperAdminToken(), smartcardDto.getId());
		assertThat("this is failed test",x.getBody(), is(smartcardDto));
		
	}
	
	@Test(dataProvider="searchByOneField",description="This method search for smart card usig one field at a time")
	@TestInfo(expectedResult="Only the smartcards with given information should be displayed")
	public void searchSmartCardByOneField(SmartcardQueryDto searchSmartCard) {
		SearchResultDto x=smartCardService.searchSmartCard(rcimTestData.getSuperAdminToken(), searchSmartCard).getBody();
		assertThat("The search couldnot find the expected smartcard",x.getResults(),hasItem(smartcardDto));
	}
	
	@Test(dataProvider="searchAndSortData",description="This test verifies search and sort function in the smartcard")
	@TestInfo(expectedResult="The smartcard should be sorted by their given sorting properties")
	public void searchAndSortSmartCard(SmartcardQueryDto searchAndSort) {
		SearchResultDto x=smartCardService.searchSmartCard(rcimTestData.getSuperAdminToken(), searchAndSort).getBody();
        assertThat(x.getResults(), SortingMatchers.isListSortedByField(searchAndSort.getSort().getProperty().getValue(),
        		searchAndSort.getSort().getDirection()));
	}

	@DataProvider
	public  Object[][] searchByOneField() {
		Page singlePage=Page.builder().number(1).size(1).build();
		SmartcardQueryDto searchByLogin=SmartcardQueryDto.builder()
				.userLogin(smartcardDto.getUser().getLogin())
				.page(singlePage)
				.build();
		SmartcardQueryDto searchByCardId=SmartcardQueryDto.builder()
				.cardId(smartcardDto.getCardId())
				.page(singlePage)
				.build();
		SmartcardQueryDto searchByCompanyId=SmartcardQueryDto.builder()
				.cardId(smartcardDto.getCardId())
				.CompanyId(smartcardDto.getCompany().getId())
				.page(singlePage)
				.build();
		SmartcardQueryDto searchByProtocol=SmartcardQueryDto.builder()
				.cardId(smartcardDto.getCardId())
				.rfidProtocol(smartcardDto.getProtocol())
				.page(singlePage)
				.build();
		
		SmartcardQueryDto searchByAllField=SmartcardQueryDto.builder()
				.cardId(smartcardDto.getCardId())
				.rfidProtocol(smartcardDto.getProtocol())
				.userLogin(smartcardDto.getUser().getLogin())
				.CompanyId(smartcardDto.getCompany().getId())	
				.page(singlePage)
				.build();
		
		return new Object[][] {
			{searchByLogin},
			{searchByCardId},
			{searchByCompanyId},
			{searchByProtocol},
			{searchByAllField}
		};
		
	}
	
	

	
	@DataProvider
	public Object[][] searchAndSortData() {
		Page page= Page.builder().number(1).size(4).build();
		Page pageBig= Page.builder().number(1).size(200).build();
		SmartCardQuerySort sortAscendingByCardId=SmartCardQuerySort.builder()
				.direction(SortDirection.ASC)
				.property(SmartCardSortProperty.CARD_ID)
				.build();
		SmartCardQuerySort sortDescendingByCardId=SmartCardQuerySort.builder()
				.direction(SortDirection.DESC)
				.property(SmartCardSortProperty.CARD_ID)
				.build();
		
		SmartCardQuerySort sortAscendingByCompanyId=SmartCardQuerySort.builder()
				.direction(SortDirection.ASC)
				.property(SmartCardSortProperty.COMPANY_ID)
				.build();
		SmartCardQuerySort sortDescendingByCompanyId=SmartCardQuerySort.builder()
				.direction(SortDirection.DESC)
				.property(SmartCardSortProperty.COMPANY_ID)
				.build();
		SmartCardQuerySort sortAscendingByProtocol=SmartCardQuerySort.builder()
				.direction(SortDirection.ASC)
				.property(SmartCardSortProperty.RFID_PROTOCOL)
				.build();
		SmartCardQuerySort sortDescendingByProtocol=SmartCardQuerySort.builder()
				.direction(SortDirection.DESC)
				.property(SmartCardSortProperty.RFID_PROTOCOL)
				.build();
		SmartCardQuerySort sortAscendingByUserLogin=SmartCardQuerySort.builder()
				.direction(SortDirection.ASC)
				.property(SmartCardSortProperty.USER_LOGIN)
				.build();
		SmartCardQuerySort sortDescendingByUserLogin=SmartCardQuerySort.builder()
				.direction(SortDirection.DESC)
				.property(SmartCardSortProperty.USER_LOGIN)
				.build();
		
		SmartcardQueryDto searchAndSortAscendingByCardId=SmartcardQueryDto.builder()
				.page(page).sort(sortAscendingByCardId).build();
		SmartcardQueryDto searchAndSortDescendingByCardId=SmartcardQueryDto.builder()
				.page(page).sort(sortDescendingByCardId).build();
		SmartcardQueryDto searchAndSortAscendingByCompanyId=SmartcardQueryDto.builder()
				.page(page).sort(sortAscendingByCompanyId).build();
		SmartcardQueryDto searchAndSortDescendingByCompanyId=SmartcardQueryDto.builder()
				.page(page).sort(sortDescendingByCompanyId).build();
		SmartcardQueryDto searchAndSortAscendingByProtocol=SmartcardQueryDto.builder()
				.page(pageBig).sort(sortAscendingByProtocol).build();
		SmartcardQueryDto searchAndSortDescendingByProtocol=SmartcardQueryDto.builder()
				.page(pageBig).sort(sortDescendingByProtocol).build();
		SmartcardQueryDto searchAndSortAscendingByUserLogin=SmartcardQueryDto.builder()
				.page(page).sort(sortAscendingByUserLogin).build();
		SmartcardQueryDto searchAndSortDescendingByUserLogin=SmartcardQueryDto.builder()
				.page(page).sort(sortDescendingByUserLogin).build();
		
		return new Object[][] {
			{searchAndSortAscendingByCardId},
			{searchAndSortDescendingByCardId},
			{searchAndSortAscendingByProtocol},
			{searchAndSortDescendingByProtocol}
		};
	}
	
	
}
