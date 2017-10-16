package com.pitechplus.rcim.backoffice.utils.builders;

import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.qautils.randomgenerators.PersonalInfoGenerator;
import com.pitechplus.rcim.backoffice.data.RcimTestData;
import com.pitechplus.rcim.backoffice.data.enums.*;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserCreate;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUserUpdate;
import com.pitechplus.rcim.backoffice.dto.booking.BookingCreateDto;
import com.pitechplus.rcim.backoffice.dto.booking.BookingDto;
import com.pitechplus.rcim.backoffice.dto.booking.FilteredSearchDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.CarSharingInfoDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.DateLocationDto;
import com.pitechplus.rcim.backoffice.dto.booking.filteredsearch.SearchBookingResultDto;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.Coordinates;
import com.pitechplus.rcim.backoffice.dto.common.FileDto;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import com.pitechplus.rcim.backoffice.dto.company.*;
import com.pitechplus.rcim.backoffice.dto.report.*;
import com.pitechplus.rcim.backoffice.dto.report.search.DamageSearchDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardCreateDto;
import com.pitechplus.rcim.backoffice.dto.smartcard.SmartCardDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.ContractDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyConfigurationEditDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyCreate;
import com.pitechplus.rcim.backoffice.dto.vehicle.*;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.pitechplus.qautils.randomgenerators.NumberGenerator.randInt;
import static com.pitechplus.rcim.backoffice.data.enums.AccessoryType.getRandomAccessories;
import static com.pitechplus.rcim.backoffice.data.enums.StatusType.INACTIVE;

/**
 * Created by dgliga on 15.02.2017.
 */
public class DtoBuilders {


    /**
     * This method builds random France phone number
     *
     * @return Valid Fr Phone number
     */
    public static PhoneNumber buildRandomFrPhoneNumber() {
        return PhoneNumber.builder()
                .countryCode("+33")
                .nationalNumber("590" + RandomStringUtils.random(6, false, true))
                .phoneNumberName(PersonalInfoGenerator.generateName(5)).build();
    }

    /**
     * This method builds a random Address
     *
     * @return Address object
     */
    public static Address buildRandomAddress() {
        String randomCountry = Locale.getISOCountries()[randInt(0, Locale.getISOCountries().length - 1)];
        String postalCode = String.valueOf(randInt(10000, 99999));
        String streetName = PersonalInfoGenerator.generateOrcName() + " Street";
        String streetNumber = String.valueOf(randInt(1, 500));
        String city = PersonalInfoGenerator.generateOrcName() + " City";
        return Address.builder()
                .city(city)
                .country(randomCountry)
                .postalCode(postalCode)
                .streetName(streetName)
                .streetNumber(streetNumber)
                .formattedAddress(streetNumber + " " + streetName + ", " + postalCode + " " + city + ", " + new Locale("", randomCountry).getDisplayCountry()).
                        build();
    }

    private static Coordinates buildRandomCoordinates() {
        return Coordinates.builder()
                .latitude(NumberGenerator.randDouble(0, 20))
                .longitude(NumberGenerator.randDouble(0, 20)).build();
    }

    /**
     * This method builds BoUserDto objects needed to make requests to add backuser in admin gateway
     *
     * @param backOfficeRole what role do you want your backuser to have
     * @param companyId      what company does the backuser belong to
     * @return BackUserCreate object with all fields valid
     */
    public static BackUserCreate buildBackUserCreate(BackOfficeRole backOfficeRole, UUID companyId) {
        return BackUserCreate.builder()
                .address(buildRandomAddress())
                .phoneNumber(buildRandomFrPhoneNumber())
                .firstName(PersonalInfoGenerator.generateOrcName())
                .lastName(PersonalInfoGenerator.generateOrcName())
                .locale(Locale.FRANCE)
                .login("rcimtesting+" + randInt(10000, 99999) + "@gmail.com")
                .role(backOfficeRole.toString())
                .companyId(companyId)
                .build();
    }

    /**
     * This method builds Back User Update object needed for request to update back user
     *
     * @param backOfficeRole which you want for the back user to updated
     * @param companyId      which you want for the back user to update
     * @return BackUserUpdate object containing all information needed to update a back user
     */
    public static BackUserUpdate buildBackUserUpdate(BackOfficeRole backOfficeRole, UUID companyId) {
        Address address = buildRandomAddress();
        return BackUserUpdate.builder()
                .address(address)
                .phoneNumber(buildRandomFrPhoneNumber())
                .secondaryPhoneNumber(buildRandomFrPhoneNumber())
                .firstName(PersonalInfoGenerator.generateOrcName())
                .lastName(PersonalInfoGenerator.generateOrcName())
                .locale(Locale.FRANCE)
                .role(backOfficeRole.toString())
                .companyId(companyId)
                .build();
    }

    /**
     * This method builds a Super Company Create object
     *
     * @param configurationId which is mandatory for any super company created
     * @param groupTemplate   which is mandatory for any super company created
     * @return SuperCompanyCreate object with fields having valid values
     */
    public static SuperCompanyCreate buildSuperCompanyCreate(UUID configurationId, String groupTemplate) {
        String name = "ZYX_" + PersonalInfoGenerator.generateOrcName() + " Of " + PersonalInfoGenerator.generateOrcName();
        return SuperCompanyCreate.builder()
                .address(buildRandomAddress())
                .backofficeResetPasswordUrl("admin.glide." + name + ".com/#/reset-password/{resetPasswordToken}")
                .capital(randInt(0, 999999))
                .configurationId(configurationId)
                .durationAfterTripToAllowLockUnlock(ThreadLocalRandom.current().nextLong(0, 20))
                .email(name + "@" + name + ".com")
                .employerCertificateRequired(false)
                .employerCertificateUrl("https://" + name + ".com")
                .endBookingDamageReportMandatory(RandomUtils.nextBoolean())
                .fiscalNumber(String.valueOf(randInt(100, 99999)) + PersonalInfoGenerator.generateName(5))
                .identityDocumentRequired(false)
                .invoiceDelegate(RandomUtils.nextBoolean())
                .invoiceLabel("A" + randInt(1000, 99999))
                .legalForm("Legal form: " + name)
                .name(name + " Hoard Faction")
                .phoneNumber(buildRandomFrPhoneNumber())
                .secondaryEmail(name + "@" + name + ".fr")
                .secondaryPhoneNumber(buildRandomFrPhoneNumber())
                .startBookingDamageReportMandatory(RandomUtils.nextBoolean())
                .templateGroup(groupTemplate)
                .termsOfSubscriptionUrl("https://subscription-" + name + ".com")
                .termsOfUseUrl("https://terms-" + name + ".com")
                .useExternalInvoiceSystem(RandomUtils.nextBoolean())
                .websiteConfirmSubscriptionUrl("www.glide." + name + ".com/#/validate-account/{confirmSubscriptionToken}")
                .websiteResetPasswordUrl("www.glide." + name + ".com/#/reset-password/{resetPasswordToken}").build();
    }

    /**
     * This method is used to create a CompanyCreateDto object with valid data
     *
     * @param configurationId value which you want the object field with same name to have
     * @return CompanyCreateDto object with all fields having valid values
     */
    public static CompanyCreateDto buildCompanyCreate(UUID configurationId) {
        String name = PersonalInfoGenerator.generateOrcName() + " of " + PersonalInfoGenerator.generateOrcName() + " the "
                + NumberGenerator.randInt(0, 100);
        return CompanyCreateDto.builder()
                .address(buildRandomAddress())
                .phoneNumber(buildRandomFrPhoneNumber())
                .secondaryPhoneNumber(buildRandomFrPhoneNumber())
                .capital(randInt(0, 99999))
                .agencyCode(PersonalInfoGenerator.generateName(10))
                .configurationId(configurationId)
                .name(name + " Company")
                .email(name + "@" + name + ".com")
                .secondaryEmail(name + "@" + name + ".org")
                .fiscalNumber(PersonalInfoGenerator.generateName(10))
                .logoUrl("https://logo-" + name + ".com")
                .legalForm(PersonalInfoGenerator.generateName(30)).build();

    }

    /**
     * This method builds a CompanyDto object out of a CompanyCreateDto
     *
     * @param companyCreateDto object with values copied from companyCreateDto
     * @param parentCompanyId  value which you want the field with same name from CompanyDto to have
     * @return CompanyDto object with properties copied from CompanyCreateDto
     */
    public static CompanyDto buildExpectedCompanyDto(CompanyCreateDto companyCreateDto, UUID parentCompanyId) {
        CompanyDto companyDto = new CompanyDto();
        BeanUtils.copyProperties(companyCreateDto, companyDto);
        companyDto.setParentCompanyId(parentCompanyId);
        return companyDto;
    }

    /**
     * This method builds a ContractDto object with fields having valid values
     *
     * @return ContractDto object
     */
    public static ContractDto buildContractDto(int plusMonthStart, int plusMonthEnd) {
        String name = PersonalInfoGenerator.generateOrcName();
        return ContractDto.builder()
                .name(name + " Contract" + " the " + NumberGenerator.randInt(1, 1000) + " son")
                .businessCarSharing((RandomUtils.nextBoolean()))
                .privateCarSharing((RandomUtils.nextBoolean()))
                .shuttle((RandomUtils.nextBoolean()))
                .rideSharing((RandomUtils.nextBoolean()))
                .reference(PersonalInfoGenerator.generateName(10))
                .contractEnd(LocalDate.now().plusMonths(plusMonthEnd).toString())
                .contractStart(LocalDate.now().plusMonths(plusMonthStart).toString()).build();
    }

    /**
     * This method creates SiteDto object with valid values for all fields
     *
     * @param subCompanyId for which you want to create a site
     * @return Sitedto object
     */
    public static SiteDto buildSiteDto(UUID subCompanyId) {
        return SiteDto.builder()
                .timeUnitOfBooking(randInt(10, 600))
                .minDurationOfBooking(randInt(10, 50))
                .maxDurationOfBooking(randInt(50, 1000))
                .reservationBuffer(randInt(15, 30))
                .maxStartDayBooking(randInt(1, 10))
                .plannedUsageAvailability(randInt(50, 100))
                .immediateUsageCarAvailability(randInt(50, 100))
                .periodBeforeCantNoMoreUpdateStartDate(randInt(10, 30))
                .periodBeforeCantNoMoreUpdateEndDate(randInt(10, 30))
                .periodBeforeCantNoMoreCancel(randInt(10, 30))
                .carPriorReservation(randInt(15, 60))
                .automaticNoShowOneWay(randInt(0, 15))
                .automaticNoShowRoundTrip(randInt(15, 30))
                .name(PersonalInfoGenerator.generateOrcName() + " Gateway " + NumberGenerator.randInt(1, 99999))
                .address(buildRandomAddress())
                .subCompanyId(subCompanyId)
                .zoneId(new ArrayList<>(ZoneId.getAvailableZoneIds()).get(randInt(0, ZoneId.getAvailableZoneIds().size() - 1)))
                .automaticShortening(RandomUtils.nextBoolean())
                .automaticExtension(RandomUtils.nextBoolean())
                .chargeExpiredBooking(RandomUtils.nextBoolean())
                .smartcardFishingEnabled(RandomUtils.nextBoolean())
                .spontaneousBookingEnabled(RandomUtils.nextBoolean())
                .spontaneousBookingUsage(randomEnum(UsageType.class))
                .smartcardEnabled(RandomUtils.nextBoolean()).build();
    }

    public static ParkingCreateDto buildParkingCreate() {
        return ParkingCreateDto.builder()
                .additionalInformation(PersonalInfoGenerator.generateFullName() + " to the valley of shadow.")
                .address(buildRandomAddress())
                .alwaysOpen(true)
                .coordinates(buildRandomCoordinates())
                .disabledAccess(RandomUtils.nextBoolean())
                .electricCharging(RandomUtils.nextBoolean())
                .gsmConnection(RandomUtils.nextBoolean())
                .name(PersonalInfoGenerator.generateOrcName() + " Son of " + PersonalInfoGenerator.generateOrcName() + "the "
                        + randInt(0, 100))
                .privateAccess(RandomUtils.nextBoolean())
                .radius(NumberGenerator.randInt(1, 500000)).build();
    }


    public static ParkingDto buildExpectedParkingDto(ParkingCreateDto parkingCreateDto, SiteDto siteDto) {
        ParkingDto parkingDto = new ParkingDto();
        BeanUtils.copyProperties(parkingCreateDto, parkingDto);
        parkingDto.setSite(siteDto);
        parkingDto.setSiteId(siteDto.getId());
        return parkingDto;
    }


    public static VehicleCreate buildVehicleCreate(RcimTestData rcimTestData) {
        return VehicleCreate.builder()
                .vin(PersonalInfoGenerator.generateName(15) + NumberGenerator.randInt(10, 99))
                .registrationNumber("TEST-" + PersonalInfoGenerator.generateOrcName() + "-" + NumberGenerator.randInt(0, 100))
                .registrationDate(LocalDate.now().toString())
                .pictureUrl("http://www." + PersonalInfoGenerator.generateOrcName() + ".com/static/images/images-7_kACPBns.jpg")
                .transmissionType(randomEnum(TransmissionType.class))
                .fuelType(randomEnum(FuelType.class))
                .mileage(NumberGenerator.randInt(0, 999999))
                .seats(NumberGenerator.randInt(1, 4))
                .categoryId(rcimTestData.getCategoryDto().getId())
                .versionId(rcimTestData.getVehicleVersionDto().getId())
                .companyId(rcimTestData.getSuperCompanyDto().getId())
                .colorId(rcimTestData.getColorDto().getId())
                .serviceLevelType(randomEnum(ServiceLevelType.class))
                .accessories(getRandomAccessories())
                .systemType(randomEnum(SystemType.class))
                .device(buildRandomDevice())
                .ownedByRci(RandomUtils.nextBoolean())
                .doorsNumber(NumberGenerator.randInt(1, 5))
                .type(randomEnum(VehicleType.class))
                .lastPosition(new VehiclePositionDto(rcimTestData.getParkingDto(), Instant.now().toString()))
                .registrationDocumentId(rcimTestData.getFileDto().getId()).build();
    }

    private static DeviceDto buildRandomDevice() {
        return DeviceDto.builder()
                .serialNumber(PersonalInfoGenerator.generateOrcName() + randInt(100000, 999999))
                .installationDate(LocalDate.now().toString())
                .removalDate(LocalDate.now().plusMonths(NumberGenerator.randInt(1, 12)).toString()).build();
    }

    public static FileDto buildFile() {
        return FileDto.builder()
                .name(PersonalInfoGenerator.generateOrcName() + ".jpeg")
                .mimeType("image/jpeg")
                .fileType(randomEnum(FileType.class))
                .content("ZGFkYQ==").build();
    }

    public static VehicleDto buildExpectedVehicleDto(VehicleCreate vehicleCreate, RcimTestData rcimTestData) {
        return VehicleDto.builder()
                .vin(vehicleCreate.getVin())
                .registrationNumber(vehicleCreate.getRegistrationNumber())
                .registrationDate(vehicleCreate.getRegistrationDate())
                .pictureUrl(vehicleCreate.getPictureUrl())
                .transmissionType(vehicleCreate.getTransmissionType())
                .fuelType(vehicleCreate.getFuelType())
                .mileage(vehicleCreate.getMileage())
                .seats(vehicleCreate.getSeats())
                .version(rcimTestData.getVehicleVersionDto())
                .category(rcimTestData.getCategoryDto())
                .serviceLevelType(vehicleCreate.getServiceLevelType())
                .statusType(INACTIVE)//new vehicle is created with status inactive always
                .colorCode(rcimTestData.getColorDto().getCode())
                .color(rcimTestData.getColorDto())
                .lastPosition(vehicleCreate.getLastPosition())
                .accessories(vehicleCreate.getAccessories())
                .systemType(vehicleCreate.getSystemType())
                .device(vehicleCreate.getDevice())
                .ownedByRci(vehicleCreate.getOwnedByRci())
                .doorsNumber(vehicleCreate.getDoorsNumber())
                .type(vehicleCreate.getType())
                .company(new CompanyId(rcimTestData.getSuperCompanyDto().getId()))
                .registrationDocumentId(rcimTestData.getFileDto().getId()).build();
    }

    public static SuperCompanyConfigurationEditDto buildSuperCompanyConfiguration (){
       return SuperCompanyConfigurationEditDto.builder()
        .name(PersonalInfoGenerator.generateName(20))
        .vatCode(PersonalInfoGenerator.generateOrcName())
        .vatRate(NumberGenerator.randFloat(0,1))
        .allowsGroups(true).build();

    }


    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        SecureRandom random = new SecureRandom();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static AutolibCardDto buildAutolibCard(int plusMonthStart, int plusMonthEnd) {
        return AutolibCardDto.builder()
                .cardNumber(NumberGenerator.randInt(10000, 99999) + PersonalInfoGenerator.generateName(4))
                .ownerName(PersonalInfoGenerator.generateOrcName())
                .pinCode(NumberGenerator.randInt(10000, 99999) + PersonalInfoGenerator.generateName(4))
                .startDate(LocalDate.now().plusMonths(plusMonthStart).toString())
                .endDate(LocalDate.now().plusMonths(plusMonthEnd).toString())
                .expirationDate(LocalDate.now().plusMonths(plusMonthEnd).toString()).build();
    }

    public static FuelCardDto buildFuelCard(int plusMonthStart, int plusMonthEnd) {
        return FuelCardDto.builder()
                .cardNumber(NumberGenerator.randInt(10000, 99999) + PersonalInfoGenerator.generateName(4))
                .pinCode(NumberGenerator.randInt(10000, 99999) + PersonalInfoGenerator.generateName(4))
                .startDate(LocalDate.now().plusMonths(plusMonthStart).toString())
                .endDate(LocalDate.now().plusMonths(plusMonthEnd).toString())
                .expirationDate(LocalDate.now().plusMonths(plusMonthEnd).toString())
                .wash(RandomUtils.nextBoolean())
                .toll(RandomUtils.nextBoolean()).build();
    }

    public static InsuranceContractDto buildInsuranceContract(int plusMonthStart, int plusMonthEnd, UUID fileId) {
        Set<String> options = new HashSet<>();
        options.add(PersonalInfoGenerator.generateName(5));
        options.add(PersonalInfoGenerator.generateName(5));
        return InsuranceContractDto.builder()
                .startDate(LocalDate.now().plusMonths(plusMonthStart).toString())
                .expirationDate(LocalDate.now().plusMonths(plusMonthEnd).toString())
                .glassBreakage(RandomUtils.nextBoolean())
                .options(options)
                .contractNumber(NumberGenerator.randInt(10000, 99999) + PersonalInfoGenerator.generateName(4))
                .types(Collections.singletonList(randomEnum(InsuranceContractType.class)))
                .uploadedFileId(fileId).build();
    }

    public static LeaseContractDto buildLeaseContract(int plusMonthStart, int plusMonthEnd) {
        return LeaseContractDto.builder()
                .startDate(LocalDate.now().plusMonths(plusMonthStart).toString())
                .contractNumber(NumberGenerator.randInt(10000, 99999) + PersonalInfoGenerator.generateName(4))
                .endDate(LocalDate.now().plusMonths(plusMonthEnd).toString())
                .actualEndDate(LocalDate.now().plusMonths(plusMonthEnd).toString())
                .serviceLevel(PersonalInfoGenerator.generateName(5))
                .totalKm(NumberGenerator.randInt(0, 999999))
                .amountExVat(NumberGenerator.randInt(0, 100)).build();
    }

    public static FilteredSearchDto buildFilteredSearch(String memberLogin, String siteId) {
        return FilteredSearchDto.builder()
                .memberLogin(memberLogin)
                .passengers(NumberGenerator.randInt(1, 4))
                .startDate(LocalDateTime.now().plusMinutes(10).withSecond(0).withNano(0).toString())
                .endDate(LocalDateTime.now().plusMinutes(70).withSecond(0).withNano(0).toString())
                .startSiteId(siteId)
                .endSiteId(siteId).build();
    }

    public static SearchBookingResultDto buildExpectedSearchBookingResult(FilteredSearchDto filteredSearchDto,
                                                                          ParkingDto parkingDto, VehicleDto vehicleDto,
                                                                          UsageType usageType) {
        return SearchBookingResultDto.builder()
                .type(BookingType.CAR_SHARING)
                .start(DateLocationDto.builder()
                        .date(filteredSearchDto.getStartDate() + ":00" + ZoneId.of(parkingDto.getSite().getZoneId())
                                .getRules().getOffset(Instant.now()).getId())
                        .address(parkingDto.getSite().getAddress())
                        .coordinates(parkingDto.getCoordinates())
                        .parking(parkingDto).build())
                .end(DateLocationDto.builder()
                        .date(filteredSearchDto.getEndDate() + ":00" + ZoneId.of(parkingDto.getSite().getZoneId())
                                .getRules().getOffset(Instant.now()).getId())
                        .address(parkingDto.getSite().getAddress())
                        .coordinates(parkingDto.getCoordinates())
                        .parking(parkingDto).build())
                .vehicle(vehicleDto)
                .reservedSeats(filteredSearchDto.getPassengers())
                .carSharingInfo(CarSharingInfoDto.builder()
                        .usageType(usageType).build()).build();
    }

    public static BookingCreateDto buildCreateBooking(String memberLogin, VehicleDto vehicleDto, ParkingDto parkingDto) {
        return BookingCreateDto.builder()
                .carSharingInfo(CarSharingInfoDto.builder()
                        .usageType(UsageType.BUSINESS)
                        .freeOfCharges(false).build())
                .memberLogin(memberLogin)
                .reservedSeats(randInt(1, 4))
                .type(BookingType.CAR_SHARING)
                .vehicle(VehicleDto.builder()
                        .id(vehicleDto.getId()).build())
                .start(DateLocationDto.builder()
                        .date(LocalDateTime.now().plusMinutes(10).withNano(0).toString() +
                                ZoneId.of(parkingDto.getSite().getZoneId()).getRules().getOffset(Instant.now()).getId())
                        .address(parkingDto.getSite().getAddress())
                        .coordinates(parkingDto.getCoordinates())
                        .parking(parkingDto).build())
                .end(DateLocationDto.builder()
                        .date(LocalDateTime.now().minusHours(1).plusMinutes(10).plusHours(3).withNano(0) + ZoneId.of(parkingDto.getSite().getZoneId())
                                .getRules().getOffset(Instant.now()).getId())
                        .address(parkingDto.getSite().getAddress())
                        .coordinates(parkingDto.getCoordinates())
                        .parking(parkingDto).build()).build();
    }

    public static BookingDto buildExpectedBooking(BookingCreateDto bookingCreateDto, VehicleDto vehicleDto) {
        return BookingDto.builder()
                .type(bookingCreateDto.getType())
                .start(bookingCreateDto.getStart())
                .end(bookingCreateDto.getEnd())
                .vehicle(vehicleDto)
                .status(BookingStatusType.SCHEDULED)//when creating booking SCHEDULED is default
                .state(BookingState.UPCOMING)//when creating booking UPCOMING is default
                .reservedSeats(bookingCreateDto.getReservedSeats())
                .paymentSettlingAllowed(false)
                .carSharingInfo(CarSharingInfoDto.builder()
                        .statusPayment(StatusPayment.NA)//When creating car sharing booking status payment N/A is default
                        .delayed(false)
                        .failed(false)
                        .freeOfCharges(false)
                        .openToRideSharing(false)
                        .usageType(bookingCreateDto.getCarSharingInfo().getUsageType()).build()).build();
    }


    public static DamageReportCreateDto buildDamageReportCreate(DamageReportType damageReportType, List<UUID> fileIds) {
        Set<DamageDto> damageDtos = new HashSet<>();
        DamageReportCreateDto damageReportCreateDto = DamageReportCreateDto.builder()
                .type(damageReportType)
                .externalCleanliness(randInt(1, 5))
                .internalCleanliness(randInt(1, 5))
                .cleanlinessComment(PersonalInfoGenerator.generateFullName() + " believes it is not acceptable.")
                .reports(damageDtos).build();

        for (UUID fileId : fileIds) {
            DamageDto damageDto = DamageDto.builder()
                    .area(randomEnum(DamageArea.class))
                    .type(randomEnum(DamageType.class))
                    .comment(PersonalInfoGenerator.generateFullName() + " omg omg omg!!!")
                    .fileId(fileId).build();
            damageReportCreateDto.getReports().add(damageDto);
        }
        damageReportCreateDto.setReports(damageDtos);
        return damageReportCreateDto;
    }

    public static DamageReportDetailsDto buildExpectedDamageReportDetails(DamageReportCreateDto damageReportCreateDto, String vehicleId) {
        for (DamageDto damageDto : damageReportCreateDto.getReports()) {
            damageDto.setVehicleId(vehicleId);
            damageDto.setCurrentStatus(DamageStatus.ACTIVE);
        }

        return DamageReportDetailsDto.builder()
                .cleanlinessComment(damageReportCreateDto.getCleanlinessComment())
                .externalCleanliness(damageReportCreateDto.getExternalCleanliness())
                .internalCleanliness(damageReportCreateDto.getInternalCleanliness())
                .type(damageReportCreateDto.getType())
                .status(DamageReportStatus.NEW)//by default when created status is NEW
                .reports(damageReportCreateDto.getReports())
                .reportRemarks(new HashSet<>())
                .build();
    }

    public static DamageDetailsDto buildExpectedDamageDetails(DamageDto damageDto, UUID bookingId, VehicleDto vehicleDto) {
        return DamageDetailsDto.builder()
                .area(damageDto.getArea())
                .type(damageDto.getType())
                .fileId(damageDto.getFileId())
                .bookingId(bookingId)
                .vehicle(DamageVehicleDto.builder()
                        .id(vehicleDto.getId())
                        .brand(vehicleDto.getVersion().getModel().getBrand().getName())
                        .model(vehicleDto.getVersion().getModel().getName())
                        .version(vehicleDto.getVersion().getName())
                        .registrationNumber(vehicleDto.getRegistrationNumber()).build())
                .currentStatus(DamageStatusDto.builder().type(DamageStatus.ACTIVE).build())
                .statuses(new HashSet<>(Collections.singletonList(DamageStatusDto.builder()
                        .type(DamageStatus.ACTIVE).build()))).build();
    }

    public static DamageSearchDto buildExpectedDamageResult(RcimTestData rcimTestData, DamageReportContextDto damageReportContextDto) {
        return DamageSearchDto.builder()
                .companyId(rcimTestData.getBookingSuperCompanyId())
                .bookingId(damageReportContextDto.getBooking().getId().toString())
                .status(DamageStatus.ACTIVE)//new created damage will always have ACTIVE status
                .vehicleBrandModel(rcimTestData.getAutomationVehicle().getVersion().getModel().getBrand().getName() + " "
                        + rcimTestData.getAutomationVehicle().getVersion().getModel().getName())
                .vehiclePlateNumber(rcimTestData.getAutomationVehicle().getRegistrationNumber())
                .fileId(damageReportContextDto.getStartDamageReport().getReports().iterator().next().getFileId())
                .creationDate(damageReportContextDto.getStartDamageReport().getReports().iterator().next().getCreatedDate())
                .vehicleId(rcimTestData.getAutomationVehicleId())
                .area(damageReportContextDto.getStartDamageReport().getReports().iterator().next().getArea())
                .type(damageReportContextDto.getStartDamageReport().getReports().iterator().next().getType())
                .build();
    }
    
    /**
     * This method is used to create a CompanyCreateDto object with valid data
     *@param login email of the existing member
     * @param superCompanyId value which you want the object field with same name to have
     * @return SmartCardCreateDto object with all fields having valid values
     */
    public static SmartCardCreateDto buildSmartCardCreate(String superCompanyId,String login) {
        return SmartCardCreateDto.builder().protocol(Protocol.ISO14443A_4).companyId(superCompanyId)
        		.userLogin(login).
        		cardId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16))
        		.build();

    }
    /**
     * This method is used to create a CompanyCreateDto object with valid data
     *@param login email of the existing member
     * @param superCompanyId value which you want the object field with same name to have
     * @return SmartCardCreateDto object with all fields having valid values
     */
    public static SmartCardCreateDto buildSmartCardCreateMissingParameter(String superCompanyId,String login,String protocol,
    		String cardId) {
    		String cardIDnew= new String();
    		Protocol protocolNew;
    		if(cardId==null) {
    			cardIDnew=null;
    		}
    		else
    		{
    			cardIDnew=UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    		}
    		
    		
    		if(protocol==null) {
    			protocolNew=null;
    		}
    		else
    		{
    			protocolNew =Protocol.ISO14443A_4;
    		}
    		
    		
    	
        return SmartCardCreateDto.builder().protocol(protocolNew).companyId(superCompanyId)
        		.userLogin(login).
        		cardId(cardIDnew)
        		.build();

    }
    
    /**
     * This method builds a SmartCardDto with given property
     *
     * @param smartcardDto this should be SmartCardDto object
     * @param superCompanyId  this should be string of the given super company 
     * @param login this should be string and is should an email.
     * @return CompanyDto object with properties copied from CompanyCreateDto
     */
    public static SmartCardDto buildExpectedCreateSmartDto(SmartCardDto smartcardDto, String superCompanyId,String login) {
    		SmartCardDto smartCardDtoTest = new SmartCardDto();
        BeanUtils.copyProperties(smartcardDto, smartCardDtoTest);
        com.pitechplus.rcim.backoffice.dto.smartcard.MemberDto  memberdto=smartCardDtoTest.getUser();
        memberdto.setLogin(login);
        com.pitechplus.rcim.backoffice.dto.smartcard.CompanyDto y=smartCardDtoTest.getCompany();
        y.setId(superCompanyId);
        smartCardDtoTest.setUser(memberdto);
        smartCardDtoTest.setCompany(y);
       return smartCardDtoTest;
    }


}
