package com.pitechplus.rcim.nissan.be.membermigrate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.pitechplus.nissan.be.nissanDto.membermigrate.MemberMigrationDto;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
//Need to negative test cases for migrate to same company, member has booking and has group
public class MigrateMemberTest extends BackendAbstract {
	private String memberId;

	@BeforeClass
	public void createMember() {
		MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(
				rcimTestData.getAdminSuperCompanyId().toString(), (nissanUtils.createArrayOfValidFiles(1)).get(0));
		memberId = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto).getBody().getId()
				.toString();
	}

	@Test
	public void migrateAUser() {
		ResponseEntity<MemberMigrationDto> x = nissanBeServices.memberMigration(rcimTestData.getSuperAdminToken(),
				NissanDtoBuilders.memberMigrationCreateDto(rcimTestData.getBookingSuperCompanyId().toString(),
						memberId));
		assertThat("Should response true but found other", x.getBody().getResult(), is(true));
	}

	@Test(description = "This test verifies that migrate member call with invalid  X-AUTH-TOKEN triggers correct error.")
	public void invalidAuthMigrateUserTest() throws IOException {
		try {
			nissanBeServices.memberMigration(rcimTestData.getSuperAdminToken() + "INVALID", NissanDtoBuilders
					.memberMigrationCreateDto(rcimTestData.getBookingSuperCompanyId().toString(), memberId));
			Assert.fail("Migration user with invalid Authentication Token worked!");

		} catch (HttpStatusCodeException exception) {
			assertThat("Server did not throw correct error!",
					ExceptionMapper.mapException(exception, BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.UNAUTHORIZED,
							ErrorMessages.INVALID_AUTHENTICATION_TOKEN, null, null));
		}
	}

	@Test(description = "This test verifies that migrate member call with invalid member id triggers correct error.")
	public void invalidMemberIdMigrateUserTest() throws IOException {
		try {
			nissanBeServices.memberMigration(rcimTestData.getSuperAdminToken(), NissanDtoBuilders
					.memberMigrationCreateDto(rcimTestData.getBookingSuperCompanyId().toString(), memberId + "yzs"));
			Assert.fail("Migration user with invalid member Id worked!");
		} catch (HttpStatusCodeException exception) {
			assertThat("Server did not throw correct error!",
					ExceptionMapper.mapException(exception, BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST,
							HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null));
		}
	}

	@Test(description = "This test verifies that migrate member call with invalid Company Id triggers correct error.")
	public void invalidCompanyIdMigrateUserTest() throws IOException {
		try {
			nissanBeServices.memberMigration(rcimTestData.getSuperAdminToken(), NissanDtoBuilders
					.memberMigrationCreateDto(rcimTestData.getBookingSuperCompanyId().toString() + "bcf", memberId));
			Assert.fail("Migration user with invalid company Id worked!");
		} catch (HttpStatusCodeException exception) {
			assertThat("Server did not throw correct error!",
					ExceptionMapper.mapException(exception, BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST,
							HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null));
		}
	}

	/*
	 * @Test public void MigrateUserToSameCompanyTest() { }
	 */
	@Test(description = "This test verifies that migrate member call with wrong member Id triggers correct error.")
	public void wrongMemberIdMigrateUserTest() throws IOException {
		try {
			String[] wrongMemberId = memberId.split("-");
			wrongMemberId[1] = "1111";
			String newWrongMemberId = wrongMemberId[0] + "-" + wrongMemberId[1] + "-" + wrongMemberId[2] + "-"
					+ wrongMemberId[3] + "-" + wrongMemberId[4];

			nissanBeServices.memberMigration(rcimTestData.getSuperAdminToken(), NissanDtoBuilders
					.memberMigrationCreateDto(rcimTestData.getBookingSuperCompanyId().toString(), newWrongMemberId));
			Assert.fail("Migration user with Wrong member Id worked!");
		} catch (HttpStatusCodeException exception) {
			assertThat("Server did not throw correct error!",
					ExceptionMapper.mapException(exception, BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND,
							HttpStatus.NOT_FOUND.getReasonPhrase(), null, null));
		}
	}

	@Test(description = "This test verifies that migrate member call with wrong member Id triggers correct error.")
	public void wrongCompanyIdMigrateUserTest() throws IOException {
		try {
			String[] wrongCompanyId = rcimTestData.getBookingSuperCompanyId().toString().split("-");
			wrongCompanyId[0] = "11111111";
			String newWrongMemberId = wrongCompanyId[0] + "-" + wrongCompanyId[1] + "-" + wrongCompanyId[2] + "-"
					+ wrongCompanyId[3]+"-"+wrongCompanyId[4];

			nissanBeServices.memberMigration(rcimTestData.getSuperAdminToken(),
					NissanDtoBuilders.memberMigrationCreateDto(newWrongMemberId, memberId));
			Assert.fail("Migration user with Wrong company Id worked!");
		} catch (HttpStatusCodeException exception) {
			assertThat("Server did not throw correct error!",
					ExceptionMapper.mapException(exception, BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.NOT_FOUND,
							HttpStatus.NOT_FOUND.getReasonPhrase(), null, null));
		}
	}

	// After test should be done with creating one user return back to initial state

}
