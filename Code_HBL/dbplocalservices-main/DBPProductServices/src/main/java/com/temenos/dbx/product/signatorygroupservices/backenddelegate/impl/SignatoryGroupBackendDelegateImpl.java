package com.temenos.dbx.product.signatorygroupservices.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.SignatoryGroupBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;
import com.temenos.dbx.product.utils.InfinityConstants;


public class SignatoryGroupBackendDelegateImpl implements SignatoryGroupBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<SignatoryGroupDTO> getSignatoryUsers(String coreCustomerId, String contractId, Map<String, Object> headersMap, DataControllerRequest request){

		List<SignatoryGroupDTO> signatoryGroupDTO = null;	
		try {
			Set<String> actionSet = new HashSet<String>();
			Set<String> users = new HashSet<String>();
			actionSet.add("BILL_PAY_APPROVE");
			actionSet.add("DOMESTIC_WIRE_TRANSFER_APPROVE");
			actionSet.add("BILL_PAY_SELF_APPROVAL");	
			actionSet.add("ACH_COLLECTION_APPROVE");
			actionSet.add("ACH_FILE_APPROVE");
			actionSet.add("ACH_FILE_SELF_APPROVAL");
			actionSet.add("ACH_PAYMENT_APPROVE");
			actionSet.add("ACH_PAYMENT_SELF_APPROVAL");
			actionSet.add("BULK_PAYMENT_REQUEST_APPROVE");   
			actionSet.add("DOMESTIC_WIRE_TRANSFER_SELF_APPROVAL");
			actionSet.add("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE");
			actionSet.add("INTERNATIONAL_WIRE_TRANSFER_APPROVE");	   
			actionSet.add("INTERNATIONAL_WIRE_TRANSFER_SELF_APPROVAL");   
			actionSet.add("INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE");
			actionSet.add("INTRA_BANK_FUND_TRANSFER_APPROVE");
			actionSet.add("INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE");
			actionSet.add("INTRA_BANK_FUND_TRANSFER_SELF_APPROVAL");
			actionSet.add("P2P_SELF_APPROVAL");
			actionSet.add("TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE");
			actionSet.add("TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE");
			actionSet.add("TRANSFER_BETWEEN_OWN_ACCOUNT_SELF_APPROVAL");
			actionSet.add("ACH_COLLECTION_SELF_APPROVAL");
			actionSet.add("CHEQUE_BOOK_REQUEST_APPROVE");
			actionSet.add("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE");
			actionSet.add("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL");
			actionSet.add("INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE");
			actionSet.add("INTER_BANK_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL");
			actionSet.add("P2P_APPROVE");

			String filter =
					InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId + DBPUtilitiesConstants.AND +
					InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId;			    

			Map<String, Object> input = new HashMap<String, Object>();

			input.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject dcresponse =
					ServiceCallHelper.invokeServiceAndGetJson(input, headersMap, URLConstants.CUSTOMER_ACTION_LIMITS_GET);
			JsonArray actions = new JsonArray();
			if (dcresponse.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
				JsonElement jsonElement = dcresponse.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					actions = jsonElement.getAsJsonArray();
					for (int i = 0; i < actions.size(); i++) {
						JsonObject jsonObject2 = actions.get(i).getAsJsonObject();

						String actionId = jsonObject2.has(InfinityConstants.Action_id)
								&& !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
								? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
										: null;
						String userId = jsonObject2.has(InfinityConstants.Customer_id)
								&& !jsonObject2.get(InfinityConstants.Customer_id).isJsonNull()
								? jsonObject2.get(InfinityConstants.Customer_id).getAsString().trim()
										: null;
						if(actionSet.contains(actionId)) {
							users.add(userId);
						}
					}
				}
			}
			List<String> signatoryGroupIDsByContractAndCIF = getSignatoryGroupIDsByContractAndCIF(contractId, coreCustomerId);
			// Creating an iterator
			Iterator<String> value = users.iterator();			 
			JSONArray signatoryUsers = new JSONArray();

			Set<String> signatoryunassignedusers = new HashSet<String>();			

			while (value.hasNext()) {
				String custId = (String) value.next();
				filter = "customerId" + DBPUtilitiesConstants.EQUAL + custId;
				input.put(DBPUtilitiesConstants.FILTER, filter);
				dcresponse =
						ServiceCallHelper.invokeServiceAndGetJson(input, headersMap, URLConstants.CUSTOMER_SIGNATORY_GROUP_GET);
				JsonElement jsonElement = dcresponse.get(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP);
				List<String> customerSignatorygroups = new ArrayList<String>(); 
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() != 0) {
					JsonArray groupsArray = jsonElement.getAsJsonArray();
					for(int i = 0; i < groupsArray.size() ; i++) {
						customerSignatorygroups.add(groupsArray.get(i).getAsJsonObject().get("signatoryGroupId").getAsString());
					}
				}

				boolean isAssociated = false;
				for(String group: customerSignatorygroups) {					
					if(signatoryGroupIDsByContractAndCIF.contains(group)) {
						isAssociated = true;
						break;
					}
				}
				if(!isAssociated) {
					signatoryunassignedusers.add(custId);  			   		   
				}
				// Result user = HelperMethods.callGetApi(request, query,HelperMethods.getHeaders(request),URLConstants.USER_GET);
			}

			String customerIds = String.join(",", signatoryunassignedusers);

			JSONArray signatoryUsersArray = fetchNoGroupUsers(customerIds,coreCustomerId);
			if (signatoryUsersArray.length() > 0) {
				for (int i = 0; i < signatoryUsersArray.length(); i++) {
					JSONObject userDetail = new JSONObject();
					JSONObject jsonObject2 = signatoryUsersArray.getJSONObject(i);
					String Name= "";
					Name = jsonObject2.optString("firstName", "");
					Name += jsonObject2.optString("lastName", ""); 
					String Id = jsonObject2.get("userId").toString();
					String UserName = jsonObject2.get("userName").toString();
					String role = jsonObject2.get("role").toString();
					userDetail.put("userId", Id);
					userDetail.put("userName", UserName);
					userDetail.put("fullName", Name);
					userDetail.put("role", role);
					signatoryUsers.put(userDetail);
				}
			}
			signatoryGroupDTO = JSONUtils.parseAsList(signatoryUsers.toString(), SignatoryGroupDTO.class);
		}

		catch (Exception e) {
			alert.prepareError("Exception in SignatoryGroupBackendDelegateImpl : "+ e.getMessage() + e).log();
		}

		return signatoryGroupDTO;
	}

	private List<String> getSignatoryGroupIDsByContractAndCIF(String contractId, String coreCustomerId) {
		JSONArray signatoryGroups = getSignatoryGroupsByContractAndCIF(contractId, coreCustomerId);
		List<String> groupIds = new ArrayList<String> ();
		if(signatoryGroups !=null) {
			for( Object item : signatoryGroups) {
				JSONObject group = (JSONObject) item;
				groupIds.add(group.getString("signatoryGroupId"));
			}
		}

		return groupIds;
	}

	private JSONArray getSignatoryGroupsByContractAndCIF(String contractId, String coreCustomerId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_SIGNATORYGROUP_GET;

		try {
			Map<String,Object> requestParams = new HashMap <String,Object>();

			String filterQuery =
					InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId + DBPUtilitiesConstants.AND +
					InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId;		
			requestParams.put(DBPUtilitiesConstants.FILTER, filterQuery.toString());

			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName). withObjectId(null).
					withOperationId(operationName). withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				return responseObj.optJSONArray(Constants.SIGNATORYGROUP);
			} else {
				return new JSONArray();
			}
		}catch (Exception e) {
			alert.prepareError("Exception occured while fetching signatories", e).log();
			return null;
		}
	}

	private JSONArray fetchNoGroupUsers(String customerIds,String coreCustomerId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_NO_GROUP_USERS_PROC;
		JSONArray signatoryGroups = new JSONArray();
		try {
			Map<String, Object> requestParams = new HashMap <String,Object>();
			requestParams.put("_customerId",customerIds);
			requestParams.put("_coreCustomerId",coreCustomerId);
			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				signatoryGroups  = CommonUtils.getFirstOccuringArray(responseObj);
			}
		}
		catch(JSONException e) {
			return signatoryGroups;
		}catch (Exception e) {
			return signatoryGroups;
		}
		return signatoryGroups;	
	}

	@Override
	public List<SignatoryGroupDTO> fetchSignatoryGroups(String coreCustomerId,String contractId,Map<String, Object> headersMap) throws ApplicationException {

		List<SignatoryGroupDTO> resultDTO = new ArrayList<>();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_SIGNATORYGROUPS_PROC;
		String customerIds=coreCustomerId.replace("\"","");
		try {
			Map<String, Object> requestParams = new HashMap <String,Object>();
			requestParams.put("_coreCustomerId",customerIds);
			requestParams.put("_contractId",contractId);
			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONArray signatoryGroups  = CommonUtils.getFirstOccuringArray(responseObj);
				if (signatoryGroups != null) {
					resultDTO = JSONUtils.parseAsList(signatoryGroups.toString(), SignatoryGroupDTO.class);
				}
			}

		}
		catch(JSONException e) {
			alert.prepareError("Exception occured while fetching signatory groups", e).log();
			return null;
		}catch (Exception e) {
			alert.prepareError("Exception occured while fetching signatory groups", e).log();
			return null;
		}

		return resultDTO;
	}

	@Override
	public List<SignatoryGroupDTO> fetchSignatoryDetails(String signatoryGroupId) {

		List<SignatoryGroupDTO> resultDTO = new ArrayList<>();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_SIGNATORYGROUP_DETAILS_PROC;

		try {
			Map<String, Object> requestParams = new HashMap <String,Object>();
			requestParams.put("_signatoryGroupId",signatoryGroupId);
			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONArray signatoryGroups  = CommonUtils.getFirstOccuringArray(responseObj);
				if (signatoryGroups != null) {
					resultDTO = JSONUtils.parseAsList(signatoryGroups.toString(), SignatoryGroupDTO.class);
				}
			}
		}
		catch(JSONException e) {
			alert.prepareError("Exception occured while fetching signatory groups", e).log();
			return null;
		}catch (Exception e) {
			alert.prepareError("Exception occured while fetching signatory groups", e).log();
			return null;
		}

		return resultDTO;
	}

	@Override
	public List<SignatoryGroupDTO> fetchSignatories(String signatoryGroupId, Map<String, Object> headersMap) {
		List<SignatoryGroupDTO> resultDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMERSIGNATORYGROUP_GET;


		try {
			Map<String,Object> requestParams = new HashMap <String,Object>();
			StringBuilder filterQuery = new StringBuilder();
			filterQuery.append(Constants.SIGNATORYGROUPID).append(DBPUtilitiesConstants.EQUAL).append(signatoryGroupId);
			requestParams.put(DBPUtilitiesConstants.FILTER, filterQuery.toString());
			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONArray signatories = responseObj.getJSONArray(Constants.CUSTOMERSIGNATORYGROUP);
				resultDTO = JSONUtils.parseAsList(signatories.toString(), SignatoryGroupDTO.class);
			}
		}catch (Exception e) {
			alert.prepareError("Exception occured while fetching signatories", e).log();
			return null;
		}
		return resultDTO;
	}
	@Override
	public SignatoryGroupDTO fetchSignatoryGroupDetails(String signatoryGroupId, Map<String, Object> headersMap) {
		SignatoryGroupDTO resultDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_SIGNATORYGROUP_GET;

		try {
			Map<String,Object> requestParams = new HashMap <String,Object>();
			StringBuilder filterQuery = new StringBuilder();
			filterQuery.append(Constants.SIGNATORYGROUPID).append(DBPUtilitiesConstants.EQUAL).append(signatoryGroupId);
			requestParams.put(DBPUtilitiesConstants.FILTER, filterQuery.toString());
			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONObject signatoryGroupDetail = responseObj.getJSONArray(Constants.SIGNATORYGROUP).getJSONObject(0);
				resultDTO = JSONUtils.parse(signatoryGroupDetail.toString(), SignatoryGroupDTO.class);
			}
		}catch (Exception e) {
			alert.prepareError("Exception occured while fetching signatories", e).log();
			return null;
		}
		return resultDTO;
	}


	@Override
	public JSONObject createSignatoryGroup(Map<String, Object> requestParams) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_SIGNATORYGROUP_CREATE_PROC;
//		Map<String, Object> requestParams = new HashMap <String,Object>();
//		requestParams.put(Constants.SIGNATORYGROUPVALUES,signatoryGroupValues);
//		requestParams.put(Constants.CUSTOMERSIGNATORYGROUPVALUES,customerSignatoryGroupValues);
		String getResponse = null;
		try{
			getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();			   
		}catch(Exception e) {
			alert.prepareError("Exception occured while creating signatory group", e).log();
			return null;
		}
		JSONObject responseObj = new JSONObject(getResponse);    
		return responseObj;
	}

	@Override
	public JSONObject updateSignatoryGroup(String signatoryGroupValues, String newSigValues, String deleteSigValues) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_SIGNATORYGROUP_UPDATE_PROC;
		Map<String, Object> requestParams = new HashMap <String,Object>();
		requestParams.put("_sigGroupValues",signatoryGroupValues);
		requestParams.put("_newSigValues",newSigValues);
		requestParams.put("_deleteSigValues",deleteSigValues);
		String getResponse = null;
		try{
			getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();			   
		}catch(Exception e) {
			alert.prepareError("Exception occured while updating signatory group", e).log();
			return null;
		}
		JSONObject responseObj = new JSONObject(getResponse);    
		return responseObj;
	}

	@Override
	public boolean checkContractCorecustomer(String contractId, String coreCustomerId, String userId, Map<String, Object> headersMap) {
		Map<String, Object> inputParams = new HashMap<>();
		String filter = "contractId" + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND +
				"coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId + DBPUtilitiesConstants.AND +
				"customerId" + DBPUtilitiesConstants.EQUAL + userId;
		try {
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject contractCustomerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.CONTRACT_CUSTOMERS_GET);
			if (null != contractCustomerJson
					&& JSONUtil.hasKey(contractCustomerJson, DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)
					&& contractCustomerJson.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS).isJsonArray()) {
				JsonArray array = contractCustomerJson.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)
						.getAsJsonArray();
				List<ContractCustomersDTO> dtoList = JSONUtils.parseAsList(array.toString(), ContractCustomersDTO.class);
				if(!dtoList.isEmpty()) {
					return true;
				}
			}
		}catch(Exception e){
			alert.prepareError("Exception occured while fetching contractcustomers", e).log();
			return false;
		}
		return false;
	}

	@Override
	public List<ContractCustomersDTO> getCorecustomersForUser(String userId, Map<String, Object> headersMap) {
		Map<String, Object> inputParams = new HashMap<>();
		List<ContractCustomersDTO> dtoList=null;
		String filter = "customerId" + DBPUtilitiesConstants.EQUAL + userId;
		try {
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject contractCustomerJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.CONTRACT_CUSTOMERS_GET);
			if (null != contractCustomerJson
					&& JSONUtil.hasKey(contractCustomerJson, DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)
					&& contractCustomerJson.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS).isJsonArray()) {
				JsonArray array = contractCustomerJson.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS).getAsJsonArray();
				dtoList = JSONUtils.parseAsList(array.toString(), ContractCustomersDTO.class);
			}
		}catch(Exception e){
			alert.prepareError("Exception occured while fetching contractcustomers", e).log();
			return null; 
		}
		return dtoList;
	}

	@Override
	public boolean isGroupPartOfPendingTransaction(String signatoryGroupId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_APPROVAL_GROUPS_FOR_PENDINGTXN_PROC;

		try {
			Map<String, Object> requestParams = new HashMap <String,Object>();
			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONArray signatoryGroups  = CommonUtils.getFirstOccuringArray(responseObj);
				Set<String> ids= new HashSet<String>();
				for(int i=0 ; i< signatoryGroups.length();i++) {
					JSONObject json = signatoryGroups.getJSONObject(i);
					ids.add(json.get(Constants.SIGNATORYGROUPID).toString());
				}
				if (ids.contains(signatoryGroupId))
					return true;
			}
		}
		catch(JSONException e) {
			alert.prepareError("Exception occured while fetching approval groups", e).log();
		}catch (Exception e) {
			alert.prepareError("Exception occured while fetching approval groups", e).log();
		}
		return false;

	}

	@Override
	public boolean isGroupPartOfApprovalRule(String signatoryGroupId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_SIGNATORYGROUPS_IN_APPROVALRULE_PROC;

		try {
			Map<String, Object> requestParams = new HashMap <String,Object>();
			String getResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(getResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONArray signatoryGroups  = CommonUtils.getFirstOccuringArray(responseObj);
				Set<String> ids= new HashSet<String>();
				for(int i=0 ; i< signatoryGroups.length();i++) {
					JSONObject json = signatoryGroups.getJSONObject(i);
					ids.add(json.get(Constants.SIGNATORYGROUPID).toString());
				}
				if (ids.contains(signatoryGroupId))
					return true;
			}
		}
		catch(JSONException e) {
			alert.prepareError("Exception occured while fetching approval groups", e).log();
		}catch (Exception e) {
			alert.prepareError("Exception occured while fetching approval groups", e).log();
		}
		return false;
	}

}