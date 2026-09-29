package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerAccountsBusinessDelegateImpl implements CustomerAccountsBusinessDelegate {
    Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public CustomerAccountsDTO createCustomerAccounts(CustomerAccountsDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        CustomerAccountsDTO resultDTO = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getId())) {
            return resultDTO;
        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            HelperMethods.removeNullValues(inputParams);
            JsonObject customerAccountsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMERACCOUNTS_CREATE);
            if (JSONUtil.isJsonNotNull(customerAccountsJson)
                    && JSONUtil.hasKey(customerAccountsJson, DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)
                    && customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).isJsonArray()) {
                JsonArray customerAccountsArray =
                        customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).getAsJsonArray();
                JsonObject object = customerAccountsArray.size() > 0 ? customerAccountsArray.get(0).getAsJsonObject()
                        : new JsonObject();
                resultDTO = (CustomerAccountsDTO) DTOUtils.loadJsonObjectIntoObject(object,
                        CustomerAccountsDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10319);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10319);
        }

        return resultDTO;
    }

    @Override
    public List<CustomerAccountsDTO> getCustomerAccounts(CustomerAccountsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        List<CustomerAccountsDTO> dtoList = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + dto.getAccountId();
        
        if(StringUtils.isNotBlank(dto.getCompanyLegalUnit())) {
        	filter = filter + DBPUtilitiesConstants.AND + 
        			"companyLegalUnit" + DBPUtilitiesConstants.EQUAL + dto.getCompanyLegalUnit();
        }
        
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        
        try {
            JsonObject customerAccountsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMERACCOUNTS_GET);
            if (JSONUtil.isJsonNotNull(customerAccountsJson)
                    && JSONUtil.hasKey(customerAccountsJson, DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)
                    && customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).isJsonArray()) {
                JsonArray customerAccountsArray =
                        customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).getAsJsonArray();
                for (JsonElement json : customerAccountsArray) {
                    dtoList.add((CustomerAccountsDTO) DTOUtils.loadJsonObjectIntoObject(json.getAsJsonObject(),
                            CustomerAccountsDTO.class, true));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the list of customers associated to the accountId"
                    + e.getMessage()).log();
        }
        return dtoList;
    }
    
    @Override
    public List<CustomerAccountsDTO> getCustomerAccountsOnCustomerId(CustomerAccountsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        List<CustomerAccountsDTO> dtoList = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + dto.getCustomerId());
        try {
            JsonObject customerAccountsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMERACCOUNTS_GET);
            if (JSONUtil.isJsonNotNull(customerAccountsJson)
                    && JSONUtil.hasKey(customerAccountsJson, DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)
                    && customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).isJsonArray()) {
                JsonArray customerAccountsArray =
                        customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).getAsJsonArray();
                for (JsonElement json : customerAccountsArray) {
                    dtoList.add((CustomerAccountsDTO) DTOUtils.loadJsonObjectIntoObject(json.getAsJsonObject(),
                            CustomerAccountsDTO.class, true));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the list of customers associated to the customerId"
                    + e.getMessage()).log();
        }
        return dtoList;
    }

    @Override
    public void createCustomerAccounts(String userId, String contractId, String coreCustomerId, Set<String> accounts,
            Map<String, Object> headersMap) throws ApplicationException {

        Map<String, Object> inputParams = new HashMap<>();
        StringBuilder accountsCSV = new StringBuilder();

        for (String account : accounts) {
            accountsCSV.append(account).append(DBPUtilitiesConstants.COMMA_SEPERATOR);
        }
        if (accountsCSV.length() > 0)
            accountsCSV.replace(accountsCSV.length() - 1, accountsCSV.length(), "");

        try {
            inputParams.put("_userId", userId);
            inputParams.put("_accountsCSV", accountsCSV.toString());
            inputParams.put("_coreCustomerId", coreCustomerId);
            inputParams.put("_contractId", contractId);

            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.USERACCOUNTS_CREATE_PROC);

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10387);
        }

    }

    @Override
    public Set<String> getValidCustomerAccounts(Set<String> customerAccountsList, String customerId,
            Map<String, Object> headersMap)
            throws ApplicationException {
        Set<String> validCustomers = new HashSet<String>();
        try {
            if (customerAccountsList == null || customerAccountsList.isEmpty()) {
                return validCustomers;
            }
            StringBuilder customerAccountsCSV = new StringBuilder();
            for (String customer : customerAccountsList) {
                if (StringUtils.isBlank(customerAccountsCSV)) {
                    customerAccountsCSV.append(customer);
                } else {
                    customerAccountsCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(customer);
                }

            }

            Map<String, Object> inputParams = new HashMap<>();
            String validCustomerAccountsCSV;
            inputParams.put("_customeraccountsCSV", customerAccountsCSV.toString());
            inputParams.put("_customerId", customerId);
            JsonObject validcustomersListJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.GET_VALID_CUSTOMERACCOUNTS_LIST_PROC);
            if (null != validcustomersListJson
                    && JSONUtil.hasKey(validcustomersListJson, DBPDatasetConstants.DATASET_RECORDS)
                    && validcustomersListJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray array = validcustomersListJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (null != object && JSONUtil.hasKey(object, "validAccounts")) {
                        validCustomerAccountsCSV = object.get("validAccounts").getAsString();
                        validCustomers =
                                HelperMethods.splitString(validCustomerAccountsCSV,
                                        DBPUtilitiesConstants.COMMA_SEPERATOR);
                    }
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10792);
        }

        return validCustomers;
    }

	@Override
    public boolean checkIfAccountPermissionEnabled(String userId, String accountNumber, Set<String> permissions,
            Map<String, Object> headersMap) throws ApplicationException {
       
        try {
           
            String userAccountDetails = (String)MemoryManager.getFromCache(DBPUtilitiesConstants.ACCOUNTS_POSTLOGIN_CACHE_KEY+userId);
            JsonArray jsonArray= new JsonParser().parse(userAccountDetails).getAsJsonObject().get(DBPUtilitiesConstants.ACCOUNTS).getAsJsonArray();
           
            if(null == jsonArray || jsonArray.size() == 0) {
               
                return false;
            }
           
            Set<String> actionsSet = new HashSet<String>();
            for (JsonElement element : jsonArray) {
                JsonObject object = element.getAsJsonObject();
                  String accountId = object.get("Account_id").getAsString();
                  if(accountId.equals(accountNumber)) {
                     
                      String actions = object.get("actions").getAsString();
                      JsonArray actionsJsonArray = new JsonParser().parse(actions).getAsJsonArray();
                    if(null== actionsJsonArray || actionsJsonArray.size() == 0) {
                       
                        return false;
                    }
                    for(JsonElement actionElement : actionsJsonArray ) {
                        actionsSet.add(actionElement.getAsString());
                    }
                   
                    if(actionsSet.containsAll(permissions)) {
                   
                        return true;
                    }
                   
                    break;
                  }
            }
           
        }catch(Exception exp) {
           
            alert.prepareError("Error occured while validating details within method checkIfAccountPermissionEnabled\n"+exp).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_29012);
        }
       
        return false;
    }
	
	@Override
    public boolean checkIfCustomerHasThisAccount(String accountNumber, String customerNumber, Map<String, Object> headersMap)
            throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        StringBuilder filter = new StringBuilder();
        
        filter.append("Account_id").append(DBPUtilitiesConstants.EQUAL).append(accountNumber).append(DBPUtilitiesConstants.AND)
        .append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(customerNumber);
        
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        try {
            JsonObject customerAccountsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMERACCOUNTS_GET);
            if (JSONUtil.isJsonNotNull(customerAccountsJson)
                    && JSONUtil.hasKey(customerAccountsJson, DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)
                    && customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).isJsonArray()) {
                JsonArray customerAccountsArray =
                        customerAccountsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS).getAsJsonArray();
                if(customerAccountsArray.size() > 0) return true;
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the list of customers associated to the accountId"
                    + e.getMessage()).log();
        }
        return false;
    }

}
