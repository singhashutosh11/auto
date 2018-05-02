package com.bravadohealth.clara1.pageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.enums.FormResetClass;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.DemographicLocators;
import com.bravadohealth.clara1.pageObjects.locators.EditModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.ManagePatientLocators;
import com.bravadohealth.clara1.pageObjects.locators.MedicalHistoryLocators;
import com.bravadohealth.clara1.pageObjects.locators.NavLocators;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.clara1.utility.PatientUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.AddPatientsData;
import com.bravadohealth.pagedataset.Contact;
import com.bravadohealth.pagedataset.Deactivate;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.ManageExistingPatient;
import com.bravadohealth.pagedataset.MedicalHistory;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class ManagePatientPage extends AbstractPage implements ConfirmationModalLocators, EditModalLocators, DemographicLocators,
MedicalHistoryLocators, ManagePatientLocators, NavLocators {

    By addPatientButtonLocator = By.xpath("//button[icon[@class='icon-add']]");
    By createAnotherCheckBoxLocator = By.xpath("//label[contains(@class,'create-another')]/input[@type='checkbox']");
    By searchInput=By.xpath("//input[@placeholder='search']");
    
	public ManagePatientPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
	}

	private List<Patient> getAddPatientsList() {
	     AddPatientsData addPatientsData= dataProvider.getPageData("addPatientsData");
	     List<Patient> patients = addPatientsData.getPatients();
        return patients;
	}
	   
	public ManagePatientPage selectPatientToDeactivate() throws Exception {
		ManageExistingPatient manageExistingPatient = dataProvider.getPageData("manageExistingPatient");
		Deactivate deactivate = manageExistingPatient.getDeactivate();
		if (deactivate != null) {
			int noOfPatients = Integer.parseInt(deactivate.getNumberOfPatient());
			int i = 1;
			ArrayList<Patient> listOfDeactivatedPatient = new ArrayList<Patient>();
			while (i <= noOfPatients) {
				String locator = "/div[contains(@class,'ui-grid-viewport')]//div[contains(@ng-repeat,'rowRenderIndex, row')]["
						+ i + "]";
				webOp.driver().findElement(By.xpath("//div[contains(@class,'ui-grid-render-container-left')]" + locator + "")).click();
				Patient deactivatedPatient = new Patient();
				PatientDetails existingPatientDetails = new PatientDetails();
				existingPatientDetails.setDemographic(new Demographic());
				existingPatientDetails.setContact(new Contact());
				deactivatedPatient.setExistingPatient(existingPatientDetails);
				WebElement patientRecord = webOp.driver()
						.findElement(By.xpath("//div[contains(@class,'ui-grid-render-container-body')]" + locator + ""));

				deactivatedPatient.getExistingPatient().getDemographic().setFirstName(patientRecord
						.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][1]//a")).getText());
				deactivatedPatient.getExistingPatient().getDemographic()
						.setBirthday(patientRecord
								.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][2]/div"))
								.getAttribute("title"));
				deactivatedPatient.getExistingPatient().getDemographic()
						.setAddress(patientRecord
								.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][3]/div"))
								.getAttribute("title"));
				String contactNumber = patientRecord
						.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][4]/div"))
						.getAttribute("title");
				if (!contactNumber.isEmpty() && contactNumber != null) {
					deactivatedPatient.getExistingPatient().getContact().setPhone(Long.parseLong(contactNumber));
				}
				deactivatedPatient.getExistingPatient().getContact()
						.setEmail(patientRecord
								.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][5]/div"))
								.getAttribute("title"));
				listOfDeactivatedPatient.add(deactivatedPatient);

				i++;
			}
			webOp.driver().findElement(By.xpath("//button[contains(@class,'deactivate-patient')]")).click();
			webOp.waitTillAllCallsDoneUsingJS();
			if (webOp.driver().findElement(By.xpath("//div[@class='modal-content']")).isDisplayed()) {
				if (deactivate.getDeactivate().equals("true")) {
					webOp.driver().findElement(okButtonLocator).click();
					webOp.waitTillAllCallsDoneUsingJS();
					VerifyDeactivatedPatient(listOfDeactivatedPatient);
				} else if (deactivate.getDeactivate().equals("false")) {
					webOp.driver().findElement(cancelButtonLocator);
				}
			}
		}
		return this;
	}

	public void VerifyDeactivatedPatient(List<Patient> listOfDeactivatedPatient) throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		HelperUtility.verifyToasterDisplayed(webOp, "successfully deactivated patient message not displayed");
		By showDeactivatedListButtonLocator = By.xpath("//button[contains(@ng-class,'showDeactivatePatient')]");
		Thread.sleep(4000);
        
		webOp.driver().findElement(showDeactivatedListButtonLocator).click();
		for (int i = 1; i <= listOfDeactivatedPatient.size(); i++) {
			PatientDetails existingPatient = listOfDeactivatedPatient.get(i - 1).getExistingPatient();
			String DeactivatePatientPhoneNumber = null;

			String locator = "/div[contains(@class,'viewport')]//div[contains(@ng-repeat,'rowRenderIndex, row')][" + i
					+ "]";
			WebElement patientRecord = webOp.driver()
					.findElement(By.xpath("//div[contains(@class,'container-body')]" + locator + ""));

			String displayedPatientName = patientRecord
					.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][1]//a")).getText();
			String displayedPatientBirthDay = patientRecord
					.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][2]/div"))
					.getAttribute("title");
			String displayedPatientAddress = patientRecord
					.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][3]/div"))
					.getAttribute("title");
			String displayedPatientPhoneNumber = patientRecord
					.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][4]/div"))
					.getAttribute("title");
			String displayedPatientPhoneEmail = patientRecord
					.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][5]/div"))
					.getAttribute("title");

			String DeactivatePatientName = existingPatient.getDemographic().getFirstName();
			String DeactivatePatientBirthDay = existingPatient.getDemographic().getBirthday();
			String DeactivatePatientAddress = existingPatient.getDemographic().getAddress();
			if (existingPatient.getContact().getPhone() != null) {
				DeactivatePatientPhoneNumber = Long.toString(existingPatient.getContact().getPhone());
			} else {
			    DeactivatePatientPhoneNumber = "";
			}
			String DeactivatePatientEmail = existingPatient.getContact().getEmail();

			Assert.assertEquals(DeactivatePatientName, displayedPatientName,
					"The deactivated patientName is not displaying properly");
			Assert.assertEquals(DeactivatePatientBirthDay, displayedPatientBirthDay,
					"The deactivated Pateint birthdy is not displaying properly");
			Assert.assertEquals(DeactivatePatientAddress, displayedPatientAddress,
					"The deactivated patient address is not displaying properly");
			Assert.assertEquals(DeactivatePatientPhoneNumber, displayedPatientPhoneNumber,
					"The deactivated Pateint phone number is not displaying properly");
			Assert.assertEquals(DeactivatePatientEmail, displayedPatientPhoneEmail,
					"The deactivated Pateint email is not displaying properly");
		}
	}

	public ManagePatientPage searchPatient() throws Exception{
		ManageExistingPatient manageExistingPatient = dataProvider.getPageData("manageExistingPatient");
		String searchText = manageExistingPatient.getSearchPatient().getSearchText();
		Boolean searchFormat = manageExistingPatient.getSearchPatient().isSearchFormat();
		String searchField=manageExistingPatient.getSearchPatient().getSearchField();
		if (searchFormat != null && searchFormat.equals(true)) {
			webOp.driver().findElement(searchInput).sendKeys(searchField+":"+searchText);
			webOp.driver().findElement(searchInput).sendKeys(Keys.ENTER);
		} else {
			webOp.driver().findElement(searchInput).sendKeys(searchText);
			webOp.driver().findElement(By.xpath("//tr[td[text()='" + searchField + "']]")).click();	
			Thread.sleep(2000);
			if(webOp.driver().findElement(By.xpath("//div[contains(@class,'ui-grid-row ng-scope')]")) !=null && dataProvider.getPageData("searchType").toString() != null) {
				String sType = dataProvider.getPageData("searchType").toString();
				if(sType.equals("Name"))
				{
					String name=webOp.driver().findElement(By.xpath("//div[contains(@class,'ui-grid-row ng-scope')][1]/div//a[contains(@class,'ng-binding')]")).getText();
					name= name.toLowerCase();
					searchText=searchText.toLowerCase();
					Boolean nameCheck = name.contains(searchText);
					/*System.out.println(name);
					System.out.println(searchText);*/
					Assert.assertTrue(nameCheck,"Incorrect Search");
				}
				else if (sType.equals("DOB")) {
					String dob=webOp.driver().findElement(By.xpath("(//div[contains(@class,'ui-grid-row ng-scope')][1]/div/div/div)[3]")).getText();
					Boolean dobCheck = dob.contains(searchText);
					/*System.out.println(dob);
					System.out.println(searchText);*/
					Assert.assertTrue(dobCheck,"Incorrect Search");
				}
				else if (sType.equals("pNumber")) {
					String pNo=webOp.driver().findElement(By.xpath("(//div[contains(@class,'ui-grid-row ng-scope')][1]/div/div/div)[5]")).getText();
					Boolean pNoCheck = pNo.contains(searchText);
					System.out.println(pNo);
					System.out.println(searchText);
					Assert.assertTrue(pNoCheck,"Incorrect Search");
				}
				else if (sType.equals("eMail")) {
					String email=webOp.driver().findElement(By.xpath("(//div[contains(@class,'ui-grid-row ng-scope')][1]/div/div/div)[6]")).getText();
					searchText=searchText.toLowerCase();
					Boolean emailCheck = email.contains(searchText);
					System.out.println(email);
					System.out.println(searchText);
					Assert.assertTrue(emailCheck,"Incorrect Search");
				}
			}
				
		}
		return this;
	}

	public boolean isAddPatientsDataAvailable() throws Exception {
        List<Patient> addPatientsList = getAddPatientsList();
        if (addPatientsList != null && !addPatientsList.isEmpty()) {
                return true;
        }
        return false;
    }

    public ManagePatientPage addPatients() throws Exception {
        openModalWindow();
        
        List<Patient> addPatientsList = getAddPatientsList();
        List<Patient> addedPatientsList = new ArrayList<Patient>();
        WritePrescriptionsPage wp = new WritePrescriptionsPage(webOp, dataProvider);
        
        for (Patient patient : addPatientsList) {
            PatientDetails newPatientDetails = patient.getNewPatient();
            wp.editPatientProfile(newPatientDetails);//adding patient profile
            wp.addEditMedicalHistory(newPatientDetails.getMedicalHistory());//add medical history
            
            Demographic demographyOfPatient = newPatientDetails.getDemographic();
            MedicalHistory medicalHistory = newPatientDetails.getMedicalHistory();
            if ((demographyOfPatient.isNavigateAway() != null && (demographyOfPatient.isNavigateAway().booleanValue() == true))
                    || (medicalHistory != null && medicalHistory.isNavigateAway() != null && (medicalHistory.isNavigateAway().booleanValue() == true))) {
                    // browser back or click on some link
                    HelperUtility.navigateBack(webOp);
                    //check url changed
                    HelperUtility.checkUrlChangesTo(webOp, AppPage.SelectPatient.getUrlContains(hMap));
                    WebElement patientsPageLink = webOp.driver().findElement(patientsLinkLocator);
                    jse.executeScript("arguments[0].click();", patientsPageLink);
                    //check url changed
                    webOp.waitTillAllCallsDoneUsingJS();
                    HelperUtility.checkUrlChangesTo(webOp, AppPage.ManagePatients.getUrlContains(hMap));
                    break;
            }
            checkCreateAnother(patient.isAddAnother());
            if (newPatientDetails.isCancelChanges() != null && newPatientDetails.isCancelChanges() == true) {
                webOp.driver().findElement(cancelChangesButtonLocator).click();
                HelperUtility.checkModalClosed(webOp, editPatientModal);
                break;
            }else {
                webOp.driver().findElement(saveChangesButtonLocator).click();
            }
            
            // expect error message if duplicate
            DuplicatePatientPage dp = PatientUtility.checkDuplicateShown(webOp, dataProvider, demographyOfPatient);
            if (dp instanceof DuplicatePatientPage) {
               webOp.driver().findElement(cancelChangesButtonLocator).click();
               break;
            }
            
            if(patient.isAddAnother() != null && patient.isAddAnother().booleanValue() == true){
                HelperUtility.verifyToasterDisplayed(webOp, "Patient details saved successfully message is not displayed");
                if(!verifyFormResetHappens()) {
                    Assert.fail("form reset did not happen");
                }
            } else {
                HelperUtility.checkModalClosed(webOp, editPatientModal);
            }
            addedPatientsList.add(patient);
        }
        checkPatientsListUpdated(addedPatientsList);
        return this;
    }
   
    public void openModalWindow() throws Exception {
    	
        wait.until(ExpectedConditions.urlContains("/manage-patients"));
        webOp.driver().findElement(addPatientButtonLocator).click();
        wait = new WebDriverWait(webOp.driver(), 2000);
        wait.until(ExpectedConditions.presenceOfElementLocated(editPatientModal));
        return;
    }
    
    public void checkCreateAnother(Boolean addAnother) throws Exception {
        WebElement createAnotherCheckBox = webOp.driver().findElement(createAnotherCheckBoxLocator);
        boolean isSelected = createAnotherCheckBox.isSelected();
        if(addAnother != null && addAnother.booleanValue() == true) {
            if(!isSelected){
                webOp.driver().findElement(createAnotherCheckBoxLocator).click();
            }
        } else{
            if(isSelected){
                webOp.driver().findElement(createAnotherCheckBoxLocator).click();
            }
        }
    }
    
    public boolean verifyFormResetHappens() {
        //check for ng-pristine or ng-untouched class
        By [] demographicElementLocators = {firstNameLocator, lastNameLocator, middleNameLocator, addressLocator,
                cityLocator, stateLocator, zipCodeLocator, birthdayLocator, genderLocator, weightLocator,
                phoneLocator, emailLocator};
        for (By locator : demographicElementLocators) {
            String locatorClass = webOp.driver().findElement(locator).getAttribute("class");
            if(!locatorClass.contains(FormResetClass.Pristine.getFormClass())){
                return false;
            }
        }
        
       By allergySelectizeLocator =By.cssSelector("selectize[config='alrgFormCtrl.allergySelectize.config']");
       By reactionSelectizeLocator =By.cssSelector("selectize[config='alrgFormCtrl.reactionsSelectize.config']");
       By homeMedSelectizeLocator =By.cssSelector("selectize[config='HMFormCtrl.drugNameSelectize.config']");
       By diagnosisSelectizeLocator =By.cssSelector("selectize[config='digNosFormCtrl.diagnosisSelectize.config']");
       
       By [] medicalHistoryElementLocators = {allergySelectizeLocator, reactionSelectizeLocator, homeMedSelectizeLocator,
               diagnosisSelectizeLocator};
       for (By locator : medicalHistoryElementLocators) {
           List<WebElement> elements = webOp.driver().findElements(locator);
           if(elements.size()!=1){
               return false;
           }
            String locatorClass = elements.get(0).getAttribute("class");
            if(!locatorClass.contains(FormResetClass.Pristine.getFormClass())){
                return false;
            }
        }
        return true;
    }

    public ManagePatientPage checkPatientsListUpdated(List<Patient> addedPatientsList) throws InterruptedException {
        List<PatientDetails> clonedPatientDetailsList = PatientUtility.getClonedAddPatientDetailsList(addedPatientsList);
        Thread.sleep(2000);
        // get ng-repeat elements from top to check if added elements exist
        List<PatientDetails> visiblePatientRecords = new ArrayList<PatientDetails>();
        Integer patientsDataSerialStarting = Integer.parseInt(webOp.driver().findElement(patientsDataSerialStartingLocator).getText());
        Integer patientsDataSerialEnding = Integer.parseInt(webOp.driver().findElement(patientsDataSerialEndingLocator).getText());
        int totalRecordsVisibleOnPage = patientsDataSerialEnding - patientsDataSerialStarting + 1;
        int totalAddedPatients = addedPatientsList.size();
        int numPatientsToCompare = Math.min(totalAddedPatients, totalRecordsVisibleOnPage);
        for(int i=1; i <=numPatientsToCompare; i++){
            WebElement patientFullNameElement = webOp.driver().findElement(By.xpath(dataGridLocator + patientDataRowLocatorSuffix + "["+i+"]" + patientDataLocatorPrefix+"[1]//a"));
            WebElement patientBirthdayElement = webOp.driver().findElement(By.xpath(dataGridLocator + patientDataRowLocatorSuffix + "["+i+"]" + patientDataLocatorPrefix+"[2]/div"));
            WebElement patientAddressElement = webOp.driver().findElement(By.xpath(dataGridLocator + patientDataRowLocatorSuffix + "["+i+"]" + patientDataLocatorPrefix+"[3]/div"));

            PatientDetails patientDetails = new PatientDetails();
            patientDetails.setDemographic(new Demographic());
            patientDetails.getDemographic().setFirstName(patientFullNameElement.getText());
            patientDetails.getDemographic().setBirthday(patientBirthdayElement.getAttribute("title"));
            patientDetails.getDemographic().setAddress(patientAddressElement.getAttribute("title"));
            visiblePatientRecords.add(patientDetails);
        }
        
        //order of elements in view is opposite to that in xml or order of addition 
        for (int i = 0; i< numPatientsToCompare; i++ ) {
            PatientDetails addedPatientDetails = clonedPatientDetailsList.get(totalAddedPatients-1-i);
            if(PatientUtility.arePatientsSame(addedPatientDetails, visiblePatientRecords.get(i))){
                addedPatientsList.remove(totalAddedPatients-1-i);
            } else {
                Assert.fail("#"+ (totalAddedPatients-i) +"patient from starting is not in view");
            }
        }
        if(addedPatientsList.isEmpty()) {
            return this;
        } else {
            if(webOp.driver().findElement(nextPageButtonLocator).isEnabled()) {
                return checkPatientsListUpdated(addedPatientsList);
            } else{
                Assert.fail(addedPatientsList.size() + " patients are not in view");
                return null;
            }
        }
    }
    
	public ManagePatientPage editPatient() throws Exception {
		ManageExistingPatient manageExistingPatient = dataProvider.getPageData("manageExistingPatient");
		PatientDetails editDetails = manageExistingPatient.getEditDetails();
		if (editDetails != null) {
			webOp.waitTillAllCallsDoneUsingJS();
			String locator = "/div[contains(@class,'ui-grid-viewport')]//div[contains(@ng-repeat,'rowRenderIndex, row')][1]";
			webOp.driver().findElement(By.xpath("//div[contains(@class,'ui-grid-render-container-body')]" + locator + "//a")).click();
		}
		return this;
	}

	public AbstractPage editPatientProfileDetails() throws Exception {
		ManageExistingPatient manageExistingPatient = dataProvider.getPageData("manageExistingPatient");
		PatientDetails editDetails = manageExistingPatient.getEditDetails();
		if(editDetails==null){
			return this;
		}
		return new WritePrescriptionsPage(webOp, dataProvider).editPatientProfile(editDetails);
	}

}
