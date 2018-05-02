package com.bravadohealth.clara1.pageObjects;

import com.bravadohealth.pageObjects.AbstractPage;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class ViewPatientPage extends AbstractPage{

	public ViewPatientPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	
	public void logout() throws Exception {
		HomeViewPage homeViewPage = new HomeViewPage(webOp, dataProvider);
		homeViewPage.logout();
	}
	
}
