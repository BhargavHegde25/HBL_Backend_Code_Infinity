package com.kony.adminconsole.handler;

import java.util.HashMap;

import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;

public class ApplicationParametersHandler {
	private static final String APPLICATION_ATTRIBUTE = "isKeyCloakEnabled";
	private static final String IS_SINGLE_ENTITY = "isSingleEntity";
	
	public static String fetchIsKeyCloakEnabled(DataControllerRequest requestInstance) { 
		String readResponse = Executor.invokeService(ServiceURLEnum.APPLICATION_READ, new HashMap<>(), null, requestInstance);
		JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readResponse);
		String attributeValue = responseJSON.getJSONArray("application").getJSONObject(0).getString(APPLICATION_ATTRIBUTE);
		return attributeValue;		
	}
	
	public static String fetchIsSingleEntity(DataControllerRequest requestInstance) { 
		String readResponse = Executor.invokeService(ServiceURLEnum.APPLICATION_READ, new HashMap<>(), null, requestInstance);
		JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readResponse);
		String attributeValue = responseJSON.getJSONArray("application").getJSONObject(0).getString(IS_SINGLE_ENTITY );
		return attributeValue;		
	}
	
}
