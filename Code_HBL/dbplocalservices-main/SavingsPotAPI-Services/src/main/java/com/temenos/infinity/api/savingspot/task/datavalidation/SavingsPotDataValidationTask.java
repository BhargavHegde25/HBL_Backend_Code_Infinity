package com.temenos.infinity.api.savingspot.task.datavalidation;


import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.api.savingspot.constants.TemenosConstants;

public class SavingsPotDataValidationTask implements ObjectProcessorTask {

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
    throws Exception {
        JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        if (HelperMethods.isJsonNotNull(reqPayloadJEle)) {
            JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
            if (requestPayload.get(TemenosConstants.POTTYPE) != null) {
            	String potType = requestPayload.get(TemenosConstants.POTTYPE).getAsString();
            	if (potType.equalsIgnoreCase(TemenosConstants.BUDGET)) {
            		return true;
            	}
            }
            
            String targetPeriod = null;
            if (requestPayload.get(TemenosConstants.TARGETPERIOD) != null) {
            	targetPeriod = requestPayload.get(TemenosConstants.TARGETPERIOD).toString();
            
	            if (targetPeriod != null && !targetPeriod.isEmpty()) {
	            	try {
	            		int nbMonths = Integer.parseInt(targetPeriod.replace("\"", ""));
	            		if (nbMonths > 120) {
	            			JsonObject resPayload = null;
	                        resPayload = ErrorCodeEnum.ERR_20050.setErrorCode(resPayload);
	                        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
	                        return false;
	            		}
	            	} catch (Exception e) {
	            		JsonObject resPayload = null;
	                    resPayload = ErrorCodeEnum.ERR_20049.setErrorCode(resPayload);
	                    fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
	                    return false;
	            	}
	            }
            }
        }
        return true;
    }
}