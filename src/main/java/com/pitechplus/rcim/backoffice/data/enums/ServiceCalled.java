package com.pitechplus.rcim.backoffice.data.enums;

/**
 * Created by dgliga on 28.02.2017.
 */
public enum ServiceCalled {

    BACK_USER_CREATE_DTO("backUserCreateDto"), BACK_USER_UPDATE_DTO("backUserUpdateDto"), BACK_USER_SEARCH("backUserSearchRequestDto"),
    SUPER_COMPANY_CREATE("companyCreateDto"), SUPER_COMPANY_UPDATE("companyUpdateDto"), SUPER_COMPANY_SEARCH("companyQueryDto"),
    SUB_COMPANY_CREATE("subCompanyEditDto"), SUB_COMPANY_UPDATE("subCompanyEditDto"), CONTRACT_CREATE("contractCreateDto"),
    PARKING_CREATE("parkingCreateDto"), PARKING_UPDATE("parkingUpdateDto"),
    MEMBER_CREATE("memberCreateDto"), MEMBER_UPDATE("memberUpdateDto"), SEARCH_BOOKING("searchBookingDto"),
    BOOKING_CREATE("bookingCreateDto"), SEARCH_GROUPS("groupQueryDto"), DAMAGE_CREATE("damageReportCreateDto"),
    SMART_CARD_CREATE_DTO("smartcardCreateDto");

    private String value;

    ServiceCalled(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
