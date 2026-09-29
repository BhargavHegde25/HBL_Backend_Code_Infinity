package com.temenos.dbx.eum.product.limitsandpermissions.resource.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.businessdelegate.api.LimitsAndPermissionsBusinessDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ContractDTO;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.CustomerGroupDTO;
import com.temenos.dbx.eum.product.limitsandpermissions.resource.api.LimitsAndPermissionsResource;
import com.temenos.dbx.product.utils.InfinityConstants;

public class LimitsAndPermissionsResourceImpl implements LimitsAndPermissionsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	LimitsAndPermissionsBusinessDelegate limitsAndPermissionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class)
			.getBusinessDelegate(LimitsAndPermissionsBusinessDelegate.class);

	@Override
	public Result UpdateServiceDefinitionLimits(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String id = inputParams.get("id");
		if (StringUtils.isBlank(id)) {
			alert.prepareError("Id cannot be null ").log();
			return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
		}
		JSONArray actionLimits = new JSONArray();
		try {
			actionLimits = new JSONArray(request.getParameter("actionLimits"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
		}

		Set<String> contracts = _getContractIds(id);
		if (contracts.size() > 0) {
			String contractIds = String.join(",", contracts);
			if (actionLimits.length() > 0) {
				for (int index = 0; index < actionLimits.length(); index++) {
					JSONObject actionObject = actionLimits.optJSONObject(index);
					ActionLimitsDTO actionLimit = _convertToActionLimitDTO(actionObject);
					actionLimit.setServiceDefinitionId(id);
					actionLimit.setContractId(contractIds);
					if (!limitsAndPermissionsBusinessDelegate.editServiceDefinitionLimits(actionLimit)) {
						return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
					}

				}
			}
			JSONArray removedActionsArray = new JSONArray();
			try {
				removedActionsArray = new JSONArray(request.getParameter("removedActions"));
			} catch (Exception e) {
				alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
				return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
			}
			if (removedActionsArray != null && removedActionsArray.length() > 0) {
				List<String> removedActionList = _getRemovedActionsList(removedActionsArray);
				for (String action : removedActionList) {
					String actions[] = action.split("\\.");
					if (!limitsAndPermissionsBusinessDelegate.deleteServiceDefinitionActions(actions[0], contractIds)) {
						return ErrorCodeEnum.ERR_28032.setErrorCode(new Result());
					}
				}

			}
		}
		return result;
	}

	@Override
	public Result UpdateCustomerRoleLimitsAndPermissions(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String id = inputParams.get("id");
		if (StringUtils.isBlank(id)) {
			alert.prepareError("Id cannot be null ").log();
			return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
		}
		JSONArray actionLimits = new JSONArray();
		try {
			actionLimits = new JSONArray(request.getParameter("actionLimits"));
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
		}

		List<String> customerIdList = _getCustomerIds(id);
		if (customerIdList.size() > 0) {
			String customerIds = String.join(",", customerIdList);
			if (actionLimits.length() > 0) {
				for (int index = 0; index < actionLimits.length(); index++) {
					JSONObject actionObject = actionLimits.optJSONObject(index);
					ActionLimitsDTO actionLimit = _convertToActionLimitDTO(actionObject);
					actionLimit.setCustomerId(customerIds);
					if (!limitsAndPermissionsBusinessDelegate.editCustomerRoleLimits(actionLimit)) {
						return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
					}

				}
			}
			JSONArray removedActionsArray = new JSONArray();
			try {
				removedActionsArray = new JSONArray(request.getParameter("removedActions"));
			} catch (Exception e) {
				alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
				return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
			}
			if (removedActionsArray != null && removedActionsArray.length() > 0) {
				List<String> removedActionList = _getRemovedActionsList(removedActionsArray);
				for (String action : removedActionList) {
					String actions[] = action.split("\\.");
					if (!limitsAndPermissionsBusinessDelegate.deleteCustomerRoleActions(actions[0], customerIds)) {
						return ErrorCodeEnum.ERR_28033.setErrorCode(new Result());
					}
				}

			}
		}
		return result;
	}

	private ActionLimitsDTO _convertToActionLimitDTO(JSONObject actionObject) {
		ActionLimitsDTO actionLimit = new ActionLimitsDTO();
		String actionId = actionObject.optString("id");
		if (StringUtils.isBlank(actionId)) {
			alert.prepareError("Action Id is mandatory input").log();
		}
		actionLimit.setActionId(actionId);
		JSONArray limits = actionObject.getJSONArray("limits");
		if (limits != null && limits.length() > 0) {
			Double minTxVal = 0.0, maxTxVal = 0.0, dailyTxVal = 0.0, weeklyTxVal = 0.0;
			for (int index = 0; index < limits.length(); index++) {
				JSONObject limitObj = limits.optJSONObject(index);
				String type = limitObj.optString("id");
				String value = limitObj.optString("value");

				if (StringUtils.equals(type, "MIN_TRANSACTION_LIMIT")) {
					minTxVal = Double.parseDouble(value);
					actionLimit.setMinTransactionLimitValue(minTxVal);
				} else if (StringUtils.equals(type, "MAX_TRANSACTION_LIMIT")) {
					maxTxVal = Double.parseDouble(value);
					actionLimit.setMaxTransactionLimitValue(maxTxVal);
				} else if (StringUtils.equals(type, "DAILY_LIMIT")) {
					dailyTxVal = Double.parseDouble(value);
					actionLimit.setDailyLimitValue(dailyTxVal);
				} else if (StringUtils.equals(type, "WEEKLY_LIMIT")) {
					weeklyTxVal = Double.parseDouble(value);
					actionLimit.setWeeklyLimitValue(weeklyTxVal);
				}

			}
		}
		return actionLimit;
	}

	private Set<String> _getContractIds(String id) {
		List<ContractDTO> contractList = limitsAndPermissionsBusinessDelegate.fetchContracts(id);
		Set<String> contractIdsList = new HashSet<String>();
		if (contractList != null) {
			contractIdsList = contractList.stream().map(ContractDTO::getId).collect(Collectors.toSet());
		}
		return contractIdsList;
	}

	private List<String> _getRemovedActionsList(JSONArray actions) {
		List<String> actionsList = new ArrayList<>();
		for (int i = 0; i < actions.length(); i++) {
			actionsList.add(actions.getString(i));
		}
		return actionsList;
	}

	private List<String> _getCustomerIds(String id) {
		List<CustomerGroupDTO> customerGroupList = limitsAndPermissionsBusinessDelegate.fetchCustomerIds(id);
		List<String> customerIdList = new ArrayList<>();
		if (customerGroupList != null) {
			for (CustomerGroupDTO customer : customerGroupList) {
				customerIdList.add(customer.getCustomerId() + "." + customer.getCoreCustomerId());
			}
		}
		return customerIdList;
	}

	@Override
	public Result addFeaturesAndActions(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String serviceDefinitionId = inputParams.get(InfinityConstants.serviceDefinitionId);

		if (StringUtils.isBlank(serviceDefinitionId)) {
			alert.prepareError("serviceDefinitionId is a mandatory parameter and can't be blank ").log();
			return ErrorCodeEnum.ERR_28032.setErrorCode(new Result(),
					"roleId or serviceDefinitionID is a mandatory parameter and can't be null");
		}

		JsonArray actionArray = new JsonArray();
		try {
			String actions = dcRequest.getParameter(InfinityConstants.actions);
			if (StringUtils.isNoneBlank(actions))
				actionArray = new JsonParser().parse(actions).getAsJsonArray();
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting actions input param  to Json Array :", e).log();
		}

		JsonArray featuresArray = new JsonArray();
		try {
			String features = dcRequest.getParameter(InfinityConstants.features);
			if (StringUtils.isNotBlank(features))
				featuresArray = new JsonParser().parse(features).getAsJsonArray();
		} catch (Exception e) {
			alert.prepareError("Caught exception while converting features input param to Json Array :", e).log();
		}

		Set<String> features = new HashSet<String>();
		Set<String> actions = new HashSet<String>();

		if (featuresArray.size() > 0) {
			featuresArray.forEach(e -> features.add(e.getAsString()));
		}

		if (actionArray.size() > 0) {
			actionArray.forEach(e -> actions.add(e.getAsString()));
		}

		if (StringUtils.isNotBlank(serviceDefinitionId)) {
			String id = AddJobEntryIfNotThere(serviceDefinitionId, null, dcRequest.getHeaderMap());
			handleServiceDefinition(serviceDefinitionId, dcRequest.getHeaderMap(), actions, features);
			updateJobStatusToComplete(id, dcRequest.getHeaderMap());
		}

		return result;
	}

	private void updateJobStatusToComplete(String id, Map<String, Object> headerMap) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(InfinityConstants.id, id);
		inputMap.put(InfinityConstants.status, InfinityConstants.jobStatus.SID_JOB_COMPLETED);
		ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap, URLConstants.INFINITY_JOB_UPDATE);
	}

	private String AddJobEntryIfNotThere(String serviceDefinitionId, String id, Map<String, Object> headerMap) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(InfinityConstants.id, id);
		inputMap.put(InfinityConstants.status, InfinityConstants.jobStatus.SID_JOB_INPROGRESS);
		inputMap.put(InfinityConstants.type, InfinityConstants.jobType.SERVICE_DEFINITION_EDIT);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(InfinityConstants.serviceDefinitionId, serviceDefinitionId);
		inputMap.put(InfinityConstants.data, jsonObject.toString());
		JsonObject infinityJobJson = new JsonObject();
		if (StringUtils.isBlank(id)) {
			id = HelperMethods.getNewId();
			inputMap.put(InfinityConstants.id, id);
			infinityJobJson = ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap,
					URLConstants.INFINITY_JOB_CREATE);
		} else {
			inputMap.put(InfinityConstants.id, id);
			infinityJobJson = ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap,
					URLConstants.INFINITY_JOB_UPDATE);
		}

		if (null != infinityJobJson && JSONUtil.hasKey(infinityJobJson, DBPDatasetConstants.DATASET_INFINITY_JOB)
				&& infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).isJsonArray()
				&& infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).getAsJsonArray().size() > 0) {
			return id;
		}

		return null;
	}

	@Override
	public Result AddNewFeaturesFromJob(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Map<String, String> bundleConfigurations = BundleConfigurationHandler
				.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, request);
		if (InfinityConstants.IMMEDIATE
				.equals(bundleConfigurations.get(BundleConfigurationHandler.CONTRACT_JOB_SCHEDULING_CONFIG))) {
			return new Result();
		}

		Map<String, String> serviceDefintionsInProgress = new HashMap<String, String>();
		Set<String> serviceDefinitions = getServiceDefintions(request.getHeaderMap());
		serviceDefintionsInProgress = getServiceDefintionsInProgress(request.getHeaderMap());
		for (String serviceDefinition : serviceDefinitions) {
			if (!serviceDefintionsInProgress.containsKey(serviceDefinition)) {
				String id = AddJobEntryIfNotThere(serviceDefinition, null, request.getHeaderMap());
				handleServiceDefinition(serviceDefinition, request.getHeaderMap(), null, null);
				updateJobStatusToComplete(id, request.getHeaderMap());
			}
		}

		return new Result();
	}

	private Set<String> getServiceDefintions(Map<String, Object> headerMap) {
		Set<String> serviceDefinitions = new HashSet<String>();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		JsonObject serviceDefinitionJSON = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
				URLConstants.SERVICEDEFINITION_GET);
		if (null != serviceDefinitionJSON
				&& JSONUtil.hasKey(serviceDefinitionJSON, DBPDatasetConstants.DATASET_SERVICEDEFINITION)
				&& serviceDefinitionJSON.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).isJsonArray()) {
			JsonArray array = serviceDefinitionJSON.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).getAsJsonArray();
			for (JsonElement element : array) {
				JsonObject serviceDefinition = element.getAsJsonObject();
				if (serviceDefinition.has(InfinityConstants.id)
						&& !serviceDefinition.get(InfinityConstants.id).isJsonNull()) {
					serviceDefinitions.add(serviceDefinition.get(InfinityConstants.id).getAsString());
				}
			}
		}

		return serviceDefinitions;
	}

	private Map<String, String> getServiceDefintionsInProgress(Map<String, Object> headerMap) {
		Map<String, String> serviceDefinitions = new HashMap<String, String>();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		try {
			String filter = InfinityConstants.status + DBPUtilitiesConstants.EQUAL
					+ InfinityConstants.jobStatus.SID_JOB_INPROGRESS;
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject infinityJobJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.INIFNITY_JOB_GET);
			if (null != infinityJobJson && JSONUtil.hasKey(infinityJobJson, DBPDatasetConstants.DATASET_INFINITY_JOB)
					&& infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).isJsonArray()) {
				JsonArray array = infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).getAsJsonArray();
				for (JsonElement element : array) {
					JsonObject job = element.getAsJsonObject();
					if (job.has(InfinityConstants.data) && !job.get(InfinityConstants.data).isJsonNull()) {
						String data = job.get(InfinityConstants.data).getAsString();
						if (data != null) {
							JsonParser parser = new JsonParser();
							JsonObject datajsonobj = parser.parse(data).getAsJsonObject();
							if (datajsonobj.has(InfinityConstants.serviceDefinitionId)
									&& !datajsonobj.get(InfinityConstants.serviceDefinitionId).isJsonNull()) {
								serviceDefinitions.put(job.get(InfinityConstants.id).getAsString(),
										datajsonobj.get(InfinityConstants.serviceDefinitionId).getAsString());
							}
						}
					}
					if (job.has(InfinityConstants.serviceDefinitionId)
							&& !job.get(InfinityConstants.serviceDefinitionId).isJsonNull()) {
						serviceDefinitions.put(job.get(InfinityConstants.serviceDefinitionId).getAsString(),
								job.get(InfinityConstants.id).getAsString());
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured",e).log();
		}

		return serviceDefinitions;
	}

	private void handleServiceDefinition(String serviceDefinitionId, Map<String, Object> headerMap, Set<String> actions,
			Set<String> features) {
		Set<String> contracts = new HashSet<String>();
		Set<String> ids = new HashSet<String>();
		if (actions == null) {
			actions = new HashSet<String>();
		}

		if (features == null) {
			features = new HashSet<String>();
		}
		Map<String, Object> inputMap = new HashMap<String, Object>();
		String filter = InfinityConstants.serviceDefinitionId + DBPUtilitiesConstants.EQUAL + serviceDefinitionId
				+ DBPUtilitiesConstants.AND + InfinityConstants.isNewAction + DBPUtilitiesConstants.EQUAL + "'1'";
		inputMap.put(DBPUtilitiesConstants.FILTER, filter);
		String companyLegalUnit = null;
		Map<String, JsonObject> map = new HashMap<String, JsonObject>();
		JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap,
				URLConstants.SERVICE_DEFINITION_ACTION_LIMIT_GET);
		if (jsonObject.has(DBPDatasetConstants.DATASET_SERVICE_DEFINITION_ACTION_LIMIT)
				&& !jsonObject.get(DBPDatasetConstants.DATASET_SERVICE_DEFINITION_ACTION_LIMIT).isJsonNull()) {
			JsonArray array = jsonObject.get(DBPDatasetConstants.DATASET_SERVICE_DEFINITION_ACTION_LIMIT)
					.getAsJsonArray();
			for (JsonElement e : array) {
				
				
				
				JsonObject actionJsonObject = e.getAsJsonObject();

				if (StringUtils.isBlank(companyLegalUnit)) {
					companyLegalUnit = actionJsonObject.has(InfinityConstants.LegalEntityId)
							&& !actionJsonObject.get(InfinityConstants.LegalEntityId).isJsonNull()
									? actionJsonObject.get(InfinityConstants.LegalEntityId).getAsString()
									: "";
				}
				String actionId = actionJsonObject.has(InfinityConstants.actionId)
						&& !actionJsonObject.get(InfinityConstants.actionId).isJsonNull()
								? actionJsonObject.get(InfinityConstants.actionId).getAsString()
								: "";
				if (!map.containsKey(actionId))
					map.put(actionJsonObject.get(InfinityConstants.actionId).getAsString(), e.getAsJsonObject());
				if (actionJsonObject.has(InfinityConstants.limitTypeId)
						&& !actionJsonObject.get(InfinityConstants.limitTypeId).isJsonNull()) {
					map.get(actionId).add(actionJsonObject.get(InfinityConstants.limitTypeId).getAsString(),
							actionJsonObject.get(InfinityConstants.value));
				}
				actions.add(actionJsonObject.get(InfinityConstants.actionId).getAsString());
				ids.add(actionJsonObject.get(InfinityConstants.id).getAsString());
			}
		}

		if (ids.isEmpty()) {
			return;
		}

		inputMap.clear();
		filter = InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + companyLegalUnit;
		inputMap.put(DBPUtilitiesConstants.FILTER, filter);
		jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap, URLConstants.FEATURE_ACTION_GET);
		if (jsonObject.has(DBPDatasetConstants.FEATURE_ACTION)
				&& !jsonObject.get(DBPDatasetConstants.FEATURE_ACTION).isJsonNull()) {
			JsonArray array = jsonObject.get(DBPDatasetConstants.FEATURE_ACTION).getAsJsonArray();
			for (JsonElement e : array) {
				JsonObject actionJsonObject = e.getAsJsonObject();
				String actionId = actionJsonObject.get(InfinityConstants.id).getAsString();
				if (actions.contains(actionId)) {
					features.add(actionJsonObject.get(InfinityConstants.Feature_id).getAsString());
					map.get(actionId).add(InfinityConstants.featureId,
							actionJsonObject.get(InfinityConstants.Feature_id));
					map.get(actionId).add(InfinityConstants.isAccountLevel,
							actionJsonObject.get(InfinityConstants.isAccountLevel));
					map.get(actionId).add(InfinityConstants.Type_id, actionJsonObject.get(InfinityConstants.Type_id));
				}
			}
		}

		if (StringUtils.isNotBlank(serviceDefinitionId)) {
			contracts = _getContractIds(serviceDefinitionId);
		}

		boolean isUpdateSuccessful = true;
		for (String contract : contracts) {
			JsonObject log = getLogObject(serviceDefinitionId, contract, actions, features);
			try {
				if (!handleContract(contract, headerMap, actions, features, map, log,companyLegalUnit)) {
					log.addProperty(InfinityConstants.status, InfinityConstants.jobStatus.SID_JOB_FAILED.toString());
					isUpdateSuccessful = false;
				}
				log.addProperty(InfinityConstants.status, InfinityConstants.jobStatus.SID_JOB_COMPLETED.toString());

			} catch (Exception e) {
				isUpdateSuccessful = false;
				log.addProperty(InfinityConstants.status, InfinityConstants.jobStatus.SID_JOB_FAILED.toString());
			}
			createLogEntry(log, headerMap);
		}

		if (isUpdateSuccessful) {
			clearIsNewflag(ids, headerMap);
		}
	}

	private JsonObject getLogObject(String serviceDefinitionId, String contract, Set<String> actions,
			Set<String> features) {
		JsonObject log = new JsonObject();
		log.addProperty(InfinityConstants.serviceDefinition, serviceDefinitionId);
		log.addProperty(InfinityConstants.contractId, contract);
		log.addProperty(InfinityConstants.actions, String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, actions));
		log.addProperty(InfinityConstants.features, String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, features));
		log.addProperty(InfinityConstants.startTime, getCurrentTimeStamp());
		return log;
	}

	private String getCurrentTimeStamp() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		return df.format(new Date());
	}

	private void createLogEntry(JsonObject log, Map<String, Object> headerMap) {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
		log.addProperty(InfinityConstants.endTime, getCurrentTimeStamp());
		inputParams.put(InfinityConstants.data, log.toString());
		try {
			ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap, URLConstants.INFINITY_JOB_LOG_CREATE);
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
	}

	private void clearIsNewflag(Set<String> ids, Map<String, Object> headerMap) {

		for (String id : ids) {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put(InfinityConstants.id, id);
			inputMap.put(InfinityConstants.isNewAction, "0");
			ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap,
					URLConstants.SERVICE_DEFINITION_ACTION_LIMIT_UPATE);
		}

	}

	private boolean handleContract(String contract, Map<String, Object> headerMap, Set<String> actions,
			Set<String> features, Map<String, JsonObject> map, JsonObject log, String legalEntityId) {
		Set<String> coreCustomerIds = new HashSet<String>();

		if (StringUtils.isNotBlank(contract)) {
			coreCustomerIds = _getCoreCustomers(contract, headerMap);
		}

		Set<String> addedFeatures = new HashSet<String>();
		Set<String> addedActions = new HashSet<String>();
		for (String coreCustomerId : coreCustomerIds) {
			addedFeatures = getFeatures(coreCustomerId, contract, headerMap);
			addedActions = getactions(coreCustomerId, contract, headerMap);

			for (String feature : features) {
				if (addedFeatures.contains(feature)) {
					continue;
				}
				ActionLimitsDTO actionLimitsDTO = new ActionLimitsDTO();
				actionLimitsDTO.setContractId(contract);
				actionLimitsDTO.setCoreCustomerId(coreCustomerId);
				actionLimitsDTO.setFeatureId(feature);
				actionLimitsDTO.setCompanyLegalUnit(legalEntityId);
				actionLimitsDTO.setIsNewFeature("true");

				if (!limitsAndPermissionsBusinessDelegate.addFeaturesToContract(actionLimitsDTO, headerMap, log)) {
					return false;
				}
			}
			JsonObject error = new JsonObject();
			ContractCoreCustomerBusinessDelegate contractCoreCustomerBD = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
			Map<String, Set<String>> contractCoreCustomerDetailsMap = new HashMap<>();
			try {
				contractCoreCustomerDetailsMap = contractCoreCustomerBD.getCoreCustomerAccountsFeaturesActions(contract,
						coreCustomerId, headerMap);
			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
				error.addProperty("error", e.getMessage());
			}
			Set<String> contractCustomerAccounts = contractCoreCustomerDetailsMap.get("accounts");
			for (String action : actions) {
				if (addedActions.contains(action)) {
					continue;
				}
				ActionLimitsDTO actionLimitsDTO = getActionLimitDTO(map.get(action));
				actionLimitsDTO.setContractId(contract);
				actionLimitsDTO.setCoreCustomerId(coreCustomerId);
				actionLimitsDTO.setIsNewAction("true");
				actionLimitsDTO.setCompanyLegalUnit(legalEntityId);
				if (!limitsAndPermissionsBusinessDelegate.addActionsToContract(actionLimitsDTO, headerMap, log,contractCustomerAccounts)) {
					return false;
				}
			}

			
			ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
			try {
				Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
				Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
				customerActions = getActionWithApproveFeatureAction(customerActions, headerMap);
				error.addProperty(InfinityConstants.accounts,
						String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerAccounts));
				error.addProperty(InfinityConstants.actions,
						String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerActions));
				error.addProperty(InfinityConstants.contractId, contract);
				error.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
				approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contract,
						String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerAccounts),
						customerActions.toArray(new String[0]), coreCustomerId, null);

			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
				error.addProperty("error", e.getMessage());
			}
			log.addProperty("approvalMatrixStatus", error.toString());
		}
		return true;

//		Set<ActionLimitsDTO> customers = new HashSet<ActionLimitsDTO>();
//
//		if (StringUtils.isNotBlank(contract)) {
//			customers = _getCustomersFromContract(contract, headerMap);
//		}
//
//		handleCustomerActions(actionLimits, customers, headerMap);
//
//		Set<ActionLimitsDTO> customRoles = new HashSet<ActionLimitsDTO>();
//
//		if (StringUtils.isNotBlank(contract)) {
//			customRoles = _getCustomRolesFromContract(contract, headerMap);
//		}
//		
//		handleCustomRoleActions(actionLimits, customRoles, headerMap);
	}

	private Set<String> getactions(String coreCustomerId, String contract, Map<String, Object> headerMap) {
		Set<String> actions = new HashSet<String>();
		Map<String, Object> inputParams = new HashMap<>();

		String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contract
				+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL
				+ coreCustomerId;

		inputParams.put(DBPUtilitiesConstants.FILTER, filter);

		JsonObject contractActionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
				URLConstants.CONTRACTACTIONLIMIT_GET);

		if (JSONUtil.isJsonNotNull(contractActionsJson)
				&& JSONUtil.hasKey(contractActionsJson, DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT)
				&& contractActionsJson.get(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT).isJsonArray()) {

			JsonArray actionArray = contractActionsJson.get(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT)
					.getAsJsonArray();
			for (JsonElement element : actionArray) {
				if (element.isJsonObject()) {
					JsonObject action = element.getAsJsonObject();
					String actionId = action.has(InfinityConstants.actionId)
							&& !action.get(InfinityConstants.actionId).isJsonNull()
									? action.get(InfinityConstants.actionId).getAsString()
									: "";
					actions.add(actionId);
				}
			}
		}
		return actions;
	}

	private Set<String> getFeatures(String coreCustomerId, String contract, Map<String, Object> headerMap) {

		Set<String> features = new HashSet<String>();
		Map<String, Object> inputParams = new HashMap<>();

		String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contract
				+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL
				+ coreCustomerId;

		inputParams.put(DBPUtilitiesConstants.FILTER, filter);

		JsonObject contractActionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
				URLConstants.CONTRACTFEATURES_GET);

		if (JSONUtil.isJsonNotNull(contractActionsJson)
				&& JSONUtil.hasKey(contractActionsJson, DBPDatasetConstants.DATASET_CONTRACTFEATURES)
				&& contractActionsJson.get(DBPDatasetConstants.DATASET_CONTRACTFEATURES).isJsonArray()) {

			JsonArray actionArray = contractActionsJson.get(DBPDatasetConstants.DATASET_CONTRACTFEATURES)
					.getAsJsonArray();
			for (JsonElement element : actionArray) {
				if (element.isJsonObject()) {
					JsonObject feature = element.getAsJsonObject();
					String featureId = feature.has(InfinityConstants.featureId)
							&& !feature.get(InfinityConstants.featureId).isJsonNull()
									? feature.get(InfinityConstants.featureId).getAsString()
									: "";
					features.add(featureId);
				}
			}
		}
		return features;
	}

	private Set<String> getActionWithApproveFeatureAction(Set<String> customerActions, Map<String, Object> headerMap) {
		StringBuilder actionsString = new StringBuilder();
		for (String action : customerActions) {
			actionsString.append(action);
			actionsString.append(",");
		}
		if (actionsString.length() > 0)
			actionsString.replace(actionsString.length() - 1, actionsString.length(), "");

		ContractFeatureActionsBusinessDelegate contractFeaturesBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);

		String actions = "";
		try {
			actions = contractFeaturesBD.getActionsWithApproveFeatureAction(actionsString.toString(), "",headerMap);
		} catch (ApplicationException e) {
			alert.prepareError(e.toString()).log();
		}

		return HelperMethods.splitString(actions, DBPUtilitiesConstants.COMMA_SEPERATOR);

	}

	private void handleCustomerRole(String customerRole, Map<String, Object> headerMap, JsonArray actionLimits,
			JsonArray features) {
		Set<ActionLimitsDTO> customers = new HashSet<ActionLimitsDTO>();

		if (StringUtils.isNotBlank(customerRole)) {
			customers = _getCustomersFromCustomerRole(customerRole, headerMap);
		}

		handleCustomerActions(actionLimits, customers, headerMap);

		Set<ActionLimitsDTO> customRoles = new HashSet<ActionLimitsDTO>();

		if (StringUtils.isNotBlank(customerRole)) {
			customRoles = _getCustomRolesFromCustomerRole(customerRole, headerMap);
		}

		handleCustomRoleActions(actionLimits, customRoles, headerMap);
	}

	private void handleCustomRoleActions(JsonArray actionLimits, Set<ActionLimitsDTO> customRoles,
			Map<String, Object> headerMap) {
		for (JsonElement actionElement : actionLimits) {
			ActionLimitsDTO actionLimitsDTO = getActionLimitDTO(actionElement.getAsJsonObject());
			for (ActionLimitsDTO customersDTO : customRoles) {
				actionLimitsDTO.setCustomRoleId(customersDTO.getCustomRoleId());
				actionLimitsDTO.setCoreCustomerId(customersDTO.getCoreCustomerId());
				actionLimitsDTO.setContractId(customersDTO.getContractId());

			}
		}
	}

	private void handleCustomerActions(JsonArray actionLimits, Set<ActionLimitsDTO> customers,
			Map<String, Object> headerMap) {
		for (JsonElement actionElement : actionLimits) {
			ActionLimitsDTO actionLimitsDTO = getActionLimitDTO(actionElement.getAsJsonObject());
			for (ActionLimitsDTO customersDTO : customers) {
				actionLimitsDTO.setCustomerId(customersDTO.getCustomerId());
				actionLimitsDTO.setCoreCustomerId(customersDTO.getCoreCustomerId());
				actionLimitsDTO.setContractId(customersDTO.getContractId());
			}
		}
	}

	private ActionLimitsDTO getActionLimitDTO(JsonObject action) {

		String actionId = action.has(InfinityConstants.actionId) && !action.get(InfinityConstants.actionId).isJsonNull()
				? action.get(InfinityConstants.actionId).getAsString()
				: null;
		
		String featureId = action.has(InfinityConstants.featureId)
				&& !action.get(InfinityConstants.featureId).isJsonNull()
						? action.get(InfinityConstants.featureId).getAsString()
						: null;
		Double minTransactionLimit = Double.parseDouble(action.has(InfinityConstants.MIN_TRANSACTION_LIMIT)
				&& !action.get(InfinityConstants.MIN_TRANSACTION_LIMIT).isJsonNull()
						? action.get(InfinityConstants.MIN_TRANSACTION_LIMIT).getAsString()
						: "0");
		Double maxTransactionLimit = Double.parseDouble(action.has(InfinityConstants.MAX_TRANSACTION_LIMIT)
				&& !action.get(InfinityConstants.MAX_TRANSACTION_LIMIT).isJsonNull()
						? action.get(InfinityConstants.MAX_TRANSACTION_LIMIT).getAsString()
						: "0");
		Double dailyLimit = Double.parseDouble(
				action.has(InfinityConstants.DAILY_LIMIT) && !action.get(InfinityConstants.DAILY_LIMIT).isJsonNull()
						? action.get(InfinityConstants.DAILY_LIMIT).getAsString()
						: "0");

		Double weeklyLimit = Double.parseDouble(
				action.has(InfinityConstants.WEEKLY_LIMIT) && !action.get(InfinityConstants.WEEKLY_LIMIT).isJsonNull()
						? action.get(InfinityConstants.WEEKLY_LIMIT).getAsString()
						: "0");
		Boolean isAccountLevel = Boolean.parseBoolean(action.has(InfinityConstants.isAccountLevel)
				&& !action.get(InfinityConstants.isAccountLevel).isJsonNull()
						? action.get(InfinityConstants.isAccountLevel).getAsString()
						: null);
		Boolean isMonetory = "MONETARY".toLowerCase().equalsIgnoreCase(
				(action.has(InfinityConstants.Type_id) && !action.get(InfinityConstants.Type_id).isJsonNull()
						? action.get(InfinityConstants.Type_id).getAsString()
						: "").toLowerCase());

		ActionLimitsDTO actionLimitsDTO = new ActionLimitsDTO();
		actionLimitsDTO.setFeatureId(featureId);
		actionLimitsDTO.setActionId(actionId);
		actionLimitsDTO.setFeatureId(featureId);
		actionLimitsDTO.setMinTransactionLimitValue(minTransactionLimit);
		actionLimitsDTO.setMaxTransactionLimitValue(maxTransactionLimit);
		actionLimitsDTO.setDailyLimitValue(dailyLimit);
		actionLimitsDTO.setWeeklyLimitValue(weeklyLimit);
		actionLimitsDTO.setAccountLevel(isAccountLevel);
		actionLimitsDTO.setMonetory(isMonetory);
		return actionLimitsDTO;
	}

	private Set<ActionLimitsDTO> _getCustomersFromCustomerRole(String customerRole, Map<String, Object> headerMap) {
		Set<ActionLimitsDTO> limitsDTOs = new HashSet<ActionLimitsDTO>();

		String filter = InfinityConstants.Group_id + DBPUtilitiesConstants.EQUAL + customerRole;

		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);

		JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.CUSTOMER_GROUP_GET);

		if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERGROUP)) {
			JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject contractCustomer = jsonArray.get(i).getAsJsonObject();
					String contractId = contractCustomer.has(InfinityConstants.contractId)
							&& !contractCustomer.get(InfinityConstants.contractId).isJsonNull()
									? contractCustomer.get(InfinityConstants.contractId).getAsString()
									: null;
					String coreCustomerId = contractCustomer.has(InfinityConstants.coreCustomerId)
							&& !contractCustomer.get(InfinityConstants.coreCustomerId).isJsonNull()
									? contractCustomer.get(InfinityConstants.coreCustomerId).getAsString()
									: null;
					String customerId = contractCustomer.has(InfinityConstants.Customer_id)
							&& !contractCustomer.get(InfinityConstants.Customer_id).isJsonNull()
									? contractCustomer.get(InfinityConstants.Customer_id).getAsString()
									: null;
					if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(contractId)
							&& StringUtils.isNotBlank(customerId)) {
						ActionLimitsDTO actionLimitsDTO = new ActionLimitsDTO();
						actionLimitsDTO.setContractId(contractId);
						actionLimitsDTO.setCoreCustomerId(coreCustomerId);
						actionLimitsDTO.setCustomerId(customerId);
						limitsDTOs.add(actionLimitsDTO);
					}
				}
			}
		}

		return limitsDTOs;
	}

	private Set<ActionLimitsDTO> _getCustomRolesFromCustomerRole(String customerRole, Map<String, Object> headerMap) {
		Set<ActionLimitsDTO> limitsDTOs = new HashSet<ActionLimitsDTO>();

		String filter = InfinityConstants.roleId + DBPUtilitiesConstants.EQUAL + customerRole;

		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);

		JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.CONTRACT_CUSTOM_ROLE_GET);

		if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE)) {
			JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject contractCustomer = jsonArray.get(i).getAsJsonObject();
					String contractId = contractCustomer.has(InfinityConstants.contractId)
							&& !contractCustomer.get(InfinityConstants.contractId).isJsonNull()
									? contractCustomer.get(InfinityConstants.contractId).getAsString()
									: null;
					String coreCustomerId = contractCustomer.has(InfinityConstants.coreCustomerId)
							&& !contractCustomer.get(InfinityConstants.coreCustomerId).isJsonNull()
									? contractCustomer.get(InfinityConstants.coreCustomerId).getAsString()
									: null;
					String customRoleId = contractCustomer.has(InfinityConstants.customRoleId)
							&& !contractCustomer.get(InfinityConstants.customRoleId).isJsonNull()
									? contractCustomer.get(InfinityConstants.customRoleId).getAsString()
									: null;
					if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(contractId)
							&& StringUtils.isNotBlank(customRoleId)) {
						ActionLimitsDTO actionLimitsDTO = new ActionLimitsDTO();
						actionLimitsDTO.setContractId(contractId);
						actionLimitsDTO.setCoreCustomerId(coreCustomerId);
						actionLimitsDTO.setCustomRoleId(customRoleId);
						limitsDTOs.add(actionLimitsDTO);
					}
				}
			}
		}

		return limitsDTOs;
	}

	private Set<ActionLimitsDTO> _getCustomersFromContract(String contract, Map<String, Object> headerMap) {

		Set<ActionLimitsDTO> limitsDTOs = new HashSet<ActionLimitsDTO>();

		String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contract;

		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.CONTRACT_CUSTOMERS_GET);
		if (response.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
			JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject contractCustomer = jsonArray.get(i).getAsJsonObject();
					String contractId = contractCustomer.has(InfinityConstants.contractId)
							&& !contractCustomer.get(InfinityConstants.contractId).isJsonNull()
									? contractCustomer.get(InfinityConstants.contractId).getAsString()
									: null;
					String coreCustomerId = contractCustomer.has(InfinityConstants.coreCustomerId)
							&& !contractCustomer.get(InfinityConstants.coreCustomerId).isJsonNull()
									? contractCustomer.get(InfinityConstants.coreCustomerId).getAsString()
									: null;
					String customerId = contractCustomer.has(InfinityConstants.customerId)
							&& !contractCustomer.get(InfinityConstants.customerId).isJsonNull()
									? contractCustomer.get(InfinityConstants.customerId).getAsString()
									: null;
					if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(contractId)
							&& StringUtils.isNotBlank(customerId)) {
						ActionLimitsDTO actionLimitsDTO = new ActionLimitsDTO();
						actionLimitsDTO.setContractId(contractId);
						actionLimitsDTO.setCoreCustomerId(coreCustomerId);
						actionLimitsDTO.setCustomerId(customerId);
						limitsDTOs.add(actionLimitsDTO);
					}
				}
			}
		}

		return limitsDTOs;
	}

	private Set<ActionLimitsDTO> _getCustomRolesFromContract(String contract, Map<String, Object> headerMap) {

		Set<ActionLimitsDTO> limitsDTOs = new HashSet<ActionLimitsDTO>();

		String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contract;

		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.CONTRACT_CUSTOM_ROLE_GET);
		if (response.has(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE)) {
			JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();

				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject roleLimit = jsonArray.get(i).getAsJsonObject();
					String customRoleId = roleLimit.has(InfinityConstants.customRoleId)
							&& !roleLimit.get(InfinityConstants.customRoleId).isJsonNull()
									? roleLimit.get(InfinityConstants.customRoleId).getAsString()
									: null;
					String contractId = roleLimit.has(InfinityConstants.contractId)
							&& !roleLimit.get(InfinityConstants.contractId).isJsonNull()
									? roleLimit.get(InfinityConstants.contractId).getAsString()
									: null;
					String coreCustomerId = roleLimit.has(InfinityConstants.coreCustomerId)
							&& !roleLimit.get(InfinityConstants.coreCustomerId).isJsonNull()
									? roleLimit.get(InfinityConstants.coreCustomerId).getAsString()
									: null;
					if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(contractId)
							&& StringUtils.isNotBlank(customRoleId)) {
						ActionLimitsDTO actionLimitsDTO = new ActionLimitsDTO();
						actionLimitsDTO.setContractId(contractId);
						actionLimitsDTO.setCoreCustomerId(coreCustomerId);
						actionLimitsDTO.setCustomRoleId(customRoleId);
						limitsDTOs.add(actionLimitsDTO);
					}
				}
			}
		}

		return limitsDTOs;
	}

	private Set<String> _getCoreCustomers(String contractId, Map<String, Object> headerMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId;
		map.put(DBPUtilitiesConstants.FILTER, filter);
		Set<String> contractCoreCustomers = new HashSet<String>();
		JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
				URLConstants.CONTRACTCORECUSTOMER_GET);
		if (jsonObject.has(DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS)) {
			JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject contractCoreCustomer = jsonArray.get(i).getAsJsonObject();
					String coreCustomerId = contractCoreCustomer.has(InfinityConstants.coreCustomerId)
							&& !contractCoreCustomer.get(InfinityConstants.coreCustomerId).isJsonNull()
									? contractCoreCustomer.get(InfinityConstants.coreCustomerId).getAsString()
									: null;
					contractCoreCustomers.add(coreCustomerId);
				}
			}
		}
		return contractCoreCustomers;
	}

}
