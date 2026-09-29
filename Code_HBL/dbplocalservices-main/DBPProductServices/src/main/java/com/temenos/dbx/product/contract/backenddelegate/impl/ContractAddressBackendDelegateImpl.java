package com.temenos.dbx.product.contract.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractAddressBackendDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ContractAddressDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.AddressBackendDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class ContractAddressBackendDelegateImpl implements ContractAddressBackendDelegate {

    private static final String CONTRACT_ID = "contractId";

    @Override
    public List<AddressDTO> getContractAddress(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        List<ContractAddressDTO> dtoList = null;
        List<AddressDTO> resultAddressList = new ArrayList<>();
        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = CONTRACT_ID + DBPUtilitiesConstants.EQUAL + contractId;
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject contractAddressJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTADDRESS_GET);
            if (null != contractAddressJson
                    && JSONUtil.hasKey(contractAddressJson, DBPDatasetConstants.DATASET_CONTRACTADDRESS)
                    && contractAddressJson.get(DBPDatasetConstants.DATASET_CONTRACTADDRESS).isJsonArray()) {
                JsonArray array = contractAddressJson.get(DBPDatasetConstants.DATASET_CONTRACTADDRESS)
                        .getAsJsonArray();
                dtoList = JSONUtils.parseAsList(array.toString(), ContractAddressDTO.class);
                if (null != dtoList) {
                    AddressBackendDelegate addressBD =
                            DBPAPIAbstractFactoryImpl.getBackendDelegate(AddressBackendDelegate.class);
                    for (ContractAddressDTO dto : dtoList) {
                        if (StringUtils.isNotBlank(dto.getAddressId())) {
                            AddressDTO addressDTO = addressBD.getAddressDetails(dto.getAddressId(), headersMap);
                            if (null != addressDTO && StringUtils.isNotBlank(addressDTO.getId()))
                                dto.setAddress(addressDTO);
                            resultAddressList.add(addressDTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10362);
        }
        return resultAddressList;
    }

    @Override
    public ContractAddressDTO createContractAddress(ContractAddressDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractAddressDTO resultDTO = null;
        if (inputDTO == null || StringUtils.isBlank(inputDTO.getId())
                || StringUtils.isBlank(inputDTO.getContractId())) {
            return resultDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, false);
            JsonObject orgAddressJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTADDRESS_CREATE);
            if (JSONUtil.isJsonNotNull(orgAddressJson)
                    && JSONUtil.hasKey(orgAddressJson, DBPDatasetConstants.DATASET_CONTRACTADDRESS)
                    && orgAddressJson.get(DBPDatasetConstants.DATASET_CONTRACTADDRESS).isJsonArray()) {
                JsonArray orgAddressArray =
                        orgAddressJson.get(DBPDatasetConstants.DATASET_CONTRACTADDRESS).getAsJsonArray();
                JsonObject object =
                        orgAddressArray.size() > 0 ? orgAddressArray.get(0).getAsJsonObject() : new JsonObject();
                resultDTO =
                        (ContractAddressDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                ContractAddressDTO.class, false);

            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10352);
        }
        return resultDTO;
    }

}
