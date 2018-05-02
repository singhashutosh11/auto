package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class EditPatientProfile extends AbstractSeleniumTest {

	@Override
	public String getExcelPath() {
		File f=new File("ClaraValidScenarios.xls");
		return  ""+f.getAbsolutePath()+";writePrescription";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		AbstractPage abstractPage = applicationPage.navigateToClaraPage()
				.logIn()
				.selectFacility()
				.addPatient()
				.addPatientDetails()
				.navigateToMedicalHistoryPage();
		new PatientMedicalHistory(this, pdp).addMedicalHistory()
		.clickOnEditPatient()
		.editPatientProfile();

	}

}
