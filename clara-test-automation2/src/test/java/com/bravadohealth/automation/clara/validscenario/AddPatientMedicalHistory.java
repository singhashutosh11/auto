package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.clara1.pageObjects.AddPatientPage;
import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class AddPatientMedicalHistory extends AbstractSeleniumTest {

	private static final String NEGETIVE_SCENARIO_CHECK = "negetiveScenarioCheck";
	private static final String YES = "Yes";

	@Override
	public String getExcelPath() {
		File f = new File("ClaraValidScenarios.xls");
		return "" + f.getAbsolutePath() + ";addPatient";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		AbstractPage abstractPage=applicationPage.navigateToClaraPage()
				.logIn()
				.selectFacility()
				.addPatient()
				.addPatientDetails()
				.navigateToMedicalHistoryPage();
		if(abstractPage instanceof SelectPharmacyPage ) {
			abstractPage=new SelectPharmacyPage(this, pdp).selectPharmacy()
			.submitPharmacySearch();
		}
		if(abstractPage instanceof PatientMedicalHistory) {
			abstractPage=new PatientMedicalHistory(this, pdp).addMedicalHistory()
					.viewAddedDetails();
		} 
		new AuditLogPage(this, pdp).viewRecentActivityLog();
	}
}