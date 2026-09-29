package com.kony.adminconsole.service.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.RoleHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ApprovalConstants;
import com.kony.adminconsole.utilities.ApprovalUtils;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to create a Role
 * 
 * @author Aditya Mankal
 *
 */
public class RoleCreateService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final int ROLE_NAME_MIN_LENGTH = 5;
	private static final int ROLE_NAME_MAX_LENGTH = 50;

	private static final int ROLE_DESCRIPTION_MIN_LENGTH = 5;
	private static final int ROLE_DESCRIPTION_MAX_LENGTH = 300;

	private static final String DEFAULT_ROLE_TYPE_ID = "ROLE_TYPE_1";
	private static final String INPUT_ADDED_SERVICEDEFINITIONS = "AddedServiceDefinitions";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();
		JSONArray legalEntitiesRoleInfo = new JSONArray();
		
		if(requestInstance.getParameter("legalEntitiesRoleInfo") != null) {
    		legalEntitiesRoleInfo = new JSONArray(
    				requestInstance.getParameter("legalEntitiesRoleInfo").toString());
    		if(legalEntitiesRoleInfo == null || legalEntitiesRoleInfo.length() == 0) {
                String message = "Empty Legal Entity role data for create Role service";
                processedResult.addParam(new Param("message", message, FabricConstants.STRING));
                ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
                return processedResult;
    		}
		} else {
		    String message = "Empty payload for create Role service";
            processedResult.addParam(new Param("message", message, FabricConstants.STRING));
            ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
            return processedResult;
		}
		
		String roleId = CommonUtilities.getNewId().toString();
		
		String[] reqPermissions = {PermissionName.CREATEROLES};
		for (int index1 = 0; index1 < legalEntitiesRoleInfo.length(); index1++) {
			JSONObject legalEntityRoleInfo = legalEntitiesRoleInfo.optJSONObject(index1);
			String legalEntityId = legalEntityRoleInfo.optString("legalEntityId");
			requestInstance.addRequestParam_("legalEntityId", legalEntityId);
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance, reqPermissions))
	        {
	            processedResult.addParam(new Param("message", "User do not have access to Create Role for Legal Entity " + legalEntityId, FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
	            alert.prepareError("Logged in user do not have access to this legalEntity - " + legalEntityId).log();
	            return processedResult;        
	        }
		}
		
		for (int index = 0; index < legalEntitiesRoleInfo.length(); index++) {
			JSONObject legalEntityRoleInfo = legalEntitiesRoleInfo.optJSONObject(index);
			
			String legalEntityId = legalEntityRoleInfo.optString("legalEntityId");
			if(legalEntityRoleInfo.has("Role_Id")){
				roleId = legalEntityRoleInfo.getString("Role_Id");
			}

			String roleName = legalEntityRoleInfo.optString(ApprovalConstants.ROLE_NAME);
			if(StringUtils.isBlank(roleName)){
				String message = "Role Name cannot be an empty string";
				processedResult.addParam(new Param("message", message, FabricConstants.STRING));
				ErrorCodeEnum.ERR_20524.setErrorCode(processedResult);
				return processedResult;
			}
			String roleDescription = legalEntityRoleInfo.optString(ApprovalConstants.ROLE_DESC);
			if(StringUtils.isBlank(roleDescription)){
				String message = "Role Description cannot be an empty string";
				processedResult.addParam(new Param("message", message, FabricConstants.STRING));
				ErrorCodeEnum.ERR_20524.setErrorCode(processedResult);
				return processedResult;
			}
			String statusId = legalEntityRoleInfo.optString(ApprovalConstants.STATUS_ID);
			String inputAddedServiceDefinitionIds = legalEntityRoleInfo.optString(INPUT_ADDED_SERVICEDEFINITIONS);
			// String inputRemovedCustomerRoles =
			// requestInstance.getParameter(INPUT_REMOVED_ROLES);
			JSONArray addedServiceDefinitionIds = null;
			// JSONArray removedCustomerRoles = null;

			Boolean isApprovalRequired = false;
			JSONObject recordId = new JSONObject();
			String requestId = "";
			EventEnum eventName = EventEnum.CREATE;

			roleDescription = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roleDescription);

			if (StringUtils.isNotBlank(inputAddedServiceDefinitionIds)) {
				addedServiceDefinitionIds = new JSONArray(inputAddedServiceDefinitionIds);
			}
			/*
			 * if (StringUtils.isNotBlank(inputRemovedCustomerRoles)) { removedCustomerRoles
			 * = new JSONArray(inputRemovedCustomerRoles); }
			 */

			try {

				// Read and validate Inputs
				if (StringUtils.length(roleName.trim()) < ROLE_NAME_MIN_LENGTH
						|| StringUtils.length(roleName) > ROLE_NAME_MAX_LENGTH) {
					// Return Error Response
					String message = "Role Name should have a minimum of " + ROLE_NAME_MIN_LENGTH
							+ " characters and a maximum of " + ROLE_NAME_MAX_LENGTH + " characters";
					processedResult.addParam(new Param("message", message, FabricConstants.STRING));
					ErrorCodeEnum.ERR_20524.setErrorCode(processedResult);
					return processedResult;
				}
				if (CommonUtilities.containAnySpecialCharacters(roleName)) {
					String message = "Role Name should not conatin special characters";
					processedResult.addParam(new Param("message", message, FabricConstants.STRING));
					ErrorCodeEnum.ERR_20524.setErrorCode(processedResult);
					return processedResult;
				}

				if (StringUtils.length(roleDescription.trim()) < ROLE_DESCRIPTION_MIN_LENGTH
						|| StringUtils.length(roleDescription) > ROLE_DESCRIPTION_MAX_LENGTH) {
					// Return Error Response
					String message = "Role Description should have a minimum of " + ROLE_DESCRIPTION_MIN_LENGTH
							+ " characters and a maximum of " + ROLE_DESCRIPTION_MAX_LENGTH + " characters";
					processedResult.addParam(new Param("message", message, FabricConstants.STRING));
					ErrorCodeEnum.ERR_20524.setErrorCode(processedResult);
					return processedResult;
				}
				if (CommonUtilities.containAnySpecialCharactersForDescription(roleDescription)) {
					String message = "Role Description should not conatin special characters";
					processedResult.addParam(new Param("message", message, FabricConstants.STRING));
					ErrorCodeEnum.ERR_20524.setErrorCode(processedResult);
					return processedResult;
				}

				// Fetch Logged In User Info
				String loggedInUserId = null;
				UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
				if (userDetailsBeanInstance != null) {
					loggedInUserId = userDetailsBeanInstance.getId();
				}

				// Make a call to workflow to get whether approval is required for this feature
				// or not when maker checker is enabled

				JSONObject approvalRequiredobj = ApprovalUtils.getApprovalDetails("",
						ApprovalConstants.EMPLOYEE_MANAGEMENT, ApprovalConstants.ROLES, loggedInUserId,
						ApprovalConstants.CONTEXT_INITIATE, requestInstance);
				requestId = approvalRequiredobj.optString(ApprovalConstants.REQUEST_ID);
				String ApprovalRequired = approvalRequiredobj.optString(ApprovalConstants.IS_APPROVAL_REQUIRED);
				isApprovalRequired = Boolean.valueOf(ApprovalRequired);
				if (isApprovalRequired)
					eventName = EventEnum.SUBMITTEDFORAPPROVAL;

				// Create Role
				createRole(roleId, roleName, roleDescription, statusId, loggedInUserId, requestInstance, requestId,
						isApprovalRequired, legalEntityId);

				// Assign Role Permissions
				JSONArray newPermissions = CommonUtilities
						.getStringAsJSONArray(legalEntityRoleInfo.optString(ApprovalConstants.PERMISSION_IDS));
				assignPermissionsToRole(roleId, CommonUtilities.getJSONArrayAsList(newPermissions), loggedInUserId,
						requestInstance, requestId, isApprovalRequired, legalEntityId);

				// Manage the internal user role to servicedefinition mapping
				// if (addedServiceDefinitionIds != null && removedCustomerRoles != null) {
				if (addedServiceDefinitionIds != null) {
					RoleHandler.editMappingForUserroleToCustomerRole(roleId, legalEntityId, addedServiceDefinitionIds,
							new JSONArray(), requestInstance, requestId, isApprovalRequired);
				}

				String attributeValue = ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance);
				if (StringUtils.isNotBlank(attributeValue) && attributeValue.equals("false")) {
					// Link Internal Users
					JSONArray newUsers = CommonUtilities
							.getStringAsJSONArray(legalEntityRoleInfo.optString(ApprovalConstants.USERIDS));
					List<String> userIds = CommonUtilities.getJSONArrayAsList(newUsers);
					if (userIds != null && !userIds.isEmpty()) {
						recordId.put(ApprovalConstants.USER_IDS, newUsers);
					}
					RoleHandler.assignRoleToUsers(requestInstance, loggedInUserId, roleId, legalEntityId, userIds,
							requestId, isApprovalRequired);

				}
				// In case of Successful insertion to approve tables
				if (isApprovalRequired) {
					// call integration service to update recordid
					recordId.put(ApprovalConstants.ID1, roleId);
					ApprovalUtils.updateapprovalrequest(requestId, recordId.toString(), requestInstance);
					processedResult
							.addParam(new Param(ApprovalConstants.REQUEST_ID, requestId, FabricConstants.STRING));
				} else {
					processedResult.addParam(new Param(ApprovalConstants.ROLE_ID, roleId, FabricConstants.STRING));
				}
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
						ActivityStatusEnum.SUCCESSFUL, "Role name: " + roleName);

			} catch (ApplicationException e) {
				// In case of Failure we need to delete approvalrequest
				if (isApprovalRequired) {
					ApprovalUtils.deleteapprovalrequest(requestId, requestInstance);
				}
				Result errorResult = new Result();
				alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
						ActivityStatusEnum.FAILED, "Role name: " + roleName);
				e.getErrorCodeEnum().setErrorCode(errorResult);
				return errorResult;
			} catch (Exception e) {
				Result errorResult = new Result();
				diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
						ActivityStatusEnum.FAILED, "Role name: " + roleName);
				ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
				return errorResult;
			}
		}
		return processedResult;

	}

	private void createRole(String roleId, String name, String description, String status, String loggedInUser,
			DataControllerRequest requestInstance, String requestId, Boolean isApprovalRequired, String legalEntityId)
			throws ApplicationException {

		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("id", roleId);
		inputMap.put("Name", name);
		inputMap.put("Status_id", status);
		inputMap.put("Parent_id", roleId);
		inputMap.put("Description", description);
		inputMap.put("createdby", loggedInUser);
		inputMap.put("Type_id", DEFAULT_ROLE_TYPE_ID);
		inputMap.put("companyLegalUnit", legalEntityId);
		inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
		String serviceResponse = null;

		// If approval is required move data to _approval table otherwise to original
		// table
		if (isApprovalRequired) {
			inputMap.put("aprRequestId", requestId);
			inputMap.put("crudAction", "INS");

			serviceResponse = Executor.invokeService(ServiceURLEnum.ROLE_APPROVE_CREATE, inputMap, null,
					requestInstance);
		} else {
			serviceResponse = Executor.invokeService(ServiceURLEnum.ROLE_CREATE, inputMap, null, requestInstance);
		}
		JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

		if (responseJSON == null || !responseJSON.has(FabricConstants.OPSTATUS)
				|| responseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20546);
		}

	}

	public void assignPermissionsToRole(String roleId, List<String> permissionsList, String loggedInUser,
			DataControllerRequest requestInstance, String requestId, Boolean isApprovalRequired, String legalEntityId)
			throws ApplicationException {

		String serviceResponse = null;
		JSONObject serviceResponseJSON = null;
		Map<String, String> inputMap = new HashMap<>();

//        for (int i = 0; i < permissionsList.size(); i++) {
//            inputMap.put("Role_id", roleId);
//            inputMap.put("Permission_id", permissionsList.get(i));
//            inputMap.put("createdby", "NULL");
//            inputMap.put("modifiedby", "NULL");
//            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
//            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
//            inputMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());
//            inputMap.put("softdeleteflag", "0");
//            serviceResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSION_CREATE, inputMap, null,
//                    requestInstance);
//            serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
//            if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
//                    || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
//                // Throw Application Exception
//                alert.prepareError("Failed CRUD Operation. Response" + serviceResponse).log();
//                throw new ApplicationException(ErrorCodeEnum.ERR_20528);
//            }
//            inputMap.clear();
		inputMap.put("_requestId", requestId);

		inputMap.put("_roleId", roleId);
		inputMap.put("_permissions", StringUtils.join(permissionsList, "|"));
		inputMap.put("_companyLegalUnit", legalEntityId);

		// If approval is required move data to _approval table otherwise to original
		// table
		if (isApprovalRequired)
			serviceResponse = Executor.invokeService(ServiceURLEnum.BULKASSIGN_PERMISSIONS_TO_ROLE_APPROVAL, inputMap,
					null, requestInstance);
		else
			serviceResponse = Executor.invokeService(ServiceURLEnum.BULKASSIGN_PERMISSIONS_TO_ROLE, inputMap, null,
					requestInstance);

		serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
				|| serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			// Throw Application Exception
			alert.prepareError("Failed CRUD Operation. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20528);
		}
	}

	public void removeUserRoleAssociation(String userId, String roleId, DataControllerRequest requestInstance)
			throws ApplicationException {

		if (StringUtils.isBlank(roleId) || StringUtils.isBlank(userId)) {
			return;
		}

		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("Role_id", roleId);
		inputMap.put("User_id", roleId);
		String serviceResponse = Executor.invokeService(ServiceURLEnum.USERROLE_DELETE, inputMap, null,
				requestInstance);
		JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
				|| serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20547);
		}

	}

	public JSONObject getUserProfile(String userId, DataControllerRequest requestInstance) throws ApplicationException {

		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ODataQueryConstants.FILTER, "User_id eq '" + userId + "'");

		String serviceResponse = Executor.invokeService(ServiceURLEnum.USERROLE_READ, inputMap, null, requestInstance);
		JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
				|| serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			alert.prepareError("Failed CRUD Operation. Response" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20530);
		}
		JSONArray userRoleRecords = serviceResponseJSON.optJSONArray("userrole");
		if (userRoleRecords != null && userRoleRecords.optJSONObject(0) != null) {
			return userRoleRecords.optJSONObject(0);
		}
		return new JSONObject();
	}

}
