package com.temenos.infinity.product.bulkpaymentservices.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.CorporateManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class BulkPaymentTemplatePOsByIdValidation implements ObjectProcessorTask {
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
            JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
            String templateId = HelperMethods.getStringFromJsonObject(requestPayload, "templateId");

            CorporateManager corporateManager = new CorporateManager(fabricRequestManager, fabricResponseManager);

            if (!corporateManager.validateBulkPaymentTemplateTitle(templateId)) {
                return updateErrorResult(fabricResponseManager);
            }
        }
            return true;
    }

    private boolean updateErrorResult(FabricResponseManager fabricResponseManager){
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_10149.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }
}