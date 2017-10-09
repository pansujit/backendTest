package com.pitechplus.rcim.backoffice.utils.mappers;

import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserUpdate;
import com.pitechplus.rcim.backoffice.dto.common.Company;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchFacets;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by dgliga on 20.02.2017.
 */

public class BackUserMapper {

    /**
     * This method maps database information from a BackUserCreate to BackUser object
     *
     * @param backUserCreate object which is used to created a back office user
     * @return BackUser object with information from the request
     */
    public static BackUser mapBackUserCreateToBackUser(BackUserCreate backUserCreate) {
        return BackUser.builder()
                .id(backUserCreate.getId())
                .address(backUserCreate.getAddress())
                .phoneNumber(backUserCreate.getPhoneNumber())
                .firstName(backUserCreate.getFirstName())
                .lastName(backUserCreate.getLastName())
                .locale(backUserCreate.getLocale())
                .login(backUserCreate.getLogin())
                .role(backUserCreate.getRole())
                .enabled(true)//user is created with enabled on true by default
                .suspended(false)//user is created with suspended on false by default
                .company(Company.builder().id(backUserCreate.getCompanyId()).build())
                .build();
    }

    /**
     * This method maps database information from a BackUserUpdate to BackUser object
     *
     * @param backUserUpdate object which is used to update a back office user
     * @return BackUser object with information from the request
     */
    public static BackUser mapBackUserUpdateToBackUser(BackUserUpdate backUserUpdate, String login, UUID id) {
        return BackUser.builder()
                .id(id)
                .address(backUserUpdate.getAddress())
                .phoneNumber(backUserUpdate.getPhoneNumber())
                .secondaryPhoneNumber(backUserUpdate.getSecondaryPhoneNumber())
                .firstName(backUserUpdate.getFirstName())
                .lastName(backUserUpdate.getLastName())
                .locale(backUserUpdate.getLocale())
                .login(login)
                .role(backUserUpdate.getRole())
                .enabled(true)//user is created with enabled on true by default
                .suspended(false)//user is created with suspended on false by default
                .company(Company.builder().id(backUserUpdate.getCompanyId()).build())
                .build();
    }

    /**
     * This method is used to map back user object to filteredsearch facets object
     *
     * @param backUser from which you want to extract information to create the filteredsearch facets object
     * @return BackUserSearchFacets object with information from a back user object
     */
    public static BackUserSearchFacets mapBackUserToSearchFacets(BackUser backUser) {
        return BackUserSearchFacets.builder()
                .lastNames(new HashSet<>(Collections.singletonList(backUser.getLastName())))
                .firstNames(new HashSet<>(Collections.singletonList(backUser.getFirstName())))
                .emails(new HashSet<>(Collections.singletonList(backUser.getLogin())))
                .roles(new HashSet<>(Collections.singletonList(BackOfficeRole.valueOf(backUser.getRole()))))
                .companies(new HashSet<>(Collections.singletonList(backUser.getCompany())))
                .enabled(new HashSet<>(Collections.singletonList(backUser.getEnabled())))
                .build();
    }
}
