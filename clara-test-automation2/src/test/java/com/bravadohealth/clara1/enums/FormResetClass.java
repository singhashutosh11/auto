package com.bravadohealth.clara1.enums;

public enum FormResetClass {
    Pristine("ng-pristine"),
    Untouched("ng-untouched")
    ;

    private final String formClass;

    FormResetClass(String formClass) {
        this.formClass = formClass;
    }
    
    public String getFormClass() {
        return this.formClass;
    }
}

