package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface PrescriptionLocators {
    By addMedicationLocator = By.xpath("//button[contains(text(),'Add medication')]");
    By prescriptionDrugNameLocator = By.xpath("//input[@placeholder='drug name']");
   // By dosageLocator = By.xpath("//input[@ng-model='medication.dose']");
   // By unitLocator = By.xpath("//select[@ng-model='medication.doseUnitUid']");
   // By frequencyInputLocator = By.xpath("//input[@placeholder='frequency']");
   // By durationLocator = By.xpath("//input[@placeholder='duration']");
   // By customSigLocator = By.xpath("//input[@placeholder='custom sig']");
    By dispenseAmountLocator = By.xpath("//input[@placeholder='dispense amount']");
   // By earliestFillDateLocator = By.xpath("//input[@placeholder='earliest fill date']");
   // By refillsLocator = By.xpath("//input[@placeholder='# refills']");
   // By substitutesCheckBoxLocator = By.xpath("//input[@type='checkbox'][@ng-model='medication.substituteOk']");
    String expandMedicationButtonLocatorString = "//button[contains(@class,'expand-medication-button')]";
    By viewPrescriptionsLocator = By.cssSelector("h3.prescriptions-label + ol.ordered-list > li");

}
