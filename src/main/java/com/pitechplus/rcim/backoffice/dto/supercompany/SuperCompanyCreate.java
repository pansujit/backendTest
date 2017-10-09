package com.pitechplus.rcim.backoffice.dto.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class SuperCompanyCreate {

    String name;
    Address address;
    String backofficeResetPasswordUrl;
    Integer capital;
    UUID configurationId;
    Long durationAfterTripToAllowLockUnlock;
    String email;
    Boolean employerCertificateRequired;
    String employerCertificateUrl;
    Boolean endBookingDamageReportMandatory;
    String fiscalNumber;
    Boolean identityDocumentRequired;
    Boolean invoiceDelegate;
    String invoiceLabel;
    String legalForm;
    PhoneNumber phoneNumber;
    PhoneNumber secondaryPhoneNumber;
    String secondaryEmail;
    Boolean startBookingDamageReportMandatory;
    String templateGroup;
    String termsOfSubscriptionUrl;
    String termsOfUseUrl;
    Boolean useExternalInvoiceSystem;
    String websiteConfirmSubscriptionUrl;
    String websiteResetPasswordUrl;

}
