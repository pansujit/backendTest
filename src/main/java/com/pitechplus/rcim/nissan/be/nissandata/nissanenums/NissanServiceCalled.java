package com.pitechplus.rcim.nissan.be.nissandata.nissanenums;

/**
 * Created by dgliga  on 27.07.2017.
 */

public enum NissanServiceCalled {

    BACK_USER_CREATE_DTO("backUserCreateDto"), BACK_USER_UPDATE_DTO("backUserUpdateDto"), BACK_USER_SEARCH("backUserSearchRequestDto"),
    SUPER_COMPANY_CREATE("companyCreateDto"), SUPER_COMPANY_UPDATE("companyUpdateDto"), SUPER_COMPANY_SEARCH("companyQueryDto"),
    SUB_COMPANY_CREATE("subCompanyEditDto"), SUB_COMPANY_UPDATE("subCompanyEditDto"), CONTRACT_CREATE("contractCreateDto"),
    PARKING_CREATE("parkingCreateDto"), PARKING_UPDATE("parkingUpdateDto");

    private String value;

    NissanServiceCalled(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
