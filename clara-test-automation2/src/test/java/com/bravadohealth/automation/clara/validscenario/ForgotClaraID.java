package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.recoverClaraIdPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class ForgotClaraID extends AbstractSeleniumTest {

	private static final String NEGETIVE_SCENARIO_CHECK = "negetiveScenarioCheck";
	private static final String YES = "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/clara_user_security/forgot_clara_id.xls";

	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
	
		File f = new File(fileUrl.getFile());
	
		try {
			return URLDecoder.decode(f.getAbsolutePath(), "UTF-8") + ";forgotClaraId";
		} catch (UnsupportedEncodingException e) {
	
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		applicationPage.navigateToClaraPage().forgotCredential().forgotClaraId();
		if (!YES.equals(getParameter(NEGETIVE_SCENARIO_CHECK))) {
			new recoverClaraIdPage(this, pdp).findClaraId();
		} 
		else {
			// verifyInvalidEmailAddressInClaraApp(pdp);
		}
	}

}
