package com.pitechplus.rcim.backoffice.utils.mappers;

import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;

/**
 * Created by dgliga on 31.05.2017.
 */
public class CompanyMapper {

    /**
     * This method maps a SuperCompanyDto object to SuperCompanyCreate object in order to check that the added super company
     * contains the correct information ( the one sent on request )
     *
     * @param superCompanyDto which comes on the response to add a super company
     * @return details of super company created in application mapped to the object type sent on request
     */
    public static SuperCompanyCreate mapSuperCompanyDtoToCreate(SuperCompanyDto superCompanyDto) {
        return SuperCompanyCreate.builder()
                .name(superCompanyDto.getName())
                .address(superCompanyDto.getAddress())
                .backofficeResetPasswordUrl(superCompanyDto.getBackofficeResetPasswordUrl())
                .capital(superCompanyDto.getCapital())
                .configurationId(superCompanyDto.getConfigurationId())
                .durationAfterTripToAllowLockUnlock(superCompanyDto.getDurationAfterTripToAllowLockUnlock())
                .email(superCompanyDto.getEmail())
                .employerCertificateUrl(superCompanyDto.getEmployerCertificateUrl())
                .employerCertificateRequired(superCompanyDto.getEmployerCertificateRequired())
                .endBookingDamageReportMandatory(superCompanyDto.getEndBookingDamageReportMandatory())
                .fiscalNumber(superCompanyDto.getFiscalNumber())
                .identityDocumentRequired(superCompanyDto.getIdentityDocumentRequired())
                .invoiceDelegate(superCompanyDto.getInvoiceDelegate())
                .invoiceLabel(superCompanyDto.getInvoiceLabel())
                .legalForm(superCompanyDto.getLegalForm())
                .phoneNumber(superCompanyDto.getPhoneNumber())
                .secondaryPhoneNumber(superCompanyDto.getSecondaryPhoneNumber())
                .secondaryEmail(superCompanyDto.getSecondaryEmail())
                .startBookingDamageReportMandatory(superCompanyDto.getStartBookingDamageReportMandatory())
                .templateGroup(superCompanyDto.getTemplateGroup())
                .termsOfSubscriptionUrl(superCompanyDto.getTermsOfSubscriptionUrl())
                .termsOfUseUrl(superCompanyDto.getTermsOfUseUrl())
                .useExternalInvoiceSystem(superCompanyDto.getUseExternalInvoiceSystem())
                .websiteConfirmSubscriptionUrl(superCompanyDto.getWebsiteConfirmSubscriptionUrl())
                .websiteResetPasswordUrl(superCompanyDto.getWebsiteResetPasswordUrl()).build();
    }
}
