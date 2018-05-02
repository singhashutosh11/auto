package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.pageObjects.locators.IdVerificationPageLocators;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class IdVerificationPage extends AbstractPage implements IdVerificationPageLocators{
	
		public IdVerificationPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public IdVerificationPage verifyIdentity() throws Exception {
		wait.until(ExpectedConditions.urlContains(AppPage.LicenseVerification.getUrlContains(hMap)));
		ClaraUserDetails claraUserDetails=dataProvider.getPageData("claraUserDetails");	
		Select designationSelector=new Select(webOp.driver().findElement(designationLocator));
		designationSelector.selectByVisibleText(claraUserDetails.getDesignation());
		Select stateDropDown = new Select(webOp.driver().findElement(stateLocator));
		stateDropDown.selectByVisibleText(claraUserDetails.getState());
		webOp.driver().findElement(stateLicenseNumberLocator).sendKeys(claraUserDetails.getStateLicenseNumber());
		webOp.driver().findElement(NPINumberLocator).sendKeys(claraUserDetails.getNPINumber());
		webOp.driver().findElement(validateLocator).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderSign));
		webOp.waitTillAllCallsDoneUsingJS();
		return this;
	}

	public IdentityProofingPage validateSuccessIDVerification() {
		ClaraUserDetails claraUserDetails=dataProvider.getPageData("claraUserDetails");
		String designation=webOp.driver().findElement(displayedDesignation).getText();
		String stateLicense=webOp.driver().findElement(displayedStateLicense).getText();
		String NPINumber=webOp.driver().findElement(displayedNPINumber).getText();

		//String NDEANNumber=webOp.driver().findElement(displayedNDEANNumber).getText();
		//webOp.driver().findElement(NDEANNumberLocator).sendKeys(claraUserDetails.getNADEANumber());
		Assert.assertEquals(designation, claraUserDetails.getDesignation(),"The entered designation is displayed incorrectly.");
		Assert.assertEquals(stateLicense, claraUserDetails.getStateLicenseNumber(),"The entered stateLicense is displayed incorrectly.");
		Assert.assertEquals(NPINumber, claraUserDetails.getNPINumber(),"The entered NPI Number is displayed incorrectly.");
		//Assert.assertEquals(NDEANNumber, claraUserDetails.getNADEANumber(),"The entered NDEAN Number is displayed correctly.");
		
		webOp.driver().findElement(continueButtonLocator).click();
		Alogs.add(AuditAction.LICENSE_VERIFIED);
		return new IdentityProofingPage(webOp, dataProvider);
	}
	
	public IdVerificationPage validateErrorMessage() throws Exception {
		String errorMessage = webOp.getParameter("errorMessage");
		Assert.assertTrue(webOp.driver().findElement(licenseError).getText().contains(errorMessage),"License verification did not fail");
		return this;
	}

}
