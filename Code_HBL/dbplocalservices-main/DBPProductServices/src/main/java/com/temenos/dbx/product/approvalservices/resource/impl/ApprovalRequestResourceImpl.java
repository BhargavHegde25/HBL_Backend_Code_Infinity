package com.temenos.dbx.product.approvalservices.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalRequestResource;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.LimitGroupDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.GeneralTransactionDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;

public class ApprovalRequestResourceImpl implements ApprovalRequestResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getCounts(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String customerId = ApprovalUtilities.getCustomerId(request);
		String customerId = CustomerSession.getCustomerId(customer);

		List<String> approveActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE, 
				FeatureAction.P2P_APPROVE, 
				FeatureAction.ACH_FILE_APPROVE, 
				FeatureAction.ACH_COLLECTION_APPROVE, 
				FeatureAction.ACH_PAYMENT_APPROVE, 
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE, 
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE, 
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE, 
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE,
				FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE,
				FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL);
		
		List<String> createActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_SINGLE_SUBMIT,
				FeatureAction.BULK_PAYMENT_MULTIPLE_SUBMIT,
				FeatureAction.BILL_PAY_CREATE, 
				FeatureAction.P2P_CREATE, 
				FeatureAction.ACH_FILE_UPLOAD, 
				FeatureAction.ACH_COLLECTION_CREATE, 
				FeatureAction.ACH_PAYMENT_CREATE, 
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE, 
				FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE, 
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE, 
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL,
				FeatureAction.CHEQUE_BOOK_REQUEST_CREATE,
				FeatureAction.IMPORT_LC_CREATE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT,
				FeatureAction.SIGNATORY_GROUP_DELETE,
				FeatureAction.APPROVAL_MATRIX_MANAGE);
		
		String permittedApproveActionIds = CustomerSession.getPermittedActionIds(request, approveActionIds);
		String permittedCreateActionIds = CustomerSession.getPermittedActionIds(request, createActionIds);
		if (StringUtils.isEmpty(permittedApproveActionIds) && StringUtils.isEmpty(permittedCreateActionIds)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}
		String localeId = request.getParameter(Constants.LANGUAGECODE);
		if(StringUtils.isEmpty(localeId)) {
			alert.prepareError("languageCode List is missing").log();
			return ErrorCodeEnum.ERR_29008.setErrorCode(new Result());
		}
		String actionIds2Send2Proc = "";
		if(permittedCreateActionIds != null){
			actionIds2Send2Proc = permittedCreateActionIds;
		}
		if(permittedApproveActionIds != null){
			if(actionIds2Send2Proc.equalsIgnoreCase("")) {
				actionIds2Send2Proc = permittedApproveActionIds;
			} else {
				actionIds2Send2Proc = actionIds2Send2Proc+","+permittedApproveActionIds;
			}
		}
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchRequests(customerId, "", "", String.join(",", actionIds2Send2Proc));
		if(mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests for counts").log();
			return ErrorCodeEnum.ERR_29024.setErrorCode(new Result());
		}
		
		List<ApprovalRequestDTO> records;
		try {
			records = JSONUtils.parseAsList(JSONUtils.stringify(mainRequests), ApprovalRequestDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching requests for counts", e).log();
			return ErrorCodeEnum.ERR_29024.setErrorCode(new Result());
		}
		
		FeatureActionBusinessDelegate featureActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
		List<FeatureActionDTO> featureactions = featureActionBusinessDelegate.fetchFeatureActionsWithLimitGroupDetails();
		records = (new FilterDTO()).merge(records, featureactions, "featureActionId=featureActionId", "featureName,featureActionName,limitGroupName");
		
		Map<String, Map<String,JSONObject> > features = new HashMap<String, Map<String,JSONObject> >();
		ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
		ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
		Boolean isSelfApproveEnable = applicationDTO != null ? applicationDTO.isSelfApprovalEnabled() : Boolean.TRUE;
		for(ApprovalRequestDTO dto : records) {
			if(StringUtils.isEmpty(dto.getTransactionId())) {
				continue;
			}
			if(dto.getLimitGroupId() == null) {
				dto.setLimitGroupId(Constants.OTHER);
				dto.setLimitGroupName(Constants.OTHER_NAME);
			}
			if(features.get(dto.getLimitGroupId()) == null) {
				features.put(dto.getLimitGroupId(), new HashMap<String, JSONObject>());
			}
			Map<String, JSONObject> featureActions = features.get(dto.getLimitGroupId());
			JSONObject countObject;
			if(featureActions.get(dto.getFeatureActionId()) == null) {
				countObject = new JSONObject();
				FeatureActionDTO featureAction = featureActionBusinessDelegate.getFeatureActionById(dto.getFeatureActionId());
				countObject.put(Constants.FEATUREACTIONID,dto.getFeatureActionId());
				countObject.put(Constants.FEATUREACTIONNAME,featureAction.getFeatureActionName());
				countObject.put(Constants.ACTION_TYPE, featureAction.getTypeId());
				countObject.put(Constants.FEATURENAME, dto.getFeatureName());
				countObject.put(Constants.MYAPPROVALSPENDING,0);
				countObject.put(Constants.MYAPPROVALSHISTORY,0);
				countObject.put(Constants.MYREQUESTSPENDING,0);
				countObject.put(Constants.MYREQUESTHISTORY,0);
				featureActions.put(dto.getFeatureActionId(),countObject);
			}
			countObject = featureActions.get(dto.getFeatureActionId());
			if(Constants.TRUE.equalsIgnoreCase(dto.getAmICreator())) {
                if(Constants.PENDING.equalsIgnoreCase(dto.getStatus())) {
                    countObject.put(Constants.MYREQUESTSPENDING, countObject.getInt(Constants.MYREQUESTSPENDING)+1);
                } else {
                    countObject.put(Constants.MYREQUESTHISTORY, countObject.getInt(Constants.MYREQUESTHISTORY)+1);
                }
            }
            if(Constants.TRUE.equalsIgnoreCase(dto.getActedByMeAlready()) && !TransactionStatusEnum.WITHDRAWN.getStatus().equalsIgnoreCase(dto.getStatus())) {
                countObject.put(Constants.MYAPPROVALSHISTORY, countObject.getInt(Constants.MYAPPROVALSHISTORY)+1);
            } else if(Constants.PENDING.equalsIgnoreCase(dto.getStatus()) && Constants.TRUE.equalsIgnoreCase(dto.getAmIApprover())
                    && Constants.FALSE.contentEquals(dto.getActedByMeAlready())) {
            	if(!(isSelfApproveEnable && Constants.TRUE.equalsIgnoreCase(dto.getAmICreator()))) {
            		countObject.put(Constants.MYAPPROVALSPENDING, countObject.getInt(Constants.MYAPPROVALSPENDING)+1);
            	}
            }
		}
		
		LimitGroupBusinessDelegate limitGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(LimitGroupBusinessDelegate.class);
		List<LimitGroupDTO> limitGroupDTOs = limitGroupBusinessDelegate.fetchLimitGroupsWithLanguageId(localeId);
		LimitGroupDTO otherGroupDTO = new LimitGroupDTO(Constants.OTHER, Constants.OTHER_NAME, Constants.OTHER_PAYMENTS);
		limitGroupDTOs.add(otherGroupDTO);
		JSONArray limitGroups = new JSONArray();
		for(LimitGroupDTO dto : limitGroupDTOs) {
			JSONObject limitGroupObject = new JSONObject();
			limitGroupObject.put(Constants.LIMITGROUPID, dto.getLimitGroupId());
			limitGroupObject.put(Constants.LIMITGROUPNAME, dto.getLimitGroupName());
			Map<String, JSONObject> featureActions = features.get(dto.getLimitGroupId());
			if(featureActions != null && featureActions.size()>0) {
				limitGroupObject.put(Constants.FEATUREACTIONS, new ArrayList<>(featureActions.values()));
			} else {
				limitGroupObject.put(Constants.FEATUREACTIONS, new ArrayList<>());
			}
			limitGroups.put(limitGroupObject);
		}
		
		JSONObject resultObject = new JSONObject();
		resultObject.put(Constants.COUNTS, limitGroups);
		result = JSONToResult.convert(resultObject.toString());
		return result;
	}
	
	@Override
	public Result getPendingCounts(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String customerId = ApprovalUtilities.getCustomerId(request);
		String customerId = CustomerSession.getCustomerId(customer);

		List<String> approveActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_REQUEST_APPROVE,
				FeatureAction.BILL_PAY_APPROVE, 
				FeatureAction.P2P_APPROVE, 
				FeatureAction.ACH_FILE_APPROVE, 
				FeatureAction.ACH_COLLECTION_APPROVE, 
				FeatureAction.ACH_PAYMENT_APPROVE, 
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE, 
				FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE, 
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE, 
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE,
				FeatureAction.CHEQUE_BOOK_REQUEST_APPROVE,
				FeatureAction.IMPORT_LC_APPROVE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT_APPROVE,
				FeatureAction.SIGNATORY_GROUP_DELETE_APPROVE,
				FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL);
				
		
		List<String> createActionIds = Arrays.asList(
				FeatureAction.BULK_PAYMENT_SINGLE_SUBMIT,
				FeatureAction.BULK_PAYMENT_MULTIPLE_SUBMIT,
				FeatureAction.BILL_PAY_CREATE, 
				FeatureAction.P2P_CREATE, 
				FeatureAction.ACH_FILE_UPLOAD, 
				FeatureAction.ACH_COLLECTION_CREATE, 
				FeatureAction.ACH_PAYMENT_CREATE, 
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE, 
				FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE, 
				FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE, 
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE,
				FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL, 
				FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL, 
				FeatureAction.INTRA_BANK_FUND_TRANSFER_CANCEL,
				FeatureAction.CHEQUE_BOOK_REQUEST_CREATE,
				FeatureAction.IMPORT_LC_CREATE,
				FeatureAction.SIGNATORY_GROUP_CREATE_EDIT,
				FeatureAction.SIGNATORY_GROUP_DELETE,
				FeatureAction.APPROVAL_MATRIX_MANAGE);
		
		String permittedApproveActionIds = CustomerSession.getPermittedActionIds(request, approveActionIds);
		String permittedCreateActionIds = CustomerSession.getPermittedActionIds(request, createActionIds);
		if (StringUtils.isEmpty(permittedApproveActionIds) && StringUtils.isEmpty(permittedCreateActionIds)) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_10227.setErrorCode(new Result());
		}
		String localeId = request.getParameter(Constants.LANGUAGECODE);
		if(StringUtils.isEmpty(localeId)) {
			alert.prepareError("languageCode List is missing").log();
			return ErrorCodeEnum.ERR_29008.setErrorCode(new Result());
		}
		String actionIds2Send2Proc = "";
		if(permittedCreateActionIds != null){
			actionIds2Send2Proc = permittedCreateActionIds;
		}
		if(permittedApproveActionIds != null){
			if(actionIds2Send2Proc.equalsIgnoreCase("")) {
				actionIds2Send2Proc = permittedApproveActionIds;
			} else {
				actionIds2Send2Proc = actionIds2Send2Proc+","+permittedApproveActionIds;
			}
		}
		ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		List<BBRequestDTO> mainRequests = approvalQueueBusinessDelegate.fetchPendingRequests(customerId, "", "", String.join(",", actionIds2Send2Proc));
		if(mainRequests == null) {
			alert.prepareError("Error occurred while fetching requests for counts").log();
			return ErrorCodeEnum.ERR_29024.setErrorCode(new Result());
		}
		
		List<ApprovalRequestDTO> records;
		try {
			records = JSONUtils.parseAsList(JSONUtils.stringify(mainRequests), ApprovalRequestDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching requests for counts", e).log();
			return ErrorCodeEnum.ERR_29024.setErrorCode(new Result());
		}
		
		FeatureActionBusinessDelegate featureActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
		List<FeatureActionDTO> featureactions = featureActionBusinessDelegate.fetchFeatureActionsWithLimitGroupDetails();
		records = (new FilterDTO()).merge(records, featureactions, "featureActionId=featureActionId", "featureName,featureActionName,limitGroupName");
		
		Map<String, Map<String,JSONObject> > features = new HashMap<String, Map<String,JSONObject> >();
		ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
		ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
		Boolean isSelfApproveEnable = applicationDTO != null ? applicationDTO.isSelfApprovalEnabled() : Boolean.TRUE;
		for(ApprovalRequestDTO dto : records) {
			if(StringUtils.isEmpty(dto.getTransactionId())) {
				continue;
			}
			if(dto.getLimitGroupId() == null) {
				dto.setLimitGroupId(Constants.OTHER);
				dto.setLimitGroupName(Constants.OTHER_NAME);
			}
			if(features.get(dto.getLimitGroupId()) == null) {
				features.put(dto.getLimitGroupId(), new HashMap<String, JSONObject>());
			}
			Map<String, JSONObject> featureActions = features.get(dto.getLimitGroupId());
			JSONObject countObject;
			if(featureActions.get(dto.getFeatureActionId()) == null) {
				countObject = new JSONObject();
				FeatureActionDTO featureAction = featureActionBusinessDelegate.getFeatureActionById(dto.getFeatureActionId());
				countObject.put(Constants.FEATUREACTIONID,dto.getFeatureActionId());
				countObject.put(Constants.FEATUREACTIONNAME,featureAction.getFeatureActionName());
				countObject.put(Constants.ACTION_TYPE, featureAction.getTypeId());
				countObject.put(Constants.FEATURENAME, dto.getFeatureName());
				countObject.put(Constants.MYAPPROVALSPENDING,0);
				countObject.put(Constants.MYAPPROVALSHISTORY,0);
				countObject.put(Constants.MYREQUESTSPENDING,0);
				countObject.put(Constants.MYREQUESTHISTORY,0);
				featureActions.put(dto.getFeatureActionId(),countObject);
			}
			countObject = featureActions.get(dto.getFeatureActionId());
			if(Constants.TRUE.equalsIgnoreCase(dto.getAmICreator())) {
                if(Constants.PENDING.equalsIgnoreCase(dto.getStatus())) {
                    countObject.put(Constants.MYREQUESTSPENDING, countObject.getInt(Constants.MYREQUESTSPENDING)+1);
                } else {
                    countObject.put(Constants.MYREQUESTHISTORY, countObject.getInt(Constants.MYREQUESTHISTORY)+1);
                }
            }
            if(Constants.TRUE.equalsIgnoreCase(dto.getActedByMeAlready()) && !TransactionStatusEnum.WITHDRAWN.getStatus().equalsIgnoreCase(dto.getStatus())) {
                countObject.put(Constants.MYAPPROVALSHISTORY, countObject.getInt(Constants.MYAPPROVALSHISTORY)+1);
            } else if(Constants.PENDING.equalsIgnoreCase(dto.getStatus()) && Constants.TRUE.equalsIgnoreCase(dto.getAmIApprover())
                    && Constants.FALSE.contentEquals(dto.getActedByMeAlready())) {
            	if(!(isSelfApproveEnable && Constants.TRUE.equalsIgnoreCase(dto.getAmICreator()))) {
            		countObject.put(Constants.MYAPPROVALSPENDING, countObject.getInt(Constants.MYAPPROVALSPENDING)+1);
            	}
            }
		}
		
		LimitGroupBusinessDelegate limitGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(LimitGroupBusinessDelegate.class);
		List<LimitGroupDTO> limitGroupDTOs = limitGroupBusinessDelegate.fetchLimitGroupsWithLanguageId(localeId);
		LimitGroupDTO otherGroupDTO = new LimitGroupDTO(Constants.OTHER, Constants.OTHER_NAME, Constants.OTHER_PAYMENTS);
		limitGroupDTOs.add(otherGroupDTO);
		JSONArray limitGroups = new JSONArray();
		for(LimitGroupDTO dto : limitGroupDTOs) {
			JSONObject limitGroupObject = new JSONObject();
			limitGroupObject.put(Constants.LIMITGROUPID, dto.getLimitGroupId());
			limitGroupObject.put(Constants.LIMITGROUPNAME, dto.getLimitGroupName());
			Map<String, JSONObject> featureActions = features.get(dto.getLimitGroupId());
			if(featureActions != null && featureActions.size()>0) {
				limitGroupObject.put(Constants.FEATUREACTIONS, new ArrayList<>(featureActions.values()));
			} else {
				limitGroupObject.put(Constants.FEATUREACTIONS, new ArrayList<>());
			}
			limitGroups.put(limitGroupObject);
		}
		
		JSONObject resultObject = new JSONObject();
		resultObject.put(Constants.COUNTS, limitGroups);
		result = JSONToResult.convert(resultObject.toString());
		return result;
	}
	
	@Override
	public Result fetchACHFileMyRequests(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHFileBusinessDelegate.class);
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_FILE_VIEW);
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.MY_REQUESTS);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			List<ACHFileDTO> files = achFileBusinessDelegate.fetchACHFiles(userId, "", params, request);
			
			if(files != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(files, ACHFileDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching ach files pending for my requests", exp).log();
			return null;
		}
		return result;
	}
	
	@Override
	public Result fetchRejectedACHFiles(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHFileBusinessDelegate.class);
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_FILE_VIEW);
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.REJECTED);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			List<ACHFileDTO> files = achFileBusinessDelegate.fetchACHFiles(userId, "", params, request);
			
			if(files != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(files, ACHFileDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching rejected ach files", exp).log();
			return null;
		}
		return result;
	}
	
	@Override
	public Result fetchACHFilesPendingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHFileBusinessDelegate.class);
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_FILE_APPROVE);
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.PENDING_FOR_MY_APPROVAL);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			List<ACHFileDTO> files = achFileBusinessDelegate.fetchACHFiles(userId, "", params, request);
			
			if(files != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(files, ACHFileDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching ach files pending for my approval", exp).log();
			return null;
		}
		return result;
	}

	@Override
	public Result fetchACHTransactionsPendingForMyApproval(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		
		Result result = null;
		ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class);
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_APPROVE, FeatureAction.ACH_PAYMENT_APPROVE);
			String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(features == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, features);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.PENDING_FOR_MY_APPROVAL);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			List<ACHTransactionDTO> transactions = achTransactionBusinessDelegate.getACHTransactions(params, userId, "", request);
			
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
			alert.prepareError("Error occurred while fetching ach files pending for my approval", exp).log();
			return null;
		}
		return result;
	}

	@Override
	public Result fetchRejectedACHTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class);
		
		try {
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			

			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW);
			String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(features == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];

			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, features);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.REJECTED);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			List<ACHTransactionDTO> transactions = achTransactionBusinessDelegate.getACHTransactions(params, userId, "", request);
			
			if(transactions != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(transactions, ACHTransactionDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching rejected ach transactions", exp).log();
			return null;
		}
		return result;
	}

	@Override
	public Result fetchGeneralTransactionMyRequests(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);

			List<String> requiredActionIds = Arrays.asList(
					FeatureAction.BILL_PAY_CREATE,
					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE,
					FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE,
					FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE,
					FeatureAction.P2P_CREATE,
					FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE,
					FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE
					);
			
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(featureActionId == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];

			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.MY_REQUESTS);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			List<GeneralTransactionDTO> transactions = generalTransactionBusinessDelegate.fetchGeneralTransactions(userId, "", "", params);
			
			if(transactions != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(transactions, GeneralTransactionDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching general transactions pending for my requests", exp).log();
			return null;
		}
		return result;
	}


	@Override
	public Result fetchACHTransactionMyRequests(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTransactionBusinessDelegate.class);

		try {
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_CREATE, FeatureAction.ACH_PAYMENT_CREATE);
			String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(features == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];

			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, features);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.MY_REQUESTS);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);

			List<ACHTransactionDTO> transactions = achTransactionBusinessDelegate.getACHTransactions(params, userId, "", request);
			
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
			alert.prepareError("Error occurred while fetching ach transactions pending for my requests", exp).log();
		}
		return result;
	}

		
	@Override
	public Result fetchGeneralTransactionsPendingForMyApproval(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);

		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);

			List<String> requiredActionIds = Arrays.asList(
					FeatureAction.BILL_PAY_APPROVE,
					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE,
					FeatureAction.INTRA_BANK_FUND_TRANSFER_APPROVE,
					FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE,
					FeatureAction.P2P_APPROVE,
					FeatureAction.DOMESTIC_WIRE_TRANSFER_APPROVE,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE,
					FeatureAction.INTERNATIONAL_WIRE_TRANSFER_APPROVE
					);
			
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];


			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);
			filterParamsMap.put(Constants._QUERY_TYPE, Constants.PENDING_FOR_MY_APPROVAL);

			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);


			List<GeneralTransactionDTO> transactions = generalTransactionBusinessDelegate.fetchGeneralTransactions(userId, "", "", params);
			
			if(transactions != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(transactions, GeneralTransactionDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching general transactions pending for my approval", exp).log();
			return null;
		}
		return result;
	}

}