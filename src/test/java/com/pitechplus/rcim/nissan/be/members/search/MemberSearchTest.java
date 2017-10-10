package com.pitechplus.rcim.nissan.be.members.search;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.data.enums.BackUserSearchProperty;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearch;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchResponse;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.SearchBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.SortingMatchers;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberSearchDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberSorter;
import com.pitechplus.rcim.nissan.be.nissandto.members.SearchMemberResponse;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class MemberSearchTest extends BackendAbstract {
	
    private MemberDto memberDto;
    private MemberCreateDto memberCreateDto;
   /*@BeforeClass
    public void createMember() {
    	System.out.println("sstarting");
    	System.out.println("the companu id is " + rcimTestData.getAdminSuperCompanyId().toString());
       memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                configsService.createFile(rcimTestData.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
       System.out.println("the admin token is +" +rcimTestData.getAdminToken());
       memberDto = nissanBeServices.createMember(rcimTestData.getAdminToken(), memberCreateDto).getBody();
    }*/
    
   /*  @Test(dataProvider="searchDataMember")
    public void searchMemberTest(MemberSearchDto searchMemberxxx) {
    	// This method should check the search member 
    	// the search DTO should be done
   ResponseEntity<SearchMemberResponse> searchResult=nissanBeServices.searchMember(rcimTestData.getAdminToken(), searchMemberxxx);
   System.out.println("this bbody is " + searchResult.getBody().getResults().get(0));
   MemberDto x=(searchResult.getBody().getResults().get(0));
   x.setDrivingLicence(null);
   memberDto.setDrivingLicence(null);
   assertThat("Get a single member service did not respond with the correct information !",
		  x , samePropertyValuesAs(memberDto));
    }
   @Test(dataProvider="searchDataMemberWithCompanyId",description = "This test verifies that calling search member only by companyId returns correct response.")
   @TestInfo(expectedResult = "Response contains all members which have the companyId by which the search was performed.")
   public void searchMemberByCompanyIdTest(MemberSearchDto searchWithCompany) {
	   ResponseEntity<SearchMemberResponse> searchResult=nissanBeServices.searchMember(rcimTestData.getAdminToken(), searchWithCompany);
       assertThat("Search by company id did not bring back users!", searchResult.getBody().getResults().size(), is(greaterThan(0)));
       memberDto.setDrivingLicence(null);
     
       assertThat("Search did not included added back user", searchResult.getBody().getResults(), hasItem(memberDto));
   }*/
   
    @DataProvider
    private Object [][] searchDataMember(){
        Page oneResultPage = Page.builder()
                .number(1)
                .size(1)
                .build();
    	
    	MemberSearchDto searchByLogin= MemberSearchDto.builder().email(memberDto.getLogin()).page(oneResultPage).build();
    	MemberSearchDto searchByFirstname=MemberSearchDto.builder().firstname(memberDto.getFirstName()).page(oneResultPage).build();
    MemberSearchDto searchByLastname= MemberSearchDto.builder().lastname(memberDto.getLastName()).page(oneResultPage).build();
    MemberSearchDto searchByMemberName= MemberSearchDto.builder().memberName(memberDto.getFirstName() +" "+ memberDto.getLastName()).page(oneResultPage).build();
    	MemberSearchDto searchByAll= MemberSearchDto.builder().email(memberDto.getLogin())
    			.firstname(memberDto.getFirstName())
    			.lastname(memberDto.getLastName())
    			.memberName(memberDto.getFirstName() +" "+ memberDto.getLastName())
    			.page(oneResultPage).build();
        
    	return new Object[][]{
    		{searchByLogin},
    		{searchByFirstname},
    		{searchByLastname},
    		{searchByMemberName},
    		{searchByAll}
    	};
    }
    
    

  
  @DataProvider
  private Object [][] searchDataMemberWithCompanyId(){
      Page oneResultPage = Page.builder()
              .number(1)
              .size(200)
              .build();
  	
  
  	MemberSearchDto searchByCompanyId=MemberSearchDto.builder().CompanyId((memberDto.getCompany().getId().toString()))
  			.page(oneResultPage).build();
      
  	return new Object[][]{
  		{searchByCompanyId},

  	};
  }
  
  @Test(description = "This test verifies that calling search back user with sorting by firstname, lastname and email works accordingly",
          dataProvider = "searchMemberRoleSorting")
  @TestInfo(expectedResult = "Response contains the back users sorted by role and the direction which you requested ( ASC / DESC )")
  public void sortByRoleTest(MemberSearchDto memberSearchDto) {
	  List<MemberDto> searchResults= nissanBeServices.searchMember(rcimTestData.getAdminToken(),memberSearchDto).getBody().getResults();
      assertThat(searchResults, SortingMatchers.isListSortedByField(memberSearchDto.getSort().getProperty().getValue(),
    		  memberSearchDto.getSort().getDirection()));
  }
  
  
  @DataProvider
  public Object[][] searchMemberRoleSorting(){
      Page AllResultPage = Page.builder()
              .number(1)
              .size(200)
              .build();
	  MemberSearchDto sortByAsending= new MemberSearchDto();
	  sortByAsending.setSort(MemberSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.FIRSTNAME).build());
	  sortByAsending.setPage(AllResultPage);
	  return new Object[][] {
		  {sortByAsending}
	  };
	
	  
  }

}
