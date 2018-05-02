package com.bravadohealth.clara1.dto;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.bravadohealth.clara1.enums.PrescriptionModeEnum;
import com.bravadohealth.pagedataset.Prescription;

public class PrescriptionAuditDTO {
	protected String drugName;
	protected String dose;
	protected Long doseUnitUid;
	protected String frequency;
	protected Integer duration;
	protected String customSig;
	protected String dispenseAmount;
	protected String earliestFillDate;
	protected Integer refills;
	protected Boolean substitutes;
	protected String noteToPharmacy;
	protected String controlledSubstanceCode;
	protected Boolean ePrescribe;

	public PrescriptionAuditDTO(JSONObject medicationDetails) throws JSONException {
		if(medicationDetails.has("dose")) {
			dose = medicationDetails.getString("dose");
		}
		if(medicationDetails.has("drugName")) {
			drugName = medicationDetails.getString("drugName");
		}
		if(medicationDetails.has("duration")) {
			duration = medicationDetails.getInt("duration");
		}
		if(medicationDetails.has("customSig")) {
			customSig = medicationDetails.getString("customSig");
		}
		if(medicationDetails.has("refillCount")) {
			refills = medicationDetails.getInt("refillCount");
		}
		if(medicationDetails.has("substituteOk")) {
			substitutes = medicationDetails.getBoolean("substituteOk");
		}
		if(medicationDetails.has("pharmacyNotes")) {
			setNoteToPharmacy(medicationDetails.getString("pharmacyNotes"));
		}
		if(medicationDetails.has("dispenseAmount")) {
			dispenseAmount = medicationDetails.getString("dispenseAmount");
		}
		if(medicationDetails.has("earliestFillDate")) {
			setEarliestFillDate(medicationDetails.getString("earliestFillDate"));
		}
		if(medicationDetails.has("prescriptionMode")) {
			setePrescribe(medicationDetails.getString("prescriptionMode"));
		}
		if(medicationDetails.has("doseUnitUid")) {
			doseUnitUid = medicationDetails.getLong("doseUnitUid");
		}
		if(medicationDetails.has("frequencyDisplayVal")) {
			frequency = medicationDetails.getString("frequencyDisplayVal");
		}
		if(medicationDetails.has("controlledSubstanceCode")) {
			controlledSubstanceCode = medicationDetails.getString("controlledSubstanceCode");
		}
		
	}

	public PrescriptionAuditDTO() {

	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public Long getDoseUnitUid() {
		return doseUnitUid;
	}

	public void setDoseUnitUid(Long doseUnitUid) {
		this.doseUnitUid = doseUnitUid;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getCustomSig() {
		return customSig;
	}

	public void setCustomSig(String customSig) {
		this.customSig = customSig;
	}

	public String getDispenseAmount() {
		return dispenseAmount;
	}

	public void setDispenseAmount(String dispenseAmount) {
		this.dispenseAmount = dispenseAmount;
	}

	public String getEarliestFillDate() {
		return earliestFillDate;
	}

	public void setEarliestFillDate(String earliestFillDate) {
		if(!StringUtils.isEmpty(earliestFillDate) && !earliestFillDate.equals("null")) {
			this.earliestFillDate = earliestFillDate;
		}
	}

	public Integer getRefills() {
		return refills;
	}

	public void setRefills(Integer refills) {
		this.refills = refills;
	}

	public Boolean getSubstitutes() {
		return substitutes;
	}

	public void setSubstitutes(Boolean substitutes) {
		this.substitutes = substitutes;
	}

	public String getNoteToPharmacy() {
		return noteToPharmacy;
	}

	public void setNoteToPharmacy(String noteToPharmacy) {
		if(!StringUtils.isEmpty(noteToPharmacy) && !noteToPharmacy.equals("null")) {
			this.noteToPharmacy = noteToPharmacy;
		}
	}

	public String getControlledSubstanceCode() {
		return controlledSubstanceCode;
	}

	public void setControlledSubstanceCode(String controlledSubstanceCode) {
		this.controlledSubstanceCode = controlledSubstanceCode;
	}

	public Boolean getePrescribe() {
		return ePrescribe;
	}

	public void setePrescribe(String prescriptionMode) {
		PrescriptionModeEnum prescriptionModeEnum = PrescriptionModeEnum.valueOf(prescriptionMode);
		if (prescriptionModeEnum == PrescriptionModeEnum.EPRESCRIPTION) {
			this.ePrescribe = Boolean.TRUE;
		} else {
			this.ePrescribe = Boolean.FALSE;
		}
	}
	
	public boolean isSameAsPrescriptionEntered(Prescription prescription) {
		boolean areAllDetailsSame = true;
		if(!StringUtils.equalsIgnoreCase(dose, prescription.getDose())){
			areAllDetailsSame = false;
		}
		else if(!StringUtils.equalsIgnoreCase(drugName, prescription.getDrugName())){
			areAllDetailsSame = false;
		}
		else if(!StringUtils.equalsIgnoreCase(frequency, prescription.getFrequency())){
			areAllDetailsSame = false;
		}
		else if(!StringUtils.equalsIgnoreCase(customSig, prescription.getCustomSig())){
			areAllDetailsSame = false;
		}
		else if(prescription.getEarliestFillDate() != null && !StringUtils.equalsIgnoreCase(earliestFillDate, getDateAsPerAuditLogFormat(prescription.getEarliestFillDate()))){
			areAllDetailsSame = false;
		}
		else if(!StringUtils.equalsIgnoreCase(dispenseAmount, prescription.getDispenseAmount())){
			areAllDetailsSame = false;
		}
		else if(!StringUtils.equalsIgnoreCase(noteToPharmacy, prescription.getNoteToPharmacy())){
			areAllDetailsSame = false;
		}
		//not comparing unit of prescription due to bug in clara-dev audit logs showing doseunitDisplayval as null
		else if(!doseUnitUid.equals(prescription.getDoseUnitUid())){
			areAllDetailsSame = false;
		}
		else if(!duration.equals(prescription.getDuration())){
			areAllDetailsSame = false;
		}
		else if(!refills.equals(prescription.getRefills())){
			areAllDetailsSame = false;
		}
		else if(!substitutes.equals(prescription.isSubstitutes())){
			areAllDetailsSame = false;
		}
		else if(!ePrescribe.equals(prescription.isEPrescribe())){
			areAllDetailsSame = false;
		}
		return areAllDetailsSame;
	}
	
	private String getDateAsPerAuditLogFormat(String dob){
		//convert date in pageData.xml in MM/DD/YYYY to YYYY-MM-DD
		String[] arr = dob.split("/");
		StringBuilder dateAsPerAuditLogFormat = new StringBuilder(arr[2]).append("-").append(arr[0]).append("-").append(arr[1]);
		return dateAsPerAuditLogFormat.toString();
	}

}
