package com.temenos.dbx.eum.product.contract.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.FeatureConfiguration;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractAddressBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractActionLimitsDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ContractBusinessDelegateImpl implements ContractBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private final static String CONTRACT_ID_DB = "contractId";

    @Override
    public ContractDTO createContract(ContractDTO inputContractDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractDTO resultContractDTO = null;
        if (null == inputContractDTO || StringUtils.isBlank(inputContractDTO.getName())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10350);
        }

        try {
            Map<String, Object> inputParams = DTOUtils.getParameterMap(inputContractDTO, false);
            HelperMethods.removeNullValues(inputParams);
            JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACT_CREATE);
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
                	diagnostic.prepareDebug("create contract service doest have contractId").log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10351);
                }
            } else {
            	diagnostic.prepareDebug("Incorrect response from create contract service").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10351);
            }
        } catch (Exception e) {
        	alert.prepareError("Exception while creating contract", e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10351);
        }
        return resultContractDTO;
    }

    @Override
    public DBXResult searchContracts(Map<String, String> inputParams, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            response = backendDelegate.searchContracts(inputParams, headersMap);
            JsonArray records = new JsonArray();
            if (response != null && response.getResponse() != null) {
                records = (JsonArray) response.getResponse();
            }
            Map<String, JsonObject> recordsMap = new HashMap<>();
            if (records.size() > 0) {
                for (JsonElement jsonElement : records) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonObject contractJson = new JsonObject();
                    JsonArray coreCustomerJsonArray = new JsonArray();
                    if (recordsMap.containsKey(JSONUtil.getString(jsonObject, "contractId"))) {
                        contractJson = recordsMap.get(JSONUtil.getString(jsonObject, "contractId"));
                        coreCustomerJsonArray = (JSONUtil.hasKey(contractJson, "customers")
                                && contractJson.get("customers").getAsJsonArray().size() > 0)
                                        ? contractJson.get("customers").getAsJsonArray()
                                        : new JsonArray();
                    }
                    contractJson.addProperty("contractId", JSONUtil.getString(jsonObject, "contractId"));
                    contractJson.addProperty("contractName", JSONUtil.getString(jsonObject, "contractName"));
                    contractJson.addProperty("serviceDefinitionId",
                            JSONUtil.getString(jsonObject, "serviceDefinitionId"));
                    contractJson.addProperty("serviceDefinitionName",
                            JSONUtil.getString(jsonObject, "serviceDefinitionName"));
                    contractJson.addProperty("email", JSONUtil.getString(jsonObject, "email"));
                    contractJson.addProperty("legalEntityId", JSONUtil.getString(jsonObject, "legalEntityId"));

                    JsonObject coreCustomerJsonObject = new JsonObject();
                    coreCustomerJsonObject.addProperty("coreCustomerName",
                            JSONUtil.getString(jsonObject, "coreCustomerName"));
                    coreCustomerJsonArray.add(coreCustomerJsonObject);

                    contractJson.add("contractCustomers", coreCustomerJsonArray);

                    recordsMap.put(JSONUtil.getString(jsonObject, "contractId"), contractJson);
                }
            }
            JsonObject contract = new JsonObject();
            JsonArray responseArray = new JsonArray();
            for (Map.Entry<String, JsonObject> record : recordsMap.entrySet()) {
                responseArray.add(record.getValue());
            }
            contract.add("contracts", responseArray);
            response = new DBXResult();
            response.setResponse(contract);
        } catch (ApplicationException e) {
            alert.prepareError("ContractBusinessDelegateImpl : Exception occured while searching contracts").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "ContractBusinessDelegateImpl : Exception occured while searching contracts" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10766);
        }
        return response;
    }

    @Override
    public ContractDTO getContractDetails(String contractId, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException {

        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        ContractBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
        return backendDelegate.getContract(contractId, legalEntityId, headersMap);

    }

    @Override
    public ContractDTO getCompleteContractDetails(String contractId, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException {

        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        ContractBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);

        ContractCoreCustomerBusinessDelegate contractCoreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        ContractDTO contractDTO = businessDelegate.getContractDetails(contractId, legalEntityId, headersMap);

        List<ContractCoreCustomersDTO> contractCustomers =
                contractCoreCustomerBD.getContractCoreCustomers(contractId, true, true, headersMap);

        contractDTO.setContractCustomers(contractCustomers);

        return contractDTO;
    }

    @Override
    public DBXResult validateCustomersForContract(JsonArray customers, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult result = new DBXResult();
        try {
            ContractBackendDelegate backenddelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            result = backenddelegate.validateCustomersForContract(customers, headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("ContractBusinessDelegateImpl : Exception occured while validating the contract customers "
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("ContractBusinessDelegateImpl : Exception occured while validating the contract customers "
                    + e.getMessage()).log();
        }
        return result;
    }

    @Override
    public DBXResult getContractFeatureActionLimits(ContractCoreCustomersDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult dbxResult = new DBXResult();
        List<ContractCoreCustomersDTO> contractCustomers = new ArrayList<>();
        FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO = new FeatureActionLimitsDTO();
        String serviceDefinitionId = "";
        String contractId = "";
        String roleId = "";
        String coreCustomerId = "";
        String userId = "";
        try {
            ContractCoreCustomerBusinessDelegate contractCustomerBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
            contractCustomers =
            	     contractCustomerBusinessDelegate.getContractCoreCustomers(dto.getContractId(), false, false,
                            headersMap);
            ContractBusinessDelegate contractBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            ContractDTO contractDTO = contractBusinessDelegate.getContractDetails(dto.getContractId(), dto.getCompanyLegalUnit(), headersMap);
            serviceDefinitionId = contractDTO.getServicedefinitionId();
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            serviceDefinitionFeatureActionDTO =
                    backendDelegate.getRestrictiveFeatureActionLimits(
                            serviceDefinitionId, contractId, roleId, coreCustomerId, userId,
                            headersMap, true, "");
            JsonObject contractFeatureActionLimits = new JsonObject();
            JsonArray coreCustomerFeatures = new JsonArray();
            JsonArray coreCustomerLimits = new JsonArray();
            for (ContractCoreCustomersDTO coreCustomerDTO : contractCustomers) {
                FeatureActionLimitsDTO coreCustomerFeatureActionDTO =
                        backendDelegate.getRestrictiveFeatureActionLimits("", "", "",
                                coreCustomerDTO.getCoreCustomerId(), userId, headersMap, true, "");
                JsonObject response = processContractFeatureActionLimits(serviceDefinitionFeatureActionDTO,
                        coreCustomerFeatureActionDTO);
                JsonObject contractCustomer = addContractCustomerDetails(coreCustomerDTO);
                contractCustomer.add("contractCustomerFeatures", response.get("features").getAsJsonArray());
                coreCustomerFeatures.add(contractCustomer);
                contractCustomer = new JsonObject();
                contractCustomer = addContractCustomerDetails(coreCustomerDTO);
                contractCustomer.add("contractCustomerLimits", response.get("limits").getAsJsonArray());
                coreCustomerLimits.add(contractCustomer);
            }
            contractFeatureActionLimits.add("features", coreCustomerFeatures);
            contractFeatureActionLimits.add("limits", coreCustomerLimits);
            if (contractFeatureActionLimits.isJsonObject())
                dbxResult.setResponse(contractFeatureActionLimits);
        } catch (ApplicationException e) {
            alert.prepareError(
                    "CotractBusinessDelegateImpl : Exception occured while fetching the Contract feature actions and limits"
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "CotractBusinessDelegateImpl : Exception occured while fetching the Contract feature actions and limits"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10775);
        }
        return dbxResult;
    }

    private JsonObject addContractCustomerDetails(ContractCoreCustomersDTO coreCustomerDTO) {
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

    private JsonObject processContractFeatureActionLimits(FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO,
            FeatureActionLimitsDTO coreCustomerFeatureActionDTO) {
        JsonObject response = new JsonObject();
        Map<String, Set<String>> serviceDefinitionFeatureaction = serviceDefinitionFeatureActionDTO.getFeatureaction();
        Map<String, JsonObject> serviceDefinitionFeatureInfo = serviceDefinitionFeatureActionDTO.getFeatureInfo();
        Map<String, JsonObject> serviceDefinitionActionsInfo = serviceDefinitionFeatureActionDTO.getActionsInfo();
        Map<String, Map<String, Map<String, String>>> serviceDefinitionMonetaryActionLimits =
                serviceDefinitionFeatureActionDTO.getMonetaryActionLimits();

        Map<String, Set<String>> coreCustomerFeatureaction = coreCustomerFeatureActionDTO.getFeatureaction();
        Map<String, Map<String, Map<String, String>>> coreCustomerMonetaryActionLimits =
                coreCustomerFeatureActionDTO.getMonetaryActionLimits();

        JsonArray featuresActionsJsonArray = new JsonArray();
        JsonArray featureActionLimitsJsonArray = new JsonArray();
        for (Entry<String, Set<String>> featureActionEntry : serviceDefinitionFeatureaction
                .entrySet()) {
            JsonObject featureJsonObject = new JsonObject();
            for (Entry<String, JsonElement> entrySet : serviceDefinitionFeatureInfo.get(featureActionEntry.getKey())
                    .entrySet()) {
                featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
            }
            JsonArray actionsJsonArray = new JsonArray();
            for (String actionId : featureActionEntry.getValue()) {
                JsonObject actionJsonObject = new JsonObject();
                for (Entry<String, JsonElement> entrySet : serviceDefinitionActionsInfo.get(actionId)
                        .entrySet()) {
                    actionJsonObject.add(entrySet.getKey(), entrySet.getValue());
                }
                actionJsonObject.addProperty("isEnabled",
                        (coreCustomerFeatureaction.containsKey(featureActionEntry.getKey())
                                && coreCustomerFeatureaction.get(featureActionEntry.getKey()) != null
                                && coreCustomerFeatureaction.get(featureActionEntry.getKey()).contains(actionId)) ? true
                                        : false);
                if(coreCustomerFeatureActionDTO.getNewFeatureAction().containsKey(featureActionEntry.getKey()) &&
                		coreCustomerFeatureActionDTO.getNewFeatureAction().get(featureActionEntry.getKey()).contains(actionId)) {
                	actionJsonObject.addProperty(InfinityConstants.isNewAction, true);
                }
                else {
                	actionJsonObject.addProperty(InfinityConstants.isNewAction, false);
                }
                actionsJsonArray.add(actionJsonObject);
            }
            featureJsonObject.add("actions", actionsJsonArray);
            featuresActionsJsonArray.add(featureJsonObject);
        }
        for (Entry<String, Map<String, Map<String, String>>> featureActionlimitsEntry : serviceDefinitionMonetaryActionLimits
                .entrySet()) {
            JsonObject featureJsonObject = new JsonObject();
            for (Entry<String, JsonElement> entrySet : serviceDefinitionFeatureInfo
                    .get(featureActionlimitsEntry.getKey())
                    .entrySet()) {
                featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
            }
            JsonArray actionsJsonArray = new JsonArray();
            for (Entry<String, Map<String, String>> actionLimitsEntry : featureActionlimitsEntry.getValue()
                    .entrySet()) {
                JsonObject actionJsonObject = new JsonObject();
                for (Entry<String, JsonElement> entrySet : serviceDefinitionActionsInfo.get(actionLimitsEntry.getKey())
                        .entrySet()) {
                    actionJsonObject.add(entrySet.getKey(), entrySet.getValue());
                }
                boolean isActionEnabled = (coreCustomerFeatureaction.containsKey(featureActionlimitsEntry.getKey())
                        && coreCustomerFeatureaction.get(featureActionlimitsEntry.getKey()) != null
                        && coreCustomerFeatureaction.get(featureActionlimitsEntry.getKey())
                                .contains(actionLimitsEntry.getKey()));
                actionJsonObject.addProperty("isEnabled", String.valueOf(isActionEnabled));
                if(coreCustomerFeatureActionDTO.getNewFeatureAction().containsKey(featureActionlimitsEntry.getKey()) &&
                		coreCustomerFeatureActionDTO.getNewFeatureAction().get(featureActionlimitsEntry.getKey()).contains(actionLimitsEntry.getKey())) {
                	actionJsonObject.addProperty(InfinityConstants.isNewAction, true);
                }
                else {
                	actionJsonObject.addProperty(InfinityConstants.isNewAction, false);
                }
                JsonArray limits = new JsonArray();
                for (Entry<String, String> limitsEntry : actionLimitsEntry.getValue().entrySet()) {
                    JsonObject json = new JsonObject();
                    json.addProperty("id", limitsEntry.getKey());
                    String value = limitsEntry.getValue();
                    if (isActionEnabled && coreCustomerMonetaryActionLimits.get(featureActionlimitsEntry.getKey())
                            .get(actionLimitsEntry.getKey()).containsKey(limitsEntry.getKey())) {
                        value = String.valueOf(Math.min(Double.valueOf(value),
                                Double.valueOf(coreCustomerMonetaryActionLimits.get(featureActionlimitsEntry.getKey())
                                        .get(actionLimitsEntry.getKey()).get(limitsEntry.getKey()))));
                    }
                    json.addProperty("value", value);
                    limits.add(json);
                }
                actionJsonObject.add("limits", limits);
                actionsJsonArray.add(actionJsonObject);
            }
            featureJsonObject.add("actions", actionsJsonArray);
            featureActionLimitsJsonArray.add(featureJsonObject);
        }
        response.add("features", featuresActionsJsonArray);
        response.add("limits", featureActionLimitsJsonArray);
        return response;
    }

    @Override
    public JsonArray getContractInfinityUsers(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            return backendDelegate.getContractInfinityUsers(contractId, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10375);
        }
    }

    @Override
    public List<ContractDTO> getListOfContractsByStatus(String statusId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException {
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            return backendDelegate.getListOfContractsByStatus(statusId, legalEntityId, headerMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10379);
        }
    }

    @Override
    public boolean updateContractStatus(String contractId, String statusId, Map<String, Object> headerMap)
            throws ApplicationException {
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            return backendDelegate.updateContractStatus(contractId, statusId, headerMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }
    }
    @Override
    public boolean updateContractStatus(ContractDTO contractDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            return backendDelegate.updateContractStatus(contractDTO, headerMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }
    }

    @Override
    public DBXResult getCoreCustomerGroupFeatureActionLimits(String coreCustomerRoleIdList,
            Map<String, Object> headersMap, String legalEntityId) throws ApplicationException {
        DBXResult dbxresult = new DBXResult();
        try {
            JsonArray coreCustomerRolesJsonArray = new JsonParser().parse(coreCustomerRoleIdList).getAsJsonArray();
            JsonObject featureActionLimits = new JsonObject();
            JsonArray features = new JsonArray();
            JsonArray limits = new JsonArray();
            for (JsonElement coreCustomerRoleJsonElement : coreCustomerRolesJsonArray) {
                String coreCustomerId =
                        JSONUtil.getString(coreCustomerRoleJsonElement.getAsJsonObject(), "coreCustomerId");
                String roleId = JSONUtil.getString(coreCustomerRoleJsonElement.getAsJsonObject(), "roleId");
                String serviceDefinitionId =
                        JSONUtil.getString(coreCustomerRoleJsonElement.getAsJsonObject(), "serviceDefinitionId");
                ContractBackendDelegate contractBackendDelegate =
                        DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
                FeatureActionLimitsDTO featureActionLimitsDTO =
                        contractBackendDelegate.getRestrictiveFeatureActionLimits(
                                serviceDefinitionId, "", roleId, coreCustomerId, "", headersMap, true, "",legalEntityId);
                JsonObject coreCustomerFeatures = new JsonObject();
                JsonObject coreCustomerLimits = new JsonObject();
                coreCustomerFeatures.addProperty("coreCustomerId", coreCustomerId);
                coreCustomerFeatures.add("coreCustomerFeatures",
                        processFeatures(featureActionLimitsDTO.getFeatureaction(),
                                featureActionLimitsDTO.getFeatureInfo(), featureActionLimitsDTO.getActionsInfo()));
                coreCustomerLimits.addProperty("coreCustomerId", coreCustomerId);
                coreCustomerLimits.add("coreCustomerLimits",
                        processLimits(featureActionLimitsDTO.getMonetaryActionLimits(),
                                featureActionLimitsDTO.getFeatureInfo(), featureActionLimitsDTO.getActionsInfo()));
                features.add(coreCustomerFeatures);
                limits.add(coreCustomerLimits);
            }
            featureActionLimits.add("features", features);
            featureActionLimits.add("limits", limits);
            dbxresult.setResponse(featureActionLimits);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10777);
        }
        return dbxresult;
    }

    private JsonArray processFeatures(Map<String, Set<String>> featureaction, Map<String, JsonObject> featureInfo,
            Map<String, JsonObject> actionsInfo) {
        JsonArray featuresJsonArray = new JsonArray();
        for (Entry<String, Set<String>> featureActionEntry : featureaction.entrySet()) {
            JsonObject featureJsonObject = new JsonObject();
            for (Entry<String, JsonElement> entrySet : featureInfo.get(featureActionEntry.getKey())
                    .entrySet()) {
                featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
            }
            JsonArray actionsJsonArray = new JsonArray();
            for (String actionId : featureActionEntry.getValue()) {
                JsonObject actionJsonObject = new JsonObject();
                for (Entry<String, JsonElement> entrySet : actionsInfo.get(actionId)
                        .entrySet()) {
                    actionJsonObject.add(entrySet.getKey(), entrySet.getValue());
                }
                actionsJsonArray.add(actionJsonObject);
            }
            featureJsonObject.add("actions", actionsJsonArray);
            featuresJsonArray.add(featureJsonObject);
        }
        return featuresJsonArray;
    }

    private JsonArray processLimits(Map<String, Map<String, Map<String, String>>> monetaryActionLimits,
            Map<String, JsonObject> featureInfo,
            Map<String, JsonObject> actionsInfo) {
        JsonArray limitsJsonArray = new JsonArray();
        for (Entry<String, Map<String, Map<String, String>>> monetaryFeatureActionEntry : monetaryActionLimits
                .entrySet()) {
            JsonObject featureJsonObject = new JsonObject();
            for (Entry<String, JsonElement> entrySet : featureInfo.get(monetaryFeatureActionEntry.getKey())
                    .entrySet()) {
                featureJsonObject.add(entrySet.getKey(), entrySet.getValue());
            }
            JsonArray actionsJsonArray = new JsonArray();
            for (Entry<String, Map<String, String>> actionsEntry : monetaryFeatureActionEntry.getValue().entrySet()) {
                JsonObject actionJsonObject = new JsonObject();
                for (Entry<String, JsonElement> entrySet : actionsInfo.get(actionsEntry.getKey())
                        .entrySet()) {
                    actionJsonObject.add(entrySet.getKey(), entrySet.getValue());
                }
                JsonArray limits = new JsonArray();
                for (Entry<String, String> limitsEntry : (monetaryActionLimits.get(monetaryFeatureActionEntry.getKey()))
                        .get(actionsEntry.getKey()).entrySet()) {
                    JsonObject limit = new JsonObject();
                    limit.addProperty("id", limitsEntry.getKey());
                    limit.addProperty("value", limitsEntry.getValue());
                    limits.add(limit);
                }
                actionJsonObject.add("limits", limits);
                actionsJsonArray.add(actionJsonObject);
            }
            featureJsonObject.add("actions", actionsJsonArray);
            limitsJsonArray.add(featureJsonObject);
        }
        return limitsJsonArray;
    }

    private ContractDTO getContractDetailsForEnrollment(String contractId, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);

        ContractAddressBusinessDelegate contractAddressBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAddressBusinessDelegate.class);

        ContractCoreCustomerBusinessDelegate contractCoreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        ContractDTO contractDTO = businessDelegate.getContractDetails(contractId, legalEntityId,headersMap);

        List<AddressDTO> address = contractAddressBD.getContractAddress(contractId, headersMap);

        contractDTO.setAddress(address);

        List<ContractCoreCustomersDTO> contractCustomers =
                contractCoreCustomerBD.getContractCoreCustomers(contractId, false, true, headersMap);

        contractDTO.setContractCustomers(contractCustomers);

        return contractDTO;
    }

    @Override
    public DBXResult getCoreCustomerBasedContractDetails(String coreCustomerId, Map<String, Object> headersMap, String legalEntityId)
            throws ApplicationException {
        DBXResult dbxresult = new DBXResult();
        String contractId = "";
        ContractDTO contractDTO = new ContractDTO();
        List<ContractDTO> dtoList = new ArrayList<>();
        try {
            ContractCoreCustomerBusinessDelegate contractCoreCustomerBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
            ContractCoreCustomersDTO contractCoreCustomerDTO = new ContractCoreCustomersDTO();
            contractCoreCustomerDTO.setCoreCustomerId(coreCustomerId);
            contractCoreCustomerDTO.setCompanyLegalUnit(legalEntityId);
            contractCoreCustomerDTO =
                    contractCoreCustomerBusinessDelegate.getContractCoreCustomers(contractCoreCustomerDTO, headersMap);
            if (contractCoreCustomerDTO != null) {
                contractId = contractCoreCustomerDTO.getContractId();
                contractDTO = getContractDetailsForEnrollment(contractId, legalEntityId, headersMap);
                dtoList.add(contractDTO);
            }
            dbxresult.setResponse(dtoList);
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractBusinessDelegateImpl : Exception occured while fetching the core customer contract details").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10781);
        }
        return dbxresult;
    }

    @Override
    public DBXResult getRelativeCoreCustomerBasedContractDetails(Map<String, String> configurations,
    		String legalEntityId, String coreCustomerId, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult dbxresult = new DBXResult();
        try {
            List<ContractDTO> dtoList = new ArrayList<>();
            CoreCustomerBusinessDelegate coreCustomerBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
            ContractCoreCustomerBusinessDelegate contractCoreCustomerBusinessDelegate =
                    DBPAPIAbstractFactoryImpl
                            .getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
            MembershipDTO membershipDTO = new MembershipDTO();
            membershipDTO.setId(coreCustomerId);
            membershipDTO.setCompanyLegalUnit(legalEntityId);
            DBXResult relativeCustomers =
                    coreCustomerBusinessDelegate.getCoreRelativeCustomers(configurations, membershipDTO, headersMap);
            if (relativeCustomers != null && relativeCustomers.getResponse() != null) {
                JsonArray jsonarray = ((JsonObject) relativeCustomers.getResponse())
                        .get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray();
                for (JsonElement jsonelement : jsonarray) {
                    if ("true".equalsIgnoreCase(JSONUtil.getString(jsonelement.getAsJsonObject(), "isAssociated"))) {
                        ContractCoreCustomersDTO contractCoreCustomerDTO = new ContractCoreCustomersDTO();
                        contractCoreCustomerDTO
                                .setCoreCustomerId(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
                        contractCoreCustomerDTO =
                                contractCoreCustomerBusinessDelegate.getContractCoreCustomers(contractCoreCustomerDTO,
                                        headersMap);
                        ContractDTO contractDTO = null;
                        if (contractCoreCustomerDTO != null) {
                            contractDTO = new ContractDTO();
                            contractDTO = getContractDetailsForEnrollment(
                                    contractCoreCustomerDTO.getContractId(), contractCoreCustomerDTO.getCompanyLegalUnit(), headersMap);
                        }
                        if (contractDTO != null) {
                            dtoList.add(contractDTO);
                        }
                    }
                }
            }
            dbxresult.setResponse(dtoList);
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractBusinessDelegateImpl : Exception occured while fetching the relative core customer contract details").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10782);
        }
        return dbxresult;
    }

    @Override
    public List<ContractAccountsDTO> getContractAccounts(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        List<ContractAccountsDTO> dtoList = new ArrayList<>();
        try {
            ContractAccountsBusinessDelegate contractAccountsBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAccountsBusinessDelegate.class);
            dtoList = contractAccountsBusinessDelegate.getContractCustomerAccounts(contractId, "", headersMap);
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractBusinessDelegateImpl : Exception occured while fetching the contract accounts "
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "ContractBusinessDelegateImpl : Exception occured while fetching the contract accounts "
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10784);
        }
        return dtoList;
    }

    @Override
    public ContractDTO updateContract(ContractDTO inputContractDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            return backendDelegate.updateContract(inputContractDTO, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10393);
        }
    }

    @Override
    public ContractDTO getContractDetails(ContractDTO contractDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
        return backendDelegate.getContractDetails(contractDTO, headersMap);

    }

    @Override
    public List<ContractActionLimitsDTO> getContractActions(ContractActionLimitsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
        return backendDelegate.getContractActions(dto, headersMap);
    }
    
    
    public DBXResult getCoreCustomerProductRolesFeatureActionLimits(String coreCustomerId, String contractId, 
    		String legalEntityId, String serviceDefinitionId,
            String roleId, JsonArray productArray, String amsRoleId,
            Map<String, Object> headerMap) throws ApplicationException {
        FeatureActionLimitsDTO serviceDefRoleFADTO = null;
        Map<String, FeatureActionLimitsDTO> contractFeatureActionDTOMap = null;
        List<ContractAccountsDTO> corecustomerAccounts = null;
        try {
            ContractAccountsBusinessDelegate contractAccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ContractAccountsBusinessDelegate.class);
            
            ContractBackendDelegate contractBackendDelegate = (ContractBackendDelegate) DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(ContractBackendDelegate.class);
            
            serviceDefRoleFADTO =
                    contractBackendDelegate.getRestrictiveFeatureActionLimits(serviceDefinitionId,
                            "", roleId, "", "", headerMap, true, "", legalEntityId);

            /* coreCustomerId null for new user enrollment from customer management */
            if(StringUtils.isNotBlank(coreCustomerId)
            		&& StringUtils.isNotBlank(contractId)) {
            	
            	corecustomerAccounts = contractAccountsBusinessDelegate
                        .getContractCustomerAccounts(contractId, coreCustomerId, headerMap);
                contractFeatureActionDTOMap = getContractActions(contractId, coreCustomerId,
                        headerMap);
            }

            return contractBackendDelegate.getNormalizedProductArrangementFeatureActionLimits(coreCustomerId,
                    serviceDefinitionId, roleId, productArray, serviceDefRoleFADTO,
                    contractFeatureActionDTOMap, corecustomerAccounts, amsRoleId, headerMap);

        } catch (Exception e) {
        	alert.prepareError("Exception caught : ", e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_29039);
        }
    }

    @Override
    public DBXResult createContractActionLimit(JsonObject contractActionObj, String contractId,
            String legalEntityId, Map<String, Object> headerMap) throws ApplicationException {
        ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractBackendDelegate.class);
        return backendDelegate.createContractActionLimit(contractActionObj, contractId, legalEntityId, headerMap);

    }

    @Override
    public DBXResult getContractFeatureActionLimitsAtProductLevel(ContractCoreCustomersDTO dto,
            Map<String, Object> headersMap) throws ApplicationException {
        List<ContractCoreCustomersDTO> contractCustomers = new ArrayList<>();
        FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO = null;
        String serviceDefinitionId = "";
        String contractId = "";
        String roleId = "";
        String coreCustomerId = "";
        String userId = "";
        Map<String, List<ContractAccountsDTO>> coreCustomerAccounts = new HashMap<>();

        // Set<String> portfolioAccounts = new HashSet<>();
        try {
            ContractCoreCustomerBusinessDelegate contractCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
            ContractAccountsBusinessDelegate contractAccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ContractAccountsBusinessDelegate.class);
            ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(ContractBackendDelegate.class);

            /**
             * Map<productRef,Map<feature, Set<actions>>>
             */
            Map<String,Map<String, Set<String>>> productIdPermissions = new HashMap<>();

            /**
             * Fetching contract accounts
             */
            List<ContractAccountsDTO> contractAccounts = contractAccountsBusinessDelegate
                    .getContractCustomerAccounts(dto.getContractId(), null, headersMap);

            /**
             * Fetching contract customer details
             * 
             */
            contractCustomers = contractCustomerBusinessDelegate.getContractCoreCustomers(dto.getContractId(), false,
                    true, headersMap);

            Set<String> productIdList = new HashSet<>();
            List<ContractAccountsDTO> accounts = null;
            for (ContractAccountsDTO contractaccount : contractAccounts) {
                productIdList.add(contractaccount.getProductId());
                if (coreCustomerAccounts.containsKey(contractaccount.getCoreCustomerId())) {
                	accounts = coreCustomerAccounts.get(contractaccount.getCoreCustomerId());
                }else {
                	accounts = new ArrayList<>();
                }
                	
                /*
                 * if (StringUtils.isNotBlank(contractaccount.getPortfolioId())) { ContractAccountsDTO
                 * portfolioAccountDTO = new ContractAccountsDTO();
                 * portfolioAccountDTO.setAccountId(contractaccount.getPortfolioId());
                 * portfolioAccountDTO.setAccountName(contractaccount.getPortfolioName());
                 * accounts.add(portfolioAccountDTO); }
                 */
                accounts.add(contractaccount);
                coreCustomerAccounts.put(contractaccount.getCoreCustomerId(), accounts);
                // portfolioAccounts.add(contractaccount.getPortfolioId());
            }

            if(FeatureConfiguration.isProductSpecificFeatueEnabled()) {
            	productIdPermissions = contractBackendDelegate.getProductLevelPermissions(productIdList);
            }
            
            ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ContractBusinessDelegate.class);
            ContractDTO contractDTO = contractBusinessDelegate.getContractDetails(
            		dto.getContractId(), dto.getCompanyLegalUnit(), headersMap);
            serviceDefinitionId = contractDTO.getServicedefinitionId();
            contractId = contractDTO.getId();
            ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(ContractBackendDelegate.class);
            /* intersection of serviceDefinition and FI*/
            serviceDefinitionFeatureActionDTO = backendDelegate.getRestrictiveFeatureActionLimits(serviceDefinitionId,
                    contractId, roleId, coreCustomerId, userId, headersMap, true, "",dto.getCompanyLegalUnit());
            Map<String, FeatureActionLimitsDTO> contractFeatureActionDTO = getContractActions(contractId, null,
                    headersMap);

            return backendDelegate.getNormalizedContractFeatureActionLimits(coreCustomerAccounts, productIdPermissions,
                    contractCustomers, serviceDefinitionFeatureActionDTO, contractFeatureActionDTO);

        } catch (ApplicationException e) {
            alert.prepareError("Application exception thrown").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing data",e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10775);
        }
    }

    @Override
    public Map<String, FeatureActionLimitsDTO> getContractActions(String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        FeatureActionLimitsDTO contractFeatureActionsDTO = null;
        Map<String, FeatureActionLimitsDTO> coreCustcontractFADTOmap = new HashMap<>();
        try {
        	ContractActionLimitsDTO contractActionLimitDTO = new ContractActionLimitsDTO();
            contractActionLimitDTO.setContractId(contractId);
            contractActionLimitDTO.setCoreCustomerId(coreCustomerId);
            List<ContractActionLimitsDTO> contractActions = 
            		getContractActions(contractActionLimitDTO, headersMap);

            for (ContractActionLimitsDTO contractActionLimit : contractActions) {
            	
            	if(!coreCustcontractFADTOmap.containsKey(contractActionLimit.getCoreCustomerId())) {
            		coreCustcontractFADTOmap.put(contractActionLimit.getCoreCustomerId(),
            				new FeatureActionLimitsDTO());
            	}
            	contractFeatureActionsDTO = coreCustcontractFADTOmap.get(
            			contractActionLimit.getCoreCustomerId());
                /**
                 * Global level permissions if accountId and LimitTypeId are null
                 */
              /*  if (StringUtils.isBlank(contractActionLimit.getAccountId()) 
                		&& StringUtils.isBlank(contractActionLimit.getLimitTypeId())) {*/
                	contractFeatureActionsDTO.getGlobalLevelActions()
                			.add(contractActionLimit.getActionId());
                	Set<String> actionsSetForaFeature = contractFeatureActionsDTO.getCoreCustomerGlobalLevelPermissions()
                			.get(contractActionLimit.getFeatureId());
                	if(actionsSetForaFeature == null) {
                		actionsSetForaFeature = new HashSet<>();
                	}
                	actionsSetForaFeature.add(contractActionLimit.getActionId());
                	contractFeatureActionsDTO.getCoreCustomerGlobalLevelPermissions()
        					.put(contractActionLimit.getFeatureId(), actionsSetForaFeature);
              /*  }*/
                
                /*
                 * account level permissions if accountId is present
                 **/

                if (StringUtils.isNotBlank(contractActionLimit.getAccountId())) {
                	
                	contractFeatureActionsDTO.getAccountLevelActions()
                			.add(contractActionLimit.getActionId());
                	
                	if(contractFeatureActionsDTO
                			.getAsscoiatedAccountActions()
                			.get(contractActionLimit.getAccountId()) == null) {
                		contractFeatureActionsDTO
                			.getAsscoiatedAccountActions()
                			.put(contractActionLimit.getAccountId(), new HashMap<>());
                	}
                	Set<String> actionsSetForaFeatureAcc = contractFeatureActionsDTO
                			.getAsscoiatedAccountActions()
                			.get(contractActionLimit.getAccountId())
                			.get(contractActionLimit.getFeatureId());
                	if(actionsSetForaFeatureAcc == null) {
                		actionsSetForaFeatureAcc = new HashSet<>();
                	}
                	actionsSetForaFeatureAcc.add(contractActionLimit.getActionId());
                	contractFeatureActionsDTO.getAsscoiatedAccountActions()
        				.get(contractActionLimit.getAccountId())
						.put(contractActionLimit.getFeatureId(), actionsSetForaFeatureAcc);
                }

                /**
                 * transaction limits and monetary actions if LimitTypeId is present
                 */
                else if (StringUtils.isNotBlank(contractActionLimit.getLimitTypeId())) {
                	
                	contractFeatureActionsDTO.getMonetaryActions()
        					.add(contractActionLimit.getActionId());
                	Map<String, Map<String, String>> monetaryActionLimits = 
                			contractFeatureActionsDTO.getCoreCustomerTransactionLimits()
                				.get(contractActionLimit.getFeatureId());
                	if(monetaryActionLimits == null) {
                		monetaryActionLimits = new HashMap<>();
                		monetaryActionLimits.put(contractActionLimit.getActionId(), new HashMap<>());
                	} else if (monetaryActionLimits.get(contractActionLimit.getActionId()) == null) {
                		monetaryActionLimits.put(contractActionLimit.getActionId(), new HashMap<>());
                	}
                	monetaryActionLimits.get(contractActionLimit.getActionId())
                		.put(contractActionLimit.getLimitTypeId(), contractActionLimit.getValue());
                	contractFeatureActionsDTO.getCoreCustomerTransactionLimits()
                		.put(contractActionLimit.getFeatureId(), monetaryActionLimits);
                }
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10818);
        }
 
        return coreCustcontractFADTOmap;
    }

    @Override
    public DBXResult getServiceDefinitionProductPermissions(String serviceDefinitionId, String productIdList,
    		String legalEntityId, Map<String, Object> headersMap) throws ApplicationException {
        DBXResult dbxresult = new DBXResult();
        try {
            ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(ContractBackendDelegate.class);
            
            boolean isProductSpecificFeatueEnabled = FeatureConfiguration.isProductSpecificFeatueEnabled();
            diagnostic.prepareDebug("isProductSpecificFeatueEnabled : "+isProductSpecificFeatueEnabled).log();
            /**
             * Get product level feature and actions from MCMS
             */
            Set<String> productsSet = new HashSet<String>();
            JsonArray productIdArray = JSONUtil.parseAsJsonArray(productIdList);
            
            for(JsonElement product : productIdArray ) {
            	if(!product.isJsonNull())
            		productsSet.add(product.getAsString());
            }
            
            Map<String, Map<String, Set<String>>> productPermissions = null;
            if(isProductSpecificFeatueEnabled) {
            	productPermissions = contractBackendDelegate
                        .getProductLevelPermissions(productsSet);
            	diagnostic.prepareDebug("Is ProductLevelPermissions retrived : "+(productPermissions != null)).log();
            }
            
            
            /**
             * Get service definition feature and actions from dbxdb
             */
            FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO = contractBackendDelegate
            		.getRestrictiveFeatureActionLimits(serviceDefinitionId, "", "", "", "", headersMap, true, "", legalEntityId);
            

            Map<String, Set<String>> serviceDefinitionFeatureaction = serviceDefinitionFeatureActionDTO
                    .getAccountLevelPermissions();
            Map<String, JsonObject> serviceDefinitionFeatureInfo = serviceDefinitionFeatureActionDTO.getFeatureInfo();
            Map<String, JsonObject> serviceDefinitionActionsInfo = serviceDefinitionFeatureActionDTO.getActionsInfo();
            diagnostic.prepareDebug("has serviceDefinition account level Featureaction : "+(serviceDefinitionFeatureaction != null
            		&& !serviceDefinitionFeatureaction.isEmpty())).log();
            
            JsonObject response = new JsonObject();
            JsonArray productFeaturesJsonArray = new JsonArray();
            
            /**
             * Loop for each product and create Features Actions after intersection for each product
             */
            for (String productId : productsSet) {
                JsonObject productFeatures = new JsonObject();
                Map<String, Set<String>> prodFeaturePermissions = null;
                JsonArray featuresActionsJsonArray = new JsonArray();
                
                if(isProductSpecificFeatueEnabled) {
                	prodFeaturePermissions = productPermissions.get(productId);
                	diagnostic.prepareDebug("productPermissions for "+productId+" is : "+prodFeaturePermissions).log();
                }
                /**
                 * For each feature in service definition
                 */
                for (Entry<String, Set<String>> featureActionEntry : serviceDefinitionFeatureaction.entrySet()) {
                    String featureId = featureActionEntry.getKey();
                    
                    // if featureId is present in Products feature lists, then perform intersection
                    if(!isProductSpecificFeatueEnabled || (
                    		prodFeaturePermissions != null
                    		&& prodFeaturePermissions.containsKey(featureId)
                    		&& prodFeaturePermissions.get(featureId) != null)) {
                    	// Iterate over actions and ignore if actionId is not present in Products feature lists
                        JsonArray actionsJsonArray = new JsonArray();
                        for (String actionId : featureActionEntry.getValue()) {
                            if(!isProductSpecificFeatueEnabled || 
                            		prodFeaturePermissions.get(featureId).contains(actionId)) {
                                actionsJsonArray.add(serviceDefinitionActionsInfo.get(actionId));
                            }
                        }
                        if (actionsJsonArray.size() > 0) {
                        	//creating deep copy to avoid corrupting original obj in 
                        	//serviceDefinitionFeatureInfo which will be referred for another product
                            JsonObject featureJsonObject = serviceDefinitionFeatureInfo.get(featureId).deepCopy();
                            featureJsonObject.add("actions", actionsJsonArray);
                            featuresActionsJsonArray.add(featureJsonObject);
                        }
                    }
                }
                productFeatures.addProperty("productId", productId);
                productFeatures.add("productFeatures", featuresActionsJsonArray);
                productFeaturesJsonArray.add(productFeatures);
            }
            response.add("features", productFeaturesJsonArray);
            dbxresult.setResponse(response);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10815);
        }

        return dbxresult;
    }

	@Override
	public Result getProductLevelPermissions(String productRef) throws ApplicationException {
		ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractBackendDelegate.class);
		try {
			Result result = contractBackendDelegate.getProductLevelPermissions(productRef);
			if( HelperMethods.hasDBPErrorMSG(result)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10817);
			}
			return result;
		}catch(Exception e) {
			alert.prepareError("Exception thrown for "+ productRef + " : ", e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10817);
		}
	}
	
	@Override
    public boolean updateContractStatus(String contractId, String statusId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException {
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            return backendDelegate.updateContractStatus(contractId, statusId, legalEntityId, headerMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }
    }

}