package com.kony.dbputilities.mfa.preprocessors;

import java.util.ArrayList;
import java.util.Arrays;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PreLoginUtil;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.dataobject.Result;

public class RequestResetPasswordOTPPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();

        PreLoginUtil mfaUtil = null;

        PayloadHandler requestPayloadHandler = null;
        PayloadHandler responsePayloadHandler = null;

        JsonObject mfaAttributes = null;

        StringBuilder serviceKeycreatedTime = new StringBuilder();

        String userId = null;
        JsonObject resultJson = new JsonObject();
        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());

        requestPayloadHandler = requestManager.getPayloadHandler();
        responsePayloadHandler = responseManager.getPayloadHandler();

        if (requestPayloadHandler.getPayloadAsJson() == null || requestPayloadHandler.getPayloadAsJson().isJsonNull()) {
            ErrorCodeEnum.ERR_10532.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

        boolean isMFAPresent = false;

        try {

            JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);
            if (mfaElement.isJsonObject()) {
                mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
            } else {
                JsonParser parser = new JsonParser();
                mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
            }
            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, MFAConstants.SERVICE_ID_PRELOGIN);
            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
            requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            isMFAPresent = true;

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (!isMFAPresent) {
            ErrorCodeEnum.ERR_10532.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }
        String serviceKey = null;
        String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        String userName = null;

        if (JSONUtil.hasKey(mfaAttributes, MFAConstants.SERVICE_KEY)) {
            serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
        }
        if (JSONUtil.hasKey(requestpayload, MFAConstants.USERNAME)) {
            userName = requestpayload.get(MFAConstants.USERNAME).getAsString();
        }

        if ((StringUtils.isBlank(serviceKey) && !serviceName.equals(MFAConstants.SERVICE_ID_PRELOGIN))) {
            ErrorCodeEnum.ERR_10533.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (StringUtils.isBlank(userName)) {
            ErrorCodeEnum.ERR_10532.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        boolean isUserValid = isValidUserName(requestManager, userName, serviceKey, serviceKeycreatedTime);

        mfaUtil = new PreLoginUtil(requestManager, userName, serviceKey, serviceKeycreatedTime);

        if (!(mfaUtil.isAppActionValid() && mfaUtil.isValidMFA())) {
            ErrorCodeEnum.ERR_10535.setErrorCode(resultJson, mfaUtil.getDbpErrMsg());
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if ((StringUtils.isBlank(userName) || !isUserValid)) {
            ErrorCodeEnum.ERR_10538.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        if (mfaUtil.isServiceKeyExpired()) {
            ErrorCodeEnum.ERR_10533.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return;
        }

        String mfaType = mfaUtil.getMFAType();
        JsonObject mfajson = null;

        if (MFAConstants.SECURE_ACCESS_CODE.equals(mfaType)) {
            if (mfaAttributes.has("OTP") && mfaAttributes.get("OTP").isJsonObject()) {
                userId = getUserId(userName, requestManager);
                mfaUtil.setUserID(userId);
                if (StringUtils.isNotBlank(userId) && mfaUtil.isValidServiceKey()) {
                    JsonObject referenceIdMappings = mfaUtil.getReferenceIdMappingsFromDB(serviceKey);
                    if (isValidPayload(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, mfaUtil,
                            requestPayloadHandler, referenceIdMappings)) {
                        requestChain.execute();
                        return;
                    } else {
                        ErrorCodeEnum.ERR_10537.setErrorCode(resultJson);
                        responsePayloadHandler.updatePayloadAsJson(resultJson);
                        return;
                    }
                }
            } else {
                if (mfaUtil.isValidMFA() && !mfaAttributes.has("OTP")) {
                    if (!mfaUtil.updateUserIdStatus()) {
                        ErrorCodeEnum.ERR_10536.setErrorCode(resultJson);
                        responsePayloadHandler.updatePayloadAsJson(resultJson);
                        return;
                    }
                    mfajson = mfaUtil.setserviceMFAAttributes();
                    responsePayloadHandler.updatePayloadAsJson(mfajson);
                    return;
                }
            }
        }

        ErrorCodeEnum.ERR_10532.setErrorCode(resultJson);
        responsePayloadHandler.updatePayloadAsJson(resultJson);
    }

    private String getUserId(String userName, FabricRequestManager requestManager) {

        String filterQuery = "UserName" + DBPUtilitiesConstants.EQUAL + userName;

        Result result = HelperMethods.callGetApi(requestManager, filterQuery, HelperMethods.getHeaders(requestManager),
                URLConstants.CUSTOMERVERIFY_GET);
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "id");
        }

        return null;
    }

    private boolean isValidUserName(FabricRequestManager requestManager, String userName, String serviceKey,
            StringBuilder serviceKeycreatedTime) {

        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + "serviceName" + DBPUtilitiesConstants.EQUAL + MFAConstants.SERVICE_ID_PRELOGIN;

        Result mfaserviceresult = HelperMethods.callGetApi(requestManager, filterQuery,
                HelperMethods.getHeaders(requestManager), URLConstants.MFA_SERVICE_GET);

        if (!HelperMethods.hasRecords(mfaserviceresult)) {
            return false;
        }

        String payload = HelperMethods.getFieldValue(mfaserviceresult, "payload");
        String createdts = HelperMethods.getFieldValue(mfaserviceresult, "Createddts");

        serviceKeycreatedTime = serviceKeycreatedTime.append(createdts);

        try {
            if (StringUtils.isNotBlank(payload)) {
                payload = CryptoText.decrypt(payload);
            } else {
                return false;
            }

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return false;
        }

        String[] values = payload.split(",");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(values));
        int index = list.indexOf(userName);
        if (index == -1)
            return false;
        String userAttributesArray;
        try {
            userAttributesArray =
                    CryptoText.decrypt(HelperMethods.getFieldValue(mfaserviceresult, "securityQuestions"));
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing securityQuestions").log();
            return false;
        }
        JsonArray jsonArray = new JsonParser().parse(userAttributesArray).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            if (index > 0) {
                index--;
                continue;
            }
            JsonObject json = jsonElement.getAsJsonObject();
            if (userName.equalsIgnoreCase(json.get("UserName").getAsString())) {
                Result customerGet =
                        HelperMethods.callGetApi(requestManager, "UserName" + DBPUtilitiesConstants.EQUAL + userName,
                                HelperMethods.getHeaders(requestManager), URLConstants.CUSTOMERVERIFY_GET);
                String customerType = HelperMethods.getFieldValue(customerGet, "CustomerType_id");
                if ("TYPE_ID_PROSPECT".equalsIgnoreCase(customerType)) {
                    return true;
                } else {
                    return DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE
                            .equals(json.get("Status_id").getAsString());
                }
            }
        }
        return false;

    }

    private boolean isValidPayload(JsonObject OTP, String serviceKey, String serviceName, PreLoginUtil mfaUtil,
            PayloadHandler requestPayloadHandler, JsonObject referenceIdMappings) {

        if (mfaUtil.isValidPhone(OTP, false, referenceIdMappings)
                && mfaUtil.isValidEmail(OTP, false, referenceIdMappings)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
            if (OTP.has(MFAConstants.PHONE)) {
                jsonObject.add("Phone", referenceIdMappings.get(OTP.get("phone").getAsString()));
            }
            if (OTP.has(MFAConstants.EMAIL)) {
                jsonObject.add("Email", referenceIdMappings.get(OTP.get(MFAConstants.EMAIL).getAsString()));
            }

            jsonObject.addProperty(MFAConstants.SERVICE_NAME, serviceName);

            if (OTP.has(MFAConstants.SECURITY_KEY)) {
                jsonObject.addProperty(MFAConstants.SECURITY_KEY, OTP.get(MFAConstants.SECURITY_KEY).getAsString());
            }

            requestPayloadHandler.updatePayloadAsJson(jsonObject);
            return true;
        }

        return false;
    }

}