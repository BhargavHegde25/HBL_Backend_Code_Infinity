package com.kony.task.datavalidation;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.PayeeManager;
import com.kony.memorymgmt.TransactionManager;
import com.kony.scaintegration.helper.Helper;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class ValidateBulkWireFileId implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        if(!HelperMethods.isDACEnabled()) {
            diagnostic.prepareDebug("data access control is disabled").log();
            return true;
        }
        JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
            JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
            if(HelperMethods.isMFAVerify(requestPayload) || Helper.isScaVerify(requestPayload)) {
                diagnostic.prepareDebug("This is MFA verification call").log();
                return true;
            }
            
            String customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
            
            TransactionManager transactionManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
            String bulkWireFileID = HelperMethods.getStringFromJsonObject(requestPayload, "bulkWireFileID");
            
            if (StringUtils.isNotBlank(bulkWireFileID) ) {
                PayeeManager payeeManager = new PayeeManager(fabricRequestManager, fabricResponseManager);
                if (!payeeManager.validateBulkWireFileId(customerId, bulkWireFileID)) {
                    return updateErrorResult(fabricResponseManager);
                }
            }
            
        }
        return true;
    }
    
    private static boolean updateErrorResult(FabricResponseManager fabricResponseManager){
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }

}
