package com.temenos.dbx.eum.product.contract.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.FeatureConfiguration;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.DependentActionsBusinessDelegate;

import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;

import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;

import com.temenos.dbx.product.dto.ContractActionLimitsDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ContractBackendDelegateImpl implements ContractBackendDelegate {

    private static final String CONTRACT_ID = "contractId";
    private static final String CONTRACT_NAME = "contractName";
    private static final String CORECUSTOMERID = "coreCustomerId";
    private static final String CORECUSTOMER_NAME = "coreCustomerName";
    private static final String EMAIL = "email";
    private static final String PHONE_COUNTRY_CODE = "phoneCountryCode";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String COUNTRY = "country";
    private static final String SERVICE_DEFINITION_ID = "serviceDefinitionId";
    private static final String LEGAL_ENTITY_ID = "legalEntityId";
    private static final String CONTRACT_ID_DB = "id";
    private static final String CONTRACT_NAME_DB = "name";
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DBXResult validateCustomersForContract(JsonArray customers, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        Map<String, Object> inputParams = new HashMap<>();
        for (JsonElement jsonElement : customers) {
            inputParams.put(DBPUtilitiesConstants.FILTER, JSONUtil.getString(jsonElement.getAsJsonObject(), ""));
            JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTCORECUSTOMER_GET);
            if (JSONUtil.hasKey(jsonObject, DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)
                    && jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS).getAsJsonArray().size() == 0) {
                jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_ASSOCIATED, "true");
            } else {
                jsonElement.getAsJsonObject().addProperty(DBPUtilitiesConstants.IS_ASSOCIATED, "false");
            }
        }
        response.setResponse(customers);
        return response;
    }

    @Override
    public DBXResult searchContracts(Map<String, String> inputParams, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult responseDTO = new DBXResult();
        Map<String, Object> procParams = new HashMap<>();
        procParams.put("_contractId", StringUtils.isNotBlank(CONTRACT_ID) ? inputParams.get(CONTRACT_ID) : "");
        procParams.put("_contractName", StringUtils.isNotBlank(CONTRACT_NAME) ? inputParams.get(CONTRACT_NAME) : "");
        procParams.put("_coreCustomerId",
                StringUtils.isNotBlank(CORECUSTOMERID) ? inputParams.get(CORECUSTOMERID) : "");
        procParams.put("_coreCustomerName",
                StringUtils.isNotBlank(CORECUSTOMER_NAME) ? inputParams.get(CORECUSTOMER_NAME) : "");
        procParams.put("_email", StringUtils.isNotBlank(EMAIL) ? inputParams.get(EMAIL) : "");
        procParams.put("_phoneCountryCode",
                StringUtils.isNotBlank(PHONE_COUNTRY_CODE) ? inputParams.get(PHONE_COUNTRY_CODE) : "");
        procParams.put("_phoneNumber", StringUtils.isNotBlank(PHONE_NUMBER) ? inputParams.get(PHONE_NUMBER) : "");
        procParams.put("_country", StringUtils.isNotBlank(COUNTRY) ? inputParams.get(COUNTRY) : "");
        procParams.put("_serviceDefinitionId",
                StringUtils.isNotBlank(SERVICE_DEFINITION_ID) ? inputParams.get(SERVICE_DEFINITION_ID) : "");
        procParams.put("_legalEntityId",inputParams.get(LEGAL_ENTITY_ID));

        JsonObject response = new JsonObject();
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
                    URLConstants.CONTRACT_SEARCH_PROC);
            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)) {
                if (response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                    responseDTO.setResponse(response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray());
                }
            } else {
                alert.prepareError("CoreCustomerBackendDelegateImpl : Backend response is not appropriate " + response).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10764);
            }
        } catch (Exception e) {
            alert.prepareError("CoreCustomerBackendDelegateImpl : Exception occured while fetching the core customers"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10764);
        }
        return responseDTO;
    }

    @Override
    public ContractDTO getContract(String contractId, String legalEntityId, Map<String, Object> headersMap) throws ApplicationException {
		if (StringUtils.isBlank(contractId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10360);
		}
		Map<String, Object> inputParams = new HashMap<>();
		ContractDTO contractDTO = null;
		inputParams.put("_contractId", contractId);
		inputParams.put("_legalEntityId", legalEntityId);
		JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
				URLConstants.CONTRACT_ADDRESS_COMMUNICATION_PROC);
		try {
			if (null != contractJson && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_RECORDS)
					&& contractJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
				// Filling contact basic info from contact table.
				JsonArray combinedarray = contractJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
				JsonElement element = combinedarray.size() > 0 ? combinedarray.get(0) : new JsonObject();
				contractDTO = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
						ContractDTO.class, false);
			}
			if (null == contractDTO || StringUtils.isBlank(contractDTO.getId())) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10361);
			}
			if (JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_RECORDS1)
					&& contractJson.get(DBPDatasetConstants.DATASET_RECORDS1).isJsonArray()) {
				JsonArray combinedarray = contractJson.get(DBPDatasetConstants.DATASET_RECORDS1).getAsJsonArray();
				// Filling contact communication info from contractcommunication table.
				contractDTO.setCommunication(
						JSONUtils.parseAsList(combinedarray.toString(), ContractCommunicationDTO.class));
			}
			if (JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_RECORDS2)
					&& contractJson.get(DBPDatasetConstants.DATASET_RECORDS2).isJsonArray()) {
				// Filling contact address info from contractaddress table.
				JsonArray combinedarray = contractJson.get(DBPDatasetConstants.DATASET_RECORDS2).getAsJsonArray();
				contractDTO.setAddress(JSONUtils.parseAsList(combinedarray.toString(), AddressDTO.class));
			}
		} catch (Exception e) {
			alert.prepareError("Error occure",e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10361);
		}
		return contractDTO;

	}

    @Override
    public FeatureActionLimitsDTO getRestrictiveFeatureActionLimits(
            String serviceDefinitionId, String contractId,
            String roleId,
            String coreCustomerId, String userId, Map<String, Object> headersMap, boolean isSuperAdmin,
            String accessPolicyIdList, String legalEntityId)
            throws ApplicationException {
        FeatureActionLimitsDTO dto = new FeatureActionLimitsDTO();
        Map<String, Object> procParams = new HashMap<>();
        String locale = "en-US";
        if (headersMap.containsKey("locale") && headersMap.get("locale") != null
                && StringUtils.isNotBlank(headersMap.get("locale").toString())) {
            String finallocale = headersMap.get("locale").toString();
            String[] localearray = finallocale.split("_");
            if (localearray.length <= 1)
                locale = finallocale;
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(localearray[0]);
                sb.append("-");
                sb.append(localearray[1]);
                locale = sb.toString();
            }
        }
        procParams.put("_locale", locale);
        procParams.put("_userId", StringUtils.isNotBlank(userId) ? userId : "");
        procParams.put("_serviceDefinitionId", StringUtils.isNotBlank(serviceDefinitionId) ? serviceDefinitionId : "");
        procParams.put("_roleId", StringUtils.isNotBlank(roleId) ? roleId : "");
        procParams.put("_coreCustomerId", StringUtils.isNotBlank(coreCustomerId) ? coreCustomerId : "");
        procParams.put("_accessPolicyIdList", String.valueOf(accessPolicyIdList));
        procParams.put("_legalEntityId", StringUtils.isNotBlank(legalEntityId) ? legalEntityId : "");
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
                    URLConstants.FETCH_RESTRICTIVE_FEATUREACTIONLIMITS_WITHLEGALENTITYID_PROC);
            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                    && response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                dto = getNormalizedFeatureActionLimits(isSuperAdmin, locale,
                        response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray(), headersMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the restrictive feature action limits" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10766);
        }
        return dto;
    }
    
    
    public FeatureActionLimitsDTO getRestrictiveFeatureActionLimits(
            String serviceDefinitionId, String contractId,
            String roleId,
            String coreCustomerId, String userId, Map<String, Object> headersMap, boolean isSuperAdmin,
            String accessPolicyIdList)
            throws ApplicationException {
        FeatureActionLimitsDTO dto = new FeatureActionLimitsDTO();
        Map<String, Object> procParams = new HashMap<>();
        String locale = "en-US";
        if (headersMap.containsKey("locale") && headersMap.get("locale") != null
                && StringUtils.isNotBlank(headersMap.get("locale").toString())) {
            String finallocale = headersMap.get("locale").toString();
            String[] localearray = finallocale.split("_");
            if (localearray.length <= 1)
                locale = finallocale;
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(localearray[0]);
                sb.append("-");
                sb.append(localearray[1]);
                locale = sb.toString();
            }
        }
        procParams.put("_locale", locale);
        procParams.put("_userId", StringUtils.isNotBlank(userId) ? userId : "");
        procParams.put("_serviceDefinitionId", StringUtils.isNotBlank(serviceDefinitionId) ? serviceDefinitionId : "");
        procParams.put("_roleId", StringUtils.isNotBlank(roleId) ? roleId : "");
        procParams.put("_coreCustomerId", StringUtils.isNotBlank(coreCustomerId) ? coreCustomerId : "");
        procParams.put("_accessPolicyIdList", String.valueOf(accessPolicyIdList));
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
                    URLConstants.FETCH_RESTRICTIVE_FEATUREACTIONLIMITS_PROC);
            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                    && response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                dto = getNormalizedFeatureActionLimits(isSuperAdmin, locale,
                        response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray(), headersMap);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the restrictive feature action limits" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10766);
        }
        return dto;
    }

    private Map<String, JsonObject> fetchLimitGroupDescription(String localeId, Map<String, Object> headersMap) {
        String filter = InfinityConstants.localeId + DBPUtilitiesConstants.EQUAL + localeId;
        Map<String, JsonObject> limitGroupMap = new HashMap<String, JsonObject>();
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headersMap,
                URLConstants.LIMIT_GROUP_DESCRIPTION_GET);
        if (response.has(DBPDatasetConstants.DATASET_LIMIT_GROUP_DESCRIPTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_LIMIT_GROUP_DESCRIPTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    JsonObject limitGroupNameAndDescription = new JsonObject();
                    limitGroupNameAndDescription.addProperty("limitGroupName",
                            JSONUtil.getString(element.getAsJsonObject(), "displayName"));
                    limitGroupNameAndDescription.addProperty("limitGroupDescription",
                            JSONUtil.getString(element.getAsJsonObject(), "displayDescription"));
                    limitGroupMap.put(JSONUtil.getString(element.getAsJsonObject(), "limitGroupId"),
                            limitGroupNameAndDescription);
                }
            }
        }
        return limitGroupMap;
    }

    private FeatureActionLimitsDTO getNormalizedFeatureActionLimits(boolean isSuperAdmin, String locale,
            JsonArray jsonarray,
            Map<String, Object> headersMap)
            throws ApplicationException {
        DependentActionsBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(DependentActionsBusinessDelegate.class);
        DBXResult response = businessDelegate.getDependentActions(headersMap);
        Map<String, Set<String>> dependentActions = (Map<String, Set<String>>) response.getResponse();
        Map<String, JsonObject> limitGroupDetails = fetchLimitGroupDescription(locale, headersMap);
        FeatureActionLimitsDTO dto = new FeatureActionLimitsDTO();
        Map<String, Set<String>> featureaction = new HashMap<>();
        Map<String, Set<String>> newFeatureAction = new HashMap<>();
        Map<String, JsonObject> featureInfo = new HashMap<>();
        Map<String, JsonObject> actionsInfo = new HashMap<>();
        Map<String, Map<String, Map<String, String>>> monetaryActionLimits = new HashMap<>();
        Map<String, Map<String, Map<String, String>>> newMonetaryActionLimits = new HashMap<>();
        Map<String, Set<String>> globalLevelPermissions = new HashMap<>();
        Map<String, Set<String>> accountLevelPermissions = new HashMap<>();
        Map<String, Set<String>> transactionLimits = new HashMap<>();

        for (JsonElement jsonelement : jsonarray) {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            String isAccountLevel = JSONUtil.getString(jsonobject, "isAccountLevel");
            String typeId = JSONUtil.getString(jsonobject, "typeId");
            if (!isSuperAdmin && !"SID_ACTION_ACTIVE".equalsIgnoreCase(JSONUtil.getString(jsonobject, "featureStatus"))
                    && !"SID_ACTION_ACTIVE".equalsIgnoreCase(JSONUtil.getString(jsonobject, "actionStatus")))
                continue;
            if (!featureInfo.containsKey(JSONUtil.getString(jsonobject, "featureId"))) {
                JsonObject feature = new JsonObject();
                feature.addProperty("featureId", JSONUtil.getString(jsonobject, "featureId"));
                feature.addProperty("featureName", JSONUtil.getString(jsonobject, "featureName"));
                feature.addProperty("featureDescription", JSONUtil.getString(jsonobject, "featureDescription"));
                feature.addProperty("featureStatus", JSONUtil.getString(jsonobject, "featureStatus"));
                featureInfo.put(JSONUtil.getString(jsonobject, "featureId"), feature);
            }
            if (!actionsInfo.containsKey(JSONUtil.getString(jsonobject, "actionId"))) {
                JsonObject action = new JsonObject();
                action.addProperty("actionId", JSONUtil.getString(jsonobject, "actionId"));
                action.addProperty("actionName", JSONUtil.getString(jsonobject, "actionName"));
                action.addProperty("actionDescription", JSONUtil.getString(jsonobject, "actionDescription"));
                action.addProperty("isAccountLevel", JSONUtil.getString(jsonobject, "isAccountLevel"));
                action.addProperty("actionStatus", JSONUtil.getString(jsonobject, "actionStatus"));
                action.addProperty("accessPolicyId", JSONUtil.getString(jsonobject, "accessPolicyId"));
                action.addProperty("actionLevelId", JSONUtil.getString(jsonobject, "actionLevelId"));
                action.addProperty("limitGroupId", JSONUtil.getString(jsonobject, "limitGroupId"));
                action.addProperty("typeId", JSONUtil.getString(jsonobject, "typeId"));
                action.addProperty("legalEntityId", JSONUtil.getString(jsonobject, "legalEntityId"));


                action.addProperty("isNewAction", JSONUtil.getString(jsonobject, "isNewAction"));

                if (dependentActions.get(JSONUtil.getString(jsonobject, "actionId")) != null)
                    action.addProperty("dependentActions",
                            dependentActions.get(JSONUtil.getString(jsonobject, "actionId")).toString());
                actionsInfo.put(JSONUtil.getString(jsonobject, "actionId"), action);
                if (StringUtils.isNotBlank(JSONUtil.getString(jsonobject, "limitGroupId"))) {
                    for (Entry<String, JsonElement> limitsEntry : limitGroupDetails
                            .get(JSONUtil.getString(jsonobject, "limitGroupId")).entrySet()) {
                        action.addProperty(limitsEntry.getKey(), limitsEntry.getValue().getAsString());
                    }
                }
            }
            
            Set<String> globalLevelPermissionsTemp = new HashSet<>();
            Set<String> accountLevelPermissionsTemp = new HashSet<>();
            Set<String> transactionLimitsTemp = new HashSet<>();
            if ("0".equalsIgnoreCase(isAccountLevel) || "false".equalsIgnoreCase(isAccountLevel)) {
                if (globalLevelPermissions.containsKey(JSONUtil.getString(jsonobject, "featureId")))
                    globalLevelPermissionsTemp = globalLevelPermissions
                            .get(JSONUtil.getString(jsonobject, "featureId"));
                globalLevelPermissionsTemp.add(JSONUtil.getString(jsonobject, "actionId"));
                globalLevelPermissions.put(JSONUtil.getString(jsonobject, "featureId"), globalLevelPermissionsTemp);
            } else {
                if (accountLevelPermissions.containsKey(JSONUtil.getString(jsonobject, "featureId")))
                    accountLevelPermissionsTemp = accountLevelPermissions
                            .get(JSONUtil.getString(jsonobject, "featureId"));
                accountLevelPermissionsTemp.add(JSONUtil.getString(jsonobject, "actionId"));
                accountLevelPermissions.put(JSONUtil.getString(jsonobject, "featureId"),
                        accountLevelPermissionsTemp);
            }
         
                if (HelperMethods.FEATUREACTION_TYPE.MONETARY.toString().equalsIgnoreCase(typeId)) {
                    if (transactionLimits.containsKey(JSONUtil.getString(jsonobject, "featureId")))
                        transactionLimitsTemp = transactionLimits.get(JSONUtil.getString(jsonobject, "featureId"));
                    transactionLimitsTemp.add(JSONUtil.getString(jsonobject, "actionId"));
                    transactionLimits.put(JSONUtil.getString(jsonobject, "featureId"), transactionLimitsTemp);
                } 
            
            Set<String> actionsSet = new HashSet<>();
            boolean isNewAction = Boolean.parseBoolean(JSONUtil.getString(jsonobject, InfinityConstants.isNewAction))
                    || "1".equals(JSONUtil.getString(jsonobject, InfinityConstants.isNewAction))||
                    "true".equals(JSONUtil.getString(jsonobject, InfinityConstants.isNewAction));
            if (isNewAction) {
                if (newFeatureAction.containsKey(JSONUtil.getString(jsonobject, "featureId"))) {
                    actionsSet = newFeatureAction.get(JSONUtil.getString(jsonobject, "featureId"));
                }
                actionsSet.add(JSONUtil.getString(jsonobject, "actionId"));
                newFeatureAction.put(JSONUtil.getString(jsonobject, "featureId"), actionsSet);
                if (HelperMethods.FEATUREACTION_TYPE.MONETARY.toString()
                        .equalsIgnoreCase(JSONUtil.getString(jsonobject, "typeId"))) {
                    Map<String, Map<String, String>> actionLimitsMap = new HashMap<>();
                    Map<String, String> limits = new HashMap<>();
                    if (newMonetaryActionLimits.containsKey(JSONUtil.getString(jsonobject, "featureId"))) {
                        actionLimitsMap = newMonetaryActionLimits.get(JSONUtil.getString(jsonobject, "featureId"));
                    }
                    if (actionLimitsMap.containsKey(JSONUtil.getString(jsonobject, "actionId"))) {
                        limits = actionLimitsMap.get(JSONUtil.getString(jsonobject, "actionId"));
                    }
                    limits.put(JSONUtil.getString(jsonobject, "limitTypeId"), getMostRestrictiveLimitValue(jsonobject));
                    actionLimitsMap.put(JSONUtil.getString(jsonobject, "actionId"), limits);
                    newMonetaryActionLimits.put(JSONUtil.getString(jsonobject, "featureId"), actionLimitsMap);
                    dto.getMonetaryActions().add(JSONUtil.getString(jsonobject, "actionId"));
                }
                if (Boolean.parseBoolean(JSONUtil.getString(jsonobject, "isAccountLevel"))
                        || "1".equals(JSONUtil.getString(jsonobject, "isAccountLevel"))
                        ||"true".equals(JSONUtil.getString(jsonobject, "isAccountLevel"))) {
                    dto.getAccountLevelActions().add(JSONUtil.getString(jsonobject, "actionId"));
                } else {
                    dto.getGlobalLevelActions().add(JSONUtil.getString(jsonobject, "actionId"));
                }
            }

            if (featureaction.containsKey(JSONUtil.getString(jsonobject, "featureId"))) {
                actionsSet = featureaction.get(JSONUtil.getString(jsonobject, "featureId"));
            }
            actionsSet.add(JSONUtil.getString(jsonobject, "actionId"));
            featureaction.put(JSONUtil.getString(jsonobject, "featureId"), actionsSet);
            if (HelperMethods.FEATUREACTION_TYPE.MONETARY.toString()
                    .equalsIgnoreCase(JSONUtil.getString(jsonobject, "typeId"))) {
                Map<String, Map<String, String>> actionLimitsMap = new HashMap<>();
                Map<String, String> limits = new HashMap<>();
                if (monetaryActionLimits.containsKey(JSONUtil.getString(jsonobject, "featureId"))) {
                    actionLimitsMap = monetaryActionLimits.get(JSONUtil.getString(jsonobject, "featureId"));
                }
                if (actionLimitsMap.containsKey(JSONUtil.getString(jsonobject, "actionId"))) {
                    limits = actionLimitsMap.get(JSONUtil.getString(jsonobject, "actionId"));
                }
                limits.put(JSONUtil.getString(jsonobject, "limitTypeId"), getMostRestrictiveLimitValue(jsonobject));
                actionLimitsMap.put(JSONUtil.getString(jsonobject, "actionId"), limits);
                monetaryActionLimits.put(JSONUtil.getString(jsonobject, "featureId"), actionLimitsMap);
                dto.getMonetaryActions().add(JSONUtil.getString(jsonobject, "actionId"));
            }
            if (Boolean.parseBoolean(JSONUtil.getString(jsonobject, "isAccountLevel"))
                    || "1".equals(JSONUtil.getString(jsonobject, "isAccountLevel"))
                    || "true".equals(JSONUtil.getString(jsonobject, "isAccountLevel"))) {
                dto.getAccountLevelActions().add(JSONUtil.getString(jsonobject, "actionId"));
            } else {
                dto.getGlobalLevelActions().add(JSONUtil.getString(jsonobject, "actionId"));
            }
        }
        
        dto.setMonetaryActionLimits(monetaryActionLimits);
        dto.setActionsInfo(actionsInfo);
        dto.setFeatureInfo(featureInfo);
        dto.setFeatureaction(featureaction);
        dto.setNewMonetaryActionLimits(newMonetaryActionLimits);
        dto.setNewFeatureAction(newFeatureAction);
        dto.setAccountLevelPermissions(accountLevelPermissions);
        dto.setGlobalLevelPermissions(globalLevelPermissions);
        dto.setTransactionLimits(transactionLimits);
        return dto;
    }

    private String getMostRestrictiveLimitValue(JsonObject jsonObject) {
        String fiLimitValue = JSONUtil.getString(jsonObject, "fiLimitValue");
        String serviceLimitValue = JSONUtil.getString(jsonObject, "serviceLimitValue");
        String roleLimitValue = JSONUtil.getString(jsonObject, "groupLimitValue");
        String coreCustomerLimitValue = JSONUtil.getString(jsonObject, "coreCustomerLimitValue");
        String minimumValue = fiLimitValue;
        if (StringUtils.isNotBlank(serviceLimitValue))
            minimumValue =
                    String.valueOf(Math.min(Double.parseDouble(minimumValue), Double.parseDouble(serviceLimitValue)));
        if (StringUtils.isNotBlank(roleLimitValue))
            minimumValue =
                    String.valueOf(Math.min(Double.parseDouble(minimumValue), Double.parseDouble(roleLimitValue)));
        if (StringUtils.isNotBlank(coreCustomerLimitValue))
            minimumValue =
                    String.valueOf(
                            Math.min(Double.parseDouble(minimumValue), Double.parseDouble(coreCustomerLimitValue)));
        return minimumValue;
    }

    @Override
    public JsonArray getContractInfinityUsers(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10374);
        }
        String backendType = "";

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
            backendType = IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType);
        } else {
            backendType = DTOConstants.CORE;
        }
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_contractId", contractId);
        inputParams.put("_backendType", backendType);
        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CONTRACT_USERS_DETAILS_GET_PROC);
        if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                && response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
        	JsonArray usersArray;

            usersArray =  response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();

           usersArray =  removeDuplicateEntries(usersArray);

            return usersArray;
            }

        throw new ApplicationException(ErrorCodeEnum.ERR_10375);

    }
    private JsonArray removeDuplicateEntries(JsonArray usersArray) {

		Set<String> users = new HashSet<>();

		JsonArray newUsersArray = new JsonArray();

		for (JsonElement user : usersArray) {

			JsonObject userobj = user.getAsJsonObject();
			String key = "";

			if (userobj.has("customerId") && userobj.has("primaryCoreCustomerId")) {

				key = userobj.get("customerId").getAsString() + "-"

						+ userobj.get("primaryCoreCustomerId").getAsString();

			}
			else if (userobj.has("customerId")) {
				key = userobj.get("customerId").getAsString();
			}
			if (StringUtils.isNotBlank(key) && 
					!users.contains(key)) {
				
				newUsersArray.add(user);

				users.add(key);

			} 

		}

		return newUsersArray;


	}


    @Override
    public List<ContractDTO> getListOfContractsByStatus(String statusId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException {
        List<ContractDTO> dtoList = new ArrayList<>();
        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = "statusId" + DBPUtilitiesConstants.EQUAL + statusId + DBPUtilitiesConstants.AND
            				+ "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;

            inputParams.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CONTRACT_GET);

            if (JSONUtil.isJsonNotNull(contractJson)
                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
                JsonArray contractArray = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT)
                        .getAsJsonArray();

                for (JsonElement element : contractArray) {
                    if (element.isJsonObject()) {
                        ContractDTO dto = (ContractDTO) DTOUtils
                                .loadJsonObjectIntoObject(element.getAsJsonObject(), ContractDTO.class, false);
                        if (null != dto) {
                            updateServiceDefinitionName(dto, headerMap);
                            dtoList.add(dto);
                        }

                    }

                }
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10379);
        }

        return dtoList;
    }

    private void updateServiceDefinitionName(ContractDTO dto, Map<String, Object> headerMap)
            throws ApplicationException {
        if (null != dto && StringUtils.isNotBlank(dto.getServicedefinitionId())) {
            ServiceDefinitionBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
            ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();
            serviceDefinitionDTO.setId(dto.getServicedefinitionId());
            serviceDefinitionDTO = businessDelegate.getServiceDefinitionDetails(serviceDefinitionDTO, headerMap);
            if (serviceDefinitionDTO == null || StringUtils.isBlank(serviceDefinitionDTO.getId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10379);
            }
            dto.setServicedefinitionName(serviceDefinitionDTO.getName());
            dto.setServiceType(serviceDefinitionDTO.getServiceType());
        }

    }

    @Override
    public boolean updateContractStatus(String contractId, String statusId, Map<String, Object> headerMap)
            throws ApplicationException {
        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(statusId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10380);
        }
        try {
            Map<String, Object> inputParams = new HashMap<>();

            inputParams.put("id", contractId);
            inputParams.put("statusId", statusId);

            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CONTRACT_UPDATE);

            if (JSONUtil.isJsonNotNull(contractJson)
                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
                JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                ContractDTO dto = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                        ContractDTO.class, false);
                if (null == dto || StringUtils.isBlank(dto.getId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10381);
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10381);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }
        return true;

    }
    
    @Override
    public boolean updateContractStatus(ContractDTO contractDTO, Map<String, Object> headerMap)
            throws ApplicationException {
    	String contractId = contractDTO.getId();
    	String statusId = contractDTO.getStatusId();
        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(statusId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10380);
        }
        try {
            Map<String, Object> inputParams = new HashMap<>();
            inputParams = DTOUtils.getParameterMap(contractDTO, true);
            HelperMethods.removeNullValues(inputParams);

            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CONTRACT_UPDATE);

            if (JSONUtil.isJsonNotNull(contractJson)
                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
                JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                ContractDTO dto = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                        ContractDTO.class, false);
                if (null == dto || StringUtils.isBlank(dto.getId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10381);
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10381);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }
        return true;

    }

    @Override
    public ContractDTO updateContract(ContractDTO inputContractDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractDTO resultContractDTO = null;
        if (null == inputContractDTO || StringUtils.isBlank(inputContractDTO.getId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10392);
        }

        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputContractDTO, false);
            HelperMethods.removeNullValues(inputParams);
            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACT_UPDATE);
            if (JSONUtil.isJsonNotNull(contractJson)
                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
                JsonArray contractArray = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT)
                        .getAsJsonArray();
                JsonObject object = contractArray.size() > 0 ? contractArray.get(0).getAsJsonObject()
                        : new JsonObject();
                resultContractDTO =
                        (ContractDTO) DTOUtils.loadJsonObjectIntoObject(object, ContractDTO.class, false);
                if (null == resultContractDTO || StringUtils.isBlank(resultContractDTO.getId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10393);
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10393);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10393);
        }
        return resultContractDTO;
    }

    @Override
    public ContractDTO getContractDetails(ContractDTO contractDTO, Map<String, Object> headersMap)
            throws ApplicationException {

        Map<String, Object> inputParams = new HashMap<>();
        ContractDTO resultContractDTO = null;
        StringBuilder filter = new StringBuilder();
        if (StringUtils.isNotBlank(contractDTO.getId())) {
            filter = filter.append(CONTRACT_ID_DB + DBPUtilitiesConstants.EQUAL + contractDTO.getId());
        }
        if (StringUtils.isNotBlank(contractDTO.getName()) && StringUtils.isNotBlank(filter)) {
            filter.append(DBPUtilitiesConstants.AND);
        }
        if (StringUtils.isNotBlank(contractDTO.getName())) {
            filter.append(CONTRACT_NAME_DB + DBPUtilitiesConstants.EQUAL + "'" + contractDTO.getName() + "'");
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CONTRACT_GET);

        if (null != contractJson && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
                && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
            JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
            JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
            resultContractDTO = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                    ContractDTO.class, false);
        }
        return resultContractDTO;
    }

    @Override
    public List<ContractActionLimitsDTO> getContractActions(ContractActionLimitsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        List<ContractActionLimitsDTO> resultContractActionLimits = new ArrayList<>();
        try {
            Map<String, Object> inputParams = new HashMap<>();
            if (StringUtils.isNotBlank(dto.getContractId())) {
            inputParams.put(DBPUtilitiesConstants.FILTER,
                    CONTRACT_ID + DBPUtilitiesConstants.EQUAL + dto.getContractId());
            } else if (StringUtils.isNotBlank(dto.getCoreCustomerId())) {
                inputParams.put(DBPUtilitiesConstants.FILTER,
                    CORECUSTOMERID + DBPUtilitiesConstants.EQUAL + dto.getCoreCustomerId());
            }

            JsonObject contractActionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTACTIONLIMIT_GET);
            JsonObject contractActionsJson1 = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ACCOUNTLEVELACTIONLIMIT_GET);

            if (JSONUtil.isJsonNotNull(contractActionsJson)
                    && JSONUtil.hasKey(contractActionsJson, DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT)
                    && contractActionsJson.get(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT).isJsonArray()) {

                JsonArray actionArray =
                        contractActionsJson.get(DBPDatasetConstants.DATASET_CONTRACTACTIONLIMIT).getAsJsonArray();
                for (JsonElement element : actionArray) {
                    if (element.isJsonObject()) {
                        ContractActionLimitsDTO contractActionsDTO =
                                (ContractActionLimitsDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                                        ContractActionLimitsDTO.class, false);

                        resultContractActionLimits.add(contractActionsDTO);

                    }

                }

            }
            if (JSONUtil.isJsonNotNull(contractActionsJson1)
                    && JSONUtil.hasKey(contractActionsJson1, DBPDatasetConstants.DATASET_ACCOUNTLEVELACTIONLIMIT)
                    && contractActionsJson1.get(DBPDatasetConstants.DATASET_ACCOUNTLEVELACTIONLIMIT).isJsonArray()) {

                JsonArray actionArray =
                        contractActionsJson1.get(DBPDatasetConstants.DATASET_ACCOUNTLEVELACTIONLIMIT).getAsJsonArray();
                for (JsonElement element : actionArray) {
                    if (element.isJsonObject()) {
                        ContractActionLimitsDTO contractActionsDTO =
                                (ContractActionLimitsDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                                        ContractActionLimitsDTO.class, false);

                        resultContractActionLimits.add(contractActionsDTO);

                    }

                }

            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10403);
        }
        return resultContractActionLimits;
    }

    @Override
    public Set<String> fetchUserCoreCustomerActions(String userId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        Set<String> actions = new HashSet<>();
        Map<String, Object> procParams = new HashMap<>();
        procParams.put("_userId", userId);
        procParams.put("_coreCustomerId", coreCustomerId);
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(procParams, headersMap,
                    URLConstants.FETCH_USER_CORECUSTOMERS_ACTIONS);
            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                    && response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray() &&
                    response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray()) {
                    actions.add(jsonelement.getAsJsonObject().get("actionId").getAsString());
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching user corecustomer actions" + e.getMessage()).log();
        }
        return actions;
    }


    public Map<String, Set<String>> getProductLevelPermissions(Set<String> productIds, Map<String, Object> headersMap)
            throws ApplicationException {
        Map<String, Set<String>> productRefPermissions = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String s : productIds) {
            if (StringUtils.isNotBlank(sb.toString()))
                sb.append("||");
            sb.append(s);
            count++;
        }
        Map<String, Object> inputmap = new HashMap<>();
        inputmap.put("loop_count", String.valueOf(count));
        inputmap.put("loop_seperator", "||");
        inputmap.put("productRef", sb.toString());
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputmap, headersMap,
                    URLConstants.GET_PRODUCTLEVEL_PERMISSIONS, headersMap.get("x-kony-authorization").toString());
            diagnostic.prepareDebug("response from alerts service " + response).log();
            JsonArray loopdataset = response.get("LoopDataset").getAsJsonArray();
            for (JsonElement jsonelement : loopdataset) {
                Set<String> productPermissions = new HashSet<>();
                Boolean atleastOneFeatureFromFacility=false;
                Boolean atleastOneFeature =false;
                String ref = JSONUtil.getString(jsonelement.getAsJsonObject(), "productRef");
                JsonArray productFacilities = jsonelement.getAsJsonObject().get("productFacilities").getAsJsonArray();
                if (productFacilities.size() > 0) {
                    JsonArray features = productFacilities.get(0).getAsJsonObject().get("features").getAsJsonArray();
                    for (JsonElement featureJsonElement : features) {
                        String featureFlag = JSONUtil.getString(featureJsonElement.getAsJsonObject(), "isSelected");
                        if ("true".equalsIgnoreCase(featureFlag)) {

                            JsonArray actions = featureJsonElement.getAsJsonObject().get("actions").getAsJsonArray();
                            for (JsonElement actionJsonElement : actions) {
                                String actionFlag = JSONUtil.getString(actionJsonElement.getAsJsonObject(),
                                        "isSelected");
                                if ("true".equalsIgnoreCase(actionFlag)) {
                                	atleastOneFeatureFromFacility=true;
                                    productPermissions
                                            .add(JSONUtil.getString(actionJsonElement.getAsJsonObject(), "actionId"));
                                }
                            }
                        }
                    }
                    
                    if(!atleastOneFeatureFromFacility)   
                    {
                        JsonArray features1 = productFacilities.get(0).getAsJsonObject().get("features").getAsJsonArray();
                        for (JsonElement featureJsonElement : features1) {
                                JsonArray actions = featureJsonElement.getAsJsonObject().get("actions").getAsJsonArray();
                                for (JsonElement actionJsonElement : actions) {
                                 productPermissions
                                                .add(JSONUtil.getString(actionJsonElement.getAsJsonObject(), "actionId"));
                                    
                                }
                            
                        }
                      
                    }
                  }
                
                JsonArray features = jsonelement.getAsJsonObject().get("features").getAsJsonArray();
                for (JsonElement featureJsonElement : features) {
                    String featureFlag = JSONUtil.getString(featureJsonElement.getAsJsonObject(), "isSelected");
                    if ("true".equalsIgnoreCase(featureFlag)) {
                        JsonArray actions = featureJsonElement.getAsJsonObject().get("actions").getAsJsonArray();
                        for (JsonElement actionJsonElement : actions) {
                            String actionFlag = JSONUtil.getString(actionJsonElement.getAsJsonObject(), "isSelected");
                            if ("true".equalsIgnoreCase(actionFlag)) {
                            	atleastOneFeature=true;
                                productPermissions
                                        .add(JSONUtil.getString(actionJsonElement.getAsJsonObject(), "actionId"));
                            }
                        }
                    }
                }
                if(!atleastOneFeature)   
                {
                    JsonArray features2 =  jsonelement.getAsJsonObject().get("features").getAsJsonArray();
                    for (JsonElement featureJsonElement : features2) {
                            JsonArray actions = featureJsonElement.getAsJsonObject().get("actions").getAsJsonArray();
                            for (JsonElement actionJsonElement : actions) {
                             productPermissions
                                            .add(JSONUtil.getString(actionJsonElement.getAsJsonObject(), "actionId"));
                                
                            }
                        
                    }
                  
                }
                productRefPermissions.put(ref, productPermissions);
                alert.prepareError("productper"+productRefPermissions).log();
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10817);
        }

        return productRefPermissions;
    }
    
    
    public Map<String,Map<String, Set<String>>> getProductLevelPermissions(Set<String> productIds)
            throws ApplicationException {
        
        // Map<productRef,Map<feature, Set<actions>>>
        Map<String,Map<String, Set<String>>> productFeaturePermissions = new HashMap<>();
        
        // call spotlight service to get product permissions for a list of products
        JsonObject response = null;
		try {
			// concatenate productIds with separator ||
			StringBuilder sb = new StringBuilder();
			for (String s : productIds) {
			    if (StringUtils.isNotBlank(sb.toString()))
			        sb.append("||");
			    sb.append(s);
			}
			
			Map<String, Object> inputmap = new HashMap<>();
	        inputmap.put("loop_count", Integer.toString(productIds.size()));
	        inputmap.put("loop_seperator", "||");
	        inputmap.put("productRef", sb.toString());
			
	        response = ServiceCallHelper.invokeServiceAndGetJson(inputmap, null,
                    URLConstants.GET_PRODUCTLEVEL_PERMISSIONS);
			
		} catch (Exception e) {
			alert.prepareError("Spotlight service call execption : ", e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10817);
		}
        
		// Map<feature, Set<actions>>
		Map<String, Set<String>> featureActions = null;
		
        try {
        	JsonArray loopdataset = response.get("LoopDataset").getAsJsonArray();
        	for (JsonElement jsonelement : loopdataset) {
        		JsonObject product = jsonelement.getAsJsonObject();
        		featureActions = new HashMap<>();
        		String productRef = JSONUtil.getString(product, "productRef");
        		
        		/**
        		 * No need to loop through productFacilities
        		loop through all facilities to add features and actions
        		JsonArray productFacilities = product.get("productFacilities").getAsJsonArray();
        		for (JsonElement productFacilityElmnt : productFacilities) {
        			JsonObject productFacility = productFacilityElmnt.getAsJsonObject();
        			JsonArray features = productFacility.get("features").getAsJsonArray();
        			// loop through features to add selected actions
        			for (JsonElement featureJsonElement : features) {
        				JsonObject feature = featureJsonElement.getAsJsonObject();
        				String featureFlag = JSONUtil.getString(feature, "isSelected");
        				if ("true".equalsIgnoreCase(featureFlag)) {
        					String featureId = JSONUtil.getString(feature, "id");
        					
        					JsonArray actions = feature.get("actions").getAsJsonArray();
                            Set<String> selectedActions = new HashSet<>();
                            // loop through actions to add selected actions
                            for (JsonElement actionJsonElement : actions) {
                            	JsonObject action = actionJsonElement.getAsJsonObject();
                                String actionFlag = JSONUtil.getString(action,
                                        "isSelected");
                                if ("true".equalsIgnoreCase(actionFlag)) {
                                	selectedActions
                                            .add(JSONUtil.getString(action, "actionId"));
                                }
                            }
                            // selected feature is added and not null actions set
                            featureActions.put(featureId, selectedActions);
        				}
        			}
        		}
        		*/
        		
        		/**
        		 * loop through all features to add features and actions
        		 */
        		JsonArray features = product.get("features").getAsJsonArray();
        		for (JsonElement featureJsonElement : features) {
        			JsonObject feature = featureJsonElement.getAsJsonObject();
        			String featureFlag = JSONUtil.getString(feature, "isSelected");
                    String featureId = JSONUtil.getString(feature, "featureId");
                    JsonArray actions = feature.get("actions").getAsJsonArray();
                    Set<String> selectedActions = featureActions.containsKey(featureId)?
                        	featureActions.get(featureId) : new HashSet<>();
                    for (JsonElement actionJsonElement : actions) {
                    	JsonObject action = actionJsonElement.getAsJsonObject();
                        String actionFlag = JSONUtil.getString(action, "isSelected");
                        if ("true".equalsIgnoreCase(actionFlag)
                            	|| "false".equalsIgnoreCase(featureFlag)) {
                            selectedActions
                                  .add(JSONUtil.getString(action, "actionId"));
                        }
                     }
                     // selected feature is added and not null actions set
                     featureActions.put(featureId, selectedActions);
        		}
        		// selected feature is added and not null actions set for a product
        		productFeaturePermissions.put(productRef, featureActions);
        	}
        	
        } catch (Exception e) {
        	alert.prepareError("Execption while reading product service response : ", e).log();
        	throw new ApplicationException(ErrorCodeEnum.ERR_10817);
		}

        return productFeaturePermissions;
    }

    @Override
    public Map<String, Set<String>> getArrangementRolePermissions(String role, String rolemapping,
            Map<String, Object> headersMap)
            throws ApplicationException, JsonMappingException, JsonProcessingException {
    	Map<String, Set<String>> aarolePermissions = new HashMap<>();
    	Map<String, String> rolemap = new HashMap<>();
    	if(StringUtils.isBlank(rolemapping)) {
    		return aarolePermissions;
    	}
    	rolemap = new ObjectMapper().readValue(rolemapping, HashMap.class);
        String group = rolemap.get(role);
        FeatureActionLimitsDTO dto = new FeatureActionLimitsDTO();
        //String legalEntityId = null;
		dto = getRestrictiveFeatureActionLimits(null,
                null, group, null, null, headersMap, true, "");// not in getCoreCustomerRoleFeatureActionLimits
        Set<String> rolePermissions = new HashSet<>();

        JsonObject actionJsonObject1 = new JsonObject();
        Map<String, Set<String>> Featureaction = dto.getFeatureaction();
        Map<String, JsonObject> ActionsInfo = dto.getActionsInfo();
        for (Entry<String, Set<String>> featureActionEntry : Featureaction
                .entrySet()) {
            for (String actionId : featureActionEntry.getValue()) {
                for (Entry<String, JsonElement> entrySet : ActionsInfo.get(actionId)
                        .entrySet()) {
                    actionJsonObject1.add(entrySet.getKey(), entrySet.getValue());
                }
                rolePermissions.add(JSONUtil.getString(actionJsonObject1.getAsJsonObject(), "actionId"));
            }
        }
        aarolePermissions.put(role, rolePermissions);
        return aarolePermissions;
    }
    
    public JsonObject retrieveAccountDetails(String accountId,
            List<ContractAccountsDTO> corecustomerAccounts) {
    	
        JsonObject json = new JsonObject();
        for (ContractAccountsDTO account1 : corecustomerAccounts) {
            if(account1.getAccountId().equals(accountId)) {
            	json.addProperty("accountId", account1.getAccountId());
                json.addProperty("accountName", account1.getAccountName());
                json.addProperty("productId", account1.getProductId());
                json.addProperty("accountType", account1.getAccountType());
                json.addProperty("ownerType", account1.getOwnerType());
                break;
            }
        }
        return json;
    }

    @Override
    public DBXResult createContractActionLimit(JsonObject contractActionObj, String contractId,
            String legalEntityId, Map<String, Object> headerMap) throws ApplicationException {
        List<String> contractActionLimits = new ArrayList<String>();
        Map<String, Map<String, Set<String>>> map = new HashMap<String, Map<String, Set<String>>>();
        Map<String, Object> inputParams = new HashMap<String, Object>();
        String serviceDefinitionType = JSONUtil.getString(contractActionObj, "serviceDefinitionType");
        JsonArray accountLevelPermissions = contractActionObj.get("accountLevelPermissions")
                .getAsJsonArray();
        JsonArray globalLevelPermissions = contractActionObj.get("globalLevelPermissions")
                .getAsJsonArray();
        JsonArray transactionLimits = contractActionObj.get("transactionLimits").getAsJsonArray();

        Map<String, Set<String>> coreCustomerFeatures = new HashMap<>();
        createAccountLevelPermissons(contractId, accountLevelPermissions, headerMap, map, contractActionLimits, false,
                coreCustomerFeatures, legalEntityId);
        createGlobalLevelPermissons(contractId, globalLevelPermissions, headerMap, map, contractActionLimits, false,
                coreCustomerFeatures, legalEntityId);
        createTransactionLimits(contractId, transactionLimits, headerMap, map, contractActionLimits, false,
                coreCustomerFeatures, legalEntityId);

        StringBuilder input = new StringBuilder("");
        int queries = contractActionLimits.size();
        if (queries > 0) {
            for (int query = 0; query < queries; query++) {
                String temp = contractActionLimits.get(query);
                // alert.prepareError("Query " + (query + 1) + ":" + temp).log();
                if (query < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }

            inputParams.put(InfinityConstants._queryInput, input.toString());
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CONTRACTACTIONLIMIT_SAVE_PROC);
            // HelperMethods.callApiAsync(inputParams, headerMap,
            // URLConstants.CONTRACTACTIONLIMIT_SAVE_PROC);
        }
        ContractFeatureActionsBusinessDelegate contractFeatureActionBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);
        for (Entry<String, Set<String>> entrySet : coreCustomerFeatures.entrySet()) {
            contractFeatureActionBD.createContractFeatures(entrySet.getValue(), contractId, entrySet.getKey(),
                    serviceDefinitionType, "false", legalEntityId, headerMap);
        }
        return new DBXResult();
    }

    private static void createAccountLevelPermissons(String contractId, JsonArray accountLevelPermissions,
            Map<String, Object> headerMap, Map<String, Map<String, Set<String>>> map, List<String> customerActionLimits,
            boolean isCustomRole, Map<String, Set<String>> coreCustomerFeatures, String legalEntityId) {
    	String isEnabled = null;
    	String isCustLvlEnabled = null;
        for (int i = 0; i < accountLevelPermissions.size(); i++) {
            JsonObject coreCustomerJson = accountLevelPermissions.get(i).getAsJsonObject();
            String coreCustomerId = JSONUtil.getString(coreCustomerJson, "coreCustomerId");
            JsonArray accountsJsonArray = coreCustomerJson.get("accounts").getAsJsonArray();
            for (int j = 0; j < accountsJsonArray.size(); j++) {
                JsonObject accountJsonObject = accountsJsonArray.get(j).getAsJsonObject();
                String accountId = JSONUtil.getString(accountJsonObject, "accountId");
                JsonArray featurePermissions = accountJsonObject.get("featurePermissions")
                        .getAsJsonArray();
                // String portfolioId = JSONUtil.getString(accountJsonObject, "portfolioId");
                String isPortfolioAccount = JSONUtil.getString(accountJsonObject, "isPortfolioAccount");
                for (int k = 0; k < featurePermissions.size(); k++) {
                    JsonObject featureJsonObject = featurePermissions.get(k).getAsJsonObject();
                    String featureId = JSONUtil.getString(featureJsonObject, "featureId");
                    isEnabled = JSONUtil.getString(featureJsonObject, "isEnabled");
                    if("false".equalsIgnoreCase(isEnabled)) {
                    	continue;
                    }
                    JsonArray permissions = featureJsonObject.get("permissions").getAsJsonArray();
                    for (int l = 0; l < permissions.size(); l++) {
                        JsonObject permissionJsonObject = permissions.get(l).getAsJsonObject();
                        String actionId = JSONUtil.getString(permissionJsonObject, "actionId");
                        isEnabled = JSONUtil.getString(permissionJsonObject, "isEnabled");
                        isCustLvlEnabled = JSONUtil.getString(permissionJsonObject, "isCustLvlEnabled");
                        if("false".equalsIgnoreCase(isEnabled)
                        		|| "false".equalsIgnoreCase(isCustLvlEnabled)) {
                        	continue;
                        }
                        addCoreCustomerFeatures(coreCustomerId, featureId, coreCustomerFeatures);
                        createContractAction(contractId, coreCustomerId, featureId, actionId, accountId, null, null,
                                null, customerActionLimits, isPortfolioAccount, legalEntityId);
                    }
                }
            }
        }
    }

    private static void addCoreCustomerFeatures(String coreCustomerId, String featureId,
            Map<String, Set<String>> coreCustomerFeatures) {
        Set<String> features = new HashSet<>();
        if (coreCustomerFeatures.containsKey(coreCustomerId))
            features = coreCustomerFeatures.get(coreCustomerId);
        features.add(featureId);
        coreCustomerFeatures.put(coreCustomerId, features);
        return;

    }

    private static void createGlobalLevelPermissons(String contractId, JsonArray globalLevelPermissions,
            Map<String, Object> headerMap, Map<String, Map<String, Set<String>>> map, List<String> customerActionLimits,
            boolean isCustomRole, Map<String, Set<String>> coreCustomerFeatures, String legalEntityId) {
    	String isEnabled = null;
        for (int i = 0; i < globalLevelPermissions.size(); i++) {
            JsonObject coreCustomerJson = globalLevelPermissions.get(i).getAsJsonObject();
            String coreCustomerId = JSONUtil.getString(coreCustomerJson, "coreCustomerId");
            JsonArray featuresJsonArray = coreCustomerJson.get("features").getAsJsonArray();
            for (int j = 0; j < featuresJsonArray.size(); j++) {
                JsonObject featureJsonObject = featuresJsonArray.get(j).getAsJsonObject();
                String featureId = JSONUtil.getString(featureJsonObject, "featureId");
                isEnabled = JSONUtil.getString(featureJsonObject, "isEnabled");
                if("false".equalsIgnoreCase(isEnabled)) {
                	continue;
                }
                
                JsonArray permissions = featureJsonObject.get("permissions").getAsJsonArray();
                for (int k = 0; k < permissions.size(); k++) {
                    JsonObject permissionJsonObject = permissions.get(k).getAsJsonObject();
                    String actionId = JSONUtil.getString(permissionJsonObject, "actionId");
                    isEnabled = JSONUtil.getString(permissionJsonObject, "isEnabled");
                    if("false".equalsIgnoreCase(isEnabled)) {
                    	continue;
                    }
                    addCoreCustomerFeatures(coreCustomerId, featureId, coreCustomerFeatures);
                    createContractAction(contractId, coreCustomerId, featureId, actionId, null, null, null, null,
                            customerActionLimits, null, legalEntityId);
                }
            }
        }
    }

    private static void createTransactionLimits(String contractId, JsonArray transactionLimits,
            Map<String, Object> headerMap, Map<String, Map<String, Set<String>>> map, List<String> customerActionLimits,
            boolean isCustomRole, Map<String, Set<String>> coreCustomerFeatures, String legalEntityId) {
        for (int i = 0; i < transactionLimits.size(); i++) {
            JsonObject coreCustomerJson = transactionLimits.get(i).getAsJsonObject();
            String coreCustomerId = JSONUtil.getString(coreCustomerJson, "coreCustomerId");
            JsonArray featurePermissions = coreCustomerJson.get("featurePermissions").getAsJsonArray();
            for (int k = 0; k < featurePermissions.size(); k++) {
                JsonObject featureJsonObject = featurePermissions.get(k).getAsJsonObject();
                String featureId = JSONUtil.getString(featureJsonObject, "featureId");
                addCoreCustomerFeatures(coreCustomerId, featureId, coreCustomerFeatures);
                String limitGroupId = JSONUtil.getString(featureJsonObject, "limitGroupId");
                String actionId = JSONUtil.getString(featureJsonObject, "actionId");
                JsonArray limits = featureJsonObject.get("limits").getAsJsonArray();
                for (int l = 0; l < limits.size(); l++) {
                    String id = JSONUtil.getString(limits.get(l).getAsJsonObject(), "id");
                    String value = JSONUtil.getString(limits.get(l).getAsJsonObject(), "value");
                    createContractAction(contractId, coreCustomerId, featureId, actionId, null, limitGroupId, id, value,
                            customerActionLimits, null, legalEntityId);
                }
            }
        }
    }

    private static void createContractAction(String contractId, String cif, String featureId, String actionId,
            String accountId, String limitGroupId, String limitTypeId, String value, List<String> strings,
            String isPortfolio, String legalEntityId) {
        StringBuilder query = new StringBuilder("");
        query.append("\"" + contractId + "\",");
        query.append("\"" + cif + "\",");
        query.append(isPortfolio == null ? "\"\"," : "\"" + isPortfolio + "\",");
        query.append(accountId == null ? "\"\"," : "\"" + accountId + "\",");
        query.append("\"" + featureId + "\",");
        query.append("\"" + actionId + "\",");
        query.append(limitGroupId == null ? "null," : "\"" + limitGroupId + "\",");
        query.append(limitTypeId == null ? "null," : "\"" + limitTypeId + "\",");
        query.append(value == null ? "null," : "\"" + value + "\",");
        query.append("\"" + legalEntityId + "\"");
        strings.add(query.toString());
    }

    @Override
    public DBXResult getNormalizedContractFeatureActionLimits(
            Map<String, List<ContractAccountsDTO>> coreCustomerAccounts, Map<String,Map<String, Set<String>>> productIdPermissions,
            List<ContractCoreCustomersDTO> contractCustomers, FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO,
            Map<String, FeatureActionLimitsDTO> contractFeatureActionDTOMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        JsonObject dbxResultJsonResponse = new JsonObject();
        
        
        
        /**
         * Feature and actions information
         */
        Map<String, JsonObject> featureInfo = serviceDefinitionFeatureActionDTO.getFeatureInfo();
        Map<String, JsonObject> actionsInfo = serviceDefinitionFeatureActionDTO.getActionsInfo();
        Map<String, Map<String, Map<String, String>>> monetaryActionLimits = serviceDefinitionFeatureActionDTO
                .getMonetaryActionLimits();
        /**
         * Global level permission array declarations
         */

        JsonArray globalPermissions = new JsonArray();
        JsonArray accountLevelPermissions = new JsonArray();
        JsonArray transactionlimits = new JsonArray();
        
        
        Map<String, Set<String>> globalLevelPermissionsSD = serviceDefinitionFeatureActionDTO
                .getGlobalLevelPermissions();
        Map<String, Set<String>> accountLevelPermissionsSD = serviceDefinitionFeatureActionDTO
                .getAccountLevelPermissions();
        
        /*This is to return all features in global level permissions for a contract */
       // globalLevelPermissionsSD.putAll(accountLevelPermissionsSD);
		Iterator<String> it = accountLevelPermissionsSD.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();

			if (globalLevelPermissionsSD.containsKey(key)) {
				globalLevelPermissionsSD.get(key).addAll(accountLevelPermissionsSD.get(key));
			} else {
				globalLevelPermissionsSD.put(key, accountLevelPermissionsSD.get(key));
			}
		}

        Map<String, Set<String>> transactionLimitsSD = serviceDefinitionFeatureActionDTO
        		.getTransactionLimits();
        
        JsonObject globalCoreCustomerJsonObject = null;
        JsonArray globalFeatureActionsJsonArray = null;
        JsonArray actionsJsonArray = null;
        JsonArray coreCustomerAccountLevelJsonArray = null;
        JsonArray accountLevelFeatureActionsJsonArray = null;
        JsonArray coreCustomerTransactionLimitsJsonArray = null;
        JsonArray limits = null;
        FeatureActionLimitsDTO contractFeatureActionDTO =null;
        boolean isActionPresentAtProduct = true;
        boolean isProductSpecificFeatueEnabled = 
        		FeatureConfiguration.isProductSpecificFeatueEnabled();
        /**
         * looping each contractCustomers
         */
        for (ContractCoreCustomersDTO contractCustomer : contractCustomers) {

            /**
             * Forming global level permissions json object
             */
        	
        	contractFeatureActionDTO = contractFeatureActionDTOMap.get(contractCustomer.getCoreCustomerId());
			Map<String, Set<String>> coreCustomerGlobalPermissions = new HashMap<>();
			if (contractFeatureActionDTO != null) {
				coreCustomerGlobalPermissions = contractFeatureActionDTO.getCoreCustomerGlobalLevelPermissions();
			}
			globalCoreCustomerJsonObject = addContractCustomerDetails(contractCustomer);
            globalFeatureActionsJsonArray = new JsonArray();
            for (Entry<String, Set<String>> featureActionEntry : globalLevelPermissionsSD.entrySet()) {
                
                actionsJsonArray = new JsonArray();
				for (String actionId : featureActionEntry.getValue()) {
					if (actionsInfo.get(actionId) != null) {
						JsonObject actionJsonObject = actionsInfo.get(actionId).deepCopy();

						boolean isEnabled = false;
						if (coreCustomerGlobalPermissions.containsKey(featureActionEntry.getKey())) {
							isEnabled = coreCustomerGlobalPermissions.get(featureActionEntry.getKey())
									.contains(actionId);
						}
						actionJsonObject.addProperty("isEnabled", String.valueOf(isEnabled));
						actionsJsonArray.add(actionJsonObject);
					}
				}
                JsonObject featureJsonObject = featureInfo
                		.get(featureActionEntry.getKey()).deepCopy();
                featureJsonObject.add("permissions", actionsJsonArray);
                globalFeatureActionsJsonArray.add(featureJsonObject);
            }
            globalCoreCustomerJsonObject.add("features", globalFeatureActionsJsonArray);
            globalPermissions.add(globalCoreCustomerJsonObject);
            diagnostic.prepareDebug("global permissions done").log();

            /**
             * Forming account level permissions
             */
            Map<String, Map<String, Set<String>>> coreCustomerAccountLevelPermissions = new HashMap<>();
			if (contractFeatureActionDTO != null) {
				coreCustomerAccountLevelPermissions = contractFeatureActionDTO.getAsscoiatedAccountActions();
			}
            JsonObject coreCustomerAccountLevel = addContractCustomerDetails(contractCustomer);
            coreCustomerAccountLevelJsonArray = new JsonArray();
            List<ContractAccountsDTO> accounts = coreCustomerAccounts.get(contractCustomer.getCoreCustomerId());
            for (ContractAccountsDTO contractAccount : accounts) {
                JsonObject accountDetails = addAccountDetails(contractAccount);
                String productId = JSONUtil.getString(accountDetails, "productId");

                diagnostic.prepareDebug("product id" + productId).log();

                accountLevelFeatureActionsJsonArray = new JsonArray();
                for (Entry<String, Set<String>> featureActionEntry : accountLevelPermissionsSD.entrySet()) {
                    JsonObject featureJsonObject = featureInfo.get(featureActionEntry.getKey()).deepCopy();
                    actionsJsonArray = new JsonArray();
                    for (String actionId : featureActionEntry.getValue()) {
                        JsonObject actionJsonObject = actionsInfo.get(actionId).deepCopy();
                        boolean isEnabled = false;
                        if (isProductSpecificFeatueEnabled) {
                        	if(productIdPermissions.containsKey(productId)) {
                        		isActionPresentAtProduct = (
                            		productIdPermissions.get(productId).containsKey(featureActionEntry.getKey())
                            		&& productIdPermissions.get(productId).get(featureActionEntry.getKey()).contains(actionId));
                        		if(!isActionPresentAtProduct) {
                                	continue;
                                }
                        	}
                        }
                        
						if (coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId()) != null
								&& coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId())
										.containsKey(featureActionEntry.getKey())) {
							isEnabled = coreCustomerAccountLevelPermissions.get(contractAccount.getAccountId())
									.get(featureActionEntry.getKey()).contains(actionId);
						}
                        actionJsonObject.addProperty("isEnabled", String.valueOf(isEnabled));
                        actionsJsonArray.add(actionJsonObject);
                    }
                    featureJsonObject.add("permissions", actionsJsonArray);
                    accountLevelFeatureActionsJsonArray.add(featureJsonObject);
                }
                accountDetails.add("featurePermissions", accountLevelFeatureActionsJsonArray);
                coreCustomerAccountLevelJsonArray.add(accountDetails);
            }
            coreCustomerAccountLevel.add("accounts", coreCustomerAccountLevelJsonArray);
            accountLevelPermissions.add(coreCustomerAccountLevel);

            diagnostic.prepareDebug("account level permissions done").log();

            /**
             * Forming transaction limits
             */

            
			Map<String, Map<String, Map<String, String>>> coreCustomerTransactionLimits = new HashMap<>();
			if (contractFeatureActionDTO != null) {
				coreCustomerTransactionLimits = contractFeatureActionDTO.getCoreCustomerTransactionLimits();
			}
			JsonObject coreCustomerTransactionLimitsJsonObject = addContractCustomerDetails(contractCustomer);
            coreCustomerTransactionLimitsJsonArray = new JsonArray();
            for (Entry<String, Set<String>> featureActionEntry : transactionLimitsSD.entrySet()) {
                JsonObject featureJsonObject = featureInfo.get(featureActionEntry.getKey()).deepCopy();
                
                for (String actionId : featureActionEntry.getValue()) {
                    for (Entry<String, JsonElement> entrySet : actionsInfo.get(actionId).entrySet()) {
                        featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
                    }
                    boolean isEnabled = false;
                    if (coreCustomerTransactionLimits.containsKey(featureActionEntry.getKey())) {
                        isEnabled = coreCustomerTransactionLimits.get(featureActionEntry.getKey())
                                .containsKey(actionId);
                    }
                    featureJsonObject.addProperty("isEnabled", String.valueOf(isEnabled));
                    limits = new JsonArray();
					if (coreCustomerTransactionLimits.get(featureActionEntry.getKey()) != null
							&& coreCustomerTransactionLimits.get(featureActionEntry.getKey()).get(actionId) != null) {
						Map<String, String> limitsMap = coreCustomerTransactionLimits.get(featureActionEntry.getKey())
								.get(actionId);
						// Map<String, String> limitsMap =
						// monetaryActionLimits.get(featureActionEntry.getKey()).get(actionId);
						for (Entry<String, String> limitsEntry : limitsMap.entrySet()) {
							JsonObject limitsJson = new JsonObject();
							limitsJson.addProperty("id", limitsEntry.getKey());
							limitsJson.addProperty("value", limitsEntry.getValue());
							limits.add(limitsJson);
						}
						featureJsonObject.add("limits", limits);
					}
					else if (monetaryActionLimits.get(featureActionEntry.getKey()) != null
							&& monetaryActionLimits.get(featureActionEntry.getKey()).get(actionId) != null) {
						Map<String, String> limitsMap = monetaryActionLimits.get(featureActionEntry.getKey())
								.get(actionId);
						for (Entry<String, String> limitsEntry : limitsMap.entrySet()) {
							JsonObject limitsJson = new JsonObject();
							limitsJson.addProperty("id", limitsEntry.getKey());
							limitsJson.addProperty("value", limitsEntry.getValue());
							limits.add(limitsJson);
						}
						featureJsonObject.add("limits", limits);
					}
				}
                coreCustomerTransactionLimitsJsonArray.add(featureJsonObject);
            }
            coreCustomerTransactionLimitsJsonObject.add("featurePermissions", coreCustomerTransactionLimitsJsonArray);
            transactionlimits.add(coreCustomerTransactionLimitsJsonObject);
            
            diagnostic.prepareDebug("transaction limits permissions done").log();

        }
        dbxResultJsonResponse.add("globalLevelPermissions", globalPermissions);
        dbxResultJsonResponse.add("accountLevelPermissions", accountLevelPermissions);
        dbxResultJsonResponse.add("transactionLimits", transactionlimits);
        response.setResponse(dbxResultJsonResponse);
        return response;
    }

    public JsonObject addContractCustomerDetails(ContractCoreCustomersDTO coreCustomerDTO) {
        JsonObject contractCustomer = new JsonObject();
        contractCustomer.addProperty("coreCustomerId", coreCustomerDTO.getCoreCustomerId());
        contractCustomer.addProperty("coreCustomerName", coreCustomerDTO.getCoreCustomerName());
        contractCustomer.addProperty("isPrimary", coreCustomerDTO.getIsPrimary());
        contractCustomer.addProperty("addressLine1", coreCustomerDTO.getAddressLine1());
        contractCustomer.addProperty("addressLine2", coreCustomerDTO.getAddressLine2());
        contractCustomer.addProperty("city", coreCustomerDTO.getCityName());
        contractCustomer.addProperty("state", coreCustomerDTO.getState());
        contractCustomer.addProperty("country", coreCustomerDTO.getCountry());
        contractCustomer.addProperty("zipCode", coreCustomerDTO.getZipCode());
        return contractCustomer;
    }

    private JsonObject addAccountDetails(ContractAccountsDTO contractAccountDTO) {
        JsonObject json = new JsonObject();
        json.addProperty("accountId", contractAccountDTO.getAccountId());
        json.addProperty("accountName", contractAccountDTO.getAccountName());
        json.addProperty("productId", contractAccountDTO.getProductId()!=null?contractAccountDTO.getProductId():"");
        json.addProperty("accountType", HelperMethods.getAccountsNames().get(contractAccountDTO.getTypeId()));
        json.addProperty("ownerType", contractAccountDTO.getOwnerType());

        /*
         * json.addProperty("portfolioId", contractAccountDTO.getPortfolioId()); json.addProperty("portfolioName",
         * contractAccountDTO.getPortfolioName()); json.addProperty("isPortfolio",
         * contractAccountDTO.getIsPortfolioAccount());
         */

        return json;
    }

    @Override
    public FeatureActionLimitsDTO getServiceDefinitionPermissions(String serviceDefinitionId,
            Map<String, Object> headersMap) throws ApplicationException {
        FeatureActionLimitsDTO dto = new FeatureActionLimitsDTO();
        Map<String, Object> inputParams = new HashMap<>();
        try {
            inputParams.put("_serviceDefinitionId", serviceDefinitionId);
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.SERVICEDEFINITIONACTIONS_GET);

            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                    && response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {

                JsonArray jsonarray = response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();

                Map<String, Set<String>> featureaction = new HashMap<>();
                Map<String, JsonObject> featureInfo = new HashMap<>();
                Map<String, JsonObject> actionsInfo = new HashMap<>();
                for (JsonElement jsonelement : jsonarray) {
                    JsonObject jsonobject = jsonelement.getAsJsonObject();
                    if (!featureInfo.containsKey(JSONUtil.getString(jsonobject, "featureId"))) {
                        JsonObject feature = new JsonObject();
                        feature.addProperty("featureId", JSONUtil.getString(jsonobject, "featureId"));
                        feature.addProperty("featureName", JSONUtil.getString(jsonobject, "featureName"));
                        feature.addProperty("featureDescription", JSONUtil.getString(jsonobject, "featureDescription"));
                        feature.addProperty("featureStatus", JSONUtil.getString(jsonobject, "featureStatus"));
                        featureInfo.put(JSONUtil.getString(jsonobject, "featureId"), feature);
                    }
                    if (!actionsInfo.containsKey(JSONUtil.getString(jsonobject, "actionId"))) {
                        JsonObject action = new JsonObject();
                        action.addProperty("actionId", JSONUtil.getString(jsonobject, "actionId"));
                        action.addProperty("actionName", JSONUtil.getString(jsonobject, "actionName"));
                        action.addProperty("actionDescription", JSONUtil.getString(jsonobject, "actionDescription"));
                        action.addProperty("actionStatus", JSONUtil.getString(jsonobject, "actionStatus"));
                        actionsInfo.put(JSONUtil.getString(jsonobject, "actionId"), action);
                    }
                    Set<String> actionsSet = new HashSet<>();
                    if (featureaction.containsKey(JSONUtil.getString(jsonobject, "featureId"))) {
                        actionsSet = featureaction.get(JSONUtil.getString(jsonobject, "featureId"));
                    }
                    actionsSet.add(JSONUtil.getString(jsonobject, "actionId"));
                    featureaction.put(JSONUtil.getString(jsonobject, "featureId"), actionsSet);
                }
                dto.setActionsInfo(actionsInfo);
                dto.setFeatureInfo(featureInfo);
                dto.setFeatureaction(featureaction);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10816);
        }
        return dto;
    }

    public DBXResult getNormalizedProductArrangementFeatureActionLimits(String coreCustomerId,
            String serviceDefinitionId, String roleId, JsonArray productArray,
            FeatureActionLimitsDTO serviceDefinitionRoleFADTO, Map<String, FeatureActionLimitsDTO> contractFeatureActionDTOMap,
            List<ContractAccountsDTO> corecustomerAccounts, String amsRoleId, Map<String, Object> headerMap)
            throws JsonMappingException, JsonProcessingException, ApplicationException {

        DBXResult response = new DBXResult();
        JsonObject dbxResultJsonResponse = new JsonObject();

        /**
         * Feature and actions information
         */
        Map<String, JsonObject> featureInfo = serviceDefinitionRoleFADTO.getFeatureInfo();
        Map<String, JsonObject> actionsInfo = serviceDefinitionRoleFADTO.getActionsInfo();
        Map<String, Map<String, Map<String, String>>> monetaryActionLimits = serviceDefinitionRoleFADTO
                .getMonetaryActionLimits();
        /**
         * Global level permission array declarations
         */

        Map<String, Set<String>> globalLevelPermissionsSD = serviceDefinitionRoleFADTO
                .getGlobalLevelPermissions();
        Map<String, Set<String>> accountLevelPermissionsSD = serviceDefinitionRoleFADTO
                .getAccountLevelPermissions();
        Map<String, Set<String>> transactionLimitsSD = serviceDefinitionRoleFADTO
        		.getTransactionLimits();
        boolean hasContract = (contractFeatureActionDTOMap != null 
        		&& !contractFeatureActionDTOMap.isEmpty());

        /**
         * Forming global level permissions json object
         */
        FeatureActionLimitsDTO contractFeatureActionDTO = null;
        Map<String, Set<String>> globalPermissionsForContract = null;

        JsonObject customerLevelJsonObject = new JsonObject();
        JsonArray globalFeatureActionsJsonArray = new JsonArray();
        
        if(hasContract) {
        	contractFeatureActionDTO = contractFeatureActionDTOMap.get(coreCustomerId);
            globalPermissionsForContract = contractFeatureActionDTO
                    .getGlobalLevelPermissions();
        }
        
        /** iterating over global level permissions of SD, RoleId and FI **/
        for (Entry<String, Set<String>> featureActionEntry : globalLevelPermissionsSD.entrySet()) {
        	String featureId = featureActionEntry.getKey();

            JsonArray actionsJsonArray = new JsonArray();
            for (String actionId : featureActionEntry.getValue()) {
                if (hasContract) {
                    if ( globalPermissionsForContract.get(featureId) != null &&
                    		globalPermissionsForContract.get(featureId).contains(actionId)) {
                    	actionsJsonArray.add(actionsInfo.get(actionId));
                    }
                } else {
                	actionsJsonArray.add(actionsInfo.get(actionId));
                }
            }
            if (actionsJsonArray.size() > 0) {
            	JsonObject featureJsonObject = featureInfo.get(featureId).deepCopy();
                featureJsonObject.add("permissions", actionsJsonArray);
                globalFeatureActionsJsonArray.add(featureJsonObject);
            }
        }

        customerLevelJsonObject.add("features", globalFeatureActionsJsonArray);
        customerLevelJsonObject.addProperty("serviceDefinitionId", serviceDefinitionId);
        customerLevelJsonObject.addProperty("roleId", roleId);
        customerLevelJsonObject.addProperty("coreCustomerId", coreCustomerId);
        dbxResultJsonResponse.add("globalLevelPermissions", customerLevelJsonObject);

        diagnostic.prepareDebug("global permissions done").log();

        /**
         * Forming account level permissions
         */
        Map<String, Map<String, Set<String>>> coreCustomerAccountLevelPermissions = null;
        if(hasContract) {
        	coreCustomerAccountLevelPermissions = 
            		contractFeatureActionDTO.getAsscoiatedAccountActions();
        }
        

        JsonObject accountlevelJsonObject = new JsonObject();
        JsonArray coreCustomerAccountLevelJsonArray = new JsonArray();
        boolean isProductActionsIntersactionEnabled = FeatureConfiguration
        		.isProductSpecificFeatueEnabled();
        boolean isAMSRoleIntersectionEnabled = FeatureConfiguration
        		.isAMSRoleFeatureEnabled();
        
        JsonObject accountJsonObject = null;
        Map<String,Map<String, Set<String>>> productRefPermissions = null;
        if(isProductActionsIntersactionEnabled) {
        	Set<String> productIds = new HashSet<>();
        	for (JsonElement accountElement : productArray) {
        		JsonObject accountObject = accountElement.getAsJsonObject();

        		if(corecustomerAccounts != null && !corecustomerAccounts.isEmpty()) {
        			accountJsonObject = retrieveAccountDetails(accountObject.get("accountId").getAsString()
                		, corecustomerAccounts);
        			accountObject.addProperty("productId", 
        					JSONUtil.getString(accountJsonObject, "productId"));
                }
        		productIds.add(JSONUtil.getString(accountObject, "productId"));
        	}
        	productRefPermissions = getProductLevelPermissions(productIds);
        	
        }
        
        String arrangementRoleId = null;
        String productId = null;
        String accountId = null;
        Map<String, Set<String>> amsRolePermissions = null;
        
        /* iterating over products array received from request payload*/
        for (JsonElement accountElement : productArray) {
            JsonObject accountObject = accountElement.getAsJsonObject();
            accountJsonObject = accountObject.deepCopy();
            
            accountId = JSONUtil.getString(accountObject, "accountId");
            productId = JSONUtil.getString(accountObject, "productId");
            
            if(isAMSRoleIntersectionEnabled) {
            	arrangementRoleId = JSONUtil.getString(accountObject, "arrangementRoleId");
            	amsRolePermissions = new HashMap<>();
            	amsRolePermissions = getArrangementRolePermissions(arrangementRoleId, amsRoleId, headerMap);
            }

            JsonArray accountLevelFeatureActionsJsonArray = new JsonArray();

            for (Entry<String, Set<String>> featureActionEntry : accountLevelPermissionsSD.entrySet()) {
                JsonArray actionsJsonArray = new JsonArray();
                for (String actionId : featureActionEntry.getValue()) {
                    /* intersection with contract action and product and ams */
                    if (coreCustomerAccountLevelPermissions != null) {
                        if (coreCustomerAccountLevelPermissions.containsKey(accountId)
                        		&& coreCustomerAccountLevelPermissions.get(accountId).containsKey(featureActionEntry.getKey())
                        		&& coreCustomerAccountLevelPermissions.get(accountId).get(featureActionEntry.getKey()).contains(actionId)
                                && (!isProductActionsIntersactionEnabled || (productRefPermissions.get(productId).containsKey(featureActionEntry.getKey())
                                		&& productRefPermissions.get(productId).get(featureActionEntry.getKey()).contains(actionId)))
                                && (!isAMSRoleIntersectionEnabled || amsRolePermissions.get(arrangementRoleId).contains(actionId))) {
                            actionsJsonArray.add(actionsInfo.get(actionId));
                        }
                    } else {
                    	/* intersection with product and ams */
                    if ((!isProductActionsIntersactionEnabled || (productRefPermissions.get(productId).containsKey(featureActionEntry.getKey())
                    		&& productRefPermissions.get(productId).get(featureActionEntry.getKey()).contains(actionId)))
                        && (!isAMSRoleIntersectionEnabled || amsRolePermissions.get(arrangementRoleId).contains(actionId))) {
                    	actionsJsonArray.add(actionsInfo.get(actionId));
                    }
                }
                }

                if (actionsJsonArray.size() > 0) {
                	JsonObject featureJsonObject = featureInfo.get(featureActionEntry.getKey()).deepCopy();
                	featureJsonObject.add("permissions", actionsJsonArray);
                    accountLevelFeatureActionsJsonArray.add(featureJsonObject);
                }
            }

            accountJsonObject.add("features", accountLevelFeatureActionsJsonArray);
            coreCustomerAccountLevelJsonArray.add(accountJsonObject);
        }

        accountlevelJsonObject.add("accounts", coreCustomerAccountLevelJsonArray);
        accountlevelJsonObject.addProperty("serviceDefinitionId", serviceDefinitionId);
        accountlevelJsonObject.addProperty("roleId", roleId);
        accountlevelJsonObject.addProperty("coreCustomerId", coreCustomerId);
        dbxResultJsonResponse.add("accountLevelPermissions", accountlevelJsonObject);

        diagnostic.prepareDebug("account level permissions done").log();

        /**
         * Forming transaction limits
         */

        Map<String, Map<String, Map<String, String>>> coreCustomerTransactionLimits = null;
        
        if(hasContract) {
        	coreCustomerTransactionLimits = contractFeatureActionDTO
                    .getMonetaryActionLimits();
        }
        
        JsonObject limitJsonObject = new JsonObject();
        JsonArray coreCustomerTransactionLimitsJsonArray = new JsonArray();
        for (Entry<String, Set<String>> featureActionEntry : transactionLimitsSD.entrySet()) {
        	
        	if(coreCustomerTransactionLimits != null && 
        			!coreCustomerTransactionLimits.containsKey(featureActionEntry.getKey())) {
            		continue;
            }
        	
        	JsonObject featureJsonObject = featureInfo.get(featureActionEntry.getKey()).deepCopy();
        	Map<String, String> limitsMap = null;
        	Map<String, String> limitsMapSD = null;
            for (String actionId : featureActionEntry.getValue()) {
            	JsonArray limits = new JsonArray();
                if (coreCustomerTransactionLimits != null) {
                    if (coreCustomerTransactionLimits.get(featureActionEntry.getKey())
                            .containsKey(actionId)) {
                        for (Entry<String, JsonElement> entrySet : actionsInfo.get(actionId).entrySet()) {
                            featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
                        }
                        limitsMap = coreCustomerTransactionLimits.get(featureActionEntry.getKey()).get(actionId);
                        limitsMapSD = monetaryActionLimits.get(featureActionEntry.getKey()).get(actionId);
                        for (Entry<String, String> limitsEntry : limitsMap.entrySet()) {
                            JsonObject limitsJson = new JsonObject();
                            limitsJson.addProperty("id", limitsEntry.getKey());
                            limitsJson.addProperty("value", Double.valueOf(
                            		Math.min(Double.parseDouble(limitsMapSD.get(limitsEntry.getKey())),
                            		Double.parseDouble(limitsEntry.getValue()))));
                            limits.add(limitsJson);
                        }
                        featureJsonObject.add("limits", limits);
                    }
                } else {
                	for (Entry<String, JsonElement> entrySet : actionsInfo.get(actionId).entrySet()) {
                        featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
                    }
                	
                    limitsMap = monetaryActionLimits.get(featureActionEntry.getKey()).get(actionId);
                    for (Entry<String, String> limitsEntry : limitsMap.entrySet()) {
                        JsonObject limitsJson = new JsonObject();
                        limitsJson.addProperty("id", limitsEntry.getKey());
                        limitsJson.addProperty("value", limitsEntry.getValue());
                        limits.add(limitsJson);
                    }
                    featureJsonObject.add("limits", limits);
                }
            }
            coreCustomerTransactionLimitsJsonArray.add(featureJsonObject);
        }
        limitJsonObject.add("featurePermissions", coreCustomerTransactionLimitsJsonArray);
        limitJsonObject.addProperty("serviceDefinitionId", serviceDefinitionId);
        limitJsonObject.addProperty("roleId", roleId);
        limitJsonObject.addProperty("coreCustomerId", coreCustomerId);
        dbxResultJsonResponse.add("transactionLimits", limitJsonObject);
        diagnostic.prepareDebug("transactionLimits permissions done").log();
        response.setResponse(dbxResultJsonResponse);
        return response;

    }
    
    @Override
	public Result getProductLevelPermissions(String productRef) throws ApplicationException {
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("productRef", productRef);
		return AdminUtil.invokeAPI(inputParams, URLConstants.GET_PRODUCTID_PERMISSIONS);
	}
    
    @Override
    public boolean updateContractStatus(String contractId, String statusId, String companyLegalUnit, Map<String, Object> headerMap)
            throws ApplicationException {
        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(statusId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10380);
        }
        try {
            Map<String, Object> inputParams = new HashMap<>();

            inputParams.put("id", contractId);
            inputParams.put("statusId", statusId);
            inputParams.put("companyLegalUnit", companyLegalUnit);

            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CONTRACT_UPDATE);

            if (JSONUtil.isJsonNotNull(contractJson)
                    && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
                    && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
                JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                ContractDTO dto = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                        ContractDTO.class, false);
                if (null == dto || StringUtils.isBlank(dto.getId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10381);
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10381);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }
        return true;

    }

}