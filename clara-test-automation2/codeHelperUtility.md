# This doc talks about all static methods that are there classes in /test-automation/src/test/java/com/bravadohealth/clara1/utility package #

### AppMessages.java ###
- This file should have all the messages that clara-app shows on ui be it failure or successful

### HelperUtility.java ##
- **isElementPresent**  
	checks if element is present in dom, useful if element is not found and we don't want to catch exception
- **isElementAbsent**  
	opposite of above
- **checkToasterDisplayed**  
	check if success/error toastr is displayed after any operation happens
- **confirmAlertModal**  
	confirm alert shown/confirm discard data	
- **navigateBack**  
	move to back page
- **checkUrlChangesTo**  
	wait until browser url changes to the expected value
- **checkModalClosed**  
	verify if modal is closed with given modal locator
- **getRandomZipCode**  
	generates random 5 digit zipCode
- **isMockProject**  
	check if automation is running on php mock project
- **checkModalAndClickPrimary** (appears same as confirmAlertModal method)  
	check if modal appears then click primary/remove button
- **getFullNameFromFirstAndLastName**  
	get full name from first and last name
- **closeAllOtherWindows**  
	if many windows open up and we want to work on only current window, then this method will close all other windows like medEd and printedMedications windows
- **getJsonArray**  
	get JSONArray object from string json, useful for verifying audit log properties(nested even)
- **getJsonObject**  
	get JSONObject object from string json, useful for verifying audit log properties(nested even)
	
### PatientService.java ###
- **setUpdatedPrescriptions**  
	setPrescriptions if modified when we edit Precriptions or we add alternate Prescriptions(also includes added but later deleted)

- **getUpdatedPrescriptions**  
	get all Prescriptions be it added or deleted

- **hasSignedControlledSubstance**  
	from the list of undeletd prescriptions, if there is any controlled substance which is ready to sign
	
### PatientUtility.java ###
- **isPediatric**  
	method to check if patient is pediatric, for now maxPediatricAge is hardcoded as 12
- **isValidEarliestFillDateForScheduledSubstance**  
	check if earliest fill date is valid for scheduled substance based on monthsDiff>=0 && monthsDiff<=6
- **isAddPatientEnabled**  
	verify if next button on add patient page is enabled
- **getFromattedAge**  
	get age matching patient demographics view section
- **arePatientsSame**  
	verify if patient data in pageData.xml matches the patient shown on managePatients page
- **checkDuplicateShown**  
	check duplicate patient error message shown on patient details modal
- **findInitialPrescriptionSerialNum**  
	returns index(0-based index) of first  prescription with drugName from the list of undeleted prescriptions
- **isPrescriptionDeleted**  
	to verify if prescription was deleted due to click on delete icon on due to interacn alerts
- **isControlledSubtanceAvailable**  
	to verify if the prescription is an undeleted controlled substance
- **getAddedPrescriptionsCount**  
	get count of undeleted/added prescriptions
- **getFirstPrescriptionWithDrugName**  
	returns first undeleted prescription with given drugName

### UserUtility.java ###
- **isAddressAvailable**  
	verifies if address should be visible on ui
	
### DrugInteracnUtility.java ###
- **checkDrugInteracnModalAndRespond**  
	check and respond to drug interacn modal
- **checkAndRespondToDrugInteracnModals**  
	check and respond to all drug interacn modals that come up after editing medical history on write prescripns screen