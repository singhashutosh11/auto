package com.bravadohealth.automation.clara.invalidscenario;

import java.io.File;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;

public class AddNewPatientNegetiveScenario extends AbstractSeleniumTest {

	@Override
	public String getExcelPath() {
		File f = new File("ClaraValidScenarios.xls");
		return "" + f.getAbsolutePath() + ";addPatient";
	}

	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToClaraPage()
		.logIn()
		.selectFacility()
		.addPatient();
		//.addPatientDetailsScenario();
	}
}
