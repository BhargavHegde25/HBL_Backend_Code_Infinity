package com.temenos.dbx.product.commonsutils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class AuditHelperMethods implements Callable<CallableUtils> {

	/**
	 * 
	 * @param key
	 * @param requestInstance
	 * @return
	 */
	DataControllerRequest requestManager;
	DataControllerResponse responseManager = null;
	String eventType;
	String eventSubType;
	String producer;
	String statusId;
	String account;
	String customerId;
	JsonObject customParams;
	JsonObject responsePayload;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public AuditHelperMethods(DataControllerRequest requestManager, DataControllerResponse responseManager,
            String eventType, String eventSubType, String producer, String statusId, String account, String customerId,
            JsonObject customParams) {

        this.requestManager = requestManager;
        this.responseManager = responseManager;
        this.eventType = eventType;
        this.eventSubType = eventSubType;
        this.producer = producer;
        this.statusId = statusId;
        this.account = account;
        this.customerId = customerId;
        this.customParams = customParams;
    }

	public AuditHelperMethods(DataControllerRequest requestManager, JsonObject responsePayload, String eventType,
            String eventSubType, String producer, String statusId, String account, String customerId,
            JsonObject customParams) {

        this.requestManager = requestManager;
        this.responsePayload = responsePayload;
        this.eventType = eventType;
        this.eventSubType = eventSubType;
        this.producer = producer;
        this.statusId = statusId;
        this.account = account;
        this.customerId = customerId;
        this.customParams = customParams;
    }

	public static String getConfigurableParameters(String key, DataControllerRequest requestInstance) {
		try {
			ServicesManager serviceManager = requestInstance.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			return configurableParametersHelper.getServerProperty(key);
		} catch (Exception are) {
			alert.prepareError("Error occured while fetching environment configuration", are).log();
		}

		return "";
	}

	// Code to check whether a json has given key or not
	public static boolean hasKey(JsonObject object, String key) {
		return object.has(key);
	}

	public static void execute(AuditHelperMethods call) throws InterruptedException, ExecutionException {
		try {
			ThreadExecutor.getExecutor(call.requestManager).execute(call);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public CallableUtils call() throws Exception {

		CallableUtils cUtil = new CallableUtils(this.responseManager, this.requestManager);
		EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, statusId,
				account, customerId, customParams);
//		if (responseManager != null) {
//			EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, statusId,
//					null, customerId, customParams);
//		} else
//			EventsDispatcher.dispatch(requestManager, responsePayload, eventType, eventSubType, producer, statusId,
//					account, customerId, customParams);

		return cUtil;
	}

}
