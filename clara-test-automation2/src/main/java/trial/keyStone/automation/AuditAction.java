package trial.keyStone.automation;

public enum AuditAction {
	
    SUCCESSFUL_LOGIN("successful login to Clara"),
    FAILED_LOGIN("failed login attempt to Clara"),
    USER_LOGOUT("User logout"),
    NEW_ACCESS_DETECTION("User accessed the application from a new environment"),
    
    FACILITY_CREATED("Facility Created"),
    FACILITY_MODIFIED("Facility details modified"),
    PASSWORD_CHANGED_BY_EMAIL_LINK("password changed via email link"),
    PASSWORD_CHANGED_FROM_PROFILE("password changed from profile"),
    USER_ACCOUNT_MODIFIED("User account modified"),
    FAVORITE_CREATED("Favorite Created"),
    FAVORITE_REMOVED("Favorite prescription deleted"),
    USER_SELECTED_FACILITY("User selected facility"),
    
    PATIENT_ACCOUNT_CREATED("Patient account was created"),
    PATIENT_ACCOUNT_MODIFIED("Patient account modified"),
    PATIENT_ACCOUNT_ACCESSED("Patient account was accessed"),

    SELECTED_NONE_ALLERGY("selected none allergy"),
    ALLERGY_CREATED("allergy created"),
    ALLERGY_DISCONTINUED("allergy discontinued"),
    ALLERGY_MARKED_AS_ERROR("allergy marked as error"),
    ALLERGY_RECONTINUED("allergy recontinued"),
    
    SELECTED_NONE_HOME_MEDICATION("selected none home medication"),
    HOME_MEDICATION_CREATED("home medication created"),
    HOME_MEDICATION_EDITED("home medication edited"),
    HOME_MEDICATION_DISCONTINUED("home medication discontinued"),
    HOME_MEDICATION_MARKED_AS_ERROR("home medication marked as error"),
    HOME_MEDICATION_RECONTINUED("home medication recontinued"),
    
    SELECTED_NONE_DIAGNOSIS("selected none diagnosis"),
    DIAGNOSIS_CREATED("diagnosis created"),
    DIAGNOSIS_DISCONTINUED("diagnosis discontinued"),
    DIAGNOSIS_MARKED_AS_ERROR("diagnosis marked as error"),
    DIAGNOSIS_RECONTINUED("diagnosis recontinued"),

    MEDICATION_PRESCRIBED("Medication prescribed"),
    PRESCRIPTION_CREATED("New prescription created"),
    PRESCRIPTION_SIGNED("Prescribed medication signed"),
    PRESCRIPTION_SIGNING_FAILED("Prescribed medication signing failed"),
    PRESCRIPTION_MODIFIED("Prescribed medication modified"),
    PRESCRIPTION_REMOVED("Prescribed medication removed"),
    CONTROLLED_SUBSTANCE_PRESCRIBED("Controlled Substance prescribed"),
    PRESCRIPTION_TRANSMITTED_TO_SURESCRIPTS("Medication eprescribed successfully"),
    PRESCRIPTION_FAILED_TO_SURESCRIPTS("Medication eprescription failed"),
    PRESCRIPTION_PRINT_JOB_CREATED("Prescription print job created"),
    TWO_FA_SERVICE_DOWN("Prescription will be printed as 2FA service is down"),
    
    PROVIDER_DEA_INTERNAL_CODE_UPDATED("Facility DEA Number Entered"),
    PROVIDER_DEA_NUMBER_UPDATED("Individual DEA Number Entered"),
    FACILITY_DEA_NUMBER_ENTERED("Should be removed after the audit tables are cleared. Facility DEA Number Entered"),
	INDIVIDUAL_DEA_NUMBER_ENTERED("Should be removed after the audit tables are cleared. Individual DEA Number Entered"),
    PROVIDER_FACILITY_FIELD_MODIFIED("Provider Facility field modified!!"),
    
    USER_ID_PROOFED("user id proofed"),
    USER_GRANTED_EPCS_PERMISSIONS("user granted epcs permissions"),
    EPCS_NOMINATION_CREATED("user nominated for epcs"),
    EPCS_NOMINATION_APPROVED("user enrolled for epcs"),
    EPCS_NOMINATION_REJECTED("user epcs nomination rejected"),
    ACL_ASSIGN_PERMISSION("New permissions assigned."),
    ACL_REMOVE_PERMISSION("Permissions removed."),
    ACL_GROUP_CREATED("New group created"),
    ACL_GROUP_DELETED("Group deleted"),
    ACL_USER_ADDED_TO_GROUP("User added to group"),
    ACL_USER_REMOVED_FROM_GROUP("User removed from group"),
    
    MEDED_TURNED_ON("Meded added"),
    MEDED_TURNED_OFF("Meded removed by user"),
    
    DRUG_ALLERGY_INTERACTION_FOUND("Drug Allergy interaction found"),
    DRUG_DRUG_INTERACTON_FOUND("Drug drug Interaction found"),
    DRUG_DUPLICATE_THERAPY_FOUND("Drug duplicate therapy found"),
    
    DESIGNATE_NOMINATOR("Designate nominator"),
    CHANGE_NOMINATOR("Change nominator"),
    DESIGNATE_ALTERNATE_NOMINATOR("Designate Alternate nominator"),
    CHANGE_ALTERNATE_NOMINATOR("Change Alternate nominator"),
    SEND_DESIGNATION_EMAIL("send nominator designation email"),
    NOTIFICATION_EMAIL_GENERATED("Notification email generated"),
    NOMINATION_EMAIL_SENT("Notification email sent"),
    SEND_UNDELIVERABLE_EMAIL_NOTICE("Send undeliverable email notice"),
    SEND_UNDELIVERABLE_REMINDER_EMAIL_NOTICE("Send undeliverable reminder email notice"),
    REMINDER_EMAIL("Notification email regenerated"),
    USER_NOMINATED_FOR_EPCS("user nominated for epcs"),
    SELF_APPROVE_FOR_EPCS("user self enrolled for epcs"),
    
    LICENSE_VERIFIED("License verified");
	
	
	
    private String text;

    private AuditAction(String text) {
        this.text = text;
    }

    public String getCode() {
        return this.name();
    }

}
