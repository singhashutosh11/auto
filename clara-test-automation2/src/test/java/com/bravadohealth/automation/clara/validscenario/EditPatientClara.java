package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.AddPatientPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class EditPatientClara extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/clara_patient/edit_patient_clara.xls";
	
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";editPatient";
		} catch (UnsupportedEncodingException e) {
			
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		AbstractPage abstractPage= applicationPage.navigateToClaraPage()
				.logIn()
				.selectFacility()
				.addPatient()
				.addPatientDetails()
				.navigateToMedicalHistoryPage();
		if(abstractPage instanceof SelectPharmacyPage ) {
			new SelectPharmacyPage(this, pdp).selectPharmacy()
			.submitPharmacySearch();
		}
		if(abstractPage instanceof PatientMedicalHistory) {
			abstractPage=new PatientMedicalHistory(this, pdp).addMedicalHistory()
					.clickOnEditPatient()
					.editPatientProfile()
					.editAllergy()
					.editHomeMedication()
					.editDiagnosis()
					.viewAddedDetails();
		}
	} 	

}
