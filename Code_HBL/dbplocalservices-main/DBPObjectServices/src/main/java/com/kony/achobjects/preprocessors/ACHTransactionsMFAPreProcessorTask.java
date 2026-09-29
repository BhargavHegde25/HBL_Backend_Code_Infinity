package com.kony.achobjects.preprocessors;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.dataobject.Result;

public class ACHTransactionsMFAPreProcessorTask implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
            throws Exception {
        Log4j2Configurator.getInstance();
        diagnostic.prepareDebug("Entered into ACHTransactionsMFAPreProcessorTask").log();

        PostLoginMFAUtil mfaUtil = null;
        JsonObject resultJson = new JsonObject();
        JsonObject mfaAttributes = new JsonObject();
        String serviceKey = null;
        String serviceName = null;

        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());
        PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
        PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
        if (requestPayloadHandler.getPayloadAsJson() == null) {
            ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

        String loggedInUserID = HelperMethods.getCustomerIdFromSession(requestManager);

        try {
            JsonElement mfaElement = requestpayload.get(MFAConstants.MFA_ATTRIBUTES);

            if (mfaElement != null) {
                if (mfaElement.isJsonObject()) {
                    mfaAttributes = requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
                } else {
                    JsonParser parser = new JsonParser();
                    mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
                }
                requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
                requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            }

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        boolean isRequestOriginal = isRequestOriginal(requestpayload, requestManager, responsePayloadHandler);
        boolean isRequestForMFA = isRequestForMFA(requestpayload, mfaAttributes);

        diagnostic.prepareDebug("IsRequestOriginal / isRequestForMFA : " + isRequestOriginal + " / " + isRequestForMFA).log();

        if (isRequestOriginal) {
            requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
            String achRequestType = requestpayload.get(MFAConstants.ACH_TYPE_IDENTIFIER).getAsString();

            if (achRequestType.equals(MFAConstants.ACH_TYPE_IDENTIFIER_VALUE_PAYMENT)) {
                serviceName = MFAConstants.ACH_SERVICE_ID_PAYMENT;

            } else if (achRequestType.equals(MFAConstants.ACH_TYPE_IDENTIFIER_VALUE_COLLECTION)) {
                serviceName = MFAConstants.ACH_SERVICE_ID_COLLECTION;
            }

            diagnostic.prepareDebug("Service Name to fetch MFA Scenario : " + serviceName).log();

            double transactionAmount = getTransactionAmount(requestpayload);
            if (transactionAmount == -0.01) {
                ErrorCodeEnum.ERR_13007.setErrorCode(resultJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
            mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
            requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
            requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
            requestpayload.addProperty(MFAConstants.ACH_TRANSACTION_AMOUNT, transactionAmount);
            diagnostic.prepareDebug("1. Request Payload: " + requestpayload).log();
            requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
        }

        if (isRequestForMFA) {
            serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            Result exisingresult = getMFAServiceRecord(serviceKey, requestManager);
            if (!HelperMethods.hasRecords(exisingresult)) {
                ErrorCodeEnum.ERR_13000.setErrorCode(resultJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            } else if (loggedInUserID.equals(HelperMethods.getFieldValue(exisingresult, MFAConstants.MFA_USER_ID))) {
                serviceName = HelperMethods.getFieldValue(exisingresult, MFAConstants.SERVICE_NAME);
                mfaAttributes.addProperty(MFAConstants.SERVICE_NAME, serviceName);
                requestpayload.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
                requestpayload.addProperty(MFAConstants.SERVICE_NAME, serviceName);
                requestManager.getPayloadHandler().updatePayloadAsJson(requestpayload);
            } else {
                ErrorCodeEnum.ERR_13003.setErrorCode(resultJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }

        }

        if (!(isRequestOriginal ^ isRequestForMFA)) {
            ErrorCodeEnum.ERR_10500.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        mfaUtil = new PostLoginMFAUtil(requestManager, loggedInUserID);

        if (StringUtils.isBlank(serviceKey)) {
            diagnostic.prepareDebug("---- ACH ---- New request.").log();
            mfaUtil.getMfaModeforRequest();
        } else {
            diagnostic.prepareDebug("---- ACH ---- OLD request.").log();
            mfaUtil.getMFaModeforRequestfromDB(serviceKey);
        }

        if (!mfaUtil.isAppActionValid()) {
            ErrorCodeEnum.ERR_10501.setErrorCode(resultJson, mfaUtil.getDbpErrMsg());
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        if ((mfaUtil.mfaConfigurationUtil == null) || (!mfaUtil.isMFARequired(requestpayload)) && StringUtils.isBlank(serviceKey)) {
            return true;
        }

        if (StringUtils.isBlank(serviceKey)) {
            resultJson = mfaUtil.setserviceMFAAttributes();
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }
        if (!mfaUtil.isValidServiceKey(serviceKey, serviceName)) {
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }
        String MFAtype = mfaUtil.getMFAType();

        if (MFAtype.equals(MFAConstants.SECURE_ACCESS_CODE) && mfaAttributes.has("OTP")
                && mfaAttributes.get("OTP").isJsonObject()) {
            return handleOTP(mfaAttributes.get("OTP").getAsJsonObject(), serviceKey, serviceName, requestPayloadHandler,
                    responsePayloadHandler, mfaUtil);
        }
        if (MFAtype.equals(MFAConstants.SECURITY_QUESTIONS) && mfaAttributes.has(MFAConstants.SECURITY_QUESTIONS_DB)
                && mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).isJsonArray()) {
            return handleSecurityQuestions(mfaAttributes.get(MFAConstants.SECURITY_QUESTIONS_DB).getAsJsonArray(),
                    serviceKey,
                    serviceName, requestPayloadHandler, responsePayloadHandler, mfaUtil);
        }
        ErrorCodeEnum.ERR_13001.setErrorCode(resultJson);
        responsePayloadHandler.updatePayloadAsJson(resultJson);
        return false;
    }

    private boolean handleSecurityQuestions(JsonArray jsonArray, String serviceKey, String serviceName,
            PayloadHandler requestPayloadHandler, PayloadHandler responsePayloadHandler, PostLoginMFAUtil mfaUtil) {
        JsonObject resultJson = new JsonObject();

        resultJson = mfaUtil.verifySecurityQuestions(jsonArray);
        if (!resultJson.has("success")) {
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        return true;
    }

    private boolean handleOTP(JsonObject OTP, String serviceKey, String serviceName,
            PayloadHandler requestPayloadHandler,
            PayloadHandler responsePayloadHandler, PostLoginMFAUtil mfaUtil) {
        JsonObject resultJson = new JsonObject();
        if (!OTP.has(MFAConstants.OTP_OTP)) {
            resultJson = mfaUtil.requestOTP(OTP, resultJson, serviceKey, true);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        } else {
            resultJson = mfaUtil.verifyOTP(OTP);
            if (!resultJson.has("success")) {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
        }

        JsonObject requestJson = mfaUtil.getRequestPayload(serviceKey, serviceName);
        mfaUtil.removeServiceKey(serviceKey);
        requestPayloadHandler.updatePayloadAsJson(requestJson);
        return true;
    }

    private boolean isRequestOriginal(JsonObject requestPayload, FabricRequestManager requestManager,
            PayloadHandler responsePayloadHandler) {
        boolean isRequestOriginal = false;
        if (requestPayload.has(MFAConstants.ACH_TYPE_IDENTIFIER)) {
            isRequestOriginal = requestPayload.get(MFAConstants.ACH_TYPE_IDENTIFIER).isJsonNull() ? false : true;
            if (isRequestOriginal) {
                isRequestOriginal = StringUtils
                        .isNotBlank(requestPayload.get(MFAConstants.ACH_TYPE_IDENTIFIER).getAsString());
            }
        } else if (requestPayload.has(MFAConstants.ACH_TEMPLATE_ID)) {
            isRequestOriginal = requestPayload.get(MFAConstants.ACH_TEMPLATE_ID).isJsonNull() ? false : true;
            if (isRequestOriginal) {
                String template_id = requestPayload.get(MFAConstants.ACH_TEMPLATE_ID).getAsString();
                if (StringUtils.isBlank(template_id)) {
                    isRequestOriginal = false;
                } else {
                    HashMap<String, String> fetchTemplate = new HashMap<>();
                    fetchTemplate.put(DBPUtilitiesConstants.FILTER,
                            MFAConstants.ACH_TEMPLATE_ID + DBPUtilitiesConstants.EQUAL + template_id);
                    Result templateDetails = HelperMethods.callApi(requestManager, fetchTemplate,
                            HelperMethods.getHeaders(requestManager), URLConstants.BB_TEMPLATE_GET);
                    String TransactionType_id = HelperMethods.getFieldValue(templateDetails, "TransactionType_id");
                    String DebitAccount = HelperMethods.getFieldValue(templateDetails, "DebitAccount");
                    String TotalAmount = HelperMethods.getFieldValue(templateDetails, "TotalAmount");
                    String Company_id = HelperMethods.getFieldValue(templateDetails, "Company_id");
                    requestPayload.addProperty(MFAConstants.ACH_TYPE_IDENTIFIER, TransactionType_id);
                    requestPayload.addProperty(MFAConstants.ACH_DEBIT_ACCOUNT, DebitAccount);
                    requestPayload.addProperty(MFAConstants.ACH_TOTAL_AMOUNT, TotalAmount);
                    requestPayload.addProperty(MFAConstants.ACH_COMPANY_ID, Company_id);
                    diagnostic.prepareDebug("Updating payload with template details: " + requestPayload).log();
                    requestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
                }
            }
        }

        return isRequestOriginal;
    }

    private boolean isRequestForMFA(JsonObject requestPayload, JsonObject mfaAttributes) {
        boolean isRequestForMFA = false;
        if (requestPayload.has(MFAConstants.MFA_ATTRIBUTES)) {
            if (mfaAttributes.has(MFAConstants.SERVICE_KEY)) {
                isRequestForMFA = mfaAttributes.get(MFAConstants.SERVICE_KEY).isJsonNull() ? false : true;
            }
        }
        return isRequestForMFA;
    }

    private Result getMFAServiceRecord(String serviceKey, FabricRequestManager requestManager) {

        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey;
        Result result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_GET);
        return result;
    }

    private double getTransactionAmount(JsonObject requestPayload) {
        double transactionAmount = 0.00;
        try {
            if (requestPayload.has(MFAConstants.ACH_TRANSACTION_RECORDS)) {
                JsonElement recordElement = new JsonParser()
                        .parse(requestPayload.get(MFAConstants.ACH_TRANSACTION_RECORDS).getAsString());
                if (!recordElement.isJsonArray()) {
                    transactionAmount = -0.01;
                } else {
                    JsonArray recordsArray = recordElement.getAsJsonArray();
                    for (int i = 0; i < recordsArray.size(); i++) {
                        JsonObject record = recordsArray.get(i).getAsJsonObject();
                        if (record.has(MFAConstants.ACH_TRANSACTION_AMOUNT)) {
                            double recordAmount = record.get(MFAConstants.ACH_TRANSACTION_AMOUNT).getAsDouble();
                            if (recordAmount <= 0.00) {
                                transactionAmount = -0.01;
                                break;
                            }
                            transactionAmount += recordAmount;
                        } else if (record.has(MFAConstants.ACH_TRANSACTION_SUB_RECORDS)) {
                            JsonElement subrecordElement;
                            try {
                                subrecordElement =
                                        record.get(MFAConstants.ACH_TRANSACTION_SUB_RECORDS).getAsJsonArray();
                            } catch (Exception e) {
                                subrecordElement = new JsonParser()
                                        .parse(record.get(MFAConstants.ACH_TRANSACTION_SUB_RECORDS).getAsString());
                            }
                            if (!subrecordElement.isJsonArray()) {
                                transactionAmount = -0.01;
                                break;
                            } else {
                                JsonArray subrecordsArray = subrecordElement.getAsJsonArray();
                                for (int j = 0; j < subrecordsArray.size(); j++) {
                                    JsonObject subrecord = subrecordsArray.get(j).getAsJsonObject();
                                    if (subrecord.has(MFAConstants.ACH_TRANSACTION_AMOUNT)) {
                                        double subrecordAmount = subrecord.get(MFAConstants.ACH_TRANSACTION_AMOUNT)
                                                .getAsDouble();
                                        if (subrecordAmount <= 0.00) {
                                            transactionAmount = -0.01;
                                            break;
                                        }
                                        transactionAmount += subrecordAmount;
                                    } else {
                                        transactionAmount = -0.01;
                                    }
                                }
                                if (transactionAmount == -0.01) {
                                    break;
                                }
                            }
                        } else {
                            transactionAmount = -0.01;
                            break;
                        }
                    }

                }

            } else {
                transactionAmount = -0.01;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            transactionAmount = -0.01;
        }
        return transactionAmount;
    }

}
