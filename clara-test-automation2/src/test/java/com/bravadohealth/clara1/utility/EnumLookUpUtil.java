package com.bravadohealth.clara1.utility;

public class EnumLookUpUtil {
	public static <E extends Enum<E>> E lookup(Class<E> enumClass, String enumName) {
		E result = null;
		try { 
	         result = Enum.valueOf(enumClass, enumName);
	      } catch (IllegalArgumentException ex) {
	         // log error if needed
	      }
	      return result;
	   }
}
