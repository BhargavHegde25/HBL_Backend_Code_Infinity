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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author naveen.yerra
 */
public class ReceivedGuaranteeClaimValidation implements ObjectProcessorTask {
    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        try {
            String documentId;
            String documentName;
            JsonObject requestPayload = fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
            if (requestPayload.has("documentInformation")) {
                JSONArray documentInformation = new JSONArray(requestPayload.get("documentInformation").getAsString());
                CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);
                for (int i = 0; i < documentInformation.length(); i++) {
                    JSONObject document = documentInformation.getJSONObject(i);
                    documentId = document.getString("documentReference");
                    documentName = document.getString("documentName");
                    if (!corporateManager.validateDocument(documentId, documentName)) {
                        return updateErrorResult(fabricResponseManager);
                    }
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