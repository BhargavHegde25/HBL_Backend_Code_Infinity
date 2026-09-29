package com.temenos.dbx.product.approvalmatrixservices.businessdelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.LegalEntityUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixStatusDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.CustomerApprovalMatrixDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.SignatoryGroupMatrixDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.CustomerActionDTO;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.ApprovalModeBusinessDelegate;
import com.temenos.dbx.product.utils.DTOMappings;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;


/**
 * 
 * @author KH2387
 * @version 1.0 Implements the {@link ApprovalMatrixBusinessDelegate}
 */
@SuppressWarnings("deprecation")
public class ApprovalMatrixBusinessDelegateImpl implements ApprovalMatrixBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Boolean createDefaultApprovalMatrixEntry(String contractId, String accountIds, String[] actionIds, String cif, String legalEntityId) {

		/*
		 * Code changes for DBB-9614
		 */
		String legalEntityCurrency = null;
		try{
			legalEntityCurrency = LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);
//			if (StringUtils.isEmpty(legaEntityCurrency)) {
//				alert.prepareError("Entity Currency cannot be empty!").log();
//				return false;
//			}
		} catch (ApplicationException ae) {
			alert.prepareError(ae.getMessage()).log();
//			return false;
		}


		try {
			ApprovalModeBusinessDelegate approvalModeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalModeBusinessDelegate.class);
			boolean isGroupRule = approvalModeBusinessDelegate.isGroupLevel(cif);
			int approvalMode = isGroupRule ? 1 : 0;
			
			//Fetch approvalmatrix entries for given cif
			List<ApprovalMatrixDTO> approvalMatrixDTOs = fetchApprovalMatrix(contractId, cif, "", "", "");
			if(approvalMatrixDTOs == null) {
				alert.prepareError("Failed to fetch Approval Matrix").log();
				return false;
			}
			
			//Fetch approvalmatrixtemplate entries for given cif
			List<ApprovalMatrixDTO> approvalMatrixtemplate = fetchApprovalMatrixTemplate(contractId, cif, "", "");
			if(approvalMatrixtemplate == null) {
				alert.prepareError("Failed to fetch Approval Matrix Template").log();
				return false;
			}
			
			if(approvalMatrixtemplate.isEmpty()) {
				if(approvalMatrixDTOs.isEmpty()) {
					
					boolean isGroupLevel;
		            String configParamValue = null;       
		            configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_MODE_DEFAULT_SIGN_GROUP);
		            
		            if(configParamValue == null || "false".equalsIgnoreCase(configParamValue)) {
		                isGroupLevel = false;
		                approvalMode = 0;
		            }
		            else {
		                isGroupLevel = true;
		                approvalMode = 1;
		            }
		           
		            createApprovalMode(cif, contractId, isGroupLevel);
					
					List<String> elibilityList = new ArrayList<String>();
						
					String requiredEntityTypes = EnvironmentConfigurationsHandler.getValue(Constants.AM_REQUIRE_APPROVALS_FOR_ENTITY_TYPE);
					if(requiredEntityTypes != null && !requiredEntityTypes.isEmpty()) {
						elibilityList = Arrays.asList(requiredEntityTypes.split("\\s*,\\s*"));
					}
						
					ContractBackendDelegate contractBD = DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
					ContractDTO contractDTO = new ContractDTO();
					contractDTO.setId(contractId);
					
					contractDTO = contractBD.getContractDetails(contractDTO, null);
					if(contractDTO == null) {
						disableApprovalMatrix(contractId, Arrays.asList(cif));
					}
					else {
						String serviceType = contractDTO.getServiceType();
						
						if(elibilityList != null && elibilityList.contains(serviceType)){
							// do not set anything into manageapprovalmatrix table
							enableApprovalMatrix(contractId, Arrays.asList(cif));
						} else{
							//set the cif into manageapprovalmatrix table.
							disableApprovalMatrix(contractId, Arrays.asList(cif));
						}
					}
				}
				
				//If template doesn't exist for given cif then create default entries in approval matrix
				return createApprovalMatrixTemplateDefaultEntry(contractId, actionIds, cif, approvalMode, legalEntityId);
			}
	
			//To find newly added actions
			Set<String> templateActions = approvalMatrixtemplate.stream().map(ApprovalMatrixDTO::getActionId).collect(Collectors.toSet());
			Set<String> newActionIds = new HashSet<String>();
			Set<String> existingRequestActionIds = new HashSet<String>();
			for(String action : actionIds) {
				if(!templateActions.contains(action)) {
					newActionIds.add(action);
				}
				else {
					existingRequestActionIds.add(action);
				}
			}
			
			if(!newActionIds.isEmpty()){
				String[] newActionIdsArray =  newActionIds.stream().toArray(String[]::new);
				//If new action exist, then add to approvalmatrixtemplate
				createApprovalMatrixTemplateDefaultEntry(contractId, newActionIdsArray, cif, approvalMode, legalEntityId);
			}
	
			
			if(approvalMatrixDTOs.isEmpty()){
				return true;
			}
	
			//To find newly added accounts
			Set<String> approvalMatrixAccounts = new HashSet<String>();
			for(ApprovalMatrixDTO matrixDTO : approvalMatrixDTOs) {
				if(existingRequestActionIds.contains(matrixDTO.getActionId())) {
					approvalMatrixAccounts.add(matrixDTO.getAccountId());
				}
			}
			
			String[] requestAccountIds = accountIds.split(",");
			Set<String> newAccountIds = new HashSet<String>();
			
			for(String accountId : requestAccountIds) {
				if(!approvalMatrixAccounts.contains(accountId)) {
					newAccountIds.add(accountId);
				}
			}
			
			if(!newAccountIds.isEmpty()){
				//If new accounts exist, then add to approvalmatrix
				
				//To get approvers for each matrix from flat structure response
				HashMap<String, ApprovalMatrixDTO> approversMap = new HashMap<String, ApprovalMatrixDTO>();
				for(ApprovalMatrixDTO templateMatrix :approvalMatrixtemplate) {
					if(existingRequestActionIds.contains(templateMatrix.getActionId())
					 && !((templateMatrix.getApprovalruleId() == null || Constants.NO_APPROVAL.equals(templateMatrix.getApprovalruleId()))
								&& "-1.00".equals(templateMatrix.getLowerlimit())
								&& "-1.00".equals(templateMatrix.getUpperlimit())
								)
					 ) {
						if(approversMap.containsKey(templateMatrix.getId())) {
							if("false".equals(templateMatrix.getIsGroupMatrix())) {
								ApprovalMatrixDTO matrixDTO = approversMap.get(templateMatrix.getId());
								String approverId = matrixDTO.getCustomerId() + ";" + templateMatrix.getCustomerId();
								matrixDTO.setCustomerId(approverId);
								approversMap.put(templateMatrix.getId(), matrixDTO);
							}
						}
						else {
							approversMap.put(templateMatrix.getId(), templateMatrix);
						}
					}
				}
				
				//If all the rules are default rules then no need to add to approval matrix
				if(approversMap.isEmpty()) {
					return true;
				}
				
				StringBuilder approvalmatrixValues = new StringBuilder();
				StringBuilder signatoryGroupMatrixValues = new StringBuilder();
				StringBuilder approvalIds = new StringBuilder();
				
				//To frame the request payload for approvalmatrix create
				for (Map.Entry<String,ApprovalMatrixDTO> templateMap : approversMap.entrySet()) {
					ApprovalMatrixDTO templateMatrix = templateMap.getValue();
					for(String account : newAccountIds) {
						approvalmatrixValues.append("\"").append(contractId).append("-")
						.append(templateMatrix.getActionId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(contractId).append("\"").append(";");
						approvalmatrixValues.append("\"").append(cif).append("\"").append(";");
						approvalmatrixValues.append("\"").append(templateMatrix.getActionId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(account).append("\"").append(";");
						approvalmatrixValues.append("\"").append(templateMatrix.getApprovalruleId()).append("\"").append(";");
						if("true".equals(templateMatrix.getIsGroupMatrix())) {
							approvalmatrixValues.append("\"").append("1").append("\"").append(";");
						}
//						else {
//							approvalmatrixValues.append("\"").append(templateMatrix.getApprovalruleId()).append("\"").append(";");
//						}
						
						approvalmatrixValues.append("\"").append(templateMatrix.getLimitTypeId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(templateMatrix.getLowerlimit()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(templateMatrix.getUpperlimit()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(legalEntityCurrency).append("\"").append(",");
						
						
						if("true".equals(templateMatrix.getIsGroupMatrix())) {
							signatoryGroupMatrixValues.append(templateMatrix.getGroupList()).append(";");
							signatoryGroupMatrixValues.append(templateMatrix.getGroupRule()).append("#");
						}
						else {
							if(! Constants.NO_APPROVAL.equalsIgnoreCase(templateMatrix.getApprovalruleId())) {   
								approvalIds.append(templateMatrix.getCustomerId());		
							}
							approvalIds.append(",");
						}
					}
				}
				
				if(approvalmatrixValues.length() > 0) {
					if(isGroupRule) {
						approvalmatrixValues.deleteCharAt(approvalmatrixValues.length() - 1);
						if(signatoryGroupMatrixValues.length() > 0) {
							signatoryGroupMatrixValues.deleteCharAt(signatoryGroupMatrixValues.length() - 1);
						}
						if(callCreateSignatoryApprovalMatrixStoredProc(approvalmatrixValues.toString(), signatoryGroupMatrixValues.toString())) {
							return true;
						}
						else {
							alert.prepareError("Failed to create signatory approval matrix").log();
							return false;
						}
					}
					else {
						approvalmatrixValues.deleteCharAt(approvalmatrixValues.length() - 1);
						if(approvalIds.length() > 0) {
							approvalIds.deleteCharAt(approvalIds.length() - 1);
						}
						if(callCreateApprovalMatrixStoredProc(approvalmatrixValues.toString(), approvalIds.toString())) {
							return true;
						}
						else {
							alert.prepareError("Failed to create approval matrix").log();
							return false;
						}
					}
				}
			}
			
			return true;
			
		} catch (Exception e) {
			alert.prepareError("Caught exception at CreateDefaultApprovalMatrixEntry method: " , e).log();
			return false;
		}
		
		
		/*
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_DEFAULT_CREATE;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		requestParameters.put("_contractId", contractId);
		requestParameters.put("_accountIds", accountIds);
		requestParameters.put("_cif", cif);
		
		String actionIdCSV = String.join(",", actionIds);
		requestParameters.put("_actionIds", actionIdCSV);
		try {
			String approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if (approvalMatrixResponseJson.has(Constants.RECORDS)
					&& approvalMatrixResponseJson.getJSONArray(Constants.RECORDS).getJSONObject(0).has(Constants.ACCOUNTLIST)) {
				diagnostic.prepareInfo("approval matrix record inserted successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to Create Default ApprovalMatrix Entry: ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to Create Default ApprovalMatrix Entry: " , e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at CreateDefaultApprovalMatrixEntry method: " , e).log();
			return false;
		}
		*/
	}

	@Override
	public Boolean deleteApprovalMatrixEntry(String contractId, String cif, Set<String> filterColumnIds, String filterColumnName) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_DEFAULT_DELETE;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		String filterColumnIdsCSV = String.join(",", filterColumnIds);
		requestParameters.put("_filterColumnIds", filterColumnIdsCSV);
		requestParameters.put("_filterColumnName", filterColumnName);

		try {
			String approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if (approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				diagnostic.prepareInfo("approval matrix record deleted successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to delete ApprovalMatrix Entry ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to delete ApprovalMatrix Entry " , e).log();
			return false;

		} catch (Exception e) {
			alert.prepareError("Caught exception at DeleteApprovalMatrixEntry method: " , e).log();
			return false;
		}
	}
	
	@Override
	public Boolean deleteApprovalMatrixForCifs(String contractId, Set<String> cifs, DataControllerRequest request) {
		
		ContractCoreCustomerBackendDelegate contractCoreCustomerBD = DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
		for(String cif : cifs) {
			try {
				
			Map<String, Set<String>> contractCoreCustomerDetailsMap = contractCoreCustomerBD.getCoreCustomerAccountsFeaturesActions(contractId, cif, request.getHeaderMap());

			Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
			customerActions = getActionWithApproveFeatureAction(customerActions, request.getHeaderMap());
			
			this.callApprovalMatrixTemplateCleanupProc(contractId, customerActions.toArray(new String[0]), cif, 
					Constants.MAX_TRANSACTION_LIMIT + ","+ Constants.DAILY_LIMIT +"," + Constants.WEEKLY_LIMIT + "," + Constants.NON_MONETARY_LIMIT);
			} catch (Exception e) {
				alert.prepareError(e.getMessage()).log();
			}
		}
		return deleteApprovalMatrixEntry(contractId,"", cifs, Constants.CIF);
	}
	
	@Override
	public Boolean deleteApprovalMatrixForAccounts(String contractId, String cif, Set<String> accounts) {
		return deleteApprovalMatrixEntry(contractId, cif, accounts, Constants.ACCOUNTID);
	}
	
	@Override
	public Boolean deleteApprovalMatrixFeatureForActions(String contractId, String cif, Set<String> actions) {		
		this.callApprovalMatrixTemplateCleanupProc(contractId, actions.toArray(new String[0]), cif,
				Constants.MAX_TRANSACTION_LIMIT + ","+ Constants.DAILY_LIMIT +"," + Constants.WEEKLY_LIMIT + "," + Constants.NON_MONETARY_LIMIT);
		return deleteApprovalMatrixEntry(contractId, cif, actions, Constants.ACTIONID);
	}
	
	@Override
	public Boolean deleteApprovalMatrixFeatureSet(String contractId, String cif, Set<String> features) {
		FeatureActionBusinessDelegate featureActionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
		Set<String> actionIdList= featureActionDelegate.getApprovalMatrixFeatureActions(features);
		this.callApprovalMatrixTemplateCleanupProc(contractId, actionIdList.toArray(new String[0]), cif,
				Constants.MAX_TRANSACTION_LIMIT + ","+ Constants.DAILY_LIMIT +"," + Constants.WEEKLY_LIMIT + "," + Constants.NON_MONETARY_LIMIT);
		if(!CollectionUtils.isEmpty(actionIdList))
			return deleteApprovalMatrixEntry(contractId, cif, actionIdList, Constants.ACTIONID);	
		return false;
	}
	
	@Override
	public List<ApprovalMatrixDTO> fetchApprovalMatrix(String contractId, String cif, String accountId, String actionId,
			String limitTypeId) {
			List<ApprovalMatrixDTO> approvalMatrix = new ArrayList<>();		

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_FETCH_RECORDS_PROC;
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		Set<String> actions = contractDelegate.fetchFeatureActions(contractId, cif);
		
		if(actions != null && StringUtils.isEmpty(actionId)) {
			actionId = String.join(",", actions);
		}
		else if (actions == null || !actions.contains(actionId))
		{
			return null;
		}
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		requestParameters.put("_accountId", accountId);
		requestParameters.put("_actions", actionId);
		requestParameters.put("_limitTypeId", limitTypeId);

		HashMap<String, Object> requestHeaders = new HashMap<>();

		String approvalMatrixResponse = null;
		JSONArray records = null;
		try {
			approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		if (approvalMatrixResponse.length()==0 || approvalMatrixResponse==null)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			JSONObject approvalMatrixJSON = new JSONObject(approvalMatrixResponse);
			records = approvalMatrixJSON.getJSONArray("records");
		}
		catch (JSONException e) {
			alert.prepareError("Error while parsing string JSON" , e).log();
			return null;
		} 
		if (records.length()==0)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			approvalMatrix = JSONUtils.parseAsList(records.toString(), ApprovalMatrixDTO.class);
			return approvalMatrix;
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;

		}
	}
	
	@Override
	public List<ApprovalMatrixDTO> fetchApprovalMatrixSignatory(String contractId, String cif, String accountId, String actionId,
			String limitTypeId) {
			List<ApprovalMatrixDTO> approvalMatrix = new ArrayList<>();		

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_FETCH_SIGNATORYRECORDS_PROC;
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		Set<String> actions = contractDelegate.fetchFeatureActions(contractId, cif);

		if(actions != null && StringUtils.isEmpty(actionId)) {
			actionId = String.join(",", actions);
		}
		else if (actions == null || !actions.contains(actionId))
		{
			return null;
		}
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		requestParameters.put("_accountId", accountId);
		requestParameters.put("_actions", actionId);
		requestParameters.put("_limitTypeId", limitTypeId);

		HashMap<String, Object> requestHeaders = new HashMap<>();

		String approvalMatrixResponse = null;
		JSONArray records = null;
		try { 
			approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, ""); 
		} catch (Exception e) { 
			alert.prepareError("Error while invoking DBService" , e).log();
			return null; 
		}

		if (approvalMatrixResponse.length()==0 || approvalMatrixResponse==null)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			JSONObject approvalMatrixJSON = new JSONObject(approvalMatrixResponse);
			records = approvalMatrixJSON.getJSONArray("records");
		}
		catch (JSONException e) {
			alert.prepareError("Error while parsing string JSON" , e).log();
			return null;
		} 
		if (records.length()==0)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			approvalMatrix = JSONUtils.parseAsList(records.toString(), ApprovalMatrixDTO.class);
			return approvalMatrix;
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;

		}
	}
	
	@Override
	public List<String> getCifList(String contractId){
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CONTRACTCORECUSTOMERS_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "contractId" + DBPUtilitiesConstants.EQUAL + contractId;
				
        requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		List<String> cifList = new ArrayList<String>();
		String response = null;
		
		try {
			response =  DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseJson = new JSONObject(response);
			JSONArray jsonArray = responseJson.optJSONArray("contractcorecustomers");
			if (jsonArray != null) { 
				for(Object obj: jsonArray) {
					JSONObject record = (JSONObject) obj;
					if(record.has("coreCustomerId")) {
						cifList.add(record.getString("coreCustomerId"));
					}
				} 
				return cifList;
				}     
        }
        catch (JSONException e) {
        	alert.prepareError("Unable to disable ApprovalMatrix record " , e).log();
        }
        catch (Exception e) {
        	alert.prepareError("Caught exception at _disableApprovalMatrix method: " , e).log();
        }
		return null;
	}
	
	@Override
	public boolean disableApprovalMatrix(String contractId, List<String> cifList) {
	
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_MANAGEAPPROVALMATRIX_UPDATE_PROC;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
        
		String cif=String.join(",", cifList);
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cifList",cif);
		requestParameters.put("_isDisabledflag", 1);

		String approvalMatrixResponse = null;
		
		try {
			approvalMatrixResponse =  DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if((approvalMatrixResponseJson != null) 
					&& approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				return true;
			}  
        }
        catch (JSONException e) {
        	alert.prepareError("Unable to disable ApprovalMatrix record " , e).log();		
        	return false;
        }
        catch (Exception e) {
        	alert.prepareError("Caught exception at _disableApprovalMatrix method: " , e).log();
        	return false;
        }
		return false;
	}
	@Override
	public boolean enableApprovalMatrix(String contractId, List<String> cifList) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_MANAGEAPPROVALMATRIX_UPDATE_PROC;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
        
		String cif=String.join(",", cifList);
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cifList",cif);
		requestParameters.put("_isDisabledflag", 0);

		String approvalMatrixResponse = null;

		try {
			approvalMatrixResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if((approvalMatrixResponseJson != null) 
					&& approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				return true;
			}    
        }
        catch (JSONException e) {
        	alert.prepareError("Unable to disable ApprovalMatrix record " , e).log();
        }
        catch (Exception e) {
        	alert.prepareError("Caught exception at _disableApprovalMatrix method: " , e).log();
        }
		return false;
	}
	
	@Override
	public List<ApprovalMatrixStatusDTO> fetchApprovalMatrixStatus(String contractId, List<String> cifList) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_MANAGEAPPROVALMATRIX_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
        String filter = "contractId" + DBPUtilitiesConstants.EQUAL + contractId;
        
		if(!CollectionUtils.isEmpty(cifList)) {
			filter += DBPUtilitiesConstants.AND + DBPUtilitiesConstants.OPEN_BRACE + "coreCustomerId" + DBPUtilitiesConstants.EQUAL +
					String.join(DBPUtilitiesConstants.OR + "coreCustomerId" + DBPUtilitiesConstants.EQUAL, cifList) + DBPUtilitiesConstants.CLOSE_BRACE;
		}
        requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		List<ApprovalMatrixStatusDTO> manageApprovalMatrixDTO = new ArrayList<ApprovalMatrixStatusDTO>();
		String approvalMatrixResponse=null;
		
		try {
			approvalMatrixResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(approvalMatrixResponseJson);
			manageApprovalMatrixDTO = JSONUtils.parseAsList(jsonArray.toString(), ApprovalMatrixStatusDTO.class);	    
			return manageApprovalMatrixDTO;
		}
        catch (JSONException e) {
        	alert.prepareError("Unable to disable ApprovalMatrix record " , e).log();
        }
        catch (Exception e) {
        	alert.prepareError("Caught exception at _disableApprovalMatrix method: " , e).log();
        }
		return null;
	}
	/*
	 * 
	 * updateApprovalMatrixEntry works only if the ApprovalMatrixDTO list has same companyId, actionId, accountId, limitTypeId
	 * 
	 */
	@Override
	public JSONObject updateApprovalMatrixEntry(List<ApprovalMatrixDTO> approvalMatrixDTOList, List<String> accountIds, String legalEntityId) {
		ApprovalMatrixDTO approvalMatrixDTOZero = approvalMatrixDTOList.get(0);
		JSONObject result = new JSONObject();

		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApproversBusinessDelegate.class);
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		
		String limitTypeId = approvalMatrixDTOZero.getLimitTypeId();
		
		if( accountIds == null || accountIds.size() == 0) {
			accountIds.add(approvalMatrixDTOZero.getAccountId());
		}
		List<String> approvers = approversBusinessDelegate.getAccountActionApproverList(approvalMatrixDTOZero.getContractId(), approvalMatrixDTOZero.getCifId(), String.join(",",accountIds), approvalMatrixDTOZero.getActionId());

		LimitsDTO companyLimitsDTO = contractDelegate.fetchLimits(approvalMatrixDTOZero.getContractId(), approvalMatrixDTOZero.getCifId(), approvalMatrixDTOZero.getActionId(), legalEntityId);
		
		if (companyLimitsDTO == null) {
			result.put("Success", false);
			result.put("ErrorCode", 5);
			return result;
		}
		
		if (CollectionUtils.isEmpty(approvers) && ! Constants.NO_APPROVAL.equalsIgnoreCase(approvalMatrixDTOZero.getApprovalruleId())) {
			result.put("Success", false);
			result.put("ErrorCode", 1);
			return result;
		} else {
			if (_checkIfValidApprovers(approvers, approvalMatrixDTOList)) {
				double lowerlimit[] ;
				double upperlimit[];
				Map<String, ArrayList<Double>> lowerLimitMap = new HashMap<>();
				Map<String, ArrayList<Double>> upperLimitMap = new HashMap<>();
				for (int i = 0; i < approvalMatrixDTOList.size(); i++) {
					ApprovalMatrixDTO amDTO = approvalMatrixDTOList.get(i);
					String accountId = amDTO.getAccountId();
					if( lowerLimitMap.containsKey(accountId) ) {
						lowerLimitMap.get(accountId).add(Double.parseDouble(amDTO.getLowerlimit()));
					}
					else {
						ArrayList<Double> lowerLimit = new ArrayList<>();
						lowerLimit.add(Double.parseDouble(amDTO.getLowerlimit()));
						lowerLimitMap.put(accountId, lowerLimit);
					}
					if( upperLimitMap.containsKey(accountId) ) {
						upperLimitMap.get(accountId).add(Double.parseDouble(amDTO.getUpperlimit()));
					}
					else {
						ArrayList<Double> upperLimit = new ArrayList<>();
						upperLimit.add(Double.parseDouble(amDTO.getUpperlimit()));
						upperLimitMap.put(accountId, upperLimit);
					}					
				}
				
				ArrayList<Double> lowerLimitList = lowerLimitMap.get(approvalMatrixDTOList.get(0).getAccountId());
				ArrayList<Double> upperLimitList = upperLimitMap.get(approvalMatrixDTOList.get(0).getAccountId());
				lowerlimit = new double[lowerLimitList.size()];
				upperlimit = new double[upperLimitList.size()];
				for( int i = 0; i < lowerLimitList.size(); i++ ) {
					lowerlimit[i] = lowerLimitList.get(i);
					upperlimit[i] = upperLimitList.get(i);
				}
				
				if (_isMutuallyExclusiveAndExhaustive(lowerlimit, upperlimit, limitTypeId, companyLimitsDTO)) {
					_approvalMatrixUpdateSoftdeleteFlag(approvalMatrixDTOList,String.join(",", accountIds));

					StringBuilder approvalmatrixValues = new StringBuilder();
					StringBuilder approvalIds = new StringBuilder();

					for (int i = 0; i < approvalMatrixDTOList.size(); i++) {
						ApprovalMatrixDTO temp = approvalMatrixDTOList.get(i);
						// name,contractId,cif,actionId,accountId,approvalruleId,limitTypeId,lowerlimit,upperlimit
						approvalmatrixValues.append("\"").append(temp.getContractId()).append("-")
								.append(temp.getActionId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getContractId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getCifId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getActionId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getAccountId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getApprovalruleId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getLimitTypeId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getLowerlimit()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getUpperlimit()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getCurrency()).append("\"").append(",");

						if(! Constants.NO_APPROVAL.equalsIgnoreCase(temp.getApprovalruleId())) {   
							List<CustomerApprovalMatrixDTO> temp2 = temp.getCustomerApprovalMatrixDTO();
							for (int j = 0; j < temp2.size(); j++) {
								approvalIds.append(temp2.get(j).getCustomerId()).append(";");
							}
							approvalIds.deleteCharAt(approvalIds.length() - 1);			
						}
						approvalIds.append(",");
					}
					approvalmatrixValues.deleteCharAt(approvalmatrixValues.length() - 1);
					if(approvalIds.length() > 0) {
						approvalIds.deleteCharAt(approvalIds.length() - 1);
					}
					if(callCreateApprovalMatrixStoredProc(approvalmatrixValues.toString(), approvalIds.toString())) {
						result.put("Success", true);
						return result;
					}
					else {
						result.put("Success", false);
						result.put("ErrorCode", 2);
						return result;
					}
					
				}
				else {					
					result.put("Success", false);
					result.put("ErrorCode", 3);
					return result;
				}
				
			} else {
				result.put("Success", false);
				result.put("ErrorCode", 4);
				return result;
			}
		}
	}
	
	/*
	 * 
	 * updateApprovalMatrixEntry works only if the ApprovalMatrixDTO list has same companyId, actionId, accountId, limitTypeId
	 * 
	 */
	@Override
	public JSONObject updateApprovalSignatoryMatrixEntry(List<ApprovalMatrixDTO> approvalMatrixDTOList, List<String> accountIds, String legalEntityId) {
		ApprovalMatrixDTO approvalMatrixDTOZero = approvalMatrixDTOList.get(0);
		JSONObject result = new JSONObject();

		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApproversBusinessDelegate.class);
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		
		String limitTypeId = approvalMatrixDTOZero.getLimitTypeId();
		
		if( accountIds == null || accountIds.size() == 0) {
			accountIds.add(approvalMatrixDTOZero.getAccountId());
		}
//		List<String> approvers = approversBusinessDelegate.getAccountActionApproverList(approvalMatrixDTOZero.getContractId(), approvalMatrixDTOZero.getCifId(), String.join(",",accountIds), approvalMatrixDTOZero.getActionId());

		LimitsDTO companyLimitsDTO = contractDelegate.fetchLimits(approvalMatrixDTOZero.getContractId(), approvalMatrixDTOZero.getCifId(), approvalMatrixDTOZero.getActionId(), legalEntityId);
		
		if (companyLimitsDTO == null) {
			result.put("Success", false);
			result.put("ErrorCode", 5);
			return result;
		}
				double lowerlimit[] ;
				double upperlimit[];
				Map<String, ArrayList<Double>> lowerLimitMap = new HashMap<>();
				Map<String, ArrayList<Double>> upperLimitMap = new HashMap<>();
				for (int i = 0; i < approvalMatrixDTOList.size(); i++) {
					ApprovalMatrixDTO amDTO = approvalMatrixDTOList.get(i);
					String accountId = amDTO.getAccountId();
					if( lowerLimitMap.containsKey(accountId) ) {
						lowerLimitMap.get(accountId).add(Double.parseDouble(amDTO.getLowerlimit()));
					}
					else {
						ArrayList<Double> lowerLimit = new ArrayList<>();
						lowerLimit.add(Double.parseDouble(amDTO.getLowerlimit()));
						lowerLimitMap.put(accountId, lowerLimit);
					}
					if( upperLimitMap.containsKey(accountId) ) {
						upperLimitMap.get(accountId).add(Double.parseDouble(amDTO.getUpperlimit()));
					}
					else {
						ArrayList<Double> upperLimit = new ArrayList<>();
						upperLimit.add(Double.parseDouble(amDTO.getUpperlimit()));
						upperLimitMap.put(accountId, upperLimit);
					}					
				}
				
				ArrayList<Double> lowerLimitList = lowerLimitMap.get(approvalMatrixDTOList.get(0).getAccountId());
				ArrayList<Double> upperLimitList = upperLimitMap.get(approvalMatrixDTOList.get(0).getAccountId());
				lowerlimit = new double[lowerLimitList.size()];
				upperlimit = new double[upperLimitList.size()];
				for( int i = 0; i < lowerLimitList.size(); i++ ) {
					lowerlimit[i] = lowerLimitList.get(i);
					upperlimit[i] = upperLimitList.get(i);
				}
				
				if (_isMutuallyExclusiveAndExhaustive(lowerlimit, upperlimit, limitTypeId, companyLimitsDTO)) {
					_approvalMatrixUpdateSoftdeleteFlag(approvalMatrixDTOList,String.join(",", accountIds));

					StringBuilder approvalmatrixValues = new StringBuilder();
					StringBuilder signatoryGroupMatrixValues = new StringBuilder();
					ApprovalMatrixDTO temp = null;
					for (int i = 0; i < approvalMatrixDTOList.size(); i++) {
						 temp = approvalMatrixDTOList.get(i);
						 
						 String approvalRule = temp.getApprovalruleId()!= null && !StringUtils.isEmpty(temp.getApprovalruleId())? "\""+temp.getApprovalruleId()+"\"" : null;
						// name,contractId,cif,actionId,accountId,approvalruleId,limitTypeId,lowerlimit,upperlimit
						approvalmatrixValues.append("\"").append(temp.getContractId()).append("-")
								.append(temp.getActionId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getContractId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getCifId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getActionId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getAccountId()).append("\"").append(";");
						approvalmatrixValues.append(approvalRule).append(";");
						approvalmatrixValues.append("\"").append(temp.getIsGroupMatrix()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getLimitTypeId()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getLowerlimit()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getUpperlimit()).append("\"").append(";");
						approvalmatrixValues.append("\"").append(temp.getCurrency()).append("\"").append(",");


						SignatoryGroupMatrixDTO temp2 = temp.getSignatoryGroupMatrixDTO();
						signatoryGroupMatrixValues.append(temp2.getGroupList()).append(";");
						signatoryGroupMatrixValues.append(temp2.getGroupRule()).append("#");
   					
					}
					
					approvalmatrixValues.deleteCharAt(approvalmatrixValues.length() - 1);
					signatoryGroupMatrixValues.deleteCharAt(signatoryGroupMatrixValues.length() - 1);
					if(callCreateSignatoryApprovalMatrixStoredProc(approvalmatrixValues.toString(), signatoryGroupMatrixValues.toString())) {
						result.put("Success", true);
						return result;
					}
					else {
						result.put("Success", false);
						result.put("ErrorCode", 2);
						return result;
					}
					
				}
				else {					
					result.put("Success", false);
					result.put("ErrorCode", 3);
					return result;
				}
				
			
		
	}

	/**
	 * method to verify if given approvers are valid
	 * 
	 * @param List<String> approvers
	 * 
	 * @param List<ApprovalMatrixDTO> approvalMatrixDTOList
	 * 
	 * @return Boolean
	 */
	private Boolean _checkIfValidApprovers(List<String> approvers, List<ApprovalMatrixDTO> approvalMatrixDTOList) {		
		Boolean flag = true;
				
		for(int i=0; i<approvalMatrixDTOList.size();i++) {
			List<CustomerApprovalMatrixDTO> customerApprovalMatrixDTO = approvalMatrixDTOList.get(i).getCustomerApprovalMatrixDTO();
			for(int j=0;j<customerApprovalMatrixDTO.size();j++) {
				if(Constants.NO_APPROVAL.equalsIgnoreCase(approvalMatrixDTOList.get(i).getApprovalruleId())) {
					continue;
				}
				else if(approvers.contains(customerApprovalMatrixDTO.get(j).getCustomerId())) {
					continue;
				}
				else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * method to invoke CreateApprovalMatrixStoredProc
	 * 
	 * @param String approvalmatrixValues
	 * 
	 * @param String approvalIds
	 *            
	 * @return Boolean
	 */
	@Override
	public Boolean callCreateApprovalMatrixStoredProc(String approvalmatrixValues, String approvalIds) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_CREATE;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		requestParameters.put("_matrixValues", approvalmatrixValues);
		requestParameters.put("_approverIds", approvalIds);

		try {
			String approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if (approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				diagnostic.prepareInfo("approval matrix record updated successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to Update ApprovalMatrix Entry: ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to Update ApprovalMatrix Entry: " , e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at _callCreateApprovalMatrixStoredProc method: " , e).log();
			return false;
		}

	}
	
	/**
	 * method to invoke CreateApprovalMatrixStoredProc
	 * 
	 * @param String approvalmatrixValues
	 * 
	 * @param String approvalIds
	 *            
	 * @return Boolean
	 */
	@Override
	public Boolean callCreateSignatoryApprovalMatrixStoredProc(String approvalmatrixValues, String approvalIds) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_SIGNATORY_APPROVALMATRIX_CREATE;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		requestParameters.put("_matrixValues", approvalmatrixValues);
		requestParameters.put("_signatorymatrixValues", approvalIds);

		try {
			String approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if (approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				diagnostic.prepareInfo("approval matrix record updated successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to Update ApprovalMatrix Entry: ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to Update ApprovalMatrix Entry: " , e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at _callCreateApprovalMatrixStoredProc method: " , e).log();
			return false;
		}

	}

	/**
	 * method to softdelete ApprovalMatrixRecord
	 * 
	 * @param {@link ApprovalMatrixDTO}
	 *             
	 * @return Boolean
	 */
	protected Boolean _approvalMatrixUpdateSoftdeleteFlag(List<ApprovalMatrixDTO> approvalMatrixDTOList, String accountIds) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_SOFTDELETE;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();
		
		if(CollectionUtils.isEmpty(approvalMatrixDTOList) ) {
			diagnostic.prepareInfo("approvalMatrixDTOList is empty").log();
			return false;
		}
		ApprovalMatrixDTO approvalMatrixDTO = approvalMatrixDTOList.get(0);	
		requestParameters.put("_contractId", approvalMatrixDTO.getContractId());
		requestParameters.put("_cif", approvalMatrixDTO.getCifId());
		requestParameters.put("_accountIds", accountIds);
		requestParameters.put("_actionId", approvalMatrixDTO.getActionId());
		requestParameters.put("_limitTypeId", approvalMatrixDTO.getLimitTypeId());

		try {
			String approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if (approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				diagnostic.prepareInfo("approval matrix record softdeleted successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to softdelete ApprovalMatrix Entry ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to softdelete ApprovalMatrix Entry " , e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at _approvalMatrixUpdateSoftdeleteFlag method: " , e).log();
			return false;
		}
	}

	/**
	 * method to verify if limits are mutually exclusive and exhaustive
	 * @param lowerlimit
	 * @param upperlimit
	 * @param limitTypeId
	 * @param companyLimitsDTO
	 * @return
	 */
	protected Boolean _isMutuallyExclusiveAndExhaustive(double[] lowerlimit, double[] upperlimit, String limitTypeId, LimitsDTO companyLimitsDTO) {
		int len = lowerlimit.length, count = 0, i = 0, deleteValue = -100;
		double maxLimit = 0;
		
		switch(limitTypeId) {
		
			case Constants.MAX_TRANSACTION_LIMIT:
				maxLimit = companyLimitsDTO.getMaxTransactionLimit();
				break;
			case Constants.DAILY_LIMIT:
				maxLimit = companyLimitsDTO.getDailyLimit();
				break;
			case Constants.WEEKLY_LIMIT:
				maxLimit = companyLimitsDTO.getWeeklyLimit();
				break;
			default:
				break;
	
		}
		double currentValue = 0;

		while (count < len && i < len) {

			if (upperlimit[i] != -1 && lowerlimit[i] > upperlimit[i]) {
				break;
			}

			if ((count == 0 && (lowerlimit[i] == 0 || lowerlimit[i] == -1)) || lowerlimit[i] == currentValue) {
				currentValue = upperlimit[i];
				lowerlimit[i] = deleteValue;
				upperlimit[i] = deleteValue;
				i = -1;
				count++;
			}
			i++;
		}
		if (count == len && (currentValue == maxLimit || currentValue == -1)) {
			return true;
		} else {
			return false;
		}

	}
	
	@Override
	public List<ApprovalMatrixDTO> fetchApprovalMatrixEntry(String companyId, String accountId, String actionId) {
		
		List<ApprovalMatrixDTO> approvalMatrix = new ArrayList<>();		

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_GET;
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		String filter = "companyId" + DBPUtilitiesConstants.EQUAL + companyId + DBPUtilitiesConstants.AND +
				"accountId" + DBPUtilitiesConstants.EQUAL + accountId + DBPUtilitiesConstants.AND +
				"actionId" + DBPUtilitiesConstants.EQUAL + actionId + DBPUtilitiesConstants.AND +
				"softdeleteflag" + DBPUtilitiesConstants.EQUAL + 0 ;
				
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String approvalMatrixResponse = null;
		JSONArray records = null;
		try {
			approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, null, "");
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		
		if (approvalMatrixResponse.length()==0 || approvalMatrixResponse==null)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			JSONObject approvalMatrixJSON = new JSONObject(approvalMatrixResponse);
			records = approvalMatrixJSON.getJSONArray("approvalmatrix");
		}
		catch (JSONException e) {
			alert.prepareError("Error while parsing string JSON" , e).log();
			return null;
		} 
		
		if (records.length()==0)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			approvalMatrix = JSONUtils.parseAsList(records.toString(), ApprovalMatrixDTO.class);
			return approvalMatrix;
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;

		}
	}

	@Override
	public List<String> fetchApproverIds(String matrixId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMERAPPROVALMATRIX_GET;
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		String filter = "approvalMatrixId" + DBPUtilitiesConstants.EQUAL + matrixId + DBPUtilitiesConstants.AND +
				"softdeleteflag" + DBPUtilitiesConstants.EQUAL + 0 ;
				
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String response = null;
		JSONArray records = null;
		try {
			response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, null, "");
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		
		if (response.length()==0 || response==null)
		{
			alert.prepareError("DB Service returned no records").log();
			return null;
		}

		try {
			JSONObject approvalMatrixJSON = new JSONObject(response);
			records = approvalMatrixJSON.getJSONArray("customerapprovalmatrix");
		}
		catch (JSONException e) {
			alert.prepareError("Error while parsing string JSON" , e).log();
			return null;
		} 
		
		if (records.length()==0)
		{
			alert.prepareError("DB Service returned no records").log();
			return null;
		}

		try {
			List<String> approverIds = new ArrayList<String>();
			for(Object obj: records) {
				JSONObject record = (JSONObject) obj;
				if(record.has("customerId")) {
					approverIds.add(record.getString("customerId"));
				}
			}
			
			//if(approverIds.size() > 0) {
			return approverIds;
			//}	
		} 
		catch (JSONException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;

		}
	}

	@Override
	public SignatoryGroupMatrixDTO fetchSignatoryGroupMatrix(String matrixId) {
		SignatoryGroupMatrixDTO approvalMatrixDTO;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_SIGNATORYGROUPMATRIX_GET;
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		String filter = "approvalMatrixId" + DBPUtilitiesConstants.EQUAL + matrixId;
				
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            approvalMatrixDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), SignatoryGroupMatrixDTO.class);
            
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		
		return approvalMatrixDTO;
	}

	@Override
	public List<String> fetchUserOfGroupList(String groupList) {
		 String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		 String operationName = OperationName.DB_GET_SIGNATORYGROUP_APPROVERS_PROC;
		 List<String> groupRule = new ArrayList<>();
		 
		 if(groupList == null || StringUtils.isEmpty(groupList)) {
			 return null;
		 }
		 
		 groupList = groupList.substring(1, groupList.length()-1);
		 HashMap<String, Object> requestParameters = new HashMap<>();
		 requestParameters.put("_groupList", groupList);
	
		 String response = null;
		 try {
			 response = DBPServiceExecutorBuilder.builder().
					 withServiceId(serviceName).
					 withObjectId(null).
					 withOperationId(operationName).
					 withRequestParameters(requestParameters).
					 build().getResponse();

			 JSONObject responseObj = new JSONObject(response);
			 JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);

			 if (jsonArray.length()==0)
			 {
				 alert.prepareError("DB Service returned no records").log();
				 return null;
			 }

			 try {
				 for(Object obj: jsonArray) {
					 JSONObject record = (JSONObject) obj;
					 if(record.has("customerId")) {
						 groupRule.add(record.getString("customerId"));
					 }
				 }
			 } 
			 catch (JSONException e) {
				 alert.prepareError("Caught exception while parsing list: " , e).log();
				 return null;

			 }

		 }
		 catch (Exception e) {
			 alert.prepareError("Error while invoking DBService" , e).log();
			 return null;
		 }
	
	       
		return groupRule;
	}

	@Override
	public List<ApprovalMatrixDTO> fetchApprovalMatrixTemplate(String contractId, String cif, String actionId,
			String limitTypeId) {
		List<ApprovalMatrixDTO> approvalMatrix = new ArrayList<>();		

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_APPROVALMATRIXTEMPLATE_PROC;
		
		if(StringUtils.isEmpty(actionId))
		{
			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
			Set<String> actions = contractDelegate.fetchFeatureActions(contractId, cif);
			
			if(actions != null) {
				actionId = String.join(",", actions);
			}
		} 
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		requestParameters.put("_actions", actionId);
		requestParameters.put("_limitTypeId", limitTypeId);

		HashMap<String, Object> requestHeaders = new HashMap<>();

		String approvalMatrixResponse = null;
		JSONArray records = null;
		
		  try { 
			  approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					  	operationName, requestParameters, requestHeaders, ""); 
		  } catch (Exception e) { 
			  alert.prepareError("Error while invoking DBService" , e).log(); return null; 
		  }
		 
		if (approvalMatrixResponse.length()==0 || approvalMatrixResponse==null)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			JSONObject approvalMatrixJSON = new JSONObject(approvalMatrixResponse);
			records = approvalMatrixJSON.getJSONArray("records");
		}
		catch (JSONException e) {
			alert.prepareError("Error while parsing string JSON" , e).log();
			return null;
		} 
		if (records.length()==0)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			approvalMatrix = JSONUtils.parseAsList(records.toString(), ApprovalMatrixDTO.class);
			return approvalMatrix;
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;

		}
	}

	@Override
	public List<String> fetchApproverIdsFromTemplate(String matrixId,DataControllerRequest dcr) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMERAPPROVALMATRIXTEMPLATE_GET;
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		String filter = "approvalMatrixId" + DBPUtilitiesConstants.EQUAL + matrixId + DBPUtilitiesConstants.AND +
				"softdeleteflag" + DBPUtilitiesConstants.EQUAL + 0 ;
				
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String response = null;
		JSONArray records = null;
		try {
			//response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,operationName, requestParameters, null, "");
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		
		if (response.length()==0 || response==null)
		{
			alert.prepareError("DB Service returned no records").log();
			return null;
		}

		try {
			JSONObject approvalMatrixJSON = new JSONObject(response);
			records = approvalMatrixJSON.getJSONArray("customerapprovalmatrixtemplate");
		}
		catch (JSONException e) {
			alert.prepareError("Error while parsing string JSON" , e).log();
			return null;
		} 
		
		if (records.length()==0)
		{
			alert.prepareError("DB Service returned no records").log();
			return null;
		}

		try {
			List<String> approverIds = new ArrayList<String>();
			for(Object obj: records) {
				JSONObject record = (JSONObject) obj;
				if(record.has("customerId")) {
					approverIds.add(record.getString("customerId"));
				}
			}
			
			//if(approverIds.size() > 0) {
			return approverIds;
			//}	
		} 
		catch (JSONException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;

		}
	}

	@Override
	public SignatoryGroupMatrixDTO fetchSignatoryGroupMatrixFromTemplate(String matrixId) {
		SignatoryGroupMatrixDTO approvalMatrixDTO;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_SIGNATORYGROUPMATRIXTEMPLATE_GET;
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		String filter = "approvalMatrixId" + DBPUtilitiesConstants.EQUAL + matrixId;
				
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            approvalMatrixDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), SignatoryGroupMatrixDTO.class);
            
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		
		return approvalMatrixDTO;
	}	
	
	@Override
	public Boolean callApprovalMatrixTemplateCleanupProc(String contractId, String[] actionIds, String cif, String limitTypeIds) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIXTEMPLATE_CLEANUP_PROC;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		String actionIdCSV = "";
		if(actionIds != null && actionIds.length >0) {
			actionIdCSV = String.join(",", actionIds);
		}
		
		requestParameters.put("_actionIds", actionIdCSV);
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		requestParameters.put("_limitTypeId", limitTypeIds);
		
		try {
			String templateResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(templateResponse);
			if (approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				diagnostic.prepareInfo("cleanup was successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to cleanup ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to cleanup " , e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at callApprovalMatrixTemplateCleanupProc method: " , e).log();
			return false;
		}
	}
	
	@Override
	public Boolean createApprovalMatrixTemplateDefaultEntry(String contractId, String[] actionIds, String cif, int approvalMode, String legalEntityId) {

		// TODO: get currency from FIOS (based on cif/contract/acc) (mostly one time operation)
		String currency = null;

		try {
			if(legalEntityId != null) {
				currency = LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);
			}
//			if (StringUtils.isEmpty(currency)) {
//				alert.prepareError("Entity Currency cannot be empty!").log();
//				return false;
//			}
		} catch (ApplicationException ae) {
			alert.prepareError(ae.getMessage()).log();
//			return false;
		}

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIXTEMPLATE_DEFAULT_CREATE_PROC;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();
		
		String actionIdCSV = "";
		if(actionIds != null && actionIds.length >0) {
			actionIdCSV = String.join(",", actionIds);
		}

		
		requestParameters.put("_actionIds", actionIdCSV);
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		requestParameters.put("_approvalMode", approvalMode);
		requestParameters.put("_currency", currency);
		
		try {
			String approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if (approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				diagnostic.prepareInfo("templaterecord inserted successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to Create No Approval Template Entry: ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to Create No Approval Template Entry: " , e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createNoApprovalTemplateEntry method: " , e).log();
			return false;
		}
	}
	
	@Override
	public JSONObject updateApprovalSignatoryMatrixTemplateEntry(List<ApprovalMatrixDTO> approvalMatrixTemplateList, int approvalMode) {
		
		JSONObject result = new JSONObject();

		StringBuilder approvalmatrixTemplateValues = new StringBuilder();
		StringBuilder signatoryGroupMatrixTemplateValues = new StringBuilder();
		ApprovalMatrixDTO temp = null;
		for (int i = 0; i < approvalMatrixTemplateList.size(); i++) {
			temp = approvalMatrixTemplateList.get(i);
			String approvalRule = temp.getApprovalruleId()!= null && !StringUtils.isEmpty(temp.getApprovalruleId())? "\""+temp.getApprovalruleId()+"\"" : null;
			// contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit
			approvalmatrixTemplateValues.append("\"").append(temp.getContractId()).append("\"").append(";");
			approvalmatrixTemplateValues.append("\"").append(temp.getCifId()).append("\"").append(";");
			approvalmatrixTemplateValues.append("\"").append(temp.getActionId()).append("\"").append(";");
			approvalmatrixTemplateValues.append(approvalRule).append(";");
			approvalmatrixTemplateValues.append("\"").append(temp.getLimitTypeId()).append("\"").append(";");
			approvalmatrixTemplateValues.append("\"").append(temp.getLowerlimit()).append("\"").append(";");
			approvalmatrixTemplateValues.append("\"").append(temp.getUpperlimit()).append("\"").append(";");
			approvalmatrixTemplateValues.append("\"").append(temp.getCurrency()).append("\"").append(";");
			approvalmatrixTemplateValues.append("\"").append(temp.getIsGroupMatrix()).append("\"").append(",");

			

			SignatoryGroupMatrixDTO temp2 = temp.getSignatoryGroupMatrixDTO();
			signatoryGroupMatrixTemplateValues.append(temp2.getGroupList()).append(";");
			signatoryGroupMatrixTemplateValues.append(temp2.getGroupRule()).append("#");

		}

		approvalmatrixTemplateValues.deleteCharAt(approvalmatrixTemplateValues.length() - 1);
		signatoryGroupMatrixTemplateValues.deleteCharAt(signatoryGroupMatrixTemplateValues.length() - 1);
		if (callCreateApprovalMatrixTemplateStoredProc(approvalmatrixTemplateValues.toString(), signatoryGroupMatrixTemplateValues.toString(), approvalMode)) {
			result.put("Success", true);
			return result;
		} else {
			result.put("Success", false);
			result.put("ErrorCode", 1);
			return result;
		}

	}
	
	@Override
	public JSONObject updateApprovalMatrixTemplateEntry(List<ApprovalMatrixDTO> approvalMatrixTemplateList, List<String> accountIds, int approvalMode) {
		ApprovalMatrixDTO approvalMatrixDTOZero = approvalMatrixTemplateList.get(0);
		JSONObject result = new JSONObject();

		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApproversBusinessDelegate.class);

		List<String> approvers = approversBusinessDelegate.getAccountActionApproverList(approvalMatrixDTOZero.getContractId(), approvalMatrixDTOZero.getCifId(), String.join(",", accountIds),
				approvalMatrixDTOZero.getActionId());

		if (CollectionUtils.isEmpty(approvers) && !Constants.NO_APPROVAL.equalsIgnoreCase(approvalMatrixDTOZero.getApprovalruleId())) {
			result.put("Success", false);
			result.put("ErrorCode", 2);
			return result;
		} else {

			StringBuilder approvalmatrixTemplateValues = new StringBuilder();
			StringBuilder approvalIds = new StringBuilder();

			for (int i = 0; i < approvalMatrixTemplateList.size(); i++) {
				ApprovalMatrixDTO temp = approvalMatrixTemplateList.get(i);
				// contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit
				approvalmatrixTemplateValues.append("\"").append(temp.getContractId()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getCifId()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getActionId()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getApprovalruleId()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getLimitTypeId()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getLowerlimit()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getUpperlimit()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getCurrency()).append("\"").append(";");
				approvalmatrixTemplateValues.append("\"").append(temp.getIsGroupMatrix()).append("\"").append(",");

				if (!Constants.NO_APPROVAL.equalsIgnoreCase(temp.getApprovalruleId())) {
					List<CustomerApprovalMatrixDTO> temp2 = temp.getCustomerApprovalMatrixDTO();
					for (int j = 0; j < temp2.size(); j++) {
						approvalIds.append(temp2.get(j).getCustomerId()).append(";");
					}
					approvalIds.deleteCharAt(approvalIds.length() - 1);
				}
				approvalIds.append(",");
			}
			approvalmatrixTemplateValues.deleteCharAt(approvalmatrixTemplateValues.length() - 1);
			if (approvalIds.length() > 0) {
				approvalIds.deleteCharAt(approvalIds.length() - 1);
			}
			if (callCreateApprovalMatrixTemplateStoredProc(approvalmatrixTemplateValues.toString(), approvalIds.toString(), approvalMode)) {
				result.put("Success", true);
				return result;
			} else {
				result.put("Success", false);
				result.put("ErrorCode", 1);
				return result;
			}

		}

	}
	
	@Override
	public Boolean callCreateApprovalMatrixTemplateStoredProc(String approvalmatrixTemplateValues, String approvalIds, int approvalMode) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CREATE_APPROVALMATRIXTEMPLATE_PROC;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		requestParameters.put("_matrixValues", approvalmatrixTemplateValues);
		requestParameters.put("_matrixApprover", approvalIds);
		requestParameters.put("_isGroupMatrix", approvalMode);

		try {
			String approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalMatrixResponseJson = new JSONObject(approvalMatrixResponse);
			if (approvalMatrixResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				diagnostic.prepareInfo("approval matrix template record updated successfully").log();
				return true;
			} else {
				alert.prepareError("Unable to Update ApprovalMatrixTemplate Entry: ").log();
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to Update ApprovalMatrixTemplate Entry: " , e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at callCreateApprovalMatrixTemplateStoredProc method: " , e).log();
			return false;
		}

	}
	
	@Override
	public List<ApprovalMatrixDTO> fetchApprovalMatrixTemplateRecords(String cifId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GET_APPROVALMATRIXTEMPLATE;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "coreCustomerId" + DBPUtilitiesConstants.EQUAL + cifId;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		List<ApprovalMatrixDTO> templateList = null;
		String poResponse = null;
		try {
			poResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(poResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			templateList = JSONUtils.parseAsList(jsonArray.toString(), ApprovalMatrixDTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch Approval matrix template records: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchApprovalMatrixTemplateRecords: ", e).log();
			return null;
		}

		return templateList;
	}

	public Set<String> getActionWithApproveFeatureAction(Set<String> actionsSet, Map<String, Object> headersMap) {
		StringBuilder actionsString = new StringBuilder();
		Set<String> hashSet = new HashSet<>();
		for (String action : actionsSet) {
			actionsString.append(action);
			actionsString.append(",");
		}
		if (actionsString.length() > 0)
			actionsString.replace(actionsString.length() - 1, actionsString.length(), "");
		try {
			ContractFeatureActionsBusinessDelegate contractFeaturesBD = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);

			String actions = contractFeaturesBD.getActionsWithApproveFeatureAction(actionsString.toString(),headersMap);

			hashSet = HelperMethods.splitString(actions, DBPUtilitiesConstants.COMMA_SEPERATOR);
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return hashSet;

	}
	
	@Override
	public ApprovalMatrixDTO fetchApprovalMatrixById(String approvalMatrixId) {
		
		List<ApprovalMatrixDTO> approvalMatrixDTOs = new ArrayList<>();		

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALMATRIX_GET;
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		String filter = "id" + DBPUtilitiesConstants.EQUAL + approvalMatrixId;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String approvalMatrixResponse = null;
		try {
			approvalMatrixResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(approvalMatrixResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			approvalMatrixDTOs = JSONUtils.parseAsList(jsonArray.toString(), ApprovalMatrixDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		
		if(approvalMatrixDTOs != null && approvalMatrixDTOs.size() > 0) {
			return approvalMatrixDTOs.get(0);
		}
		
		return null;
	}
	
	@Override
	public List<CustomerActionDTO> fetchCustomerActions(String cifId, String contractId, Set<String> setEligibleForApprovals) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GET_CUSTOMERACTIONS;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		
		String filter = "coreCustomerId" + DBPUtilitiesConstants.EQUAL + "'" + cifId + "'"+ DBPUtilitiesConstants.AND +
				"contractId" + DBPUtilitiesConstants.EQUAL + "'" + contractId + "'";
		
		String filterWithIds ="";
		filterWithIds= setEligibleForApprovals.stream().reduce(filterWithIds, (item, element) -> "Action_id"+ DBPUtilitiesConstants.EQUAL +"'"+ element + (("'"  + DBPUtilitiesConstants.OR   + item).equals("'" +DBPUtilitiesConstants.OR)? "'": ("'"+DBPUtilitiesConstants.OR + item)));

		String finalFilter = (setEligibleForApprovals.size()>0)? filter + DBPUtilitiesConstants.AND + "( " + filterWithIds + ")" : filter; 
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, finalFilter.trim());
		List<CustomerActionDTO> actionsList = new ArrayList<> ();

		String response = null;
		JSONArray records = null;
		try {
			  response = DBPServiceExecutorBuilder.builder()
					  .withServiceId(serviceName)
					  .withObjectId(null)
					  .withOperationId(operationName)
					  .withRequestParameters(requestParameters) 
					  .build().getResponse();
			 
			if (response.length() == 0 || response == null) {
				alert.prepareError("DB Service returned no records").log();
				return actionsList;
			}

			try {
				JSONObject responseObj = new JSONObject(response);
				records = responseObj.getJSONArray("customeraction");
			} catch (JSONException e) {
				alert.prepareError("Error while parsing string JSON", e).log();
				return null;
			}

			if (records.length() == 0) {
				alert.prepareError("DB Service returned no records").log();
				return actionsList;
			}

			actionsList = getDTOList(records.toString(), CustomerActionDTO.class);

			return actionsList;
			
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch customer actions records: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchCustomerActions: ", e).log();
			return null;
		}
	}
	
	  @SuppressWarnings("unchecked")
	    private <T> List<T> getDTOList(String jsonArrayString, Class<T> classType) {
	        List<T> dtoList = new ArrayList<>();
	        JsonParser parser = new JsonParser();
	        JsonArray array = parser.parse(jsonArrayString).getAsJsonArray();
	        for (JsonElement element : array) {
	            if (element.isJsonObject()) {
	                T dto = null;
	                if (null != DTOMappings.getDTOObjectPropertyMappings(classType)) {
	                    dto = (T) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), classType, true);
	                } else {
	                    dto = (T) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), classType, false);
	                }
	                if (null != dto) {
	                    dtoList.add(dto);
	                }

	            }

	        }
	        return dtoList;
	    }
	  
	private Boolean createApprovalMode(String cif, String contractId, Boolean isGroupLevel) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CREATE_APPROVALMODE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		UUID uuidValue = UUID.randomUUID();
		requestParams.put("id", uuidValue.toString());
		requestParams.put("coreCustomerId", cif);
		requestParams.put("contractId", contractId);
		requestParams.put("isGroupLevel", isGroupLevel);
		String editResponse = null;

		try {
			editResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParams)
					.build().getResponse();

			JSONObject approvalModeResponseJson = new JSONObject(editResponse);
			if((approvalModeResponseJson != null) 
					&& approvalModeResponseJson.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0) {
				return true;
			}  
        }
        catch (JSONException e) {
        	alert.prepareError("Unable to create approvalMode " , e).log();		
        	return false;
        }
        catch (Exception e) {
        	alert.prepareError("Caught exception at createApprovalMode method: " , e).log();
        	return false;
        }
		return false;
	}
	
	@Override
	public Map<String, LimitsDTO> fetchLimits(String contractId, String coreCustomerId, List<String> actionIds) {
		Map<String, LimitsDTO> limits = new HashMap<>();
		
		Map<String, LimitsDTO> limitsList = _fetchLimitsDTO(fetchContractActionLimits(contractId, coreCustomerId));
		Map<String, LimitsDTO> featureLimits = fetchLimits();
		
		for(String action : actionIds) {
			LimitsDTO limitsDTO = new LimitsDTO();
			
			limitsDTO.setMaxTransactionLimit(Math.min(featureLimits.get(action)!= null ? featureLimits.get(action).getMaxTransactionLimit() : 0.0, limitsList.get(action) != null ? limitsList.get(action).getMaxTransactionLimit() : 0.0 ));
			limitsDTO.setDailyLimit(Math.min(featureLimits.get(action) != null ? featureLimits.get(action).getDailyLimit(): 0.0, limitsList.get(action)!= null ? limitsList.get(action).getDailyLimit(): 0.0));
			limitsDTO.setWeeklyLimit(Math.min(featureLimits.get(action)!= null ? featureLimits.get(action).getWeeklyLimit(): 0.0, limitsList.get(action)!= null ? limitsList.get(action).getWeeklyLimit(): 0.0));
			
			limits.put(action, limitsDTO);
		}
		
			
		return limits;
	}
		
	private JSONArray fetchContractActionLimits(String contractId, String coreCustomerId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CONTRACTACTIONLIMIT_GET;

		String filter = "contractId" + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND +
				"coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId;


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
	
	private Map<String, LimitsDTO> _fetchLimitsDTO(JSONArray limitsArray) {
		Map<String, LimitsDTO> limits = new HashMap<>();
		

		try {
			
			for (Object obj : limitsArray) {
				
				LimitsDTO limitsDTO = null;
				JSONObject limitsObj = (JSONObject) obj;
				String action = (limitsObj.has("actionId")) ? limitsObj.getString("actionId") : (limitsObj.has("Action_id")) ? limitsObj.getString("Action_id"): "";
				String limitType = (limitsObj.has("limitTypeId")) ? limitsObj.getString("limitTypeId") : (limitsObj.has("LimitType_id")) ? limitsObj.getString("LimitType_id"): "";
				Double limit = (limitsObj.has("value")) ? limitsObj.getDouble("value") : 0;
				
				if (!StringUtils.isEmpty(action) && !StringUtils.isEmpty(limitType)) {

					if (!limits.containsKey(action)) {
						limits.put(action, new LimitsDTO());
					}

					limitsDTO = limits.get(action);
				}
				
				
				
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
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch contract-corecustomer limits from DB: ", e).log();
			return null;
		}

		return limits;
	}

	private Map<String, LimitsDTO> fetchLimits() {
		Map<String, LimitsDTO> limitsDTO = null;

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_ACTIONLIMIT_GET;

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceId).withObjectId(null)
					.withOperationId(operationId).withRequestParameters(null).build().getResponse();
			
			if (response == null) {
				limitsDTO = null;
			}
			JSONObject limitsJSON = new JSONObject(response);
			JSONArray limitsArray = CommonUtils.getFirstOccuringArray(limitsJSON);
			limitsDTO = _fetchLimitsDTO(limitsArray);

		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching global FI level limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
	@Override
	public List<ApprovalMatrixDTO> fetchAccountDetails(String contractId, String cif, String accountIds) {
		List<ApprovalMatrixDTO> approvalMatrix = new ArrayList<>();		

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_ACCOUNTDETAILS_PROC;
		
		
		HashMap<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_contractId", contractId);
		requestParameters.put("_cif", cif);
		requestParameters.put("_accountIds", accountIds);

		HashMap<String, Object> requestHeaders = new HashMap<>();

		String approvalMatrixResponse = null;
		JSONArray records = null;
		try {
			approvalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" , e).log();
			return null;
		}
		
		if (approvalMatrixResponse.length()==0 || approvalMatrixResponse==null)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			JSONObject approvalMatrixJSON = new JSONObject(approvalMatrixResponse);
			records = approvalMatrixJSON.getJSONArray("records");
		}
		catch (JSONException e) {
			alert.prepareError("Error while parsing string JSON" , e).log();
			return null;
		} 
		if (records.length()==0)
		{
			alert.prepareError("DB Service returned no records").log();
			return approvalMatrix;
		}

		try {
			approvalMatrix = JSONUtils.parseAsList(records.toString(), ApprovalMatrixDTO.class);
			return approvalMatrix;
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;

		}
	}
	
	/*
	public List<ApprovalMatrixDTO> fetchAccountDetailsWithList(String contractId, String cif, List<String> accountIds) {
		
		List<String> temp = new ArrayList<>();
		temp.addAll(accountIds);
		if(temp.contains("CUSTOMERID_LEVEL")) {
			temp.remove("CUSTOMERID_LEVEL");
		}
		
		return fetchAccountDetails(contractId, cif, String.join(",", temp));
	}*/
	
	@Override
	public List<CustomerActionDTO> fetchCustomerActionsByUserID(String cifId, String contractId, String userId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GET_CUSTOMERACTIONS;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + userId
				+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cifId
				+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId
				+ DBPUtilitiesConstants.AND + InfinityConstants.isAllowed + DBPUtilitiesConstants.EQUAL + "1"
				+ DBPUtilitiesConstants.AND + "softdeleteflag"+ DBPUtilitiesConstants.EQUAL + "0";
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		List<CustomerActionDTO> actionsList = new ArrayList<> ();
		String response = null;
		JSONArray records = null;
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			
			if (response.length() == 0 || response == null) {
				alert.prepareError("DB Service returned no records").log();
				return actionsList;
			}

			try {
				JSONObject responseObj = new JSONObject(response);
				records = responseObj.getJSONArray("customeraction");
			} catch (JSONException e) {
				alert.prepareError("Error while parsing string JSON", e).log();
				return null;
			}

			if (records.length() == 0) {
				alert.prepareError("DB Service returned no records").log();
				return actionsList;
			}

			actionsList = getDTOList(records.toString(), CustomerActionDTO.class);

			return actionsList;
			
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch customer actions records: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchCustomerActionsByUserID: ", e).log();
			return null;
		}

	}

	@Override
	public List<CustomerActionDTO> fetchCustomerActionsByLoggedUserID(String userId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GET_CUSTOMERACTIONS;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + userId
				+ DBPUtilitiesConstants.AND + InfinityConstants.isAllowed + DBPUtilitiesConstants.EQUAL + "1"
				+ DBPUtilitiesConstants.AND + "softdeleteflag"+ DBPUtilitiesConstants.EQUAL + "0";
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		List<CustomerActionDTO> actionsList = new ArrayList<> ();
		String response = null;
		JSONArray records = null;
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			
			if (response.length() == 0 || response == null) {
				alert.prepareError("DB Service returned no records").log();
				return actionsList;
			}

			try {
				JSONObject responseObj = new JSONObject(response);
				records = responseObj.getJSONArray("customeraction");
			} catch (JSONException e) {
				alert.prepareError("Error while parsing string JSON", e).log();
				return null;
			}

			if (records.length() == 0) {
				alert.prepareError("DB Service returned no records").log();
				return actionsList;
			}

			actionsList = getDTOList(records.toString(), CustomerActionDTO.class);

			return actionsList;
			
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch customer actions records: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchCustomerActionsByUserID: ", e).log();
			return null;
		}

	}
	
	@Override
	public Map<String, JSONObject> fetchFeatureActionsEligibleForApproval() {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FEATUREACTION_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		Map<String, JSONObject> resultMap = new HashMap<>();

		String filter ="approveFeatureAction ne null";
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String response = null;
		JSONArray records = null;
		try {
			
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();

			if (response.length() == 0 || response == null) {
				alert.prepareError("DB Service returned no records").log();
				return resultMap;
			}

			try {
				JSONObject responseObj = new JSONObject(response);
				records = responseObj.getJSONArray("featureaction");
			} catch (JSONException e) {
				alert.prepareError("Error while parsing string JSON", e).log();
				return null;
			}

			if (records.length() == 0) {
				alert.prepareError("DB Service returned no records").log();
				return resultMap;
			} 

			for(int n = 0; n< records.length(); n++) {

				JSONObject record = records.getJSONObject(n);
				resultMap.put(record.getString("id"), record);
			}

			return resultMap;

		} catch (JSONException je) {
			
			alert.prepareError("Failed to fetch customer actions records: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchCustomerActions: ", e).log();
			
			return null;
		}			
	}
	
	@Override
	public Map<String, JSONObject> fetchFeatureActiondetails(Set<String> customerActionsSet) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FEATUREACTION_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		Map<String, JSONObject> resultMap = new HashMap<>();

		String filter ="";
		filter= customerActionsSet.stream().reduce(filter, (item, element) -> "id"+ DBPUtilitiesConstants.EQUAL +"'"+ element + (("'"  + DBPUtilitiesConstants.OR   + item).equals("'" +DBPUtilitiesConstants.OR)? "'": ("'"+DBPUtilitiesConstants.OR + item)));

		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		String response = null;
		JSONArray records = null;
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();

			if (response.length() == 0 || response == null) {
				alert.prepareError("DB Service returned no records").log();
				return resultMap;
			}

			try {
				JSONObject responseObj = new JSONObject(response);
				records = responseObj.getJSONArray("featureaction");
			} catch (JSONException e) {
				alert.prepareError("Error while parsing string JSON", e).log();
				return null;
			}

			if (records.length() == 0) {
				alert.prepareError("DB Service returned no records").log();
				return resultMap;
			} 

			for(int n = 0; n< records.length(); n++) {

				JSONObject record = records.getJSONObject(n);
				resultMap.put(record.getString("id"), record);
			}

			return resultMap;

		} catch (JSONException je) {
			alert.prepareError("Failed to fetch customer actions records: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchCustomerActions: ", e).log();
			return null;
		}

	}

	/**
	 * @description - to fetch the approval matrix currency for the given combination of cif, contract and account
	 * @param cifId
	 * @param contractId
	 * @param accountId
	 * @return - the user's approval matrix currency
	 */
	@Override
	@Deprecated
	public String fetchUserApprovalCurrency(String cifId, String contractId, String accountId){
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_GET_APPROVALMATRIXTEMPLATE;
		String table = "approvalmatrixtemplate";

		String filter = "coreCustomerId" + DBPUtilitiesConstants.EQUAL + cifId + DBPUtilitiesConstants.AND + "contractId" + DBPUtilitiesConstants.EQUAL + contractId;

		if(!StringUtils.isEmpty(accountId)){
			operationName = OperationName.DB_APPROVALMATRIX_GET;
			filter = filter + DBPUtilitiesConstants.AND + "accountId" + DBPUtilitiesConstants.EQUAL + accountId;
			table = "approvalmatrix";
		}

		try{
			Map<String, Object> reqParams = new HashMap<>();
			reqParams.put("$filter", filter);
			String responseStr = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(reqParams)
					.withRequestHeaders(null)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(responseStr);
			if(responseObj.has("opstatus") && Integer.parseInt(responseObj.optString("opstatus")) == 0){
				JSONArray data = responseObj.getJSONArray(table);
				if(data.length() > 0){
					JSONObject object = (JSONObject) data.get(0);
					if(StringUtils.isEmpty(object.getString("currency"))){
						alert.prepareError("Currency data not found for the given user!").log();
					}
					else{
						return object.getString("currency");
					}
				}
				else{
					alert.prepareError("No records found for the given user!").log();
				}
			}
			else{
				alert.prepareError("Error while reading db - + " + table + " +! ").log();
			}
		} catch (Exception e){
			
			alert.prepareError("Exception occured while fetching the currency info of user: " + e).log();
			return null;
		}
		return null;
	}
}
