package com.temenos.dbx.product.contract.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.businessdelegate.api.DependentActionsBusinessDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
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
    private static final String CONTRACT_ID_DB = "id";
    private static final String CONTRACT_NAME_DB = "name";

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
    public ContractDTO getContract(String contractId, Map<String, Object> headersMap) throws ApplicationException {
        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        Map<String, Object> inputParams = new HashMap<>();
        ContractDTO contractDTO = null;
        String filter = CONTRACT_ID_DB + DBPUtilitiesConstants.EQUAL + contractId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CONTRACT_GET);

        if (null != contractJson && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
                && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
            JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
            JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
            contractDTO = (ContractDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                    ContractDTO.class, false);

            if (null == contractDTO || StringUtils.isBlank(contractDTO.getId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10361);
            }

            ServiceDefinitionDTO dto = new ServiceDefinitionDTO();
            dto.setId(contractDTO.getServicedefinitionId());
            ServiceDefinitionBusinessDelegate serviceBD =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
            dto = serviceBD.getServiceDefinitionDetails(dto, headersMap);

            contractDTO.setServicedefinitionName(dto.getName());

        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_10361);
        }

        return contractDTO;
    }

    @Override
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
        Map<String, JsonObject> featureInfo = new HashMap<>();
        Map<String, JsonObject> actionsInfo = new HashMap<>();
        Map<String, Map<String, Map<String, String>>> monetaryActionLimits = new HashMap<>();
        for (JsonElement jsonelement : jsonarray) {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
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
            Set<String> actionsSet = new HashSet<>();
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
            }
        }
        dto.setMonetaryActionLimits(monetaryActionLimits);
        dto.setActionsInfo(actionsInfo);
        dto.setFeatureInfo(featureInfo);
        dto.setFeatureaction(featureaction);
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
            return response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();

        }

        throw new ApplicationException(ErrorCodeEnum.ERR_10375);

    }

    @Override
    public List<ContractDTO> getListOfContractsByStatus(String statusId, Map<String, Object> headerMap)
            throws ApplicationException {
        List<ContractDTO> dtoList = new ArrayList<>();
        try {
            Map<String, Object> inputParams = new HashMap<>();
            String filter = "statusId" + DBPUtilitiesConstants.EQUAL + statusId;

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
            inputParams.put("companyLegalUnit","GB0010001");

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
        if (StringUtils.isBlank(dto.getContractId())) {
            return null;
        }

        List<ContractActionLimitsDTO> resultContractActionLimits = new ArrayList<>();
        try {
            Map<String, Object> inputParams = new HashMap<>();

            inputParams.put(DBPUtilitiesConstants.FILTER,
                    CONTRACT_ID + DBPUtilitiesConstants.EQUAL + dto.getContractId());

            JsonObject contractActionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTACTIONLIMIT_GET);

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

}
