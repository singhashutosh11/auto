package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.EditUserDetailsPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class CheckACLTextAndDescription extends AbstractSeleniumTest{

	private static final String CONFIG_FILE = "/clara_scenarios/clara_facility/check_acl_text_and_description.xls";
	
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";aclTextAndDescription";
		} catch (UnsupportedEncodingException e) {
			
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		
		EditUserDetailsPage editUserPage = applicationPage.navigateToClaraPage()
			.logIn()
			.selectFacility()
			.moveToAccountPage()
			.selectUsersTabUnderAccountPage()
			.selectAUserFromUserList()
			.switchToManagePermissionsTab();
		
		if (pdp.getPageData("institutionalPermissionGroups") == null) {
			editUserPage.verifyIfPrescribingPermissionsHasEPCSPermissions();
		} else {
			editUserPage.verifyPermissionGroups();
		}
	}
}
