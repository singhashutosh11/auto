package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;

import com.bravadohealth.clara1.enums.PlanType;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class ChooseLicensePage extends AbstractPage {
	By continueButtonLocator=By.xpath("//button[a[contains(text(),'Continue')]]");

	public ChooseLicensePage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public AbstractPage continueAfterLicenseSelected() {
		ClaraUserDetails claraUserDetails=dataProvider.getPageData("claraUserDetails");
		String facilityOwnerLicenseLabel=claraUserDetails.getLicenseType();
		webOp.driver().findElement(By.xpath("//button[h2[text() ='"+facilityOwnerLicenseLabel+"']]")).click();
		webOp.driver().findElement(continueButtonLocator).click();
		
		PlanType ownerPlanType = PlanType.getPlanTypeFromUiLabel(facilityOwnerLicenseLabel);
		if(PlanType.Basic != ownerPlanType) {
			return new IdVerificationPage(webOp, dataProvider);
		} else {
			return new HomeViewPage(webOp, dataProvider);
		}
	}


}
