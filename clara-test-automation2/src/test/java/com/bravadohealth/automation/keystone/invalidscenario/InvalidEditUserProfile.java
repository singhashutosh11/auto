package com.bravadohealth.automation.keystone.invalidscenario;

import java.io.File;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;

public class InvalidEditUserProfile  extends AbstractSeleniumTest{

	@Override
	public String getExcelPath() {
		File f=new File("keyStoneInvalidScenario.xls");
		return  ""+f.getAbsolutePath()+";editUser";
	}


	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToKSHomePage()
		.logIn()
		.verifyUserDetails()
		.navigateToUserProfile()
		.invalidEditUserField();
	}

}
