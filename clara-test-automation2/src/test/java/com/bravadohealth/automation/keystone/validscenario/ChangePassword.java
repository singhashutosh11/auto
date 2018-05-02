package com.bravadohealth.automation.keystone.validscenario;

import java.io.File;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.keystone.pageObjects.LogInPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;

public class ChangePassword extends AbstractSeleniumTest{
	@Override
	public String getExcelPath() {
		File f=new File("credential.xls");
		return  ""+f.getAbsolutePath()+";changePassword";
	}


	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToKSHomePage()
		.logIn()
		.verifyUserDetails()
		.changePassWord()
		.changeMobileNumber();
	}





}
