package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.AuthorizePrescriptionsPage;
import com.bravadohealth.clara1.pageObjects.DuplicatePatientPage;
import com.bravadohealth.clara1.pageObjects.HomeViewPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.PrescriptionResultPage;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class VerifyPrescriptionInteraction extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/clara_prescription/verify_prescription_interaction.xls";
	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";drugInteractions";
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
		AbstractPage abstractPage2 = null;
		if(abstractPage instanceof PatientMedicalHistory) {
			PatientMedicalHistory pmp = (PatientMedicalHistory) abstractPage;
			abstractPage2 = pmp.addMedicalHistory()
								.moveToWritePrescriptionsPage();

			if(abstractPage2 instanceof WritePrescriptionsPage) {
				WritePrescriptionsPage wpp = (WritePrescriptionsPage) abstractPage2;
				AbstractPage abstractPage3 = wpp.checkPediatricAndUpdateWeight()
											.addPrescriptions()
											.editFullPatient();
				if(!(abstractPage3 instanceof DuplicatePatientPage)) {
					AbstractPage abstractPage4 = wpp.addAlternatePrescriptions()
													.verifyPrescriptions();
					
					if(abstractPage4 instanceof PrescriptionResultPage) {
						PrescriptionResultPage prp = (PrescriptionResultPage) abstractPage4;
						prp.waitUntilPrescriptionResultPageisLoaded()
						.moveToAuditTrailPage()
						.searchAuditTrailForDrugInteractions()
						.logout();
					}
				}
			}
		}
	}	

}
