package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface MedicalHistoryLocators {
    
    By addHomeMedicationLocator = By.xpath("//button[contains(text(),'Add home medication')]");
    By drugNameLocator = By.xpath("//input[@placeholder='drug name'][contains(@class,'ng-empty')]");
    By dosageLocator = By.xpath("//input[@ng-model='newHomeMed.dose'][contains(@class,'ng-empty')]");
    String homeMedDoseUnitLocator = "//select[@ng-model='newHomeMed.doseUnitUid'][contains(@class,'dose-unit-select')]";
    By frequencyLocator = By.xpath("//input[@placeholder='frequency'][contains(@class,'ng-empty')]");
    By lastTakenLocator = By.xpath("//input[@placeholder='last taken']");
    By patientDetailsMedicationLocator = By.xpath("//span[@class='homemeds']");

    By addAllergyLocator = By.xpath("//button[contains(text(),'Add allergy')]");
    By allergylocator = By.xpath("//input[@placeholder='allergy']");
    By reactionLocator = By.xpath("//input[@placeholder='reactions']");
    By patientDetailsAllergiesLocator = By.xpath("//span[@class='allergies']");

    By addDiagnoseLocator = By.xpath("//button[contains(text(),'Add diagnosis')]");
    By diagnosesLocator = By.xpath("//input[@placeholder='diagnosis']");
    String diagnosesLocatorString = "//input[@placeholder='diagnosis']";
    By patientDetailsDiagnosesLocator = By.xpath("//span[@class='diagnoses']");
    
    /*--- section show locators ---*/
    By showAllergiesSectionButtonLocator = By.cssSelector("button.btn-show-allergies.edit-med-history-toggle");
    By showHomeMedsSectionButtonLocator = By.cssSelector("button.btn-show-homemeds.edit-med-history-toggle");
    By showDiagnosesSectionButtonLocator = By.cssSelector("button.btn-show-diagnoses.edit-med-history-toggle");
        
}
