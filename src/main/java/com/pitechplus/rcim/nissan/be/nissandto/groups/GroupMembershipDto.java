package com.pitechplus.rcim.nissan.be.nissandto.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Created by dgliga  on 03.08.2017.
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
public class GroupMembershipDto {

    UUID id;
    UUID memberId;
    Boolean owner;
    GroupMembershipContractDto groupMembershipContract;
}
