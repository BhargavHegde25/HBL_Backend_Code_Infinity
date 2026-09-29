package com.kony.adminconsole.handler;

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
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GroupHandler {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	public static void removeGroupActions(JSONArray actions, String group_id, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException, Exception {

		Map<String, String> inputMap = new HashMap<String, String>();
		String filter = "";
		for (int i = 0; i < actions.length(); i++) {
			if (StringUtils.isNotBlank(filter)) {
				filter += " or ";
			} else {
				filter += "(";
			}
			filter += "Action_id eq '" + actions.getString(i) + "'";
		}
		filter += ")";
		filter += "and Group_id eq '" + group_id + "'";
		inputMap.put(ODataQueryConstants.FILTER, filter);
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.GROUPACTIONLIMIT_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("groupactionlimit")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			alert.prepareError("Failed to read group action limit").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21859);
		}
		JSONArray groupActionRecords = readResponse.getJSONArray("groupactionlimit");
		for (int i = 0; i < groupActionRecords.length(); i++) {
			inputMap.clear();
			JSONObject record = groupActionRecords.getJSONObject(i);
			inputMap.put("id", record.getString("id"));
			JSONObject deleteResponse = CommonUtilities.getStringAsJSONObject(
					Executor.invokeService(ServiceURLEnum.GROUPACTIONLIMIT_DELETE, inputMap, null, requestInstance));
			if (deleteResponse == null || !deleteResponse.has(FabricConstants.OPSTATUS)
					|| deleteResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				processedResult
						.addParam(new Param("FailureReason", String.valueOf(deleteResponse), FabricConstants.STRING));
				throw new ApplicationException(ErrorCodeEnum.ERR_21860);
			}
		}
	}

	public static JSONArray findGroupCustomers(String groupId, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException, Exception {
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Group_id eq '" + groupId + "'");
		inputMap.put(ODataQueryConstants.SELECT, "Customer_id");

		String readCustomerGroupResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERGROUP_READ, inputMap, null,
				requestInstance);
		JSONObject readCustomerGroupResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerGroupResponse);

		if (readCustomerGroupResponseJSON == null || !readCustomerGroupResponseJSON.has(FabricConstants.OPSTATUS)
				|| readCustomerGroupResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_21865);
		}
		JSONArray readCustomerJSONArray = readCustomerGroupResponseJSON.optJSONArray("customergroup");
		return readCustomerJSONArray;
	}

	public static void removeGroupCustomerActions(JSONArray actions, String group_id,
			DataControllerRequest requestInstance, Result processedResult) throws ApplicationException, Exception {

		Map<String, String> inputMap = new HashMap<String, String>();
		String filter = "";
		JSONArray readCustomerJSONArray = findGroupCustomers(group_id, requestInstance, processedResult);
		if (readCustomerJSONArray != null && readCustomerJSONArray.length() > 0) {
			for (int i = 0; i < readCustomerJSONArray.length(); i++) {
				if (StringUtils.isNotBlank(filter)) {
					filter += " or ";
				} else {
					filter += " (";
				}

				JSONObject customerJSONObject = readCustomerJSONArray.getJSONObject(i);
				String customerId = customerJSONObject.optString("Customer_id");
				filter += "Customer_id eq '" + customerId + "'";
			}
			filter += " )";
		}
		inputMap.put(ODataQueryConstants.FILTER, filter);
		inputMap.put(ODataQueryConstants.SELECT, "id,Action_id");
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.CUSTOMERACTION_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("customeraction")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			alert.prepareError("Failed to read group action limit").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21859);
		}
		JSONArray customerActionRecords = readResponse.getJSONArray("customeraction");
		for (int i = 0; i < actions.length(); i++) {
			String action = actions.getString(i);
			for (int j = 0; j < customerActionRecords.length(); j++) {
				JSONObject record = customerActionRecords.optJSONObject(j);
				String caActionid = record.optString("Action_id");
				if (StringUtils.equals(caActionid, action)) {
					inputMap.clear();
					inputMap.put("id", record.getString("id"));
					JSONObject deleteResponse = CommonUtilities.getStringAsJSONObject(Executor
							.invokeService(ServiceURLEnum.CUSTOMERACTION_DELETE, inputMap, null, requestInstance));
					if (deleteResponse == null || !deleteResponse.has(FabricConstants.OPSTATUS)
							|| deleteResponse.getInt(FabricConstants.OPSTATUS) != 0) {
						processedResult.addParam(
								new Param("FailureReason", String.valueOf(deleteResponse), FabricConstants.STRING));
						throw new ApplicationException(ErrorCodeEnum.ERR_21860);
					}
				}
			}
		}
	}

	public static void addGroupActions(JSONArray actionlimits, String group_id, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException, Exception {

		Map<String, String> existingMap = getExistingActionsMap(group_id, requestInstance, processedResult);

		Map<String, String> inputMap = new HashMap<String, String>();

		for (int i = 0; i < actionlimits.length(); i++) {
			String action_id = actionlimits.getJSONObject(i).getString("id");
			String key = group_id;
			key += action_id;
			inputMap.clear();
			inputMap.put("Group_id", group_id);
			inputMap.put("Action_id", action_id);
			if (actionlimits.getJSONObject(i).has("limits")
					&& (actionlimits.getJSONObject(i).getJSONArray("limits").length() > 0)) {
				JSONArray limits = actionlimits.getJSONObject(i).getJSONArray("limits");
				for (int j = 0; j < limits.length(); j++) {
					String currentKey = key + limits.getJSONObject(j).getString("id");
					inputMap.put("LimitType_id", limits.getJSONObject(j).getString("id"));
					inputMap.put("value", limits.getJSONObject(j).getString("value"));
					inputMap.put("id", getId(currentKey, existingMap));
					ServiceURLEnum serviceURL = getServiceURL(currentKey, existingMap);

					JSONObject createResponse = CommonUtilities
							.getStringAsJSONObject(Executor.invokeService(serviceURL, inputMap, null, requestInstance));
					if (createResponse == null || !createResponse.has(FabricConstants.OPSTATUS)
							|| createResponse.getInt(FabricConstants.OPSTATUS) != 0) {
						processedResult.addParam(
								new Param("FailureReason", String.valueOf(createResponse), FabricConstants.STRING));
						throw new ApplicationException(ErrorCodeEnum.ERR_21861);
					}
				}
			} else {
				inputMap.put("id", getId(key, existingMap));
				ServiceURLEnum serviceURL = getServiceURL(key, existingMap);
				JSONObject createResponse = CommonUtilities
						.getStringAsJSONObject(Executor.invokeService(serviceURL, inputMap, null, requestInstance));
				if (createResponse == null || !createResponse.has(FabricConstants.OPSTATUS)
						|| createResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					processedResult.addParam(
							new Param("FailureReason", String.valueOf(createResponse), FabricConstants.STRING));
					throw new ApplicationException(ErrorCodeEnum.ERR_21861);
				}
			}
		}
	}

	public static Result updateCustomerActionLimits(JSONArray actions, String group_id,
			DataControllerRequest requestInstance, Result result) throws ApplicationException, Exception {

		Map<String, String> inputMap = new HashMap<String, String>();
		String filter = "";
		JSONArray readCustomerJSONArray = findGroupCustomers(group_id, requestInstance, result);
		if (readCustomerJSONArray != null && readCustomerJSONArray.length() > 0) {
			for (int i = 0; i < readCustomerJSONArray.length(); i++) {
				if (StringUtils.isNotBlank(filter)) {
					filter += " or ";
				} else {
					filter += "(";
				}

				JSONObject customerJSONObject = readCustomerJSONArray.getJSONObject(i);
				String customerId = customerJSONObject.optString("Customer_id");
				filter += "Customer_id eq '" + customerId + "'";
			}
			filter += ")";
		}
		inputMap.put(ODataQueryConstants.FILTER, filter);
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.CUSTOMERACTION_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("customeraction")) {
			result.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			alert.prepareError("Failed to read group action limit").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21859);
		}
		JSONArray customerActionRecords = readResponse.optJSONArray("customeraction");

		for (int i = 0; i < actions.length(); i++) {
			String action_id = actions.optJSONObject(i).optString("id");
			if (actions.optJSONObject(i).has("limits")
					&& (actions.optJSONObject(i).optJSONArray("limits").length() > 0)) {
				JSONArray limits = actions.getJSONObject(i).getJSONArray("limits");
				for (int j = 0; j < limits.length(); j++) {
					JSONObject limitObj = limits.optJSONObject(j);
					String type = limitObj.optString("id");
					String value = limitObj.optString("value");
					for (int k = 0; k < customerActionRecords.length(); k++) {
						JSONObject record = customerActionRecords.optJSONObject(k);
						String caActionid = record.optString("Action_id");
						String caLimitType = record.optString("LimitType_id");
						String caValue = record.optString("value");
						String caId = record.optString("id");

						if (StringUtils.equals(caActionid, action_id) && getLimitType(type, caLimitType)) {
							if (StringUtils.isBlank(value)) {
								alert.prepareError("Limit cannot be empty").log();
								ErrorCodeEnum.ERR_21361.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							}
							if (!isDecimalNumber(value)) {
								alert.prepareError("Limit should be decimal").log();
								ErrorCodeEnum.ERR_21371.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							}
							if (Double.parseDouble(value) > Double.parseDouble(caValue)) {
								continue;
							}
							// Updating limit in actionlimit table
							inputMap.clear();
							inputMap.put("Action_id", caActionid);
							inputMap.put("LimitType_id", caLimitType);
							inputMap.put("value", value);
							inputMap.put("id", caId);
							String editActionLimitMinTransResponse = Executor.invokeService(
									ServiceURLEnum.CUSTOMERACTION_UPDATE, inputMap, null, requestInstance);
							JSONObject editActionLimitMinTransResponseJSON = CommonUtilities
									.getStringAsJSONObject(editActionLimitMinTransResponse);
							if ((editActionLimitMinTransResponseJSON != null)
									&& editActionLimitMinTransResponseJSON.has(FabricConstants.OPSTATUS)
									&& editActionLimitMinTransResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
								diagnostic.prepareDebug("Action limit updated successfully.").log();
							} else {
								alert.prepareError("Failed to edit action limit").log();
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								ErrorCodeEnum.ERR_21367.setErrorCode(result);
								return result;
							}

						}
					}
				}

			}
		}
		return result;
	}

	private static boolean weeklyLimitChecks(String caLimitType) {
		switch (caLimitType) {
		case "PRE_APPROVED_TRANSACTION_LIMIT":
		case "AUTO_DENIED_TRANSACTION_LIMIT":
		case "PRE_APPROVED_WEEKLY_LIMIT":
		case "AUTO_DENIED_WEEKLY_LIMIT":
		case "PRE_APPROVED_DAILY_LIMIT":
		case "AUTO_DENIED_DAILY_LIMIT":
			return true;
		default:
			return false;

		}
	}

	private static boolean dailyLimitChecks(String caLimitType) {
		switch (caLimitType) {
		case "PRE_APPROVED_TRANSACTION_LIMIT":
		case "AUTO_DENIED_TRANSACTION_LIMIT":
		case "PRE_APPROVED_DAILY_LIMIT":
		case "AUTO_DENIED_DAILY_LIMIT":
			return true;
		default:
			return false;

		}
	}

	private static boolean transactionLimitChecks(String caLimitType) {
		switch (caLimitType) {
		case "PRE_APPROVED_TRANSACTION_LIMIT":
		case "AUTO_DENIED_TRANSACTION_LIMIT":
			return true;
		default:
			return false;

		}
	}

	private static boolean getLimitType(String type, String caLimitType) {
		// TODO Auto-generated method stub

		if (StringUtils.equals(type, "MAX_TRANSACTION_LIMIT") && transactionLimitChecks(caLimitType)) {
			return true;
		}
		if (StringUtils.equals(type, "DAILY_LIMIT") && dailyLimitChecks(caLimitType)) {
			return true;
		}
		if (StringUtils.equals(type, "WEEKLY_LIMIT") && weeklyLimitChecks(caLimitType)) {
			return true;
		}
		return false;
	}

	private static ServiceURLEnum getServiceURL(String key, Map<String, String> existingMap) {
		if (existingMap.containsKey(key)) {
			return ServiceURLEnum.GROUPACTIONLIMIT_UPDATE;
		}
		return ServiceURLEnum.GROUPACTIONLIMIT_CREATE;
	}

	private static String getId(String key, Map<String, String> existingMap) {
		if (existingMap.containsKey(key)) {
			return existingMap.get(key);
		}
		return String.valueOf(CommonUtilities.getNewId());
	}

	private static Map<String, String> getExistingActionsMap(String group_id, DataControllerRequest requestInstance,
			Result processedResult) throws ApplicationException {
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Group_id eq '" + group_id + "'");
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.GROUPACTIONLIMIT_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("groupactionlimit")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			alert.prepareError("Failed to read group action limit").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21859);
		}

		Map<String, String> resultMap = new HashMap<>();
		JSONArray groupActions = readResponse.getJSONArray("groupactionlimit");
		for (int i = 0; i < groupActions.length(); i++) {
			JSONObject record = groupActions.getJSONObject(i);
			String key = group_id;
			key += record.getString("Action_id");
			if (record.has("LimitType_id")) {
				key += record.getString("LimitType_id");
			}
			resultMap.put(key, record.getString("id"));
		}

		return resultMap;
	}

	public static boolean isDecimalNumber(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static Dataset createBusinessTypeDataset(String groupId, HashMap<String, String> businessIdNameMap,
			DataControllerRequest requestInstance, Result result) {

		Dataset businessTypeDataset = new Dataset();
		businessTypeDataset.setId("businessTypes");
		try {
			Map<String, String> existingMap = getGroupBusinessTypeCustomersCountMap(requestInstance, result);

			// Fetch business type list from groupbusinesstype table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "BusinessType_id,isDefaultGroup");
			inputMap.put(ODataQueryConstants.FILTER, "Group_id eq '" + groupId + "'");

			String readBusinessTypeResponse = Executor.invokeService(ServiceURLEnum.GROUPBUSINESSTYPE_READ, inputMap,
					null, requestInstance);

			JSONObject readBusinessTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readBusinessTypeResponse);
			if (readBusinessTypeResponseJSON != null && readBusinessTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readBusinessTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readBusinessTypeResponseJSON.has("groupbusinesstype")) {
				JSONArray readBusinessTypeJSONArray = readBusinessTypeResponseJSON.optJSONArray("groupbusinesstype");
				if ((readBusinessTypeJSONArray != null) && (readBusinessTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readBusinessTypeJSONArray.length(); indexVar++) {
						JSONObject featureTypeJSONObject = readBusinessTypeJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String businessTypeId = featureTypeJSONObject.optString("BusinessType_id");
						String key = groupId;
						key += businessTypeId;
						
						String count = getCustomerCount(key, existingMap);
						
						String isDefaultGroup = featureTypeJSONObject.optString("isDefaultGroup");
						Param businessTypeId_Param = new Param("id", businessTypeId, FabricConstants.STRING);
						currRecord.addParam(businessTypeId_Param);
						Param businessTypeName_Param = new Param("name", businessIdNameMap.get(businessTypeId),
								FabricConstants.STRING);
						currRecord.addParam(businessTypeName_Param);
						Param isDefaultGroup_Param = new Param("isDefaultGroup", isDefaultGroup,
								FabricConstants.STRING);
						currRecord.addParam(isDefaultGroup_Param);
						Param customerCount_Param = new Param("customerCount", (count==null)?"0":count,
								FabricConstants.STRING);
						currRecord.addParam(customerCount_Param);
						businessTypeDataset.addRecord(currRecord);
					}
					return businessTypeDataset;
				}
			} else {
				result.addParam(new Param("message", readBusinessTypeResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Feature Type Response: " + readBusinessTypeResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21354.setErrorCode(result);
				return businessTypeDataset;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Feature Type. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21354.setErrorCode(result);
			return businessTypeDataset;
		}
		return businessTypeDataset;
	}

	public static HashMap<String, String> createBusinessIdNameMap(DataControllerRequest requestInstance) {

		HashMap<String, String> businessIdNameMap = new HashMap<String, String>();

		try {

			// Fetch Role type list from membergrouptype table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "id, name");

			String readBusinessTypeResponse = Executor.invokeService(ServiceURLEnum.BUSINESSTYPE_READ, inputMap, null,
					requestInstance);

			JSONObject readBusinessTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readBusinessTypeResponse);
			if (readBusinessTypeResponseJSON != null && readBusinessTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readBusinessTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readBusinessTypeResponseJSON.has("businesstype")) {
				JSONArray readBusinessTypeJSONArray = readBusinessTypeResponseJSON.optJSONArray("businesstype");
				if ((readBusinessTypeJSONArray != null) && (readBusinessTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readBusinessTypeJSONArray.length(); indexVar++) {
						JSONObject businessTypeJSONObject = readBusinessTypeJSONArray.getJSONObject(indexVar);
						String businessTypeId = businessTypeJSONObject.optString("id");
						String businessTypeName = businessTypeJSONObject.optString("name");
						businessIdNameMap.put(businessTypeId, businessTypeName);
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Business Type. Exception: ", e).log();
		}
		return businessIdNameMap;
	}

	public static void removeGroupBusinessTypes(JSONArray businesstypes, String group_id,
			DataControllerRequest requestInstance, Result processedResult) throws ApplicationException, Exception {

		Map<String, String> inputMap = new HashMap<String, String>();
		for (int i = 0; i < businesstypes.length(); i++) {
			inputMap.clear();
			inputMap.put("Group_id", group_id);
			inputMap.put("BusinessType_id", businesstypes.getString(i));
			JSONObject deleteResponse = CommonUtilities.getStringAsJSONObject(
					Executor.invokeService(ServiceURLEnum.GROUPBUSINESSTYPE_DELETE, inputMap, null, requestInstance));
			if (deleteResponse == null || !deleteResponse.has(FabricConstants.OPSTATUS)
					|| deleteResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				processedResult
						.addParam(new Param("FailureReason", String.valueOf(deleteResponse), FabricConstants.STRING));
				throw new ApplicationException(ErrorCodeEnum.ERR_21860);
			}
		}
	}

	private static Map<String, String> getExistingBusinessTypesMap(String group_id,
			DataControllerRequest requestInstance, Result processedResult) throws ApplicationException {
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(ODataQueryConstants.FILTER, "Group_id eq '" + group_id + "'");
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.GROUPBUSINESSTYPE_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("groupbusinesstype")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			alert.prepareError("Failed to read group action limit").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21859);
		}

		Map<String, String> resultMap = new HashMap<>();
		JSONArray groupBusinessTypes = readResponse.getJSONArray("groupbusinesstype");
		for (int i = 0; i < groupBusinessTypes.length(); i++) {
			JSONObject record = groupBusinessTypes.getJSONObject(i);
			String key = group_id;
			key += record.getString("BusinessType_id");
			resultMap.put(key, record.getString("BusinessType_id"));
		}
		return resultMap;
	}
	private static Map<String, String> getGroupBusinessTypeCustomersCountMap(DataControllerRequest requestInstance, Result processedResult) throws ApplicationException {
		Map<String, String> inputMap = new HashMap<String, String>();
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.GROUPBUSINESSTYPECUSTOMERCOUNT_VIEW_READ, inputMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("groupbusinesstypecustomercount_view")) {
			processedResult.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
			alert.prepareError("Failed to read group action limit").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21859);
		}

		Map<String, String> resultMap = new HashMap<>();
		JSONArray groupBusinessTypes = readResponse.getJSONArray("groupbusinesstypecustomercount_view");
		for (int i = 0; i < groupBusinessTypes.length(); i++) {
			JSONObject record = groupBusinessTypes.getJSONObject(i);
			String key = record.getString("Group_id");
			key += record.getString("BusinessType_id");
			resultMap.put(key, record.getString("Customers_Count"));
		}
		return resultMap;
	}
	private static String getCustomerCount(String key, Map<String, String> existingMap)
	{
		if (existingMap.containsKey(key)) {
			return existingMap.get(key);
		}
		return null;
	}

	private static ServiceURLEnum getBusinessTypeURL(String key, Map<String, String> existingMap) {
		if (existingMap.containsKey(key)) {
			return ServiceURLEnum.GROUPBUSINESSTYPE_UPDATE;
		}
		return ServiceURLEnum.GROUPBUSINESSTYPE_CREATE;
	}

	public static void addGroupBusinessTypes(JSONArray businesstypes, String group_id,
			DataControllerRequest requestInstance, Result processedResult) throws ApplicationException, Exception {

		Map<String, String> existingMap = getExistingBusinessTypesMap(group_id, requestInstance, processedResult);

		Map<String, String> inputMap = new HashMap<String, String>();

		for (int i = 0; i < businesstypes.length(); i++) {
			String businesstype_id = businesstypes.getJSONObject(i).getString("id");
			String isDefault = businesstypes.getJSONObject(i).getString("isDefault");
			String key = group_id;
			key += businesstype_id;

			ServiceURLEnum serviceURL = getBusinessTypeURL(key, existingMap);

			if(serviceURL.equals(ServiceURLEnum.GROUPBUSINESSTYPE_CREATE) ) {
				inputMap.clear();
				inputMap.put("Group_id", group_id);
				inputMap.put("BusinessType_id", businesstype_id);
				inputMap.put("isDefaultGroup", isDefault);				
				JSONObject createResponse = CommonUtilities
						.getStringAsJSONObject(Executor.invokeService(serviceURL, inputMap, null, requestInstance));
				if (createResponse == null || !createResponse.has(FabricConstants.OPSTATUS)
						|| createResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					processedResult.addParam(
							new Param("FailureReason", String.valueOf(createResponse), FabricConstants.STRING));
					throw new ApplicationException(ErrorCodeEnum.ERR_21861);
				}
			}
			if(isDefault.equals("1") || serviceURL.equals(ServiceURLEnum.GROUPBUSINESSTYPE_UPDATE))
			{
				inputMap.clear();
				inputMap.put("_businessTypeId", businesstype_id);
				inputMap.put("_groupId", group_id);
				inputMap.put("_isDefault", isDefault);
				String updateBusinessTypeGroupResponse = Executor.invokeService(
						ServiceURLEnum.BUSINESSTYPE_DEFAULTGROUP_UPDATE_PROC, inputMap, null, requestInstance);
				JSONObject updateBusinessTypeGroupsResponseJSON = CommonUtilities
						.getStringAsJSONObject(updateBusinessTypeGroupResponse);
				if ((updateBusinessTypeGroupsResponseJSON != null)
						&& updateBusinessTypeGroupsResponseJSON.has(FabricConstants.OPSTATUS)
						&& updateBusinessTypeGroupsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
					diagnostic.prepareDebug("Group Business type default group updated successfully.").log();
				} else {
					alert.prepareError("Group Business type default group update failed with business type id '"
							+ businesstype_id + "'.").log();
					processedResult.addParam(new Param("FailureReason",
							String.valueOf(updateBusinessTypeGroupsResponseJSON), FabricConstants.STRING));
					throw new ApplicationException(ErrorCodeEnum.ERR_21861);
				}
			}
		}
	}
}
