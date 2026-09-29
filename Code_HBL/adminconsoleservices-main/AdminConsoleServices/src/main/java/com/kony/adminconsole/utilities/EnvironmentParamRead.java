package com.kony.adminconsole.utilities;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.konylabs.middleware.controller.DataControllerRequest;

public class EnvironmentParamRead {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	public static int getSuggestionRecordLimit(DataControllerRequest request){
    	
		int limitValue = 0;
    	try {
    		
    		String runtimeParamValue = EnvironmentConfiguration.AC_MSG_SUGGESTION_LIMIT.getValue(request);
    		
    		limitValue = Integer.parseInt(runtimeParamValue);
    		
    		if(limitValue <= 0) {
    			limitValue = 0;
    		}
    		
    	} catch(Exception exp) {
    		alert.prepareError("Exception occured while parsing limitValue", exp).log();
    		limitValue = 0;
    	}
    	
    	return limitValue;
    	
    }
	
	public static int getMsgCreateThreadCount(DataControllerRequest request){
    	
		int limitValue = 0;
    	try {
    		
    		String runtimeParamValue = EnvironmentConfiguration.AC_MSG_CREATE_THREAD_COUNT.getValue(request);
    		
    		limitValue = Integer.parseInt(runtimeParamValue);
    		
    		if(limitValue <= 0) {
    			limitValue = 10;
    		}
    		
    	} catch(Exception exp) {
    		alert.prepareError("Exception occured while parsing limitValue", exp).log();
    		limitValue = 10;
    	}
    	
    	return limitValue;
    	
    }

}
