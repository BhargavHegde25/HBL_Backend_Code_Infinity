/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.preprocessor.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.CorporateManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

public class GuaranteeLinkedPayeeValidation implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        if (!HelperMethods.isDACEnabled()) {
            diagnostic.prepareDebug("data access control is disabled").log();
            return true;
        }
        JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
            JSONObject requestPayload = new JSONObject(reqPayloadJEle.getAsJsonObject().toString());
            String previousName = requestPayload.has("previousName") ? (String) requestPayload.get("previousName") : (String) requestPayload.get("name");
            if (!requestPayload.has("id") && previousName == null) {
                return updateUnauthorizedErrorResult(fabricResponseManager);
            }
            CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);
            if (!corporateManager.validateEditBeneficiary(previousName))
                return updateErrorResult(fabricResponseManager, previousName);
        }
        return true;
    }

    private static boolean updateErrorResult(FabricResponseManager fabricResponseManager, String previousName) {
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_29035.setErrorCode(resPayload);
        resPayload.addProperty("Message",
                "Sorry, the beneficiary ‘" + previousName + "’ is using by other GT & SBLC, we can’t edit this from here. Note: You can remove this and add a new beneficiary.");
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }

    private static boolean updateUnauthorizedErrorResult(FabricResponseManager fabricResponseManager) {
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_10149.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }

}