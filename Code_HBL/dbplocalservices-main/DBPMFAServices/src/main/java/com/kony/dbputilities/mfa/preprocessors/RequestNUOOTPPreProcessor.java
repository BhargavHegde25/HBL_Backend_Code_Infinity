package com.kony.dbputilities.mfa.preprocessors;

import java.util.Calendar;
import java.util.Date;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PreLoginUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.dataobject.Result;

public class RequestNUOOTPPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    PreLoginUtil mfaUtil = null;

    PayloadHandler requestPayloadHandler = null;
    PayloadHandler responsePayloadHandler = null;

    FabricRequestChain requestChain = null;

    JsonObject mfaAttributes = null;

    String userId = null;

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();

        JsonObject resultJson = new JsonObject();
        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());

        this.requestPayloadHandler = requestManager.getPayloadHandler();
        this.responsePayloadHandler = responseManager.getPayloadHandler();

        if (requestPayloadHandler.getPayloadAsJson() == null || requestPayloadHandler.getPayloadAsJson().isJsonNull()) {
            ErrorCodeEnum.ERR_10557.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

        String serviceKey = new String();
        String phone = new String();
        String securityKey = new String();
        String messageType = new String();

        if (requestpayload.has("Phone") && !requestpayload.get("Phone").isJsonNull()) {
            phone = requestpayload.get("Phone").getAsString();
        }

        if (StringUtils.isBlank(phone)) {
            ErrorCodeEnum.ERR_10555.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (requestpayload.has("messageType") && !requestpayload.get("messageType").isJsonNull()) {
            messageType = requestpayload.get("messageType").getAsString();
        }

        if (requestpayload.has(MFAConstants.MFA_ATTRIBUTES)
                && requestpayload.get(MFAConstants.MFA_ATTRIBUTES).isJsonObject()) {
            mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
            if (mfaAttributes.has("serviceKey")) {
                serviceKey = mfaAttributes.get("serviceKey").getAsString();
            }
            if (mfaAttributes.has("securityKey")) {
                securityKey = mfaAttributes.get("securityKey").getAsString();
            }
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Phone", phone);
        jsonObject.addProperty("messageType", messageType);
        jsonObject.addProperty("serviceKey", serviceKey);
        jsonObject.addProperty("securityKey", securityKey);

        if (!requestpayload.has(MFAConstants.MFA_ATTRIBUTES)) {
            requestPayloadHandler.updatePayloadAsJson(jsonObject);
            requestChain.execute();
            return;
        }

        if (StringUtils.isBlank(serviceKey)) {
            ErrorCodeEnum.ERR_10533.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (isServiceKeyExpired(requestManager, serviceKey)) {
            ErrorCodeEnum.ERR_10548.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        requestPayloadHandler.updatePayloadAsJson(jsonObject);
        requestChain.execute();

        return;
    }

    public boolean isServiceKeyExpired(FabricRequestManager requestManager, String serviceKey) {

        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey;
        Result result = HelperMethods.callGetApi(requestManager, filterQuery, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_GET);

        if (!HelperMethods.hasRecords(result) || HelperMethods.hasError(result)) {
            return false;
        }

        String string = HelperMethods.getFieldValue(result, "Createddts");

        if (StringUtils.isNotBlank(string)) {
            Date createdts = HelperMethods.getFormattedTimeStamp(string);
            Calendar generatedCal = Calendar.getInstance();
            generatedCal.setTime(createdts);

            Date verifyDate = new Date();
            Calendar verifyingCal = Calendar.getInstance();
            verifyingCal.setTime(verifyDate);

            int otpValidityTime = getServiceKeyExpiretime(requestManager);
            generatedCal.add(Calendar.MINUTE, otpValidityTime);

            long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
            long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

            if (GeneratedMilliSeconds > verifyingMilliSeconds) {
                return false;
            }
        }

        return true;
    }

    private int getServiceKeyExpiretime(FabricRequestManager requestManager) {
        try {
            return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, requestManager));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return 10;
    }

}