package com.kony.logservices.util;

public class VariableUtils {
		
		public static String quote(String sortVariable) {
				
				if(QueryFormer.getDBType().equalsIgnoreCase("ORACLE"))
					return "\"" + sortVariable + "\"";
				else
					return sortVariable;		
	}
}