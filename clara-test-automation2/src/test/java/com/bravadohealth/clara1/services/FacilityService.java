package com.bravadohealth.clara1.services;

import java.util.Date;

import com.bravadohealth.pagedataset.Facility;

import trial.keyStone.automation.PageDataProvider;
import trial.keyStone.automation.WebOperation;

public class FacilityService {
	
    private static ThreadLocal<Facility> currentFacility = new ThreadLocal<Facility>();
    private static ThreadLocal<Date> dateTimeAtSelectFacility = new ThreadLocal<Date>();
    private static ThreadLocal<Boolean> isNominatorChanged = new ThreadLocal<Boolean>(); 
    private static ThreadLocal<Boolean> isAltNominatorChanged = new ThreadLocal<Boolean>();
    private static ThreadLocal<String> nominaotrToken = new ThreadLocal<String>(); 
    private static ThreadLocal<String> altNominaotrToken = new ThreadLocal<String>(); 
    
    public static void initializeFacility(WebOperation webOp, PageDataProvider dataProvider){
    	if(getFacility() != null){
    		return;
    	}
    	
    	Facility facility = null;
    	if(webOp.getParameter("facility") != null) {
    		facility = dataProvider.getPageData("facility");
		}
		//TODO: use the facility name directly from sheet to select facility
		else if(webOp.getParameter("facilityName") != null){
			String facilityName = dataProvider.getPageData("facilityName");
			facility = new Facility();
			facility.setFacilityName(facilityName);
		} else {
			facility = new Facility();
		}
    	currentFacility.set(facility);
    }
    
    public static void setFacility(Facility facility){
    	currentFacility.set(facility);
    }
    
    public static Facility getFacility(){
        return currentFacility.get();
    }
    
    public static void setDateTimeAtSelectFacility() {
    	dateTimeAtSelectFacility.set(new Date());
    }
    
    public static Date getDateTimeAtSelectFacility() {
    	return dateTimeAtSelectFacility.get();
    }

	public static Boolean getIsNominatorChanged() {
		return isNominatorChanged.get();
	}

	public static void setIsNominatorChanged(Boolean changed) {
		isNominatorChanged.set(changed);
	}

	public static Boolean getIsAltNominatorChanged() {
		return isAltNominatorChanged.get();
	}

	public static void setIsAltNominatorChanged(Boolean changed) {
		isAltNominatorChanged.set(changed);
	}

	public static void setNominatorEmailToken(String token) {
		nominaotrToken.set(token);
	}

	public static void setAltNominatorEmailToken(String token) {
		altNominaotrToken.set(token);
	}

	public static String getNominatorEmailToken() {
		return nominaotrToken.get();
	}

	public static String getAltNominatorEmailToken() {
		return altNominaotrToken.get();
	}
}
