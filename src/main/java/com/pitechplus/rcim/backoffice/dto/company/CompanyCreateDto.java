package com.pitechplus.rcim.backoffice.dto.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga on 30.06.2017.
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
public class CompanyCreateDto {

    String name;
    String email;
    String secondaryEmail;
    PhoneNumber phoneNumber;
    PhoneNumber secondaryPhoneNumber;
    String fiscalNumber;
    String logoUrl;
    Address address;
    String legalForm;
    Integer capital;
    String agencyCode;
    UUID configurationId;
}
