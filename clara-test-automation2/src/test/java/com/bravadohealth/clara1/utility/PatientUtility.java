package com.bravadohealth.clara1.utility;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import com.bravadohealth.clara1.pageObjects.DuplicatePatientPage;
import com.bravadohealth.clara1.pageObjects.locators.EditModalLocators;
import com.bravadohealth.pagedataset.Contact;
import com.bravadohealth.pagedataset.Demographic;
import com.bravadohealth.pagedataset.Patient;
import com.bravadohealth.pagedataset.PatientDetails;
import com.bravadohealth.pagedataset.Prescription;
import com.bravadohealth.pagedataset.PrescriptionsPageData;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class PatientUtility implements EditModalLocators {
	private String presciptionSelectizeElementIdSuffix = "medDrugNameSelectize";

	public static int pediatricAgeLimit = 12;
	public static boolean isPediatric(String dob) {
		String[] arr = dob.split("/");
		LocalDate dobLD =LocalDate.of(Integer.parseInt(arr[2]), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
		LocalDate nowLD = LocalDate.now();
		Period patientAge = calculatePeriodBetween(dobLD, nowLD);
		if(patientAge.getYears() < pediatricAgeLimit) {
			return true;
		}
		else if(patientAge.getYears() == pediatricAgeLimit && patientAge.getMonths() == 0 && patientAge.getDays() == 0){
			return true;
		} else {
			return false;
		}
	}
	
	private static Period calculatePeriodBetween(LocalDate startDate, LocalDate endDate) {
		return Period.between(startDate, endDate);
	}

	public static boolean isValidEarliestFillDateForScheduledSubstance(String EFD, Prescription prescription) {
		String[] arr = EFD.split("/");
		LocalDate EFDate =LocalDate.of(Integer.parseInt(arr[2]), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
		LocalDate nowLD = LocalDate.now();
		Period datePeriod = calculatePeriodBetween(nowLD, EFDate);
		if(datePeriod.getMonths()<0 && datePeriod.getDays()<0) {
			return false;
		}
		if(prescription.getControlledSubstance().isSchedule3N4()!=null && datePeriod.getMonths()>6) {
			return false;
		}
		return true;
	}

	public static boolean isAddPatientEnabled(Demographic demographic, Contact contact) {
		return !StringUtils.isEmpty(demographic.getFirstName()) &&
				!StringUtils.isEmpty(demographic.getLastName()) &&
				!StringUtils.isEmpty(demographic.getBirthday()) &&
				!StringUtils.isEmpty(demographic.getGender()) &&
				!StringUtils.isEmpty(demographic.getAddress()) &&
				!StringUtils.isEmpty(demographic.getCity()) &&
				!StringUtils.isEmpty(demographic.getState()) &&
				demographic.getZipCode()!= null &&
				contact.getPhone()!= null &&
				(!isPediatric(demographic.getBirthday()) || demographic.getWeight()!=null);
	}

	public static String getFromattedAge(String dateOfBirth) {
		if (StringUtils.isEmpty(dateOfBirth)) {
			return "";
		}
		String[] arr = dateOfBirth.split("/");
		LocalDate dobLD =LocalDate.of(Integer.parseInt(arr[2]), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
		LocalDate nowLD = LocalDate.now();
		long years = ChronoUnit.YEARS.between(dobLD, nowLD);
		long months = ChronoUnit.MONTHS.between(dobLD, nowLD);
		long days = ChronoUnit.DAYS.between(dobLD, nowLD);

		if(days<=30) {
			if(days == 1) {
				return "1 day";
			} else {
				return days + " days";
			}
		}
		else if(months>=1&&months<36) {
			if(months == 1) {
				return "1 mo.";
			} else {
				return months +" mos.";
			}
		}
		else {//years>=3
			if(years == 1) {
				return "1 yr.";
			} else {
				return years + " yrs.";
			}
		}   
	};

	public static boolean arePatientsSame(PatientDetails pdFromXml, PatientDetails pdFromView){
		String firstNameXml = pdFromXml.getDemographic().getFirstName();
		String lastNameXml = pdFromXml.getDemographic().getLastName();
		String middleNameXml = pdFromXml.getDemographic().getMiddleName();

		Integer zipCodeXml = pdFromXml.getDemographic().getZipCode();
		String birthDayXml = pdFromXml.getDemographic().getBirthday();

		String fullNameXml = firstNameXml + " " +(!StringUtils.isEmpty(middleNameXml) ? middleNameXml + " " : "") + lastNameXml;

		String fullNameView = pdFromView.getDemographic().getFirstName();
		String completeAddressView = pdFromView.getDemographic().getAddress();
		String birthDayView = pdFromView.getDemographic().getBirthday();

		if(!fullNameXml.equals(fullNameView)) {
			return false;
		}
		if(!completeAddressView.endsWith(zipCodeXml.toString())) {
			return false;
		}
		if(!birthDayXml.equals(birthDayView)) {
			return false;
		}
		return true;
	}

	//TODO: need to verify the purpose it was created for
	public static List<PatientDetails> getClonedAddPatientDetailsList(List<Patient> patients) {
		List<PatientDetails> detailsList = new ArrayList<PatientDetails>();
		for( int i=0; i<patients.size(); i++){
			detailsList.add(patients.get(i).getNewPatient());
		}
		return detailsList;
	}


	public static DuplicatePatientPage checkDuplicateShown(WebOperation webOp, PageDataProvider dataProvider, Demographic demographyOfPatient) throws InterruptedException {
		if (demographyOfPatient != null && demographyOfPatient.isDuplicatePatientRecord() != null && demographyOfPatient.isDuplicatePatientRecord().booleanValue() == true) {
			Thread.sleep(2000);
			Boolean isDuplicate = HelperUtility.isElementPresent(webOp, formFailureErrorMessage)
					&&webOp.driver().findElement(formFailureErrorMessage).getText().contains(AppMessages.DUPLICATE_PATIENT_ERROR);
			Assert.assertTrue(isDuplicate, "For duplicate entry in the record , duplicate message is not displayed.");
			return new DuplicatePatientPage(webOp, dataProvider);
		} 
		return null;
	}

	//TODO: update the method to take care of updates to profile as well
	//similar to  com.bravadohealth.clara1.utility.PatientUtility.getPatientFullNameExcludingMiddleName
	//but should take care of middle Name also
	public static String getPatientFullNameIncludingMiddleName(PatientDetails patientDetails) {
		Demographic demographic = patientDetails.getDemographic();
		StringBuilder fullName =  new StringBuilder(demographic.getFirstName());
		if(!StringUtils.isEmpty(demographic.getMiddleName())){
			fullName.append(" ").append(demographic.getMiddleName());
		}
		fullName.append(" ").append(demographic.getLastName());
		return fullName.toString();
	}
	
	public static String getPatientFullNameExcludingMiddleName(Patient patient) {
		PatientDetails patientDetails;
		if (patient.getNewPatient() != null) {
			patientDetails = patient.getNewPatient();
		} else {
			patientDetails = patient.getExistingPatient();
		}
		String firstName = patientDetails.getDemographic().getFirstName();
		String lastName = patientDetails.getDemographic().getLastName();

		// for new patient only
		if (patientDetails.getEditDetails() != null) {
			Demographic demographyOfEditPatientProfile = patientDetails.getEditDetails().getDemographic();
			if (demographyOfEditPatientProfile != null) {
				if (demographyOfEditPatientProfile.getFirstName() != null) {
					firstName = demographyOfEditPatientProfile.getFirstName();
				}
				if (demographyOfEditPatientProfile.getLastName() != null) {
					lastName = demographyOfEditPatientProfile.getLastName();
				}
			}
		}

		// for new and old both patients
		PrescriptionsPageData prescriptionsPageData = patientDetails.getPrescriptionsPageData();
		if(prescriptionsPageData != null) {
			PatientDetails editFullPatientData = prescriptionsPageData.getEditFullPatientData();
			if (editFullPatientData != null) {
				Demographic demographyOfEditFullPatient = editFullPatientData.getDemographic();
				if (demographyOfEditFullPatient != null) {
					if (demographyOfEditFullPatient.getFirstName() != null) {
						firstName = demographyOfEditFullPatient.getFirstName();
					}
					if (demographyOfEditFullPatient.getLastName() != null) {
						lastName = demographyOfEditFullPatient.getLastName();
					}
				}
			}
		}
		return HelperUtility.getFullNameFromFirstAndLastName(firstName, lastName);
	}

	public static int findInitialPrescriptionSerialNum(List<Prescription> initialPrescriptions, String drugName){
		int i= 0;
		for(Prescription prescription : initialPrescriptions) {
			if(!isPrescriptionDeleted(prescription)){
				if(prescription.getDrugName().equals(drugName)){
					return i;// 0-indexed
				}
				i++;
			}
		}
		return -1;
	}

	public static boolean isPrescriptionDeleted(Prescription prescription){
		String delete = prescription.getDelete();
		return (delete!=null && delete.equalsIgnoreCase("true"));
	}
	public static boolean isControlledSubtanceAvailable(Prescription prescription){
		return !isPrescriptionDeleted(prescription) && prescription.getControlledSubstance()!=null;
	}
	//TODO: move it to PatientService
	public static int getAddedPrescriptionsCount(List<Prescription> prescriptions){
		int addedPrescriptions = 0;
		for(Prescription prescription : prescriptions) {
			if(!isPrescriptionDeleted(prescription)){
				addedPrescriptions++;
			}
		}
		return addedPrescriptions;
	}
	//TODO: move it to PatientService
	public static Prescription getFirstPrescriptionWithDrugName(String drugName, List<Prescription> prescriptions) {
		for (Prescription prescription : prescriptions) {
			if (!isPrescriptionDeleted(prescription) && prescription.getDrugName().equalsIgnoreCase(drugName)) {
				return prescription;
			}
		}
		return null;
	}
}
