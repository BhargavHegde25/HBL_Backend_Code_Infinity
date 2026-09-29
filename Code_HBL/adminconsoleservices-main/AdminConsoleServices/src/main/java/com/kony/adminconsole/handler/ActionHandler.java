package com.kony.adminconsole.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.dto.Action;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Handler to perform operations related to Action
 * 
 * @author Rishi Gupta
 * 
 */
public class ActionHandler {

    private ActionHandler() {
        // Private Constructor
    }

    public static HashMap<String, ArrayList<Action>> getChildActions(Set<String> permissionsList,
            DataControllerRequest requestInstance) throws ApplicationException {

        Map<String, String> inputMap = new HashMap<>();
        StringBuilder filterQueryBuffer = new StringBuilder();
        HashMap<String, ArrayList<Action>> actionsInfo = new HashMap<>();
        inputMap.put(ODataQueryConstants.SELECT, "id,Name,Permission_id,isEnabled");
        for (String currPermission : permissionsList) {
            filterQueryBuffer.append("Permission_id eq '" + currPermission + "' or ");
        }
        if (filterQueryBuffer.toString().trim().endsWith("or")) {
            filterQueryBuffer.delete(filterQueryBuffer.lastIndexOf("or"), filterQueryBuffer.length());
            filterQueryBuffer.trimToSize();
        }
        inputMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString().trim());
        String operationResponse = Executor.invokeService(ServiceURLEnum.COMPOSITEACTION_READ, inputMap, null,
                requestInstance);
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && operationResponseJSON.has("compositeaction")) {

            JSONArray actionsRecords = operationResponseJSON.getJSONArray("compositeaction");

            String childActionName, childActionId, parentPermissionId;
            JSONObject currActionJSONObject;
            ArrayList<Action> childActionList;

            for (int indexVar = 0; indexVar < actionsRecords.length(); indexVar++) {

                if (actionsRecords.get(indexVar) instanceof JSONObject) {

                    currActionJSONObject = actionsRecords.getJSONObject(indexVar);
                    parentPermissionId = currActionJSONObject.optString("Permission_id");
                    if (!actionsInfo.containsKey(parentPermissionId)) {
                        childActionList = new ArrayList<Action>();
                        actionsInfo.put(parentPermissionId, childActionList);
                    }
                    childActionList = actionsInfo.get(parentPermissionId);

                    Action currChildActionObject = new Action();
                    childActionName = currActionJSONObject.optString("Name");
                    childActionId = currActionJSONObject.optString("id");
                    if (StringUtils.equalsIgnoreCase(currActionJSONObject.optString("isEnabled"), "1")
                            || StringUtils.equalsIgnoreCase(currActionJSONObject.optString("isEnabled"),
                                    String.valueOf(true))) {
                        currChildActionObject.setIsEnabled(true);
                    } else {
                        currChildActionObject.setIsEnabled(false);
                    }
                    currChildActionObject.setParentPermissionId(parentPermissionId);
                    currChildActionObject.setId(childActionId);
                    currChildActionObject.setName(childActionName);
                    childActionList.add(currChildActionObject);
                }
            }
            return actionsInfo;
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_20744);
        }
    }

    public static void removeActionFromRoles(DataControllerRequest requestInstance, String permissionId,
            List<String> rolesList, Map<String, ArrayList<Action>> compositeActionMapping)
            throws ApplicationException {

        Map<String, String> inputMap = new HashMap<>();

        if (rolesList != null && !rolesList.isEmpty()) {
            ArrayList<Action> currentChildActions = new ArrayList<Action>();
            String operationResponse;
            JSONObject operationResponseJSON;
            if (compositeActionMapping.containsKey(permissionId)) {
                currentChildActions = compositeActionMapping.get(permissionId);
            }

            for (String currRoleId : rolesList) {
                if (StringUtils.isBlank(currRoleId)) {
                    continue;
                }
                inputMap.put("Role_id", currRoleId);

                // If the current permission is composite, then the corresponding child
                // permissions are to be removed
                inputMap.remove("Permission_id");
                for (Action currAction : currentChildActions) {
                    inputMap.put("CompositeAction_id", currAction.getId());
                    Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEPERMISSION_DELETE, inputMap, null,
                            requestInstance);
                }
                Map<String, String> postParametersMap = new HashMap<>();
            	postParametersMap.put("_roleId", currRoleId);
    	    	postParametersMap.put("_PermissionIds", permissionId);
    	    	String getDeleteResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSIONDELETE_PROC, postParametersMap, null, requestInstance);
    			JSONObject getDeleteResponseJson = CommonUtilities.getStringAsJSONObject(getDeleteResponse);
    			if (getDeleteResponseJson == null || !getDeleteResponseJson.has(FabricConstants.OPSTATUS)
    					|| getDeleteResponseJson.getInt(FabricConstants.OPSTATUS) != 0) {
    				throw new ApplicationException(ErrorCodeEnum.ERR_20527);
                    }
            }
        }
    }

    public static void assignActionToRoles(DataControllerRequest requestInstance, String loggedInUserId,
            String permissionId, List<String> rolesList,
            HashMap<String, ArrayList<Action>> compositeActionMapping) throws ApplicationException {

        Map<String, String> inputMap = new HashMap<>();
        Map<String, String> roleActionMap = new HashMap<>();
        
        if (rolesList != null && !rolesList.isEmpty()) {
            String operationResponse;
            JSONObject operationResponseJSON;
            ArrayList<Action> currentChildActions = new ArrayList<Action>();
            inputMap.put("Permission_id", permissionId);
            inputMap.put("createdby", loggedInUserId);
            inputMap.put("modifiedby", loggedInUserId);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());

            if (compositeActionMapping.containsKey(permissionId)) {
                currentChildActions = compositeActionMapping.get(permissionId);
            }

            for (String currRoleId : rolesList) {
                if (StringUtils.isBlank(currRoleId)) {
                    continue;
                }
                inputMap.put("Role_id", currRoleId);

                // If the current permission is composite, then the corresponding child
                // permissions are to be added
                if (!currentChildActions.isEmpty()) {
                    inputMap.remove("Permission_id");
                    for (Action currAction : currentChildActions) {
                    	String filter = "CompositeAction_id eq '" + currAction.getId() + "'"+" and Role_id eq '" + currRoleId + "'";
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
                        inputMap.put("CompositeAction_id", currAction.getId());
                        if (currAction.isEnabled()) {
                            inputMap.put("isEnabled", "1");
                        } else {
                            inputMap.put("isEnabled", "0");
                        }
                        if(currJson==null) {
                        operationResponse = Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_CREATE,
                                inputMap, null, requestInstance);
                        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                        if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                                || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                            throw new ApplicationException(ErrorCodeEnum.ERR_20525);
                        }
                        }
                    }
                    inputMap.remove("isEnabled");
                    inputMap.remove("CompositeAction_id");
                    inputMap.put("Permission_id", permissionId);
                }

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

	public static void removeActionFromUsers(DataControllerRequest requestInstance, String permissionId,
			List<String> usersList) throws ApplicationException {

		if (usersList != null && !usersList.isEmpty()) {
			for (String currUserID : usersList) {
				if (StringUtils.isBlank(currUserID)) {
					continue;
				}
				Map<String, String> postParametersMap = new HashMap<>();
				postParametersMap.put("_userId", currUserID);
				postParametersMap.put("_PermissionIds", permissionId);
				String getDeleteResponse = Executor.invokeService(ServiceURLEnum.USERPERMISSIONDELETE_PROC,
						postParametersMap, null, requestInstance);
				JSONObject getDeleteResponseJson = CommonUtilities.getStringAsJSONObject(getDeleteResponse);
				if (getDeleteResponseJson == null || !getDeleteResponseJson.has(FabricConstants.OPSTATUS)
						|| getDeleteResponseJson.getInt(FabricConstants.OPSTATUS) != 0) {
					throw new ApplicationException(ErrorCodeEnum.ERR_20528);
				}
			}
		}
	}

    public static void assignActionToUsers(DataControllerRequest requestInstance, String loggedInUserId,
            String permissionID, List<String> usersList,
            HashMap<String, ArrayList<Action>> compositeActionMapping) throws ApplicationException {

        if (usersList != null && !usersList.isEmpty()) {
            String operationResponse;
            JSONObject operationResponseJSON;
            Map<String, String> inputMap = new HashMap<>();
            Map<String, String> userActionMap = new HashMap<>();
            ArrayList<Action> currentChildActions = new ArrayList<Action>();
            inputMap.put("createdby", loggedInUserId);
            inputMap.put("modifiedby", loggedInUserId);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());

            if (compositeActionMapping.containsKey(permissionID)) {
                currentChildActions = compositeActionMapping.get(permissionID);
            }

            for (String currUserId : usersList) {
                if (StringUtils.isBlank(currUserId)) {
                    continue;
                }
                inputMap.put("User_id", currUserId);

                // If the current permission is composite, then the corresponding child
                // permissions are to be added
                inputMap.remove("Permission_id");
                for (Action currAction : currentChildActions) {
                	String filter = "CompositeAction_id eq '" + currAction.getId() + "'"+" and User_id eq '" + currUserId + "'";
                	userActionMap.put(ODataQueryConstants.FILTER, filter);
                    String readEndpoint = Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_READ,
                    		userActionMap, null, requestInstance);
                    JSONObject readEndpointResponse = CommonUtilities.getStringAsJSONObject(readEndpoint);
                    JSONObject currJson=null;
                    if (readEndpointResponse != null && readEndpointResponse.has(FabricConstants.OPSTATUS)
                            && readEndpointResponse.getInt(FabricConstants.OPSTATUS) == 0) {
                    	JSONArray userActionJSONArray = readEndpointResponse.getJSONArray("usercompositeaction");
                    	if(userActionJSONArray.length()>0) {
                    		currJson = userActionJSONArray.optJSONObject(0);
            			}
                    }
                    inputMap.put("CompositeAction_id", currAction.getId());
                    if (currAction.isEnabled()) {
                        inputMap.put("isEnabled", "1");
                    } else {
                        inputMap.put("isEnabled", "0");
                    }
                    if(currJson==null) {
                    operationResponse = Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_CREATE, inputMap,
                            null, requestInstance);
                    operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                    if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                            || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_20527);
                    }
                    }
                }
                inputMap.remove("isEnabled");
                inputMap.remove("CompositeAction_id");

                inputMap.put("Permission_id", permissionID);
                operationResponse = Executor.invokeService(ServiceURLEnum.USERPERMISSION_CREATE, inputMap, null,
                        requestInstance);
                operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_20527);
                }
            }
        }
    }

    public static JSONObject getAggregateCompositeActions(String userID, String roleID, String permissionID,
            DataControllerRequest requestInstance, String selectQuery, String isEnabled) {

        JSONObject readCompositePermission = getCompositeActions(selectQuery, permissionID, isEnabled,
                requestInstance);
        if ((!readCompositePermission.has(FabricConstants.OPSTATUS))
                || readCompositePermission.getInt(FabricConstants.OPSTATUS) != 0
                || (!readCompositePermission.has("composite_actions_view"))) {
            JSONObject res = new JSONObject();
            res.put("ErrorEnum", ErrorCodeEnum.ERR_20744);
            res.put("response", String.valueOf(readCompositePermission));
            return res;
        }
        JSONArray finalCompositeActions = readCompositePermission.getJSONArray("composite_actions_view");

        if (StringUtils.isNotBlank(roleID)) {
            JSONObject readRoleCompositeAction = getRoleCompositeActions(roleID, isEnabled, requestInstance);
            if (readRoleCompositeAction == null || !readRoleCompositeAction.has(FabricConstants.OPSTATUS)
					|| readRoleCompositeAction.getInt(FabricConstants.OPSTATUS) != 0) {
                JSONObject res = new JSONObject();
                res.put("ErrorEnum", ErrorCodeEnum.ERR_20745);
                res.put("response", String.valueOf(readRoleCompositeAction));
                return res;
            }
            JSONArray roleCompositeActions = readRoleCompositeAction.getJSONArray("records");

            // Override role based composite permissions on final composite permissions
            Set<String> roleCompActionSet = new HashSet<String>();
            for (Object actionObject : roleCompositeActions) {
                JSONObject action = (JSONObject) actionObject;
                for (Object finalActionObject : finalCompositeActions) {
                    JSONObject finalAction = (JSONObject) finalActionObject;
                    if (finalAction.getString("id").equals(action.getString("CompositeAction_id"))) {
                        finalAction.put("isEnabled", action.getString("isEnabled"));
                        roleCompActionSet.add(finalAction.getString("id"));
                    }
                }
            }
            
            //Making isEnabled to false for the compositaction which is not available for role 
            for (Object finalActionObject : finalCompositeActions) {
                JSONObject finalAction = (JSONObject) finalActionObject;
                if (!roleCompActionSet.contains(finalAction.getString("id"))) {
                    finalAction.put("isEnabled", "false");
                }
            }
            
        }
        if (StringUtils.isNotBlank(userID)) {
            JSONObject readUserCompositeAction = getUserCompositeActions(userID, isEnabled, requestInstance);
            if ((!readUserCompositeAction.has(FabricConstants.OPSTATUS))
                    || readUserCompositeAction.getInt(FabricConstants.OPSTATUS) != 0
                    || (!readUserCompositeAction.has("usercompositeaction"))) {

                JSONObject res = new JSONObject();
                res.put("ErrorEnum", ErrorCodeEnum.ERR_20746);
                res.put("response", String.valueOf(readUserCompositeAction));
                return res;
            }
            JSONArray userCompositeActions = readUserCompositeAction.getJSONArray("usercompositeaction");

            // Override user based composite permissions on final composite permissions
            for (Object actionObject : userCompositeActions) {
                JSONObject action = (JSONObject) actionObject;
                for (Object finalActionObject : finalCompositeActions) {
                    JSONObject finalAction = (JSONObject) finalActionObject;
                    if (finalAction.getString("id").equals(action.getString("CompositeAction_id"))) {
                        finalAction.put("isEnabled", action.getString("isEnabled"));
                    }
                }
            }
        }
        JSONObject aggregateCompositeActions = new JSONObject();
        aggregateCompositeActions.put("CompositeActions", finalCompositeActions);
        return aggregateCompositeActions;
    }

    public static JSONObject getCompositeActions(String selectQuery, String permissionID, String isEnabled,
            DataControllerRequest requestInstance) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.SELECT, selectQuery);
        String filter = "Permission_id eq '" + permissionID + "'";
        if (isEnabled!= null) 
        	filter += " and isEnabled eq '" + isEnabled + "'";
        postParametersMap.put(ODataQueryConstants.FILTER, filter);
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.COMPOSITEACTION_VIEW_READ,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
    }

    public static JSONObject getRoleCompositeActions(String roleID, String isEnabled,
            DataControllerRequest requestInstance) {
    	Map<String, String> postParametersMap = new HashMap<String, String>();
    	postParametersMap.put("_roleIds", roleID);
    	if(isEnabled==null)
    		isEnabled="NULL";
    	postParametersMap.put("_isEnabled", isEnabled);
		String getRolesResponse = Executor.invokeService(ServiceURLEnum.ROLESCOMPOSITEACTIONS_PROC, postParametersMap, null, requestInstance);
		JSONObject getRolesResponseJson = CommonUtilities.getStringAsJSONObject(getRolesResponse);
		return getRolesResponseJson;
    }

    public static JSONObject getUserCompositeActions(String userID, String isEnabled,
            DataControllerRequest requestInstance) {

        Map<String, String> postParametersMap = new HashMap<String, String>();
        String filter = "User_id eq '" + userID + "'";
        if (isEnabled != null) {
            filter += " and isEnabled eq '" + isEnabled + "'";
        }
        postParametersMap.put(ODataQueryConstants.FILTER, filter);
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_READ,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

    }
	public static String getAdminRoleId(String role,DataControllerRequest requestInstance)
	{
	    
        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put(ODataQueryConstants.FILTER, "Name eq '" + role + "'");
        inputMap.put(ODataQueryConstants.SELECT, "id");

        String readRoleResponse = Executor.invokeService(ServiceURLEnum.ROLE_READ, inputMap, null,
                requestInstance);

        String roleId = null;

        JSONObject readRoleResponseJSON = CommonUtilities.getStringAsJSONObject(readRoleResponse);
        if (readRoleResponseJSON != null && readRoleResponseJSON.has(FabricConstants.OPSTATUS)
                && readRoleResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readRoleResponseJSON.has("role")) {
            JSONArray readRoleJSONArray = readRoleResponseJSON.optJSONArray("role");
            if (!(readRoleJSONArray == null || readRoleJSONArray.length() < 1)) {
                JSONObject roleObj = readRoleJSONArray.getJSONObject(0);
                roleId = roleObj.getString("id");
            }
        }
        return roleId;
	}

}
