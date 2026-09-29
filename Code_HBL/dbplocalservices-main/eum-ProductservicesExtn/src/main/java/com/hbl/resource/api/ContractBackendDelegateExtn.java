package com.hbl.resource.api;

import java.util.List;
import java.util.Map;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.dto.ContractDTO;

public interface ContractBackendDelegateExtn extends ContractBackendDelegate{
	 public boolean updateContractStatus(String contractId, String statusId, String companyLegalUnit, Map<String, Object> headerMap)
	            throws ApplicationException;
	 public List<ContractDTO> getListOfContractsByStatus(String statusId, String legalEntityId, Map<String, Object> headerMap)
	            throws ApplicationException;
}
