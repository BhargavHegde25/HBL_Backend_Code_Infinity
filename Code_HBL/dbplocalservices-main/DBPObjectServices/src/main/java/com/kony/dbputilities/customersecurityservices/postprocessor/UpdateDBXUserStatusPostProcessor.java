package com.kony.dbputilities.customersecurityservices.postprocessor;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.postprocessors.ObjectServicesConstants;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;
import com.kony.utilities.HelperMethods;

public class UpdateDBXUserStatusPostProcessor implements ObjectServicePostProcessor, ObjectServicesConstants {

	LoggerUtil logger = new LoggerUtil(UpdateDBXUserStatusPostProcessor.class);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		try {
			PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
			PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
			JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
			JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

			String opstatus = com.kony.utilities.HelperMethods.getStringFromJsonObject(responsePayload,
					DBPConstants.FABRIC_OPSTATUS_KEY, true);

			ServicesManager servicesManager = requestManager.getServicesManager();
			OperationData op = servicesManager.getOperationData();
			String producer = op.getServiceId() + "/operations/" + op.getObjectId() + "/" + op.getOperationId();

			Map<String, String> userDetails = com.kony.dbputilities.util.HelperMethods
					.getCustomerFromIdentityService(requestManager);
			boolean isSuperAdmin = Boolean.parseBoolean(userDetails.get("isC360Admin"));
			String producerId = null;
			String producerName = null;
			//String eventType = PARAM_PROFILE_UPDATE;
			String eventType = PARAM_LOGIN;
			String eventSubType = null;
			String statusId = PARAM_SID_EVENT_SUCCESS;
			String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters("ENABLE_EVENTS", requestManager);
			if (!opstatus.equals("0") || ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
				statusId = PARAM_SID_EVENT_FAILURE;
			}
			String sessionid = HelperMethods.getSessionId(requestManager);

			String statusChange = requestPayload.get("Status").getAsString();
			String consumerName = requestPayload.get("UserName").getAsString();
			String filterQuery = "UserName" + DBPUtilitiesConstants.EQUAL + "'"+consumerName +"'";
			Result result = com.kony.dbputilities.util.HelperMethods.callGetApi(requestManager, filterQuery,
					com.kony.dbputilities.util.HelperMethods.getHeaders(requestManager), URLConstants.CUSTOMER_GET);

			String consumerId = com.kony.dbputilities.util.HelperMethods.getFieldValue(result, "id");

			if (!isSuperAdmin) {
				producerName = userDetails.get("UserName");
				producerId = userDetails.get("customer_id");
			} else {
				producerName = "ADMIN";
			}
			diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

			if (enableEvents != null && enableEvents.equalsIgnoreCase("true")) {
				if (!isSuperAdmin) {
					eventSubType = getEventSubType(statusChange, true);
					JsonObject producerCustomerParams = new JsonObject();
					producerCustomerParams.addProperty("modifiedId", consumerId);
					producerCustomerParams.addProperty("modifiedName", consumerName);
					producerCustomerParams.addProperty("modifiedStatus", statusChange);
					producerCustomerParams.addProperty("sessionId", sessionid);
					EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer,
							statusId, null, producerId, producerCustomerParams);
				}
				else
				{
				eventType = PARAM_LOGIN_ADMIN;
				eventSubType = getEventSubType(statusChange, false);
				JsonObject consumerCustomerParams = new JsonObject();
				if (StringUtils.isNotBlank(producerId))
					consumerCustomerParams.addProperty("modifiedById", producerId);
				consumerCustomerParams.addProperty("modifiedByName", producerName);
				consumerCustomerParams.addProperty("modifiedStatus", statusChange);
				consumerCustomerParams.addProperty("sessionId", sessionid);
				
				EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, statusId,
						null, consumerId, consumerCustomerParams);
			}
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while updating user status auditing:" + e.getStackTrace()).log();
		}
	}

	private String getEventSubType(String statusChange, boolean isProducerEventGenerator) {

		switch (statusChange) {
		case "SUSPENDED":
			if (isProducerEventGenerator)
				return PARAM_ACCOUNT_SUSPENDED;
			else
			return PARAM_ACCOUNT_SUSPENDED_ADMIN;
		case "ACTIVE":
			if (isProducerEventGenerator)
				return PARAM_BO_STATUS_ACTIVE;
			else
				return PARAM_ACCOUNT_ACTIVATED_ADMIN;
		}
		return null;
	}

}
