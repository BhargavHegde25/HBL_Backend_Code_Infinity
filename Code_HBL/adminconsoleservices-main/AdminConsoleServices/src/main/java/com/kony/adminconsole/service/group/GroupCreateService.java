package com.kony.adminconsole.service.group;

import com.hbl.adminconsole.getlistcache.GetListCacheInvalidator;
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
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.GroupHandler;
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

public class GroupCreateService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			return invokeOperation(methodID, inputArray, requestInstance, responseInstance);
		} finally {
			// Online-banking getList cache: this operation changes data getList returns. Runs even
			// after a part-way failure, because some rows may already be written.
			GetListCacheInvalidator.permissionsChanged();
		}
	}

	private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		try {

			@SuppressWarnings("unchecked")
			Map<String, String> input = (HashMap<String, String>) inputArray[1];
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			if (StringUtils.isBlank(input.get("Name")) || CommonUtilities.containSpecialChars(input.get("Name"))) {
				Param result_param = new Param("Invalid", "Name cannot be null and shouldnot contain special chars",
						FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(input.get("Description"))
					|| CommonUtilities.containSpecialChars(input.get("Description"))) {
				Param result_param = new Param("Invalid",
						"Description cannot be null and should not contain special characters", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(input.get("Status_id"))
					|| CommonUtilities.containSpecialChars(input.get("Status_id"))) {
				Param result_param = new Param("Invalid",
						"Status cannot be null and should not contain special characters", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(input.get("Type_id"))
					|| CommonUtilities.containSpecialChars(input.get("Type_id"))) {
				Param result_param = new Param("Invalid",
						"Type_Id cannot be null and should not contain special characters", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(input.get("User_id"))
					|| CommonUtilities.containSpecialChars(input.get("User_id"))) {
				Param result_param = new Param("Invalid",
						"User_id cannot be null and should not contain special characters", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(input.get("isEAgreementActive"))
					|| CommonUtilities.containSpecialChars(input.get("isEAgreementActive"))) {
				Param result_param = new Param("Invalid",
						"IEAgreementActive cannot be null and should not contain special characters",
						FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else {
				JSONArray CustomersJSONArray = new JSONArray(requestInstance.getParameter("Customers"));
				Result createGroupResult = createGroup(input.get("Name").toString(),
						input.get("Description").toString(), input.get("Status_id").toString(),
						input.get("User_id").toString(), input.get("Type_id").toString(),
						input.get("isEAgreementActive"), authToken, requestInstance);
				String group_id = createGroupResult.getAllParams().get(0).getValue();
				if (StringUtils.equalsIgnoreCase(input.get("Type_id"), "TYPE_ID_CAMPAIGN")
						&& CustomersJSONArray.length() == 0) {
					result.addParam(new Param("Group_id", group_id, FabricConstants.STRING));
					result.addParam(new Param("Status", "Success", FabricConstants.STRING));
					return result;
				}
				result.addParam(new Param("Group_id", group_id, FabricConstants.STRING));
				if (requestInstance.getParameter("addedOrUpdatedActions") != null) {
					JSONArray addedActions = new JSONArray(requestInstance.getParameter("addedOrUpdatedActions"));
					if (addedActions.length() > 0) {
						GroupHandler.addGroupActions(addedActions, group_id, requestInstance, result);
					}
				}
				if (requestInstance.getParameter("addedOrUpdatedBusinessTypes") != null) {
					JSONArray addedBusinessTypes = new JSONArray(
							requestInstance.getParameter("addedOrUpdatedBusinessTypes"));
					if (addedBusinessTypes.length() > 0) {
						GroupHandler.addGroupBusinessTypes(addedBusinessTypes, group_id, requestInstance, result);
					}
				}
				if (!(input.get("Type_id").equalsIgnoreCase("TYPE_ID_BUSINESS"))) {
					if (CustomersJSONArray.length() > 0) {
						createCustomersGroup(CustomersJSONArray, group_id, authToken, input.get("User_id").toString(),
								ServiceURLEnum.CUSTOMERGROUP_CREATE, requestInstance);
					}
				}
			}
			Param result_param = new Param("Status", "Success", FabricConstants.STRING);
			result.addParam(result_param);
			return result;
		} catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			alert.prepareError("Exception occured in GroupCreateService JAVA service. ApplicationException: ", e).log();
		} catch (Exception e) {
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
		}
		return result;
	}

	public Result createGroup(String name, String Desc, String Status_id, String User_id, String Type_Id,
			String isEAgreementActive, String authToken, DataControllerRequest requestInstance) {
		Result result = new Result();
		Param result_param = new Param();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		String id = CommonUtilities.getNewId().toString();
		postParametersMap.clear();
		postParametersMap.put("id", id.toString());
		postParametersMap.put("Name", name);
		postParametersMap.put("Description", Desc);
		postParametersMap.put("Status_id", Status_id);
		postParametersMap.put("Type_id", Type_Id);
		postParametersMap.put("isEAgreementActive", isEAgreementActive);
		postParametersMap.put("createdby", User_id);
		String createGroupResponse = Executor.invokeService(ServiceURLEnum.MEMBERGROUP_CREATE, postParametersMap, null,
				requestInstance);
		JSONObject createGroupResponseJSON = CommonUtilities.getStringAsJSONObject(createGroupResponse);
		if (createGroupResponseJSON != null && createGroupResponseJSON.has(FabricConstants.OPSTATUS)
				&& createGroupResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERROLES, EventEnum.CREATE,
					ActivityStatusEnum.SUCCESSFUL, "Group created successfully. Group Name: " + name);
			result_param = new Param("Group_id", id.toString(), FabricConstants.STRING);
			result.addParam(result_param);
			return result;
		} else {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERROLES, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "Group creation failed with group name '" + name + "'.");
			ErrorCodeEnum.ERR_20404.setErrorCode(result);
			return result;
		}
	}

	public Result createEntitlements(JSONArray EntitlementsJSONArray, String Group_id, String authToken, String User_id,
			ServiceURLEnum createEntitlementURL, DataControllerRequest requestInstance) {
		Result result = new Result();
		Param result_param = new Param();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		for (int i = 0; i < EntitlementsJSONArray.length(); i++) {
			postParametersMap.clear();
			postParametersMap.put("Group_id", Group_id);
			postParametersMap.put("Service_id", ((JSONObject) EntitlementsJSONArray.get(i)).getString("Service_id"));
			if (!((JSONObject) EntitlementsJSONArray.get(i)).getString("TransactionFee_id").equals("")) {
				postParametersMap.put("TransactionFee_id",
						((JSONObject) EntitlementsJSONArray.get(i)).getString("TransactionFee_id"));
			} /*
				 * else { postParametersMap.put("TransactionFee_id", ((JSONObject)
				 * EntitlementsJSONArray.get(i)).getString("TransactionFee_id")); }
				 */
			if (!((JSONObject) EntitlementsJSONArray.get(i)).getString("TransactionLimit_id").equals("")) {
				postParametersMap.put("TransactionLimit_id",
						((JSONObject) EntitlementsJSONArray.get(i)).getString("TransactionLimit_id"));
			} /*
				 * else { postParametersMap.put("TransactionLimit_id", ((JSONObject)
				 * EntitlementsJSONArray.get(i)).getString("TransactionLimit_id")); }
				 */
			postParametersMap.put("createdby", User_id);

			String createGroupEntitlementResponse = Executor.invokeService(createEntitlementURL, postParametersMap,
					null, requestInstance);
			JSONObject createGroupEntitlmentResponseJSON = CommonUtilities
					.getStringAsJSONObject(createGroupEntitlementResponse);
			int getOpStatusCode = createGroupEntitlmentResponseJSON.getInt(FabricConstants.OPSTATUS);
			if (getOpStatusCode == 0) {
				result_param = new Param("Status", "Success", FabricConstants.STRING);
				result.addParam(result_param);
			} else {
				ErrorCodeEnum.ERR_20421.setErrorCode(result);
			}

		}
		return result;

	}

	public Result createCustomersGroup(JSONArray CustomersJSONArray, String Group_id, String authToken, String User_id,
			ServiceURLEnum createCustomersURL, DataControllerRequest requestInstance) {
		Result result = new Result();
		Param result_param = new Param();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		for (int i = 0; i < CustomersJSONArray.length(); i++) {
			postParametersMap.clear();
			postParametersMap.put("Group_id", Group_id);
			postParametersMap.put("Customer_id", ((JSONObject) CustomersJSONArray.get(i)).getString("Customer_id"));
			postParametersMap.put("createdby", User_id);

			String createCustomersGroupResponse = Executor.invokeService(createCustomersURL, postParametersMap, null,
					requestInstance);
			JSONObject createCustomersGroupResponseJSON = CommonUtilities
					.getStringAsJSONObject(createCustomersGroupResponse);
			if (createCustomersGroupResponseJSON != null
					&& createCustomersGroupResponseJSON.has(FabricConstants.OPSTATUS)
					&& createCustomersGroupResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result_param = new Param("Status", "Success", FabricConstants.STRING);
				result.addParam(result_param);
			} else {
				ErrorCodeEnum.ERR_20425.setErrorCode(result);
			}
		}
		return result;

	}

}