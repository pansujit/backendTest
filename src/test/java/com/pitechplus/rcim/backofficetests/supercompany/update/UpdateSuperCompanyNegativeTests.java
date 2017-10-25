package com.pitechplus.rcim.backofficetests.supercompany.update;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.search.companyconfig.ConfigurationQueryDto;
import com.pitechplus.rcim.backoffice.dto.search.companyconfig.SearchCompanyConfigurations;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
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
import java.util.List;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.constants.ErrorMessages.NO_COMPANY_FOUND;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dgliga on 04.07.2017.
 */
public class UpdateSuperCompanyNegativeTests extends BackendAbstract {

    private UUID superCompanyId;
    private SuperCompanyCreate updateSuperCompany;
    private Cloner cloningMachine;

    @BeforeClass(description = "This method creates a super company and prepares object used for update super company " +
            "and cloning object.")
    public void addSuperCompany() {
        SuperCompanyCreate superCompanyCreate = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        superCompanyId = companyService.createSuperCompany(rcimTestData.getAdminToken(), superCompanyCreate).getBody().getId();
        List<String> templates = configsService.getTemplates(rcimTestData.getAdminToken());
        String randomTemplate = templates.get(NumberGenerator.randInt(0, templates.size() - 1));
        SearchCompanyConfigurations searchCompanyConfigurations = companyService.searchCompanyConfigurations(rcimTestData.getAdminToken(),
                new ConfigurationQueryDto(new Page(0, 30))).getBody();
        UUID randomConfigId = searchCompanyConfigurations.getResults().get(NumberGenerator.randInt(0,
                searchCompanyConfigurations.getResults().size() - 1)).getId();
        updateSuperCompany = DtoBuilders.buildSuperCompanyCreate(randomConfigId, randomTemplate);
        cloningMachine = new Cloner();
    }

    @Test(description = "This test verifies that update super company call with invalid X-AUTH-TOKEN triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 401 Unauthorized with message: " + ErrorMessages.INVALID_AUTHENTICATION_TOKEN)
    public void updateSuperCompanyInvalidXAuthTest() throws IOException {
        try {
            companyService.updateSuperCompany("Invalid X-AUTH-TOKEN", superCompanyId, updateSuperCompany);
            Assert.fail("Super Company was updated with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN,
                            null, null));
        }
    }

    @Test(description = "This test verifies that request to update super company with all missing mandatory fields triggers " +
            "correct error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with validationErrors for: " +
            "\n- configurationId, capital, legalForm, email, fiscalNumber, name, address, phoneNumber")
    public void updateSuperCompanyWithAllMissingMandatoryFieldsTest() throws IOException {
        try {
            companyService.updateSuperCompany(rcimTestData.getSuperAdminToken(), superCompanyId, new SuperCompanyCreate());
            Assert.fail("Super Company was updated with invalid x auth!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_UPDATE, CONFIGURATION_ID_MAY_NOT_BE_NULL,
                                    CAPITAL_MAY_NOT_BE_NULL, LEGAL_FORM_MAY_NOT_BE_EMPTY, EMAIL_MAY_NOT_BE_EMPTY, FISCAL_NUMBER_MAY_NOT_BE_EMPTY,
                                    NAME_MAY_NOT_BE_EMPTY, ADDRESS_MAY_NOT_BE_NULL, PHONE_NUMBER_MAY_NOT_BE_NULL)));
        }
    }

    @Test(description = "This test verifies that request to update super company with invalid company id triggers correct error " +
            "from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad request with developerMessage: " + NO_COMPANY_FOUND + "{id}")
    public void updateSuperCompanyInvalidSuperCompanyIdTest() throws IOException {
        UUID invalidId = new UUID(NumberGenerator.randInt(1, 10), NumberGenerator.randInt(1, 20));
        try {
            companyService.updateSuperCompany(rcimTestData.getSuperAdminToken(), invalidId, updateSuperCompany);
            Assert.fail("Super Company was updated with invalid super company id!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
       
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                            NO_COMPANY_FOUND + invalidId, null));
        }
    }

    @Test(description = "This test verifies that request to update super company with invalid phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void updateSuperCompanyWithInvalidPhoneTest() throws IOException {
        SuperCompanyCreate invalidPhoneNumber = cloningMachine.deepClone(updateSuperCompany);
        invalidPhoneNumber.getPhoneNumber().setNationalNumber(PersonalInfoGenerator.generateName(9));
        try {
            companyService.updateSuperCompany(rcimTestData.getSuperAdminToken(), superCompanyId, invalidPhoneNumber);
            Assert.fail("Super Company was updated with invalid phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_UPDATE, PHONE_NUMBER_IS_INVALID)));
        }
    }

    @Test(description = "This test verifies that request to update super company with invalid secondary phone number triggers correct " +
            "error from server.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with validationErrors: invalid phoneNumber")
    public void updateSuperCompanyWithInvalidSecondaryPhoneTest() throws IOException {
        SuperCompanyCreate invalidPhoneNumber = cloningMachine.deepClone(updateSuperCompany);
        invalidPhoneNumber.getSecondaryPhoneNumber().setNationalNumber(PersonalInfoGenerator.generateName(9));
        try {
            companyService.updateSuperCompany(rcimTestData.getSuperAdminToken(), superCompanyId, invalidPhoneNumber);
            Assert.fail("Super Company was updated with invalid secondary phone number!");
        } catch (HttpStatusCodeException exception) {
            //verify that error received from server is the correct one
            assertThat("Server did not throw correct error!", ExceptionMapper.mapException(exception, BackOfficeException.class),
                    ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), null,
                            ValidationErrorsBuilder.buildValidationErrors(ServiceCalled.SUPER_COMPANY_UPDATE, SECONDARY_PHONE_NUMBER_IS_INVALID)));
        }
    }

    @Test(dataProvider = "superCompanyUpdateDuplicates", description = "This test verifies that calling update super company " +
            "service with name or fiscalNumber which is already used triggers correct error.")
    @TestInfo(expectedResult = "Server responds with 400 Bad Request with developerMessage: A company with this name/fiscalNumber " +
            "already exists")
    public void updateSuperCompanyWithDuplicateFieldTest(SuperCompanyCreate updateSuperCompany, String developerMessage) throws IOException {
        try {
            companyService.updateSuperCompany(rcimTestData.getSuperAdminToken(), superCompanyId, updateSuperCompany);
            Assert.fail("Super company updated with duplicate item!");
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
    private Object[][] superCompanyUpdateDuplicates() {
        //create object with all fields
        companyService.createSuperCompany(rcimTestData.getAdminToken(), updateSuperCompany);
        SuperCompanyCreate sameName = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        sameName.setName(updateSuperCompany.getName());
        SuperCompanyCreate sameFiscalNumber = DtoBuilders.buildSuperCompanyCreate(rcimTestData.getConfigurationId(),
                rcimTestData.getGroupTemplate());
        sameFiscalNumber.setFiscalNumber(updateSuperCompany.getFiscalNumber());

        return new Object[][]{
                {sameName, "A company with this name already exists"},
                {sameFiscalNumber, "A company with this fiscal number already exists"}
        };
    }
}
