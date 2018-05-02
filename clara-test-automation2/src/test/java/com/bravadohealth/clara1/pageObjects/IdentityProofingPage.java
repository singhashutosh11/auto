package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.bravadohealth.clara1.enums.PlanType;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class IdentityProofingPage extends AbstractPage implements LoaderLocators{
	
	By continueButtonLocator=By.xpath("//button[a[contains(text(),'Continue')]]");
	By successIdProofingButtonLocator=By.xpath("//button[contains(text(), 'success')]");
	By failureIdProofingButtonLocator=By.xpath("//button[contains(text(), 'failure')]");
	
	public IdentityProofingPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public DoneWithAddedLicensePage continueToDone() throws Exception {
		webOp.driver().findElement(continueButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		return new DoneWithAddedLicensePage(webOp, dataProvider);
	}
	
	public IdentityProofingPage markIdProofingAsSuccess() throws Exception {
		webOp.driver().findElement(successIdProofingButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		return this;
	}
	
	public IdentityProofingPage markIdProofingAsFailure() throws Exception {
		webOp.driver().findElement(failureIdProofingButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		return this;
	}

}
