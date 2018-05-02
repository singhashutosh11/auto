package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.AddPatientPage;
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

public class AuthorizePrescriptionClara extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/clara_prescription/authorize_prescription_clara.xls";
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
	
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";authorizePrescription";
		} catch (UnsupportedEncodingException e) {
			
			throw new RuntimeException(e);
		}
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
		if(abstractPage instanceof SelectPharmacyPage ) {
			abstractPage=new SelectPharmacyPage(this, pdp).selectPharmacy()
					.submitPharmacySearch();
		}
		if(abstractPage instanceof PatientMedicalHistory) {
			abstractPage=new PatientMedicalHistory(this, pdp).addMedicalHistory()
					/* .clickOnEditPatient()
        .editPatientProfile()
        .editAllergy()
        .editHomeMedication()
        .editDiagnosis()
        .viewAddedDetails()*/
					.moveToWritePrescriptionsPage();
		}

		if(abstractPage instanceof WritePrescriptionsPage) {
			WritePrescriptionsPage wpp = (WritePrescriptionsPage) abstractPage;
			AbstractPage abstractPage1 = wpp.checkPediatricAndUpdateWeight()
					.editFullPatient();
			if(!(abstractPage1 instanceof DuplicatePatientPage)) {
				AbstractPage abstractPage2 = wpp.addPrescriptions()
						.verifyPrescriptions();//--> bug in subs and dispense amount on clara-dev        
				//.moveToAuthorizePrescriptionsPage();

				if(abstractPage2 instanceof AuthorizePrescriptionsPage) {
					AuthorizePrescriptionsPage app = (AuthorizePrescriptionsPage) abstractPage2;
					AbstractPage abstractPageIfEdit = app.navigateToAuthorizePrescriptionsPage()
														.goBackIfEditPrescriptions();
					AbstractPage abstractPage3 = null;
					if(abstractPageIfEdit instanceof WritePrescriptionsPage) {
						wpp = (WritePrescriptionsPage) abstractPageIfEdit;
						abstractPage3 = wpp.editPrescriptions()
								.moveToAuthorizePrescriptionsPageAgain();                            
					} 
					if(abstractPage3 instanceof AuthorizePrescriptionsPage) {
						app = (AuthorizePrescriptionsPage) abstractPage3; 
						app.navigateToAuthorizePrescriptionsPage()
						.verifyDetails();//-->bug on clara-dev resetting subs to OK automatically when click on GO Back
					}
					app.authorizePrescriptions()
					.submitAuthorizedPrescriptions();   
				}
			/*	if(abstractPage2 instanceof PrintPage) {
					PrintPage PP=(PrintPage) abstractPage2;
					PP.verifyProviderDetails()
					.verifyPatientDetails()
					.verifyPrintedMedication();
				}*/
				//new AuditLogPage(this, pdp).viewRecentActivityLog();
			}
		}
	} 	

}
