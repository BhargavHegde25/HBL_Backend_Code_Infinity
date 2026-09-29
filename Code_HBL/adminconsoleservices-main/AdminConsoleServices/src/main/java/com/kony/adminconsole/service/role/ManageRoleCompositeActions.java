/**
 * 
 */
package com.kony.adminconsole.service.role;

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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author Aditya Mankal
 * 
 * 
 *         Service to manage the added/removed Composite permissions to a Role
 *
 */
public class ManageRoleCompositeActions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {

            Result processedResult = new Result();
            ErrorCodeEnum errorInformation = null;

            String roleId = requestInstance.getParameter("roleId");
            String addedCompositeActions = requestInstance.getParameter("addedCompositeActions");
            String removedCompositeActions = requestInstance.getParameter("removedCompositeActions");
            String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);

            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);

            if (StringUtils.isEmpty(roleId)) {
                errorInformation = ErrorCodeEnum.ERR_20521;
            } else {
                JSONArray listOfAddedCompositeActions = CommonUtilities
                        .getStringAsJSONArray(addedCompositeActions);
                JSONArray listOfRemovedCompositeActions = CommonUtilities
                        .getStringAsJSONArray(removedCompositeActions);

                Record initializeRolePermissionMappingOperationRecord = initRoleCompositeActionMapping(roleId,
                		listOfAddedCompositeActions, listOfRemovedCompositeActions, userDetailsBeanInstance,
                        authToken, requestInstance);
                processedResult.addRecord(initializeRolePermissionMappingOperationRecord);

                Record manageAddedCompositeActionsOperationRecord = processCompositeActions(roleId,
                		listOfAddedCompositeActions, userDetailsBeanInstance, true, authToken, requestInstance);
                if (manageAddedCompositeActionsOperationRecord != null) {
                    if (manageAddedCompositeActionsOperationRecord.getParamByName("status") != null
                            && manageAddedCompositeActionsOperationRecord.getParamByName("status").getValue()
                                    .equalsIgnoreCase("failure")) {
                        errorInformation = ErrorCodeEnum.ERR_20525;
                    }
                    processedResult.addRecord(manageAddedCompositeActionsOperationRecord);
                }

                Record manageRemovedCompositeActionsOperationRecord = processCompositeActions(roleId,
                		listOfRemovedCompositeActions, userDetailsBeanInstance, false, authToken, requestInstance);

                if (manageRemovedCompositeActionsOperationRecord != null) {
                    if (manageRemovedCompositeActionsOperationRecord.getParamByName("status") != null
                            && manageRemovedCompositeActionsOperationRecord.getParamByName("status").getValue()
                                    .equalsIgnoreCase("failure")) {
                        errorInformation = ErrorCodeEnum.ERR_20525;
                    }
                    processedResult.addRecord(manageRemovedCompositeActionsOperationRecord);
                }
            }

            if (errorInformation != null) {
                errorInformation.setErrorCode(processedResult);
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Role Composite Actions Update Failed");
                return processedResult;
            }
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                    ActivityStatusEnum.SUCCESSFUL, "Role Composite Actions Updated successfully");
            return processedResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ROLES, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Role Composite Actions Update Failed");
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

    private Record processCompositeActions(String roleId, JSONArray compositeActionsList,
            UserDetailsBean userDetailsBeanInstance, boolean isAddedActionsOperation, String authToken,
            DataControllerRequest requestInstance) {

        if (compositeActionsList == null || compositeActionsList.length() == 0) {
            return null;
        }

        Record addCompositeActionsResponse = new Record();
        Param operationStatus = new Param("status", "Successful", FabricConstants.STRING);
        addCompositeActionsResponse.addParam(operationStatus);

        String currActionId, currOperationResponse, isEnabledFlag;
        JSONObject currOperationResponseJSON;
        Param currOperationParam;

        if (isAddedActionsOperation) {
        	addCompositeActionsResponse.setId("addCompositeActions");
            isEnabledFlag = "1";
        } else {
        	addCompositeActionsResponse.setId("removedCompositeActions");
            isEnabledFlag = "0";
        }

        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put("Role_id", roleId);
        postParametersMap.put("isEnabled", isEnabledFlag);
        postParametersMap.put("modifiedby", userDetailsBeanInstance.getId());
        postParametersMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

        for (int indexVar = 0; indexVar < compositeActionsList.length(); indexVar++) {

            currActionId = compositeActionsList.optString(indexVar);
            postParametersMap.put("CompositeAction_id", currActionId);

            currOperationResponse = Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_UPDATE,
                    postParametersMap, null, requestInstance);
            currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
            if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
                    || currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                operationStatus.setValue("Failure");
            }
            currOperationParam = new Param("action: " + currActionId, currOperationResponse,
                    FabricConstants.STRING);
            addCompositeActionsResponse.addParam(currOperationParam);

        }
        return addCompositeActionsResponse;
    }

    private Record initRoleCompositeActionMapping(String roleId, JSONArray listOfAddedCompositeActions,
            JSONArray listOfRemovedCompositeActions, UserDetailsBean userDetailsBeanInstance, String authToken,
            DataControllerRequest requestInstance) {

        Record operationRecord = new Record();
        operationRecord.setId("initializeRoleCompositeActionMapping");
        Map<String, String> postParametersMap = new HashMap<String, String>();

        postParametersMap.put(ODataQueryConstants.SELECT, "CompositeAction_id");
        postParametersMap.put(ODataQueryConstants.FILTER, "Role_id eq '" + roleId + "'");
        String readRoleCompositeActionResponse = Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_READ,
                postParametersMap, null, requestInstance);

        Set<String> compositeActionSet = new HashSet<String>();
        String currActionId, currOperationResponse;

        if (listOfAddedCompositeActions != null && listOfAddedCompositeActions.length() > 0) {
            for (int indexVar = 0; indexVar < listOfAddedCompositeActions.length(); indexVar++) {
            	currActionId = listOfAddedCompositeActions.optString(indexVar);
            	compositeActionSet.add(currActionId);
            }
        }

        if (listOfRemovedCompositeActions != null && listOfRemovedCompositeActions.length() > 0) {
            for (int indexVar = 0; indexVar < listOfRemovedCompositeActions.length(); indexVar++) {
            	currActionId = listOfRemovedCompositeActions.optString(indexVar);
                compositeActionSet.add(currActionId);
            }
        }

        JSONObject readRoleCompositeActionResponseJSON = CommonUtilities
                .getStringAsJSONObject(readRoleCompositeActionResponse);
        if (readRoleCompositeActionResponseJSON != null
                && readRoleCompositeActionResponseJSON.has(FabricConstants.OPSTATUS)
                && readRoleCompositeActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && readRoleCompositeActionResponseJSON.has("rolecompositeaction")) {
            JSONArray actionsArray = readRoleCompositeActionResponseJSON
                    .getJSONArray("rolecompositeaction");
            if (actionsArray != null) {
                for (int indexVar = 0; indexVar < actionsArray.length(); indexVar++) {
                	compositeActionSet.remove(actionsArray.get(indexVar));
                }
            }
        }

        postParametersMap.put("Role_id", roleId);
        postParametersMap.put("isEnabled", "0");
        postParametersMap.put("createdby", userDetailsBeanInstance.getId());
        postParametersMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
        postParametersMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());
        postParametersMap.put("softdeleteflag", "0");

        Param currOperationParam = null;
        for (String currAction : compositeActionSet) {
            postParametersMap.put("CompositeAction_id", currAction);
            currOperationResponse = Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEACTION_CREATE,
                    postParametersMap, null, requestInstance);
            currOperationParam = new Param("insertAction: " + currAction, currOperationResponse,
                    FabricConstants.STRING);
            operationRecord.addParam(currOperationParam);
        }
        return operationRecord;
    }
}
