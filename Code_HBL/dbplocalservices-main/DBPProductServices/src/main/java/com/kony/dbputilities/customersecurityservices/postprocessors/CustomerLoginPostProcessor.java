package com.kony.dbputilities.customersecurityservices.postprocessors;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.customersecurityservices.CreateCustomerAddress;
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.CreateCustomerPreference;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.LoginMFAUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class CustomerLoginPostProcessor implements DataPostProcessor2 {
    private static Alert alert;
    private static Diagnostic diagnostic;
    
    @Override
    public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {
		Log4j2Configurator.getInstance();
        Result retValue = new Result();
        if(result.getParamValueByName("isDBXUser")!=null)
        retValue.addParam(new Param("isDBXUser", result.getParamValueByName("isDBXUser")));
    	if(result.getParamValueByName("migratedUser")!=null) 
        retValue.addParam(new Param("migratedUser", result.getParamValueByName("migratedUser")));
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        String dbxErrorCode = HelperMethods.getParamValue(result.getParamByName(DBPUtilitiesConstants.ERROR_CODE));

        String dbxErrMessage = HelperMethods.getParamValue(result.getParamByName("errorMessage"));

        String mfa_key = dcRequest.getParameter("mfa_key");

        Record dbxUsrAttr = result.getRecordById(DBPUtilitiesConstants.USR_ATTR);
        Record coreUsrAttr = result.getRecordById(DBPUtilitiesConstants.CORE_ATTR);
        Record dbxSecurityAttr = result.getRecordById(DBPUtilitiesConstants.SECURITY_ATTRIBUTES);
        Record coreSecurityAttr = result.getRecordById(DBPUtilitiesConstants.CORE_SECURITY_ATTRIBUTES);
        LoginMFAUtil mfaUtililty = null;
        String legalEntityId = "";
        if(dbxUsrAttr !=null)
        {
        	legalEntityId = dbxUsrAttr.getParamValueByName("defaultLegalEntity");
        	if(StringUtils.isBlank(legalEntityId)) {
        		legalEntityId = dbxUsrAttr.getParamValueByName("homeLegalEntity");
        	}
        }
        if ((null != dbxUsrAttr && StringUtils.isBlank(dbxErrorCode)) || StringUtils.isNotBlank(mfa_key)) {

            String userID = null;
            String username = null;
            String userType = null;
            String isCSRAssistMode = null;

            if (dbxUsrAttr != null) {
                userID = dbxUsrAttr.getParamValueByName("customer_id");
                username = dbxUsrAttr.getParamValueByName("UserName");
                userType = dbxUsrAttr.getParamValueByName("CustomerType_id");
                isCSRAssistMode = dbxUsrAttr.getParamValueByName("isCSRAssistMode");
                dbxUsrAttr.addParam(new Param("customerTypeId", userType, MWConstants.STRING));
            }

            mfaUtililty = new LoginMFAUtil(dcRequest, username);
            mfaUtililty.setUser_id(userID);
            
            if (dbxSecurityAttr != null && (isCSRAssistMode == null
                    || StringUtils.equals(isCSRAssistMode, "false"))) {
            	
                getCustomerPermissions(userID, dbxSecurityAttr, dcRequest, HelperMethods.getCustomerTypes().get("Prospect").equals(userType),legalEntityId);
            }

            retValue = mfaUtililty.checkAndAddMFAAttributes(retValue, dbxUsrAttr, dbxSecurityAttr, mfa_key, dcResponse);

            diagnostic.prepareDebug("Response from checkAndAddMFAAttributes is : " + ResultToJSON.convert(retValue)).log();

            pushAlerts(dcRequest, result, dcResponse, dbxUsrAttr, retValue);

        } else {
            if (null != coreUsrAttr || StringUtils.isNotBlank(mfa_key)) {

                String userID = null;
                String username = null;

                if (null != coreUsrAttr) {
                    coreUsrAttr.setId(DBPUtilitiesConstants.USR_ATTR);
                    String userType = null;
                    userType = coreUsrAttr.getParamValueByName("CustomerType_id");
                    coreUsrAttr.addParam(new Param("customerTypeId", userType, MWConstants.STRING));
                    coreSecurityAttr.setId(DBPUtilitiesConstants.SECURITY_ATTRIBUTES);
                    if (coreUsrAttr != null)
                        userID = coreUsrAttr.getParamValueByName("customer_id");
                    getCustomerPermissions(userID, coreUsrAttr, dcRequest, HelperMethods.getCustomerTypes().get("Prospect").equals(userType),legalEntityId);

                    retValue.addRecord(coreUsrAttr);
                    retValue.addRecord(coreSecurityAttr);
                    String coreUserId = HelperMethods.getFieldValue(coreUsrAttr, "user_id");
                    String filter = "BackendId" + DBPUtilitiesConstants.EQUAL + coreUserId;

                    Map<String, String> inputParams = new HashMap<>();
                    inputParams.put(DBPUtilitiesConstants.FILTER, filter);
                    JsonObject backendIdentifier = HelperMethods.callApiJson(dcRequest, inputParams,
                            HelperMethods.getHeaders(dcRequest), URLConstants.BACKENDIDENTIFIER_GET);
                    String customerId = HelperMethods.generateUniqueCustomerId(dcRequest);
                    if (!backendIdentifier.isJsonNull() && backendIdentifier.has("backendidentifier")
                            && backendIdentifier.get("backendidentifier").getAsJsonArray().size() > 0) {
                        JsonArray jsonArray = backendIdentifier.get("backendidentifier").getAsJsonArray();
                        Record userAttributes = retValue.getRecordById(DBPUtilitiesConstants.USR_ATTR);
                        String IdentifierString = jsonArray.toString();
                        userAttributes.addParam(
                                new Param("backendidentifier", IdentifierString, DBPUtilitiesConstants.STRING_TYPE));
                        customerId = jsonArray.get(0).getAsJsonObject().get("Customer_id").getAsString();
                    } else {
                        asyncUpdateCustomerDataInDBX(dcRequest, result, customerId);
                        createBackendIdentifier(dcRequest, retValue, customerId, coreUserId);
                    }
                    coreUsrAttr.addParam(new Param("user_id", customerId, "String"));
                    coreUsrAttr.addParam(new Param("customer_id", customerId, "String"));
                    dbxUsrAttr = retValue.getRecordById(DBPUtilitiesConstants.USR_ATTR);
                    dbxSecurityAttr = retValue.getRecordById(DBPUtilitiesConstants.SECURITY_ATTRIBUTES);
                    userID = customerId;
                    username = dbxUsrAttr.getParamValueByName("UserName");
                    userType = dbxUsrAttr.getParamValueByName("CustomerType_id");
                    if (StringUtils.isBlank(userType)) {
                        userType = "TYPE_ID_RETAIL";
                    }
                    dbxUsrAttr.addParam(new Param("customerTypeId", userType, MWConstants.STRING));

                }
                mfaUtililty = new LoginMFAUtil(dcRequest, username);
                mfaUtililty.setUser_id(userID);
                retValue = mfaUtililty.checkAndAddMFAAttributes(retValue, dbxUsrAttr, dbxSecurityAttr, mfa_key,
                        dcResponse);
                pushAlerts(dcRequest, result, dcResponse, dbxUsrAttr, retValue);
                if (!retValue.getNameOfAllParams().contains("errmsg")) {
                    retValue.addOpstatusParam(0);
                    retValue.addHttpStatusCodeParam(200);
                }

                return retValue;
            } else if (StringUtils.isNotBlank(dbxErrorCode) || StringUtils.isNotBlank(dbxErrMessage)) {

            } else if (result.getNameOfAllParams().contains("core_message")) {
                dbxErrMessage = result.getParamValueByName("core_message");
                dbxErrorCode = ErrorCodeEnum.ERR_10160.getErrorCode() + "";
            }
            setError(retValue, dbxErrorCode, dbxErrMessage);
            pushAlerts(dcRequest, result, dcResponse, dbxUsrAttr, retValue);
        }

        diagnostic.prepareDebug("Response from LoginPostProcessor is : " + ResultToJSON.convert(retValue)).log();

        if (!retValue.getNameOfAllParams().contains("errmsg")) {
        	try{
                dbxUsrAttr.removeParamByName("UserName");
                retValue.removeRecordById(DBPUtilitiesConstants.USR_ATTR);
                retValue.addRecord(dbxUsrAttr);
            }
            catch(Exception e) {
            	alert.prepareError("Error occured while removing username from result.").log();
            }
            retValue.addOpstatusParam(0);
            retValue.addHttpStatusCodeParam(200);
        }
        return retValue;
    }

    public void getCustomerPermissions(String customerId, Record securityAttr,
            DataControllerRequest dcRequest, boolean isProspect, String legalEntityId) {
        try {
            Set<String> features;
            Set<String> actions;

            CustomerActionsBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
            Map<String, Object> map = dcRequest.getHeaderMap();
            map.put(InfinityConstants.isProspectFlow, isProspect);
            Map<String, Set<String>> securityAttributes =
                    businessDelegate.getSecurityAttributes(customerId, map,legalEntityId);
            
            actions = securityAttributes.get("actions");
            features = securityAttributes.get("features");

            if (null == actions || null == features) {
                actions = new HashSet<>();
                features = new HashSet<>();
            }

//            securityAttr.addParam(new Param("permissions", getJSONString(actions), MWConstants.STRING));
//            securityAttr.addParam(new Param("features", getJSONString(features), MWConstants.STRING));
            securityAttr.addParam(new Param("session_ttl", "-1", MWConstants.STRING));
        } catch (Exception e) {
            alert.prepareError(e.getMessage(), e).log();
//            securityAttr.addParam(new Param("permissions", "[]", MWConstants.STRING));
//            securityAttr.addParam(new Param("features", "[]", MWConstants.STRING));
        }

        diagnostic.prepareDebug("SecurityAttribues Response from getCustomerPermissions is "
                + ResultToJSON.convertRecord(securityAttr)).log();
    }

    private String getJSONString(Set<String> set) {
        return (new JSONArray(set.toString())).toString();
    }

    private boolean isBusinessAction(String roleType_id) {
        return roleType_id.equals("TYPE_ID_BUSINESS") || roleType_id.equals("TYPE_ID_BUSINESS");
    }

    private void createBackendIdentifier(DataControllerRequest dcRequest, Result retValue, String customerId,
            String coreUserId) {
        Map<String, String> backendIdentifiers = new HashMap<>();
        backendIdentifiers.put("id", UUID.randomUUID().toString());
        backendIdentifiers.put("Customer_id", customerId);
        backendIdentifiers.put("sequence_number", "1");
        backendIdentifiers.put("BackendId", coreUserId);
        backendIdentifiers.put("BackendType", "Core");
        backendIdentifiers.put("identifier_name", "customer_id");
        JsonObject backendIdentifier = new JsonObject();
        try {
            backendIdentifier = HelperMethods.callApiJson(dcRequest, backendIdentifiers,
                    HelperMethods.getHeaders(dcRequest), URLConstants.BACKENDIDENTIFIER_CREATE);
        } catch (HttpCallException e) {
            alert.prepareError("Caught exception while Creating backend identifier Create :", e).log();
        }
        if (!backendIdentifier.isJsonNull() && backendIdentifier.has("backendidentifier")
                && backendIdentifier.get("backendidentifier").getAsJsonArray().size() > 0) {
            JsonArray jsonArray = backendIdentifier.get("backendidentifier").getAsJsonArray();
            Record userAttributes = retValue.getRecordById(DBPUtilitiesConstants.USR_ATTR);
            String identifierString = jsonArray.toString();
            userAttributes
                    .addParam(new Param("backendidentifier", identifierString, DBPUtilitiesConstants.STRING_TYPE));
        }

    }

    private void setError(Result retValue, String dbxErrorCode, String dbxErrMessage) {
        retValue.addParam(new Param("errmsg", dbxErrMessage, "String"));
        retValue.addParam(new Param("backend_error_code", dbxErrorCode, "int"));
        retValue.addParam(new Param("backend_error_message", dbxErrMessage, "String"));
        retValue.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "401", "int"));
        retValue.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "20921", "int"));
    }

    private void pushAlerts(DataControllerRequest dcRequest, Result result, DataControllerResponse dcResponse,
            Record dbxUsrAttr, Result retValue) {

        String loginStatus = "failed";
        String userStatus = null;
        if (retValue != null && retValue.getRecordById("security_attributes") != null
                && retValue.getRecordById("security_attributes").getParamByName("session_token") != null
                && retValue.getRecordById(DBPUtilitiesConstants.USR_ATTR) != null) {
            loginStatus = "success";
        }

        if (result != null && result.getParamByName("errorCode") != null
                && result.getParamByName("errorCode").getValue().contains("10088")) {
            userStatus = "locked";
        }

        String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", dcRequest);

        if (enableEvents == null) {
            enableEvents = "true";
        }
        DataControllerRequest dcReq = null;
        dcReq = dcRequest;

        if (enableEvents.equalsIgnoreCase("true") && null != retValue) {
            if (dbxUsrAttr == null) {
                dbxUsrAttr = retValue.getRecordById(DBPUtilitiesConstants.USR_ATTR);
            }

            AsyncPushEvent(result, dcReq, dcResponse, loginStatus, userStatus, dbxUsrAttr, retValue);
        }
    }

    private static void AsyncPushEvent(Result result, DataControllerRequest request, DataControllerResponse response,
            String loginStatus, String userStatus, Record dbxUsrAttr, Result retValue) {

        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() {
                PushEvent(result, request, response, loginStatus, userStatus, dbxUsrAttr, retValue);
                return new Result();
            }
        };

        try {
            ThreadExecutor.getExecutor(request).execute(callable);
        } catch (InterruptedException e) {
            alert.prepareError("Caught exception while Executing Thread :", e).log();
            Thread.currentThread().interrupt();

        }

    }

    private static JsonObject setCSRRelatedParams(Record dbxUsrAttr, JsonObject customParams) {
        try {
            if (dbxUsrAttr != null && customParams != null) {
                if (dbxUsrAttr.getParamValueByName("CSR_Role") != null) {
                    customParams.addProperty("CSR_Role", dbxUsrAttr.getParamValueByName("CSR_Role"));
                }
                if (dbxUsrAttr.getParamValueByName("CSR_User_Id") != null) {
                    customParams.addProperty("CSR_User_Id", dbxUsrAttr.getParamValueByName("CSR_User_Id"));
                }
                if (dbxUsrAttr.getParamValueByName("CSR_Name") != null) {
                    customParams.addProperty("CSR_Name", dbxUsrAttr.getParamValueByName("CSR_Name"));
                }
                if (dbxUsrAttr.getParamValueByName("CSR_Role") != null) {
                    customParams.addProperty("CSR_Username", dbxUsrAttr.getParamValueByName("CSR_Username"));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while setting CSRRelatedParams :", e).log();
        }
        return customParams;
    }

    protected static void PushEvent(Result result, DataControllerRequest request, DataControllerResponse response,
            String loginStatus, String userStatus, Record dbxUsrAttr, Result retValue) {
    	
    	String mfaName = getMfaName(request);
    	if(StringUtils.equals(mfaName, "Origination"))
    		return; // Do not send events if request was initiated from Origination app
    	
        String eventType = "LOGIN";
        String username = null;
        if (dbxUsrAttr != null) {
            username = dbxUsrAttr.getParamValueByName("UserName");
        }
        String eventSubType = "LOGIN_ATTEMPT";
        String StatusId = "SID_EVENT_SUCCESS";
        JsonObject customParams = new JsonObject();
        customParams = setCSRRelatedParams(dbxUsrAttr, customParams);
        String sessionid = null;
        try {
            if (retValue != null && retValue.getRecordById(DBPUtilitiesConstants.SECURITY_ATTRIBUTES) != null) {
                sessionid = retValue.getRecordById(DBPUtilitiesConstants.SECURITY_ATTRIBUTES)
                        .getParamValueByName(DBPUtilitiesConstants.SESSION_TOKEN);
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting session token :", e).log();
        }
        if (sessionid != null) {
            customParams.addProperty("sessionId", sessionid);
        }

        if ((username == null || username.equals("") || username.length() < 2) && request != null) {
            username = request.getParameter("userid");
        }

        if (loginStatus != null && loginStatus.contains("failed")
                && (result != null && (result.getParamByName(DBPUtilitiesConstants.ERROR_CODE) != null
                        && result.getParamByName(DBPUtilitiesConstants.ERROR_CODE).getValue().contains("10095")))
                || (retValue != null && (retValue.getParamByName(DBPUtilitiesConstants.ERROR_CODE) != null
                        && retValue.getParamByName(DBPUtilitiesConstants.ERROR_CODE).getValue().contains("10095")))) {
            eventSubType = "LOGIN_ATTEMPT";
            StatusId = "SID_EVENT_FAILURE";
            String lastTwoDigits = "";
            String firstTwoDigits = "";
            if (username != null && username.length() > 2) {
                lastTwoDigits = username.substring(username.length() - 2);
                firstTwoDigits = username.substring(0, 2);
            }

            customParams.addProperty("MaskedUserName", firstTwoDigits + "XXXX" + lastTwoDigits);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String serverDate = dateFormat.format(new Date());

            customParams.addProperty("ServerDate", serverDate);
            Calendar cal = Calendar.getInstance();

            customParams.addProperty("ServerTime", timeFormat.format(cal.getTime()));
            EventsDispatcher.dispatch(request, response, eventType, eventSubType, "DbxUserLogin", StatusId, null,
                    username, customParams);
        }

        if (loginStatus != null && loginStatus.contains("failed")
                && (result != null && (result.getParamByName(DBPUtilitiesConstants.ERROR_CODE) != null
                        && result.getParamByName(DBPUtilitiesConstants.ERROR_CODE).getValue().contains("10097")))
                || (retValue != null && (retValue.getParamByName(DBPUtilitiesConstants.ERROR_CODE) != null
                        && retValue.getParamByName(DBPUtilitiesConstants.ERROR_CODE).getValue().contains("10097")))) {
            eventSubType = "LOGIN_UNAUTHORISED_ACCESS";
            StatusId = "SID_EVENT_FAILURE";
            String lastTwoDigits = "";
            String firstTwoDigits = "";
            if (username != null && username.length() > 2) {
                lastTwoDigits = username.substring(username.length() - 2);
                firstTwoDigits = username.substring(0, 2);
            }

            customParams.addProperty("MaskedUserName", firstTwoDigits + "XXXX" + lastTwoDigits);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String serverDate = dateFormat.format(new Date());

            customParams.addProperty("ServerDate", serverDate);
            Calendar cal = Calendar.getInstance();

            customParams.addProperty("ServerTime", timeFormat.format(cal.getTime()));
            EventsDispatcher.dispatch(request, response, eventType, eventSubType, "DbxUserLogin", StatusId, null,
                    username, customParams);

        }

        if (userStatus != null && userStatus.contains("locked")) {
            eventType = "LOGIN";
            eventSubType = "ACCOUNT_LOCKED";
            StatusId = "SID_EVENT_SUCCESS";
            EventsDispatcher.dispatch(request, response, eventType, eventSubType, "DbxUserLogin", StatusId, null,
                    username, customParams);
        }
        if (loginStatus != null && loginStatus.contains("success")) {
            eventType = "LOGIN";
            eventSubType = "LOGIN_ATTEMPT";
            StatusId = "SID_EVENT_SUCCESS";
            EventsDispatcher.dispatch(request, response, eventType, eventSubType, "DbxUserLogin", StatusId, null,
                    username, customParams);
        }
    }

    private static void asyncUpdateCustomerDataInDBX(DataControllerRequest dcRequest, Result result,
            String customerId) {

        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() {
                String customerID = null;
                Record user = null;
                Map<String, String> input = null;
                try {
                    Result result1 = getCustomerFromCore(result);
                    user = result1.getAllRecords().get(0);
                    input = getInputParams(user);
                    input.put("id", customerId);
                    Result customer = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_CREATE);
                    if (HelperMethods.hasRecords(customer)) {
                        customerID = HelperMethods.getFieldValue(customer, "id");
                    }
                } catch (HttpCallException e) {
                    alert.prepareError("Caught exception while Customer Create :", e).log();
                }

                if (customerID == null) {
                    return new Result();
                }

                input.put("id", customerID);
                CreateCustomerCommunication.invoke(input, dcRequest);
                input.put("id", customerID);
                CreateCustomerAddress.invoke(input, dcRequest);
                input.put("id", customerID);
                CreateCustomerPreference.invoke(input, dcRequest);
                input.put("id", customerID);
                CreateCustomerGroup(input, dcRequest);

                return new Result();
            }

            private void CreateCustomerGroup(Map<String, String> input, DataControllerRequest dcRequest) {

                String customerId = input.get("id");
                Map<String, String> postParamMapGroup = new HashMap<>();
                postParamMapGroup.put("Customer_id", customerId);
                postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
                try {
                    HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_GROUP_CREATE);
                } catch (HttpCallException e) {
                    alert.prepareError("Caught exception while Customer group Create :", e).log();
                }

            }

            private Map<String, String> getInputParams(Record user) {
                Map<String, String> input = new HashMap<>();

                input.put("FirstName", user.getParamValueByName("FirstName"));
                input.put("LastName", user.getParamValueByName("LastName"));
                input.put("UserName", user.getParamValueByName("UserName"));
                input.put("unsuccessfulLoginAttempts", user.getParamValueByName("unsuccessfulLoginAttempts"));
                input.put("lockCount", user.getParamValueByName("lockCount"));
                input.put("Gender", user.getParamValueByName("gender"));
                input.put("DateOfBirth", user.getParamValueByName("DateOfBirth"));
                input.put("DrivingLicenseNumber", user.getParamValueByName("drivingLicenseNumber"));
                input.put("Ssn", user.getParamValueByName("Ssn"));
                input.put("Cvv", user.getParamValueByName("cvv"));
                input.put("Token", user.getParamValueByName("token"));
                input.put("Pin", user.getParamValueByName("pin"));
                input.put("SpouseName",
                        user.getParamValueByName("spousefirstname") + user.getParamValueByName("spouselastname"));
                input.put("NoOfDependents", user.getParamValueByName("noofdependents"));
                input.put("UserCompany", user.getParamValueByName("userCompany"));
                input.put("CountryCode", user.getParamValueByName("countryCode"));
                input.put("UserImage", user.getParamValueByName("userImage"));
                input.put("UserImageURL", user.getParamValueByName("userImageURL"));
                input.put("ValidDate", user.getParamValueByName("validDate"));
                input.put("isUserAccountLocked", user.getParamValueByName("isUserAccountLocked"));
                input.put("IsPinSet", user.getParamValueByName("isPinSet"));
                input.put("IsPhoneEnabled", user.getParamValueByName("isPhoneEnabled"));
                input.put("IsEmailEnabled", user.getParamValueByName("isEmailEnabled"));
                input.put("isSuperAdmin", user.getParamValueByName("isSuperAdmin"));
                input.put("CurrentLoginTime", user.getParamValueByName("currentLoginTime"));
                input.put("Lastlogintime", user.getParamValueByName("lastlogintime"));
                input.put("IsCoreIdentityScope", "1");
                input.put("areDepositTermsAccepted", user.getParamValueByName("areDepositTermsAccepted"));
                input.put("areAccountStatementTermsAccepted",
                        user.getParamValueByName("areAccountStatementTermsAccepted"));
                input.put("areUserAlertsTurnedOn", user.getParamValueByName("areUserAlertsTurnedOn"));
                input.put("isBillPaySupported", user.getParamValueByName("isBillPaySupported"));
                input.put("isBillPayActivated", user.getParamValueByName("isBillPayActivated"));
                input.put("isP2PSupported", user.getParamValueByName("isP2PSupported"));
                input.put("isP2PActivated", user.getParamValueByName("isP2PActivated"));
                input.put("isWireTransferEligible", user.getParamValueByName("isWireTransferEligible"));
                input.put("isWireTransferActivated", user.getParamValueByName("isWireTransferActivated"));

                input.put("isEnrolled", user.getParamValueByName("isEnrolled"));
                input.put("Bank_id", user.getParamValueByName("Bank_id"));
                input.put("Session_id", user.getParamValueByName("Session_id"));

                input.put("MaritalStatus", user.getParamValueByName("maritalstatus"));
                input.put("MaritalStatus_id",
                        StringUtils.isNotBlank(user.getParamValueByName("maritalstatus")) ? "SID_MARRIED" : null);

                input.put("SpouseFirstName", user.getParamValueByName("spousefirstname"));
                input.put("SpouseLastName", user.getParamValueByName("spouselastname"));

                input.put("DefaultAccountDeposit", user.getParamValueByName("default_account_deposit"));
                input.put("DefaultAccountTransfers", user.getParamValueByName("default_account_transfers"));
                input.put("DefaultModule_id", user.getParamValueByName("defaultModule_id"));
                input.put("ssDefaultAccountPaymentsn", user.getParamValueByName("default_account_payments"));
                input.put("DefaultAccountCardless", user.getParamValueByName("default_account_cardless"));
                input.put("DefaultAccountBillPay", user.getParamValueByName("default_account_billPay"));
                input.put("DefaultToAccountP2P", user.getParamValueByName("default_to_account_p2p"));
                input.put("DefaultFromAccountP2P", user.getParamValueByName("default_from_account_p2p"));
                input.put("ShowBillPayFromAccPopup", user.getParamValueByName("showBillPayFromAccPopup"));

                input.put("Phone", user.getParamValueByName("Phone"));
                input.put("Email", user.getParamValueByName("Email"));

                input.put("addressLine1", user.getParamValueByName("addressLine1"));
                input.put("addressLine2", user.getParamValueByName("addressLine2"));
                input.put("city", user.getParamValueByName("city"));
                input.put("state", user.getParamValueByName("state"));
                input.put("country", user.getParamValueByName("country"));
                input.put("zipcode", user.getParamValueByName("zipcode"));

                return input;
            }

            private Result getCustomerFromCore(Result result) {

                Record coreUsrAttr = result.getRecordById(DBPUtilitiesConstants.CORE_ATTR);
                Result retResult = new Result();
                retResult.addRecord(coreUsrAttr);
                return retResult;
            }

        };
        try {
            ThreadExecutor.getExecutor(dcRequest).execute(callable);
        } catch (InterruptedException e) {
            alert.prepareError("Caught exception while Executing Thread :", e).log();
            Thread.currentThread().interrupt();
        }

    }

    private static String getMfaName(DataControllerRequest dcRequest) {
        String reportingParams = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        try {
            reportingParams = URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            alert.prepareError("Caught exception while Decoding Reporting Params : ", e).log();
        }
        try {
            JSONObject jsonObject = new JSONObject(reportingParams);
            if (reportingParams.contains("mfaname")) {
                return jsonObject.getString("mfaname");
            }
        } catch (JSONException e) {
            alert.prepareError("Caught exception while Getting mfaname from reporting Params: ", e).log();
        }
        return "";
    }
}