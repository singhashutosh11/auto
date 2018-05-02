package com.bravadohealth.automation.keystone.invalidscenario;

import java.io.File;

import org.testng.annotations.Test;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;

public class InvalidLogIn extends AbstractSeleniumTest{
	
	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToKSHomePage()
		.InvalidlogIn();
	}

	@Override
	public String getExcelPath() {
		File f=new File("keyStoneInvalidScenario.xls");
		return  ""+f.getAbsolutePath()+";invalidLogInData";
	}
}
