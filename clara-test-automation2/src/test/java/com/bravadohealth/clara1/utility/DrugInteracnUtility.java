package com.bravadohealth.clara1.utility;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.bravadohealth.clara1.enums.DDAlertResponse;
import com.bravadohealth.clara1.enums.DrugInteractionAlertType;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.DrugInteracnModalLocators;
import com.bravadohealth.pagedataset.DDInteracnAlert;
import com.bravadohealth.pagedataset.Prescription;

import trial.keyStone.automation.WebOperation;

public class DrugInteracnUtility implements ConfirmationModalLocators{

	public static String thizClassName = DrugInteracnUtility.class.getName();
	
	public static void checkDrugInteracnModalAndRespond(WebOperation webOp, DDInteracnAlert interacnAlert) throws InterruptedException{
		Thread.sleep(2000);
		if (HelperUtility.isElementPresent(webOp, modalLocator)) {
			WebDriverWait waitLocal = new WebDriverWait(webOp.driver(), 10);
			waitLocal.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
			DrugInteractionAlertType alertType = DrugInteractionAlertType.valueOf(interacnAlert.getAlertType());
			String expectedAlertTypeText = alertType.getAlertTextForUI();
			WebElement alertTypeTextElement = webOp.driver().findElement(DrugInteracnModalLocators.drugInteracnAlertHeadingLocator);
			if(!alertTypeTextElement.getText().contains(expectedAlertTypeText) ) {
				webOp.getSoftAssert().fail("drug interaction alert type did not match"+", class="+thizClassName+", method="+Thread.currentThread().getStackTrace()[1].getMethodName() + ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			}			
			respondToInteracnAlert(webOp, interacnAlert);
			waitLocal.until(ExpectedConditions.invisibilityOfElementLocated(modalLocator));
		} else {
			webOp.getSoftAssert().fail("drug interaction modal not found"+", class="+thizClassName+", method="+Thread.currentThread().getStackTrace()[1].getMethodName() + ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			webOp.logger().error("drug interaction modal not found");
		}
	}
	
	private static DDAlertResponse respondToInteracnAlert(WebOperation webOp, DDInteracnAlert interacnAlert){
		//Ignore refers to continue in new implemented modal
		if(DDAlertResponse.Ignore == DDAlertResponse.getAlertResponse(interacnAlert.getResponse())){
			//select reason to override
			List<String> overrideReason=interacnAlert.getReason();
			for(String reason:overrideReason) {
				webOp.driver().findElement(By.xpath("//ul[contains(@class,'checklist')]/li[label[contains(text(),'"+reason+"')]]/input")).click();
			}
			webOp.driver().findElement(continueRxButtonLocator).click();
			return DDAlertResponse.Ignore;
		} else {
			webOp.driver().findElement(removeRxButtonLocator).click();
			return DDAlertResponse.Accept;
		}
	}
	
	public static void checkAndRespondToDrugInteracnModals(WebOperation webOp, Map<DDInteracnAlert, Prescription> interactionAlertMapToPrescription) throws InterruptedException{
		int totalInteracnAlerts = interactionAlertMapToPrescription.size();
		while(totalInteracnAlerts>0) {
			Thread.sleep(2000);
			if (HelperUtility.isElementPresent(webOp, modalLocator)) {
				WebDriverWait waitLocal = new WebDriverWait(webOp.driver(), 10);
				waitLocal.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
				String alertHeading = webOp.driver().findElement(DrugInteracnModalLocators.drugInteracnAlertHeadingLocator).getText();
				String thisMedication = webOp.driver().findElement(DrugInteracnModalLocators.drugInteracnAlertThisMedicationLocator).getText();
				String otherMedOrAllergy = webOp.driver().findElement(DrugInteracnModalLocators.drugInteracnOtherMedOrAllergyLocator).getText();

				DrugInteractionAlertType alertType = getAlertTypeBasedOnUiText(alertHeading);
				
				Map.Entry<DDInteracnAlert, Prescription> mapEntry = 
					getAlertMatchingGivenDetails(interactionAlertMapToPrescription, alertType, thisMedication, otherMedOrAllergy);
				DDInteracnAlert interacnAlert = mapEntry.getKey();
				Prescription prescription = mapEntry.getValue();
				DDAlertResponse response = respondToInteracnAlert(webOp, interacnAlert);
				waitLocal.until(ExpectedConditions.invisibilityOfElementLocated(modalLocator));
				if(response == DDAlertResponse.Accept) {
					prescription.setDelete("true");
				}
				
				interactionAlertMapToPrescription.remove(interacnAlert);
			} else {
				webOp.getSoftAssert().fail("drug interaction modal not found"+", class="+thizClassName+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
				webOp.logger().error("drug interaction modal not found");
			}
			totalInteracnAlerts --;
		}
	}
	
	public static Map.Entry<DDInteracnAlert, Prescription> getAlertMatchingGivenDetails(Map<DDInteracnAlert, Prescription> interactionAlertMapToPrescription, 
			DrugInteractionAlertType alertType, String thisMed, String otherMedOrAllergy) {
		
		for(Map.Entry<DDInteracnAlert, Prescription> mapEntry : interactionAlertMapToPrescription.entrySet()) {
			DDInteracnAlert interacnAlert = mapEntry.getKey();
			if(interacnAlert.getAlertType().equals(alertType.name())
				&& thisMed.equalsIgnoreCase(interacnAlert.getDrugName())){
				if(alertType == DrugInteractionAlertType.DrugAllergy){
					if(otherMedOrAllergy.equalsIgnoreCase(interacnAlert.getAllergyName())){
						return mapEntry;
					}
				} else if(otherMedOrAllergy.equalsIgnoreCase(interacnAlert.getPrescriptionDrugName())){
					return mapEntry;
				} else if(otherMedOrAllergy.equalsIgnoreCase(interacnAlert.getHomeMedDrugName())){
					return mapEntry;
				}
			}
		}
		return null;
	}
	
	private static DrugInteractionAlertType getAlertTypeBasedOnUiText(String alertHeading) {
		for(DrugInteractionAlertType alertType : DrugInteractionAlertType.values()) {
			if(alertHeading.contains(alertType.getAlertTextForUI())){
				return alertType;
			}
		}
		return null;
	}
	
}
