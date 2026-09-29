package com.kony.dbputilities.customersecurityservices.postprocessor;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.dbx.product.utils.InfinityConstants;

public class InfinityUserPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        JsonObject responsePayloadJson = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
       
        JsonArray transactionLimits = responsePayloadJson.has(InfinityConstants.transactionLimits) && !responsePayloadJson.get(InfinityConstants.transactionLimits).isJsonNull()
        		? responsePayloadJson.get(InfinityConstants.transactionLimits).getAsJsonArray() : new JsonArray(); 
        
        for(JsonElement transactionLimitElement: transactionLimits) {
        	JsonObject transactionLimit = transactionLimitElement.getAsJsonObject();
        	
        	JsonArray limitGroups = transactionLimit.has(InfinityConstants.limitGroups) && !transactionLimit.get(InfinityConstants.limitGroups).isJsonNull()
            		? transactionLimit.get(InfinityConstants.limitGroups).getAsJsonArray() : new JsonArray(); 
            
            for(JsonElement limitGroupElement : limitGroups) {
            	JsonObject limitGroup = limitGroupElement.getAsJsonObject();
            	if(limitGroup.has(InfinityConstants.limits) && !limitGroup.get(InfinityConstants.limits).isJsonNull()) {
            		JsonArray limits = limitGroup.get(InfinityConstants.limits).getAsJsonArray();
            		
            		for(JsonElement limitElement : limits) {
            			JsonObject limit = limitElement.getAsJsonObject();
            			String value = limit.has(InfinityConstants.value) && !limit.get(InfinityConstants.value).isJsonNull()
            	        		? limit.get(InfinityConstants.value).getAsString() : "0.0"; 
            			String minValue = limit.has(InfinityConstants.minValue) && !limit.get(InfinityConstants.minValue).isJsonNull()
            	        		? limit.get(InfinityConstants.minValue).getAsString() : "0.0"; 
            			String maxValue = limit.has(InfinityConstants.maxValue) && !limit.get(InfinityConstants.maxValue).isJsonNull()
            	        		? limit.get(InfinityConstants.maxValue).getAsString() : "0.0"; 
            			limit.addProperty(InfinityConstants.value, convertFromExponentialToNumber(value));
            			limit.addProperty(InfinityConstants.minValue, convertFromExponentialToNumber(minValue));
            			limit.addProperty(InfinityConstants.maxValue, convertFromExponentialToNumber(maxValue));
            		}
            	}
            }
            
            JsonArray accounts = transactionLimit.has(InfinityConstants.accounts) && !transactionLimit.get(InfinityConstants.accounts).isJsonNull()
            		? transactionLimit.get(InfinityConstants.accounts).getAsJsonArray() : new JsonArray(); 
            
            for(JsonElement accountElement : accounts) {
            	JsonObject account = accountElement.getAsJsonObject();
            	if(account.has(InfinityConstants.featurePermissions) && !account.get(InfinityConstants.featurePermissions).isJsonNull()) {
            		JsonArray featurePermissions = account.get(InfinityConstants.featurePermissions).getAsJsonArray();
            		
            		for(JsonElement featurePermissionElement : featurePermissions) {
            			JsonObject featurePermission = featurePermissionElement.getAsJsonObject();
            			if(featurePermission.has(InfinityConstants.limits) && !featurePermission.get(InfinityConstants.limits).isJsonNull()) {
                    		JsonArray limits = featurePermission.get(InfinityConstants.limits).getAsJsonArray();
                    		for(JsonElement limitElement : limits) {
                    			JsonObject limit = limitElement.getAsJsonObject();
                    			String value = limit.has(InfinityConstants.value) && !limit.get(InfinityConstants.value).isJsonNull()
                    	        		? limit.get(InfinityConstants.value).getAsString() : "0.0"; 
                    			limit.addProperty(InfinityConstants.value, convertFromExponentialToNumber(value));
                    		}
                    	}
            		}
            	}
            }
        }
        responseManager.getPayloadHandler().updatePayloadAsJson(responsePayloadJson);
    }

    
    private String convertFromExponentialToNumber(String number) {
    	return new BigDecimal(number).toPlainString();
    }
    
    
    
}
