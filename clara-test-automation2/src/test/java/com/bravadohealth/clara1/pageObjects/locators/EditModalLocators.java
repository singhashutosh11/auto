package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface EditModalLocators {
    By editPatientLocator = By.cssSelector("div[class*='edit-patient-button'] button");
    By editPatientModal = By.cssSelector("div.edit-patient-content");

    By discardButtonLocator = By.xpath("//button[@ng-click='patDtailModlCtrl.cancel()']");
    By cancelChangesButtonLocator = discardButtonLocator;
    By saveChangesButtonLocator = By.xpath("//button[contains(text(),'Save changes')]");
    
    By formFailureErrorMessage = By.xpath("//div[contains(@class,'form-failure-error-msg')]");
}
