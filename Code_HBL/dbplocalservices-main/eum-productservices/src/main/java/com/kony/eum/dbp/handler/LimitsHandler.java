package com.kony.eum.dbp.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.temenos.dbx.eum.product.constants.OperationName;
import com.temenos.dbx.eum.product.constants.ServiceId;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbp.dto.AccountLimitDTO;
import com.kony.eum.dbp.dto.CustomerBasicDTO;
import com.kony.eum.dbp.dto.PreviousCustomerActionDTO;
import com.kony.eum.dbp.dto.PreviousLimitDTO;
import com.kony.eum.dbputilities.customersecurityservices.createOrgEmployeeAccounts;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.FeatureActionDTO;
import com.temenos.dbx.product.dto.OrganisationFeaturesDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class LimitsHandler {

    private static final String RETAIL = "RETAIL";
    private static final String NON_ACCOUNT_LEVEL = "NON_ACCOUNT_LEVEL";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Set<String> accoutLevel = null;
    private static Map<String, String> actionFeatureMap = null;
    private static Set<String> monetory = null;
    private static Map<String, String> limitsMapping = null;

    public static void modifyCustomerActionLimits(String organizationId, String contractId, String coreCustomerId,
            String group_id, JSONArray actions,
            JSONArray accounts, JSONArray featuresJSON, String customerId, DataControllerRequest dcRequest,
            Result result) throws Exception, ApplicationException {

        // Get account level action limits, first map contains all the accounts, inner
        // map contains the actions

        Set<String> limits = HelperMethods.getCustomerLimitsTypes();

        String roleType_id = getRoleType(group_id, dcRequest, result);

        Map<String, Map<String, PreviousCustomerActionDTO>> previousCustomerActions = getPreviousCustomerActions(
                customerId, organizationId, group_id, dcRequest, result);

        Set<String> accountsSet = new HashSet<String>();
        JSONObject jsonObject;
        for (int i = 0; i < accounts.length(); i++) {
            jsonObject = accounts.getJSONObject(i);
            if (jsonObject.has("accountId")) {
                accountsSet.add(jsonObject.getString("accountId"));
            }
        }

        List<String> params = new ArrayList<String>();

        for (int i = 0; i < featuresJSON.length(); i++) {
            jsonObject = featuresJSON.getJSONObject(i);
            String featureId = jsonObject.getString("featureID");
            if (jsonObject.has("Actions")) {
                actions = jsonObject.getJSONArray("Actions");
                for (int j = 0; j < actions.length(); j++) {
                    jsonObject = actions.getJSONObject(j);
                    String limitGroupId = null;
                    if (jsonObject.has("limitGroupId") && StringUtils.isNotBlank(jsonObject.getString("limitGroupId")))
                        limitGroupId = jsonObject.getString("limitGroupId");
                    String actionID = jsonObject.getString(DBPUtilitiesConstants.ACTION_ID);
                    boolean isAllowed = false;

                    if (jsonObject.has(DBPUtilitiesConstants.IS_ENABLED)
                            && "true".equals(jsonObject.getString(DBPUtilitiesConstants.IS_ENABLED))) {
                        isAllowed = true;

                    } else if (jsonObject.has(DBPUtilitiesConstants.IS_ENABLED)
                            && "false".equals(jsonObject.getString(DBPUtilitiesConstants.IS_ENABLED))) {
                        isAllowed = false;
                    }

                    boolean isAccoutLevel = accoutLevel.contains(actionID);

                    if (!isAccoutLevel) {
                        createCustomerActionLimit(contractId, coreCustomerId, roleType_id, customerId, featureId,
                                actionID, null, isAllowed, null,
                                null,
                                dcRequest, result, params, limitGroupId);
                    } else if (isAccoutLevel) {

                        Set<String> set = new HashSet<String>();
                        if (jsonObject.has(DBPUtilitiesConstants.ACCOUNTS)
                                && jsonObject.get(DBPUtilitiesConstants.ACCOUNTS) != null) {
                            accounts = jsonObject.getJSONArray(DBPUtilitiesConstants.ACCOUNTS);
                            for (int k = 0; k < accounts.length(); k++) {
                                jsonObject = accounts.getJSONObject(k);

                                isAllowed = false;

                                if (jsonObject.has(DBPUtilitiesConstants.IS_ENABLED)
                                        && "true".equals(jsonObject.getString(DBPUtilitiesConstants.IS_ENABLED))) {
                                    isAllowed = true;

                                } else if (jsonObject.has(DBPUtilitiesConstants.IS_ENABLED)
                                        && "false".equals(jsonObject.getString(DBPUtilitiesConstants.IS_ENABLED))) {
                                    isAllowed = false;
                                }

                                String accountId = jsonObject.getString("id");
                                set.add(accountId);
                                boolean isMonetory = monetory.contains(actionID);

                                if (!isMonetory) {
                                    createCustomerActionLimit(contractId, coreCustomerId, roleType_id,
                                            customerId, featureId, actionID,
                                            accountId,
                                            isAllowed, null, null, dcRequest, result, params, limitGroupId);
                                } else if (isMonetory && isAllowed) {
                                    if (jsonObject.has(DBPUtilitiesConstants.LIMITS)) {
                                        jsonObject = jsonObject.getJSONObject(DBPUtilitiesConstants.LIMITS);
                                    }
                                    for (String limitTypeId : limits) {

                                        Double value = null;
                                        Double previousValue = 0.0;

                                        if (limitTypeId.contains("PRE")) {
                                            value = 0.0;
                                        } else {
                                            value = previousValue;
                                        }

                                        if (jsonObject.has(limitTypeId)) {
                                            if (HelperMethods
                                                    .isStringADouble(jsonObject.getString(limitTypeId))) {
                                                value = Double.parseDouble(jsonObject.getString(limitTypeId));
                                            }
                                        }

                                        if (previousValue != value) {
                                            createCustomerActionLimit(contractId, coreCustomerId, roleType_id,
                                                    customerId, featureId,
                                                    actionID,
                                                    accountId,
                                                    isAllowed, limitTypeId, String.valueOf(value), dcRequest,
                                                    result, params, limitGroupId);

                                        } else {
                                            createCustomerActionLimit(contractId, coreCustomerId, roleType_id,
                                                    customerId, featureId,
                                                    actionID,
                                                    accountId,
                                                    isAllowed, limitTypeId, String.valueOf(previousValue),
                                                    dcRequest, result, params, limitGroupId);
                                        }

                                    }
                                } else {
                                    createCustomerActionLimit(contractId, coreCustomerId, roleType_id,
                                            customerId, featureId, actionID,
                                            accountId,
                                            isAllowed, null, null, dcRequest, result, params, limitGroupId);
                                }
                            }
                        }

                        for (String account : accountsSet) {
                            if (!set.contains(account)) {
                                createCustomerActionLimit(contractId, coreCustomerId, roleType_id, customerId,
                                        featureId, actionID, account,
                                        false, null,
                                        null, dcRequest, result, params, limitGroupId);
                            }
                        }
                    }
                }
            }
        }

        StringBuilder input = new StringBuilder("");
        int queries = params.size();
        if (queries > 0) {
            for (int query = 0; query < queries; query++) {
                String temp = params.get(query);
                if (query < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }

            Map<String, Object> inputParams = new HashMap<String, Object>();

            inputParams.put("_queryInput", input.toString());

            alert.prepareError("Query string : " + input.toString()).log();
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, dcRequest.getHeaderMap(),
                    URLConstants.CUSTOMER_ACTION_SAVE_PROC);
        }
    }

    private static void removeCustomerActions(String customerId, DataControllerRequest dcRequest) {
        try {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("_customerId", customerId);
            HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_ACTION_DELETE_PROC);
            alert.prepareError("executed customeraction delete proc").log();
        } catch (HttpCallException e) {
            alert.prepareError("Error occured while removing actions of custmer", e).log();
        }
    }

    private static void createCustomerActionLimit(String contractId, String coreCustomerId, String roleType_id,
            String customerId, String featureId,
            String actionId,
            String accountId,
            boolean isAllowed, String limitTypeId, String value, DataControllerRequest dcRequest,
            Result result,
            List<String> params, String limitGroupId) {

        StringBuilder query = new StringBuilder("");
        query.append("\"" + roleType_id + "\",");
        query.append("\"" + customerId + "\",");
        query.append("\"" + contractId + "\",");
        query.append("\"" + coreCustomerId + "\",");
        query.append("\"" + featureId + "\",");
        query.append("\"" + actionId + "\",");
        query.append(accountId == null ? "null," : "\"" + accountId + "\",");
        query.append("\"" + (isAllowed ? "1" : "0") + "\",");
        query.append(limitGroupId == null ? "null," : "\"" + limitGroupId + "\",");
        query.append(limitTypeId == null ? "null," : "\"" + limitTypeId + "\",");
        query.append(value == null ? "null" : "\"" + value + "\"");

        params.add(query.toString());

    }

    public static String getRoleType(String group_id, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ApplicationException {
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + group_id);

        String response = ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputParams,
                HelperMethods.getHeaders(dcRequest), URLConstants.MEMBERGROUP_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13520);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("membergroup")) {
            result.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13520);
        }
        JSONArray memberGroup = responseJson.getJSONArray("membergroup");
        if (memberGroup.length() != 1) {
            result.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13520);
        }
        return memberGroup.getJSONObject(0).getString("Type_id");
    }

    private static Map<String, Map<String, PreviousCustomerActionDTO>> getPreviousCustomerActions(String customerId,
            String organizationId, String group_id, DataControllerRequest dcRequest, Result result)
            throws ApplicationException, HttpCallException {
        Map<String, Map<String, PreviousCustomerActionDTO>> previousCustomerActions =
                new HashMap<String, Map<String, PreviousCustomerActionDTO>>();

        List<DBPDTO> featureActions = getFeatureActions(dcRequest);

        accoutLevel = new HashSet<String>();
        monetory = new HashSet<String>();
        actionFeatureMap = new HashMap<String, String>();
        for (DBPDTO dbpdto : featureActions) {
            FeatureActionDTO featureAction = (FeatureActionDTO) dbpdto;
            if (featureAction.isAccountLevel()) {
                accoutLevel.add(featureAction.getId());
            }

            if (featureAction.isMonetory()) {
                monetory.add(featureAction.getId());
            }

            actionFeatureMap.put(featureAction.getId(), featureAction.getFeature_id());
        }

        return previousCustomerActions;
    }

    private static Set<String> removeUnCommonActions(JSONArray orgActions, JSONArray groupActions) {
        Set<String> commonActions = new HashSet<String>();
        Set<String> orgActionSet = new HashSet<String>();
        Set<String> groupActionSet = new HashSet<String>();
        for (int i = 0; i < orgActions.length(); i++) {
            JSONObject jsonObject = orgActions.getJSONObject(i);
            orgActionSet.add(jsonObject.getString("Action_id"));
        }

        for (int i = 0; i < groupActions.length(); i++) {
            JSONObject jsonObject = groupActions.getJSONObject(i);
            groupActionSet.add(jsonObject.getString("Action_id"));
        }

        for (String str : orgActionSet) {
            if (groupActionSet.contains(str)) {
                commonActions.add(str);
            }
        }

        return commonActions;
    }

    private static void loadGroupActions(JSONArray groupActions,
            Map<String, Map<String, PreviousCustomerActionDTO>> previousCustomerActions)
            throws ApplicationException, HttpCallException {
        for (int j = 0; j < groupActions.length(); j++) {
            JSONObject groupAction = groupActions.getJSONObject(j);
            String actionId = groupAction.getString("Action_id");
            String limitTypeId = groupAction.optString("LimitType_id");
            String value = groupAction.optString("value");

            for (Map.Entry<String, Map<String, PreviousCustomerActionDTO>> e : previousCustomerActions.entrySet()) {
                Map<String, PreviousCustomerActionDTO> actions = e.getValue();
                if (actions.containsKey(actionId)) {
                    PreviousCustomerActionDTO currentAction = actions.get(actionId);
                    currentAction.setAllowed(true);
                    currentAction.setDerived(true);
                    Map<String, PreviousLimitDTO> currentLimits = currentAction.getLimits();

                    if (StringUtils.isNotBlank(limitTypeId) && StringUtils.isNotBlank(value)) {
                        PreviousLimitDTO previousLimit = null;
                        if (currentLimits.containsKey(limitTypeId)) {
                            previousLimit = currentLimits.get(limitTypeId);
                            previousLimit.setValue(Math.min(Double.valueOf(value), previousLimit.getValue()));
                        } else {
                            previousLimit = new PreviousLimitDTO();
                            previousLimit.setValue(Double.valueOf(value));
                        }
                        previousLimit.setAllowed(true);
                        previousLimit.setDerived(true);
                        currentLimits.put(limitTypeId, previousLimit);
                    }
                }
            }
        }

    }

    private static void loadOrgActions(JSONArray orgActionsArray, DataControllerRequest dcRequest,
            String organizationId, Set<String> suspendedFeatures, Set<String> commonActions,
            Map<String, Map<String, PreviousCustomerActionDTO>> previousCustomerActions) {

        Set<String> accounts = createOrgEmployeeAccounts.getOrganizationAccounts(dcRequest, organizationId);

        for (int i = 0; i < orgActionsArray.length(); i++) {
            JSONObject orgAction = orgActionsArray.getJSONObject(i);
            String actionId = orgAction.getString("Action_id");

            if (suspendedFeatures.contains(actionId) || !commonActions.contains(actionId)) {
                continue;
            }

            for (String key : accounts) {
                String accountId = key;
                String limitTypeId = orgAction.optString("LimitType_id");
                String value = orgAction.optString("value");

                if (!accoutLevel.contains(actionId)) {
                    accountId = NON_ACCOUNT_LEVEL;
                }
                Map<String, PreviousCustomerActionDTO> actions = new HashMap<String, PreviousCustomerActionDTO>();
                Map<String, PreviousLimitDTO> currentLimits = new HashMap<String, PreviousLimitDTO>();
                // Check if the map contains the account
                if (previousCustomerActions.containsKey(accountId)) {
                    actions = previousCustomerActions.get(accountId);
                }

                PreviousCustomerActionDTO currentAction = new PreviousCustomerActionDTO();
                if (actions.containsKey(actionId)) {
                    currentAction = actions.get(actionId);
                    currentLimits = currentAction.getLimits();
                }

                PreviousLimitDTO previousLimit = new PreviousLimitDTO();

                if (StringUtils.isNotBlank(limitTypeId) && StringUtils.isNotBlank(value)) {
                    if (currentLimits.containsKey(limitTypeId)) {
                        previousLimit = currentLimits.get(limitTypeId);
                        previousLimit.setValue(Math.min(Double.valueOf(value), previousLimit.getValue()));
                    } else {
                        previousLimit.setValue(Double.valueOf(value));
                    }
                    previousLimit.setAllowed(false);
                    previousLimit.setDerived(true);
                    currentLimits.put(limitTypeId, previousLimit);
                }
                currentAction.setAccountId(accountId);
                currentAction.setActionId(actionId);
                currentAction.setLimits(currentLimits);
                actions.put(actionId, currentAction);
                previousCustomerActions.put(accountId, actions);
            }
        }
    }

    private static JSONArray getOrganisationActions(String organizationId, DataControllerRequest dcRequest,
            Result result) throws ApplicationException, HttpCallException {

        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "Organisation_id" + DBPUtilitiesConstants.EQUAL + organizationId);

        String response = ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputParams,
                HelperMethods.getHeaders(dcRequest), URLConstants.ORGANISATION_ACTION_LIMIT_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13511);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("organisationactionlimit")) {
            result.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13511);
        }

        return responseJson.getJSONArray("organisationactionlimit");
    }

    private static JSONArray getGroupActions(String groupId, DataControllerRequest dcRequest, Result result)
            throws ApplicationException, HttpCallException {

        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "Group_id" + DBPUtilitiesConstants.EQUAL + groupId);

        String response = ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputParams,
                HelperMethods.getHeaders(dcRequest), URLConstants.GROUP_ACTION_LIMITS_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13518);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("groupactionlimit")) {
            result.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13518);
        }

        return responseJson.getJSONArray("groupactionlimit");
    }

    public static Map<String, AccountLimitDTO> getCustomerActionLimits(String action, String customerId,
            DataControllerRequest dcRequest, Result result) throws Exception, ApplicationException {
        Map<String, AccountLimitDTO> accountsMap = getAccountActionsFromMemory(action, customerId, dcRequest, result);

        return getCustomerActionLimits(accountsMap, action, customerId, dcRequest, result);
    }

    public static Map<String, AccountLimitDTO> getCustomerActionLimits(Map<String, AccountLimitDTO> accountsMap, String action, String customerId, DataControllerRequest dcRequest, Result result)
            throws Exception, ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Map<String, Double>> accountLimitsMap = new HashMap<>();
        inputParams.put("_customerId", customerId);
        inputParams.put("_featureActionId", action);
        String minAccountLevelLimits = DBPServiceExecutorBuilder.builder()
                .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                .withObjectId(null)
                .withOperationId(OperationName.DB_FETCH_ACCOUNTLEVELCUSTOMERLIMITS_FOR_FEATUREACTION_PROC)
                .withRequestHeaders(dcRequest.getHeaderMap())
                .withRequestParameters(inputParams)
                .build().getResponse();
        // SETTING ONLY THE MAX, DAILY AND WEEKLY LIMITS
        JSONObject responseObj = new JSONObject(minAccountLevelLimits);
        if(responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0){
            if(responseObj.has("records")){
                JSONArray limitsArr = responseObj.getJSONArray("records");
                if(limitsArr.length() == 0){
                    throw new ApplicationException(ErrorCodeEnum.ERR_11026);
                }
                else{
                    for(Object obj: limitsArr){
                        JSONObject limitObj = (JSONObject) obj;
                        String accountId = limitObj.optString("accountId", null);
                        String limitTypeId = limitObj.optString("baseLimitTypeId", null);
                        String minValueStr = limitObj.optString("minLimitValue", null);
                        Double minValue = minValueStr != null ? Double.parseDouble(minValueStr) : 0.0;
                        if(accountId != null && limitTypeId != null && minValue != null){
                            if(!accountLimitsMap.containsKey(accountId)){
                                accountLimitsMap.put(accountId, new HashMap<>());
                            }
                            accountLimitsMap.get(accountId).put(limitTypeId, minValue);
                        }
                    }
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_11026);
            }
        }
        else{
            throw new ApplicationException(ErrorCodeEnum.ERR_12000);
        }

        // ON THE SAME ACCOUNTS MAP, SET ONLY THE PRE-APPROVED AND AUTO-DENY LIMITS
        // the MAX, DAILY and WEEKLY limits are not to be updated
        Set<String> prohibitedLimitTypes = new HashSet<>();
        prohibitedLimitTypes.add("MAX_TRANSACTION_LIMIT");
        prohibitedLimitTypes.add("DAILY_LIMIT");
        prohibitedLimitTypes.add("WEEKLY_LIMIT");
        Set<String> accountIdSet = accountLimitsMap.keySet();
        inputParams.clear();
        inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + "Action_id" + DBPUtilitiesConstants.EQUAL + action);
        String customerActionResponse = DBPServiceExecutorBuilder.builder()
                .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                .withObjectId(null)
                .withOperationId(OperationName.DB_GET_CUSTOMERACTIONS)
                .withRequestHeaders(dcRequest.getHeaderMap())
                .withRequestParameters(inputParams)
                .build().getResponse();
        responseObj = new JSONObject(customerActionResponse);
        if(responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0){
            if(responseObj.has("customeraction")){
                JSONArray customerActionArr = responseObj.getJSONArray("customeraction");
                if(customerActionArr.length() == 0){
                    throw new ApplicationException(ErrorCodeEnum.ERR_11026);
                }
                else{
                    for(Object obj: customerActionArr){
                        JSONObject actionObject = (JSONObject) obj;
                        String accountId = actionObject.optString("Account_id", null);
                        String limitTypeId = actionObject.optString("LimitType_id", null);
                        String valueString = actionObject.optString("value", null);
                        Double value = valueString != null ? Double.parseDouble(valueString) : 0.0;
                        if(accountId != null && limitTypeId != null && value != null){
                            if(!accountLimitsMap.containsKey(accountId)){
                                accountLimitsMap.put(accountId, new HashMap<>());
                            }
                            if(!prohibitedLimitTypes.contains(limitTypeId)) {
                                accountLimitsMap.get(accountId).put(limitTypeId, value);
                            }
                        }
                    }
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_11026);
            }
        }
        else{
            throw new ApplicationException(ErrorCodeEnum.ERR_12000);
        }
        for (Map.Entry<String, AccountLimitDTO> e : accountsMap.entrySet()) {
            String accountId = e.getKey();
            AccountLimitDTO accountLimitDTO = e.getValue();
            Map<String, Double> accLimits = accountLimitsMap.containsKey(accountId) ? accountLimitsMap.get(accountId) : new HashMap<>();
            // PERFORM A SAFETY-CHECK ON PRE-APPROVE AND AUTO-DENY LIMITS. IF VALUES DON'T EXIST, FALL BACK TO THE MAX LIMITS
            if(!accLimits.containsKey("PRE_APPROVED_TRANSACTION_LIMIT")){
                accLimits.put("PRE_APPROVED_TRANSACTION_LIMIT", 0.0);
            }
            if(!accLimits.containsKey("PRE_APPROVED_DAILY_LIMIT")){
                accLimits.put("PRE_APPROVED_DAILY_LIMIT", 0.0);
            }
            if(!accLimits.containsKey("PRE_APPROVED_WEEKLY_LIMIT")){
                accLimits.put("PRE_APPROVED_WEEKLY_LIMIT", 0.0);
            }
            if(!accLimits.containsKey("AUTO_DENIED_TRANSACTION_LIMIT")){
                accLimits.put("AUTO_DENIED_TRANSACTION_LIMIT", accLimits.get("MAX_TRANSACTION_LIMIT"));
            }
            if(!accLimits.containsKey("AUTO_DENIED_DAILY_LIMIT")){
                accLimits.put("AUTO_DENIED_DAILY_LIMIT", accLimits.get("DAILY_LIMIT"));
            }
            if(!accLimits.containsKey("AUTO_DENIED_WEEKLY_LIMIT")){
                accLimits.put("AUTO_DENIED_WEEKLY_LIMIT", accLimits.get("WEEKLY_LIMIT"));
            }
            accountLimitDTO.setLimits(accLimits);
            accountsMap.put(accountId, accountLimitDTO);
        }
        return accountsMap;
    }

    private static Map<String, Double> getOrganisationLimits(String org_id, String actionId,
            DataControllerRequest dataControllerRequest, Result processedResult)
            throws ApplicationException, HttpCallException {

        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "Organisation_id" + DBPUtilitiesConstants.EQUAL + org_id
                + DBPUtilitiesConstants.AND + "Action_id" + DBPUtilitiesConstants.EQUAL + actionId);

        String response = ServiceCallHelper.invokeServiceAndGetString(dataControllerRequest, inputParams,
                HelperMethods.getHeaders(dataControllerRequest), URLConstants.ORGANISATION_ACTION_LIMIT_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13511);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("organisationactionlimit")) {
            processedResult.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13511);
        }

        JSONArray orgLimits = responseJson.getJSONArray("organisationactionlimit");
        Map<String, Double> limits = new HashMap<>();
        for (Object orgLimitObj : orgLimits) {
            JSONObject orgLimit = (JSONObject) orgLimitObj;
            if (orgLimit.has("LimitType_id") && orgLimit.has("value")) {
                limits.put(orgLimit.getString("LimitType_id"), Double.parseDouble(orgLimit.getString("value")));
            }

        }
        return limits;
    }

    private static CustomerBasicDTO getCustomerBasicInfo(String customerId, DataControllerRequest dataControllerRequest,
            Result processedResult) throws HttpCallException, ApplicationException {
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerId);

        String response = ServiceCallHelper.invokeServiceAndGetString(dataControllerRequest, inputParams,
                HelperMethods.getHeaders(dataControllerRequest), URLConstants.CUSTOMER_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13508);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("customer")) {
            processedResult.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13508);
        }
        CustomerBasicDTO cb = new CustomerBasicDTO();
        JSONArray customers = responseJson.getJSONArray("customer");
        if (customers.length() == 1) {
            JSONObject customer = customers.getJSONObject(0);
            cb.setId(customer.getString("id"));
            cb.setUserName(customer.getString("UserName"));
            cb.setStatus_id(customer.getString("Status_id"));

            if (customer.has("Organization_Id")) {
                cb.setOrganization_Id(customer.getString("Organization_Id"));
                cb.setBusinessUser(true);
            } else {
                cb.setBusinessUser(false);
            }
        } else {
            processedResult.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13508);
        }

        return cb;
    }

    private static Map<String, Double> getBaseActionLimits(String actionId, DataControllerRequest dataControllerRequest,
            Result processedResult) throws HttpCallException, ApplicationException {
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "Action_id" + DBPUtilitiesConstants.EQUAL + actionId);

        String response = ServiceCallHelper.invokeServiceAndGetString(dataControllerRequest, inputParams,
                HelperMethods.getHeaders(dataControllerRequest), URLConstants.ACTION_LIMIT_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13507);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("actionlimit")) {
            processedResult.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13507);
        }

        Map<String, Double> limits = new HashMap<>();
        JSONArray actionLimits = responseJson.getJSONArray("actionlimit");
        for (Object actionLimitObj : actionLimits) {
            JSONObject actionLimit = (JSONObject) actionLimitObj;
            if (actionLimit.has("LimitType_id") && actionLimit.has("value")) {
                limits.put(actionLimit.getString("LimitType_id"), Double.parseDouble(actionLimit.getString("value")));
            }
        }

        return limits;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, AccountLimitDTO> getAccountActionsFromMemory(String actionId, String customerId,
            DataControllerRequest dataControllerRequest, Result processedResult) throws Exception {
        String cacheKey = DBPUtilitiesConstants.ACCOUNTS_POSTLOGIN_CACHE_KEY + customerId;
        String cacheValue = (String) MemoryManager.getFromCache(cacheKey);
        JsonParser parser = new JsonParser();
        JsonObject accounts = StringUtils.isNotBlank(cacheValue) && parser.parse(cacheValue).isJsonObject()
                ? parser.parse(cacheValue).getAsJsonObject()
                : new JsonObject();

        List<HashMap<String, String>> accountActions = new ArrayList<>();
        if (JSONUtil.hasKey(accounts, "Accounts") && accounts.get("Accounts").isJsonArray()) {
            accountActions = HelperMethods.getAllRecordsMap(accounts.get("Accounts").getAsJsonArray().toString());
        }

        Map<String, AccountLimitDTO> result = new HashMap<>();

        for (Map<String, String> account : accountActions) {
            JSONArray actionsJSON = new JSONArray(account.get("actions"));
            Set<String> actions = getHashSetFromJSONArray(actionsJSON);
            if (actions.contains(actionId)) {
                AccountLimitDTO accountLimitDTO = new AccountLimitDTO();
                accountLimitDTO.setAccountId(account.get("accountID"));
                accountLimitDTO.setBusinessAccount(Boolean.parseBoolean(account.get("isBusinessAccount")));
                accountLimitDTO.setLimits(new HashMap<String, Double>());
                result.put(account.get("accountID"), accountLimitDTO);
            }
        }
        return result;

    }

    private static Set<String> getHashSetFromJSONArray(JSONArray actionsJSON) {
        Set<String> res = new HashSet<>();
        for (Object stringObj : actionsJSON) {
            res.add((String) stringObj);
        }
        return res;
    }

    private static Map<String, Double> getGroupActionLimits(String customerId, String actionId, String groupId,
            DataControllerRequest dataControllerRequest, Result processedResult)
            throws ApplicationException, HttpCallException {
        Map<String, Object> inputParams = new HashMap<String, Object>();

        inputParams.put(DBPUtilitiesConstants.FILTER, "Group_id" + DBPUtilitiesConstants.EQUAL + groupId
                + DBPUtilitiesConstants.AND + "Action_id" + DBPUtilitiesConstants.EQUAL + actionId);

        String response = ServiceCallHelper.invokeServiceAndGetString(dataControllerRequest, inputParams,
                HelperMethods.getHeaders(dataControllerRequest), URLConstants.GROUP_ACTION_LIMITS_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13527);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("groupactionlimit")) {
            processedResult.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13527);
        }

        JSONArray orgLimits = responseJson.getJSONArray("groupactionlimit");
        Map<String, Double> limits = new HashMap<>();
        for (Object orgLimitObj : orgLimits) {
            JSONObject orgLimit = (JSONObject) orgLimitObj;
            if (orgLimit.has("LimitType_id") && orgLimit.has("value")) {
                limits.put(orgLimit.getString("LimitType_id"), Double.parseDouble(orgLimit.getString("value")));
            }

        }
        return limits;
    }

    private static Map<String, Map<String, Double>> getCustomerDirectActionLimits(String customerId, String actionId,
            DataControllerRequest dataControllerRequest, Result processedResult)
            throws ApplicationException, HttpCallException {
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER,
                "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + "Action_id"
                        + DBPUtilitiesConstants.EQUAL + actionId + DBPUtilitiesConstants.AND + "isAllowed"
                        + DBPUtilitiesConstants.EQUAL + "'1'");

        String response = ServiceCallHelper.invokeServiceAndGetString(dataControllerRequest, inputParams,
                HelperMethods.getHeaders(dataControllerRequest), URLConstants.CUSTOMER_ACTION_LIMITS_GET);
        if (response == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_13512);
        }
        JSONObject responseJson = new JSONObject(response);
        if (HelperMethods.hasErrorOpstatus(responseJson) || !responseJson.has("customeraction")) {
            processedResult.addParam(new Param("FailureReason", String.valueOf(responseJson)));
            throw new ApplicationException(ErrorCodeEnum.ERR_13512);
        }

        Map<String, Map<String, Double>> customerlimits = new HashMap<>();
        JSONArray responseArray = responseJson.getJSONArray("customeraction");
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject customerLimitRow = responseArray.getJSONObject(i);
            Map<String, Double> limits;
            String accountId = RETAIL;
            if (customerLimitRow.has("Account_id")) {
                if (customerlimits.containsKey(customerLimitRow.getString("Account_id"))) {
                    limits = customerlimits.get(customerLimitRow.getString("Account_id"));
                } else {
                    limits = new HashMap<>();
                }
                accountId = customerLimitRow.getString("Account_id");
            } else {
                if (customerlimits.containsKey(RETAIL)) {
                    limits = customerlimits.get(RETAIL);
                } else {
                    limits = new HashMap<>();
                }
            }
            if (customerLimitRow.has("LimitType_id") && customerLimitRow.has("value")) {
                limits.put(customerLimitRow.getString("LimitType_id"),
                        Double.parseDouble(customerLimitRow.getString("value")));
            }
            customerlimits.put(accountId, limits);
        }

        return customerlimits;
    }

    private static boolean isBusinessAction(String roleType_id) {
        return roleType_id.equals("TYPE_ID_BUSINESS") || roleType_id.equals("TYPE_ID_BUSINESS");
    }

    public static String fetchGroupActionLimitsFromDB(String groupId, String actionId, String actionType,
            String isOnlyPremissions, DataControllerRequest dcRequest) {
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("_groupId", groupId);
        if (StringUtils.isNotBlank(actionType))
            inputMap.put("_actionType", actionType);
        else
            inputMap.put("_actionType", "");

        if (StringUtils.isNotBlank(actionId))
            inputMap.put("_actionId", actionId);
        else
            inputMap.put("_actionId", "");

        inputMap.put("_isOnlyPremissions", isOnlyPremissions);

        String response = null;
        try {
            response = ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputMap,
                    HelperMethods.getHeaders(dcRequest), URLConstants.GROUP_ACTIONS_PROC);
        } catch (HttpCallException e) {
            alert.prepareError("Exception when fetching groupActionProc :" + e.getMessage()).log();
        }
        return response;
    }

    public static String fetchOrganizationActionsFromDB(String organizationId, String actionId, String actionType,
            DataControllerRequest dcRequest) {
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("_organizationId", organizationId);
        if (StringUtils.isNotBlank(actionType))
            inputMap.put("_actionType", actionType);
        else
            inputMap.put("_actionType", "");

        if (StringUtils.isNotBlank(actionId))
            inputMap.put("_actionId", actionId);
        else
            inputMap.put("_actionId", "");

        String response = null;
        try {
            response = ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputMap,
                    HelperMethods.getHeaders(dcRequest), URLConstants.ORGANIZATION_ACTIONS_PROC);
        } catch (HttpCallException e) {
            alert.prepareError("Exception while fetching the organization account action limits").log();
        }
        return response;
    }

    public static String fetchFeaturesInformation(DataControllerRequest dcRequest) {
        String response = null;
        try {
            response = ServiceCallHelper.invokeServiceAndGetString(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                    URLConstants.FEATURE_GET);
        } catch (HttpCallException e) {
            alert.prepareError("Exception while fetching the organization account action limits").log();
        }
        return response;
    }

    private static Set<String> loadSuspendedFeatures(DataControllerRequest dcRequest, String organizationId) {
        Set<String> suspendedFeatures = new HashSet<String>();
        String filter = "featureStatus" + DBPUtilitiesConstants.EQUAL + "SID_FEATURE_SUSPENDED"
                + DBPUtilitiesConstants.AND + "organisationId" + DBPUtilitiesConstants.EQUAL + organizationId;
        List<DBPDTO> dbpdtos = DTOUtils.getDTOListfromDB(dcRequest, filter, URLConstants.ORGANISATIONFEATURES_GET,
                false, true);
        suspendedFeatures = new HashSet<String>();
        OrganisationFeaturesDTO dto;
        for (DBPDTO dbpdto : dbpdtos) {
            dto = (OrganisationFeaturesDTO) dbpdto;
            suspendedFeatures.add(dto.getFeatureId());
        }

        return suspendedFeatures;
    }

    private static List<DBPDTO> getFeatureActions(DataControllerRequest dcRequest)
            throws ApplicationException, HttpCallException {
        return DTOUtils.getDTOListfromDB(dcRequest, null, URLConstants.FEATURE_ACTION_GET, true, true);
    }

    public static JsonObject getCustomerGroupOrganisationActionLimit(String customerId, String organisationId,
            String isOnlyPermissionsFlag, DataControllerRequest dcRequest, Result result) {

        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put("_customerId", customerId);
        inputParams.put("_organisationId", organisationId);
        inputParams.put("_isOnlyPremissions", isOnlyPermissionsFlag);

        String response = null;
        try {

            response = ServiceCallHelper.invokeServiceAndGetString(dcRequest, inputParams,
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_ORG_GROUP_ACTION_LIMITS);
        } catch (HttpCallException e) {
            alert.prepareError("Exception occured while fetching customer_group_org_actionlimits_proc :" + e.getMessage()).log();
        }
        if (response == null) {
            ErrorCodeEnum.ERR_10708.setErrorCode(result);
            return null;
        }

        JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();

        if (!jsonResponse.has("opstatus") || jsonResponse.get("opstatus").getAsInt() != 0
                || !jsonResponse.has("records")) {
            ErrorCodeEnum.ERR_10708.setErrorCode(result);
            return null;
        }
        return jsonResponse;
    }

    public static void modifyCustomerActionLimitsForOwner(String organizationId, String group_id, JsonArray accounts,
            String customerId, DataControllerRequest dcRequest, Result result) {
        Map<String, Map<String, PreviousCustomerActionDTO>> previousCustomerActions =
                new HashMap<String, Map<String, PreviousCustomerActionDTO>>();
        try {
            getPreviousCustomerActions(customerId, organizationId, group_id, dcRequest, result);
        } catch (ApplicationException | HttpCallException e) {
            alert.prepareError(e.toString()).log();
        }

        Map<String, PreviousCustomerActionDTO> previousActions;

        Map<String, String> limitsTypeMapping = getLimitsTypeMap();
        String roleType_id = null;
        try {
            roleType_id = getRoleType(group_id, dcRequest, result);
        } catch (HttpCallException | ApplicationException e) {
        	 alert.prepareError(e.toString()).log();
        }
        String accountID = null;
        boolean isAllowed = true;
        String actionID = null;
        List<String> params = new ArrayList<String>();
        PreviousCustomerActionDTO previousCustomerActionDTO = null;
        Map<String, PreviousLimitDTO> previousLimits = null;
        for (Entry<String, Map<String, PreviousCustomerActionDTO>> entry : previousCustomerActions.entrySet()) {

            accountID = entry.getKey();

            if (accountID.equals(NON_ACCOUNT_LEVEL)) {
                accountID = null;
            }

            previousActions = entry.getValue();

            for (Entry<String, PreviousCustomerActionDTO> entry2 : previousActions.entrySet()) {

                actionID = entry2.getKey();
                previousCustomerActionDTO = entry2.getValue();
                previousLimits = previousCustomerActionDTO.getLimits();

                Set<String> limits = HelperMethods.getCustomerLimitsTypes();

                if (previousLimits != null && previousLimits.size() > 0) {
                    for (String limitTypeId : limits) {
                        limitTypeId = limitsTypeMapping.get(limitTypeId);
                        PreviousLimitDTO limitsDTO = previousLimits.get(limitTypeId);
                        if (limitsDTO != null) {
                            // createCustomerActionLimit(roleType_id, customerId, actionID, accountID, isAllowed,
                            // limitTypeId,
                            // limitsDTO.getValue() + "", dcRequest, result, params);
                        } else {
                            // createCustomerActionLimit(roleType_id, customerId, actionID, accountID, isAllowed,
                            // limitTypeId,
                            // "0", dcRequest, result, params);
                        }
                    }
                } else {
                    // createCustomerActionLimit(roleType_id, customerId, actionID, accountID, isAllowed, null, null,
                    // dcRequest, result, params);
                }
            }
        }

        StringBuilder input = new StringBuilder("");
        int queries = params.size();
        if (queries > 0) {
            for (int query = 0; query < queries; query++) {
                String temp = params.get(query);
                if (query < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }

            Map<String, Object> inputParams = new HashMap<String, Object>();

            inputParams.put("_queryInput", input.toString());

            ServiceCallHelper.invokeServiceAndGetJson(inputParams, dcRequest.getHeaderMap(),
                    URLConstants.CUSTOMER_ACTION_SAVE_PROC);
        }
    }

    private static Map<String, String> getLimitsTypeMap() {

        if (limitsMapping == null) {
            limitsMapping = new HashMap<String, String>();
            limitsMapping.put("AUTO_DENIED_WEEKLY_LIMIT", "WEEKLY_LIMIT");
            limitsMapping.put("PRE_APPROVED_WEEKLY_LIMIT", "PRE_APPROVED_WEEKLY_LIMIT");
            limitsMapping.put("PRE_APPROVED_TRANSACTION_LIMIT", "MIN_TRANSACTION_LIMIT");
            limitsMapping.put("AUTO_DENIED_TRANSACTION_LIMIT", "MAX_TRANSACTION_LIMIT");
            limitsMapping.put("PRE_APPROVED_DAILY_LIMIT", "PRE_APPROVED_DAILY_LIMIT");
            limitsMapping.put("AUTO_DENIED_DAILY_LIMIT", "DAILY_LIMIT");
        }

        return limitsMapping;
    }
}
