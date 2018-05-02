package com.bravadohealth.automation.clara.validscenario;

import java.io.File;

import com.bravadohealth.clara1.pageObjects.ManagePatientPage;
import com.bravadohealth.clara1.pageObjects.SelectPharmacyPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class AddPatientWithFullDetails extends AbstractSeleniumTest {

	private static final String	NEGETIVE_SCENARIO_CHECK				= "negetiveScenarioCheck";
	private static final String	YES						= "Yes";
	@Override
	public String getExcelPath() {
		File f=new File("ClaraValidScenarios.xls");
		return  ""+f.getAbsolutePath()+";addPatientWithFullDetails";
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		ManagePatientPage mpp = applicationPage.navigateToClaraPage()
        .logIn()
        .selectFacility()
        .ClickPatient();
        if (!YES.equals(getParameter(NEGETIVE_SCENARIO_CHECK))) {
            if(mpp.isAddPatientsDataAvailable()) {
                mpp.addPatients();
            }
        } else {
        }
	} 	

}
