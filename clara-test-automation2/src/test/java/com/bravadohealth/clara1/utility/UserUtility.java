package com.bravadohealth.clara1.utility;

public class UserUtility {
    
    public static boolean isAddressAvailable(String address, String city, String state, String zipCode){
        if (address != null && city != null && state != null && zipCode !=null) {
          return true;
        } 
        return false;
    }
}
