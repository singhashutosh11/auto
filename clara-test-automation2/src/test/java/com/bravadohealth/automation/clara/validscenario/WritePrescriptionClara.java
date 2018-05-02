package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.AuthorizePrescriptionsPage;
import com.bravadohealth.clara1.pageObjects.DuplicatePatientPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.PrescriptionResultPage;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.AuditLogFilter;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;

public class WritePrescriptionClara extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/clara_prescription/write_prescription_clara.xls";
	
	@Override
	public String getExcelPath() {

		URL fileUrl = getClass().getResource(CONFIG_FILE);
	
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";writePrescription";
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
			AbstractPage abstractPage2 = wpp.checkPediatricAndUpdateWeight()
					.editFullPatient();
			if(!(abstractPage2 instanceof DuplicatePatientPage)) {
				AbstractPage abstractPage3 = null;
				abstractPage3 = wpp.addPrescriptions() 
				.verifyPrescriptions();

				if(abstractPage3 instanceof AuthorizePrescriptionsPage) {
					AuthorizePrescriptionsPage app = (AuthorizePrescriptionsPage) abstractPage3;
					AbstractPage abstractPageIfEdit = app.navigateToAuthorizePrescriptionsPage()
														.goBackIfEditPrescriptions();
					if(abstractPageIfEdit instanceof WritePrescriptionsPage) {
						abstractPage3 = wpp.editPrescriptions()
						.verifyPrescriptions();
					}	
				}
				
				if(abstractPage3 instanceof PrescriptionResultPage && StringUtils.isNotBlank(getParameter("auditLogFilter"))) {
					PrescriptionResultPage prp = (PrescriptionResultPage) abstractPage3;
					prp.waitUntilPrescriptionResultPageisLoaded()
					.moveToAuditTrailPage()
					.verifyAuditTrailForSavedOrUpdatedPrescriptions()
					.logout();
				}
			}
		}
		
		//new AuditLogPage(this, pdp).viewRecentActivityLog();
	} 	

}
