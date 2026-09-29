package com.temenos.dbx.product.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.businessdelegate.api.OrganizationEmployeesBusinessDelegate;
import com.temenos.dbx.product.dto.OrganisationEmployeesViewDTO;
import com.temenos.dbx.product.dto.OrganizationEmployeesDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class OrganizationEmployeesBusinessDelegateImpl implements OrganizationEmployeesBusinessDelegate {

    @Override
    public JsonObject getOrgEmployeeList(String orgId, String filterColumnName, String filterValue,
            String urlToFetchList, Map<String, ? extends Object> headerMap) {
        if (StringUtils.isBlank(orgId) || StringUtils.isBlank(filterColumnName) || StringUtils.isBlank(filterValue)
                || StringUtils.isBlank(urlToFetchList)) {
            JsonObject result = new JsonObject();
            ErrorCodeEnum.ERR_10219.setErrorCode(result);
            return result;

        }
        return getList(orgId, filterColumnName, filterValue, urlToFetchList, headerMap);
    }

    private JsonObject getList(String orgId, String filterColumnName, String filterValue, String urlToFetchList,
            Map<String, ? extends Object> headerMap) {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("_organisationId", orgId);
        inputMap.put("_filterColumnName", filterColumnName);
        inputMap.put("_filterColumnValue", filterValue);

        return ServiceCallHelper.invokeServiceAndGetJson(inputMap, (Map<String, Object>) headerMap, urlToFetchList);
    }

    @Override
    public OrganizationEmployeesDTO createOrganizationEmployee(OrganizationEmployeesDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        OrganizationEmployeesDTO resultDTO = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getId())) {
            return resultDTO;
        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            HelperMethods.removeNullValues(inputParams);
            JsonObject orgEmployeeJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATIONEMPLOYEE_CREATE);
            if (JSONUtil.isJsonNotNull(orgEmployeeJson)
                    && JSONUtil.hasKey(orgEmployeeJson, DBPDatasetConstants.DATASET_ORGANISATIONEMPLOYEES)
                    && orgEmployeeJson.get(DBPDatasetConstants.DATASET_ORGANISATIONEMPLOYEES).isJsonArray()) {
                JsonArray orgEmployeeArray = orgEmployeeJson.get(DBPDatasetConstants.DATASET_ORGANISATIONEMPLOYEES)
                        .getAsJsonArray();
                JsonObject object = orgEmployeeArray.size() > 0 ? orgEmployeeArray.get(0).getAsJsonObject()
                        : new JsonObject();
                resultDTO = (OrganizationEmployeesDTO) DTOUtils.loadJsonObjectIntoObject(object,
                        OrganizationEmployeesDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10317);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10317);
        }

        return resultDTO;
    }

    @Override
    public List<OrganisationEmployeesViewDTO> getOrganizationEmployees(OrganisationEmployeesViewDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        List<OrganisationEmployeesViewDTO> responseDTO = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        boolean isAndAppended = false;
        if (StringUtils.isNoneBlank(inputDTO.getOrgemp_orgid())) {
            sb.append("orgemp_orgid").append(DBPUtilitiesConstants.EQUAL).append(inputDTO.getOrgemp_orgid());
            isAndAppended = true;
        }
        if (StringUtils.isNoneBlank(inputDTO.getCustcomm_typeid())) {
            if (isAndAppended)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("custcomm_typeid").append(DBPUtilitiesConstants.EQUAL).append(inputDTO.getCustcomm_typeid());
            isAndAppended = true;
        }
        if (StringUtils.isNoneBlank(sb.toString())) {
            inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        }
        JsonObject response = new JsonObject();
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATIONEMPLOYEEVIEW_GET);
            if (JSONUtil.isJsonNotNull(response)
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_ORGANISATIONEMPLOYEEVIEW)
                    && response.get(DBPDatasetConstants.DATASET_ORGANISATIONEMPLOYEEVIEW).isJsonArray()) {
                JsonArray array = response.get(DBPDatasetConstants.DATASET_ORGANISATIONEMPLOYEEVIEW)
                        .getAsJsonArray();
                for (JsonElement accountsJson : array) {
                    if (accountsJson != null && accountsJson.isJsonObject()) {
                        responseDTO.add((OrganisationEmployeesViewDTO) DTOUtils.loadJsonObjectIntoObject(
                                accountsJson.getAsJsonObject(), OrganisationEmployeesViewDTO.class, true));
                    }
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10322);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10322);
        }
        return responseDTO;
    }

}
