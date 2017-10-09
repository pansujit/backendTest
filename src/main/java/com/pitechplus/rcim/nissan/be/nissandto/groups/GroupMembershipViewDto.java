package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga  on 21.07.2017.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class GroupMembershipViewDto {
    UUID id;
    String groupPublicId;
    UUID groupId;
    String email;
    Boolean owner;
    UUID memberId;
    String memberFirstName;
    String memberLastName;
    Integer memberAge;
    String memberDescription;
    PhoneNumber memberPhoneNumber;
    GroupMembershipContractDto groupMembershipContract;

}
