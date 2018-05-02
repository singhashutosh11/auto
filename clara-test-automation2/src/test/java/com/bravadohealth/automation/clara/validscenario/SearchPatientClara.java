package com.bravadohealth.automation.clara.validscenario;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Logger;

import com.bravadohealth.clara1.pageObjects.AuditLogPage;
import com.bravadohealth.clara1.pageObjects.AuthorizePrescriptionsPage;
import com.bravadohealth.clara1.pageObjects.HomeViewPage;
import com.bravadohealth.clara1.pageObjects.WritePrescriptionsPage;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;
import trial.keyStone.automation.PageDataProvider;

public class SearchPatientClara extends AbstractSeleniumTest {

	private static final String NEGETIVE_SCENARIO_CHECK = "negetiveScenarioCheck";
	private static final String YES = "Yes";
	private static final String CONFIG_FILE = "/clara_scenarios/clara_patient/search_patient_clara.xls";

	@Override
	public String getExcelPath() {
		URL fileUrl = getClass().getResource(CONFIG_FILE);
		
		File f = new File(fileUrl.getFile());
		
		try {
			return URLDecoder.decode(f.getAbsolutePath() , "UTF-8")+ ";searchPatient";
		} catch (UnsupportedEncodingException e) {
			
			throw new RuntimeException(e);
		}	
	}

	@Override
	protected void executeTest() throws Exception {
		PageDataProvider pdp = getPageDataProvider(PageData.class);
		ApplicationPage applicationPage = new ApplicationPage(this, pdp);
		applicationPage.navigateToClaraPage().logIn()
		.selectFacility();
		if (!YES.equals(getParameter(NEGETIVE_SCENARIO_CHECK))) {
			//AbstractPage ap=null;
			WritePrescriptionsPage wp =(WritePrescriptionsPage)new HomeViewPage(this, pdp).searchPatient();
			wp.addPrescriptions()
				.verifyPrescriptions();
			/*ap=wp.moveToAuthorizePrescriptionsPage();

			if(ap instanceof AuthorizePrescriptionsPage) {
				AuthorizePrescriptionsPage app = (AuthorizePrescriptionsPage) ap; 
				app.authorizePrescriptions()
				.submitAuthorizedPrescriptions(); 
			}*/
			new AuditLogPage(this, pdp).viewActivityLogForPatient();
		} else {
			try {
				new HomeViewPage(this, pdp).searchPatient();
			}
			catch(Exception ex) {
				if(ex.getMessage().equals("Patient not found"))
				{
					Logger LOG = Logger.getLogger(this.getClass().toString());
					LOG.info("No such patient found");
				}
				else {
					// some other exception
					throw ex;
				}
			}
		}
	}
}
