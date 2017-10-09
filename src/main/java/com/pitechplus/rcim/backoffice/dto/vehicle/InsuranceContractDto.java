package com.pitechplus.rcim.backoffice.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.data.enums.InsuranceContractType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by dgliga on 17.08.2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InsuranceContractDto {

    UUID id;
    String contractNumber;
    String expirationDate;
    Boolean glassBreakage;
    String startDate;
    UUID uploadedFileId;
    Set<String> options;
    List<InsuranceContractType> types;

}
