package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
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
import com.kony.adminconsole.dto.CustomerBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerHandler;
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
 * CustomerUpdateActions service will update mapping between customer and
 * actions
 * 
 * 
 */
public class CustomerUpdateActions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String INPUT_LIST_OF_UPDATED_ACTIONS = "updatedActions";
	private static final String INPUT_LIST_OF_REMOVED_ACTIONS = "removedActions";
	private static final String INPUT_CUSTOMER_TYPE = "Type_id";
	private static final String INPUT_USERNAME = "userName";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			Result processedResult = new Result();
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			String AuthToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			String username = requestInstance.getParameter(INPUT_USERNAME);
			String customerType = requestInstance.getParameter(INPUT_CUSTOMER_TYPE);

			String modifiedByName = userDetailsBeanInstance.getUserName();
			// Check the access control for this customer for current logged-in internal
			// user
			if (StringUtils.isBlank(username)) {
				ErrorCodeEnum.ERR_20533.setErrorCode(processedResult);
				return processedResult;
			}
			CustomerBean customerBean = CustomerHandler.getCustomerGeneralInformation(username, requestInstance);
			if (customerBean == null) {
				ErrorCodeEnum.ERR_21850.setErrorCode(processedResult);
				return processedResult;
			}

			String customerID = customerBean.getId();

			CustomerHandler.doesCurrentLoggedinUserHasAccessToCustomer(null, customerID, requestInstance,
					processedResult);
			if (processedResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
				return processedResult;
			}
			// End of access check

			JSONArray updatedActions = null, listOfRemovedActions = null;
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_LIST_OF_UPDATED_ACTIONS))) {
				updatedActions = new JSONArray(requestInstance.getParameter(INPUT_LIST_OF_UPDATED_ACTIONS));
			}
			if (requestInstance.getParameter(INPUT_LIST_OF_REMOVED_ACTIONS) != null) {
				listOfRemovedActions = new JSONArray(requestInstance.getParameter(INPUT_LIST_OF_REMOVED_ACTIONS));
			}
			// Remove customer actions
			if (listOfRemovedActions != null && listOfRemovedActions.length() >= 1) {
				deleteActions(AuthToken, modifiedByName, listOfRemovedActions, customerID, requestInstance);
			}
			// Update customer actions
			if (updatedActions != null && updatedActions.length() >= 1) {
				JSONObject createResponseJSON = createActions(AuthToken, modifiedByName, updatedActions, customerID,
						customerType, requestInstance);
				if (createResponseJSON == null || !createResponseJSON.has(FabricConstants.OPSTATUS)
						|| createResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED, "Updating customer actions failed. Customer id: " + customerID);
					ErrorCodeEnum.ERR_20529.setErrorCode(processedResult);
					Param statusParam = new Param("Status", "Edit failure", FabricConstants.STRING);
					processedResult.addParam(statusParam);
					return processedResult;

				}
			}

			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
					ActivityStatusEnum.SUCCESSFUL, "Assign actions to customer successful. Customer id: " + customerID);
			Param statusParam = new Param("Status", "Edit successful", FabricConstants.STRING);
			processedResult.addParam(statusParam);
			return processedResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
	}

	public void deleteActions(String authToken, String modifiedByName, JSONArray listOfRemovedActions,
			String customerID, DataControllerRequest requestInstance) {
		// TODO Auto-generated method stub
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.clear();
		inputMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + customerID + "'");
		inputMap.put(ODataQueryConstants.SELECT, "id,Action_id");
		Result result = new Result();
		String currActionId;
		String readCustomerActionsResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERACTION_READ, inputMap, null,
				requestInstance);

		JSONObject readCustomerActionsResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerActionsResponse);
		if (readCustomerActionsResponseJSON != null && readCustomerActionsResponseJSON.has(FabricConstants.OPSTATUS)
				&& readCustomerActionsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readCustomerActionsResponseJSON.has("customeraction")) {
			JSONArray readCustomerActionsJSONArray = readCustomerActionsResponseJSON.optJSONArray("customeraction");
			if (!(readCustomerActionsJSONArray == null || readCustomerActionsJSONArray.length() < 1)) {
				for (int indexVar = 0; indexVar < listOfRemovedActions.length(); indexVar++) {

					String actionID = listOfRemovedActions.getString(indexVar);

					for (int i = 0; i < readCustomerActionsJSONArray.length(); i++) {
						JSONObject customerActionsObj = readCustomerActionsJSONArray.getJSONObject(i);
						currActionId = customerActionsObj.optString("Action_id");
						if (currActionId.equalsIgnoreCase(actionID)) {
							inputMap.clear();
							inputMap.put("id", customerActionsObj.optString("id"));
							String deleteCustomerGroupResponse = Executor.invokeService(
									ServiceURLEnum.CUSTOMERACTION_DELETE, inputMap, null, requestInstance);
							JSONObject deleteCustomerGroupResponseJSON = CommonUtilities
									.getStringAsJSONObject(deleteCustomerGroupResponse);
							if (deleteCustomerGroupResponseJSON != null
									&& deleteCustomerGroupResponseJSON.has(FabricConstants.OPSTATUS)
									&& deleteCustomerGroupResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
								diagnostic.prepareDebug("Deleted customer action successfully").log();
							} else {
								diagnostic.prepareDebug("Failed to delete customer action").log();
								ErrorCodeEnum.ERR_21024.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							}
						}
					}
				}
			} else {
				// No Records Exists
			}
		} else {
			diagnostic.prepareDebug("Failed to get customer action").log();
			ErrorCodeEnum.ERR_20983.setErrorCode(result);
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
		}

	}

	public JSONObject createActions(String AuthToken, String modifiedByName, JSONArray updatedActions,
			String CustomerID, String customerType, DataControllerRequest requestInstance) {

		JSONObject createResponseJSON = null;
		for (int indexVar = 0; indexVar < updatedActions.length(); indexVar++) {
			Map<String, String> postParametersMap = new HashMap<String, String>();
			JSONObject actionJSON = null;

			actionJSON = updatedActions.getJSONObject(indexVar);

			JSONArray limits = actionJSON.optJSONArray("limits");
			if (limits != null && limits.length() > 0) {
				for (int k = 0; k < limits.length(); k++) {
					JSONObject limit = limits.getJSONObject(k);
					if (limit.length() != 0) {
						postParametersMap.clear();
						String id = CommonUtilities.getNewId().toString();
						postParametersMap.clear();
						postParametersMap.put("id", id.toString());
						postParametersMap.put("Action_id", actionJSON.optString("id"));
						postParametersMap.put("Customer_id", CustomerID);
						postParametersMap.put("RoleType_id", customerType);
						postParametersMap.put("createdby", modifiedByName);
						postParametersMap.put("modifiedby", modifiedByName);
						postParametersMap.put("LimitType_id", limit.optString("id"));
						postParametersMap.put("value", limit.optString("value"));
						postParametersMap.put("isAllowed", "1");
						String addResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERACTION_CREATE,
								postParametersMap, null, requestInstance);
						createResponseJSON = CommonUtilities.getStringAsJSONObject(addResponse);
					}
				}
			} else {
				String id = CommonUtilities.getNewId().toString();
				postParametersMap.clear();
				postParametersMap.put("id", id.toString());
				postParametersMap.put("Action_id", actionJSON.optString("id"));
				postParametersMap.put("Customer_id", CustomerID);
				postParametersMap.put("RoleType_id", customerType);
				postParametersMap.put("createdby", modifiedByName);
				postParametersMap.put("modifiedby", modifiedByName);
				postParametersMap.put("isAllowed", "1");
				String addResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERACTION_CREATE, postParametersMap,
						null, requestInstance);
				createResponseJSON = CommonUtilities.getStringAsJSONObject(addResponse);
			}
		}
		return createResponseJSON;
	}

}