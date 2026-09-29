package com.kony.dbputilities.customersecurityservices.postprocessor;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.postprocessors.ObjectServicesConstants;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;

public class SetOrgEmployeePasswordPostProcessor implements ObjectServicePostProcessor, ObjectServicesConstants {

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

            String eventType = PARAM_PROFILE_UPDATE;
            String eventSubType = PARAM_STATUS_ACTIVE;
            String statusId = PARAM_SID_EVENT_SUCCESS;
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters("ENABLE_EVENTS", requestManager);
            if (!opstatus.equals("0") || ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_FAILURE;
            }
            String sessionid = HelperMethods.getSessionId(requestManager);

            String consumerName = requestPayload.get("UserName").getAsString();
            String filterQuery = "UserName" + DBPUtilitiesConstants.EQUAL + "'"+consumerName+"'";
            Result result = com.kony.dbputilities.util.HelperMethods.callGetApi(requestManager, filterQuery,
                    com.kony.dbputilities.util.HelperMethods.getHeaders(requestManager), URLConstants.CUSTOMER_GET);

            String consumerId = com.kony.dbputilities.util.HelperMethods.getFieldValue(result, "id");

            JsonObject consumerCustomerParams = new JsonObject();
            consumerCustomerParams.addProperty("statusId", DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE);
            consumerCustomerParams.addProperty("sessionId", sessionid);
            consumerCustomerParams.addProperty("UserName", consumerName);
            consumerCustomerParams.addProperty("id", consumerId);

            diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

            if (enableEvents != null && enableEvents.equals("true")) {
                EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer, statusId,
                        null, consumerId, consumerCustomerParams);
            }

            String isUserNameSetUpEnabled = JSONUtil.getString(responsePayload, "isUserNameSetUpEnabled");
            if (StringUtils.isNotBlank(isUserNameSetUpEnabled) && "true".equalsIgnoreCase(isUserNameSetUpEnabled)) {
                eventType = PARAM_USER;
                eventSubType = PARAM_USER_CREATE;
                Map<String, String> input = new HashMap<>();
                input.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + consumerId);
                JsonObject response = (com.kony.dbputilities.util.HelperMethods.callApiJson(requestManager, input,
                        com.kony.dbputilities.util.HelperMethods.getHeaders(requestManager),
                        URLConstants.CUSTOMERGROUPINFOVIEW_GET)).get("customergroupinfo_view").getAsJsonArray().get(0)
                                .getAsJsonObject();
                input.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + consumerId);
                JsonObject customerCommunciation =
                        (com.kony.dbputilities.util.HelperMethods.callApiJson(requestManager, input,
                                com.kony.dbputilities.util.HelperMethods.getHeaders(requestManager),
                                URLConstants.CUSTOMERCOMMUNICATIONVIEW_GET)).get("customercommunicationview")
                                        .getAsJsonArray().get(0).getAsJsonObject();
                consumerCustomerParams.addProperty("roleId", JSONUtil.getString(response, "Group_id"));
                consumerCustomerParams.addProperty("roleName", JSONUtil.getString(response, "Group_name"));
                consumerCustomerParams.addProperty("firstName", JSONUtil.getString(customerCommunciation, "FirstName"));
                consumerCustomerParams.addProperty("lastName", JSONUtil.getString(customerCommunciation, "LastName"));
                consumerCustomerParams.addProperty("SSN", JSONUtil.getString(customerCommunciation, "Ssn"));
                consumerCustomerParams.addProperty("dateOfBirth",
                        JSONUtil.getString(customerCommunciation, "DateOfBirth"));
                if (enableEvents != null && enableEvents.equals("true")) {
                    EventsDispatcher.dispatch(requestManager, responseManager, eventType, eventSubType, producer,
                            statusId,
                            null, consumerId, consumerCustomerParams);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while auditing the log for customer status update" + e.getMessage()).log();
        }
    }

}
