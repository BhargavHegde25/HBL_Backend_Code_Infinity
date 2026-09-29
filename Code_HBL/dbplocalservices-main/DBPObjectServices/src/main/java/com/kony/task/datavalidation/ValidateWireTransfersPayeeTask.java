package com.kony.task.datavalidation;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.PayeeManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class ValidateWireTransfersPayeeTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        try {
            JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
                JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
                String  payeeId = HelperMethods.getStringFromJsonObject(requestPayload, "payeeId");
                String  customerId =HelperMethods.getCustomerIdFromSession(fabricRequestManager);
                PayeeManager payeeManager = new PayeeManager(fabricRequestManager, fabricResponseManager);
                if(StringUtils.isNotBlank(payeeId) && !payeeManager.validateWireTransferPayeeId(customerId, payeeId)){
                    JsonObject resPayload = null;
                    resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
                    fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                    return false;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Error while validating wire transfer Payees", e).log();
        }
        return true;
    }

}