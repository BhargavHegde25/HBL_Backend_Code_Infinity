package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.OrganizationEmployeesDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CredentialCheckerBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class CredentialCheckerBusinessDelegateImpl implements CredentialCheckerBusinessDelegate {

    @Override
    public DBXResult update(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        if (credentialCheckerDTO.persist(DTOUtils.getParameterMap(credentialCheckerDTO, true), headerMap)) {
            dbxResult.setResponse(credentialCheckerDTO.getId());
        } else {
            dbxResult.setDbpErrMsg("Customer update Failed");
        }
        return dbxResult;
    }

    @Override
    public DBXResult get(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        dbxResult.setResponse(credentialCheckerDTO.loadDTO());
        return dbxResult;
    }

    @Override
    public boolean delete(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", credentialCheckerDTO.getId());
        JsonObject response = new JsonObject();
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
                    URLConstants.CREDENTIAL_CHECKER_DELETE);
            if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, "deletedRecords")
                    && response.get("deletedRecords").getAsInt() == 1) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public CredentialCheckerDTO create(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap)
            throws ApplicationException {

        CredentialCheckerDTO responseDTO = new CredentialCheckerDTO();
        Map<String, Object> map = new HashMap<>();
        map.put("id", credentialCheckerDTO.getId());
        map.put("UserName", credentialCheckerDTO.getUserName());
        map.put("linktype", credentialCheckerDTO.getLinktype());
        map.put("createdts", credentialCheckerDTO.getCreatedts());
        map.put("retryCount", credentialCheckerDTO.getRetryCount());
        JsonObject response = new JsonObject();
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
                    URLConstants.CREDENTIAL_CHECKER_CREATE);
            if (JSONUtil.isJsonNotNull(response)
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CREDENTIALCHECKER)
                    && response.get(DBPDatasetConstants.DATASET_CREDENTIALCHECKER).isJsonArray()) {
                JsonArray credentialCehckJsonArray = response.get(DBPDatasetConstants.DATASET_CREDENTIALCHECKER)
                        .getAsJsonArray();
                JsonObject object = credentialCehckJsonArray.size() > 0
                        ? credentialCehckJsonArray.get(0).getAsJsonObject()
                        : new JsonObject();
                responseDTO = (CredentialCheckerDTO) DTOUtils.loadJsonObjectIntoObject(object,
                        CredentialCheckerDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10317);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10317);
        }
        return responseDTO;
    }
}
