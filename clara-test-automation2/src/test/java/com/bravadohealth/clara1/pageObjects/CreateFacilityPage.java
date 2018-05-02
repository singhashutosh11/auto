package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.bravadohealth.clara1.pageObjects.locators.CreateFacilityLocators;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.Facility;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class CreateFacilityPage extends AbstractPage implements CreateFacilityLocators {

	public CreateFacilityPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}

	public CreateFacilityPage FillInFacilityDetails() {
		Facility facility = dataProvider.getPageData("facility");
		webOp.driver().findElement(facilityName).sendKeys(facility.getFacilityName());
		webOp.driver().findElement(facilityAddress).sendKeys(facility.getAddress());
		webOp.driver().findElement(cityName).sendKeys(facility.getCity());
		Select stateDropDown = new Select(webOp.driver().findElement(StateLocator));
		stateDropDown.selectByVisibleText(facility.getState());
		webOp.driver().findElement(zipCode).sendKeys(Integer.toString(facility.getZipCode()));
		webOp.driver().findElement(phoneNumber).sendKeys(Long.toString(facility.getPhone()));
		webOp.driver().findElement(faxNumber).sendKeys(Long.toString(facility.getFaxNumber()));
		if (facility.getFacilityNPI() != null) {
			webOp.driver().findElement(facilityNPI).sendKeys(facility.getFacilityNPI());
		}
		webOp.driver().findElement(facilityDEA).sendKeys(facility.getFacilityDEA());
		webOp.driver().findElement(validateLocator).click();
		return this;
	}

	public IdVerificationPage chooselicenseType() throws Exception {
		ClaraUserDetails claraUserDetails = dataProvider.getPageData("claraUserDetails");
		webOp.driver()
				.findElement(By.xpath("//button[h2[contains(text(),'" + claraUserDetails.getLicenseType() + "')]]"))
				.click();
		webOp.driver().findElement(continueButtonLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		return new IdVerificationPage(webOp, dataProvider);
	}

}
