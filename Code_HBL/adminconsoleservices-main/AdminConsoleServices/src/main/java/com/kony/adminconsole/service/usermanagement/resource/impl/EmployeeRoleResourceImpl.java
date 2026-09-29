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
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeeRoleBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeeRoleResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EmployeeRoleResourceImpl implements EmployeeRoleResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String ROLE_NAME = "roleName";
	private static final String ROLE_ID = "roleId";
	private static final String DESCRIPTION = "description";
	private static final String STATUS = "status";
	private static final String ACCESS_CONTROL = "accessControl";
	
	EmployeeRoleBusinessDelegate employeeRoleBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(EmployeeRoleBusinessDelegate.class);
	
	@Override
	public Result getEmployeeRoles(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		
		try {
			
			Map<String, Object> postParametersMap = new HashMap<>();
	        
	        JSONObject serviceResponse =
	        		employeeRoleBusinessDelegate.getEmployeeRoles(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22191.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch EmployeeRole Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeeRoles: ");

            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeeRoles: "+
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Role fetched successfully");
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22191.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in getEmployeeRoleDetails. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch EmployeeRole Details: ");
		}
		return result;
	}

	@Override
	public Result getEmployeeRoleDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		String roleId = StringUtils.EMPTY;
		try {
			
			roleId = request.getParameter(ROLE_ID);
			if(StringUtils.isBlank(roleId)) {
				return ErrorCodeEnum.ERR_22169.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("roleId", roleId);
	        
	        JSONObject serviceResponse =
	        		employeeRoleBusinessDelegate.getEmployeeRoleDetails(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22160.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch EmployeeRole Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeeRole Details: "+ roleId);

            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeeRole Details: "+ roleId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Role fetched successfully for roleId: "+ roleId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22160.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in getEmployeeRoleDetails. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch EmployeeRole Details: "+ roleId);
		}
		return result;
	}
	
	@Override
	public Result createEmployeeRole(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		String roleName = StringUtils.EMPTY;
		
		try {
			
			roleName = request.getParameter(ROLE_NAME);
			if(StringUtils.isBlank(roleName)) {
				return ErrorCodeEnum.ERR_22185.setErrorCode(result);
			}
			
			String description = request.getParameter(DESCRIPTION);
			if(StringUtils.isBlank(description)) {
				return ErrorCodeEnum.ERR_22165.setErrorCode(result);
			}
			
			String status = request.getParameter(STATUS);
			if(StringUtils.isBlank(status)) {
				return ErrorCodeEnum.ERR_22166.setErrorCode(result);
			}
			
			String accessControl = request.getParameter(ACCESS_CONTROL);
			if(StringUtils.isBlank(accessControl)) {
				return ErrorCodeEnum.ERR_22186.setErrorCode(result);
			}
			
			JSONArray accessControlArr = null;
			try {
				accessControlArr = new JSONArray(accessControl);
			}catch(Exception exp) {
				alert.prepareError("Invalid accessControl in createEmployeeRole: "+accessControl).log();
				return ErrorCodeEnum.ERR_22186.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(ROLE_NAME, roleName);
	    	postParametersMap.put(DESCRIPTION, description);
	    	postParametersMap.put(STATUS, status);
	    	postParametersMap.put(ACCESS_CONTROL, accessControlArr);
	    	
	    	UserDetailsBean loggedInUserDetails;
			try {
				loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
				postParametersMap.put("createdby", loggedInUserDetails.getUserName());
			} catch (ApplicationException e1) {
				alert.prepareError("Failed to retrive logged-in user details: "+e1).log();
			}
	        
	        JSONObject serviceResponse =
	        		employeeRoleBusinessDelegate.createEmployeeRole(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22184.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to create EmployeeRole: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create employeeRole: "+ roleName);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create employee Role: "+ roleName +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Role create successful for roleName: "+ roleName);
            }
	        
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22184.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in createEmployeeRole Resource service. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Failed to create Employee Role: "+ roleName);
		}
		return result;
	}

	@Override
	public Result updateEmployeeRoleDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();

		String roleId = StringUtils.EMPTY;
		try {
			
			roleId = request.getParameter(ROLE_ID);
			if(StringUtils.isBlank(roleId)) {
				return ErrorCodeEnum.ERR_22192.setErrorCode(result);
			}
			
			String roleName = request.getParameter(ROLE_NAME);
			if(StringUtils.isBlank(roleName)) {
				return ErrorCodeEnum.ERR_22185.setErrorCode(result);
			}
			
			String description = request.getParameter(DESCRIPTION);
			if(StringUtils.isBlank(description)) {
				return ErrorCodeEnum.ERR_22165.setErrorCode(result);
			}
			
			String status = request.getParameter(STATUS);
			if(StringUtils.isBlank(status)) {
				return ErrorCodeEnum.ERR_22166.setErrorCode(result);
			}
			
			String accessControl = request.getParameter(ACCESS_CONTROL);
			if(StringUtils.isBlank(accessControl)) {
				return ErrorCodeEnum.ERR_22186.setErrorCode(result);
			}
			
			JSONArray accessControlArr = null;
			try {
				accessControlArr = new JSONArray(accessControl);
			}catch(Exception exp) {
				alert.prepareError("Invalid accessControl in createEmployeeRole: "+accessControl).log();
				return ErrorCodeEnum.ERR_22186.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("roleId", roleId);
	    	postParametersMap.put("roleName", roleName);
	    	postParametersMap.put("description", description);
	    	postParametersMap.put("status", status);
	    	postParametersMap.put(ACCESS_CONTROL, accessControlArr);
	    	
	    	UserDetailsBean loggedInUserDetails;
			try {
				loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
				postParametersMap.put("modifiedby", loggedInUserDetails.getUserName());
			} catch (ApplicationException e1) {
				alert.prepareError("Failed to retrive logged-in user details: "+e1).log();
			}
	        
	        JSONObject serviceResponse =
	        		employeeRoleBusinessDelegate.updateEmployeeRoleDetails(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22189.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update EmployeeRole: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employeeRole: "+ roleId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employee Role: "+ roleId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Role Update successful for roleId: "+ roleId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22189.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in updateEmployeeRoleDetails Resource service. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to update EmployeeRoleDetails: "+ roleId);
		}
		return result;
	}

	@Override
	public Result updateEmployeeRoleStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		
		String roleId = StringUtils.EMPTY;
		try {
			
			roleId = request.getParameter(ROLE_ID);
			if(StringUtils.isBlank(roleId)) {
				return ErrorCodeEnum.ERR_22169.setErrorCode(result);
			}
			
			String status = request.getParameter(STATUS);
			if(StringUtils.isBlank(status)) {
				return ErrorCodeEnum.ERR_22166.setErrorCode(result);
			}

			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("roleId", roleId);
	    	postParametersMap.put("status", status);
	    	
	    	UserDetailsBean loggedInUserDetails;
			try {
				loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
				postParametersMap.put("modifiedby", loggedInUserDetails.getUserName());
			} catch (ApplicationException e1) {
				alert.prepareError("Failed to retrive logged-in user details: "+e1).log();
			}
	        
	        JSONObject serviceResponse =
	        		employeeRoleBusinessDelegate.updateEmployeeRoleStatus(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22190.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update EmployeeRole Status: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employeeRole Status: "+ roleId);

            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employee Role Status: "+ roleId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Role Status Update successful for roleId: "+ roleId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22190.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in updateEmployeeRole Status Resource service. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to update EmployeeRole Status: "+ roleId);
		}
		return result;
	}

}
