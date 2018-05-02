package com.bravadohealth.automation.clara.invalidscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class ErrorValidationDuringFacilitySetup extends AbstractSeleniumTest{
	
	private static final String CONFIG_FILE = "/clara_scenarios/meet_clara/facilityCreation_error_scenarios.xls";

	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);

		File f = new File(fileUrl.getFile());

		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";meetClara";
		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		AbstractPage abstractPage = applicationPage.navigateToClaraPage()
				.moveToSelectPlanPage()
				.selectPlan()
				.selectNoOfUser()
				.checkOut()
				.createClaraId()
				.ConfirmOrder()
				.getStarted()
				.providerFacilityDetailsWithSameNominators();

	}

}
