package com.kony.dbputilities.customersecurityservices.postprocessors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;

public class CreateBackendIdentifierPostProcessor implements ObjectServicePostProcessor {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        try {
            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

            String operationId = requestManager.getServicesManager().getOperationData().getOperationId();
            String backendId = getBackendId(requestManager, requestPayload);
            String infinityCustomerId = getInfinityCustomerId(responsePayload, operationId);

            if (StringUtils.isBlank(backendId) || StringUtils.isBlank(infinityCustomerId)) {
                return;
            }

            Map<String, String> inputParams = new HashMap<>();
            inputParams.put("id", UUID.randomUUID().toString());
            inputParams.put("Customer_id", infinityCustomerId);
            inputParams.put("sequenceNumber", "9");
            inputParams.put("BackendId", backendId);
            inputParams.put("BackendType", DBPUtilitiesConstants.CORE_BACKENDTYPE);
            inputParams.put("identifier_name", "customer_id");
            inputParams.put("isTypeBusiness", "1");
            final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                inputParams.put("BackendType", ServiceId.BACKEND_TYPE);
                inputParams.put("identifier_name", ServiceId.BACKEND_CUSTOMER_IDENTIFIER_NAME);
            }

            HelperMethods.callApi(requestManager, inputParams, HelperMethods.getHeaders(requestManager),
                    URLConstants.BACKENDIDENTIFIER_CREATE);

        } catch (Exception e) {
            alert.prepareError("Exception occured while creating the entry in backend identifier").log();
        }
    }

    private String getInfinityCustomerId(JsonObject responsePayload, String operationId) {
        String infinityCustomerId = "";

        try {
            if ("CreateSmallOrganizationEmployeeForAdmin".equalsIgnoreCase(operationId) && responsePayload.has("id")
                    && !responsePayload.get("id").isJsonNull()
                    && StringUtils.isNotBlank(responsePayload.get("id").getAsString())) {
                infinityCustomerId = responsePayload.get("id").getAsString();
            } else if (responsePayload.has("customerId")
                    && !responsePayload.get("customerId").isJsonNull()
                    && StringUtils.isNotBlank(responsePayload.get(DBPUtilitiesConstants.CUSTOMERID).getAsString())) {
                infinityCustomerId = responsePayload.get(DBPUtilitiesConstants.CUSTOMERID).getAsString();
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the infinity customer id").log();
        }
        return infinityCustomerId;
    }

    private String getBackendId(FabricRequestManager requestManager, JsonObject requestPayload) {
        String backendId = "";

        try {
            if (requestPayload.has(DBPUtilitiesConstants.BACKENDID)
                    && !requestPayload.get(DBPUtilitiesConstants.BACKENDID).isJsonNull()
                    && StringUtils.isNotBlank(requestPayload.get(DBPUtilitiesConstants.BACKENDID).getAsString())) {
                backendId = requestPayload.get(DBPUtilitiesConstants.BACKENDID).getAsString();
            } else if (requestPayload.has("serviceKey") && !requestPayload.get("serviceKey").isJsonNull()
                    && StringUtils.isNotBlank(requestPayload.get("serviceKey").getAsString())) {

                String decryptedPayload = CryptoText.decrypt(getDecryptedPayloadOnServiceKey(requestManager,
                        requestPayload.get("serviceKey").getAsString()));
                JsonObject serviceKeyPayload = new JsonParser().parse(decryptedPayload).getAsJsonObject();
                backendId = serviceKeyPayload.get(DBPUtilitiesConstants.BACKENDID).getAsString();
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the backend id").log();
        }
        return backendId;
    }

    private String getDecryptedPayloadOnServiceKey(FabricRequestManager requestManager, String servicekey) {
        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + servicekey;
        Result result = HelperMethods.callGetApi(requestManager, filterQuery, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_GET);
        return HelperMethods.getFieldValue(result, "payload");
    }
}
