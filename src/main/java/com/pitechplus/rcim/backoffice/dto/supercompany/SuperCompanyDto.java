package com.pitechplus.rcim.backoffice.dto.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

/**
 * Created by dgliga on 08.05.2017.
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuperCompanyDto {

    UUID id;
    String name;
    String email;
    String secondaryEmail;
    PhoneNumber phoneNumber;
    PhoneNumber secondaryPhoneNumber;
    String fiscalNumber;
    String logoUrl;
    Address address;
    UUID templateId;
    String termsOfUseUrl;
    String termsOfSubscriptionUrl;
    ContractDto contract;
    Long durationAfterTripToAllowLockUnlock;
    String invoiceLabel;
    String legalForm;
    Integer capital;
    Boolean invoiceDelegate;
    String templateGroup;
    String websiteResetPasswordUrl;
    String websiteConfirmSubscriptionUrl;
    String backofficeResetPasswordUrl;
    List<ApplicationType> authorizedApplications;
    String employerCertificateUrl;
    UUID configurationId;
    ComputedConfigurationDto computedConfiguration;
    Boolean identityDocumentRequired;
    Boolean employerCertificateRequired;
    Boolean useExternalInvoiceSystem;
    Boolean startBookingDamageReportMandatory;
    Boolean endBookingDamageReportMandatory;

}
