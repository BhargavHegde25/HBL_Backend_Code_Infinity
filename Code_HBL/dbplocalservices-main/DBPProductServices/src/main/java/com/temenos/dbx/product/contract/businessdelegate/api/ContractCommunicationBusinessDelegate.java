package com.temenos.dbx.product.contract.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;

public interface ContractCommunicationBusinessDelegate extends BusinessDelegate {

    public ContractCommunicationDTO createContractCommunication(ContractCommunicationDTO dto,
            Map<String, Object> headersMap) throws ApplicationException;

    public List<ContractCommunicationDTO> getContractCommunication(String contractId,
            Map<String, Object> headersMap) throws ApplicationException;

    public void deleteContractCommunicationAddress(String contractId, Map<String, Object> headersMap);

}
