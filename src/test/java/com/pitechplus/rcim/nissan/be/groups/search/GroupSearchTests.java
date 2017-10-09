package com.pitechplus.rcim.nissan.be.groups.search;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.custommatchers.SoftAssert;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.custommatchers.SortingMatchers;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupPropertyDto;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.GroupStateDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.*;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.pitechplus.rcim.nissan.be.nissanutils.nissancustommatchers.NissanSortingMatchers;
import org.joda.time.LocalDate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga  on 13.09.2017.
 */
public class GroupSearchTests extends BackendAbstract {

    private SoftAssert softAssert;
    private GroupViewDto groupDto;

    @BeforeClass
    public void createParkingGroup() throws InterruptedException {
        softAssert = new SoftAssert();

        groupDto = nissanBeServices.createGroup(rcimTestData.getSuperAdminToken(),
                NissanDtoBuilders.buildGroupCreateDto(nissanUtils.createListOfMembersOfTheSameCompany(2, 0, rcimTestData),
                        rcimTestData, parkingService.createParking(rcimTestData.getSuperAdminToken(), rcimTestData.getSiteDto().getId(),
                                DtoBuilders.buildParkingCreate()).getBody())).getBody();
        groupDto.setParking(parkingService.getParking(rcimTestData.getSuperAdminToken(), groupDto.getParkingId()).getBody());
    }

    @Test(description = "This test verifies that booking search for first page ( as shown in admin app also ) works accordingly")
    @TestInfo(expectedResult = "Groups are retrieved according to the size sent on request.")
    public void searchFirstPageGroupsTest() {
        GroupQueryDto groupQueryDto = GroupQueryDto.builder()
                .page(Page.builder().size(10).number(1).build()).build();
        GroupResultDto groupResultDto = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(),
                groupQueryDto).getBody();
        assertThat("First Page not retrieved correctly!", groupResultDto.getResults().size(),
                is(groupQueryDto.getPage().getSize()));
        //todo: not enough verifications in order to determine it actually worked
    }

    @Test(description = "This test verifies that search groups request by invoicing suspended field " +
            "works accordingly.", dataProvider = "groupInvoicingSuspended")
    @TestInfo(expectedResult = "Search response contains groups which have the invoicing suspended field as specified in the " +
            "search groups request.")
    public void searchGroupsInvoicingSuspended(Boolean groupInvoicingSuspended) {
        GroupQueryDto byInvoicingSuspended = GroupQueryDto.builder()
                .invoicingSuspended(groupInvoicingSuspended)
                .page(Page.builder().number(1).size(50).build()).build();
        GroupResultDto groupResultDto = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(),
                byInvoicingSuspended).getBody();
        for (GroupSearchDto groupSearchDto : groupResultDto.getResults()) {
            softAssert.assertTrue(groupSearchDto.getInvoicingSuspended().equals(groupInvoicingSuspended),
                    "Group returned by search did not have the correct invoicing suspended! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }


    @Test(dataProvider = "groupSize", description = "This test verifies that search groups request by group size " +
            "works accordingly.")
    @TestInfo(expectedResult = "Search response contains groups which have the group size as specified in the " +
            "search groups request.")
    public void searchGroupsNumberOfMembers(Integer groupSize) {
        GroupQueryDto bySize = GroupQueryDto.builder()
                .groupSize(groupSize)
                .page(Page.builder().number(1).size(50).build()).build();
        GroupResultDto groupResultDto = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(),
                bySize).getBody();
        for (GroupSearchDto groupSearchDto : groupResultDto.getResults()) {
            softAssert.assertTrue(groupSearchDto.getGroupSize().equals(groupSize),
                    "Group returned by search did not have the correct size! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(dataProvider = "groupStatus", description = "This test verifies that search groups request by group status type " +
            "works accordingly.")
    @TestInfo(expectedResult = "Search response contains groups which have the group status type as specified in the " +
            "search groups request.")
    public void searchGroupsStatusTypeTest(GroupStateDto groupStateDto) {
        GroupQueryDto byStatus = GroupQueryDto.builder()
                .groupState(groupStateDto)
                .page(Page.builder().number(1).size(50).build()).build();
        GroupResultDto groupResultDto = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(),
                byStatus).getBody();
        for (GroupSearchDto groupSearchDto : groupResultDto.getResults()) {
            softAssert.assertTrue(groupSearchDto.getGroupState().equals(groupStateDto),
                    "Group returned by search did not have the correct status! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(description = "This test verifies that search groups request by start date works accordingly.")
    @TestInfo(expectedResult = "Search response contains groups which have the group start date as specified in the " +
            "search groups request.")
    public void searchGroupsByStartDate() {
        String startDate = new LocalDate().toString();
        GroupQueryDto byStartDate = GroupQueryDto.builder()
                .startDate(startDate)
                .page(Page.builder().number(1).size(50).build()).build();
        GroupResultDto groupResultDto = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(),
                byStartDate).getBody();
        for (GroupSearchDto groupSearchDto : groupResultDto.getResults()) {
            softAssert.assertTrue(groupSearchDto.getStartDate().equals(startDate),
                    "Group returned by search did not have the correct startDate! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(description = "This test verifies that search groups request by end date works accordingly.")
    @TestInfo(expectedResult = "Search response contains groups which have the group endDate as specified in the " +
            "search groups request.")
    public void searchGroupsByEndDate() {
        String endDate = new LocalDate().toString();
        GroupQueryDto byEndDate = GroupQueryDto.builder()
                .endDate(endDate)
                .page(Page.builder().number(1).size(50).build()).build();
        GroupResultDto groupResultDto = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(),
                byEndDate).getBody();
        for (GroupSearchDto groupSearchDto : groupResultDto.getResults()) {
            softAssert.assertTrue(groupSearchDto.getStartDate().equals(endDate),
                    "Group returned by search did not have the correct endDate! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(description = "This test verifies that search groups request by group publicId works accordingly.")
    @TestInfo(expectedResult = "Search response contains groups which have the group publicId as specified in the " +
            "search groups request.")
    public void searchByGroupId() {
        String publicId = groupDto.getPublicId();
        GroupQueryDto byGroupId = GroupQueryDto.builder()
                .publicId(publicId)
                .page(Page.builder().number(1).size(50).build()).build();
        List<GroupSearchDto> searchResults = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(), byGroupId).getBody().getResults();
        softAssert.assertTrue(searchResults.size() > 0, "The result list was empty!");
        for (GroupSearchDto groupSearchDto : searchResults) {
            softAssert.assertTrue(groupSearchDto.getPublicId().contains(publicId),
                    "Group returned by search did not have the correct end date! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(description = "This test verifies that search groups request by site name works accordingly.")
    @TestInfo(expectedResult = "Search response contains groups which have the group site name as specified in the " +
            "search groups request.")
    public void searchBySite() {
        String siteName = groupDto.getParking().getSite().getName();
        String[] siteNameSplit = siteName.split("\\s+");
        Boolean containsSiteName = false;
        GroupQueryDto bySiteName = GroupQueryDto.builder()
                .siteName(siteName)
                .page(Page.builder().number(1).size(50).build()).build();
        List<GroupSearchDto> searchResults = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(), bySiteName).getBody().getResults();
        softAssert.assertTrue(searchResults.size() > 0, "The result list was empty!");
        for (GroupSearchDto groupSearchDto : searchResults) {
            for (String word : siteNameSplit)
                if (groupSearchDto.getSiteName().contains(word))
                    containsSiteName = true;
            softAssert.assertTrue(containsSiteName,
                    "Group returned by search did not have the correct site name! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(dataProvider = "memberName", description = "This test verifies that search groups request by member name works accordingly.")
    @TestInfo(expectedResult = "Search response contains groups which have the group with at least one member name as specified in the " +
            "search groups request.")
    public void searchByMemberName(String memberName) {
        GroupQueryDto byMemberName = GroupQueryDto.builder()
                .memberName(memberName)
                .page(Page.builder().number(1).size(50).build()).build();
        List<GroupSearchDto> searchResults = nissanBeServices.searchGroups(rcimTestData.getSuperAdminToken(), byMemberName).getBody().getResults();
        softAssert.assertTrue(searchResults.size() > 0, "The result list was empty! ");
        for (GroupSearchDto groupSearchDto : searchResults) {
            Boolean containsName = false;
            Set<GroupMembershipViewDto> groupMembershipViewDto = nissanBeServices.getGroup(rcimTestData.getSuperAdminToken(),
                    groupSearchDto.getId()).getBody().getGroupMemberships();
            for (GroupMembershipViewDto groupMember : groupMembershipViewDto)
                if ((groupMember.getMemberLastName().equals(memberName)) || (groupMember.getMemberFirstName().equals(memberName))) {
                    containsName = true;
                    break;
                }
            softAssert.assertTrue(containsName,
                    "Group returned by search did not have the correct member name! Group id:" + groupSearchDto.getId());
        }
        softAssert.assertAll();
    }

    @Test(dataProvider = "groupStatusSorting", description = "This test verifies that calling search groups with sorting by status works accordingly")
    @TestInfo(expectedResult = "Response contains the groups sorted by status and the direction which you requested ( ASC / DESC )")
    public void sortByGroupStatus(GroupQueryDto groupQueryDto) {
        List<GroupSearchDto> searchResults = nissanBeServices.searchGroups(rcimTestData.getAdminToken(), groupQueryDto).getBody().getResults();
        assertThat(searchResults, NissanSortingMatchers.isListSortedByGroupStatus(groupQueryDto.getSort().getProperty().getValue(),
                groupQueryDto.getSort().getDirection()));
    }

    @Test(dataProvider = "groupSearchStringSorting", description = "This test verifies that calling search groups with sorting by string field works accordingly")
    @TestInfo(expectedResult = "Response contains the groups sorted by the field which you requested ( publicId / siteName / startDate/EndDate )" +
            "and the direction which you requested ( ASC / DESC )")
    public void sortByStringFieldTest(GroupQueryDto groupQueryDto) {
        List<GroupSearchDto> searchResults = nissanBeServices.searchGroups(rcimTestData.getAdminToken(), groupQueryDto).getBody().getResults();
        assertThat(searchResults, SortingMatchers.isListSortedByField(groupQueryDto.getSort().getProperty().getValue(),
                groupQueryDto.getSort().getDirection()));
    }

    @DataProvider
    private Object[][] groupInvoicingSuspended() {
        return new Object[][]{
                {true},
                {false}
        };
    }

    @DataProvider
    private Object[][] groupSize() {
        return new Object[][]{
                {2},
                {3},
                {4},
                {5}
        };
    }

    @DataProvider
    private Object[][] groupStatus() {
        return new Object[][]{
                {GroupStateDto.INACTIVE},
                {GroupStateDto.ACTIVE},
                {GroupStateDto.CLOSED},
                {GroupStateDto.ENDED},
                {GroupStateDto.PENDING},
                {GroupStateDto.READY}
        };
    }

    @DataProvider
    private Object[][] memberName() {

        return new Object[][]{
                {groupDto.getGroupMemberships().iterator().next().getMemberFirstName()},
                {groupDto.getGroupMemberships().iterator().next().getMemberLastName()}
        };
    }

    /**
     * This data provider is used to create group searches by group status in both ASC / DESC sort directions
     *
     * @return GroupQueryDto objects used to make requests to search group service
     */
    @DataProvider
    private Object[][] groupStatusSorting() {
        Page allGroupsPage = Page.builder()
                .number(1)
                .size(10000)
                .build();
        GroupQueryDto ascendingState = new GroupQueryDto();
        ascendingState.setSort(GroupSorterDto.builder().direction(SortDirection.ASC).property(GroupPropertyDto.GROUP_STATE).build());
        ascendingState.setPage(allGroupsPage);
        GroupQueryDto descendingState = new GroupQueryDto();
        descendingState.setSort(GroupSorterDto.builder().direction(SortDirection.DESC).property(GroupPropertyDto.GROUP_STATE).build());
        descendingState.setPage(allGroupsPage);

        return new Object[][]{
                {ascendingState},
                {descendingState}
        };
    }

    /**
     * This data provider is used to create back user searches by one string field in both ASC / DESC sort directions
     *
     * @return BackUserSearch objects used to make requests to search back user service
     */
    @DataProvider
    private Object[][] groupSearchStringSorting() {
        Page allBackUsersPage = Page.builder()
                .number(1)
                .size(10000)
                .build();

        GroupQueryDto ascendingGroupId = new GroupQueryDto();
        ascendingGroupId.setSort(GroupSorterDto.builder().direction(SortDirection.ASC).property(GroupPropertyDto.PUBLIC_ID).build());
        ascendingGroupId.setPage(allBackUsersPage);

        GroupQueryDto descendingPublicId = new GroupQueryDto();
        descendingPublicId.setSort(GroupSorterDto.builder().direction(SortDirection.DESC).property(GroupPropertyDto.PUBLIC_ID).build());
        descendingPublicId.setPage(allBackUsersPage);

        GroupQueryDto ascendingSiteName = new GroupQueryDto();
        ascendingSiteName.setSort(GroupSorterDto.builder().direction(SortDirection.ASC).property(GroupPropertyDto.SITE_NAME).build());
        ascendingSiteName.setPage(allBackUsersPage);

        GroupQueryDto descendingSiteName = new GroupQueryDto();
        descendingSiteName.setSort(GroupSorterDto.builder().direction(SortDirection.DESC).property(GroupPropertyDto.SITE_NAME).build());
        descendingSiteName.setPage(allBackUsersPage);

        return new Object[][]{
                {ascendingGroupId},
                {descendingPublicId},
                {ascendingSiteName},
                {descendingSiteName}
        };
    }
}