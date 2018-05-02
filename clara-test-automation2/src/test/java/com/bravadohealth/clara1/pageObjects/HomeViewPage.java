package com.bravadohealth.clara1.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.pageObjects.locators.ConfirmationModalLocators;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.pageObjects.locators.NavLocators;
import com.bravadohealth.clara1.services.UserService;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.Facility;
import com.bravadohealth.pagedataset.ManageFacilityDetails;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class HomeViewPage extends AbstractPage implements ConfirmationModalLocators, NavLocators, LoaderLocators{

	By pickPatientInputLocator = By.id("pick-patient-selectized");
	By addPatientLocator = By.xpath("//span[contains(@class ,'label') and contains(text(),'Add new patient')]");
	By accountLocator = By.xpath("//span[contains(@class,'gn-text') and text()='Account']");
	By getStartedLocator=By.xpath("//button[a[text()='Get Started']]");

	private String getStartedPage="https://clara-dev.bravadocloud.com/#!/app/setup/get-started";


	public HomeViewPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public void logout() throws Exception {
		Boolean discardChanges = null;// should be passed as param
		webOp.waitTillAllCallsDoneUsingJS();
		WebElement accountMenuElement = webOp.driver().findElement(accountMenuLocator);
		new Actions(webOp.driver()).moveToElement(accountMenuElement).click().perform();
		wait.until(ExpectedConditions.presenceOfElementLocated(signOutLocator));
		WebElement signOutElement = webOp.driver().findElement(signOutLocator);
		new Actions(webOp.driver()).moveToElement(signOutElement).click().perform();
		if (webOp.getParameter("patient") != null) {
			Patient patient = dataProvider.getPageData("patient");
			// expect popup if unsaved data
			if (HelperUtility.isElementPresent(webOp, navigateAwayAlertTitleLocator)) {
				if (discardChanges == null || discardChanges == false) {
					// stay on page
					webOp.driver().findElement(cancelButtonLocator).click();
				} else {
					// signout of page
					webOp.driver().findElement(okButtonLocator).click();
				}
			}
		}
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		wait.until(ExpectedConditions.urlContains(AppPage.LoginPage.getUrlContains(hMap)));
	}

	public HomeViewPage selectFacility() throws Exception {
		ManageFacilityDetails manageFacilityDetails = dataProvider.getPageData("manageFacilityDetails");
		if(Integer.parseInt(manageFacilityDetails.getNoOfFacility())>1) {
			String facilityName = null;
			if(webOp.getParameter("facility") != null){
				Facility facilityToSelect = dataProvider.getPageData("facility");
				facilityName = facilityToSelect.getFacilityName();
			}
			//TODO: use the facility name directly from sheet to select facility
			else if(webOp.getParameter("facilityName") != null){
				facilityName = dataProvider.getPageData("facilityName");
			} else {
				//TODO: this else should get removed in future
				facilityName = manageFacilityDetails.getFacility().get(0).getFacilityName();
			}
			webOp.driver().findElement(By.xpath("//a[contains(@class,'ng-binding')][contains(text(),'"+facilityName+"')]")).click();
			webOp.waitTillAllCallsDoneUsingJS();
		}
		
		Alogs.add(AuditAction.USER_SELECTED_FACILITY);
		return this;
	}
	
	/*if(patient.getExistingPatient() != null)
	{
		searchExistingPatient();
	}*/
	
	public AddPatientPage addPatient() throws Exception {
		Patient patient = dataProvider.getPageData("patient");
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.presenceOfElementLocated(pickPatientInputLocator));
		webOp.driver().findElement(pickPatientInputLocator).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(addPatientLocator));
		webOp.driver().findElement(addPatientLocator).click();
		return new AddPatientPage(webOp, dataProvider);
	}
	
	public WritePrescriptionsPage searchExistingPatient() throws Exception {
		Patient patient = dataProvider.getPageData("patient");
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.presenceOfElementLocated(pickPatientInputLocator));
		webOp.driver().findElement(pickPatientInputLocator).click();
		PatientDetails patientDetails = patient.getExistingPatient();
		Demographic demographic = patientDetails.getDemographic();
		webOp.waitTillAllCallsDoneUsingJS();
		if (demographic.getNameSearchInput() != null) {
			String nameSearchInput = demographic.getNameSearchInput();
			webOp.driver().findElement(pickPatientInputLocator).sendKeys("" + nameSearchInput + "");
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(By.xpath("//span[contains(text(),'"+demographic.getFirstName()+"')]")).click();
		}
		else if (demographic.getDobSearchInput() != null) {
			String dobSearchInput = demographic.getDobSearchInput();
			dobSearchInput = dobSearchInput.replace('-', '/');
			webOp.driver().findElement(pickPatientInputLocator).sendKeys("" + dobSearchInput + "");
			webOp.waitTillAllCallsDoneUsingJS();
			webOp.driver().findElement(By.xpath("//span[contains(text(),'"+demographic.getFirstName()+"')]")).click();
		}
		return new WritePrescriptionsPage(webOp, dataProvider);
	}
	
	public WritePrescriptionsPage searchPatient() throws Exception {
		Patient patient = dataProvider.getPageData("patient");

		if (patient != null) {
			PatientDetails patientDetails = patient.getExistingPatient();
			Demographic demographic = patientDetails.getDemographic();
			By dataSelectableDivLocator = By.cssSelector("div[data-selectable]");
			//By birthdaySpanLocator = By.cssSelector("span.caption");
			// By nameSpanLocator = By.cssSelector("span.label");

			webOp.waitTillAllCallsDoneUsingJS();
			List<WebElement> elements = null;
			boolean foundElement = false;
			if (demographic.getDobSearchInput() != null) {
				String dobSearchInput = demographic.getDobSearchInput();
				dobSearchInput = dobSearchInput.replace('-', '/');
				webOp.driver().findElement(pickPatientInputLocator).sendKeys("" + dobSearchInput + "");
				webOp.waitTillAllCallsDoneUsingJS();
				try {
					webOp.driver().findElement(By.xpath("//span[contains(text(),'"+demographic.getFirstName()+"')][contains(text(),'"+demographic.getLastName()+"')]")).click();
					foundElement = true;
				}
				catch(org.openqa.selenium.NoSuchElementException ex) {
					foundElement = false;
					// throw the caught exception if its not a negative scenario
					if(!dataProvider.getPageData("negetiveScenarioCheck").equals("Yes"))
					{
						throw ex;
					}
				}
				/*elements = webOp.driver().findElements(dataSelectableDivLocator);
				for (WebElement element : elements) {
					String spanText = element.findElement(By.cssSelector("span.label")).getText();
					if (spanText != null && spanText.equals("Add new patient")) {
						continue;
					} else {
						String completeName = spanText;
						String captionSpanText = element.findElement(By.xpath("//span[contains(@class,'label text-overflow-ellipsis')][contains(@title,'"+ demographic.getFirstName() +"')]")).getText();
						if (completeName
								.contains(demographic.getFirstName() + " "
										+ (demographic.getMiddleName() != null ? (demographic.getMiddleName() + " ")
												: "")
										+ demographic.getLastName())
								&& captionSpanText.contains(dobSearchInput)) {
							wait.until(ExpectedConditions.elementToBeClickable(element));
							element.click();
							foundElement = true;
							break;
						}
					}
				}*/
				
			} else if (demographic.getNameSearchInput() != null) {
				String nameSearchInput = demographic.getNameSearchInput().toLowerCase();
				webOp.driver().findElement(pickPatientInputLocator).sendKeys("" + nameSearchInput + "");
				webOp.waitTillAllCallsDoneUsingJS();
				elements = webOp.driver().findElements(dataSelectableDivLocator);
				StringBuilder nameSearchInputPatternBuilder = new StringBuilder("(.*)");
				for (String nameParts : nameSearchInput.split("\\s+")) {
					nameSearchInputPatternBuilder.append(nameParts + "(.*)");
				}
				webOp.waitTillAllCallsDoneUsingJS();
				for (WebElement element : elements) {
					//WebElement foll = element.findElement(By.xpath("//span[contains(@class,'label text-overflow-ellipsis')][contains(@title,'"+ demographic.getNameSearchInput() +"')]"));
					//String spanText = element.findElement(By.xpath("//span[contains(@class,'label text-overflow-ellipsis')][contains(@title,'"+ demographic.getFirstName() +"')]")).getText();
					System.out.println("Address is "+demographic.getAddress());
					//String spanText = webOp.driver().findElement(By.xpath("//span[contains(text(),'"+demographic.getFirstName()+"')][contains(text(),'"+demographic.getLastName()+"')][contains(text(),'"+demographic.getAddress()+"')]")).getText();
					
					WebElement desiredSearchResultElement;
					
					try {
						desiredSearchResultElement = webOp.driver().findElement(By.xpath("//span[@title='"+demographic.getFirstName()+" "+demographic.getLastName()+"']"));
					}
					catch(org.openqa.selenium.NoSuchElementException ex) {
						foundElement = false;
						break;
					}
					
					String spanText = desiredSearchResultElement.getAttribute("innerText");
					System.out.println(spanText);
					
					webOp.waitTillAllCallsDoneUsingJS();
					if (spanText != null && !spanText.isEmpty()) {
						if(spanText.equals("Add new patient")) {
							continue;
						} else {
							String completeName = spanText; // String to be scanned
							// to find the pattern.
							WebElement innerSpanWithGenderDOBAddressElement = desiredSearchResultElement.findElement(By.xpath("//span[contains(text(),'"+demographic.getAddress()+"')]"));
							if (completeName.toLowerCase().matches(nameSearchInputPatternBuilder.toString()) && 
									innerSpanWithGenderDOBAddressElement.getAttribute("innerText").contains(demographic.getBirthday())) {
								desiredSearchResultElement.click();
								foundElement = true;
								break;
							}
						}
					}
				}
			}
			if (foundElement != true)
			{
				webOp.logger().info("Patient not found");
				throw new Exception("Patient not found");
			}
		}
		
		return new WritePrescriptionsPage(webOp, dataProvider);
	}

	public ManagePatientPage ClickPatient() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOf(webOp.driver().findElement(By.xpath("//span[contains(text(),'Patients')]"))));
		webOp.driver().findElement(By.xpath("//span[contains(text(),'Patients')]")).click();
		webOp.waitTillAllCallsDoneUsingJS();
		return new ManagePatientPage(webOp, dataProvider);
	}

	public AccountPage moveToAccountPage() throws Exception {
		
		Thread.sleep(2000);
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(accountMenuLocator));
		retryingFindClick(accountMenuLocator);
		webOp.waitTillAllCallsDoneUsingJS();
		try {
			
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(accountLinkLocator));
			webOp.driver().findElement(accountLinkLocator).click();
		} catch (Exception e) {

			retryingFindClick(accountMenuLocator);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(accountLinkLocator));
			webOp.driver().findElement(accountLinkLocator).click();
		}
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		return new AccountPage(webOp, dataProvider);
	}

	public CreateFacilityPage navigateToCreateFacility(){
		webOp.driver().get(getStartedPage);
		webOp.driver().findElement(getStartedLocator).click();
		return new CreateFacilityPage(webOp, dataProvider);
	}
	
	public HomeViewPage respondToSelfApproveForEPCSPopup() throws Exception {
		Boolean selfApproveForEPCSResponse = Boolean.valueOf(webOp.getParameter("selfApproveForEPCSResponse"));
		return respondToSelfApproveForEPCSPopup(selfApproveForEPCSResponse);
	}
	
	public HomeViewPage respondToSelfApprovePopupAgainAfterInitialCancelResponse() throws Exception {
		Boolean selfApproveForEPCSResponseAgain = Boolean.valueOf(webOp.getParameter("selfApproveForEPCSResponseAgain"));
		return respondToSelfApproveForEPCSPopup(selfApproveForEPCSResponseAgain);
	}
	
	private HomeViewPage respondToSelfApproveForEPCSPopup(Boolean selfApproveForEPCSResponse) throws Exception {
		wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
		WebElement selfApprovalModal = webOp.driver().findElement(modalLocator);
		if(Boolean.TRUE == selfApproveForEPCSResponse) {
			selfApprovalModal.findElement(okButtonLocator).click();
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
			HelperUtility.completeTwoFAWithDUOBypass(webOp, wait, jse, UserService.getCurrentUser());
			boolean enrolled = HelperUtility.isToasterDisplayed(webOp);
			if(!enrolled) {
				webOp.logger().error("epcs not enrolled email = {}", UserService.getCurrentUser().getEmail());
				webOp.getSoftAssert().fail("Could not enroll user for epcs"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			} else {
				Alogs.add(AuditAction.SELF_APPROVE_FOR_EPCS);
			}
		}
		else if(Boolean.FALSE == selfApproveForEPCSResponse) {
			webOp.driver().findElement(cancelButtonLocator).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(modalLocator));
		}
		return this;
	}
}




