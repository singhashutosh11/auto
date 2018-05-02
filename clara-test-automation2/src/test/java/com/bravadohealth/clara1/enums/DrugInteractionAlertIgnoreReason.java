package com.bravadohealth.clara1.enums;

//TODO: use enum for ignore reasons in pageData.xml instead of hardcoding actual ignore reason
public enum DrugInteractionAlertIgnoreReason {
	
	PATIENT_HOME_THERPY("Patient will discontinue existing or home therapy"),
    DOCUMENTED_INCORRECT("Documented medication incorrect/outdated"),
    PREVIOUSLY_TOLERATED("Patient previously tolerated"),
    ADVERSE_NOT_ALLERGY("Adverse reaction, not allergy"),
    INCORRECT_ALLERGY("Documented allergy incorrect/outdated"),
    ALLERGY_DOES_NOT_APPLY("Allergy does not apply to route or formulation"),
    DESENSITIZATION_COMPLETE("Desensitization completed or planned"),
    BENEFITS_OUTWEIGH_RISK("Benefits outweigh risks"),
    PATIENT_MEDICATION_DISCONTINUED("Patient medication was/will be discontinued"),
    INTERACTION_DOES_NOT_APPLY("Interaction does not apply to route or formulation"),
    COMBINATION_FOR_LIMITED_TIME("Combination to be used for limited time"),
    DOSAGE_ADJUSTED_INTERACTION("Dosage was/will be adjusted to minimize interaction"),
    PERVIOUSLY_TOLERATED_COMBINATION("Patient previously tolerated combination"),
    PATIENT_BEING_MONITORED("Patient being monitored");

    private final String text;

    private DrugInteractionAlertIgnoreReason(String text) {
        this.text = text;
    }
    
    public String getCode() {
        return this.name();
    }
    
    public String getText() {
        return this.text;
    }
}
