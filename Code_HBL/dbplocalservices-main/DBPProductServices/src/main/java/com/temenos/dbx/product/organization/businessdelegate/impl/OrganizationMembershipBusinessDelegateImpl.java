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
import com.temenos.dbx.product.dto.OrganizationMembershipDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationMembershipBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class OrganizationMembershipBusinessDelegateImpl implements OrganizationMembershipBusinessDelegate {

    @Override
    public OrganizationMembershipDTO createOrganizationMembership(OrganizationMembershipDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        OrganizationMembershipDTO dto = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getTaxId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10285);
        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);

            HelperMethods.removeNullValues(inputParams);
            JsonObject organisationMembershipJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATIONMEMBERSHIP_CREATE);
            if (JSONUtil.isJsonNotNull(organisationMembershipJson)
                    && JSONUtil.hasKey(organisationMembershipJson, DBPDatasetConstants.DATASET_ORGANISATIONMEMBERSHIP)
                    && organisationMembershipJson.get(DBPDatasetConstants.DATASET_ORGANISATIONMEMBERSHIP)
                            .isJsonArray()) {
                JsonArray organisationArray =
                        organisationMembershipJson.get(DBPDatasetConstants.DATASET_ORGANISATIONMEMBERSHIP)
                                .getAsJsonArray();
                JsonObject object =
                        organisationArray.size() > 0 ? organisationArray.get(0).getAsJsonObject() : new JsonObject();
                dto = (OrganizationMembershipDTO) DTOUtils
                        .loadJsonObjectIntoObject(object, OrganizationMembershipDTO.class, true);
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10286);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10286);
        }
        return dto;
    }

}
