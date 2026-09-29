package com.temenos.infinity.api.savingspot.task.datavalidation;


import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.SavingsPotManager;
import com.kony.utilities.Constants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SavingsPotClosedValidationTask implements ObjectProcessorTask {

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
    throws Exception {
        JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        if (HelperMethods.isJsonNotNull(reqPayloadJEle)) {
            JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
            SavingsPotManager savingsPotManager = new SavingsPotManager(fabricRequestManager, fabricResponseManager);
            String potId = requestPayload.get(Constants.SAVINGSPOTID).toString();
            String savingsPotId =potId.replaceAll("\"", "");
            String customerId = HelperMethods.getAPIUserIdFromSession(fabricRequestManager).toString();
            if (!savingsPotManager.isSavingsPotClosed(customerId, savingsPotId)) {
                return true;
            } else {
            	JsonObject resPayload = null;
                resPayload = ErrorCodeEnum.ERR_20047.setErrorCode(resPayload);
                fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                return false;
            }
        }
        return true;
    }
}