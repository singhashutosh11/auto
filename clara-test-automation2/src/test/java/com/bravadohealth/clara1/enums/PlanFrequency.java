package com.bravadohealth.clara1.enums;

public enum PlanFrequency {
    Monthly("monthly"),  //calls constructor with value "monthly"
    Annual("annual")  //calls constructor with value "annual"
    ; // semicolon needed when fields / methods follow

    private final String frequency;

    PlanFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public String getFrequency() {
        return this.frequency;
    }
    
    public static PlanFrequency getPlanFrequency(String frequency) {
        if (frequency.equalsIgnoreCase("monthly")) {
            return PlanFrequency.Monthly;
        }
        else { //frequency.equalsIgnoreCase("annual")
            return PlanFrequency.Annual;
        }
    }
}
