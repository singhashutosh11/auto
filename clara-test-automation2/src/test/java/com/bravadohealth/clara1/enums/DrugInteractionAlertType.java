package com.bravadohealth.clara1.enums;

import trial.keyStone.automation.AuditAction;

public enum DrugInteractionAlertType {
    DrugDrug("Drug-Drug", AuditAction.DRUG_DRUG_INTERACTON_FOUND),
    DrugAllergy("Drug-Allergy", AuditAction.DRUG_ALLERGY_INTERACTION_FOUND),
    DuplicateTherapy("Duplicate-Therapy", AuditAction.DRUG_DUPLICATE_THERAPY_FOUND);

    private final String alertTextForUI;
    
    private final AuditAction auditAction;

    DrugInteractionAlertType(String alertTextForUI, AuditAction auditAction) {
        this.alertTextForUI = alertTextForUI;
        this.auditAction = auditAction;
    }
    
    public String getAlertTextForUI() {
        return this.alertTextForUI;
    }
    
    public AuditAction getAuditAction() {
        return this.auditAction;
    }
    
    public static DrugInteractionAlertType getAlertType(String alertType) {
        if (alertType.equalsIgnoreCase("DrugDrug")) {
            return DrugInteractionAlertType.DrugDrug;
        }
        else if (alertType.equalsIgnoreCase("DrugAllergy")) {
            return DrugInteractionAlertType.DrugAllergy;
        }
        else {
            return DrugInteractionAlertType.DuplicateTherapy;
        }
    }

}
