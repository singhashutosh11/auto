package com.bravadohealth.automation.clara.invalidscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.GlobalNavContainer;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class UpdateFacility extends AbstractSeleniumTest{

	private static final String CONFIG_FILE = "/clara_scenarios/clara_facility/update_facility.xls";

	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";editFacility";
		} catch (UnsupportedEncodingException e) {
			
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		applicationPage.navigateToClaraPage()
		.logIn()
		.selectFacility()
		.moveToAccountPage()
		.moveToFacilityPage()
		.clickOnEditFacility()
		.editFacilityDetailsNegativeScenario()
		.cancelEditFacilityDetailsIfNeeded()
		.clickOnEditFacilitySettings()
		.editFacilitySettingsNegativeScenario()
		.cancelEditFacilitySettingsDetailsIfNeeded();
		
		new GlobalNavContainer(this, pdp)
		.moveGlobalNavContainerIntoView()
		.logout();		
		
	}

}
