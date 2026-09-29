package com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;

public class BackendIdentifiersBackendDelegateimpl implements BackendIdentifiersBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DBXResult getList(BackendIdentifierDTO backendIdentifierDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        String filter = "";

        if (StringUtils.isNotBlank((backendIdentifierDTO.getCustomer_id()))) {
            filter += "Customer_id" + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getCustomer_id();
        }

        if (StringUtils.isNotBlank((backendIdentifierDTO.getBackendType()))) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getBackendType();
        }

        if (StringUtils.isNotBlank((backendIdentifierDTO.getBackendId()))) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += DTOConstants.BACKENDID + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getBackendId();
        }

        if (StringUtils.isBlank(filter)) {
            return dbxResult;
        }

        Map<String, Object> inputParams = new HashMap<String, Object>();

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        String authToken = (String) headerMap.get(MWConstants.X_KONY_AUTHORIZATION_HEADER);
        List<BackendIdentifierDTO> identifierDTOs = new ArrayList<BackendIdentifierDTO>();
        try {
            JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.BACKENDIDENTIFIER_GET, authToken);

            if (result.has("backendidentifier") && !result.get("backendidentifier").isJsonNull()
                    && result.get("backendidentifier").isJsonArray()
                    && result.get("backendidentifier").getAsJsonArray().size() > 0) {
                JsonArray jsonArray = result.get("backendidentifier").getAsJsonArray();

                for (JsonElement json : result.get("backendidentifier").getAsJsonArray()) {
                    if (json != null) {
                        identifierDTOs
                                .add((BackendIdentifierDTO) DTOUtils.loadJsonObjectIntoObject(json.getAsJsonObject(),
                                        BackendIdentifierDTO.class, true));
                    }
                }

            }
            dbxResult.setResponse(identifierDTOs);
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting PartyId: ", e).log();
        }

        return dbxResult;
    }

    @Override
    public DBXResult get(BackendIdentifierDTO backendIdentifierDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
       
        String filter = "";
        
        if (StringUtils.isNotBlank((backendIdentifierDTO.getCustomer_id()))) {
            filter += "Customer_id" + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getCustomer_id();
        }

        if (StringUtils.isNotBlank((backendIdentifierDTO.getBackendType()))) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getBackendType();
        }
       
        if (StringUtils.isNotBlank((backendIdentifierDTO.getBackendId()))) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += DTOConstants.BACKENDID + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getBackendId();
        }
        
        if (StringUtils.isNotBlank((backendIdentifierDTO.getCompanyLegalUnit()))) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += DTOConstants.LEGALENTITYID + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getCompanyLegalUnit();
        }
        if (StringUtils.isNotBlank((backendIdentifierDTO.getContractId()))) {
            if (StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += "contractId" + DBPUtilitiesConstants.EQUAL + backendIdentifierDTO.getContractId();
        }
         
        if (StringUtils.isBlank(filter)) {
            return dbxResult;
        }
        
        
        Map<String, Object> inputParams = new HashMap<String, Object>();

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        String authToken = (String) headerMap.get(MWConstants.X_KONY_AUTHORIZATION_HEADER);

        Map<String, String> headers = new HashMap<String, String>();

        for (Entry<String, Object> entry : headerMap.entrySet()) {
            headers.put(entry.getKey(), (String) entry.getValue());
        }

        try {
            JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.BACKENDIDENTIFIER_GET, authToken);

            if (result.has("backendidentifier") && !result.get("backendidentifier").isJsonNull()
                    && result.get("backendidentifier").isJsonArray()
                    && result.get("backendidentifier").getAsJsonArray().size() > 0) {
                JsonArray jsonArray = result.get("backendidentifier").getAsJsonArray();

                JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

                BackendIdentifierDTO identifierDTO = new BackendIdentifierDTO();
                DTOUtils.loadDTOFromJson(identifierDTO, jsonObject, true);

                dbxResult.setResponse(identifierDTO);
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting PartyId: ", e).log();
        }

        return dbxResult;
    }

    @Override
    public List<BackendIdentifierDTO> getBackendIdentifierList(List<BackendIdentifierDTO> dtoList,
            Map<String, Object> headerMap) throws ApplicationException {
        List<BackendIdentifierDTO> responseDTOlist = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (dtoList != null && dtoList.size() > 0) {
            for (BackendIdentifierDTO dto : dtoList) {
                if (sb != null && StringUtils.isNotBlank(sb.toString()))
                    sb.append(DBPUtilitiesConstants.OR);
                if (StringUtils.isNotBlank(dto.getBackendId()))
                    sb.append(DTOConstants.BACKENDID).append(DBPUtilitiesConstants.EQUAL).append(dto.getBackendId());
                if (StringUtils.isNotBlank(dto.getCompanyLegalUnit())) {
                	sb.append(DBPUtilitiesConstants.AND);
                    sb.append(DTOConstants.LEGALENTITYID).append(DBPUtilitiesConstants.EQUAL).append(dto.getCompanyLegalUnit());
                }
            }
        }
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        try {
            JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.BACKENDIDENTIFIER_GET,
                    (String) headerMap.get(MWConstants.X_KONY_AUTHORIZATION_HEADER));

            if (result.has(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER)
                    && !result.get(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER).isJsonNull()
                    && result.get(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER).isJsonArray()
                    && result.get(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER).getAsJsonArray().size() > 0) {
                for (JsonElement jsonelement : result.get(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER)
                        .getAsJsonArray()) {
                    if (jsonelement != null) {
                        responseDTOlist
                                .add((BackendIdentifierDTO) DTOUtils.loadJsonObjectIntoObject(
                                        jsonelement.getAsJsonObject(),
                                        BackendIdentifierDTO.class, true));
                    }
                    diagnostic.prepareDebug("shre9"+responseDTOlist.toString()).log();
                }
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting PartyId: ", e).log();
        }
        return responseDTOlist;
    }

    @Override
    public BackendIdentifierDTO createBackendIdentifier(BackendIdentifierDTO backendIdentifierDTO,
            Map<String, Object> headerMap) throws ApplicationException {
        BackendIdentifierDTO resultbackendIdentifierDTO = null;
        if (null == backendIdentifierDTO || StringUtils.isBlank(backendIdentifierDTO.getBackendId())
                || StringUtils.isBlank(backendIdentifierDTO.getId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10396);
        }

        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(backendIdentifierDTO, true);
            HelperMethods.removeNullValues(inputParams);
            JsonObject BackendIdentifierJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.BACKENDIDENTIFIER_CREATE);
            if (JSONUtil.isJsonNotNull(BackendIdentifierJson)
                    && JSONUtil.hasKey(BackendIdentifierJson, DBPDatasetConstants.DATASET_BACKENDIDENTIFIER)
                    && BackendIdentifierJson.get(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER).isJsonArray()) {
                JsonArray backendArray = BackendIdentifierJson.get(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER)
                        .getAsJsonArray();
                JsonObject object = backendArray.size() > 0 ? backendArray.get(0).getAsJsonObject()
                        : new JsonObject();
                resultbackendIdentifierDTO =
                        (BackendIdentifierDTO) DTOUtils.loadJsonObjectIntoObject(object, BackendIdentifierDTO.class,
                                true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10397);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10397);
        }
        return resultbackendIdentifierDTO;
    }
    
    @Override
    public String getBackendType(BackendIdentifierDTO backendIdentifierDTO,Map<String, Object> headerMap) throws ApplicationException{

       String backednType= backendIdentifierDTO.getBackendType().toUpperCase();
       String companyLegalId = backendIdentifierDTO.getCompanyLegalUnit();
        
       String filter =  "BackendType eq "+ backednType + " and companyLegalUnit eq "+ companyLegalId;
     
       Map<String, Object> inputParams = new HashMap<String, Object>();

       inputParams.put(DBPUtilitiesConstants.FILTER, filter);
       String authToken = (String) headerMap.get(MWConstants.X_KONY_AUTHORIZATION_HEADER);
       Map<String, String> headers = new HashMap<String, String>();

       for (Entry<String, Object> entry : headerMap.entrySet()) {
            headers.put(entry.getKey(), (String) entry.getValue());
        }

       try {
            JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.BACKENDIDENTIFIER_GET, authToken);

           if (result.has("backendidentifier") && !result.get("backendidentifier").isJsonNull()
                    && result.get("backendidentifier").isJsonArray()
                    && result.get("backendidentifier").getAsJsonArray().size() > 0) {
                JsonArray jsonArray = result.get("backendidentifier").getAsJsonArray();
               JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

               backednType = JSONUtil.getString(jsonObject.getAsJsonObject(), "BackendType");
       
            }

       } catch (Exception e) {
            alert.prepareError("Caught exception while getting BackendType: ", e).log();
        }

       return backednType;
    }
}