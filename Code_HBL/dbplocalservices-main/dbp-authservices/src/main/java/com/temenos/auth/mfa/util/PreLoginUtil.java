package com.temenos.auth.mfa.util;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
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
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class PreLoginUtil {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private Result mfaConf = null;

    private Record mfa = null;

    private String dbpErrCode = null;
    private String dbpErrMsg = null;

    private boolean isValidMFA = false;

    private JsonObject communication = null;
    private JsonObject primaryCommunication = null;

    private String user_id = null;
    private String userName = null;

    private String serviceKey = null;
    private String serviceName = null;
    private String createddts = null;

    private FabricRequestManager requestManager;
    private DataControllerRequest dcRequest;

    private String mfaservice_userid;

    public PreLoginUtil(DataControllerRequest dcRequest) {

        this.dcRequest = dcRequest;

        Map<String, String> inputParams = new HashMap<>();

        try {
            mfaConf = AdminUtil.invokeAPI(inputParams, URLConstants.GET_MFA_CONFIGURATION, dcRequest);
        } catch (HttpCallException e) {

            alert.prepareError(e.getMessage()).log();
        }

        isValidMFA = configureMFA();
    }

    public PreLoginUtil(FabricRequestManager requestManager, String userName, String serviceKey,
            StringBuilder serviceKeyGeneratedTime) {

        this.requestManager = requestManager;
        this.userName = userName;
        this.serviceKey = serviceKey;
        this.serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        this.createddts = serviceKeyGeneratedTime.toString();

        Map<String, String> inputParams = new HashMap<>();

        mfaConf = AdminUtil.invokeAPI(inputParams, URLConstants.GET_MFA_CONFIGURATION, requestManager);

        isValidMFA = configureMFA();
    }

    public PreLoginUtil(FabricRequestManager requestManager, String serviceKey) {

        this.requestManager = requestManager;
        this.serviceName = MFAConstants.SERVICE_ID_PRELOGIN;
        this.serviceKey = serviceKey;
        this.user_id = HelperMethods.getCustomerIdFromSession(requestManager);

        Map<String, String> inputParams = new HashMap<>();

        mfaConf = AdminUtil.invokeAPI(inputParams, URLConstants.GET_MFA_CONFIGURATION, requestManager);

        isValidMFA = configureMFA();
    }

    public boolean configureMFA() {

        if (mfaConf == null) {
            return false;
        }

        if (mfaConf.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
            setDbpErrMsg(mfaConf.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY).getValue());
        }
        if (mfaConf.getNameOfAllParams().contains("errmsg")) {
            setDbpErrMsg(mfaConf.getParamByName("errmsg").getValue());
        }

        if (mfaConf.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_CODE_KEY)) {
            setDbpErrCode(mfaConf.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY).getValue());
        }

        if (!HelperMethods.hasRecords(mfaConf)) {
            return false;
        }

        if (mfaConf.getIdOfAllDatasets().contains("mfaTypes")) {
            for (Record record : mfaConf.getDatasetById("mfaTypes").getAllRecords()) {
                if (record.getParam("mfaTypeId").getValue().equals(MFAConstants.SECURE_ACCESS_CODE)) {
                    mfa = record;
                }
            }
        }

        isValidMFA = mfa != null;

        return isValidMFA;
    }

    public String getMFAType() {
        return mfa.getParam(MFAConstants.MFA_TYPE_ID).getValue();
    }

    public boolean isValidMFA() {
        return isValidMFA;
    }

    public void setValidMFA(boolean isValidMFA) {
        this.isValidMFA = isValidMFA;
    }

    public boolean updateUserIdStatus() {

        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + "serviceName" + DBPUtilitiesConstants.EQUAL + serviceName;
        Result result = new Result();

        result = HelperMethods.callGetApi(requestManager, filterQuery, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_GET);

        if (!HelperMethods.hasRecords(result)) {
            return false;
        }

        if (HelperMethods.hasRecords(result)
                && StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "User_id"))) {
            this.user_id = HelperMethods.getFieldValue(result, "User_id");
            return true;
        }

        filterQuery = new String();
        result = new Result();

        filterQuery = "UserName" + DBPUtilitiesConstants.EQUAL + userName;

        result = HelperMethods.callGetApi(requestManager, filterQuery, HelperMethods.getHeaders(requestManager),
                URLConstants.CUSTOMERVERIFY_GET);

        if (!HelperMethods.hasRecords(result)) {
            return false;
        }

        if (HelperMethods.hasRecords(result)) {
            this.user_id = String.valueOf(HelperMethods.getFieldValue(result, "id"));
        }

        Map<String, String> mfaservice = new HashMap<>();
        mfaservice.put("serviceKey", serviceKey);
        mfaservice.put("User_id", this.user_id);

        result = new Result();
        try {
            result = HelperMethods.callApi(requestManager, mfaservice, HelperMethods.getHeaders(requestManager),
                    URLConstants.MFA_SERVICE_UPDATE);
        } catch (Exception e) {
            return false;
        }

        if (HelperMethods.hasRecords(result)) {
            return true;
        }

        return false;
    }

    public boolean updateUserId() {

        Map<String, String> mfaservice = new HashMap<>();
        mfaservice.put("serviceKey", serviceKey);
        mfaservice.put("User_id", this.user_id);

        Result result = new Result();
        try {
            result = HelperMethods.callApi(requestManager, mfaservice, HelperMethods.getHeaders(requestManager),
                    URLConstants.MFA_SERVICE_UPDATE);
        } catch (Exception e) {
            return false;
        }

        if (HelperMethods.hasRecords(result)) {
            return true;
        }

        return false;
    }

    public boolean isServiceKeyExpired() {

        String string = this.createddts;

        if (StringUtils.isNotBlank(string)) {
            Date createdts = HelperMethods.getFormattedTimeStamp(string);
            Calendar generatedCal = Calendar.getInstance();
            generatedCal.setTime(createdts);

            Date verifyDate = new Date();
            Calendar verifyingCal = Calendar.getInstance();
            verifyingCal.setTime(verifyDate);

            int otpValidityTime = getServiceKeyExpiretime();
            generatedCal.add(Calendar.MINUTE, otpValidityTime);

            long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
            long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

            if (GeneratedMilliSeconds > verifyingMilliSeconds) {
                return false;
            }
        }

        return true;
    }

    private int getServiceKeyExpiretime() {
        try {
            if (dcRequest != null) {
                return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, dcRequest));
            } else {
                return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, requestManager));
            }

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return 10;
    }

    public JsonObject setserviceMFAAttributes() {

        JsonObject resultJson = new JsonObject();

        JsonObject mfaAttributes = new JsonObject();
        String serviceKey = this.serviceKey;

        JsonObject communication = null;
        String mfaType = getMFAType();

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
            updatePayloadWithReferenceIdMappings(requestManager, serviceKey, referenceIdMapings);
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

                resultJson = requestOTP(OTP, resultJson, false);
                if (resultJson.has(MFAConstants.MFA_ATTRIBUTES)) {
                    mfaAttributes = resultJson.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
                } else {
                    return resultJson;
                }
            }

            mfaAttributes.addProperty("communicationType", communicationType);
            mfaAttributes.addProperty("sacPreferenceCriteria", getCommunicationType());
            mfaAttributes.add(MFAConstants.CUSTOMER_COMMUNICATION, referenceCommunication);
        }
        mfaAttributes.addProperty("isMFARequired", "true");
        mfaAttributes.addProperty("MFAType", mfaType);
        mfaAttributes.addProperty(MFAConstants.SERVICE_KEY, serviceKey);
        resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);

        return resultJson;
    }

    private void updatePayloadWithReferenceIdMappings(FabricRequestManager requestManager, String serviceKey,
            JsonObject referenceIdMapings) {

        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + "serviceName" + DBPUtilitiesConstants.EQUAL + MFAConstants.SERVICE_ID_PRELOGIN;

        Result mfaserviceresult = HelperMethods.callGetApi(requestManager, filterQuery,
                HelperMethods.getHeaders(requestManager), URLConstants.MFA_SERVICE_GET);

        if (!HelperMethods.hasRecords(mfaserviceresult)) {
            return;
        }

        String payload = HelperMethods.getFieldValue(mfaserviceresult, "payload");

        try {
            if (StringUtils.isNotBlank(payload)) {
                payload = CryptoText.decrypt(payload);
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return;
        }
        StringBuilder sb = new StringBuilder(payload);
        if (StringUtils.isNotBlank(payload)) {
            sb.append(",");
        }
        sb.append(referenceIdMapings.toString());

        try {
            payload = CryptoText.encrypt(sb.toString());
        } catch (Exception e) {
            alert.prepareError("Exception while decrypting" + e.getMessage()).log();
            return;
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("serviceKey", serviceKey);
        hashMap.put("payload", payload.toString());
        HelperMethods.callApi(requestManager, hashMap, HelperMethods.getHeaders(requestManager),
                URLConstants.MFA_SERVICE_UPDATE);
        return;
    }

    private int generateRandomID() {
        SecureRandom rand = new SecureRandom();
        return (int) (100000 + (rand.nextFloat() * 900000));
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

    public JsonObject getCommunicationData(boolean isOnlyPrimary, boolean sendMaskedToo) {

        if (StringUtils.isBlank(user_id)) {
            return communication;
        }

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + user_id;

        if (isOnlyPrimary) {
            filter += DBPUtilitiesConstants.AND + "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";
        }

        Result result = new Result();

        result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        if (!HelperMethods.hasRecords(result)) {
            DBXResult dbxResult = new DBXResult();
            CommunicationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
            CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
            communicationDTO.setCustomer_id(user_id);
            if (isOnlyPrimary) {
                dbxResult =
                        backendDelegate.getPrimaryMFACommunicationDetails(communicationDTO,
                                new HashMap<String, Object>());
            } else {
                JsonArray jsonarray = new JsonArray();
                dbxResult =
                        backendDelegate.getPrimaryMFACommunicationDetails(communicationDTO,
                                new HashMap<String, Object>());
                for (JsonElement jsonelement : ((JsonObject) dbxResult.getResponse())
                        .get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                        .getAsJsonArray()) {
                    jsonarray.add(jsonelement.getAsJsonObject());
                }
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
                            getMaskedEmail(record.getParam(DBPUtilitiesConstants.VALUE).getValue()));
                }
                contact.addProperty(MFAConstants.UNMASKED, record.getParam(DBPUtilitiesConstants.VALUE).getValue());
                email.add(contact);
            } else if (record.getParam(DBPUtilitiesConstants.TYPE_ID).getValue()
                    .equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)
                    && StringUtils.isNotBlank(record.getParam(DBPUtilitiesConstants.VALUE).getValue())) {

                String mobile = record.getParam(DBPUtilitiesConstants.VALUE).getValue();
                mobile = processMobile(mobile);
                if (sendMaskedToo) {
                    contact.addProperty("masked", getMaskedPhoneNumber(mobile));
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
            return communication;
        }

        return null;
    }

    private String getMaskedPhoneNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder(phoneNumber);
        for (int i = 3; i < sb.length() - 2; ++i) {
            sb.setCharAt(i, 'X');
        }
        return sb.toString();
    }

    public String getMaskedEmail(String email) {
        StringBuilder sb = new StringBuilder(email);
        for (int i = 3; i < sb.length() && sb.charAt(i) != '@'; ++i) {
            sb.setCharAt(i, 'X');
        }
        return sb.toString();
    }

    public JsonObject requestOTP(JsonObject OTP, JsonObject resultJson, boolean isCheckNeeded) {
        boolean isPrimary = !isPrimaryCommunication();

        JsonObject mfaAttributes = new JsonObject();

        boolean isPhoneValid = true;
        boolean isEmailValid = true;

        JsonObject referenceIdMappings = getReferenceIdMappingsFromDB(serviceKey);
        if (isCheckNeeded) {
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

            String serviceName = this.serviceName;

            Map<String, String> map = new HashMap<>();
            map.put(MFAConstants.SERVICE_KEY, this.serviceKey);
            if (OTP.has(MFAConstants.PHONE)) {
                map.put("Phone", OTP.get(MFAConstants.PHONE).getAsString());
            }
            if (OTP.has(MFAConstants.EMAIL)) {
                map.put("Email", OTP.get(MFAConstants.EMAIL).getAsString());
            }

            map.put(MFAConstants.SERVICE_NAME, serviceName);
            map.put("userId", this.user_id);

            if (OTP.has(MFAConstants.SECURITY_KEY)) {
                map.put(MFAConstants.SECURITY_KEY, OTP.get(MFAConstants.SECURITY_KEY).getAsString());
            }

            result = HelperMethods.callApi(requestManager, map,
                    HelperMethods.getHeadersWithReportingParams(requestManager), URLConstants.REQUEST_PRELOGIN_OTP);

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

    public JsonObject getReferenceIdMappingsFromDB(String serviceKey) {
        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey;
        Result mfaresult = HelperMethods.callGetApi(requestManager, filterQuery,
                HelperMethods.getHeaders(requestManager), URLConstants.MFA_SERVICE_GET);
        if (HelperMethods.hasError(mfaresult) || !HelperMethods.hasRecords(mfaresult))
            return new JsonObject();
        String referenceIdMappings = new String();
        try {
            referenceIdMappings = CryptoText.decrypt(HelperMethods.getFieldValue(mfaresult, "payload"));
        } catch (Exception e) {
            alert.prepareError("Exception while decrypting" + e.getMessage()).log();
            return new JsonObject();
        }
        String values =
                referenceIdMappings.substring(referenceIdMappings.indexOf('{'), referenceIdMappings.indexOf('}') + 1);
        JsonObject resultJson = new JsonParser().parse(values).getAsJsonObject();
        return resultJson;
    }

    public boolean isValidPhone(JsonObject OTP, boolean isPrimary, JsonObject referenceIdMappings) {
        return referenceIdMappings.has(OTP.get("phone").getAsString());
    }

    public boolean isValidEmail(JsonObject OTP, boolean isPrimary, JsonObject referenceIdMappings) {
        return referenceIdMappings.has(OTP.get("email").getAsString());
    }

    private String processMobile(String mobile) {

        if (StringUtils.isBlank(mobile)) {
            return mobile;
        }

        String mobilenumber = "";

        if (!mobile.contains("+")) {
            mobilenumber = "+";
        }
        String[] strings = mobile.split("-");
        for (int i = 0; i < strings.length; i++) {
            mobilenumber += strings[i];
        }

        return mobilenumber;
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

    public JsonObject addVerifyAttributes(JsonObject resultJson, int retryCount) {

        int maxAttemptsAllowed = maxFailedAttemptsAllowed();
        resultJson.addProperty("maxFailedAttemptsAllowed", maxAttemptsAllowed + "");

        resultJson.addProperty("remainingFailedAttempts", (maxFailedAttemptsAllowed() - (retryCount + 1)) + "");
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

    public String getCommunicationType() {

        String communicationType = null;

        for (Record record : mfa.getRecordById(MFAConstants.MFA_CONFIGURATION).getAllRecords()) {
            if (record.getParam(MFAConstants.MFA_KEY).getValue().equals(MFAConstants.SAC_PREFERENCE_CRITERIA)) {
                communicationType = record.getParam("mfaValue").getValue();
                break;
            }
        }

        if (MFAConstants.DISPLAY_ALL.equals(communicationType)
                || MFAConstants.DISPLAY_NO_VALUE.equals(communicationType)) {
            Result result = new Result();

            if (requestManager != null) {
                result = HelperMethods.callGetApi(requestManager, "id" + DBPUtilitiesConstants.EQUAL + user_id,
                        HelperMethods.getHeaders(requestManager), URLConstants.CUSTOMERVERIFY_GET);
            } else {
                try {
                    result = HelperMethods.callGetApi(dcRequest, "id" + DBPUtilitiesConstants.EQUAL + user_id,
                            HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERVERIFY_GET);
                } catch (HttpCallException e) {

                    alert.prepareError(e.getMessage()).log();
                }
            }

            String customerType = HelperMethods.getFieldValue(result, "CustomerType_id");

            if (customerType.equals("TYPE_ID_SMALL_BUSINESS")) {
                communicationType = MFAConstants.DISPLAY_PRIMARY;
            }
        }

        return communicationType;
    }

    public boolean isMFARequired() {
        try {
            return "true".equals((mfaConf.getParamByName(MFAConstants.IS_MFA_REQUIRED).getValue()));
        } catch (Exception e) {

        }

        return false;
    }

    public int getOTPLength() {
        for (Record record : mfa.getRecordById(MFAConstants.MFA_CONFIGURATION).getAllRecords()) {
            if (record.getParam(MFAConstants.MFA_KEY).getValue().equals(MFAConstants.SAC_CODE_LENGTH)) {
                return Integer.parseInt(record.getParam("mfaValue").getValue());
            }
        }

        return 6;
    }

    public int getSACCodeExpiretime() {
        for (Record record : mfa.getRecordById(MFAConstants.MFA_CONFIGURATION).getAllRecords()) {
            if (record.getParam(MFAConstants.MFA_KEY).getValue().equals(MFAConstants.SAC_CODE_EXPIRES_AFTER)) {
                return Integer.parseInt(record.getParam("mfaValue").getValue());
            }
        }

        return 1;
    }

    public boolean shouldLockUser() {
        boolean status = false;
        for (Record record : mfa.getRecordById(MFAConstants.MFA_CONFIGURATION).getAllRecords()) {
            if (record.getParam(MFAConstants.MFA_KEY).getValue().equals(MFAConstants.LOCK_USER)) {
                status = "true".equals(record.getParam("mfaValue").getValue());
            }
        }

        if (status) {
            lockUser();
        }

        return status;
    }

    public int maxFailedAttemptsAllowed() {
        for (Record record : mfa.getRecordById(MFAConstants.MFA_CONFIGURATION).getAllRecords()) {
            if (record.getParam(MFAConstants.MFA_KEY).getValue().equals(MFAConstants.MAX_FAILED_ATTEMPTS_ALLOWED)) {
                return Integer.parseInt(record.getParam("mfaValue").getValue());
            }
        }

        return 1;
    }

    private int sacMaxResendRequestsAllowed() {
        for (Record record : mfa.getRecordById(MFAConstants.MFA_CONFIGURATION).getAllRecords()) {
            if (record.getParam(MFAConstants.MFA_KEY).getValue().equals(MFAConstants.SAC_MAX_RESEND_REQUESTS_ALLOWED)) {
                return Integer.parseInt(record.getParam("mfaValue").getValue());
            }
        }

        return 1;
    }

    public JsonObject getRequestPayload(String serviceKey, String serviceName) {
        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL + serviceName;

        Result result = new Result();
        if (requestManager != null) {
            if (StringUtils.isNotBlank(user_id)) {
                filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user_id;
            }

            result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                    URLConstants.MFA_SERVICE_GET);
        } else {
            if (StringUtils.isNotBlank(user_id)) {
                filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user_id;
            }

            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.MFA_SERVICE_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        String payload = HelperMethods.getFieldValue(result, "payload");

        if (StringUtils.isNotBlank(payload)) {
            try {
                return new JsonParser().parse(CryptoText.decrypt(payload)).getAsJsonObject();
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        return new JsonObject();

    }

    public boolean isStateVerified(String serviceKey, String serviceName) {
        Result result = new Result();
        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL + serviceName;
        try {
            if (requestManager != null) {
                filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user_id;
                result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                        URLConstants.MFA_SERVICE_GET);
            } else {
                filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user_id;
                try {
                    result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                            URLConstants.MFA_SERVICE_GET);
                } catch (HttpCallException e) {

                    alert.prepareError(e.getMessage()).log();
                }
            }

            if (HelperMethods.hasRecords(result) && !isServiceKeyExpired()) {
                return "true".equals(HelperMethods.getFieldValue(result, MFAConstants.IS_VERIFIED));
            }

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return false;
    }

    public boolean isPrimaryCommunication() {

        return getCommunicationType().equals(MFAConstants.DISPLAY_ALL);
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

    public int getMaximumOTPsperDay() {

        String requestLimit = "50";
        try {
            if (dcRequest != null) {
                requestLimit = URLFinder.getPathUrl(URLConstants.OTP_REQUEST_LIMIT, dcRequest);
            } else {
                requestLimit = URLFinder.getPathUrl(URLConstants.OTP_REQUEST_LIMIT, requestManager);
            }
        } catch (Exception e) {

            alert.prepareError(e.getMessage()).log();
        }
        if (StringUtils.isNotBlank(requestLimit)) {
            return Integer.parseInt(requestLimit);
        }

        return 50;
    }

    public JsonObject addRequestAttributes(JsonObject mfaAttributes, int retryCount) {
        int maximumAllowedRetries = sacMaxResendRequestsAllowed();
        mfaAttributes.addProperty("sacMaxResendRequestsAllowed", maximumAllowedRetries + "");
        mfaAttributes.addProperty("remainingResendAttempts", (maximumAllowedRetries - (retryCount + 1)) + "");
        mfaAttributes.addProperty("sacCodeLength", getOTPLength() + "");

        return mfaAttributes;
    }

    public boolean shouldLogoutUser() {

        boolean status = false;
        for (Record record : mfa.getRecordById(MFAConstants.MFA_CONFIGURATION).getAllRecords()) {
            if (record.getParam(MFAConstants.MFA_KEY).getValue().equals(MFAConstants.LOGOUT_USER)) {
                status = "true".equals(record.getParam("mfaValue").getValue());
            }
        }
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

        Map<String, String> map = new HashMap<>();

        Result result = new Result();

        String accountLockoutTime = null;

        try {
            if (dcRequest != null) {
                result = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                        URLConstants.PASSWORDLOCKOUTSETTINGS_GET);
            } else {
                result = HelperMethods.callApi(requestManager, map, HelperMethods.getHeaders(requestManager),
                        URLConstants.PASSWORDLOCKOUTSETTINGS_GET);
            }

            if (HelperMethods.hasRecords(result)) {
                Dataset dataset = result.getDatasetById("passwordlockoutsettings");
                accountLockoutTime = dataset.getRecord(0).getParam("accountLockoutTime").getValue();

            }

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (accountLockoutTime == null) {

            if (dcRequest != null) {
                map.put("userName", userName);
                try {
                    result = AdminUtil.invokeAPI(map, URLConstants.PASSWORD_LOCKOUT_SETTINGS, dcRequest);
                } catch (HttpCallException e) {

                    alert.prepareError(e.getMessage()).log();
                }
            } else {
                map.put("userName", userName);
                result = AdminUtil.invokeAPI(map, URLConstants.PASSWORD_LOCKOUT_SETTINGS, requestManager);
            }

            if (result.getIdOfAllRecords().contains("passwordlockoutsettings")) {
                return result.getRecordById("passwordlockoutsettings").getParamByName("accountLockoutTime").getValue();
            }
        }

        if (accountLockoutTime != null) {

            int time = Integer.parseInt(accountLockoutTime);
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

    public String getSMSText(String otp) {

        String smsText = "";

        if (mfa.getNameOfAllParams().contains(MFAConstants.SMS_TEXT)) {
            smsText = mfa.getParam(MFAConstants.SMS_TEXT).getValue();
        }

        return replacetext(smsText, otp);
    }

    public String getEmailBody(String otp) {

        String smsText = "";

        if (mfa.getNameOfAllParams().contains(MFAConstants.EMAIL_BODY)) {
            smsText = mfa.getParam(MFAConstants.EMAIL_BODY).getValue();
        }

        return replacetext(smsText, otp);
    }

    public String getEmailSubject() {

        if (mfa.getNameOfAllParams().contains(MFAConstants.EMAIL_SUBJECT)) {
            return mfa.getParam(MFAConstants.EMAIL_SUBJECT).getValue();
        }

        return "OTP";
    }

    public String replacetext(String text, String otp) {

        JsonObject payload = getRequestPayload();

        if (text.contains("[#]OTP[/#]")) {
            text = text.replace("[#]OTP[/#]", otp);
        }

        if (text.contains("[#]Transfer Amount[/#]")) {
            if (payload.has("amount") && !payload.get("amount").isJsonNull()) {
                text = text.replace("[#]Transfer Amount[/#]", payload.get("amount").getAsString());
            } else {
                if (payload.has("paidAmount") && !payload.get("paidAmount").isJsonNull()) {
                    text = text.replace("[#]Transfer Amount[/#]", payload.get("paidAmount").getAsString());
                }
            }
        }

        if (text.contains("[#]Account Number[/#]")) {
            if (payload.has("fromAccountNumber") && !payload.get("fromAccountNumber").isJsonNull()) {
                String accoutnNumber = payload.get("fromAccountNumber").getAsString();
                accoutnNumber = accoutnNumber.substring(accoutnNumber.length() - 4, accoutnNumber.length());
                text = text.replace("[#]Account Number[/#]", accoutnNumber);
            }
        }

        if (text.contains("[#]Payee Name[/#]")) {
            if (payload.has("toAccountNumber") && !payload.get("toAccountNumber").isJsonNull()) {
                String accoutnNumber = payload.get("toAccountNumber").getAsString();
                accoutnNumber = accoutnNumber.substring(accoutnNumber.length() - 4, accoutnNumber.length());
                text = text.replace("[#]Payee Name[/#]", accoutnNumber);
            }
        }

        if (text.contains("[#]Server Date[/#]")) {
            String date = HelperMethods.getCurrentDate();
            text = text.replace("[#]Server Date[/#]", date);
        }

        if (text.contains("[#]Server Time[/#]")) {
            String date = HelperMethods.getCurrentTime();
            text = text.replace("[#]Server Time[/#]", date);
        }

        return text;
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
            try {
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_UPDATE);
            } catch (HttpCallException e) {

                alert.prepareError(e.getMessage()).log();
            }
        } else {
            input.put("lockCount", (new PasswordLockoutSettings(requestManager).getAccountLockoutThreshold() + 1) + "");
            input.put("lockedOn", HelperMethods.getCurrentTimeStamp());
            HelperMethods.callApi(requestManager, input, HelperMethods.getHeaders(requestManager),
                    URLConstants.CUSTOMER_UPDATE);

        }

    }

    public boolean isAppActionValid() {

        if (getDbpErrCode() != null) {
            if ("21334".equals(getDbpErrCode()) || "20866".equals(getDbpErrCode())) {
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean isValidServiceKey() {
        Result result = new Result();
        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL + serviceName;
        try {
            if (requestManager != null) {
                filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user_id;
                result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                        URLConstants.MFA_SERVICE_GET);
            } else {
                filter += DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user_id;
                try {
                    result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                            URLConstants.MFA_SERVICE_GET);
                } catch (HttpCallException e) {
                    alert.prepareError(e.getMessage()).log();
                    return false;
                }
            }

            if (HelperMethods.hasRecords(result)) {
                return true;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return false;
        }
        return false;
    }

    public boolean isValidOnlyServiceKey(String serviceName) {
        Result result = new Result();
        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL + serviceName;
        try {
            if (requestManager != null) {
                result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                        URLConstants.MFA_SERVICE_GET);
            } else {
                try {
                    result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                            URLConstants.MFA_SERVICE_GET);
                } catch (HttpCallException e) {
                    alert.prepareError(e.getMessage()).log();
                    return false;
                }
            }

            if (HelperMethods.hasRecords(result)) {
                this.createddts = HelperMethods.getFieldValue(result, "Createddts");
                this.createddts = HelperMethods.getFieldValue(result, "Createddts");
                this.mfaservice_userid = HelperMethods.getFieldValue(result, "User_id");
                return !isServiceKeyExpired();
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return false;
        }
        return false;
    }

    public void createEntryInStateChecker() {
        if (dcRequest != null) {
            HashMap<String, String> input = new HashMap<>();
            input.put(MFAConstants.SERVICE_KEY, dcRequest.getParameter(MFAConstants.SERVICE_KEY));
            input.put(MFAConstants.SERVICE_NAME, dcRequest.getParameter(MFAConstants.SERVICE_NAME));
            input.put(MFAConstants.IS_VERIFIED, "true");
            HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MFA_SERVICE_UPDATE);

        } else {
            JsonObject payload = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

            if (payload.has(MFAConstants.MFA_ATTRIBUTES)) {
                JsonObject mfaAttributes = new JsonObject();
                JsonElement mfaElement = payload.get(MFAConstants.MFA_ATTRIBUTES);

                if (mfaElement.isJsonObject()) {
                    mfaAttributes = payload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();
                } else {
                    JsonParser parser = new JsonParser();
                    mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
                }

                HashMap<String, String> input = new HashMap<>();

                if (mfaAttributes.has(MFAConstants.SERVICE_KEY)) {
                    input.put(MFAConstants.SERVICE_KEY, mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString());
                }

                input.put(MFAConstants.IS_VERIFIED, "true");

                HelperMethods.callApi(requestManager, input, HelperMethods.getHeaders(requestManager),
                        URLConstants.MFA_SERVICE_UPDATE);

            }

        }
    }

    public String getUserId() {
        return this.user_id;
    }

    public void setUserID(String userId) {
        this.user_id = userId;
    }

    public String getMFAUserId() {

        return this.mfaservice_userid;
    }
}
