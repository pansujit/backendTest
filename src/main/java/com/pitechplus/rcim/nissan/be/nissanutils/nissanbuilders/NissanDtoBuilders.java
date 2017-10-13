package com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders;

import com.pitechplus.nissan.be.nissanDto.membermigrate.MemberMigrationCreateDto;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.backoffice.data.RcimTestData;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandata.nissanenums.*;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipContractDto;
import com.pitechplus.rcim.nissan.be.nissandto.groups.GroupMembershipDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.*;
import org.apache.commons.lang.math.RandomUtils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.pitechplus.qautils.randomgenerators.NumberGenerator.randInt;

/**
 * Created by dgliga  on 21.07.2017.
 */
public class NissanDtoBuilders {


    /**
     * This method is used to create a memberCreateDto object with valid data
     *
     * @return memberCreateDto object with all fields having valid values
     */
    public static MemberCreateDto buildMemberCreateDto(String superCompanyId, UUID fileId) {

        return MemberCreateDto.builder()
                .login("rcimtesting+" + randInt(0, 999999) + "_" + randInt(0, 999999) + "@gmail.com")
                .password("1Aaaaaaa")
                .firstName(PersonalInfoGenerator.generateOrcName() + PersonalInfoGenerator.generateOrcName())
                .lastName(PersonalInfoGenerator.generateOrcName() + PersonalInfoGenerator.generateOrcName())
                .phoneNumber(DtoBuilders.buildRandomFrPhoneNumber())
                .birthDate(LocalDate.now().minusYears(19).toString())
                .address(DtoBuilders.buildRandomAddress())
                .vip((RandomUtils.nextBoolean()))
                .locale(Locale.FRANCE)
                .drivingLicence(buildDrivingLicenceDto(fileId))
                .companyId(UUID.fromString(superCompanyId)).build();
    }


    /**
     * This method is used to create a randomDrivingLicence object with valid data
     *
     * @return drivingLicenceDto object with all fields having valid values
     */
    public static DrivingLicenceDto buildDrivingLicenceDto(UUID fileId) {

        return DrivingLicenceDto.builder()
                .fileId(fileId)
                .licenceNumber(PersonalInfoGenerator.generateOrcName() + randInt(1, 9000) + PersonalInfoGenerator.generateName(5))
                .expirationDate(LocalDate.now().plusMonths(10).toString())
                .cityDeliverance(PersonalInfoGenerator.generateOrcName() + " City")
                .deliveranceDate(LocalDate.now().plusMonths(6).toString())
                .status(ReviewStatus.TO_REVIEW)
                .validated(false)
                .build();
    }

    /**
     * This method is used to create a random DocumentaryEvidence object with valid data
     *
     * @return documentaryEvidenceDto object with all fields having valid values
     */
    public static DocumentaryEvidenceDto buildDocumentaryEvidenceDto(UUID id) {

        //  ReviewStatus status = randomEnum(ReviewStatus.class);
        RcimTestData rcimTestData1 = new RcimTestData();

        return DocumentaryEvidenceDto.builder()
                .fileId(id)
                .status(ReviewStatus.TO_REVIEW)
                .validated(false)
                .build();
    }

    /**
     * This method is used to create a memberUpdateDto object with valid data
     *
     * @return memberUpdateDto object with all fields having valid values
     */
    public static MemberUpdateDto buildMemberUpdateDto(List<UUID> idFile, DrivingLicenceDto drivingLicenceDto) {

        return MemberUpdateDto.builder()
                .civility(randomEnum(Civility.class))
                .firstName(PersonalInfoGenerator.generateOrcName() + PersonalInfoGenerator.generateOrcName())
                .lastName(PersonalInfoGenerator.generateOrcName() + PersonalInfoGenerator.generateOrcName())
                .phoneNumber(DtoBuilders.buildRandomFrPhoneNumber())
                .secondaryPhoneNumber(DtoBuilders.buildRandomFrPhoneNumber())
                .emergencyPhoneNumber(DtoBuilders.buildRandomFrPhoneNumber())
                .birthDate(LocalDate.now().minusYears(19).toString())
                .birthPlace(PersonalInfoGenerator.generateOrcName())
                .commercialOffers(RandomUtils.nextBoolean())
                .address(DtoBuilders.buildRandomAddress())
                .vip(RandomUtils.nextBoolean())
                .avatar(NissanDtoBuilders.buildMemberAvatar(idFile.get(0), idFile.get(1)))
                .drivingLicence(drivingLicenceDto)
                .identityDocument(NissanDtoBuilders.buildDocumentaryEvidenceDto(idFile.get(2)))
                .employerCertificate(NissanDtoBuilders.buildDocumentaryEvidenceDto(idFile.get(3)))
                .lastTaxNotice(NissanDtoBuilders.buildDocumentaryEvidenceDto(idFile.get(4)))
                .lastPaySlip((NissanDtoBuilders.buildDocumentaryEvidenceDto(idFile.get(5))))
                .locale(Locale.CANADA_FRENCH)
                .technician(RandomUtils.nextBoolean())
                .sex(randomEnum(SexDto.class))
                .maritalStatus(randomEnum(MaritalStatus.class))
                .profession(PersonalInfoGenerator.generateOrcName())
                .description(PersonalInfoGenerator.generateOrcName()).build();

    }


    /**
     * This method is used to create an avatarMember object with valid data
     *
     * @return MemberAvatarDto object with all fields having valid values
     */
    private static MemberAvatarDto buildMemberAvatar(UUID avatarId, UUID thumbnailId) {
        return MemberAvatarDto.builder()
                .avatarId(avatarId)
                .thumbnailId(thumbnailId).build();
    }

    /**
     * This method is used to create an groupCreate object with valid data
     *
     * @return GroupCreateDto object with all fields having valid values
     */

    public static GroupCreateDto buildGroupCreateDto(Set<GroupMembershipDto> groupMembershipsList, RcimTestData rcimTestData, ParkingDto parkingDto) {

        return GroupCreateDto.builder()
                .companyId(rcimTestData.getCompanyDto().getId())
                .publicId(PersonalInfoGenerator.generateName(10))
                .isPrivate(RandomUtils.nextBoolean())
                .parkingId(parkingDto.getId())
                .groupMemberships(groupMembershipsList).build();
    }


    /**
     * This method is used to create an groupCreate object with valid data
     *
     * @return GroupMembershipDto object with all fields having valid values
     */

    public static GroupMembershipDto buildGroupMembershipDto(MemberDto memberDto, UUID contractFileId) {
        return GroupMembershipDto.builder()
                .memberId(memberDto.getId())
                .owner(false)
                .groupMembershipContract(NissanDtoBuilders.buildGroupMembershipContractDto(contractFileId)).build();
    }


    /**
     * This method is used to create a groupMembershipContractDto object with valid data
     *
     * @return GroupMembershipContractDto object with all fields having valid values
     */
    public static GroupMembershipContractDto buildGroupMembershipContractDto(UUID contractFileId) {
        int contractDuration = randInt(6, 18);
        return GroupMembershipContractDto.builder()
                .publicId(PersonalInfoGenerator.generateName(10))
                .pricePerMonth(new BigDecimal(Math.random()).setScale(2, BigDecimal.ROUND_HALF_UP))
                .contractDuration(contractDuration)
                .allocatedDistance(randInt(100, 300))
                .allocatedTime(randInt(100, 200))
                .contractFileId(contractFileId)
                .state(randomEnum(ContractState.class))
                .type(randomEnum(ContractType.class))
                .startDate(LocalDate.now().toString())
                .endDate(LocalDate.now().plusMonths(contractDuration).toString()).build();
    }

    public static MemberMigrationCreateDto memberMigrationCreateDto(String anotherCompanyId, String memberId) {
    		return MemberMigrationCreateDto.builder().newCompanyId(anotherCompanyId).memberId(memberId).build();
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        SecureRandom random = new SecureRandom();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

}


