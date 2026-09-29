package com.kony.adminconsole.service.role;

import java.util.ArrayList;
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

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.dto.Action;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.ActionHandler;
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
import com.konylabs.middleware.dataobject.JSONToResult;
/**
 * Service to manage the Roles(Edit Role,assign/remove users,assign/remove
 * permissions)
 *
 * @author Aditya Mankal, Akhil
 * 
 */
public class ManageRolesService implements JavaService2 {

	private static final int ROLE_NAME_MAX_CHARS = 50;
	private static final int ROLE_NAME_MIN_CHARS = 5;
	private static final int ROLE_DESCRIPTION_MAX_CHARS = 300;
	private static final int ROLE_DESCRIPTION_MIN_CHARS = 5;
//	private static final String INPUT_ADDED_ROLES = "AddedRoles";
//	private static final String INPUT_REMOVED_ROLES = "RemovedRoles";
	private static final String INPUT_ADDED_SERVICEDEFINITIONS = "AddedServiceDefinitions";
	private static final String INPUT_REMOVED_SERVICEDEFINITIONS = "RemovedServiceDefinitions";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();

		Boolean isApprovalRequired = false;
		Boolean isPreviousApprovalPending = false;
		EventEnum eventName = EventEnum.UPDATE;

		JSONObject recordId = new JSONObject();
		String requestId = "";
		try {
			// Fetch Logged in User Info
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);

			// Read Input Parameters
			String userId = userDetailsBeanInstance.getId();
			JSONArray legalEntitiesRoleInfo = new JSONArray();
			
			if(requestInstance.getParameter("legalEntitiesRoleInfo") != null) {
	            legalEntitiesRoleInfo = new JSONArray(
	                    requestInstance.getParameter("legalEntitiesRoleInfo").toString());
	            if(legalEntitiesRoleInfo == null || legalEntitiesRoleInfo.length() == 0) {
	                String message = "Empty Legal Entity role data for manage Role service";
	                processedResult.addParam(new Param("message", message, FabricConstants.STRING));
	                ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
	                return processedResult;
	            }
	        } else {
	            String message = "Empty payload for manage Role service";
	            processedResult.addParam(new Param("message", message, FabricConstants.STRING));
	            ErrorCodeEnum.ERR_20541.setErrorCode(processedResult);
	            return processedResult;
	        }

			String[] reqPermissions = {PermissionName.ASSIGN_ROLE_PERMISSIONS,PermissionName.ASSIGN_USERROLE,PermissionName.ASSIGNUSERS,PermissionName.MODIFY_ROLESTATUS};
			for(int index1 = 0; index1 < legalEntitiesRoleInfo.length(); index1++){
				JSONObject legalEntityRoleInfo = legalEntitiesRoleInfo.optJSONObject(index1);
				String legalEntityId = legalEntityRoleInfo.optString("legalEntityId");
				requestInstance.addRequestParam_("legalEntityId", legalEntityId);
				if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	            {
					processedResult.addParam(new Param("message", "User do not have access to Update Role for Legal Entity " + legalEntityId, FabricConstants.STRING));
	                ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
	                alert.prepareError("Logged in user do not have access to this legalEntity - " + legalEntityId).log();
	                return processedResult;        
	            }
			}
			
			ArrayList<JSONObject> createRoleLegalEntitiesRoleInfo = new ArrayList<JSONObject>();

			for(int i = 0; i < legalEntitiesRoleInfo.length(); i++) {
			JSONObject legalEntityRoleInfo = legalEntitiesRoleInfo.optJSONObject(i);
			String legalEntityId =  legalEntityRoleInfo.optString("legalEntityId");
			
			String roleDetailsJSONString = null;
			String assignedToJSONString = null;
			String removedFromJSONString = null;
			String inputRemovedServiceDefinitions = null;
			String inputAddedServiceDefinitions = null;
    		if(legalEntityRoleInfo.has("Role_Details") && legalEntityRoleInfo.optJSONObject("Role_Details") != null) {
    		   roleDetailsJSONString = legalEntityRoleInfo.getJSONObject("Role_Details").toString();
    		}
			if(legalEntityRoleInfo.has("AssignedTo") && legalEntityRoleInfo.optJSONObject("AssignedTo") != null) {
			    assignedToJSONString = legalEntityRoleInfo.getJSONObject("AssignedTo").toString();
			}
			if(legalEntityRoleInfo.has("RemovedFrom") && legalEntityRoleInfo.optJSONObject("RemovedFrom") != null) {
			    removedFromJSONString = legalEntityRoleInfo.getJSONObject("RemovedFrom").toString();
			}
			if(legalEntityRoleInfo.has(INPUT_ADDED_SERVICEDEFINITIONS) && legalEntityRoleInfo.optJSONArray(INPUT_ADDED_SERVICEDEFINITIONS) != null) {
			    inputAddedServiceDefinitions = legalEntityRoleInfo.getJSONArray(INPUT_ADDED_SERVICEDEFINITIONS).toString();
			}
			if(legalEntityRoleInfo.has(INPUT_REMOVED_SERVICEDEFINITIONS) && legalEntityRoleInfo.optJSONArray(INPUT_REMOVED_SERVICEDEFINITIONS) != null) {
			    inputRemovedServiceDefinitions = legalEntityRoleInfo.getJSONArray(INPUT_REMOVED_SERVICEDEFINITIONS).toString();
			}
			JSONArray addedAddedServiceDefinitions = null;
			JSONArray removedRemovedServiceDefinitions = null;

			if (StringUtils.isNotBlank(inputAddedServiceDefinitions)) {
				addedAddedServiceDefinitions = new JSONArray(inputAddedServiceDefinitions);
			}
			if (StringUtils.isNotBlank(inputRemovedServiceDefinitions)) {
				removedRemovedServiceDefinitions = new JSONArray(inputRemovedServiceDefinitions);
			}

			String roleName = null;
			boolean isValidRoleData = true;

			JSONObject roleDetailsJSONObject = CommonUtilities.getStringAsJSONObject(roleDetailsJSONString);
			JSONObject assignedToJSONObject = CommonUtilities.getStringAsJSONObject(assignedToJSONString);
			JSONObject removedFromJSONObject = CommonUtilities.getStringAsJSONObject(removedFromJSONString);
			String roleId = roleDetailsJSONObject.optString("id");

			Map<String, String> inputMap = new HashMap<>();

			// Validate the Role information
			String roleDescription = null, updateRoleResponse = null;
			StringBuffer errorMessageBuffer = new StringBuffer();

			// Make a call to workflow to get whether approval is required for this feature
			// or not when maker checker is enabled
			recordId.put(ApprovalConstants.ID1, roleId);
			recordId.put("companyLegalUnit", legalEntityId);
			JSONObject approvalRequiredobj = ApprovalUtils.getApprovalDetails(recordId.toString(),
					ApprovalConstants.EMPLOYEE_MANAGEMENT, ApprovalConstants.ROLES, userId,
					ApprovalConstants.CONTEXT_INITIATE, requestInstance);
			requestId = approvalRequiredobj.optString(ApprovalConstants.REQUEST_ID);
			String ApprovalRequired = approvalRequiredobj.optString(ApprovalConstants.IS_APPROVAL_REQUIRED);
			String previousApprovalPending = approvalRequiredobj.optString(ApprovalConstants.PREVIOUS_APPROVAL_PENDING);

			isApprovalRequired = Boolean.valueOf(ApprovalRequired);
			isPreviousApprovalPending = Boolean.valueOf(previousApprovalPending);
			// If approval is required change the event name for audit history and dump all
			// the role data into _approval tables
			if (isApprovalRequired && isPreviousApprovalPending) {
				processedResult.addStringParam(ApprovalConstants.IS_PREVIOUS_APPROVAL_PENDING, "1");
				return processedResult;
			}

			inputMap.put("id", roleId);
			
			if (legalEntityRoleInfo.has("legalEntityId")) {
				if (StringUtils.isBlank(legalEntityId)) {
					errorMessageBuffer.append("LegalEntityId cannot be an empty string\n");
					isValidRoleData = false;
				} else if (CommonUtilities.containAnySpecialCharacters(legalEntityId)) {
					errorMessageBuffer.append("LegalEntityId cannot contain special characters");
					isValidRoleData = false;
				} else
					inputMap.put("companyLegalUnit", legalEntityId);
			} else {
				errorMessageBuffer.append("LegalEntityId is mandatory field");
				isValidRoleData = false;
			}

			Map<String, String> postParametersMap = new HashMap<>();
			postParametersMap.put(ODataQueryConstants.FILTER, "id eq " + roleId + " and companyLegalUnit eq '"+ legalEntityId +"'");
			JSONObject roleReadDetailsResponse = CommonUtilities.getStringAsJSONObject(Executor.invokeService(
					ServiceURLEnum.ROLE_READ, postParametersMap, null, requestInstance));
			if(roleReadDetailsResponse != null){
				JSONArray roleJsonArray = roleReadDetailsResponse.getJSONArray("role");
				if(roleJsonArray ==  null || roleJsonArray.length() == 0){
					createRoleLegalEntitiesRoleInfo.add(legalEntityRoleInfo);
					continue;
				}
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_21456);
			}

			if (roleDetailsJSONObject != null) {

				if (roleDetailsJSONObject.has("Name")) {
					roleName = roleDetailsJSONObject.getString("Name");
					if (StringUtils.isBlank(roleName)) {
						errorMessageBuffer.append("Role Name cannot be an empty string\n");
						isValidRoleData = false;
					} else if (CommonUtilities.containAnySpecialCharacters(roleName)) {
						errorMessageBuffer.append("Role Name cannot contain special characters");
						isValidRoleData = false;
					} else if (roleName.length() > ROLE_NAME_MAX_CHARS) {
						errorMessageBuffer
								.append("Role Name cannot have more than " + ROLE_NAME_MAX_CHARS + " characters\n");
						isValidRoleData = false;
					} else if (roleName.trim().length() < ROLE_NAME_MIN_CHARS) {
						errorMessageBuffer
								.append("Role Name cannot have less than " + ROLE_NAME_MIN_CHARS + " characters\n");
						isValidRoleData = false;
					} else
						inputMap.put("Name", roleName);
				}

				if (roleDetailsJSONObject.has("Description")) {
					roleDescription = roleDetailsJSONObject.getString("Description");
					if (StringUtils.isBlank(roleDescription)) {
						errorMessageBuffer.append("Role Description cannot be an empty string\n");
						isValidRoleData = false;
					} else if (CommonUtilities.containAnySpecialCharactersForDescription(roleDescription)) {
						errorMessageBuffer.append("Role Description cannot contain special characters");
						isValidRoleData = false;
					} else if (roleDescription.length() > ROLE_DESCRIPTION_MAX_CHARS) {
						errorMessageBuffer.append("Role Description cannot have more than " + ROLE_DESCRIPTION_MAX_CHARS
								+ " characters\n");
						isValidRoleData = false;
					} else if (roleDescription.trim().length() < ROLE_DESCRIPTION_MIN_CHARS) {
						errorMessageBuffer.append("Role Description cannot have less than " + ROLE_DESCRIPTION_MIN_CHARS
								+ " characters\n");
						isValidRoleData = false;
					} else {
						roleDescription = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(roleDescription);// Modified

						inputMap.put("Description", roleDescription);
					}
				}

				if (roleDetailsJSONObject.has("Status_id")) {
					inputMap.put("Status_id", roleDetailsJSONObject.getString("Status_id"));

				}

				if (isValidRoleData) {

					// For logging, Role Name is mandatory.Fetch the value
					if (StringUtils.isBlank(roleName)) {
						try {
							Map<String, String> readPostParametersMap = new HashMap<>();
							readPostParametersMap.put(ODataQueryConstants.FILTER, "id eq " + roleId + " and companyLegalUnit eq '"+ legalEntityId +"'");
							JSONObject roleReadResponse = CommonUtilities.getStringAsJSONObject(Executor.invokeService(
									ServiceURLEnum.ROLE_READ, readPostParametersMap, null, requestInstance));
							JSONObject roleJsonObject = roleReadResponse.getJSONArray("role").getJSONObject(0);
							roleName = roleJsonObject.getString("Name");
						} catch (Exception e) {
							// Can be ignored
						}
					}
					// role table
					if (isApprovalRequired) {

						/*
						 * Dump Details to _approval tables
						 */
						eventName = EventEnum.SUBMITTEDFORAPPROVAL;
						inputMap.put("_roleId", roleId);
						inputMap.put("_requestId", requestId);
						Executor.invokeService(ServiceURLEnum.ROLEDATAMOVEMENT_APPROVAL_PROC, inputMap, null,
								requestInstance);

						// If approval is required update the record in _approval table else update in
						inputMap.put("aprRequestId", requestId);
						inputMap.put("crudAction", "UPD");
						updateRoleResponse = Executor.invokeService(ServiceURLEnum.ROLE_APPROVE_UPDATE, inputMap, null,
								requestInstance);
					} else {
						updateRoleResponse = Executor.invokeService(ServiceURLEnum.ROLE_UPDATE, inputMap, null,
								requestInstance);
					}
					JSONObject updateRoleResponseJSON = CommonUtilities.getStringAsJSONObject(updateRoleResponse);
					if (updateRoleResponseJSON != null && updateRoleResponseJSON.has(FabricConstants.OPSTATUS)
							&& updateRoleResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
								ActivityStatusEnum.SUCCESSFUL, "Role name: " + roleName);
					} else {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
								ActivityStatusEnum.FAILED, "Role name: " + roleName);
						throw new ApplicationException(ErrorCodeEnum.ERR_20543);
					}
				} else { // Invalid Role data
					throw new ApplicationException(ErrorCodeEnum.ERR_20524);
				}
			}

			Set<String> listOfPermissions = new HashSet<>();
			JSONArray permissionsRemovedFromRoleArray = null, permissionsAssignedToRoleArray = null,
					roleRemovedFromUsersArray = null, roleAssignedToUsersArray = null;

			// Process removed permissions and users
			if (removedFromJSONObject != null) {
				if (removedFromJSONObject.has("permissionList")) {
					permissionsRemovedFromRoleArray = removedFromJSONObject.getJSONArray("permissionList");
					recordId.put(ApprovalConstants.PERMISSIONS_REMOVED_ARRAY, permissionsRemovedFromRoleArray);
					for (int indexVar = 0; indexVar < permissionsRemovedFromRoleArray.length(); indexVar++) {
						listOfPermissions.add(permissionsRemovedFromRoleArray.optString(indexVar));
					}
					recordId.put(ApprovalConstants.PERMISSIONS_REMOVED,
							StringUtils.join(listOfPermissions.toArray(), ','));

				}
				if (removedFromJSONObject.has("usersList")) {
					roleRemovedFromUsersArray = removedFromJSONObject.getJSONArray("usersList");
					recordId.put(ApprovalConstants.ROLE_REMOVED_FROMUSERSARRAY, roleRemovedFromUsersArray);

				}
			}

			// Process added permissions and users
			if (assignedToJSONObject != null) {
				if (assignedToJSONObject.has("permissionList")) {
					permissionsAssignedToRoleArray = assignedToJSONObject.getJSONArray("permissionList");
					for (int indexVar = 0; indexVar < permissionsAssignedToRoleArray.length(); indexVar++) {
						listOfPermissions.add(permissionsAssignedToRoleArray.optString(indexVar));
					}
					recordId.put(ApprovalConstants.PERMISSIONS_ADDED_ARRAY, permissionsAssignedToRoleArray);

				}
				if (assignedToJSONObject.has("usersList")) {
					roleAssignedToUsersArray = assignedToJSONObject.getJSONArray("usersList");
					recordId.put(ApprovalConstants.ROLE_ASSIGNED_TO_USERS_ARRAY, roleAssignedToUsersArray);

				}
			}

			// Get the Composite Permission Information for all the permissions listed in
			// the added/removed list
			HashMap<String, ArrayList<Action>> compositeActionMapping = ActionHandler.getChildActions(listOfPermissions,
					requestInstance);

			// Processing the removed Permissions list. Composite permissions corresponding
			// to each parent permission are also removed

			RoleHandler.removePermissionsAndActionsFromRole(requestInstance, roleId, legalEntityId,
					CommonUtilities.getJSONArrayAsList(permissionsRemovedFromRoleArray), requestId, isApprovalRequired);

			// Processing the added Permissions list. Composite permissions corresponding to
			// each parent permission are also added
			RoleHandler.assignPermissionsAndActionsToRole(requestInstance, userId, roleId, legalEntityId,
					CommonUtilities.getJSONArrayAsList(permissionsAssignedToRoleArray), compositeActionMapping,
					requestId, isApprovalRequired);

			// Manage the internal user role to customer role mapping
			if (addedAddedServiceDefinitions != null && removedRemovedServiceDefinitions != null) {
				recordId.put(ApprovalConstants.REMOVED_SERVICEDEFINITIONS_ARRAY, removedRemovedServiceDefinitions);
				recordId.put(ApprovalConstants.ADDED_SERVICEDEFINITIONS_ARRAY, addedAddedServiceDefinitions);

				RoleHandler.editMappingForUserroleToCustomerRole(roleId, legalEntityId, addedAddedServiceDefinitions,
						removedRemovedServiceDefinitions, requestInstance, requestId, isApprovalRequired);
			}
			 
			/*
			 * // Get the Composite Permission Information for all the permissions listed in
			 * // the added/removed list HashMap<String, ArrayList<Permission>>
			 * compositePermissionMapping = PermissionHandler
			 * .getChildPermissions(listOfPermissions, requestInstance);
			 * 
			 * // Processing the removed Permissions list. Composite permissions
			 * corresponding // to each parent permission are also removed
			 * RoleHandler.removePermissionsFromRole(requestInstance, roleId,
			 * CommonUtilities.getJSONArrayAsList(permissionsRemovedFromRoleArray),
			 * compositePermissionMapping);
			 * 
			 * // Processing the added Permissions list. Composite permissions corresponding
			 * to // each parent permission are also added
			 * RoleHandler.assignPermissionToRole(requestInstance, userId, roleId,
			 * CommonUtilities.getJSONArrayAsList(permissionsAssignedToRoleArray),
			 * compositePermissionMapping);
			 * 
			 * // Manage the internal user role to customer role mapping if
			 * (addedCustomerRoles != null && removedCustomerRoles != null) {
			 * RoleHandler.editMappingForUserroleToCustomerRole(roleId, addedCustomerRoles,
			 * removedCustomerRoles, processedResult, requestInstance); }
			 */
			String attributeValue = ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance);
			if (attributeValue.equals("false")) {
				// Processing the removed users list. The listed users are unlinked from the
				// stated role

				RoleHandler.removeRoleFromUsers(requestInstance, roleId, legalEntityId,
						CommonUtilities.getJSONArrayAsList(roleRemovedFromUsersArray), requestId, isApprovalRequired);

				// Processing the assigned users list.The listed users are assigned the stated
				// role
				RoleHandler.assignRoleToUsers(requestInstance, userId, roleId, legalEntityId,
						CommonUtilities.getJSONArrayAsList(roleAssignedToUsersArray), requestId, isApprovalRequired);
			}
			if (isApprovalRequired) {
				// call integration service to update recordid
				recordId.put(ApprovalConstants.ID1, roleId);
				ApprovalUtils.updateapprovalrequest(requestId, recordId.toString(), requestInstance);
				processedResult.addParam(new Param(ApprovalConstants.REQUEST_ID, requestId, FabricConstants.STRING));
			} else {
				processedResult.addParam(new Param(ApprovalConstants.ROLE_ID, roleId, FabricConstants.STRING));
			}
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
					ActivityStatusEnum.SUCCESSFUL, "Role name: " + roleName);
		 }

	    // for newly added legal entities for role creating role legal entities data
		if (createRoleLegalEntitiesRoleInfo.size() > 0) {
			JSONArray legalEntitiesCreateRoleInfoArray = new JSONArray();
			for (int j = 0; j < createRoleLegalEntitiesRoleInfo.size(); j++) {
				JSONObject legalEntityRoleInfo = createRoleLegalEntitiesRoleInfo.get(j);
				JSONObject legalEntityCreateRoleInfo = new JSONObject();
				JSONObject roleDetailsJSONObject = legalEntityRoleInfo.optJSONObject("Role_Details");
				JSONObject assignedToObject = legalEntityRoleInfo.optJSONObject("AssignedTo");
				JSONArray addedServiceDefinitionsObject = legalEntityRoleInfo
						.optJSONArray("AddedServiceDefinitions");
				legalEntityCreateRoleInfo.put("legalEntityId", legalEntityRoleInfo.optString("legalEntityId"));
				legalEntityCreateRoleInfo.put("Role_Name", roleDetailsJSONObject.optString("Name"));
				legalEntityCreateRoleInfo.put("Role_Desc", roleDetailsJSONObject.optString("Description"));
				legalEntityCreateRoleInfo.put("Status_id", roleDetailsJSONObject.optString("Status_id"));
				legalEntityCreateRoleInfo.put("Role_Id", roleDetailsJSONObject.optString("id"));
				legalEntityCreateRoleInfo.put("system_user", userId);
				legalEntityCreateRoleInfo.put("Permission_ids", assignedToObject.optJSONArray("permissionList"));
				legalEntityCreateRoleInfo.put("User_ids", assignedToObject.optJSONArray("usersList"));
				legalEntityCreateRoleInfo.put("AddedServiceDefinitions", addedServiceDefinitionsObject);
				legalEntitiesCreateRoleInfoArray.put(legalEntityCreateRoleInfo);
			}
			if (legalEntitiesCreateRoleInfoArray.length() > 0) {
				Map<String, String> postParametersMap = new HashMap<>();
				postParametersMap.put("legalEntitiesRoleInfo", legalEntitiesCreateRoleInfoArray.toString());
				JSONObject createroleForPendingLEResponse = CommonUtilities
						.getStringAsJSONObject(Executor.invokeService(
								ServiceURLEnum.ROLESANDPERMISSIONMANAGEMENT_CREATEROLE, postParametersMap, null,
								requestInstance));
				if (createroleForPendingLEResponse == null) {
					throw new ApplicationException(ErrorCodeEnum.ERR_20546);
				} else if(createroleForPendingLEResponse.has(ErrorCodeEnum.ERROR_CODE_KEY) 
					|| createroleForPendingLEResponse.has(ErrorCodeEnum.ERROR_MESSAGE_KEY)){
					processedResult = JSONToResult.convert(createroleForPendingLEResponse.toString());
					return processedResult;
				}
			}
		}

		// update the access restricted legal entities role name & description as same as for all the legal entities of that role
		ArrayList<String> legalEntityIdsList = new ArrayList<String>();
		for(int i=0; i < legalEntitiesRoleInfo.length(); i++) {
			legalEntityIdsList.add(legalEntitiesRoleInfo.getJSONObject(i).getString("legalEntityId"));
		}

		if (legalEntitiesRoleInfo.getJSONObject(0).has("Role_Details")
				&& legalEntitiesRoleInfo.getJSONObject(0).getJSONObject("Role_Details") != null
				&& legalEntitiesRoleInfo.getJSONObject(0).getJSONObject("Role_Details").has("Name") 
				&& legalEntitiesRoleInfo.getJSONObject(0).getJSONObject("Role_Details").has("Description")) {
			String roleName = legalEntitiesRoleInfo.getJSONObject(0).getJSONObject("Role_Details")
					.getString("Name");
			String roleDescription = legalEntitiesRoleInfo.getJSONObject(0).getJSONObject("Role_Details")
					.getString("Description");
			String roleId = legalEntitiesRoleInfo.getJSONObject(0).getJSONObject("Role_Details").getString("id");
			HashMap<String, String> inputMap = new HashMap<>();
			
			inputMap.put("id", roleId);
			inputMap.put("Name", roleName);
			inputMap.put("Description", roleDescription);

			Map<String, String> roleMap = new HashMap<>();			
			String role_Id = legalEntitiesRoleInfo.getJSONObject(0).getJSONObject("Role_Details").getString("id");
			roleMap.put(ODataQueryConstants.FILTER, "id eq " + role_Id);
			
			JSONObject roleReadDetailsResponse = CommonUtilities.getStringAsJSONObject(Executor.invokeService(
					ServiceURLEnum.ROLE_READ, roleMap, null, requestInstance));

			if (roleReadDetailsResponse != null) {
				JSONArray roleJsonArray = roleReadDetailsResponse.getJSONArray("role");
				for(int i=0; i < roleJsonArray.length(); i++) {
					String legalEntityId = roleJsonArray.getJSONObject(i).getString("companyLegalUnit");
					if(!legalEntityIdsList.contains(legalEntityId)) {
						inputMap.put("companyLegalUnit", legalEntityId);
						String updateRoleResponse = Executor.invokeService(ServiceURLEnum.ROLE_UPDATE, inputMap, null,
								requestInstance);
						JSONObject updateRoleResponseJSON = CommonUtilities.getStringAsJSONObject(updateRoleResponse);
						if (updateRoleResponseJSON != null && updateRoleResponseJSON.has(FabricConstants.OPSTATUS)
								&& updateRoleResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
							AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
									ActivityStatusEnum.SUCCESSFUL, "Role name: " + roleName);
						} else {
							AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, eventName,
									ActivityStatusEnum.FAILED, "Role name: " + roleName);
							throw new ApplicationException(ErrorCodeEnum.ERR_20543);
						}
					}
				}
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_21456);
			}
		}

		} catch (ApplicationException e) {
			if (isApprovalRequired) {
				ApprovalUtils.deleteapprovalrequest(requestId, requestInstance);
			}
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			Param javaExceptionParam = new Param("JavaError", e.getMessage(), FabricConstants.STRING);
			errorResult.addParam(javaExceptionParam);
			alert.prepareError("Exception in Managing Role Configuration. Exception:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
		if (null != processedResult.getParamByName("dbpErrMsg")
				&& StringUtils.isNotBlank(processedResult.getParamByName("dbpErrMsg").getValue())) {
			ApprovalUtils.deleteapprovalrequest(requestId, requestInstance);
		}
		return processedResult;
	}
}
