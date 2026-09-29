package com.temenos.dbx.eum.product.contract.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

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
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCommunicationBackendDelegate;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;

public class ContractCommunicationBackendDelegateImpl implements ContractCommunicationBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final String CONTRACT_ID = "contractId";

    @Override
    public List<ContractCommunicationDTO> getContractCommunication(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        List<ContractCommunicationDTO> dtoList = null;
        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = CONTRACT_ID + DBPUtilitiesConstants.EQUAL + contractId;
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject contractCommJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTCOMMUNICATION_GET);
            if (null != contractCommJson
                    && JSONUtil.hasKey(contractCommJson, DBPDatasetConstants.DATASET_CONTRACTCOMMUNICATION)
                    && contractCommJson.get(DBPDatasetConstants.DATASET_CONTRACTCOMMUNICATION).isJsonArray()) {
                JsonArray array = contractCommJson.get(DBPDatasetConstants.DATASET_CONTRACTCOMMUNICATION)
                        .getAsJsonArray();
                dtoList = JSONUtils.parseAsList(array.toString(), ContractCommunicationDTO.class);
            }
        } catch (Exception e) {
        	alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10362);
        }
        return dtoList;
    }

    @Override
    public void deleteContractCommunicationAddress(String contractId, Map<String, Object> headersMap) {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_contractId", contractId);
        ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CONTRACT_COMMUNICATION_ADDRESS_DELETE_PROC);

    }
}
