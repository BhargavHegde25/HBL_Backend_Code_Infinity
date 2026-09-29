package com.kony.dbputilities.mfa;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.PasswordLockoutSettings;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.mfa.utils.MFAConfigurationUtil;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.mfa.utils.SecurityQuestionsUtil;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class LoginMFAUtil {
    private LoggerUtil logger;
    private JSONObject mfaConf = null;
    private String dbpErrCode = null;
    private String dbpErrMsg = null;
    private MFAServiceUtil mfaServiceUtil;
    private JsonObject communication = null;
    private Record communicationRecord = null;
    private Record primaryCommunicationRecord = null;
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    public MFAConfigurationUtil mfaConfigurationUtil = null;
    private String user_id = null;

    private FabricRequestManager requestManager;
    private DataControllerRequest dcRequest;

    private SecurityQuestionsUtil securityQuestionsUtil;

    Result mfaServiceResult = null;

    public LoginMFAUtil(DataControllerRequest dcRequest, String username) {
        this.dcRequest = dcRequest;
        logger = new LoggerUtil(LoginMFAUtil.class);
    }

    public LoginMFAUtil(FabricRequestManager requestManager) {
        this.requestManager = requestManager;
        logger = new LoggerUtil(LoginMFAUtil.class);
    }

    public LoginMFAUtil(DataControllerRequest dcRequest) {
        this.dcRequest = dcRequest;
        logger = new LoggerUtil(LoginMFAUtil.class);
    }

    public Result getMFAServiceResult(String serviceKey, String serviceName) {
        if (mfaServiceUtil == null) {
            Map<String, String> map = new HashMap<>();
            map.put(MFAConstants.SERVICE_KEY, serviceKey);
            map.put(MFAConstants.SERVICE_NAME, serviceName);

            try {
                if (dcRequest != null)
                    mfaServiceResult = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                            URLConstants.LOGIN_MFA_CONCURRENT_ORCHESTRATION);
                else if (requestManager != null)
                    mfaServiceResult = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(requestManager),
                            URLConstants.LOGIN_MFA_CONCURRENT_ORCHESTRATION);
            } catch (HttpCallException e1) {
                e1.getMessage();
            }
            if (null != mfaServiceResult) {
                user_id = mfaServiceResult.getParamValueByName("customerId");
            }

            if (StringUtils.isBlank(user_id)
                    || !HelperMethods.getFieldValue(mfaServiceResult, "User_id").equals(user_id)) {
                mfaServiceResult = new Result();
            }
        }

        return mfaServiceResult;
    }

    public boolean getMFaModeforRequestfromDB(String serviceKey, String serviceName) {

        getMFAServiceResult(serviceKey, serviceName);

        if (HelperMethods.hasRecords(mfaServiceResult)) {
            setMFaModeforRequest(mfaServiceResult);
        }

        return mfaConfigurationUtil != null && mfaConfigurationUtil.isValidMFA();
    }

    public void setMFaModeforRequest(Result result) {

        if (mfaServiceResult == null) {
            mfaServiceResult = result;
        }

        String str = HelperMethods.getFieldValue(mfaServiceResult, "mfaservice", "payload");

        JSONObject jsonObject = new JSONObject();

        if (StringUtils.isNotBlank(str)) {
            try {
                jsonObject = new JSONObject(CryptoText.decrypt(str));
            } catch (Exception e) {

                alert.prepareError("Caught exception while Decrypting : ", e).log();
            }
        }

        mfaConf = jsonObject.getJSONObject("mfaMode");

        mfaServiceUtil = new MFAServiceUtil(mfaServiceResult);
        configureMFA();
    }

    public void getMFAModeforRequest() {

        Map<String, String> inputParams = new HashMap<>();
        String actionId = null;

        actionId = dcRequest.getParameter(MFAConstants.SERVICE_NAME);

        String reportingParams = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);

        String appId = null;
        if (StringUtils.isNotBlank(reportingParams)) {
            JSONObject reportingParamsJson = new JSONObject();
            try {
                reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, "UTF-8"));
            } catch (JSONException | UnsupportedEncodingException e) {

                alert.prepareError("Unable to parse reporting Params ", e).log();
            }
            appId = reportingParamsJson.optString("aid");
        }

        inputParams.put("appId", appId);
        inputParams.put("actionId", actionId);

        try {
            JsonObject result = GetMfaModeUtil.getMfaMode(appId, actionId, dcRequest);
            if (JSONUtil.isJsonNull(result) || JSONUtil.hasKey(result, ErrorCodeEnum.ERROR_MESSAGE_KEY)
                    || JSONUtil.hasKey(result, ErrorCodeEnum.ERROR_CODE_KEY)) {
                result = AdminUtil.invokeAPIAndGetJson(inputParams, URLConstants.GET_MFA_MODE, dcRequest);
            }
            mfaConf = new JSONObject(result.toString());
        } catch (HttpCallException e) {
            alert.prepareError("Caught exception while getting MFAMode : ", e).log();
        }

        configureMFA();

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

    public Result checkAndAddMFAAttributes(Result retValue, Record dbxUsrAttr, Record dbxSecurityAttr, String mfa_key,
            DataControllerResponse dcResponse) {

        if (dbxUsrAttr != null) {
            if ((StringUtils.isBlank(dcRequest.getParameter("Password"))
                    || ("$password").equalsIgnoreCase(dcRequest.getParameter("Password")))
                    && StringUtils.isNotBlank(dcRequest.getParameter("sharedSecret"))
                    && !("$sharedSecret").equalsIgnoreCase(dcRequest.getParameter("sharedSecret"))) {

                diagnostic.prepareDebug("Logged in with shared Secret, MFA not Triggered").log();
                retValue.addRecord(dbxUsrAttr);
                retValue.addRecord(dbxSecurityAttr);
                return retValue;
            }

            String isCSRAssistMode = new String();
            if (StringUtils.isNotBlank(dbxUsrAttr.getParamValueByName("isCSRAssistMode"))) {
                isCSRAssistMode = dbxUsrAttr.getParamValueByName("isCSRAssistMode");
                if ("true".equalsIgnoreCase(isCSRAssistMode)) {
                    diagnostic.prepareDebug("CSR Assist Mode, MFA not Triggered").log();
                    retValue.addRecord(dbxUsrAttr);
                    retValue.addRecord(dbxSecurityAttr);
                    return retValue;
                }
            }
        }

        JsonObject jsonObject = new JsonObject();
        String serviceName = MFAConstants.SERVICE_ID_LOGIN;
        jsonObject.addProperty(MFAConstants.SERVICE_NAME, serviceName);
        jsonObject.addProperty(MFAConstants.SERVICE_KEY, mfa_key);
        dcRequest.addRequestParam_(MFAConstants.MFA_ATTRIBUTES, jsonObject.toString());
        dcRequest.addRequestParam_(MFAConstants.SERVICE_NAME, jsonObject.get(MFAConstants.SERVICE_NAME).getAsString());

        if (StringUtils.isBlank(dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION))) {
            dcRequest.getHeaderMap().put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                    dcRequest.getHeader(DBPUtilitiesConstants.AUTHORIZATION));
        }

        boolean isDeviceRegistered = false;
        if (dbxUsrAttr != null
                && dbxUsrAttr.getNameOfAllParams().contains(DBPUtilitiesConstants.IS_DEVICE_REGISTERED)) {
            isDeviceRegistered = dbxUsrAttr.getParamByName(DBPUtilitiesConstants.IS_DEVICE_REGISTERED).getValue()
                    .equalsIgnoreCase("true");
            logger.debug("IS_DEVICE_REGISTERED : " + isDeviceRegistered);
        }
        if (StringUtils.isNotBlank(mfa_key)) {
            diagnostic.prepareDebug("MFA_KEY is present in input :" + mfa_key).log();
            Result mfaServiceResult = getMFAServiceResult(mfa_key, serviceName);
            if (isStateVerified(mfaServiceResult)) {
                if (dbxUsrAttr == null && dbxSecurityAttr == null) {
                    jsonObject = getRequestPayload(mfaServiceResult);
                    OTPUtil.removeServiceKey(mfa_key, dcRequest);
                    if (jsonObject.has(DBPUtilitiesConstants.USR_ATTR)
                            && jsonObject.has(DBPUtilitiesConstants.SECURITY_ATTRIBUTES)) {
                        JsonObject userAttributes = jsonObject.get(DBPUtilitiesConstants.USR_ATTR).getAsJsonObject();
                        JsonObject securityAttributes = jsonObject.get(DBPUtilitiesConstants.SECURITY_ATTRIBUTES)
                                .getAsJsonObject();
                        dbxUsrAttr = new Record();
                        dbxUsrAttr.setId(DBPUtilitiesConstants.USR_ATTR);
                        dbxSecurityAttr = new Record();
                        dbxSecurityAttr.setId(DBPUtilitiesConstants.SECURITY_ATTRIBUTES);
                        for (Entry<String, JsonElement> entry : userAttributes.entrySet()) {
                            dbxUsrAttr.addParam(entry.getKey(), entry.getValue().getAsString());
                        }
                        for (Entry<String, JsonElement> entry : securityAttributes.entrySet()) {
                            dbxSecurityAttr.addParam(entry.getKey(), entry.getValue().getAsString());
                        }
                        if (isDeviceRegistered) {
                            dbxUsrAttr.addParam(new Param(DBPUtilitiesConstants.IS_DEVICE_REGISTERED, "true"));
                        } else {
                            dbxUsrAttr.addParam(new Param(DBPUtilitiesConstants.IS_DEVICE_REGISTERED, "false"));
                        }
                        retValue.addRecord(dbxUsrAttr);
                        retValue.addRecord(dbxSecurityAttr);

                        return retValue;
                    }
                }
            }
            setError(retValue, ErrorCodeEnum.ERR_10524.getErrorCodeAsString(), ErrorCodeEnum.ERR_10524.getMessage());
        } else {
            // if (isDeviceRegistered && null != dbxUsrAttr) {
            // diagnostic.prepareDebug("Device is registered " + isDeviceRegistered + " MFA Triggered :" + !isDeviceRegistered).log();
            // dbxUsrAttr.addParam(new Param(DBPUtilitiesConstants.IS_DEVICE_REGISTERED, "true"));
            // retValue.addRecord(dbxUsrAttr);
            // retValue.addRecord(dbxSecurityAttr);
            // return retValue;
            // } else {
            getMFAModeforRequest();

            diagnostic.prepareDebug("Device is registered " + isDeviceRegistered + " MFA Triggered :" + !isDeviceRegistered).log();
            if (!isAppActionValid() || ((mfaConfigurationUtil != null) && !mfaConfigurationUtil.isValidMFA())) {
                setError(retValue, ErrorCodeEnum.ERR_10525.getErrorCodeAsString(),
                        getDbpErrMsg() + "  " + ErrorConstants.UNABLE_TO_DETERMINE_C360_CONFIGURATION);
                return retValue;
            }

            if ((!isAppActionPresent() || (mfaConfigurationUtil == null)
                    || (!isMFARequired() && null != dbxUsrAttr))) {
                dbxUsrAttr.addParam(new Param(DBPUtilitiesConstants.IS_DEVICE_REGISTERED, "false"));
                retValue.addRecord(dbxUsrAttr);
                retValue.addRecord(dbxSecurityAttr);
                return retValue;
            } else if (null != dbxUsrAttr) {
                retValue.addParam(new Param("is_mfa_enabled", "true", Param.BOOLEAN_CONST));
                retValue.addParam("user_id", dbxUsrAttr.getParamValueByName("user_id"));
                Record mfa_meta = new Record();
                mfa_meta.setId("mfa_meta");
                setserviceMFAAttributes(mfa_meta, dbxUsrAttr, dbxSecurityAttr);

                if (mfa_meta.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)
                        || mfa_meta.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_CODE_KEY)) {
                    String dbpErrorMesage = mfa_meta.getParamValueByName(ErrorCodeEnum.ERROR_MESSAGE_KEY);
                    String dbpErrorCode = mfa_meta.getParamValueByName(ErrorCodeEnum.ERROR_CODE_KEY);
                    setError(retValue, dbpErrorCode, dbpErrorMesage);
                } else {

                    retValue.addRecord(mfa_meta);
                }
            }
            // }
        }

        return retValue;
    }

    private void setError(Result retValue, String dbxErrorCode, String dbxErrMessage) {

        retValue.addParam(new Param("errmsg", dbxErrMessage, "String"));
        retValue.addParam(new Param("backend_error_code", dbxErrorCode, "int"));
        retValue.addParam(new Param("backend_error_message", dbxErrMessage, "String"));
        retValue.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "401", "int"));
        retValue.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "20921", "int"));
    }

    // public JsonObject getCommunicationDataJson(boolean isOnlyPrimary, boolean sendMaskedToo) {
    //
    // diagnostic.prepareDebug("Inside get communication data Json \n send Only Primary -> " + isOnlyPrimary
    // + ".log(); sendMaskedToo -> " + sendMaskedToo);
    //
    // if (StringUtils.isBlank(user_id)) {
    // return communication;
    // }
    //
    // CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
    // communicationDTO.setCustomer_id(user_id);
    //
    // Result result = new Result();
    //
    // DBXResult dbxResult = new DBXResult();
    // CommunicationBackendDelegate backendDelegate =
    // DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
    // if (isOnlyPrimary) {
    // dbxResult = backendDelegate.getPrimaryCommunicationDetails(communicationDTO, new HashMap<String, Object>());
    // }
    // else {
    // dbxResult = backendDelegate.getCommunicationDetails(communicationDTO, new HashMap<String, Object>());
    // }
    //
    // if(dbxResult.getResponse() != null) {
    // JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
    // result = JSONToResult.convert(jsonObject.toString());
    // }
    //
    // if (!HelperMethods.hasRecords(result)) {
    // return communication;
    // }
    //
    // JsonObject communication = new JsonObject();
    //
    // JsonArray phone = new JsonArray();
    // JsonArray email = new JsonArray();
    // JsonObject contact = new JsonObject();
    //
    // for (Record record : result.getAllDatasets().get(0).getAllRecords()) {
    // contact = new JsonObject();
    // if (record.getParam(DBPUtilitiesConstants.TYPE_ID).getValue().equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)
    // && StringUtils.isNotBlank(record.getParam(DBPUtilitiesConstants.VALUE).getValue())) {
    // if (sendMaskedToo) {
    // contact.addProperty("masked",
    // OTPUtil.getMaskedEmail(record.getParam(DBPUtilitiesConstants.VALUE).getValue()));
    // }
    // contact.addProperty(MFAConstants.UNMASKED, record.getParam(DBPUtilitiesConstants.VALUE).getValue());
    // email.add(contact);
    // } else if (record.getParam(DBPUtilitiesConstants.TYPE_ID).getValue()
    // .equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)
    // && StringUtils.isNotBlank(record.getParam(DBPUtilitiesConstants.VALUE).getValue())) {
    //
    // String mobile = record.getParam(DBPUtilitiesConstants.VALUE).getValue();
    // mobile = OTPUtil.processMobile(mobile);
    // if (sendMaskedToo) {
    // contact.addProperty("masked", OTPUtil.getMaskedPhoneNumber(mobile));
    // }
    // contact.addProperty(MFAConstants.UNMASKED, mobile);
    // phone.add(contact);
    // }
    // }
    // if (phone.size() > 0 || email.size() > 0) {
    // if (phone.size() > 0)
    // communication.add(MFAConstants.PHONE, phone);
    // if (email.size() > 0)
    // communication.add(MFAConstants.EMAIL, email);
    //
    // diagnostic.prepareDebug("Response from getCustomerCommunication : " + communication.toString()).log();
    //
    // return communication;
    // }
    //
    // diagnostic.prepareDebug("Response from getCustomerCommunication : " + null).log();
    //
    // return null;
    // }

    private Record getOTPAttributes(String communicationType, Record dbxUsrAttr) {

        diagnostic.prepareDebug("communicationType :" + communicationType).log();
        if (communicationType.equals(MFAConstants.DISPLAY_ALL)) {

            communicationRecord = getCommunicationDataRecord(false, true, dbxUsrAttr);
            return communicationRecord;
        } else {
            primaryCommunicationRecord = getCommunicationDataRecord(true, true, dbxUsrAttr);

            return primaryCommunicationRecord;
        }
    }

    private Record getCommunicationDataRecord(boolean isOnlyPrimary, boolean sendMaskedToo, Record dbxUsrAttr) {
        Record communication = null;
        diagnostic.prepareDebug(
                "Inside get communication data record \n send Only Primary -> " + isOnlyPrimary + ".log(); sendMaskedToo -> "
                        + sendMaskedToo + "; dbxUserAttributes -> " + ResultToJSON.convertRecord(dbxUsrAttr));
        if (StringUtils.isBlank(user_id)) {
            return communication;
        }

        Dataset phone = new Dataset();
        phone.setId(MFAConstants.PHONE);
        Dataset email = new Dataset();
        email.setId(MFAConstants.EMAIL);
        Record contact = new Record();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + user_id;

        if (isOnlyPrimary) {
            contact = new Record();
            if (dbxUsrAttr.getNameOfAllParams().contains("Email")
                    && StringUtils.isNotBlank(dbxUsrAttr.getParam("Email").getValue())) {
                if (sendMaskedToo) {
                    contact.addParam("masked", OTPUtil.getMaskedEmail(dbxUsrAttr.getParam("Email").getValue()));
                }
                contact.addParam(MFAConstants.UNMASKED, dbxUsrAttr.getParam("Email").getValue());
                email.addRecord(contact);
            }

            contact = new Record();
            if (dbxUsrAttr.getNameOfAllParams().contains("Phone")
                    && StringUtils.isNotBlank(dbxUsrAttr.getParam("Phone").getValue())) {

                String mobile = dbxUsrAttr.getParam("Phone").getValue();
                mobile = OTPUtil.processMobile(mobile);
                if (sendMaskedToo) {
                    contact.addParam("masked", OTPUtil.getMaskedPhoneNumber(mobile));
                }
                contact.addParam(MFAConstants.UNMASKED, mobile);
                phone.addRecord(contact);
            }
        } else {
            Result result = new Result();

            CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
            communicationDTO.setCustomer_id(user_id);

            DBXResult dbxResult = new DBXResult();
            CommunicationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
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

            if (!HelperMethods.hasRecords(result)) {
                return communication;
            }

            for (Record record : result.getAllDatasets().get(0).getAllRecords()) {
                contact = new Record();
                if (record.getParam(DBPUtilitiesConstants.TYPE_ID).getValue()
                        .equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)
                        && StringUtils.isNotBlank(record.getParam(DBPUtilitiesConstants.VALUE).getValue())) {

                    if (sendMaskedToo) {
                        contact.addParam("masked",
                                OTPUtil.getMaskedEmail(record.getParam(DBPUtilitiesConstants.VALUE).getValue()));
                    }
                    contact.addParam(MFAConstants.UNMASKED, record.getParam(DBPUtilitiesConstants.VALUE).getValue());
                    email.addRecord(contact);
                } else if (record.getParam(DBPUtilitiesConstants.TYPE_ID).getValue()
                        .equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)
                        && StringUtils.isNotBlank(record.getParam(DBPUtilitiesConstants.VALUE).getValue())) {

                    String mobile = record.getParam(DBPUtilitiesConstants.VALUE).getValue();
                    mobile = OTPUtil.processMobile(mobile);
                    if (sendMaskedToo) {
                        contact.addParam("masked", OTPUtil.getMaskedPhoneNumber(mobile));
                    }
                    contact.addParam(MFAConstants.UNMASKED, mobile);
                    phone.addRecord(contact);
                }
            }
        }

        if (phone.getAllRecords().size() > 0 || email.getAllRecords().size() > 0) {
            communication = new Record();
            if (phone.getAllRecords().size() > 0) {
                communication.addDataset(phone);
            }
            if (email.getAllRecords().size() > 0) {
                communication.addDataset(email);
            }

            diagnostic.prepareDebug("Response from getCustomerCommunication : " + ResultToJSON.convertRecord(communication)).log();
            return communication;
        }

        diagnostic.prepareDebug("Response from getCustomerCommunication : " + null).log();
        return null;
    }

    public boolean isRetryAllowed(int retryCount) {

        return retryCount >= mfaConfigurationUtil.sacMaxResendRequestsAllowed();
    }

    public Result addRequestAttributes(Result result, int retryCount) {

        int maximumAllowedRetries = mfaConfigurationUtil.sacMaxResendRequestsAllowed();
        result.addParam(new Param("sacMaxResendRequestsAllowed", maximumAllowedRetries + "",
                DBPUtilitiesConstants.STRING_TYPE));
        result.addParam(new Param("remainingResendAttempts", (maximumAllowedRetries - (retryCount + 1)) + "",
                DBPUtilitiesConstants.STRING_TYPE));
        result.addParam(new Param("sacCodeLength", mfaConfigurationUtil.getOTPLength() + "",
                DBPUtilitiesConstants.STRING_TYPE));

        return result;
    }

    public Result addVerifyAttributes(Result result, Result retRes) {

        retRes.addParam(
                new Param("maxFailedAttemptsAllowed", mfaConfigurationUtil.maxFailedAttemptsAllowed() + "", "int"));
        String str = HelperMethods.getFieldValue(result, "InvalidAttempt");
        if (StringUtils.isNotBlank(str)) {
            int InvalidAttempt = Integer.parseInt(str);
            retRes.addParam(new Param("remainingFailedAttempts",
                    (mfaConfigurationUtil.maxFailedAttemptsAllowed() - (InvalidAttempt + 1)) + "", "int"));
            retRes.addParam(new Param("failedAttempts", (InvalidAttempt + 1) + "", "int"));
            if ((mfaConfigurationUtil.maxFailedAttemptsAllowed() - (InvalidAttempt + 1)) <= 0) {
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

        int maxAttemptsAllowed = mfaConfigurationUtil.maxFailedAttemptsAllowed();
        resultJson.addProperty("maxFailedAttemptsAllowed", maxAttemptsAllowed + "");

        if ((mfaConfigurationUtil.maxFailedAttemptsAllowed() - (retryCount + 1)) > 0) {
            resultJson.addProperty("remainingFailedAttempts",
                    (mfaConfigurationUtil.maxFailedAttemptsAllowed() - (retryCount + 1)) + "");
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

    private int getSecurityQuestionsRetry() {

        if (mfaServiceUtil == null) {
            JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                    .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

            String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            String serviceName = mfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();
            getMFaModeforRequestfromDB(serviceKey, serviceName);
        }

        return mfaServiceUtil.getSecurityQuestionsRetry();

    }

    private JsonArray getSecurityQuestionsfromDB() {

        if (mfaServiceUtil == null) {
            JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                    .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

            String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            String serviceName = mfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();
            getMFaModeforRequestfromDB(serviceKey, serviceName);
        }

        return mfaServiceUtil.getSecurityQuestionsArrayfromDB();
    }

    public JsonObject getSecurityQuestionsJsonObjectFromDB() {

        if (mfaServiceUtil == null) {
            JsonObject mfaAttributes = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject()
                    .get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject();

            String serviceKey = mfaAttributes.get(MFAConstants.SERVICE_KEY).getAsString();
            String serviceName = mfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();
            getMFaModeforRequestfromDB(serviceKey, serviceName);
        }

        return mfaServiceUtil.getSecurityQuestionsJsonfromDB();
    }

    private JsonArray getSecurityQuestionsAttributesJsonArray() {
        return securityQuestionsUtil
                .getSecurityQuestionsAttributesJsonArray(mfaConfigurationUtil.getnumberofSecurityQuestions());
    }

    public boolean isMFARequired() {
        return mfaConfigurationUtil.isMFARequired() && isMFARequiredForLogin();
    }

    public boolean shouldLockUser() {

        boolean status = mfaConfigurationUtil.shouldLockUser();

        if (status) {
            lockUser();
        }

        return status;
    }

    public JsonObject getRequestPayload(String serviceKey, String serviceName) {

        if (mfaServiceUtil == null) {
            getMFAServiceResult(serviceKey, serviceName);
        }

        return mfaServiceUtil.getRequestPayload();

    }

    public JsonObject getRequestPayload(Result result) {

        String payload = HelperMethods.getFieldValue(result, "payload");

        if (StringUtils.isNotBlank(payload)) {
            try {
                return new JsonParser().parse(CryptoText.decrypt(payload)).getAsJsonObject();
            } catch (Exception e) {

                alert.prepareError("Caught exception while getting getRequestPayload : ", e).log();
            }
        }

        return new JsonObject();
    }

    public boolean isValidServiceKey(String serviceKey, String serviceName) {

        if (mfaServiceUtil == null) {
            getMFAServiceResult(serviceKey, serviceName);
        }

        return mfaServiceUtil.isValidServiceKey(getServiceKeyExpiretime());

    }

    public boolean isStateVerified(String serviceKey, String serviceName) {
        if (mfaServiceUtil == null) {
            getMFaModeforRequestfromDB(serviceKey, serviceName);
        }

        return mfaServiceUtil.isStateVerified();
    }

    public boolean isStateVerified(Result result) {

        if (HelperMethods.hasRecords(result) && !isServiceKeyExpired(result)) {
            return "true".equals(HelperMethods.getFieldValue(result, MFAConstants.IS_VERIFIED));
        }
        return false;

    }

    private boolean isServiceKeyExpired(Result result) {

        String string = HelperMethods.getFieldValue(result, "Createddts");

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
                diagnostic.prepareDebug("IS_SERVICE_KEY_EXPIRED : " + false).log();
                return false;
            }
        }
        diagnostic.prepareDebug("IS_SERVICE_KEY_EXPIRED : " + true).log();
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
            alert.prepareError("Caught exception while Getting getServiceKeyExpiretime : ", e).log();
        }

        return 10;
    }

    public boolean isPrimaryCommunication() {

        return mfaConfigurationUtil.getCommunicationType().equals(MFAConstants.DISPLAY_ALL);
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

            alert.prepareError("Caught exception while getting path URL: ", e).log();
        }
        if (StringUtils.isNotBlank(requestLimit)) {
            return Integer.parseInt(requestLimit);
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

        if (jsonArray.size() != mfaConfigurationUtil.getnumberofSecurityQuestions()) {
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

        if (!status || questionsID.size() != mfaConfigurationUtil.getnumberofSecurityQuestions()) {
            ErrorCodeEnum.ERR_10506.setErrorCode(resultJson);
            return resultJson;
        }

        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("securityQuestions", jsonArray.toString());

        status = verifySecurityQuestionsFromDB(jsonArray);

        JsonObject mfaAttributes = new JsonObject();

        if (!status) {
            int retryCount = getSecurityQuestionsRetry();
            if (retryCount < mfaConfigurationUtil.maxFailedAttemptsAllowed()) {
                addVerifyAttributes(mfaAttributes, retryCount);
                if (retryCount + 1 < mfaConfigurationUtil.maxFailedAttemptsAllowed()) {
                    JsonArray securityQuestionsArray = getSecurityQuestionsAttributesJsonArray();
                    OTPUtil.updateSecurityQuestionsRetryCount(retryCount, securityQuestionsArray, requestManager);
                    mfaAttributes.add("securityQuestions", securityQuestionsArray);
                }
            } else {
                ErrorCodeEnum.ERR_10072.setErrorCode(resultJson);
            }
            resultJson.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);

            return resultJson;
        }

        resultJson.addProperty("success", "success");
        return resultJson;
    }

    public JsonObject verifyPIN(JsonObject PIN) {

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
				// set MFA attributes
				Record rc = result.getRecordById("MFAAttributes");
				JsonObject mfaAttributes = new JsonObject();
				mfaAttributes.addProperty("maxFailedAttemptsAllowed",
						rc.getParamValueByName("maxFailedAttemptsAllowed"));
				mfaAttributes.addProperty("remainingFailedAttempts", rc.getParamValueByName("remainingFailedAttempts"));
				mfaAttributes.addProperty("failedAttempts", rc.getParamValueByName("failedAttempts"));

				if (rc.getParamValueByName("failedAttempts") == rc.getParamValueByName("maxFailedAttemptsAllowed")) {
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
    
    private boolean verifySecurityQuestionsFromDB(JsonArray jsonArray) {
        return securityQuestionsUtil.verifySecurityQuestionsFromDB(jsonArray);
    }

    public String getLockoutTime() {

        int accountLockoutTime = 0;

        if (dcRequest != null) {
            accountLockoutTime = new PasswordLockoutSettings(dcRequest).getAutoUnLockPeriod();
        } else if (requestManager != null) {
            accountLockoutTime = new PasswordLockoutSettings(requestManager).getAutoUnLockPeriod();
        }

        String time = "";

        if (accountLockoutTime != 0) {

            int minutes = accountLockoutTime % 60;
            if (minutes != 0) {
                time += minutes + " Minutes ";
            }

            int hours = accountLockoutTime / 60;
            int days = hours / 24;
            hours = hours % 24;
            if (hours != 0) {
                time = hours + " Hours " + time;
            }
            if (days != 0) {
                time = days + " Days " + time;
            }

        } else
            time = 30 + " Minutes";

        return time;
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
            if (null != dcRequest) {
                user_id = StringUtils.isNotBlank(HelperMethods.getCustomerIdFromKnownUserToken(dcRequest))
                        ? HelperMethods.getCustomerIdFromKnownUserToken(dcRequest)
                        : HelperMethods.getCustomerIdFromSession(dcRequest);
            } else if (null != requestManager) {
                user_id = StringUtils.isNotBlank(HelperMethods.getCustomerIdFromKnownUserToken(requestManager))
                        ? HelperMethods.getCustomerIdFromKnownUserToken(requestManager)
                        : HelperMethods.getCustomerIdFromSession(requestManager);
            }
            if (StringUtils.isBlank(user_id)) {
                return;
            }
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

    private JSONObject getReportingParams() {

        String reportingParams = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        try {
            reportingParams = URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting getReportingParams : ", e).log();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(reportingParams);
        } catch (JSONException ex) {
            ex.getMessage();
        }

        return jsonObject;
    }

    public boolean isAppActionValid() {

        if (getDbpErrCode() != null) {
            if ("21334".equals(getDbpErrCode()) || "20866".equals(getDbpErrCode()) || "21318".equals(getDbpErrCode())
                    || "20888".equals(getDbpErrCode()) || "21341".equals(getDbpErrCode())) {
                diagnostic.prepareDebug("Invalid AppAction returned from C360").log();
                return false;
            }

        }

        return true;
    }

    public boolean isAppActionPresent() {

        if (getDbpErrCode() != null) {
            if ("21341".equals(getDbpErrCode())) {
                diagnostic.prepareDebug("App Action not present in C360").log();
                return false;
            }
        }

        diagnostic.prepareDebug("App Action present in C360").log();
        return true;
    }

    public boolean isMFARequiredForLogin() {

        JSONObject jsonObject = getReportingParams();

        if (jsonObject.has("aid") && jsonObject.getString("aid").equals("ConsumerLend")) {

            diagnostic.prepareDebug("isMFARequiredForLogin : " + false).log();
            return false;
        }

        diagnostic.prepareDebug("isMFARequiredForLogin : " + true).log();
        return true;
    }

    public Record setserviceMFAAttributes(Record mfa_meta, Record dbxUsrAttr, Record dbxSecurityAttr) {

        Record mfaAttributes = new Record();
        mfaAttributes.setId(MFAConstants.MFA_ATTRIBUTES);
        JsonObject payload = getRequestPayload(dbxUsrAttr, dbxSecurityAttr);

        String serviceKey = null;

        Record communication = null;
        Dataset securityQuestions = null;
        String mfaType = mfaConfigurationUtil.getMFAType();
        diagnostic.prepareDebug("MFAType is " + mfaType).log();
        if (mfaType.equals(MFAConstants.SECURE_ACCESS_CODE)) {
            String communicationType = mfaConfigurationUtil.getCommunicationType();
            communication = getOTPAttributes(communicationType, dbxUsrAttr);

            if (communication == null) {
                ErrorCodeEnum.ERR_10504.setErrorCode(mfa_meta, "Invalid communication Information");
                return mfa_meta;
            }

            JSONObject referenceCommunication =
                    new JSONObject((ResultToJSON.convertRecord(communication)).toString());

            JsonObject referenceIdMapings = new JsonObject();
            JSONArray phone = null;
            JSONArray email = null;
            int phoneSize = 0;
            int emailSize = 0;
            if (referenceCommunication.has("phone")) {
                phone = referenceCommunication.getJSONArray("phone");
                phoneSize = phone.length();
            }
            if (referenceCommunication.has("email")) {
                email = referenceCommunication.getJSONArray("email");
                emailSize = email.length();
            }
            for (int i = 0; i < Math.max(phoneSize, emailSize); i++) {
                if (i < phoneSize) {
                    String randomId = String.valueOf(HelperMethods.getNumericId());
                    referenceIdMapings.addProperty(randomId,
                            ((JSONObject) phone.get(i)).get("unmasked").toString());
                    ((JSONObject) phone.get(i)).put("referenceId", randomId);
                    ((JSONObject) phone.get(i)).put("unmasked", randomId);
                }
                if (i < emailSize) {
                    String randomId = String.valueOf(HelperMethods.getNumericId());
                    referenceIdMapings.addProperty(randomId,
                            ((JSONObject) email.get(i)).get("unmasked").toString());
                    ((JSONObject) email.get(i)).put("referenceId", randomId);
                    ((JSONObject) email.get(i)).put("unmasked", randomId);
                }
            }
            serviceKey = getServiceKey(payload, referenceIdMapings, null);
            if (serviceKey == null) {
                ErrorCodeEnum.ERR_10504.setErrorCode(mfa_meta);
                return mfa_meta;
            }
            if (!communicationType.equals(MFAConstants.DISPLAY_ALL)) {
                JsonObject OTP = new JsonObject();
                Dataset phones = communication.getDatasetById(MFAConstants.PHONE);
                Dataset emails = communication.getDatasetById(MFAConstants.EMAIL);
                if (HelperMethods.hasRecords(phones)) {
                    OTP.addProperty(MFAConstants.PHONE, phones.getRecord(0).getParamValueByName(MFAConstants.UNMASKED));
                }

                if (HelperMethods.hasRecords(emails)) {
                    OTP.addProperty(MFAConstants.EMAIL, emails.getRecord(0).getParamValueByName(MFAConstants.UNMASKED));
                }

                mfa_meta = OTPUtil.requestOTP(OTP, mfa_meta, serviceKey, dcRequest);
                if (mfa_meta.getIdOfAllRecords().contains(MFAConstants.MFA_ATTRIBUTES)) {
                    mfaAttributes = mfa_meta.getRecordById(MFAConstants.MFA_ATTRIBUTES);
                } else {
                    return mfa_meta;
                }
            }

            mfaAttributes.addParam(new Param("communicationType", communicationType));
            mfaAttributes.addParam(new Param("sacPreferenceCriteria", communicationType));
            communication = HelperMethods.constructRecordFromJSONObject(referenceCommunication);
            communication.setId(MFAConstants.CUSTOMER_COMMUNICATION);
            mfaAttributes.addRecord(communication);
        } else if (mfaType.equals(MFAConstants.SECURITY_QUESTIONS)) {
            securityQuestions = getSecurityQuestionsAttributesDataset();
            serviceKey = getServiceKey(payload, null, securityQuestions);
            if (serviceKey == null) {
                ErrorCodeEnum.ERR_10504.setErrorCode(mfa_meta);
                return mfa_meta;
            }
            securityQuestions.setId("securityQuestions");
            mfaAttributes.addDataset(securityQuestions);
        }

        mfaAttributes.addParam("isMFARequired", "true");
        mfaAttributes.addParam("MFAType", mfaType);
        mfaAttributes.addParam(MFAConstants.SERVICE_KEY, serviceKey);
        mfaAttributes.addParam(DBPUtilitiesConstants.IS_DEVICE_REGISTERED, "false");
        mfaAttributes.setId(MFAConstants.MFA_ATTRIBUTES);
        mfa_meta.addRecord(mfaAttributes);
        return mfa_meta;
    }

    private Dataset getSecurityQuestionsAttributesDataset() {

        return securityQuestionsUtil
                .getSecurityQuestionsAttributesDataset(mfaConfigurationUtil.getnumberofSecurityQuestions());
    }

    private String getServiceKey(JsonObject requestpayload, JsonElement securityQuestionsArray,
            Dataset securityQuestionsDataset) {
        return MFAServiceUtil.getServiceKey(requestpayload, securityQuestionsArray, user_id,
                new JsonParser().parse(mfaConf.toString()), requestManager, dcRequest, securityQuestionsDataset);
    }

    private JsonObject getRequestPayload(Record dbxUsrAttr, Record dbxSecurityAttr) {

        JsonObject jsonObject = new JsonObject();
        JsonObject userAttr = new JsonObject();
        for (Param param : dbxUsrAttr.getAllParams()) {
            userAttr.addProperty(param.getName(), param.getValue());
        }

        JsonObject securityAttr = new JsonObject();
        for (Param param : dbxSecurityAttr.getAllParams()) {
            securityAttr.addProperty(param.getName(), param.getValue());
        }

        jsonObject.add(DBPUtilitiesConstants.USR_ATTR, userAttr);
        jsonObject.add(DBPUtilitiesConstants.SECURITY_ATTRIBUTES, securityAttr);
        JsonObject mfaAttributes = new JsonObject();
        mfaAttributes.addProperty(MFAConstants.SERVICE_NAME,
                new JsonParser().parse(dcRequest.getParameter(MFAConstants.MFA_ATTRIBUTES)).getAsJsonObject()
                        .get(MFAConstants.SERVICE_NAME).getAsString());
        jsonObject.add(MFAConstants.MFA_ATTRIBUTES, mfaAttributes);
        return jsonObject;
    }

    public void createEntryInStateChecker() {

        if (dcRequest != null) {
            HashMap<String, String> input = new HashMap<>();
            input.put(MFAConstants.SERVICE_KEY, dcRequest.getParameter(MFAConstants.SERVICE_KEY));
            input.put(MFAConstants.SERVICE_NAME, dcRequest.getParameter(MFAConstants.SERVICE_NAME));
            input.put(MFAConstants.IS_VERIFIED, "true");
            try {
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.MFA_SERVICE_UPDATE);
            } catch (HttpCallException e) {
                alert.prepareError("Caught exception while updating MFA SERIVCE: ", e).log();
            }
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

    public boolean isRecentProspect(String userID) {

        String filter = "id" + DBPUtilitiesConstants.EQUAL + userID;

        try {
            Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERVERIFY_GET);
            return isRecentProspect(result);
        } catch (HttpCallException e) {

            alert.prepareError("Caught exception while customerVerify GET : ", e).log();
        }

        return false;
    }

    private boolean isRecentProspect(Result result) {

        String string = HelperMethods.getFieldValue(result, "createdts");

        if (StringUtils.isNotBlank(string)) {
            Date createdts = HelperMethods.getFormattedTimeStamp(string);
            Calendar generatedCal = Calendar.getInstance();
            generatedCal.setTime(createdts);

            Date verifyDate = new Date();
            Calendar verifyingCal = Calendar.getInstance();
            verifyingCal.setTime(verifyDate);

            int otpValidityTime = getProspectExpiretime();
            generatedCal.add(Calendar.MINUTE, otpValidityTime);

            long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
            long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

            if (GeneratedMilliSeconds > verifyingMilliSeconds) {
                return false;
            }
        }

        return true;
    }

    private int getProspectExpiretime() {

        try {
            if (dcRequest != null) {
                return Integer.parseInt(URLFinder.getPathUrl(URLConstants.PROSPECT_LOGIN_EXPIRE_TIME, dcRequest));
            } else {
                return Integer.parseInt(URLFinder.getPathUrl(URLConstants.PROSPECT_LOGIN_EXPIRE_TIME, requestManager));
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting getProspectExpiretime : ", e).log();
        }

        return 5;
    }

    public void setUser_id(String userID) {
        this.user_id = userID;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public String getSMSText(String otp) {
        return mfaConfigurationUtil.getSMSText(otp, getRequestPayload());
    }

    public String getEmailBody(String otp) {
        // TODO Auto-generated method stub
        return mfaConfigurationUtil.getEmailBody(otp, getRequestPayload());
    }

}
