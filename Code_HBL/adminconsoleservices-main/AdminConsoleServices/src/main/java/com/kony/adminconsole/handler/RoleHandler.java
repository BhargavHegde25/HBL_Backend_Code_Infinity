/**
 * 
 */
package com.kony.adminconsole.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.dto.Action;
import com.kony.adminconsole.dto.Permission;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ApprovalConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;


/**
 * 
 * Handler to perform operations related to Role
 * 
 * @author Aditya Mankal
 * 
 */
public class RoleHandler {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	private RoleHandler() {
		// Private Constructor
	}

    public static void removePermissionsFromRole(DataControllerRequest requestInstance, String roleId,
            List<String> permissionsList, Map<String, ArrayList<Permission>> compositePermissionMapping)
            throws ApplicationException {

        if (permissionsList != null && !permissionsList.isEmpty()) {
            Map<String, String> inputMap = new HashMap<>();
            ArrayList<Permission> currentChildPermissions = null;
            String operationResponse = StringUtils.EMPTY;
            JSONObject operationResponseJSON;
            inputMap.put("Role_id", roleId);
          

            for (String currPermissionId : permissionsList) {
                if (StringUtils.isBlank(currPermissionId))
                    continue;
                // If the current permission is composite, then the corresponding child
                // permissions are to be removed
                inputMap.remove("Permission_id");
                if (compositePermissionMapping.containsKey(currPermissionId)) {
                    currentChildPermissions = compositePermissionMapping.get(currPermissionId);
                    for (Permission currPermission : currentChildPermissions) {
                        inputMap.put("CompositePermission_id", currPermission.getId());
                        Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEPERMISSION_DELETE, inputMap, null,
                                requestInstance);
                    }
                }
                inputMap.remove("CompositePermission_id");

                inputMap.put("Permission_id", currPermissionId);
                operationResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSION_DELETE, inputMap, null,
                        requestInstance);

                operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_20528);
                }
            }
        }
    }
	public static void removePermissionsAndActionsFromRole(DataControllerRequest requestInstance, String roleId,
            List<String> permissionsList) throws ApplicationException {
		
		if (permissionsList != null && !permissionsList.isEmpty()) {
        	String permissions = StringUtils.join(permissionsList.toArray(),','); 
            Map<String, String> postParametersMap = new HashMap<>();
        	postParametersMap.put("_roleId", roleId);
	    	postParametersMap.put("_PermissionIds", permissions);
	    	String getDeleteResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSIONDELETE_PROC, postParametersMap, null, requestInstance);
			JSONObject getDeleteResponseJson = CommonUtilities.getStringAsJSONObject(getDeleteResponse);
			if (getDeleteResponseJson == null || !getDeleteResponseJson.has(FabricConstants.OPSTATUS)
					|| getDeleteResponseJson.getInt(FabricConstants.OPSTATUS) != 0) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20528);
			}
		}
	}
	public static void removePermissionsAndActionsFromRole(DataControllerRequest requestInstance, String roleId, String legalEntityId,
			List<String> permissionsList,String requestId,Boolean isApprovalRequired) throws ApplicationException {

		if (permissionsList != null && !permissionsList.isEmpty()) {
			String permissions = StringUtils.join(permissionsList.toArray(), ',');
			Map<String, String> postParametersMap = new HashMap<>();
			postParametersMap.put("_roleId", roleId);
			postParametersMap.put("_companyLegalUnit", legalEntityId);
			postParametersMap.put("_PermissionIds", permissions);
			String getDeleteResponse=null;
			if(isApprovalRequired)
			{
				postParametersMap.put("_requestId", requestId);
			 getDeleteResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSIONUPDATE_APPROVAL_PROC,
					postParametersMap, null, requestInstance);
			}
			else
			{
				getDeleteResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSIONDELETE_PROC,
						postParametersMap, null, requestInstance);
			}
			JSONObject getDeleteResponseJson = CommonUtilities.getStringAsJSONObject(getDeleteResponse);
			if (getDeleteResponseJson == null || !getDeleteResponseJson.has(FabricConstants.OPSTATUS)
					|| getDeleteResponseJson.getInt(FabricConstants.OPSTATUS) != 0) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20528);
			}
		}
	}

	public static void assignPermissionsAndActionsToRole(DataControllerRequest requestInstance, String userID,
			String roleID, List<String> permissionsList, Map<String, ArrayList<Action>> compositeActionMapping)
			throws ApplicationException {

        if (permissionsList != null && !permissionsList.isEmpty()) {
            String operationResponse;
            JSONObject operationResponseJSON;
            ArrayList<Action> currentChildActions;
            Map<String, String> inputMap = new HashMap<>();
            Map<String, String> roleActionMap = new HashMap<>();
            inputMap.clear();
            inputMap.put("Role_id", roleID);
            inputMap.put("createdby", userID);
            inputMap.put("modifiedby", userID);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            Map<String,Boolean> actionEnabledMap = new HashMap<String, Boolean>();
            for (String currPermissionId : permissionsList) {
                if (StringUtils.isBlank(currPermissionId)) {
                    continue;
                }
                // If the current permission is composite, then the corresponding child
                // permissions are to be added
                inputMap.remove("Permission_id");
                if (compositeActionMapping.containsKey(currPermissionId)) {
                    currentChildActions = compositeActionMapping.get(currPermissionId);
                    for (Action currAction : currentChildActions) {                    	
                        String filter = "CompositeAction_id eq '" + currAction.getId() + "'"+" and Role_id eq '" + roleID + "'";
                        roleActionMap.put(ODataQueryConstants.FILTER, filter);
                        String readEndpoint = Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_READ,
                        		roleActionMap, null, requestInstance);
                        JSONObject readEndpointResponse = CommonUtilities.getStringAsJSONObject(readEndpoint);
                        JSONObject currJson=null;
                        if (readEndpointResponse != null && readEndpointResponse.has(FabricConstants.OPSTATUS)
                                && readEndpointResponse.getInt(FabricConstants.OPSTATUS) == 0) {
                        	JSONArray roleActionJSONArray = readEndpointResponse.getJSONArray("rolecompositeaction");
                        	if(roleActionJSONArray.length()>0) {
                        		currJson = roleActionJSONArray.optJSONObject(0);
                			}
                        }
                        if (currAction.isEnabled() || (actionEnabledMap.containsKey(currAction.getId()) && actionEnabledMap.get(currAction.getId()))) {
                    		inputMap.put("isEnabled", "1");
                    	} 
                    	else {
                    		inputMap.put("isEnabled", "0");
                    	}
                    	inputMap.put("CompositeAction_id", currAction.getId());
                        if(currJson==null) {
                        	Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_CREATE, inputMap, null,
                                requestInstance);
                        }
                        else if((inputMap.get("isEnabled")=="1") && (actionEnabledMap.containsKey(currAction.getId())) &&
    							!(actionEnabledMap.get(currAction.getId()))) {
    						Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_UPDATE, inputMap, null,
    								requestInstance);
    					}
    					actionEnabledMap.put(currAction.getId(), inputMap.get("isEnabled")=="1"?true:false);
                    }
                }
                inputMap.remove("isEnabled");
                inputMap.remove("CompositeAction_id");

				inputMap.put("Permission_id", currPermissionId);
				operationResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSION_CREATE, inputMap, null,
						requestInstance);
				operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

				if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
						|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
					throw new ApplicationException(ErrorCodeEnum.ERR_20528);
				}
			}
		}
	}

	public static void assignPermissionsAndActionsToRole(DataControllerRequest requestInstance, String userID,
			String roleID, String legalEntityId, List<String> permissionsList, Map<String, ArrayList<Action>> compositeActionMapping,
			String requestId,Boolean isApprovalRequired) throws ApplicationException {

		if (permissionsList != null && !permissionsList.isEmpty()) {
			String operationResponse;
			JSONObject operationResponseJSON;
			ArrayList<Action> currentChildActions;
			Map<String, String> inputMap = new HashMap<>();
			Map<String, String> roleActionMap = new HashMap<>();
			inputMap.clear();
			inputMap.put("Role_id", roleID);
			inputMap.put("companyLegalUnit", legalEntityId);
			inputMap.put("createdby", userID);
			inputMap.put("modifiedby", userID);
			inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
			Map<String, Boolean> actionEnabledMap = new HashMap<String, Boolean>();
			for (String currPermissionId : permissionsList) {
				if (StringUtils.isBlank(currPermissionId)) {
					continue;
				}
				// If the current permission is composite, then the corresponding child
				// permissions are to be added
				inputMap.remove("Permission_id");
				if (compositeActionMapping.containsKey(currPermissionId)) {
					currentChildActions = compositeActionMapping.get(currPermissionId);
					for (Action currAction : currentChildActions) {
						String filter = "CompositeAction_id eq '" + currAction.getId() + "'" + " and Role_id eq '"
								+ roleID + "' and companyLegalUnit eq '" + legalEntityId + "'";
						roleActionMap.put(ODataQueryConstants.FILTER, filter);
						String readEndpoint = Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_READ,
								roleActionMap, null, requestInstance);
						JSONObject readEndpointResponse = CommonUtilities.getStringAsJSONObject(readEndpoint);
						JSONObject currJson = null;
						if (readEndpointResponse != null && readEndpointResponse.has(FabricConstants.OPSTATUS)
								&& readEndpointResponse.getInt(FabricConstants.OPSTATUS) == 0) {
							JSONArray roleActionJSONArray = readEndpointResponse.getJSONArray("rolecompositeaction");
							if (roleActionJSONArray.length() > 0) {
								currJson = roleActionJSONArray.optJSONObject(0);
							}
						}
						if (currAction.isEnabled() || (actionEnabledMap.containsKey(currAction.getId())
								&& actionEnabledMap.get(currAction.getId()))) {
							inputMap.put("isEnabled", "1");
						} else {
							inputMap.put("isEnabled", "0");
						}
						inputMap.put("CompositeAction_id", currAction.getId());
						if (currJson == null) {
							if (isApprovalRequired) {
								inputMap.put("aprRequestId", requestId);
								inputMap.put("crudAction", "INS");
								Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_APPROVAL_CREATE, inputMap,
										null, requestInstance);
							} else {
								Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_CREATE, inputMap, null,
										requestInstance);
							}
						} else if ((inputMap.get("isEnabled") == "1")
								&& (actionEnabledMap.containsKey(currAction.getId()))
								&& !(actionEnabledMap.get(currAction.getId()))) {
							if (isApprovalRequired) {
								inputMap.put("aprRequestId", requestId);
								inputMap.put("crudAction", "UPD");
								Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_APPROVAL_UPDATE, inputMap,
										null, requestInstance);
							} else {
								Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_UPDATE, inputMap, null,
										requestInstance);
							}
						}
						actionEnabledMap.put(currAction.getId(), inputMap.get("isEnabled") == "1" ? true : false);
					}
				}
				inputMap.remove("isEnabled");
				inputMap.remove("CompositeAction_id");

				inputMap.put("Permission_id", currPermissionId);
				if (isApprovalRequired) {
					inputMap.put("aprRequestId", requestId);
					inputMap.put("crudAction", "INS");
					operationResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSION_APPROVAL_CREATE, inputMap,
							null, requestInstance);
				} else {
					operationResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSION_CREATE, inputMap, null,
							requestInstance);
				}
				operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_20528);
                }
            }
        }
    }

    public static void assignPermissionToRole(DataControllerRequest requestInstance, String userID, String roleID,
            List<String> permissionsList, Map<String, ArrayList<Permission>> compositePermissionMapping)
            throws ApplicationException {

        if (permissionsList != null && !permissionsList.isEmpty()) {
            String operationResponse;
            JSONObject operationResponseJSON;
            ArrayList<Permission> currentChildPermissions;
            Map<String, String> inputMap = new HashMap<>();
            inputMap.clear();
            inputMap.put("Role_id", roleID);
            inputMap.put("createdby", userID);
            inputMap.put("modifiedby", userID);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            for (String currPermissionId : permissionsList) {
                if (StringUtils.isBlank(currPermissionId)) {
                    continue;
                }
                // If the current permission is composite, then the corresponding child
                // permissions are to be added
                inputMap.remove("Permission_id");
                if (compositePermissionMapping.containsKey(currPermissionId)) {
                    currentChildPermissions = compositePermissionMapping.get(currPermissionId);
                    for (Permission currPermission : currentChildPermissions) {
                        inputMap.put("CompositePermission_id", currPermission.getId());
                        if (currPermission.isEnabled()) {
                            inputMap.put("isEnabled", "1");
                        } else {
                            inputMap.put("isEnabled", "0");
                        }
                        Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEPERMISSION_CREATE, inputMap, null,
                                requestInstance);
                    }
                }
                inputMap.remove("isEnabled");
                inputMap.remove("CompositePermission_id");

                inputMap.put("Permission_id", currPermissionId);
                operationResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSION_CREATE, inputMap, null,
                        requestInstance);
                operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_20528);
                }
            }
        }
    }

    public static void removeRoleFromUsers(DataControllerRequest requestInstance, String roleID, List<String> usersList)
            throws ApplicationException {

        if (usersList != null && !usersList.isEmpty()) {
            String operationResponse;
            JSONObject operationResponseJSON;
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("Role_id", roleID);
            for (String currUserId : usersList) {
                if (StringUtils.isBlank(currUserId)) {
                    continue;
                }
                inputMap.put("User_id", currUserId);
                operationResponse = Executor.invokeService(ServiceURLEnum.USERROLE_DELETE, inputMap, null,
                        requestInstance);
                operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_20527);
                }
            }
        }
    }

	public static void removeRoleFromUsers(DataControllerRequest requestInstance, String roleID, String legalEntityId, List<String> usersList,
			String requestId,Boolean isApprovalRequired) throws ApplicationException {

		if (usersList != null && !usersList.isEmpty()) {
			String operationResponse;
			JSONObject operationResponseJSON;
			Map<String, String> inputMap = new HashMap<>();
			inputMap.put("Role_id", roleID);
			inputMap.put("companyLegalUnit", legalEntityId);
			for (String currUserId : usersList) {
				if (StringUtils.isBlank(currUserId)) {
					continue;
				}
				inputMap.put("User_id", currUserId);
				if (isApprovalRequired) {
					inputMap.put("aprRequestId", requestId);
					inputMap.put("crudAction", "DEL");
					operationResponse = Executor.invokeService(ServiceURLEnum.USERROLE_APPROVE_UPDATE,
							inputMap, null, requestInstance);
				} else {
					operationResponse = Executor.invokeService(ServiceURLEnum.USERROLE_DELETE, inputMap, null,
							requestInstance);
				}

				operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
				if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
						|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
					throw new ApplicationException(ErrorCodeEnum.ERR_20527);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void assignRoleToUsers(DataControllerRequest requestInstance, String loggedInUserID, String roleID, String legalEntityID,
			List<String> usersList,String requestId, Boolean isApprovalRequired) throws ApplicationException {

		if (usersList != null && !usersList.isEmpty()) {
			// Fetch roles for the users to whom new role is assigned
			Map<String, String> userRoleMap = RoleHandler.getUsersRoleMap(requestInstance, usersList, legalEntityID);
			Map<String, String> inputMap = new HashMap<>();
			String operationResponse;
			JSONObject operationResponseJSON;
			for (Map.Entry<String, String> userRoleMapEntrySet : userRoleMap.entrySet()) {
				inputMap.clear();
				JSONObject currJSONObject = StringUtils.isNotBlank(userRoleMapEntrySet.getValue())
						? new JSONObject(userRoleMapEntrySet.getValue())
						: null;
				String user_id = userRoleMapEntrySet.getKey();
				String role_id = null != currJSONObject ? currJSONObject.optString(ApprovalConstants.ROLE_ID) : "";
				String legalEntityId = null != currJSONObject ? currJSONObject.optString("companyLegalUnit") : "";
				if (isApprovalRequired) {
					// Create entries in the approval table for the user with previous roles to whom
					// new role is assigned.
					currJSONObject.put(ApprovalConstants.CREATED_TS, CommonUtilities.getISOFormattedLocalTimestamp());
					currJSONObject.put(ApprovalConstants.LAST_MODIFIED_TS,
							CommonUtilities.getISOFormattedLocalTimestamp());
					currJSONObject.put(ApprovalConstants.SYNCTIMESTAMP_TS,
							CommonUtilities.getISOFormattedLocalTimestamp());
					try {
						HashMap<String, String> inputParams = new ObjectMapper().readValue(currJSONObject.toString(),
								HashMap.class);
						inputParams.put("aprRequestId", requestId);
						inputParams.put("crudAction", "DEL");
						String res = Executor.invokeService(ServiceURLEnum.USERROLE_APPROVE_CREATE, inputParams, null,
								requestInstance);
						alert.prepareError(res).log();
					} catch (Exception e) {
						alert.prepareError("Error occurred: ", e).log();					}

				} else {
					// Delete the current Role of the users to whom the new Role is to be assigned
					inputMap.put(ApprovalConstants.USERID, user_id);
					inputMap.put(ApprovalConstants.ROLE_ID, role_id);
					inputMap.put("companyLegalUnit", legalEntityId);
					Executor.invokeService(ServiceURLEnum.USERROLE_DELETE, inputMap, null, requestInstance);
				}
			}

			inputMap.clear();
			inputMap.put(ApprovalConstants.ROLE_ID, roleID);
			inputMap.put("companyLegalUnit", legalEntityID);
			inputMap.put(ApprovalConstants.CREATEDBY, loggedInUserID);
			inputMap.put(ApprovalConstants.MODIFIEDBY, loggedInUserID);
			inputMap.put(ApprovalConstants.CREATED_TS, CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put(ApprovalConstants.LAST_MODIFIED_TS, CommonUtilities.getISOFormattedLocalTimestamp());
			inputMap.put(ApprovalConstants.SYNCTIMESTAMP_TS, CommonUtilities.getISOFormattedLocalTimestamp());
			if (roleID.equalsIgnoreCase(ApprovalConstants.ROLE_ID_SUPER_ADMIN))

			{
				inputMap.put(ApprovalConstants.HAS_SUPER_ADMIN_PRIVILAGES, "1");
			} else {
				inputMap.put(ApprovalConstants.HAS_SUPER_ADMIN_PRIVILAGES, "0");
			}
			for (String currUserId : usersList) {
				if (StringUtils.isBlank(currUserId)) {
					continue;
				}
				inputMap.put(ApprovalConstants.USERID, currUserId);
				// Assigning user to new role, if approval is required then create in _approval
				// table otherwise in original table.
				if (isApprovalRequired) {
					inputMap.put("aprRequestId", requestId);
					inputMap.put("crudAction", "INS");
					operationResponse = Executor.invokeService(ServiceURLEnum.USERROLE_APPROVE_CREATE, inputMap, null,
							requestInstance);
				} else {
					operationResponse = Executor.invokeService(ServiceURLEnum.USERROLE_CREATE, inputMap, null,
							requestInstance);
				}
				operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
				if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
						|| operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
					throw new ApplicationException(ErrorCodeEnum.ERR_20527);
				}
			}
		}
	}

	public static void assignRoleToUsers(DataControllerRequest requestInstance, String loggedInUserID, String roleID,
			List<String> usersList) throws ApplicationException {

        if (usersList != null && !usersList.isEmpty()) {
            Map<String, String> userRoleMap = InternalUserHandler.getUsersRoleMap(requestInstance, usersList);
            Map<String, String> inputMap = new HashMap<>();
            String operationResponse;
            JSONObject operationResponseJSON;
            for (Map.Entry<String, String> userRoleMapEntrySet : userRoleMap.entrySet()) {
                // Delete the current Role of the users to whom the new Role is to be assigned
                inputMap.clear();
                inputMap.put("User_id", userRoleMapEntrySet.getKey());
                inputMap.put("Role_id", userRoleMapEntrySet.getValue());
                Executor.invokeService(ServiceURLEnum.USERROLE_DELETE, inputMap, null, requestInstance);
            }

            inputMap.clear();
            inputMap.put("Role_id", roleID);
            inputMap.put("createdby", loggedInUserID);
            inputMap.put("modifiedby", loggedInUserID);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());
            if (roleID.equalsIgnoreCase("RID_SUPERADMIN")) {
                inputMap.put("hasSuperAdminPrivilages", "1");
            } else {
                inputMap.put("hasSuperAdminPrivilages", "0");
            }
            for (String currUserId : usersList) {
                if (StringUtils.isBlank(currUserId)) {
                    continue;
                }
                inputMap.put("User_id", currUserId);
                operationResponse = Executor.invokeService(ServiceURLEnum.USERROLE_CREATE, inputMap, null,
                        requestInstance);
                operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_20527);
                }
            }
        }
    }

	public static void editMappingForUserroleToCustomerRole(String inputCurrentInternalRole, String legalEntityId,
			JSONArray addedServiceDefinitions, JSONArray removedServiceDefinitions,
			DataControllerRequest requestInstance, String requestId, Boolean isApprovalRequired) throws ApplicationException {
		// Delete mapping
		
			for (Object removedServiceDefinition : removedServiceDefinitions) {
				String serviceDefinitionId = (String) removedServiceDefinition;
				Map<String, String> postParametersMap = new HashMap<String, String>();
				postParametersMap.put("UserRole_id", inputCurrentInternalRole);
				postParametersMap.put("companyLegalUnit", legalEntityId);
				postParametersMap.put("servicedefinitionId", serviceDefinitionId);
				String deleteEndpointResponse = null;
				if (isApprovalRequired) {
					postParametersMap.put("aprRequestId", requestId);
					postParametersMap.put("crudAction", "DEL");
					deleteEndpointResponse = Executor.invokeService(ServiceURLEnum.USERROLESERVICEDEFINITION_APPROVE_UPDATE,
							postParametersMap, null, requestInstance);
				} else {
				deleteEndpointResponse = Executor.invokeService(ServiceURLEnum.USERROLESERVICEDEFINITION_DELETE,
						postParametersMap, null, requestInstance);
				}

				CommonUtilities.getStringAsJSONObject(deleteEndpointResponse);
				// Ignore any failure in deletion
			}
		

		// Create mapping
		for (Object serviceDefinition : addedServiceDefinitions) {
			String serviceDefinitionId = (String) serviceDefinition;
			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.put("UserRole_id", inputCurrentInternalRole);
			postParametersMap.put("companyLegalUnit", legalEntityId);
			postParametersMap.put("servicedefinitionId", serviceDefinitionId);
			String createEndpointResponse = null;

			// If approval is required move data to _approval table otherwise to original
			// table
			if (isApprovalRequired) {
				postParametersMap.put("aprRequestId", requestId);
				postParametersMap.put("crudAction", "INS");
				createEndpointResponse = Executor.invokeService(ServiceURLEnum.USERROLESERVICEDEFINITION_APPROVE_CREATE,
						postParametersMap, null, requestInstance);
			} else {
				createEndpointResponse = Executor.invokeService(ServiceURLEnum.USERROLESERVICEDEFINITION_CREATE,
						postParametersMap, null, requestInstance);
			}

			JSONObject createResponse = CommonUtilities.getStringAsJSONObject(createEndpointResponse);
			if (createResponse == null || !createResponse.has(FabricConstants.OPSTATUS)
					|| createResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				throw new ApplicationException(ErrorCodeEnum.ERR_21597);
			}
		}

	}

	public static void editMappingForUserroleToCustomerRole(String inputCurrentInternalRole,
			JSONArray addedServiceDefinitions, JSONArray removedServiceDefinitions,
			DataControllerRequest requestInstance) throws ApplicationException {
		// Delete mapping
		for (Object removedServiceDefinition : removedServiceDefinitions) {
			String serviceDefinitionId = (String) removedServiceDefinition;
			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.put("UserRole_id", inputCurrentInternalRole);
			postParametersMap.put("servicedefinitionId", serviceDefinitionId);
			String deleteEndpointResponse = Executor.invokeService(ServiceURLEnum.USERROLESERVICEDEFINITION_DELETE,
					postParametersMap, null, requestInstance);
			CommonUtilities.getStringAsJSONObject(deleteEndpointResponse);
			// Ignore any failure in deletion
		}

		// Create mapping
		for (Object serviceDefinition : addedServiceDefinitions) {
			String serviceDefinitionId = (String) serviceDefinition;
			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.put("UserRole_id", inputCurrentInternalRole);
			postParametersMap.put("servicedefinitionId", serviceDefinitionId);
			String createEndpointResponse = null;
			createEndpointResponse = Executor.invokeService(ServiceURLEnum.USERROLESERVICEDEFINITION_CREATE,
					postParametersMap, null, requestInstance);

			JSONObject createResponse = CommonUtilities.getStringAsJSONObject(createEndpointResponse);
			if (createResponse == null || !createResponse.has(FabricConstants.OPSTATUS)
					|| createResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				throw new ApplicationException(ErrorCodeEnum.ERR_21597);
			}
		}

	}

	public static String getRoleIdFromName(String name, DataControllerRequest requestInstance) {
		String id = null;
		Map<String, String> postParametersMap = new HashMap<String, String>();
		String filter = "Name eq '" + name + "'";
		postParametersMap.put(ODataQueryConstants.FILTER, filter);
		String readEndpoint = Executor.invokeService(ServiceURLEnum.ROLE_READ, postParametersMap, null,
				requestInstance);
		JSONObject readEndpointResponse = CommonUtilities.getStringAsJSONObject(readEndpoint);
		if (readEndpointResponse != null && readEndpointResponse.has(FabricConstants.OPSTATUS)
				&& readEndpointResponse.getInt(FabricConstants.OPSTATUS) == 0) {
			JSONArray roleJSONArray = readEndpointResponse.getJSONArray("role");
			if (roleJSONArray.length() > 0) {
				JSONObject currJson = roleJSONArray.optJSONObject(0);
				id = currJson.optString("id");
			}
		}
		return id;
	}

	public static Map<String, String> getUsersRoleMap(DataControllerRequest requestInstance, List<String> usersList, String legalEntityId)
			throws ApplicationException {

		Map<String, String> userRoleMap = new HashMap<>();
		if (usersList == null || usersList.isEmpty()) {
			return userRoleMap;
		}

		// Construct Filter Query
		StringBuffer filterQueryBuffer = new StringBuffer();
		for (String currUserId : usersList) {
			filterQueryBuffer.append("User_id eq '" + currUserId + "' or ");
		}
		filterQueryBuffer.trimToSize();
		String filterQuery = StringUtils.trim(
				CommonUtilities.replaceLastOccuranceOfString(filterQueryBuffer.toString(), "or", StringUtils.EMPTY));
		filterQuery = filterQuery + " and companyLegalUnit eq '" + legalEntityId + "'";
		// Prepare Query Map
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put(ODataQueryConstants.FILTER, filterQuery);

		// Read User Role
		String serviceResponse = Executor.invokeService(ServiceURLEnum.USERROLE_READ, queryMap, null, requestInstance);
		JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
				|| serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !serviceResponseJSON.has("userrole")) {
			alert.prepareError("Failed to read userrole records.Response:" + serviceResponse).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20548);
		}

		// Construct Result Map
		JSONObject currJSONObject;
		JSONArray userRoleArray = serviceResponseJSON.getJSONArray("userrole");
		for (Object currObject : userRoleArray) {
			if (currObject instanceof JSONObject) {
				currJSONObject = (JSONObject) currObject;
				if (currJSONObject.has("User_id") && currJSONObject.has("Role_id")) {
					userRoleMap.put(currJSONObject.optString("User_id"), currJSONObject.toString());
				}
			}
		}

		// Return Result Map
		return userRoleMap;
	}

}