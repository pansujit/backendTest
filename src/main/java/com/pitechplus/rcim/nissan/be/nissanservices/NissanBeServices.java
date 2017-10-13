package com.pitechplus.rcim.nissan.be.nissanservices;

import com.pitechplus.nissan.be.nissanDto.membermigrate.MemberMigrationCreateDto;
import com.pitechplus.nissan.be.nissanDto.membermigrate.MemberMigrationDto;
import com.pitechplus.qautils.restutils.RestExchangeInfo;
import com.pitechplus.qautils.restutils.RestTemplateUtils;
import com.pitechplus.rcim.backoffice.data.enums.ApplicationType;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearch;
import com.pitechplus.rcim.backoffice.dto.search.supercompany.SuperCompanySearchResult;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.nissan.be.nissandto.groups.*;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberSearchDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberUpdateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.SearchMemberResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by dgliga  on 24.07.2017.
 */
@Component("nissan-be-services")
public class NissanBeServices {

	@Value("${services.gw.admin}")
	private String gwAdminBaseUrl;

	@Value("${members}")
	private String members;

	@Value("${get.member}")
	private String getMember;

	@Value("${view.update.member}")
	private String updateMember;

	@Value("${groups}")
	private String groups;

	@Value("${get.single.group}")
	private String getGroup;

	@Value("${save.group.membership}")
	private String saveGroupMembership;


	@Value("${search.groups}")
	private String searchGroup;

	@Value("${member.search}")
	private String searchMember;
	@Value("${member.migration}")
	private String memberMigration;
	

	private RestTemplateUtils restTemplateUtils = new RestTemplateUtils(15000, 15000);


	public ResponseEntity<MemberDto> createMember(String xAuthToken, MemberCreateDto memberCreate) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity<MemberCreateDto> memberCreateDtoHttpEntity = new HttpEntity<>(memberCreate, headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: Create Member")
				.url(gwAdminBaseUrl + members)
				.httpMethod(HttpMethod.POST)
				.requestBody(memberCreateDtoHttpEntity)
				.response(MemberDto.class)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
	}
	public ResponseEntity<SearchMemberResponse> searchMember (String xAuthToken,MemberSearchDto searchMemberxxx) {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity memberGetHttpEntity = new HttpEntity<>(searchMemberxxx,headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: search Member")
				.url(gwAdminBaseUrl + searchMember)
				.httpMethod(HttpMethod.POST)
				.requestBody(memberGetHttpEntity).sleepTimeAfterRequestInSec(5)
				.response(SearchMemberResponse.class)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);

	}

	public ResponseEntity<MemberDto> getMember (String xAuthToken, UUID memberID) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity memberGetHttpEntity = new HttpEntity<>(headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: Get Member")
				.url(gwAdminBaseUrl + getMember)
				.httpMethod(HttpMethod.GET)
				.requestBody(memberGetHttpEntity)
				.response(MemberDto.class)
				.uriVariables(memberID)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);

	}

	public ResponseEntity<MemberDto> updateMember(String xAuthToken, UUID memberId, MemberUpdateDto memberUpdateDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity<MemberUpdateDto> memberUpdateDtoHttpEntity = new HttpEntity<>(memberUpdateDto, headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: Update Member : " + memberId.toString())
				.url(gwAdminBaseUrl + updateMember)
				.httpMethod(HttpMethod.PUT)
				.requestBody(memberUpdateDtoHttpEntity)
				.uriVariables(memberId)
				.response(MemberDto.class)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
	}

	public ResponseEntity<GroupViewDto> createGroup(String xAuthToken, GroupCreateDto groupCreateDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity<GroupCreateDto> groupCreateDtoHttpEntity = new HttpEntity<>(groupCreateDto, headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: Create Group")
				.url(gwAdminBaseUrl + groups)
				.httpMethod(HttpMethod.POST)
				.requestBody(groupCreateDtoHttpEntity)
				.response(GroupViewDto.class)
				.sleepTimeAfterRequestInSec(2)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
	}

	public ResponseEntity<GroupViewDto> getGroup (String xAuthToken, UUID groupId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity groupGetHttpEntity = new HttpEntity<>(headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: Get Group")
				.url(gwAdminBaseUrl + getGroup)
				.httpMethod(HttpMethod.GET)
				.requestBody(groupGetHttpEntity)
				.response(GroupViewDto.class)
				.uriVariables(groupId)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);

	}

	public ResponseEntity<GroupMembershipViewDto> saveGroupMembership (String xAuthToken, UUID groupId, GroupMembershipDto groupMembershipDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity saveGroupMembershipHttpEntity = new HttpEntity<>(groupMembershipDto,headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: to add a new member to an existing group")
				.url(gwAdminBaseUrl + saveGroupMembership)
				.httpMethod(HttpMethod.POST)
				.requestBody(saveGroupMembershipHttpEntity)
				.response(GroupMembershipViewDto.class)
				.uriVariables(groupId)
				.sleepTimeAfterRequestInSec(2)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);

	}

	public ResponseEntity<GroupResultDto> searchGroups (String xAuthToken, GroupQueryDto groupQueryDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity searchGroups = new HttpEntity<>(groupQueryDto, headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: Search for groups")
				.url(gwAdminBaseUrl + searchGroup)
				.httpMethod(HttpMethod.POST)
				.requestBody(searchGroups)
				.response(GroupResultDto.class)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
	}
	
	public ResponseEntity<MemberMigrationDto> memberMigration(String xAuthToken, MemberMigrationCreateDto memberMigrationCreate) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-AUTH-TOKEN", xAuthToken);
		headers.set("x-app-origin", ApplicationType.GLIDE_BO.toString());
		HttpEntity<MemberMigrationCreateDto> memberCreateDtoHttpEntity = new HttpEntity<>(memberMigrationCreate, headers);
		RestExchangeInfo restExchangeInfo = RestExchangeInfo.builder()
				.requestDescription("Call service: Migrate member")
				.url(gwAdminBaseUrl + memberMigration)
				.httpMethod(HttpMethod.POST)
				.requestBody(memberCreateDtoHttpEntity)
				.response(MemberMigrationDto.class)
				.build();
		return restTemplateUtils.makeExchange(restExchangeInfo, BackOfficeException.class);
	}

}
