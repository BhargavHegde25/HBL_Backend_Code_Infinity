package com.temenos.dbx.product.contract.businessdelegate.impl;

import java.util.List;
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
import com.temenos.dbx.product.contract.backenddelegate.api.ContractCommunicationBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCommunicationBusinessDelegate;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class ContractCommunicationBusinessDelegateImpl implements ContractCommunicationBusinessDelegate {

    @Override
    public ContractCommunicationDTO createContractCommunication(ContractCommunicationDTO inputCommunicationDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCommunicationDTO resultCommunicationDTO = null;
        if (inputCommunicationDTO == null || StringUtils.isBlank(inputCommunicationDTO.getId())
                || StringUtils.isBlank(inputCommunicationDTO.getContractId())) {
            return resultCommunicationDTO;

        }
        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputCommunicationDTO, false);
            JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTCOMMUNICATION_CREATE);
            if (JSONUtil.isJsonNotNull(communicationJson)
                    && JSONUtil.hasKey(communicationJson, DBPDatasetConstants.DATASET_CONTRACTCOMMUNICATION)
                    && communicationJson.get(DBPDatasetConstants.DATASET_CONTRACTCOMMUNICATION).isJsonArray()) {
                JsonArray commuicationArray =
                        communicationJson.get(DBPDatasetConstants.DATASET_CONTRACTCOMMUNICATION).getAsJsonArray();
                JsonObject object =
                        commuicationArray.size() > 0 ? commuicationArray.get(0).getAsJsonObject() : new JsonObject();
                resultCommunicationDTO =
                        (ContractCommunicationDTO) DTOUtils.loadJsonObjectIntoObject(object,
                                ContractCommunicationDTO.class, false);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10355);
        }
        return resultCommunicationDTO;
    }

    @Override
    public List<ContractCommunicationDTO> getContractCommunication(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        ContractCommunicationBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCommunicationBackendDelegate.class);
        return backendDelegate.getContractCommunication(contractId, headersMap);
    }

    @Override
    public void deleteContractCommunicationAddress(String contractId, Map<String, Object> headersMap) {
        ContractCommunicationBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCommunicationBackendDelegate.class);
        backendDelegate.deleteContractCommunicationAddress(contractId, headersMap);

    }

}
