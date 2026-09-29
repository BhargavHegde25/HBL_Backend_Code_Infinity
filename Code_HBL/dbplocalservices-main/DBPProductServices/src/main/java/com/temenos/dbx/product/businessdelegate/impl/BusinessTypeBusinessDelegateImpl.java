package com.temenos.dbx.product.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.businessdelegate.api.BusinessTypeBusinessDelegate;
import com.temenos.dbx.product.dto.BusinessTypeDTO;
import com.temenos.dbx.product.dto.BusinessTypeRoleDTO;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 *
 */
public class BusinessTypeBusinessDelegateImpl implements BusinessTypeBusinessDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<BusinessTypeDTO> getBusinessType(Map<String, Object> headersMap) throws ApplicationException {

        List<BusinessTypeDTO> businessTypeDTOList = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.SELECT, "id,name,minAuthSignatory,maxAuthSignatory");
        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.BUSINESSTYPE_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching business types :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10228);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("businesstype")) {
            alert.prepareError("Exception occured while fetching business types :").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10229);
        }

        if (response.get("businesstype").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10252);
        }
        try {
            List<BusinessTypeDTO> businessTypes =
                    JSONUtils.parseAsList(response.get("businesstype").getAsJsonArray().toString(),
                            BusinessTypeDTO.class);

            for (BusinessTypeDTO dto : businessTypes) {
                String filterQuery = "BusinessType_id" + DBPUtilitiesConstants.EQUAL + dto.getId()
                        + DBPUtilitiesConstants.AND + "isDefaultGroup" + DBPUtilitiesConstants.EQUAL + "true";
                inputParams.clear();
                inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
                response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.GROUPBUSINESSTYPE_GET);
                if (JSONUtil.hasKey(response, "groupbusinesstype")
                        && response.get("groupbusinesstype").getAsJsonArray().size() > 0) {
                    businessTypeDTOList.add(dto);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10228);
        }
        return businessTypeDTOList;
    }

    @Override
    public List<BusinessTypeRoleDTO> getBusinessTypeRoles(BusinessTypeRoleDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {

        List<BusinessTypeRoleDTO> businessTypeRoleDTO = new ArrayList<>();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_businessTypeId", inputDTO.getBusinessTypeId());

        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.BUSINESSTYPEROLE_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching business types :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10229);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("records")) {
            alert.prepareError("Exception occured while fetching the business type role information").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10229);
        }

        if (response.get("records").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10251);
        }

        try {
            businessTypeRoleDTO = JSONUtils.parseAsList(response.get("records").getAsJsonArray().toString(),
                    BusinessTypeRoleDTO.class);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10229);
        }

        return businessTypeRoleDTO;
    }

}
