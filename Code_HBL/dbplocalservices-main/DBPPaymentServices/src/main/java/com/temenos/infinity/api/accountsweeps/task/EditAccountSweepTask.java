package com.temenos.infinity.api.accountsweeps.task;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author naveen.yerra
 */
public class EditAccountSweepTask implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        try {
            JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
            if(response.has("serviceRequestId")) {
                response.addProperty("isSweepCreated","true");
                response.addProperty("isEdit",true);
                fabricResponseManager.getPayloadHandler().updatePayloadAsJson(response);
            }
        } catch (Exception e) {
            alert.prepareError("Exception while updating account sweep information into customer account", e).log();
            return false;
        }
        return true;
    }

}