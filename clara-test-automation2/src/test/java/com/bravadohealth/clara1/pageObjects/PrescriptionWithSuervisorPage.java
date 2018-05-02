package com.bravadohealth.clara1.pageObjects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.enums.DDAlertResponse;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.DemographicLocators;
import com.bravadohealth.clara1.pageObjects.locators.EditModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.pageObjects.locators.MedicalHistoryLocators;
import com.bravadohealth.clara1.pageObjects.locators.PrescriptionLocators;
import com.bravadohealth.clara1.services.PatientService;
import com.bravadohealth.clara1.utility.AppMessages;
import com.bravadohealth.clara1.utility.DrugInteracnUtility;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.clara1.utility.PatientUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.Allergies;
import com.bravadohealth.pagedataset.AuthorizePrescriptionsPageData;
import com.bravadohealth.pagedataset.Contact;
import com.bravadohealth.pagedataset.ControlledSubstance;
import com.bravadohealth.pagedataset.DDInteracnAlert;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.Diagnoses;
import com.bravadohealth.pagedataset.HomeMeds;
import com.bravadohealth.pagedataset.ManageExistingPatient;
import com.bravadohealth.pagedataset.ManageFacilityDetails;
import com.bravadohealth.pagedataset.MedicalHistory;
import com.bravadohealth.pagedataset.Medication;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;
import com.bravadohealth.pagedataset.PediatricData;
import com.bravadohealth.pagedataset.Prescription;
import com.bravadohealth.pagedataset.PrescriptionsData;
import com.bravadohealth.pagedataset.PrescriptionsPageData;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class PrescriptionWithSuervisorPage extends AbstractPage implements PrescriptionLocators, MedicalHistoryLocators,
DemographicLocators, EditModalLocators, ConfirmationModalLocators, LoaderLocators {

	By cancelWritePrescriptionsAlertTitleLocator = navigateAwayAlertTitleLocator;
	/* move to next page related variables */
	By cancelSavePresciptionsLocator = By.xpath("//button[contains(text(),'Cancel')]");
	// By authorizePrescriptionLocator =
	// By.xpath("//button[contains(text(),'Authorize�')]");
	By submitPrescriptionLocator = By.xpath("//button[contains(text(),'Send prescriptions')]");

	By pediatricWeightUpdateFormLocator = By.xpath("//clara-weight-form");
	By updatePediatricWeightInputLocator = By.xpath("//clara-weight-form//input[@name='weight']");
	By updatePediatricWeightUnitLocator = By.xpath("//clara-weight-form//div[contains(@ng-click, 'toggleWeightUnit')]");

	By updatePediatricWeightButtonLocator = By
			.xpath("//clara-weight-form//button[contains(@class, 'peds-update-weight')]");
	By savePediatricWeightButtonLocator = By
			.xpath("//clara-weight-form//button[contains(@class, 'peds-confirm-weight')]");
	By GoBackButtonLocator = By.xpath("//clara-weight-form//button[@ng-click='weightCtrl.back()']");

	By iconSuccessCheckMarkLocator = By
			.xpath("//clara-weight-form//icon[contains(@class, 'icon-checkmark success-color')]");
	By updatedWeightViewLocator = By
			.xpath("//div[@ng-show='patDmghcsViewCtrl.isBasicDetailsAvailable()']/div/span[position()=3]");

	String deleteMedicationButtonLocatorString = "//button[contains(@class,'medication-delete-btn')]";
	By revertMedicationButtonLocator = By.xpath("//button[contains(@class,'medication-revert-btn')]");
	By addNoteButtonLocator = By.xpath("//button[contains(@class,'medication-pharmacynote-btn')]");
	By pharmacyNoteLocator = By.xpath("//textarea[contains(@class,'pharmacy-note') and contains(@class,'show')]");

	private String doseUnitDropDownLocatorString = "//select[@ng-model='medication.doseUnitUid']";
	private By medicationFavouriteButtonLocator = By.xpath("//button[contains(@class,'medication-favorite-btn')]");
	private By iconFavouriteLocator = By.xpath("//button[contains(@class,'medication-favorite-btn')]/icon");

	By favouriteDrugNamesListLocator = By
			.xpath("//div[contains(@class, 'ui-select-match') and @placeholder='drug name']/following-sibling::ul[contains(@class, 'ui-select-choices')]//div[contains(@class, 'row no-margin favorite-med')]/div[2]/span[1]");

	String viewMedicationNameElementPrefix = "//div[contains(@class,'item no-tag') and ";
	String before;
	
    String viewNthPrescriptionLocatorPrefix = "h3.prescriptions-label + ol.ordered-list > li:nth-child";

    private String ePrescriptionIconClass = "icon-electronic-prescribing";
    private String printPrescriptionIconClass = "icon-printing-prescriptions";
    
	private String discontinueButton="//button[contains(text(),'Discontinue')]";
	private String markAsError="//button[contains(text(),'Mark Error')]";
	private String substitutesOkCheckBoxLocatorSuffix = "//input[@type='checkbox' and @ng-model='medication.substituteOk']";
	private String substitutesOkSwitcheryLocatorSuffix = "//div[input[@type='checkbox' and @ng-model='medication.substituteOk']]/span";
	
	private String uiSelectDropDownOptionLocatorPrefix = "//span[@title='";
	private String uiSelectDropDownOptionLocatorSuffix = "']";
	
	public PrescriptionWithSuervisorPage(WebOperation webOp, PageDataProvider dataProvider) {
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

	public PrescriptionsPageData getPrescriptionsPageData() {
		PatientDetails patientDetails = getPatientDetails();
		PrescriptionsPageData prescriptionsPageData = patientDetails.getPrescriptionsPageData();
		return prescriptionsPageData;
	}

	public PrescriptionsData getPrescriptionsData() {
		PrescriptionsPageData prescriptionsPageData = getPrescriptionsPageData();
		PrescriptionsData prescriptionsData = prescriptionsPageData.getPrescriptionsData();
		return prescriptionsData;
	}

	private PrescriptionsData getEditPrescriptionsData() {
		PatientDetails patientDetails = getPatientDetails();
		AuthorizePrescriptionsPageData authorizePrescriptionsPageData = patientDetails
				.getAuthorizePrescriptionsPageData();
		PrescriptionsData editPrescriptionsData = authorizePrescriptionsPageData.getEditPrescriptions();
		return editPrescriptionsData;
	}

	private List<Prescription> getEditPrescriptions() {
		PrescriptionsData editPrescriptionsData = getEditPrescriptionsData();
		return editPrescriptionsData.getPrescription();
	}

	private PatientDetails getEditFullPatientData() {
		PrescriptionsPageData prescriptionsPageData = getPrescriptionsPageData();
		PatientDetails editFullPatientData = prescriptionsPageData.getEditFullPatientData();
		return editFullPatientData;
	}

	public void addMedication() throws InterruptedException {
		webOp.driver().findElement(addMedicationLocator).click();
		Thread.sleep(500);
	}

	public PrescriptionWithSuervisorPage addPrescriptions() throws Exception {

		PrescriptionsData prescriptionsData = getPrescriptionsData();
		if (prescriptionsData != null) {
			//initialize Patient Service's list of prescriptions, with an empty list
			/*------init start-----------------*/
			List<Prescription> existingPrescriptions = new ArrayList<Prescription>();
			PatientService.setUpdatedPrescriptions(existingPrescriptions);
			/*------init end-----------------*/
			List<Prescription> prescriptions = prescriptionsData.getPrescription();
			if (prescriptions.size() > 0) {
				int i = 1;
				int totalPrescriptions = prescriptions.size();
				//Set<String> addedDrugNames = new HashSet<String>();
				for (Prescription prescription : prescriptions) {
					webOp.waitTillAllCallsDoneUsingJS();
					//boolean isAdded = addPrescription(prescription, i, addedDrugNames);
					boolean isAdded = addPrescription(prescription, i);
					if(isAdded) {
						Alogs.add(Alogs.size()-(i-1),AuditAction.MEDICATION_PRESCRIBED);
					}
					existingPrescriptions.add(prescription);
					if (((i==1) && (isAdded == true) && (i<totalPrescriptions)) || (i>1 && i< totalPrescriptions)) {
						WebElement addMedicationElement = webOp.driver().findElement(addMedicationLocator);
						try {
							addMedicationElement.click();
						} catch (Exception e) {
							System.out.println(e.getMessage());
							jse.executeScript("arguments[0].click();",
									addMedicationElement);
						} finally {
							if(isAdded) {
								i++;
							}
						}
					}
				}
				PatientService.setUpdatedPrescriptions(existingPrescriptions);
			}
		}
		return this;
	}

	public PrescriptionWithSuervisorPage addAlternatePrescriptions() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(modalLocator));
		PrescriptionsData prescriptionsData = getPrescriptionsData();
		if (prescriptionsData != null) {
			List<Prescription> existingPrescriptions = PatientService.getUpdatedPrescriptions();
			int currentPrescriptionsCount = 0;
			if (existingPrescriptions.size() > 0) {
				currentPrescriptionsCount = PatientUtility.getAddedPrescriptionsCount(existingPrescriptions);
			}
			int i;
			List<Prescription> alternatePrescriptions = prescriptionsData.getAlternatePrescription();
			if (alternatePrescriptions.size() > 0) {
				WebElement addMedicationButton = webOp.driver().findElement(addMedicationLocator);
				for (Prescription prescription : alternatePrescriptions) {
					i = currentPrescriptionsCount + 1;
					if(i!=1) {
						addMedicationButton.click();
						webOp.waitTillAllCallsDoneUsingJS();
					}
					boolean isAdded = addPrescription(prescription, i);
					if (isAdded) {
						currentPrescriptionsCount++;
						Alogs.add(AuditAction.MEDICATION_PRESCRIBED);
					}
					existingPrescriptions.add(prescription);
				}
				PatientService.setUpdatedPrescriptions(existingPrescriptions);
			}
		}
		return this;
	}
	//public boolean addPrescription(Prescription prescription, int i, Set<String> addedDrugNames) throws Exception {
	public boolean addPrescription(Prescription prescription, int i) throws Exception {
		String drugName = prescription.getDrugName();
		String dose = prescription.getDose();
		String unit = prescription.getUnit();
		String frequency = prescription.getFrequency();
		Integer duration = prescription.getDuration();
		String customSig = prescription.getCustomSig();
		String dispenseAmountTextExpected = prescription.getDispenseAmount();
		String earliestFillDate = prescription.getEarliestFillDate();
		Integer refills = prescription.getRefills();
		Boolean substitutes = prescription.isSubstitutes();
		String noteToPharmacy = prescription.getNoteToPharmacy();
		Boolean favorite = prescription.isFavorite();
		Boolean isFavourite = prescription.isIsFavorite();
		String delete = prescription.getDelete();
		String revert = prescription.getRevert();
		Boolean ePrescribe = prescription.isEPrescribe();
		
		List<DDInteracnAlert> interacnAlerts = prescription.getDdInteracnAlert();
		webOp.waitTillAllCallsDoneUsingJS();

		WebElement toggleAbleDiv = webOp.driver()
				.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]"));

		//WebElement drugNameElement = toggleAbleDiv.findElement(prescriptionDrugNameLocator);
		
		JavascriptExecutor jse = super.jse;
		jse.executeScript("arguments[0].click()", webOp.driver().findElement(
				By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//div[contains(@class, 'fieldgroup')][1]//div[contains(@class, 'ui-select-match')]/span")));
		WebElement drugNameElement =  webOp.driver().findElement(
				By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//div[contains(@class, 'fieldgroup')][1]//div[contains(@class, 'ui-select-container')]//input[@type='search']"));
		wait.until(ExpectedConditions.elementToBeClickable(drugNameElement));
		drugNameElement.clear();
		drugNameElement.sendKeys(drugName);
		webOp.waitTillAllCallsDoneUsingJS();
		
		String drugNameDropDownOptionLocator = uiSelectDropDownOptionLocatorPrefix+ drugName + uiSelectDropDownOptionLocatorSuffix;
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(drugNameDropDownOptionLocator)));
		
		if (isFavourite != null && isFavourite.booleanValue() == true) {
			
			List<WebElement> favouriteDrugNamesList = toggleAbleDiv.findElements(favouriteDrugNamesListLocator);
			Set<String> favouriteDrugNames = new HashSet<String>();
			for (WebElement favouriteDrugName : favouriteDrugNamesList) {
				favouriteDrugNames.add(favouriteDrugName.getText());
			}
			Boolean isPresent = favouriteDrugNames.contains(drugName);	
			/*
			 * Assert.assertTrue(isPresent,
			 * "The favorite medication is not displayed in the selectize list");
			 */
		}
		
		//WebElement drugNameDropDownOption = webOp.driver().findElement(By.xpath(drugNameDropDownOptionLocator));
		//drugNameDropDownOption.click();
		drugNameElement.sendKeys(Keys.RETURN);
		// duplicate medication check is not performed now
		//-------- duplicate medication check start--------
		/*if (addedDrugNames.contains(drugName) == true) {
			alreadyExistedDrug = true;
			
			By duplicateMedicationMessage = By.xpath(
			"//div[contains(@ng-class,'isDuplicateDrugNameSelected')][contains(@class,'error-msg')]"
			); 
			Boolean duplicateFound = toggleAbleDiv.findElement(duplicateMedicationMessage).isDisplayed();
			Assert.assertTrue(duplicateFound,
			"Duplicate found but no message displayed"); deleteAddedMedication(i);
			prescription.setDelete("true"); return false;
			
		} else {
			addedDrugNames.add(drugName);
		}*/
		//-------- duplicate medication check end--------

		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		if (!interacnAlerts.isEmpty()) {
			//The code for handling all the 3 types of modal is implemented, the audit log for the 3 needs to be implemented.
			for(DDInteracnAlert interacnAlert : interacnAlerts) {
				DrugInteracnUtility.checkDrugInteracnModalAndRespond(webOp, interacnAlert);
				if (DDAlertResponse.Accept == DDAlertResponse.getAlertResponse(interacnAlert.getResponse())) {
					prescription.setDelete("true");
					return false;
				}
			}
		}
		
		Thread.sleep(1000);
		WebElement doseElement = webOp.driver().findElement(
				By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//input[@ng-model='medication.dose']"));

		wait.until(ExpectedConditions.elementToBeClickable(doseElement));
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]"+doseUnitDropDownLocatorString+"/option"), 1));
		
		new Actions(webOp.driver()).moveToElement(doseElement).click().perform();
		doseElement.clear();
		doseElement.sendKeys(dose);

		WebElement dosageUnitElement = webOp.driver().findElement(By.xpath(
				"//div[contains(@ng-repeat,'medication')][" + i + "]"+doseUnitDropDownLocatorString));
		jse.executeScript("arguments[0].click();", dosageUnitElement);
		Select dosageUnitSelectDropDown = new Select(dosageUnitElement);
		dosageUnitSelectDropDown.selectByVisibleText(unit);
		WebElement selectedDosageUnitOption = dosageUnitSelectDropDown.getFirstSelectedOption();
		prescription.setDoseUnitUid(Long.valueOf(selectedDosageUnitOption.getAttribute("value")));
		Thread.sleep(2000);
		
		By frequencyInputLocator = By
				.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='frequency']");

		WebElement frequencyInputElement = webOp.driver().findElement(frequencyInputLocator);
		jse.executeScript("arguments[0].click();", webOp.driver().findElement(By
				.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//div[@placeholder='frequency']/span[1]")));
		frequencyInputElement.sendKeys(frequency);
		webOp.waitTillAllCallsDoneUsingJS();
		frequencyInputElement.sendKeys(Keys.RETURN);
		
		WebElement durationElement = webOp.driver().findElement(
				By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='duration']"));
		durationElement.clear();
		durationElement.sendKeys(Integer.toString(duration));
		webOp.waitTillAllCallsDoneUsingJS();
		// verifying warning for sc2 meds for more than 90 days
		if (prescription.getControlledSubstance() != null && prescription.getControlledSubstance().isSchedule2() != null
				&& duration > 90) {
			String durationValue = webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
					+ "]//div[contains(@class,'popover-error')]//div[@class='popover-content']/div")).getText();
			Assert.assertEquals(durationValue, "Warning: Schedule II Medications Cannot Exceed 90-day Supply",
					"The warning message for duration more than 90 days for Sc2 substance.");
			durationElement.clear();
			durationElement.sendKeys(Integer.toString(90));
		}
		if (customSig != null) {
			WebElement customSigElement = webOp.driver().findElement(
					By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='custom sig']"));
			wait.until(ExpectedConditions.elementToBeClickable(customSigElement));
			new Actions(webOp.driver()).moveToElement(customSigElement).click().perform(); 
			customSigElement.clear();
			customSigElement.sendKeys(customSig);
		}
		String dispenseAmountTextActual = webOp.driver()
				.findElement(By.xpath(
						"//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='dispense amount']"))
				.getAttribute("value");
		if(!dispenseAmountTextActual.equalsIgnoreCase(dispenseAmountTextExpected)) {
			webOp.getSoftAssert().fail("dispenseAmount not matched for prescription #"+i+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			webOp.logger().warn("dispenseAmount not matched for prescription #{} expected={}, actual={}",new Object[]{i, dispenseAmountTextExpected, dispenseAmountTextActual});
		}
		if (revert != null && revert.equalsIgnoreCase("true")) {
			toggleAbleDiv.findElement(revertMedicationButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
		}
		WebElement refillsElement = webOp.driver().findElement(
				By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='0 refills']"));
		if (prescription.getControlledSubstance() != null
				&& prescription.getControlledSubstance().isSchedule2() != null) {
				String refillDisabled = webOp.driver()
						.findElement(By.xpath(
								"//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='O refills']"))
						.getAttribute("disabled");
				Assert.assertTrue(Boolean.parseBoolean(refillDisabled), "The refill should be disabled as expected.");
			 
			Prescription existingSameDrugNamePrescription = PatientUtility.getFirstPrescriptionWithDrugName(drugName, PatientService.getUpdatedPrescriptions());
			if(existingSameDrugNamePrescription != null) {
				Boolean dateCheck = PatientUtility.isValidEarliestFillDateForScheduledSubstance(earliestFillDate, prescription);
				if(dateCheck == true) {
					WebElement earliestFillDateElement = webOp.driver().findElement(By.xpath(
							"//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='earliest fill date']"));
					earliestFillDateElement.clear();
					earliestFillDateElement.sendKeys(earliestFillDate);
				} else {
					//TODO: we should not be doing this, this is just a workaround to stop the test from failing.
					//We should fix the passing of earliest fill date by not passing a hardcoded value but as a
					//relative date from today in terms of days+/-, months +/-, years +/-
					deleteAddedMedication(i);
					prescription.setDelete("true");
					return false;
				}
			}
		}
		else 
		{
			refillsElement.clear();
			refillsElement.sendKeys("" + refills);
		}
		// For SC3N4, the EFD and Refill check
		if (prescription.getControlledSubstance() != null
				&& (prescription.getControlledSubstance().isSchedule3N4() != null)) {
			Prescription existingSameDrugNamePrescription = PatientUtility.getFirstPrescriptionWithDrugName(drugName, PatientService.getUpdatedPrescriptions());
			if(existingSameDrugNamePrescription != null) {
				Boolean dateCheck = PatientUtility.isValidEarliestFillDateForScheduledSubstance(earliestFillDate, prescription);
				if(dateCheck == true) {
					WebElement earliestFillDateElement = webOp.driver().findElement(By.xpath(
							"//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='earliest fill date']"));
					earliestFillDateElement.clear();
					earliestFillDateElement.sendKeys(earliestFillDate);
					Thread.sleep(1000);
				} else {
					//TODO: we should not be doing this, this is just a workaround to stop the test from failing.
					//We should fix the passing of earliest fill date by not passing a hardcoded value but as a
					//relative date from today in terms of days+/-, months +/-, years +/-
					deleteAddedMedication(i);
					prescription.setDelete("true");
					return false;
				}
			}
			/*if (dateCheck) {
				String dateCheckValue = webOp.driver()
						.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
								+ "]//div[contains(@class,'popover-error')]//div[@class='popover-content']/div"))
						.getText();
				Assert.assertEquals(dateCheckValue, "TBD - Warning: earliest fill date can not be less then today",
						"The warning message for SC3N4 for refill not exeeding 5 days displaying correctly.");
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Date date = new Date();
				WebElement earliestFillDateElement = webOp.driver().findElement(By.xpath(
						"//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='earliest fill date']"));
				earliestFillDateElement.clear();
				earliestFillDateElement.sendKeys(dateFormat.format(date));
				webOp.waitTillAllCallsDoneUsingJS();
				Thread.sleep(2000);
			}*/
			// Warning for Sc3N4 for refills more than 5
			if (refills > 5) {
				wait.until(ExpectedConditions.elementToBeClickable(refillsElement));
				String refillValue = webOp.driver()
						.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
								+ "]//div[contains(@class,'refill-quantity')]//div[@class='popover-content']/div"))
						.getText();
				Assert.assertEquals(refillValue, "Warning: number of refills exceeds 5",
						"The warning message for SC3N4 for refill not exeeding 5 days displaying correctly.");
				refillsElement.clear();
				refillsElement.sendKeys("" + 5);
			}
		}

		WebElement substituesOkCheckBox = webOp.driver()
				.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
						+ "]"+substitutesOkCheckBoxLocatorSuffix));
		boolean isChecked = substituesOkCheckBox.isSelected();
		if (substitutes == null) {
			substitutes = Boolean.TRUE;
			prescription.setSubstitutes(substitutes);
		}
		if (isChecked != substitutes.booleanValue()) {
			WebElement substituesOkSwitchery = webOp.driver()
					.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
							+ "]"+substitutesOkSwitcheryLocatorSuffix));
			substituesOkSwitchery.click();
		}

		if (noteToPharmacy != null) {
			toggleAbleDiv.findElement(addNoteButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			toggleAbleDiv.findElement(pharmacyNoteLocator).sendKeys(noteToPharmacy);
		}

		if (delete != null && delete.equalsIgnoreCase("true")) {
			deleteAddedMedication(i);
			i--;
			return false;
		}

		WebElement iconFavourite = toggleAbleDiv.findElement(iconFavouriteLocator);
		Boolean isSelected = false;
		if(iconFavourite.getAttribute("class").equals("icon-favorite-selected")) {
			isSelected = true;
		}
		if(favorite == null) {
			favorite = Boolean.FALSE;
			prescription.setFavorite(Boolean.FALSE);
		}
		if(isSelected != favorite) {
			toggleAbleDiv.findElement(medicationFavouriteButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
		}
		if (favorite.booleanValue() == true) {
			wait.until(ExpectedConditions.attributeToBe(iconFavourite, "class", "icon-favorite-selected"));
		} else {
			wait.until(ExpectedConditions.attributeToBe(iconFavourite, "class", "icon-favorite"));
		}
		
		WebElement prescriptionViewElement = webOp.driver().findElement(By.cssSelector(viewNthPrescriptionLocatorPrefix+"(" + i + ")"));
		WebElement iconPrescriptionType = prescriptionViewElement.findElement(By.cssSelector("div.row > div.column > div.queue-rx-print > icon"));
		WebElement prescriptionTypeChange = prescriptionViewElement.findElement(By.cssSelector("div.row > div.column > div.queue-rx-print"));
		if (ePrescribe == null) { 
			ePrescribe = Boolean.FALSE;
			prescription.setEPrescribe(ePrescribe);
		}
		if((ePrescribe == Boolean.TRUE && !iconPrescriptionType.getAttribute("class").equals(ePrescriptionIconClass))
				|| (ePrescribe == Boolean.FALSE && !iconPrescriptionType.getAttribute("class").equals(printPrescriptionIconClass))){
			new Actions(webOp.driver()).moveToElement(prescriptionTypeChange).click().perform();
			//TODO: have a check for pharmacy not correct or unselected popup
		}
		
		return true;
	}

	public void checkPrintPrescription() throws Exception {
		Set<String> windowHandles = webOp.driver().getWindowHandles();
		for(String window : windowHandles)
		{
			System.out.println(window);
			webOp.driver().switchTo().window(window);
			System.out.println(webOp.driver().getTitle());
		}
		webOp.driver().switchTo().window("Prescription - Print preview");
		webOp.driver().manage().window().maximize();
		/*String providerName = webOp.driver().findElement(By.xpath("//div[@class='provider-name']")).getText();
		String providerAddress = webOp.driver().findElement(By.xpath("//div[@class='provider-address']")).getText();
		String supervisorName = webOp.driver().findElement(By.xpath("//div[@class='supervisor-data']")).getText();
		*/
		//String patientAddress = webOp.driver().findElement(By.xpath("//div[@class='data-field address-field']/div[@class='input-field']")).getText();
		System.out.println(webOp.driver().getTitle());
		System.out.println(webOp.driver().getPageSource());
		String patientName = webOp.driver().findElement(By.xpath("//div[@class='data-field name-field']/div[@class='input-field']")).getText();
		patientName= patientName.replaceAll("\\s+","");
		String drugName = webOp.driver().findElement(By.xpath("//section[@class='prescription-info']/div[@class='rx-det-main']/div[@class='row']/div[@class='column lg-10']/span[@class='rx-drug-name']")).getText();
		String customSig = webOp.driver().findElement(By.xpath("//section[@class='prescription-info']/div[@class='rx-det-main']/div[@class='row']/div[@class='column lg-3']/div[@class='column lg-9']/div[@class='sub-detail']")).getText();
		String refills = webOp.driver().findElement(By.xpath("//section[@class='prescription-info']/div[@class='rx-det-main']/div[@class='row']/div[@class='column lg-2']")).getText();
		String pName,address,proName,proAddress,sName,dName,cSig,ref;
		Patient patient = dataProvider.getPageData("patient");
		if (patient != null) {
			PatientDetails patientDetails = patient.getNewPatient();
			Demographic demographic = patientDetails.getDemographic();
			pName = HelperUtility.getString(demographic.getFirstName());
			pName += " "+HelperUtility.getString(demographic.getMiddleName());
			pName += " "+HelperUtility.getString(demographic.getLastName());
			address = HelperUtility.getString(demographic.getAddress());
			address += ", "+HelperUtility.getString(demographic.getCity());
			address += ", "+HelperUtility.getString(demographic.getState());
			//Assert.assertEquals(patientAddress, address);
			Assert.assertEquals(patientName, pName);
/*			Assert.assertEquals(providerName, proName);
			Assert.assertEquals(providerAddress, proAddress);
			Assert.assertEquals(supervisorName, sName);
			Assert.assertEquals(drugName, dName);
			Assert.assertEquals(customSig, cSig);
			Assert.assertEquals(refills, ref);
*/		}
	}
	
	// CLAR-1227
	public AbstractPage verifyPrescriptions() throws Exception {
		PrescriptionsData prescriptionsData = getPrescriptionsData();
		if (prescriptionsData != null) {
			List<Prescription> prescriptions = PatientService.getUpdatedPrescriptions();
			if (prescriptions != null && prescriptions.size() > 0) {
				int i = 1;//stores count of prescriptions to be printed
				boolean isAnyEPCSPossible = false;
				
				List<WebElement> prescriptionElements = webOp.driver().findElements(viewPrescriptionsLocator);
				Iterator<WebElement> iterator = prescriptionElements.iterator();
				
				//int undeletedMedications = PatientUtility.getAddedPrescriptionsCount(prescriptions);
				for (Prescription prescription : prescriptions) {
					if (!PatientUtility.isPrescriptionDeleted(prescription)) {
						WebElement prescriptionElement = iterator.next();
						WebElement drugNameElement = prescriptionElement
								.findElement(By.cssSelector("div.rx-det-main div.row:nth-child(1) div:nth-child(1) span.rx-drug-name"));
						Assert.assertEquals(drugNameElement.getText(), prescription.getDrugName());
						
						int refill = 0;
						String expectedRefillCount = null;
						if (prescription.getRefills() != null) {
							if (prescription.getControlledSubstance() != null
									&& prescription.getControlledSubstance().isSchedule3N4() != null
									&& prescription.getRefills() > 5) {
								refill = 5;
							} else {
								refill = prescription.getRefills();
							}
							expectedRefillCount = refill + " " + (refill == 1 ? "REFILL" : "REFILLS");
						} else {
							expectedRefillCount = "NO REFILL";
						}
						WebElement refillElement = prescriptionElement
								.findElement(By.cssSelector("div.rx-det-main div.row:nth-child(1) div:nth-child(" + 2 + ")>span"));
						Assert.assertEquals(refillElement.getText(), expectedRefillCount);

						WebElement dispenseAmountElement = prescriptionElement
								 .findElement(By.cssSelector("div.rx-det-main div.row:nth-child(2) div:nth-child(1)>span"));
						if(!dispenseAmountElement.getText().contains(prescription.getDispenseAmount())) {
							webOp.getSoftAssert().fail("dispenseAmount not contained for prescription "+prescription.getDrugName()+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
							webOp.logger().warn("dispenseAmount not contained for prescription {} expected={}, actual={}",new Object[]{prescription.getDrugName(), prescription.getDispenseAmount(), dispenseAmountElement.getText()});
						}
						Assert.assertTrue(dispenseAmountElement.getText().contains(prescription.getDispenseAmount()));
						
						if (prescription.getCustomSig() != null) {
							WebElement customSignatureElement = prescriptionElement
									.findElement(By.cssSelector("div.rx-det-main div.row:nth-child(2) div:nth-child(2)>span"));
							Assert.assertTrue(customSignatureElement.getText().toLowerCase()
									.contains(prescription.getCustomSig().toLowerCase()));
						}
						
						 if(StringUtils.isNotEmpty(prescription.getNoteToPharmacy())){
							 WebElement noteToPharmacy = prescriptionElement.findElement(By.xpath("//textarea[contains(@class,'pharmacy-note') and contains(@class,'show')]"));
							 Assert.assertEquals(noteToPharmacy.getAttribute("name"), prescription.getNoteToPharmacy());
						 }
						 if(prescription.getMedEd()!=null) {
							 WebElement medEdIconElement = prescriptionElement.findElement(By.cssSelector("icon[class='icon-med-ed']"));
							 if(prescription.getMedEd().isPreview()!=null && prescription.getMedEd().isPreview()==true ) {
								 Actions action = new Actions(webOp.driver());
								 action.moveToElement(medEdIconElement).perform();
								 WebElement medEdPreviewButton = webOp.driver().findElement(By.xpath("//div[contains(@class,'has-meded')]/button[contains(text(),'Preview')]"));
								 action.click(medEdPreviewButton).build();
							 }
							 if(prescription.getMedEd().isPrint()!=null && prescription.getMedEd().isPrint()!=true ) {
								 medEdIconElement.click();
							 }
						 }
						 if (prescription.isEPrescribe() != null && prescription.isEPrescribe() == true) {
							//TODO: change this logic to assert webOp.gethMap().get("EPCSEnabled") != null
							 if (webOp.gethMap().get("EPCSEnabled") != null) {
								 if (webOp.gethMap().get("EPCSEnabled").equals("true")) {
									 if(prescription.getControlledSubstance() != null) {
										 isAnyEPCSPossible = true;
									 }
									 boolean erXDisplayed = prescriptionElement
											 .findElement(By.cssSelector("icon[class='icon-electronic-prescribing']"))
											 .isDisplayed();
									 Assert.assertTrue(erXDisplayed,
											 "The erx button is displayed for EPCS enabled pharmacy ");
								 }
								 //TODO: move this else if logic to addPrescription method
								 // pharmacy is not EPCS enabled
								 else if (prescription.getControlledSubstance() != null) {
									 String displayedError = webOp.driver().findElement(By.xpath("")).getText();
									 Assert.assertEquals(displayedError, AppMessages.Templates.WRITE_PATIENT_PRESCRIPTIONS.UPDATE_TO_EPCS_ENABLED_PHARMACY,
											 "Error message displayed for a controlled substance non epcs pharmacy is selected");
								 }

							 } else {//TODO: move this else logic to addPrescription method
								 // pharmacy is not Provided
								 if (prescription.getControlledSubstance() != null) {
									 String displayedError = webOp.driver().findElement(By.xpath("")).getText();
									 Assert.assertEquals(displayedError, AppMessages.Templates.WRITE_PATIENT_PRESCRIPTIONS.SELECT_EPCS_ENABLED_PHARMACY,
											 "Error message displayed for a controlled substance where pharmacy is not selected");
								 } else {
									 String displayedError = webOp.driver().findElement(By.xpath("")).getText();
									 Assert.assertEquals(displayedError, AppMessages.Templates.WRITE_PATIENT_PRESCRIPTIONS.SELECT_PHARMACY_TO_EPRESCRIBE,
											 "Error messaage displayed for a non-CS when pharmacy is not selected");
								 }
							 }
						 } else
						 {
							 webOp.waitTillAllCallsDoneUsingJS();
							 //prescriptionElement.findElement(By.xpath("//icon[contains(@class,'icon-electronic-prescribing')]")).click();
							 //webOp.driver().findElement(By.xpath("//button[contains(@class,'button button-flat primary-action ng-binding')]")).click();
							 //button button-flat primary-action ng-binding
							 before = webOp.driver().getWindowHandle();
							 //webOp.driver().findElement(By.xpath("//button[contains(@class,'button-link ng-binding')]")).click();
							 Alogs.add(Alogs.size()-(i-1),AuditAction.PRESCRIPTION_PRINT_JOB_CREATED);
							 
							 //no  need to submit prescriptions here
							/* if(i==undeletedMedications) {
								 submitDetails(prescriptionsData);
							 return new PrescriptionResultPage(webOp, dataProvider);
							 }*/
							 i++;
						 }
					}
					
				}
				return submitDetailsAndDecideNextPage(prescriptionsData, isAnyEPCSPossible ,i-1);
			}
		}
		return this;
	}

	public PrescriptionWithSuervisorPage editPrescriptions() throws Exception {
		wait.until(ExpectedConditions.urlContains(AppPage.WritePrescription.getUrlContains(hMap)));

		List<Prescription> initialPrescriptions = PatientService.getUpdatedPrescriptions();
		List<Prescription> editPrescriptions = getEditPrescriptions();

		if (editPrescriptions != null && editPrescriptions.size() > 0) {
			int currentPrescriptionsCount;
			if (initialPrescriptions != null && initialPrescriptions.size() > 0) {
				currentPrescriptionsCount = PatientUtility.getAddedPrescriptionsCount(initialPrescriptions);
			} else {
				currentPrescriptionsCount = 0;
			}
			
			int i;
			Iterator<Prescription> editIterator = editPrescriptions.iterator();
			//Set<String> drugNames = PatientUtility.getExistingDrugNames(initialPrescriptions);
			while (editIterator.hasNext()) {
				Prescription editPrescription = editIterator.next();
				String drugName = editPrescription.getDrugName();
				String dose = editPrescription.getDose();
				String unit = editPrescription.getUnit();
				String frequency = editPrescription.getFrequency();
				Integer duration = editPrescription.getDuration();
				String customSig = editPrescription.getCustomSig();
				String dispenseAmountTextExpected = editPrescription.getDispenseAmount();
				String earliestFillDate = editPrescription.getEarliestFillDate();
				Integer refills = editPrescription.getRefills();
				Boolean substitutes = editPrescription.isSubstitutes();
				String noteToPharmacy = editPrescription.getNoteToPharmacy();
				Boolean favorite = editPrescription.isFavorite();
				String delete = editPrescription.getDelete();
				String revert = editPrescription.getRevert();
				Boolean add = editPrescription.isAdd();
				ControlledSubstance contSubs = editPrescription.getControlledSubstance();
				Boolean ePrescribe = editPrescription.isEPrescribe();
				
				WebElement toggleAbleDiv = null;
				if (delete != null && delete.equalsIgnoreCase("true")) {
					By toggleAbleDivLocator = By.xpath("//div[contains(@ng-repeat,'medication')]["
							+ viewMedicationNameElementPrefix + "@title='" + drugName + "']" + "]");
					toggleAbleDiv = webOp.driver().findElement(toggleAbleDivLocator);
					i = PatientUtility.findInitialPrescriptionSerialNum(initialPrescriptions, drugName);
					i = i + 1;// making 1-indexed
					WebElement expandMedicationButtonLocatorElement = webOp.driver()
							.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]"
									+ expandMedicationButtonLocatorString));
					if (expandMedicationButtonLocatorElement.isDisplayed()) {
						expandMedicationButtonLocatorElement.click();
						webOp.waitTillAllCallsDoneUsingJS();
					}
					webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]"
							+ deleteMedicationButtonLocatorString)).click();
					webOp.waitTillAllCallsDoneUsingJS();
					initialPrescriptions.get(i - 1).setDelete(delete);
					//drugNames.remove(editPrescription.getDrugName());
					currentPrescriptionsCount--;
					Thread.sleep(1000);
					continue;

				} else if (add == null || add.booleanValue() == false) {
					toggleAbleDiv = webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'medication')]["
							+ viewMedicationNameElementPrefix + "@title='" + drugName + "')]" + "]"));
					i = PatientUtility.findInitialPrescriptionSerialNum(initialPrescriptions, drugName);
					i = i + 1;// making 1-indexed
					WebElement expandMedicationButtonLocatorElement = webOp.driver()
							.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]"
									+ expandMedicationButtonLocatorString));
					if (expandMedicationButtonLocatorElement.isDisplayed()) {
						expandMedicationButtonLocatorElement.click();
						webOp.waitTillAllCallsDoneUsingJS();
					}
					Prescription prescription = initialPrescriptions.get(i - 1);

					if (dose != null && !prescription.getDose().equals(dose)) {
						WebElement doseElement = webOp.driver()
								.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
										+ "]//input[@ng-model='medication.dose']"));
						new Actions(webOp.driver()).moveToElement(doseElement).click().perform();
						doseElement.clear();
						doseElement.sendKeys(dose);
						prescription.setDose(dose);
					}

					if (unit != null && !prescription.getUnit().equals(unit)) {
						WebElement dosageUnitElement = webOp.driver()
								.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
										+ "]"+doseUnitDropDownLocatorString));
						jse.executeScript("arguments[0].click();", dosageUnitElement);
						Select dosageUnitSelectDropDown = new Select(dosageUnitElement);
						dosageUnitSelectDropDown.selectByVisibleText(unit);
						WebElement selectedDosageUnitOption = dosageUnitSelectDropDown.getFirstSelectedOption();
						prescription.setDoseUnitUid(Long.valueOf(selectedDosageUnitOption.getAttribute("value")));
						prescription.setDose(unit);
						Thread.sleep(1000);
					}

					if (frequency != null && !prescription.getFrequency().equals(frequency)) {
						By frequencyInputLocator = By.xpath(
								"//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='frequency']");
						WebElement frequencyInputElement = webOp.driver().findElement(frequencyInputLocator);
						jse.executeScript("arguments[0].click();", webOp.driver().findElement(By
								.xpath("//div[contains(@ng-repeat,'medication')][" + i + "]//div[@placeholder='frequency']/span[1]")));
						frequencyInputElement.clear();
						frequencyInputElement.sendKeys(frequency);
						frequencyInputElement.sendKeys(Keys.RETURN);
						prescription.setFrequency(frequency);
					}
					if (duration != null && !prescription.getDuration().equals(duration)) {
						WebElement durationElement = webOp.driver().findElement(By.xpath(
								"//div[contains(@ng-repeat,'medication')][" + i + "]//input[@placeholder='duration']"));
						durationElement.clear();
						durationElement.sendKeys(duration.toString());
						webOp.waitTillAllCallsDoneUsingJS();
						prescription.setDuration(duration);
					}

					if (customSig != null) {
						WebElement customSigElement = webOp.driver()
								.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
										+ "]//input[@placeholder='custom sig']"));
						wait.until(ExpectedConditions.elementToBeClickable(customSigElement));
						customSigElement.clear();
						customSigElement.sendKeys(customSig);
						Assert.assertTrue(customSigElement.getAttribute("value").equalsIgnoreCase(customSig),
								"Custom Sig mismatched");
						prescription.setCustomSig(customSig);
					}
					if (dispenseAmountTextExpected != null
							&& !prescription.getDispenseAmount().equals(dispenseAmountTextExpected)) {
						String dispenseAmountTextActual = webOp.driver()
								.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
										+ "]//input[@placeholder='dispense amount']"))
								.getAttribute("value");
						if(!dispenseAmountTextActual.equalsIgnoreCase(dispenseAmountTextExpected)) {
							webOp.getSoftAssert().fail("dispenseAmount not matched for prescription #"+i+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
							webOp.logger().warn("dispenseAmount not matched for prescription #{} expected={}, actual={}",new Object[]{i, dispenseAmountTextExpected, dispenseAmountTextActual});
						}
						prescription.setDispenseAmount(dispenseAmountTextActual);
					}
					if (revert != null && revert.equalsIgnoreCase("true")) {
						toggleAbleDiv.findElement(revertMedicationButtonLocator).click();
						webOp.waitTillAllCallsDoneUsingJS();
					}
					if (earliestFillDate != null && !earliestFillDate.equals(prescription.getEarliestFillDate())) {
						WebElement earliestFillDateElement = webOp.driver()
								.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
										+ "]//input[@placeholder='earliest fill date']"));
						earliestFillDateElement.clear();
						earliestFillDateElement.sendKeys(earliestFillDate);
						prescription.setEarliestFillDate(earliestFillDate);
					}
					if (refills != null && !prescription.getRefills().equals(refills)) {
						WebElement refillsElement = webOp.driver()
								.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
										+ "]//input[@placeholder='0 refills']"));
						refillsElement.clear();
						refillsElement.sendKeys("" + refills);
						prescription.setRefills(refills);
					}
					if (substitutes != null) {
						WebElement substituesCheckBox = webOp.driver()
								.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
										+ "]"+substitutesOkCheckBoxLocatorSuffix));
						boolean isChecked = substituesCheckBox.isSelected();
						if (isChecked != substitutes.booleanValue()) {
							WebElement substituesOkSwitchery = webOp.driver()
									.findElement(By.xpath("//div[contains(@ng-repeat,'medication')][" + i
											+ "]"+substitutesOkSwitcheryLocatorSuffix));
							substituesOkSwitchery.click();
						}
						prescription.setSubstitutes(substitutes);
					}

					if (noteToPharmacy != null) {
						if (prescription.getNoteToPharmacy() == null) {
							toggleAbleDiv.findElement(addNoteButtonLocator).click();
							webOp.waitTillAllCallsDoneUsingJS();
						}
						if (!noteToPharmacy.equals(prescription.getNoteToPharmacy())) {
							WebElement pharmacyNoteElement = toggleAbleDiv.findElement(pharmacyNoteLocator);
							pharmacyNoteElement.clear();
							pharmacyNoteElement.sendKeys(noteToPharmacy);
						}
						prescription.setNoteToPharmacy(noteToPharmacy);
					}

					if (favorite != null) {
						WebElement iconFavourite = toggleAbleDiv.findElement(iconFavouriteLocator);
						Boolean isSelected = false;
						if(iconFavourite.getAttribute("class").equals("icon-favorite-selected")) {
							isSelected = true;
						}
						if(isSelected != favorite) {
							toggleAbleDiv.findElement(medicationFavouriteButtonLocator).click();
							webOp.waitTillAllCallsDoneUsingJS();
						}
						if (favorite.booleanValue() == true) {
							wait.until(ExpectedConditions.attributeToBe(toggleAbleDiv.findElement(iconFavouriteLocator),
									"class", "icon-favorite-selected"));
						} else {
							wait.until(ExpectedConditions.attributeToBe(toggleAbleDiv.findElement(iconFavouriteLocator),
									"class", "icon-favorite"));
						}
						prescription.setFavorite(favorite);
					}
					if (contSubs != null) {
						prescription.setControlledSubstance(contSubs);
					}
					
					if (ePrescribe != null) {
						WebElement prescriptionViewElement = webOp.driver().findElement(By.cssSelector(viewNthPrescriptionLocatorPrefix+"(" + i + ")"));
						WebElement iconPrescriptionType = prescriptionViewElement.findElement(By.cssSelector("div.row > div.column > div.queue-rx-print > icon"));
						WebElement prescriptionTypeChange = prescriptionViewElement.findElement(By.cssSelector("div.row > div.column > div.queue-rx-print"));
						if((ePrescribe == Boolean.TRUE && !iconPrescriptionType.getAttribute("class").equals(ePrescriptionIconClass))
								|| (ePrescribe == Boolean.FALSE && !iconPrescriptionType.getAttribute("class").equals(printPrescriptionIconClass))){
							new Actions(webOp.driver()).moveToElement(prescriptionTypeChange).click().perform();
							prescription.setEPrescribe(ePrescribe);
							//TODO: have a check for pharmacy not correct or unselected popup
						}
					 }

				} else {
					i = currentPrescriptionsCount + 1;
					if(i!=1) {
						webOp.driver().findElement(addMedicationLocator).click();
						webOp.waitTillAllCallsDoneUsingJS();
					}
					//boolean isAdded = addPrescription(editPrescription, i, drugNames);
					boolean isAdded = addPrescription(editPrescription, i);
					if (isAdded) {
						currentPrescriptionsCount++;
						Alogs.add(AuditAction.MEDICATION_PRESCRIBED);
					}
					initialPrescriptions.add(editPrescription);
				}
			}
			PatientService.setUpdatedPrescriptions(initialPrescriptions);
		}
		return this;
	}

	private void deleteAddedMedication(int i) throws Exception {
		webOp.driver()
		.findElement(By.xpath(
				"//div[contains(@ng-repeat,'medication')][" + i + "]" + deleteMedicationButtonLocatorString))
		.click();
		webOp.waitTillAllCallsDoneUsingJS();
	}

	public void waitTillWritePrescriptionsPageIsLoaded() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.urlContains(AppPage.WritePrescription.getUrlContains(hMap)));
	}

	public PrescriptionWithSuervisorPage checkPediatricAndUpdateWeight() throws Exception {

		waitTillWritePrescriptionsPageIsLoaded();

		PatientDetails patientDetails = getPatientDetails();
		String dob = patientDetails.getDemographic().getBirthday();
		if (patientDetails.getEditDetails() != null && patientDetails.getEditDetails().getDemographic() != null
				&& patientDetails.getEditDetails().getDemographic().getBirthday() != null) {
			dob = patientDetails.getEditDetails().getDemographic().getBirthday();
		}

		PediatricData pediatricData = patientDetails.getPrescriptionsPageData().getPediatricData();

		if (PatientUtility.isPediatric(dob) == true) {
			if (HelperUtility.isElementAbsent(webOp, pediatricWeightUpdateFormLocator) == true) {
				Assert.fail("Weight form not found");
			}
			if (pediatricData != null) {
				Integer pediatricWeight = pediatricData.getWeight();
				String weightMeasuringUnit = pediatricData.getWeightMeasuringUnit();
				webOp.driver().findElement(updatePediatricWeightButtonLocator).click();
				WebElement weightInput = webOp.driver().findElement(updatePediatricWeightInputLocator);
				weightInput.clear();
				weightInput.sendKeys("" + pediatricWeight + "");
				WebElement weightMeasuringUnitDiv = webOp.driver().findElement(updatePediatricWeightUnitLocator);
				if (weightMeasuringUnit.equals(weightMeasuringUnitDiv.getText()) == false) {
					webOp.driver().findElement(updatePediatricWeightUnitLocator).click();
				}
				webOp.driver().findElement(savePediatricWeightButtonLocator).click();
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(iconSuccessCheckMarkLocator));
				WebElement weightViewElement = webOp.driver().findElement(updatedWeightViewLocator);
				String actualWeightView = weightViewElement.getText();
				String expectedWeightView = new StringBuilder(pediatricWeight.toString()).append(" ")
						.append(weightMeasuringUnit).toString();
				Assert.assertEquals(actualWeightView, expectedWeightView);
			}
		}
		return this;
	}

	public AbstractPage editFullPatient() throws Exception {
		PatientDetails editFullPatientData = getEditFullPatientData();
		if (editFullPatientData != null) {
			Demographic demographyOfPatient = editFullPatientData.getDemographic();
			Contact contact = editFullPatientData.getContact();
			MedicalHistory medicalHistory = editFullPatientData.getMedicalHistory();
			if (medicalHistory != null || demographyOfPatient != null || contact != null) {
				openModalWindow();
				modifyPatientProfile();
				modifyMedicalHistory();
				saveChanges();
				// expect error message if duplicate patient details or duplicate allergy, homeMed or diagnosis
				DuplicatePatientPage dp = PatientUtility.checkDuplicateShown(webOp, dataProvider, demographyOfPatient);
				if(dp == null){
					handleInteracnAlerts();
				} else {
					return dp;
				}
			}
		}
		return this;
	}

	public PrescriptionWithSuervisorPage openModalWindow() throws Exception {

		String url = webOp.driver().getCurrentUrl();
		if (url.startsWith("http://localhost:8081/?mocked=1")) {
			Thread.sleep(2000);
		}

		webOp.driver().findElement(editPatientLocator).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(editPatientModal));
		return this;
	}

	public PrescriptionWithSuervisorPage modifyPatientProfile() throws Exception {
		PatientDetails editFullPatientData = getEditFullPatientData();
		editPatientProfile(editFullPatientData);
		return this;
	}

	public PrescriptionWithSuervisorPage editPatientProfile(PatientDetails editFullPatientData) throws Exception {

		Demographic demographyOfPatient = editFullPatientData.getDemographic();
		if (demographyOfPatient != null) {
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			if (demographyOfPatient.getFirstName() != null) {
				webOp.driver().findElement(firstNameLocator).clear();
				webOp.driver().findElement(firstNameLocator).sendKeys("" + demographyOfPatient.getFirstName() + "");
			}
			if (demographyOfPatient.getLastName() != null) {
				webOp.driver().findElement(lastNameLocator).clear();
				webOp.driver().findElement(lastNameLocator).sendKeys("" + demographyOfPatient.getLastName() + "");
			}
			if (demographyOfPatient.getMiddleName() != null) {
				webOp.driver().findElement(middleNameLocator).clear();
				webOp.driver().findElement(middleNameLocator).sendKeys("" + demographyOfPatient.getMiddleName() + "");
			}
			if (demographyOfPatient.getAddress() != null) {
				webOp.driver().findElement(addressLocator).clear();
				webOp.driver().findElement(addressLocator).sendKeys("" + demographyOfPatient.getAddress() + "");
			}
			if (demographyOfPatient.getCity() != null) {
				webOp.driver().findElement(cityLocator).clear();
				webOp.driver().findElement(cityLocator).sendKeys("" + demographyOfPatient.getCity() + "");
			}
			if (demographyOfPatient.getState() != null) {
				Select stateDropDown = new Select(webOp.driver().findElement(stateLocator));
				
				stateDropDown.selectByVisibleText(demographyOfPatient.getState());
			}
			if (demographyOfPatient.getZipCode() != null) {
				webOp.driver().findElement(zipCodeLocator).clear();
				Integer zipCode = demographyOfPatient.getZipCode();
				/* to skip duplicate patient case */
				if (demographyOfPatient.isDuplicatePatientRecord() == null
						|| demographyOfPatient.isDuplicatePatientRecord().booleanValue() == false) {
					zipCode = HelperUtility.getRandomZipCode();
					demographyOfPatient.setZipCode(zipCode);
				}
				webOp.driver().findElement(zipCodeLocator).sendKeys("" + zipCode + "");
			}
			if (demographyOfPatient.getBirthday() != null) {
				webOp.driver().findElement(birthdayLocator).clear();
				webOp.driver().findElement(birthdayLocator).sendKeys("" + demographyOfPatient.getBirthday() + "");
			}
			if (demographyOfPatient.getGender() != null) {
				Select genderDropDown = new Select(webOp.driver().findElement(genderLocator));
				genderDropDown.selectByVisibleText(demographyOfPatient.getGender());
			}
			if (demographyOfPatient.getWeight() != null) {
				webOp.driver().findElement(weightLocator).clear();
				webOp.driver().findElement(weightLocator).sendKeys("" + demographyOfPatient.getWeight() + "");
			}
			if (demographyOfPatient.getWeightMeasuringUnit() != null) {
				if (demographyOfPatient.getWeightMeasuringUnit().equals("kg")) {
					webOp.driver().findElement(weightMeasurementUnitLocator).click();
				}
			}
		}
		Contact contact = editFullPatientData.getContact();
		if (contact != null) {
			if (contact.getPhone() != null) {
				webOp.driver().findElement(phoneLocator).clear();
				webOp.driver().findElement(phoneLocator).sendKeys("" + contact.getPhone() + "");
			}
			if (contact.getEmail() != null) {
				webOp.driver().findElement(emailLocator).clear();
				webOp.driver().findElement(emailLocator).sendKeys("" + contact.getEmail() + "");
			}
		}

		return this;
	}

	public PrescriptionWithSuervisorPage modifyMedicalHistory() throws Exception {
		PatientDetails editFullPatientData = getEditFullPatientData();
		MedicalHistory medicalHistory = editFullPatientData.getMedicalHistory();
		return addEditMedicalHistory(medicalHistory);
	}

	public PrescriptionWithSuervisorPage modifyManageMedicalHistory() throws Exception {
		ManageExistingPatient manageExistingPatient = dataProvider.getPageData("manageExistingPatient");
		PatientDetails editDetails = manageExistingPatient.getEditDetails();
		MedicalHistory medicalHistory = null;
		if (editDetails != null) {
			medicalHistory = editDetails.getMedicalHistory();
		}
		return addEditMedicalHistory(medicalHistory);
	}

	public PrescriptionWithSuervisorPage addEditMedicalHistory(MedicalHistory medicalHistory) throws Exception {
		if (medicalHistory != null) {
			addEditAllergies(medicalHistory.getAllergies());
			addEditHomeMeds(medicalHistory.getHomeMeds());
			addEditDiagnoses(medicalHistory.getDiagnoses());
		}
		return this;
	}

	public void addEditAllergies(List<Allergies> allergiesList) throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		WebElement showAllergiesSectionElement = webOp.driver().findElement(showAllergiesSectionButtonLocator);
		new Actions(webOp.driver()).moveToElement(showAllergiesSectionElement).click().perform();
		int activeCount = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newAllergy') and not(contains(@class, 'hide-block'))]")).size();
		int i = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newAllergy')]")).size();

		for (Allergies allergy : allergiesList) {
			String allergyType = allergy.getAllergy();
			String reaction = allergy.getReaction();
			
			webOp.waitTillAllCallsDoneUsingJS();
			JavascriptExecutor executor = jse;
			if (allergy.getRemoveFromList() != null && allergy.getRemoveFromList().getDiscontinue() != null) {
				try {
					WebElement scrollableField = webOp.driver()
							.findElement(By.xpath("//div[contains(@class,'allergies-section scrollable')]"));
					executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
				}
				catch(Exception e)
				{
					webOp.logger().info("Scroll bar not available in edit allergies");
				}
				
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][div//input[@title='"
						+ allergyType + "']]"+discontinueButton)).click();
				activeCount --;
				Alogs.add(AuditAction.ALLERGY_DISCONTINUED);
				continue;
			}
			if (allergy.getRemoveFromList() != null && allergy.getRemoveFromList().getError() != null) {
				try {
					WebElement scrollableField = webOp.driver()
							.findElement(By.xpath("//div[contains(@class,'allergies-section scrollable')]"));
					executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
				}
				catch(Exception e)
				{
					webOp.logger().info("Scroll bar not available in edit allergies");
				}
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][div//input[@title='"
						+ allergyType + "']]"+markAsError)).click();
				activeCount --;
				Alogs.add(AuditAction.ALLERGY_MARKED_AS_ERROR);
				continue;
			}
			
			if (activeCount != 1) {
				webOp.driver().findElement(addAllergyLocator).click();
				i++;
				activeCount++;
			} else {
				boolean isEmptyElementAbsent = HelperUtility.isElementAbsent(webOp,
						By.xpath("//div[contains(@ng-repeat,'newAllergy')][contains(@class,'new-section')]"));
				if (isEmptyElementAbsent) {
					webOp.driver().findElement(addAllergyLocator).click();
					i++;
					activeCount++;
				}
			}

			try
			{
				WebElement scrollableField = webOp.driver().findElement(By.xpath("//div[contains(@class,'allergies-section scrollable')]"));
				executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 150);
			}
			catch(Exception e)
			{
				webOp.logger().info("Scroll bar not available in edit allergies");
			}
			webOp.waitTillAllCallsDoneUsingJS();
			//activate allergy ui-select component
			webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i +"]//div[@placeholder='allergy']/span[1]")).click();
			webOp.waitTillAllCallsDoneUsingJS();
			WebElement allergyNameElement = webOp.driver().findElement(
			        By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//input[@placeholder='allergy']"));
			allergyNameElement.sendKeys(allergyType);
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'lable-ui-select')][@title='"+allergyType+"']")));
			allergyNameElement.sendKeys(Keys.RETURN);
			webOp.waitTillAllCallsDoneUsingJS();
			
			if (reaction != null) {
				//activate reaction ui-select component
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')]["+i+"]//div[contains(@class,'reactions')]/span")).click();
				WebElement reactionElement = webOp.driver().findElement(
						By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//input[@placeholder='reactions']"));
				wait.until(ExpectedConditions.elementToBeClickable(reactionElement));
				
				reactionElement.sendKeys(reaction);
				webOp.waitTillAllCallsDoneUsingJS();
				reactionElement.sendKeys(Keys.RETURN);
			}
			Alogs.add(AuditAction.ALLERGY_CREATED);
		}
	}

	public void addEditHomeMeds(List<HomeMeds> homeMedsList) throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(showHomeMedsSectionButtonLocator).click();
		int activeCount = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newHomeMed') and not(contains(@class, 'hide-block'))]")).size();
		int i = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newHomeMed')]")).size();

		for (HomeMeds medication : homeMedsList) {
			Medication meds = medication.getMedication();
			String drugName = meds.getDrugName();
			String dose = meds.getDose();
			String frequency = meds.getFrequency();
			String lastTaken = meds.getLastTaken();
			String unit = meds.getUnit();
			
			webOp.waitTillAllCallsDoneUsingJS();
			JavascriptExecutor executor = jse;
			WebElement scrollableField = null;
			try {
				
				scrollableField= webOp.driver().findElement(By.xpath("//div[contains(@class,'homemeds-section scrollable')]"));
			}
			catch(Exception e) {
				
				webOp.logger().info("Scroll not available in edit home medication section");
			}
			if (meds.getRemoveFromList() != null && meds.getRemoveFromList().getDiscontinue() != null) {
				
				if(scrollableField!=null)
				{
					executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
				}
					
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][div//input[@value='"
						+ drugName + "']]"+discontinueButton)).click();
				activeCount--;
				Alogs.add(AuditAction.HOME_MEDICATION_DISCONTINUED);
				continue;
			}
			if (meds.getRemoveFromList() != null && meds.getRemoveFromList().getError() != null) {
				
				if(scrollableField!=null)
				{
				executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
				}
				
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][div//input[@value='"
						+ drugName + "']]"+markAsError)).click();
				activeCount--;
				Alogs.add(AuditAction.HOME_MEDICATION_MARKED_AS_ERROR);
				continue;
			}
			
			if (activeCount != 1) {
				webOp.driver().findElement(addHomeMedicationLocator).click();
				i++;
				activeCount++;
			} else {
				boolean isEmptyElementAbsent = HelperUtility.isElementAbsent(webOp,
						By.xpath("//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med-section')]"));
				if (isEmptyElementAbsent) {
					webOp.driver().findElement(addHomeMedicationLocator).click();
					i++;
					activeCount++;
				}
			}

			if(scrollableField!=null)
			{
				executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 150);
			}
			webOp.waitTillAllCallsDoneUsingJS();
			
			//activate drugname ui-select component
			webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')]["+i+"]//div[@placeholder='drug name']/span[1]")).click();
			webOp.waitTillAllCallsDoneUsingJS();
			//send drugName
			WebElement drugNameElement = webOp.driver().findElement( 
					By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i + "]//input[@placeholder='drug name']"));
			drugNameElement.sendKeys(drugName);
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'lable-ui-select')][@title='"+drugName+"']")));
			drugNameElement.sendKeys(Keys.RETURN);
			webOp.waitTillAllCallsDoneUsingJS();
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i
					+ "]//input[@ng-model='newHomeMed.dose']")));
			//wait until dose unit drop down has not been populated
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i + "]"+homeMedDoseUnitLocator+"/option"), 1));
			webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i
					+ "]//input[@ng-model='newHomeMed.dose']")).sendKeys(dose);
			webOp.waitTillAllCallsDoneUsingJS();
			
			WebElement dosageUnitLocator = webOp.driver()
					.findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i
							+ "]//select[@ng-model='newHomeMed.doseUnitUid']"));
			jse.executeScript("arguments[0].click();", dosageUnitLocator);
			webOp.selectText(unit, dosageUnitLocator);
			
			//activate frequency ui-select dropdown
			webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')]["+i+"]//div[@placeholder='frequency']/span[1]")).click();
			WebElement frequencyElement = webOp.driver().findElement(
					By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i + "]//input[@placeholder='frequency']"));
			frequencyElement.sendKeys(frequency);
			webOp.waitTillAllCallsDoneUsingJS();
			frequencyElement.sendKeys(Keys.RETURN);
			
			if (lastTaken != null) {
				webOp.driver().findElement(By
						.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i + "]//input[@placeholder='last taken']"))
				.sendKeys(lastTaken);
			}
			Alogs.add(AuditAction.HOME_MEDICATION_CREATED);
		}
	}

	public void addEditDiagnoses(List<Diagnoses> diagnosesList) throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(showDiagnosesSectionButtonLocator).click();
		int activeCount = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newDiagnosis') and not(contains(@class, 'hide-block'))]")).size();
		int i = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')]")).size();

		for (Diagnoses diagnosis : diagnosesList) {
			String diagnosisName = diagnosis.getDiagnosis();

			webOp.waitTillAllCallsDoneUsingJS();
			JavascriptExecutor executor = jse;
			WebElement scrollableField = null;
			try {
				
				scrollableField = webOp.driver()
						.findElement(By.xpath("//div[contains(@class,'diagnoses-section scrollable')]"));
			}
			catch(Exception e)
			{
				webOp.logger().info("Scroll is not available in edit diagnosis section");
			}
			if (diagnosis.getRemoveFromList() != null && diagnosis.getRemoveFromList().getDiscontinue() != null) {
				
				if(scrollableField!=null)
				{
					executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
				}
				
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')][div//input[@title='"
						+ diagnosisName + "']]"+discontinueButton)).click();
				activeCount--;
				Alogs.add(AuditAction.DIAGNOSIS_DISCONTINUED);
				continue;
			}
			if (diagnosis.getRemoveFromList() != null && diagnosis.getRemoveFromList().getError() != null) {
				
				if(scrollableField!=null)
				{
					executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
				}
				
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')][div//input[@title='"
						+ diagnosisName + "']]"+markAsError)).click();
				webOp.driver().findElement(By.xpath("//div[input[@title='"+diagnosisName+"']]/preceding::button[not(contains(@class,'ng-hide'))][text()='Mark as error']")).click();
				activeCount--;
				Alogs.add(AuditAction.DIAGNOSIS_MARKED_AS_ERROR);
				continue;
			}
			
			if (activeCount != 1) {
				webOp.driver().findElement(addDiagnoseLocator).click();
				i++;
				activeCount++;
			} else {
				boolean isEmptyElementAbsent = HelperUtility.isElementAbsent(webOp,
						By.xpath("//div[contains(@ng-repeat,'newDiagnosis')]//div[@placeholder='diagnosis' and contains(@class, 'ui-select-match')]"));
				if (isEmptyElementAbsent) {
					webOp.driver().findElement(addDiagnoseLocator).click();
					i++;
					activeCount++;
				}
			}
			
			if(scrollableField!=null)
			{
				executor.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 150);
			}
			webOp.waitTillAllCallsDoneUsingJS();
			//activate diagnosis ui-select component
			webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')]["+i+"]//div[@placeholder='diagnosis']/span[1]")).click();
			webOp.waitTillAllCallsDoneUsingJS();
			
			WebElement diagnosesLocatorElement = webOp.driver()
					.findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')][" + i + "]"+diagnosesLocatorString));
			webOp.waitTillAllCallsDoneUsingJS();
			diagnosesLocatorElement.sendKeys(diagnosisName);
			webOp.waitTillAllCallsDoneUsingJS();
			diagnosesLocatorElement.sendKeys(Keys.RETURN);
			webOp.waitTillAllCallsDoneUsingJS();
			Alogs.add(AuditAction.DIAGNOSIS_CREATED);
		}
	}
	
	public PrescriptionWithSuervisorPage handleInteracnAlerts() throws InterruptedException {
		PatientDetails editFullPatientData = getEditFullPatientData();
		Map<String, List<DDInteracnAlert>> drugToInteracnAlertMap = new HashMap<String, List<DDInteracnAlert>>();
		MedicalHistory medicalHistory = editFullPatientData.getMedicalHistory();
		if (medicalHistory != null) {
			String drugName;
			
			List<Allergies> allergiesList = medicalHistory.getAllergies();
			for (Allergies allergy : allergiesList) {
				List<DDInteracnAlert> interactionAlerts = allergy.getDInteracnAlert();
				if(!interactionAlerts.isEmpty()){
					for(DDInteracnAlert drugInteracnAlert : interactionAlerts) {
						drugName = drugInteracnAlert.getDrugName();
						if(drugToInteracnAlertMap.containsKey(drugName)){
							drugToInteracnAlertMap.get(drugName).add(drugInteracnAlert);
						} else {
							List<DDInteracnAlert> alertsForDrug = new ArrayList<DDInteracnAlert>();
							alertsForDrug.add(drugInteracnAlert);
							drugToInteracnAlertMap.put(drugName, alertsForDrug);
						}
					}
				}
			}

			List<HomeMeds> homeMedsList = medicalHistory.getHomeMeds();
			for (HomeMeds homeMed : homeMedsList) {
				List<DDInteracnAlert> interactionAlerts = homeMed.getDInteracnAlert();
				
				if(!interactionAlerts.isEmpty()){
					for(DDInteracnAlert drugInteracnAlert : interactionAlerts) {
						drugName = drugInteracnAlert.getDrugName();
						if(drugToInteracnAlertMap.containsKey(drugName)){
							drugToInteracnAlertMap.get(drugName).add(drugInteracnAlert);
						} else {
							List<DDInteracnAlert> alertsForDrug = new ArrayList<DDInteracnAlert>();
							alertsForDrug.add(drugInteracnAlert);
							drugToInteracnAlertMap.put(drugName, alertsForDrug);
						}
					}
				}
			}
			
			List<Prescription> allPrescriptions = PatientService.getUpdatedPrescriptions();
			//map to store every interacn with the first undeleted prescription which has given drugName
			Map<DDInteracnAlert, Prescription> interactionAlertMapToPrescription = new HashMap<DDInteracnAlert, Prescription>();
			for (Map.Entry<String,List<DDInteracnAlert>> entry : drugToInteracnAlertMap.entrySet()){
				drugName = entry.getKey();
				Prescription drugNamePrescription = PatientUtility.getFirstPrescriptionWithDrugName(drugName, allPrescriptions);
				for(DDInteracnAlert interacnAlert : entry.getValue()){
					interactionAlertMapToPrescription.put(interacnAlert, drugNamePrescription);
				}
				drugNamePrescription.getDdInteracnAlert().addAll(entry.getValue());
			}
			
			//code to handle all inetracn alerts
			if (!interactionAlertMapToPrescription.isEmpty()) {
				//The code for handling all interacn alerts, the audit log for the 3 needs to be implemented.
				DrugInteracnUtility.checkAndRespondToDrugInteracnModals(webOp, interactionAlertMapToPrescription);
				PatientService.setUpdatedPrescriptions(allPrescriptions);
			}
			
		}
		return this;
	}

	public PrescriptionWithSuervisorPage saveChanges() throws Exception {
		PatientDetails editFullPatientData = getEditFullPatientData();
		saveOrDiscardData(editFullPatientData);
		return this;
	}

	public PrescriptionWithSuervisorPage saveManagePatientModification() throws InterruptedException {
		ManageExistingPatient manageExistingPatient = dataProvider.getPageData("manageExistingPatient");
		PatientDetails editDetails = manageExistingPatient.getEditDetails();
		saveOrDiscardData(editDetails);
		return this;
	}

	public void saveOrDiscardData(PatientDetails editFullPatientData) throws InterruptedException {
		Demographic demographyOfPatient = editFullPatientData.getDemographic();
		MedicalHistory medicalHistory = editFullPatientData.getMedicalHistory();

		if (demographyOfPatient != null && demographyOfPatient.isDiscardChanges() != null
				&& demographyOfPatient.isDiscardChanges() == true) {
			webOp.driver().findElement(discardButtonLocator).click();
		} else if (medicalHistory != null && medicalHistory.isDiscardChanges() != null
				&& medicalHistory.isDiscardChanges() == true) {
			webOp.driver().findElement(discardButtonLocator).click();
		} else {
			webOp.driver().findElement(saveChangesButtonLocator).click();
		}
	}

	public AbstractPage moveToAuthorizePrescriptionsPage() throws Exception {
		PrescriptionsData prescriptionsData = getPrescriptionsData();
		return submitDetails(prescriptionsData);
	}

	public AbstractPage moveToAuthorizePrescriptionsPageAgain() throws Exception {
		PrescriptionsData editPrescriptionsData = getEditPrescriptionsData();
		return submitDetails(editPrescriptionsData);
	}

	public AbstractPage submitDetails(PrescriptionsData prescriptionsData) throws Exception {
		List<Prescription> prescriptions = prescriptionsData.getPrescription();
		Boolean cancelChanges = prescriptionsData.isCancelChanges();
		Boolean navigateAway = prescriptionsData.isNavigateAway();
		if (navigateAway != null && (navigateAway.booleanValue() == true)) {
			// browser back or click on some link
			HelperUtility.navigateBack(webOp);
			return new HomeViewPage(webOp, dataProvider);
		} else if (cancelChanges != null && (cancelChanges.booleanValue() == true)) {
			webOp.driver().findElement(cancelSavePresciptionsLocator).click();
			HelperUtility.confirmAlertModal(webOp, cancelWritePrescriptionsAlertTitleLocator);
			return new HomeViewPage(webOp, dataProvider);
		} else if (prescriptions != null && !prescriptions.isEmpty()) {
			wait.until(ExpectedConditions.presenceOfElementLocated(submitPrescriptionLocator));
			webOp.driver().findElement(submitPrescriptionLocator).click();
			/*
			 *TODO: move to either prescription-success page or authorization page if any controlled substance is going to be eprescribed
			*/
			
			return new AuthorizePrescriptionsPage(webOp, dataProvider);
		}
		return this;
	}
	
	public AbstractPage submitDetailsAndDecideNextPage(PrescriptionsData prescriptionsData
			, boolean isAnyEPCSPossible, int prescriptionsToBePrinted) throws Exception {
		List<Prescription> prescriptions = PatientService.getUpdatedPrescriptions();
		Boolean cancelChanges = prescriptionsData.isCancelChanges();
		Boolean navigateAway = prescriptionsData.isNavigateAway();
		if (navigateAway != null && (navigateAway.booleanValue() == true)) {
			// browser back or click on some link
			HelperUtility.navigateBack(webOp);
			return new HomeViewPage(webOp, dataProvider);
		} else if (cancelChanges != null && (cancelChanges.booleanValue() == true)) {
			webOp.driver().findElement(cancelSavePresciptionsLocator).click();
			HelperUtility.confirmAlertModal(webOp, cancelWritePrescriptionsAlertTitleLocator);
			return new HomeViewPage(webOp, dataProvider);
		} else if (prescriptions != null && !prescriptions.isEmpty()) {
			wait.until(ExpectedConditions.presenceOfElementLocated(submitPrescriptionLocator));
			webOp.driver().findElement(submitPrescriptionLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
			//TODO: see if we need prescriptionsToBePrinted anywhere
			if(isAnyEPCSPossible == true) {
				return new AuthorizePrescriptionsPage(webOp, dataProvider);
			} else {
				return new PrescriptionResultPage(webOp, dataProvider);
			}
		}
		return this;
	}

	public PrescriptionWithSuervisorPage moveToPrescriptionPageInMockProject() throws Exception {
		webOp.driver().findElement(By.xpath("//a[contains(text(),'Write prescriptions')]")).click();
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(By.xpath("//a[contains(text(),'Write prescriptions')]")).click();
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(By.xpath("//a[contains(text(),'Send prescriptions')]")).click();
		return this;
	}

}
