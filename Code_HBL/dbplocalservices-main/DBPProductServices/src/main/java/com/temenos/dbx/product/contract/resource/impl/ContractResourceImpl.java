package com.temenos.dbx.product.contract.resource.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.BusinessConfigurationBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAddressBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCommunicationBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ExcludedContractAccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.temenos.dbx.product.contract.resource.api.ContractResource;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractActionLimitsDTO;
import com.temenos.dbx.product.dto.ContractAddressDTO;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.ExcludedContractAccountDTO;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.signatorygroupservices.resource.api.SignatoryGroupResource;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ContractResourceImpl implements ContractResource {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");

    private static final String CONTRACT_ID = "contractId";
    private static final String CONTRACT_NAME = "contractName";
    private static final String SERVICE_DEFINITION_ID = "serviceDefinitionId";
    private static final String FAX_ID = "faxId";
    private static final String COMMUNICATION = "communication";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String PHONE_COUNTRY_CODE = "phoneCountryCode";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    private static final String CONTRACTCUSTOMERS = "contractCustomers";
    private static final String ISPRIMARY = "isPrimary";
    private static final String ISBUSINESS = "isBusiness";
    private static final String SECTORID = "sectorId";
    private static final String CORECUSTOMERID = "coreCustomerId";
    private static final String CORECUSTOMER_NAME = "coreCustomerName";
    private static final String ACCOUNTS = "accounts";
    private static final String ACCOUNTID = "accountId";
    private static final String TYPEID = "typeId";
    private static final String ACCOUNTNAME = "accountName";
    private static final String FEATURES = "features";
    private static final String FEATUREID = "featureId";
    private static final String ACTIONS = "actions";
    private static final String ACTIONID = "actionId";
    private static final String ISALLOWED = "isAllowed";
    private static final String LIMITS = "limits";
    private static final String LIMITID = "id";
    private static final String LIMITVALUE = "value";
    private static final String COUNTRY = "country";
    private static final String OWNERTYPE = "ownerType";
    private static final String COMM_VALUE = "value";
    private static final String STATUSID = "statusId";
    private static final String REJECTEDREASON = "rejectedReason";
    private static final String REJECTEDBY = "rejectedby";
    private static final String ISDEFAULTACTIONSENABLED = "isDefaultActionsEnabled";
    private static final String ARRANGEMENTID = "arrangementId";
    private static final String ACCOUNTHOLDERNAME = "accountHolderName";
    private static final String ACCOUNTSTATUS = "accountStatus";
    private static final String ACCOUNTTYPE = "accountType";
    public static final String IMPLICIT_ACCOUNT_ACCESS = "implicitAccountAccess";
    private static final String EXCLUDED_ACCOUNTS = "excludedAccounts";

    @Override
    public Result createContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String contractId;
        Set<String> validContractCustomers;
        try {
            String contractName = inputParams.get(CONTRACT_NAME);
            String serviceDefinitionId = inputParams.get(SERVICE_DEFINITION_ID);
            String faxId = inputParams.get(FAX_ID);
            String communication = inputParams.get(COMMUNICATION);
            String address = inputParams.get(ADDRESS);
            String contractCustomers = inputParams.get(CONTRACTCUSTOMERS);
            String isDefaultActionsEnabled = dcRequest.getParameter(ISDEFAULTACTIONSENABLED);

            if (StringUtils.isBlank(contractName) || StringUtils.isBlank(serviceDefinitionId)
                    || StringUtils.isBlank(contractCustomers)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10348);

            }

            String serviceDefinitionType = getServiceType(serviceDefinitionId, dcRequest);

            if (StringUtils.isBlank(serviceDefinitionType)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10349);
            }

            boolean isNotExists = verifyContractName(null, contractName, dcRequest);

            if (!isNotExists) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10401);
            }

            validContractCustomers = getValidContractCustomers(contractCustomers, true, dcRequest);
            if (validContractCustomers == null || validContractCustomers.isEmpty()) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10368);
            }

            checkIfPayloadContainsValidAccountsForAllCustomers(contractCustomers, null, dcRequest);

            if (StringUtils.isBlank(isDefaultActionsEnabled)) {
                isDefaultActionsEnabled = DBPUtilitiesConstants.BOOLEAN_STRING_FALSE;
            }

            List<AddressDTO> addressList = DTOUtils.getDTOList(address, AddressDTO.class);

            String contractStatus = getAutoApprovalStatus(isDefaultActionsEnabled, dcRequest);

            ContractDTO contractDTO =
                    createContract(contractName, serviceDefinitionId, serviceDefinitionType, faxId, contractStatus,
                            dcRequest);
            contractId = contractDTO.getId();
            createContractCommunication(communication, contractId, dcRequest);
            createContractAddress(addressList, contractId, dcRequest);
            Set<String> createdValidContractCustomers =
                    createContractCustomers(contractCustomers, validContractCustomers, contractId, dcRequest);

            Map<String, Set<ContractAccountsDTO>> createdCustomerAccounts = createContractAccounts(contractCustomers,
                    createdValidContractCustomers, null, contractId, dcRequest);

            createExcludedContractAccounts(contractCustomers, contractId, dcRequest);

            Map<String, Set<String>> featuresCreated =
                    createContractFeatures(contractCustomers, createdValidContractCustomers, null, contractId,
                            serviceDefinitionType, isDefaultActionsEnabled, dcRequest);
            if (DBPUtilitiesConstants.BOOLEAN_STRING_FALSE.equalsIgnoreCase(isDefaultActionsEnabled)) {
                createContractActionLimits(contractCustomers, featuresCreated, contractId, serviceDefinitionType,
                        dcRequest);
            }

            createDefaultApprovalMatrixEntry(contractCustomers, contractId, createdValidContractCustomers, dcRequest);
            result.addParam(new Param(CONTRACT_ID, contractId, DBPUtilitiesConstants.STRING_TYPE));
            result.addParam(new Param("status", "success", DBPUtilitiesConstants.STRING_TYPE));

            // Below parameters are added in request to use while enrolling the contract
            dcRequest.setAttribute(CONTRACT_ID, contractId);
            dcRequest.setAttribute("serviceType", serviceDefinitionType);
            dcRequest.setAttribute("createdValidCustomers", createdValidContractCustomers);
            dcRequest.setAttribute("createdCustomerAccounts", createdCustomerAccounts);
            dcRequest.setAttribute("createdCustomerAccounts", createdCustomerAccounts);
            dcRequest.setAttribute("contractStatus", contractDTO.getStatusId());

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10351);

        }
        return result;
    }

    private void createExcludedContractAccounts(String contractCustomers, String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();

        ExcludedContractAccountsBusinessDelegate contractAccountsBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ExcludedContractAccountsBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && JSONUtil.hasKey(customerJson, EXCLUDED_ACCOUNTS)
                    && customerJson.get(EXCLUDED_ACCOUNTS).isJsonArray()) {
                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                JsonArray accountsArray = customerJson.get(EXCLUDED_ACCOUNTS).getAsJsonArray();

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject =
                            accountElement.isJsonObject() ? accountElement.getAsJsonObject() : new JsonObject();
                    String accountId =
                            JSONUtil.hasKey(accountObject, ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString() : "";
                    String accountName =
                            JSONUtil.hasKey(accountObject, ACCOUNTNAME) ? accountObject.get(ACCOUNTNAME).getAsString()
                                    : "";
                    String typeId =
                            JSONUtil.hasKey(accountObject, TYPEID) ? accountObject.get(TYPEID).getAsString() : "";
                    String ownerType =
                            JSONUtil.hasKey(accountObject, OWNERTYPE) ? accountObject.get(OWNERTYPE).getAsString() : "";
                    String arrangementId = JSONUtil.hasKey(accountObject, ARRANGEMENTID)
                            ? accountObject.get(ARRANGEMENTID).getAsString()
                            : "";
                    String accountStatus = JSONUtil.hasKey(accountObject, ACCOUNTSTATUS)
                            ? accountObject.get(ACCOUNTSTATUS).getAsString()
                            : DBPUtilitiesConstants.DEFAULT_ACCOUNT_STATUS;

                    if (StringUtils.isNotBlank(accountId)) {

                        ExcludedContractAccountDTO contractAccountDTO = new ExcludedContractAccountDTO();
                        contractAccountDTO.setAccountId(accountId);
                        contractAccountDTO.setAccountName(accountName);
                        contractAccountDTO.setContractId(contractId);
                        contractAccountDTO.setCoreCustomerId(coreCustomerId);
                        contractAccountDTO.setId(idFormatter.format(new Date()));
                        contractAccountDTO.setTypeId(typeId);
                        contractAccountDTO.setOwnerType(ownerType);
                        contractAccountDTO.setArrangementId(arrangementId);
                        contractAccountDTO.setStatusDesc(accountStatus);

                        ExcludedContractAccountDTO createdAcontractAccountDTO = contractAccountsBD
                                .getExcludedContractAccount(contractAccountDTO, dcRequest.getHeaderMap());

                        if (createdAcontractAccountDTO == null
                                || StringUtils.isBlank(createdAcontractAccountDTO.getId())) {

                            contractAccountDTO =
                                    contractAccountsBD.createExcludedContractAccount(contractAccountDTO,
                                            dcRequest.getHeaderMap());
                            if (null == contractAccountDTO || StringUtils.isBlank(contractAccountDTO.getId())) {
                                throw new ApplicationException(ErrorCodeEnum.ERR_10413);
                            }
                        }

                    }

                }
            }

        }

    }

    private void checkIfPayloadContainsValidAccountsForAllCustomers(String contractCustomers, String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
        boolean accountsStatus = false;

        ContractAccountsBusinessDelegate contractAccountsBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAccountsBusinessDelegate.class);
        Set<String> givenAccountsList = new HashSet<>();

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && StringUtils.isNotBlank(customerJson.get(CORECUSTOMERID).getAsString())
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
                JsonArray accountsArray = customerJson.get(ACCOUNTS).getAsJsonArray();

                if (accountsArray.size() == 0) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10408);
                }

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject =
                            accountElement.isJsonObject() ? accountElement.getAsJsonObject() : new JsonObject();
                    String accountId =
                            accountObject.has(ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString() : "";
                    String accountName =
                            accountObject.has(ACCOUNTNAME) ? accountObject.get(ACCOUNTNAME).getAsString() : "";
                    String typeId =
                            accountObject.has(TYPEID) ? accountObject.get(TYPEID).getAsString() : "";

                    if (StringUtils.isBlank(accountId) || StringUtils.isBlank(accountName)
                            || StringUtils.isBlank(typeId)) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_10409);
                    }
                    givenAccountsList.add(accountId);
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10410);
            }
        }
        if (StringUtils.isNotBlank(contractId)) {
            Set<String> existingContractAccounts = new HashSet<>();
            Set<String> newlyAddedAccounts = new HashSet<>();
            List<ContractAccountsDTO> contractAccountsList =
                    contractAccountsBD.getContractCustomerAccounts(contractId, null, dcRequest.getHeaderMap());
            for (ContractAccountsDTO dto : contractAccountsList) {
                String accountId = dto.getAccountId();
                if (StringUtils.isNotBlank(accountId)) {
                    existingContractAccounts.add(accountId);
                }
            }

            for (String givenAccountId : givenAccountsList) {
                if (!existingContractAccounts.contains(givenAccountId)) {
                    newlyAddedAccounts.add(givenAccountId);
                }
            }

            accountsStatus =
                    contractAccountsBD.checkIfGivenAccountsAreValid(newlyAddedAccounts, dcRequest.getHeaderMap());
        } else {
            accountsStatus =
                    contractAccountsBD.checkIfGivenAccountsAreValid(givenAccountsList, dcRequest.getHeaderMap());
        }

        if (!accountsStatus) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10412);
        }
    }

    private boolean verifyContractName(String contarctId, String contractName, DataControllerRequest dcRequest)
            throws ApplicationException {
        ContractBusinessDelegate contractBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
        ContractDTO dto = new ContractDTO();
        ContractDTO resultContractDTO;
        if (StringUtils.isBlank(contarctId) && StringUtils.isNotBlank(contractName)) {
            dto.setName(contractName);
            resultContractDTO = contractBD.getContractDetails(dto, dcRequest.getHeaderMap());
            if (null == resultContractDTO || StringUtils.isBlank(resultContractDTO.getId())) {
                return true;
            }
        } else if (StringUtils.isNotBlank(contarctId) && StringUtils.isNotBlank(contractName)) {
            dto.setName(contractName);
            resultContractDTO = contractBD.getContractDetails(dto, dcRequest.getHeaderMap());
            if (null == resultContractDTO || StringUtils.isBlank(resultContractDTO.getId())
                    || (StringUtils.isNotBlank(resultContractDTO.getId())
                            && contarctId.equalsIgnoreCase(resultContractDTO.getId()))) {
                return true;

            }

        }
        return false;

    }

    private void createDefaultApprovalMatrixEntry(String contractCustomers, String contractId,
            Set<String> createdValidContractCoreCustomers, DataControllerRequest dcRequest)
            throws ApplicationException {
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
        ContractCoreCustomerBusinessDelegate contractCoreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()
                    && createdValidContractCoreCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))) {

                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                Map<String, Set<String>> contractCoreCustomerDetailsMap =
                        contractCoreCustomerBD.getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId,
                                dcRequest.getHeaderMap());

                Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
                Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
                customerActions = getActionWithApproveFeatureAction(customerActions, dcRequest);

                try {
                    approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
                            String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerAccounts),
                            customerActions.toArray(new String[0]), coreCustomerId, null);
                } catch (Exception e) {
                    alert.prepareError(e.getMessage()).log();
                    alert.prepareError("failed to create approval matrix").log();
                }

            }
        }

    }

    private Set<String> getActionWithApproveFeatureAction(Set<String> actionsSet, DataControllerRequest dcRequest)
            throws ApplicationException {
        StringBuilder actionsString = new StringBuilder();
        for (String action : actionsSet) {
            actionsString.append(action);
            actionsString.append(",");
        }
        if (actionsString.length() > 0)
            actionsString.replace(actionsString.length() - 1, actionsString.length(), "");

        ContractFeatureActionsBusinessDelegate contractFeaturesBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);

        String actions = contractFeaturesBD.getActionsWithApproveFeatureAction(actionsString.toString(),
                dcRequest.getHeaderMap());

        return HelperMethods.splitString(actions, DBPUtilitiesConstants.COMMA_SEPERATOR);

    }

    private String getServiceType(String serviceDefinitionId, DataControllerRequest dcRequest)
            throws ApplicationException {
        ServiceDefinitionDTO dto = new ServiceDefinitionDTO();
        dto.setId(serviceDefinitionId);
        ServiceDefinitionBusinessDelegate serviceBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
        dto = serviceBD.getServiceDefinitionDetails(dto, dcRequest.getHeaderMap());
        if (dto == null || StringUtils.isBlank(dto.getId()) || StringUtils.isBlank(dto.getServiceType())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10377);
        }

        return dto.getServiceType();
    }

    private Set<String> getValidContractCustomers(String contractCustomers,
            boolean checkIfAtleastOnePrimaryCustomerInGivenList, DataControllerRequest dcRequest)
            throws ApplicationException {
        boolean isAtleastaCustomerisPrimary = false;
        Set<String> givenCustomers = new HashSet<>();
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
        ContractCoreCustomerBusinessDelegate customerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        for (JsonElement customerElement : contractCustomersArray) {
            if (JSONUtil.isJsonNotNull(customerElement) && customerElement.isJsonObject()) {
                JsonObject customerJson = customerElement.getAsJsonObject();
                String customerId =
                        customerJson.has(CORECUSTOMERID) ? customerJson.get(CORECUSTOMERID).getAsString() : "";
                String isPrimary =
                        customerJson.has(ISPRIMARY) ? customerJson.get(ISPRIMARY).getAsString() : "";
                if (StringUtils.isNotBlank(customerId)) {
                    givenCustomers.add(customerId);
                }
                if (StringUtils.isNotBlank(customerId) && !isAtleastaCustomerisPrimary
                        && "true".equalsIgnoreCase(isPrimary)) {
                    isAtleastaCustomerisPrimary = true;
                }

            }
        }
        if (checkIfAtleastOnePrimaryCustomerInGivenList && !isAtleastaCustomerisPrimary) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10369);
        }
        return customerBD.getValidCoreContractCustomers(givenCustomers, dcRequest.getHeaderMap());
    }

    private Set<String> createContractCustomers(String contractCustomers, Set<String> validContractCustomers,
            String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {
        Set<String> createdValidCustomers = new HashSet<>();
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
        ContractCoreCustomerBusinessDelegate customerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        for (JsonElement customerElement : contractCustomersArray) {
            if (JSONUtil.isJsonNotNull(customerElement) && customerElement.isJsonObject()) {
                JsonObject customerJson = customerElement.getAsJsonObject();
                String customerId =
                        customerJson.has(CORECUSTOMERID) ? customerJson.get(CORECUSTOMERID).getAsString() : "";
                String isPrimary =
                        customerJson.has(ISPRIMARY) ? customerJson.get(ISPRIMARY).getAsString() : "";
                String customerName =
                        customerJson.has(CORECUSTOMER_NAME) ? customerJson.get(CORECUSTOMER_NAME).getAsString() : "";
                String isBusinessType =
                        customerJson.has(ISBUSINESS) ? customerJson.get(ISBUSINESS).getAsString() : "";
                String sectorId =
                        customerJson.has(SECTORID) ? customerJson.get(SECTORID).getAsString() : "";
                String implicitAccountAccess = customerJson.has(IMPLICIT_ACCOUNT_ACCESS)
                        ? customerJson.get(IMPLICIT_ACCOUNT_ACCESS).getAsString()
                        : "";
                if (StringUtils.isBlank(customerId) || StringUtils.isBlank(customerName)) {
                    return createdValidCustomers;
                }

                if (StringUtils.isBlank(implicitAccountAccess)
                        || !DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(implicitAccountAccess)) {
                    implicitAccountAccess = DBPUtilitiesConstants.BOOLEAN_STRING_FALSE;
                }
                if (!validContractCustomers.contains(customerId) || !JSONUtil.hasKey(customerJson, ACCOUNTS)
                        || !customerJson.get(ACCOUNTS).isJsonArray()
                        || customerJson.get(ACCOUNTS).getAsJsonArray().size() == 0) {
                    continue;
                }
                ContractCoreCustomersDTO coreCustomerDTO = new ContractCoreCustomersDTO();
                coreCustomerDTO.setContractId(contractId);
                coreCustomerDTO.setCoreCustomerId(customerId);
                coreCustomerDTO.setId(idFormatter.format(new Date()));
                coreCustomerDTO.setCoreCustomerName(customerName);
                coreCustomerDTO.setCompanyLegalUnit("GB0010001");
                coreCustomerDTO.setImplicitAccountAccess(implicitAccountAccess);
                if (isBusinessType.equalsIgnoreCase(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE)) {
                    coreCustomerDTO.setIsBusiness(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                } else {
                    coreCustomerDTO.setIsBusiness(DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
                }

                if (isPrimary.equalsIgnoreCase(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE)) {
                    coreCustomerDTO.setIsPrimary(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                } else {
                    coreCustomerDTO.setIsPrimary(DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
                }

                if (StringUtils.isNotBlank(sectorId)) {
                    coreCustomerDTO.setSectorId(sectorId);
                }

                ContractCoreCustomersDTO resultCustomerDTO =
                        customerBD.createContractCustomer(coreCustomerDTO, dcRequest.getHeaderMap());
                if (resultCustomerDTO != null && StringUtils.isNotBlank(resultCustomerDTO.getCoreCustomerId())) {
                    createdValidCustomers.add(customerId);
                }

            }

        }

        return createdValidCustomers;
    }

    private ContractDTO createContract(String contractName, String serviceDefinitionId, String serviceDefinitionType,
            String faxId, String contractStatus, DataControllerRequest dcRequest) throws ApplicationException {
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setId(HelperMethods.generateUniqueContractId(dcRequest));
        contractDTO.setName(contractName);
        contractDTO.setServicedefinitionId(serviceDefinitionId);
        contractDTO.setServiceType(serviceDefinitionType);
        contractDTO.setFaxId(faxId);
        contractDTO.setStatusId(contractStatus);
        contractDTO.setCompanyLegalUnit("GB0010001");

        ContractBusinessDelegate contractBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
        return contractBD.createContract(contractDTO, dcRequest.getHeaderMap());

    }

    private void createContractCommunication(String communication, String contractId, DataControllerRequest dcRequest)
            throws ApplicationException {

        if (StringUtils.isBlank(communication)) {
            return;
        }

        ContractCommunicationBusinessDelegate contractCommunicationBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCommunicationBusinessDelegate.class);
        ContractCommunicationDTO contractCommunicationDTO = null;
        JsonParser parser = new JsonParser();
        JsonArray communicationArray = parser.parse(communication).getAsJsonArray();
        for (JsonElement communicationElement : communicationArray) {
            if (JSONUtil.isJsonNotNull(communicationElement) && communicationElement.isJsonObject()) {
                JsonObject communicationJson = communicationElement.getAsJsonObject();
                if (JSONUtil.hasKey(communicationJson, PHONE_NUMBER)
                        && JSONUtil.hasKey(communicationJson, PHONE_COUNTRY_CODE)) {
                    contractCommunicationDTO = new ContractCommunicationDTO();
                    contractCommunicationDTO.setContractId(contractId);
                    contractCommunicationDTO
                            .setPhoneCountryCode(communicationJson.get(PHONE_COUNTRY_CODE).getAsString());
                    contractCommunicationDTO.setValue(communicationJson.get(PHONE_NUMBER).getAsString());
                    contractCommunicationDTO.setId(idFormatter.format(new Date()));
                    contractCommunicationDTO.setIsPreferredContactMethod(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                    contractCommunicationDTO.setTypeId(DBPUtilitiesConstants.COMM_TYPE_PHONE);
                    contractCommunicationDTO.setCompanyLegalUnit("GB0010001");
                    contractCommunicationBD.createContractCommunication(contractCommunicationDTO,
                            dcRequest.getHeaderMap());

                }
                if (JSONUtil.hasKey(communicationJson, EMAIL)) {
                    contractCommunicationDTO = new ContractCommunicationDTO();
                    contractCommunicationDTO.setContractId(contractId);
                    contractCommunicationDTO.setValue(communicationJson.get(EMAIL).getAsString());
                    contractCommunicationDTO.setId(idFormatter.format(new Date()));
                    contractCommunicationDTO.setIsPreferredContactMethod(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                    contractCommunicationDTO.setTypeId(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
                    contractCommunicationBD.createContractCommunication(contractCommunicationDTO,
                            dcRequest.getHeaderMap());
                }

            }

        }
    }

    private void createContractAddress(List<AddressDTO> addressList, String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {
        AddressBusinessDelegate addressBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AddressBusinessDelegate.class);

        ContractAddressBusinessDelegate contractAddressBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAddressBusinessDelegate.class);

        for (AddressDTO inputDTO : addressList) {
            inputDTO.setId(idFormatter.format(new Date()));
            AddressDTO resultDTO = addressBD.createAddress(inputDTO, dcRequest.getHeaderMap());
            if (null == resultDTO || StringUtils.isBlank(resultDTO.getId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10354);
            }

            ContractAddressDTO contractAddressDTO = new ContractAddressDTO();
            contractAddressDTO.setId(idFormatter.format(new Date()));
            contractAddressDTO.setContractId(contractId);
            contractAddressDTO.setAddressId(resultDTO.getId());
            contractAddressDTO.setCompanyLegalUnit("GB0010001");
            contractAddressBD.createContractAddress(contractAddressDTO, dcRequest.getHeaderMap());
        }

    }

    /**
     * 
     * @param contractCustomers
     * @param validContractCustomers
     * @param coreCustomersAccountsMapToCreate
     * @param contractId
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    private Map<String, Set<ContractAccountsDTO>> createContractAccounts(String contractCustomers,
            Set<String> validContractCustomers,
            Map<String, Set<String>> coreCustomersAccountsMapToCreate, String contractId,
            DataControllerRequest dcRequest)
            throws ApplicationException {
        ContractAccountsDTO contractAccountDTO;

        Map<String, Set<ContractAccountsDTO>> customerAccountsMap = new HashMap<>();
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();

        ContractAccountsBusinessDelegate contractAccountsBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAccountsBusinessDelegate.class);

        AccountsBusinessDelegate accountsBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountsBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && validContractCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
                    && (coreCustomersAccountsMapToCreate == null || coreCustomersAccountsMapToCreate
                            .containsKey(JSONUtil.getString(customerJson, CORECUSTOMERID)))
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                String coreCustomerName = JSONUtil.getString(customerJson, CORECUSTOMER_NAME);
                JsonArray accountsArray = customerJson.get(ACCOUNTS).getAsJsonArray();

                Set<ContractAccountsDTO> customerAccounts = new HashSet<>();

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject =
                            accountElement.isJsonObject() ? accountElement.getAsJsonObject() : new JsonObject();
                    String accountId =
                            JSONUtil.hasKey(accountObject, ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString() : "";
                    String accountName =
                            JSONUtil.hasKey(accountObject, ACCOUNTNAME) ? accountObject.get(ACCOUNTNAME).getAsString()
                                    : "";
                    String typeId =
                            JSONUtil.hasKey(accountObject, TYPEID) ? accountObject.get(TYPEID).getAsString() : "";
                    String ownerType =
                            JSONUtil.hasKey(accountObject, OWNERTYPE) ? accountObject.get(OWNERTYPE).getAsString() : "";
                    String arrangementId = JSONUtil.hasKey(accountObject, ARRANGEMENTID)
                            ? accountObject.get(ARRANGEMENTID).getAsString()
                            : "";
                    String accountHoldername = JSONUtil.hasKey(accountObject, ACCOUNTHOLDERNAME)
                            ? accountObject.get(ACCOUNTHOLDERNAME).getAsString()
                            : "";
                    String accountStatus = StringUtils.isNotBlank(JSONUtil.getString(accountObject, ACCOUNTSTATUS))
                            ? JSONUtil.getString(accountObject, ACCOUNTSTATUS)
                            : DBPUtilitiesConstants.DEFAULT_ACCOUNT_STATUS;
                    String accountType = JSONUtil.hasKey(accountObject, ACCOUNTTYPE)
                            ? accountObject.get(ACCOUNTTYPE).getAsString()
                            : "";

                    if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(accountName)
                            && StringUtils.isNotBlank(typeId) && (coreCustomersAccountsMapToCreate == null
                                    || coreCustomersAccountsMapToCreate.get(coreCustomerId).contains(accountId))) {

                        contractAccountDTO = new ContractAccountsDTO();
                        contractAccountDTO.setAccountId(accountId);
                        contractAccountDTO.setAccountName(accountName);
                        contractAccountDTO.setContractId(contractId);
                        contractAccountDTO.setCoreCustomerId(coreCustomerId);
                        contractAccountDTO.setId(idFormatter.format(new Date()));
                        contractAccountDTO.setTypeId(typeId);
                        contractAccountDTO.setOwnerType(ownerType);
                        contractAccountDTO.setArrangementId(arrangementId);
                        contractAccountDTO.setStatusDesc(accountStatus);
                        contractAccountDTO.setCompanyLegalUnit("GB0010001");

                        contractAccountDTO =
                                contractAccountsBD.createContractAccount(contractAccountDTO, dcRequest.getHeaderMap());
                        if (null == contractAccountDTO || StringUtils.isBlank(contractAccountDTO.getId())) {
                            throw new ApplicationException(ErrorCodeEnum.ERR_10357);
                        }

                        AccountsDTO accountGetDTO =
                                accountsBD.getAccountDetailsByAccountID(accountId, dcRequest.getHeaderMap());
                        if (null == accountGetDTO || StringUtils.isBlank(accountGetDTO.getAccountId())) {
                            AccountsDTO inputDTO = new AccountsDTO();
                            inputDTO.setAccountId(accountId);
                            inputDTO.setAccountName(accountName);
                            inputDTO.setAccountType(accountType);
                            inputDTO.setTypeId(typeId);
                            inputDTO.setAccountHolder(accountHoldername);
                            inputDTO.setStatusDescription(accountStatus);
                            inputDTO.setArrangementId(arrangementId);
                            inputDTO.setMembershipId(coreCustomerId);
                            inputDTO.setMembershipName(coreCustomerName);
                            AccountsDTO accountCreatedDTO =
                                    accountsBD.createAccount(inputDTO, dcRequest.getHeaderMap());
                            if (null == accountCreatedDTO || StringUtils.isBlank(accountCreatedDTO.getAccountId())) {
                                throw new ApplicationException(ErrorCodeEnum.ERR_10297);
                            }

                        }

                        customerAccounts.add(contractAccountDTO);
                    }
                }

                customerAccountsMap.put(coreCustomerId, customerAccounts);

            }

        }

        return customerAccountsMap;
    }

    private Map<String, Set<String>> createContractFeatures(String contractCustomers,
            Set<String> validContractCustomers, Map<String, Set<String>> coreCustomersFeaturesMapToCreate,
            String contractId, String serviceDefinitionType,
            String isDefaultActionsEnabled, DataControllerRequest dcRequest) throws ApplicationException {

        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();

        Map<String, Set<String>> customerFeaturesCreatedMap = new HashMap<>();
        ContractFeatureActionsBusinessDelegate contractFeatureActionBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && validContractCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
                    && (coreCustomersFeaturesMapToCreate == null || coreCustomersFeaturesMapToCreate
                            .containsKey(JSONUtil.getString(customerJson, CORECUSTOMERID)))
                    && JSONUtil.hasKey(customerJson, FEATURES) && customerJson.get(FEATURES).isJsonArray()) {
                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                JsonArray featuresArray = customerJson.get(FEATURES).getAsJsonArray();
                Set<String> customerFeatures = new HashSet<>();
                for (JsonElement featureElement : featuresArray) {
                    JsonObject featureJson =
                            featureElement.isJsonObject() ? featureElement.getAsJsonObject() : new JsonObject();
                    String featureId =
                            featureJson.has(FEATUREID) ? featureJson.get(FEATUREID).getAsString() : "";
                    JsonArray actionsArray =
                            featureJson.has(ACTIONS) && featureJson.get(ACTIONS).isJsonArray()
                                    ? featureJson.get(ACTIONS).getAsJsonArray()
                                    : new JsonArray();
                    if (StringUtils.isNotBlank(featureId)
                            && (actionsArray.size() > 0 ||
                                    DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(isDefaultActionsEnabled))
                            && (coreCustomersFeaturesMapToCreate == null
                                    || coreCustomersFeaturesMapToCreate.get(coreCustomerId).contains(featureId))) {
                        customerFeatures.add(featureId);
                    }

                }

                Set<String> createdCustomerFeatures =
                        contractFeatureActionBD.createContractFeatures(customerFeatures, contractId, coreCustomerId,
                                serviceDefinitionType, isDefaultActionsEnabled, dcRequest.getHeaderMap());
                if (createdCustomerFeatures != null && !createdCustomerFeatures.isEmpty()) {
                    customerFeaturesCreatedMap.put(coreCustomerId, createdCustomerFeatures);
                }

            }

        }

        return customerFeaturesCreatedMap;
    }

    private void createContractActionLimits(String contractCustomers, Map<String, Set<String>> featuresCreated,
            String contractId, String serviceDefinitionType, DataControllerRequest dcRequest)
            throws ApplicationException {
        String isNewAction = "0";
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();

        ContractFeatureActionsBusinessDelegate contractFeatureActionBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);

        StringBuilder queryString = new StringBuilder();

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && featuresCreated.containsKey(JSONUtil.getString(customerJson, CORECUSTOMERID))
                    && JSONUtil.hasKey(customerJson, FEATURES) && customerJson.get(FEATURES).isJsonArray()) {
                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                JsonArray featuresArray = customerJson.get(FEATURES).getAsJsonArray();
                Set<String> customerFeatures = featuresCreated.get(coreCustomerId);
                for (JsonElement featureElement : featuresArray) {
                    JsonObject featureJson =
                            featureElement.isJsonObject() ? featureElement.getAsJsonObject() : new JsonObject();
                    String featureId =
                            featureJson.has(FEATUREID) ? featureJson.get(FEATUREID).getAsString() : "";
                    JsonArray actionsArray =
                            featureJson.has(ACTIONS) && featureJson.get(ACTIONS).isJsonArray()
                                    ? featureJson.get(ACTIONS).getAsJsonArray()
                                    : new JsonArray();
                    if (!customerFeatures.contains(featureId)) {
                        continue;
                    }

                    for (JsonElement action : actionsArray) {
                        JsonObject actionJson = action.isJsonObject() ? action.getAsJsonObject() : new JsonObject();
                        String actionId = actionJson.has(ACTIONID) ? actionJson.get(ACTIONID).getAsString() : "";
                        String isAllowed = actionJson.has(ISALLOWED) ? actionJson.get(ISALLOWED).getAsString() : "";
                        JsonArray limitsArray = actionJson.has(LIMITS) && actionJson.get(LIMITS).isJsonArray()
                                ? actionJson.get(LIMITS).getAsJsonArray()
                                : new JsonArray();
                        if (StringUtils.isNotBlank(actionId) && "true".equalsIgnoreCase(isAllowed)) {
                            if (limitsArray.size() > 0) {
                                for (JsonElement limitElement : limitsArray) {
                                    JsonObject limitJson = limitElement.isJsonObject() ? limitElement.getAsJsonObject()
                                            : new JsonObject();
                                    String limitId = limitJson.has(LIMITID) ? limitJson.get(LIMITID).getAsString() : "";
                                    String limitValue =
                                            limitJson.has(LIMITVALUE) ? limitJson.get(LIMITVALUE).getAsString() : "";
                                    if (StringUtils.isNotBlank(limitId) && StringUtils.isNotBlank(limitValue)) {
                                        queryString.append(
                                                getQueryString(contractId, coreCustomerId, featureId, actionId,
                                                        isNewAction, limitId,
                                                        limitValue));
                                    }

                                }
                            } else {

                                queryString.append(
                                        getQueryString(contractId, coreCustomerId, featureId, actionId, isNewAction,
                                                "@", "@"));
                            }
                        }

                    }

                }

            }

        }

        if (StringUtils.isNotBlank(queryString) && queryString.length() > 0) {
            queryString.replace(queryString.length() - 1, queryString.length(), "");
            contractFeatureActionBD.createContractActionLimits(queryString, dcRequest.getHeaderMap());
        }

    }

    private String getQueryString(String contractId, String coreCustomerId, String featureId, String actionId,
            String isNewAction, String limitId, String limitValue) {
        StringBuilder query = new StringBuilder("");

        query.append("\"" + contractId + "\",");
        query.append("\"" + coreCustomerId + "\",");
        query.append("\"" + featureId + "\",");
        query.append("\"" + actionId + "\",");
        query.append("\"" + isNewAction + "\",");
        query.append("\"" + limitId + "\",");
        query.append("\"" + limitValue + "\"");
        query.append("|");

        return query.toString();

    }

    @Override
    public Result searchContracts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerId =
                inputParams.get(CORECUSTOMERID);
        String coreCustomerName =
                inputParams.get(CORECUSTOMER_NAME);

        String contractId = inputParams.get(CONTRACT_ID);
        String contractName = inputParams.get(CONTRACT_NAME);
        String serviceDefinitionId = inputParams.get(SERVICE_DEFINITION_ID);

        String email =
                inputParams.get(EMAIL);
        String phoneCountryCode =
                inputParams.get(PHONE_COUNTRY_CODE);
        String phoneNumber =
                inputParams.get(PHONE_NUMBER);
        String country =
                inputParams.get(COUNTRY);

        if ((StringUtils.isBlank(coreCustomerId) && StringUtils.isBlank(contractId) && StringUtils.isBlank(contractName)
                && StringUtils.isBlank(serviceDefinitionId) && StringUtils.isBlank(coreCustomerName)
                && StringUtils.isBlank(email) && StringUtils.isBlank(phoneCountryCode)
                && StringUtils.isBlank(phoneNumber)
                && StringUtils.isBlank(country))
                || (StringUtils.isNotBlank(phoneNumber) && StringUtils.isBlank(phoneCountryCode))) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10755);
        }

        DBXResult response = new DBXResult();
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            response = businessDelegate.searchContracts(inputParams, dcRequest.getHeaderMap());
            if (response != null && response.getResponse() != null) {
                result = ConvertJsonToResult.convert((JsonObject) response.getResponse());
            }
        } catch (ApplicationException e) {
            alert.prepareError("ContractResourceImpl : Exception occured while searching contracts").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("ContractResourceImpl : Exception occured while searching contracts" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10786);
        }
        return result;

    }

    @Override
    public Result getContractDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String contractId = inputParams.get(CONTRACT_ID);

        if (StringUtils.isBlank(contractId)) {
            contractId = getPrimaryContractId(dcRequest, HelperMethods.getCustomerIdFromSession(dcRequest));
        }

        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10360);
        }
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            ContractDTO contractDTO = businessDelegate.getCompleteContractDetails(contractId, dcRequest.getHeaderMap());

            if (null != contractDTO) {
                String contractString = JSONUtils.stringify(contractDTO);
                JsonParser parser = new JsonParser();
                JsonObject resultJson = formatContractDetailsRespnse(parser.parse(contractString).getAsJsonObject());
                result = JSONToResult.convert(resultJson.toString());
            }

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10363);
        }

        return result;
    }

    private String getPrimaryContractId(DataControllerRequest dcRequest, String customerId) {

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

        BackendIdentifiersBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
        backendIdentifierDTO.setCustomer_id(customerId);

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
            backendIdentifierDTO
                    .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
        } else {
            backendIdentifierDTO.setBackendType(DTOConstants.CORE);
        }

        DBXResult dbxResult = new DBXResult();
        try {
            dbxResult = backendDelegate.get(backendIdentifierDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
        	alert.prepareError("Exception", e).log();
        }

        String coreCustomerId = "";
        if (dbxResult.getResponse() != null) {
            backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            coreCustomerId = backendIdentifierDTO.getBackendId();
        }
        if (StringUtils.isBlank(coreCustomerId)) {
            return null;
        }

        String filter =
                InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
                        + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId;
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject jsonResponse =
                ServiceCallHelper.invokeServiceAndGetJson(input, dcRequest.getHeaderMap(),
                        URLConstants.CONTRACT_CUSTOMERS_GET);
        if (jsonResponse.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
            JsonElement jsonElement = jsonResponse.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                JsonObject contractCustomer = jsonArray.get(0).getAsJsonObject();
                return contractCustomer.has(InfinityConstants.contractId)
                        && !contractCustomer.get(InfinityConstants.contractId).isJsonNull()
                                ? contractCustomer.get(InfinityConstants.contractId).getAsString()
                                : null;
            }
        }

        return null;
    }

    private JsonObject formatContractDetailsRespnse(JsonObject contractJson) {
        JsonArray resultCommunicationArray = new JsonArray();
        if (contractJson.has(COMMUNICATION) && contractJson.get(COMMUNICATION).isJsonArray()) {
            JsonArray commArray = contractJson.get(COMMUNICATION).getAsJsonArray();
            JsonObject resultCommJson = new JsonObject();
            for (JsonElement element : commArray) {
                JsonObject communicationJson = element.isJsonObject() ? element.getAsJsonObject()
                        : new JsonObject();
                if (JSONUtil.isJsonNotNull(communicationJson) && JSONUtil.hasKey(communicationJson, TYPEID)
                        && JSONUtil.getString(communicationJson, TYPEID)
                                .equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
                    resultCommJson.addProperty(PHONE_COUNTRY_CODE,
                            JSONUtil.getString(communicationJson, PHONE_COUNTRY_CODE));
                    resultCommJson.addProperty(PHONE_NUMBER,
                            JSONUtil.getString(communicationJson, COMM_VALUE));
                } else if (JSONUtil.isJsonNotNull(communicationJson) && JSONUtil.hasKey(communicationJson, TYPEID)
                        && JSONUtil.getString(communicationJson, TYPEID)
                                .equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
                    resultCommJson.addProperty(EMAIL,
                            JSONUtil.getString(communicationJson, COMM_VALUE));
                }

            }
            contractJson.remove(COMMUNICATION);
            resultCommunicationArray.add(resultCommJson);
            contractJson.add(COMMUNICATION, resultCommunicationArray);
        }
        return contractJson;
    }

    @Override
    public Result editContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String contractId;
        Set<String> addedCustomersList = new HashSet<>();
        Set<String> removedCustomersList = new HashSet<>();
        Set<String> existingCustomersList = new HashSet<>();
        String existingPrimaryCoreCustomerId = null;
        StringBuilder currentPrimaryCoreCustomerId = new StringBuilder();
        try {
            String contractName = inputParams.get(CONTRACT_NAME);
            String communication = inputParams.get(COMMUNICATION);
            String address = inputParams.get(ADDRESS);
            String contractCustomers = inputParams.get(CONTRACTCUSTOMERS);
            String faxId = inputParams.get(FAX_ID);
            contractId = inputParams.get(CONTRACT_ID);


            if (StringUtils.isBlank(contractName) || StringUtils.isBlank(contractId)
                    || StringUtils.isBlank(contractCustomers)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10364);

            }
            String serviceDefinitionId = getServiceDefinitionID(contractId, dcRequest);
            String serviceDefinitionType = getServiceType(serviceDefinitionId, dcRequest);

            if (StringUtils.isBlank(serviceDefinitionType)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10365);
            }

            boolean isNotExists = verifyContractName(contractId, contractName, dcRequest);

            if (!isNotExists) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10401);
            }

            checkIfPayloadContainsValidAccountsForAllCustomers(contractCustomers, contractId, dcRequest);

            existingPrimaryCoreCustomerId = getExistingPrimaryCoreCustomerId(contractId, dcRequest);
            if (StringUtils.isBlank(existingPrimaryCoreCustomerId)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10400);
            }

            segregateGivenCustomersListAndUpdateCoreCustomerId(contractCustomers, contractId, addedCustomersList,
                    removedCustomersList, existingCustomersList, currentPrimaryCoreCustomerId, dcRequest);

            if (!existingPrimaryCoreCustomerId.equalsIgnoreCase(currentPrimaryCoreCustomerId.toString())) {
                updateCoreCustomerDetails(existingPrimaryCoreCustomerId, contractId, false, dcRequest);
            }

            updateContractDetails(contractId, contractName, faxId, dcRequest);


            addNewCustomers(contractCustomers, addedCustomersList, contractId, serviceDefinitionType,dcRequest);


            deleteRemovedCustomers(contractId, removedCustomersList, dcRequest);

            updateExistingCutomers(contractCustomers, existingCustomersList, contractId, serviceDefinitionType,
                    dcRequest);

            if (!existingPrimaryCoreCustomerId.equalsIgnoreCase(currentPrimaryCoreCustomerId.toString())) {
                updateCoreCustomerDetails(currentPrimaryCoreCustomerId.toString(),
                        contractId, true, dcRequest);
            }

            deleteContractCommunicationAddress(contractId, dcRequest);
            List<AddressDTO> addressList = DTOUtils.getDTOList(address, AddressDTO.class);
            createContractCommunication(communication, contractId, dcRequest);
            createContractAddress(addressList, contractId, dcRequest);
            createExcludedContractAccounts(contractCustomers, contractId, dcRequest);

            result.addParam(new Param(CONTRACT_ID, contractId, DBPUtilitiesConstants.STRING_TYPE));
            result.addParam(new Param("status", "success", DBPUtilitiesConstants.STRING_TYPE));

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10365);

        }
        return result;
    }

    private void updateCoreCustomerDetails(String coreCustomerId,
            String contractId, boolean isPrimary, DataControllerRequest dcRequest) throws ApplicationException {
        ContractCoreCustomerBusinessDelegate coreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        String id = null;
        ContractCoreCustomersDTO coreCustomerDTO = new ContractCoreCustomersDTO();
        coreCustomerDTO.setContractId(contractId);
        coreCustomerDTO.setCoreCustomerId(coreCustomerId);
        List<ContractCoreCustomersDTO> coreCustomersDTO =
                coreCustomerBD.getContractCoreCustomerDetails(coreCustomerDTO, dcRequest.getHeaderMap());
        if (null != coreCustomersDTO && !coreCustomersDTO.isEmpty()) {
            id = coreCustomersDTO.get(0).getId();
        }

        if (StringUtils.isNotBlank(id)) {
            coreCustomerDTO = new ContractCoreCustomersDTO();
            coreCustomerDTO.setId(id);
            coreCustomerDTO.setIsPrimary(Boolean.toString(isPrimary));
            coreCustomerBD.updateContractCustomer(coreCustomerDTO, dcRequest.getHeaderMap());
        }

    }

    private String getExistingPrimaryCoreCustomerId(String contractId, DataControllerRequest dcRequest)
            throws ApplicationException {
        ContractCoreCustomerBusinessDelegate coreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        String coreCustomerId = "";

        ContractCoreCustomersDTO coreCustomerDTO = new ContractCoreCustomersDTO();
        coreCustomerDTO.setContractId(contractId);
        coreCustomerDTO.setIsPrimary(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
        List<ContractCoreCustomersDTO> coreCustomersDTO =
                coreCustomerBD.getContractCoreCustomerDetails(coreCustomerDTO, dcRequest.getHeaderMap());
        if (null != coreCustomersDTO && !coreCustomersDTO.isEmpty()) {
            coreCustomerId = coreCustomersDTO.get(0).getCoreCustomerId();
        }

        return coreCustomerId;
    }

    private void updateContractDetails(String contractId, String ContractName, String faxId,
            DataControllerRequest dcRequest) throws ApplicationException {
        ContractDTO dto = new ContractDTO();
        dto.setId(contractId);
        dto.setName(ContractName);
        dto.setFaxId(faxId);

        ContractBusinessDelegate contractBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
        contractBD.updateContract(dto, dcRequest.getHeaderMap());

    }

    private String getServiceDefinitionID(String contractId, DataControllerRequest dcRequest)
            throws ApplicationException {
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setId(contractId);
        ContractBusinessDelegate contractBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
        contractDTO = contractBD.getContractDetails(contractId, dcRequest.getHeaderMap());
        if (contractDTO == null || StringUtils.isBlank(contractDTO.getId())
                || StringUtils.isBlank(contractDTO.getServicedefinitionId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10378);
        }
        return contractDTO.getServicedefinitionId();
    }

    private void updateExistingCutomers(String contractCustomers, Set<String> existingCustomersList, String contractId,
            String serviceDefinitionType, DataControllerRequest dcRequest) throws ApplicationException {

        Map<String, Set<String>> removedCustomerAccountsMap = new HashMap<>();
        Map<String, Set<String>> removedCustomerFeaturesMap = new HashMap<>();
        Map<String, Set<String>> removedCustomerActionsMap = new HashMap<>();

        Map<String, Set<String>> addedCustomerAccountsMap = new HashMap<>();
        Map<String, Set<String>> addedCustomerFeaturesMap = new HashMap<>();
        Map<String, Set<String>> addedCustomerActionsMap = new HashMap<>();

        Map<String, Set<String>> existingCustomerAccountsMap = new HashMap<>();
        Map<String, Set<String>> existingCustomerFeaturesMap = new HashMap<>();
        Map<String, Set<String>> existingCustomerActionsMap = new HashMap<>();

        StringBuilder changedActionLimitsQuery = new StringBuilder();
        StringBuilder decreasedLimitsQuery = new StringBuilder();

        Map<String, Map<String, Map<String, String>>> existingActionsMap =
                getExistingActionDetails(contractId, dcRequest);

        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
        ContractCoreCustomerBusinessDelegate contractCoreCustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && existingCustomersList.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
                Set<String> existingAccountsList = new HashSet<>();
                Set<String> addedAccountsList = new HashSet<>();
                Set<String> removedAccountsList = new HashSet<>();

                Set<String> existingFeaturesList = new HashSet<>();
                Set<String> addedFeaturesList = new HashSet<>();
                Set<String> removedFeaturesList = new HashSet<>();

                Set<String> existingActionsList = new HashSet<>();
                Set<String> addedActionsList = new HashSet<>();
                Set<String> removedActionsList = new HashSet<>();

                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                if (!existingActionsMap.containsKey(coreCustomerId)) {
                    Map<String, Map<String, String>> actionsMap = new HashMap<>();
                    existingActionsMap.put(coreCustomerId, actionsMap);

                }
                Map<String, Set<String>> contractCoreCustomerDetailsMap =
                        contractCoreCustomerBD.getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId,
                                dcRequest.getHeaderMap());
                JsonArray accountsArray = customerJson.get(ACCOUNTS).getAsJsonArray();

                Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
                Set<String> customerFeatures = contractCoreCustomerDetailsMap.get("features");
                Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject =
                            accountElement.isJsonObject() ? accountElement.getAsJsonObject() : new JsonObject();
                    String accountId =
                            accountObject.has(ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString() : "";
                    String accountName =
                            accountObject.has(ACCOUNTNAME) ? accountObject.get(ACCOUNTNAME).getAsString() : "";
                    String typeId =
                            accountObject.has(TYPEID) ? accountObject.get(TYPEID).getAsString() : "";

                    if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(accountName)
                            && StringUtils.isNotBlank(typeId)) {
                        if (customerAccounts.contains(accountId)) {
                            existingAccountsList.add(accountId);
                        } else {
                            addedAccountsList.add(accountId);
                        }

                    }
                }

                for (String accountId : customerAccounts) {
                    if (!existingAccountsList.contains(accountId) && !addedAccountsList.contains(accountId)) {
                        removedAccountsList.add(accountId);
                    }
                }

                removedCustomerAccountsMap.put(coreCustomerId, removedAccountsList);
                addedCustomerAccountsMap.put(coreCustomerId, addedAccountsList);
                existingCustomerAccountsMap.put(coreCustomerId, existingAccountsList);

                JsonArray featuresArray = customerJson.get(FEATURES).getAsJsonArray();

                for (JsonElement featureElement : featuresArray) {
                    JsonObject featureJson =
                            featureElement.isJsonObject() ? featureElement.getAsJsonObject() : new JsonObject();
                    String featureId =
                            featureJson.has(FEATUREID) ? featureJson.get(FEATUREID).getAsString() : "";
                    JsonArray actionsArray =
                            featureJson.has(ACTIONS) && featureJson.get(ACTIONS).isJsonArray()
                                    ? featureJson.get(ACTIONS).getAsJsonArray()
                                    : new JsonArray();
                    if (StringUtils.isNotBlank(featureId) && actionsArray.size() > 0) {
                        if (customerFeatures.contains(featureId)) {
                            existingFeaturesList.add(featureId);
                        } else {
                            addedFeaturesList.add(featureId);
                        }
                    } else {
                        continue;
                    }

                    for (JsonElement action : actionsArray) {
                        JsonObject actionJson = action.isJsonObject() ? action.getAsJsonObject() : new JsonObject();
                        String actionId = actionJson.has(ACTIONID) ? actionJson.get(ACTIONID).getAsString() : "";
                        String isAllowed = actionJson.has(ISALLOWED) ? actionJson.get(ISALLOWED).getAsString() : "";
                        JsonArray limitsArray =
                                actionJson.has(LIMITS) && actionJson.get(LIMITS).isJsonArray()
                                        ? actionJson.get(LIMITS).getAsJsonArray()
                                        : new JsonArray();
                        if (StringUtils.isNotBlank(actionId) && "true".equalsIgnoreCase(isAllowed)) {
                            if (customerActions.contains(actionId)) {
                                existingActionsList.add(actionId);
                            } else {
                                addedActionsList.add(actionId);
                            }
                        }

                        if (limitsArray.size() > 0) {

                            for (JsonElement limitRecord : limitsArray) {
                                JsonObject limitJson =
                                        limitRecord.isJsonObject() ? limitRecord.getAsJsonObject() : new JsonObject();
                                String limitId = limitJson.has(LIMITID) ? limitJson.get(LIMITID).getAsString() : "";
                                String limitValue =
                                        limitJson.has(LIMITVALUE) ? limitJson.get(LIMITVALUE).getAsString() : "";

                                updateLimitQueries(changedActionLimitsQuery, decreasedLimitsQuery, existingActionsMap,
                                        contractId, coreCustomerId, featureId, actionId, limitId, limitValue);

                            }
                        } else if (StringUtils.isNotBlank(actionId) && addedActionsList.contains(actionId)) {
                            updateLimitQueries(changedActionLimitsQuery, decreasedLimitsQuery, existingActionsMap,
                                    contractId, coreCustomerId, featureId, actionId, "@", "@");
                        }

                    }

                }

                for (String featureId : customerFeatures) {
                    if (!existingFeaturesList.contains(featureId) && !addedFeaturesList.contains(featureId)) {
                        removedFeaturesList.add(featureId);
                    }
                }

                removedCustomerFeaturesMap.put(coreCustomerId, removedFeaturesList);
                addedCustomerFeaturesMap.put(coreCustomerId, addedFeaturesList);
                existingCustomerFeaturesMap.put(coreCustomerId, existingFeaturesList);

                for (String actionId : customerActions) {
                    if (!existingActionsList.contains(actionId) && !addedActionsList.contains(actionId)) {
                        removedActionsList.add(actionId);
                    }
                }

                removedCustomerActionsMap.put(coreCustomerId, removedActionsList);
                addedCustomerActionsMap.put(coreCustomerId, addedActionsList);
                existingCustomerActionsMap.put(coreCustomerId, existingActionsList);

                contractCoreCustomerBD.deleteCoreCustomerAccounts(removedAccountsList, contractId, coreCustomerId,
                        dcRequest.getHeaderMap());
                contractCoreCustomerBD.deleteCoreCustomerFeatures(removedFeaturesList, contractId, coreCustomerId,
                        dcRequest.getHeaderMap());
                contractCoreCustomerBD.deleteCoreCustomerActions(removedActionsList, contractId, coreCustomerId,
                        dcRequest.getHeaderMap());
                updateApprovalMatrixEntriesForNewAccountsCreated(addedAccountsList, existingActionsList, coreCustomerId,
                        contractId, dcRequest);
                updateApprovalMatrixEntriesForNewActionsAdded(addedActionsList, existingAccountsList, addedAccountsList,
                        coreCustomerId, contractId, dcRequest);

            }
        }

        createContractAccounts(contractCustomers, existingCustomersList, addedCustomerAccountsMap, contractId,
                dcRequest);
        createContractFeatures(contractCustomers, existingCustomersList, addedCustomerFeaturesMap, contractId,
                serviceDefinitionType, DBPUtilitiesConstants.BOOLEAN_STRING_FALSE, dcRequest);

        updateContractActions(changedActionLimitsQuery, decreasedLimitsQuery, dcRequest);

    }

    private void updateApprovalMatrixEntriesForNewActionsAdded(Set<String> addedActionsList,
            Set<String> existingAccountsList, Set<String> addedAccountsList, String coreCustomerId, String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {
        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

        Set<String> customerActions = getActionWithApproveFeatureAction(addedActionsList, dcRequest);
        existingAccountsList.addAll(addedAccountsList);

        try {
            approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
                    String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, existingAccountsList),
                    customerActions.toArray(new String[0]), coreCustomerId, null);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            alert.prepareError("failed to create approval matrix").log();
        }

    }

    private void updateApprovalMatrixEntriesForNewAccountsCreated(Set<String> addedAccountsList,
            Set<String> existingActionsList, String coreCustomerId, String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {
        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

        Set<String> customerActions = getActionWithApproveFeatureAction(existingActionsList, dcRequest);

        try {
            if (null != addedAccountsList && !addedAccountsList.isEmpty()) {
                approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
                        String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, addedAccountsList),
                        customerActions.toArray(new String[0]), coreCustomerId, null);
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            alert.prepareError("failed to create approval matrix").log();
        }

    }

    private void updateContractActions(StringBuilder changedActionLimitsQuery, StringBuilder decreasedLimitsQuery,
            DataControllerRequest dcRequest) throws ApplicationException {

        ContractFeatureActionsBusinessDelegate contractFeatureActionBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);
        if (StringUtils.isNotBlank(changedActionLimitsQuery) && changedActionLimitsQuery.length() > 0) {
            changedActionLimitsQuery.replace(changedActionLimitsQuery.length() - 1, changedActionLimitsQuery.length(),
                    "");
            contractFeatureActionBD.createContractActionLimits(changedActionLimitsQuery, dcRequest.getHeaderMap());
        }

        if (StringUtils.isNotBlank(decreasedLimitsQuery) && decreasedLimitsQuery.length() > 0) {
            decreasedLimitsQuery.replace(decreasedLimitsQuery.length() - 1, decreasedLimitsQuery.length(),
                    "");

            contractFeatureActionBD.decreaseUserLimits(decreasedLimitsQuery, dcRequest.getHeaderMap());
        }

    }

    private void updateLimitQueries(StringBuilder changedActionLimitsQuery, StringBuilder decreasedLimitsQuery,
            Map<String, Map<String, Map<String, String>>> existingActionsMap, String contractId, String coreCustomerId,
            String featureId, String actionId, String limitId, String limitValue) {
        String isNewAction = "0";
        if (existingActionsMap.containsKey(coreCustomerId)
                && existingActionsMap.get(coreCustomerId).containsKey(actionId)
                && existingActionsMap.get(coreCustomerId).get(actionId).containsKey(limitId)) {
            String existingLimitValue = existingActionsMap.get(coreCustomerId).get(actionId).get(limitId);
            if (StringUtils.isNotBlank(existingLimitValue) && StringUtils.isNotBlank(limitValue)) {
                if (Double.parseDouble(existingLimitValue) > Double.parseDouble(limitValue)) {
                    decreasedLimitsQuery.append(
                            getQueryString(contractId, coreCustomerId, featureId, actionId, isNewAction, limitId,
                                    limitValue));
                }
                if (Double.parseDouble(existingLimitValue) != Double.parseDouble(limitValue)) {
                    changedActionLimitsQuery
                            .append(getQueryString(contractId, coreCustomerId, featureId, actionId, isNewAction,
                                    limitId,
                                    limitValue));
                }

            }
        } else if (existingActionsMap.containsKey(coreCustomerId)
                && !existingActionsMap.get(coreCustomerId).containsKey(actionId)
                && StringUtils.isNotBlank(limitValue) && StringUtils.isNotBlank(limitId)) {

            changedActionLimitsQuery
                    .append(getQueryString(contractId, coreCustomerId, featureId, actionId, isNewAction, limitId,
                            limitValue));

        }

    }

    private Map<String, Map<String, Map<String, String>>> getExistingActionDetails(String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {

        Map<String, Map<String, Map<String, String>>> existingActionsMap = new HashMap<>();
        ContractBusinessDelegate actionBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
        ContractActionLimitsDTO dto = new ContractActionLimitsDTO();
        dto.setContractId(contractId);
        List<ContractActionLimitsDTO> contractActionsList = actionBD.getContractActions(dto, dcRequest.getHeaderMap());
        if (null != contractActionsList && !contractActionsList.isEmpty()) {
            for (ContractActionLimitsDTO contractActionDTO : contractActionsList) {
                if (contractId.equalsIgnoreCase(contractActionDTO.getContractId())) {
                    String coreCustomerId = contractActionDTO.getCoreCustomerId();
                    String actionId = contractActionDTO.getActionId();
                    String limitTYpeId = contractActionDTO.getLimitTypeId();
                    String limitValue = contractActionDTO.getValue();
                    if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(actionId)
                            && StringUtils.isNotBlank(limitTYpeId) && StringUtils.isNotBlank(limitValue)) {

                        if (!existingActionsMap.containsKey(coreCustomerId)) {
                            Map<String, Map<String, String>> actionsMap = new HashMap<>();
                            Map<String, String> limitMap = new HashMap<>();
                            limitMap.put(limitTYpeId, limitValue);
                            actionsMap.put(actionId, limitMap);
                            existingActionsMap.put(coreCustomerId, actionsMap);
                        } else if (existingActionsMap.containsKey(coreCustomerId)
                                && !existingActionsMap.get(coreCustomerId).containsKey(actionId)) {
                            Map<String, Map<String, String>> actionsMap = existingActionsMap.get(coreCustomerId);
                            Map<String, String> limitMap = new HashMap<>();
                            limitMap.put(limitTYpeId, limitValue);
                            actionsMap.put(actionId, limitMap);
                        } else if (existingActionsMap.containsKey(coreCustomerId)
                                && existingActionsMap.get(coreCustomerId).containsKey(actionId)
                                && !existingActionsMap.get(coreCustomerId).get(actionId).containsKey(limitTYpeId)) {
                            Map<String, String> limitMap = existingActionsMap.get(coreCustomerId).get(actionId);
                            limitMap.put(limitTYpeId, limitValue);

                        }

                    }
                }

            }

        }
        return existingActionsMap;
    }

    private void deleteContractCommunicationAddress(String contractId, DataControllerRequest dcRequest) {
        ContractCommunicationBusinessDelegate communicationBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCommunicationBusinessDelegate.class);
        communicationBD.deleteContractCommunicationAddress(contractId, dcRequest.getHeaderMap());

    }

    private Set<String> addNewCustomers(String contractCustomers, Set<String> addedCustomersList, String contractId,
            String serviceDefinitionType, DataControllerRequest dcRequest) throws ApplicationException {
        Set<String> createdValidContractCustomers = new HashSet<>();
        if (!addedCustomersList.isEmpty()) {
            ContractCoreCustomerBusinessDelegate customerBD =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
            Set<String> validCustomers =
                    customerBD.getValidCoreContractCustomers(addedCustomersList, dcRequest.getHeaderMap());
            createdValidContractCustomers =
                    createContractCustomers(contractCustomers, validCustomers, contractId, dcRequest);

            createContractAccounts(contractCustomers, createdValidContractCustomers, null, contractId, dcRequest);
            Map<String, Set<String>> featuresCreated =
                    createContractFeatures(contractCustomers, createdValidContractCustomers, null, contractId,
                            serviceDefinitionType, "false", dcRequest);
            createContractActionLimits(contractCustomers, featuresCreated, contractId, serviceDefinitionType,
                    dcRequest);

            createDefaultApprovalMatrixEntry(contractCustomers, contractId, createdValidContractCustomers, dcRequest);
        }
        return createdValidContractCustomers;

    }

    private void deleteRemovedCustomers(String contractId, Set<String> removedCustomersList,
            DataControllerRequest dcRequest) throws ApplicationException {
        ContractCoreCustomerBusinessDelegate customersBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        customersBD.deleteContractCoreCustomers(removedCustomersList, contractId, dcRequest.getHeaderMap());

        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

        approvalmatrixDelegate.deleteApprovalMatrixForCifs(contractId, removedCustomersList, dcRequest);

    }

    private void segregateGivenCustomersListAndUpdateCoreCustomerId(String contractCustomers, String contractId,
            Set<String> addedCustomersList, Set<String> removedCustomersList, Set<String> notUpdatedCustomersList,
            StringBuilder currentPrimaryCoreCustomerId, DataControllerRequest dcRequest)
            throws ApplicationException {
        boolean isAtleastaCustomerisPrimary = false;
        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(contractCustomers)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10365);
        }

        Set<String> createdCustomers = new HashSet<>();
        ContractCoreCustomerBusinessDelegate customersBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        List<ContractCoreCustomersDTO> customersList =
                customersBD.getContractCoreCustomers(contractId, false, false, dcRequest.getHeaderMap());
        for (ContractCoreCustomersDTO dto : customersList) {
            createdCustomers.add(dto.getCoreCustomerId());
        }
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
        for (JsonElement customerElement : contractCustomersArray) {
            if (JSONUtil.isJsonNotNull(customerElement) && customerElement.isJsonObject()) {
                JsonObject customerJson = customerElement.getAsJsonObject();
                String customerId =
                        customerJson.has(CORECUSTOMERID) ? customerJson.get(CORECUSTOMERID).getAsString() : "";
                String customerName =
                        customerJson.has(CORECUSTOMER_NAME) ? customerJson.get(CORECUSTOMER_NAME).getAsString() : "";
                String isPrimary =
                        customerJson.has(ISPRIMARY) ? customerJson.get(ISPRIMARY).getAsString() : "";
                if (StringUtils.isBlank(customerId) || StringUtils.isBlank(customerName)) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10364);
                }
                if (createdCustomers.contains(customerId)) {
                    notUpdatedCustomersList.add(customerId);
                } else {
                    addedCustomersList.add(customerId);
                }
                if (!isAtleastaCustomerisPrimary && "true".equalsIgnoreCase(isPrimary)) {
                    isAtleastaCustomerisPrimary = true;
                    currentPrimaryCoreCustomerId.append(customerId);
                }

            }

        }

        for (ContractCoreCustomersDTO dto : customersList) {
            if (!notUpdatedCustomersList.contains(dto.getCoreCustomerId())) {
                removedCustomersList.add(dto.getCoreCustomerId());
            }
        }

        if (!isAtleastaCustomerisPrimary) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10369);
        }

    }

    @Override
    public Result getContractFeatureActionLimits(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        DBXResult response = new DBXResult();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String contractId = StringUtils.isNotBlank(inputParams.get(CONTRACT_ID)) ? inputParams.get(CONTRACT_ID)
                : dcRequest.getParameter(CONTRACT_ID);
        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10774);
        }
        ContractCoreCustomersDTO dto = new ContractCoreCustomersDTO();
        dto.setContractId(contractId);
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            response = businessDelegate.getContractFeatureActionLimits(dto, dcRequest.getHeaderMap());
            if (response != null && response.getResponse() != null) {
                result = ConvertJsonToResult.convert((JsonObject) response.getResponse());
            }
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10775);
        }
        return result;
    }

    @Override
    public Result getContractInfinityUsers(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String contractId = StringUtils.isNotBlank(inputParams.get(CONTRACT_ID)) ? inputParams.get(CONTRACT_ID)
                : dcRequest.getParameter(CONTRACT_ID);
        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10375);
        }
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            JsonArray resultJson = businessDelegate.getContractInfinityUsers(contractId, dcRequest.getHeaderMap());
            updateEmailForUsers(resultJson, dcRequest);
            JsonObject object = new JsonObject();
            object.add(InfinityConstants.contractUsers, resultJson);
            result = JSONToResult.convert(object.toString());
            addSignatoryGroups(result);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10375);
        }

        return result;
    }

    private void addSignatoryGroups(Result result) {

        Dataset contractUsers = result.getDatasetById(InfinityConstants.contractUsers);

        for (Record record : contractUsers.getAllRecords()) {
            String id = record.getParamValueByName(InfinityConstants.customerId);
            Result result2 = DBPAPIAbstractFactoryImpl.getResource(SignatoryGroupResource.class)
                    .fetchSignatoryGroupDetailsById(id);
            if (result2.getDatasetById(InfinityConstants.SignatoryGroups) != null) {
                result2.getDatasetById(InfinityConstants.SignatoryGroups).setId(InfinityConstants.signatoryGroups);
            }
            record.addAllDatasets(result2.getAllDatasets());
            record.addAllParams(result2.getAllParams());
            record.addAllRecords(result2.getAllRecords());
        }

    }

    private void updateEmailForUsers(JsonArray resultJson, DataControllerRequest dcRequest) {
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        if (JSONUtil.isJsonNotNull(resultJson) && resultJson.size() > 0) {
            for (JsonElement element : resultJson) {
                if (JSONUtil.isJsonNotNull(element) && element.isJsonObject()) {
                    JsonObject userJson = element.getAsJsonObject();
                    String primaryCorecustomerId = JSONUtil.getString(userJson, "primaryCoreCustomerId");
                    String customerId = JSONUtil.getString(userJson, InfinityConstants.customerId);
                    if (StringUtils.isNotBlank(primaryCorecustomerId)) {

                        CommunicationBackendDelegate backendDelegate =
                                DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
                        CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
                        customerCommunicationDTO.setCustomer_id(customerId);
                        DBXResult dbxresult =
                                backendDelegate.getPrimaryMFACommunicationDetails(customerCommunicationDTO,
                                        dcRequest.getHeaderMap());
                        if (dbxresult.getResponse() == null)
                            continue;
                        JsonArray communicationArray = ((JsonObject) dbxresult.getResponse()).getAsJsonObject()
                                .get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
                        String email = "";
                        for (JsonElement jsonelement : communicationArray) {
                            JsonObject object = jsonelement.getAsJsonObject();
                            if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
                                email = JSONUtil.getString(object, "Value");
                        }
                        userJson.addProperty("Email", email);

                    }
                }
            }

        }

    }

    @Override
    public Result getListOfContractsByStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String statusId = StringUtils.isNotBlank(inputParams.get(STATUSID)) ? inputParams.get(STATUSID)
                    : dcRequest.getParameter(STATUSID);

            ContractBusinessDelegate contractBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(ContractBusinessDelegate.class);

            List<ContractDTO> dtoList = contractBD.getListOfContractsByStatus(statusId, dcRequest.getHeaderMap());
            if (null != dtoList) {
                String contractString = JSONUtils.stringifyCollectionWithTypeInfo(dtoList, ContractDTO.class);
                JSONArray contractArray = new JSONArray(contractString);
                JSONObject contractJson = new JSONObject();
                contractJson.put(DBPDatasetConstants.DATASET_CONTRACT, contractArray);
                result = ConvertJsonToResult.convert(contractJson.toString());
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10379);

        }
        return result;
    }

    @Override
    public Result updateContractStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        String userId = null;
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        HelperMethods.removeNullValues(inputParams);
        List<String> allowedStatus = new ArrayList<>();
        allowedStatus.add(DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
        allowedStatus.add(DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED);

        String statusId = StringUtils.isNotBlank(inputParams.get(STATUSID)) ? inputParams.get(STATUSID)
                : dcRequest.getParameter(STATUSID);
        String contractId = StringUtils.isNotBlank(inputParams.get(CONTRACT_ID)) ? inputParams.get(CONTRACT_ID)
                : dcRequest.getParameter(CONTRACT_ID);

        if (StringUtils.isBlank(statusId) || StringUtils.isBlank(contractId) || !allowedStatus.contains(statusId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10382);
        }

        ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ContractBusinessDelegate.class);
        ContractDTO inputDTO = new ContractDTO();
        ContractDTO outputDTO = new ContractDTO();
        inputDTO.setId(contractId);
        try {

            outputDTO = contractBusinessDelegate.getContractDetails(contractId, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while updating contract status").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }

        if (StringUtils.isNotBlank(outputDTO.getStatusId())
                && DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(outputDTO.getStatusId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10383);
        }
        if (DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED.equalsIgnoreCase(statusId)) {
            inputDTO.setRejectedReason(dcRequest.getParameter(REJECTEDREASON));
            inputDTO.setRejectedby(dcRequest.getParameter(REJECTEDBY));
            inputDTO.setRejectedts(HelperMethods.getCurrentTimeStamp());
        }
        boolean isUpdateSuccess = false;

        inputDTO.setStatusId(statusId);
        try {

            isUpdateSuccess =
                    contractBusinessDelegate.updateContractStatus(contractId, statusId, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while updating contract status").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10381);
        }
        result.addStringParam(DBPUtilitiesConstants.IS_UPDATE_SUCEES, String.valueOf(isUpdateSuccess));
        dcRequest.addRequestParam_(DBPUtilitiesConstants.IS_UPDATE_SUCEES, String.valueOf(isUpdateSuccess));

        if (isUpdateSuccess & DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(statusId)) {
            JsonArray array = contractBusinessDelegate.getContractInfinityUsers(contractId, dcRequest.getHeaderMap());

            if (JSONUtil.isJsonNotNull(array) && array.size() > 0) {
                JsonObject object = array.get(0).getAsJsonObject();
                userId = object.has("customerId") ? object.get("customerId").getAsString() : "";

            }

            if (StringUtils.isNotBlank(userId)) {

                // These details are being used in orchestration service.
                dcRequest.addRequestParam_("userId", userId);
                dcRequest.addRequestParam_(CONTRACT_ID, contractId);

            }
        }

        dcRequest.addRequestParam_("contractStatus", statusId);
        return result;
    }

    @Override
    public Result getCoreCustomerGroupFeatureActionLimits(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerRoleIdList = StringUtils.isNotBlank(inputParams.get("coreCustomerRoleIdList"))
                ? inputParams.get("coreCustomerRoleIdList")
                : dcRequest.getParameter("coreCustomerRoleIdList");
        if (StringUtils.isBlank(coreCustomerRoleIdList)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10779);
        }
        DBXResult response = new DBXResult();
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            response = businessDelegate.getCoreCustomerGroupFeatureActionLimits(coreCustomerRoleIdList,
                    dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10777);
        }
        if (response != null && response.getResponse() != null) {
            result = ConvertJsonToResult.convert((JsonObject) response.getResponse());
        }
        return result;
    }

    @Override
    public Result getCoreCustomerBasedContractDetails(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerId = StringUtils.isNotBlank(inputParams.get(CORECUSTOMERID))
                ? inputParams.get(CORECUSTOMERID)
                : dcRequest.getParameter("coreCustomerRoleIdList");
        if (StringUtils.isBlank(coreCustomerId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10779);
        }
        DBXResult response = new DBXResult();
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            response = businessDelegate.getCoreCustomerBasedContractDetails(coreCustomerId,
                    dcRequest.getHeaderMap());
            if (response != null && response.getResponse() != null) {
                JsonArray jsonarray = new JsonArray();
                List<ContractDTO> dto = (List<ContractDTO>) response.getResponse();
                if (dto.size() > 0) {
                    String contractString = JSONUtils.stringify(dto);
                    jsonarray = new JsonParser().parse(contractString).getAsJsonArray();
                }
                JsonObject jsonobject = new JsonObject();
                jsonobject.add("contracts", jsonarray);
                result = JSONToResult.convert(jsonobject.toString());
            }
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10781);
        }
        return result;
    }

    @Override
    public Result getRelativeCoreCustomerBasedContractDetails(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerId = StringUtils.isNotBlank(inputParams.get(CORECUSTOMERID))
                ? inputParams.get(CORECUSTOMERID)
                : dcRequest.getParameter(CORECUSTOMERID);
        if (StringUtils.isBlank(coreCustomerId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10779);
        }
        DBXResult response = new DBXResult();
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            response = businessDelegate.getRelativeCoreCustomerBasedContractDetails(configurations, coreCustomerId,
                    dcRequest.getHeaderMap());
            if (response != null && response.getResponse() != null) {
                JsonArray jsonarray = new JsonArray();
                List<ContractDTO> dto = (List<ContractDTO>) response.getResponse();
                if (dto.size() > 0) {
                    String contractString = JSONUtils.stringify(dto);
                    jsonarray = new JsonParser().parse(contractString).getAsJsonArray();
                }
                JsonObject jsonobject = new JsonObject();
                jsonobject.add("contracts", jsonarray);
                result = JSONToResult.convert(jsonobject.toString());
            }
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract level feature action limits"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10782);
        }
        return result;
    }

    @Override
    public Result getContractAccounts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String contractId = StringUtils.isNotBlank(inputParams.get(CONTRACT_ID))
                ? inputParams.get(CONTRACT_ID)
                : dcRequest.getParameter(CONTRACT_ID);
        if (StringUtils.isBlank(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10783);
        }
        List<ContractAccountsDTO> dtoList = new ArrayList<>();
        try {
            ContractBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
            dtoList = businessDelegate.getContractAccounts(contractId,
                    dcRequest.getHeaderMap());
            if (dtoList != null) {
                JsonArray jsonarray = new JsonArray();
                if (dtoList.size() > 0) {
                    String contractAccountsString = JSONUtils.stringify(dtoList);
                    jsonarray = new JsonParser().parse(contractAccountsString).getAsJsonArray();
                }
                JsonObject jsonobject = new JsonObject();
                jsonobject.add("contractAccounts", jsonarray);
                result = JSONToResult.convert(jsonobject.toString());
            }
        } catch (ApplicationException e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract accounts"
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "ContractResourceImpl : Exception occured while fetching the contract accounts"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10784);
        }
        return result;
    }

    @Override
    public boolean validateEnrollContractPayload(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_SERVICEKEY = "serviceKey";
        final String INPUT_AUTHORIZEDSIGNATORY = "authorizedSignatory";

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String serviceKey = StringUtils.isNotBlank(inputParams.get(INPUT_SERVICEKEY))
                ? inputParams.get(INPUT_SERVICEKEY)
                : dcRequest.getParameter(INPUT_SERVICEKEY);
        String authorizedSignatory = StringUtils.isNotBlank(inputParams.get(INPUT_AUTHORIZEDSIGNATORY))
                ? inputParams.get(INPUT_AUTHORIZEDSIGNATORY)
                : dcRequest.getParameter(INPUT_AUTHORIZEDSIGNATORY);
        String contractCustomers = StringUtils.isNotBlank(inputParams.get(CONTRACTCUSTOMERS))
                ? inputParams.get(CONTRACTCUSTOMERS)
                : dcRequest.getParameter(CONTRACTCUSTOMERS);

        ApplicationBusinessDelegate applicationBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApplicationBusinessDelegate.class);

        List<CustomerDTO> authorizedSignatoryList = DTOUtils.getDTOList(authorizedSignatory, CustomerDTO.class);
        Map<String, Set<String>> accountsGiven = getGivenAccountsDetails(contractCustomers);

        JsonObject payloadJson = getPayload(serviceKey, dcRequest);
        ApplicationDTO applicationDTO = applicationBD.getApplicationProperties(dcRequest.getHeaderMap());
        if ("0".equals(applicationDTO.getIsAccountCentricCore())
                || "false".equals(applicationDTO.getIsAccountCentricCore())) {
            return validateCustomerCentricPayload(payloadJson, accountsGiven, authorizedSignatoryList);

        } else if ("1".equals(applicationDTO.getIsAccountCentricCore())
                || "true".equals(applicationDTO.getIsAccountCentricCore())) {
            return false;

        }
        return false;

    }

    private boolean validateCustomerCentricPayload(JsonObject payloadJson, Map<String, Set<String>> accountsGiven,
            List<CustomerDTO> authorizedSignatoryList) {
        JsonObject object = payloadJson.get("requestPayload").getAsJsonObject();
        String payloadSsn = object.has("Ssn") ? object.get("Ssn").getAsString() : "";
        String payloadDOB = object.has("DateOfBirth") ? object.get("DateOfBirth").getAsString() : "";
        String payloadLastName = object.has("LastName") ? object.get("LastName").getAsString() : "";

        CustomerDTO dto = authorizedSignatoryList.get(0);
        boolean status = payloadSsn.equalsIgnoreCase(dto.getSsn()) && payloadDOB.equalsIgnoreCase(dto.getDateOfBirth())
                && payloadLastName.equalsIgnoreCase(dto.getLastName());

        Map<String, String> accountToCorecustomerMap = new HashMap<>();

        if (status) {
            JsonArray payloadAccountsArray = payloadJson.get("Accounts").getAsJsonArray();
            for (JsonElement element : payloadAccountsArray) {
                if (element.isJsonObject()) {
                    JsonObject accountJson = element.getAsJsonObject();
                    String accountId = accountJson.has(ACCOUNTID) ? accountJson.get(ACCOUNTID).getAsString() : "";
                    String coreCustomerId =
                            accountJson.has(CORECUSTOMERID) ? accountJson.get(CORECUSTOMERID).getAsString() : "";
                    accountToCorecustomerMap.put(accountId, coreCustomerId);
                }

            }

            for (Entry<String, Set<String>> entry : accountsGiven.entrySet()) {
                String coreCustomerId = entry.getKey();
                Set<String> accounts = entry.getValue();
                for (String accountId : accounts) {
                    if (!accountToCorecustomerMap.containsKey(accountId)
                            || !coreCustomerId.equalsIgnoreCase(accountToCorecustomerMap.get(accountId))) {
                        return false;
                    }
                }

            }

        }
        return status;
    }

    private JsonObject getPayload(String serviceKey, DataControllerRequest dcRequest) throws ApplicationException {

        MFAServiceDTO dto = new MFAServiceDTO();
        dto.setServiceKey(serviceKey);
        MFAServiceBusinessDelegate bd = DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
        List<MFAServiceDTO> list = bd.getMfaService(dto, new HashMap<>(), dcRequest.getHeaderMap());
        if (!list.isEmpty()) {
            dto = list.get(0);
        }
        MFAServiceUtil util = new MFAServiceUtil(dto);
        if (!util.isStateVerified()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10324);
        }
        int otpValidTimeInMinutes = util.getServiceKeyExpiretime(dcRequest);
        if (!util.isValidServiceKey(otpValidTimeInMinutes)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10325);
        }
        return util.getRequestPayload();
    }

    private Map<String, Set<String>> getGivenAccountsDetails(String contractCustomers)
            throws ApplicationException {
        ContractAccountsDTO contractAccountDTO;

        Map<String, Set<String>> corecustomerAccounts = new HashMap<>();
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson =
                    customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                JsonArray accountsArray = customerJson.get(ACCOUNTS).getAsJsonArray();

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject =
                            accountElement.isJsonObject() ? accountElement.getAsJsonObject() : new JsonObject();
                    String accountId =
                            accountObject.has(ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString() : "";
                    String accountName =
                            accountObject.has(ACCOUNTNAME) ? accountObject.get(ACCOUNTNAME).getAsString() : "";
                    String typeId =
                            accountObject.has(TYPEID) ? accountObject.get(TYPEID).getAsString() : "";

                    if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(accountName)
                            && StringUtils.isNotBlank(typeId)) {
                        if (corecustomerAccounts.containsKey(coreCustomerId)) {
                            corecustomerAccounts.get(coreCustomerId).add(accountId);
                        } else {
                            Set<String> accounts = new HashSet<>();
                            accounts.add(accountId);
                            corecustomerAccounts.put(coreCustomerId, accounts);
                        }
                    }
                }

            }

        }

        return corecustomerAccounts;
    }

    private String getAutoApprovalStatus(String isDefaultActionsEnabled, DataControllerRequest dcRequest)
            throws ApplicationException {
        if (DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(isDefaultActionsEnabled)) {
            BusinessConfigurationBusinessDelegate configurationBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(BusinessConfigurationBusinessDelegate.class);

            return configurationBD.getAutoApprovalStatus(dcRequest.getHeaderMap());
        }
        return DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE;
    }

}
