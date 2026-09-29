package com.temenos.dbx.product.approvalservices.businessdelegate.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentRecordBusinessDelegate;
import com.temenos.dbx.product.constants.*;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.SignatoryGroupMatrixDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBActedRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.RequestHistoryDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.CustomerSignatoryGroupDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupRequestMatrixDTO;

/**
 * 
 * @author KH2174
 * @version 1.0
 * ApprovalQueueBusinessDelegateImpl implements {@link ApprovalQueueBusinessDelegate}
 *
 */
public class ApprovalQueueBusinessDelegateImpl implements ApprovalQueueBusinessDelegate{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	SignatoryGroupBusinessDelegate sigGrpBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
	BulkPaymentRecordBusinessDelegate bulkPaymentRecordBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentRecordBusinessDelegate.class);
	ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileBusinessDelegate.class);
	ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
	BillPayTransactionBusinessDelegate billPayTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
	P2PTransactionBusinessDelegate p2PTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
	InterBankFundTransferBusinessDelegate interBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
	IntraBankFundTransferBusinessDelegate intraBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
	DomesticWireTransactionBusinessDelegate domesticWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
	InternationalWireTransactionBusinessDelegate internationalWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
	InternationalFundTransferBusinessDelegate internationalFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
	OwnAccountFundTransferBusinessDelegate ownAccountFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);

	@Override
	public String addTransactionToApprovalQueue(BBRequestDTO bbrequestDTO) {

		//make a db call to bbrequest
		int requiredSets = bbrequestDTO.getApprovalMatrixIds().size();
		int receivedSets = 0;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_CREATE;

		Map<String, Object> requestParameters = null;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bbrequestDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		requestParameters.put("requiredSets",requiredSets);
		requestParameters.put("receivedSets",receivedSets);
		requestParameters.remove("requestId");
		
		String addTransactionResponse = null;

		try {
			//AAC-15315 date format change
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
			sdf.setTimeZone(TimeZone.getDefault());
			String createdts = sdf.format(new Date());
			//requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			//end of AAC-15315 date format change
			requestParameters.put("createdts", createdts);
			requestParameters.put("requestId", HelperMethods.getUniqueNumericString(13));
			UUID uuidValue = UUID.randomUUID();
			requestParameters.put("assocRequestId", uuidValue);
			
			addTransactionResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, null, "");
		} catch (Exception e) {
			alert.prepareError("Unable to create entry in bbreuqest: " + e).log();
			return null;
		}
		if(addTransactionResponse==null || addTransactionResponse.equals("")) {
			alert.prepareError("Unable to create entry in bbreuqest: ").log();
			return null;
		}

		JSONObject response = new JSONObject(addTransactionResponse);
		JSONArray resposneArray = response.getJSONArray("bbrequest");

		JSONObject addTransactionJSONObj  = resposneArray.getJSONObject(0);
		String requestId = (addTransactionJSONObj.has("requestId")) ? addTransactionJSONObj.getString("requestId") : null;

		//make a db call to add mappings to requestapprovalmatrix

		
		List<String> approvalMatrixIds = bbrequestDTO.getApprovalMatrixIds();
		for(String approvalMatrixId:approvalMatrixIds) {
			operationName = OperationName.DB_REQUESTAPPROVALMATRIX_CREATE;
			requestParameters = new HashMap<>();
			SignatoryGroupMatrixDTO sigApprovalMatrix = bbrequestDTO.getSignatoryGroupMatrices().get(approvalMatrixId);
			boolean isGroupRule = sigApprovalMatrix.isGroupMatrix();
			
			requestParameters.put("isGroupRule", isGroupRule);
			requestParameters.put("approvalMatrixId", approvalMatrixId);
			requestParameters.put("requestId",requestId);
			requestParameters.put("receivedApprovals", 0);
			String requestApprovalMatrixResponse = null;
			try {
				requestApprovalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationName, requestParameters, null, "");
			} catch (Exception e) {
				alert.prepareError("Unable to create entry in requestApprovalMatrix: " + e).log();
				deleteTransactionFromApprovalQueue(requestId);
				return null;
			}
			if(requestApprovalMatrixResponse==null || requestApprovalMatrixResponse.equals("")) {
				alert.prepareError("Unable to create entry in requestApprovalMatrix: ").log();
				deleteTransactionFromApprovalQueue(requestId);
				return null;
			}
			
			if(isGroupRule) {
                operationName = OperationName.DB_SIGNATORYGROUPREQUESTMATRIX_CREATE;
                String groupList = sigApprovalMatrix.getGroupList();
                String groupRuleValue = sigApprovalMatrix.getGroupRule();
                requestParameters = new HashMap<>();
               
                requestParameters.put("signatoryGroupRequestMatrixId", HelperMethods.getNewId());
                requestParameters.put("approvalMatrixId", approvalMatrixId);
                requestParameters.put("requestId",requestId);
                requestParameters.put("groupList", groupList);
                requestParameters.put("groupRuleValue", groupRuleValue);
                requestParameters.put("pendingGroupList", groupList);
                String sigGroupRequestMatrixResponse = null;
                
                try{
                	sigGroupRequestMatrixResponse = DBPServiceExecutorBuilder.builder().
                            withServiceId(serviceName).
                            withObjectId(null).
                            withOperationId(operationName).
                            withRequestParameters(requestParameters).
                            build().getResponse();
                   
                }catch(Exception e) {
                	alert.prepareError("Unable to create entry in sigGroupRequestMatrix: " + e).log();
                    return null;
                }
                
                if(sigGroupRequestMatrixResponse==null || sigGroupRequestMatrixResponse.equals("")) {
                    alert.prepareError("Unable to create entry in sigGroupRequestMatrix: ").log();
                    return null;
                }
                }
		}
		
		logActedRequest(requestId, bbrequestDTO.getCompanyId(), bbrequestDTO.getStatus(), "Request created", bbrequestDTO.getCreatedby(),
				bbrequestDTO.getStatus());

		return requestId;
	}
	
	@Override
	public String updateTransactionInApprovalQueue(BBRequestDTO bbrequestDTO) {

		//make a db call to bbrequest
		int requiredSets = bbrequestDTO.getApprovalMatrixIds().size();
		int receivedSets = 0;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_UPDATE;

		Map<String, Object> requestParameters = null;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bbrequestDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		requestParameters.put("requiredSets",requiredSets);
		requestParameters.put("receivedSets",receivedSets);
		
		String addTransactionResponse = null;

		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			
			addTransactionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
		} catch (Exception e) {
			alert.prepareError("Unable to create entry in bbreuqest: " + e).log();
			return null;
		}
		if(addTransactionResponse==null || addTransactionResponse.equals("")) {
			alert.prepareError("Unable to create entry in bbreuqest: ").log();
			return null;
		}

		JSONObject response = new JSONObject(addTransactionResponse);
		JSONArray resposneArray = response.getJSONArray("bbrequest");

		JSONObject addTransactionJSONObj  = resposneArray.getJSONObject(0);
		String requestId = (addTransactionJSONObj.has("requestId")) ? addTransactionJSONObj.getString("requestId") : null;

		
		requestParameters = new HashMap<String, Object>();
		String filter = "requestId" + DBPUtilitiesConstants.EQUAL + requestId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			addTransactionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(OperationName.DB_REQUESTAPPROVALMATRIX_DELETE).
					withRequestParameters(requestParameters).
					build().getResponse();
		}
		catch(Exception e) {
			alert.prepareError("Unable to delete requestApprovalMatrix Entries: " + e).log();
			return null;
		}
		
		try {
			addTransactionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(OperationName.DB_SIGNATORYGROUPREQUESTMATRIX_DELETE).
					withRequestParameters(requestParameters).
					build().getResponse();
		}
		catch(Exception e) {
			alert.prepareError("Unable to delete SignatoryGroupRequestMatrix Entries: " + e).log();
			return null;
		}
		
		//make a db call to add mappings to requestapprovalmatrix

		operationName = OperationName.DB_REQUESTAPPROVALMATRIX_CREATE;
		List<String> approvalMatrixIds = bbrequestDTO.getApprovalMatrixIds();
		for(String approvalMatrixId:approvalMatrixIds) {
			requestParameters = new HashMap<>();
			SignatoryGroupMatrixDTO sigApprovalMatrix = bbrequestDTO.getSignatoryGroupMatrices().get(approvalMatrixId);
			boolean isGroupRule = sigApprovalMatrix.isGroupMatrix();
			
			requestParameters.put("isGroupRule", isGroupRule);
			requestParameters.put("approvalMatrixId", approvalMatrixId);
			requestParameters.put("requestId",requestId);
			requestParameters.put("receivedApprovals", 0);
			String requestApprovalMatrixResponse = null;
			try {
				requestApprovalMatrixResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationName, requestParameters, null, "");
			} catch (Exception e) {
				alert.prepareError("Unable to create entry in requestApprovalMatrix: " + e).log();
				deleteTransactionFromApprovalQueue(requestId);
				return null;
			}
			if(requestApprovalMatrixResponse==null || requestApprovalMatrixResponse.equals("")) {
				alert.prepareError("Unable to create entry in requestApprovalMatrix: ").log();
				deleteTransactionFromApprovalQueue(requestId);
				return null;
			}
			
			if(isGroupRule) {
                operationName = OperationName.DB_SIGNATORYGROUPREQUESTMATRIX_CREATE;
                String groupList = sigApprovalMatrix.getGroupList();
                String groupRuleValue = sigApprovalMatrix.getGroupRule();
                requestParameters = new HashMap<>();
               
                requestParameters.put("signatoryGroupRequestMatrixId", HelperMethods.getNewId());
                requestParameters.put("approvalMatrixId", approvalMatrixId);
                requestParameters.put("requestId",requestId);
                requestParameters.put("groupList", groupList);
                requestParameters.put("groupRuleValue", groupRuleValue);
                requestParameters.put("pendingGroupList", groupList);
                String sigGroupRequestMatrixResponse = null;
                
                try{
                	sigGroupRequestMatrixResponse = DBPServiceExecutorBuilder.builder().
                            withServiceId(serviceName).
                            withObjectId(null).
                            withOperationId(operationName).
                            withRequestParameters(requestParameters).
                            build().getResponse();
                   
                }catch(Exception e) {
                	alert.prepareError("Unable to create entry in sigGroupRequestMatrix: " + e).log();
                    return null;
                }
                
                if(sigGroupRequestMatrixResponse==null || sigGroupRequestMatrixResponse.equals("")) {
                    alert.prepareError("Unable to create entry in sigGroupRequestMatrix: ").log();
                    return null;
                }
            }
		}
		
		// update old request's softdeleteflag 
		
		String operation = OperationName.DB_BBACTEDREQUEST_UPDATE;

		Map<String, Object> request = new HashMap <String,Object>();;

		 request.put("_requestId", bbrequestDTO.getRequestId());
	        
		String serviceresponse = null;

		try {	
			serviceresponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operation).
					withRequestParameters(request).
					build().getResponse();  		
		} catch (Exception e) {
			alert.prepareError("Unable to update entry in bbactedrequest: " + e).log();
			return null;
		}
		
		logActedRequest(requestId, bbrequestDTO.getCompanyId(), bbrequestDTO.getStatus(), "Request created", bbrequestDTO.getCreatedby(),
				bbrequestDTO.getStatus());

		return requestId;
	}

	@Override
	public boolean deleteTransactionFromApprovalQueue(String requestId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_DELETE;

		String deleteResponse = "";
		Map<String, Object> requestParams = new HashMap<String, Object>();
		try {

			requestParams.put("requestId", requestId);

			deleteResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParams, null, "");

			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching actions of a request", exp).log();
			return false;
		}
		return false;
	}

	@Override
	public List<RequestHistoryDTO> fetchRequestHistory(String requestId, String customerId) {

		List<RequestHistoryDTO> actions;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_REQUEST_HISTORY_PROC;

		String fetchResponse = "";
		Map<String, Object> requestParams = new HashMap<String, Object>();
		try {

			requestParams.put(Constants._REQUESTID, requestId);
			requestParams.put(Constants._CUSTOMER_ID, customerId);

			fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParams, null, "");

			JSONObject fetchRes = new JSONObject(fetchResponse);
			JSONArray response = fetchRes.getJSONArray(Constants.RECORDS);
			actions = JSONUtils.parseAsList(response.toString(), RequestHistoryDTO.class);
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching actions of a request", exp).log();
			return null;
		}
		return actions;
	}
	
	@Override
	public List<BBActedRequestDTO> fetchRequestHistory(String customerId, Set<String> requestIds) {

		List<BBActedRequestDTO> actions;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBACTEDREQUEST_GET;
		
		String filter = "";
		
		if (CollectionUtils.isNotEmpty(requestIds))
			filter = DBPUtilitiesConstants.OPEN_BRACE + 
			"requestId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "requestId" + DBPUtilitiesConstants.EQUAL, requestIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;
		
		if(StringUtils.isNotEmpty(filter) && StringUtils.isNotEmpty(customerId))
			filter = filter + DBPUtilitiesConstants.AND;
		
		if(StringUtils.isNotEmpty(customerId))
			filter = filter + "createdby" + DBPUtilitiesConstants.EQUAL + customerId;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		try {
			
			requestParams.put(DBPUtilitiesConstants.FILTER, filter);
			requestParams.put(DBPUtilitiesConstants.ORDERBY, "createdts");

			String fetchResponse = DBPServiceExecutorBuilder.builder().
							withServiceId(serviceName).
							withObjectId(null).
							withOperationId(operationName).
							withRequestParameters(requestParams).
							build().getResponse();

			JSONObject fetchRes = new JSONObject(fetchResponse);
			JSONArray response = CommonUtils.getFirstOccuringArray(fetchRes);
			actions = JSONUtils.parseAsList(response.toString(), BBActedRequestDTO.class);
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching actions of a request", exp).log();
			return null;
		}
		return actions;
	}

	@Override
	public BBRequestDTO updateBBRequestCounter(String requestId, int counter, DataControllerRequest dcr) {

		String fetchResponse = new String();
		JSONObject records = null;
		BBRequestDTO bBRequestDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_UPDATECOUNTER_PROC;

		// ADP-7058 - fetch additional meta data while updating the bbrequest status----------------------------------
		String additionalMetaString = null;
		if(dcr != null){
			ApprovalRequestDTO additionalMeta = this.fetchAdditionalBackendData(requestId, dcr);

			if(additionalMeta != null){
				try{
					additionalMetaString = new Gson().toJson(additionalMeta);
				}catch(Exception e){
					alert.prepareError(e.toString()).log();
				}

			}
		}

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_requestId", requestId);
		requestParams.put("_counter", "" + counter);
		requestParams.put("_additionalMeta", additionalMetaString);

		try {
			fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParams, null, "");
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" + e).log();
		}
		try {
			JSONObject fetchResponseJSON = new JSONObject(fetchResponse);
			
			records = fetchResponseJSON.getJSONArray("records").getJSONObject(0);
			bBRequestDTO = JSONUtils.parse(records.toString(), BBRequestDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" + e).log();
			return null;
		}		

		// handled by bbrequest_updatecounter_proc internally - hence commenting
//		if((records.getInt("requiredSets") <= records.getInt("receivedSets")) && records.getString("status").equals(TransactionStatusEnum.PENDING.getStatus())) {
//			bBRequestDTO = updateBBRequestStatus(requestId, TransactionStatusEnum.APPROVED.getStatus(), dcr);
//		}
		return bBRequestDTO;

	}

	@Override
	public BBRequestDTO updateBBRequestStatus(String requestId, String status, DataControllerRequest dcr) {

		String fetchResponse = new String();
		JSONArray record = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_UPDATESTATUS_PROC;

		// ADP-7058 - fetch additional meta data while updating the bbrequest status----------------------------------
		String additionalMetaString = null;
		if(dcr != null){
			ApprovalRequestDTO additionalMeta = this.fetchAdditionalBackendData(requestId, dcr);

			if(additionalMeta != null){
				try{
					additionalMetaString = new Gson().toJson(additionalMeta);
				}catch(Exception e){
					alert.prepareError(e.toString()).log();
				}

			}
		}

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_requestId", requestId);
		requestParams.put("_status", status);	
		requestParams.put("_additionalMeta", additionalMetaString);
		try {
			fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParams, null, "");
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" + e).log();
			return null;
		}
		try {
			JSONObject fetchResponseJSON = new JSONObject(fetchResponse);
			record = fetchResponseJSON.getJSONArray("records");
			BBRequestDTO bBRequestDTO = JSONUtils.parse(record.getJSONObject(0).toString(), BBRequestDTO.class);

			return bBRequestDTO;
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" + e).log();
			return null;
		}

	}
	
	@Override
	public boolean logActedRequest(String requestId,String companyId, String status,String comments,String createdby,String actions, String groupName) {

		String fetchResponse = new String();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBACTEDREQUEST_CREATE_PROC;

		JSONObject requestObj = new JSONObject();
		requestObj.put("requestId", requestId);
		requestObj.put("companyId", companyId);
		requestObj.put("status", status);
		requestObj.put("comments", comments);
		requestObj.put("createdby", createdby);
		requestObj.put("action", actions);
		requestObj.put("groupName", groupName);

		Map<String, Object> payload = new HashMap<>();
		payload.put("_input", requestObj.toString());

		try {
			fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, payload, null, "");
			JSONObject fetchResponseJSON = new JSONObject(fetchResponse);
			if(fetchResponseJSON.has("opstatus") && 0 == fetchResponseJSON.optInt("opStatus")) {
				return true;
			}
			return false;
		}
		catch (Exception e) {
			alert.prepareError("Error while invoking DBService" + e).log();
			return false;
		}
	}	
	
	@Override
	public boolean logActedRequest(String requestId,String companyId, String status,String comments,String createdby,String actions) {
		return logActedRequest(requestId,companyId,status,comments,createdby,actions, null);
	}	
	
	@Override
	public Object approveACHTransaction(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr) {
		int counter = callFetchRequestProc(requestId, customerId, comments, companyId);
		BBRequestDTO bBRequestDTO = null;
		
		if (counter == -1) {
			return null;
		} 
		else {

			bBRequestDTO = updateBBRequestCounter(requestId, counter, dcr);

			if (bBRequestDTO != null && bBRequestDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {

				ACHTransactionBusinessDelegate aCHTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ACHTransactionBusinessDelegate.class);

				ACHTransactionDTO aCHTransactionDTO = aCHTransactionBusinessDelegate
						.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), null);
				
				if (aCHTransactionDTO == null) {
					aCHTransactionDTO=new ACHTransactionDTO();
				}
				aCHTransactionDTO.setTransaction_id(bBRequestDTO.getTransactionId());
				aCHTransactionDTO.setFeatureActionId(bBRequestDTO.getFeatureActionId());
				return aCHTransactionDTO;
				

			}
			else {
				return bBRequestDTO;
			}
		}
	}
	
	@Override
	public Object approveACHFile(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr) {
		int counter = callFetchRequestProc(requestId, customerId, comments, companyId);
		BBRequestDTO bBRequestDTO = null;

		if (counter == -1) {
			return null;
		} else {
			
			bBRequestDTO = updateBBRequestCounter(requestId, counter, dcr);

			if (bBRequestDTO != null && bBRequestDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {

				ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ACHFileBusinessDelegate.class);

				ACHFileDTO achFileDTO = achFileBusinessDelegate.updateStatus(
						bBRequestDTO.getTransactionId(), bBRequestDTO.getStatus(), null);

				if (achFileDTO == null) {
					achFileDTO=new ACHFileDTO();
				} 
				achFileDTO.setAchFile_id(bBRequestDTO.getTransactionId());
				achFileDTO.setFeatureActionId(bBRequestDTO.getFeatureActionId());
				return achFileDTO;
				

			} else {
				return bBRequestDTO;
			}
		}
	}
	
	@Override
	public int callFetchRequestProc(String requestId, String customerId, String comments, String companyId) {
		BBRequestDTO requestDTO = getBbRequest(requestId);
		if(requestDTO == null){
			alert.prepareError("Error while fetching BBRequest details").log();
			return -1;
		}
		boolean isGroupMatrix = Boolean.parseBoolean(requestDTO.getIsGroupMatrix());
		
		int counter;
		if(isGroupMatrix) {
			counter = callFetchRequestProcWithSignatoryGroup(requestId, customerId, comments, companyId);
		}
		else {
			counter = callFetchRequestProcWithoutSignatoryGroup(requestId,customerId);
			if(counter != -1) {
				logActedRequest(requestId, companyId, TransactionStatusEnum.APPROVED.getStatus(), comments, customerId,
					TransactionStatusEnum.APPROVED.getStatus());
			}
		}
		return counter;
	}
	
	private int callFetchRequestProcWithoutSignatoryGroup(String requestId, String customerId) {
		List<BBRequestDTO> bbRequestDTOList = fetchRequestApprovalMatrixDetails(requestId, customerId);
		if(bbRequestDTOList == null || bbRequestDTOList.isEmpty()) {
			return -1;
		}
		
		List<String> reqAppMatIdsList = bbRequestDTOList.stream().map(BBRequestDTO::getRequestApprovalMatrixId).collect(Collectors.toList());
		
		String reqAppMatIdsString = String.join(",", reqAppMatIdsList);
		if(!updateRequestApprovalMatrix(reqAppMatIdsString)) {
			alert.prepareError("Failed to increment received approvals in requestapprovalmatrix").log();
			return -1;
		}
		
		int count = 0;
		
		for(BBRequestDTO bbRequestDTO : bbRequestDTOList) {
			int receivedApprovals = Integer.valueOf(bbRequestDTO.getReceivedApprovals());
			int numberOfApprovals = Integer.valueOf(bbRequestDTO.getNumberOfApprovals());
			
			if(receivedApprovals >= numberOfApprovals)
				continue;
			
			if(receivedApprovals + 1 >= numberOfApprovals)
				count++;
		}
		
		return count;
	}
	
	private List<BBRequestDTO> fetchRequestApprovalMatrixDetails(String requestId, String customerId) {
		List<BBRequestDTO> bbRequestDTOList = new ArrayList<BBRequestDTO>();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_REQUESTAPPROVALMATRIX_DETAILS_PROC;

		HashMap<String, Object> requestParameters = new HashMap<>();

        requestParameters.put("_customerId", customerId);
		requestParameters.put("_requestId", requestId);
        
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			bbRequestDTOList = JSONUtils.parseAsList(jsonArray.toString(), BBRequestDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONException occured in fetchRequestApprovalMatrixDetails" + jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured in fetchRequestApprovalMatrixDetails" + exp).log();
			return null;
		}
		
		return bbRequestDTOList;
	}
	
	
	/*
	 * Method to approve a signatory group request
	 */
	private int callFetchRequestProcWithSignatoryGroup(String requestId, String customerId, String comments, String companyId) {
		List<CustomerSignatoryGroupDTO> customerSignatoryGroupDTOs =  sigGrpBusinessDelegate.fetchCustomerSignatoryGroups(customerId); 
		if(customerSignatoryGroupDTOs == null || customerSignatoryGroupDTOs.size() == 0) {
			alert.prepareError("User is not associated to any signatory group").log();
			return -1;
		}
		
		Map<String,String> sigGrpIdNameMap = new HashMap<String,String>();
		for(CustomerSignatoryGroupDTO cusSigGrpDTO :customerSignatoryGroupDTOs) {
			sigGrpIdNameMap.put(cusSigGrpDTO.getSignatoryGroupId(), cusSigGrpDTO.getSignatoryGroupName());
		}
		
		Set<String> customerGroupIds = sigGrpIdNameMap.keySet();
		
		List<SignatoryGroupRequestMatrixDTO> signatoryGroupRequestMatrixDTOs =  sigGrpBusinessDelegate.fetchPendingSignatoryGroupRequestMatrix(requestId);
		if(signatoryGroupRequestMatrixDTOs == null || signatoryGroupRequestMatrixDTOs.size() == 0) {
			alert.prepareError("User is not associated to any pending signatory group").log();
			return -1;
		}
		
		Set<String> customerGroupNames = new HashSet<String>();
		
		int counter = 0;
		boolean isValid = false;
		
		for(SignatoryGroupRequestMatrixDTO sigGrpReqMatrixDTO :signatoryGroupRequestMatrixDTOs) {
			List<String> pendingGroupList = getListFromString(sigGrpReqMatrixDTO.getPendingGroupList());
			List<String> actualGroupList = getListFromString(sigGrpReqMatrixDTO.getGroupList());
			
			if(pendingGroupList == null || pendingGroupList.isEmpty()) {
				alert.prepareError("Not a valid Pending Group List").log();
				return -1;
			}
			
			if(actualGroupList == null || actualGroupList.isEmpty()) {
				alert.prepareError("Not a valid Group List").log();
				return -1;
			}
			
			List<Integer> groupColumnIndices = new ArrayList<Integer>();
			for(String id : customerGroupIds) {
				if(!pendingGroupList.contains(id)) {
					continue;
				}
				int index = actualGroupList.indexOf(id);
				if(index > -1) {
					customerGroupNames.add(sigGrpIdNameMap.get(id));
					groupColumnIndices.add(index);
				}
			}
			if(groupColumnIndices.isEmpty()) {
				continue;
			}
			
			int[][] groupRuleValueMatrix = get2DMatrixFromString(sigGrpReqMatrixDTO.getGroupRuleValue());
			if(groupRuleValueMatrix == null || groupRuleValueMatrix.length <= 0) {
				alert.prepareError("Not a valid Group Rule Value").log();
				return -1;
			}
			
			int[][] updatedGroupRuleValueMatrix = decrementValues(groupRuleValueMatrix, groupColumnIndices);
			pendingGroupList = getPendingGroupList(actualGroupList,updatedGroupRuleValueMatrix);
			
			String updatedGroupRuleValueString = Arrays.deepToString(updatedGroupRuleValueMatrix);
			
			SignatoryGroupRequestMatrixDTO updatedGrpReqDTO = new SignatoryGroupRequestMatrixDTO();
			updatedGrpReqDTO.setSignatoryGroupRequestMatrixId(sigGrpReqMatrixDTO.getSignatoryGroupRequestMatrixId());
			updatedGrpReqDTO.setPendingGroupList(pendingGroupList.toString().replaceAll("\\s+",""));
			updatedGrpReqDTO.setGroupRuleValue(updatedGroupRuleValueString);
			
			if(isAnyApprovalRuleSatisfied(updatedGroupRuleValueMatrix)) {
				updatedGrpReqDTO.setApproved(true);
				if(!incrementReceivedApprovalsInRequestMatrix(sigGrpReqMatrixDTO.getRequestId(), sigGrpReqMatrixDTO.getApprovalMatrixId())) {
					alert.prepareError("Failed to increment received approvals in requestapprovalmatrix").log();
					return -1;
				}
				counter++;
			}
			
			if(!sigGrpBusinessDelegate.updateSignatoryGroupRequestMatrix(updatedGrpReqDTO)) {
				alert.prepareError("Failed to update signatorygrouprequestmatrix").log();
				return -1;
			}
			
			isValid = true;
		}
		
		if(!isValid) {
			alert.prepareError("Not a valid request or user is not authorized to apaprove this request").log();
			return -1;
		}
		
		logActedRequest(requestId, companyId, TransactionStatusEnum.APPROVED.getStatus(), comments, customerId,
				TransactionStatusEnum.APPROVED.getStatus(), String.join(", ", customerGroupNames));
		
		return counter;
	}

	/*
	 * Parse String value to List
	 */
	private List<String> getListFromString(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		str = str.replaceAll("\\s+","");
		str = str.substring(1, str.length()-1);
		return Arrays.asList(str.split(","));
	}

	/*
	 * Parse String value to 2D Matrix
	 */
	private int[][] get2DMatrixFromString(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		str = str.replaceAll("\\s+","");
		str = str.replace("[", "");
		str = str.substring(0, str.length() - 2);
		String strList[] = str.split("],");

		int rowLength = strList.length;
		int colLength = strList[0].split(",").length;
		int matrix[][] = new int[rowLength][colLength];

		for (int i = 0; i < rowLength; i++) {
			String single_int[] = strList[i].split(",");
			for (int j = 0; j < colLength; j++) {
				matrix[i][j] = Integer.parseInt(single_int[j]);
			}
		}

		return matrix;
	}

	/*
	 * Verifies if atleast one row has all zero values (Satisfies Approval Rule)
	 */
	private boolean isAnyApprovalRuleSatisfied(int[][] updatedGroupRuleValueMatrix) {
		for (int row = 0; row < updatedGroupRuleValueMatrix.length; row++) {
			int count = 0; 
			for (int col = 0; col < updatedGroupRuleValueMatrix[row].length; col++) {
				if(updatedGroupRuleValueMatrix[row][col] == 0) {
					count++;
				}
			}
			if(count == updatedGroupRuleValueMatrix[row].length) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Removes Group Id from actualGroupList to get the pending group list
	 */
	private List<String> getPendingGroupList(List<String> actualGroupList, int[][] updatedGroupRuleValueMatrix) {
		int rowLength = updatedGroupRuleValueMatrix.length;
		int columnLength = updatedGroupRuleValueMatrix[0].length;
		List<Integer> col = new ArrayList<Integer>();
		for (int i = 0; i < columnLength; i++) {
			int count = 0; 
			for (int j = 0; j < rowLength; j++) {
				if(updatedGroupRuleValueMatrix[j][i] == 0) {
					count++;
				}
			}
			if(count == rowLength) {
				col.add(i);
			}
		}
		
		List<String> pendingGroupIds = new ArrayList<String>();
		for(int i = 0; i< actualGroupList.size(); i++) {
			if(!col.contains(i)) {
				pendingGroupIds.add(actualGroupList.get(i));
			}
		}
		
		return pendingGroupIds;
	}

	/*
	 * Decrements matrix element value based on groupColumnIndices
	 */
	private int[][] decrementValues(int[][] groupRuleValueMatrix, List<Integer> groupColumnIndices) {
		for (int i = 0; i < groupRuleValueMatrix.length; i++) {
			for (int j = 0; j < groupColumnIndices.size(); j++) {
				if(groupRuleValueMatrix[i][groupColumnIndices.get(j)] > 0) {
					groupRuleValueMatrix[i][groupColumnIndices.get(j)] = groupRuleValueMatrix[i][groupColumnIndices.get(j)] - 1;
				}
			}
		}
		return groupRuleValueMatrix;
	}

	@Override
	public Object approveGeneralTransaction(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr) {
		int counter = callFetchRequestProc(requestId, customerId, comments, companyId);
		BBRequestDTO bBRequestDTO = null;
		
		if (counter == -1) {
			return null;
		} 
		else {

			bBRequestDTO = updateBBRequestCounter(requestId, counter, dcr);

			if (bBRequestDTO != null && bBRequestDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {

				GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
				
				Boolean status = generalTransactionBusinessDelegate.updateStatus(bBRequestDTO.getTransactionId(), bBRequestDTO.getFeatureActionId(), bBRequestDTO.getStatus());
				

					JSONObject response = new JSONObject();
					response.put("Transaction_id",bBRequestDTO.getTransactionId());
					response.put("FeatureAction_id",bBRequestDTO.getFeatureActionId());
					response.put("createdBy", bBRequestDTO.getCreatedby());
					return response;

				

			}
			else {
				return bBRequestDTO;
			}
		}
	}
	

	@Override
	public BBRequestDTO authorizationCheckForRejectAndWithdrawl(String requestId, String companyId, String featureactionlist) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_AUTHORIZATIONCHECKFORREJECTANDWITHDRAWL_PROC;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		requestParameters.put("_companyId", companyId);
		requestParameters.put("_requestId", requestId);
		requestParameters.put("_featureactionlist", featureactionlist);

		try {
			String response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject responseJson = new JSONObject(response);
			if (responseJson.has(Constants.RECORDS)
					&& responseJson.getJSONArray(Constants.RECORDS).length() > 0) {
				diagnostic.prepareInfo("Request details fetched successfully").log();
				BBRequestDTO bbRequestDTO = JSONUtils.parse(responseJson.getJSONArray(Constants.RECORDS).getJSONObject(0).toString(), BBRequestDTO.class);
				return bbRequestDTO;
			} else {
				alert.prepareError("AUTHORIZATIONCHECKFORREJECTANDWITHDRAWL_PROC resulted no records ").log();
				return null;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to Call AUTHORIZATIONCHECKFORREJECTANDWITHDRAWL_PROC: " +e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at _authorizationCheckForRejectAndWithdrawl method: " +e).log();
			return null;
		}
	}
	
	
	@Override
	public Boolean checkIfUserAlreadyApproved(String requestId, String customerId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBACTEDREQUEST_GET;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		StringBuilder filter = new StringBuilder();
		filter.append("requestId").append(DBPUtilitiesConstants.EQUAL).append(requestId);
		filter.append(DBPUtilitiesConstants.AND).append("createdby").append(DBPUtilitiesConstants.EQUAL).append(customerId);
		filter.append(DBPUtilitiesConstants.AND).append("status").append(DBPUtilitiesConstants.EQUAL).append(TransactionStatusEnum.APPROVED);
		filter.append(DBPUtilitiesConstants.AND).append("softdeleteflag").append(DBPUtilitiesConstants.EQUAL).append("0");
		requestParameters.put("$filter", filter.toString());

		try {
			String response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject responseJson = new JSONObject(response);
			if (responseJson.has(Constants.BBACTEDREQUEST)
					&& responseJson.getJSONArray(Constants.BBACTEDREQUEST).length() == 0) {
				diagnostic.prepareInfo("Customer never approved before").log();
				return false;
			} else {
				alert.prepareError("Customer may have approved before").log();
				return true;
			}
		} catch (JSONException e) {
			alert.prepareError("Caught exception at checkIfUserAlreadyApproved method: " +e).log();
			return true;
		} catch (Exception e) {
			alert.prepareError("Caught exception at checkIfUserAlreadyApproved method: " +e).log();
			return true;
		}
	}
	
	@Override
	public String getRequestStatus(String requestId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_GET;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		StringBuilder filter = new StringBuilder();
		filter.append("requestId").append(DBPUtilitiesConstants.EQUAL).append(requestId);
		requestParameters.put("$filter", filter.toString());

		try {
			String response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject responseJson = new JSONObject(response);
			if (responseJson.has(Constants.BBREQUEST)
					&& responseJson.getJSONArray(Constants.BBREQUEST).length() > 0) {
				diagnostic.prepareInfo("Fetch Request details success").log();
				return responseJson.getJSONArray(Constants.BBREQUEST).getJSONObject(0).getString("status");
			} else {
				alert.prepareError("Fetch Request details failed").log();
				return null;
			}
		} catch (JSONException e) {
			alert.prepareError("Caught exception at getRequestStatus method: " +e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getRequestStatus method: " +e).log();
			return null;
		}
	}

	@Override
	public boolean autoRejectInvalidPendingTransactionsWhenApproverPermissionIsRevoked(String customerId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_AUTO_REJECT_INVALID_PENDING_TRANSCATIONS_PROC;

		String response = null;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		try {

			requestParams.put("_customerId", customerId);
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(response);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 ) {
				return true;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching actions of a request", exp).log();
			return false;
		}
		return false;
	}
	
	@Override
	public List<BBRequestDTO> fetchRequests(String customerId, String transactionId, String requestId, String featureActionlist) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_APPROVALQUEUE_PROC;
		List<BBRequestDTO> approvalrequests = new ArrayList<BBRequestDTO>();

		String response = null;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_customerId", customerId);
		requestParams.put("_transactionIds", transactionId);
		requestParams.put("_requestIds", requestId);
		requestParams.put("_featureactionlist", featureActionlist);
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(response);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 ) {
				JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
				approvalrequests = JSONUtils.parseAsList(jsonArray.toString(), BBRequestDTO.class);
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while fetching Approval Queue", e).log();
			return approvalrequests;
		}
		return approvalrequests;
	}
	
	@Override
	public List<BBRequestDTO> fetchPendingRequests(String customerId, String transactionId, String requestId, String featureActionlist) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_PENDING_APPROVALSQUEUE_PROC;
		List<BBRequestDTO> approvalrequests = new ArrayList<BBRequestDTO>();

		String response = null;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_customerId", customerId);
		requestParams.put("_transactionIds", transactionId);
		requestParams.put("_requestIds", requestId);
		requestParams.put("_featureactionlist", featureActionlist);
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(response);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 ) {
				JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
				approvalrequests = JSONUtils.parseAsList(jsonArray.toString(), BBRequestDTO.class);
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while fetching Approval Queue", e).log();
			return approvalrequests;
		}
		return approvalrequests;
	}

	@Override
	public JSONArray fetchApprovers(String requestId, String customerId) { 
		BBRequestDTO requestDTO = getBbRequest(requestId);
		if(requestDTO == null){
			alert.prepareError("Error while fetching BBRequest details").log();
			return null;
		}
		
		boolean isGroupMatrix = Boolean.parseBoolean(requestDTO.getIsGroupMatrix());
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "";
		if(!isGroupMatrix) {
			operationName = OperationName.DB_FETCH_APPROVERS_PROC;
		}
		else {
			operationName = OperationName.DB_FETCH_PENDING_GROUP_LIST_PROC;
		}
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_requestId", requestId);
		
		JSONArray jsonArray = null;
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(response);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 ) {
				jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while fetching Approvers", e).log();
		}
		return jsonArray;
	}

	/**
	 * @author sribarani.vasthan
	 * Start: Added as part of ADP-2810
	 * @param requestId
	 */
	@Override
	public BBRequestDTO getBbRequest(String requestId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_GET;
		HashMap<String, Object> requestParameters = new HashMap<>();

		StringBuilder filter = new StringBuilder();
		filter.append("requestId").append(DBPUtilitiesConstants.EQUAL).append(requestId);
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter.toString());

		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONArray bbRequestArray = CommonUtils.getFirstOccuringArray(new JSONObject(response));
			if (bbRequestArray == null) {
				filter = new StringBuilder();
				filter.append("assocRequestId").append(DBPUtilitiesConstants.EQUAL).append(requestId);
				requestParameters.put(DBPUtilitiesConstants.FILTER, filter.toString());
				response = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						withRequestParameters(requestParameters).
						build().getResponse();
				bbRequestArray = CommonUtils.getFirstOccuringArray(new JSONObject(response));
			}
			List<BBRequestDTO> bbRequestList = JSONUtils.parseAsList(bbRequestArray.toString(), BBRequestDTO.class);
			if(CollectionUtils.isNotEmpty(bbRequestList)) {
				return bbRequestList.get(0);
			}
			return null;
		} catch (JSONException e) {
			alert.prepareError("Exception caught at getBbRequest method: " + e).log();
		} catch (Exception e) {
			alert.prepareError("Exception caught at getBbRequest method: " + e).log();
		}
		return null;
	}
	
	@Override
	public boolean updateBBRequestTransactionId(String requestId, String transactionId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_UPDATE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("requestId", requestId);
		requestParams.put("transactionId", transactionId);
		
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("updatedRecords") == 1) {
				return true;
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the bbrequest",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the bbrequest",exp).log();
			return false;
		}
		
		return false;
	}
	
	@Override
	public BBRequestDTO approve(String requestId, String comments, String featureActionId, DataControllerRequest request) {
		BBRequestDTO bbRequestDTO = null;
		
		String serviceName = ServiceId.DBP_APPROVAL_REQUEST_SERVICES;
        String operationName = OperationName.APPROVE;
        
        Map<String, Object> approveInput = new HashMap<String, Object>();
		approveInput.put("requestId", requestId);
		approveInput.put("comments", comments);
		approveInput.put("featureActionId", featureActionId);
		
		try {
			String approveResponse = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(approveInput).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(approveResponse);
			bbRequestDTO = JSONUtils.parse(jsonRsponse.toString(), BBRequestDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
			return null;
		}
		
		return bbRequestDTO;
	}
	
	@Override
	public TransactionStatusDTO validateForApprovals(TransactionStatusDTO transactionStatusDTO, DataControllerRequest request) {
		TransactionStatusDTO statusDTO = null;
		
		String serviceName = ServiceId.DBP_APPROVAL_REQUEST_SERVICES;
        String operationName = OperationName.VALIDATE_FOR_APPROVALS;
        
        String status = transactionStatusDTO.getStatus() == null? null : transactionStatusDTO.getStatus().toString();
        transactionStatusDTO.setStatus(null);
        

        String offsetDetails = transactionStatusDTO.getOffsetDetails() == null? null : new JSONObject(transactionStatusDTO.getOffsetDetails()).toString();
        transactionStatusDTO.setOffsetDetails(null);
        
        Map<String, Object> requestParameters = null;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(transactionStatusDTO).toString(), String.class, Object.class);
			requestParameters.put("status", status);
			requestParameters.put("offsetDetails", offsetDetails);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			statusDTO = JSONUtils.parse(jsonRsponse.toString(), TransactionStatusDTO.class);
			if(jsonRsponse.has("isSelfApproved") && jsonRsponse.get("isSelfApproved") != null && StringUtils.isNotBlank(jsonRsponse.get("isSelfApproved").toString())) {
				statusDTO.setSelfApproved(Boolean.parseBoolean(jsonRsponse.get("isSelfApproved").toString()));
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
			return null;
		}

		return statusDTO;
	}
	
	@Override
	public TransactionStatusDTO updateBackendIdInApprovalQueue(String requestId, String transactionId,  boolean isSelfApproved,  String featureActionId, DataControllerRequest request) {
		TransactionStatusDTO statusDTO = null;
		
		String serviceName = ServiceId.DBP_APPROVAL_REQUEST_SERVICES;
        String operationName = OperationName.UPDATE_BACKENDID_IN_APPROVALQUEUE;
        if(transactionId.startsWith(Constants.REFERENCE_KEY)){
        	transactionId=transactionId.replace(Constants.REFERENCE_KEY,"");
        }
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);
        requestParameters.put("transactionId", transactionId);
        requestParameters.put("isSelfApproved", isSelfApproved);
        requestParameters.put("featureActionId", featureActionId);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			statusDTO = JSONUtils.parse(jsonRsponse.toString(), TransactionStatusDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
			return null;
		}

		return statusDTO;
	}

	@Override
	public Object approveChequeBookrequest(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr) {
		int counter = callFetchRequestProc(requestId, customerId, comments, companyId);
		BBRequestDTO bBRequestDTO = null;
		
		if (counter == -1) {
			return null;
		} 
		else {

			bBRequestDTO = updateBBRequestCounter(requestId, counter, dcr);

			if (bBRequestDTO != null && bBRequestDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {

					JSONObject response = new JSONObject();
					response.put("Transaction_id",bBRequestDTO.getTransactionId());
					response.put("FeatureAction_id",bBRequestDTO.getFeatureActionId());
					response.put("createdBy", bBRequestDTO.getCreatedby());
					return response;
			}
			else {
				return bBRequestDTO;
			}
		}

	}
	@Override
	public Object approveNewActionTypes(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr) {
		int counter = callFetchRequestProc(requestId, customerId, comments, companyId);
		BBRequestDTO bBRequestDTO = null;
		
		if (counter == -1) {
			return null;
		} 
		else {

			bBRequestDTO = updateBBRequestCounter(requestId, counter, dcr);

			if (bBRequestDTO != null && bBRequestDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {

					JSONObject response = new JSONObject();
					response.put("Transaction_id",bBRequestDTO.getTransactionId());
					response.put("FeatureAction_id",bBRequestDTO.getFeatureActionId());
					response.put("createdBy", bBRequestDTO.getCreatedby());
					return response;
			}
			else {
				return bBRequestDTO;
			}
		}

	}

	@Override
	public void executeChequeBookRequestAfterApproval(String requestId,String transactionId, String featureActionId,
			DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBP_CHEQUEMANAGEMENT_SERVICES;
        String operationName = OperationName.APPROVE_CHEQUEBOOK_REQUEST;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("featureActionId", featureActionId);
        requestParameters.put("serviceRequestId", transactionId);
        requestParameters.put("requestId", requestId);
        
		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(dcr.getHeaderMap()).
	        		withDataControllerRequest(dcr).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
		}

	}

	@Override
	public Boolean withdrawRequest(String requestId, String comments, DataControllerRequest request) {
		String serviceName = ServiceId.DBP_CHEQUEMANAGEMENT_SERVICES;
        String operationName = OperationName.WITHDRAW_CHEQUE;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);
        requestParameters.put("comments", comments);

        try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			if (jsonResponse.has("requestId") && StringUtils.isNotBlank(jsonResponse.getString("requestId"))) {
				return true;
			}
			return false;
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while withdrawing the request",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while withdrawing the request",exp).log();
			return false;
		}
	}

	@Override
	public Boolean rejectChequeBookRequest(String requestId, String comments, DataControllerRequest request) {
		
		String serviceName = ServiceId.CHEQUE_MANAGEMENT_SERVICE;
        String operationName = OperationName.REJECT_CHEQUEBOOK_RECORD;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);
        requestParameters.put("comments", comments);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			if(jsonResponse.has("requestId") && jsonResponse.get("requestId") != null && StringUtils.isNotBlank(jsonResponse.get("requestId").toString())) {
				return true;
			}
			return false;
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while rejecting the request",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while rejecting the request",exp).log();
			return false;
		}

	}

	@Override
	public List<ApprovalRequestDTO> fetchChequeBookInfo(List<BBRequestDTO> requests, DataControllerRequest request) {
		List<ApprovalRequestDTO> chequeBooks = new ArrayList<ApprovalRequestDTO>();
		String transactionIds = "";
		StringBuilder ids = new StringBuilder();
		
		if(CollectionUtils.isEmpty(requests))
			return chequeBooks;
		
		for(BBRequestDTO bBRequestDTO : requests) {
			if(StringUtils.isNotBlank(bBRequestDTO.getTransactionId())) {
				ids.append(bBRequestDTO.getTransactionId());
				ids.append(",");
			}
		}
		transactionIds = ids.toString();
		if(transactionIds.endsWith(",")) {
			transactionIds = transactionIds.substring(0, transactionIds.length()-1);
		}
		
		if(StringUtils.isBlank(transactionIds)) {
			return chequeBooks;
		}
		String serviceName = ServiceId.CHEQUE_MANAGEMENT_SERVICE;
        String operationName = OperationName.GET_CHEQUE_DETAILS;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("transactionIds", transactionIds);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			chequeBooks = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while rejecting the request",jsonExp).log();
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while rejecting the request",exp).log();
		}
		for(ApprovalRequestDTO cheque: chequeBooks) {
			String transactionId = cheque.getTransactionId();
			for(BBRequestDTO bbRequest: requests) {
				if(!StringUtils.isBlank(transactionId) && transactionId.equals(bbRequest.getTransactionId())) {
					cheque.setRequestId(bbRequest.getRequestId());
		        	cheque.setAmIApprover(bbRequest.getAmIApprover());
		        	cheque.setAmICreator(bbRequest.getAmICreator());
		        	cheque.setRequiredApprovals(bbRequest.getRequiredApprovals());
		        	cheque.setFeatureActionId(bbRequest.getFeatureActionId());
		        	cheque.setReceivedApprovals(bbRequest.getReceivedApprovals());
		        	cheque.setStatus(bbRequest.getStatus());
		        	cheque.setIsGroupMatrix(bbRequest.getIsGroupMatrix());
		        	break;
				}
			}	
		}

		return chequeBooks;
	}

	@Override
	public ApprovalRequestDTO fetchChequeBookDetails(String transactionId, DataControllerRequest request) {
		List<ApprovalRequestDTO> chequeBooks = new ArrayList<ApprovalRequestDTO>();

		String serviceName = ServiceId.CHEQUE_MANAGEMENT_SERVICE;
        String operationName = OperationName.GET_CHEQUE_DETAILS;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("transactionIds", transactionId);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			chequeBooks = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while rejecting the request",jsonExp).log();
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while rejecting the request",exp).log();
		}
		return chequeBooks.get(0);
	}
	
	@Override
	public boolean incrementReceivedApprovalsInRequestMatrix(String requestId, String approvalMatrixId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INCREMENT_RECEIVEDAPPROVALS_PROC;

		HashMap<String, Object> requestParams = new HashMap<>();
		requestParams.put("_requestId", requestId);
		requestParams.put("_approvalMatrixId", approvalMatrixId);

		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonObj = new JSONObject(updateResponse);
			if(jsonObj.getInt("opstatus") == 0 && jsonObj.getInt("httpStatusCode") == 0) {
				return true;
			}
			
		} catch (JSONException e) {
			alert.prepareError("Unable to Call incrementRecievedApprovalsProc: " + e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at incrementRecievedApprovalsProc method: " + e).log();
			return false;
		}
		
		return false;
	}
	
	@Override
	public boolean updateRequestApprovalMatrix(String requestApprovalMatrixId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_UPDATE_REQUESTAPPROVALMATRIX_PROC;

		HashMap<String, Object> requestParams = new HashMap<>();
		requestParams.put("_requestApprovalMatrixId", requestApprovalMatrixId);

		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonObj = new JSONObject(updateResponse);
			if(jsonObj.getInt("opstatus") == 0 && jsonObj.getInt("httpStatusCode") == 0) {
				return true;
			}
			
		} catch (JSONException e) {
			alert.prepareError("Unable to Call update_requestapprovalmatrix_proc: " + e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at updateRequestApprovalMatrix method: " + e).log();
			return false;
		}
		
		return false;
	}
	
	@Override
	public JSONArray fetchRequestApproverIds(String requestId) { 
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("_requestId", requestId);
		requestParams.put("_status", "");
		
		JSONArray jsonArray = null;
		JSONArray responseArray = new JSONArray();
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_GETREQUESTAPPROVERS_PROC).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(response);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 ) {
				jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
				for(int i=0;i<jsonArray.length();i++) {
					String approverId = jsonArray.getJSONObject(i).getString(Constants.APPROVERS);
					String legalEntityId = jsonArray.getJSONObject(i).getString(Constants.COMPANY_LEGAL_UNIT);
					JSONObject approver = new JSONObject();
					approver.put(Constants.CUSTOMERID, approverId);
					approver.put(Constants.COMPANY_LEGAL_UNIT, legalEntityId);
					responseArray.put(approver);
				}	
			}
			
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while fetching Approvers", e).log();
		}
		return responseArray;
	}
	
	@Override
	public Object approveLetterOfCredit(String requestId, String customerId, String comments, String companyId, DataControllerRequest dcr) {
		int counter = callFetchRequestProc(requestId, customerId, comments, companyId);
		BBRequestDTO bBRequestDTO = null;
		
		if (counter == -1) {
			return null;
		} 
		else {

			bBRequestDTO = updateBBRequestCounter(requestId, counter, dcr);

			if (bBRequestDTO != null && bBRequestDTO.getStatus().equals(TransactionStatusEnum.APPROVED.getStatus())) {

					JSONObject response = new JSONObject();
					response.put("Transaction_id",bBRequestDTO.getTransactionId());
					response.put("FeatureAction_id",bBRequestDTO.getFeatureActionId());
					response.put("createdBy", bBRequestDTO.getCreatedby());
					return response;
			}
			else {
				return bBRequestDTO;
			}
		}
	}

	@Override
	public void executeLetterOfCreditAfterApproval(String requestId, String transactionId, String featureActionId,
			DataControllerRequest dcr) {
		String serviceName = ServiceId.DBP_TRADEFINANCE_SERVICES;
        String operationName = OperationName.APPROVE_LETTEROFCREDIT;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("featureActionId", featureActionId);
        requestParameters.put("serviceRequestId", transactionId);
        requestParameters.put("requestId", requestId);
        
		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(dcr.getHeaderMap()).
	        		withDataControllerRequest(dcr).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
		}
		
	}

	@Override
	public Boolean rejectLetterOfCredit(String requestId, String comments, DataControllerRequest request) {
		
		String serviceName = ServiceId.DBP_TRADEFINANCE_SERVICES;
        String operationName = OperationName.REJECT_LETTEROFCREDIT;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);
        requestParameters.put("comments", comments);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			if(jsonResponse.has("requestId") && jsonResponse.get("requestId") != null && StringUtils.isNotBlank(jsonResponse.get("requestId").toString())) {
				return true;
			}
			return false;
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while rejecting the request",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while rejecting the request",exp).log();
			return false;
		}
	}

	@Override
	public Boolean withdrawLetterOfCredit(String requestId, String comments, DataControllerRequest request) {
		String serviceName = ServiceId.DBP_TRADEFINANCE_SERVICES;
        String operationName = OperationName.WITHDRAW_LETTEROFCREDIT;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);
        requestParameters.put("comments", comments);

        try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			if (jsonResponse.has("requestId") && StringUtils.isNotBlank(jsonResponse.getString("requestId"))) {
				return true;
			}
			return false;
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while withdrawing the request",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while withdrawing the request",exp).log();
			return false;
		}
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchLetterOfCreditDetails(List<BBRequestDTO> requests,
			DataControllerRequest dcr) {
		List<ApprovalRequestDTO> letterOfCredits = new ArrayList<ApprovalRequestDTO>();
		String transactionIds = "";
		StringBuilder ids = new StringBuilder();
		
		if(CollectionUtils.isEmpty(requests))
			return letterOfCredits;
		
		for(BBRequestDTO bBRequestDTO : requests) {
			if(StringUtils.isNotBlank(bBRequestDTO.getTransactionId())) {
				ids.append(bBRequestDTO.getTransactionId());
				ids.append(",");
			}
		}
		transactionIds = ids.toString();
		if(transactionIds.endsWith(",")) {
			transactionIds = transactionIds.substring(0, transactionIds.length()-1);
		}
		
		if(StringUtils.isBlank(transactionIds)) {
			return letterOfCredits;
		}
		String serviceName = ServiceId.DBP_TRADEFINANCE_SERVICES;
        String operationName = OperationName.FETCH_LETTEROFCREDIT;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("transactionIds", transactionIds);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(dcr.getHeaderMap()).
	        		withDataControllerRequest(dcr).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			letterOfCredits = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while rejecting the request",jsonExp).log();
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while rejecting the request",exp).log();
		}
		for(ApprovalRequestDTO cheque: letterOfCredits) {
			String transactionId = cheque.getTransactionId();
			for(BBRequestDTO bbRequest: requests) {
				if(!StringUtils.isBlank(transactionId) && transactionId.equals(bbRequest.getTransactionId())) {
					cheque.setRequestId(bbRequest.getRequestId());
		        	cheque.setAmIApprover(bbRequest.getAmIApprover());
		        	cheque.setAmICreator(bbRequest.getAmICreator());
		        	cheque.setRequiredApprovals(bbRequest.getRequiredApprovals());
		        	cheque.setFeatureActionId(bbRequest.getFeatureActionId());
		        	cheque.setReceivedApprovals(bbRequest.getReceivedApprovals());
		        	cheque.setStatus(bbRequest.getStatus());
		        	cheque.setIsGroupMatrix(bbRequest.getIsGroupMatrix());
		        	break;
				}
			}	
		}

		return letterOfCredits;
	}

	/**
	 * @description - ADP-7058 - OVERLOAD - update the additional meta column for bbrequest
	 * @param requestId - the request Id string
	 * @param dcr
	 * @return true/false - if the update is a success or a failure
	 * @author - sourav.raychaudhuri
	 */
	@Override
	public boolean updateAdditionalMetaForApprovalRequest(String requestId, DataControllerRequest dcr){
		BBRequestDTO bbRequestDTO = this.getBbRequest(requestId);
		return this.updateAdditionalMetaForApprovalRequest(bbRequestDTO, dcr);
	}

	/**
	 * @description - ADP-7058 - update the additional meta column for bbrequest
	 * @param bbRequest - the request Id string
	 * @param dcr
	 * @return true/false - if the update is a success or a failure
	 * @author - sourav.raychaudhuri
	 */
	@Override
	public boolean updateAdditionalMetaForApprovalRequest(BBRequestDTO bbRequest, DataControllerRequest dcr){
		ApprovalRequestDTO additionalMeta = this.fetchAdditionalBackendData(bbRequest, dcr);
		additionalMeta.setRequestId(bbRequest.getRequestId());
		return this.updateAdditionalMetaForApprovalRequestInDB(additionalMeta);
	}
	public boolean updateAdditionalMetaForApprovalRequestInDB(ApprovalRequestDTO approvalRequestDTO){
		try{
			Map<String, Object> requestParameters = new HashMap<>();
			requestParameters.put("_requestId", approvalRequestDTO.getRequestId());
			requestParameters.put("_additionalMeta", JSONUtils.stringify(approvalRequestDTO));
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_BBREQUEST_UPDATEADDITIONALMETA_PROC).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			if(responseObj.has("opstatus") && Integer.parseInt(responseObj.get("opstatus").toString()) == 0){
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			alert.prepareError("Error occured while updating the additionalMeta field for the request: " + e).log();
			return false;
		}
	}
	/**
	 * @desciption - ADP-7058 - OVERLOADED - fetch the additional t24 meta data to be stored in the bbrequest table
	 * @param requestId - bbrequest id
	 * @param dcr
	 * @return ApprovalRequestDTO - the t24 meta data consisting of the additional backend info
	 * @author - sourav.raychaudhuri
	 */
	@Override
	public ApprovalRequestDTO fetchAdditionalBackendData(String requestId, DataControllerRequest dcr){
		// get the bbrequest
		BBRequestDTO requestDTO = this.getBbRequest(requestId);
		return this.fetchAdditionalBackendData(requestDTO, dcr);
	}

	/**
	 * @desciption - ADP-7058 - fetch the additional t24 meta data to be stored in the bbrequest table
	 * @param bbRequest - bbrequest object
	 * @param dcr
	 * @return ApprovalRequestDTO - the t24 meta data consisting of the additional backend info
	 * @author - sourav.raychaudhuri
	 */
	@Override
	public ApprovalRequestDTO fetchAdditionalBackendData(BBRequestDTO bbRequest, DataControllerRequest dcr){

		List<BBRequestDTO> bbRequestList = new ArrayList<>();
		bbRequestList.add(bbRequest);

		List<ApprovalRequestDTO> approvalRequests = new ArrayList<>();

		try{
			switch (bbRequest.getFeatureActionId()) {
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL:
				case FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE:
					approvalRequests.addAll(internationalFundTransferBusinessDelegate.fetchInternationalTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL:
				case FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE:
					approvalRequests.addAll(interBankFundTransferBusinessDelegate.fetchInterBankTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE:
				case FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL:
					approvalRequests.addAll(intraBankFundTransferBusinessDelegate.fetchIntraBankTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE:
				case FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL:
					approvalRequests.addAll(ownAccountFundTransferBusinessDelegate.fetchOwnAccountFundTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE:
					approvalRequests.addAll(internationalWireTransactionBusinessDelegate.fetchWireTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE:
					approvalRequests.addAll(domesticWireTransactionBusinessDelegate.fetchWireTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.BILL_PAY_CREATE:
					approvalRequests.addAll(billPayTransactionBusinessDelegate.fetchBillPayTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.P2P_CREATE:
					approvalRequests.addAll(p2PTransactionBusinessDelegate.fetchP2PTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.ACH_FILE_UPLOAD:
					approvalRequests.addAll(achFileBusinessDelegate.fetchACHFilesWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.ACH_COLLECTION_CREATE:
				case FeatureAction.ACH_PAYMENT_CREATE:
					approvalRequests.addAll(achTransactionBusinessDelegate.fetchACHTransactionsWithApprovalInfo(bbRequestList, dcr));
					break;
				case FeatureAction.BULK_PAYMENT_REQUEST_SUBMIT:
					approvalRequests.addAll(JSONUtils.parseAsList(new JSONArray(bulkPaymentRecordBusinessDelegate.fetchAllRecordsFromBackendWithApprovalInfo(bbRequestList, dcr)).toString(), ApprovalRequestDTO.class));
					break;
				case FeatureAction.CHEQUE_BOOK_REQUEST_CREATE:
					approvalRequests.addAll(fetchChequeBookInfo(bbRequestList, dcr));
					break;
				case FeatureAction.IMPORT_LC_CREATE:
					approvalRequests.addAll(fetchLetterOfCreditDetails(bbRequestList, dcr));
					break;
			}
		} catch(Exception e){
			
			alert.prepareError("Exception in invoking backend calls for meta data: " + e).log();
			return null;
		}
		if(approvalRequests.size() >= 1){
			diagnostic.prepareTrace("Additional meta-data for requestId: " + bbRequest.getRequestId() + " - " + approvalRequests).log();
			return approvalRequests.get(0);
		}
		else{
			alert.prepareError("Exception in fetching backend data - No backend data found!").log();
			return null;
		}
	}
}
