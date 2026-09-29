package com.temenos.dbx.product.contract.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.contract.backenddelegate.api.ExcludedContractAccountsBackendDelegate;
import com.temenos.dbx.product.dto.ExcludedContractAccountDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ExcludedContractAccountsBackendDelegateImpl implements ExcludedContractAccountsBackendDelegate {

    @Override
    public ExcludedContractAccountDTO createExcludedContractAccount(ExcludedContractAccountDTO inputAccountDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ExcludedContractAccountDTO resultAccountDTO = null;
        if (inputAccountDTO == null || StringUtils.isBlank(inputAccountDTO.getId())
                || StringUtils.isBlank(inputAccountDTO.getContractId())) {
            return resultAccountDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputAccountDTO, false);
            JsonObject contractAccountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.EXCLUDED_CONTRACTACCOUNT_CREATE);
            if (JSONUtil.isJsonNotNull(contractAccountJson)
                    && JSONUtil.hasKey(contractAccountJson, DBPDatasetConstants.DATASET_EXCLUDEDCONTRACTACCOUNTS)
                    && contractAccountJson.get(DBPDatasetConstants.DATASET_EXCLUDEDCONTRACTACCOUNTS).isJsonArray()) {
                JsonArray contractAccountArray =
                        contractAccountJson.get(DBPDatasetConstants.DATASET_EXCLUDEDCONTRACTACCOUNTS).getAsJsonArray();
                JsonObject object =
                        contractAccountArray.size() > 0 ? contractAccountArray.get(0).getAsJsonObject()
                                : new JsonObject();
                resultAccountDTO =
                        (ExcludedContractAccountDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                ExcludedContractAccountDTO.class, false);
                if (resultAccountDTO == null || StringUtils.isBlank(resultAccountDTO.getId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10413);
                }

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10413);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10413);
        }
        return resultAccountDTO;
    }

    @Override
    public ExcludedContractAccountDTO getExcludedContractAccount(ExcludedContractAccountDTO inputAccountDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ExcludedContractAccountDTO resultAccountDTO = null;
        if (inputAccountDTO == null || StringUtils.isBlank(inputAccountDTO.getAccountId())) {
            return resultAccountDTO;

        }
        try {
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put(DBPUtilitiesConstants.FILTER,
                    InfinityConstants.accountId + DBPUtilitiesConstants.EQUAL + inputAccountDTO.getAccountId());
            JsonObject contractAccountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.EXCLUDED_CONTRACTACCOUNT_GET);
            if (JSONUtil.isJsonNotNull(contractAccountJson)
                    && JSONUtil.hasKey(contractAccountJson, DBPDatasetConstants.DATASET_EXCLUDEDCONTRACTACCOUNTS)
                    && contractAccountJson.get(DBPDatasetConstants.DATASET_EXCLUDEDCONTRACTACCOUNTS).isJsonArray()) {
                JsonArray contractAccountArray =
                        contractAccountJson.get(DBPDatasetConstants.DATASET_EXCLUDEDCONTRACTACCOUNTS).getAsJsonArray();
                JsonObject object =
                        contractAccountArray.size() > 0 ? contractAccountArray.get(0).getAsJsonObject()
                                : new JsonObject();
                resultAccountDTO =
                        (ExcludedContractAccountDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                ExcludedContractAccountDTO.class, false);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10414);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10414);
        }
        return resultAccountDTO;
    }

}
