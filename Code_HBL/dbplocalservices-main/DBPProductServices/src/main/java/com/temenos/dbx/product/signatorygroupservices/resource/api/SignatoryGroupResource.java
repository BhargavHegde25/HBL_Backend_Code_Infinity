package com.temenos.dbx.product.signatorygroupservices.resource.api;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;


/**
 * 
 * @author KH1769
 * @version 1.0
 * Interface for SignatoryGroupResource extends {@link Resource}
 *
 */
public interface SignatoryGroupResource extends Resource{
    
	/**
	 * (INFO) Fetches all signatory groups for the given core customer id of user by calling business delegate method to fetch the same
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2347
	 */
	Result fetchSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * (INFO) Fetches all the details of the signatory group with the given group id of user by calling business delegate method to fetch the same
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2347
	 */
	Result fetchSignatoryGroupDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO)calls the business delegate for fetching all the assigned users of coreCustomerId(given in input params) 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH1769
	 */
	public Result getSignatoryUsers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * (INFO)calls the business delegate for creating a signatory group for the user 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2347
	 */
	Result createSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO)calls the business delegate for creating a signatory group for the user 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2347
	 */
	Result updateSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * (INFO)calls the business delegate for fetching all signatory groups under given corecustomerIds 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2661
	 */
	Result fetchSignatoryGroupsByCoreCustomerIds(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * (INFO)calls the business delegate for fetching all signatory groups under logged in user's corecustomerId  
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2661
	 */
	Result fetchAllSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO) delete Signatory Group with the given signatoryGroupId
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author 22712
	 */
	Result deleteSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * Checks the availability of a signatory group name
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 */
	Result isSignatoryGroupNameAvailable(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);


	/**
	 * (INFO)calls the business delegate to check if the signatory group can be deleted or not  
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 * @author KH2661
	 */
	Result isEligibleforDelete(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * Handler method for create/update infinity user 
	 * @param input
	 * @return
	 */
	Result updateSignatoryGroupForInfinityUser(JSONObject input);
	
	/**
	 * Fetches all the signatory group details of a customer
	 * @param customerId
	 * @return
	 */
	Result fetchSignatoryGroupDetailsById(String customerId);

	/**
	 * Handler method for updating signatory groups related to custom role
	 * @param input
	 * @return
	 */
	Result updateSignatoryGroupForCustomRole(JSONObject input);
	
	JSONObject createSignatoryGroupAfterApproval(String signatoryGroupName, String signatoryGroupDescription, String coreCustomerId,
			String contractId, JSONArray signatories, String createdby,boolean isCreateFlow, DataControllerRequest dcr);
	
	JSONObject convertDetails(List<SignatoryGroupDTO> signatories);

	
}
