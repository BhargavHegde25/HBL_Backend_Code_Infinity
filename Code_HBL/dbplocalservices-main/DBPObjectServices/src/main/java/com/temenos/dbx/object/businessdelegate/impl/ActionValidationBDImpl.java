package com.temenos.dbx.object.businessdelegate.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.scaintegration.helper.Helper;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class ActionValidationBDImpl implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();

			if(HelperMethods.isMFAVerify(requestPayload)
					|| HelperMethods.isDACSkip(requestPayload) || Helper.isScaVerify(requestPayload)) {
				return true;
			}

			String permission = requestPayload.has(MFAConstants.SERVICE_NAME) && !requestPayload.get(MFAConstants.SERVICE_NAME).isJsonNull() ? requestPayload.get(MFAConstants.SERVICE_NAME).getAsString() : "";
			
			if(StringUtils.isBlank(permission)) {
			    updateErrorCode(fabricResponseManager);
                return false;
			}
			 Map<String, String> loggedInUserInfo = com.kony.dbputilities.util.HelperMethods.getCustomerFromAPIDBPIdentityService(fabricRequestManager);
		        if (com.kony.dbputilities.util.HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
		            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(fabricRequestManager);
		            if (userPermissions.contains(permission)) {
		                return true;
		            }
		        }
		        else {
		            return true;
		        }
		}
		
		updateErrorCode(fabricResponseManager);
		return false;
	}

	private void updateErrorCode(FabricResponseManager fabricResponseManager) {
		JsonObject resPayload = null;
		if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
			resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		}
		resPayload = ErrorCodeEnum.ERR_12007.setErrorCode(resPayload);
		fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
	}

}
