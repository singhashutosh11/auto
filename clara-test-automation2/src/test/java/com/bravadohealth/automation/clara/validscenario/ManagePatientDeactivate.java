package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.clara1.pageObjects.AddPatientPage;
import com.bravadohealth.clara1.pageObjects.ManagePatientPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class ManagePatientDeactivate extends AbstractSeleniumTest{

	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	@Override
	public String getExcelPath() {
		File f = new File("ClaraValidScenarios.xls");
		return "" + f.getAbsolutePath() + ";managePatient";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		applicationPage.navigateToClaraPage()
			.logIn()
			.selectFacility()
			.ClickPatient();
		if (!YES.equals(getParameter(NEGETIVE_SCENARIO_CHECK))) {
			new ManagePatientPage(this, pdp).selectPatientToDeactivate();
		} 
		else {
			
		}
		
	}

}
