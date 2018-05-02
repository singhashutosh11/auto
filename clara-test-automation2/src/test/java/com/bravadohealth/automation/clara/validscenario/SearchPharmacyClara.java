package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import org.testng.annotations.DataProvider;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.AuthorizePrescriptionsPage;
import com.bravadohealth.clara1.pageObjects.DuplicatePatientPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;
import com.bravadohealth.pagedataset.Patient;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.DemographicLocators;
import com.bravadohealth.clara1.pageObjects.locators.NavLocators;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.keystone.pageObjects.HomePage;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.ManageFacilityDetails;
import com.bravadohealth.pagedataset.PatientDetails;

import trial.keyStone.automation.AuditAction;

public class SearchPharmacyClara extends AbstractSeleniumTest{
	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	@Override
	public String getExcelPath() {
		File f=new File("ClaraValidScenarios.xls");
		return  ""+f.getAbsolutePath()+";searchPharmacy";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		Patient patient = pdp.getPageData("patient");
		AbstractPage abstractPage;
		if(pdp.getPageData("useExistingPharmacy")!=null) {
			abstractPage = applicationPage.navigateToClaraPage()
					.logIn()
					.selectFacility()
					.searchExistingPatient();
			if(pdp.getPageData("useExistingPharmacy").equals("Edit")) {
				abstractPage = new SelectPharmacyPage(this, pdp).selectPharmacy()
																.submitPharmacySearch();
				//System.out.println("HELLO");
			}
			//System.out.println("OUT");
			WritePrescriptionsPage wpp = (WritePrescriptionsPage) abstractPage;
			if(!(abstractPage instanceof DuplicatePatientPage)) {
				abstractPage=wpp.addPrescriptions() 
								.verifyPrescriptions();
			}
		}
		else {
			abstractPage = applicationPage.navigateToClaraPage()
					.logIn()
					.selectFacility()
					.addPatient()
					.addPatientDetails()
					.navigateToMedicalHistoryPage();
		}
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
			/* if(getPatientDetails().getPrescriptionsPageData().getPediatricData()!=null) {
				AbstractPage abstractPage2 = wpp.checkPediatricAndUpdateWeight()
					.editFullPatient();
			*/
			if(!(abstractPage instanceof DuplicatePatientPage)) {
				abstractPage=wpp.addPrescriptions() 
				.verifyPrescriptions();
			}
		}
		
		new AuditLogPage(this, pdp).viewActivityLogForPharmacy();
	}
}
