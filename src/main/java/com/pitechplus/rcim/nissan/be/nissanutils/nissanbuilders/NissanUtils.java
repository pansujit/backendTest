package com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders;

import com.pitechplus.rcim.backoffice.data.RcimTestData;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.service.BackUserService;
import com.pitechplus.rcim.backoffice.service.ConfigsService;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanservices.NissanBeServices;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;

/**
 * Created by dgliga  on 16.08.2017.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("files-Id")
public class NissanUtils {

    @Autowired
    ConfigsService backOfficeService;

    @Autowired
    BackUserService backUserService;

    @Autowired
    NissanBeServices nissanBeServices;

    @Value("${bo.super.admin.username}")
    String boSuperAdminUsername;

    @Value("${bo.super.admin.password}")
    String boSuperAdminPassword;


    public List<UUID> createArrayOfValidFiles(int nrOfValidFiles) {
        return IntStream.range(0, nrOfValidFiles)
                .mapToObj(index -> backOfficeService.createFile(extractXAuthTokenFromResponse(backUserService.authUser
                        (new Login(boSuperAdminUsername, boSuperAdminPassword))), DtoBuilders.buildFile()).getBody().getId())
                .collect(Collectors.toList());
    }

    public HashSet<GroupMembershipDto> createListOfMembersOfTheSameCompany(int nrOfMembers, int ownerMember, RcimTestData rcimTestData) {

        HashSet<GroupMembershipDto> listOfGroupMembers = new HashSet<>();
        List<UUID> listOfFiles = createArrayOfValidFiles(nrOfMembers);
        for (int i = 0; i < nrOfMembers; i++) {
            MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(rcimTestData.getMemberDto().getCompany().getId().toString(),
                    listOfFiles.get(i));
            memberCreateDto.setCompanyId(rcimTestData.getSuperCompanyDto().getId());
            MemberDto memberDto = nissanBeServices.createMember(rcimTestData.getSuperAdminToken(), memberCreateDto).getBody();
            memberDto.setCompany(rcimTestData.getSuperCompanyDto());
            GroupMembershipDto groupMembership = NissanDtoBuilders.buildGroupMembershipDto(memberDto, createArrayOfValidFiles(1).get(0));

            if (i == ownerMember)
                groupMembership.setOwner(true);
            listOfGroupMembers.add(groupMembership);
        }
        return listOfGroupMembers;
    }
}
