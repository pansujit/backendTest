package com.pitechplus.rcim.backoffice.data.enums;

import com.pitechplus.rcim.backoffice.constants.ErrorMessages;

/**
 * Created by dgliga on 28.02.2017.
 */
public enum ValidationError {
    LOGIN_MAY_NOT_BE_EMPTY("login in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    FIRST_NAME_MAY_NOT_BE_EMPTY("firstName in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    LAST_NAME_MAY_NOT_BE_EMPTY("lastName in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    ADDRESS_MAY_NOT_BE_NULL("address in ", ErrorMessages.MAY_NOT_BE_NULL),
    LOCALE_MAY_NOT_BE_NULL("locale in ", ErrorMessages.MAY_NOT_BE_NULL),
    PHONE_NUMBER_MAY_NOT_BE_NULL("phoneNumber in ", ErrorMessages.MAY_NOT_BE_NULL),
    FORMATTED_ADDRESS_MAY_NOT_BE_EMPTY("address.formattedAddress in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    PHONE_NUMBER_IS_INVALID("phoneNumber in ", " The phone number is not valid"),
    SECONDARY_PHONE_NUMBER_IS_INVALID("secondaryPhoneNumber in ", " The phone number is not valid"),
    NATIONAL_NUMBER_MAY_NOT_BE_EMPTY("phoneNumber.nationalNumber in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    COUNTRY_CODE_MAY_NOT_BE_EMPTY("phoneNumber.countryCode in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    PAGE_MAY_NOT_BE_NULL("page in ", ErrorMessages.MAY_NOT_BE_NULL),
    PAGE_NUMBER_INVALID("page.number in ", ErrorMessages.GREATER_OR_EQUAL_ZERO),
    PAGE_SIZE_INVALID("page.size in ", ErrorMessages.GREATER_OR_EQUAL_ZERO),
    CONFIGURATION_ID_MAY_NOT_BE_NULL("configurationId in ", ErrorMessages.MAY_NOT_BE_NULL),
    CAPITAL_MAY_NOT_BE_NULL("capital in ", ErrorMessages.MAY_NOT_BE_NULL),
    LEGAL_FORM_MAY_NOT_BE_EMPTY("legalForm in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    EMAIL_MAY_NOT_BE_EMPTY("email in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    EMAIL_WRONG_FORMAT("login in ", ErrorMessages.NOT_WELL_FORMED),
    FISCAL_NUMBER_MAY_NOT_BE_EMPTY("fiscalNumber in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    NAME_MAY_NOT_BE_EMPTY("name in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    CONTRACT_START_MAY_NOT_BE_NULL("contractStart in ", ErrorMessages.MAY_NOT_BE_NULL),
    COORDINATES_MAY_NOT_BE_NULL("coordinates in ", ErrorMessages.MAY_NOT_BE_NULL),
    COMPANYID_MAY_NOT_BE_NULL("companyId in ", ErrorMessages.MAY_NOT_BE_NULL),
    PASSWORD_IS_NOT_PROPERLY_FORMED("password in ", ErrorMessages.WRONG_PASSWORD),
    MEMBER_LOGIN_MAY_NOT_BE_EMPTY("memberLogin in ", ErrorMessages.MAY_NOT_BE_EMPTY),
    TYPE_MAY_NOT_BE_NULL("type in ", ErrorMessages.MAY_NOT_BE_NULL),
    START_MAY_NOT_BE_NULL("start in ", ErrorMessages.MAY_NOT_BE_NULL),
    RESERVED_SEATS_MAY_NOT_BE_NULL("reservedSeats in ", ErrorMessages.MAY_NOT_BE_NULL),
    EXTERNAL_CLEANLINESS_MAY_NOT_BE_NULL("externalCleanliness in ", ErrorMessages.MAY_NOT_BE_NULL),
    INTERNAL_CLEANLINESS_MAY_NOT_BE_NULL("internalCleanliness in ", ErrorMessages.MAY_NOT_BE_NULL);

    private final String field;
    private final String errorMessage;

    ValidationError(String field, String errorType) {
        this.field = field;
        this.errorMessage = errorType;
    }

    public String getField() {
        return this.field;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
