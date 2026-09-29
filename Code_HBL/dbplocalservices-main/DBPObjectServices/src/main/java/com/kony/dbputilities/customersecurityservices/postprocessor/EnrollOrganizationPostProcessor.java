package com.kony.dbputilities.customersecurityservices.postprocessor;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.postprocessors.ObjectServicesConstants;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class EnrollOrganizationPostProcessor implements ObjectServicePostProcessor, ObjectServicesConstants {

    LoggerUtil logger = new LoggerUtil(EnrollOrganizationPostProcessor.class);
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

            if (StringUtils.isNotBlank(JSONUtil.getString(responsePayload, DBPUtilitiesConstants.CUSTOMERID))) {
                String filterQuery =
                        "serviceKey" + DBPUtilitiesConstants.EQUAL + JSONUtil.getString(requestPayload, "serviceKey");
                Map<String, String> inputParams = new HashMap<>();
                inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
                HelperMethods.callApi(requestManager, inputParams, HelperMethods.getHeaders(requestManager),
                        URLConstants.MFA_SERVICE_DELETE);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while deleting the serviceKey").log();
        }
    }
}
