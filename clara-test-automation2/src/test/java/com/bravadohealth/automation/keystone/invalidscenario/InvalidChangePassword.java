package com.bravadohealth.automation.keystone.invalidscenario;

import java.io.File;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class InvalidChangePassword extends AbstractSeleniumTest {

	
	public String getExcelPath() {
		File f=new File("keyStoneInvalidScenario.xls");
		return  ""+f.getAbsolutePath()+";invalidChangePassword";
	}


	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToKSHomePage()
		.logIn()
		.verifyUserDetails()
		.InvalidchangePassWord();
	}


}
