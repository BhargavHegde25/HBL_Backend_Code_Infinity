package com.kony.AdminConsole.PreAndPostProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.AdminConsole.Utilities.CryptoText;
import com.kony.AdminConsole.Utilities.ServiceCallHelper;
import com.kony.AdminConsole.Utilities.URLConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.utils.HttpCallException;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.konylabs.middleware.dataobject.Result;

public class GetSecurityQuestionsPostProcessor implements ObjectServicePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {

        JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

        if (jsonObject.has(DBPConstants.DBP_ERROR_CODE_KEY) || jsonObject.has(DBPConstants.DBP_ERROR_MESSAGE_KEY)) {
            responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
            return;
        }

        String serviceKey = generateServiceKey(requestManager);

        if (StringUtils.equals(null, serviceKey)) {
            JsonObject response = new JsonObject();
            ErrorCodeEnum.ERR_10701.setErrorCode(response);
            responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
            return;
        }
        jsonObject.addProperty("serviceKey", serviceKey);

        responseManager.getPayloadHandler().updatePayloadAsJson(jsonObject);
        return;
    }

    private String generateServiceKey(FabricRequestManager requestManager) throws HttpCallException {

        Map<String, Object> inputParams = new HashMap<>();

        String serviceKey = HelperMethods.getNewId();
        String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        JsonObject payload = new JsonObject();
        String data = payload.toString();
        try {
            data = CryptoText.encrypt(data);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }
        String Createddts = HelperMethods.getCurrentTimeStamp();

        inputParams.put("serviceKey", serviceKey);
        inputParams.put("serviceName", serviceName);
        inputParams.put("Createddts", Createddts);

        Result result = null;
        result = ServiceCallHelper.invokeServiceAndGetResult(requestManager, inputParams,
                HelperMethods.getHeaders(requestManager), URLConstants.MFA_SERVICE_CREATE);
        if (HelperMethods.hasRecords(result)) {
            return serviceKey;
        }
        return null;
    }
}
