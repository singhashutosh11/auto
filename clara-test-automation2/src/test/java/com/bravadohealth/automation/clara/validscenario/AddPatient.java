package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.AuthorizePrescriptionsPage;
import com.bravadohealth.clara1.pageObjects.DuplicatePatientPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class AddPatient extends AbstractSeleniumTest {

	private static final String CONFIG_FILE = "/clara_scenarios/clara_patient/add_patient.xls";
	
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";addPatient";
		} catch (UnsupportedEncodingException e) {
			
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		
		PatientMedicalHistory patientMedicalHistory = applicationPage.navigateToClaraPage()
				.logIn()
				.selectFacility()
				.addPatient()
				.addPatientDetails();
		
		// only for valid(non-negative) scenarios
		if(pdp.getPageData("negetiveScenarioCheck") == null)
		{
			AbstractPage abstractPage=patientMedicalHistory.navigateToMedicalHistoryPage();
	
			if(abstractPage instanceof SelectPharmacyPage ) {
				abstractPage=new SelectPharmacyPage(this, pdp).selectPharmacy()
				.submitPharmacySearch();
			}
			if(abstractPage instanceof PatientMedicalHistory) {
				abstractPage=new PatientMedicalHistory(this, pdp).addMedicalHistory()
				.clickOnEditPatient()
				.editPatientProfile()
				.editAllergy()
				.editHomeMedication()
				.editDiagnosis()
				.viewAddedDetails()
				.moveToWritePrescriptionsPage();
			}
			if(abstractPage instanceof WritePrescriptionsPage) {
				WritePrescriptionsPage wpp = (WritePrescriptionsPage) abstractPage;
				if(!(abstractPage instanceof DuplicatePatientPage)) {
					abstractPage = wpp.addPrescriptions() 
									  .verifyPrescriptions();
				}
				if(pdp.getPageData("AuditLogCheck") !=null)	
					new AuditLogPage(this, pdp).viewRecentActivityLog();
			}
		}
	}
}
