package com.temenos.dbx.product.signatorygroupservices.backenddelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;

public interface SignatoryGroupBackendDelegate extends BackendDelegate {

	List<SignatoryGroupDTO> fetchSignatoryGroups(String coreCustomerId, String contractId, Map<String, Object> headersMap)
			throws ApplicationException;

	SignatoryGroupDTO fetchSignatoryGroupDetails(String signatoryGroupId, Map<String, Object> headersMap);

	List<SignatoryGroupDTO> fetchSignatories(String signatoryGroupId, Map<String, Object> headersMap);

	List<SignatoryGroupDTO> getSignatoryUsers(String coreCustomerId, String contractId, Map<String, Object> headersMap, DataControllerRequest request);

	JSONObject createSignatoryGroup(Map<String, Object> inputParams);
	
	JSONObject updateSignatoryGroup(String signatoryGroupValues, String newSigValues, String deleteSigValues );

	List<SignatoryGroupDTO> fetchSignatoryDetails(String signatoryGroupId);

	boolean checkContractCorecustomer(String contractId, String coreCustomerId, String userId, Map<String, Object> headersMap);

	List<ContractCustomersDTO> getCorecustomersForUser(String userId, Map<String, Object> headersMap);

	boolean isGroupPartOfPendingTransaction(String signatoryGroupId);
	
	boolean isGroupPartOfApprovalRule(String signatoryGroupId);

	
}

	