package com.pitechplus.rcim.backofficetests.supercompany.contract;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.supercompany.ContractDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.CONTRACTS_OVERLAP;
import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.CONTRACT_SAME_NAME;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.CONTRACT_START_MAY_NOT_BE_NULL;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.NAME_MAY_NOT_BE_EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 05.07.2017.
 */
public class ContractCreateNegativeTests extends BackendAbstract {
    private UUID superCompanyId;

    @BeforeClass(description = "This method creates a super company which we need in order to create contracts.")
    public void addSuperCompany() {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        superCompanyId = companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate).getBody().getId();
    }

    @Test(description = "This test verifies that create contract for company call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidXAuthTest() throws IOException {
        ContractDto contractDto = DtoBuilders.buildContractDto(0, 1);
        try {
            companyService.createContract(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(),
                    superCompanyId, contractDto);
            Assert.fail("Contract was created with invalid X-AUTH-TOKEN");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that create contract for company call with invalid company id triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 404 Not Found with developerMessage: No company found with id :{id}")
    public void invalidCompanyIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        ContractDto contractDto = DtoBuilders.buildContractDto(0, 1);
        try {
            companyService.createContract(rcimTestData.getSuperAdminToken(), invalidId, contractDto);
            Assert.fail("Contract was created for invalid company id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No company found with id : " + invalidId, null));
        }
    }

    @Test(description = "This test verifies that request to create contract for company with all missing mandatory fields triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- name and contractStartDate")
    public void missingAllMandatoryFieldsTest() throws IOException {
        try {
            companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, new ContractDto());
            Assert.fail("Contract was created with all missing mandatory fields");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.CONTRACT_CREATE, NAME_MAY_NOT_BE_EMPTY,
                                    CONTRACT_START_MAY_NOT_BE_NULL)));
        }
    }

    @Test(description = "This test verifies that request to create contract for company in same period as previously created " +
            "contract triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: " + CONTRACTS_OVERLAP)
    public void createContractSamePeriodsTest() throws IOException {
        ContractDto firstContract = DtoBuilders.buildContractDto(0, 1);
        ContractDto secondContract = DtoBuilders.buildContractDto(0, 1);
        companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, firstContract);
        try {
            companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, secondContract);
            Assert.fail("Contract was created for same periods!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            CONTRACTS_OVERLAP, null));
        }
    }

    @Test(description = "This test verifies that request to create contract for company period  which overlaps with period " +
            "of previously created contract triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: " + CONTRACTS_OVERLAP)
    public void createContractOverlapPeriodsTest() throws IOException {
        //create contract objects which overlap in time period
        ContractDto firstContract = DtoBuilders.buildContractDto(2, 4);
        ContractDto secondContract = DtoBuilders.buildContractDto(3, 5);

        companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, firstContract);
        try {
            companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, secondContract);
            Assert.fail("Contract was created for periods which overlap!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            CONTRACTS_OVERLAP, null));
        }
    }

    @Test(description = "This test verifies that request to create contract for company with same name as previously created " +
            "contract triggers correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: " + CONTRACTS_OVERLAP)
    public void sameNameTest() throws IOException {
        ContractDto firstContract = DtoBuilders.buildContractDto(6, 7);
        ContractDto secondContract = DtoBuilders.buildContractDto(8, 9);
        secondContract.setName(firstContract.getName());
        companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, firstContract);
        try {
            companyService.createContract(rcimTestData.getSuperAdminToken(), superCompanyId, secondContract);
            Assert.fail("Contract was created for periods which overlap!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            CONTRACT_SAME_NAME, null));
        }
    }
}
