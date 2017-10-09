package com.pitechplus.rcim.backoffice.data;

import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.backoffice.dto.backuser.BackUser;
import com.pitechplus.rcim.backoffice.dto.backuser.Login;
import com.pitechplus.rcim.backoffice.dto.common.FileDto;
import com.pitechplus.rcim.backoffice.dto.company.CompanyDto;
import com.pitechplus.rcim.backoffice.dto.company.ParkingDto;
import com.pitechplus.rcim.backoffice.dto.company.SiteDto;
import com.pitechplus.rcim.backoffice.dto.supercompany.SuperCompanyDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.CategoryDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.ColorDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleDto;
import com.pitechplus.rcim.backoffice.dto.vehicle.VehicleVersionDto;
import com.pitechplus.rcim.backoffice.service.*;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberCreateDto;
import com.pitechplus.rcim.nissan.be.nissandto.members.MemberDto;
import com.pitechplus.rcim.nissan.be.nissanservices.NissanBeServices;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanDtoBuilders;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

import static com.pitechplus.rcim.backoffice.utils.DataExtractors.extractXAuthTokenFromResponse;

/**
 * Created by dgliga on 31.05.2017.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Component("testData")
public class RcimTestData {


    @Autowired
    protected NissanBeServices nissanBeServices;

    @Autowired
    protected BackUserService backUserService;

    @Autowired
    protected CompanyService companyService;

    @Autowired
    protected ConfigsService configsService;

    @Autowired
    protected ParkingService parkingService;

    @Autowired
    protected SiteService siteService;

    @Autowired
    protected VehicleService vehicleService;

    @Autowired
    protected MemberService memberService;


    @Value("${bo.admin.username}")
    String boAdminUsername;

    @Value("${bo.admin.password}")
    String boAdminPassword;

    @Value("${bo.super.admin.username}")
    String boSuperAdminUsername;

    @Value("${bo.super.admin.password}")
    String boSuperAdminPassword;

    @Value("${super.company.booking.id}")
    String bookingSuperCompanyId;

    @Value("${automation.site.id}")
    String automationSiteId;

    @Value("${automation.vehicle.id}")
    String automationVehicleId;

    @Value("${automation.parking.id}")
    String automationParkingId;


    String superAdminToken;
    UUID configurationId;
    String groupTemplate;
    UUID addedSuperCompanyId;
    String adminToken;
    UUID adminSuperCompanyId;
    UUID adminCompanyId;
    SuperCompanyDto superCompanyDto;
    CompanyDto companyDto;
    SiteDto siteDto;
    ParkingDto parkingDto;
    CategoryDto categoryDto;
    VehicleVersionDto vehicleVersionDto;
    ColorDto colorDto;
    FileDto fileDto;
    MemberDto memberDto;
    VehicleDto automationVehicle;
    ParkingDto automationParking;
    String memberPassword;

    @PostConstruct
    public void initRcimTestData() throws Exception {
        ResponseEntity<BackUser> adminLogin = backUserService.authUser(new Login(boAdminUsername, boAdminPassword));
        this.adminToken = extractXAuthTokenFromResponse(adminLogin);
        this.superAdminToken = extractXAuthTokenFromResponse(backUserService.authUser(new Login(boSuperAdminUsername, boSuperAdminPassword)));
        List<String> templates = configsService.getTemplates(this.adminToken);
        this.groupTemplate = templates.get(NumberGenerator.randInt(0, templates.size() - 1));

        this.configurationId = companyService.createSuperCompanyConfiguration(this.superAdminToken,
                DtoBuilders.buildSuperCompanyConfiguration()).getBody().getId();
        this.adminSuperCompanyId = adminLogin.getBody().getCompany().getId();
        this.adminCompanyId = adminLogin.getBody().getCompany().getId();
        List<CategoryDto> categoryDtos = configsService.getCategories(this.superAdminToken);
        this.categoryDto = categoryDtos.get(NumberGenerator.randInt(0, categoryDtos.size() - 1));
        List<VehicleVersionDto> vehicleVersionDtos = configsService.getVersions(this.superAdminToken);
        this.vehicleVersionDto = vehicleVersionDtos.get(NumberGenerator.randInt(0, vehicleVersionDtos.size() - 1));
        List<ColorDto> colorDtos = configsService.getColors(this.superAdminToken);
        this.colorDto = colorDtos.get(NumberGenerator.randInt(0, colorDtos.size() - 1));
        this.fileDto = configsService.createFile(this.superAdminToken, DtoBuilders.buildFile()).getBody();

        //create super company, with company, site and parking
        this.superCompanyDto = companyService.createSuperCompany(this.superAdminToken,
                DtoBuilders.buildSuperCompanyCreate(this.configurationId, this.groupTemplate)).getBody();
        this.companyDto = companyService.createCompany(this.superAdminToken, this.superCompanyDto.getId(),
                DtoBuilders.buildCompanyCreate(this.configurationId)).getBody();
        this.siteDto = siteService.createSite(this.superAdminToken, this.companyDto.getId(),
                DtoBuilders.buildSiteDto(this.companyDto.getId())).getBody();
        this.parkingDto = parkingService.createParking(this.superAdminToken, this.siteDto.getId(),
                DtoBuilders.buildParkingCreate()).getBody();
        this.memberDto = createEnabledMember();

        this.automationVehicle = vehicleService.getVehicle(this.getSuperAdminToken(),
                UUID.fromString(this.getAutomationVehicleId())).getBody();
        this.automationParking = parkingService.getParking(this.getSuperAdminToken(),
                UUID.fromString(this.getAutomationParkingId())).getBody();
    }


    public MemberDto createEnabledMember() {
        MemberCreateDto memberCreateDto = NissanDtoBuilders.buildMemberCreateDto(this.getBookingSuperCompanyId(),
                configsService.createFile(this.getSuperAdminToken(), DtoBuilders.buildFile()).getBody().getId());
        MemberDto member = nissanBeServices.createMember(this.getSuperAdminToken(), memberCreateDto).getBody();
        memberService.approveDocument(this.getSuperAdminToken(), member.getDrivingLicence().getId());
        memberService.approveMemberProfile(this.getSuperAdminToken(), member.getId());
        memberService.enableMember(this.getSuperAdminToken(), member.getLogin(), true);
        this.memberPassword = memberCreateDto.getPassword();
        return nissanBeServices.getMember(this.superAdminToken, member.getId()).getBody();
    }
}