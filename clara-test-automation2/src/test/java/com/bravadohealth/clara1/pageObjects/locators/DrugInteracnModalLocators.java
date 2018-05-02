package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface DrugInteracnModalLocators {

	By drugInteracnAlertHeadingLocator = By.xpath("//div[contains(@class, 'modal-header')]//h1");
	By drugInteracnAlertThisMedicationLocator = By.xpath("//div[contains(@class, 'modal-body')]//div[contains(@class, 'section-wrap')]//div[contains(@class, 'row')][1]//span[contains(@class, 'bold highlight-error')]");
	By drugInteracnOtherMedOrAllergyLocator = By.xpath("//div[contains(@class, 'modal-body')]//div[contains(@class, 'section-wrap')]//div[contains(@class, 'row')][2]//span[contains(@class, 'bold')]");

}
