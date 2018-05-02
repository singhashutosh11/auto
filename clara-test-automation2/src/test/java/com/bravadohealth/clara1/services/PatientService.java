package com.bravadohealth.clara1.services;

import java.util.ArrayList;
import java.util.List;

import com.bravadohealth.clara1.utility.PatientUtility;
import com.bravadohealth.pagedataset.Prescription;

public class PatientService {
	
    private static ThreadLocal<List<Prescription>> updatedPrescriptions = new ThreadLocal<List<Prescription>>();
    
    public static void setUpdatedPrescriptions(List<Prescription> prescriptions){
    	
    	updatedPrescriptions.set(prescriptions);
    }
    
    public static List<Prescription> getUpdatedPrescriptions(){
        return updatedPrescriptions.get();
    }
    
    public static boolean hasSignedControlledSubstance(){
        for( Prescription prescription : updatedPrescriptions.get()){
          if( PatientUtility.isControlledSubtanceAvailable(prescription)
        		  && (Boolean.TRUE == prescription.getControlledSubstance().isReadyToSign())) {
             return true;
          }
        }
        return false;
    }
    
	
	public static List<Prescription> getUndeletedPrescriptions() {
		List<Prescription> undeletedPrescriptions = new ArrayList<Prescription>();
		for (Prescription prescription : updatedPrescriptions.get()) {
			if (!PatientUtility.isPrescriptionDeleted(prescription)) {
				undeletedPrescriptions.add(prescription);
			}
		}
		return undeletedPrescriptions;
	}
	
	public static Prescription getFirstDeletedPrescriptionWithDrugName(String drugName) {
		for (Prescription prescription : updatedPrescriptions.get()) {
			if (PatientUtility.isPrescriptionDeleted(prescription) && prescription.getDrugName().equalsIgnoreCase(drugName)) {
				return prescription;
			}
		}
		return null;
	}
    
}
