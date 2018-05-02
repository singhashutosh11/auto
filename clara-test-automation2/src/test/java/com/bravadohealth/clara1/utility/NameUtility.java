package com.bravadohealth.clara1.utility;

import java.util.Random;

public class NameUtility {

	public static String generateRandom() {
		String aToZ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rand=new Random();
		StringBuilder res=new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int randIndex=rand.nextInt(aToZ.length()); 
			res.append(aToZ.charAt(randIndex));            
		}
		return res.toString();
	}
}
