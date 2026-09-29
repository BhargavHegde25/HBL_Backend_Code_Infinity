package com.kony.consent.task.datavalidation;

import java.util.ArrayList;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.memorymgmt.ConsentsManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.SessionMap;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class UpdateCDPConsentServiceValidation implements ObjectProcessorTask{
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		// TODO Auto-generated method stub
		ConsentsManager consentsManager = new ConsentsManager(fabricRequestManager, fabricResponseManager);
		SessionMap CDPConsentId = consentsManager.getCDPConsentFromSession(HelperMethods.getCustomerIdFromSession(fabricRequestManager));
		String CDPArrangementId = CDPConsentId.getAttributeValueForKey("CDP_CONSENTS", "CDP_CONSENTS_ARRANGEMENT_ID");
		String PaymentBackend= EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");
		if("Mock".equalsIgnoreCase(PaymentBackend)) {
			return true;
		}
		if (null == CDPConsentId || CDPConsentId.isEmpty()) {
            diagnostic.prepareDebug("CDPConsentId is Null / Empty").log();
            return false;
        }
        diagnostic.prepareDebug("CDPConsentId: " + CDPConsentId.toString()).log();
        JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
        	
        	if(CDPArrangementId == "" || CDPArrangementId == null) {
				JsonObject resPayload = null;
	        	resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
				return false;
			}
        	
        	JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
        	requestPayload.addProperty("arrangementId", CDPArrangementId);
        	fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
        }
        
        if (!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		String permission;
		List<String> featureActionIdList = new ArrayList<>();
        featureActionIdList.add("CDP_CONSENT_EDIT");
        permission =HelperMethods.getPermittedUserActionIds(fabricRequestManager,featureActionIdList);
        
        if(permission == null) {
        	JsonObject resPayload = null;
        	resPayload = ErrorCodeEnum.ERR_12001.setErrorCode(resPayload);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
			return false;
        }
		return true;
    }
}