package com.bravadohealth.clara1.enums;

public enum PlanType {
    Pro("Pro"),  //calls constructor with value "Pro"
    ProCS("Pro CS"),  //calls constructor with value "Pro CS"
    Basic("Basic"),  //calls constructor with value "Basic"
    ; // semicolon needed when fields / methods follow

    private final String label;
    
    PlanType(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public static PlanType getPlanType(String type) {
        if (type.equalsIgnoreCase("Pro")) {
            return PlanType.Pro;
        } else if (type.equalsIgnoreCase("ProCS")) {
            return PlanType.ProCS;
        }
        else { //type.equalsIgnoreCase("Basic")
            return PlanType.Basic;
        }
    }
    
    public static PlanType getPlanTypeFromUiLabel(String label) {
        if (label.equalsIgnoreCase(Pro.getLabel())) {
            return PlanType.Pro;
        } else if (label.equalsIgnoreCase(ProCS.getLabel())) {
            return PlanType.ProCS;
        }
        else {
            return PlanType.Basic;
        }
    }

}
