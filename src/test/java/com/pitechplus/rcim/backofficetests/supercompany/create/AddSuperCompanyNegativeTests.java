package com.pitechplus.rcim.backofficetests.supercompany.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.rits.cloning.Cloner;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 09.06.2017.
 */
public class AddSuperCompanyNegativeTests extends BackendAbstract {

    @Test(description = "This test verifies that create super company call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void invalidTokenTest() throws IOException {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        try {
            companyService.createSuperCompany(rcimTestData.getSuperAdminToken() + PersonalInfoGenerator.generateOrcName(),
                    superCompanyCreate);
            Assert.fail("Super company created with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to create super company with all missing mandatory fields triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- configurationId, capital, legalForm, email, fiscalNumber, name, address, phoneNumber")
    public void missingMandatoryFieldsTest() throws IOException {
        try {
            companyService.createSuperCompany(rcimTestData.getAdminToken(), new SuperCompanyCreate());
            Assert.fail("Super company created with missing mandatory fields!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE, CONFIGURATION_ID_MAY_NOT_BE_NULL,
                                    CAPITAL_MAY_NOT_BE_NULL, LEGAL_FORM_MAY_NOT_BE_EMPTY, EMAIL_MAY_NOT_BE_EMPTY, FISCAL_NUMBER_MAY_NOT_BE_EMPTY,
                                    NAME_MAY_NOT_BE_EMPTY, ADDRESS_MAY_NOT_BE_NULL, PHONE_NUMBER_MAY_NOT_BE_NULL)));
        }
    }

    @Test(dataProvider = "superCompanyCreateMissingMandatory", description = "This test verifies that request to create " +
            "super company with one missing mandatory field triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors: depending on which field is missing " +
            "the correct error/errors is/are checked.")
    public void oneMissingMandatoryFieldTest(SuperCompanyCreate superCompanyCreate, Set<String> validationErrors) throws IOException {
        try {
            companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate);
            Assert.fail("Super company created with missing mandatory field!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            null, validationErrors));
        }
    }

    @Test(description = "This test verifies that request to create super company with invalid phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void invalidPhoneNumberTest() throws IOException {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        superCompanyCreate.getPhoneNumber().setNationalNumber(RandomStringUtils.random(5));
        try {
            companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate);
            Assert.fail("Super company created with invalid phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE, PHONE_NUMBER_IS_INVALID)));
        }
    }

    @Test(dataProvider = "superCompanyCreateDuplicates", description = "This test verifies that calling create super company " +
            "service with name or fiscalNumber which is already used triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: A company with this name/fiscalNumber " +
            "already exists")
    public void duplicateFieldCreateTest(SuperCompanyCreate superCompanyCreate, String developerMessage) throws IOException {
        try {
            companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate);
            Assert.fail("Super company created with invalid phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            developerMessage, null));
        }
    }

    /**
     * This data provider creates SuperCompanyCreate objects, each with one missing mandatory field
     *
     * @return SuperCompanyCreate objects and set of validation errors
     */
    @DataProvider
    private Object[][] superCompanyCreateMissingMandatory() {
        //create object with all fields
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        Cloner cloningMachine = new Cloner();
        SuperCompanyCreate noConfigurationId = cloningMachine.deepClone(superCompanyCreate);
        noConfigurationId.setConfigurationId(null);
        SuperCompanyCreate noCapital = cloningMachine.deepClone(superCompanyCreate);
        noCapital.setCapital(null);
        SuperCompanyCreate noLegalForm = cloningMachine.deepClone(superCompanyCreate);
        noLegalForm.setLegalForm(null);
        SuperCompanyCreate noEmail = cloningMachine.deepClone(superCompanyCreate);
        noEmail.setEmail(null);
        SuperCompanyCreate noFiscalNumber = cloningMachine.deepClone(superCompanyCreate);
        noFiscalNumber.setFiscalNumber(null);
        SuperCompanyCreate noName = cloningMachine.deepClone(superCompanyCreate);
        noName.setName(null);
        SuperCompanyCreate noAddress = cloningMachine.deepClone(superCompanyCreate);
        noAddress.setAddress(null);
        SuperCompanyCreate noPhoneNumber = cloningMachine.deepClone(superCompanyCreate);
        noPhoneNumber.setPhoneNumber(null);
        return new Object[][]{
                {noConfigurationId, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        CONFIGURATION_ID_MAY_NOT_BE_NULL)},
                {noCapital, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        CAPITAL_MAY_NOT_BE_NULL)},
                {noLegalForm, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        LEGAL_FORM_MAY_NOT_BE_EMPTY)},
                {noEmail, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        EMAIL_MAY_NOT_BE_EMPTY)},
                {noFiscalNumber, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        FISCAL_NUMBER_MAY_NOT_BE_EMPTY)},
                {noName, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        NAME_MAY_NOT_BE_EMPTY)},
                {noAddress, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        ADDRESS_MAY_NOT_BE_NULL)},
                {noPhoneNumber, ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_CREATE,
                        PHONE_NUMBER_MAY_NOT_BE_NULL)},
        };
    }

    /**
     * This data provider creates SuperCompanyCreate objects with duplicate fields for which validation is made
     * - name and fiscalNumber
     *
     * @return SuperCompanyCreate objects and developer message
     */
    @DataProvider
    private Object[][] superCompanyCreateDuplicates() {
        //create object with all fields
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate);
        SuperCompanyCreate sameName = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        sameName.setName(superCompanyCreate.getName());
        SuperCompanyCreate sameFiscalNumber = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        sameFiscalNumber.setFiscalNumber(superCompanyCreate.getFiscalNumber());

        return new Object[][]{
                {sameName, "A company with this name already exists"},
                {sameFiscalNumber, "A company with this fiscal number already exists"}
        };
    }


}
