package com.temenos.dbx.product.businessdelegate.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.AddressBackendDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class AddressBusinessDelegateImpl implements AddressBusinessDelegate {

    @Override
    public AddressDTO getAddressDetails(String addressId, Map<String, Object> headerMap)
            throws ApplicationException {

        AddressBackendDelegate addressBD =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(AddressBackendDelegate.class);
        return addressBD.getAddressDetails(addressId, headerMap);
    }

    @Override
    public AddressDTO createAddress(AddressDTO inputDTO, Map<String, Object> headerMap) throws ApplicationException {
        AddressDTO resultDTO = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getId())) {
            return resultDTO;
        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
            JsonObject addressJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.ADDRESS_CREATE);
            if (JSONUtil.isJsonNotNull(addressJson)
                    && JSONUtil.hasKey(addressJson, DBPDatasetConstants.DATASET_ADDRESS)
                    && addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).isJsonArray()) {
                JsonArray addressArray =
                        addressJson.get(DBPDatasetConstants.DATASET_ADDRESS).getAsJsonArray();
                JsonObject object = addressArray.size() > 0 ? addressArray.get(0).getAsJsonObject() : new JsonObject();
                resultDTO =
                        (AddressDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                AddressDTO.class, true);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10299);
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10299);
        }
        return resultDTO;
    }

}
