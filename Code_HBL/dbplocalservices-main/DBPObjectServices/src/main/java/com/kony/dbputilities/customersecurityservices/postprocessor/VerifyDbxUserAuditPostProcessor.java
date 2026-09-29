package com.kony.dbputilities.customersecurityservices.postprocessor;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.postprocessors.ObjectServicesConstants;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.kony.utils.TokenUtils;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class VerifyDbxUserAuditPostProcessor implements ObjectProcessorTask, ObjectServicesConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
			throws Exception {

		try {
			Log4j2Configurator.getInstance();
			PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
			PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
			JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
			JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

			String opstatus = com.kony.utilities.HelperMethods.getStringFromJsonObject(responsePayload,
					DBPConstants.FABRIC_OPSTATUS_KEY, true);
			String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
			String eventType = PARAM_LOGIN;
			String eventSubType = PARAM_CANT_LOGIN;
			String producer = "RBObjects/objects/DbxUser/VerifyDbxUser";
			String StatusId = PARAM_SID_EVENT_SUCCESS;
			String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters("ENABLE_EVENTS", requestManager);
			if (!opstatus.equals("0") || ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
				StatusId = PARAM_SID_EVENT_FAILURE;
			}

			String sessionid = null;
			String username = null;

			try {
				sessionid = requestManager.getServicesManager().getIdentityHandler().getSecurityAttributes()
						.get("session_token").toString();
			} catch (Exception e) {
				alert.prepareError("error while fetching session id in can't login.", e).log();
			}
			if (sessionid == null) {
				try {
					String authkey = requestManager.getHeadersHandler().getHeader("X-Kony-Authorization");
					TokenUtils tokenobj = new TokenUtils(authkey);
					customerid = tokenobj.getValue(URLConstants.PROVIDER_USER_ID);
					sessionid = tokenobj.getValue(URLConstants.SESSIONID);
				} catch (Exception e) {
					alert.prepareError("2. error while fetching session id in can't login", e).log();
				}
			}

			try {
				if (responsePayload.get("user_attributes") != null
						&& responsePayload.get("user_attributes").isJsonArray()) {
					JsonArray userAttrArray = responsePayload.get("user_attributes").getAsJsonArray();
					JsonObject obj = userAttrArray.get(0).getAsJsonObject();
					username = obj.get("UserName").getAsString();
					customerid = obj.get("id").getAsString();
				}
			} catch (Exception e) {
				alert.prepareError("error while fetching username and customerid in can't login.", e).log();
			}

			JsonObject customParams = new JsonObject();
			customParams.addProperty("DateOfBirth", requestPayload.get("DateOfBirth").getAsString());
			String ssn = requestPayload.get("Ssn").getAsString();
			ssn = "XXXXX" + ssn.substring(ssn.length() - 4);

			customParams.addProperty("Ssn", ssn);
			customParams.addProperty("LastName", requestPayload.get("LastName").getAsString());

			requestManager.getPayloadHandler().updatePayloadAsJson(customParams);

			if (sessionid != null) {
				customParams.addProperty("sessionId", sessionid);
			}
			if (username != null) {
				customParams.addProperty("user", username);
			}
			if (customerid != null) {
				customParams.addProperty("customerId", customerid);
			}

			diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

			if (enableEvents != null && enableEvents.equals("true")) {
				EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, StatusId,
						null, customerid, customParams);
			}

		} catch (Exception e) {
			alert.prepareError("Error occured in VerifyDbxUserPostProcessor2", e).log();
		}
		return true;

	}

}
