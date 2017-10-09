package com.pitechplus.rcim.backofficetests.backuser.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.backoffice.data.dataproviders.BackOfficeDataProviders;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.BackendAbstract;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static com.pitechplus.rcim.backoffice.utils.mappers.BackUserMapper.mapBackUserCreateToBackUser;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 14.02.2017.
 */
public class AddBackUserTests extends BackendAbstract {

    @Test(description = "This test verifies valid request to add back office user is handled accordingly by server",
            dataProvider = "backOfficeRoles", dataProviderClass = BackOfficeDataProviders.class)
    @TestInfo(expectedResult = "Server responds with 200 OK and and Body contains information which was sent on back user create " +
            "request body.")
    public void addBackOfficeUserTest(BackOfficeRole backOfficeRole) {
        BackUserCreate backUserCreate = DtoBuilders.buildBackUserCreate(backOfficeRole, rcimTestData.getAdminSuperCompanyId());
        if (backOfficeRole.equals(BackOfficeRole.ROLE_FLEET_MANAGER)) {
            backUserCreate.setCompanyId(companyService.getCompanies(rcimTestData.getAdminToken(),
                    rcimTestData.getAdminSuperCompanyId()).get(0).getId());
        }
        ResponseEntity<BackUser> addBackUserResponse = backUserService.createBackUser(rcimTestData.getAdminToken(), backUserCreate);
        backUserCreate.setId(addBackUserResponse.getBody().getId());
        backUserCreate.setCompanyId(rcimTestData.getAdminSuperCompanyId());
        assertThat("Information given on response to add back office user does not correspond to request to add back user!",
                addBackUserResponse.getBody(), samePropertyValuesAs(mapBackUserCreateToBackUser(backUserCreate)));
    }
}
