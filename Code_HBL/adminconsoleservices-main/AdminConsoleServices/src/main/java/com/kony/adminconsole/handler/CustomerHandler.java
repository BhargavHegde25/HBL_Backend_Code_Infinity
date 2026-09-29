package com.kony.adminconsole.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.dto.CustomerBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.service.customermanagement.CustomerGetContactInfo;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.JSONUtil;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.StatusEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerHandler {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String REQUEST_TYPE_IDENTIFIER = "REQUEST";
    private static final String NOTIFICATION_TYPE_IDENTIFIER = "NOTIFICATION";
    private static final String COMM_TYPE_EMAIL = "COMM_TYPE_EMAIL";
    private static final String COMM_TYPE_PHONE = "COMM_TYPE_PHONE";

    public static final int DEFAULT_UNLOCK_COUNT = 4;

    public static void deleteAllSecurityQuestionsForACustomer(String customerId, DataControllerRequest requestInstance,
            String authToken) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put("$filter", "Customer_id eq '" + customerId + "'");
        String readResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERSECURITYQUESTIONS_READ, postParametersMap,
                null, requestInstance);
        JSONObject readResponseJSONObject = CommonUtilities.getStringAsJSONObject(readResponse);
        if ((!readResponseJSONObject.has(FabricConstants.OPSTATUS))
                || readResponseJSONObject.getInt(FabricConstants.OPSTATUS) != 0) {
            alert.prepareError("Failed to read customer security questions! " + readResponse).log();
        }
        JSONArray customerSecurityQuestions = readResponseJSONObject.getJSONArray("customersecurityquestions");

        postParametersMap.clear();
        postParametersMap.put("Customer_id", customerId);
        for (Object customerSecurityQuestionObject : customerSecurityQuestions) {
            JSONObject customerSecurityQuestion = (JSONObject) customerSecurityQuestionObject;
            postParametersMap.put("SecurityQuestion_id", customerSecurityQuestion.getString("SecurityQuestion_id"));
            String deleteRespose = Executor.invokeService(ServiceURLEnum.CUSTOMERSECURITYQUESTIONS_DELETE,
                    postParametersMap, null, requestInstance);
            JSONObject deleteJSONObject = CommonUtilities.getStringAsJSONObject(deleteRespose);
            if ((!deleteJSONObject.has(FabricConstants.OPSTATUS))
                    || deleteJSONObject.getInt(FabricConstants.OPSTATUS) != 0) {
                alert.prepareError("Failed to delete customer security questions! " + deleteRespose).log();
            }
        }
    }

    public JSONObject readCustomerAddr(String authToken, String customerID, DataControllerRequest requestInstance) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER, "CustomerId eq '" + customerID + "'");
        postParametersMap.put(ODataQueryConstants.SELECT,
                "Address_id,AddressType,AddressLine1,AddressLine2,ZipCode,CityName,City_id,RegionName,Region_id,RegionCode,CountryName,Country_id,CountryCode,isPrimary");
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERADDRESS_VIEW_READ,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

    }

    public Result readCustomerMobileInfo(String authToken, String customerID, DataControllerRequest requestInstance,
            Result communicationResult) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER,
                "Customer_id eq '" + customerID + "' and Type_id eq COMM_TYPE_PHONE");
        postParametersMap.put(ODataQueryConstants.SELECT, "id,isPrimary,Value,Extension,Description");
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERCOMMUNICATION_READ,
                postParametersMap, null, requestInstance);
        JSONObject readEndpointResponseJSON = CommonUtilities.getStringAsJSONObject(readEndpointResponse);

        if (readEndpointResponseJSON != null && readEndpointResponseJSON.has(FabricConstants.OPSTATUS)
                && readEndpointResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                && readEndpointResponseJSON.has("customercommunication")) {
            processMobileNumbers(readEndpointResponseJSON.getJSONArray("customercommunication"));
            return communicationResult;
        } else {
            ErrorCodeEnum.ERR_20882.setErrorCode(communicationResult);
            return null;
        }
    }

    public Result readCustomerMobileAndEmailInfo(String customerID, Result communicationResult,
            DataControllerRequest requestInstance) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerID + "' and (Type_id eq '"
                + COMM_TYPE_EMAIL + "' or Type_id eq '" + COMM_TYPE_PHONE + "')");
        postParametersMap.put(ODataQueryConstants.SELECT, "id,Type_id,isPrimary,Value,Extension,Description");
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERCOMMUNICATION_READ,
                postParametersMap, null, requestInstance);
        JSONObject readEndpointResponseJSON = CommonUtilities.getStringAsJSONObject(readEndpointResponse);

        if (readEndpointResponseJSON != null && readEndpointResponseJSON.has(FabricConstants.OPSTATUS)
                && readEndpointResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                && readEndpointResponseJSON.has("customercommunication")) {
            JSONArray communicationInfoArray = readEndpointResponseJSON.getJSONArray("customercommunication");
            JSONArray emailArray = new JSONArray();
            JSONArray phoneArray = new JSONArray();

            for (Object communicationJSONObject : communicationInfoArray) {
                JSONObject communicationInfoJSONObject = (JSONObject) communicationJSONObject;
                if (communicationInfoJSONObject.getString("Type_id").equalsIgnoreCase(COMM_TYPE_EMAIL)) {
                    emailArray.put(communicationInfoJSONObject);
                } else {
                    phoneArray.put(communicationInfoJSONObject);
                }
            }
            // Emails
            Dataset emailIdsArrayDataset = CommonUtilities.constructDatasetFromJSONArray(emailArray);
            emailIdsArrayDataset.setId("EmailIds");
            communicationResult.addDataset(emailIdsArrayDataset);

            // Phone numbers
            processMobileNumbers(phoneArray);
            Dataset phoneArrayDataset = CommonUtilities.constructDatasetFromJSONArray(phoneArray);
            phoneArrayDataset.setId("ContactNumbers");
            communicationResult.addDataset(phoneArrayDataset);
        } else {
            ErrorCodeEnum.ERR_20882.setErrorCode(communicationResult);
        }
        return communicationResult;
    }

    public static void processMobileNumbers(JSONArray customerCommunicationArray) {
        String phoneNumber, extension, countryCode;
        String currValue;
        String currValueArray[];
        for (int indexVar = 0; indexVar < customerCommunicationArray.length(); indexVar++) {
            JSONObject currRecord = customerCommunicationArray.optJSONObject(indexVar);
            currValue = currRecord.optString("Value");
            if (StringUtils.isNotBlank(currValue)) {
                currValueArray = StringUtils.split(currValue, CustomerGetContactInfo.CONTACT_VALUE_SEPERATOR);
                if (currValueArray != null && currValueArray.length >= 2) {
                    countryCode = currValueArray[0];
                    currRecord.put("phoneCountryCode", countryCode);
                    phoneNumber = currValueArray[1];
                    currRecord.put("phoneNumber", phoneNumber);
                    if (currValueArray.length == 3) {
                        extension = currValueArray[2];
                        currRecord.put("phoneExtension", extension);
                    }
                } else {
                    currRecord.put("phoneNumber", currValue);
                    currRecord.put("phoneCountryCode", "");
                }

            }
        }
    }

    public JSONObject readCustomerEmailInfo(String authToken, String customerID,
            DataControllerRequest requestInstance) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER,
                "Customer_id eq '" + customerID + "' and Type_id eq COMM_TYPE_EMAIL");
        postParametersMap.put(ODataQueryConstants.SELECT, "id,isPrimary,Value,Extension,Description");
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERCOMMUNICATION_READ,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

    }

//    public static JSONObject getCustomerPrimaryEmailAddress(String customerID, DataControllerRequest requestInstance)
//            throws Exception {
//
//        Map<String, String> postParametersMap = new HashMap<String, String>();
//        postParametersMap.put(ODataQueryConstants.FILTER,
//                "Customer_id eq '" + customerID + "' and Type_id eq 'COMM_TYPE_EMAIL' and isPrimary eq 'true'");
//        postParametersMap.put(ODataQueryConstants.SELECT, "id,isPrimary,Value,Extension,Description");
//        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERCOMMUNICATION_READ,
//                postParametersMap, null, requestInstance);
//        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
//
//    }

    public JSONObject readCustomerPreferredInfo(String authToken, String customerID,
            DataControllerRequest requestInstance) throws Exception {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.SELECT, "PreferredContactMethod,PreferredContactTime");
        postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + customerID + "'");
        String readEndpointResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

    }

    public JSONObject getSecurityQuestionsForGivenCustomer(String customerId, String authToken,
            DataControllerRequest requestInstance) {
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER, "Customer_id eq'" + customerId + "'");
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERSECURITYQUESTION_VIEW_READ,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
    }

    public JSONObject getSecurityQuestionsForGivenCustomerWithoutAnswer(String systemUser, String customerId,
            String authToken, DataControllerRequest requestInstance) {
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER, "Customer_id eq'" + customerId + "'");
        postParametersMap.put(ODataQueryConstants.SELECT, "SecurityQuestion_id,Question,QuestionStatus_id");
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERSECURITYQUESTION_VIEW_READ,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
    }

    public Record constructRecordFromJSON(JSONObject JSON) {
        Record response = new Record();
        if (JSON == null || JSON.length() == 0) {
            return response;
        }
        Iterator<String> keys = JSON.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            Param param = new Param(key, JSON.getString(key), FabricConstants.STRING);
            response.addParam(param);
        }

        return response;
    }

    /**
     * Method to Get Customer Id from Customer Username
     * 
     * @param customerUsername
     * @param requestInstance
     * @return customerId
     */
    public static String getCustomerId(String customerUsername, DataControllerRequest requestInstance) {
        try {
            String customerId = null;
            String readCustomerResponse;
            if (StringUtils.isBlank(customerUsername))
                return customerUsername;
            Map<String, String> postParametersMap = new HashMap<String, String>();
            JSONObject readCustomerResponseJSON;
            postParametersMap.put(ODataQueryConstants.SELECT, "id");
            postParametersMap.put(ODataQueryConstants.FILTER, "UserName eq '" + customerUsername + "'");
            postParametersMap.put(ODataQueryConstants.TOP, "1");
            readCustomerResponse =
                    Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
            readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
            if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                    && readCustomerResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                    && readCustomerResponseJSON.has("customer")) {
                JSONArray customerJSONArray = readCustomerResponseJSON.getJSONArray("customer");
                if (customerJSONArray.length() > 0) {
                    JSONObject currCustomerObject = customerJSONArray.getJSONObject(0);
                    if (currCustomerObject.has("id")) {
                        customerId = currCustomerObject.optString("id");
                    }
                }
            }
            return customerId;
        } catch (Exception e) {
            alert.prepareError("Exception in CustomerHandler getCustomerId" + e).log();
        }
        return null;
    }

    /**
     * Method to get Customer Username from Customer Id
     * 
     * @param customerId
     * @param requestInstance
     * @return customerUsername
     */
    public static String getCustomerUsername(String customerId, DataControllerRequest requestInstance) {
        try {
            String customerUsername = null;
            String readCustomerResponse;
            if (StringUtils.isBlank(customerId))
                return customerId;
            Map<String, String> postParametersMap = new HashMap<String, String>();
            JSONObject readCustomerResponseJSON;
            postParametersMap.put(ODataQueryConstants.SELECT, "UserName");
            postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + customerId + "'");
            postParametersMap.put(ODataQueryConstants.TOP, "1");
            readCustomerResponse =
                    Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
            readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
            if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                    && readCustomerResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                    && readCustomerResponseJSON.has("customer")) {
                JSONArray customerJSONArray = readCustomerResponseJSON.getJSONArray("customer");
                if (customerJSONArray.length() > 0) {
                    JSONObject currCustomerObject = customerJSONArray.getJSONObject(0);
                    if (currCustomerObject.has("UserName")) {
                        customerUsername = currCustomerObject.optString("UserName");
                    }
                }
            }
            return customerUsername;
        } catch (Exception e) {
            alert.prepareError("Exception in CustomerHandler getCustomerUsername" + e).log();
        }
        return null;
    }

    /**
     * Get Customer Name Details. First Name, Middle Name Last Name & Salutation
     * 
     * @param customerUsername
     * @param requestInstance
     * @return customerDetailsJSONObject
     */
    public static JSONObject getCustomerNameDetails(String customerUsername, DataControllerRequest requestInstance) {
        JSONObject customerDetailsJSONObject = new JSONObject();
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER, "UserName eq '" + customerUsername + "'");
        postParametersMap.put(ODataQueryConstants.SELECT, "FirstName,MiddleName,LastName,Salutation");
        String readCustomerResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
        JSONObject readCustomerResponseJSONObject = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
        if (readCustomerResponseJSONObject != null && readCustomerResponseJSONObject.has(FabricConstants.OPSTATUS)
                && readCustomerResponseJSONObject.getInt(FabricConstants.OPSTATUS) == 0
                && readCustomerResponseJSONObject.has("customer")) {
            if (readCustomerResponseJSONObject.getJSONArray("customer").length() >= 1)
                customerDetailsJSONObject = readCustomerResponseJSONObject.getJSONArray("customer").getJSONObject(0);
        }
        return customerDetailsJSONObject;
    }

    /**
     * Get Customer Details.
     * 
     * @param customerUsername
     * @param customerId
     * @param requestInstance
     * @return customerDetailsJSONObject
     */
    public JSONObject getCustomerDetails(String customerUsername, String customerId,
            DataControllerRequest requestInstance) throws ApplicationException {
        JSONObject customerDetailsJSONObject = new JSONObject();
        Map<String, String> postParametersMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(customerUsername)) {
            postParametersMap.put(ODataQueryConstants.FILTER, "UserName eq '" + customerUsername + "'");
        } else {
            postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + customerId + "'");
        }

        postParametersMap.put(ODataQueryConstants.SELECT,
                "id,UserName,Status_id,IsAssistConsented,FirstName,MiddleName,LastName,CustomerType_id");
        String readCustomerResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
        JSONObject readCustomerResponseJSONObject = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
        if (readCustomerResponseJSONObject != null && readCustomerResponseJSONObject.has(FabricConstants.OPSTATUS)
                && readCustomerResponseJSONObject.getInt(FabricConstants.OPSTATUS) == 0
                && readCustomerResponseJSONObject.has("customer")) {
            if (readCustomerResponseJSONObject.getJSONArray("customer").length() >= 1) {
                customerDetailsJSONObject = readCustomerResponseJSONObject.getJSONArray("customer").getJSONObject(0);
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_21028);
            }
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_20139);
        }
        return customerDetailsJSONObject;
    }

    /**
     * Resolve Customers of a Group
     * 
     * @param requestInstance
     * @param groupID
     * @return listOfCustomers
     */
    public static ArrayList<String> getCustomersOfAGroup(DataControllerRequest requestInstance, String groupID) {
        if (StringUtils.isBlank(groupID))
            return new ArrayList<String>();
        ArrayList<String> listOfCustomers = new ArrayList<String>();
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.SELECT, "Customer_id");
        postParametersMap.put(ODataQueryConstants.FILTER, "Group_id eq '" + groupID + "'");
        String readCustomerGroupResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMERGROUP_READ, postParametersMap, null, requestInstance);
        JSONObject readCustomerGroupResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerGroupResponse);
        if (readCustomerGroupResponseJSON != null && readCustomerGroupResponseJSON.has(FabricConstants.OPSTATUS)
                && readCustomerGroupResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                && readCustomerGroupResponseJSON.has("customergroup")) {
            JSONArray customerGroupJSONArray = readCustomerGroupResponseJSON.getJSONArray("customergroup");
            JSONObject currRecordJSONObject;
            for (int indexVar = 0; indexVar < customerGroupJSONArray.length(); indexVar++) {
                currRecordJSONObject = customerGroupJSONArray.getJSONObject(indexVar);
                if (currRecordJSONObject.has("Customer_id")) {
                    listOfCustomers.add(currRecordJSONObject.optString("Customer_id"));
                }
            }
        }
        return listOfCustomers;
    }

    /**
     * Resolve Id's of customers via username Returns a Map of Customers data. Structure: <Username,CustomerId>
     * 
     * @param requestInstance
     * @param customerUsernameList
     * @return customerIDMap
     */
    public static Map<String, String> getCustomersIdList(DataControllerRequest requestInstance,
            List<String> customerUsernameList) {
        Map<String, String> customerIdMap = new HashMap<String, String>();
        if (customerUsernameList != null && !customerUsernameList.isEmpty()) {
            StringBuffer filterQueryBuffer = new StringBuffer();
            for (String currCustomerUsername : customerUsernameList) {
                filterQueryBuffer.append("UserName eq '" + currCustomerUsername + "' or ");
            }
            filterQueryBuffer.trimToSize();
            String filterQuery = CommonUtilities.replaceLastOccuranceOfString(filterQueryBuffer.toString(), "or", "");
            filterQuery = filterQuery.trim();
            Map<String, String> postParametersMap = new HashMap<String, String>();
            postParametersMap.put(ODataQueryConstants.SELECT, "UserName,id");
            postParametersMap.put(ODataQueryConstants.FILTER, filterQuery);
            String readCustomerResponse =
                    Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
            JSONObject readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
            if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                    && readCustomerResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readCustomerResponseJSON.has("customer")) {
                diagnostic.prepareDebug("CUSTOMER_READ Status:Successfull").log();
                JSONObject currCustomerRecordJSONObject = null;
                JSONArray customerRecordsJSONArray = readCustomerResponseJSON.getJSONArray("customer");
                for (Object currObject : customerRecordsJSONArray) {
                    if (currObject instanceof JSONObject) {
                        currCustomerRecordJSONObject = (JSONObject) currObject;
                        if (currCustomerRecordJSONObject.has("id") && currCustomerRecordJSONObject.has("UserName")) {
                            customerIdMap.put(currCustomerRecordJSONObject.optString("UserName"),
                                    currCustomerRecordJSONObject.optString("id"));
                        }

                    }
                }
            }
        }
        return customerIdMap;
    }

    /**
     * Resolve username of customers via Id Returns a Map of Customers data. Structure: <CustomerId,Username>
     * 
     * @param requestInstance
     * @param customerUsernameList
     * @return customerUsernameMap
     */
    public static Map<String, String> getCustomersUsernameList(DataControllerRequest requestInstance,
            List<String> customerIdList) {
        Map<String, String> customerUsernameMap = new HashMap<String, String>();
        if (customerIdList != null && !customerIdList.isEmpty()) {
            StringBuffer filterQueryBuffer = new StringBuffer();
            for (String currCustomerId : customerIdList) {
                filterQueryBuffer.append("id eq '" + currCustomerId + "' or ");
            }
            filterQueryBuffer.trimToSize();
            String filterQuery = CommonUtilities.replaceLastOccuranceOfString(filterQueryBuffer.toString(), "or", "");
            filterQuery = filterQuery.trim();
            Map<String, String> postParametersMap = new HashMap<String, String>();
            postParametersMap.put(ODataQueryConstants.SELECT, "UserName,id");
            postParametersMap.put(ODataQueryConstants.FILTER, filterQuery);
            String readCustomerResponse =
                    Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
            JSONObject readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
            if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                    && readCustomerResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readCustomerResponseJSON.has("customer")) {
                diagnostic.prepareDebug("CUSTOMER_READ Status:Successfull").log();
                JSONObject currCustomerRecordJSONObject = null;
                JSONArray customerRecordsJSONArray = readCustomerResponseJSON.getJSONArray("customer");
                for (Object currObject : customerRecordsJSONArray) {
                    if (currObject instanceof JSONObject) {
                        currCustomerRecordJSONObject = (JSONObject) currObject;
                        if (currCustomerRecordJSONObject.has("id") && currCustomerRecordJSONObject.has("UserName")) {
                            customerUsernameMap.put(currCustomerRecordJSONObject.optString("id"),
                                    currCustomerRecordJSONObject.optString("UserName"));
                        }

                    }
                }
            }
        }
        return customerUsernameMap;
    }

    public String getCustomerType(String customerId, DataControllerRequest requestInstance)
            throws ApplicationException {
        if (StringUtils.isBlank(customerId) || requestInstance == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20792);
        }
        String customerType = StringUtils.EMPTY;
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, "id eq '" + customerId + "'");
        inputMap.put(ODataQueryConstants.SELECT, "CustomerType_id");

        String readCustomerResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, inputMap, null, requestInstance);
        JSONObject readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
        if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                && readCustomerResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readCustomerResponseJSON.has("customer")) {
            JSONArray customersJSONArray = readCustomerResponseJSON.optJSONArray("customer");
            if (customersJSONArray != null && customersJSONArray.length() > 0) {
                JSONObject currJSONObject = null;
                for (Object currObject : customersJSONArray) {
                    if (currObject instanceof JSONObject) {
                        currJSONObject = (JSONObject) currObject;
                        customerType = currJSONObject.optString("CustomerType_id");
                        diagnostic.prepareDebug("Resolved Customer Type").log();
                        break;
                    }
                }
            } else {
                diagnostic.prepareDebug("Empty result set. Unrecognised Customer ID").log();
            }
        } else {
            diagnostic.prepareDebug("Failed to resolve Customer Type").log();
        }
        return customerType;
    }

    public static JSONArray getCustomerEntitlements(String customerId, DataControllerRequest requestInstance,
            Result processedResult) {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("_customerID", customerId);

        String readCustomerResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_ENTITLEMENTS_PROC, inputMap, null, requestInstance);
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);

        if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
                || responseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !responseJSON.has("records")) {
            ErrorCodeEnum.ERR_20793.setErrorCode(processedResult);
            return null;
        }
        return responseJSON.getJSONArray("records");
    }

    public static JSONArray getCustomerActions(String customerId, DataControllerRequest requestInstance,
            Result processedResult) {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("_customerId", customerId);
        inputMap.put("_actionId", "");
        String readCustomerResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_ACTIONS_PROC, inputMap, null, requestInstance);
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);

        if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
                || responseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !responseJSON.has("records")) {
            ErrorCodeEnum.ERR_20793.setErrorCode(processedResult);
            return null;
        }
        return responseJSON.getJSONArray("records");
    }

    public static Map<String, Set<String>> getUserActions(String customerId, DataControllerRequest requestInstance,
            Result processedResult) {
    	
    	Map<String, Set<String>> securityAttributesMap = new HashMap<>();
    	
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("_userId", customerId);
        
        String readUserActionResponseResponse = null;
        
        try {
        	readUserActionResponseResponse =
                Executor.invokeService(ServiceURLEnum.USER_SECURITYATTRIBUTES_GET_PROC, inputMap, null, requestInstance);
        

	        JsonParser parser = new JsonParser();
	        JsonObject securityAttributesJson = parser.parse(readUserActionResponseResponse).getAsJsonObject();
	        if (null != securityAttributesJson
	                && JSONUtil.hasKey(securityAttributesJson, ACConstants.DATASET_RECORDS)
	                && securityAttributesJson.get(ACConstants.DATASET_RECORDS).isJsonArray()
	                && JSONUtil.hasKey(securityAttributesJson, ACConstants.DATASET_RECORDS1)
	                && securityAttributesJson.get(ACConstants.DATASET_RECORDS1).isJsonArray()) {
	            JsonArray actionsArray =
	                    securityAttributesJson.get(ACConstants.DATASET_RECORDS).getAsJsonArray();
	            JsonArray featuresArray = securityAttributesJson.get(ACConstants.DATASET_RECORDS1).getAsJsonArray();
	
	            JsonElement actionsElement =
	                    actionsArray.size() > 0 ? actionsArray.get(0) : new JsonObject();
	            JsonElement featuresElement =
	                    featuresArray.size() > 0 ? featuresArray.get(0) : new JsonObject();
	            if (actionsElement.isJsonObject()) {
	                JsonObject object = actionsElement.getAsJsonObject();
	                if (null != object && JSONUtil.hasKey(object, ACConstants.ACTIONS)) {
	
	                    securityAttributesMap.put(ACConstants.ACTIONS,
	                            splitString(object.get(ACConstants.ACTIONS).getAsString(),
	                            		ACConstants.COMMA_SEPERATOR));
	                }
	            }
	
	            if (featuresElement.isJsonObject()) {
	                JsonObject object = featuresElement.getAsJsonObject();
	                if (null != object && JSONUtil.hasKey(object, ACConstants.FEATURES)) {
	
	                    securityAttributesMap.put(ACConstants.FEATURES,
	                            splitString(object.get(ACConstants.FEATURES).getAsString(),
	                            		ACConstants.COMMA_SEPERATOR));
	                }
	            }
	        }
        
        }catch(Exception exp) {
        	
        	diagnostic.prepareDebug("USER_SECURITYATTRIBUTES_GET_PROC response: "+readUserActionResponseResponse).log();
        	diagnostic.prepareDebug("Encounter Exception while trying to fetch Customer Entitlement: "+exp).log();
        }
        
        return securityAttributesMap;
    }
    
    public static Result getCustomerRequestNotificationCount(DataControllerRequest requestInstance, String customerId,
            Result processedResult) {

        if (StringUtils.isBlank(customerId)) {
            ErrorCodeEnum.ERR_20865.setErrorCode(processedResult);
            return processedResult;
        }
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER, "customerId eq '" + customerId + "'");

        String readcardRequestNotificationCountViewReadResponse = Executor.invokeService(
                ServiceURLEnum.CARD_REQUEST_NOTIFICATION_COUNT_VIEW_READ, postParametersMap, null, requestInstance);
        JSONObject readcardRequestNotificationCountViewReadResponseJSON =
                CommonUtilities.getStringAsJSONObject(readcardRequestNotificationCountViewReadResponse);

        if (readcardRequestNotificationCountViewReadResponseJSON == null
                || !readcardRequestNotificationCountViewReadResponseJSON.has("card_request_notification_count_view")) {
            ErrorCodeEnum.ERR_20302.setErrorCode(processedResult);
            return processedResult;
        }

        int totalRequestCount = 0, totalNotificationCount = 0;
        JSONArray countArray = readcardRequestNotificationCountViewReadResponseJSON
                .getJSONArray("card_request_notification_count_view");
        JSONObject currRecordJSONObject = null;

        int currRequestCount, currNotificationCount;
        for (int indexVar = 0; indexVar < countArray.length(); indexVar++) {
            if (countArray.get(indexVar) instanceof JSONObject) {
                currRecordJSONObject = countArray.getJSONObject(indexVar);

                if (currRecordJSONObject.has("reqType")) {

                    if (currRecordJSONObject.optString("reqType").equalsIgnoreCase(REQUEST_TYPE_IDENTIFIER)) {
                        currRequestCount = currRecordJSONObject.optInt("requestcount");
                        totalRequestCount += currRequestCount;

                    } else if (currRecordJSONObject.optString("reqType")
                            .equalsIgnoreCase(NOTIFICATION_TYPE_IDENTIFIER)) {
                        currNotificationCount = currRecordJSONObject.optInt("requestcount");
                        totalNotificationCount += currNotificationCount;
                    }
                }
            }
        }

        Param requestCountParam = new Param("requestCount", String.valueOf(totalRequestCount), FabricConstants.STRING);
        Param notificationCountParam =
                new Param("notificationCount", String.valueOf(totalNotificationCount), FabricConstants.STRING);
        processedResult.addParam(requestCountParam);
        processedResult.addParam(notificationCountParam);

        return processedResult;
    }

    public static void computeCustomerBasicInformation(DataControllerRequest requestInstance, Result processedResult,
            String customerId, String username, String legalEntityId)
            throws DBPAuthenticationException, JSONException, ParseException, InterruptedException, ExecutionException {
        List<Callable<Result>> listOfCallable = Arrays.asList(new Callable<Result>() {
            @Override
            public Result call() throws ApplicationException, ParseException {
                Result basicResult = new Result();

                JSONObject customerViewJson;
                if (customerId == null && username == null) {
                    ErrorCodeEnum.ERR_20688.setErrorCode(basicResult);
                    basicResult.addParam(new Param("Status", "Failure", FabricConstants.STRING));
                    return basicResult;

                }
                JSONObject getResponseJSON =
                        DBPServices.computeCustomerBasicInformation(customerId, username, legalEntityId, requestInstance);
                if (getResponseJSON != null && getResponseJSON.has(FabricConstants.OPSTATUS)
                        && getResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                        && getResponseJSON.has("customerbasicinfo_view")) {

                    customerViewJson = getResponseJSON.getJSONObject("customerbasicinfo_view");
                    String SSN = customerViewJson.optString("SSN");
//                    String CustomerTypeId = customerViewJson.optString("CustomerType_id");
//                    if(CustomerTypeId.equalsIgnoreCase("TYPE_ID_PROSPECT"))
//                    {
//                    	customerViewJson.put("CustomerType_id", "TYPE_ID_RETAIL");
//                    }
                    customerViewJson.put("SSN", maskSSN(SSN));
                    
                    String isEnrolledFromSpotlight = customerViewJson.optString("isEnrolledFromSpotlight");
    				String isProfileExist = customerViewJson.optString("isProfileExist");
    				String isCustomerEnrolled = customerViewJson.optString("isCustomerEnrolled");
    				String userId = customerViewJson.optString("id");
    				
    				if(StringUtils.isNotBlank(userId) && 
    						!(( null!=isEnrolledFromSpotlight && isEnrolledFromSpotlight.equalsIgnoreCase("1"))&&
    						( null!=isProfileExist && isProfileExist.equalsIgnoreCase("false"))&&
    						(null!=isCustomerEnrolled && isCustomerEnrolled.equalsIgnoreCase("false")))) {
    					
    					boolean hasAccess = CustomerHandler.doesCurrentLoggedinUserHasAccessToGivenCustomer("", userId,
    							requestInstance, new Result());
    				
    					if(hasAccess) {
    						customerViewJson.put("isCustomerAccessiable", true);
    					} else {
    						customerViewJson.put("isCustomerAccessiable", false);
    					}
    				}
    				
    				Record customerbasicinfo_view = CommonUtilities.constructRecordFromJSONObject(customerViewJson);
                    customerbasicinfo_view.setId("customerbasicinfo_view");
                    basicResult.addRecord(customerbasicinfo_view);

                    Record configuration = new Record();
                    configuration.setId("Configuration");
                    configuration.addParam(new Param("value", customerViewJson.getString("accountLockoutTime")));
                    basicResult.addRecord(configuration);
                    String currentStatus;

                    String lockedOnTS = customerViewJson.optString("lockedOn");
                    if (customerViewJson.getString("CustomerStatus_id")
                            .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {
                        currentStatus = "LOCKED";
                    } else if (customerViewJson.getString("CustomerStatus_id")
                            .equalsIgnoreCase(StatusEnum.SID_CUS_SUSPENDED.name())) {
                        currentStatus = "SUSPENDED";
                    } else if (customerViewJson.getString("CustomerStatus_id")
                            .equalsIgnoreCase(StatusEnum.SID_CUS_ACTIVE.name())) {
                        currentStatus = "ACTIVE";
                    } else if (customerViewJson.getString("CustomerStatus_id")
                            .equalsIgnoreCase(StatusEnum.SID_CUS_INACTIVE.name())) {
                    	currentStatus = "INACTIVE";
                    } else {
                        currentStatus = "NEW";
                    }
                    
                    diagnostic.debug("currentStatus in adminconsole###"+ currentStatus);

                    Record statusResponse = new Record();
                    statusResponse.setId("OLBCustomerFlags");
                    statusResponse.addParam(new Param("LockedOn", lockedOnTS));

                    if (customerViewJson.getString("CustomerStatus_id")
                            .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {

                        if (StringUtils.isNotBlank(lockedOnTS)) {
                            String lockDuration = "0";

                            if (customerViewJson.has("accountLockoutTime")) {
                                lockDuration = customerViewJson.getString("accountLockoutTime");
                            }
                            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                            TimeZone timezone = TimeZone.getTimeZone("UTC");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                            Calendar elapsedLockedOnDate = Calendar.getInstance();
                            elapsedLockedOnDate.setTimeZone(timezone);
                            elapsedLockedOnDate.setTime(dateFormat.parse(lockedOnTS));
                            elapsedLockedOnDate.add(Calendar.MINUTE, Integer.parseInt(lockDuration));
                            Calendar currentDate = Calendar.getInstance();
                            currentDate.setTimeZone(timezone);

                            /*
                            if (elapsedLockedOnDate.before(currentDate)) {
                                // Time has been elapsed. Calling unlock service
                                JSONObject unlockResponse = DBPServices
                                        .unlockDBXUser(customerViewJson.getString("Username"), requestInstance);
                                basicResult.addParam(new Param("unlockStatus", String.valueOf(unlockResponse),
                                        FabricConstants.STRING));
                                if (unlockResponse == null || !unlockResponse.has(FabricConstants.OPSTATUS)
                                        || unlockResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                                    ErrorCodeEnum.ERR_20538.setErrorCode(basicResult);
                                    return basicResult;
                                }
                                // set updated status
                                currentStatus = "ACTIVE";
                            }
                            */
                        }
                    }

                    statusResponse.addParam(new Param("Status", currentStatus));
                    customerbasicinfo_view.addRecord(statusResponse);
                } else if(getResponseJSON.has("dbpErrCode")) {
                	basicResult.addParam("dbpErrCode", getResponseJSON.get("dbpErrCode").toString());
                	basicResult.addParam("dbpErrMsg", getResponseJSON.get("dbpErrMsg").toString());
                }
                return basicResult;
            }
        });

        List<Result> listOfResults = ThreadExecutor.executeAndWaitforCompletion(listOfCallable);
        for (Result result : listOfResults) {
            processedResult.addAllDatasets(result.getAllDatasets());
            processedResult.addAllParams(result.getAllParams());
            processedResult.addAllRecords(result.getAllRecords());
        }

    }

    public static String maskSSN(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        StringBuilder sb = new StringBuilder("****");
        int len = value.length();
        if (len >= 4) {
            sb.append(value.substring(len - 4));
        } else if (len >= 2) {
            sb.append(value.substring(len - 2));
        } else {
            sb.append(value);
        }

        return sb.toString();
    }

    public JSONObject get(ServiceURLEnum getServiceURL, String customerId, DataControllerRequest requestInstance) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put("_customerId", customerId);

        String getResponse = Executor.invokeService(getServiceURL, postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(getResponse);

    }

    public static Result getUnreadMessageCount(DataControllerRequest requestInstance, String customerID)
            throws ApplicationException {

        Result processedResult = new Result();

        if (StringUtils.isBlank(customerID)) {
            ErrorCodeEnum.ERR_20131.setErrorCode(processedResult);
            processedResult.addParam(new Param("validationError",
                    "ERROR: Could not resolve the Customer ID. Please verify the passed Customer Username.",
                    FabricConstants.STRING));
            return processedResult;
        }

        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("_customerId", customerID);

        String operationResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_UNREAD_MESSAGE_COUNT_PROC_SERVICE,
                inputMap, null, requestInstance);
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && operationResponseJSON.has("records")) {
            JSONArray customerUnreadMessagesRecords = operationResponseJSON.getJSONArray("records");
            if (customerUnreadMessagesRecords.length() > 0) {
                JSONObject currRecordJSON = customerUnreadMessagesRecords.getJSONObject(0);
                if (currRecordJSON.has("messageCount")) {
                    processedResult.addParam(new Param("unreadMessageCount", currRecordJSON.optString("messageCount"),
                            FabricConstants.INT));
                }
            } else {
                processedResult.addParam(new Param("unreadMessageCount", String.valueOf(0), FabricConstants.INT));
                
            }
        }
         if(operationResponseJSON.has("records1")) {
        	JSONArray customerPriorityMessagesRecords = operationResponseJSON.getJSONArray("records1");
        	 JSONObject currRecordJSON = customerPriorityMessagesRecords.getJSONObject(0);
        	 if(currRecordJSON.has("priorityMessageCount")) {
             	processedResult.addParam(new Param("priorityMessageCount", currRecordJSON.optString("priorityMessageCount"),
                         FabricConstants.INT));
             }else {
            	 processedResult.addParam(new Param("priorityMessageCount", String.valueOf(0), FabricConstants.INT));
             }
        }
        else {
        	throw new ApplicationException(ErrorCodeEnum.ERR_20126);
        }
        return processedResult;
        

    }

    public static void doesCurrentLoggedinUserHasAccessToCustomer(String customerUsername, String customerId,
            DataControllerRequest requestInstance, Result processedResult) {

        try {
            Map<String, String> inputMap = new HashMap<String, String>();
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            if (userDetailsBeanInstance == null) {
                ErrorCodeEnum.ERR_21591.setErrorCode(processedResult);
                return;
            }

            inputMap.put("_roleIds", userDetailsBeanInstance.getRoleId());
            inputMap.put("_customerId", StringUtils.isBlank(customerId) ? "" : customerId);
            inputMap.put("_customerUsername", StringUtils.isBlank(customerUsername) ? "" : customerUsername);
            JSONObject readResponse = CommonUtilities.getStringAsJSONObject(Executor.invokeService(
                    ServiceURLEnum.INTERNAL_USER_ACCESS_TO_CUSTOMER_ON_SERVICEDEFINITION_PROC, inputMap, null, requestInstance));
            if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
                    || readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("records")
                    || !(readResponse.getJSONArray("records").length() > 0)
                    || !(readResponse.getJSONArray("records").getJSONObject(0).has("isCustomerAccessible"))) {
                processedResult
                        .addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
                ErrorCodeEnum.ERR_21590.setErrorCode(processedResult);
                return;
            }
            Boolean isCustomerAccessible = Boolean.parseBoolean(
                    readResponse.getJSONArray("records").getJSONObject(0).getString("isCustomerAccessible"));

            if (!isCustomerAccessible) {
                ErrorCodeEnum.ERR_21591.setErrorCode(processedResult);
                return;
            }
        } catch (Exception e) {
            processedResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21590.setErrorCode(processedResult);
            return;
        }

    }

    public static Boolean doesCurrentLoggedinUserHasAccessToGivenCustomer(String customerUsername, String customerId,
            DataControllerRequest requestInstance, Result processedResult) {

        try {
            Map<String, String> inputMap = new HashMap<String, String>();
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            if (userDetailsBeanInstance == null) {
                ErrorCodeEnum.ERR_21591.setErrorCode(processedResult);
                return false;
            }

            inputMap.put("_roleIds", userDetailsBeanInstance.getRoleId());
            inputMap.put("_customerId", StringUtils.isBlank(customerId) ? "" : customerId);
            inputMap.put("_customerUsername", StringUtils.isBlank(customerUsername) ? "" : customerUsername);
            JSONObject readResponse = CommonUtilities.getStringAsJSONObject(Executor.invokeService(
                    ServiceURLEnum.INTERNAL_USER_ACCESS_TO_CUSTOMER_ON_SERVICEDEFINITION_PROC, inputMap, null, requestInstance));
            if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
                    || readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("records")
                    || !(readResponse.getJSONArray("records").length() > 0)
                    || !(readResponse.getJSONArray("records").getJSONObject(0).has("isCustomerAccessible"))) {
                processedResult
                        .addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
                ErrorCodeEnum.ERR_21590.setErrorCode(processedResult);
                return null;
            }
            return Boolean.parseBoolean(
                    readResponse.getJSONArray("records").getJSONObject(0).getString("isCustomerAccessible"));

        } catch (Exception e) {
            processedResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21590.setErrorCode(processedResult);
            return null;
        }

    }

    public JSONArray getIntersectionOfCustomerandCSREntitlements(String customerId, JSONArray csrCompositePermissions,
            DataControllerRequest requestInstance) {
        Result processedResult = new Result();
        JSONArray customerEntitlements = getCustomerEntitlements(customerId, requestInstance, processedResult);
        JSONArray finalArrayOfEntitlements = new JSONArray();
        List<String> customerEntitlementIds = new ArrayList<String>();

        // Prepare list of customer entitlement ids
        for (Object entitlementRecord : customerEntitlements) {
            customerEntitlementIds.add(((JSONObject) entitlementRecord).getString("serviceId"));
        }

        // Iterate through the CSR entitlements
        for (Object entitlementRecord : csrCompositePermissions) {
            JSONObject entitlementJSONObject = (JSONObject) entitlementRecord;
            if (customerEntitlementIds.contains(entitlementJSONObject.getString("Service_id"))) {
                finalArrayOfEntitlements.put(entitlementJSONObject);
            }
        }

        return finalArrayOfEntitlements;
    }

    public JSONArray getIntersectionOfCustomerandCSRActions(String customerId, JSONArray csrCompositeActions,
            DataControllerRequest requestInstance) {
        Result processedResult = new Result();
//        JSONArray customerActions = getCustomerActions(customerId, requestInstance, processedResult);
        Map<String,Set<String>> customerActions = getUserActions(customerId, requestInstance, processedResult);
        JSONArray finalArrayOfActions = new JSONArray();
//        List<String> customerActionIds = new ArrayList<String>();
        if(null == customerActions || !customerActions.containsKey(ACConstants.ACTIONS)
        						   || null == customerActions.get(ACConstants.ACTIONS)) {
        	
        	return finalArrayOfActions;
        }
        Set<String> customerActionIds = customerActions.get(ACConstants.ACTIONS);
        // Prepare list of customer entitlement ids
//        for (Object actionRecord : customerActions) {
//            customerActionIds.add(((JSONObject) actionRecord).getString("Action_id"));
//        }

        // Iterate through the CSR entitlements
        for (Object actionRecord : csrCompositeActions) {
            JSONObject actionJSONObject = (JSONObject) actionRecord;
            if (customerActionIds.contains(actionJSONObject.getString("Action_id"))) {
                finalArrayOfActions.put(actionJSONObject);
            }
        }

        return finalArrayOfActions;
    }

    public String getAttributeFromApplicationTable(String attribute, DataControllerRequest requestInstance)
            throws ApplicationException {

        Map<String, String> inputMap = new HashMap<>();

        String readResponse = Executor.invokeService(ServiceURLEnum.APPLICATION_READ, inputMap, null, requestInstance);
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readResponse);

        if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
                || responseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !responseJSON.has("application")
                || responseJSON.getJSONArray("application").length() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20890);
        }
        String attributeValue = responseJSON.getJSONArray("application").getJSONObject(0).getString(attribute);
        if (StringUtils.isBlank(attributeValue)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20890);
        }
        return attributeValue;
    }

    public String[] getAttributesFromApplicationTable(String[] attributes, DataControllerRequest requestInstance)
            throws ApplicationException {

        Map<String, String> inputMap = new HashMap<>();

        String readResponse = Executor.invokeService(ServiceURLEnum.APPLICATION_READ, inputMap, null, requestInstance);
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readResponse);

        if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
                || responseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !responseJSON.has("application")
                || responseJSON.getJSONArray("application").length() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20890);
        }
        JSONObject applicationRecord = responseJSON.getJSONArray("application").getJSONObject(0);
        String[] attributeValues = new String[attributes.length];
        Arrays.fill(attributeValues, "");
        for(int index = 0; index < attributes.length; index++){
            String attribute = attributes[index];
            if(applicationRecord.has(attribute) && !StringUtils.isEmpty(applicationRecord.optString(attribute))) {
                String attributeValue = applicationRecord.getString(attribute);
                attributeValues[index] = attributeValue;
            }
        }
        return attributeValues;
    }
    
    /**
     * Method to get the customer general information
     * 
     * @param customerUsername
     * @param requestInstance
     * @return customerId
     * @throws ApplicationException
     */
    public static CustomerBean getCustomerGeneralInformation(String customerUsername,
            DataControllerRequest requestInstance) throws ApplicationException {
        try {
            CustomerBean cb = new CustomerBean();
            String readCustomerResponse;
            if (StringUtils.isBlank(customerUsername))
                return null;
            Map<String, String> postParametersMap = new HashMap<String, String>();
            JSONObject readCustomerResponseJSON;
            postParametersMap.put(ODataQueryConstants.SELECT,
                    "id,FirstName,MiddleName,LastName,Status_id,UserName,Organization_Id");
            postParametersMap.put(ODataQueryConstants.FILTER, "UserName eq '" + customerUsername + "'");
            readCustomerResponse =
                    Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);
            readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
            if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                    && readCustomerResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                    && readCustomerResponseJSON.has("customer")) {
                JSONArray customerJSONArray = readCustomerResponseJSON.getJSONArray("customer");
                if (customerJSONArray.length() > 0) {
                    JSONObject currCustomerObject = customerJSONArray.getJSONObject(0);
                    cb.setId(currCustomerObject.optString("id"));
                    cb.setFirstName(currCustomerObject.optString("FirstName"));
                    cb.setMiddleName(currCustomerObject.optString("MiddleName"));
                    cb.setLastName(currCustomerObject.optString("LastName"));
                    cb.setUsername(currCustomerObject.optString("UserName"));
                    cb.setStatusId(currCustomerObject.optString("Status_id"));
                    cb.setOrganizationId(currCustomerObject.optString("Organization_Id"));
                    return cb;
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_21850);
        }
        return null;
    }

    /**
     * Method to Get Customer Id from Customer Username
     * 
     * @param customerUsername
     * @return customerId
     */
    public static String getCustomerId(String customerUsername) {
        try {
            String customerId = null;
            String readCustomerResponse;
            if (StringUtils.isBlank(customerUsername))
                return customerUsername;
            Map<String, Object> postParametersMap = new HashMap<>();
            JSONObject readCustomerResponseJSON;
            postParametersMap.put(ODataQueryConstants.SELECT, "id");
            postParametersMap.put(ODataQueryConstants.FILTER, "UserName eq '" + customerUsername + "'");
            postParametersMap.put(ODataQueryConstants.TOP, "1");
            // readCustomerResponse =
            // Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParametersMap, null, requestInstance);

            readCustomerResponse =
                    DBPServiceExecutorBuilder.builder().withServiceId(ServiceURLEnum.CUSTOMER_READ.getServiceName())
                            .withOperationId(ServiceURLEnum.CUSTOMER_READ.getOperationName())
                            .withRequestParameters(postParametersMap).build().getResponse();

            readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
            if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                    && readCustomerResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                    && readCustomerResponseJSON.has("customer")) {
                JSONArray customerJSONArray = readCustomerResponseJSON.getJSONArray("customer");
                if (customerJSONArray.length() > 0) {
                    JSONObject currCustomerObject = customerJSONArray.getJSONObject(0);
                    if (currCustomerObject.has("id")) {
                        customerId = currCustomerObject.optString("id");
                    }
                }
            }
            return customerId;
        } catch (Exception e) {
            alert.prepareError("Exception in CustomerHandler getCustomerId" + e).log();
        }
        return null;
    }
    
    public static List<String> getInfinityCustomers(DataControllerRequest requestInstance, Set<String> servicedefinitionList) {
        
    	List<String> idList = new ArrayList<String>();
    	if (servicedefinitionList.size() == 0) {
        	
        	return idList;
        }
            
    	String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_INFINITY_CUSTOMER_FROM_SERVICEDEFINITION_PROC;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		requestParameters.put("_servicedefinitionIds", String.join(",", servicedefinitionList));
		

		String response = null;
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
			
			if (responseObj != null && responseObj.has(FabricConstants.OPSTATUS)
	                && responseObj.getInt(FabricConstants.OPSTATUS) == 0
	                && responseObj.has("records")) {

				JSONArray responseRecords = responseObj.getJSONArray("records");
				if (responseRecords.length() > 0) {
					for(int i=0 ; i<responseRecords.length(); i++ ) {
						
						JSONObject currRecordJSON = responseRecords.getJSONObject(i);
		                if (currRecordJSON.has("customerId")) {
		                	idList.add(currRecordJSON.optString("customerId"));
		                }
		                if (currRecordJSON.has("custId")) {
		                	idList.add(currRecordJSON.optString("custId"));
		                }
					}
	            }
			}
			
		} catch (JSONException jexp) {
			alert.prepareError("JSONException, Unable to find servicedefinition Ids: " + jexp).log();
			
		} catch (Exception exp) {
			alert.prepareError("Caught exception to find servicedefinition Ids: " + exp).log();
		}
		
        return idList;
    }
    
    private static Set<String> splitString(String inputString, String delimiter) {
        Set<String> hashSet = new HashSet<>();
        try {
            hashSet = new HashSet<>(Arrays.asList(StringUtils.split(inputString, delimiter)));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return hashSet;

    }
}
