package com.temenos.dbx.product.approvalservices.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

/**
 * 
 * @author KH2387
 * @version 1.0 Extends the {@link ApproversBusinessDelegate}
 */
public class ApproversBusinessDelegateImpl implements ApproversBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	/**
	 *  method to fetch approver Ids for a given actionId,accountId and organizationId
	 *  @param String companyId
	 *  @param String accountId
	 *  @param String actionId
	 *  return list of approvers
	 */
	@Override
	public List<String> getAccountActionApproverList(String contractId, String cif, String accountIds, String actionId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GETACCOUNTACTIONAPPROVERLIST;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		FeatureActionBusinessDelegate featureActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
		FeatureActionDTO featureActionDTO = featureActionBusinessDelegate.getFeatureActionById(actionId);

		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		requestParameters.put("_accountIds", accountIds);
		requestParameters.put("_approvalActionList", featureActionDTO.getApproveFeatureAction());
		requestParameters.put("_featureId", featureActionDTO.getFeatureId());

		try {
			String approversResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approversResponseJson = new JSONObject(approversResponse);
			if (approversResponseJson.has(Constants.RECORDS)
					&& approversResponseJson.getJSONArray(Constants.RECORDS).length() > 0) {
				diagnostic.prepareInfo("approvers fetched successfully").log();
				JSONArray approversArray = approversResponseJson.getJSONArray(Constants.RECORDS);
				List<String> approvers = new ArrayList<String>();
				for(int i=0;i<approversArray.length();i++) {
					approvers.add(approversArray.getJSONObject(i).getString("id"));
				}				
				return approvers;
			} else {
				alert.prepareError("Unable to fetch approvers list: with approversResponse"+approversResponse).log();
				return null;
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch approvers list ").log();
			
			
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAccountActionApproverList method: ").log();
			
			
			return null;
		}
	}

	@Override
	public List<String> getRequestApproversList(String requestId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GETREQUESTAPPROVERS_PROC;

		HashMap<String, Object> requestParameters = new HashMap<>();

		requestParameters.put("_requestId", requestId);
		requestParameters.put("_status", "");
		
		try {
			String approversResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject approversResponseJson = new JSONObject(approversResponse);
			if (approversResponseJson.has(Constants.RECORDS)
					&& approversResponseJson.getJSONArray(Constants.RECORDS).length() > 0) {
				diagnostic.prepareInfo("approvers fetched successfully").log();
				JSONArray approversArray = approversResponseJson.getJSONArray(Constants.RECORDS);
				List<String> approvers = new ArrayList<String>();
				for(int i=0;i<approversArray.length();i++) {
					String FullName = approversArray.getJSONObject(i).getString(Constants.FirstName) + " " + approversArray.getJSONObject(i).getString(Constants.LastName);
					approvers.add(FullName);
				}				
				return approvers;
			} else {
				alert.prepareError("Unable to fetch approvers list: with approversResponse: "+approversResponse).log();
				return null;
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch approvers list ",e).log();
			
			
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getRequestApproverList method: ",e).log();
			
			return null;
		}
	}
	
	@Override
	public List<String> getRequestActedApproversList(String requestId, String status) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GETREQUESTAPPROVERS_PROC;

		HashMap<String, Object> requestParameters = new HashMap<>();

		requestParameters.put("_requestId", requestId);
		requestParameters.put("_status", status);
		
		try {
			String approversResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject approversResponseJson = new JSONObject(approversResponse);
			if (approversResponseJson.has(Constants.RECORDS)
					&& approversResponseJson.getJSONArray(Constants.RECORDS).length() > 0) {
				diagnostic.prepareInfo("approvers fetched successfully").log();
				JSONArray approversArray = approversResponseJson.getJSONArray(Constants.RECORDS);
				List<String> approvers = new ArrayList<String>();
				for(int i=0;i<approversArray.length();i++) {
					String FullName = approversArray.getJSONObject(i).getString(Constants.FirstName) + " " + approversArray.getJSONObject(i).getString(Constants.LastName);
					approvers.add(FullName);
				}				
				return approvers;
			} else {
				alert.prepareError("Unable to fetch approvers list: with approversResponse: "+approversResponse).log();
				return null;
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch approvers list ",e).log();
			
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getRequestActedApproversList method: ",e).log();
			
			return null;
		}
	}
}
