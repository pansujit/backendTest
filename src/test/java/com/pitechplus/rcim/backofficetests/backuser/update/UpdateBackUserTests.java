package com.pitechplus.rcim.backofficetests.backuser.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.dataproviders.BackOfficeDataProviders;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserUpdate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static com.pitechplus.rcim.backoffice.utils.mappers.BackUserMapper.mapBackUserUpdateToBackUser;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 28.02.2017.
 */
public class UpdateBackUserTests extends BackendAbstract {

    private UUID backProfileId;
    private String backUserLogin;

    @BeforeClass
    public void addBackUser() {
        BackUserCreate backUserCreate = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        ResponseEntity<BackUser> backUserResponseEntity = backUserService.createBackUser(rcimTestData.getAdminToken(),
                backUserCreate);
        backProfileId = backUserResponseEntity.getBody().getId();
        backUserLogin = backUserResponseEntity.getBody().getLogin();
    }

    @Test(description = "This test verifies valid request to update back office user is handled accordingly by server",
            dataProvider = "backOfficeRoles", dataProviderClass = BackOfficeDataProviders.class)
    @TestInfo(expectedResult = "Server responds with 200 OK and back user entity contains information from update request.")
    public void updateBackUserTest(BackOfficeRole backOfficeRole) {
        BackUserUpdate backUserUpdate = DtoBuilders.buildBackUserUpdate(backOfficeRole, rcimTestData.getAdminSuperCompanyId());
        if (backOfficeRole.equals(BackOfficeRole.ROLE_FLEET_MANAGER)) {
            backUserUpdate.setCompanyId(companyService.getCompanies(rcimTestData.getAdminToken(),
                    rcimTestData.getAdminSuperCompanyId()).get(0).getId());
        }
        ResponseEntity<BackUser> updatedBackUserResponse = backUserService.updateBackUser(rcimTestData.getAdminToken(), backProfileId, backUserUpdate);
        backUserUpdate.setCompanyId(rcimTestData.getAdminSuperCompanyId());
        assertThat("Back user entity on update response does not reflect the request made to update!!",
                updatedBackUserResponse.getBody(), samePropertyValuesAs(mapBackUserUpdateToBackUser(backUserUpdate, backUserLogin,
                        backProfileId)));
    }
}
