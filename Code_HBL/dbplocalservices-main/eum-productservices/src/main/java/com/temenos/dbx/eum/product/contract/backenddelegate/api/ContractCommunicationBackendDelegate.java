package com.temenos.dbx.eum.product.contract.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;

public interface ContractCommunicationBackendDelegate extends BackendDelegate {

    public List<ContractCommunicationDTO> getContractCommunication(String contractId, Map<String, Object> headersMap)
            throws ApplicationException;

    public void deleteContractCommunicationAddress(String contractId, Map<String, Object> headersMap);

}
