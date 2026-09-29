package com.kony.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class CreateBulkWireTemplateObjectServicePostProcessor
		implements ObjectServicePostProcessor, ObjectServicesConstants, ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		try {

			PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
			JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
			JsonObject customParams = new JsonObject();
			String opstatus = "";
			String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
			String eventType = PARAM_BULKWIRE_TEMPLATE;
			String eventSubType = PARAM_CREATE_BULKWIRE_TEMPLATE;
			String producer = "BulkWireObjects/operations/BulkWire/CreateBulkWireTemplate";
			String statusId = PARAM_SID_EVENT_FAILURE;
			String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
					requestManager);

			if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
				opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
			}

			if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
				statusId = PARAM_SID_EVENT_SUCCESS;
			}

			diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

			if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {
				try {
					ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
							eventType, eventSubType, producer, statusId, null, customerid, customParams));
				} catch (Exception ex) {
					alert.prepareError("Exception Occured while invoking objectServiceHelperMethods", ex).log();
				}
			}

		} catch (Exception ex) {
			alert.prepareError("Exception occured in ObjectService PostProcessor while Creating BULKWIRE TEMPLATE=", ex).log();
		}

	}

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			execute(fabricRequestManager, fabricResponseManager);
		} catch (Exception e) {
			alert.prepareError("exception occured in execute method of objectservice", e).log();

		}
		return true;
	}

}
