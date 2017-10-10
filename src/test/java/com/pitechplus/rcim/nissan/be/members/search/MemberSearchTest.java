package com.pitechplus.rcim.nissan.be.members.search;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.BackUserSearchProperty;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.SortingMatchers;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberSearchDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberSorter;
import com.pitechplus.rcim.nissan.be.nissandto.members.SearchMemberResponse;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;

public class MemberSearchTest extends BackendAbstract {

	private MemberDto memberDto;
	private MemberCreateDto memberCreateDto;

	@BeforeClass
	public void createMember() {
		memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
				configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody()
						.getId());
		memberDto = nissanBeServices.createMember(rcimTestData.getAdminToken(), memberCreateDto).getBody();
	}

	@Test(dataProvider = "searchDataMember")
	public void searchMemberTest(MemberSearchDto searchMemberxxx) {
		// This method should check the search member
		// the search DTO should be done
		ResponseEntity<SearchMemberResponse> searchResult = nissanBeServices.searchMember(rcimTestData.getAdminToken(),
				searchMemberxxx);
		MemberDto x = (searchResult.getBody().getResults().get(0));
		x.setDrivingLicence(null);
		memberDto.setDrivingLicence(null);
		assertThat("Get a single member service did not respond with the correct information !", x,
				samePropertyValuesAs(memberDto));
	}

	@Test(dataProvider = "searchDataMemberWithCompanyId", description = "This test verifies that calling search member only by companyId returns correct response.")
	@TestInfo(expectedResult = "Response contains all members which have the companyId by which the search was performed.")
	public void searchMemberByCompanyIdTest(MemberSearchDto searchWithCompany) {
		ResponseEntity<SearchMemberResponse> searchResult = nissanBeServices.searchMember(rcimTestData.getAdminToken(),
				searchWithCompany);
		assertThat("Search by company id did not bring member!", searchResult.getBody().getResults().size(),
				is(greaterThan(0)));
		memberDto.setDrivingLicence(null);

		assertThat("Search did not included added member", searchResult.getBody().getResults(), hasItem(memberDto));
	}

	

	@Test(description = "This test verifies that calling search Member with sorting by firstname, lastname and email works accordingly", dataProvider = "searchMemberByField")
	@TestInfo(expectedResult = "Response contains the Member sorted by firstname, lastname and email and the direction which you requested ( ASC / DESC )")
	public void sortByRoleTest(MemberSearchDto memberSearchDto) {
		List<MemberDto> searchResults = nissanBeServices.searchMember(rcimTestData.getAdminToken(), memberSearchDto)
				.getBody().getResults();
		assertThat(searchResults, SortingMatchers.isListSortedByField(
				memberSearchDto.getSort().getProperty().getValue(), memberSearchDto.getSort().getDirection()));
	}

	@Test(description = "This test verifies that calling search Member with sorting by VIP works", dataProvider = "searchMemberSortingByVIP")
	@TestInfo(expectedResult = "Response contains the Member sorted by VIP and the direction which you requested ( ASC / DESC )")
	public void sortByVIP(MemberSearchDto memberSearchDto) {
		List<MemberDto> searchResults = nissanBeServices.searchMember(rcimTestData.getAdminToken(), memberSearchDto)
				.getBody().getResults();
		assertThat(searchResults, SortingMatchers.isListSortedByBoolean(
				memberSearchDto.getSort().getProperty().getValue(), memberSearchDto.getSort().getDirection()));
	}

	@DataProvider
	public Object[][] searchMemberByField() {
		Page AllResultPage = Page.builder().number(1).size(200).build();
		MemberSearchDto sortByAsendingFirstname = new MemberSearchDto();
		sortByAsendingFirstname.setSort(
				MemberSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.FIRSTNAME).build());
		sortByAsendingFirstname.setPage(AllResultPage);

		MemberSearchDto sortByDessendingFirstname = new MemberSearchDto();
		sortByDessendingFirstname.setSort(MemberSorter.builder().direction(SortDirection.DESC)
				.property(BackUserSearchProperty.FIRSTNAME).build());
		sortByDessendingFirstname.setPage(AllResultPage);

		MemberSearchDto sortByAsendingLatsName = new MemberSearchDto();
		sortByAsendingLatsName.setSort(
				MemberSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.LASTNAME).build());
		sortByAsendingLatsName.setPage(AllResultPage);

		MemberSearchDto sortByDesendingLastname = new MemberSearchDto();
		sortByDesendingLastname.setSort(
				MemberSorter.builder().direction(SortDirection.DESC).property(BackUserSearchProperty.LASTNAME).build());
		sortByDesendingLastname.setPage(AllResultPage);

		MemberSearchDto sortByAsendingEmail = new MemberSearchDto();
		sortByAsendingEmail.setSort(
				MemberSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.EMAIL).build());
		sortByAsendingEmail.setPage(AllResultPage);

		MemberSearchDto sortByDesendingEmail = new MemberSearchDto();
		sortByDesendingEmail.setSort(
				MemberSorter.builder().direction(SortDirection.DESC).property(BackUserSearchProperty.EMAIL).build());
		sortByDesendingEmail.setPage(AllResultPage);

		return new Object[][] { { sortByAsendingFirstname }, { sortByDessendingFirstname }, { sortByAsendingLatsName },
				{ sortByDesendingLastname }, { sortByAsendingEmail }, { sortByDesendingEmail } };
	}

	@DataProvider
	public Object[][] searchMemberSortingByVIP() {
		Page AllResultPage = Page.builder().number(1).size(200).build();
		MemberSearchDto sortByAsendingVIP = new MemberSearchDto();
		sortByAsendingVIP.setSort(
				MemberSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.VIP).build());
		sortByAsendingVIP.setPage(AllResultPage);
		MemberSearchDto sortByDescendingVIP = new MemberSearchDto();
		sortByDescendingVIP.setSort(
				MemberSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.VIP).build());
		sortByDescendingVIP.setPage(AllResultPage);

		return new Object[][] { { sortByAsendingVIP }, { sortByDescendingVIP } };
	}
	@DataProvider
	private Object[][] searchDataMember() {
		Page oneResultPage = Page.builder().number(1).size(1).build();

		MemberSearchDto searchByLogin = MemberSearchDto.builder().email(memberDto.getLogin()).page(oneResultPage)
				.build();
		MemberSearchDto searchByFirstname = MemberSearchDto.builder().firstname(memberDto.getFirstName())
				.page(oneResultPage).build();
		MemberSearchDto searchByLastname = MemberSearchDto.builder().lastname(memberDto.getLastName())
				.page(oneResultPage).build();
		MemberSearchDto searchByMemberName = MemberSearchDto.builder()
				.memberName(memberDto.getFirstName() + " " + memberDto.getLastName()).page(oneResultPage).build();
		MemberSearchDto searchByAll = MemberSearchDto.builder().email(memberDto.getLogin())
				.firstname(memberDto.getFirstName()).lastname(memberDto.getLastName())
				.memberName(memberDto.getFirstName() + " " + memberDto.getLastName()).page(oneResultPage).build();

		return new Object[][] { 
			{ searchByLogin }, 
			{ searchByFirstname }, 
			{ searchByLastname }, 
			{ searchByMemberName },
			{ searchByAll } };
	}

	@DataProvider
	private Object[][] searchDataMemberWithCompanyId() {
		Page oneResultPage = Page.builder().number(1).size(200).build();

		MemberSearchDto searchByCompanyId = MemberSearchDto.builder()
				.CompanyId((memberDto.getCompany().getId().toString())).page(oneResultPage).build();

		return new Object[][] { { searchByCompanyId },

		};
	}

}
