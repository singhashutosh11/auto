package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.FacilityPage;
import com.bravadohealth.clara1.pageObjects.GlobalNavContainer;
import com.bravadohealth.clara1.pageObjects.HomeViewPage;
import com.bravadohealth.clara1.pageObjects.ManagePatientPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class AutoLogout extends AbstractSeleniumTest{

	private static final String	IDLE_LOGOUT_TIME	= "checkAuditLog";
	private static final String	RESET_TIMER				= "resetTime";
	private static final String	ACTION				= "action";
	private static final String	CONTINUE			= "Continue";
	private static final String	SIGNOUT				= "Signout";
	private static final String	YES				= "yes";
	private static final String	CHECK_AUDIT_LOG	= "checkAuditLog";
	private static final String CONFIG_FILE = "/clara_scenarios/clara_user_security/auto_logout.xls";
	
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);

		File f = new File(fileUrl.getFile());

		try {
			return URLDecoder.decode(f.getAbsolutePath(), "UTF-8") + ";logout";
		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		//AbstractPage abstractPage;
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		applicationPage.navigateToClaraPage()
		.logIn()
		.selectFacility()
		.moveToAccountPage()
		.moveToFacilityPage()
		.clickOnEditFacilitySettings()
		.editFacilitySettingsDetails()
		.verifyModifiedFacilitySettingsDetails();

		if (YES.equals(getParameter(RESET_TIMER))) {
			new FacilityPage(this, pdp).resetLogoutTimeOnAnyAction();
		}
		new FacilityPage(this, pdp).verifyIdleLogoutTimeModalOpen();


		if (CONTINUE.equals(getParameter(ACTION))) {
			new FacilityPage(this, pdp).clickOnContinueToRemainLoggedIn();
		}
		
		if (SIGNOUT.equals(getParameter(ACTION))) {
			new FacilityPage(this, pdp).signoutOnClickOfLogoutModalSignout();			
		}

		if(!CONTINUE.equals(getParameter(ACTION)) && !SIGNOUT.equals(getParameter(ACTION))) {
			new FacilityPage(this, pdp).autoSignOutAfterModalOpen();
		}
		
		if(YES.equals(getParameter(CHECK_AUDIT_LOG))) {
			applicationPage.navigateToClaraPage()
			.logIn()
			.selectFacility();
			
			new GlobalNavContainer(this, pdp)
			.moveGlobalNavContainerIntoView()
			.moveToAuditTrails();

			new AuditLogPage(this, pdp)
			.viewActivityLogAfterAutoLogout()
			.logout();
		}
	}
}
