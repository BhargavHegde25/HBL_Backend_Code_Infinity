package com.kony.dbputilities.payeeservices;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.konylabs.middleware.controller.DataControllerRequest;

public class VariableUtils {

		public static String quote(String sortVariable, DataControllerRequest dcRequest) {
			
			if(QueryFormer.getDBType(dcRequest).equalsIgnoreCase("ORACLE"))
				return "\"" + sortVariable + "\"";
			else
				return sortVariable;		
	}
}
