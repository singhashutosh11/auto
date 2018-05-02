package com.bravadohealth.clara1.pageObjects.locators;

import org.openqa.selenium.By;

public interface ManagePatientLocators {
    
   String patientDataRowLocatorSuffix = "/div[contains(@class,'ui-grid-viewport')]//div[contains(@ng-repeat,'rowRenderIndex, row')]";
   String dataGridLocator = "//div[contains(@class,'ui-grid-render-container-body')]";        
   String checkBoxGridLocator = "//div[contains(@class,'ui-grid-render-container-left')]"; 
   
   String patientDataLocatorPrefix = "//div[contains(@ng-repeat,'colRenderIndex, col')]";
   
   By patientsDataSerialStartingLocator = By.xpath("//div[@class='pagination-button-wrap']/span/b[1]");
   By patientsDataSerialEndingLocator = By.xpath("//div[@class='pagination-button-wrap']/span/b[2]");
   
   By nextPageButtonLocator = By.xpath("//button[contains(@class, 'page-forward')]");
   By previousPageButtonLocator = By.xpath("//button[contains(@class, 'page-back')]");
}
