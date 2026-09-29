package com.temenos.infinity.api.srmstransactions.utils;


import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class GetValidIBANMockOperation implements ObjectProcessorTask {

	 public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
	        try {
	            String paymentBackend = EnvironmentConfigurationsHandler.getServerProperty("PAYMENT_BACKEND");
	            if("MOCK".equals(paymentBackend) || "SRMS_MOCK".equals(paymentBackend)) {
	            	JsonObject payload=new JsonObject(); 
	                String IBAN = HelperMethods.getStringFromJsonObject(fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject(),"iban", false);
	                payload.addProperty("iban:",IBAN);
	                payload.addProperty("isIBANValid: ", "YES");
	                payload.addProperty("opstatus", Integer.parseInt("0"));
	                payload.addProperty("httpStatusCode", Integer.parseInt("200"));
	                fabricResponseManager.getPayloadHandler().updatePayloadAsJson(payload);
	                return false;
	            }
	        } catch (Exception e){
	            return false;
	        }
	        return true;
	    }
}
