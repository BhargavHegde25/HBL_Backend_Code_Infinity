package com.temenos.dbx.datamigrationservices.resource.impl;

import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateApprovalModeResource;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.ApprovalModeBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.ApprovalModeBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.ApprovalModeDTO;

public class MigrateApprovalModeResourceImpl implements MigrateApprovalModeResource {
	private static final LoggerUtil logger = new LoggerUtil(MigrateApprovalModeResourceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Result updateApprovalMode(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		String coreCustomerId = "";
		String contractId = "";
		String isGroupLevel = "";
		try {
			if (StringUtils.isBlank(requestInstance.getParameter("cif"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Invalid or missing coreCustomerId");
				return result;
			}
			if (StringUtils.isBlank(requestInstance.getParameter("contractId"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Missing contractId in request");
				return result;
			}
			if (StringUtils.isBlank(requestInstance.getParameter("isGroupLevel"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Missing input isGroupLevel in request");
				return result;
			}
			contractId = requestInstance.getParameter("contractId");
			coreCustomerId = requestInstance.getParameter("cif");
			isGroupLevel = requestInstance.getParameter("isGroupLevel");
			
			ApprovalModeBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ApprovalModeBackendDelegate.class);
			ApprovalModeDTO fetchDTO = backendDelegate.fetchApprovalMode(coreCustomerId, contractId,requestInstance.getHeaderMap(), requestInstance);
			// Validate if the matrix is already enabled
			if (fetchDTO != null && fetchDTO.getId() != null) {				
				int isGroupMatrix = fetchDTO.getIsGroupLevel() ? 1 : 0;
				logger.debug("************** isGroupMatrix :"+isGroupMatrix);
				logger.debug("************** isGroupLevel :"+isGroupLevel);
				if(isGroupMatrix == Integer.parseInt(isGroupLevel)) {
					// Throw error message if no change in the matrix mode
					result.addParam(new Param("FailureReason", "Approval mode is already set to the same", "string"));
					ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Error while trying to update ApprovalMode");
					return result;
				}
			}
			Map<String, Object> postParametersMap = (Map<String, Object>) inputArray[1];
			postParametersMap.put("coreCustomerId", coreCustomerId);
			postParametersMap.put("contractId", contractId);
			postParametersMap.put("isGroupLevel", isGroupLevel);

			result = updateApprovalMode(postParametersMap, requestInstance);
			logger.debug("**************** updateApprovalMode result :"+ResultToJSON.convert(result));

			if (result == null || !result.hasParamByName("opstatus") || !result.getOpstatusParamValue().equals("0")) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Error while trying to update ApprovalMode");
				result.addParam(new Param("status", "Failure", "string"));
				return result;
			}
			if (result.hasParamByName("dbpErrMsg")) {
				result.addParam(new Param("status", "Failure", "string"));
				return result;
			}
		} catch (Exception e) {
			result.addParam(new Param("FailureReason", e.getMessage(), "string"));
			ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Error while trying to update ApprovalMode");
		}
		return result;
	}
	
	private Result updateApprovalMode(Map<String, Object> inputParams, DataControllerRequest request) {
		
		Result result = new Result();
		Map<String, Object> headersMap = HelperMethods.addJWTAuthHeader(request, request.getHeaderMap(),
				AuthConstants.POST_LOGIN_FLOW);

		ApprovalModeBusinessDelegate approvalModeBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ApprovalModeBusinessDelegate.class);
		
		ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
		
		ContractCoreCustomerBackendDelegate contractCoreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);

		String coreCustomerId = inputParams.get("coreCustomerId").toString();
		String contractId = inputParams.get("contractId").toString();

		String legalEntityId = null;
		try{
			legalEntityId = LegalEntityUtil.getLegalEntityForCifAndContract(coreCustomerId, contractId);
		} catch (ApplicationException ae){
			ae.getErrorCodeEnum().setErrorCode(new Result());
		}

//		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		
		boolean hasPermission = true;
//		String userId = CustomerSession.getCustomerId(customer);
//		if(!CustomerSession.IsAPIUser(customer))
//		{			
//			hasPermission = approvalModeBusinessDelegate.checkForApprovalMatrixPermission(coreCustomerId,
//					contractId, userId, headersMap, request);
//		}
		Boolean isGroupLevel = false;
		int approvalMode =0;
		String isGlevel = inputParams.get("isGroupLevel").toString();
		if (isGlevel.equalsIgnoreCase("true") || isGlevel.equals("1")) {
			isGroupLevel = true;
			approvalMode = 1;
		}
		else {
			isGroupLevel = false;
			approvalMode = 0;
		}
		if (hasPermission) {
			try {
				 
				Map<String, Set<String>> contractCoreCustomerDetailsMap = contractCoreCustomerBD
						.getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId, request.getHeaderMap());

				Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
				customerActions = approvalMatrixBusinessDelegate.getActionWithApproveFeatureAction(customerActions, request.getHeaderMap());

				approvalMatrixBusinessDelegate.callApprovalMatrixTemplateCleanupProc(contractId,
						customerActions.toArray(new String[0]), coreCustomerId, Constants.MAX_TRANSACTION_LIMIT.trim() + ","+ Constants.DAILY_LIMIT.trim() +"," + Constants.WEEKLY_LIMIT.trim() + "," + Constants.NON_MONETARY_LIMIT.trim() );
				
				approvalMatrixBusinessDelegate.createApprovalMatrixTemplateDefaultEntry(contractId,
						customerActions.toArray(new String[0]), coreCustomerId, approvalMode, legalEntityId);
				
				ApprovalModeDTO approvalDTO = approvalModeBusinessDelegate.updateApprovalMode(coreCustomerId,
						contractId, isGroupLevel, headersMap, request);
				
				logger.debug("**************** updateApprovalMode approvalDTO :"+new JSONObject(approvalDTO));

				// JSONArray rulesJSONArr = new JSONArray(approvalDTO);
				//JSONObject responseObj = new JSONObject();
				if (approvalDTO != null && approvalDTO.getUpdatedRecords() > 0) {
					result.addParam(new Param("status", "Updated Successfully", "string"));
			        result.addOpstatusParam(0);
			        result.addHttpStatusCodeParam(0);
				}else if (approvalDTO != null && approvalDTO.getUpdatedRecords() == 0) {
					result.addParam(new Param("status", "Created Successfully", "string"));
					result.addOpstatusParam(0);
					result.addHttpStatusCodeParam(0);
				}

				logger.debug("**************** updateApprovalMode result111 :"+ResultToJSON.convert(result));
				//result = JSONToResult.convert(responseObj.toString());
				return result;
			} catch (Exception e) {
				logger.error("Error while fetching SavingsPotCategoriesDTO from SavingsPotBusinessDelegate : " , e);
				return new Result();
			}
		} else {
			return ErrorCodeEnum.ERR_21017.setErrorCode(new Result());
		}
	}
}
