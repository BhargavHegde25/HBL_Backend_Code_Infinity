package com.kony.adminconsole.service.signatorygroup.resource.impl;

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
import com.kony.adminconsole.service.signatorygroup.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.kony.adminconsole.service.signatorygroup.resource.api.SignatoryGroupResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SignatoryGroupResourceImpl implements SignatoryGroupResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String INPUT_SG_NAME = "signatoryGroupName";
	private static final String INPUT_SG_DESC = "signatoryGroupDescription";
	private static final String INPUT_SG_ID = "signatoryGroupId";
	private static final String INPUT_CORE_CISTOMER_ID = "coreCustomerId";
	private static final String INPUT_CONTRACT_ID = "contractId";
	private static final String INPUT_SIGNATORIES = "signatories";
	private static final String INPUT_USERNAME = "userName";
	private static final String INPUT_CORE_CUSTOMER_IDS = "coreCustomerIds";
	private static final String INPUT_IS_GROUP_LEVEL = "isGroupLevel";
	
	SignatoryGroupBusinessDelegate signatorygroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
	
	@Override
	public Result createSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		String signatoryGroupName = StringUtils.EMPTY;
		String coreCustomerId = StringUtils.EMPTY;
		String contractId = StringUtils.EMPTY;
		String signatoryGroupDescription = StringUtils.EMPTY;
		
		try {
			
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_SG_NAME))) {
	            ErrorCodeEnum.ERR_22096.setErrorCode(result);
	            return result;
	        }
	    	
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CISTOMER_ID))) {
	            ErrorCodeEnum.ERR_22038.setErrorCode(result);
	            return result;
	        }
	    	
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
	    	
	    	signatoryGroupName = requestInstance.getParameter(INPUT_SG_NAME);
	    	coreCustomerId = requestInstance.getParameter(INPUT_CORE_CISTOMER_ID);
	    	contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
	    	
	    	if(StringUtils.isNotBlank(requestInstance.getParameter(INPUT_SG_DESC))) {
	    		signatoryGroupDescription = requestInstance.getParameter(INPUT_SG_DESC);
	    	}
	    	
	    	
	    	String signatories = "[]";
	    	
	    	if(StringUtils.isNotBlank(requestInstance.getParameter(INPUT_SIGNATORIES))) {
	    		signatories = stringifyForVelocityTemplate (requestInstance.getParameter(INPUT_SIGNATORIES));
	    	}
	    	
	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	        postParametersMap.put("signatoryGroupName", signatoryGroupName);
	        postParametersMap.put("coreCustomerId", coreCustomerId);
	        postParametersMap.put("contractId", contractId);
	        postParametersMap.put("signatoryGroupDescription", signatoryGroupDescription);
	        postParametersMap.put("signatories", signatories);
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.createSignatoryGroup(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22088.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Signatory Group Creation failed: coreCustomerId:"+signatoryGroupName
                        +" coreCustomerId: "+ coreCustomerId + " contractId: "+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in createSignatoryGroup", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22088.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Signatory Group Creation failed: coreCustomerId:"+signatoryGroupName
                    +" coreCustomerId: "+ coreCustomerId + " contractId: "+contractId);
        }

        return result;
	}

	@Override
	public Result updateSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String signatoryGroupName = StringUtils.EMPTY;
		String signatoryGroupId = StringUtils.EMPTY;
		String signatoryGroupDescription = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_SG_ID))) {
	            ErrorCodeEnum.ERR_22098.setErrorCode(result);
	            return result;
	        }
			
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_SG_NAME))) {
	            ErrorCodeEnum.ERR_22096.setErrorCode(result);
	            return result;
	        }
	    	
	    	signatoryGroupId = requestInstance.getParameter(INPUT_SG_ID);
	    	signatoryGroupName = requestInstance.getParameter(INPUT_SG_NAME);
	    	
	    	if(StringUtils.isNotBlank(requestInstance.getParameter(INPUT_SG_DESC))) {
	    		signatoryGroupDescription = requestInstance.getParameter(INPUT_SG_DESC);
	    	}
	    	
	    	String signatories = "[]";
	    	
	    	if(StringUtils.isNotBlank(requestInstance.getParameter(INPUT_SIGNATORIES))) {
	    		signatories = stringifyForVelocityTemplate (requestInstance.getParameter(INPUT_SIGNATORIES));
	    	}
	    	
	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("signatoryGroupId", signatoryGroupId);
	        postParametersMap.put("signatoryGroupName", signatoryGroupName);
	        postParametersMap.put("signatoryGroupDescription", signatoryGroupDescription);
	        postParametersMap.put("signatories", signatories);
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.updateSignatoryGroups(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22089.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Signatory Group Update failed: signatoryGroupId:"+signatoryGroupId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in updateSignatoryGroups", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22089.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Signatory Group Update failed: signatoryGroupId:"+signatoryGroupId);
        }

        return result;
	}

	@Override
	public Result deleteSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
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
	        		signatorygroupBusinessDelegate.deleteSignatoryGroup(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22090.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.DELETE,
                        ActivityStatusEnum.FAILED, "Signatory Group Delete failed: signatoryGroupId:"+signatoryGroupId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in deleteSignatoryGroup", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22090.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.DELETE,
                    ActivityStatusEnum.FAILED, "Signatory Group Delete failed: signatoryGroupId:"+signatoryGroupId);
        }

        return result;
	}

	@Override
	public Result getNoGroupUsers(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		String coreCustomerId = StringUtils.EMPTY;
    	String contractId = StringUtils.EMPTY;
    	
		try {
	    	
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CISTOMER_ID))) {
	            ErrorCodeEnum.ERR_22038.setErrorCode(result);
	            return result;
	        }
	    	
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
	    	coreCustomerId = requestInstance.getParameter(INPUT_CORE_CISTOMER_ID);
	    	contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
	    	
	    	
	    	
	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	
	        postParametersMap.put("coreCustomerId", coreCustomerId);
	        postParametersMap.put("contractId", contractId);
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.getNoGroupUsers(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22091.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch getNoGroupUsers, coreCustomerId: "+coreCustomerId +
                        ", contractId: "+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getNoGroupUsers", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22091.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch getNoGroupUsers, coreCustomerId: "+coreCustomerId + 
                    		", contractId: "+contractId);
        }

        return result;
	}

	@Override
	public Result getApprovalPermissionsForUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String userName = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_USERNAME))) {
	            ErrorCodeEnum.ERR_22099.setErrorCode(result);
	            return result;
	        }
	    	
			userName = requestInstance.getParameter(INPUT_USERNAME);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("userName", userName);
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.getApprovalPermissionsForUser(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22092.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch ApprovalPermissionsForUser, userName :"+userName);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getApprovalPermissionsForUser", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22092.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch ApprovalPermissionsForUser, userName :"+userName);
        }

        return result;
	}

	@Override
	public Result getAllSignatoryGroupsbyCoreCustomerIds(String methodID, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {
		
		Result result = new Result();
		String coreCustomerIds = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CUSTOMER_IDS))) {
	            ErrorCodeEnum.ERR_22100.setErrorCode(result);
	            return result;
	        }
	    	
			coreCustomerIds = requestInstance.getParameter(INPUT_CORE_CUSTOMER_IDS);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("coreCustomerIds", stringifyForVelocityTemplate(coreCustomerIds));
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.getAllSignatoryGroupsbyCoreCustomerIds(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22093.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch SignatoryGroups, coreCustomerIds :"+coreCustomerIds);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getAllSignatoryGroupsbyCoreCustomerIds", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22093.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch SignatoryGroups, coreCustomerIds :"+coreCustomerIds);
        }

        return result;
        
	}

	@Override
	public Result getAllSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
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
	        		signatorygroupBusinessDelegate.getAllSignatoryGroups(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22094.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch SignatoryGroups, contractId :"+contractId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getAllSignatoryGroups", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22094.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch SignatoryGroups, contractId :"+contractId);
        }

        return result;
        
	}

	@Override
	public Result getSignatoryGroupDetails(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
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
	        		signatorygroupBusinessDelegate.getSignatoryGroupDetails(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22095.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed fetch getSignatoryGroupDetails: signatoryGroupId:"+signatoryGroupId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in deleteSignatoryGroup", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22095.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed fetch getSignatoryGroupDetails: signatoryGroupId:"+signatoryGroupId);
        }

        return result;
        
	}

	@Override
	public Result fetchApprovalMode(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String coreCustomerId = StringUtils.EMPTY;
		String contractId = StringUtils.EMPTY;
		
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CISTOMER_ID))) {
	            ErrorCodeEnum.ERR_22038.setErrorCode(result);
	            return result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
	    	
			coreCustomerId = requestInstance.getParameter(INPUT_CORE_CISTOMER_ID);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("coreCustomerId", coreCustomerId);
	    	postParametersMap.put("contractId", contractId);
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.fetchApprovalMode(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22111.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch fetchApprovalMode: contractId:"+contractId +
                        " and coreCustomerId: "+coreCustomerId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in fetchApprovalMode", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22111.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch fetchApprovalMode: contractId:"+contractId +
                    " and coreCustomerId: "+coreCustomerId);
        }

        return result;
	}

	@Override
	public Result updateApprovalMode(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String coreCustomerId = StringUtils.EMPTY;
		String contractId = StringUtils.EMPTY;
		String isGroupLevel = StringUtils.EMPTY;
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CISTOMER_ID))) {
	            ErrorCodeEnum.ERR_22038.setErrorCode(result);
	            return result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_IS_GROUP_LEVEL))) {
	            ErrorCodeEnum.ERR_22114.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			coreCustomerId = requestInstance.getParameter(INPUT_CORE_CISTOMER_ID);
			isGroupLevel = requestInstance.getParameter(INPUT_IS_GROUP_LEVEL);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("coreCustomerId", coreCustomerId);
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("isGroupLevel", isGroupLevel);
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.updateApprovalMode(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22112.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to update ApprovalMode: contractId:"+contractId +
                        " and coreCustomerId: "+coreCustomerId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in updateApprovalMode", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22112.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to update ApprovalMode: contractId:"+contractId +
                    " and coreCustomerId: "+coreCustomerId);
        }

        return result;
	}

	@Override
	public Result deleteApprovalMode(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String coreCustomerId = StringUtils.EMPTY;
		String contractId = StringUtils.EMPTY;
		try {
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CISTOMER_ID))) {
	            ErrorCodeEnum.ERR_22038.setErrorCode(result);
	            return result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_22097.setErrorCode(result);
	            return result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_IS_GROUP_LEVEL))) {
	            ErrorCodeEnum.ERR_22114.setErrorCode(result);
	            return result;
	        }
	    	
			contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
			coreCustomerId = requestInstance.getParameter(INPUT_CORE_CISTOMER_ID);

	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("coreCustomerId", coreCustomerId);
	    	postParametersMap.put("contractId", contractId);
	        
	        JSONObject serviceResponse =
	        		signatorygroupBusinessDelegate.deleteApprovalMode(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22113.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to deleteApprovalMode ApprovalMode: contractId:"+contractId +
                        " and coreCustomerId: "+coreCustomerId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in deleteApprovalMode", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22113.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to update deleteApprovalMode: contractId:"+contractId +
                    " and coreCustomerId: "+coreCustomerId);
        }

        return result;
        
	}
	
	@Override
	public Result isSignatoryGroupEligibleForDelete(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
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
	        		signatorygroupBusinessDelegate.isSignatoryGroupEligibleForDelete(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22117.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.DELETE,
                        ActivityStatusEnum.FAILED, "Failed to verify if SignatoryGroup eligible for delete: signatoryGroupId:"+signatoryGroupId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in isSignatoryGroupEligibleForDelete", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22117.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.SIGNATORYGROUP, EventEnum.DELETE,
                    ActivityStatusEnum.FAILED, "Failed to verify if SignatoryGroup eligible for delete: signatoryGroupId:"+signatoryGroupId);
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
