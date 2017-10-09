package com.pitechplus.rcim.backofficetests.company.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.company.CompanyCreateDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.BackendAbstract;
import com.rits.cloning.Cloner;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashSet;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 03.07.2017.
 */
public class AddCompanyNegativeTests extends BackendAbstract {

    @Test(description = "This test verifies that create company call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        CompanyCreateDto companyCreateDto = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        try {
            companyService.createCompany(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(),
                    rcimTestData.getSuperCompanyDto().getId(), companyCreateDto);
            Assert.fail("Company created with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to create company with all missing mandatory fields triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- fiscalNumber, phoneNumber, address, legalForm, name, email, capital")
    public void missingMandatoryFieldsTest() throws IOException {
        try {
            companyService.createCompany(rcimTestData.getAdminToken(), rcimTestData.getSuperCompanyDto().getId(), new CompanyCreateDto());
            Assert.fail("Company created with missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE, FISCAL_NUMBER_MAY_NOT_BE_EMPTY,
                                    PHONE_NUMBER_MAY_NOT_BE_NULL, ADDRESS_MAY_NOT_BE_NULL, LEGAL_FORM_MAY_NOT_BE_EMPTY, NAME_MAY_NOT_BE_EMPTY,
                                    EMAIL_MAY_NOT_BE_EMPTY, CAPITAL_MAY_NOT_BE_NULL)));
        }
    }

    @Test(dataProvider = "companyCreateMissingMandatory", description = "This test verifies that request to create " +
            "company with one missing mandatory field triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors: depending on which field is missing " +
            "the correct error/errors is/are checked.")
    public void oneMissingMandatoryFieldTest(CompanyCreateDto companyCreateDto, HashSet<String> validationErrors) throws IOException {
        try {
            companyService.createCompany(rcimTestData.getAdminToken(), rcimTestData.getSuperCompanyDto().getId(), companyCreateDto);
            Assert.fail("Company created with missing mandatory field!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, validationErrors));
        }
    }

    @Test(description = "This test verifies that request to create company with invalid phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void invalidPhoneNumberTest() throws IOException {
        CompanyCreateDto companyCreateDto = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        companyCreateDto.getPhoneNumber().setNationalNumber(RandomStringUtils.random(5));
        try {
            companyService.createCompany(rcimTestData.getAdminToken(), rcimTestData.getSuperCompanyDto().getId(), companyCreateDto);
            Assert.fail("Company created with invalid phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE, PHONE_NUMBER_IS_INVALID)));
        }
    }


    @Test(dataProvider = "duplicateFieldsCompany", description = "This test verifies that calling create company " +
            "service with name, fiscalNumber or agencyCode which is already used triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: A company with this name/fiscalNumber " +
            "already exists")
    public void duplicateFieldTest(CompanyCreateDto companyCreateDto, String developerMessage) throws IOException {
        try {
            companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(), companyCreateDto);
            Assert.fail("Company created with duplicate item!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            developerMessage, null));
        }
    }

    /**
     * This data provider creates CompanyCreateDto objects, each with one missing mandatory field
     *
     * @return CompanyCreateDto objects and set of validation errors
     */
    @DataProvider
    private Object[][] companyCreateMissingMandatory() {
        //create object with all fields
        CompanyCreateDto companyCreateDto = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        Cloner cloningMachine = new Cloner();
        CompanyCreateDto noFiscalNumber = cloningMachine.deepClone(companyCreateDto);
        noFiscalNumber.setFiscalNumber(null);
        CompanyCreateDto noPhoneNumber = cloningMachine.deepClone(companyCreateDto);
        noPhoneNumber.setPhoneNumber(null);
        CompanyCreateDto noAddress = cloningMachine.deepClone(companyCreateDto);
        noAddress.setAddress(null);
        CompanyCreateDto noLegalForm = cloningMachine.deepClone(companyCreateDto);
        noLegalForm.setLegalForm(null);
        CompanyCreateDto noName = cloningMachine.deepClone(companyCreateDto);
        noName.setName(null);
        CompanyCreateDto noEmail = cloningMachine.deepClone(companyCreateDto);
        noEmail.setEmail(null);
        CompanyCreateDto noCapital = cloningMachine.deepClone(companyCreateDto);
        noCapital.setCapital(null);

        return new Object[][]{
                {noFiscalNumber, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE,
                        FISCAL_NUMBER_MAY_NOT_BE_EMPTY)},
                {noPhoneNumber, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE,
                        PHONE_NUMBER_MAY_NOT_BE_NULL)},
                {noAddress, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE,
                        ADDRESS_MAY_NOT_BE_NULL)},
                {noLegalForm, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE,
                        LEGAL_FORM_MAY_NOT_BE_EMPTY)},
                {noEmail, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE,
                        EMAIL_MAY_NOT_BE_EMPTY)},
                {noName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE,
                        NAME_MAY_NOT_BE_EMPTY)},
                {noCapital, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUB_COMPANY_CREATE,
                        CAPITAL_MAY_NOT_BE_NULL)},
        };
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
        CompanyCreateDto validCompanyCreateDto = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        companyService.createCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getSuperCompanyDto().getId(), validCompanyCreateDto);
        CompanyCreateDto sameName = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        sameName.setName(validCompanyCreateDto.getName());
        CompanyCreateDto sameFiscalNumber = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        sameFiscalNumber.setFiscalNumber(validCompanyCreateDto.getFiscalNumber());
        CompanyCreateDto sameAgencyCode = DtoBuilders.buildCompanyCreate(rcimTestData.getConfigurationId());
        sameAgencyCode.setAgencyCode(validCompanyCreateDto.getAgencyCode());
        return new Object[][]{
                {sameName, "A company with this name already exists"},
                {sameFiscalNumber, "A company with this fiscal number already exists"},
                {sameAgencyCode, "A company with this agency code already exists"},
        };
    }
}
