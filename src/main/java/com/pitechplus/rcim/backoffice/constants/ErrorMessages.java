package com.pitechplus.rcim.backoffice.constants;

/**
 * Created by dgliga on 21.02.2017.
 */
public class ErrorMessages {

    public static final String INVALID_AUTHENTICATION_TOKEN = "Invalid authentication token";
    public static final String NO_BACK_USER_FOUND = "No back profile found for id ";
    public static final String NO_COMPANY_FOUND = "No company found for id : ";
    public static final String NO_SUB_COMPANY_FOUND = "No sub-company found for id : ";
    public static final String MAY_NOT_BE_EMPTY = " may not be empty";
    public static final String MAY_NOT_BE_NULL = " may not be null";
    public static final String GREATER_OR_EQUAL_ZERO = " must be greater than or equal to 0";
    public static final String CONTRACTS_OVERLAP = "The dates for the contract are not valid or overlap an existing one";
    public static final String CONTRACT_SAME_NAME = "A contract with this name already exists";
    public static final String WRONG_PASSWORD = " is not properly formed";
    public static final String NOT_WELL_FORMED = " not a well-formed email address";
    public static final String TWO_GROUP_OWNERS = "A group cannot have more than one owner";
    public static final String MEMBER_ADDED_MULTIPLE_TIMES ="A group cannot contain the same member multiple times";
    public static final String EXISTING_PUBLICID= "Group public id already exists:";
    public static final String GROUP_LIMIT_REACHED= "Group membership limit reached";

    //todo: add message error if it will be implemented
    public static final String COUNTRY_IS_INVALID = "";
    public static final String ROLE_IS_INVALID = "";
    public static final String LOCALE_IS_INVALID = "";
}
