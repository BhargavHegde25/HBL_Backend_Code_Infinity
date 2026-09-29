package com.temenos.dbx.product.accounts.backenddelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountsBackendDelegate;
import com.temenos.dbx.product.dto.AccountTypeDTO;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class AccountsBackendDelegateImpl implements AccountsBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<AccountsDTO> checkIfAccountAvailable(AccountsDTO accountDTO, Map<String, Object> headersMap)
            throws ApplicationException {

        List<AccountsDTO> accountsDTO = new ArrayList<>();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.FILTER,
                "Account_id" + DBPUtilitiesConstants.EQUAL + accountDTO.getAccountId());
        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, URLConstants.ACCOUNT_GET);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the accounts information in backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10239);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("accounts")) {
            alert.prepareError("Exception occured while fetching the account information").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10239);
        }

        if (response.get("accounts").getAsJsonArray().size() == 0) {
            return accountsDTO;
        }
        try {
            for (JsonElement accountsJson : response.get("accounts").getAsJsonArray()) {
                if (accountsJson != null) {
                    accountsDTO.add((AccountsDTO) DTOUtils.loadJsonObjectIntoObject(accountsJson.getAsJsonObject(),
                            AccountsDTO.class, true));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing the account information " + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10246);

        }
        return accountsDTO;
    }

    @Override
    public List<AccountTypeDTO> getAccountTypes(Map<String, Object> headersMap) throws ApplicationException {
        List<AccountTypeDTO> accountTypesDTOList = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ACCOUNT_TYPE_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the account types in backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10253);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("accounttype")) {
            alert.prepareError("Exception occured while fetching the accountypes in backend delegate").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10253);
        }

        if (response.get("accounttype").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10254);
        }
        try {
            for (JsonElement accountsJson : response.get("accounttype").getAsJsonArray()) {
                if (accountsJson != null) {
                    accountTypesDTOList.add((AccountTypeDTO) DTOUtils
                            .loadJsonObjectIntoObject(accountsJson.getAsJsonObject(), AccountTypeDTO.class, false));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing the account types " + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10254);

        }
        return accountTypesDTOList;
    }

    @Override
    public AccountsDTO getAccountDetailsByAccountID(String accountId, Map<String, Object> headersMap)
            throws ApplicationException {
        AccountsDTO dto = null;
        if (StringUtils.isBlank(accountId)) {
            return dto;
        }
        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ACCOUNT_GET);
            if (JSONUtil.isJsonNotNull(accountJson)
                    && JSONUtil.hasKey(accountJson, DBPDatasetConstants.DATASET_ACCOUNTS)
                    && accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS).isJsonArray()) {
                JsonArray accountArray = accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS).getAsJsonArray();
                JsonObject object = accountArray.size() > 0 ? accountArray.get(0).getAsJsonObject() : new JsonObject();
                dto = (AccountsDTO) DTOUtils.loadJsonObjectIntoObject(object, AccountsDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10293);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10293);
        }

        return dto;
    }

    @Override
    public AccountsDTO createAccount(AccountsDTO inputDTO, Map<String, Object> headersMap) throws ApplicationException {
        AccountsDTO resultDTO = null;
        if (StringUtils.isBlank(inputDTO.getAccountId())) {
            return resultDTO;
        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            JsonObject accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ACCOUNTS_CREATE);
            if (JSONUtil.isJsonNotNull(accountJson)
                    && JSONUtil.hasKey(accountJson, DBPDatasetConstants.DATASET_ACCOUNTS)
                    && accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS).isJsonArray()) {
                JsonArray accountArray = accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS).getAsJsonArray();
                JsonObject object = accountArray.size() > 0 ? accountArray.get(0).getAsJsonObject() : new JsonObject();
                resultDTO = (AccountsDTO) DTOUtils.loadJsonObjectIntoObject(object, AccountsDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10294);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10294);
        }

        return resultDTO;
    }

    @Override
    public AccountsDTO updateAccount(AccountsDTO inputDTO, Map<String, Object> headersMap) throws ApplicationException {
        AccountsDTO resultDTO = null;
        if (StringUtils.isBlank(inputDTO.getAccountId())) {
            return resultDTO;
        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            JsonObject accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ACCOUNTS_UPDATE);
            if (JSONUtil.isJsonNotNull(accountJson)
                    && JSONUtil.hasKey(accountJson, DBPDatasetConstants.DATASET_ACCOUNTS)
                    && accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS).isJsonArray()) {
                JsonArray accountArray = accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS).getAsJsonArray();
                JsonObject object = accountArray.size() > 0 ? accountArray.get(0).getAsJsonObject() : new JsonObject();
                resultDTO = (AccountsDTO) DTOUtils.loadJsonObjectIntoObject(object, AccountsDTO.class, true);
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10295);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10295);
        }

        return resultDTO;
    }

    @Override
    public List<AccountsDTO> getOrganizationAccounts(AccountsDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        List<AccountsDTO> organizationAccounts = new ArrayList<>();
        if (StringUtils.isBlank(inputDTO.getOrganizationId())) {
            return organizationAccounts;
        }
        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + inputDTO.getOrganizationId();
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ACCOUNT_GET);
            if (JSONUtil.isJsonNotNull(accountJson)
                    && JSONUtil.hasKey(accountJson, DBPDatasetConstants.DATASET_ACCOUNTS)
                    && accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS).isJsonArray()) {
                for (JsonElement accountsJson : accountJson.get(DBPDatasetConstants.DATASET_ACCOUNTS)
                        .getAsJsonArray()) {
                    if (accountsJson != null) {
                        organizationAccounts.add((AccountsDTO) DTOUtils
                                .loadJsonObjectIntoObject(accountsJson.getAsJsonObject(), AccountsDTO.class, true));
                    }
                }

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10305);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10305);
        }

        return organizationAccounts;
    }

    @Override
    public Map<String, String> processNewAccounts(List<AllAccountsViewDTO> allAccounts, String customerId,
            Map<String, Object> headersMap) throws ApplicationException {

        JsonArray accountsArray = new JsonArray();
        Set<String> resultSet = new HashSet<String>();
        JsonObject object = null;
        for (AllAccountsViewDTO dto : allAccounts) {
            String accountId = dto.getAccountId();
            String coreCustomerId = dto.getMembershipId();
            String accountType = dto.getAccountType();
            String accountName = dto.getAccountName();
            String arrangementId = dto.getArrangementId();
            object = new JsonObject();
            object.addProperty("accountId", accountId);
            object.addProperty("customerId", coreCustomerId);
            object.addProperty("accountType", accountType);
            object.addProperty("accountName", accountName);
            object.addProperty("arrangementId", arrangementId);
            object.addProperty("roleDisplayName", dto.getOwnership());
            accountsArray.add(object);

        }
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("accounts", accountsArray.toString());
        inputParams.put("customerId", customerId);
        JsonObject accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.NEWACCOUNTS_PROCESSING_INTEGRATION_SERVICE);

        if (JSONUtil.hasKey(accountJson, "accounts") && JSONUtil.hasKey(accountJson, "newAccounts")) {
            String accounts = JSONUtil.getString(accountJson, "accounts");
            resultSet = new HashSet<String>(Arrays.asList(accounts.split(" ")));
        }
        String newAccounts = JSONUtil.getString(accountJson, "newAccounts");
        Map<String, String> newAcntCoreCustomerId = new HashMap<>();
        if (StringUtils.isNotBlank(newAccounts)) {
            String[] accountsInfo = newAccounts.split("\\|");
            for (int i = 0; i < accountsInfo.length; i++) {
                String[] singleAccountInfo = accountsInfo[i].split(":");
                newAcntCoreCustomerId.put(singleAccountInfo[1], singleAccountInfo[0]);
            }
        }

        Map<String, String> accountIdIsNewMap = new HashMap<>();
        for (String s : resultSet) {
            if (newAcntCoreCustomerId.containsKey(s))
                accountIdIsNewMap.put(s, "1");
            else
                accountIdIsNewMap.put(s, "0");
        }
        return accountIdIsNewMap;
    }
}
