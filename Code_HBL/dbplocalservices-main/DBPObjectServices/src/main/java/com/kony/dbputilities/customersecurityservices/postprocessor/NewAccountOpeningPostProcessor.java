package com.kony.dbputilities.customersecurityservices.postprocessor;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.postprocessors.ObjectServicesConstants;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class NewAccountOpeningPostProcessor implements ObjectServicePostProcessor, ObjectServicesConstants {

    LoggerUtil logger = new LoggerUtil(EnrollOrganizationPostProcessor.class);
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();
        try {

            PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
            JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            requestPayload.addProperty("isOnBoradingFlow", "false");
            requestPayloadHandler.updatePayloadAsJson(requestPayload);
            JsonArray customerAccounts =
                    new JsonParser().parse(JSONUtil.getString(responsePayload, "customerAccounts")).getAsJsonArray();
            createCustomerAccounts(requestManager, responseManager, customerAccounts);
            if (JSONUtil.hasKey(responsePayload, "customerAccounts"))
                responsePayload.remove("customerAccounts");
            responsePayloadHandler.updatePayloadAsJson(responsePayload);
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating the accounts").log();
        }
    }

    private void createCustomerAccounts(FabricRequestManager requestManager, FabricResponseManager responseManager,
            JsonArray productsJsonArray) {
        Map<String, String> retailContractMap = new HashMap<>();
        retailContractMap.put("customerId", HelperMethods.getCustomerIdFromSession(requestManager));
        JsonArray accounts = new JsonArray();
        for (int i = 0; i < productsJsonArray.size(); i++) {
            JsonObject temp = new JsonObject();
            temp = productsJsonArray.get(i).getAsJsonObject();
            JsonObject account = new JsonObject();
            account.addProperty("accountName",
                    JSONUtil.getString(temp, "productName"));
            account.addProperty("accountId",
                    JSONUtil.getString(temp, "accountId"));
            account.addProperty("isEnabled", "true");
            account.addProperty("typeId", JSONUtil.getString(temp, "productTypeId"));
            account.addProperty("ownerType",
                    JSONUtil.getString(temp, "Owner"));
            account.addProperty("accountStatus",
                    JSONUtil.getString(temp, "Active"));
            accounts.add(account);
        }
        retailContractMap.put("accounts", accounts.toString());
        retailContractMap.put("isOnBoradingFlow", "false");

        try {
            HelperMethods.callApi(requestManager, retailContractMap, HelperMethods.getHeaders(requestManager),
                    URLConstants.CREATE_RETAIL_CONTRACT);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}