package com.pitechplus.rcim.backofficetests.company.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.company.CompanyCreateDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.rits.cloning.Cloner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.NO_SUB_COMPANY_FOUND;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 04.07.2017.
 */
public class UpdateCompanyNegativeTests extends BackendAbstract {

    private UUID companyId;
    private CompanyCreateDto updateCompany;
    private Cloner cloningMachine;

    @BeforeClass(description = "This method creates a company and prepares object used for update company and cloning object.")
    public void createCompany() {
        companyId = companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(),
                DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId())).getBody().getId();
        updateCompany = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        cloningMachine = new Cloner();
    }

    @Test(description = "This test verifies that update company call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void updateCompanyInvalidXAuthTest() throws IOException {
        try {
            companyService.updateCompany("Invalid X-AUTH-TOKEN", companyId, updateCompany);
            Assert.fail("Company was updated with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to update company with all missing mandatory fields triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- capital, legalForm, email, fiscalNumber, name, address, phoneNumber")
    public void updateCompanyWithAllMissingMandatoryFieldsTest() throws IOException {
        try {
            companyService.updateCompany(rcimTestData.getSuperAdminToken(), companyId, new CompanyCreateDto());
            Assert.fail("Company was updated with ALL missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_UPDATE,
                                    CAPITAL_MAY_NOT_BE_NULL, LEGAL_FORM_MAY_NOT_BE_EMPTY, EMAIL_MAY_NOT_BE_EMPTY, FISCAL_NUMBER_MAY_NOT_BE_EMPTY,
                                    NAME_MAY_NOT_BE_EMPTY, ADDRESS_MAY_NOT_BE_NULL, PHONE_NUMBER_MAY_NOT_BE_NULL)));
        }
    }

    @Test(description = "This test verifies that request to update company with invalid company id triggers correct error " +
            "from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: " + NO_SUB_COMPANY_FOUND + "{id}")
    public void updateCompanyInvalidCompanyIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            companyService.updateCompany(rcimTestData.getSuperAdminToken(), invalidId, updateCompany);
            Assert.fail("Company was updated with invalid company id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            NO_SUB_COMPANY_FOUND + invalidId, null));
        }
    }

    @Test(description = "This test verifies that request to update company with invalid phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void updateCompanyWithInvalidPhoneTest() throws IOException {
        CompanyCreateDto invalidPhoneNumber = cloningMachine.deepClone(updateCompany);
        invalidPhoneNumber.getPhoneNumber().setNationalNumber(PersonalInfoGenerator.generateName(9));
        try {
            companyService.updateCompany(rcimTestData.getSuperAdminToken(), companyId, invalidPhoneNumber);
            Assert.fail("Company was updated with invalid phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_UPDATE, PHONE_NUMBER_IS_INVALID)));
        }
    }

    @Test(description = "This test verifies that request to update company with invalid secondary phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void updateCompanyWithInvalidSecondaryPhoneTest() throws IOException {
        CompanyCreateDto invalidPhoneNumber = cloningMachine.deepClone(updateCompany);
        invalidPhoneNumber.getSecondaryPhoneNumber().setNationalNumber(PersonalInfoGenerator.generateName(9));
        try {
            companyService.updateCompany(rcimTestData.getSuperAdminToken(), companyId, invalidPhoneNumber);
            Assert.fail("Company was updated with invalid secondary phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_UPDATE, SECONDARY_PHONE_NUMBER_IS_INVALID)));
        }
    }

    @Test(dataProvider = "duplicateFieldsCompany", description = "This test verifies that calling update company " +
            "service with name, fiscalNumber or agencyCode which is already used triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: A company with this name/fiscalNumber " +
            "already exists")
    public void duplicateFieldTest(CompanyCreateDto companyCreateDto, String developerMessage) throws IOException {
        try {
            companyService.updateCompany(rcimTestData.getSuperAdminToken(), companyId, companyCreateDto);
            Assert.fail("Company updated with duplicate item!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            developerMessage, null));
        }
    }

    /**
     * This data provider creates CompanyCreateDto objects with duplicate fields for which validation is made
     * - name, fiscalNumber and agencyCode
     *
     * @return CompanyCreateDto objects and developer message
     */
    @DataProvider
    private Object[][] duplicateFieldsCompany() {
        //create object with all fields
        companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(), updateCompany);
        CompanyCreateDto sameName = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        sameName.setName(updateCompany.getName());
        CompanyCreateDto sameFiscalNumber = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        sameFiscalNumber.setFiscalNumber(updateCompany.getFiscalNumber());
        CompanyCreateDto sameAgencyCode = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        sameAgencyCode.setAgencyCode(updateCompany.getAgencyCode());
        return new Object[][]{
                {sameName, "A company with this name already exists"},
                {sameFiscalNumber, "A company with this fiscal number already exists"},
                {sameAgencyCode, "A company with this agency code already exists"},
        };
    }


}
