package com.pitechplus.rcim.backoffice.data.dataproviders;

import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import org.testng.annotations.DataProvider;

/**
 * Created by dgliga on 28.02.2017.
 */
public class BackOfficeDataProviders {

    @DataProvider
    public static Object[][] backOfficeRoles() {
        return new Object[][]{
                {BackOfficeRole.ROLE_ADMIN},
                {BackOfficeRole.ROLE_CALL_CENTER_OPERATOR},
                {BackOfficeRole.ROLE_SUPER_ADMIN},
                {BackOfficeRole.ROLE_FLEET_MANAGER},
        };
    }

    @DataProvider
    public static Object[][] booleanProvider() {
        return new Object[][]{
                {true},
                {false},
        };
    }
}
