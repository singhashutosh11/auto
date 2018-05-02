package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class ValidPatientSearch extends AbstractSeleniumTest {

	@Override
	public String getExcelPath() {
		File f = new File("ClaraValidScenarios.xls");
		return "" + f.getAbsolutePath() + ";searchPatient";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		applicationPage.navigateToClaraPage().logIn()
		.selectFacility()
		.searchPatient();
	}
}