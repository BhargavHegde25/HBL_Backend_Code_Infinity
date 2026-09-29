package com.kony.adminconsole.service.approvalmatrix.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.kony.adminconsole.service.approvalmatrix.resource.api.ApprovalMatrixResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ApprovalMatrixResourceImpl implements ApprovalMatrixResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	
	private static final String INPUT_CONTRACT_ID = "contractId";
	private static final String INPUT_ACCOUNT_ID = "accountId";
	private static final String INPUT_FEATURE_ID = "featureId";
	private static final String INPUT_ACTION_ID = "actionId";
	private static final String INPUT_IS_GROUP_MATRIX = "isGroupMatrix";
	private static final String INPUT_LIMIT_TYPE_ID = "limitTypeId";
	private static final String INPUT_LIMITS = "limits";
	private static final String INPUT_SG_ID = "signatoryGroupId";
	private static final String INPUT_CIF = "cif";
	private static final String INPUT_DISABLE = "disable";
	
	ApprovalMatrixBusinessDelegate approvalmatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

	@Override
	public Result getApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
	    	
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CIF))) {
	            ErrorCodeEnum.ERR_22116.setErrorCode(result);
	            return result;
	        }
			
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			cif = requestInstance.getParameter(INPUT_CIF);
			String accountId = StringUtils.EMPTY;
			String actionId = StringUtils.EMPTY;
			if(StringUtils.isNotBlank(requestInstance.getParameter(INPUT_ACCOUNT_ID))) {
				accountId = requestInstance.getParameter(INPUT_ACCOUNT_ID);
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter(INPUT_ACTION_ID))) {
				actionId = requestInstance.getParameter(INPUT_ACTION_ID);
			}

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("cif", cif);
	    	postParametersMap.put("accountId", accountId);
	    	postParametersMap.put("actionId", actionId);
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.getApprovalMatrix(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22101.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch ApprovalMatrix, contractId :"+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getApprovalMatrix", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22101.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch ApprovalMatrix, contractId :"+contractId);
        }

        return result;
	}

	@Override
	public Result createApprovalRuleSGLevel(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;
		String actionId = StringUtils.EMPTY;
		String limitTypeId = StringUtils.EMPTY;
		String limits = StringUtils.EMPTY;
		String isGroupMatrix = StringUtils.EMPTY;
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CIF))) {
	            ErrorCodeEnum.ERR_22108.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_ACTION_ID))) {
	            ErrorCodeEnum.ERR_20866.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMIT_TYPE_ID))) {
	            ErrorCodeEnum.ERR_22109.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMITS))) {
	            ErrorCodeEnum.ERR_22110.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			cif  = requestInstance.getParameter(INPUT_CIF);
			String accountId = StringUtils.isBlank(requestInstance.getParameter(INPUT_ACCOUNT_ID)) ? StringUtils.EMPTY : requestInstance.getParameter(INPUT_ACCOUNT_ID) ;
			actionId = requestInstance.getParameter(INPUT_ACTION_ID);
			isGroupMatrix = StringUtils.isBlank(requestInstance.getParameter(INPUT_IS_GROUP_MATRIX))? "0" : requestInstance.getParameter(INPUT_IS_GROUP_MATRIX);
			limitTypeId = requestInstance.getParameter(INPUT_LIMIT_TYPE_ID);
			limits = requestInstance.getParameter(INPUT_LIMITS);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("cif", cif);
	    	postParametersMap.put("accountId", accountId);
	    	postParametersMap.put("actionId", actionId);
	    	postParametersMap.put("limitTypeId", limitTypeId);
	    	postParametersMap.put("limits", stringifyForVelocityTemplate(limits));
	    	postParametersMap.put("isGroupMatrix", isGroupMatrix);
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.createApprovalRuleSGLevel(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22102.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create ApprovalRule at Signatory Grooup Level, contractId :"+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in createApprovalRuleSGLevel", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22102.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Failed to create ApprovalRule at Signatory Grooup Level, contractId :"+contractId);
        }

        return result;
	}

	@Override
	public Result createApprovalRuleUserLevel(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif  = StringUtils.EMPTY;
		String actionId = StringUtils.EMPTY;
		String limitTypeId = StringUtils.EMPTY;
		String limits = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CIF))) {
	            ErrorCodeEnum.ERR_22108.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_ACTION_ID))) {
	            ErrorCodeEnum.ERR_20866.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMIT_TYPE_ID))) {
	            ErrorCodeEnum.ERR_22109.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMITS))) {
	            ErrorCodeEnum.ERR_22110.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			cif  = requestInstance.getParameter(INPUT_CIF);
			String accountId = StringUtils.isBlank(requestInstance.getParameter(INPUT_ACCOUNT_ID)) ? StringUtils.EMPTY : requestInstance.getParameter(INPUT_ACCOUNT_ID) ;
			actionId = requestInstance.getParameter(INPUT_ACTION_ID);
			limitTypeId = requestInstance.getParameter(INPUT_LIMIT_TYPE_ID);
			limits = requestInstance.getParameter(INPUT_LIMITS);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("cif", cif);
	    	postParametersMap.put("accountId", accountId);
	    	postParametersMap.put("actionId", actionId);
	    	postParametersMap.put("limitTypeId", limitTypeId);
	    	postParametersMap.put("limits", stringifyForVelocityTemplate(limits));
	    	
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.createApprovalRuleUserLevel(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22103.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create ApprovalRule at User Level, contractId :"+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in createApprovalRuleUserLevel", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22103.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Failed to create ApprovalRule at User Level, contractId :"+contractId);
        }

        return result;
	}

	@Override
	public Result getApprovalRules(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif  = StringUtils.EMPTY;
		String actionId = StringUtils.EMPTY;
		String limitTypeId = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CIF))) {
	            ErrorCodeEnum.ERR_22108.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_ACTION_ID))) {
	            ErrorCodeEnum.ERR_20866.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMIT_TYPE_ID))) {
	            ErrorCodeEnum.ERR_22109.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			cif  = requestInstance.getParameter(INPUT_CIF);
			String accountId = StringUtils.isBlank(requestInstance.getParameter(INPUT_ACCOUNT_ID)) ? StringUtils.EMPTY : requestInstance.getParameter(INPUT_ACCOUNT_ID) ;
			String featureId = StringUtils.isBlank(requestInstance.getParameter(INPUT_FEATURE_ID)) ? StringUtils.EMPTY : requestInstance.getParameter(INPUT_FEATURE_ID);
			actionId = requestInstance.getParameter(INPUT_ACTION_ID);
			limitTypeId = requestInstance.getParameter(INPUT_LIMIT_TYPE_ID);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("cif", cif);
	    	postParametersMap.put("accountId", accountId);
	    	postParametersMap.put("featureId", featureId);
	    	postParametersMap.put("actionId", actionId);
	    	postParametersMap.put("limitTypeId", limitTypeId);
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.getApprovalRules(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22104.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch Approval Rules, contractId :"+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getApprovalRules", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22104.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Approval Rules, contractId :"+contractId);
        }

        return result;
	}

	@Override
	public Result updateApprovalRuleUserLevel(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String cif  = StringUtils.EMPTY;
		String actionId = StringUtils.EMPTY;
		String limitTypeId = StringUtils.EMPTY;
		String limits = StringUtils.EMPTY;
		String contractId = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CIF))) {
	            ErrorCodeEnum.ERR_22108.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_ACTION_ID))) {
	            ErrorCodeEnum.ERR_20866.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMIT_TYPE_ID))) {
	            ErrorCodeEnum.ERR_22109.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMITS))) {
	            ErrorCodeEnum.ERR_22110.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			cif  = requestInstance.getParameter(INPUT_CIF);
			String accountId = StringUtils.isBlank(requestInstance.getParameter(INPUT_ACCOUNT_ID)) ? StringUtils.EMPTY : requestInstance.getParameter(INPUT_ACCOUNT_ID) ;
			actionId = requestInstance.getParameter(INPUT_ACTION_ID);
			limitTypeId = requestInstance.getParameter(INPUT_LIMIT_TYPE_ID);
			limits = requestInstance.getParameter(INPUT_LIMITS);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("cif", cif);
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("accountId", accountId);
	    	postParametersMap.put("actionId", actionId);
	    	postParametersMap.put("limitTypeId", limitTypeId);
	    	postParametersMap.put("limits", stringifyForVelocityTemplate(limits));
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.updateApprovalRuleUserLevel(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22105.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to Update ApprovalRule at User Level, cifId :"+cif);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in updateApprovalRuleUserLevel", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22105.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to Update ApprovalRule at User Level, cifId :"+cif);
        }

        return result;
	}

	@Override
	public Result updateApprovalRuleSGLevel(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String cif  = StringUtils.EMPTY;
		String actionId = StringUtils.EMPTY;
		String limitTypeId = StringUtils.EMPTY;
		String limits = StringUtils.EMPTY;
		String isGroupMatrix = StringUtils.EMPTY;
		String contractId = StringUtils.EMPTY;
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CIF))) {
	            ErrorCodeEnum.ERR_22108.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_ACTION_ID))) {
	            ErrorCodeEnum.ERR_20866.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMIT_TYPE_ID))) {
	            ErrorCodeEnum.ERR_22109.setErrorCode(result);
	            return result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LIMITS))) {
	            ErrorCodeEnum.ERR_22110.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			cif  = requestInstance.getParameter(INPUT_CIF);
			String accountId = StringUtils.isBlank(requestInstance.getParameter(INPUT_ACCOUNT_ID)) ? StringUtils.EMPTY : requestInstance.getParameter(INPUT_ACCOUNT_ID) ;
			isGroupMatrix = StringUtils.isBlank(requestInstance.getParameter(INPUT_IS_GROUP_MATRIX))? "1" : requestInstance.getParameter(INPUT_IS_GROUP_MATRIX);
			actionId = requestInstance.getParameter(INPUT_ACTION_ID);
			limitTypeId = requestInstance.getParameter(INPUT_LIMIT_TYPE_ID);
			limits = requestInstance.getParameter(INPUT_LIMITS);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("cif", cif);
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("accountId", accountId);
	    	postParametersMap.put("actionId", actionId);
	    	postParametersMap.put("limitTypeId", limitTypeId);
	    	//postParametersMap.put("limits", stringifyForVelocityTemplate(limits));
	    	postParametersMap.put("limits", limits);
	    	postParametersMap.put("isGroupMatrix", isGroupMatrix);
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.updateApprovalRuleSGLevel(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22106.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to Update ApprovalRule at Signatory Group Level, cifId :"+cif);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in updateApprovalRuleSGLevel", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22106.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to Update ApprovalRule at Signatory Group  Level, cifId :"+cif);
        }

        return result;
        
	}

	@Override
	public Result getApproversInSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String signatoryGroupId = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_SG_ID))) {
	            ErrorCodeEnum.ERR_22098.setErrorCode(result);
	            return result;
	        }
	    	
	    	signatoryGroupId = requestInstance.getParameter(INPUT_SG_ID);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("signatoryGroupId", signatoryGroupId);
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.getApproversInSignatoryGroup(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22107.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch Approvers in a Signatory Group, signatoryGroupId :"+signatoryGroupId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getApproversInSignatoryGroup", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22107.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Approvers in a Signatory Group, signatoryGroupId :"+signatoryGroupId);
        }

        return result;
	}
	
	@Override
	public Result getApprovalMatrixByContractId(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("contractId", contractId);
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.getApprovalMatrixByContractId(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22118.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch ApprovalMatrix By ContractId, contractId :"+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getApprovalMatrixByContractId", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22118.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch ApprovalMatrix By ContractId, contractId :"+contractId);
        }

        return result;
	}
	
	@Override
	public Result getAccountActionCustomerApproverList(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;
		String accountId = StringUtils.EMPTY;
		String actionId = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
	    	
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CIF))) {
	            ErrorCodeEnum.ERR_22116.setErrorCode(result);
	            return result;
	        }
			
			if(StringUtils.isBlank(requestInstance.getParameter(INPUT_ACTION_ID))) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
	            return result;
			}
			
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			cif = requestInstance.getParameter(INPUT_CIF);
			
			if(StringUtils.isNotBlank(requestInstance.getParameter(INPUT_ACCOUNT_ID))) {
				accountId = requestInstance.getParameter(INPUT_ACCOUNT_ID);
			}
			
			actionId = requestInstance.getParameter(INPUT_ACTION_ID);
			

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("cif", cif);
	    	postParametersMap.put("accountId", accountId);
	    	postParametersMap.put("actionId", actionId);
	        
	        JSONObject serviceResponse =
	        		approvalmatrixBusinessDelegate.getAccountActionCustomerApproverList(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22119.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch ApproverList, contractId :"+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getAccountActionCustomerApproverList", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22119.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.APPROVALMATRIX, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch ApproverList, contractId :"+contractId);
        }

        return result;
	}
	
	@Override
	public Result updateApprovalMatrixStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response){

		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;
		String disable = StringUtils.EMPTY;

		try {
			if ( StringUtils.isBlank(request.getParameter(INPUT_CONTRACT_ID)) ) {
				ErrorCodeEnum.ERR_21960.setErrorCode(result);
				return result;
			} else if (StringUtils.isBlank(request.getParameter(INPUT_CIF)) ) {
				ErrorCodeEnum.ERR_21798.setErrorCode(result);
				return result;
			} else if (StringUtils.isBlank(request.getParameter(INPUT_DISABLE)) ) {
				ErrorCodeEnum.ERR_22072.setErrorCode(result);
				return result;
			} else {
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);

				contractId = request.getParameter(INPUT_CONTRACT_ID);
				cif = request.getParameter(INPUT_CIF);
				disable = request.getParameter(INPUT_DISABLE);
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put(INPUT_CONTRACT_ID, contractId);
				postParametersMap.put(INPUT_CIF, cif);
				postParametersMap.put(INPUT_DISABLE, disable);
				JSONObject serviceResponse =
						approvalmatrixBusinessDelegate.updateApprovalMatrixStatus(postParametersMap,
								dbpServicesClaimsToken);
				if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
						|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22073.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,
							ActivityStatusEnum.FAILED, "Update approval matrix status failed for cif : "+ cif
							+" and contractId: "+contractId);
					return result;
				} else if (serviceResponse.has("dbpErrMsg")) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					result.addParam(new Param("errMsg", serviceResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
				} else {

					result = CommonUtilities.constructResultFromJSONObject(serviceResponse);

					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
							ActivityStatusEnum.SUCCESSFUL,
							"Successfully updated approval matrix status for cif : "+ cif + 
							" and contractId: "+contractId);

				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in update ApprovalMatrix status: ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22073.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Update approval matrix status failed for cif : "+ cif
					+" and contractId: "+contractId);
		}
		return result;
	}
	
	@Override
	public Result isApprovalMatrixDisabled(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		String contractId = StringUtils.EMPTY;
		String cif = StringUtils.EMPTY;

		try {
			if ( StringUtils.isBlank(request.getParameter(INPUT_CONTRACT_ID)) ) {
				ErrorCodeEnum.ERR_21960.setErrorCode(result);
				return result;
			} else if (StringUtils.isBlank(request.getParameter(INPUT_CIF)) ) {
				ErrorCodeEnum.ERR_21798.setErrorCode(result);
				return result;
			}else {
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);

				contractId = request.getParameter(INPUT_CONTRACT_ID);
				cif = request.getParameter(INPUT_CIF);
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put(INPUT_CONTRACT_ID, contractId);
				postParametersMap.put(INPUT_CIF, cif);
				JSONObject serviceResponse =
						approvalmatrixBusinessDelegate.isApprovalMatrixDisabled(postParametersMap,
								dbpServicesClaimsToken);
				if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
						|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22074.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.FAILED, "failed to fetch approval matrix status  for cif : "+ cif
							+" and contractId: "+contractId);
					return result;
				} else if (serviceResponse.has("dbpErrMsg")) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					result.addParam(new Param("errMsg", serviceResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
				} else {

					result = CommonUtilities.constructResultFromJSONObject(serviceResponse);

					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.SUCCESSFUL,
							"Successfully fetched approval matrix status for cif : "+ cif + 
							" and contractId: "+contractId);

				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in isApprovalMatrixDisabled : ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22074.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "failed to fetch approval matrix status  for cif : "+ cif
					+" and contractId: "+contractId);
		}
		return result;
	}
	
	private  String stringifyForVelocityTemplate(String str) {
		if (StringUtils.isBlank(str)) {

			return "\"\"";
		}else if(str.contains("\\")) {
			str= str.replace("\\","\\\\" );
		}
		return "\"" + str.replace("\"", "\\\"") + "\"";
	}

}
