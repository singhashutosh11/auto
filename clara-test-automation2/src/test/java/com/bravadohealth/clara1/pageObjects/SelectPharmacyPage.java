package com.bravadohealth.clara1.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bravadohealth.clara1.pageObjects.locators.LoaderLocators;
import com.bravadohealth.clara1.utility.HelperUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;
import com.bravadohealth.pagedataset.PharmacyPageData;
import com.bravadohealth.pagedataset.PharmacySearchDetails;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class SelectPharmacyPage extends AbstractPage implements LoaderLocators{

	By cancelAddPatientAlertTitleLocator = By.xpath("//div[contains(@class,'modal-header')]/h1[text()='Are you sure?']");
	By selectPharmacyModalLocator = By.cssSelector("div.search-pharmacy-content");
	/* move to next page related variables */
	By cancelSavePharmacyLocator = By.xpath("//button[contains(text(),'Cancel')]");
	By findPharmacy=By.xpath("//a[contains(text(),'Find a pharmacy')]");
	By editPharmacyLink=By.xpath("//a[contains(@class,'inline-edit-link')]");
	By zipCodeInput=By.xpath("//input[@placeholder='zip code']");
	By pharmcayName=By.xpath("//input[@placeholder='pharmacy name']");
	By savePharmacy=By.xpath("//button[contains(text(),'Save pharmacy')]");
	By PharmacyResult=By.xpath("//li[contains(@class,'pharmacy-result')][1]");

	By PharmacyNameOnly=By.xpath("//span[contains(@class,'pharmacy-name')]");
	By PharmacyStreetOnly=By.xpath("//span[contains(@class,'pharmacy-street')]");
	By PharmacyLocationOnly=By.xpath("//span[contains(@class,'pharmacy-location')]");
	By PharmacyPhoneOnly=By.xpath("//span[contains(@class,'pharmacy-phone')]");
	By PharmacyFaxOnly=By.xpath("//span[contains(@class,'pharmacy-fax')]");

	By confirmPharmacyName=By.xpath("//div[@class='pharmacy-confirm-wrap']//span[contains(@class,'pharmacy-name')]");
	By confirmPharmacyStreet=By.xpath("//div[@class='pharmacy-confirm-wrap']//span[contains(@class,'pharmacy-street')]");
	By confirmPharmacyLocation=By.xpath("//div[@class='pharmacy-confirm-wrap']//span[contains(@class,'pharmacy-location')]");
	By confirmPharmacyPhone=By.xpath("//div[@class='pharmacy-confirm-wrap']//span[contains(@class,'detail pharmacy-phone')]");
	By confirmPharmacyFax=By.xpath("//div[@class='pharmacy-confirm-wrap']//span[contains(@class,'detail pharmacy-fax')]");

	public SelectPharmacyPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}

	public SelectPharmacyPage navigateToSelectPharmacyPage() throws Exception {
		//wait.until(ExpectedConditions.urlContains(AppPage.SelectPharmacy.getUrlContains(hMap)));
		return this;
	}

	public SelectPharmacyPage selectPharmacy() throws Exception {
		Patient patient = dataProvider.getPageData("patient");
		PatientDetails pDetails;
		if(patient.getNewPatient() !=null)
			pDetails=patient.getNewPatient();
		else
			pDetails = patient.getExistingPatient();
		PharmacyPageData pharmacyData=pDetails.getPharmacyPageData();
		if(pharmacyData!=null) {
			PharmacySearchDetails pharmacySearchDetails = null;
			if(pDetails.getPharmacyPageData().getPharmacySearch().getNew()!=null ) {
				pharmacySearchDetails=pDetails.getPharmacyPageData().getPharmacySearch().getNew();
				Thread.sleep(1500);
				if(patient.getNewPatient() !=null)
					webOp.driver().findElement(findPharmacy).click();
				else webOp.driver().findElement(editPharmacyLink).click();
			}
			else if(pDetails.getPharmacyPageData().getPharmacySearch().getExisting()!=null) {
				pharmacySearchDetails=pDetails.getPharmacyPageData().getPharmacySearch().getExisting();
				webOp.driver().findElement(editPharmacyLink).click();
			}
			searchPharmacy(pharmacySearchDetails);
		}
		return this;
	}

	public SelectPharmacyPage searchPharmacy(PharmacySearchDetails pharmacySearchDetails) throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		wait.until(ExpectedConditions.visibilityOfElementLocated(selectPharmacyModalLocator));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreenLocator));
		if(pharmacySearchDetails.getNearBy()!=null) {
			webOp.driver().findElement(By.xpath("//button[text()='"+pharmacySearchDetails.getNearBy()+"']")).click();
		}
		if(pharmacySearchDetails.getZipCode()!=null) {
			webOp.driver().findElement(zipCodeInput).sendKeys(pharmacySearchDetails.getZipCode());
		}
		if(pharmacySearchDetails.getPharmacyName()!=null) {
			webOp.driver().findElement(pharmcayName).sendKeys(pharmacySearchDetails.getPharmacyName());
		}
		if(pharmacySearchDetails.getSearchPerimeter()!=null) {
			webOp.waitTillAllCallsDoneUsingJS();
			String sPeri=pharmacySearchDetails.getSearchPerimeter().toString();
			/*String left;
			if(sPeri.equals("10 mi"))
				left="left: 22%;";
			else if (sPeri.equals("25 mi"))
				left="left: 55%;";
			else left="left: 100%;";
			System.out.println(left);
			*///("//div[contains(@class,'slider-tick-label')][contains(text(),'10 mi')]")
			Thread.sleep(1500);
			webOp.driver().findElement(By.xpath("//div[contains(@class,'slider-tick-label')][contains(text(),'"+sPeri+"')]")).click();
			//WebElement element=webOp.driver().findElement(By.xpath("//div[contains(@class,'slider-tick round')][contains(@style,'"+left+"')]"));
			//jse.executeScript("arguments[0].click()", element);
		}
		if(pharmacySearchDetails.getSearchFilter()!=null) {
			for(int i=0;i<pharmacySearchDetails.getSearchFilter().size();i++) {
				String searchFilter=pharmacySearchDetails.getSearchFilter().get(i);
				if(searchFilter.equals("EPCS")) {
					webOp.gethMap().put("EPCSEnabled", "true");
				}
				webOp.waitTillAllCallsDoneUsingJS();
				synchronized(webOp.driver()) {
					try {
						webOp.driver().wait(30);
					}
					catch(InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				Thread.sleep(2000);
				webOp.driver().findElement(By.xpath("//button[text()='"+searchFilter+"']")).click();
			}
			if(!webOp.gethMap().containsKey("EPCSEnabled"))			// && webOp.gethMap().get("EPCSEnabled").isEmpty()) 
			{
				webOp.gethMap().put("EPCSEnabled", "false");
			}
		}
		//list of pharmacy displays
		if(webOp.driver().findElements(PharmacyResult).size()!=0) {
			Thread.sleep(3000);
			WebElement PharmacyEntry;
			PharmacyEntry=webOp.driver().findElement(By.xpath("//li[contains(@class,'pharmacy-result')][1]"));
			PharmacyEntry.click();
			webOp.gethMap().put("PharmacyName",PharmacyEntry.findElement(PharmacyNameOnly).getText());
			//System.out.println(webOp.gethMap().get("PharmacyStreet"));	
			webOp.gethMap().put("PharmacyStreet",PharmacyEntry.findElement(PharmacyStreetOnly).getText());
			webOp.gethMap().put("PharmacyLocation",PharmacyEntry.findElement(PharmacyLocationOnly).getText());
			webOp.gethMap().put("PharmacyPhone",PharmacyEntry.findElement(PharmacyPhoneOnly).getText());
			webOp.gethMap().put("PharmacyFax",PharmacyEntry.findElement(PharmacyFaxOnly).getText());
		}
		else {
			webOp.logger().info("No results found");
			webOp.driver().findElement(By.xpath("//div[@class='modal-dialog ']//button[text()='Cancel']")).click();
		}
		return this;
	}

	public AbstractPage submitPharmacySearch() throws Exception {
		webOp.waitTillAllCallsDoneUsingJS();
		Patient patient = dataProvider.getPageData("patient");
		if(patient.getNewPatient()!=null)
		{
			if(patient.getNewPatient().getPharmacyPageData()!=null) {
				Boolean cancelChanges = patient.getNewPatient().getPharmacyPageData().isCancelChanges();
				if (cancelChanges != null && (cancelChanges.booleanValue() == true)) {
					webOp.driver().findElement(cancelSavePharmacyLocator).click();
				}
				else {
					webOp.driver().findElement(savePharmacy).click();
					//viewPharmacyDetails();
				}
			}
		}
		else
			webOp.driver().findElement(savePharmacy).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(selectPharmacyModalLocator));
		
		//Flogress Bar Check
		String pName=webOp.gethMap().get("PharmacyName");
		String pStreet=webOp.gethMap().get("PharmacyStreet");
		Thread.sleep(2000);
		String fStreet= webOp.driver().findElement(By.xpath("//span[contains(@class,'pharmacy-street ng-binding')]")).getText();
		String fName= webOp.driver().findElement(By.xpath("//button[contains(@class,'button-link button-tiny pharmacy-name-restrict field-info ng-binding')]")).getText();
		Assert.assertTrue(fName.contains(pName), "The name is not displaying correctly:");
		pStreet=pStreet.replaceAll("\\s+", "").replaceAll(",", "");
		fStreet=fStreet.replaceAll("\\s+", "").replaceAll(",", "");
		webOp.logger().info(pStreet);
		webOp.logger().info(fStreet);
		Boolean streetCheck= fStreet.contains(pStreet);
		Assert.assertTrue(streetCheck, "The street is not displaying correctly:");
		
		PharmacySearchDetails pharmacySearchDetails = null;
		PatientDetails pDetails;
		if (patient.getNewPatient()!=null)
			pDetails = patient.getNewPatient();
		else
			pDetails = patient.getExistingPatient();
		if(pDetails.getPharmacyPageData().getPharmacySearch().getExisting()!=null) {
			pharmacySearchDetails=patient.getNewPatient().getPharmacyPageData().getPharmacySearch().getExisting();
			webOp.driver().findElement(editPharmacyLink).click();
			searchPharmacy(pharmacySearchDetails);
			webOp.driver().findElement(savePharmacy).click();
		}
		
		if(pDetails.getMedicalHistory()!=null){
			return new PatientMedicalHistory(webOp, dataProvider);
		} else {
			return new WritePrescriptionsPage(webOp, dataProvider);
		}
	}

	private void viewPharmacyDetails() {
		Patient patient = dataProvider.getPageData("patient");
		if(patient.getNewPatient().getPharmacyPageData().getPharmacySearch()!=null) {
			Assert.assertEquals(webOp.driver().findElement(confirmPharmacyName).getText(), webOp.gethMap().get("PharmacyName"),"The pharmacyName is displayed Properly");
			Assert.assertEquals(webOp.driver().findElement(confirmPharmacyStreet).getText(), webOp.gethMap().get("PharmacyStreet"),"The pharmacyStreet address is displayed correctly");
			Assert.assertEquals(webOp.driver().findElement(confirmPharmacyLocation).getText(), webOp.gethMap().get("PharmacyLocation"),"The pharmacyLocation is displayed correctly");
			Assert.assertEquals(webOp.driver().findElement(confirmPharmacyPhone).getText(), webOp.gethMap().get("PharmacyPhone"),"The pharmacy phoneNumber is displayed correctly");
			Assert.assertEquals(webOp.driver().findElement(confirmPharmacyFax).getText(), webOp.gethMap().get("PharmacyFax"),"The pharmacy fax number is displayed correctly");
		}
	}

}
