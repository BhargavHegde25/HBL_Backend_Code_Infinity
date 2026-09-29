package com.temenos.dbx.product.achservices.resource.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHTransactionBackendDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionRecordDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionResource;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.commonsutils.LogEvents;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public class ACHTransactionResourceImpl implements ACHTransactionResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getInstance().
			getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CustomerBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class); 
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	
	@Override
	public Result createACHTransaction(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) {
				
			Result result = new Result();
			ACHTransactionDTO achtransactionDTO=null;
		
			ACHCommonsBusinessDelegate achCommonsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
			AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
			
            @SuppressWarnings("unchecked")
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
                        
            @SuppressWarnings("unchecked")
			Map<String, String> inputParam = (HashMap<String, String>) inputArray[1];

            String templateRequestTypeId = inputParam.get("TemplateRequestType_id");
            String maxAmount = inputParam.get("MaxAmount");
            String transactionType_id = inputParam.get("TransactionType_id");
            String debitAccount = inputParam.get("DebitAccount");
            String effectiveDate = inputParam.get("EffectiveDate");
            String recordsIP = inputParam.get("Records");
            String validate = inputParams.get("validate") == null || (StringUtils.isEmpty(inputParams.get("validate").toString())) ? null : inputParams.get("validate").toString();
            String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
            String baseCurrency  = application.getBaseCurrencyFromCache();
    		String transactionCurrency = inputParams.get("transactionCurrency") == null ? baseCurrency : inputParams.get("transactionCurrency").toString();
    		String serviceCharge = inputParams.get("serviceCharge") == null ? "0.0" : inputParams.get("serviceCharge").toString();
        	Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
    		String createdby = CustomerSession.getCustomerId(customer);
    		
    		boolean isBusinessUser = CustomerSession.IsBusinessUser(customer);
    		
    		ACHTransactionBackendDelegate achTransactionBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
 				   .getFactoryInstance(BackendDelegateFactory.class)
 				   .getBackendDelegate(ACHTransactionBackendDelegate.class);
    			
//    		//unauthorized user check
//    		if(!isBusinessUser) {
//    			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
//    		}   		

            // Input validations

            if (templateRequestTypeId == null || templateRequestTypeId.equals("")) {
                return ErrorCodeEnum.ERR_12027.setErrorCode(new Result());
            }
            if (transactionType_id == null || transactionType_id.equals("")) {
                return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
            }
            if (debitAccount == null || debitAccount.equals("")) {
                return ErrorCodeEnum.ERR_12022.setErrorCode(new Result());
            }
            if (maxAmount == null || maxAmount.equals("")) {
                return ErrorCodeEnum.ERR_12029.setErrorCode(new Result());
            }
            if (recordsIP == null || recordsIP.equals("")) {
                return ErrorCodeEnum.ERR_12028.setErrorCode(new Result());
            }
            if (effectiveDate == null || effectiveDate.equals("")) {
                return ErrorCodeEnum.ERR_12021.setErrorCode(new Result());
            }
            
            CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, debitAccount.toString());
    		String contractId = account.getContractId();
    		String coreCustomerId = account.getCoreCustomerId();
    		String companyId = account.getOrganizationId();
            
            if( ACHConstants.TEMPLATE_ID_NOT_AVAILABLE.equals(inputParam.get("Template_id")) ) {
            	 inputParams.put("template_id", ACHConstants.TEMPLATE_ID_DEFAULT );
            	 inputParams.remove( "Template_id");
            }

            boolean doesSubRecordExists = ACHConstants.TEMPLATE_REQUEST_TYPE_FEDERAL_TAX.equals(templateRequestTypeId);

            String transactionType = achCommonsDelegate.getTransactionTypeById(Integer.valueOf(transactionType_id));
            
            if( transactionType == null ) {
            	return ErrorCodeEnum.ERR_12306.setErrorCode(new Result());
            }
            String featureActionId = null;
            
            if (transactionType.equals( ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT ) ) {
                featureActionId = FeatureAction.ACH_PAYMENT_CREATE;
            } else if (transactionType.equals( ACHConstants.ACH_TRANSACTION_TYPE_COLLECTION ) ) {
                featureActionId = FeatureAction.ACH_COLLECTION_CREATE;
            }
            
            inputParam.put("transactionTypeName", transactionType);
            
    		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
    		
    		if(! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, debitAccount, CustomerSession.IsCombinedUser(customer))) {
    			return ErrorCodeEnum.ERR_10139.setErrorCode(result);
    		}            

            JSONArray records = new JSONArray(recordsIP.toString());

            Double totalAmount = achCommonsDelegate.getTotalAmountFromRecords(records, doesSubRecordExists);
            inputParams.put("totalAmount", totalAmount);

            if (totalAmount <= 0) {
                return ErrorCodeEnum.ERR_12301.setErrorCode(new Result());
            }

            if (totalAmount > Double.parseDouble(maxAmount)) {
                return ErrorCodeEnum.ERR_12302.setErrorCode(new Result());
            }

            inputParams.put("companyId", companyId);
            inputParams.put("createdby", createdby);
            inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
            inputParams.put("featureActionId", featureActionId);
            
            try {
    			achtransactionDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), ACHTransactionDTO.class);
    		} catch (IOException e) {
    			alert.prepareError("Error occured while fetching the input params: " + e).log();
    			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());			
    		}
            
            
            if("true".equalsIgnoreCase(validate)) {
            	ACHTransactionDTO validateachtrxDTO = achTransactionBackendDelegate.validateTransaction(achtransactionDTO,dcRequest);
    			try {
    				 result = JSONToResult.convert(new JSONObject(validateachtrxDTO).toString());
    				 return result;
    			} catch (JSONException e) {
    				alert.prepareError("Error occured while converting the response from Line of Business service for ach transfer: ", e).log();
    				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
    			}
    		}
            
            TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
    		transactionStatusDTO.setCustomerId(createdby);
    		transactionStatusDTO.setCompanyId(companyId);
    		transactionStatusDTO.setAccountId(debitAccount);
    		transactionStatusDTO.setAmount(totalAmount);
    		transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
    		transactionStatusDTO.setDate(effectiveDate);
    		transactionStatusDTO.setTransactionCurrency(null);
    		transactionStatusDTO.setFeatureActionID(featureActionId);
    		transactionStatusDTO.setConfirmationNumber(backendid);
    		transactionStatusDTO.setTransactionCurrency(transactionCurrency);
    		transactionStatusDTO.setServiceCharge(serviceCharge);
    		
            
    		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, dcRequest);	
    		
    		if(transactionStatusDTO == null) {			
    			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
    		}
    		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
    			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
    			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
    			return result;
    		}
    		
    		TransactionStatusEnum transactionStatus =transactionStatusDTO.getStatus();
    		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
    		achtransactionDTO.setRequestId(transactionStatusDTO.getRequestId());
    		achtransactionDTO.setStatus(transactionStatus.getStatus());
    		achtransactionDTO.setTransactionCurrency(transactionCurrency);
    		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
    		achtransactionDTO.setConfirmationNumber(confirmationNumber);
    		try {
    			achtransactionDTO.setTotalAmount(transactionStatusDTO.getAmount().doubleValue());
    		}
    		catch(NumberFormatException e) {
    			alert.prepareError("Invalid amount value", e).log();
    			return ErrorCodeEnum.ERR_10624.setErrorCode(new Result());
    		}
    		achtransactionDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
    		achtransactionDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
			
    		ACHTransactionDTO achDbxDTO = achTransactionBusinessDelegate.createTransactionAtDBX(achtransactionDTO);
    		if(achDbxDTO == null) {
    			alert.prepareError("Error occured while creating entry into the DBX table: ").log();
    			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
    		}
    		if(achDbxDTO.getDbpErrCode() != null || achDbxDTO.getDbpErrMsg() != null) {
    			result.addParam(new Param("dbpErrCode", achDbxDTO.getDbpErrCode()));
    			result.addParam(new Param("dbpErrMsg", achDbxDTO.getDbpErrMsg()));
    			return result;
    		}	

            String transactionId = achDbxDTO.getTransaction_id();
            long tempRequestTypeId = achDbxDTO.getTemplateRequestType_id();
            String recordsData = (String) inputParams.remove("Records");

			//Creating records and sub records for the template created
			List<ACHTransactionRecordDTO> queryData = _getTransactionRecordDTOForCreation(recordsData, transactionId+"", tempRequestTypeId+"");            
            
			Result operationResult = achTransactionBusinessDelegate.createTransactionRecordAndSubRecords(queryData);
			
			if(null == operationResult || null != operationResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY)
	                || null != operationResult.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
				return operationResult;
			} 
    		
			String referenceId = achDbxDTO.getTransaction_id();
			String requestid = "";
			String requestObj = null;
			try {
				requestObj = new JSONObject(achDbxDTO).toString();
				result = JSONToResult.convert(requestObj);
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for ach transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
			ACHTransactionDTO achtrxndto= new ACHTransactionDTO();
			if (transactionStatus == TransactionStatusEnum.SENT) {
				if(StringUtils.isEmpty(backendid)) {
					achDbxDTO.setTransaction_id(null);
					achtrxndto = achTransactionBackendDelegate.createTransactionWithoutApproval(achDbxDTO, dcRequest);					
					if(achtrxndto == null) {	
						achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
						return ErrorCodeEnum.ERR_12600.setErrorCode(result);
					}
				}
				else {
					achtrxndto = achTransactionBusinessDelegate.approveTransaction(backendid, dcRequest);
					if(achtrxndto == null) {	
						achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
						return ErrorCodeEnum.ERR_29020.setErrorCode(result);
					}
				}
				if(achtrxndto.getDbpErrCode() != null || achtrxndto.getDbpErrMsg() != null) {
					achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
	                return ErrorCodeEnum.ERR_00000.setErrorCode(result, achtrxndto.getDbpErrMsg());
				}
				String refId = achtrxndto.getReferenceID();
				if(refId == null || "".equals(refId)) {
					achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
					return ErrorCodeEnum.ERR_12601.setErrorCode(result);
				}
				achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.EXECUTED.getStatus(), refId);
				result.addParam(new Param("transaction_id", refId));
				result.addParam(new Param("referenceId", achtrxndto.getReferenceID()));
				result.addParam(new Param("status", transactionStatus.getStatus()));
		        result.addParam(new Param("message", transactionStatus.getMessage()));
			}
			else if(transactionStatus == TransactionStatusEnum.PENDING){
				requestid = transactionStatusDTO.getRequestId();
				String pendingrefId = null;
				if(StringUtils.isEmpty(backendid))
				{
					achtrxndto = achTransactionBusinessDelegate.createPendingTransaction(achDbxDTO, dcRequest);
					if(achtrxndto == null)
					{
						achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
						alert.prepareError("Error occured while creating entry into the backend table: ").log();
						return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
					}
					if(achtrxndto.getDbpErrCode() != null || achtrxndto.getDbpErrMsg() != null) {
							achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
						return ErrorCodeEnum.ERR_00000.setErrorCode(result, achtrxndto.getDbpErrMsg());
					}
					backendid = achtrxndto.getReferenceID();
				}
					pendingrefId= backendid;
					achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, transactionStatus.toString(), backendid);
					transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, dcRequest);
					if(transactionStatusDTO == null) 
					{							
						achTransactionBackendDelegate.deleteTransaction(backendid, null, dcRequest);
						achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(), backendid);						
						return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
					}	
					if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
						achTransactionBackendDelegate.deleteTransaction(backendid, null, dcRequest);
						achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, TransactionStatusEnum.FAILED.getStatus(),backendid);
						result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
						result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
						return result;
					}
					transactionStatus = transactionStatusDTO.getStatus();
					backendid = transactionStatusDTO.getConfirmationNumber();
					
				/**
				 * Start: Added as part of Alerts
				 */
				Map <String,Object> additionalParams = new HashMap<String,Object>();
				additionalParams.put("totalAmount", Double.toString(totalAmount));
				additionalParams.put("transactionType", transactionType);
				try {
					LogEvents.pushAlertsForApprovalRequests( featureActionId, dcRequest, response,inputParams, null,  backendid, requestid, CustomerSession.getCustomerName(customer),additionalParams);
				} catch (Exception e) {
					alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
				}
				/**
				 * Ending: Added as part of Alerts
				 */
				result.addParam(new Param("requestId", requestid));
				

				if (transactionStatus == TransactionStatusEnum.APPROVED) {
					result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
					result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
					result.addParam(new Param("referenceId", backendid));
					result.addParam(new Param("transaction_id", backendid));
				}
				else {
					result.addParam(new Param("status", transactionStatus.getStatus()));
					result.addParam(new Param("message", transactionStatus.getMessage()));
					result.addParam(new Param("referenceId", pendingrefId));
					result.addParam(new Param("transaction_id", pendingrefId));
					achTransactionBusinessDelegate.updateStatusUsingTransactionId(referenceId, transactionStatus.toString(), pendingrefId);
				}
				
			}
			else if(transactionStatus == TransactionStatusEnum.APPROVED){	
				result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
		        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
		        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		        result.addParam(new Param("transaction_id", transactionStatusDTO.getConfirmationNumber()));
			}

    		JSONObject achTransactionJSON = new JSONObject();
    		if( transactionStatus == TransactionStatusEnum.SENT || transactionStatus == TransactionStatusEnum.PENDING ) {
    			achTransactionJSON = new JSONObject(achtrxndto);
    			//result = JSONToResult.convert( achTransactionJSON.toString() ); 
    		}
    	
    		if(!application.getIsStateManagementAvailableFromCache()) {
    			result.addParam(new Param("transaction_id", transactionId));
    		}
    	
    	try {
    		auditloggingforACH (dcRequest,response,inputArray,achTransactionJSON,transactionType,transactionStatus,totalAmount,requestid,referenceId,referenceId);
		} catch(Exception e) {
			alert.prepareError("Error occured while audit logging.",e).log();
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), dcRequest);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}

    	return result;  		
	}

	/**
	 * @author KH2317
	 * @version 1.0
	 * @param recordData string form array which contains records
	 * @param transactionId which contains the id of transaction created above
	 * @param templateRequestTypeId contains type of the template record 
	 * @return String containing queries for creating records and sub records
	 * **/
	private List<ACHTransactionRecordDTO> _getTransactionRecordDTOForCreation(String recordsData, String transactionId, String templateRequestType_id) {
		
		List<ACHTransactionRecordDTO> recordsDTOList = null;
		
		try {
			
			JSONArray records = new JSONArray(recordsData);
			int numberOfRecords = records.length();
			
			for(int i=0; i < numberOfRecords; i++) {
				JSONObject record = records.getJSONObject(i);
				record.put("transaction_id", transactionId);
				record.put("templateRequestType_id", templateRequestType_id);
			}
			recordsDTOList = JSONUtils.parseAsList(records.toString(), ACHTransactionRecordDTO.class);
			
		} catch (IOException e) {
			alert.prepareError("Exception occured while updating records",e).log();
		}
		
		return recordsDTOList;
	}

	@Override
	public Result fetchAllACHTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		
		ACHTransactionBusinessDelegate transactionDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
		
		List<ACHTransactionDTO> transactions = null;
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String customerId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW);
			String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(features == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, features);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			if(!params.isValidFilter()) {
				alert.prepareError("Input contains special characters").log();
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, new JSONArray());
				return JSONToResult.convert(resultObj.toString());
			}
			
			transactions = transactionDelegate.getACHTransactions(params, customerId, "", request);
		
			if(transactions != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(transactions, ACHTransactionDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while defining resources for fetchAllACHTransactions", exp).log();
			return null;
		}
		return result;
	}

	@Override
	public Result fetchACHTransactionById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		
		ACHTransactionBusinessDelegate transactionDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String customerId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW);
			String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(features == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, features);
			
			Object transactionId = filterParamsMap.get(Constants._TRANSACTION_ID);  
			
			if (transactionId == "" || transactionId == null) {
				return ErrorCodeEnum.ERR_12042.setErrorCode(new Result());
			}
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			ACHTransactionDTO achDTO = transactionDelegate.fetchTransactionById(transactionId.toString(), request);
			if(achDTO == null) {
				alert.prepareError("Record Doesn't Exist").log();
	            return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
			}
			
			transactionId = achDTO.getTransaction_id();
			
			List<ACHTransactionDTO> transactions = transactionDelegate.getACHTransactions(params, customerId, transactionId, request);
			/*List<ACHTransactionDTO> transactions = new ArrayList<ACHTransactionDTO>();
			ACHTransactionDTO transaction = transactionDelegate.fetchTransactionById(transactionId.toString(), request);
			transactions.add(transaction);
			*/
			
			if(transactions != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(transactions, ACHTransactionDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while defining resources for fetchAllACHTransactions", exp).log();
			return null;
		}
		return result;
	}
	/**
	 * Logs ACH transactions auditactivity
	 * @param request
	 * @param response
	 * @param inputArray
	 * @param achTransactionJSON
	 * @param transactionType
	 * @param transactionStatus
	 * @param totalAmount
	 * @param requestId
	 * @param referenceId
	 * @param confirmationNumber
	 */
	public void auditloggingforACH (DataControllerRequest request,DataControllerResponse response, Object[] inputArray,JSONObject achTransactionJSON,String transactionType,TransactionStatusEnum transactionStatus, Double totalAmount,String requestId, String referenceId,String confirmationNumber)
	{
		try
		{
		String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase("false")) return; 
			
		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApproversBusinessDelegate.class);
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParam = (HashMap<String, String>) inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		String debitAccount = inputParam.get("DebitAccount");
		String eventsubtype = null;
		String status =  ACHConstants.SID_EVENT_FAILURE;
		 if (transactionType.equals( ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT ) )
		 {
			 if( ACHConstants.NO_TEMPLATE_USED.equals(inputParam.get("TemplateName")) ) {
            	 eventsubtype  = ACHConstants.ACH_PAYMENT_ONE_TIME_TRANSFER;
            } 
			 else
			 {
				 eventsubtype  = ACHConstants.ACH_PAYMENT_TEMPLATE_TRANSFER;
			 }
         } else if (transactionType.equals( ACHConstants.ACH_TRANSACTION_TYPE_COLLECTION ) ) {
        	 if( ACHConstants.NO_TEMPLATE_USED.equals(inputParam.get("TemplateName")) ) {
            	 eventsubtype  = ACHConstants.ACH_COLLECTION_ONE_TIME_TRANSFER;
            } 
			 else
			 {
				 eventsubtype  = ACHConstants.ACH_COLLECTION_TEMPLATE_TRANSFER;
			 }
         }
		//Adding common customparams for every log
		JsonObject customparams = new JsonObject();
		if (referenceId == null) {referenceId = "NA";}
		customparams.addProperty("referenceId", referenceId);
		customparams.addProperty("amount",totalAmount);   
		customparams.addProperty("fromAccountNumber",debitAccount);
		customparams.addProperty("createdBy",CustomerSession.getCustomerName(customer));
		String dtFormat = "yyyy-MM-dd'T'hh:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
		Date currdate = new Date();
		String date = formatter.format(currdate);
		customparams.addProperty("dateCreated",date);
		JsonObject resultObject = new JsonParser().parse(achTransactionJSON.toString()).getAsJsonObject();
		
		List<String> approvers = new ArrayList<String>() ;
		
		switch (transactionStatus) {
		case SENT:        
			if(confirmationNumber == null || "".equals(confirmationNumber)) {
				resultObject = ErrorCodeEnum.ERR_12601.setErrorCode(resultObject);
				customparams.add("Response",resultObject);
				break;
			}
			else 
			{
				resultObject.addProperty("referenceId", referenceId);
				resultObject.addProperty("status",transactionStatus.getStatus());
				resultObject.addProperty("message",transactionStatus.getMessage());
				customparams.add("Response",resultObject);
				customparams.addProperty("approvers","pre-approved");
				customparams.addProperty("approvedBy","pre-approved");
				status = ACHConstants.SID_EVENT_SUCCESS;
				break;
			}
		case PENDING:
			resultObject.addProperty("referenceId", referenceId);
			resultObject.addProperty("status",transactionStatus.getStatus());
			resultObject.addProperty("message",transactionStatus.getMessage());
			eventsubtype = Constants.PENDING_APPROVAL_ + eventsubtype;
			customparams.add("Response",resultObject);
			approvers = approversBusinessDelegate.getRequestApproversList(requestId);
			customparams.addProperty("approvers",approvers.toString());
			status = ACHConstants.SID_EVENT_SUCCESS;
			break;
		
		case DENIED_AD_MAX_TRANSACTION:
			resultObject = ErrorCodeEnum.ERR_12501.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		case DENIED_AD_DAILY:
			resultObject = ErrorCodeEnum.ERR_12502.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		case DENIED_AD_WEEKLY:
			resultObject = ErrorCodeEnum.ERR_12503.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		case DENIED_MAX_TRANSACTION:
			resultObject = ErrorCodeEnum.ERR_12504.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		case DENIED_DAILY:
			resultObject = ErrorCodeEnum.ERR_12505.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		case DENIED_WEEKLY:
			resultObject = ErrorCodeEnum.ERR_12506.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		case DENIED_INVALID_APPROVAL_MATRIX:
			resultObject = ErrorCodeEnum.ERR_12507.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		case DENIED_MIN_TRANSACTION:
			resultObject = ErrorCodeEnum.ERR_12512.setErrorCode(resultObject);
			customparams.add("Response",resultObject);
			break;
			
		default:
			break;
			
		} 
		if(isSMEUser) {
			customparams.addProperty("approvedBy", "N/A");
			customparams.addProperty("rejectedBy", "N/A");
		}


		AdminUtil.addAdminUserNameRoleIfAvailable(customparams, request);
		
		EventsDispatcher.dispatch(request, response, "MAKE_TRANSFER", eventsubtype,
				"ACHTransactions/createACHTransaction",status, null, CustomerSession.getCustomerName(customer), customparams);
	}
	catch (Exception e)
		{
		 alert.prepareError("Error doinf audit logging").log();
		}
	}
	public JSONObject fetchTranscationEntryWithRecordsAndSubRecords (String transactionId)
	{
		try 
		{           
		ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance().
				getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class);
		
		JSONObject transactionDetails = achTransactionBusinessDelegate.fetchTranscationEntryWithRecordsAndSubRecords(transactionId);
		JSONObject transaction = new JSONObject();
		JSONArray transactionArray = transactionDetails.getJSONArray("records");
		int i,size=transactionArray.length();
		transactionDetails = (JSONObject) transactionArray.get(0);
		if (transactionDetails.has("transaction_id")){transaction.put("transaction_id",transactionDetails.get("transaction_id"));}
		if (transactionDetails.has("fromAccount")){transaction.put("fromAccount",transactionDetails.get("fromAccount"));}
		if (transactionDetails.has("effectiveDate")){transaction.put("effectiveDate",transactionDetails.get("effectiveDate"));}
		if (transactionDetails.has("requestId")){transaction.put("requestId",transactionDetails.get("requestId"));}
		if (transactionDetails.has("createdby")){transaction.put("createdby",transactionDetails.get("createdby"));}
		if (transactionDetails.has("createdOn")){transaction.put("createdOn",transactionDetails.get("createdOn"));}
		if (transactionDetails.has("maxAmount")){transaction.put("maxAmount",transactionDetails.get("maxAmount"));}
		if (transactionDetails.has("status")){transaction.put("status",transactionDetails.get("status"));}
		if (transactionDetails.has("transactionType_id")){transaction.put("transactionType_id",transactionDetails.get("transactionType_id"));}
		if (transactionDetails.has("templateType_id")){transaction.put("templateType_id",transactionDetails.get("templateType_id"));}
		if (transactionDetails.has("companyId")){transaction.put("companyId",transactionDetails.get("companyId"));}
		if (transactionDetails.has("templateRequestType_id")){transaction.put("templateRequestType_id",transactionDetails.get("templateRequestType_id"));}
		if (transactionDetails.has("templateName")){transaction.put("templateName",transactionDetails.get("templateName"));}
		if (transactionDetails.has("confirmationNumber")){transaction.put("confirmationNumber",transactionDetails.get("confirmationNumber"));}
		if (transactionDetails.has("actedBy")){transaction.put("actedBy",transactionDetails.get("actedBy"));}
		if (transactionDetails.has("template_id")){transaction.put("template_id",transactionDetails.get("template_id"));}
		if (transactionDetails.has("totalAmount")){transaction.put("totalAmount",transactionDetails.get("totalAmount"));}
		if (transactionDetails.has("featureActionId")){transaction.put("featureActionId",transactionDetails.get("featureActionId"));}
		if (transactionDetails.has("templateRequestType")){transaction.put("templateRequestType",transactionDetails.get("templateRequestType"));}
		
		JSONArray records = new JSONArray();
		JSONObject record = new JSONObject();
		JSONArray subrecords = new JSONArray();
		JSONObject subrecord = new JSONObject();
		String recordId, subrecordId;
		recordId = transactionDetails.getString("transactionRecord_id");
		record.put("transactionRecord_id",recordId);
		record.put("toAccountNumber",transactionDetails.get("toAccountNumber"));
		record.put("toAccountType",transactionDetails.get("toAccountType"));
		if (transactionDetails.has("abatrcNumber")){record.put("abatrcNumber",transactionDetails.get("abatrcNumber"));}
		if (transactionDetails.has("detail_id")){record.put("detail_id",transactionDetails.get("detail_id"));}
		if (transactionDetails.has("amount")){record.put("amount",transactionDetails.get("amount"));}
		if (transactionDetails.has("additionalInfo")){record.put("additionalInfo",transactionDetails.get("additionalInfo"));}
		if (transactionDetails.has("eIN")){record.put("eIN",transactionDetails.get("eIN"));}
		if (transactionDetails.has("isZeroTaxDue")){record.put("isZeroTaxDue",transactionDetails.get("isZeroTaxDue"));}
		if (transactionDetails.has("taxType_id")){record.put("taxType_id",transactionDetails.get("taxType_id"));}
		if (transactionDetails.has("record_Name")){record.put("record_Name",transactionDetails.get("record_Name"));}
		if (transactionDetails.has("taxType")){record.put("taxType",transactionDetails.get("taxType"));}
		
		if (transactionDetails.has("transcationSubRecord_id"))
			{
				subrecordId = transactionDetails.getString("transcationSubRecord_id");
				subrecord.put("transcationSubRecord_id",subrecordId);
			if (transactionDetails.has("subrecordamount")){subrecord.put("amount",transactionDetails.get("subrecordamount"));}
			if (transactionDetails.has("taxSubCategory_id")){subrecord.put("taxSubCategory_id",transactionDetails.get("taxSubCategory_id"));}
			if (transactionDetails.has("taxSubType")){subrecord.put("taxSubType",transactionDetails.get("taxSubType"));}
			subrecords.put(subrecord);
			
			}
		
		String currRecId;
		for (i=1;i<size;i++)
		{
			
			transactionDetails = (JSONObject) transactionArray.get(i);
			currRecId = transactionDetails.getString("transactionRecord_id");
			if (currRecId.equals(recordId))
			{
				if (transactionDetails.has("transcationSubRecord_id"))
				{
				subrecord = new JSONObject();
				subrecordId = transactionDetails.getString("transcationSubRecord_id");
				subrecord.put("transcationSubRecord_id",subrecordId);
				if (transactionDetails.has("subrecordamount")){subrecord.put("amount",transactionDetails.get("subrecordamount"));}
				if (transactionDetails.has("taxSubCategory_id")){subrecord.put("taxSubCategory_id",transactionDetails.get("taxSubCategory_id"));}
				if (transactionDetails.has("taxSubType")){subrecord.put("taxSubType",transactionDetails.get("taxSubType"));}
				subrecords.put(subrecord);
				}
			}
			else 
			{
			record.put("subrecords",subrecords);
			records.put(record);
			record = new JSONObject();
			record.put("transactionRecord_id",currRecId);
			record.put("toAccountNumber",transactionDetails.get("toAccountNumber"));
			record.put("toAccountType",transactionDetails.get("toAccountType"));
			if (transactionDetails.has("abatrcNumber")){record.put("abatrcNumber",transactionDetails.get("abatrcNumber"));}
			if (transactionDetails.has("detail_id")){record.put("detail_id",transactionDetails.get("detail_id"));}
			if (transactionDetails.has("amount")){record.put("amount",transactionDetails.get("amount"));}
			if (transactionDetails.has("additionalInfo")){record.put("additionalInfo",transactionDetails.get("additionalInfo"));}
			if (transactionDetails.has("eIN")){record.put("eIN",transactionDetails.get("eIN"));}
			if (transactionDetails.has("isZeroTaxDue")){record.put("isZeroTaxDue",transactionDetails.get("isZeroTaxDue"));}
			if (transactionDetails.has("taxType_id")){record.put("taxType_id",transactionDetails.get("taxType_id"));}
			if (transactionDetails.has("record_Name")){record.put("record_Name",transactionDetails.get("record_Name"));}
			if (transactionDetails.has("taxType")){record.put("taxType",transactionDetails.get("taxType"));}
			subrecord = new JSONObject();
			subrecords = new JSONArray();
			if (transactionDetails.has("transcationSubRecord_id"))
			{
			subrecordId = transactionDetails.getString("transcationSubRecord_id");
			subrecord.put("transcationSubRecord_id",subrecordId);
			subrecord.put("amount",transactionDetails.get("subrecordamount"));
			subrecord.put("taxSubCategory_id",transactionDetails.get("taxSubCategory_id"));
			subrecord.put("taxSubType",transactionDetails.get("taxSubType"));
			subrecords.put(subrecord);
			}
			recordId = currRecId;
			}
		}
		record.put("subrecords",subrecords);
		records.put(record);
		transaction.put("records",records);
		return transaction;
		}
		catch(Exception e)
		{
			alert.prepareError("Error while marshalling transaction details").log();
			return null;
		}
	}
}
