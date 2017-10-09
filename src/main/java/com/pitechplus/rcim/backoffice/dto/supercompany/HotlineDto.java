package com.pitechplus.rcim.backoffice.dto.supercompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

/**
 * Created by dgliga on 08.05.2017.
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
class HotlineDto {

    UUID id;
    String name;
    String customerServiceName;
    String customerServiceEmail;
    String noReplyEmail;
    List<PhoneNumber> phoneNumbers;

}
