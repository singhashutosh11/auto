package com.bravadohealth.clara1.pageObjects;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.bravadohealth.clara1.utility.PatientUtility;
import com.bravadohealth.pageObjects.AbstractPage;
import com.bravadohealth.pagedataset.ClaraUserDetails;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.Prescription;
import com.bravadohealth.pagedataset.PrescriptionsData;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

import com.bravadohealth.pagedataset.PatientDetails;

public class PrintPage extends AbstractPage {
	By viewPrintedMedicationLocator=By.xpath("");

	public PrintPage(WebOperation webOp, PageDataProvider dataProvider) {
		super(webOp, dataProvider);
		// TODO Auto-generated constructor stub
	}
	
	public PrintPage focusNewWindow() {
		String currentWindow=webOp.driver().getWindowHandle();
		for (String winHandle:webOp.driver().getWindowHandles()) {
			if(!winHandle.equals(currentWindow)) {
				webOp.driver().switchTo().window(winHandle);
				webOp.driver().manage().window().maximize();
				String newTitle = webOp.driver().getTitle();
				System.out.println(newTitle);
				webOp.driver().findElements(By.cssSelector("div.provider-name"));
				break;
			}
		}
		return this;
	}

	public PrintPage verifyProviderDetails() {
		focusNewWindow();
		ClaraUserDetails claraUserDetails=dataProvider.getPageData("claraUserDetails");
		String providerName =completTheName(claraUserDetails.getFirstname(),claraUserDetails.getMiddleName(),claraUserDetails.getLastName());
		String designation=claraUserDetails.getDesignation();
		webOp.driver().findElements(By.cssSelector("div.provider-name"));
		for(WebElement providerNameElement:webOp.driver().findElements(By.cssSelector("div.provider-name"))) {
			String[] NameNDesignation=providerNameElement.getText().split(",");
			String displayedProviderName=NameNDesignation[0].trim();
			String displayedDesignation=NameNDesignation[1].trim();
			Assert.assertEquals(providerName, displayedProviderName,"The providername is displayed correctly");
			Assert.assertEquals(designation,displayedDesignation,"The provider designation is displayed correctly.");
		}
		return this;
	}
	private String completTheName(String firstname, String middleName, String lastName) {
		return firstname +" " +(!StringUtils.isEmpty(middleName) ? middleName + " " : "") + lastName;		
	}
	public PrintPage verifyPatientDetails() {
		Patient patient=dataProvider.getPageData("patient");
		PatientDetails patientDetails=null;
		if(patient.getNewPatient()!=null) {
			patientDetails=patient.getNewPatient();
		}
		else
			patientDetails=patient.getExistingPatient();
		Demographic demographic=patientDetails.getDemographic();
		String PatientName=completTheName(demographic.getFirstName(),demographic.getMiddleName(),demographic.getLastName());
		String displayedPatientName=webOp.driver().findElement(By.xpath("//displayedPateintName")).getText();
		Assert.assertEquals(displayedPatientName, PatientName,"The patient name is displayed correctly");
		String patientAddress=demographic.getAddress();
		//String statecode=AddressUtility.StateAbbreviations.get(demographic.getState());
		Integer zipCode=demographic.getZipCode();
		//String completeAddress=patientAddress+","+statecode+","+Integer.toString(zipCode);
		String displayedAddress=webOp.driver().findElement(By.xpath("//patiemtAddress")).getText();
		//Assert.assertEquals(completeAddress, displayedAddress,"The address is  displayed correctly");

		return this;
	}

	public PrintPage verifyPrintedMedication() {
		PrescriptionsData prescriptionsData = new WritePrescriptionsPage(webOp, dataProvider).getPrescriptionsData();
		if(prescriptionsData != null) {
			List<Prescription> prescriptions = prescriptionsData.getPrescription();
			if (prescriptions != null && prescriptions.size() > 0) {
				int i = 1;
				List<WebElement> prescriptionElements = webOp.driver().findElements(viewPrintedMedicationLocator);
				Iterator<WebElement> iterator = prescriptionElements.iterator();
				for (Prescription prescription : prescriptions) {
					if(prescription.isEPrescribe()!=null && prescription.isEPrescribe()==false){
						if(!PatientUtility.isPrescriptionDeleted(prescription)) {
							WebElement prescriptionElement = iterator.next();
							WebElement drugNameElement = prescriptionElement.findElement(By.cssSelector("div.row div:nth-child(1) span.rx-drug-name"));
							Assert.assertEquals(drugNameElement.getText(), prescription.getDrugName());
							int refill=0;
							String expectedRefillCount =null;
							if(prescription.getRefills()!=null) {
								if(prescription.getControlledSubstance()!=null &&prescription.getControlledSubstance().isSchedule3N4()!=null && prescription.getRefills()>5) {
									refill=5;
								}
								else
									refill=prescription.getRefills();
								expectedRefillCount = refill +" "+ (refill == 1 ? "REFILL": "REFILLS");
							}	
							else
								expectedRefillCount="NO REFILL";
							WebElement refillElement = prescriptionElement.findElement(By.cssSelector("div.row div:nth-child("+2+")"));
							Assert.assertEquals(refillElement.getText(), expectedRefillCount);

							String expectedSubstitutes = null;
							if(prescription.isSubstitutes()!=null && prescription.isSubstitutes().booleanValue() == true)
								expectedSubstitutes = "SUBS OK";
							else 
								expectedSubstitutes = "NO SUBS";
							WebElement substitutesElement = prescriptionElement.findElement(By.cssSelector("div.row div:nth-child("+3+")"));
							//Assert.assertEquals(substitutesElement.getText(), expectedSubstitutes);
							WebElement dispenseAmountElement = prescriptionElement.findElement(By.cssSelector("span:nth-child("+2+")"));
							//Assert.assertTrue(dispenseAmountElement.getText().contains(prescription.getDispenseAmount()));
							if(prescription.getCustomSig()!=null) {
								WebElement customSignatureElement = prescriptionElement.findElement(By.cssSelector("span:nth-child("+3+")"));
								Assert.assertTrue(customSignatureElement.getText().toLowerCase().contains(prescription.getCustomSig().toLowerCase()));
							}
						}
						else 
							continue;
					}
					i++;
				}
			}
		}
		return this;
	}
}