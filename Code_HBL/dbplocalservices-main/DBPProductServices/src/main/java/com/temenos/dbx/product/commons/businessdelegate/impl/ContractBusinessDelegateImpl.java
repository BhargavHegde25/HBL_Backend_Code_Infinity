package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class ContractBusinessDelegateImpl implements ContractBusinessDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	FeatureActionBusinessDelegate featureActionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);

	/*@Override
	public List<ContractCoreCustomersDTO> fetchContractCustomers() {
		
		List<ContractCoreCustomersDTO> contractCoreCustomers = new ArrayList<ContractCoreCustomersDTO>();
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_CONTRACTCORECUSTOMERS_GET).
					withRequestParameters(null).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray actions = CommonUtils.getFirstOccuringArray(res);
			contractCoreCustomers = JSONUtils.parseAsList(actions.toString(), ContractCoreCustomersDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return contractCoreCustomers;
	}*/
	
	@Override
	public List<String> fetchContractCustomers(String userId) {
		
		List<String> contractCustomers = new ArrayList<String>();
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(DBPUtilitiesConstants.FILTER, "customerId" + DBPUtilitiesConstants.EQUAL + userId);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_CONTRACTCUSTOMERS_GET).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray contracts = CommonUtils.getFirstOccuringArray(res);
			
			for(Object obj: contracts) {
				JSONObject contract = (JSONObject)(obj);
				String contractId = contract.optString("contractId");
				String coreCustomerId = contract.optString("coreCustomerId");
				contractCustomers.add(contractId + "_" + coreCustomerId);
			}
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return contractCustomers;
	}

	@Override
	public LimitsDTO fetchLimits(String contractId, String coreCustomerId, String actionId, String legalEntityId) {
		LimitsDTO limitsDTO = null;
		
		JSONArray limitsArray = fetchContractActionLimits(contractId, coreCustomerId, actionId);
		limitsDTO = _fetchLimitsDTO(limitsArray);
		LimitsDTO featureLimits = featureActionDelegate.fetchLimits(actionId, legalEntityId);
		
		limitsDTO.setMaxTransactionLimit(Math.min(featureLimits.getMaxTransactionLimit(), limitsDTO.getMaxTransactionLimit()));
		limitsDTO.setDailyLimit(Math.min(featureLimits.getDailyLimit(), limitsDTO.getDailyLimit()));
		limitsDTO.setWeeklyLimit(Math.min(featureLimits.getWeeklyLimit(), limitsDTO.getWeeklyLimit()));
		limitsDTO.setMinTransactionLimit(featureLimits.getMinTransactionLimit());
		return limitsDTO;
	}
	
	private LimitsDTO _fetchLimitsDTO(JSONArray limitsArray) {
		
		LimitsDTO limitsDTO = new LimitsDTO();
		
		try {
			for(Object obj: limitsArray) {
	
				JSONObject limitsObj = (JSONObject) obj;
	
				String limitType = (limitsObj.has("limitTypeId")) ? limitsObj.getString("limitTypeId"): "";
				Double limit = (limitsObj.has("value")) ? limitsObj.getDouble("value"): 0;
	
				switch (limitType) {
	
					case Constants.MAX_TRANSACTION_LIMIT:
						limitsDTO.setMaxTransactionLimit(limit);
						break;
					case Constants.DAILY_LIMIT:
						limitsDTO.setDailyLimit(limit);
						break;
					case Constants.WEEKLY_LIMIT:
						limitsDTO.setWeeklyLimit(limit);
						break;
					default:
						break;
				}
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch contract-corecustomer limits from DB: ", e).log();
			return null;
		}
		
		return limitsDTO;
	}
	
	@Override
	public LimitsDTO fetchExhaustedLimits(String contractId, String coreCustomerId, String featureActionID, String date) {
		
		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
		
		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("companyid", contractId + "_" + coreCustomerId);
		requestParameters.put("date", date);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			if(response == null) {
				return null;
			}
			limitsDTO = JSONUtils.parse(response, LimitsDTO.class);
			
			if(limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyLimit() == null || limitsDTO.getWeeklyLimit() == null)) {
				return null;
			}
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching Organization limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
	@Override
	public Set<String> fetchFeatureActions(String contractId, String coreCustomerId) {
		
		Set<String> featureActions = new HashSet<String>();

		try {
			JSONArray actions = fetchContractActionLimits(contractId, coreCustomerId, null);
			
			if(actions != null && actions.length() > 0) {
				
				for(Object permissionObject : actions) {
					JSONObject permission = (JSONObject) permissionObject;
					String actionId = permission.optString(Constants.ACTIONID);
					featureActions.add(actionId);
				}
			}
			else {
				alert.prepareError("No active feature actions exists for the given organisation in DB: ").log();
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch Organisation feature action entry from DB: " + e).log();
		}
		catch (Exception e) {
			alert.prepareError("Caught exception whiel Organisation feature action entry from DB: " + e).log();
		}
		
		return featureActions;
	}
	
	
	private JSONArray fetchContractActionLimits(String contractId, String coreCustomerId, String actionId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CONTRACTACTIONLIMIT_GET;
		
		String filter = "contractId" + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND +
				"coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId;
		
		if(StringUtils.isNotEmpty(actionId))
			filter = filter + DBPUtilitiesConstants.AND + "actionId" + DBPUtilitiesConstants.EQUAL + actionId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			if(response == null) {
				return null;
			}
			JSONObject actionObj = new JSONObject(response);
			JSONArray actions = CommonUtils.getFirstOccuringArray(actionObj);
			
			return actions;
		}
		catch(JSONException e) {
			alert.prepareError("Failed to fetch contract action limits from DB: " + e).log();
		}
		catch(Exception e) {
			alert.prepareError("Failed to fetch contract action limits from DB: " + e).log();
		}
		return null;
	}

	public List<ContractCoreCustomersDTO> fetchContractCustomers(Set<String> coreCustomerIds) {
		List<ContractCoreCustomersDTO> contractCoreCustomers = new ArrayList<>();
		HashMap<String,Object> requestParameters = new HashMap<String,Object>();
		try {
			if (CollectionUtils.isNotEmpty(coreCustomerIds)) {
				String filter = " ( " + "coreCustomerId" + " eq "
						+ String.join(" or coreCustomerId eq ", (Iterable) coreCustomerIds) + " ) ";
				requestParameters.put("$filter", filter);
				String response = DBPServiceExecutorBuilder.builder().withServiceId("dbpRbLocalServicesdb")
						.withObjectId(null).withOperationId(OperationName.DB_CONTRACTCORECUSTOMERS_GET)
						.withRequestParameters(requestParameters).build().getResponse();
				JSONObject res = new JSONObject(response);
				JSONArray actions = CommonUtils.getFirstOccuringArray(res);
				contractCoreCustomers = JSONUtils.parseAsList(actions.toString(), ContractCoreCustomersDTO.class);
			}
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return contractCoreCustomers;
	}
	
}
