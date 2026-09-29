package com.kony.adminconsole.service.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to manage the Permssions(Edit Permissions,assign/remove
 * users,assign/remove roles)
 *
 * @author Aditya Mankal
 * 
 */
public class ManagePermissionsService implements JavaService2 {

	//private static final int PERMISSION_NAME_MAX_CHARS = 25;
	private static final int PERMISSION_NAME_MAX_CHARS = 50; //TSR-913240 fix
	private static final int PERMISSION_DESCRIPTION_MAX_CHARS = 300;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();

		Map<String, String> postParametersMap = new HashMap<>();
		UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);

		String userID = userDetailsBeanInstance.getId();
		String permissionDetailsJSONString = requestInstance.getParameter("Permission_Details");
		String roleAssignedToJSONString = requestInstance.getParameter("AssignedTo");
		String roleRemovedFromJSONString = requestInstance.getParameter("RemovedFrom");
		String permissionName = null;

		try {
			boolean isValidPermissionData = true;
			JSONObject permissionDetailsJSONObject = CommonUtilities.getStringAsJSONObject(permissionDetailsJSONString);
			JSONObject permissionAssignedToJSONObject = CommonUtilities.getStringAsJSONObject(roleAssignedToJSONString);
			JSONObject permissionRemovedFromJSONObject = CommonUtilities
					.getStringAsJSONObject(roleRemovedFromJSONString);
			String permissionId = permissionDetailsJSONObject.getString("id");
			StringBuffer errorMessageBuffer = new StringBuffer();
			String permissionDescription = null, updatePermissionResponse = null;

			// Validate the Permission information and update if deemed to be valid
			postParametersMap.clear();
			postParametersMap.put("id", permissionId);
			if (permissionDetailsJSONObject.has("Name")) {
				permissionName = permissionDetailsJSONObject.getString("Name");
				if (StringUtils.isBlank(permissionName)) {
					errorMessageBuffer.append("Permission Name cannot be an empty string\n");
					isValidPermissionData = false;
				} else if (CommonUtilities.containSpecialChars(permissionName)) {
					errorMessageBuffer.append("Permission Name cannot contain special characters");
					isValidPermissionData = false;
				} else if (permissionName.length() > PERMISSION_NAME_MAX_CHARS) {
					errorMessageBuffer.append(
							"Permission Name cannot have more than " + PERMISSION_NAME_MAX_CHARS + " characters\n");
					isValidPermissionData = false;
				} else
				{
					permissionName = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(permissionName);
					postParametersMap.put("Name", StringEscapeUtils.escapeHtml(permissionName));
				}
			}
			if (permissionDetailsJSONObject.has("Description")) {
				permissionDescription = permissionDetailsJSONObject.getString("Description");
				if (StringUtils.isBlank(permissionDescription)) {
					errorMessageBuffer.append("Permission Description cannot be an empty string\n");
					isValidPermissionData = false;
				} else if (CommonUtilities.containSpecialChars(permissionDescription)) {
					errorMessageBuffer.append("Permission Description cannot contain special characters");
					isValidPermissionData = false;
				} else if (permissionDescription.length() > PERMISSION_DESCRIPTION_MAX_CHARS) {
					errorMessageBuffer.append("Permission Description cannot have more than "
							+ PERMISSION_DESCRIPTION_MAX_CHARS + " characters\n");
					isValidPermissionData = false;
				} else
				{
					permissionDescription= CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(permissionDescription);

					postParametersMap.put("Description", StringEscapeUtils.escapeHtml(permissionDescription));
				}
			}

			if (permissionDetailsJSONObject.has("Status_id"))
				postParametersMap.put("Status_id", permissionDetailsJSONObject.getString("Status_id"));

			if (isValidPermissionData) {
				if (StringUtils.isBlank(permissionName)) {
					try { // For logging, permission name is mandatory.
						Map<String, String> readPostParametersMap = new HashMap<String, String>();
						readPostParametersMap.put(ODataQueryConstants.FILTER, "id eq " + permissionId);
						JSONObject readPermissionResponseJSON = CommonUtilities
								.getStringAsJSONObject(Executor.invokeService(ServiceURLEnum.PERMISSION_READ,
										readPostParametersMap, null, requestInstance));
						permissionName = readPermissionResponseJSON.getJSONArray("permission").getJSONObject(0)
								.getString("Name");
					} catch (Exception e) {
						// Can be ignored
					}
				}
				updatePermissionResponse = Executor.invokeService(ServiceURLEnum.PERMISSION_UPDATE, postParametersMap,
						null, requestInstance);
				Param updatePermissionRecord = new Param("updatePermissionResponse", updatePermissionResponse,
						FabricConstants.STRING);
				processedResult.addParam(updatePermissionRecord);
				JSONObject updatePermissionResponseJSON = CommonUtilities
						.getStringAsJSONObject(updatePermissionResponse);
				if (updatePermissionResponseJSON != null && updatePermissionResponseJSON.has(FabricConstants.OPSTATUS)
						&& updatePermissionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
							ActivityStatusEnum.SUCCESSFUL,
							"Update Permission Successful. Permission name: " + permissionName);
				} else {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED, "Update Permission Failed. Permission name: " + permissionName);
				}
			} else { // Invalid Permission data
				ErrorCodeEnum.ERR_20523.setErrorCode(processedResult);
				Param errorDescriptionParam = new Param("errorDescription", errorMessageBuffer.toString(),
						FabricConstants.STRING);
				processedResult.addParam(errorDescriptionParam);
				return processedResult;
			}

			Set<String> listOfPermissions = new HashSet<String>();
			listOfPermissions.add(permissionId);
			JSONArray permissionRemovedFromRolesArray = null, permissionAssignedToRolesArray = null,
					permissionRemovedFromUsersArray = null, permissionAssignedToUsersArray = null;

			if (permissionRemovedFromJSONObject != null) {
				if (permissionRemovedFromJSONObject.has("rolesList")) {
					permissionRemovedFromRolesArray = permissionRemovedFromJSONObject.getJSONArray("rolesList");
				}
				if (permissionRemovedFromJSONObject.has("usersList")) {
					permissionRemovedFromUsersArray = permissionRemovedFromJSONObject.getJSONArray("usersList");
				}
			}

			if (permissionAssignedToJSONObject != null) {
				if (permissionAssignedToJSONObject.has("rolesList")) {
					permissionAssignedToRolesArray = permissionAssignedToJSONObject.getJSONArray("rolesList");
				}
				if (permissionAssignedToJSONObject.has("usersList")) {
					permissionAssignedToUsersArray = permissionAssignedToJSONObject.getJSONArray("usersList");
				}
			}
			HashMap<String, ArrayList<Action>> compositeActionMapping = ActionHandler.getChildActions(listOfPermissions,
					requestInstance);

			ActionHandler.removeActionFromRoles(requestInstance, permissionId,
					CommonUtilities.getJSONArrayAsList(permissionRemovedFromRolesArray), compositeActionMapping);
			ActionHandler.assignActionToRoles(requestInstance, userID, permissionId,
					CommonUtilities.getJSONArrayAsList(permissionAssignedToRolesArray), compositeActionMapping);
			
			String attributeValue = ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance);
			if(attributeValue.equalsIgnoreCase("false") || attributeValue.equals("0")) {

			ActionHandler.removeActionFromUsers(requestInstance, permissionId,
					CommonUtilities.getJSONArrayAsList(permissionRemovedFromUsersArray));

			ActionHandler.assignActionToUsers(requestInstance, userID, permissionId,
					CommonUtilities.getJSONArrayAsList(permissionAssignedToUsersArray), compositeActionMapping);
			}
			/*
			 * // Get the Composite Permission Information for the stated permission
			 * HashMap<String, ArrayList<Permission>> compositePermissionMapping =
			 * PermissionHandler .getChildPermissions(listOfPermissions, requestInstance);
			 * 
			 * // Processing the removed Roles list. Composite permissions corresponding to
			 * // each parent permission are also removed
			 * PermissionHandler.removePermissionFromRoles(requestInstance, permissionId,
			 * CommonUtilities.getJSONArrayAsList(permissionRemovedFromRolesArray),
			 * compositePermissionMapping);
			 * 
			 * // Processing the added Roles list. Composite permissions corresponding to
			 * each // parent permission are also added
			 * PermissionHandler.assignPermissionToRoles(requestInstance, userID,
			 * permissionId,
			 * CommonUtilities.getJSONArrayAsList(permissionAssignedToRolesArray),
			 * compositePermissionMapping);
			 * 
			 * // Processing the removed users list. The listed users are unlinked from the
			 * // stated role PermissionHandler.removePermissionFromUsers(requestInstance,
			 * permissionId,
			 * CommonUtilities.getJSONArrayAsList(permissionRemovedFromUsersArray),
			 * compositePermissionMapping);
			 * 
			 * // Processing the assigned users list. The listed users are assigned the
			 * stated // role PermissionHandler.assignPermissionToUsers(requestInstance,
			 * userID, permissionId,
			 * CommonUtilities.getJSONArrayAsList(permissionAssignedToUsersArray),
			 * compositePermissionMapping);
			 */
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PERMISSIONS, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "Permission name: " + permissionName);

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			Param javaExceptionParam = new Param("JavaError", e.getMessage(), FabricConstants.STRING);
			errorResult.addParam(javaExceptionParam);
			alert.prepareError("Exception in Managing Permission Configuration. Exception:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}
}
