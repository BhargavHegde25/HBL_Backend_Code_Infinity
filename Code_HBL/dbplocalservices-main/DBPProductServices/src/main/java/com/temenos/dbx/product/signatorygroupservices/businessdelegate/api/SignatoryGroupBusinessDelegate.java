package com.temenos.dbx.product.signatorygroupservices.businessdelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.CustomerDTO;

import com.temenos.dbx.product.signatorygroupservices.dto.CustomerSignatoryGroupDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupRequestMatrixDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Interface for SignatoryGroupBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface SignatoryGroupBusinessDelegate extends BusinessDelegate {

	List<SignatoryGroupDTO> fetchSignatoryGroups(String coreCustomerId, String contractId, Map<String, Object> headersMap)
			throws ApplicationException;

	List<SignatoryGroupDTO> fetchSignatoryDetails(String signatoryGroupId, Map<String, Object> headersMap);
	
	List<SignatoryGroupDTO> getSignatoryUsers(String coreCustomerId, String contractId, Map<String, Object> headersMap, DataControllerRequest request);

	JSONObject createSignatoryGroup(String signatoryGroupName, String signatoryGroupDescription, String coreCustomerId,
			String contractId, JSONArray signatories, String createdby);
	
	/**
	 * Fetches Pending Signatory Group Request Matrix
	 * @param requestId
	 * @return List of Signatory Group Details
	 */
	public List<SignatoryGroupRequestMatrixDTO> fetchPendingSignatoryGroupRequestMatrix(String requestId);
	
	/**
	 * Gets Signatory Group details associated to customer
	 * @param customerId
	 * @return List of CustomerSignatoryGroupDTO
	 */
	public List<CustomerSignatoryGroupDTO> fetchCustomerSignatoryGroups(String customerId);

	/**
	 * @description Fetch Signatory Group details associated with customer and request
	 * @param customerId
	 * @param requestId
	 * @return List of CustomerSignatoryGroupDTO
	 */
	public List<CustomerSignatoryGroupDTO> fetchCustomerSignatoryGroupsAssociatedWithRequest(String customerId, String requestId);


	/**
	 * Updates SignatoryGroupRequestMatrix Table with given input parameters
	 * @param SignatoryGroupRequestMatrixDTO
	 * @return boolean true - if successful otherwise false
	 */
	public boolean updateSignatoryGroupRequestMatrix(SignatoryGroupRequestMatrixDTO sigGrpReqMatrixDTO);
	
	JSONObject updateSignatoryGroup(String signatoryGroupId, String signatoryGroupName, String signatoryGroupDescription, String coreCustomerId,
			String contractId, JSONArray signatories, String createdby, DataControllerRequest request);
	
	public boolean hasSignatoryGroupAccesstoUser(String sigGroupId, String coreCustomerId,String contractId,Map<String, Object> headersMap);
	
	public boolean isGroupNameDuplicate(String groupName, String coreCustomerId, String contractId,
            Map<String, Object> headersMap);

    public boolean isSignatoryAlreadyPresentInAnotherGroup(String customerId, String coreCustomerId, String contractId,
            Map<String, Object> headersMap);

	boolean checkContractCorecustomer(String contractId, String coreCustomerId, String userId, Map<String, Object> headersMap);

	JSONArray getCorecustomersForUser(String userId, Map<String, Object> headersMap);

	boolean isEligibleForDelete(String signatoryGroupId);

	SignatoryGroupDTO fetchSignatoryGroupDetails(String signatoryGroupId, Map<String, Object> headersMap);

    /**
	 * Fetches Signatory Group Customer Details
	 * @param signatoryGroupId
	 * @return List of Customers belonging to that Group
	 */
	public List<SignatoryGroupDTO> fetchSignatoryGroupCustomers(String signatoryGroupId);

	
    public boolean deleteSignatoryGroup(String signatoryGroupId, Map<String, Object> headersMap);

	public boolean updateSignatoryGroupForInfinityUser(String cif, String contractId, String customerId,
			String signatoryGroupId);

	public boolean deleteSignatoryGroupAfterApproval(String signatoryGroupId);
	public JSONObject updateSignatoryGroupAfterApproval(String signatoryGroupValues, String newSigValues,
			String deleteSigValues);

	public JSONArray fetchNewSignatoryGroupUserDetails(String customerIdList, String coreCustomerIdInput);

	
}
