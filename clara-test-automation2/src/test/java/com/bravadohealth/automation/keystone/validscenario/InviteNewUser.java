package com.bravadohealth.automation.keystone.validscenario;

import java.io.File;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;

public class InviteNewUser extends AbstractSeleniumTest {
	
	@Override
	public String getExcelPath() {
		File f=new File("credential.xls");
		return  ""+f.getAbsolutePath()+";inviteUser";
	}


	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToKSHomePage()
		.logIn()
		.verifyUserDetails()
		.navigateToAdminPage()
		.inviteUser();
	}

}
