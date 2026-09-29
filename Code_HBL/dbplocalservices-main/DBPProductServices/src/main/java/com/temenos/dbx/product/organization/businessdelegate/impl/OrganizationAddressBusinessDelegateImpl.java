package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.dto.OrganizationAddressDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationAddressBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class OrganizationAddressBusinessDelegateImpl implements OrganizationAddressBusinessDelegate {

    @Override
    public OrganizationAddressDTO createOrganizationAddress(OrganizationAddressDTO inputDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {
        OrganizationAddressDTO resultDTO = null;
        if (inputDTO == null || StringUtils.isBlank(inputDTO.getId())
                || StringUtils.isBlank(inputDTO.getOrganizationId())) {
            return resultDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            JsonObject orgAddressJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATIONADDRESS_CREATE);
            if (JSONUtil.isJsonNotNull(orgAddressJson)
                    && JSONUtil.hasKey(orgAddressJson, DBPDatasetConstants.DATASET_ORGANISATIONADDRESS)
                    && orgAddressJson.get(DBPDatasetConstants.DATASET_ORGANISATIONADDRESS).isJsonArray()) {
                JsonArray orgAddressArray =
                        orgAddressJson.get(DBPDatasetConstants.DATASET_ORGANISATIONADDRESS).getAsJsonArray();
                JsonObject object =
                        orgAddressArray.size() > 0 ? orgAddressArray.get(0).getAsJsonObject() : new JsonObject();
                resultDTO =
                        (OrganizationAddressDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                OrganizationAddressDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10301);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10301);
        }
        return resultDTO;
    }

}
