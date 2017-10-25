package com.pitechplus.rcim;

import com.pitechplus.qautils.reports.ExtentReporterNG;
import com.pitechplus.qautils.testnglisteners.RetryAnalyzer;
import com.pitechplus.qautils.testnglisteners.TestLogListener;
import com.pitechplus.rcim.backoffice.data.RcimTestData;
import com.pitechplus.rcim.backoffice.service.*;
import com.pitechplus.rcim.mobile.service.MobileService;
import com.pitechplus.rcim.nissan.be.nissanservices.NissanBeServices;
import com.pitechplus.rcim.nissan.be.nissanutils.nissanbuilders.NissanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

/**
 * @author dgliga on 14.05.2015.
 */
@Component("rcim-backoffice-abstract")
@Listeners({ExtentReporterNG.class, TestLogListener.class})
@ContextConfiguration(locations = {"classpath:conf/rcim-automation-config.xml"})
public abstract class BackendAbstract extends AbstractTestNGSpringContextTests {

    @Value("${bo.super.admin.username}")
    protected String boSuperAdminUsername;

    @Value("${bo.super.admin.password}")
    protected String boSuperAdminPassword;

    @Autowired
    protected NissanBeServices nissanBeServices;

    @Autowired
    protected BackUserService backUserService;

    @Autowired
    protected BookingService bookingService;

    @Autowired
    protected CompanyService companyService;

    @Autowired
    protected ConfigsService configsService;

    @Autowired
    protected ParkingService parkingService;

    @Autowired
    protected ReportsService reportsService;

    @Autowired
    protected SiteService siteService;

    @Autowired
    protected VehicleService vehicleService;

    @Autowired
    protected MobileService mobileService;

    @Autowired
    protected RcimTestData rcimTestData;

    @Autowired
    protected NissanUtils nissanUtils;
    
    @Autowired
    protected SmartCardService smartCardService;
    
    @Autowired
    protected ColorService colorService;
    
    @Autowired
    protected VoucherService voucherService;
    
    

    @BeforeTest(alwaysRun = true)
    public void retryAnalyzerSetup(ITestContext context) throws Exception {
        RetryAnalyzer.applyRetryOnTestMethodFailure(context, 0);
    }
}
