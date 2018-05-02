package com.bravadohealth.clara1.pageObjects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.DemographicLocators;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.clara1.utility.PatientUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.Contact;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class AddPatientPage extends AbstractPage implements DemographicLocators, ConfirmationModalLocators {

    By cancelAddPatientAlertTitleLocator = By.xpath("//div[contains(@class,'modal-header')]/h1[text()='Are you sure?']");
	By cancelButtonLocator = By.xpath("//button[contains(text(),'Cancel')]");
	By NextButtonFromDemographicLocator = By.xpath("//button[contains(text(),'Next')]");

	public AddPatientPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public void logout() throws Exception {
		HomeViewPage homeViewPage = new HomeViewPage(webOp, dataProvider);
		homeViewPage.logout();
	}

	public PatientMedicalHistory addPatientDetails() throws Exception {
		Patient patient = dataProvider.getPageData("patient");
		if (patient != null) {
			PatientDetails patientDetails = patient.getNewPatient();
			Demographic demographic = patientDetails.getDemographic();
			String firstName = HelperUtility.getString(demographic.getFirstName());
			String middleName = HelperUtility.getString(demographic.getMiddleName());
			String lastName = HelperUtility.getString(demographic.getLastName());
			String address = HelperUtility.getString(demographic.getAddress());
			String city = HelperUtility.getString(demographic.getCity());
			String state = HelperUtility.getString(demographic.getState());
			Integer zipCode = HelperUtility.getRandomZipCode();
			String gender = HelperUtility.getString(demographic.getGender());
			Float weight = demographic.getWeight();
			String weightMeasuringUnit = demographic.getWeightMeasuringUnit();
			String birthday;
			if(demographic.getBirthday()==null) {
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Calendar today = Calendar.getInstance();
				String DOB = dataProvider.getPageData("DateOfBirth");
				if(DOB.equals("Today"))
					birthday = dateFormat.format(today.getTime());
				else if (DOB.equals("Days")) {
					today.add(Calendar.DATE, -3);
					birthday = dateFormat.format(today.getTime());
				}
				else if (DOB.equals("Months")) {
					today.add(Calendar.MONTH, -7);
					birthday = dateFormat.format(today.getTime());
				}
				else {
					today.add(Calendar.YEAR, -5);
					birthday = dateFormat.format(today.getTime());
				}
			}
			else {
				birthday = HelperUtility.getString(demographic.getBirthday());
			}
			Contact contact = patientDetails.getContact();
			Long phone = contact.getPhone();
			String email = HelperUtility.getString(contact.getEmail());

			wait.until(ExpectedConditions.presenceOfElementLocated(firstNameLocator));
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(firstNameLocator).sendKeys("" + firstName + "");
			webOp.driver().findElement(lastNameLocator).sendKeys("" + lastName + "");
			webOp.driver().findElement(middleNameLocator).sendKeys("" + middleName + "");
			webOp.waitTillAllCallsDoneUsingJS();
			
			//checking address icon
			Assert.assertTrue(webOp.driver()
					.findElement(By.xpath("//div[input[@name='address']]/icon[@class='icon-home nofocus-icon']"))
					.isDisplayed(), "The home icon is displayed in address field");
			
			webOp.driver().findElement(addressLocator).sendKeys("" + address + "");
			webOp.driver().findElement(cityLocator).sendKeys("" + city + "");
			Select stateDropDown = new Select(webOp.driver().findElement(stateLocator));
			stateDropDown.selectByVisibleText(state);

			/*
			 * to skip the duplicate patient issue, take a random zipcode
			 */
			if (demographic.isDuplicatePatientRecord() != null
					&& demographic.isDuplicatePatientRecord().booleanValue() == true) {
				webOp.driver().findElement(zipCodeLocator).sendKeys("" + zipCode + "");
			} else {
				//zipCode = HelperUtility.getRandomZipCode();
				webOp.driver().findElement(zipCodeLocator).sendKeys("" + zipCode + "");
			}
			webOp.driver().findElement(birthdayLocator).sendKeys("" + birthday + "");
			webOp.waitTillAllCallsDoneUsingJS();
			String expectedAge = new StringBuilder("(").append(PatientUtility.getFromattedAge(birthday)).append(" old)").toString();
			Assert.assertEquals(webOp.driver().findElement(ageViewLocator).getText(), expectedAge, "The formatted age displayed is wrong");
			
			Select genderDropDown = new Select(webOp.driver().findElement(genderLocator));
			genderDropDown.selectByVisibleText(gender);
			webOp.driver().findElement(weightLocator).sendKeys("" + weight + "");
			if (weightMeasuringUnit.equals("kg")) {
				webOp.driver().findElement(weightMeasurementUnitLocator).click();
			}
			
			//checking phone icon
			Assert.assertTrue(webOp.driver()
					.findElement(By.xpath("//div[input[@name='phone']]/icon[@class='icon-phone nofocus-icon']"))
					.isDisplayed(), "The phone icon is displaying in phone number input field");
			webOp.driver().findElement(phoneLocator).sendKeys("" + phone + "");
			//checking email icon
			Assert.assertTrue(webOp.driver()
					.findElement(By.xpath("//div[input[@name='emailid']]/icon[@class='icon-mail nofocus-icon']"))
					.isDisplayed(), "The email icon is displaying in email input field");
			webOp.driver().findElement(emailLocator).sendKeys("" + email + "");
			
			if(demographic.getBirthday()==null) {
				String DOB = dataProvider.getPageData("DateOfBirth").toString();
				String patientAge= webOp.driver().findElement(By.xpath("//span[contains(@class,'sub-detail age ng-binding')]")).getText();
				Boolean ageFormatCheck;
				if(DOB.equals("Today"))
					ageFormatCheck=patientAge.equals("(0 days old)");
				else if (DOB.equals("Days")) {
					ageFormatCheck=patientAge.equals("(3 days old)");
				}
				else if (DOB.equals("Months")) {
					ageFormatCheck=patientAge.equals("(7 mos. old)");
				}
				else {
					ageFormatCheck=patientAge.equals("(5 yrs. old)");
				}
				Assert.assertTrue(ageFormatCheck, "Age Format is not correct");
			}
			
			if (demographic.isNavigateAway() != null && demographic.isNavigateAway() == true) {
				// browser back or click on some link
				HelperUtility.navigateBack(webOp);
				Assert.assertTrue(verifyThatPatientWasNotAdded(webOp, demographic), "Patient not added when we navigate away from patient details page");
			} 
			//cancel button in add patient page
			else if (demographic.isCancelChanges() != null && demographic.isCancelChanges() == true) {
				webOp.driver().findElement(cancelButtonLocator).click();				
				if (HelperUtility.isElementPresent(webOp, cancelAddPatientAlertTitleLocator)) {
					webOp.driver().findElement(okButtonLocator).click();
				}
			}
			 else if (PatientUtility.isAddPatientEnabled(demographic, contact) == false) {
				 
				boolean isAddmedicalHistoryButtonEnabled = webOp.driver().findElement(NextButtonFromDemographicLocator).isEnabled();
				Assert.assertFalse(isAddmedicalHistoryButtonEnabled, "Add medical history should be disabled");
			} else {
				return submitPatientDetails();
			}
		}
		return new PatientMedicalHistory(webOp, dataProvider);
	}
	
	public boolean verifyThatPatientWasNotAdded(WebOperation webOp, Demographic demographic) throws Exception {
		// TODO
		String dobSearchInput = demographic.getBirthday();
		dobSearchInput = dobSearchInput.replace('-', '/');
		webOp.driver().findElement(By.id("pick-patient-selectized")).sendKeys("" + dobSearchInput + "");
		webOp.waitTillAllCallsDoneUsingJS();
		
		WebElement searchResultElement = null;
		
		try {
			searchResultElement = webOp.driver().findElement(By.xpath("//span[contains(text(),'"+demographic.getFirstName()+"')][contains(text(),'"+demographic.getLastName()+"')]"));
		}
		catch(org.openqa.selenium.NoSuchElementException ex) {
			return true;
		}
		
		return false;
	}
	
	public PatientMedicalHistory submitPatientDetails() throws Exception{
	    Patient patient = dataProvider.getPageData("patient");
	    PatientDetails patientDetails = patient.getNewPatient();
        Demographic demographic = patientDetails.getDemographic();
	    webOp.driver().findElement(NextButtonFromDemographicLocator).click();
        webOp.waitTillAllCallsDoneUsingJS();
        if(HelperUtility.isMockProject(hMap)) {
            webOp.driver().findElement(NextButtonFromDemographicLocator).click();
            webOp.waitTillAllCallsDoneUsingJS();
        } 
        if (demographic.isDuplicatePatientRecord() != null
                && demographic.isDuplicatePatientRecord().booleanValue() == true) {
            Thread.sleep(1000);
            Boolean duplicateMessage = webOp.driver()
                    .findElement(By.xpath("//div[@class='toast-top-full-width clara-alert-message-container']"))
                    .isDisplayed();
            Assert.assertTrue(duplicateMessage,
                    "For duplicate entry in the record , duplicate message is displaying.");
        } else {
        	Alogs.add(AuditAction.PATIENT_ACCOUNT_CREATED);
            return new PatientMedicalHistory(webOp, dataProvider);
        }
        return null;
	}
}
