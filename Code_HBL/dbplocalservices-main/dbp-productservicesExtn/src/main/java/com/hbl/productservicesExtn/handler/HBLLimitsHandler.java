package com.hbl.productservicesExtn.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.kony.dbp.dto.AccountLimitDTO;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class HBLLimitsHandler {
	public static Map<String, AccountLimitDTO> getCustomerActionLimits(Map<String, AccountLimitDTO> accountsMap,
            String action, String customerId, DataControllerRequest dcRequest, Result result)
            throws Exception, ApplicationException {
//        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> masterLimitsMap = fetchCustomerAccountLevelMasterLimits(customerId, action);
        Map<String, Map<String, Double>> accountLimitsMap = (Map<String, Map<String, Double>>) masterLimitsMap.get(Constants.CUSTOMER_LIMITS);
//        inputParams.put("_customerId", customerId);
//        inputParams.put("_featureActionId", action);
//        String minAccountLevelLimits = DBPServiceExecutorBuilder.builder()
//                .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
//                .withObjectId(null)
//                .withOperationId(OperationName.DB_FETCH_ACCOUNTLEVELCUSTOMERLIMITS_FOR_FEATUREACTION_PROC)
//                .withRequestHeaders(dcRequest.getHeaderMap())
//                .withRequestParameters(inputParams)
//                .build().getResponse();
//        // SETTING ONLY THE MAX, DAILY AND WEEKLY LIMITS
//        JSONObject responseObj = new JSONObject(minAccountLevelLimits);
//        if(responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0){
//            if(responseObj.has("records")){
//                JSONArray limitsArr = responseObj.getJSONArray("records");
//                if(limitsArr.length() == 0){
//                    throw new ApplicationException(ErrorCodeEnum.ERR_11026);
//                }
//                else{
//                    for(Object obj: limitsArr){
//                        JSONObject limitObj = (JSONObject) obj;
//                        String accountId = limitObj.optString("accountId", null);
//                        String limitTypeId = limitObj.optString("baseLimitTypeId", null);
//                        String minValueStr = limitObj.optString("minLimitValue", null);
//                        Double minValue = minValueStr != null ? Double.parseDouble(minValueStr) : 0.0;
//                        if(accountId != null && limitTypeId != null && minValue != null){
//                            if(!accountLimitsMap.containsKey(accountId)){
//                                accountLimitsMap.put(accountId, new HashMap<>());
//                            }
//                            accountLimitsMap.get(accountId).put(limitTypeId, minValue);
//                        }
//                    }
//                }
//            } else {
//                throw new ApplicationException(ErrorCodeEnum.ERR_11026);
//            }
//        }
//        else{
//            throw new ApplicationException(ErrorCodeEnum.ERR_12000);
//        }
//
//        // ON THE SAME ACCOUNTS MAP, SET ONLY THE PRE-APPROVED AND AUTO-DENY LIMITS
//        // the MAX, DAILY and WEEKLY limits are not to be updated
//        Set<String> prohibitedLimitTypes = new HashSet<>();
//        prohibitedLimitTypes.add("MAX_TRANSACTION_LIMIT");
//        prohibitedLimitTypes.add("DAILY_LIMIT");
//        prohibitedLimitTypes.add("WEEKLY_LIMIT");
//        Set<String> accountIdSet = accountLimitsMap.keySet();
//        inputParams.clear();
//        inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + "Action_id" + DBPUtilitiesConstants.EQUAL + action);
//        String customerActionResponse = DBPServiceExecutorBuilder.builder()
//                .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
//                .withObjectId(null)
//                .withOperationId(OperationName.DB_GET_CUSTOMERACTIONS)
//                .withRequestHeaders(dcRequest.getHeaderMap())
//                .withRequestParameters(inputParams)
//                .build().getResponse();
//        responseObj = new JSONObject(customerActionResponse);
//        if(responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0){
//            if(responseObj.has("customeraction")){
//                JSONArray customerActionArr = responseObj.getJSONArray("customeraction");
//                if(customerActionArr.length() == 0){
//                    throw new ApplicationException(ErrorCodeEnum.ERR_11026);
//                }
//                else{
//                    for(Object obj: customerActionArr){
//                        JSONObject actionObject = (JSONObject) obj;
//                        String accountId = actionObject.optString("Account_id", null);
//                        String limitTypeId = actionObject.optString("LimitType_id", null);
//                        String valueString = actionObject.optString("value", null);
//                        Double value = valueString != null ? Double.parseDouble(valueString) : 0.0;
//                        if(accountId != null && limitTypeId != null && value != null){
//                            if(!accountLimitsMap.containsKey(accountId)){
//                                accountLimitsMap.put(accountId, new HashMap<>());
//                            }
//                            if(!prohibitedLimitTypes.contains(limitTypeId)) {
//                                accountLimitsMap.get(accountId).put(limitTypeId, value);
//                            }
//                        }
//                    }
//                }
//            } else {
//                throw new ApplicationException(ErrorCodeEnum.ERR_11026);
//            }
//        }
//        else{
//            throw new ApplicationException(ErrorCodeEnum.ERR_12000);
//        }
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
            // Mobile limits
            
            if(!accLimits.containsKey(HBLConstants.MB_PRE_APPROVED_TRANSACTION_LIMIT)){
                accLimits.put(HBLConstants.MB_PRE_APPROVED_TRANSACTION_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(HBLConstants.MB_PRE_APPROVED_DAILY_LIMIT)){
                accLimits.put(HBLConstants.MB_PRE_APPROVED_DAILY_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(HBLConstants.MB_PRE_APPROVED_WEEKLY_LIMIT)){
                accLimits.put(HBLConstants.MB_PRE_APPROVED_WEEKLY_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(HBLConstants.MB_AUTO_DENIED_TRANSACTION_LIMIT)){
                accLimits.put(HBLConstants.MB_AUTO_DENIED_TRANSACTION_LIMIT, accLimits.get(HBLConstants.MB_MAX_TRANSACTION_LIMIT));
            }
            if(!accLimits.containsKey(HBLConstants.MB_AUTO_DENIED_DAILY_LIMIT)){
                accLimits.put(HBLConstants.MB_AUTO_DENIED_DAILY_LIMIT, accLimits.get(HBLConstants.MB_DAILY_LIMIT));
            }
            if(!accLimits.containsKey(HBLConstants.MB_AUTO_DENIED_WEEKLY_LIMIT)){
                accLimits.put(HBLConstants.MB_AUTO_DENIED_WEEKLY_LIMIT, accLimits.get(HBLConstants.MB_WEEKLY_LIMIT));
            }
            
            accountLimitDTO.setLimits(accLimits);
            accountsMap.put(accountId, accountLimitDTO);
        }
        return accountsMap;
    }
	public static Map<String, Object> fetchCustomerAccountLevelMasterLimits(String customerId, String featureActionId) throws ApplicationException {
        Map<String, Object> masterLimitsMap = new HashMap<>();
        Map<String, Double> baseLimitsMap = new HashMap<>();
        Map<String, Double> contractLimitsMap = new HashMap<>();
        Map<String, Double> serviceDefLimitsMap = new HashMap<>();
        Map<String, Map<String, Double>> accountLimitsMap = new HashMap<>();
        Map<String, Object> inputParams = new HashMap<>();
        String limitGroupId = null;
        // <contractId, <coreCustomerId, <roleId, <limitTypeId, value>>>>
        Map<String, Map<String, Map<String, Map<String, Double>>>> roleLimitsMap = new HashMap<>();

        inputParams.put("_customerId", customerId);
        inputParams.put("_featureActionId", featureActionId);
        JSONObject responseObj;
        try{
            String minAccountLevelLimits = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_ACCOUNTLEVELCUSTOMERLIMITS_FOR_FEATUREACTION_PROC)
                    .withRequestParameters(inputParams)
                    .build().getResponse();
            responseObj = new JSONObject(minAccountLevelLimits);
            if(responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0){
                if(responseObj.has("records")){
                    JSONArray limitsArr = responseObj.getJSONArray("records");
                    if(limitsArr.length() == 0){
                        throw new ApplicationException(ErrorCodeEnum.ERR_11026);
                    }
                    else{
                        for(Object obj: limitsArr){
                            JSONObject limitObj = (JSONObject) obj;
                            String limitTypeId = limitObj.optString("baseLimitTypeId", null);
                            String baseLimitValueStr = limitObj.optString("baseLimitValue", null);
                            Double baseLimitValue =  baseLimitValueStr != null ? Double.parseDouble(baseLimitValueStr) : 0.0;
                            String serviceDefLimitValueStr = limitObj.optString("serviceDefLimitValue", null);
                            Double serviceDefLimitValue =  serviceDefLimitValueStr != null ? Double.parseDouble(serviceDefLimitValueStr) : 0.0;
                            String contractLimitValueStr = limitObj.optString("contractLimitValue", null);
                            Double contractLimitValue =  contractLimitValueStr != null ? Double.parseDouble(contractLimitValueStr) : 0.0;
                            String roleLimitValueStr = limitObj.optString("roleLimitValue", null);
                            Double roleLimitValue =  roleLimitValueStr != null ? Double.parseDouble(roleLimitValueStr) : 0.0;
                            String minValueStr = limitObj.optString("minLimitValue", null);
                            Double minValue = minValueStr != null ? Double.parseDouble(minValueStr) : 0.0;
                            String accountId = limitObj.optString("accountId", null);
                            limitGroupId = limitObj.optString("limitGroupId", null);
                            String contractId = limitObj.optString("contractId", null);
                            String coreCustomerId = limitObj.optString("coreCustomerId", null);
                            String roleId = limitObj.optString("roleId", null);

                            // setting base limits
                            if(limitTypeId != null){
                                if(!baseLimitsMap.containsKey(limitTypeId)){
                                    baseLimitsMap.put(limitTypeId, baseLimitValue);
                                }
                                if(!serviceDefLimitsMap.containsKey(limitTypeId)){
                                    serviceDefLimitsMap.put(limitTypeId, serviceDefLimitValue);
                                }
                                if(!contractLimitsMap.containsKey(limitTypeId)){
                                    contractLimitsMap.put(limitTypeId, contractLimitValue);
                                }
                                if(contractId != null && coreCustomerId != null && roleId != null){
                                    if(!roleLimitsMap.containsKey(contractId)){
                                        roleLimitsMap.put(contractId, new HashMap<>());
                                    }
                                    if(!roleLimitsMap.get(contractId).containsKey(coreCustomerId)){
                                        roleLimitsMap.get(contractId).put(coreCustomerId, new HashMap<>());
                                    }
                                    if(!roleLimitsMap.get(contractId).get(coreCustomerId).containsKey(roleId)){
                                        roleLimitsMap.get(contractId).get(coreCustomerId).put(roleId, new HashMap<>());
                                    }
                                    if(!roleLimitsMap.get(contractId).get(coreCustomerId).get(roleId).containsKey(limitTypeId)){
                                        roleLimitsMap.get(contractId).get(coreCustomerId).get(roleId).put(limitTypeId, roleLimitValue);
                                    }
                                }
                                // setting customer account level limits
                                if(accountId != null && minValue != null){
                                    if(!accountLimitsMap.containsKey(accountId)){
                                        accountLimitsMap.put(accountId, new HashMap<>());
                                    }
                                    accountLimitsMap.get(accountId).put(limitTypeId, minValue);
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
        } catch (JSONException je){

        } catch (Exception e){

        }

        // ON THE SAME ACCOUNTS MAP, SET ONLY THE PRE-APPROVED AND AUTO-DENY LIMITS
        // the MAX, DAILY and WEEKLY limits are not to be updated
        Set<String> prohibitedLimitTypes = new HashSet<>(Arrays.asList(Constants.MAX_TRANSACTION_LIMIT, Constants.DAILY_LIMIT, Constants.WEEKLY_LIMIT, HBLConstants.MB_MAX_TRANSACTION_LIMIT, HBLConstants.MB_DAILY_LIMIT, HBLConstants.MB_WEEKLY_LIMIT));

        inputParams.clear();
        inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + "Action_id" + DBPUtilitiesConstants.EQUAL + featureActionId);
        try{
            String customerActionResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_GET_CUSTOMERACTIONS)
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
        } catch (JSONException je){

        } catch (DBPApplicationException e){

        }

        // PERFORM A SAFETY-CHECK ON PRE-APPROVE AND AUTO-DENY LIMITS. IF VALUES DON'T EXIST, FALL BACK TO THE MAX LIMITS
        for(Map.Entry<String, Map<String, Double>> accountLimit : accountLimitsMap.entrySet()){
            Map<String, Double> accLimits = accountLimitsMap.get(accountLimit.getKey());

            if(!accLimits.containsKey(Constants.PRE_APPROVED_TRANSACTION_LIMIT)){
                accLimits.put(Constants.PRE_APPROVED_TRANSACTION_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(Constants.PRE_APPROVED_DAILY_LIMIT)){
                accLimits.put(Constants.PRE_APPROVED_DAILY_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(Constants.PRE_APPROVED_WEEKLY_LIMIT)){
                accLimits.put(Constants.PRE_APPROVED_WEEKLY_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(Constants.AUTO_DENIED_TRANSACTION_LIMIT)){
                accLimits.put(Constants.AUTO_DENIED_TRANSACTION_LIMIT, accLimits.get(Constants.MAX_TRANSACTION_LIMIT));
            }
            if(!accLimits.containsKey(Constants.AUTO_DENIED_DAILY_LIMIT)){
                accLimits.put(Constants.AUTO_DENIED_DAILY_LIMIT, accLimits.get(Constants.DAILY_LIMIT));
            }
            if(!accLimits.containsKey(Constants.AUTO_DENIED_WEEKLY_LIMIT)){
                accLimits.put(Constants.AUTO_DENIED_WEEKLY_LIMIT, accLimits.get(Constants.WEEKLY_LIMIT));
            }
            
           // Mobile limits
            
            if(!accLimits.containsKey(HBLConstants.MB_PRE_APPROVED_TRANSACTION_LIMIT)){
                accLimits.put(HBLConstants.MB_PRE_APPROVED_TRANSACTION_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(HBLConstants.MB_PRE_APPROVED_DAILY_LIMIT)){
                accLimits.put(HBLConstants.MB_PRE_APPROVED_DAILY_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(HBLConstants.MB_PRE_APPROVED_WEEKLY_LIMIT)){
                accLimits.put(HBLConstants.MB_PRE_APPROVED_WEEKLY_LIMIT, 0.0);
            }
            if(!accLimits.containsKey(HBLConstants.MB_AUTO_DENIED_TRANSACTION_LIMIT)){
                accLimits.put(HBLConstants.MB_AUTO_DENIED_TRANSACTION_LIMIT, accLimits.get(HBLConstants.MB_MAX_TRANSACTION_LIMIT));
            }
            if(!accLimits.containsKey(HBLConstants.MB_AUTO_DENIED_DAILY_LIMIT)){
                accLimits.put(HBLConstants.MB_AUTO_DENIED_DAILY_LIMIT, accLimits.get(HBLConstants.MB_DAILY_LIMIT));
            }
            if(!accLimits.containsKey(HBLConstants.MB_AUTO_DENIED_WEEKLY_LIMIT)){
                accLimits.put(HBLConstants.MB_AUTO_DENIED_WEEKLY_LIMIT, accLimits.get(HBLConstants.MB_WEEKLY_LIMIT));
            }
        }
        masterLimitsMap.put(Constants.BASE_LIMITS, baseLimitsMap);
        masterLimitsMap.put(Constants.SERVICE_DEF_LIMITS, serviceDefLimitsMap);
        masterLimitsMap.put(Constants.CONTRACT_LIMITS, contractLimitsMap);
        masterLimitsMap.put(Constants.ROLE_LIMITS, roleLimitsMap);
        masterLimitsMap.put(Constants.CUSTOMER_LIMITS, accountLimitsMap);
        masterLimitsMap.put(Constants.LIMITGROUPID, limitGroupId);
        return masterLimitsMap;
    }


}
