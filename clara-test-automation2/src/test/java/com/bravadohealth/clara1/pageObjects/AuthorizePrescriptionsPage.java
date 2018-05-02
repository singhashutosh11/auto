package com.bravadohealth.clara1.pageObjects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.pageObjects.locators.AuthorizePrescriptionLocators;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.DemographicLocators;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.services.PatientService;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.clara1.utility.PatientUtility;
import com.bravadohealth.clara1.utility.UserUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.AuthorizePrescriptionsPageData;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.Contact;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;
import com.bravadohealth.pagedataset.Prescription;
import com.bravadohealth.pagedataset.PrescriptionsData;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class AuthorizePrescriptionsPage extends AbstractPage implements AuthorizePrescriptionLocators,LoaderLocators{

	public AuthorizePrescriptionsPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public void logout() throws Exception {
		HomeViewPage homeViewPage = new HomeViewPage(webOp, dataProvider);
		homeViewPage.logout();
	}

	private PatientDetails getPatientDetails() {
		Patient patient = dataProvider.getPageData("patient");
		PatientDetails patientDetails;
		if (patient.getNewPatient() != null) {
			patientDetails = patient.getNewPatient();
		} else {
			patientDetails = patient.getExistingPatient();
		}
		return patientDetails;
	}

	private AuthorizePrescriptionsPageData getAuthorizePrescriptionsPageData() {
		PatientDetails patientDetails = getPatientDetails();
		AuthorizePrescriptionsPageData authorizePrescriptionsPageData = patientDetails.getAuthorizePrescriptionsPageData();
		return authorizePrescriptionsPageData;
	}

	public AuthorizePrescriptionsPage navigateToAuthorizePrescriptionsPage() throws Exception {
		List<Prescription> prescriptions = PatientService.getUpdatedPrescriptions();
		boolean isAnyEPCS = false;
		if (prescriptions.size() > 0) {
			for (Prescription prescription : prescriptions) {
				if(PatientUtility.isControlledSubtanceAvailable(prescription) && prescription.isEPrescribe()!=null && prescription.isEPrescribe().equals(true)){
					isAnyEPCS = true;
					break;
				}
			}
		}
		wait.until(ExpectedConditions.urlContains(AppPage.AuthorizePrescriptions.getUrlContains(hMap)));
		return this;
	}

	public AuthorizePrescriptionsPage verifyCompletePrintPage() {
		//new PrintPage(webOp, dataProvider).verifyProviderDetails().verifyPatientDetails().verifyPrintedMedication();
		return this;
	}

	//for CLAR-46
	public AbstractPage goBackIfEditPrescriptions() throws Exception {
		AuthorizePrescriptionsPageData authorizePrescriptionsPageData = getAuthorizePrescriptionsPageData();
		PrescriptionsData editPrescriptionsData = authorizePrescriptionsPageData.getEditPrescriptions();
		if ( editPrescriptionsData != null && ( editPrescriptionsData.getPrescription().isEmpty() == false)) {
			webOp.driver().findElement(goBackLinkLocator).click();
			return new WritePrescriptionsPage(webOp, dataProvider);
		}
		return this;
	}

	//for CLAR-393
	public AuthorizePrescriptionsPage verifyDetails() throws Exception {
		ClaraUserDetails userDetails = dataProvider.getPageData("claraUserDetails");
		String expectedCompleteName = userDetails.getFirstname()+" "+userDetails.getLastName()+", "+HelperUtility.getString(userDetails.getDesignation());
		String actualCompleteName = webOp.driver().findElement(prescriberNameLocator).getText();
		Assert.assertEquals(actualCompleteName, expectedCompleteName);
		String address = userDetails.getAddress();
		String city = userDetails.getCity();
		String state = userDetails.getState();
		String zipCode = userDetails.getZipCode();
		if(UserUtility.isAddressAvailable(address, city, state, zipCode)) {
			String expectedCompleteAddress = new StringBuilder(address).append("\n").append(city).append(", "+state+" "+zipCode).toString();
			System.out.println(expectedCompleteAddress);
			String actualCompleteAddress = webOp.driver().findElement(prescriberAddressLocator).getText();
			Assert.assertEquals(actualCompleteAddress, expectedCompleteAddress);
		}
		String expectedDEANumber = userDetails.getDEANumber();
		String actualDEANumber = webOp.driver().findElement(prescriberDEALocator).getText();
		Assert.assertEquals(actualDEANumber, expectedDEANumber);

		PatientDetails patientDetails = getPatientDetails();
		String expectedFullName = PatientUtility.getPatientFullNameIncludingMiddleName(patientDetails);
		String actualFullName = webOp.driver().findElement(patientNameLocator).getText();
		Assert.assertEquals(actualFullName, expectedFullName);

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String expectedDate = sdf.format(today);
		String actualDate = webOp.driver().findElement(dateIssuedLocator).getText();
		Assert.assertEquals(actualDate, expectedDate);

		List<Prescription> updatedprescriptions = PatientService.getUpdatedPrescriptions();
		int i=1;
		for(Prescription prescription : updatedprescriptions){
			if(PatientUtility.isControlledSubtanceAvailable(prescription)&& prescription.isEPrescribe()!=null && prescription.isEPrescribe().equals(true) && webOp.gethMap().get("EPCSEnabled").equals("true")){
				WebElement drugNameElement = webOp.driver().findElement(By.xpath(controlledSubstancesLocatorPrefix+"["+i+"]"+prescriptionDetailsRow1LocatorString+"//span[position()="+1+"]"));
				Assert.assertEquals(drugNameElement.getText(), prescription.getDrugName());
				WebElement dispenseAmountElement = webOp.driver().findElement(By.xpath(controlledSubstancesLocatorPrefix+"["+i+"]"+prescriptionDetailsRow1LocatorString+"//span[position()="+2+"]"));
				Assert.assertTrue(dispenseAmountElement.getText().contains(prescription.getDispenseAmount()));
				WebElement customSignatureElement = webOp.driver().findElement(By.xpath(controlledSubstancesLocatorPrefix+"["+i+"]"+prescriptionDetailsRow1LocatorString+"//span[position()="+3+"]"));;
				Assert.assertTrue(customSignatureElement.getText().toLowerCase().contains(prescription.getCustomSig().toLowerCase()));

				String expectedRefillCount = prescription.getRefills() +" "+ (prescription.getRefills().intValue() == 1 ? "refill": "refills"); 
				WebElement refillElement = webOp.driver().findElement(By.xpath(controlledSubstancesLocatorPrefix+"["+i+"]"+prescriptionDetailsRow2LocatorString+"//span[position()="+1+"]"));
				Assert.assertEquals(refillElement.getText(), expectedRefillCount);
				String expectedSubstitutes = null;
				if(prescription.isSubstitutes()!=null && prescription.isSubstitutes().booleanValue() == true)
					expectedSubstitutes = "subs OK";
				else 
					expectedSubstitutes = "no subs";
				WebElement substitutesElement = webOp.driver().findElement(By.xpath(controlledSubstancesLocatorPrefix+"["+i+"]"+prescriptionDetailsRow2LocatorString+"//span[position()="+2+"]"));;
				Assert.assertEquals(substitutesElement.getText(), expectedSubstitutes);
				i++;
			}
		}
		return this;
	}

	//for CLAR-1143
	public AuthorizePrescriptionsPage authorizePrescriptions() throws Exception {
		List<Prescription> updatedprescriptions = PatientService.getUpdatedPrescriptions();
		int i=1; 
		for(Prescription prescription : updatedprescriptions){
			if(PatientUtility.isControlledSubtanceAvailable(prescription) && prescription.isEPrescribe()!=null && prescription.isEPrescribe().equals(true)){
				boolean readyToSign = prescription.getControlledSubstance().isReadyToSign().booleanValue();
				if(readyToSign==true) {
					WebElement controlledSubstaneElement = webOp.driver().findElement(By.xpath(controlledSubstancesLocatorPrefix+"["+i+"]"));
					controlledSubstaneElement.click();
					webOp.waitTillAllCallsDoneUsingJS();
					WebElement readyToSignIconElement = controlledSubstaneElement.findElement(readyToSignIconLocator);
					Assert.assertEquals(readyToSignIconElement.getAttribute("class"), "icon-checkmark");
				}
				i++;
			}
		}
		return this;
	}

	public AbstractPage submitAuthorizedPrescriptions() throws Exception {
		AuthorizePrescriptionsPageData authorizePrescriptionsPageData = getAuthorizePrescriptionsPageData();
		Boolean navigateAway = authorizePrescriptionsPageData.isNavigateAway();   
		if (navigateAway != null && (navigateAway.booleanValue() == true)) {
			// browser back or click on some link
			HelperUtility.navigateBack(webOp);
			return new WritePrescriptionsPage(webOp, dataProvider);
		} 
		else if(PatientService.hasSignedControlledSubstance()){
			webOp.driver().findElement(signPrescriptionsButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
			webOp.driver().findElement(By.id("pwd")).sendKeys("Clara123");
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(By.id("two-factor-next-step-1")).click();
			//Push button click
			webOp.driver().findElement(By.xpath("//button[contains(text(),'Push')]")).click();
			List<Prescription> updatedprescriptions = PatientService.getUpdatedPrescriptions();
			int i=1; 
			for(Prescription prescription : updatedprescriptions){
				if(PatientUtility.isControlledSubtanceAvailable(prescription) && prescription.isEPrescribe()!=null && prescription.isEPrescribe().equals(false)){
					verifyCompletePrintPage();
					return this;
				}
				i++;
			}
			if(!HelperUtility.isMockProject(hMap)) {
				//Alert alert = webOp.driver().switchTo().alert();
				/*String expectedAlertText = PatientService.getSignedPrescriptionsAlertText();
				Assert.assertTrue(alert.getText().contains(expectedAlertText), "error in alert message");
				alert.accept();*/
				//check 2F modal displayed
			} else{
				// check 2F modal displayed
			}
			return this;
		} else {
			webOp.driver().findElement(signPrescriptionsButtonLocator).click();
			Alert alert = webOp.driver().switchTo().alert();
			System.out.println(alert.getText());
			alert.accept();
			//no prescriptions window alert displayed
			return this;
		}
	}

}
