package com.bravadohealth.clara1.utility;

public interface AppMessages {
	String DUPLICATE_PATIENT_ERROR = "TBD - A patient with these details already exists.";
	public interface Templates{
		public interface WRITE_PATIENT_PRESCRIPTIONS{
			String SELECT_PHARMACY_TO_EPRESCRIBE = "To send the prescription electronically, select a pharmacy.";
			String SELECT_EPCS_ENABLED_PHARMACY = "To send the prescription electronically, select a pharmacy that accepts EPCS.";
			String UPDATE_TO_EPCS_ENABLED_PHARMACY = "To send the prescription electronically, select a pharmacy that accepts EPCS.";
		}
		public interface NON_INSTITUTIONAL_EPCS_ENROLLMENT{
            String NO_DATA_FOR_NOMINATION = "There's nobody to nominate right now.";
        }
	}
}
