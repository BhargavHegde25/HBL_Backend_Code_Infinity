package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerBusinessTypeBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerBusinessTypeDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerBusinessTypeBusinessDelegateImpl implements CustomerBusinessTypeBusinessDelegate {

    @Override
    public CustomerBusinessTypeDTO createCustomerBusinessType(CustomerBusinessTypeDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        CustomerBusinessTypeDTO resultDTO = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getCustomerId())) {
            return resultDTO;
        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            HelperMethods.removeNullValues(inputParams);
            JsonObject customerBusinessTypeBDJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMERBUSINESSTYPE_CREATE);
            if (JSONUtil.isJsonNotNull(customerBusinessTypeBDJson)
                    && JSONUtil.hasKey(customerBusinessTypeBDJson, DBPDatasetConstants.DATASET_CUSTOMERBUSINESSTYPE)
                    && customerBusinessTypeBDJson.get(DBPDatasetConstants.DATASET_CUSTOMERBUSINESSTYPE)
                            .isJsonArray()) {
                JsonArray customerBusinessTypeArray =
                        customerBusinessTypeBDJson.get(DBPDatasetConstants.DATASET_CUSTOMERBUSINESSTYPE)
                                .getAsJsonArray();
                JsonObject object =
                        customerBusinessTypeArray.size() > 0 ? customerBusinessTypeArray.get(0).getAsJsonObject()
                                : new JsonObject();
                resultDTO = (CustomerBusinessTypeDTO) DTOUtils.loadJsonObjectIntoObject(object,
                        CustomerBusinessTypeDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10329);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10329);
        }

        return resultDTO;
    }

}
