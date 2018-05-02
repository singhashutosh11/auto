package com.bravadohealth.clara1.pageObjects;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.pageObjects.locators.DemographicLocators;
import com.bravadohealth.clara1.pageObjects.locators.EditModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.MedicalHistoryLocators;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.Allergies;
import com.bravadohealth.pagedataset.Contact;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.Diagnoses;
import com.bravadohealth.pagedataset.HomeMeds;
import com.bravadohealth.pagedataset.MedicalHistory;
import com.bravadohealth.pagedataset.Medication;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class PatientMedicalHistory extends AbstractPage implements DemographicLocators, MedicalHistoryLocators, EditModalLocators{
	By discardButtonLocator = By.xpath("//button[contains(text(),'Discard changes')]");
	By saveChangesButtonLocator = By.xpath("/html/body/div[1]/div/div/div/div[2]/div/button[2]");

	/* move to next page related variables */
	By cancelSaveMedicalHistoryLocator = By.xpath("//button/a[contains(text(),'Cancel')]");
	By writePrescriptionsLocator = By.xpath("//button[text()='Next']");

	private String NewDiagnosisInputField="//div[contains(@ng-repeat,'newDiagnosis')]";
	private String discontinueButton="//button[contains(text(),'Discontinue')]";
	private String markAsError="//button[contains(text(),'Mark as error')]";

	public PatientMedicalHistory(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public void logout() throws Exception {
		HomeViewPage homeViewPage = new HomeViewPage(webOp, dataProvider);
		homeViewPage.logout();
	}

	public PatientDetails getPatientDetails() {
		Patient patient = dataProvider.getPageData("patient");
		PatientDetails patientDetails;
		if (patient.getNewPatient() != null) {
			patientDetails = patient.getNewPatient();
		} else {
			patientDetails = patient.getExistingPatient();
		}
		return patientDetails;
	}

	public AbstractPage navigateToMedicalHistoryPage() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		/*webOp.driver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		String url = webOp.driver().getCurrentUrl();
		Assert.assertTrue(url.contains("/patient-encounter/patient-medical-history"));*/
		wait.until(ExpectedConditions.urlContains(AppPage.PatientMedicalHistory.getUrlContains(hMap)));
		Patient patient=dataProvider.getPageData("patient");
		if(patient.getNewPatient().getPharmacyPageData()!=null && patient.getNewPatient().getPharmacyPageData().getPharmacySearch()!=null){
			return new SelectPharmacyPage(webOp, dataProvider);
		}
		return new PatientMedicalHistory(webOp, dataProvider);
	}

	public PatientMedicalHistory clickOnEditPatient() throws Exception {
		Patient patient = dataProvider.getPageData("patient");
		if (patient.getNewPatient().getEditDetails() != null
				&& patient.getNewPatient().getEditDetails().getDemographic() != null) {
			//webOp.driver().findElement(editPatientLocator).click();
			jse.executeScript("arguments[0].click()", webOp.driver().findElement(editPatientLocator)); 
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.presenceOfElementLocated(editPatientModal));
		}
		return this;
	}

	public PatientMedicalHistory editPatientProfile() throws Exception {
		PatientDetails patientDetails = getPatientDetails();
		if (patientDetails.getEditDetails() != null) {
			Demographic demographyOfPatient = patientDetails.getEditDetails().getDemographic();
			if (demographyOfPatient != null) {
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
					webOp.driver().findElement(middleNameLocator)
					.sendKeys("" + demographyOfPatient.getMiddleName() + "");
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
					if (demographyOfPatient.isDuplicatePatientRecord() == null || demographyOfPatient.isDuplicatePatientRecord().booleanValue() == false) {
						zipCode = HelperUtility.getRandomZipCode();
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
				Contact contact = patientDetails.getContact();
				if (contact != null && contact.getPhone() != null) {
					webOp.driver().findElement(phoneLocator).clear();
					webOp.driver().findElement(phoneLocator).sendKeys("" + contact.getPhone() + "");
				}
				if (contact != null && contact.getEmail() != null) {
					webOp.driver().findElement(emailLocator).clear();
					webOp.driver().findElement(emailLocator).sendKeys("" + contact.getEmail() + "");
				}
				webOp.waitTillAllCallsDoneUsingJS();
				if (demographyOfPatient.isNavigateAway() != null && (demographyOfPatient.isNavigateAway().booleanValue() == true)) {
					// browser back or click on some link
					HelperUtility.navigateBack(webOp);
					//check url changed
					HelperUtility.checkUrlChangesTo(webOp, AppPage.AddNewPatient.getUrlContains(hMap));
					webOp.driver().quit();// stop the test
				}
				if (demographyOfPatient.isDiscardChanges() != null && demographyOfPatient.isDiscardChanges() == true) {
					webOp.driver().findElement(discardButtonLocator).click();
				} else {
					webOp.driver().findElement(saveChangesButtonLocator).click();
				}

				if (demographyOfPatient.isDuplicatePatientRecord() != null
						&& demographyOfPatient.isDuplicatePatientRecord().booleanValue() == true) {
					Thread.sleep(1000);
					Boolean isDuplicate = HelperUtility.isElementPresent(webOp, formFailureErrorMessage);
					Assert.assertTrue(isDuplicate,
							"For duplicate entry in the record , duplicate message is not displayed.");
					webOp.driver().quit();// stop the test
				}
			}
		}
		return this;
	}

	public PatientMedicalHistory addMedicalHistory() throws Exception {
		PatientDetails patientDetails = getPatientDetails();
		MedicalHistory medicalHistory = patientDetails.getMedicalHistory();
		if (medicalHistory != null) {
			addAllergies(medicalHistory);
			addHomeMedication(medicalHistory);
			addDiagnosis(medicalHistory);
		}
		return this;
	}

	private void addDiagnosis(MedicalHistory medicalHistory) throws Exception {
		List<Diagnoses> diagnoses = medicalHistory.getDiagnoses();
		int i = 1;
		//if(diagnoses == null ) --> doctor did not select anything for diagnoses
		if (diagnoses.isEmpty()) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a#select-none-diagnoses")));
			WebElement selectNoneElement = webOp.driver().findElement(By.cssSelector("a#select-none-diagnoses"));
			new Actions(webOp.driver()).moveToElement(selectNoneElement).click().perform();
			Alogs.add(AuditAction.SELECTED_NONE_DIAGNOSIS);
		} else {
			WebElement addDiagnosesLink = webOp.driver()
					.findElement(By.xpath("//a[contains(text(),'Add diagnoses or conditions')]"));
			jse.executeScript("arguments[0].scrollIntoView(); arguments[0].click()", addDiagnosesLink);
			webOp.waitTillAllCallsDoneUsingJS();
			// webOp.driver().findElement(By.xpath("//a[contains(text(),'Add
			// diagnoses or conditions')]")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='First diagnosis or condition']")));
			for (Diagnoses diagnosis : diagnoses) {
				String diagnose = diagnosis.getDiagnosis();
				WebElement element = webOp.driver()
						.findElement(By.xpath(""+NewDiagnosisInputField+"[" + i + "]"));
				try
				{
					if (webOp.driver().findElement(By.xpath("//div[contains(@class,'diagnoses-section scrollable')]")) != null)
					{
						WebElement scrollableField = webOp.driver()
								.findElement(By.xpath("//div[contains(@class,'diagnoses-section scrollable')]"));
						jse.executeScript("arguments[0].scrollTop = arguments[1];",
								scrollableField, 150);
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}  
				if (diagnosis.getRemoveFromList() != null && diagnosis.getRemoveFromList().getRemove() != null) {
					webOp.driver().findElement(By.xpath(""+NewDiagnosisInputField+"[" + i + "]//button[contains(text(),'Remove')]")).click();
					continue;
				}
				webOp.waitTillAllCallsDoneUsingJS();
				jse.executeScript("arguments[0].click();", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')]["+i+"]//div[@placeholder='diagnosis']/span[1]")));
				webOp.waitTillAllCallsDoneUsingJS();
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')][" + i + "]//input[@placeholder='diagnosis']")).sendKeys(diagnose);
				webOp.waitTillAllCallsDoneUsingJS();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'lable-ui-select')][@title='"+diagnose+"']")));
				element.findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')][" + i + "]//input[@placeholder='diagnosis']")).sendKeys(Keys.RETURN);
				Alogs.add(Alogs.size()-(i-1),AuditAction.DIAGNOSIS_CREATED);
				i++;
				webOp.waitTillAllCallsDoneUsingJS();
				jse.executeScript("arguments[0].click();", webOp.driver().findElement(addDiagnoseLocator));
				//webOp.driver().findElement(addDiagnoseLocator).click();
			}
			webOp.driver().findElement(saveChangesButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(editPatientModal));
		}
	}

	public PatientMedicalHistory editDiagnosis() throws Exception {
		PatientDetails patientDetails = getPatientDetails();
		if (patientDetails.getEditDetails() == null
				|| patientDetails.getEditDetails().getMedicalHistory() == null
				|| patientDetails.getEditDetails().getMedicalHistory().getDiagnoses() == null) {
			return this;
		}
		List<Diagnoses> diagnoses = patientDetails.getEditDetails().getMedicalHistory().getDiagnoses();
		int i;
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@ng-click,'digNosViewCtrl.addEditDiagnoses()')][text()='Edit']")));
		/*webOp.driver()
			.findElement
			(By.xpath("//button[contains(@ng-click,'digNosViewCtrl.addEditDiagnoses()')][text()='Edit']"))
			.click();*/
		jse.executeScript("arguments[0].click()", webOp.driver().findElement(By.xpath("//button[contains(@ng-click,'digNosViewCtrl.addEditDiagnoses()')][text()='Edit']"))); 
/*		jse.executeScript("function getElementByXpath(path) {\r\n" + 
				"  return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\r\n" + 
				"}; getElementByXpath(\"//button[contains(@ng-click,'digNosViewCtrl.addEditDiagnoses()')][text()='Edit']\").click();");
*/		for (Diagnoses diagnosis : diagnoses) {
			String diagnosisName = diagnosis.getDiagnosis();
			webOp.waitTillAllCallsDoneUsingJS();
			WebElement scrollableField = webOp.driver()
					.findElement(By.xpath("//div[contains(@class,'flow-section diagnoses-section show')]"));
			if (diagnosis.getRemoveFromList() != null) {
				if (diagnosis.getRemoveFromList().getDiscontinue() != null) {
					jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
					webOp.driver().findElement(By.xpath(""+NewDiagnosisInputField+"[div//input[@title='"
							+ diagnosisName + "']]"+discontinueButton+"")).click();
					Alogs.add(AuditAction.DIAGNOSIS_DISCONTINUED);
					continue;
				} else if (diagnosis.getRemoveFromList().getError() != null) {
					jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
					webOp.driver().findElement(By.xpath(""+NewDiagnosisInputField+"[div//input[@title='"+ diagnosisName + "']]"+markAsError+"")).click();
					Alogs.add(AuditAction.DIAGNOSIS_MARKED_AS_ERROR);
					continue;
				}
			}
			webOp.driver().findElement(addDiagnoseLocator).click();
			i = webOp.driver().findElements(By.xpath(""+NewDiagnosisInputField+"")).size();
			jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 150);
			webOp.waitTillAllCallsDoneUsingJS();
			WebElement diagnosesLocatorElement = webOp.driver()
					.findElement(By.xpath(""+NewDiagnosisInputField+"[" + i + "]"+diagnosesLocatorString+""));
			jse.executeScript("arguments[0].click();", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newDiagnosis')]["+i+"]//div[@placeholder='diagnosis']/span[1]")));
			diagnosesLocatorElement.sendKeys(diagnosisName);
			webOp.waitTillAllCallsDoneUsingJS();
			diagnosesLocatorElement.sendKeys(Keys.RETURN);			
			webOp.waitTillAllCallsDoneUsingJS();
			Alogs.add(AuditAction.DIAGNOSIS_CREATED);
			i++;
		}
		Thread.sleep(2000);
		webOp.driver().findElement(saveChangesButtonLocator).click();
		return this;
	}

	public PatientMedicalHistory viewAddedDetails() throws Exception {
		PatientDetails patientDetails = getPatientDetails();
		verifyAllergies(patientDetails);
		verifyHomeMedication(patientDetails);
		verifyDiagnosis(patientDetails);
		return this;
	}

	public AbstractPage moveToWritePrescriptionsPage() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(editPatientModal));
		PatientDetails patientDetails = getPatientDetails();
		Boolean cancelChanges = patientDetails.getMedicalHistory().isCancelChanges();
		if (cancelChanges != null && (cancelChanges.booleanValue() == true)) {
			webOp.driver().findElement(cancelSaveMedicalHistoryLocator).click();
			return new HomeViewPage(webOp, dataProvider);
		} else {
			WebElement writePrescriptionsButtonElement = webOp.driver().findElement(writePrescriptionsLocator);
			jse.executeScript("arguments[0].click();", writePrescriptionsButtonElement);
			// webOp.driver().findElement(writePrescriptionsLocator).click();
			return new WritePrescriptionsPage(webOp, dataProvider);
		}
	}

	private void verifyDiagnosis(PatientDetails patientDetails) throws InterruptedException {
		int i = 1;
		String allDiagnoses = "";
		if (patientDetails.getEditDetails() != null 
				&& patientDetails.getEditDetails().getMedicalHistory().getDiagnoses() != null) {
			HashMap<String, Object> diagnoseMap = new HashMap<String, Object>();
			for (Diagnoses diagnoses : patientDetails.getMedicalHistory().getDiagnoses()) {
				if (diagnoses.getDiagnosis() != null) {
					diagnoseMap.put(diagnoses.getDiagnosis(), i - 1);
					i++;
				}
			}
			for (Diagnoses diagnoses : patientDetails.getEditDetails().getMedicalHistory().getDiagnoses()) {
				if (diagnoses.getRemoveFromList() != null) {
					int j = (Integer) diagnoseMap.get(diagnoses.getDiagnosis());
					patientDetails.getMedicalHistory().getDiagnoses().get(j)
					.setRemoveFromList(diagnoses.getRemoveFromList());
				} else {
					patientDetails.getMedicalHistory().getDiagnoses().add(diagnoses);
				}
			}
			webOp.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

		}

		if(patientDetails.getMedicalHistory()!=null)
		{
			int k = 1, l = 1;
			i=1;
			MedicalHistory medicalHistory = patientDetails.getMedicalHistory();
			List<Diagnoses> diagnoses = medicalHistory.getDiagnoses();
			if (diagnoses != null && !diagnoses.isEmpty()) {
				for (Diagnoses diagnosis : diagnoses) {
					String diagnose = diagnosis.getDiagnosis();
					String DiagnoseName = null;
					if (diagnosis.getRemoveFromList() != null) {
						if (diagnosis.getRemoveFromList().getDiscontinue() != null) {
							webOp.driver()
							.findElement(By
									.xpath("//button[contains(@ng-click,'digNosViewCtrl')][contains(text(),'Show discontinued')]"))
							.click();
							WebElement DiagnoseConstLocator = webOp.driver().findElement(
									By.xpath("//li[contains(@ng-repeat,'digNosViewCtrl.status.DISCONTINUED')][" + k + "]"));
							DiagnoseName = DiagnoseConstLocator
									.findElement(By.xpath("//span[contains(@class,'diagnosis')]")).getText();
							k++;
						} else if (diagnosis.getRemoveFromList().getError() != null) {
							webOp.driver()
							.findElement(By
									.xpath("//button[contains(@ng-click,'digNosViewCtrl')][contains(text(),'Show errors')]"))
							.click();
							webOp.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
							WebElement DiagnoseConstLocator = webOp.driver().findElement(By.xpath(
									"//li[contains(@ng-repeat,'digNosViewCtrl.status.MARKED_AS_ERROR')][" + l + "]"));
							jse.executeScript(
									"arguments[0].scrollIntoView(); arguments[0].click()", DiagnoseConstLocator);
							DiagnoseName = webOp.driver()
									.findElement(
											By.xpath("//li[contains(@ng-repeat,'digNosViewCtrl.status.MARKED_AS_ERROR')]["
													+ l + "]//span[contains(@class,'diagnosis')]"))
									.getText();
							l++;
						}
					}
					if (diagnosis.getRemoveFromList() == null) {
						DiagnoseName = webOp.driver()
								.findElement(By.xpath("//li[contains(@ng-repeat,'digNosViewCtrl.status.ACTIVE')][" + i + "]//span[contains(@class,'diagnosis')]"))
								.getText();
						i++;
						allDiagnoses = allDiagnoses + ", " + DiagnoseName;
					}
					webOp.logger().info("Verifying that "+diagnose+" is displayed in the Medical History");
					Assert.assertEquals(DiagnoseName, diagnose,
							"The entered diagnose name is not displayed in the Medical History");
				}
				String allDiagnoseDetails = formattingForPatientDetails(allDiagnoses);

				String patientDetailsDiagnoses = webOp.driver().findElement(patientDetailsDiagnosesLocator).getText();
				webOp.logger().info("Verifying that all the entered diagnosis details are displayed in the Patient details section");
				Assert.assertEquals(patientDetailsDiagnoses, allDiagnoseDetails, 
						"The entered diagnoses are not displayed in patient details section");
			} 

			else {
				String patientDetailsDiagnoses = webOp.driver().findElement(patientDetailsDiagnosesLocator).getText();
				webOp.logger().info("Verifying that no diagnoses are documented");
				Assert.assertTrue(patientDetailsDiagnoses.equals("None documented."),
						"When none is selected for home medication ,none documented is not displayed");
			}
		}

	}

	private String formattingForPatientDetails(String medicationDetails) {
		medicationDetails.trim();
		if (medicationDetails.startsWith(",")) {
			medicationDetails = medicationDetails.substring(2, medicationDetails.length());
		}
		return medicationDetails;
	}

	private void verifyHomeMedication(PatientDetails patientDetails) throws Exception {
		int i = 0;
		String allMeds = "";
		if (patientDetails.getEditDetails() != null 
				&& patientDetails.getEditDetails().getMedicalHistory().getHomeMeds() != null) {
			HashMap<String, Object> homeMedMap = new HashMap();
			for (HomeMeds medication : patientDetails.getMedicalHistory().getHomeMeds()) {
				if (medication.getMedication() != null) {
					homeMedMap.put(medication.getMedication().getDrugName(), i);
					i++;
				}
			}
			for (HomeMeds medication : patientDetails.getEditDetails().getMedicalHistory().getHomeMeds()) {
				if (medication.getMedication().getRemoveFromList() != null) {
					int j = (Integer) homeMedMap.get(medication.getMedication().getDrugName());
					patientDetails.getMedicalHistory().getHomeMeds().get(j).getMedication()
					.setRemoveFromList(medication.getMedication().getRemoveFromList());
				} else {
					patientDetails.getMedicalHistory().getHomeMeds().add(medication);
				}
			}
		}
		if(patientDetails.getMedicalHistory()!=null) {
			int k = 1, l = 1;
			i=1;
			MedicalHistory medicalHistory = patientDetails.getMedicalHistory();
			List<HomeMeds> homeMedication = medicalHistory.getHomeMeds();
			if (homeMedication != null && !homeMedication.isEmpty()) {
				for (HomeMeds medication : homeMedication) {
					Medication meds = medication.getMedication();
					String drugName = meds.getDrugName();
					String dose = meds.getDose() + " " + meds.getUnit();
					String frequency = meds.getFrequency();
					String lastTaken = meds.getLastTaken();
					String MedName = null;
					String MedDosages = null;
					String MedFrquency = null;
					String MedLastTaken = null;
					if (meds.getRemoveFromList() != null) {
						if (meds.getRemoveFromList().getDiscontinue() != null) {
							webOp.driver()
							.findElement(By
									.xpath("//button[contains(@ng-click,'HMViewCtrl')][contains(text(),'Show discontinued')]"))
							.click();
							WebElement discontinuedMedication = webOp.driver().findElement(
									By.xpath("//li[contains(@ng-repeat,'homeMedStatus.DISCONTINUE')][" + k + "]"));
							MedName = discontinuedMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-name')]")).getText();
							MedDosages = discontinuedMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-dosage')]")).getText();
							MedFrquency = discontinuedMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-frequency')]"))
									.getText();
							MedLastTaken = discontinuedMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-last-taken')]"))
									.getText();
							k++;
						} else if (meds.getRemoveFromList().getError() != null) {
							webOp.driver()
							.findElement(By
									.xpath("//button[contains(@ng-click,'HMViewCtrl')][contains(text(),'Show errors')]"))
							.click();
							WebElement ErroredMedication = webOp.driver().findElement(
									By.xpath("//li[contains(@ng-repeat,'homeMedStatus.MARKED_AS_ERROR')][" + i + "]"));
							MedName = ErroredMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-name')]")).getText();
							MedDosages = ErroredMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-dosage')]")).getText();
							MedFrquency = ErroredMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-frequency')]"))
									.getText();
							MedLastTaken = ErroredMedication
									.findElement(By.xpath("//span[contains(@class,'home-medication-last-taken')]"))
									.getText();
							i++;
						}
					}
					if (meds.getRemoveFromList() == null) {
						webOp.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
						MedName = webOp.driver().findElement(By.xpath("//li[contains(@ng-repeat,'homeMedStatus.ACTIVE')]["
								+ l + "]//span[contains(@class,'home-medication-name')]")).getText();
						MedDosages = webOp.driver()
								.findElement(By.xpath("//li[contains(@ng-repeat,'homeMedStatus.ACTIVE')][" + l
										+ "]//span[contains(@class,'home-medication-dosage')]"))
								.getText();
						MedFrquency = webOp.driver()
								.findElement(By.xpath("//li[contains(@ng-repeat,'homeMedStatus.ACTIVE')][" + l
										+ "]//span[contains(@class,'home-medication-frequency')]"))
								.getText();
						// MedLastTaken =
						// MedicationConstElement.findElement(By.xpath("//span[contains(@class,'home-medication-last-taken')]")).getText();
						l++;
						allMeds = allMeds + ", " + MedName;
					}
					webOp.logger().info("Verifying that "+drugName+" is displayed under home medication");
					Assert.assertEquals(MedName, drugName,  "The entered medicine "+drugName+" name is not displayed under home medication");
					if (meds.getDose() != null) {
						webOp.logger().info("Verifying that "+dose+" is displayed under home medication");
						Assert.assertEquals(MedDosages, dose, 
								"The entered medicine dosage "+dose+" is not displayed under home medication");
					}
					if(meds.getFrequency()!=null){
						webOp.logger().info("Verifying that "+frequency+" is displayed under home medication");
						Assert.assertTrue(frequency.equalsIgnoreCase(MedFrquency),
								"The entered medicine  frquency "+frequency+" is not displayed under home medication");
					}
					/*
					 * Assert.assertEquals(lastTaken, MedLastTaken,
					 * "The entered medicine last taken is displayed under home medication"
					 * );
					 */
				}
				String allMedDetails = formattingForPatientDetails(allMeds);
				if (allMedDetails.isEmpty()) {
					allMedDetails = "None";
				}
				String patientDetailsMedication = webOp.driver().findElement(patientDetailsMedicationLocator).getText();
				webOp.logger().info("Verifying that "+allMedDetails+" is displayed in the Patient details section");
				Assert.assertEquals(patientDetailsMedication, allMedDetails, 
						"The entered medicines "+allMedDetails+" are not displayed under patient details section");
			} 

			else {
				webOp.waitTillAllCallsDoneUsingJS();
				webOp.driver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				String noHomeMedication = webOp.driver().findElement(patientDetailsMedicationLocator).getText();
				webOp.logger().info("Verifying that no medications are documented is displayed");
				Assert.assertEquals(noHomeMedication, "None documented.",
						"When none is selected for home medication ,none is displayed");
				
			}
		}

	}

	private void verifyAllergies(PatientDetails patientDetails) throws Exception {
		int i = 1;
		String allAllergies = "";
		if (patientDetails.getEditDetails() != null 
				&& patientDetails.getEditDetails().getMedicalHistory().getAllergies() != null) {
			HashMap<String, Object> allergyMap = new HashMap();
			for (Allergies allergy : patientDetails.getMedicalHistory().getAllergies()) {
				allergyMap.put(allergy.getAllergy(), i);
				i++;
			}
			for (Allergies allergy : patientDetails.getEditDetails().getMedicalHistory().getAllergies()) {
				if (allergy.getRemoveFromList() != null) {
					int j = (Integer) allergyMap.get(allergy.getAllergy());
					patientDetails.getMedicalHistory().getAllergies().get(j)
					.setRemoveFromList(allergy.getRemoveFromList());
				} else {
					patientDetails.getMedicalHistory().getAllergies().add(allergy);
				}
			}

		}

		if(patientDetails.getMedicalHistory()!=null)
		{
			Thread.sleep(2000);
			int k=1, l=1;
			i=1;
			MedicalHistory medicalHistory = patientDetails.getMedicalHistory();
			List<Allergies> allergies = medicalHistory.getAllergies();
			if (allergies != null && !allergies.isEmpty()) {
				for (Allergies allergy : allergies) {
					String allergyType = allergy.getAllergy();
					String reaction = allergy.getReaction();
					String displayedAllergy = null;
					String displayedReaction = null;

					if (allergy.getRemoveFromList() != null) {
						if(allergy.getRemoveFromList().getDiscontinue() != null){
							webOp.waitTillAllCallsDoneUsingJS();
							webOp.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
							webOp.driver()
							.findElement(By
									.xpath("//button[contains(@ng-click,'alrgViewCtrl')][contains(text(),'Show discontinued')]"))
							.click();
							displayedAllergy = webOp.driver()
									.findElement(By.xpath("//li[contains(@ng-repeat,'allergyStatus.DISCONTINUE')][" + k
											+ "]//span[contains(@class,'detail allergy')]"))
									.getText();
							if (reaction != null) {
								displayedReaction = webOp.driver()
										.findElement(By.xpath("//li[contains(@ng-repeat,'allergyStatus.DISCONTINUE')][" + k
												+ "]//span[contains(@class,'sub-detail rxn')]"))
										.getText();
								webOp.logger().info("Verifying that "+reaction+" reaction is displayed for the "+displayedAllergy+" allergy");
								Assert.assertEquals(displayedReaction, reaction, 
										"The reaction "+reaction+" is not displayed corresponding to "+displayedAllergy+" allergy for the patient");
								
							}
							k++;
						}
						else if(allergy.getRemoveFromList().getError() != null){
							webOp.waitTillAllCallsDoneUsingJS();
							webOp.driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
							webOp.driver()
								.findElement(
								By.xpath("//button[contains(@ng-click,'alrgViewCtrl')][contains(text(),'Show errors')]"))
							.click();
							displayedAllergy = webOp.driver()
									.findElement(By.xpath("//li[contains(@ng-repeat,'allergyStatus.MARKED_AS_ERROR')][" + i
											+ "]//span[contains(@class,'detail allergy')]"))
									.getText();
							if (reaction != null) {
								displayedReaction = webOp.driver()
										.findElement(By.xpath("//li[contains(@ng-repeat,'allergyStatus.MARKED_AS_ERROR')][" + i
												+ "]//span[contains(@class,'sub-detail rxn')]"))
										.getText();
								Assert.assertEquals(reaction, displayedReaction,
										"The reaction is displayed corresponding to allergy for the patient");
							}
							i++;
						}
					} if (allergy.getRemoveFromList() == null) {
						displayedAllergy = webOp.driver()
								.findElement(By.xpath("//li[contains(@ng-repeat,'allergyStatus.ACTIVE')][" + l
										+ "]//span[contains(@class,'detail allergy')]"))
								.getText();
						if (reaction != null) {
							displayedReaction = webOp.driver()
									.findElement(By.xpath("//li[contains(@ng-repeat,'allergyStatus.ACTIVE')][" + l
											+ "]//span[contains(@class,'sub-detail rxn')]"))
									.getText();
							webOp.logger().info("Verifying "+reaction+" is displayed corresponding to allergy for the patient");
							Assert.assertEquals(displayedReaction, reaction, 
									"The "+reaction+" reaction is not displayed corresponding to "+displayedAllergy+" allergy for the patient");
							
						}
						allAllergies = allAllergies + ", " + displayedAllergy;
						l++;
					}
					webOp.logger().info("Verifying the allergy "+allergyType+" is displayed as added in the patient record");
					Assert.assertEquals(displayedAllergy, allergyType, 
							"The allergy is not displayed as added in the patient record");
				}
				String allAllergyDetails = formattingForPatientDetails(allAllergies);
				String allergiesUnderPatientDetails = webOp.driver().findElement(patientDetailsAllergiesLocator).getText();
				webOp.logger().info("Verifying that "+allAllergyDetails+" are displayed correctly under  patient details");
				Assert.assertEquals(allergiesUnderPatientDetails, allAllergyDetails, 
						"The allergies are not displayed correctly under patient details");
			}
			
			else {
				String noAllergy = webOp.driver().findElement(patientDetailsAllergiesLocator).getText();
				webOp.logger().info("Verifying that no allergies are documented");
				Assert.assertEquals(noAllergy, "None documented.", "When none is selected for allergy,no allergies documented is not displayed");
			}
		}

	}

	private void addHomeMedication(MedicalHistory medicalHistory) throws Exception {
		List<HomeMeds> homeMedication = medicalHistory.getHomeMeds();
		int i = 1;
		//if (homeMedication == null) can't generate this through pageData-xml 
		if (homeMedication.isEmpty()) {
			webOp.waitTillAllCallsDoneUsingJS();
			Thread.sleep(1000);
			Alogs.add(AuditAction.SELECTED_NONE_HOME_MEDICATION);
			WebElement selectNoneElement = webOp.driver().findElement(By.cssSelector("a#select-none-homemeds"));
			new Actions(webOp.driver()).moveToElement(selectNoneElement).click().perform();
		} else {
			WebElement addHomeMedsLink = webOp.driver()
					.findElement(By.xpath("//a[contains(text(),'Add home medications')]"));
			jse.executeScript("arguments[0].scrollIntoView(); arguments[0].click()", addHomeMedsLink);
			webOp.waitTillAllCallsDoneUsingJS();
			// webOp.driver().findElement(By.xpath("//a[contains(text(),'Add
			// home medications')]")).click();
			int medicationIndex = 0;
			for (HomeMeds medication : homeMedication) {
				Medication meds = medication.getMedication();
				String drugName = meds.getDrugName();
				String dose = meds.getDose();
				String frequency = meds.getFrequency();
				String lastTaken = meds.getLastTaken();
				String unit = meds.getUnit();
				webOp.waitTillAllCallsDoneUsingJS();
				WebElement element = webOp.driver()
						.findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i + "]"));
				try
				{
					if (webOp.driver().findElement(By.xpath("//div[contains(@class,'flow-section-scroll')]")) != null)
					{
						WebElement scrollableField = webOp.driver()
								.findElement(By.xpath("//div[contains(@class,'flow-section-scroll')]")); 
						jse.executeScript("arguments[0].scrollTop = arguments[1];",scrollableField, 150);
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}  
				if (meds.getRemoveFromList() != null && meds.getRemoveFromList().getRemove() != null) {
					WebElement removeElement = element
							.findElements(By.xpath("//button[contains(@ng-click,'removeThisHomeMed')]")).get(medicationIndex);
					jse.executeScript("arguments[0].click();",removeElement);
					// element.findElement(By.xpath("//button[contains(text(),'Remove')]")).click();
					continue;
				}
				jse.executeScript("arguments[0].click();", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')]["+i+"]//div[@placeholder='drug name']/span[1]")));
				webOp.waitTillAllCallsDoneUsingJS();
				element.findElement(drugNameLocator).sendKeys(drugName);
				webOp.waitTillAllCallsDoneUsingJS();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'lable-ui-select')][@title='"+drugName+"']")));
				webOp.driver().findElement(By.xpath("//span[contains(@class,'ui-select-highlight')]")).click();
				webOp.waitTillAllCallsDoneUsingJS();
				wait.until(ExpectedConditions.elementToBeClickable(dosageLocator));
				//wait until dose unit drop down has not been populated
				wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][" + i + "]"+homeMedDoseUnitLocator+"/option"), 1));
				
				element.findElement(By.xpath("(//input[@ng-model='newHomeMed.dose'])["+i+"]")).sendKeys(dose);
				webOp.waitTillAllCallsDoneUsingJS();
				WebElement dosageUnitElement = element.findElements(By.xpath(homeMedDoseUnitLocator)).get(medicationIndex++);
				//((JavascriptExecutor) webOp.driver()).executeScript("arguments[0].click();", dosageUnitLocator);
				if(unit!=null)
				{
					webOp.selectText(unit, dosageUnitElement);
					dosageUnitElement.sendKeys(Keys.RETURN);
				}
				webOp.waitTillAllCallsDoneUsingJS();
				jse.executeScript("arguments[0].click();", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')]["+i+"]//div[@placeholder='frequency']/span[1]")));
				//webOp.selectText(frequency.toUpperCase(), webOp.driver().findElement(By.xpath("//ul[not(contains(@class,'ng-hide'))][contains(@repeat,'frequency')]")));
				element.findElement(frequencyLocator).sendKeys(frequency);
				element.findElement(By.xpath("//span[@title='"+frequency.toUpperCase()+"']")).click();
				webOp.waitTillAllCallsDoneUsingJS();
				//element.findElement(By.xpath("(//input[@placeholder='frequency'])["+i+"]")).sendKeys(Keys.RETURN);
				if (lastTaken != null) {
					String lastTakenLocator="//div[contains(@ng-repeat,'newHomeMed')][" + i + "]//input[@placeholder='last taken']";
					webOp.driver().findElement(By.xpath(lastTakenLocator)).sendKeys(lastTaken);
					webOp.waitTillAllCallsDoneUsingJS();
					webOp.driver().findElement(By.xpath(lastTakenLocator)).sendKeys(Keys.TAB);
				}
				Alogs.add(Alogs.size()-(i-1),AuditAction.HOME_MEDICATION_CREATED);
				i++;
				webOp.waitTillAllCallsDoneUsingJS();
				webOp.driver().findElement(addHomeMedicationLocator).click();
			}
			webOp.driver().findElement(saveChangesButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(editPatientModal));
		}
	}

	public PatientMedicalHistory editHomeMedication() throws Exception {
		PatientDetails patientDetails = getPatientDetails();
		if (patientDetails.getEditDetails() == null
				|| patientDetails.getEditDetails().getMedicalHistory() == null
				|| patientDetails.getEditDetails().getMedicalHistory().getHomeMeds() == null) {
			return this;
		}
		List<HomeMeds> homeMedication = patientDetails.getEditDetails().getMedicalHistory().getHomeMeds();
		int i = 1;
		webOp.waitTillAllCallsDoneUsingJS();
		By editHomeMedButton = By.xpath("//button[contains(@ng-click,'HMViewCtrl.addEditHomeMed')][text()='Edit']");
		wait.until(ExpectedConditions.presenceOfElementLocated(editHomeMedButton));
		jse.executeScript("arguments[0].click()", webOp.driver().findElement(editHomeMedButton));
		//webOp.driver().findElement(editHomeMedButton).click();
		for (HomeMeds medication : homeMedication) {
			Medication meds = medication.getMedication();
			String drugName = meds.getDrugName();
			String dose = meds.getDose();
			String frequency = meds.getFrequency();
			String lastTaken = meds.getLastTaken();
			String unit = meds.getUnit();
			webOp.waitTillAllCallsDoneUsingJS();
			WebElement scrollableField = null;
			By scroll = By.xpath("//div[contains(@class,'flow-section-scroll')]");
			try	
			{
				scrollableField = webOp.driver()
						.findElement(scroll);
			}
			catch(Exception e) {       
				webOp.logger().info("Element is Not Displayed: " + scroll);
		    }
			if (meds.getRemoveFromList() != null) {
				if (meds.getRemoveFromList().getDiscontinue() != null) {
					if(scrollableField!=null)
					{
						jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
					}
					webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][div//input[@value='"
							+ drugName + "']]"+discontinueButton)).click();
					Alogs.add(AuditAction.HOME_MEDICATION_DISCONTINUED);
					continue;
				} else if (meds.getRemoveFromList().getError() != null) {
					if(scrollableField!=null)
					{
						jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
					}
					webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][div//input[@value='"
					        + drugName + "']]"+markAsError)).click();
					Alogs.add(AuditAction.HOME_MEDICATION_MARKED_AS_ERROR);
					continue;
				}
			}
			webOp.driver().findElement(addHomeMedicationLocator).click();
			i = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')]")).size();
			if(scrollableField!=null)
			{
				jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 150);
			}
			webOp.waitTillAllCallsDoneUsingJS();
			
			//WebElement element = webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')][" + i + "]"));
			WebElement drugNameElement = webOp.driver()
					.findElement(By.xpath(
							"//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')][" + i + "]//input[@placeholder='drug name']"));
			jse.executeScript("arguments[0].click();", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')]["+i+"]//div[@placeholder='drug name']/span[1]")));			
			drugNameElement.sendKeys(drugName);
			
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'lable-ui-select')][@title='"+drugName+"']")));
			drugNameElement.sendKeys(Keys.RETURN);
			webOp.waitTillAllCallsDoneUsingJS();
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'new-home-med')][" + i + "]//input[@ng-model='newHomeMed.dose']")));
			webOp.driver().findElement(By.xpath("//div[contains(@class,'new-home-med')][" + i + "]//input[@ng-model='newHomeMed.dose']")).clear();
			webOp.driver().findElement(By.xpath("//div[contains(@class,'new-home-med')][" + i
					+ "]//input[@ng-model='newHomeMed.dose']")).sendKeys(dose);
			webOp.waitTillAllCallsDoneUsingJS();
			Thread.sleep(2000);
			WebElement dosageUnitLocator = webOp.driver()
					.findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')][" + i
							+ "]//select[@ng-model='newHomeMed.doseUnitUid'][contains(@class,'ng-untouched')]"));
			jse.executeScript("arguments[0].click();", dosageUnitLocator);
			webOp.selectText(unit, dosageUnitLocator);
			
			jse.executeScript("arguments[0].click();", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')]["+i+"]//div[@placeholder='frequency']/span[1]")));
			WebElement frequencyElement = webOp.driver()
					.findElement(By.xpath(
							"//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')][" + i + "]//input[@placeholder='frequency']"));

			frequencyElement.sendKeys(frequency);
			webOp.waitTillAllCallsDoneUsingJS();
			frequencyElement.sendKeys(Keys.RETURN);
			
			if (lastTaken != null) {
				webOp.driver().findElement(By
						.xpath("//div[contains(@ng-repeat,'newHomeMed')][contains(@class,'new-home-med')][" + i + "]//input[@placeholder='last taken']"))
				.sendKeys(lastTaken);
			}
			Alogs.add(AuditAction.HOME_MEDICATION_CREATED);
			//Alogs.add(AuditAction.HOME_MEDICATION_EDITED);
			i++;
		}
		webOp.driver().findElement(saveChangesButtonLocator).click();
		return this;
	}

	private void addAllergies(MedicalHistory medicalHistory) throws Exception {
		List<Allergies> allergies = medicalHistory.getAllergies();
		int i = 1;
		//if(allergies == null) doctor did not select anything for allergies
		if (allergies.isEmpty()) {
			Alogs.add(AuditAction.SELECTED_NONE_ALLERGY);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a#select-none-allergies")));
			jse.executeScript("arguments[0].click()",webOp.driver().findElement(By.cssSelector("a#select-none-allergies")));
		} else {
			WebElement addAllergiesLink = webOp.driver().findElement(By.xpath("//a[contains(text(),'Add allergies')]"));
			jse.executeScript("arguments[0].scrollIntoView(); arguments[0].click()", addAllergiesLink);
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'allergies-section show')]")));
			for (Allergies allergy : allergies) {
				String allergyType = allergy.getAllergy();
				String reaction = allergy.getReaction();
				webOp.waitTillAllCallsDoneUsingJS();
				if (allergy.getRemoveFromList() != null && allergy.getRemoveFromList().getRemove() != null) {
					webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//button[contains(text(),'Remove')]")).click();
					continue;
				}
				webOp.waitTillAllCallsDoneUsingJS();
				jse.executeScript("arguments[0].click()", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i +"]//div[@placeholder='allergy']/span[1]")));
				webOp.waitTillAllCallsDoneUsingJS();
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//input[@placeholder='allergy']")).sendKeys(allergyType);
				webOp.logger().info("Entered allery: "+allergyType);
				webOp.waitTillAllCallsDoneUsingJS();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'lable-ui-select')][@title='"+allergyType+"']")));
				//webOp.wait(1, 1000);
				webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//input[@placeholder='allergy']")).sendKeys(Keys.RETURN);

				if (reaction != null) {	                
					WebElement reactionElement = webOp.driver().findElement(
							By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//input[@placeholder='reactions']"));
					jse.executeScript("arguments[0].click()", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')]["+i+"]//div[contains(@class,'reactions')]/span")));
					webOp.waitTillAllCallsDoneUsingJS();
					Thread.sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(reactionElement));
					//webOp.waitTillAllCallsDoneUsingJS();
					//Thread.sleep(5000);
					reactionElement.sendKeys(reaction);
					webOp.waitTillAllCallsDoneUsingJS();
					//wait.ignoring(StaleElementReferenceException.class,NoSuchElementException.class).until(ExpectedConditions.visibilityOf(webOp.driver().findElement(By.xpath("//span[@class='select2-results']//span[@title='"+reaction+"']"))));
					Thread.sleep(2000);
					reactionElement.sendKeys(Keys.RETURN);
				}
				Alogs.add(Alogs.size()-(i-1),AuditAction.ALLERGY_CREATED);
				i++;
				webOp.driver().findElement(addAllergyLocator).click();
			}
			webOp.driver().findElement(saveChangesButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(editPatientModal));
		}
	}

	public PatientMedicalHistory editAllergy() throws Exception {
		PatientDetails patientDetails = getPatientDetails();
		if (patientDetails.getEditDetails() == null
				|| patientDetails.getEditDetails().getMedicalHistory() == null
				|| patientDetails.getEditDetails().getMedicalHistory().getAllergies() == null) {
			return this;
		}
		List<Allergies> allergies = patientDetails.getEditDetails().getMedicalHistory().getAllergies();
		int i;
		webOp.waitTillAllCallsDoneUsingJS();
		wait.ignoring(NoSuchElementException.class).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[h2[contains(text(),'Medical History')]]")));
		/*webOp.driver()
		.findElement(By.xpath("//button[contains(@ng-click,'alrgViewCtrl.addEditAllergies')][text()='Edit']"))
		.click();*/
		jse.executeScript("arguments[0].click()", webOp.driver()
				.findElement(By.xpath("//button[contains(@ng-click,'alrgViewCtrl.addEditAllergies')][text()='Edit']")));
		for (Allergies allergy : allergies) {
			String allergyType = allergy.getAllergy();
			String reaction = allergy.getReaction();
			webOp.waitTillAllCallsDoneUsingJS();
			WebElement scrollableField = null;
			By scroll = By.xpath("//div[contains(@class,'allergies-section scrollable')]");
			try	
			{
				scrollableField = webOp.driver()
						.findElement(scroll);
			}
			catch(Exception e) {       
				webOp.logger().info("Element is Not Displayed: " + scroll);
		    }
			if (allergy.getRemoveFromList() != null) {
				if (allergy.getRemoveFromList().getDiscontinue() != null) {
					if(scrollableField!=null)
					{
						jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
					}
					webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][div//input[@title='"+ allergyType + "']]"+discontinueButton+"")).click();
					Alogs.add(AuditAction.ALLERGY_DISCONTINUED);
					continue;
				} else if (allergy.getRemoveFromList().getError() != null) {
					if(scrollableField!=null)
					{
						jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 0);
					}
					webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][div//input[@title='"
							+ allergyType + "']]"+markAsError+"")).click();
					Alogs.add(AuditAction.ALLERGY_MARKED_AS_ERROR);
					continue;
				}
			}
			webOp.driver().findElement(addAllergyLocator).click();
			i = webOp.driver().findElements(By.xpath("//div[contains(@ng-repeat,'newAllergy')]")).size();
			if(scrollableField!=null)
			{
				jse.executeScript("arguments[0].scrollTop = arguments[1];", scrollableField, 150);
			}
			webOp.waitTillAllCallsDoneUsingJS();
			
			jse.executeScript("arguments[0].click()", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i +"]//div[@placeholder='allergy']/span[1]")));
			webOp.waitTillAllCallsDoneUsingJS();
			WebElement allergyNameElement = webOp.driver().findElement(
			        By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//input[@placeholder='allergy']"));
			allergyNameElement.sendKeys(allergyType);
			webOp.waitTillAllCallsDoneUsingJS();
			allergyNameElement.sendKeys(Keys.RETURN);
			webOp.waitTillAllCallsDoneUsingJS();
			
			if (reaction != null) {
				//Thread.sleep(3000)
				jse.executeScript("arguments[0].click()", webOp.driver().findElement(By.xpath("//div[contains(@ng-repeat,'newAllergy')]["+i+"]//div[contains(@class,'reactions')]/span")));;
				WebElement reactionElement = webOp.driver().findElement(
						By.xpath("//div[contains(@ng-repeat,'newAllergy')][" + i + "]//input[@placeholder='reactions']"));
				wait.until(ExpectedConditions.elementToBeClickable(reactionElement));
				reactionElement.sendKeys(reaction);
				webOp.waitTillAllCallsDoneUsingJS();
				reactionElement.sendKeys(Keys.RETURN);
			}
			Alogs.add(AuditAction.ALLERGY_CREATED);
			i++;
		}
		webOp.driver().findElement(saveChangesButtonLocator).click();
		return this;
	}
}
