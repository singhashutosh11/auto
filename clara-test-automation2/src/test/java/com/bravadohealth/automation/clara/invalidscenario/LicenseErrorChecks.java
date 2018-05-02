package com.bravadohealth.automation.clara.invalidscenario;

import java.io.File;

import com.bravadohealth.clara1.pageObjects.IdVerificationPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class LicenseErrorChecks extends AbstractSeleniumTest{
	
	@Override
	public String getExcelPath() {
		File f=new File("ClaraInvalidScenario.xls");
		return  ""+f.getAbsolutePath()+";meetClara";
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
				.provideFacilityDetails()
				.continueAfterLicenseSelected();
				if(abstractPage instanceof IdVerificationPage) {
					IdVerificationPage idVerificationPage = (IdVerificationPage) abstractPage;
					abstractPage = idVerificationPage.verifyIdentity()
									.validateErrorMessage();
				}
	}

}
