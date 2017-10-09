package com.pitechplus.rcim.backofficetests.backuser.retrieve;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.backoffice.data.enums.BackOfficeRole;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.BackendAbstract;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.custommatchers.PropertyValuesMatcher.samePropertyValuesAs;
import static com.pitechplus.rcim.backoffice.utils.mappers.BackUserMapper.mapBackUserCreateToBackUser;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 23.02.2017.
 */
public class GetBackUserTests extends BackendAbstract {

    private UUID backProfileId;
    private BackUserCreate backUserCreate;

    @BeforeMethod
    public void addBackUser() {
        backUserCreate = DtoBuilders.buildBackUserCreate(BackOfficeRole.ROLE_CALL_CENTER_OPERATOR,
                rcimTestData.getAdminSuperCompanyId());
        ResponseEntity<BackUser> backUserResponseEntity = backUserService.createBackUser(rcimTestData.getAdminToken(),
                backUserCreate);
        backProfileId = backUserResponseEntity.getBody().getId();
        backUserCreate.setId(backUserResponseEntity.getBody().getId());

    }

    @Test(description = "This test verifies that valid call to get single back user works accordingly.")
    @TestInfo(expectedResult = "Back user retrieved from server has all the correct information from creation.")
    public void getSingleBackUserTest() {
        ResponseEntity<BackUser> getBackUserResponse = backUserService.getSingleBackUser(rcimTestData.getAdminToken(),
                backProfileId);
        assertThat("Information given on response to get back office user is not the same as the one stored in database!",
                getBackUserResponse.getBody(), samePropertyValuesAs(mapBackUserCreateToBackUser(backUserCreate)));
    }
}
