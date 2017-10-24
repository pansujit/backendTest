package com.pitechplus.rcim.nissan.be.members.create;

import com.pitechplus.qautils.annotations.TestInfo;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.nissan.be.nissandto.members.CommentCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.CommentResponseDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberImpersonateResultDto;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanmappers.MemberMapper;
import com.rits.cloning.Cloner;

import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by dgliga  on 24.07.2017.
 */
public class AddMembersTests extends BackendAbstract {
 	String ranodmString=UUID.randomUUID().toString().replace("-", "").substring(0, 10);
	private MemberDto responseMemberDto;
	private  MemberDto expectedMemberDto;
    @Test(priority=1,description = "This test verifies that call to create a member works accordingly.")
    @TestInfo(expectedResult = "Member created with correct information which was given on request.")
    public void createMemberTest() {

        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getAdminSuperCompanyId().toString(),
                (nissanUtils.createArrayOfValidFiles(1)).get(0));
        expectedMemberDto = MemberMapper.mapMemberCreateDtoToMemberDto(memberCreateDto, companyService
                .getSuperCompany(rcimTestData.getSuperAdminToken(), rcimTestData.getAdminSuperCompanyId()).getBody());
       
        responseMemberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(),
                memberCreateDto).getBody();
        assertThat("Information given on response to add member does not reflect request !",
                responseMemberDto, is(expectedMemberDto));
    }
  /*  @Test(priority=2,dependsOnMethods="createMemberTest")
    public void postCommentTest() {
    	CommentCreateDto x=NissanDtoBuilders.memberCommentCreateDto(ranodmString);
    	CommentResponseDto responseComment=nissanBeServices.postMemberComment(rcimTestData.getSuperAdminToken(), x, 
    			responseMemberDto.getId().toString()).getBody();
    	Cloner cloner= new Cloner();
    	CommentResponseDto test=cloner.deepClone(responseComment);
    	test.setBody(x.getBody());
    	assertThat("The response doesnot contains the given body",responseComment.getBody(),is(test.getBody()));
    	assertThat("The response doesnot contains the given body",responseComment.getDate(),is(test.getDate()));
    	assertThat("The response doesnot contains the given body",responseComment.getSubmittedBy(),is(test.getSubmittedBy()));

    }
    
   @Test(priority=3,dependsOnMethods= {"createMemberTest","postCommentTest"})
    public void getCommentTest() {
	   CommentResponseDto[] response=nissanBeServices.getMemberCommment(rcimTestData.getSuperAdminToken(), 
    			responseMemberDto.getId().toString()).getBody();
	  List<CommentResponseDto> y= Arrays.asList(response);

    	assertThat("The response doesnot contains the given body",y.get(0).getBody(),is(ranodmString));
    }
    @Test(priority=4,dependsOnMethods="createMemberTest")
    public void impersonateMemberTest() {
       MemberImpersonateResultDto response=nissanBeServices.impersonateMember(rcimTestData.getSuperAdminToken(), 
    			responseMemberDto.getId().toString()).getBody();

    	assertThat("The response doesnot contains the givern email",response.getLogin(),is(responseMemberDto.getLogin()));
    	assertThat("The response contains null token",response.getToken(),is(notNullValue()));
    	assertThat("The response contains null token",response.getApplication(),is(notNullValue()));

    }
    
    @Test(priority=5,dependsOnMethods="createMemberTest")
    public void rejectProfileMemberTest() {
       ResponseEntity<Object> response=nissanBeServices.profileReject(rcimTestData.getSuperAdminToken(), 
    			NissanDtoBuilders.memberCommentCreateDto("hello"),responseMemberDto.getId().toString());

    	assertThat("The response doesnot contains the success code",response.getStatusCodeValue(),is(204));

    }
    @Test(priority=6,dependsOnMethods="createMemberTest")
    public void approveProfileMemberTest() {
       ResponseEntity<Object> response=nissanBeServices.profileApprove(rcimTestData.getSuperAdminToken(), 
    			responseMemberDto.getId().toString());

    	assertThat("The response doesnot contains the success code",response.getStatusCodeValue(),is(204));

    }
    @Test(priority=7,dependsOnMethods="createMemberTest")
    public void suspendMemberTest() {
       MemberDto response=nissanBeServices.memberSuspend(rcimTestData.getSuperAdminToken(),NissanDtoBuilders.memberCommentCreateDto("hello"),
    			responseMemberDto.getId().toString()).getBody();
    	assertThat("The response contains The suspended member is false",response.getSuspended(),is(Boolean.TRUE));

    }
    @Test(priority=8,dependsOnMethods= {"createMemberTest","suspendMemberTest"})
    public void unsuspendMemberTest() {
       MemberDto response=nissanBeServices.memberUnsuspend(rcimTestData.getSuperAdminToken(), 
    			responseMemberDto.getId().toString()).getBody();
    	assertThat("The response contains The suspended member is true",response.getSuspended(),is(Boolean.FALSE));

    }
    
    @Test(priority=9,dependsOnMethods= {"createMemberTest"})
    public void updateEmailMemberTest() {
    	String email=UUID.randomUUID().toString().replace("-","").substring(12,18)+"@"+"test.com";
    	NissanDtoBuilders.updateEmailCreateDto(email);
    	
       MemberDto response=nissanBeServices.changeEmail(rcimTestData.getSuperAdminToken(),NissanDtoBuilders.updateEmailCreateDto(email),
    		   responseMemberDto.getId().toString()).getBody(); 
       responseMemberDto.setLogin(email);	
    	assertThat("The response contains the old email, update is not done",response.getLogin(),is(email));

    }*/
    
    
    


}

