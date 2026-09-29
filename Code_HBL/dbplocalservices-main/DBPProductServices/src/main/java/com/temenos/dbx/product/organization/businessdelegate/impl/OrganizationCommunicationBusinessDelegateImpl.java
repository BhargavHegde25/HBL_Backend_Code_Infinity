package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.dto.OrganizationCommunicationDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationCommunicationBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class OrganizationCommunicationBusinessDelegateImpl implements OrganizationCommunicationBusinessDelegate {

    @Override
    public OrganizationCommunicationDTO createOrganizationCommunication(OrganizationCommunicationDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        OrganizationCommunicationDTO resultDTO = null;
        if (inputDTO == null || StringUtils.isBlank(inputDTO.getId())
                || StringUtils.isBlank(inputDTO.getOrganizationId())) {
            return resultDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            HelperMethods.removeNullValues(inputParams);
            JsonObject orgCommunicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATIONCOMMUNICATION_CREATE);
            if (JSONUtil.isJsonNotNull(orgCommunicationJson)
                    && JSONUtil.hasKey(orgCommunicationJson, DBPDatasetConstants.DATASET_ORGANISATIONCOMMUNICATION)
                    && orgCommunicationJson.get(DBPDatasetConstants.DATASET_ORGANISATIONCOMMUNICATION)
                            .isJsonArray()) {
                JsonArray orgCommunicationArray =
                        orgCommunicationJson.get(DBPDatasetConstants.DATASET_ORGANISATIONCOMMUNICATION)
                                .getAsJsonArray();
                JsonObject object = orgCommunicationArray.size() > 0 ? orgCommunicationArray.get(0).getAsJsonObject()
                        : new JsonObject();
                resultDTO = (OrganizationCommunicationDTO) DTOUtils.loadJsonObjectIntoObject(object,
                        OrganizationCommunicationDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10303);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10303);
        }
        return resultDTO;
    }

}
