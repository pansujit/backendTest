package com.pitechplus.rcim.backoffice.utils.builders;

import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.data.enums.BackUserSearchProperty;
import com.pitechplus.rcim.backoffice.data.enums.SuperCompanySearchProperty;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.data.enums.SortDirection;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearch;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSorter;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearch;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySorter;

import java.util.UUID;

/**
 * Created by dgliga on 13.06.2017.
 */
public class SearchBuilders {


    /**
     * This method builds a Back User Search object which is needed for the request to filteredsearch a back user
     *
     * @param backUser               from which you want to extract the information for the filteredsearch
     * @param pageNumber             which page from the response you want
     * @param pageSize               how big you want the page from the response to be
     * @param backUserSearchProperty based on which you want to sort the results
     * @param sortDirection          direction of sorting ASC / DESC
     * @return BackUserSearch object valid for making a filteredsearch back user call
     */
    public static BackUserSearch buildBackUserSearch(BackUser backUser, int pageNumber, int pageSize,
                                                     BackUserSearchProperty backUserSearchProperty, SortDirection sortDirection) {
        return BackUserSearch.builder()
                .companyId(backUser.getCompany().getId())
                .email(backUser.getLogin())
                .firstName(backUser.getFirstName())
                .lastName(backUser.getLastName())
                .page(Page.builder().number(pageNumber).size(pageSize).build())
                .role(BackOfficeRole.valueOf(backUser.getRole()))
                .enabled(backUser.getEnabled())
                .sort(BackUserSorter.builder().direction(sortDirection).property(backUserSearchProperty).build())
                .build();
    }

    /**
     * This method builds a Back User Search object by companyId only
     *
     * @param companyId          for which you want to make the filteredsearch
     * @param expectedResultSize how many results you want on the filteredsearch response
     * @return BackUserSearch object valid to make call to filteredsearch back user by companyId
     */
    public static BackUserSearch buildBackUserSearchByCompanyId(UUID companyId, int expectedResultSize) {
        return BackUserSearch.builder()
                .companyId(companyId)
                .page(Page.builder().number(1).size(expectedResultSize).build())
                .build();
    }

    /**
     * This method builds a Back User Search object by role only
     *
     * @param backOfficeRole     for which you want to make the filteredsearch
     * @param expectedResultSize how many results you want on the filteredsearch response
     * @return BackUserSearch object valid to make call to filteredsearch back user by role
     */
    public static BackUserSearch buildBackUserSearchByRole(BackOfficeRole backOfficeRole, int expectedResultSize) {
        return BackUserSearch.builder()
                .role(backOfficeRole)
                .page(Page.builder().number(1).size(expectedResultSize).build())
                .build();
    }

    /**
     * This method builds a Back User Search object by enabled only
     *
     * @param enabled            true or false depending on which back users you want to filteredsearch
     * @param expectedResultSize how many results you want on the filteredsearch response
     * @return BackUserSearch object valid to make call to filteredsearch back user by enabled true / false
     */
    public static BackUserSearch buildBackUserSearchByEnabled(boolean enabled, int expectedResultSize) {
        return BackUserSearch.builder()
                .enabled(enabled)
                .page(Page.builder().number(1).size(expectedResultSize).build())
                .build();
    }

    public static SuperCompanySearch buildSuperCompanySearch(int pageNumber, int pageSize,
                                                             SuperCompanySearchProperty superCompanySearchProperty,
                                                             SortDirection sortDirection) {
        return SuperCompanySearch.builder()
                .page(Page.builder().number(pageNumber).size(pageSize).build())
                .sort(SuperCompanySorter.builder().direction(sortDirection).property(superCompanySearchProperty).build())
                .build();
    }
}
