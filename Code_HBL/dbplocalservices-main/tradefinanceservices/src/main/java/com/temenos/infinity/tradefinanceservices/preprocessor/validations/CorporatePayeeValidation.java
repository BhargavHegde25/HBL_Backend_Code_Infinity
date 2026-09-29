/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.preprocessor.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.AccountsManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CorporatePayeeValidation implements ObjectProcessorTask {
    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        try {
            AccountsManager accManager = new AccountsManager(fabricRequestManager);
            JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
            SessionMap extsAccntStore = accManager.getExternalBankAccountsFromSession("");
            requestPayload.addProperty("ExternalPayees", extsAccntStore.toString());
            fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
        } catch (Exception e) {
            updateErrorCode(fabricResponseManager);
            return false;
        }
        return true;
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
