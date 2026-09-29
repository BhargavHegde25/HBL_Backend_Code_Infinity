package com.temenos.dbx.product.accounts.backenddelegate.impl;

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
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountInformationBackendDelegate;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class AccountInformationBackendDelegateImpl implements AccountInformationBackendDelegate {

    @Override
    public List<AllAccountsViewDTO> getAllAccountsInformation(AllAccountsViewDTO accountDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        String membershipId = accountDTO.getMembershipId();
        String accountId = accountDTO.getAccountId();
        String taxID = accountDTO.getTaxId();
        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        JsonArray array = new JsonArray();
        List<AllAccountsViewDTO> dtoList = new ArrayList<>();
        try {
            if (StringUtils.isBlank(membershipId) && StringUtils.isBlank(accountId) && StringUtils.isBlank(taxID)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10248);
            }
            Map<String, Object> inputParams = new HashMap<>();
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                final String RESULT_DATASET = "Accounts";
                if (StringUtils.isNotBlank(membershipId)) {
                    inputParams.put("Membership_id", membershipId);
                } else if (StringUtils.isNotBlank(accountId)) {
                    inputParams.put("Account_id", accountId);
                } else {
                    inputParams.put("Taxid", taxID);
                }

                String serviceId = ServiceId.COMMON_ORCHESTRATION_SERVICE;
                String operationName = OperationName.GET_ALL_ACCOUNTS;
                headersMap.put("companyId",
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                JsonObject accountsJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                        inputParams, headersMap);
                if (null != accountsJson
                        && JSONUtil.hasKey(accountsJson, RESULT_DATASET)
                        && accountsJson.get(RESULT_DATASET).isJsonArray()) {
                    array = accountsJson.get(RESULT_DATASET).getAsJsonArray();
                }
            } else {
                String filter = null;
                if (StringUtils.isNotBlank(membershipId)) {
                    filter = "Membership_id" + DBPUtilitiesConstants.EQUAL + membershipId;
                } else if (StringUtils.isNotBlank(accountId)) {
                    filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
                } else {
                    filter = "Taxid" + DBPUtilitiesConstants.EQUAL + taxID;
                }
                inputParams.put(DBPUtilitiesConstants.FILTER, filter);

                JsonObject membershipAccountsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.ALLACCOUNTSVIEW_GET);
                if (null != membershipAccountsJson
                        && JSONUtil.hasKey(membershipAccountsJson, DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                        && membershipAccountsJson.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW).isJsonArray()) {
                    array = membershipAccountsJson.get(DBPDatasetConstants.DATASET_ALLACCOUNTSVIEW)
                            .getAsJsonArray();
                }
            }
            Set<String> accounts = new HashSet<>();
            for (JsonElement element : array) {
                if (element.isJsonObject()) {
                    AllAccountsViewDTO dto = (AllAccountsViewDTO) DTOUtils
                            .loadJsonObjectIntoObject(element.getAsJsonObject(), AllAccountsViewDTO.class, true);
                    if (null != dto && !accounts.contains(dto.getAccountId())) {
                        dtoList.add(dto);
                        accounts.add(dto.getAccountId());
                    }

                }

            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);

        }
        return dtoList;
    }
}
