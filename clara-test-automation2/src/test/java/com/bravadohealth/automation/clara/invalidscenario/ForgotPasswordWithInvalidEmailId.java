package com.bravadohealth.automation.clara.invalidscenario;

import java.io.File;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;

public class ForgotPasswordWithInvalidEmailId extends AbstractSeleniumTest{

	@Override
	public String getExcelPath() {
		File f=new File("ClaraInvalidScenario.xls");
		return  ""+f.getAbsolutePath()+";forgotPassword";
	}


	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToClaraPage()
		.forgotCredential()
		.verifyInvalidEmailAddressInClaraApp();
	}
}
