package com.temenos.dbx.product.contract.backenddelegate.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractAccountBackendDelegate;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOUtils;

public class ContractAccountBackendDelegateImpl implements ContractAccountBackendDelegate {

    private static final String CONTRACT_ID = "contractId";
    private static final String CORECUSTOMER_ID = "coreCustomerId";

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<ContractAccountsDTO> getContractCustomerAccounts(String contractId, String customerId,
            Map<String, Object> headersMap) throws ApplicationException {
        List<ContractAccountsDTO> dtoList = null;
        try {
            Map<String, Object> inputParams = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotBlank(contractId)) {
                sb.append(CONTRACT_ID).append(DBPUtilitiesConstants.EQUAL).append(contractId);
            }
            if (StringUtils.isNotBlank(customerId)) {
                if (StringUtils.isNotBlank(sb.toString())) {
                    sb.append(DBPUtilitiesConstants.AND);
                }
                sb.append(CORECUSTOMER_ID).append(DBPUtilitiesConstants.EQUAL).append(customerId);
            }
            inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
            JsonObject contractAccountsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTACCOUNT_GET);
            if (null != contractAccountsJson
                    && JSONUtil.hasKey(contractAccountsJson, DBPDatasetConstants.DATASET_CONTRACTACCOUNT)
                    && contractAccountsJson.get(DBPDatasetConstants.DATASET_CONTRACTACCOUNT).isJsonArray()) {
                JsonArray array = contractAccountsJson.get(DBPDatasetConstants.DATASET_CONTRACTACCOUNT)
                        .getAsJsonArray();
                dtoList = JSONUtils.parseAsList(array.toString(), ContractAccountsDTO.class);
                for (ContractAccountsDTO dto : dtoList) {
                    String typeId = dto.getTypeId();
                    dto.setAccountType(HelperMethods.getAccountsNames().get(typeId));
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10362);
        }

        return dtoList;
    }

    @Override
    public ContractAccountsDTO createContractAccount(ContractAccountsDTO inputAccountDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractAccountsDTO resultAccountDTO = null;
        if (inputAccountDTO == null || StringUtils.isBlank(inputAccountDTO.getId())
                || StringUtils.isBlank(inputAccountDTO.getContractId())) {
            return resultAccountDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputAccountDTO, false);
            JsonObject contractAccountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTACCOUNT_CREATE);
            if (JSONUtil.isJsonNotNull(contractAccountJson)
                    && JSONUtil.hasKey(contractAccountJson, DBPDatasetConstants.DATASET_CONTRACTACCOUNT)
                    && contractAccountJson.get(DBPDatasetConstants.DATASET_CONTRACTACCOUNT).isJsonArray()) {
                JsonArray contractAccountArray =
                        contractAccountJson.get(DBPDatasetConstants.DATASET_CONTRACTACCOUNT).getAsJsonArray();
                JsonObject object =
                        contractAccountArray.size() > 0 ? contractAccountArray.get(0).getAsJsonObject()
                                : new JsonObject();
                resultAccountDTO =
                        (ContractAccountsDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                ContractAccountsDTO.class, false);
                if (resultAccountDTO == null || StringUtils.isBlank(resultAccountDTO.getId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10357);
                }

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10357);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10357);
        }
        return resultAccountDTO;
    }

    @Override
    public DBXResult validateAccountsContractAssociation(JsonArray customerAccountsJsonArray,
            Map<String, Object> headersMap)
            throws ApplicationException {
        final String DATASET_RECORDS1 = "records1";
        final String PARAM_ACCOUNTID_LIST = "accountIdList";
        final String PARAM_EXCLUDED_ACCOUNTID_LIST = "excludedaccountIdList";
        DBXResult response = new DBXResult();
        StringBuilder accountIdList = new StringBuilder();
        Set<String> accountsSet = new HashSet<>();
        Set<String> excludedAccountsSet = new HashSet<>();
        for (JsonElement jsonElement : customerAccountsJsonArray) {
            if (StringUtils.isNotBlank(accountIdList))
                accountIdList.append(",");
            accountIdList.append(JSONUtil.getString(jsonElement.getAsJsonObject(), "accountId"));
        }
        Map<String, Object> procParams = new HashMap<>();
        procParams.put("_accountIdList", accountIdList.toString());
        try {
            JsonObject json = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
                    URLConstants.GET_ASSOCIATED_CONTRACT_ACCOUNTS);
            if (JSONUtil.hasKey(json, DBPDatasetConstants.DATASET_RECORDS)
                    && json.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0
                    && JSONUtil.hasKey(
                            json.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().get(0).getAsJsonObject(),
                            PARAM_ACCOUNTID_LIST)
                    && StringUtils.isNotBlank(JSONUtil.getString(
                            json.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().get(0).getAsJsonObject(),
                            PARAM_ACCOUNTID_LIST))) {
                accountsSet =
                        HelperMethods.splitString(
                                JSONUtil.getString(json.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().get(0)
                                        .getAsJsonObject(), PARAM_ACCOUNTID_LIST),
                                DBPUtilitiesConstants.COMMA_SEPERATOR);
            }

            if (JSONUtil.hasKey(json, DATASET_RECORDS1)
                    && json.get(DATASET_RECORDS1).getAsJsonArray().size() > 0
                    && JSONUtil.hasKey(
                            json.get(DATASET_RECORDS1).getAsJsonArray().get(0).getAsJsonObject(),
                            PARAM_EXCLUDED_ACCOUNTID_LIST)
                    && StringUtils.isNotBlank(JSONUtil.getString(
                            json.get(DATASET_RECORDS1).getAsJsonArray().get(0).getAsJsonObject(),
                            PARAM_EXCLUDED_ACCOUNTID_LIST))) {
                excludedAccountsSet =
                        HelperMethods.splitString(
                                JSONUtil.getString(json.get(DATASET_RECORDS1).getAsJsonArray().get(0)
                                        .getAsJsonObject(), PARAM_EXCLUDED_ACCOUNTID_LIST),
                                DBPUtilitiesConstants.COMMA_SEPERATOR);
            }
            for (JsonElement jsonElement : customerAccountsJsonArray) {
                String accountId = JSONUtil.getString(jsonElement.getAsJsonObject(), "accountId");
                if (accountsSet.contains(accountId)) {
                    jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_ASSOCIATED,
                            DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                    jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_SELECTED,
                            DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                } else {
                    jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_ASSOCIATED,
                            DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
                    jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_SELECTED,
                            DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
                }

                if (accountsSet.contains(accountId) || excludedAccountsSet.contains(accountId)) {
                    jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_NEW,
                            DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
                } else {
                    jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_NEW,
                            DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                }

            }
            response.setResponse(customerAccountsJsonArray);
        } catch (Exception e) {
            alert.prepareError("ContractAccountBackendDelegateImpl : Exception occured while validating the contract accounts"
                    + e.getLocalizedMessage()).log();
        }
        return response;
    }

    @Override
    public boolean checkIfGivenAccountsAreValid(Set<String> accountsList, Map<String, Object> headersMap)
            throws ApplicationException {
        Set<String> invalidAccounts = new HashSet<>();
        try {
            if (accountsList == null || accountsList.isEmpty()) {
                return true;
            }
            StringBuilder accountsListCSV = new StringBuilder();
            for (String accountId : accountsList) {
                if (StringUtils.isBlank(accountsListCSV)) {
                    accountsListCSV.append(accountId);
                } else {
                    accountsListCSV.append(DBPUtilitiesConstants.COMMA_SEPERATOR).append(accountId);
                }

            }

            Map<String, Object> inputParams = new HashMap<>();
            String invalidAccountsCSV;
            inputParams.put("_accountIdList", accountsListCSV.toString());
            JsonObject invalidAccountsListJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.GET_ASSOCIATED_CONTRACT_ACCOUNTS);
            if (null != invalidAccountsListJson
                    && JSONUtil.hasKey(invalidAccountsListJson, DBPDatasetConstants.DATASET_RECORDS)
                    && invalidAccountsListJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray array = invalidAccountsListJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (null != object && JSONUtil.hasKey(object, "accountIdList")) {
                        invalidAccountsCSV = object.get("accountIdList").getAsString();
                        invalidAccounts =
                                HelperMethods.splitString(invalidAccountsCSV, DBPUtilitiesConstants.COMMA_SEPERATOR);
                    }
                }

            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10411);
        }

        return invalidAccounts.isEmpty();
    }

}
