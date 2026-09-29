/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.preprocessor.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import org.json.JSONObject;

public class GuaranteesValidation implements ObjectProcessorTask {
    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        try {
            JsonObject requestPayload = fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
            if (requestPayload.has("limitInstructions")) {
                JSONObject limitInstructions = new JSONObject(requestPayload.get("limitInstructions").getAsString());
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);
                if (!corporateManager.validateLimitInstructions(limitInstructions)) {
                    return updateErrorResult(fabricResponseManager);
                }
            }
        } catch (Exception e) {
            updateErrorResult(fabricResponseManager);
            return false;
        }
        return true;
    }

    private boolean updateErrorResult(FabricResponseManager fabricResponseManager) {
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_10149.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }
}