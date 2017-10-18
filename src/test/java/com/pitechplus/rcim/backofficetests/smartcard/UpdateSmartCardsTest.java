package com.pitechplus.rcim.backofficetests.smartcard;

import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.CARDID_MAY_NOT_BE_EMPTY;
import static com.pitechplus.rcim.backoffice.data.enums.ValidationError.PROTOCOL_MAY_NOT_BE_NULL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.constants.ErrorMessages;
import com.pitechplus.rcim.backoffice.data.enums.Protocol;
import com.pitechplus.rcim.backoffice.data.enums.ServiceCalled;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardCreateDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardUpdateDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.backoffice.utils.builders.ValidationErrorsBuilder;
import com.pitechplus.rcim.backoffice.utils.custommatchers.ExceptionMatcher;
import com.pitechplus.rcim.backoffice.utils.exceptions.BackOfficeException;
import com.pitechplus.rcim.backoffice.utils.mappers.ExceptionMapper;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;

public class UpdateSmartCardsTest extends BackendAbstract {
	private String login;
	private String superCompanyId;
	private SmartCardDto smartcardDto;
	private SmartCardCreateDto smartCardCreateDto;
	@BeforeClass
	public void createSmartCard() {
		MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(
				rcimTestData.getAdminSuperCompanyId().toString(), (nissanUtils.createArrayOfValidFiles(1)).get(0));
		MemberDto responseMemberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto)
				.getBody();
		login = responseMemberDto.getLogin();
		superCompanyId = responseMemberDto.getCompany().getId().toString();
		smartCardCreateDto = DtoBuilders.buildSmartCardCreate(superCompanyId, login);
	    smartcardDto = smartCardService.createANewSmartCard(rcimTestData.getSuperAdminToken(), smartCardCreateDto).getBody();
		 
	}
	@Test
	public void updateSmartCardTest() {
		SmartCardUpdateDto smartCardUpdateDto=DtoBuilders.updateSmartCardBuild(superCompanyId, login, Protocol.HID_PROX, 
				smartcardDto.getId(), UUID.randomUUID().toString().replace("-","").substring(0,20));
		SmartCardDto x=
				smartCardService.updateSmartCard(rcimTestData.getSuperAdminToken(), 
						smartcardDto.getId(), smartCardUpdateDto).getBody();
		assertThat("The update is not done according to given information in smartcard",x,is(DtoBuilders.buildExpectedUpdateSmartCardDto(smartcardDto, x)));
	}
	
	@Test(dataProvider="missingMandatoryFieldData")
	public void updateSmartCardMissingMandatoryFieldTest(String cardId,Protocol protocol,Set<String>validationError ) throws IOException {
		try {
			SmartCardUpdateDto smartCardUpdateDto=DtoBuilders.updateSmartCardBuild(superCompanyId, login,protocol,null,cardId);
					smartCardService.updateSmartCard(rcimTestData.getSuperAdminToken(), 
							smartcardDto.getId(), smartCardUpdateDto).getBody();
			Assert.fail("Works without manadatory field");
			
		}catch(HttpStatusCodeException exception) {
			assertThat("",ExceptionMapper.mapException(exception,BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
							null,validationError ));
		}
		
	}
	@Test(dataProvider="negativeUpdateSmartCardData")
	public void negativeUpdateSmartCardTest(Protocol protocol,String id,String cardId
			) throws IOException {
		try {
			SmartCardUpdateDto smartCardUpdateDto=DtoBuilders.updateSmartCardBuild(superCompanyId, login,protocol,id,cardId);
			smartCardService.updateSmartCard(rcimTestData.getSuperAdminToken(), 
					smartcardDto.getId(), smartCardUpdateDto).getBody();
	Assert.fail("Works with invalid field");
		}catch(HttpStatusCodeException exception) {
			assertThat("The server cannot find the good exception",ExceptionMapper.mapException(exception, BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(
							HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), 
							null, null));
			
		}
	}
	@Test()
	public void InvalidAuthUpdateSmartCardTest() throws IOException {
		try {
			SmartCardUpdateDto smartCardUpdateDto=DtoBuilders.updateSmartCardBuild(superCompanyId, login,Protocol.AWID,null,"hello");
			smartCardService.updateSmartCard(rcimTestData.getSuperAdminToken()+"zezze", 
					smartcardDto.getId(), smartCardUpdateDto).getBody();
	Assert.fail("Works with invalid Authentication Token");
		}catch(HttpStatusCodeException exception) {
			assertThat("The server cannot find the good exception",ExceptionMapper.mapException(exception, BackOfficeException.class),
					ExceptionMatcher.isExpectedBackOfficeException(
							HttpStatus.UNAUTHORIZED, ErrorMessages.INVALID_AUTHENTICATION_TOKEN, 
							null, null));
			
		}
	}
	
	@DataProvider
	public Object[][] negativeUpdateSmartCardData() {
		Protocol invalidProtocol=Protocol.INVALID;
		Protocol validProtocol=Protocol.AWID;
		String validID=UUID.randomUUID().toString();
		String invalidUUID=UUID.randomUUID().toString().substring(0, 27);
		String cardId="hello";
		return new Object[][] {
			{invalidProtocol,validID,cardId},
			{validProtocol,invalidUUID,cardId},

		};
	}
	@DataProvider
	public Object[][] missingMandatoryFieldData() {
		String cardIdNull=null;
		Protocol protocolNull=null;
		Protocol protocolValue=Protocol.HID_ICLASS;
		String cardIdValue=UUID.randomUUID().toString().replace("-", "").substring(0,6);
		return new Object[][] {
			{cardIdNull,protocolValue,ValidationErrorsBuilder.buildValidationErrors(
					ServiceCalled.SMART_CARD_UPDATE_DTO,CARDID_MAY_NOT_BE_EMPTY)},
			{cardIdValue,protocolNull,ValidationErrorsBuilder.buildValidationErrors(
					ServiceCalled.SMART_CARD_UPDATE_DTO,PROTOCOL_MAY_NOT_BE_NULL)},
			{cardIdNull,protocolNull,ValidationErrorsBuilder.buildValidationErrors(
					ServiceCalled.SMART_CARD_UPDATE_DTO,PROTOCOL_MAY_NOT_BE_NULL,CARDID_MAY_NOT_BE_EMPTY)}
		};
	}
	
	

	

}
