package com.bravadohealth.clara1.enums;

import java.util.HashMap;

import com.bravadohealth.clara1.utility.HelperUtility;

public enum AppPage {
	
    LoginPage("/login","/index"),
    FcilityPage("/facility-view","/facility-view"),
    AccountPage("/account/","/account/"),
    SelectPatient("/select-patient", "/home-view"),
    SelectFacility("/select-facility", "/home-view"),
    AddNewPatient("/new-patient", "/add-new-patient"),
    PatientMedicalHistory("/patient-medical-history", "/add-medical-history"),
   // SelectPharmacy("/select-pharmacy", "/select-pharmacy"),
    WritePrescription("/patient-prescription", "/write-prescriptions"),
    ManagePatients("/manage-patients", "/manage-patients"),
    PrescriptionResult("/prescription-", "/result-view"),
    AuthorizePrescriptions("/authorize-prescriptions", "/authorize-prescriptions"),
    AuditTrails("/audit-trails", null),
    UsersPage("/users-view","/users-view"),
    SetupFacility("setup/create-facility", "setup/create-facility"),
    LicenseVerification("setup/id-verification", "setup/id-verification")
    ;

    private final String urlContains;
    private final String mockUrlContains;

    AppPage(String urlContains, String mockUrlContains) {
        this.urlContains = urlContains;
        this.mockUrlContains = mockUrlContains;
    }
    
    public String getUrlContains(HashMap<String,String> hMap) {
        if(!HelperUtility.isMockProject(hMap)) {
            return this.urlContains;
        } else {
            return this.mockUrlContains;
        }
    }
}
