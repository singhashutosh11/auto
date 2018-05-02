package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class SetUpFacility extends AbstractSeleniumTest {
	private static final String NEGETIVE_SCENARIO_CHECK = "negetiveScenarioCheck";
	private static final String YES = "Yes";

	@Override
	public String getExcelPath() {
		File f = new File("ClaraValidScenarios.xls");
		return "" + f.getAbsolutePath() + ";meetClara";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		AbstractPage abstractPage = applicationPage.navigateToClaraPage()
				.logIn()
				.navigateToCreateFacility()
				.FillInFacilityDetails()
				.chooselicenseType()
				.verifyIdentity()
				.validateSuccessIDVerification()
				.continueToDone()
				.postCompletionAction();

	}

}
