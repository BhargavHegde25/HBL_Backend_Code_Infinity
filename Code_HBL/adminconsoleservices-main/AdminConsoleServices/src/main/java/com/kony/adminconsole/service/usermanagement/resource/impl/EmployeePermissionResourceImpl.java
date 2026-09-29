package com.kony.adminconsole.service.usermanagement.resource.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeePermissionBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeePermissionResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EmployeePermissionResourceImpl implements EmployeePermissionResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String PERMISSION_NAME = "permissionName";
	private static final String PERMISSION_ID = "permissionId";
	private static final String DESCRIPTION = "description";
	private static final String STATUS = "status";
	private static final String LEGAL_ENTITIES = "legalEntities";
	private static final String ACTIONS = "actions";
	private static final String ORG_UNITS = "organisationalUnits";
	
	EmployeePermissionBusinessDelegate employeePermissionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(EmployeePermissionBusinessDelegate.class);
	
	@Override
	public Result getEmployeePermissions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		
		try {
			
			Map<String, Object> postParametersMap = new HashMap<>();
	        
	        JSONObject serviceResponse =
	        		employeePermissionBusinessDelegate.getEmployeePermissionDetails(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22160.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch EmployeePermission Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeePermissions: ");

            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeePermissions: "+
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Permission fetched successfully");
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22160.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in getEmployeePermissionDetails. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch EmployeePermission Details: ");
		}
		return result;
	}

	@Override
	public Result getEmployeePermissionDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		
		String permissionId = StringUtils.EMPTY;
		try {
			
			permissionId = request.getParameter(PERMISSION_ID);
			if(StringUtils.isBlank(permissionId)) {
				return ErrorCodeEnum.ERR_22169.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("permissionId", permissionId);
	        
	        JSONObject serviceResponse =
	        		employeePermissionBusinessDelegate.getEmployeePermissionDetails(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22160.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch EmployeePermission Details: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeePermission Details: "+ permissionId);

            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch EmployeePermission Details: "+ permissionId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Permission fetched successfully for permissionId: "+ permissionId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22160.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in getEmployeePermissionDetails. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch EmployeePermission Details: "+ permissionId);
		}
		return result;
	}
	
	@Override
	public Result createEmployeePermission(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		String permissionName = StringUtils.EMPTY;
		
		try {
			
			permissionName = request.getParameter(PERMISSION_NAME);
			if(StringUtils.isBlank(permissionName)) {
				return ErrorCodeEnum.ERR_22164.setErrorCode(result);
			}
			
			String description = request.getParameter(DESCRIPTION);
			if(StringUtils.isBlank(description)) {
				return ErrorCodeEnum.ERR_22165.setErrorCode(result);
			}
			
			String status = request.getParameter(STATUS);
			if(StringUtils.isBlank(status)) {
				return ErrorCodeEnum.ERR_22166.setErrorCode(result);
			}
			
			String legalEntities = request.getParameter(LEGAL_ENTITIES);
			if(StringUtils.isBlank(legalEntities)) {
				return ErrorCodeEnum.ERR_22167.setErrorCode(result);
			}
			Set<String> legalEntitySet = new HashSet<>();
			try {
				JSONArray jsonArray = new JSONArray(legalEntities);
				for(int i=0; i<jsonArray.length();i++) {
					legalEntitySet.add(jsonArray.get(i).toString());
				}
			}catch(Exception exp) {
				alert.prepareError("Invalid legalEntities in createEmployeePermission: "+legalEntities).log();
				return ErrorCodeEnum.ERR_22167.setErrorCode(result);
			}
			
			
			String actions = request.getParameter(ACTIONS);
			if(StringUtils.isBlank(actions)) {
				return ErrorCodeEnum.ERR_22168.setErrorCode(result);
			}
			
			Set<String> actionSet = new HashSet<>();
			try {
				JSONArray jsonArray = new JSONArray(actions);
				for(int i=0; i<jsonArray.length();i++) {
					actionSet.add(jsonArray.get(i).toString());
				}
			}catch(Exception exp) {
				alert.prepareError("Invalid action array in createEmployeePermission: "+actions).log();
				return ErrorCodeEnum.ERR_22168.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("permissionName", permissionName);
	    	postParametersMap.put("description", description);
	    	postParametersMap.put("status", status);
	    	postParametersMap.put("legalEntitySet", legalEntitySet);
	    	postParametersMap.put("actionSet", actionSet);
	    	
	    	UserDetailsBean loggedInUserDetails;
			try {
				loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
				postParametersMap.put("createdby", loggedInUserDetails.getUserName());
			} catch (ApplicationException e1) {
				alert.prepareError("Failed to retrive logged-in user details: "+e1).log();
			}
	        
	        JSONObject serviceResponse =
	        		employeePermissionBusinessDelegate.createEmployeePermission(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22163.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to create EmployeePermission: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create employeePermission: "+ permissionName);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create employee Permission: "+ permissionName +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Permission create successful for permissionName: "+ permissionName);
            }
	        
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22163.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in createEmployeePermission Resource service. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Failed to create Employee Permission: "+ permissionName);
		}
		return result;
	}

	@Override
	public Result updateEmployeePermissionDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();

		String permissionId = StringUtils.EMPTY;
		try {
			
			permissionId = request.getParameter(PERMISSION_ID);
			if(StringUtils.isBlank(permissionId)) {
				return ErrorCodeEnum.ERR_22169.setErrorCode(result);
			}
			
			String permissionName = request.getParameter(PERMISSION_NAME);
			if(StringUtils.isBlank(permissionName)) {
				return ErrorCodeEnum.ERR_22164.setErrorCode(result);
			}
			
			String description = request.getParameter(DESCRIPTION);
			if(StringUtils.isBlank(description)) {
				return ErrorCodeEnum.ERR_22165.setErrorCode(result);
			}
			
			String status = request.getParameter(STATUS);
			if(StringUtils.isBlank(status)) {
				return ErrorCodeEnum.ERR_22166.setErrorCode(result);
			}
			
			String legalEntities = request.getParameter(LEGAL_ENTITIES);
			if(StringUtils.isBlank(legalEntities)) {
				return ErrorCodeEnum.ERR_22167.setErrorCode(result);
			}

			Set<String> legalEntitySet = new HashSet<>();
			try {
				JSONArray jsonArray = new JSONArray(legalEntities);
				for(int i=0; i<jsonArray.length();i++) {
					legalEntitySet.add(jsonArray.get(i).toString());
				}
			}catch(Exception exp) {
				alert.prepareError("Invalid legalEntities in createEmployeePermission: "+legalEntities).log();
				return ErrorCodeEnum.ERR_22167.setErrorCode(result);
			}
			
			
			String actions = request.getParameter(ACTIONS);
			if(StringUtils.isBlank(actions)) {
				return ErrorCodeEnum.ERR_22168.setErrorCode(result);
			}
			
			Set<String> actionSet = new HashSet<>();
			try {
				JSONArray jsonArray = new JSONArray(actions);
				for(int i=0; i<jsonArray.length();i++) {
					actionSet.add(jsonArray.get(i).toString());
				}
			}catch(Exception exp) {
				alert.prepareError("Invalid action array in createEmployeePermission: "+actions).log();
				return ErrorCodeEnum.ERR_22168.setErrorCode(result);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("permissionId", permissionId);
	    	postParametersMap.put("permissionName", permissionName);
	    	postParametersMap.put("description", description);
	    	postParametersMap.put("status", status);
	    	postParametersMap.put("legalEntitySet", legalEntitySet);
	    	postParametersMap.put("actionSet", actionSet);
	    	
	    	UserDetailsBean loggedInUserDetails;
			try {
				loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
				postParametersMap.put("modifiedby", loggedInUserDetails.getUserName());
			} catch (ApplicationException e1) {
				alert.prepareError("Failed to retrive logged-in user details: "+e1).log();
			}
	        
	        JSONObject serviceResponse =
	        		employeePermissionBusinessDelegate.updateEmployeePermissionDetails(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22161.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update EmployeePermission: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employeePermission: "+permissionId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employee Permission: "+ permissionId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Permission Update successful for permissionId: "+ permissionId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22161.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in updateEmployeePermissionDetails Resource service. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to update EmployeePermissionDetails: "+ permissionId);
		}
		return result;
	}

	@Override
	public Result updateEmployeePermissionStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		
		String permissionId = StringUtils.EMPTY;
		try {
			
			permissionId = request.getParameter(PERMISSION_ID);
			if(StringUtils.isBlank(permissionId)) {
				return ErrorCodeEnum.ERR_22169.setErrorCode(result);
			}
			
			String status = request.getParameter(STATUS);
			if(StringUtils.isBlank(status)) {
				return ErrorCodeEnum.ERR_22166.setErrorCode(result);
			}

			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("permissionId", permissionId);
	    	postParametersMap.put("status", status);
	    	
	    	UserDetailsBean loggedInUserDetails;
			try {
				loggedInUserDetails = LoggedInUserHandler.getUserDetails(request);
				postParametersMap.put("modifiedby", loggedInUserDetails.getUserName());
			} catch (ApplicationException e1) {
				alert.prepareError("Failed to retrive logged-in user details: "+e1).log();
			}
	        
	        JSONObject serviceResponse =
	        		employeePermissionBusinessDelegate.updateEmployeePermissionStatus(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22162.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update EmployeePermission Status: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employeePermission Status: "+ permissionId);

            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update employee Permission Status: "+ permissionId +
                        							", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Employee Permission Status Update successful for permissionId: "+ permissionId);
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22162.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in updateEmployeePermission Status Resource service. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to update EmployeePermission Status: "+ permissionId);
		}
		return result;
	}
	
	@Override
	public Result fetchLegalEntityList(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		
		String legalEntities = StringUtils.EMPTY;
		String organisationalUnits = StringUtils.EMPTY;
		try {
			
			Map<String, Object> postParametersMap = new HashMap<>();
			
			if(StringUtils.isNotBlank(request.getParameter(LEGAL_ENTITIES))) {
				legalEntities = request.getParameter(LEGAL_ENTITIES);
				postParametersMap.put(LEGAL_ENTITIES, legalEntities);
			}
			
			if(StringUtils.isNotBlank(request.getParameter(ORG_UNITS))) {
				organisationalUnits = request.getParameter(ORG_UNITS);
				postParametersMap.put(ORG_UNITS, organisationalUnits);
			}

	        
	        JSONObject serviceResponse =
	        		employeePermissionBusinessDelegate.fetchLegalEntityList(postParametersMap);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22171.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch LegalEntities: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch LegalEntities");

            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch LegalEntities: "+", Reason: "+serviceResponse.has("dbpErrMsg"));
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully fetch Legal Entities ");
            }
			
		}catch(Exception exp) {
			ErrorCodeEnum.ERR_22171.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in fetchLegalEntityList. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch LegalEntities: ");
		}
		return result;
	}

	@Override
	public Result getPermissionsByLegalEntities(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();

		try {
			Map<String, Object> postParametersMap = new HashMap<>();
			if (StringUtils.isNotBlank(request.getParameter("legalEntityIds"))) {
				postParametersMap.put(LEGAL_ENTITIES, request.getParameter("legalEntityIds"));
			}

			JSONObject serviceResponse = employeePermissionBusinessDelegate
					.getPermissionsByLegalEnities(postParametersMap);
			if (serviceResponse == null) {
				ErrorCodeEnum.ERR_22193.setErrorCode(result);
				result.addParam(new Param("status", "Failed", FabricConstants.STRING));
				alert.prepareError("Failed to fetch permissions by legal entities: " + serviceResponse).log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Failed to fetch permissions by legal entities: ");

			} else if (serviceResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Failed to fetch permissions by legal entities: " + ", Reason: "
								+ serviceResponse.has("dbpErrMsg"));
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL,
						"Employee Permission details by legal entities fetched successfully");
			}

		} catch (Exception exp) {
			ErrorCodeEnum.ERR_22193.setErrorCode(result);
			result.addParam(new Param("FailureReason", exp.getMessage(), FabricConstants.STRING));
			alert.prepareError("Exception occured in getPermissionsByLegalEntities. Error: ", exp).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.PERMISSIONS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Failed to fetch permissions by legal entities: ");
		}
		return result;
	}

}
