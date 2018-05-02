package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.pageObjects.AbstractPage;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class PrescriptionResultPage extends AbstractPage {

	public PrescriptionResultPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	

	public void logout() throws Exception {
		HomeViewPage homeViewPage = new HomeViewPage(webOp, dataProvider);
		homeViewPage.logout();
	}
	
	public PrescriptionResultPage waitUntilPrescriptionResultPageisLoaded() throws Exception {
		wait.until(ExpectedConditions.urlContains(AppPage.PrescriptionResult.getUrlContains(hMap)));
		return this;
	}
	
	public AuditLogPage moveToAuditTrailPage() throws Exception {
		HelperUtility.closeAllOtherWindows(webOp);
		return new AuditLogPage(webOp, dataProvider).navigateToAuditPage();
	}

}