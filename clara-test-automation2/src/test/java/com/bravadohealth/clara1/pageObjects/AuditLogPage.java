package com.bravadohealth.clara1.pageObjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.dto.PrescriptionAuditDTO;
import com.bravadohealth.clara1.enums.AppPage;
import com.bravadohealth.clara1.enums.DDAlertResponse;
import com.bravadohealth.clara1.enums.DrugInteractionAlertType;
import com.bravadohealth.clara1.enums.EPCSNominatorType;
import com.bravadohealth.clara1.enums.SupportedTimeZoneEnum;
import com.bravadohealth.clara1.pageObjects.constants.ApplicationConstatnts;
import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.pageObjects.locators.NavLocators;
import com.bravadohealth.clara1.services.FacilityService;
import com.bravadohealth.clara1.services.PatientService;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.clara1.utility.PatientUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pageObjects.ApplicationPage;
import com.bravadohealth.pagedataset.AuditLogFilter;
import com.bravadohealth.pagedataset.AuditRecordMatchingColumns;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.DDInteracnAlert;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.EditFacility;
import com.bravadohealth.pagedataset.EditFacilitySection;
import com.bravadohealth.pagedataset.EditFacilitySettings;
import com.bravadohealth.pagedataset.Facility;
import com.bravadohealth.pagedataset.Nominator;
import com.bravadohealth.pagedataset.PageData;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PharmacyPageData;
import com.bravadohealth.pagedataset.PatientDetails;
import com.bravadohealth.pagedataset.Prescription;
import com.bravadohealth.pagedataset.PrescriptionsPageData;
import com.bravadohealth.pagedataset.SearchOption;

import trial.keyStone.automation.AuditAction;
import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class AuditLogPage extends AbstractPage implements LoaderLocators, NavLocators{

	By AuditLogs=By.xpath("//div[contains(@class,'container-body')]//div[@class='ui-grid-canvas']/div[contains(@ng-repeat,'rowRenderIndex, row')]");
	String auditLogLocator="//div[contains(@class,'container-body')]//div[@class='ui-grid-canvas']/div[contains(@ng-repeat,'rowRenderIndex, row')]";

	By searchInputType=By.xpath("//select[@placeholder='search Type']");
	By searchInputText=By.cssSelector("input[placeholder='search']");
	By showMyLog=By.cssSelector("input[placeholder='Show my logs only']");
	By startDate=By.cssSelector("input[placeholder='start date']");
	By endDate=By.cssSelector("input[placeholder='end data']");

	private By auditLogTimeStampLocator = By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][1]/div");
	private By auditLogDrugNameLocator = By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][5]/div");
	private By auditLogPatientNameLocator = By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][6]/div");

	private String showDetailsLinkLocatorString = "//div[contains(@ng-repeat,'colRenderIndex, col')][7]/div/a";
	private By showDetailsModalLocator = By.xpath("//div[@class='modal-content']");
	private By showDetailsModalContentLocator = By.xpath("//div[@class='modal-content']//div[contains(@class,'modal-body')]/pre");
	private By showDetailsModalCloseButtonLocator = By.xpath("//div[@class='modal-content']//button[contains(@class, 'close')]");

	private By auditLogScrollContainer = By.xpath("//div[contains(@class, 'ui-grid-viewport')]");
	public AuditLogPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public void logout() throws Exception {
		new GlobalNavContainer(webOp, dataProvider).logout();
	}

	public AbstractPage viewRecentActivityLog() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		navigateToAuditPage();
		int i=0;
		WebElement auditlog=null;
		Date date = new Date();
		String curTime = String.format("%02d:%02d", date.getHours(), date.getMinutes()-1);
		List<String> actionlist = new ArrayList<String>();
		List<WebElement> arr = webOp.driver().findElements(By.xpath("//div[contains(text(),'"+curTime+"')]/preceding::div[contains(@ng-repeat,'rowRenderIndex, row')]//div[contains(@ng-repeat,'colRenderIndex, col')][2]/div"));//div[contains(@ng-repeat,'rowRenderIndex, row')]//div[contains(text(),'"+date.getHours()+":"+date.getMinutes()+"')]"));
		arr.addAll(webOp.driver().findElements(By.xpath("//div[contains(text(),'"+String.format("%02d:%02d", date.getHours(), date.getMinutes())+"')]/preceding::div[contains(@ng-repeat,'rowRenderIndex, row')]//div[contains(@ng-repeat,'colRenderIndex, col')][2]/div")));
		for(int j=0; j<arr.size();j++) {
			String title = arr.get(j).getAttribute("title");
			actionlist.add(title);
		}
		for(int j=0;j<Alogs.size();j++)
		{
			if(!actionlist.contains(Alogs.get(j).getCode()))
			{
				webOp.logger().info("Did not find "+Alogs.get(j).getCode()+" in the Audit Trail");
				webOp.getSoftAssert().assertTrue(actionlist.contains(Alogs.get(j).getCode()),"Did not find "+Alogs.get(j).getCode()+" in the Audit Trail");
				break;
			}

		}

		//TODO: update the below code to check if any prescription was deleted , then skip validating
		//TODO: take prescriptions form PatientService.getUpdatedPrescriptions
		webOp.logger().info("Validated the Audit trail");
		while(i<Alogs.size()){

			auditlog=webOp.driver().findElement(By.xpath(""+auditLogLocator+"["+(i+1)+"]"));
			String userName=auditlog.findElement(By.xpath("//div[contains(@ng-repeat,'colRenderIndex, col')][4]/div")).getAttribute("title");
			if(userName.equals(hMap.get("userName"))) {
				if(Alogs.get(Alogs.size()-i-1).getCode() != null) {
					//code is failing at patient account modified before patient created log
					String auditAction=webOp.driver().findElement(By.xpath("//div[contains(@class,'container-body')]//div[@class='ui-grid-canvas']/div[contains(@ng-repeat,'rowRenderIndex, row')]["+(i+1)+"]//div[contains(@ng-repeat,'colRenderIndex, col')][2]/div")).getText();
					//Assert.assertEquals(auditAction,Alogs.get(Alogs.size()-1-i).getCode(),"The audit log for the corresponding acction is sorted correctly");
					if(auditAction.equals(AuditAction.MEDICATION_PRESCRIBED.getCode())) {
						//if(auditAction.equals(AuditAction.PRESCRIPTION_CREATED.getCode())) {	
						List<Prescription> prescriptions =new WritePrescriptionsPage(webOp, dataProvider).getPrescriptionsData().getPrescription();
						int k=1;
						for (Prescription prescription : prescriptions) {
							webOp.waitTillAllCallsDoneUsingJS();
							//webOp.driver().findElement(By.xpath(""+auditLogLocator+"["+(i+1)+"]//div[contains(@ng-repeat,'colRenderIndex, col')][6]/div")).click();
							//Thread.sleep(3000);
							webOp.driver().findElement(By.xpath("(//div[contains(@ng-repeat,'rowRenderIndex, row') and contains(.//div,'MEDICATION_PRESCRIBED')])["+(k++)+"]//div[contains(@ng-repeat,'(colRenderIndex, col) in colContainer.renderedColumns track by col.uid')][7]")).click();
							Thread.sleep(12000);
							System.out.println("i is "+i);
							if(webOp.driver().findElement(By.xpath("//div[@class='modal-content'][div/button[contains(@ng-click,'auditModalCtrl.close')]]")).isDisplayed()) {
								String completeJson=webOp.driver().findElement(By.xpath("//div[@class='modal-content']/div[@class='text-left cs-overlay-content ng-scope']/div[@class='modal-body details-modal-content']/pre")).getText();
								String drugName = prescription.getDrugName();
								String dose = prescription.getDose();
								String unit = prescription.getUnit();
								String frequency = prescription.getFrequency();
								Integer duration = prescription.getDuration();
								String customSig = prescription.getCustomSig();
								String dispenseAmount = prescription.getDispenseAmount();
								String earliestFillDate = prescription.getEarliestFillDate();
								Integer refills = prescription.getRefills();
								Boolean substitutes = prescription.isSubstitutes();
								String noteToPharmacy = prescription.getNoteToPharmacy();
								String response=null;
								if(!prescription.getDdInteracnAlert().isEmpty())
								{
									for(DDInteracnAlert interacnAlert : prescription.getDdInteracnAlert()) {
										response= interacnAlert.getResponse();
										if (DDAlertResponse.Accept == DDAlertResponse.getAlertResponse(response)) {
											break;
										}
									}
								}
								System.out.println(drugName);
								Boolean drugNameCheck=completeJson.contains(drugName);
								System.out.println(drugNameCheck);
								Boolean doseCheck=completeJson.contains(dose);
								Boolean customSigCheck=completeJson.contains(customSig);
								Boolean dispenseAmountCheck=completeJson.contains(dispenseAmount);
								Boolean frequencyCheck=completeJson.contains(frequency.toUpperCase());
								if(response==null || response=="ignore")
								{
									Assert.assertTrue(drugNameCheck, "The drugName is displaying correctly");
									Assert.assertTrue(doseCheck, "The dose is displaying correctly");
									Assert.assertTrue(customSigCheck, "The CustomSig is displaying correctly");
									Assert.assertTrue(dispenseAmountCheck, "The dispenseAmount is displaying correctly");
									Assert.assertTrue(frequencyCheck, "The frequency is displaying correctly");
								}

								//Assert.assertEquals(actual,substitutes, "The drugName is displaying correctly");
								webOp.driver().findElement(By.xpath("//div[@class='modal-content']//button")).click();
							}
							i++;
						}
						i--;
					}
				}
			}
			i++;
		}
		return new AbstractPage(webOp, dataProvider);
	}

	private void verifyAuditTrailForSentEPrescriptions(AuditLogFilter auditFilter) throws Exception {
		List<WebElement> auditlogs;
		String completeJson;
		
		List<Prescription> prescriptions = PatientService.getUndeletedPrescriptions();
		List<Prescription> ePrescribedPrescriptions = new ArrayList<Prescription>();
		Set<String> uniqueDrugNames = new HashSet<String>();
		for(Prescription prescription : prescriptions) {
			if(prescription.isEPrescribe()!=null && Boolean.TRUE == prescription.isEPrescribe()) {
				uniqueDrugNames.add(prescription.getDrugName());
				ePrescribedPrescriptions.add(prescription);
			}
		}
		
		if(ePrescribedPrescriptions.isEmpty()) {
			return;
		}
		int totalEprescriptions = ePrescribedPrescriptions.size();
		String expectedPatientFullName = PatientUtility.getPatientFullNameExcludingMiddleName((Patient) dataProvider.getPageData("patient"));

		searchLog(auditFilter);
		
		auditlogs = webOp.driver().findElements(AuditLogs);
		int totalAuditLogs = auditlogs.size();
		
		int foundPrescriptions = 0;
		boolean areAllDetailsSame;
		for(int rowsCovered = 1; rowsCovered <= totalAuditLogs; rowsCovered++) {
			WebElement auditLog = webOp.driver().findElement(By.xpath(auditLogLocator+"["+rowsCovered+"]"));
			jse.executeScript("arguments[0].scrollIntoView();", auditLog);
			String patientNameShown = auditLog.findElement(auditLogPatientNameLocator).getText();
			String drugNameShown = auditLog.findElement(auditLogDrugNameLocator).getText();

			if(expectedPatientFullName.equalsIgnoreCase(patientNameShown) && uniqueDrugNames.contains(drugNameShown)) {
				/*---------------------open View Details modal---------------------*/ 
				By showDetailsLinkLocator = By.xpath(auditLogLocator+"["+rowsCovered+"]"+showDetailsLinkLocatorString);
				webOp.driver().findElement(showDetailsLinkLocator).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
				WebElement showDetailsModalContent = webOp.driver().findElement(showDetailsModalContentLocator);
				completeJson = showDetailsModalContent.getText();
				JSONObject fullAuditLog = HelperUtility.getJsonObject(completeJson);
				JSONObject medicationDetails = fullAuditLog.getJSONObject("prescriptionDetails");
				PrescriptionAuditDTO prescriptionAuditDTO = new PrescriptionAuditDTO(medicationDetails);
				for(Prescription prescription: ePrescribedPrescriptions) {
					areAllDetailsSame = prescriptionAuditDTO.isSameAsPrescriptionEntered(prescription);
					if(areAllDetailsSame){
						foundPrescriptions++;
						ePrescribedPrescriptions.remove(prescription);
						break;
					}
				}
				Thread.sleep(2000);
				WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
				new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
				/*---------------------close View Details modal---------------------*/

			}

			if(foundPrescriptions == totalEprescriptions) {
				break;
			}
		}
		if(foundPrescriptions < totalEprescriptions) {
			webOp.getSoftAssert().fail("found less audit logs for ePrescribed medications"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			webOp.logger().error("found less audit logs for ePrescribed medications expected = {}, actual = {}", new Object[]{totalEprescriptions, foundPrescriptions});
		}
	}
	
	public void verifyAuditTrailForPrintedPrescriptions(AuditLogFilter auditFilter) throws Exception {
		List<WebElement> auditlogs;
		String completeJson;
		
		List<Prescription> prescriptions = PatientService.getUndeletedPrescriptions();
		List<Prescription> printedPrescriptions = new ArrayList<Prescription>();
		Set<String> uniqueDrugNames = new HashSet<String>();
		for(Prescription prescription : prescriptions) {
			if(prescription.isEPrescribe()==null || Boolean.FALSE == prescription.isEPrescribe()) {
				uniqueDrugNames.add(prescription.getDrugName());
				printedPrescriptions.add(prescription);
			}
		}
		
		if(printedPrescriptions.isEmpty()) {
			return;
		}
		int totalPrintedprescriptions = printedPrescriptions.size();
		String expectedPatientFullName = PatientUtility.getPatientFullNameExcludingMiddleName((Patient) dataProvider.getPageData("patient"));

		searchLog(auditFilter);
		
		auditlogs = webOp.driver().findElements(AuditLogs);
		int totalAuditLogs = auditlogs.size();
		
		int foundPrescriptions = 0;
		boolean areAllDetailsSame;
		for(int rowsCovered = 1; rowsCovered <= totalAuditLogs; rowsCovered++) {
			WebElement auditLog = webOp.driver().findElement(By.xpath(auditLogLocator+"["+rowsCovered+"]"));
			jse.executeScript("arguments[0].scrollIntoView();", auditLog);
			String patientNameShown = auditLog.findElement(auditLogPatientNameLocator).getText();
			String drugNameShown = auditLog.findElement(auditLogDrugNameLocator).getText();

			if(expectedPatientFullName.equalsIgnoreCase(patientNameShown) && uniqueDrugNames.contains(drugNameShown)) {
				/*---------------------open View Details modal---------------------*/
				By showDetailsLinkLocator = By.xpath(auditLogLocator+"["+rowsCovered+"]"+showDetailsLinkLocatorString);
				webOp.driver().findElement(showDetailsLinkLocator).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
				completeJson = webOp.driver().findElement(showDetailsModalContentLocator).getText();
				JSONObject fullAuditLog = HelperUtility.getJsonObject(completeJson);
				JSONObject medicationDetails = fullAuditLog.getJSONObject("medicationDetails");
				PrescriptionAuditDTO prescriptionAuditDTO = new PrescriptionAuditDTO(medicationDetails);
				for(Prescription prescription: printedPrescriptions) {
					areAllDetailsSame = prescriptionAuditDTO.isSameAsPrescriptionEntered(prescription);
					if(areAllDetailsSame){
						foundPrescriptions++;
						printedPrescriptions.remove(prescription);
						break;
					}
				}
				Thread.sleep(2000);
				WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
				new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
				/*---------------------close View Details modal---------------------*/

			}

			if(foundPrescriptions == totalPrintedprescriptions) {
				break;
			}
		}
		if(foundPrescriptions < totalPrintedprescriptions) {
			webOp.getSoftAssert().fail("found less audit logs for printed medications"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			webOp.logger().error("found less audit logs for printed medications expected = {}, actual = {}", new Object[]{totalPrintedprescriptions, foundPrescriptions});
		}
	}
	
	public void verifyAuditTrailForRemovedPrescriptions(AuditLogFilter auditFilter) throws Exception {
		List<WebElement> auditlogs;
		String completeJson;
		
		AuditAction action = AuditAction.valueOf(auditFilter.getSearchLog().getSearchText());
		AuditRecordMatchingColumns matchingColumns = auditFilter.getMatchingColumns();
		String drugNameDeleted = matchingColumns.getDrugName();
		Prescription deletedPrescription = PatientService.getFirstDeletedPrescriptionWithDrugName(drugNameDeleted);
		
		String expectedPatientFullName = PatientUtility.getPatientFullNameExcludingMiddleName((Patient) dataProvider.getPageData("patient"));

		searchLog(auditFilter);
		
		auditlogs = webOp.driver().findElements(AuditLogs);
		int totalAuditLogs = auditlogs.size();
		
		boolean areAllDetailsSame = false;
		for(int rowsCovered = 1; rowsCovered <= totalAuditLogs; rowsCovered++) {
			WebElement auditLog = webOp.driver().findElement(By.xpath(auditLogLocator+"["+rowsCovered+"]"));
			jse.executeScript("arguments[0].scrollIntoView();", auditLog);
			String patientNameShown = auditLog.findElement(auditLogPatientNameLocator).getText();
			String drugNameShown = auditLog.findElement(auditLogDrugNameLocator).getText();

			if(expectedPatientFullName.equalsIgnoreCase(patientNameShown) && drugNameDeleted.contains(drugNameShown)) {
				/*---------------------open View Details modal---------------------*/
				By showDetailsLinkLocator = By.xpath(auditLogLocator+"["+rowsCovered+"]"+showDetailsLinkLocatorString);
				webOp.driver().findElement(showDetailsLinkLocator).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
				completeJson = webOp.driver().findElement(showDetailsModalContentLocator).getText();
				JSONObject medicationDetails = HelperUtility.getJsonObject(completeJson);
				PrescriptionAuditDTO prescriptionAuditDTO = new PrescriptionAuditDTO(medicationDetails);
				areAllDetailsSame = prescriptionAuditDTO.isSameAsPrescriptionEntered(deletedPrescription);
				
				Thread.sleep(2000);
				WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
				new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
				/*---------------------close View Details modal---------------------*/

			}
			
			if(areAllDetailsSame){
				break;
			}
		}
		if(!areAllDetailsSame) {
			webOp.getSoftAssert().fail("not found audit log ="+action.name()+" {} for medication ="+drugNameDeleted+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			webOp.logger().error("not found audit log = {} for medication = {}", new Object[]{action.name(), drugNameDeleted});
		}
	}
	
	public AuditLogPage verifyAuditTrailForSavedOrUpdatedPrescriptions() throws Exception {
		
		AuditLogFilter auditFilter = dataProvider.getPageData("auditLogFilter");
		String searchText = auditFilter.getSearchLog().getSearchText();
		if(StringUtils.equalsIgnoreCase(AuditAction.PRESCRIPTION_REMOVED.name(), searchText)) {
			verifyAuditTrailForRemovedPrescriptions(auditFilter);
		} else if(StringUtils.equalsIgnoreCase(AuditAction.MEDICATION_PRESCRIBED.name(), searchText)){
			//issue with MEDICATION_PRESCRIBED audit log not showing the doseUnitDisplayVal string,
			//that's why we need to do separate searches
			auditFilter.getSearchLog().setSearchText(AuditAction.PRESCRIPTION_TRANSMITTED_TO_SURESCRIPTS.name());
			verifyAuditTrailForSentEPrescriptions(auditFilter);
			auditFilter.getSearchLog().setSearchText(AuditAction.PRESCRIPTION_PRINT_JOB_CREATED.name());
			verifyAuditTrailForPrintedPrescriptions(auditFilter);
		}
		return this;
	}
	
	public AbstractPage viewActivityLogForPatient() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		navigateToAuditPage();
		int i=0;
		WebElement auditlog=null;
		//AuditLogFilter aLogFilter = dataProvider.getPageData("auditLogFilter");
		searchAuditTrailForFilter();
		Patient patient = dataProvider.getPageData("patient");
		String fName =  patient.getExistingPatient().getDemographic().getFirstName();
		String mName= null;
		if (patient.getExistingPatient().getDemographic().getMiddleName() != null)
			mName=patient.getExistingPatient().getDemographic().getMiddleName();
		String lName =  patient.getExistingPatient().getDemographic().getLastName();

		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(By.xpath("//div[1]/div[contains(@class,'ng-isolate-scope')]/div/div[contains(@class,'ui-grid-cell-contents ng-scope')]/a")).click();
		if(webOp.driver().findElement(By.xpath("//div[@class='modal-content'][div/button[contains(@ng-click,'auditModalCtrl.close')]]")).isDisplayed()) {
			String completeJson=webOp.driver().findElement(By.xpath("//div[@class='modal-content']/div[@class='text-left cs-overlay-content ng-scope']/div[@class='modal-body details-modal-content']/pre")).getText();
			//System.out.println(fName);

			Boolean fNameCheck=completeJson.contains(fName);
			Assert.assertTrue(fNameCheck, "First name is not displaying correctly:");
			Boolean lNameCheck=completeJson.contains(fName);
			Assert.assertTrue(lNameCheck, "Last name is not displaying correctly:");
			if(mName != null) {
				Boolean mNameCheck=completeJson.contains(fName);
				Assert.assertTrue(mNameCheck, "Middle name is not displaying correctly:");
			}
			webOp.driver().findElement(By.xpath("//div[@class='modal-content']//button")).click();
		}
		return new AbstractPage(webOp, dataProvider);
	}

	public AbstractPage viewActivityLogForPharmacy() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		navigateToAuditPage();
		int i=0;
		WebElement auditlog=null;
		AuditLogFilter aLogFilter = dataProvider.getPageData("auditLogFilter");
		searchAuditTrailForFilter();
		String pName;
		//String pStreet;
		if(dataProvider.getPageData("pharmacyNameRef") != null) {
			pName=dataProvider.getPageData("pharmacyNameRef").toString();
			//pStreet="";
		}
		else {
			//flag=0;
			pName=webOp.gethMap().get("PharmacyName");
			//pStreet=webOp.gethMap().get("PharmacyStreet");
		}
		//String pZip= new PharmacyPageData().getPharmacySearch().getNew().getZipCode();
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(By.xpath("//div[1]/div[contains(@class,'ng-isolate-scope')]/div/div[contains(@class,'ui-grid-cell-contents ng-scope')]/a")).click();
		if(webOp.driver().findElement(By.xpath("//div[@class='modal-content'][div/button[contains(@ng-click,'auditModalCtrl.close')]]")).isDisplayed()) {
			String completeJson=webOp.driver().findElement(By.xpath("//div[@class='modal-content']/div[@class='text-left cs-overlay-content ng-scope']/div[@class='modal-body details-modal-content']/pre")).getText();
			System.out.println(pName);
			Boolean pNameCheck=completeJson.contains(pName);
			Assert.assertTrue(pNameCheck, "The name is not displaying correctly:");
			/*if(flag==0) {
				System.out.println(pStreet);
				Boolean pStreetCheck=completeJson.contains(pStreet);
				Assert.assertTrue(pStreetCheck, "The street not is displaying correctly");
			}*/
			webOp.driver().findElement(By.xpath("//div[@class='modal-content']//button")).click();
		}
		return new AbstractPage(webOp, dataProvider);
	}

	public AbstractPage viewActivityLogForMedicines() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		navigateToAuditPage();
		int i=0;
		WebElement auditlog=null;
		AuditLogFilter aLogFilter = dataProvider.getPageData("auditLogFilter");
		searchAuditTrailForFilter();
		String pName;
		//String pStreet;
		if(dataProvider.getPageData("pharmacyNameRef") != null) {
			pName=dataProvider.getPageData("pharmacyNameRef").toString();
			//pStreet="";
		}
		else {
			//flag=0;
			pName=webOp.gethMap().get("PharmacyName");
			//pStreet=webOp.gethMap().get("PharmacyStreet");
		}
		//String pZip= new PharmacyPageData().getPharmacySearch().getNew().getZipCode();
		webOp.waitTillAllCallsDoneUsingJS();
		webOp.driver().findElement(By.xpath("//div[1]/div[contains(@class,'ng-isolate-scope')]/div/div[contains(@class,'ui-grid-cell-contents ng-scope')]/a")).click();
		if(webOp.driver().findElement(By.xpath("//div[@class='modal-content'][div/button[contains(@ng-click,'auditModalCtrl.close')]]")).isDisplayed()) {
			String completeJson=webOp.driver().findElement(By.xpath("//div[@class='modal-content']/div[@class='text-left cs-overlay-content ng-scope']/div[@class='modal-body details-modal-content']/pre")).getText();
			System.out.println(pName);
			Boolean pNameCheck=completeJson.contains(pName);
			Assert.assertTrue(pNameCheck, "The name is not displaying correctly:");
			/*if(flag==0) {
				System.out.println(pStreet);
				Boolean pStreetCheck=completeJson.contains(pStreet);
				Assert.assertTrue(pStreetCheck, "The street not is displaying correctly");
			}*/
			webOp.driver().findElement(By.xpath("//div[@class='modal-content']//button")).click();
		}
		return new AbstractPage(webOp, dataProvider);
	}

	public AuditLogPage navigateToAuditPage() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfElementLocated(accountMenuLocator));
		Thread.sleep(1000);
		WebElement accountMenuElement = webOp.driver().findElement(accountMenuLocator);
		accountMenuElement.click();
		moveToAuditTrail(wait);
		return this;
	}

	public void moveToAuditTrail(WebDriverWait wait) throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfElementLocated(AuditTrailLocator));
		Thread.sleep(1000);
		webOp.driver().findElement(AuditTrailLocator).click();
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.urlContains(AppPage.AuditTrails.getUrlContains(hMap)));

	}

	public AbstractPage searchAuditTrailForFilter() throws Exception {
		//navigateToAuditPage();
		AuditLogFilter auditlogs=dataProvider.getPageData("auditLogFilter");
		return searchLog(auditlogs);
	}

	public AuditLogPage searchAuditTrailForDrugInteractions() throws Exception {
		List<Prescription> prescriptions = PatientService.getUpdatedPrescriptions();

		//commented due to timeZone setting on clara-dev
		/*
		//Checking for time range as fifteen minutes from now for searching audit logs
		Date nowDate  =  new Date();
		Date fifteenMinutesBeforeTime = new Date((nowDate.getTime()- (15 *60*1000)));
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String startDate = formatter.format(nowDate);
		String endDate = formatter.format(fifteenMinutesBeforeTime);
		 */

		String expectedPatientFullName = PatientUtility.getPatientFullNameExcludingMiddleName((Patient) dataProvider.getPageData("patient"));
		List<WebElement> auditlogs;
		String completeJson;

		for (Prescription prescription : prescriptions) {
			List<DDInteracnAlert> interacnAlerts = prescription.getDdInteracnAlert();
			if (!interacnAlerts.isEmpty()) {
				for(DDInteracnAlert interacnAlert : interacnAlerts) {
					DrugInteractionAlertType alertType = DrugInteractionAlertType.valueOf(interacnAlert.getAlertType());
					AuditAction action = alertType.getAuditAction();
					String expectedOtherMedOrAllergy = null;
					if(alertType == DrugInteractionAlertType.DrugAllergy && interacnAlert.getAllergyName()!= null) {
						expectedOtherMedOrAllergy = interacnAlert.getAllergyName();
					} else if ((alertType == DrugInteractionAlertType.DrugDrug || alertType == DrugInteractionAlertType.DuplicateTherapy)) {
						if( interacnAlert.getHomeMedDrugName()!= null){
							expectedOtherMedOrAllergy = interacnAlert.getHomeMedDrugName();
						}
						else if( interacnAlert.getPrescriptionDrugName()!= null){
							expectedOtherMedOrAllergy = interacnAlert.getPrescriptionDrugName();
						}
					}

					AuditLogFilter auditFilter  = new AuditLogFilter();
					SearchOption searchLog = new SearchOption();
					searchLog.setSearchField("Action");
					searchLog.setSearchText(action.name());
					searchLog.setSearchFormat(Boolean.TRUE);
					auditFilter.setSearchLog(searchLog);
					auditFilter.setShowMyLog(Boolean.TRUE);
					//commented due to timeZone setting on clara-dev
					/*
					auditFilter.setStartDate(startDate);
					auditFilter.setEndDate(endDate);
					 */
					searchLog(auditFilter);
					boolean isFound = false;
					auditlogs = webOp.driver().findElements(AuditLogs);
					int totalAuditLogs = auditlogs.size();

					for(int rowsCovered = 1; rowsCovered <= totalAuditLogs; rowsCovered++) {
						WebElement auditLog = webOp.driver().findElement(By.xpath(auditLogLocator+"["+rowsCovered+"]"));
						jse.executeScript("arguments[0].scrollIntoView();", auditLog);
						String patientNameShown = auditLog.findElement(auditLogPatientNameLocator).getText();
						String drugNameShown = auditLog.findElement(auditLogDrugNameLocator).getText();
						if(expectedPatientFullName.equalsIgnoreCase(patientNameShown) && prescription.getDrugName().equalsIgnoreCase(drugNameShown)) {
							
							/*---------------------open View Details modal---------------------*/
							By showDetailsLinkLocator = By.xpath(auditLogLocator+"["+rowsCovered+"]"+showDetailsLinkLocatorString);
							WebElement showDetailsLink = auditLog.findElement(showDetailsLinkLocator);
							showDetailsLink.click();
							
							wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
							completeJson = webOp.driver().findElement(showDetailsModalContentLocator).getText();
							JSONArray jsonArray = HelperUtility.getJsonArray(completeJson);
							JSONObject interacnLogObject = jsonArray.getJSONObject(0);
							String status = interacnLogObject.getString("status");

							if(expectedOtherMedOrAllergy != null) {
								JSONObject mapDrugs = interacnLogObject.getJSONObject("mapDrugs");
								String actualAllergyOrMedName = mapDrugs.getString(mapDrugs.names().getString(0));
								if(!expectedOtherMedOrAllergy.equalsIgnoreCase(actualAllergyOrMedName)){
									continue;
								}
							}

							if (DDAlertResponse.Ignore == DDAlertResponse.getAlertResponse(interacnAlert.getResponse())) {
								if(!DDAlertResponse.Ignore.getStatusinAuditLogDetails().equals(status)) {
									continue;
								}
								isFound = true;

								JSONArray overrideReasonArr = interacnLogObject.getJSONArray("overrideReason");
								String[] overrideReasons = new String[overrideReasonArr.length()];
								for(int i = 0; i < overrideReasonArr.length(); i++) {
									overrideReasons[i] = overrideReasonArr.getString(i);
								}
								Set<String> overrideReasonsFromExcel = new HashSet<String>();
								for(String overrideReasonFromExcel : interacnAlert.getReason() ){
									overrideReasonsFromExcel.add(overrideReasonFromExcel);
								}

								for(String overrideReason : overrideReasons ){
									if(!overrideReasonsFromExcel.contains(overrideReason)) {
										webOp.getSoftAssert().fail(action.name() + "drug interaction overrideReason " +overrideReason+" not found"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName() + ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
										webOp.logger().error("{} drug interaction {} overrideReason not found", new Object[]{action.name(), overrideReason});
										break;
									}
								}
							} else {
								if(!DDAlertResponse.Accept.getStatusinAuditLogDetails().equals(status)) {
									continue;
								}
								isFound = true;
							}

							Thread.sleep(2000);
							WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
							new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
							wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
							/*---------------------close View Details modal---------------------*/
							if(isFound){
								break;
							}
						}
					}

					if(!isFound) {
						webOp.getSoftAssert().fail(action.name() + "drug interaction log not found"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
						webOp.logger().error("{} drug interaction log is not found", action.name());
					}

					//don't check even if there are remaining interactions
					if (DDAlertResponse.Accept == DDAlertResponse.getAlertResponse(interacnAlert.getResponse())) {
						break;
					}
				}
			}			
		}
		return this;
	}

	public AuditLogPage verifyAuditTrailForFacilityEdit() throws Exception {
		verifyAuditTrailForFacilityDetailsEdit();
		verifyAuditTrailForFacilitySettingsDetailsEdit();
		return this;
	}

	public AuditLogPage verifyAuditTrailForFacilityDetailsEdit() throws Exception {
		verifyAuditTrailForFacilityNominatorsEdit();
		return this;
	}

	public AuditLogPage verifyAuditTrailForFacilitySettingsDetailsEdit() throws Exception {
		verifyAuditTrailForFacilitySettingsTimeZoneAndDSTEdit();
		return this;
	}

	public AuditLogPage verifyAuditTrailForFacilityNominatorsEdit() throws Exception {
		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");
		if(editFacilitySection == null) {
			return this;
		}
		EditFacility editFacility = editFacilitySection.getEditFacility();
		if(editFacility == null) {
			return this;
		}
		if(editFacility.getNominator() == null && editFacility.getAltNominator() == null) {
			return this;
		}
		if(editFacility.getNominator() != null && Boolean.TRUE == FacilityService.getIsNominatorChanged()){
			verifyAuditTrailForFacilityNominatorEdit(AuditAction.CHANGE_NOMINATOR, editFacility.getNominator(), EPCSNominatorType.PRIMARY);
		}
		if(editFacility.getAltNominator() != null && Boolean.TRUE == FacilityService.getIsAltNominatorChanged()){
			verifyAuditTrailForFacilityNominatorEdit(AuditAction.CHANGE_ALTERNATE_NOMINATOR, editFacility.getAltNominator(), EPCSNominatorType.ALTERNATE);
		}
		return this;
	}

	private void verifyAuditTrailForFacilityNominatorEdit(AuditAction action, Nominator currentNominator, EPCSNominatorType epcsNominatorType) throws Exception {
		AuditLogFilter auditFilter  = new AuditLogFilter();
		SearchOption searchLog = new SearchOption();
		searchLog.setSearchField("Action");
		searchLog.setSearchText(action.name());
		searchLog.setSearchFormat(Boolean.TRUE);
		auditFilter.setSearchLog(searchLog);
		auditFilter.setShowMyLog(Boolean.TRUE);

		searchLog(auditFilter);

		By showDetailsLinkLocator = By.xpath(auditLogLocator+"[1]"+showDetailsLinkLocatorString);
		WebElement showDetailsLink = webOp.driver().findElement(showDetailsLinkLocator);
		/*---------------------open View Details modal---------------------*/ 
		showDetailsLink.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
		String completeJson = webOp.driver().findElement(showDetailsModalContentLocator).getText();		
		JSONObject auditLogDetailsObject = HelperUtility.getJsonObject(completeJson);
		JSONObject nominatorDeatilsLogged = auditLogDetailsObject.getJSONObject("nominatorDetails");
		if(currentNominator.getFirstName() != null) {
			String firstNameSaved = nominatorDeatilsLogged.getString("firstName");
			if(!currentNominator.getFirstName().equalsIgnoreCase(firstNameSaved)) {
				webOp.logger().error(action.name()+" nominatorDetails firstName not correct expected = {}, actual = {}", new Object[]{currentNominator.getFirstName(), firstNameSaved});
				webOp.getSoftAssert().fail(action.name()+" nominatorDetails firstName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			}
		} else if(currentNominator.getLastName() != null) {
			String lastNameSaved = nominatorDeatilsLogged.getString("lastName");
			if(!currentNominator.getLastName().equalsIgnoreCase(lastNameSaved)) {
				webOp.logger().error(action.name()+" nominatorDetails lastName not correct expected = {}, actual = {}", new Object[]{currentNominator.getLastName(), lastNameSaved});
				webOp.getSoftAssert().fail(action.name()+" nominatorDetails lastName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			}
		} else if(currentNominator.getEmailAddress() != null) {
			String emailSaved = nominatorDeatilsLogged.getString("email");
			if(!currentNominator.getEmailAddress().equalsIgnoreCase(emailSaved)) {
				webOp.logger().error(action.name()+" nominatorDetails email not correct expected = {}, actual = {}", new Object[]{currentNominator.getEmailAddress(), emailSaved});
				webOp.getSoftAssert().fail(action.name()+" nominatorDetails email is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
			}
		}
		Thread.sleep(2000);
		WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
		new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
		/*---------------------close View Details modal---------------------*/
	}

	public AuditLogPage verifyAuditTrailForFacilitySettingsTimeZoneAndDSTEdit() throws Exception {

		EditFacilitySection editFacilitySection = dataProvider.getPageData("editFacilitySection");

		if(editFacilitySection == null) {
			return this;
		}

		EditFacilitySettings facilitySettings = editFacilitySection.getEditFacilitySettings();
		if(facilitySettings == null) {
			return this;
		}
		if(facilitySettings.getTimeZone() == null || facilitySettings.isApplyDST() == null) {
			return this;
		}

		validateTimeStamp(facilitySettings.getTimeZone(), facilitySettings.isApplyDST());
		return this;
	}

	public void validateTimeStamp(String timeZone,Boolean applyDST) throws Exception {

		AuditAction action = AuditAction.USER_SELECTED_FACILITY;
		AuditLogFilter auditFilter  = new AuditLogFilter();
		SearchOption searchLog = new SearchOption();
		searchLog.setSearchField("Action");
		searchLog.setSearchText(action.name());
		searchLog.setSearchFormat(Boolean.TRUE);
		auditFilter.setSearchLog(searchLog);
		auditFilter.setShowMyLog(Boolean.TRUE);

		searchLog(auditFilter);

		WebElement auditLog = webOp.driver().findElement(By.xpath(auditLogLocator+"[1]"));
		String timeStamp = auditLog.findElement(auditLogTimeStampLocator).getText();		
		SupportedTimeZoneEnum tzEnum = SupportedTimeZoneEnum.valueOf(timeZone);

		TimeZone selectedTimeZone = TimeZone.getTimeZone(tzEnum.getValue());
		SimpleDateFormat dateTimeParser = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		int offsetInHours = tzEnum.getOffsetInHours();
		Date NearByDateTimeWhenFacilitySelected = FacilityService.getDateTimeAtSelectFacility();
		if(selectedTimeZone.inDaylightTime(NearByDateTimeWhenFacilitySelected)) {
			//set offset of selectedTimeZone with respect to GMT
			if(applyDST.booleanValue() == true) {
				offsetInHours += ApplicationConstatnts.DSTOffsetInUSInHrs;
			}
			selectedTimeZone = TimeZone.getTimeZone("GMT");
			selectedTimeZone.setRawOffset(offsetInHours*60*60*1000);
		}
		dateTimeParser.setTimeZone(selectedTimeZone);
		Date givenDateInSelectedTimeZoneAndWithDst = dateTimeParser.parse(timeStamp);

		if(NearByDateTimeWhenFacilitySelected.getTime() - givenDateInSelectedTimeZoneAndWithDst.getTime() < 0) {
			webOp.logger().error(action.name()+" timestamp not correct as per timeZone = {} and dst = {}", new Object[]{tzEnum.getLabel(), applyDST});
			webOp.getSoftAssert().fail(action.name()+" timestamp is not correct as per timeZone and dst settings"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
	}
	

	public AuditLogPage viewActivityLogAfterSetUpFacility() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		int i=0;
		WebElement auditlog=null;
		ClaraUserDetails claraUserDetails=dataProvider.getPageData("claraUserDetails"); 
		moveToAuditTrail(wait);
		AuditLogFilter auditlogs=dataProvider.getPageData("auditLogFilter");
		searchLog(auditlogs);
		webOp.driver().findElement(By.xpath("//div[1]/div[contains(@class,'ng-isolate-scope')]/div/div[contains(@class,'ui-grid-cell-contents ng-scope')]/a")).click();
		if(webOp.driver().findElement(By.xpath("//div[@class='modal-content'][div/button[contains(@ng-click,'auditModalCtrl.close')]]")).isDisplayed()) {
			String completeJson=webOp.driver().findElement(By.xpath("//div[@class='modal-content']/div[@class='text-left cs-overlay-content ng-scope']/div[@class='modal-body details-modal-content']/pre")).getText();
			//System.out.println(fName);
			String firstName=claraUserDetails.getFirstname();
			String stateLicenseNumber=claraUserDetails.getStateLicenseNumber();
			Boolean firstNameCheck=completeJson.contains(firstName);
			Assert.assertTrue(firstNameCheck, "facilityOwner name is not displaying correctly:");
			Boolean stateLicenseNumberCheck=completeJson.contains(stateLicenseNumber); 	 	
			Assert.assertTrue(stateLicenseNumberCheck, "StateLicenseNumber is not displaying correctly:");
			//havent verified status of license verification
			//completeJson.split(":");
			webOp.driver().findElement(By.xpath("//div[@class='modal-content']//button")).click();
		}
		return this;

	}
	
	public AuditLogPage viewActivityLogAfterAutoLogout() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		AuditLogFilter auditFilter  = new AuditLogFilter();
		SearchOption searchLog = new SearchOption();
		searchLog.setSearchField("Action");
		searchLog.setSearchText("USER_LOGOUT");
		searchLog.setSearchFormat(Boolean.TRUE);
		auditFilter.setSearchLog(searchLog);
		auditFilter.setShowMyLog(Boolean.TRUE);
		
		searchLog(auditFilter);
		
		By showDetailsLinkLocator = By.xpath(auditLogLocator+"[1]"+showDetailsLinkLocatorString);
		WebElement showDetailsLink = webOp.driver().findElement(showDetailsLinkLocator);
		/*---------------------open View Details modal---------------------*/ 
		showDetailsLink.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
		String completeJson = webOp.driver().findElement(showDetailsModalContentLocator).getText();		
		JSONObject auditLogDetailsObject = HelperUtility.getJsonObject(completeJson);
		String reason = auditLogDetailsObject.getString("reason");
		String expectedReason = "Session timed out"; 
		if(!expectedReason.equalsIgnoreCase(reason)) {
			webOp.logger().error("USER_LOGOUT" + " reason not correct");
			webOp.getSoftAssert().fail("USER_LOGOUT"+" reason is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		Thread.sleep(2000);
		WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
		new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
		/*---------------------close View Details modal---------------------*/
		
		return this;
	}
	
	
	public AuditLogPage verifyAuditLogForTimeZoneAndDSTSetDuringCreateFacility() throws Exception
	{
		Facility facility = dataProvider.getPageData("facility");
		if(facility!=null)
		{
			validateTimeStamp(facility.getTimeZone(), facility.isApplyDST());
		}
		return this;
	}
	
	public AuditLogPage verifyAuditTrailForNonInstitutionalNomination(AuditAction action) throws Exception {
		Facility facility = dataProvider.getPageData("facility");
		ClaraUserDetails claraUser = dataProvider.getPageData("claraUserDetails");
		
		Boolean doesPrimaryNominatorNominates = Boolean.valueOf(webOp.getParameter("doesPrimaryNominatorNominates"));
		Boolean doesAlternateNominatorNominates = Boolean.valueOf(webOp.getParameter("doesAlternateNominatorNominates"));
		Nominator nominator = null;
		EPCSNominatorType epcsNominatorType = null;
		if(Boolean.TRUE == doesPrimaryNominatorNominates && !StringUtils.equalsIgnoreCase(webOp.getParameter("isFacilityOwnerNominator"),"TRUE")) {
			nominator = facility.getNominator();
			epcsNominatorType =EPCSNominatorType.PRIMARY;
		} 
		if(Boolean.TRUE == doesAlternateNominatorNominates) {
			nominator = facility.getAltNominator();
			epcsNominatorType =EPCSNominatorType.ALTERNATE;
		}
		
		AuditLogFilter auditlogs=dataProvider.getPageData("auditLogFilter");
		searchLog(auditlogs);

		By showDetailsLinkLocator = By.xpath(auditLogLocator+"[1]"+showDetailsLinkLocatorString);
		WebElement showDetailsLink = webOp.driver().findElement(showDetailsLinkLocator);
		/*---------------------open View Details modal---------------------*/ 
		showDetailsLink.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
		String completeJson = webOp.driver().findElement(showDetailsModalContentLocator).getText();		
		JSONObject auditLogDetailsObject = HelperUtility.getJsonObject(completeJson);
		
		JSONObject nominatorDeatilsLogged = auditLogDetailsObject.getJSONObject("nominatorDetails");
		String nominatorEmailShown = nominatorDeatilsLogged.getString("email");
		if(!nominatorEmailShown.equalsIgnoreCase(nominator.getEmailAddress())) {
			webOp.logger().error(action.name()+" nominatorDetails email not correct expected = {}, actual = {}", new Object[]{nominator.getEmailAddress(), nominatorEmailShown});
			webOp.getSoftAssert().fail(action.name()+" nominatorDetails email is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		String nominatorTypeShown = nominatorDeatilsLogged.getString("nominatorType");
		if(!epcsNominatorType.name().equalsIgnoreCase(nominatorTypeShown)) {
			webOp.logger().error(action.name()+" nominatorDetails nominatorType not correct expected = {}, actual = {}", new Object[]{epcsNominatorType.name(), nominatorTypeShown});
			webOp.getSoftAssert().fail(action.name()+" nominatorDetails nominatorType is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}

		JSONObject providerDetailsLogged = auditLogDetailsObject.getJSONObject("providerDetails");
		String providerFirstNameShown = providerDetailsLogged.getString("firstName");
		if(!providerFirstNameShown.equalsIgnoreCase(claraUser.getFirstname())) {
			webOp.logger().error(action.name()+" providerDetails firstName not correct expected = {}, actual = {}", new Object[]{claraUser.getFirstname(), providerFirstNameShown});
			webOp.getSoftAssert().fail(action.name()+" providerDetails firstName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		String providerLastNameShown = providerDetailsLogged.getString("lastName");
		if(!providerLastNameShown.equalsIgnoreCase(claraUser.getLastName())) {
			webOp.logger().error(action.name()+" providerDetails lastName not correct expected = {}, actual = {}", new Object[]{claraUser.getLastName(), providerLastNameShown});
			webOp.getSoftAssert().fail(action.name()+" nominatorDetails lastName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		
		JSONObject facilityDetailsLogged = auditLogDetailsObject.getJSONObject("facilityDetails");
		String facilityNameShown = facilityDetailsLogged.getString("facilityName");
		if(!facilityNameShown.equalsIgnoreCase(facility.getFacilityName())) {
			webOp.logger().error(action.name()+" facilityDetails facilityName not correct expected = {}, actual = {}", new Object[]{facility.getFacilityName(), facilityNameShown});
			webOp.getSoftAssert().fail(action.name()+" facilityDetails facilityName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		
		Thread.sleep(2000);
		WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
		new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
		/*---------------------close View Details modal---------------------*/
		return this;
	}

	
	public AuditLogPage verifyAuditTrailForSelfApprovalForEPCS(AuditAction action) throws Exception {
		Facility facility = dataProvider.getPageData("facility");
		ClaraUserDetails claraUser = dataProvider.getPageData("claraUserDetails");

		AuditLogFilter auditlogs=dataProvider.getPageData("auditLogFilter");
		searchLog(auditlogs);
		
		By showDetailsLinkLocator = By.xpath(auditLogLocator+"[1]"+showDetailsLinkLocatorString);
		WebElement showDetailsLink = webOp.driver().findElement(showDetailsLinkLocator);
		/*---------------------open View Details modal---------------------*/ 
		showDetailsLink.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(showDetailsModalLocator));
		String completeJson = webOp.driver().findElement(showDetailsModalContentLocator).getText();		
		JSONObject auditLogDetailsObject = HelperUtility.getJsonObject(completeJson);
		
		JSONObject providerDetailsLogged = auditLogDetailsObject.getJSONObject("providerDetails");
		String providerFirstNameShown = providerDetailsLogged.getString("firstName");
		if(!providerFirstNameShown.equalsIgnoreCase(claraUser.getFirstname())) {
			webOp.logger().error(action.name()+" providerDetails firstName not correct expected = {}, actual = {}", new Object[]{claraUser.getFirstname(), providerFirstNameShown});
			webOp.getSoftAssert().fail(action.name()+" providerDetails firstName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		String providerLastNameShown = providerDetailsLogged.getString("lastName");
		if(!providerLastNameShown.equalsIgnoreCase(claraUser.getLastName())) {
			webOp.logger().error(action.name()+" providerDetails lastName not correct expected = {}, actual = {}", new Object[]{claraUser.getLastName(), providerLastNameShown});
			webOp.getSoftAssert().fail(action.name()+" nominatorDetails lastName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		
		JSONObject facilityDetailsLogged = auditLogDetailsObject.getJSONObject("facilityDetails");
		String facilityNameShown = facilityDetailsLogged.getString("facilityName");
		if(!facilityNameShown.equalsIgnoreCase(facility.getFacilityName())) {
			webOp.logger().error(action.name()+" facilityDetails facilityName not correct expected = {}, actual = {}", new Object[]{facility.getFacilityName(), facilityNameShown});
			webOp.getSoftAssert().fail(action.name()+" facilityDetails facilityName is not correct"+", class="+this.getClass().getName()+", method="+Thread.currentThread().getStackTrace()[1].getMethodName()+ ", lineNumber="+Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		
		Thread.sleep(2000);
		WebElement closeModalElement = webOp.driver().findElement(showDetailsModalCloseButtonLocator);
		new Actions(webOp.driver()).moveToElement(closeModalElement).click().perform();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(showDetailsModalLocator));
		/*---------------------close View Details modal---------------------*/
		return this;
	}
	
	public AbstractPage searchLog(AuditLogFilter auditlogs) throws Exception {
		String searchText = auditlogs.getSearchLog().getSearchText();
		Boolean searchFormat = auditlogs.getSearchLog().isSearchFormat();
		String searchField=auditlogs.getSearchLog().getSearchField();
		if (searchFormat != null && searchFormat.equals(true)) {
			Select auditFilter=new Select(webOp.driver().findElement(searchInputType));
			auditFilter.selectByVisibleText(searchField);
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
			webOp.driver().findElement(searchInputText).clear();
			webOp.driver().findElement(searchInputText).sendKeys(searchText);
			webOp.driver().findElement(searchInputText).sendKeys(Keys.ENTER);
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		} 
		if(auditlogs.isShowMyLog()!=null) {
			WebElement showMyElement = webOp.driver().findElement(showMyLog);
			boolean isChecked = showMyElement.isSelected();
			if(isChecked != auditlogs.isShowMyLog().booleanValue()) {
				webOp.driver().findElement(showMyLog).click();
				webOp.waitTillAllCallsDoneUsingJS();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
			}
		}

		searchLogWithinDateRange(auditlogs);
		return this;
	}

	public AbstractPage searchLogWithinDateRange(AuditLogFilter auditlogs) throws Exception {
		if(auditlogs.getStartDate()!=null && auditlogs.getEndDate()!=null) {
			WebElement startDateElement = webOp.driver().findElement(startDate);
			startDateElement.clear();
			startDateElement.sendKeys(auditlogs.getStartDate());
			webOp.waitTillAllCallsDoneUsingJS();
			WebElement endDateElement = webOp.driver().findElement(endDate);
			endDateElement.clear();
			endDateElement.sendKeys(auditlogs.getEndDate());
			webOp.driver().findElement(endDate).sendKeys(Keys.ENTER);
			webOp.waitTillAllCallsDoneUsingJS();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		}
		return this;
	}

}
