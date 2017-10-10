package com.pitechplus.rcim.nissan.be.nissandto.members;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.pagination.Page;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSorter;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class MemberSearchDto {
	
String CompanyId;
String firstname;
String lastname;
String memberName;
String email;
Page page;
MemberSorter sort;

}
