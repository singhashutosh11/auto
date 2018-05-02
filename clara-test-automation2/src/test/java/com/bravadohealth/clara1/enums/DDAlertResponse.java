package com.bravadohealth.clara1.enums;

public enum DDAlertResponse {
    Accept("accept", "ALERT"),
    Ignore("ignore", "IGNORE")
    ; // semicolon needed when fields / methods follow

    private final String aliasInExcel;
    private final String statusinAuditLogDetails;
    
    DDAlertResponse(String aliasInExcel, String statusinAuditLogDetails) {
        this.aliasInExcel = aliasInExcel;
        this.statusinAuditLogDetails = statusinAuditLogDetails;
    }
    
    public String getStatusinAuditLogDetails(){
    	return this.statusinAuditLogDetails;
    }
    
    public static DDAlertResponse getAlertResponse(String aliasInExcel) {
        if (aliasInExcel.equalsIgnoreCase("accept")) {
            return DDAlertResponse.Accept;
        }
        else { //aliasInExcel.equalsIgnoreCase("ignore")
            return DDAlertResponse.Ignore;
        }
    }
}
