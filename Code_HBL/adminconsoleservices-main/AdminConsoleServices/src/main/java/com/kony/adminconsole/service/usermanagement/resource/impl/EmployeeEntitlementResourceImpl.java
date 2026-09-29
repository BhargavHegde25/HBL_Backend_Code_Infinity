package com.kony.adminconsole.service.usermanagement.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.preprocessor.ProductsAuthorizationPreProcessor;
import com.kony.adminconsole.service.productmanagement.preprocessor.MCMSTokenGeneration;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeeEntitlementBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeeEntitlementResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EmployeeEntitlementResourceImpl implements EmployeeEntitlementResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String ENTITLEMENTS = "entitlements";
	private static final String USERID = "userId";
	private static final String RESOURCEID = "resourceId";
	public static final String PARAM_AUTHORIZATION = "Authorization";
	
	ProductsAuthorizationPreProcessor authObj = new ProductsAuthorizationPreProcessor();
//	MCMSTokenGeneration authObj = new MCMSTokenGeneration();
	
	EmployeeEntitlementBusinessDelegate employeeEntitlementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(EmployeeEntitlementBusinessDelegate.class);

	@Override
	public Result createEntitlementByUserId(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		HashMap<String,String> map = new HashMap<>();
		String userId = StringUtils.EMPTY;
		String entitlements = StringUtils.EMPTY;
		try {
			
			userId = request.getParameter(USERID);
			if(StringUtils.isBlank(userId)) {
				return ErrorCodeEnum.ERR_22181.setErrorCode(result);
			}
			
			entitlements = request.getParameter(ENTITLEMENTS);
			if(StringUtils.isBlank(entitlements)) {
				return ErrorCodeEnum.ERR_22182.setErrorCode(result);
			}
			
			try {
				new JSONArray(entitlements);

			}catch(Exception exp) {
				alert.prepareError("Invalid entitlements in createEntitlementByUserId: "+entitlements).log();
				return ErrorCodeEnum.ERR_22182.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(USERID, userId);
	    	postParametersMap.put(ENTITLEMENTS, entitlements);
	    	
	    	boolean isAuthSuccess = authObj.execute(map, request,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = request.getParameter(PARAM_AUTHORIZATION);
	        
	        JSONObject serviceResponse =
	        		employeeEntitlementBusinessDelegate.createEntitlementByUserId(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22180.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to create EntitlementByUserId Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create EntitlementByUserId Details: "+ userId);

            } else if (serviceResponse.has("dbpErrMsg") && StringUtils.isNotBlank(serviceResponse.getString("dbpErrMsg"))) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create EntitlementByUserId Details: "+ userId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	serviceResponse.remove("dbpErrMsg");
            	serviceResponse.remove("dbpErrCode");
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Entitlement successfully created: "+ userId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22180.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in createEntitlementByUserId. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Failed to create EntitlementByUserId Details: "+ userId);
		}
		return result;
	}

	@Override
	public Result getEntitlementByUserId(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		String userId = StringUtils.EMPTY;
		try {
			
			userId = request.getParameter(USERID);
			if(StringUtils.isBlank(userId)) {
				return ErrorCodeEnum.ERR_22181.setErrorCode(result);
			}

			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(USERID, userId);
	    	
	    	boolean isAuthSuccess = authObj.execute(new HashMap<String,String>(), request,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = request.getParameter(PARAM_AUTHORIZATION);
	        
	        JSONObject serviceResponse =
	        		employeeEntitlementBusinessDelegate.getEntitlementByUserId(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22179.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch EntitlementByUserId Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EntitlementByUserId Details: "+ userId);

            } else if (serviceResponse.has("dbpErrMsg")&& StringUtils.isNotBlank(serviceResponse.getString("dbpErrMsg"))) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EntitlementByUserId Details: "+ userId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	serviceResponse.remove("dbpErrMsg");
            	serviceResponse.remove("dbpErrCode");
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Entitlement successfully fetched: "+ userId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22179.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in getEntitlementByUserId. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch EntitlementByUserId Details: "+ userId);
		}
		return result;
	}

	@Override
	public Result updateEntitlementByUserId(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		String userId = StringUtils.EMPTY;
		String entitlements = StringUtils.EMPTY;
		try {
			
			userId = request.getParameter(USERID);
			if(StringUtils.isBlank(userId)) {
				return ErrorCodeEnum.ERR_22181.setErrorCode(result);
			}
			
			entitlements = request.getParameter(ENTITLEMENTS);
			if(StringUtils.isBlank(entitlements)) {
				return ErrorCodeEnum.ERR_22182.setErrorCode(result);
			}
			
			try {
				new JSONArray(entitlements);

			}catch(Exception exp) {
				alert.prepareError("Invalid entitlements in updateEntitlementByUserId: "+entitlements).log();
				return ErrorCodeEnum.ERR_22182.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(USERID, userId);
	    	postParametersMap.put(ENTITLEMENTS, entitlements);
	        
	    	boolean isAuthSuccess = authObj.execute(new HashMap<String,String>(), request,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = request.getParameter(PARAM_AUTHORIZATION);
			
	        JSONObject serviceResponse =
	        		employeeEntitlementBusinessDelegate.updateEntitlementByUserId(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22178.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update EntitlementByUserId Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update EntitlementByUserId Details: "+ userId);

            } else if (serviceResponse.has("dbpErrMsg")&& StringUtils.isNotBlank(serviceResponse.getString("dbpErrMsg"))) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update EntitlementByUserId  Details: "+ userId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	serviceResponse.remove("dbpErrMsg");
            	serviceResponse.remove("dbpErrCode");
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Entitlement successfully update: "+ userId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22178.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in updateEntitlementByUserId. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to  update EntitlementByUserId Details: "+ userId);
		}
		return result;
	}

	@Override
	public Result getEntitlement(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		String userId = StringUtils.EMPTY;
		String resourceId  = StringUtils.EMPTY;
		
		try {
			
			userId = request.getParameter(USERID);
			if(StringUtils.isBlank(userId)) {
				return ErrorCodeEnum.ERR_22181.setErrorCode(result);
			}
			
			resourceId = request.getParameter(RESOURCEID);
			if(StringUtils.isBlank(resourceId)) {
				return ErrorCodeEnum.ERR_22183.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(USERID, userId);
	    	postParametersMap.put(RESOURCEID, resourceId);
	        
	    	boolean isAuthSuccess = authObj.execute(new HashMap<String,String>(), request,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = request.getParameter(PARAM_AUTHORIZATION);
			
	        JSONObject serviceResponse =
	        		employeeEntitlementBusinessDelegate.getEntitlement(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22177.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to getEntitlement Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EntitlementByUserId Details: "+ userId);

            } else if (serviceResponse.has("dbpErrMsg")&& StringUtils.isNotBlank(serviceResponse.getString("dbpErrMsg"))) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to getEntitlement Details: "+ userId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	serviceResponse.remove("dbpErrMsg");
            	serviceResponse.remove("dbpErrCode");
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Entitlement successfully fetched: "+ userId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22177.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in getEntitlement. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to getEntitlement Details: "+ userId);
		}
		return result;
	}

	@Override
	public Result updateEntitlement(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		String userId = StringUtils.EMPTY;
		String entitlements = StringUtils.EMPTY;
		String resourceId  = StringUtils.EMPTY;
		try {
			
			userId = request.getParameter(USERID);
			if(StringUtils.isBlank(userId)) {
				return ErrorCodeEnum.ERR_22181.setErrorCode(result);
			}
			
			entitlements = request.getParameter(ENTITLEMENTS);
			if(StringUtils.isBlank(entitlements)) {
				return ErrorCodeEnum.ERR_22182.setErrorCode(result);
			}
			
			try {
				new JSONArray(entitlements);

			}catch(Exception exp) {
				alert.prepareError("Invalid entitlements in updateEntitlement: "+entitlements).log();
				return ErrorCodeEnum.ERR_22182.setErrorCode(result);
			}
			
			resourceId = request.getParameter(RESOURCEID);
			if(StringUtils.isBlank(resourceId)) {
				return ErrorCodeEnum.ERR_22183.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(USERID, userId);
	    	postParametersMap.put(ENTITLEMENTS, entitlements);
	    	postParametersMap.put(RESOURCEID, resourceId);
	        
	    	boolean isAuthSuccess = authObj.execute(new HashMap<String,String>(), request,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = request.getParameter(PARAM_AUTHORIZATION);
			
	        JSONObject serviceResponse =
	        		employeeEntitlementBusinessDelegate.updateEntitlement(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22176.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update Entitlement Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update Entitlement Details: "+ userId);

            } else if (serviceResponse.has("dbpErrMsg")&& StringUtils.isNotBlank(serviceResponse.getString("dbpErrMsg"))) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update Entitlement  Details: "+ userId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	serviceResponse.remove("dbpErrMsg");
            	serviceResponse.remove("dbpErrCode");
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Entitlement successfully updated: "+ userId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22176.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in updateEntitlement. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to  update Entitlement Details: "+ userId);
		}
		return result;
	}

	@Override
	public Result deleteEntitlement(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		String userId = StringUtils.EMPTY;
		String resourceId  = StringUtils.EMPTY;
		
		try {
			
			userId = request.getParameter(USERID);
			if(StringUtils.isBlank(userId)) {
				return ErrorCodeEnum.ERR_22181.setErrorCode(result);
			}
			
			resourceId = request.getParameter(RESOURCEID);
			if(StringUtils.isBlank(resourceId)) {
				return ErrorCodeEnum.ERR_22183.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(USERID, userId);
	    	postParametersMap.put(RESOURCEID, resourceId);
	        
	    	boolean isAuthSuccess = authObj.execute(new HashMap<String,String>(), request,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = request.getParameter(PARAM_AUTHORIZATION);
			
	        JSONObject serviceResponse =
	        		employeeEntitlementBusinessDelegate.deleteEntitlement(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22175.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to delete Entitlement Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.DELETE,
                        ActivityStatusEnum.FAILED, "Failed to delete Entitlement Details: "+ userId);

            } else if (serviceResponse.has("dbpErrMsg") && StringUtils.isNotBlank(serviceResponse.getString("dbpErrMsg"))) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.DELETE,
                        ActivityStatusEnum.FAILED, "Failed to delete Entitlement Details: "+ userId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	serviceResponse.remove("dbpErrMsg");
            	serviceResponse.remove("dbpErrCode");
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.DELETE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Entitlement successfully deleted: "+ userId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22175.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in deleteEntitlement. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.USERS, EventEnum.DELETE,
                    ActivityStatusEnum.FAILED, "Failed to delete Entitlement Details: "+ userId);
		}
		return result;
	}

}
