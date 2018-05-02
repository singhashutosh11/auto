package com.bravadohealth.automation.clara.invalidscenario;

import java.io.File;
import java.io.IOException;

import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.PageData;

import trial.keyStone.automation.AbstractSeleniumTest;

public class SearchPatientClaraWithRandomInput extends AbstractSeleniumTest {

	@Override
	public String getExcelPath() {
		File f=new File("ClaraInvalidScenario.xls");
		return  ""+f.getAbsolutePath()+";ViewPatient";
		
	}


	@Override
	protected void executeTest() throws Exception {
		ApplicationPage applicationPage = new ApplicationPage(this, getPageDataProvider(PageData.class));
		applicationPage.navigateToClaraPage()
		.logIn()
		.searchPatient();

	}


}
