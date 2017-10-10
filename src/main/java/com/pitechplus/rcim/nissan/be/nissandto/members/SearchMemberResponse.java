package com.pitechplus.rcim.nissan.be.nissandto.members;

import java.util.List;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchMetadata;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchResponse;
import com.pitechplus.rcim.backoffice.dto.search.backuser.BackUserSearchResponse.BackUserSearchResponseBuilder;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class SearchMemberResponse {
	
	 List<MemberDto> results;
	 BackUserSearchMetadata metadata;

}
