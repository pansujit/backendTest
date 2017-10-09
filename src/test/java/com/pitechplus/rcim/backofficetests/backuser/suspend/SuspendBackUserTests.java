package com.pitechplus.rcim.backofficetests.backuser.suspend;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by dgliga on 02.03.2017.
 */
public class SuspendBackUserTests extends BackendAbstract {

    private UUID backProfileId;

    @BeforeMethod
    public void addBackUser() {
        BackUserCreate backUserCreate = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        ResponseEntity<BackUser> backUserResponseEntity = backUserService.createBackUser(rcimTestData.getAdminToken(),
                backUserCreate);
        backProfileId = backUserResponseEntity.getBody().getId();
    }

    @Test(description = "This test verifies that calling service suspend back user works accordingly.")
    @TestInfo(expectedResult = "Service responds with back user entity which has suspended field value on true.")
    public void suspendBackUserTest() {
        ResponseEntity<BackUser> suspendResponse = backUserService.suspendOrAllowBackUser(rcimTestData.getAdminToken(),
                backProfileId, true);
        assertThat("Back user was not marked as suspended!", true, is(suspendResponse.getBody().getSuspended()));

    }

    @Test(description = "This test verifies that calling service suspend back user to unsuspend a back user works accordingly.")
    @TestInfo(expectedResult = "Service responds with back user entity which has suspended field value on false.")
    public void unSuspendBackUserTest() {
        backUserService.suspendOrAllowBackUser(rcimTestData.getAdminToken(), backProfileId, true);
        ResponseEntity<BackUser> unSuspendResponse = backUserService.suspendOrAllowBackUser(rcimTestData.getAdminToken(),
                backProfileId, false);
        assertThat("Back user was marked as suspended!", false, is(unSuspendResponse.getBody().getSuspended()));
    }
}
