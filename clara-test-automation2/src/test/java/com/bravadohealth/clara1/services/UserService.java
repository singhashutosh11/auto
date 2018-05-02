package com.bravadohealth.clara1.services;

import com.bravadohealth.pagedataset.ClaraUserDetails;

public class UserService {
	
    private static ThreadLocal<ClaraUserDetails> currentUser = new ThreadLocal<ClaraUserDetails>();
    
    public static void setCurrentUser(ClaraUserDetails claraUserDetails){
    	currentUser.set(claraUserDetails);
    }
        
    public static ClaraUserDetails getCurrentUser(){
        return currentUser.get();
    }
        
}
