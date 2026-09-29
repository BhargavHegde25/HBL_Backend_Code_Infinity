package com.kony.dbputilities.mfa;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.utils.OTPUtil;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.PasswordLockoutSettings;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.constants.ServiceNameConstant;
import com.temenos.dbx.mfa.utils.MFAConfigurationUtil;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.mfa.utils.SecurityQuestionsUtil;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class PostLoginMFAUtil {
    private LoggerUtil logger = null;
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private JSONObject mfaConf = null;

    private String dbpErrCode = null;
    private String dbpErrMsg = null;

    public MFAConfigurationUtil mfaConfigurationUtil = null;
    JsonObject communication = null;
    JsonObject primaryCommunication = null;

    FabricRequestManager requestManager;
    DataControllerRequest dcRequest;

    String user_id = null;

    private MFAServiceUtil mfaServiceUtil;

    private SecurityQuestionsUtil securityQuestionsUtil;

    private void initLOG() {
        if (logger == null) {
            logger = new LoggerUtil(PostLoginMFAUtil.class);
        }
    }

    public PostLoginMFAUtil(DataControllerRequest dcRequest) {
        initLOG();
        this.dcRequest = dcRequest;
        user_id = HelperMethods.getCustomerIdFromSession(dcRequest);
    }

    public PostLoginMFAUtil(DataControllerRequest dcRequest, String userId) {
        initLOG();
        this.dcRequest = dcRequest;
        this.user_id = userId;
    }

    public PostLoginMFAUtil(FabricRequestManager requestManager) {
        initLOG();
        this.requestManager = requestManager;
        user_id = HelperMethods.getCustomerIdFromSession(requestManager);
    }

    public PostLoginMFAUtil(FabricRequestManager requestManager, String userId) {
        initLOG();
        this.requestManager = requestManager;
        this.user_id = userId;
    }

    public MFAServiceUtil getMFAServiceUtil() {
        return this.mfaServiceUtil;
    }

    public void getMFaModeforRequestfromDB(String serviceKey) {

        getMFAServiceResult(serviceKey, null);

        mfaConf = mfaServiceUtil.getMFAMODE();

        configureMFA();
    }

    public void getMFaModeforRequestfromDB(String serviceKey, String serviceName) {
    	diagnostic.prepareDebug("getMFaModeforRequestfromDB ##").log();

        getMFAServiceResult(serviceKey, serviceName);

        mfaConf = mfaServiceUtil.getMFAMODE();

        configureMFA();
    }

    private void getMFAServiceResult(String serviceKey, String serviceName) {
        if (mfaServiceUtil == null) {

            mfaServiceUtil = new MFAServiceUtil();

            String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey;

            if (StringUtils.isNotBlank(serviceName)) {
                filter += DBPUtilitiesConstants.AND + MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL
                        + serviceName;
            }

            if (StringUtils.isNotBlank(user_id)) {
                filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user_id;
            }

            if (requestManager != null) {

                mfaServiceUtil.loadDTO(requestManager, filter);

            } else {
                mfaServiceUtil.loadDTO(dcRequest, filter);
            }
        }
    }

    public void configureMFA() {
        if (mfaConf == null) {
            return;
        }

        if (mfaConf.keySet().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
            setDbpErrMsg(mfaConf.getString(ErrorCodeEnum.ERROR_MESSAGE_KEY));
        } else if (mfaConf.keySet().contains("errmsg")) {
            setDbpErrMsg(mfaConf.getString("errmsg"));
        }

        if (mfaConf.keySet().contains(ErrorCodeEnum.ERROR_CODE_KEY)) {
            dbpErrCode = mfaConf.getInt(ErrorCodeEnum.ERROR_CODE_KEY) + "";
        } else if (mfaConf.keySet().contains("errcode")) {

            dbpErrCode = mfaConf.getInt("errcode") + "";

        }
        if (isAppActionValid() && StringUtils.isEmpty(dbpErrCode) && StringUtils.isEmpty(dbpErrMsg)) {

            mfaConfigurationUtil = new MFAConfigurationUtil(mfaConf);

            if (mfaConfigurationUtil.isValidMFA()) {
                if (MFAConstants.SECURITY_QUESTIONS.equals(mfaConfigurationUtil.getPrimaryMFATypeId())) {
                    mfaConfigurationUtil.setBackupMFAType(areSecurityQuestionsPresent());
                }

            }
        }
    }

    public void getMfaModeforRequest() {
        Map<String, String> inputParams = new HashMap<>();

        JsonObject payload = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

        String actionId = null;
        if (payload.has(MFAConstants.MFA_ATTRIBUTES)) {
            JsonObject mfaAttributes = new JsonObject();
            JsonElement mfaElement = payload.get(MFAConstants.MFA_ATTRIBUTES);

            if (mfaElement.isJsonObject()) {
                mfaAttributes = payload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
            } else {
                JsonParser parser = new JsonParser();
                mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
            }
            diagnostic.debug("MFAConstants SERVICE_NAME ###" + mfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString());

            if (mfaAttributes.has(MFAConstants.SERVICE_NAME)) {
                actionId = mfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();
            }
        }

        String reportingParams =
                requestManager.getHeadersHandler().getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);

        String appId = null;
        if (StringUtils.isNotBlank(reportingParams)) {
            JSONObject reportingParamsJson = new JSONObject();
            try {
                reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, "UTF-8"));
            } catch (JSONException | UnsupportedEncodingException e) {

                alert.prepareError("parsing exception :", e).log();
            }
            appId = reportingParamsJson.optString("aid");
        }

        inputParams.put("appId", appId);
        inputParams.put("actionId", actionId);

        try {
            JsonObject result = AdminUtil.invokeAPIAndGetJson(inputParams, URLConstants.GET_MFA_MODE, dcRequest);
            mfaConf = new JSONObject(result.toString());
        } catch (HttpCallException e) {
            alert.prepareError("Caught exception while getting MFAMode : ", e).log();
        }

        configureMFA();
    }

    private boolean areSecurityQuestionsPresent() {

        if (securityQuestionsUtil == null) {

            securityQuestionsUtil = new SecurityQuestionsUtil();

            if (dcRequest != null) {
                securityQuestionsUtil.loadCustomerSecurityQuestions(dcRequest, user_id);
            } else {
                securityQuestionsUtil.loadCustomerSecurityQuestions(requestManager, user_id);
            }
        }

        return securityQuestionsUtil.areSecurityQuestionsPresent();
    }

    public JsonObject getCommunicationData(boolean isOnlyPrimary, boolean sendMaskedToo) {

        diagnostic.prepareDebug("Inside get communication data Json \n send Only Primary -> " + isOnlyPrimary
                + "; sendMaskedToo -> " + sendMaskedToo).log();

        if (StringUtils.isBlank(user_id)) {
            return communication;
        }

        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
        communicationDTO.setCustomer_id(user_id);

        Result result = new Result();

        DBXResult dbxResult = new DBXResult();
        CommunicationBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
        if (isOnlyPrimary) {
            dbxResult =
                    backendDelegate.getPrimaryMFACommunicationDetails(communicationDTO, new HashMap<String, Object>());
        } else {
            JsonArray jsonarray = new JsonArray();
            dbxResult = new DBXResult();
            dbxResult = backendDelegate.getCommunicationDetails(communicationDTO, new HashMap<String, Object>());
            for (JsonElement jsonelement : ((JsonObject) dbxResult.getResponse())
                    .get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                    .getAsJsonArray()) {
                jsonarray.add(jsonelement.getAsJsonObject());
            }
            JsonObject response = new JsonObject();
            response.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, jsonarray);
            dbxResult.setResponse(response);
        }

        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            result = JSONToResult.convert(jsonObject.toString());
        }

        if (!HelperMethods.hasRecords(result)) {
            return communication;
        }

        JsonObject communication = new JsonObject();

        JsonArray phone = new JsonArray();
        JsonArray email = new JsonArray();
        JsonObject contact = new JsonObject();

        for (Record record : result.getAllDatasets().get(0).getAllRecords()) {
            contact = new JsonObject();
            if (record.getParam(DBPUtilitiesConstants.TYPE_ID).getValue().equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)
                    && StringUtils.isNotBlank(record.getParam(DBPUtilitiesConstants.VALUE).getValue())) {
                if (sendMaskedToo) {
                    contact.addProperty("masked",
                            OTPUtil.getMaskedEmail(record.getParam(DBPUtilitiesConstants.VALUE).getValue()));
                }
                contact.addProperty(MFAConstants.UNMASKED, record.getParam(DBPUtilitiesConstants.VALUE).getValue());
                email.add(contact);
            } else if (record.getParam(DBPUtilitiesConstants.TYPE_ID).getValue()
                    .equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)
                    && StringUtils.isNotBlank(record.getParam(DBPUtilitiesConstants.VALUE).getValue())) {

                String mobile = record.getParam(DBPUtilitiesConstants.VALUE).getValue();
                mobile = OTPUtil.processMobile(mobile);
                if (sendMaskedToo) {
                    contact.addProperty("masked", OTPUtil.getMaskedPhoneNumber(mobile));
                }
                contact.addProperty(MFAConstants.UNMASKED, mobile);
                phone.add(contact);
            }
        }
        if (phone.size() > 0 || email.size() > 0) {
            if (phone.size() > 0)
                communication.add(MFAConstants.PHONE, phone);
            if (email.size() > 0)
                communication.add(MFAConstants.EMAIL, email);

            diagnostic.prepareDebug("Response from getCustomerCommunication : " + communication.toString()).log();

            return communication;
        }

        diagnostic.prepareDebug("Response from getCustomerCommunication : " + null).log();

        return null;
    }

    public boolean isRetryAllowed(int retryCount) {

        return retryCount >= sacMaxResendRequestsAllowed();
    }

    public Result addRequestAttributes(Result result, int retryCount) {

        int maximumAllowedRetries = sacMaxResendRequestsAllowed();
        result.addParam(new Param("sacMaxResendRequestsAllowed", maximumAllowedRetries + "",
                DBPUtilitiesConstants.STRING_TYPE));
        result.addParam(new Param("remainingResendAttempts", (maximumAllowedRetries - (retryCount + 1)) + "",
                DBPUtilitiesConstants.STRING_TYPE));
        result.addParam(new Param("sacCodeLength", getOTPLength() + "", DBPUtilitiesConstants.STRING_TYPE));

        return result;
    }

    public Result addVerifyAttributes(Result result, Result retRes) {

        retRes.addParam(new Param("maxFailedAttemptsAllowed", maxFailedAttemptsAllowed() + "", "int"));
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            int InvalidAttempt = Integer.parseInt(str);
            retRes.addParam(new Param("remainingFailedAttempts",
                    (maxFailedAttemptsAllowed() - (InvalidAttempt + 1)) + "", "int"));
            retRes.addParam(new Param("failedAttempts", (InvalidAttempt + 1) + "", "int"));
            if ((maxFailedAttemptsAllowed() - (InvalidAttempt + 1)) <= 0) {
                retRes.addParam(new Param("remainingFailedAttempts", "0", "int"));
                String lockoutTime = getLockoutTime();
                if (shouldLockUser()) {
                    lockUser();
                    retRes.addParam(new Param("lockUser", "true", "String"));
                    retRes.addParam(new Param("lockoutTime", lockoutTime, "int"));
                } else {
                    retRes.addParam(new Param("lockUser", "false", "String"));
                }

                if (shouldLogoutUser()) {
                    retRes.addParam(new Param("logoutUser", "true", "String"));
                    retRes.addParam(new Param("lockoutTime", lockoutTime, "int"));
                } else {
                    retRes.addParam(new Param("logoutUser", "false", "String"));
                }
            }
        }

        return retRes;
    }

    private JSONObject getReportingParams() {

        String reportingParams = requestManager.getHeadersHandler()
                .getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        JSONObject jsonObject = new JSONObject();
        try {
            reportingParams = URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (StringUtils.isBlank(reportingParams)) {
            return jsonObject;
        }

        try {
            jsonObject = new JSONObject(reportingParams);
        } catch (JSONException ex) {
            ex.getMessage();
        }

        return jsonObject;
    }

    public JsonObject addVerifyAttributes(JsonObject resultJson, int retryCount) {

        int maxAttemptsAllowed = maxFailedAttemptsAllowed();
        resultJson.addProperty("maxFailedAttemptsAllowed", maxAttemptsAllowed + "");

        if ((maxFailedAttemptsAllowed() - (retryCount + 1)) > 0) {
            resultJson.addProperty("remainingFailedAttempts", (maxFailedAttemptsAllowed() - (retryCount + 1)) + "");
        }

        resultJson.addProperty("failedAttempts", (retryCount + 1) + "");
        String lockoutTime = getLockoutTime();
        if (maxAttemptsAllowed <= (retryCount + 1) && shouldLockUser()) {
            resultJson.addProperty("remainingFailedAttempts", "0");
            resultJson.addProperty("lockUser", "true");
            resultJson.addProperty("lockoutTime", lockoutTime);
        } else {
            resultJson.addProperty("lockUser", "false");
        }

        if (maxAttemptsAllowed <= (retryCount + 1) && shouldLogoutUser()) {
            resultJson.addProperty("remainingFailedAttempts", "0");
            resultJson.addProperty("logoutUser", "true");
            resultJson.addProperty("lockoutTime", lockoutTime);
        } else {
            resultJson.addProperty("logoutUser", "false");
        }

        return resultJson;
    }

    public JsonObject setserviceMFAAttributes() {

        JsonObject resultJson = new JsonObject();

        JsonObject mfaAttributes = new JsonObject();
        PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

        JsonObject communication = null;
        JsonArray securityQuestions = null;
        String mfaType = getMFAType();
        String serviceKey = "";
        if (mfaType.equals(MFAConstants.SECURE_ACCESS_CODE)) {
            String communicationType = getCommunicationType();
            communication = getOTPAttributes(communicationType);
            if (communication == null) {
                ErrorCodeEnum.ERR_10504.setErrorCode(resultJson, "Invalid communication Information");
                return resultJson;
            }
            JsonObject referenceCommunication = new JsonParser().parse(communication.toString()).getAsJsonObject();

            JsonObject referenceIdMapings = new JsonObject();
            JsonArray phone = null;
            JsonArray email = null;
            int phoneSize = 0;
            int emailSize = 0;
            if (JSONUtil.hasKey(referenceCommunication, "phone")) {
                phone = referenceCommunication.get("phone").getAsJsonArray();
                phoneSize = phone.size();
            }
            if (JSONUtil.hasKey(referenceCommunication, "email")) {
                email = referenceCommunication.get("email").getAsJsonArray();
                emailSize = email.size();
            }
            for (int i = 0; i < Math.max(phoneSize, emailSize); i++) {
                if (i < phoneSize) {
                    String randomId = String.valueOf(generateRandomID());
                    referenceIdMapings.addProperty(randomId,
                            phone.get(i).getAsJsonObject().get("unmasked").getAsString());
                    phone.get(i).getAsJsonObject().addProperty("referenceId", randomId);
                    phone.get(i).getAsJsonObject().addProperty("unmasked", randomId);
                }
                if (i < emailSize) {
                    String randomId = String.valueOf(generateRandomID());
                    referenceIdMapings.addProperty(randomId,
                            email.get(i).getAsJsonObject().get("unmasked").getAsString());
                    email.get(i).getAsJsonObject().addProperty("referenceId", randomId);
                    email.get(i).getAsJsonObject().addProperty("unmasked", randomId);
                }
            }
            serviceKey = getServiceKey(requestpayload, referenceIdMapings);
            if (serviceKey == null) {
                ErrorCodeEnum.ERR_10504.setErrorCode(resultJson);
                return resultJson;
            }

            if (!communicationType.equals(MFAConstants.DISPLAY_ALL)) {
                JsonObject OTP = new JsonObject();
                JsonArray phones = JSONUtil.hasKey(communication, MFAConstants.PHONE)
                        ? communication.get(MFAConstants.PHONE).getAsJsonArray()
                        : new JsonArray();
                JsonArray emails = JSONUtil.hasKey(communication, MFAConstants.EMAIL)
                        ? communication.get(MFAConstants.EMAIL).getAsJsonArray()
                        : new JsonArray();
                if (phones.size() > 0) {
                    OTP.addProperty(MFAConstants.PHONE,
                            phones.get(0).getAsJsonObject().get(MFAConstants.UNMASKED).getAsString());
                }

                if (emails.size() > 0) {
                    OTP.addProperty(MFAConstants.EMAIL,
                            emails.get(0).getAsJsonObject().get(MFAConstants.UNMASKED).getAsString());
                }
                resultJson = requestOTP(OTP, resultJson, serviceKey, false);
                if (resultJson.has(MFAConstants.MFA_ATTRIBUTES)) {
                    mfaAttributes = resultJson.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
                } else {
                    return resultJson;
                }
            }

            mfaAttributes.addProperty("communicationType", communicationType);
            mfaAttributes.addProperty("sacPreferenceCriteria", getCommunicationType());
            mfaAttributes.add(MFAConstants.CUSTOMER_COMMUNICATION, referenceCommunication);
        } else if (mfaType.equals(MFAConstants.SECURITY_QUESTIONS)) {
            securityQuestions = getSecurityQuestionsAttributesJsonArray();
            serviceKey = getServiceKey(requestpayload, securityQuestions);
            if (serviceKey == null) {
                ErrorCodeEnum.ERR_10504.setErrorCode(resultJson);
                return resultJson;
            }

            mfaAttributes.add("securityQuestions", securityQuestions);
        }else if (mfaType.equals(MFAConstants.TRANSACTION_PIN)) {
            //securityQuestions = getSecurityQuestionsAttributesJsonArray();
            serviceKey = getServiceKey(requestpayload, securityQuestions);
            if (serviceKey == null) {
                ErrorCodeEnum.ERR_10504.setErrorCode(resultJson);
                return resultJson;
            }

           // mfaAttributes.add("securityQuestions", securityQuestions);
        }

        mfaAttributes.addProperty("isMFARequired", "true");
        mfaAttributes.addProperty("MFAType", mfaType);
        mfaAttributes.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
        resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);

        return resultJson;
    }

    private String getServiceKey(JsonObject requestpayload, JsonElement securityQuestions) {

        return MFAServiceUtil.getServiceKey(requestpayload, securityQuestions, user_id,
                new JsonParser().parse(mfaConf.toString()), requestManager, dcRequest, null);

    }

    private int getSecurityQuestionsRetry() {

        JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

        String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();

        getMFAServiceResult(serviceKey, null);

        return mfaServiceUtil.getSecurityQuestionsRetry();
    }

    private JsonArray getSecurityQuestionsfromDB() {

        JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

        String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();

        getMFAServiceResult(serviceKey, null);

        return mfaServiceUtil.getSecurityQuestionsArrayfromDB();
    }

    private JsonObject getSecurityQuestionsJsonObjectFromDB() {

        JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

        String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();

        getMFAServiceResult(serviceKey, null);

        return mfaServiceUtil.getSecurityQuestionsJsonfromDB();

    }

    private JsonArray getSecurityQuestionsAttributesJsonArray() {
        return securityQuestionsUtil
                .getSecurityQuestionsAttributesJsonArray(mfaConfigurationUtil.getnumberofSecurityQuestions());
    }

    private int getnumberofSecurityQuestions() {

        return mfaConfigurationUtil.getnumberofSecurityQuestions();
    }

    private JsonObject getOTPAttributes(String communicationType) {

        if (communicationType.equals(MFAConstants.DISPLAY_ALL)) {
            if (communication == null) {
                communication = getCommunicationData(false, true);
            }
            return communication;
        } else {
            if (primaryCommunication == null) {
                primaryCommunication = getCommunicationData(true, true);
            }
            return primaryCommunication;
        }
    }

    public String getCommunicationType() {

        return mfaConfigurationUtil.getCommunicationType();
    }

    public String getMFAType() {

        return mfaConfigurationUtil.getMFAType();
    }

    public double getFrequencyAmount() {

        return mfaConfigurationUtil.getFrequencyAmount();
    }

    public boolean isMFARequiredAlways() {

        return mfaConfigurationUtil.isMFARequiredAlways();
    }

    public boolean isMFARequired() {

        return mfaConfigurationUtil.isMFARequired();
    }

    public int getOTPLength() {

        return mfaConfigurationUtil.getOTPLength();
    }

    public int getSACCodeExpiretime() {

        return mfaConfigurationUtil.getSACCodeExpiretime();
    }

    public boolean shouldLockUser() {

        boolean status = mfaConfigurationUtil.shouldLockUser();

        if (status) {
            lockUser();
        }

        return status;
    }

    public int maxFailedAttemptsAllowed() {

        return mfaConfigurationUtil.maxFailedAttemptsAllowed();
    }

    private int sacMaxResendRequestsAllowed() {

        return mfaConfigurationUtil.sacMaxResendRequestsAllowed();
    }

    public JsonObject getRequestPayload(String serviceKey, String serviceName) {

        getMFAServiceResult(serviceKey, serviceName);

        return mfaServiceUtil.getRequestPayload();

    }

    public boolean isValidServiceKey(String serviceKey, String serviceName) {

        getMFAServiceResult(serviceKey, serviceName);

        return mfaServiceUtil.isValidServiceKey(getServiceKeyExpiretime());

    }

    private int getServiceKeyExpiretime() {

        try {
            return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, requestManager));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return 10;
    }

    public boolean isPrimaryCommunication() {

        return getCommunicationType().equals(MFAConstants.DISPLAY_ALL);
    }

    public JsonObject requestOTP(JsonObject OTP, JsonObject resultJson, String serviceKey, boolean isCheckNeeded) {
        boolean isPrimary = !isPrimaryCommunication();

        JsonObject mfaAttributes = new JsonObject();

        boolean isPhoneValid = true;
        boolean isEmailValid = true;

        if (isCheckNeeded) {
            JsonObject referenceIdMappings = getSecurityQuestionsJsonObjectFromDB();
            isPhoneValid = isValidPhone(OTP, isPrimary, referenceIdMappings);
            isEmailValid = isValidEmail(OTP, isPrimary, referenceIdMappings);
            if (isPhoneValid) {
                OTP.add("phone", referenceIdMappings.get(OTP.get("phone").getAsString()));
            }
            if (isEmailValid) {
                OTP.add("email", referenceIdMappings.get(OTP.get("email").getAsString()));
            }
        }

        Result result = null;

        if (isPhoneValid || isEmailValid) {
            JsonObject inputMFAAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                    .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

            String serviceName = inputMFAAttributes.get(MFAConstants.SERVICE_NAME).getAsString();

            Map<String, String> map = new HashMap<>();
            map.put(MFAConstants.SERVICE_KEY, serviceKey);
            if (OTP.has(MFAConstants.PHONE)) {
                map.put("Phone", OTP.get(MFAConstants.PHONE).getAsString());
            }
            if (OTP.has(MFAConstants.EMAIL)) {
                map.put("Email", OTP.get(MFAConstants.EMAIL).getAsString());
            }

            map.put(MFAConstants.SERVICE_NAME, serviceName);

            if (OTP.has(MFAConstants.SECURITY_KEY)) {
                map.put(MFAConstants.SECURITY_KEY, OTP.get(MFAConstants.SECURITY_KEY).getAsString());
            }

            result = HelperMethods.callApi(requestManager, map,
                    HelperMethods.getHeadersWithReportingParams(requestManager), URLConstants.REQUEST_MFA_OTP);
            if (result != null && result.getNameOfAllParams().contains(DBPUtilitiesConstants.SUCCESS)) {
                mfaAttributes = new JsonObject();
                mfaAttributes.addProperty(MFAConstants.SECURITY_KEY,
                        result.getParamByName(MFAConstants.SECURITY_KEY).getValue());
                mfaAttributes.addProperty("sacMaxResendRequestsAllowed",
                        result.getParamByName("sacMaxResendRequestsAllowed").getValue());
                if (result.getNameOfAllParams().contains("remainingResendAttempts")) {
                    mfaAttributes.addProperty("remainingResendAttempts",
                            result.getParamByName("remainingResendAttempts").getValue());
                    mfaAttributes.addProperty("sacCodeLength", result.getParamByName("sacCodeLength").getValue());
                }
                resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
                resultJson.addProperty(DBPUtilitiesConstants.SUCCESS,
                        result.getParamByName(DBPUtilitiesConstants.SUCCESS).getValue());
            } else {
                if (result != null) {
                    if (result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
                        resultJson.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY,
                                result.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY).getValue());
                    }
                    if (result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_CODE_KEY)) {
                        resultJson.addProperty(ErrorCodeEnum.ERROR_CODE_KEY,
                                result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY).getValue());
                    }
                    if (result.getNameOfAllParams().contains(DBPUtilitiesConstants.VALIDATION_ERROR)) {
                        resultJson.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY,
                                result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR).getValue());
                    }
                    return resultJson;
                }

                ErrorCodeEnum.ERR_10063.setErrorCode(resultJson);

            }
        } else {
            ErrorCodeEnum.ERR_10058.setErrorCode(resultJson);
        }
        return resultJson;
    }

    public boolean isValidPhone(JsonObject OTP, boolean isPrimary, JsonObject referenceIdMappings) {
        return referenceIdMappings.has(OTP.get("phone").getAsString());
    }

    public boolean isValidEmail(JsonObject OTP, boolean isPrimary, JsonObject referenceIdMappings) {
        return referenceIdMappings.has(OTP.get("email").getAsString());
    }

    public JsonObject verifyOTP(JsonObject OTP) {

        JsonObject inputMfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

        String serviceKey = inputMfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
        Result result;
        JsonObject resultJson = new JsonObject();

        String serviceName = inputMfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();

        Map<String, String> map = new HashMap<>();
        map.put(MFAConstants.SERVICE_KEY, serviceKey);
        map.put("Otp", OTP.get(MFAConstants.OTP_OTP).getAsString());
        if (OTP.has(MFAConstants.SECURITY_KEY)) {
            map.put(MFAConstants.SECURITY_KEY, OTP.get(MFAConstants.SECURITY_KEY).getAsString());
        }
        map.put(MFAConstants.SERVICE_NAME, serviceName);
        result = HelperMethods.callApi(requestManager, map, HelperMethods.getHeadersWithReportingParams(requestManager),
                URLConstants.VERIFY_MFA_OTP);
        if (result != null && result.getNameOfAllParams().contains(DBPUtilitiesConstants.SUCCESS)) {
            resultJson.addProperty(DBPUtilitiesConstants.SUCCESS,
                    result.getParamByName(DBPUtilitiesConstants.SUCCESS).getValue());
        } else {
            if (result != null) {
                JsonObject mfaAttributes = new JsonObject();
                if (result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
                    resultJson.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY,
                            result.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY).getValue());
                }
                if (result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_CODE_KEY)) {
                    resultJson.addProperty(ErrorCodeEnum.ERROR_CODE_KEY,
                            result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY).getValue());
                }

                if (result.getNameOfAllParams().contains("maxFailedAttemptsAllowed")) {
                    mfaAttributes.addProperty("maxFailedAttemptsAllowed",
                            result.getParamByName("maxFailedAttemptsAllowed").getValue());
                    mfaAttributes.addProperty("remainingFailedAttempts",
                            result.getParamByName("remainingFailedAttempts").getValue());
                    mfaAttributes.addProperty("failedAttempts", result.getParamByName("failedAttempts").getValue());
                }

                if (result.getNameOfAllParams().contains("lockUser")) {
                    mfaAttributes.addProperty("lockUser", result.getParamByName("lockUser").getValue());
                    mfaAttributes.addProperty("lockoutTime", result.getParamByName("lockoutTime").getValue());
                }

                if (result.getNameOfAllParams().contains("isOTPExpired")) {
                    mfaAttributes.addProperty("isOTPExpired", result.getParamByName("isOTPExpired").getValue());
                }

                if (result.getNameOfAllParams().contains("sacMaxResendRequestsAllowed")
                        && result.getNameOfAllParams().contains("remainingResendAttempts")) {
                    mfaAttributes.addProperty("sacMaxResendRequestsAllowed",
                            result.getParamByName("sacMaxResendRequestsAllowed").getValue());
                    mfaAttributes.addProperty("remainingResendAttempts",
                            result.getParamByName("remainingResendAttempts").getValue());
                }

                if (result.getNameOfAllParams().contains("logoutUser")) {
                    mfaAttributes.addProperty("logoutUser", result.getParamByName("logoutUser").getValue());
                    mfaAttributes.addProperty("lockoutTime", result.getParamByName("lockoutTime").getValue());

                }

                resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);

                return resultJson;
            }

            ErrorCodeEnum.ERR_10075.setErrorCode(resultJson);

        }

        return resultJson;
    }
    
	public JsonObject verifyPIN(JsonObject PIN) {

		diagnostic.prepareDebug("verifyPIN DEF ##"+ PIN.toString()).log();
		JsonObject inputMfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
				.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

		String serviceKey = inputMfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
		Result result;
		JsonObject resultJson = new JsonObject();

		String serviceName = inputMfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();

		Map<String, String> map = new HashMap<>();
		map.put(MFAConstants.SERVICE_KEY, serviceKey);
		map.put("pin", PIN.get(MFAConstants.PIN).getAsString());
		map.put("userName", PIN.get(MFAConstants.USER_NAME).getAsString());

		/*
		 * if (PIN.has(MFAConstants.SECURITY_KEY)) { map.put(MFAConstants.SECURITY_KEY,
		 * PIN.get(MFAConstants.SECURITY_KEY).getAsString()); }
		 */
		map.put(MFAConstants.SERVICE_NAME, serviceName);
		result = HelperMethods.callApi(requestManager, map, HelperMethods.getHeadersWithReportingParams(requestManager),
				URLConstants.VERIFY_TRANSACTION_PIN);
		// String isPinValid = result.getParamValueByName("isPinValid");
		if (result != null & (result.getParamValueByName("isPinValid")).equalsIgnoreCase("true")) {
			resultJson.addProperty(DBPUtilitiesConstants.SUCCESS, "success");
			resultJson.addProperty("isPinValid", "true");
			resultJson.addProperty("opstatus", "0");
			resultJson.addProperty("httpStatusCode", "200");
		} else {
			if (result != null) {
				diagnostic.prepareDebug("Inside ELSE verifyPIN ##").log();
				// set MFA attributes
				Record rc = result.getRecordById("MFAAttributes");
				JsonObject mfaAttributes = new JsonObject();
				diagnostic.prepareDebug("Inside ELSE verifyPIN rc##"+ rc.toString()).log();
				mfaAttributes.addProperty("maxFailedAttemptsAllowed",
						rc.getParamValueByName("maxFailedAttemptsAllowed"));
				mfaAttributes.addProperty("remainingFailedAttempts", rc.getParamValueByName("remainingFailedAttempts"));
				mfaAttributes.addProperty("failedAttempts", rc.getParamValueByName("failedAttempts"));

				diagnostic.prepareDebug("failedAttempts rc##"+ rc.getParamValueByName("failedAttempts")).log();
				diagnostic.prepareDebug("maxFailedAttemptsAllowed rc##"+ rc.getParamValueByName("maxFailedAttemptsAllowed")).log();
				if (rc.getParamValueByName("failedAttempts").equalsIgnoreCase(rc.getParamValueByName("maxFailedAttemptsAllowed"))) {
					diagnostic.prepareDebug("logoutuser cond rc##").log();
					mfaAttributes.addProperty("lockUser", rc.getParamValueByName("lockUser"));
					mfaAttributes.addProperty("logoutUser", rc.getParamValueByName("logoutUser"));
					mfaAttributes.addProperty("lockoutTime", rc.getParamValueByName("lockoutTime"));
				}
				resultJson.addProperty("isPinValid", "false");
				resultJson.addProperty("opstatus", "0");
				resultJson.addProperty("httpStatusCode", "200");
				resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
				return resultJson;
			}else {
				ErrorCodeEnum.ERR_10075.setErrorCode(resultJson);
			}
			
		}
		return resultJson;
	}

    public int getMaximumOTPsperDay() {

        if (dcRequest != null) {
            String requestLimit = URLFinder.getPathUrl(URLConstants.OTP_REQUEST_LIMIT, dcRequest);
            if (StringUtils.isNotBlank(requestLimit)) {
                return Integer.parseInt(requestLimit);
            }
        } else {
            String requestLimit = URLFinder.getPathUrl(URLConstants.OTP_REQUEST_LIMIT, requestManager);
            if (StringUtils.isNotBlank(requestLimit)) {
                return Integer.parseInt(requestLimit);
            }
        }

        return 50;
    }

    public boolean shouldLogoutUser() {

        boolean status = mfaConfigurationUtil.shouldLogoutUser();
        if (status) {
            logout();
        }

        return status;
    }

    private void logout() {

        String url = "";
        Map<String, String> inputParams = new HashMap<>();
        Map<String, String> headers = null;
        if (dcRequest != null) {
            url = URLFinder.getPathUrl(URLConstants.DBP_LOGOUT_URL, dcRequest);
            headers = HelperMethods.getHeaders(dcRequest);
            HelperMethods.callExternalApiAsync(inputParams, headers, url, dcRequest);
        } else {
            url = URLFinder.getPathUrl(URLConstants.DBP_LOGOUT_URL, requestManager);
            headers = HelperMethods.getHeaders(requestManager);
            HelperMethods.callExternalApiAsync(inputParams, headers, url, requestManager);
        }

    }

    public JsonObject verifySecurityQuestions(JsonArray jsonArray) {

        JsonObject resultJson = new JsonObject();

        JsonArray securityQuestions = getSecurityQuestionsfromDB();

        boolean status = false;

        if (jsonArray.size() != getnumberofSecurityQuestions()) {
            status = false;
        }

        Set<String> questionsID = new HashSet<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("questionId")) {
                String questionId = jsonObject.get("questionId").getAsString();
                boolean matched = false;
                if (!questionsID.contains(questionId)) {
                    for (JsonElement element : securityQuestions) {
                        JsonObject question = element.getAsJsonObject();
                        if (questionId.equals(question.get("SecurityQuestion_id").getAsString())) {
                            matched = true;
                        }
                    }
                }
                if (!matched) {
                    status = false;
                    break;
                }

                questionsID.add(questionId);
                status = true;
            }
        }

        if (!status || questionsID.size() != getnumberofSecurityQuestions()) {
            ErrorCodeEnum.ERR_10506.setErrorCode(resultJson);
            return resultJson;
        }

        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("securityQuestions", jsonArray.toString());

        status = securityQuestionsUtil.verifySecurityQuestionsFromDB(jsonArray);

        JsonObject mfaAttributes = new JsonObject();

        if (!status) {
            int retryCount = getSecurityQuestionsRetry();
            if (retryCount < maxFailedAttemptsAllowed()) {
                addVerifyAttributes(mfaAttributes, retryCount);
                if (retryCount + 1 < maxFailedAttemptsAllowed()) {
                    JsonArray securityQuestionsArray = getSecurityQuestionsAttributesJsonArray();
                    updateSecurityQuestionsRetryCount(retryCount, securityQuestionsArray);
                    mfaAttributes.add("securityQuestions", securityQuestionsArray);
                }
            } else {
                ErrorCodeEnum.ERR_10072.setErrorCode(resultJson);
                return resultJson;
            }
            resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);

            return resultJson;
        }

        resultJson.addProperty("success", "success");
        return resultJson;
    }

    private void updateSecurityQuestionsRetryCount(int retryCount, JsonArray securityQuestionsArray) {

        JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

        String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();

        Map<String, String> map = new HashMap<>();

        map.put(MFAConstants.SERVICE_KEY, serviceKey);
        map.put(MFAConstants.RETRY_COUNT, (retryCount + 1) + "");
        try {
            map.put(MFAConstants.SECURITY_QUESTIONS_DB, CryptoText.encrypt(securityQuestionsArray.toString()));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        HelperMethods.callApiAsync(requestManager, map, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_UPDATE);
    }

    public void removeServiceKey(String serviceKey) {

        Map<String, String> map = new HashMap<>();
        map.put(MFAConstants.SERVICE_KEY, serviceKey);
        if (requestManager != null) {
            HelperMethods.callApiAsync(requestManager, map, HelperMethods.getHeaders(requestManager),
                    URLConstants.MFA_SERVICE_DELETE);
        } else {
            HelperMethods.callApiAsync(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MFA_SERVICE_DELETE);
        }

    }

    public String getLockoutTime() {
        PasswordLockoutSettings lockoutSettings;
        if (dcRequest != null) {
            lockoutSettings = new PasswordLockoutSettings(dcRequest);
        } else {
            lockoutSettings = new PasswordLockoutSettings(requestManager);
        }

        String accountLockoutTime = "";
        int time = lockoutSettings.getAutoUnLockPeriod();
        if (time != 0) {
            accountLockoutTime = "";
            int minutes = time % 60;
            if (minutes != 0) {
                accountLockoutTime = minutes + " Minutes ";
            }

            int hours = time / 60;
            int days = hours / 24;
            hours = hours % 24;
            if (hours != 0) {
                accountLockoutTime = hours + " Hours ";
            }
            if (minutes != 0) {
                accountLockoutTime = days + " Days ";
            }

        } else
            accountLockoutTime = 5 + " Minutes";

        return accountLockoutTime;
    }

    public String getEmailSubject() {

        return mfaConfigurationUtil.getEmailSubject();
    }

    private JsonObject getRequestPayload() {

        String serviceKey = "";
        String serviceName = "";

        if (requestManager != null) {
            serviceKey = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                    .get(MFAConstants.SERVICE_KEY).getAsString();
            serviceName = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                    .get(MFAConstants.SERVICE_NAME).getAsString();

        } else {
            serviceKey = dcRequest.getParameter(MFAConstants.SERVICE_KEY);
            serviceName = dcRequest.getParameter(MFAConstants.SERVICE_NAME);
        }

        return getRequestPayload(serviceKey, serviceName);
    }

    public String getDbpErrMsg() {
        return dbpErrMsg;
    }

    public void setDbpErrMsg(String dbpErrMsg) {
        this.dbpErrMsg = dbpErrMsg;
    }

    public String getDbpErrCode() {
        return dbpErrCode;
    }

    public void setDbpErrCode(String dbpErrCode) {
        this.dbpErrCode = dbpErrCode;
    }

    private void lockUser() {

        if (StringUtils.isBlank(user_id)) {
            return;
        }

        Map<String, String> input = new HashMap<>();
        input.put("id", user_id);

        if (dcRequest != null) {
            input.put("lockCount", (new PasswordLockoutSettings(dcRequest).getAccountLockoutThreshold() + 1) + "");
            input.put("lockedOn", HelperMethods.getCurrentTimeStamp());
            HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_UPDATE);
        } else {
            input.put("lockCount", (new PasswordLockoutSettings(requestManager).getAccountLockoutThreshold() + 1) + "");
            input.put("lockedOn", HelperMethods.getCurrentTimeStamp());
            HelperMethods.callApiAsync(requestManager, input, HelperMethods.getHeaders(requestManager),
                    URLConstants.CUSTOMER_UPDATE);

        }

    }

    public boolean MFABypassed(JsonObject requestpayload) {

        Map<String, String> map = HelperMethods.getCustomerFromIdentityService(requestManager);
        if (StringUtils.isNotBlank((map.get("isSchedulingEngine"))))
            return true;

        String bypassesTransfers = URLFinder.getPathUrl("MFA_TRANSACTION_TYPES_BYPASSES", requestManager);
        if (StringUtils.isNotBlank(bypassesTransfers)) {
            String[] bypassesTransactionTypes = bypassesTransfers.split(",");
            String transactionType = null;
            if (requestpayload.has("transactionType")) {
                transactionType = requestpayload.get("transactionType").getAsString();
                for (int i = 0; i < bypassesTransactionTypes.length; i++) {
                    if (bypassesTransactionTypes[i].equals(transactionType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getValidServiceName(JsonObject requestpayload, String operationString) {

        Record record = new Record();

        String transactionType;

        List<String> oldServices = Arrays.asList(
                "rbobjects_transactions_partialupdate",
                "rbobjects_transactions_create",
                "rbobjects_transactions_createtransfer");

        if (oldServices.contains(operationString)) {
            transactionType = getTrasactionType(requestpayload);
        } else {
            transactionType = operationString;
        }

        if ("rbobjects_transactions_createbulkbillpay".equalsIgnoreCase(operationString)) {
            transactionType = "BillPay";
        }

        String filter = MFAConstants.TRANSACTION_TYPE + DBPUtilitiesConstants.EQUAL + transactionType;

        Result result = new Result();
        result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_CONFIG_GET);

        if (!HelperMethods.hasRecords(result)) {
            return "dummyTransactionType";
        }

        record = HelperMethods.getRecord(result);

        if (transactionType.equals("ExternalTransfer")) {
            String accountNumber = requestpayload.has("ExternalAccountNumber")
                    && !requestpayload.get("ExternalAccountNumber").isJsonNull()
                            ? requestpayload.get("ExternalAccountNumber").getAsString()
                            : "";
            if (StringUtils.isBlank(accountNumber)) {
                accountNumber =
                        requestpayload.has("toAccountNumber") ? requestpayload.get("toAccountNumber").getAsString()
                                : "";
            }

            if (StringUtils.isBlank(accountNumber) && JSONUtil.hasKey(requestpayload, "IBAN")) {
                accountNumber = requestpayload.get("IBAN").getAsString();
            }

            if (StringUtils.isNotBlank(accountNumber)) {
                String serviceName =
                        checkBeneficiary(result, accountNumber, "accountNumber", URLConstants.EXTERNAL_ACCOUNTS);
                // since eu region has only international and domestic 1 time transfer
                boolean isEuropianGeography = HelperMethods.isEuropieanGeography();
                if (isEuropianGeography && "dummyTransactionType".equalsIgnoreCase(serviceName)) {
                    if (JSONUtil.hasKey(requestpayload, "IBAN")) {
                        serviceName = ServiceNameConstant.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE.name();
                    }
                    if (JSONUtil.hasKey(requestpayload, "toAccountNumber")) {
                        serviceName = ServiceNameConstant.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE.name();
                    }
                }
                return serviceName;
            }

        } else if (transactionType.equals("Wire")) {

            String payeeId = requestpayload.has("payeeId") ? requestpayload.get("payeeId").getAsString() : "";
            if (!StringUtils.isBlank(payeeId)) {
                return checkBeneficiary(result, payeeId, "payeeId", URLConstants.WIRED_RECIPIENTS);
            }
        }

        return record.getParamValueByName(MFAConstants.SERVICE_NAME);

    }

    private String getTrasactionType(JsonObject requestpayload) {

        if (requestpayload.has(MFAConstants.TRANSACTION_TYPE)) {
            return requestpayload.get(MFAConstants.TRANSACTION_TYPE).getAsString();
        }

        return "dummyTransactionType";
    }

    private String checkBeneficiary(Result result, String accountNumber, String id, String url) {
        Result accountsResult =
                HelperMethods.callGetApi(requestManager, null, HelperMethods.getHeaders(requestManager), url);

        if (HelperMethods.hasRecords(accountsResult)) {
            Record account = new Record();
            for (Record record : accountsResult.getAllDatasets().get(0).getAllRecords()) {
                if (record.getNameOfAllParams().contains(id)
                        && record.getParamByName(id).getValue().equals(accountNumber)) {
                    account = record;
                }
            }
            for (Record record : result.getAllDatasets().get(0).getAllRecords()) {
                String field = HelperMethods.getFieldValue(record, "field");
                String value = HelperMethods.getFieldValue(record, "value");

                if (account.getNameOfAllParams().contains(field)
                        && account.getParamByName(field).getValue().equals(value)) {
                    return record.getParamValueByName(MFAConstants.SERVICE_NAME);
                }
            }
        }

        return "dummyTransactionType";
    }

    public boolean isAppActionValid() {

        if (getDbpErrCode() != null) {
            if ("21334".equals(getDbpErrCode()) || "20866".equals(getDbpErrCode()) || "21318".equals(getDbpErrCode())
                    || "20888".equals(getDbpErrCode()) || "21341".equals(getDbpErrCode())
                    || !"21349".equals(getDbpErrCode())) {
                diagnostic.prepareDebug("Invalid AppAction returned from C360").log();
                return false;
            }

        }

        return true;
    }

    public boolean isMFARequired(JsonObject requestpayload) {
        return mfaConfigurationUtil.isMFARequired(requestpayload);
    }

    public static Result constructResultFromJSONObject(JSONObject JSONObject) {
        Result response = new Result();
        if (JSONObject == null || JSONObject.length() == 0) {
            return response;
        }
        Iterator<String> keys = JSONObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            if (JSONObject.get(key) instanceof String) {
                Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Integer) {
                Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof Boolean) {
                Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
                response.addParam(param);

            } else if (JSONObject.get(key) instanceof JSONArray) {
                Dataset dataset = HelperMethods.constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
                dataset.setId(key);
                response.addDataset(dataset);
            }
        }

        return response;
    }

    public static JsonObject constructJsonFromResult(Result result) {
        JsonObject response = new JsonObject();
        if (result == null) {
            return response;
        }

        List<Param> params = result.getAllParams();

        for (Param param : params) {
            response.addProperty(param.getName(), param.getValue());
        }

        List<Dataset> datasets = result.getAllDatasets();
        for (Dataset dataset : datasets) {
            response.add(dataset.getId(), constructJsonfromDataset(dataset));
        }

        List<Record> records = result.getAllRecords();
        for (Record record : records) {
            response.add(record.getId(), constructJsonfromRecord(record));
        }

        return response;
    }

    private static JsonElement constructJsonfromRecord(Record parentRecord) {
        JsonObject response = new JsonObject();
        if (parentRecord == null) {
            return response;
        }

        List<Param> params = parentRecord.getAllParams();

        for (Param param : params) {
            response.addProperty(param.getName(), param.getValue());
        }

        List<Dataset> datasets = parentRecord.getAllDatasets();
        for (Dataset dataset : datasets) {
            response.add(dataset.getId(), constructJsonfromDataset(dataset));
        }

        List<Record> records = parentRecord.getAllRecords();
        for (Record record : records) {
            response.add(record.getId(), constructJsonfromRecord(record));
        }

        return response;
    }

    private static JsonElement constructJsonfromDataset(Dataset parentDataset) {
        JsonArray response = new JsonArray();
        if (parentDataset == null) {
            return response;
        }

        List<Record> records = parentDataset.getAllRecords();
        for (Record record : records) {
            response.add(constructJsonfromRecord(record));
        }

        return response;
    }

    private int generateRandomID() {
        SecureRandom rand = new SecureRandom();
        return (int) (100000 + (rand.nextFloat() * 900000));
    }

    public String getSMSText(String otp) {
        return mfaConfigurationUtil.getSMSText(otp, getRequestPayload());
    }

    public String getEmailBody(String otp) {

        return mfaConfigurationUtil.getEmailBody(otp, getRequestPayload());
    }

    public void setError(JsonObject resultJson, String dbxErrorCode, String dbxErrMessage) {
        resultJson.addProperty("errmsg", dbxErrMessage);
        try {
            resultJson.addProperty("backend_error_code", Integer.parseInt(dbxErrorCode));
        } catch (Exception e) {
        }
        resultJson.addProperty("backend_error_message", dbxErrMessage);
        resultJson.addProperty(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, 401);
        resultJson.addProperty(DBPConstants.FABRIC_OPSTATUS_KEY, 20921);
    }

    public JsonObject getRequestPayloadWithoutMFAAttributes(String serviceKey, String serviceName) {
        JsonObject jsonObject = getRequestPayload(serviceKey, serviceName);

        jsonObject.remove(MFAConstants.MFA_ATTRIBUTES);
        jsonObject.remove(MFAConstants.SERVICE_NAME);
        jsonObject.remove("mfaMode");
        return jsonObject;
    }

    public boolean MFABypassed(JsonObject requestpayload, FabricRequestManager requestManager) {
        if (!MFABypassed(requestpayload)) {
            try {
                OperationData data = requestManager.getServicesManager().getOperationData();
                if (data != null
                        && ((data.getServiceId() != null && !"externalusermanagement".equals(data.getServiceId().toLowerCase()))
                                || (data.getObjectId() != null && !"externalusers".equals(data.getObjectId().toLowerCase()))
                                || (data.getOperationId() != null
                                        && !"updatedetails".equals(data.getOperationId().toLowerCase())))) {
                    return false;
                } else {
                    for (Entry<String, JsonElement> entry : requestpayload.entrySet()) {
                        try {
                            if (("phonenumbers".equals(entry.getKey().toLowerCase())
                                    || "emailids".equals(entry.getKey().toLowerCase()))
                                    && !entry.getValue().isJsonNull()) {
                                JsonElement jsonElement = new JsonParser().parse(entry.getValue().getAsString());
                                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                                    return false;
                                }
                            }
                            else if("deletecommunicationid".equals(entry.getKey().toLowerCase()) && !entry.getValue().isJsonNull()){
                                return StringUtils.isBlank(entry.getValue().getAsString());
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
            } catch (AppRegistryException e) {
            	alert.prepareError("Exception", e).log();
            }
        }

        return true;
    }
    
	public JSONObject getReportingParams(FabricRequestManager fabrequestManager) {
		String encodedReportingParams = fabrequestManager.getHeadersHandler().getHeader("X-Kony-ReportingParams");
		JSONObject reportingParamsJson = null;
		if (StringUtils.isNotBlank(encodedReportingParams)) {
			try {
				reportingParamsJson = new JSONObject(URLDecoder.decode(encodedReportingParams, "utf-8"));
			} catch (Exception e) {
				alert.prepareError("error reading reporting params " + e.getLocalizedMessage(), e).log();
			}
		}
		return reportingParamsJson;
	}

	public JsonObject verifyBiometric(JsonObject biometric, FabricRequestManager fabrequestManager) {
		diagnostic.prepareDebug("verifyBiometric INPUT:::" + biometric.toString()).log();
		JsonObject resultJson = new JsonObject();
		// Header check: X-Kony-Bio-Auth-Code
		String headerBioCode = fabrequestManager.getHeadersHandler().getHeader("X-Kony-Bio-Auth-Code");
		diagnostic.prepareDebug("verifyBiometric HEADER ::: X-Kony-Bio-Auth-Code = " + headerBioCode).log();

		boolean isValid = false;

		if ("5000".equals(headerBioCode)) {
			isValid = true;
			diagnostic.prepareDebug("verifyBiometric: Success from header").log();
		} else if (biometric != null && biometric.has(MFAConstants.BIOMETRIC_STATUS)) {
			String biometricStatus = biometric.get(MFAConstants.BIOMETRIC_STATUS).getAsString();
			diagnostic.prepareDebug("verifyBiometric: BiometricStatus in body = " + biometricStatus).log();
			isValid = "5000".equals(biometricStatus);
		}

		if (isValid) {
			resultJson.addProperty(DBPUtilitiesConstants.SUCCESS, "success");
			resultJson.addProperty("isBiometricValid", "true");
			resultJson.addProperty("opstatus", "0");
			resultJson.addProperty("httpStatusCode", "200");
		} else {
			diagnostic.prepareDebug("verifyBiometric: Authentication failed").log();
//			JsonObject mfaAttributes = new JsonObject();
//			mfaAttributes.addProperty("maxFailedAttemptsAllowed", "3");
//			mfaAttributes.addProperty("remainingFailedAttempts", "2");
//			mfaAttributes.addProperty("failedAttempts", "1");

			resultJson.addProperty("isBiometricValid", "false");
			resultJson.addProperty("opstatus", "0");
			resultJson.addProperty("httpStatusCode", "200");
			// resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
		}

		return resultJson;
	}

}
