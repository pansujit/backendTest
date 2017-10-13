package com.pitechplus.rcim.backoffice.dto.smartcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import com.pitechplus.rcim.backoffice.dto.supercompany.ContractDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyConfigurationDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyDto {
	Address address;
	String backofficeResetPasswordUrl;
	Boolean billBeforeBooking;
	int capital;
	String configurationId;
	SuperCompanyConfigurationDto computedConfiguration;
	ContractDto contract;
	DrivingAuthorisationSettingsDto drivingAuthorisationSettings;
	String email;
	boolean employerCertificateRequired;
	String employerCertificateUrl;
	Boolean drivingLicenceRequired;
	int durationAfterTripToAllowLockUnlock;
	Boolean enableSafetyDeposit;
	boolean endBookingDamageReportMandatory;
	String fiscalNumber;
	String id;
	Boolean identityDocumentRequired;
	Boolean invoiceDelegate;
	String invoiceLabel;
	String legalForm;
	String logoUrl;
	String name;
	PhoneNumber phoneNumber;
	int safetyDepositAmount;
	String secondaryEmail;
	PhoneNumber secondaryPhoneNumber;
	boolean sendDrivingAuthorisation;
	boolean startBookingDamageReportMandatory;
	String templateGroup;
	String templateId;
	String termsOfSubscriptionUrl;
	String termsOfUseUrl;
	boolean useExternalInvoiceSystem;
	String websiteConfirmSubscriptionUrl;
	String websiteResetPasswordUrl;
	int preBookingBillingOffset;
}
