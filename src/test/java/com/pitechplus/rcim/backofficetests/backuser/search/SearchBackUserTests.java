package com.pitechplus.rcim.backofficetests.backuser.search;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.backoffice.data.dataproviders.BackOfficeDataProviders;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.data.enums.BackUserSearchProperty;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearch;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchResponse;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSorter;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.SearchBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.SortingMatchers;
import com.pitechplus.rcim.BackendAbstract;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static com.pitechplus.rcim.backoffice.utils.mappers.BackUserMapper.mapBackUserToSearchFacets;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by dgliga on 10.03.2017.
 */
public class SearchBackUserTests extends BackendAbstract {

    private BackUser addedBackUser;

    @BeforeClass
    public void addBackUser() {
        addedBackUser = backUserService.createBackUser(rcimTestData.getAdminToken(),
                DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                        rcimTestData.getAdminSuperCompanyId())).getBody();
    }

    @Test(description = "This test verifies that calling search back user with all fields completed returns correct response.",
            dataProvider = "backUserSearchByOneField")
    @TestInfo(expectedResult = "Response contains correct information of searched back user.")
    public void searchAddedBackUserTest(BackUserSearch backUserSearch) {
        ResponseEntity<BackUserSearchResponse> searchResponse = backUserService.searchBackUser(rcimTestData.getAdminToken(),
                backUserSearch);
        assertThat("Back user from results is not the same as the one searched for !",
                searchResponse.getBody().getResults().get(0), samePropertyValuesAs(addedBackUser));
        assertThat("Search metadata facets on response is not the expected one !",
                searchResponse.getBody().getMetadata().getFacets(),
                samePropertyValuesAs(mapBackUserToSearchFacets(addedBackUser)));
    }

    @Test(description = "This test verifies that calling search back user only by companyId returns correct response.")
    @TestInfo(expectedResult = "Response contains all back users which have the companyId by which the search was performed.")
    public void searchBackUsersByCompanyIdTest() {
        addedBackUser = backUserService.createBackUser(rcimTestData.getAdminToken(),
                DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                        rcimTestData.getAdminSuperCompanyId())).getBody();
        ResponseEntity<BackUserSearchResponse> searchResponse = backUserService.searchBackUser(rcimTestData.getAdminToken(),
                SearchBuilders.buildBackUserSearchByCompanyId(rcimTestData.getAdminSuperCompanyId(), 10000));
        assertThat("Search by company id did not bring back users!", searchResponse.getBody().getResults().size(), is(greaterThan(0)));
        assertThat("Search did not included added back user", searchResponse.getBody().getResults(), hasItem(addedBackUser));
    }

    @Test(description = "This test verifies that calling search back user with empty search body works accordingly.")
    @TestInfo(expectedResult = "Response contains all back users which have the companyId belonging to the user which " +
            "the X-AUTH-TOKEN was used to call the service.")
    public void searchBackUsersByAdminTokenOnlyTest() {
        BackUserSearch emptySearch = BackUserSearch.builder()
                .page(Page.builder().number(1).size(10000).build())
                .build();
        ResponseEntity<BackUserSearchResponse> searchResponse = backUserService.searchBackUser(rcimTestData.getAdminToken(),
                emptySearch);
        assertThat("Search by company id did not bring back users!", searchResponse.getBody().getResults().size(), is(greaterThan(0)));
        assertThat("Search did not included added back user", searchResponse.getBody().getResults(), hasItem(addedBackUser));
    }

    @Test(description = "This test verifies that calling filteredsearch back user by role works accordingly.",
            dataProvider = "backOfficeRoles", dataProviderClass = BackOfficeDataProviders.class)
    @TestInfo(expectedResult = "Response contains all the back users with the role searched.")
    public void searchBackUsersByRoleTest(BackOfficeRole backOfficeRole) {
        ResponseEntity<BackUserSearchResponse> searchResponse = backUserService.searchBackUser(rcimTestData.getAdminToken(),
                SearchBuilders.buildBackUserSearchByRole(backOfficeRole, 10000));
        assertThat("Status is not ok!", searchResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat("Search by role did not bring any bak users!", searchResponse.getBody().getResults().size(), is(greaterThan(0)));
    }


    @Test(description = "This test verifies that calling search back user by enabled true/false works accordingly")
    @TestInfo(expectedResult = "Response contains all the back users matching the search criteria ( enabled true or false )")
    public void searchByEnabledTest() {
        ResponseEntity<BackUserSearchResponse> searchResponse = backUserService.searchBackUser(rcimTestData.getAdminToken(),
                SearchBuilders.buildBackUserSearchByEnabled(true, 10000));
        assertThat("Search by enabled did not bring any bak users!", searchResponse.getBody().getResults().size(), is(greaterThan(0)));
        assertThat("Search did not included added back user", searchResponse.getBody().getResults(), hasItem(addedBackUser));
    }

    //todo: add tests for enabled false after integrating service to enable users

    @Test(description = "This test verifies that calling search back user with sorting by string field works accordingly",
            dataProvider = "backUserSearchStringSorting")
    @TestInfo(expectedResult = "Response contains the back users sorted by the field which you requested ( firstName / lastName / email )" +
            "and the direction which you requested ( ASC / DESC )")
    public void sortByStringFieldTest(BackUserSearch backUserSearch) {
        List<BackUser> searchResults = backUserService.searchBackUser(rcimTestData.getAdminToken(), backUserSearch).getBody().getResults();
        assertThat(searchResults, SortingMatchers.isListSortedByField(backUserSearch.getSort().getProperty().getValue(),
                backUserSearch.getSort().getDirection()));
    }

    @Test(description = "This test verifies that calling search back user with sorting by enabled works accordingly.",
            dataProvider = "backUserSearchEnabledSorting")
    @TestInfo(expectedResult = "Response contains the back users sorted by the field which you requested ( enabled true / false )" +
            "and the direction which you requested ( ASC / DESC )")
    public void sortByEnabledTest(BackUserSearch backUserSearch) {
        List<BackUser> searchResults = backUserService.searchBackUser(rcimTestData.getAdminToken(), backUserSearch).getBody().getResults();
        assertThat(searchResults, SortingMatchers.isListSortedByBoolean(backUserSearch.getSort().getProperty().getValue(),
                backUserSearch.getSort().getDirection()));
    }

    @Test(description = "This test verifies that calling search back user with sorting by role works accordingly",
            dataProvider = "backUserSearchRoleSorting")
    @TestInfo(expectedResult = "Response contains the back users sorted by role and the direction which you requested ( ASC / DESC )")
    public void sortByRoleTest(BackUserSearch backUserSearch) {
        List<BackUser> searchResults = backUserService.searchBackUser(rcimTestData.getAdminToken(), backUserSearch).getBody().getResults();
        assertThat(searchResults, SortingMatchers.isListSortedByRole(backUserSearch.getSort().getProperty().getValue(),
                backUserSearch.getSort().getDirection()));
    }

    @DataProvider
    private Object[][] backUserSearchByOneField() {
        addedBackUser = backUserService.createBackUser(rcimTestData.getAdminToken(),
                DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                        rcimTestData.getAdminSuperCompanyId())).getBody();
        Page oneResultPage = Page.builder()
                .number(1)
                .size(1)
                .build();
        BackUserSearch searchByLastName = BackUserSearch.builder()
                .lastName(addedBackUser.getLastName())
                .page(oneResultPage)
                .build();
        BackUserSearch searchByFirstName = BackUserSearch.builder()
                .firstName(addedBackUser.getFirstName())
                .page(oneResultPage)
                .build();
        BackUserSearch searchByEmail = BackUserSearch.builder()
                .email(addedBackUser.getLogin())
                .page(oneResultPage)
                .build();

        BackUserSearch searchByAllFields = SearchBuilders.buildBackUserSearch(addedBackUser, 1, 1,
                BackUserSearchProperty.FIRSTNAME, SortDirection.ASC);

        return new Object[][]{
                {searchByLastName},
                {searchByFirstName},
                {searchByEmail},
                {searchByAllFields}
        };
    }

    /**
     * This data provider is used to create back user searches by one string field in both ASC / DESC sort directions
     *
     * @return BackUserSearch objects used to make requests to search back user service
     */
    @DataProvider
    private Object[][] backUserSearchStringSorting() {
        Page allBackUsersPage = Page.builder()
                .number(1)
                .size(10000)
                .build();

        BackUserSearch ascendingFirstName = new BackUserSearch();
        ascendingFirstName.setSort(BackUserSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.FIRSTNAME).build());
        ascendingFirstName.setPage(allBackUsersPage);
        BackUserSearch descendingFirstName = new BackUserSearch();
        descendingFirstName.setSort(BackUserSorter.builder().direction(SortDirection.DESC).property(BackUserSearchProperty.FIRSTNAME).build());
        descendingFirstName.setPage(allBackUsersPage);
        BackUserSearch ascendingLastName = new BackUserSearch();
        ascendingLastName.setSort(BackUserSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.LASTNAME).build());
        ascendingLastName.setPage(allBackUsersPage);
        BackUserSearch descendingLastName = new BackUserSearch();
        descendingLastName.setSort(BackUserSorter.builder().direction(SortDirection.DESC).property(BackUserSearchProperty.LASTNAME).build());
        descendingLastName.setPage(allBackUsersPage);
        BackUserSearch ascendingEmail = new BackUserSearch();
        ascendingEmail.setSort(BackUserSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.EMAIL).build());
        ascendingEmail.setPage(allBackUsersPage);
        BackUserSearch descendingEmail = new BackUserSearch();
        descendingEmail.setSort(BackUserSorter.builder().direction(SortDirection.DESC).property(BackUserSearchProperty.EMAIL).build());
        descendingEmail.setPage(allBackUsersPage);

        return new Object[][]{
                {ascendingFirstName},
                {descendingFirstName},
                {ascendingLastName},
                {descendingLastName},
                {ascendingEmail},
                {descendingEmail}
        };
    }

    /**
     * This data provider is used to create back user searches by boolean field enabled in both ASC / DESC sort directions
     *
     * @return BackUserSearch objects used to make requests to search back user service
     */
    @DataProvider
    private Object[][] backUserSearchEnabledSorting() {
        Page allBackUsersPage = Page.builder()
                .number(1)
                .size(10000)
                .build();

        BackUserSearch ascendingEnabled = new BackUserSearch();
        ascendingEnabled.setSort(BackUserSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.ENABLED).build());
        ascendingEnabled.setPage(allBackUsersPage);
        BackUserSearch descendingEnabled = new BackUserSearch();
        descendingEnabled.setSort(BackUserSorter.builder().direction(SortDirection.DESC).property(BackUserSearchProperty.ENABLED).build());
        descendingEnabled.setPage(allBackUsersPage);

        return new Object[][]{
                {ascendingEnabled},
                {descendingEnabled},

        };
    }

    /**
     * This data provider is used to create back user searches by back office role in both ASC / DESC sort directions
     *
     * @return BackUserSearch objects used to make requests to search back user service
     */
    @DataProvider
    private Object[][] backUserSearchRoleSorting() {
        Page allBackUsersPage = Page.builder()
                .number(1)
                .size(10000)
                .build();
        BackUserSearch ascendingRole = new BackUserSearch();
        ascendingRole.setSort(BackUserSorter.builder().direction(SortDirection.ASC).property(BackUserSearchProperty.ROLE).build());
        ascendingRole.setPage(allBackUsersPage);
        BackUserSearch descendingRole = new BackUserSearch();
        descendingRole.setSort(BackUserSorter.builder().direction(SortDirection.DESC).property(BackUserSearchProperty.ROLE).build());
        descendingRole.setPage(allBackUsersPage);

        return new Object[][]{
                {ascendingRole},
                {descendingRole},
        };
    }


}
