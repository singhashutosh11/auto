package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.clara1.pageObjects.AuthorizePrescriptionsPage;
import com.bravadohealth.clara1.pageObjects.PatientMedicalHistory;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class EditPrescription extends AbstractSeleniumTest {

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
		if(abstractPage instanceof SelectPharmacyPage ) {
			abstractPage=new SelectPharmacyPage(this, pdp).selectPharmacy()
					.submitPharmacySearch();
		}
		WritePrescriptionsPage wpp = (WritePrescriptionsPage) new PatientMedicalHistory(this, pdp).moveToWritePrescriptionsPage();
		wpp = (WritePrescriptionsPage) wpp.addPrescriptions().verifyPrescriptions();
		AuthorizePrescriptionsPage authorizePrescriptionPage = (AuthorizePrescriptionsPage)wpp.editPrescriptions().moveToAuthorizePrescriptionsPageAgain();
		authorizePrescriptionPage.navigateToAuthorizePrescriptionsPage()
		.authorizePrescriptions()
		.submitAuthorizedPrescriptions();

	}

}
