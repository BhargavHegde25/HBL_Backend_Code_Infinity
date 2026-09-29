package com.kony.task.datavalidation;

import java.util.ArrayList;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.ConsentsManager;
import com.kony.memorymgmt.DirectDebitManager;
import com.kony.memorymgmt.SessionMap;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class StopNextPaymentServiceValidation implements ObjectProcessorTask{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		// TODO Auto-generated method stub
		if (!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		DirectDebitManager directDebitManager = new DirectDebitManager(fabricRequestManager, fabricResponseManager);
		SessionMap DirectDebitId = directDebitManager.getDirectDebitFromSession(HelperMethods.getCustomerIdFromSession(fabricRequestManager));
		if (null == DirectDebitId || DirectDebitId.isEmpty()) {
			JsonObject resPayload = null;
        	resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
			return false;
        }
        diagnostic.prepareDebug("DirectDebitId: " + DirectDebitId.toString()).log();
        JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
        	JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
        	String directDebitIdFromPayload = HelperMethods.getStringFromJsonObject(requestPayload, "Id");
        	if(!DirectDebitId.hasKey(directDebitIdFromPayload)) {
        		JsonObject resPayload = null;
            	resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
    			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
    			return false;
        	}
        }
		
		/*String permission;
		List<String> featureActionIdList = new ArrayList<>();
        featureActionIdList.add("DIRECT_DEBIT_CANCEL");
        permission =HelperMethods.getPermittedUserActionIds(fabricRequestManager,featureActionIdList);
        if(permission == null) {
        	JsonObject resPayload = null;
        	resPayload = ErrorCodeEnum.ERR_12001.setErrorCode(resPayload);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
			return false;
        }*/
        
        
		return true;
    }
}