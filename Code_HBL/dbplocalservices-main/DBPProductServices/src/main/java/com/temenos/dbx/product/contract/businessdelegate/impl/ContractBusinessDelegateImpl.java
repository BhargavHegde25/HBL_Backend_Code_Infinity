package com.temenos.dbx.product.contract.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAddressBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCommunicationBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractActionLimitsDTO;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.utils.DTOUtils;

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
                    throw new ApplicationException(ErrorCodeEnum.ERR_10351);
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10351);
            }
        } catch (Exception e) {
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
    public ContractDTO getContractDetails(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {

        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        ContractBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
        return backendDelegate.getContract(contractId, headersMap);

    }

    @Override
    public ContractDTO getCompleteContractDetails(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {

        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        ContractBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);

        ContractCommunicationBusinessDelegate contractCommunicationBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCommunicationBusinessDelegate.class);

        ContractAddressBusinessDelegate contractAddressBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAddressBusinessDelegate.class);

        ContractCoreCustomerBusinessDelegate contractCoreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        ContractDTO contractDTO = businessDelegate.getContractDetails(contractId, headersMap);

        List<ContractCommunicationDTO> communication =
                contractCommunicationBD.getContractCommunication(contractId, headersMap);

        contractDTO.setCommunication(communication);

        List<AddressDTO> address = contractAddressBD.getContractAddress(contractId, headersMap);

        contractDTO.setAddress(address);

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
                    contractCustomerBusinessDelegate.getContractCoreCustomers(dto.getContractId(), false, true,
                            headersMap);
            ContractBusinessDelegate contractBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            ContractDTO contractDTO = contractBusinessDelegate.getContractDetails(dto.getContractId(), headersMap);
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
    public List<ContractDTO> getListOfContractsByStatus(String statusId, Map<String, Object> headerMap)
            throws ApplicationException {
        try {
            ContractBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            return backendDelegate.getListOfContractsByStatus(statusId, headerMap);
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
    public DBXResult getCoreCustomerGroupFeatureActionLimits(String coreCustomerRoleIdList,
            Map<String, Object> headersMap) throws ApplicationException {
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
                                serviceDefinitionId, "", roleId, coreCustomerId, "", headersMap, true, "");
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

    private ContractDTO getContractDetailsForEnrollment(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);

        ContractAddressBusinessDelegate contractAddressBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAddressBusinessDelegate.class);

        ContractCoreCustomerBusinessDelegate contractCoreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        ContractDTO contractDTO = businessDelegate.getContractDetails(contractId, headersMap);

        List<AddressDTO> address = contractAddressBD.getContractAddress(contractId, headersMap);

        contractDTO.setAddress(address);

        List<ContractCoreCustomersDTO> contractCustomers =
                contractCoreCustomerBD.getContractCoreCustomers(contractId, false, true, headersMap);

        contractDTO.setContractCustomers(contractCustomers);

        return contractDTO;
    }

    @Override
    public DBXResult getCoreCustomerBasedContractDetails(String coreCustomerId, Map<String, Object> headersMap)
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
            contractCoreCustomerDTO =
                    contractCoreCustomerBusinessDelegate.getContractCoreCustomers(contractCoreCustomerDTO, headersMap);
            if (contractCoreCustomerDTO != null) {
                contractId = contractCoreCustomerDTO.getContractId();
                contractDTO = getContractDetailsForEnrollment(contractId, headersMap);
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
            String coreCustomerId, Map<String, Object> headersMap)
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
                                    contractCoreCustomerDTO.getContractId(), headersMap);
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
}
