package com.temenos.dbx.product.contract.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

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
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.contract.backenddelegate.api.ServiceDefinitionBackendDelegate;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ServiceDefinitionBackendDelegateImpl implements ServiceDefinitionBackendDelegate {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Override
    public ServiceDefinitionDTO getServiceDefinitionDetails(ServiceDefinitionDTO serviceDefinitionDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        if (serviceDefinitionDTO == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10376);
        }
        String filter = "";
        Map<String, Object> inputParams = new HashMap<>();
        ServiceDefinitionDTO dto;
        if (StringUtils.isNotBlank(serviceDefinitionDTO.getId())) {
            filter = ID + DBPUtilitiesConstants.EQUAL + serviceDefinitionDTO.getId();
        } else if (StringUtils.isNotBlank(serviceDefinitionDTO.getName())) {
            filter = NAME + DBPUtilitiesConstants.EQUAL + serviceDefinitionDTO.getName();
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject serviceDefinitionJSON = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.SERVICEDEFINITION_GET);
        if (null != serviceDefinitionJSON
                && JSONUtil.hasKey(serviceDefinitionJSON, DBPDatasetConstants.DATASET_SERVICEDEFINITION)
                && serviceDefinitionJSON.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).isJsonArray()) {
            JsonArray array = serviceDefinitionJSON.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).getAsJsonArray();
            JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
            dto = (ServiceDefinitionDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                    ServiceDefinitionDTO.class, false);

        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_10376);
        }
        return dto;
    }

    public String fetchDefaultRoleId(ServiceDefinitionDTO serviceDefinitionDTO, Map<String, Object> headersMap) {
        String filter = "serviceDefinitionId" + DBPUtilitiesConstants.EQUAL + serviceDefinitionDTO.getId()
                + DBPUtilitiesConstants.AND +
                "isDefaultGroup" + DBPUtilitiesConstants.EQUAL + '1';
        Map<String, Object> input = new HashMap<>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, headersMap,
                URLConstants.GROUP_SERVICEDEFINITION);
        if (jsonResponse.has(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION)
                && jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION).isJsonArray()
                && jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION).getAsJsonArray().size() > 0) {
            JsonObject groupServiceDefinition =
                    jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION).getAsJsonArray()
                            .get(0)
                            .getAsJsonObject();
            return JSONUtil.getString(groupServiceDefinition, InfinityConstants.Group_id);
        }
        return "";
    }

}
