package com.pitechplus.rcim.backofficetests.supercompany.search;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.backoffice.data.enums.SuperCompanySearchProperty;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearch;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearchResult;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySorter;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import com.pitechplus.rcim.backoffice.utils.custommatchers.SortingMatchers;
import com.pitechplus.rcim.BackendAbstract;
import com.rits.cloning.Cloner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 13.06.2017.
 */
public class SearchSuperCompaniesTest extends BackendAbstract {

    private Set<SuperCompanyDto> superCompanies;

    @BeforeClass(description = "Make call to get all super companies using GET service.")
    public void getAllSuperCompanies() {
        superCompanies = new HashSet<>(Arrays.asList(companyService.getSuperCompanies(rcimTestData.getAdminToken())
                .getBody()));
    }


    @Test(description = "This test verifies that calling search super company by NAME sorting ASC / DESC works accordingly",
            dataProvider = "superCompaniesSorting")
    @TestInfo(expectedResult = "Response contains the super companies sorted by the field which you requested ( NAME)" +
            "and the direction which you requested ( ASC / DESC ) and all super companies with correct details are brought.")
    public void superCompanySearchTest(SuperCompanySearch superCompanySearch) {
        SuperCompanySearchResult searchResult = companyService.searchSuperCompanies(rcimTestData.getSuperAdminToken(),
                superCompanySearch).getBody();
        assertThat(searchResult.getResults(), SortingMatchers.isListSortedByField(superCompanySearch.getSort().getProperty()
                .getValue().toLowerCase(), superCompanySearch.getSort().getDirection()));
        assertThat("super companies brought by search is not working accordingly.", new HashSet<>(searchResult.getResults()),
                is(superCompanies));

    }

    /**
     * This data provider creates super company search requests in order to verify sorting by NAME
     *
     * @return SuperCompanySearch objects
     */
    @DataProvider
    private Object[][] superCompaniesSorting() {
        Page allSuperCompaniesPage = Page.builder()
                .number(1)
                .size(50)
                .build();
        Cloner cloningMachine = new Cloner();
        SuperCompanySearch ascName = SuperCompanySearch.builder()
                .page(allSuperCompaniesPage)
                .sort(new SuperCompanySorter(SortDirection.ASC, SuperCompanySearchProperty.NAME)).build();
        SuperCompanySearch descName = cloningMachine.deepClone(ascName);
        descName.setSort((new SuperCompanySorter(SortDirection.DESC, SuperCompanySearchProperty.NAME)));

        return new Object[][]{
                {ascName},
                {descName},
        };
    }
}
