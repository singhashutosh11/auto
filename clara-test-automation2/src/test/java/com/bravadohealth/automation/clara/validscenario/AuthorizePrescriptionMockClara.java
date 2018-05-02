package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.clara1.pageObjects.AddPatientPage;
import com.bravadohealth.clara1.pageObjects.AuthorizePrescriptionsPage;
import com.bravadohealth.clara1.pageObjects.DuplicatePatientPage;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class AuthorizePrescriptionMockClara extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	@Override
	public String getExcelPath() {
		File f=new File("ClaraValidScenarios.xls");
		return  ""+f.getAbsolutePath()+";authorizePrescription";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		AbstractPage aP = applicationPage.navigateToClaraPageAtMockProject()
				.logIn()
				.addPatient();
		if(aP instanceof AddPatientPage) {
			aP=new AddPatientPage(this, pdp).submitPatientDetails()
			.navigateToMedicalHistoryPage();
			//.submitMedicalHistoryDetails();
		}
		//if(aP instanceof SelectPharmacyPage) {
	/*		SelectPharmacyPage sp = (SelectPharmacyPage) aP;
			AbstractPage aP2 = sp.navigateToSelectPharmacyPage()
					.submitPharmacyDetails();*/
			if(aP instanceof WritePrescriptionsPage) {
				WritePrescriptionsPage wpp = (WritePrescriptionsPage) aP; 
				AbstractPage aP3 = wpp.moveToPrescriptionPageInMockProject()
						;//submitPrescriptionDetails();

				if(aP3 instanceof AuthorizePrescriptionsPage) {
					AuthorizePrescriptionsPage app = (AuthorizePrescriptionsPage) aP3;
					app.navigateToAuthorizePrescriptionsPage()
					.submitAuthorizedPrescriptions();
				}
			}
		//}
	} 	

}
