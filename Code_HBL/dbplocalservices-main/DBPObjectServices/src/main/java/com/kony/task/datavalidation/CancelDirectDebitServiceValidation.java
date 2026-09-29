package com.kony.task.datavalidation;

import java.util.ArrayList;
import java.util.List;

import com.temenos.dbx.product.constants.Constants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.ConsentsManager;
import com.kony.memorymgmt.DirectDebitManager;
import com.kony.memorymgmt.SessionMap;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CancelDirectDebitServiceValidation implements ObjectProcessorTask{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		// TODO Auto-generated method stub
		if (!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		//Commenting as permissions will not be present in security attributes
//		String permission;
//		List<String> featureActionIdList = new ArrayList<>();
//        featureActionIdList.add("DIRECT_DEBIT_CANCEL");
//        permission =HelperMethods.getPermittedUserActionIds(fabricRequestManager,featureActionIdList);
//        if(permission == null) {
//        	JsonObject resPayload = null;
//        	resPayload = ErrorCodeEnum.ERR_12001.setErrorCode(resPayload);
//			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
//			return false;
//        }
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if(PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
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
        	String directDebitIdFromPayload = HelperMethods.getStringFromJsonObject(requestPayload, "directDebitId");
        	if(!DirectDebitId.hasKey(directDebitIdFromPayload)) {
        		JsonObject resPayload = null;
            	resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
    			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
    			return false;
        	}
        }
        
        
		return true;
    }
}