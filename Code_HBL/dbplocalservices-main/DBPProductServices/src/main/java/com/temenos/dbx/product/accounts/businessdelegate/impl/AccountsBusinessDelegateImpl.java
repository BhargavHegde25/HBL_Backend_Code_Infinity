package com.temenos.dbx.product.accounts.businessdelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountHolderBackendDelegate;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountInformationBackendDelegate;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountsBackendDelegate;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.AccountTypeDTO;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.BusinessSignatoryDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;
import com.temenos.dbx.product.dto.OrganisationDTO;
import com.temenos.dbx.product.dto.SignatoryTypeDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.BusinessSignatoryBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.SignatoryTypeBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;

public class AccountsBusinessDelegateImpl implements AccountsBusinessDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public List<AllAccountsViewDTO> getAccountInformation(AllAccountsViewDTO accountDTO, Map<String, Object> headersMap)
            throws ApplicationException {

        List<AllAccountsViewDTO> allAccountsViewDTO = new ArrayList<>();

        try {
            AccountHolderBackendDelegate accountHolderBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountHolderBackendDelegate.class);
            allAccountsViewDTO = accountHolderBackendDelegate.getAccountInformation(accountDTO, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        }
        return allAccountsViewDTO;
    }

    @Override
    public AllAccountsViewDTO validateGetAccountInformationInput(Map<String, String> inputParams)
            throws ApplicationException {
        AllAccountsViewDTO inputDTO = new AllAccountsViewDTO();

        String accountId = inputParams.get("accountId");
        String accountName = inputParams.get("accountName");
        String accountType = inputParams.get("accountType");
        String accountHolderName = inputParams.get("accountHolderName");
        String customerInformation = inputParams.get("customerInformation");
        String companyInformation = inputParams.get("companyInformation");
        /**
         * AccountId is mandatory in DBXDB Product Other mandatory checks should be made in case of other
         * implementations
         */
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(companyInformation)
                || StringUtils.isBlank(customerInformation)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10237);
        }
        inputDTO.setAccountId(accountId);
        if (StringUtils.isNotBlank(accountType))
            inputDTO.setAccountType(accountType);
        if (StringUtils.isNotBlank(accountName))
            inputDTO.setAccountName(accountName);
        if (StringUtils.isNotBlank(accountHolderName))
            inputDTO.setAccountHolderName(accountHolderName);

        return inputDTO;
    }

    @Override
    public AllAccountsViewDTO validateGetAccountInformationOutput(List<AllAccountsViewDTO> accountHolder,
            Map<String, String> inputParams)
            throws ApplicationException {
        AllAccountsViewDTO primaryAccountHolder = null;
        try {
            /**
             * Validating the Primary Account Holder information against the input and returns the primary account
             * holder information
             */
            JsonObject customerInfoJson =
                    new JsonParser().parse(inputParams.get("customerInformation")).getAsJsonArray().get(0)
                            .getAsJsonObject();
            for (AllAccountsViewDTO accountDTO : accountHolder) {
                List<MembershipOwnerDTO> holderDTOList = accountDTO.getMembershipownerDTO();
                for (MembershipOwnerDTO childDTO : holderDTOList) {
                    if (DBPUtilitiesConstants.MEMBERTYPE_PRIMARY.equalsIgnoreCase(childDTO.getMemberType())) {

                        boolean isFirstnameValid = childDTO.getFirstName()
                                .equalsIgnoreCase(JSONUtil.getString(customerInfoJson, "firstName"));
                        boolean isLastnameValid = childDTO.getLastName()
                                .equalsIgnoreCase(JSONUtil.getString(customerInfoJson, "lastName"));
                        boolean isEmailValid = childDTO.getEmail()
                                .equalsIgnoreCase(JSONUtil.getString(customerInfoJson, "email"));
                        boolean isPhoneValid = processPhone(childDTO.getPhone())
                                .equalsIgnoreCase(processPhone(JSONUtil.getString(customerInfoJson, "phone")));
                        boolean isSSNValid =
                                childDTO.getSsn().equalsIgnoreCase(JSONUtil.getString(customerInfoJson, "ssn"));
                        boolean isDOBValid = childDTO.getDateOfBirth()
                                .equalsIgnoreCase(JSONUtil.getString(customerInfoJson, "dateOfBirth"));
                        if (isFirstnameValid && isLastnameValid && isEmailValid && isPhoneValid && isSSNValid
                                && isDOBValid) {
                            primaryAccountHolder = accountDTO;
                            return primaryAccountHolder;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10247);
        }
        return primaryAccountHolder;
    }

    private String processPhone(String phone) {
        if (!StringUtils.contains(phone, "-") && !StringUtils.contains(phone, "+")) {
            phone = "+91-" + phone;
        }
        return phone;
    }

    @Override
    public JsonObject getAccountInformationServiceKeyParameters(AllAccountsViewDTO accountDTO) {
        JsonObject mandatoryParamList = new JsonObject();

        JsonArray accountInfoJsonArray = new JsonArray();
        JsonObject accounts = new JsonObject();
        JsonObject accountinfojson = new JsonObject();
        accountinfojson.addProperty("accountHolderName", accountDTO.getAccountHolderName());
        accountinfojson.addProperty("accountId", accountDTO.getAccountId());
        accountinfojson.addProperty("accountType", accountDTO.getAccountType());
        accountinfojson.addProperty("accountName", accountDTO.getAccountName());
        accountinfojson.addProperty("typeId", accountDTO.getTypeId());
        accountInfoJsonArray.add(accountinfojson);
        accounts.add(DBPUtilitiesConstants.ACCOUNTS, accountInfoJsonArray);
        mandatoryParamList.add(DBPUtilitiesConstants.ACCOUNTS, accounts);

        String backendId = "";
        for (MembershipOwnerDTO childDTO : accountDTO.getMembershipownerDTO()) {
            if (DBPUtilitiesConstants.MEMBERTYPE_PRIMARY.equalsIgnoreCase(childDTO.getMemberType())) {
                backendId = childDTO.getId();
            }
        }
        mandatoryParamList.addProperty(DBPUtilitiesConstants.BACKENDID, backendId);
        return mandatoryParamList;
    }

    @Override
    public boolean checkAccountIfAvailable(AccountsDTO accountDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        List<AccountsDTO> accountsDTO = new ArrayList<>();
        boolean isAccountAvailable = Boolean.TRUE;
        try {
            AccountsBackendDelegate accountsBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountsBackendDelegate.class);
            accountsDTO = accountsBackendDelegate.checkIfAccountAvailable(accountDTO, headersMap);

            for (AccountsDTO account : accountsDTO) {
                if (StringUtils.isNotBlank(account.getOrganizationId())
                        || StringUtils.isNotBlank(account.getUserId())) {
                    isAccountAvailable = false;
                }
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        }
        return isAccountAvailable;
    }

    @Override
    public List<AllAccountsViewDTO> getAllAccountsInformation(AllAccountsViewDTO accountDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        List<AllAccountsViewDTO> allAccountsViewDTO = new ArrayList<>();

        try {
            AccountInformationBackendDelegate accountsBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountInformationBackendDelegate.class);
            allAccountsViewDTO = accountsBackendDelegate.getAllAccountsInformation(accountDTO, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);
        }
        return allAccountsViewDTO;
    }

    @Override
    public String[] getUnUsedAccountsList(String accountsCSV, Map<String, Object> headersMap)
            throws ApplicationException {
        String[] validAccountsList = new String[0];
        try {
            if (StringUtils.isBlank(accountsCSV)) {
                return validAccountsList;
            }
            Map<String, Object> inputParams = new HashMap<>();
            String validAccountsCSV;
            inputParams.put("_accountsList", accountsCSV);
            JsonObject accountsListJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.GET_VALID_ORG_ACCOUNTS_LIST);
            if (null != accountsListJson && JSONUtil.hasKey(accountsListJson, DBPDatasetConstants.DATASET_RECORDS)
                    && accountsListJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray array = accountsListJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
                JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (null != object && JSONUtil.hasKey(object, "accountsList")) {
                        validAccountsCSV = object.get("accountsList").getAsString();
                        validAccountsList = StringUtils.split(validAccountsCSV, ",");
                    }
                }

            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10256);
        }

        return validAccountsList;
    }

    @Override
    public List<AccountTypeDTO> getAccountTypes(Map<String, Object> headersMap) throws ApplicationException {
        List<AccountTypeDTO> accountTypes = new ArrayList<>();

        try {
            AccountsBackendDelegate accountsBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountsBackendDelegate.class);
            accountTypes = accountsBackendDelegate.getAccountTypes(headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        }
        return accountTypes;
    }

    @Override
    public AccountsDTO getAccountDetailsByAccountID(String accountId, Map<String, Object> headersMap)
            throws ApplicationException {
        AccountsDTO dto = null;
        try {
            AccountsBackendDelegate accountsBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountsBackendDelegate.class);
            dto = accountsBackendDelegate.getAccountDetailsByAccountID(accountId, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10293);
        }
        return dto;
    }

    @Override
    public AccountsDTO createAccount(AccountsDTO dto, Map<String, Object> headersMap) throws ApplicationException {
        AccountsDTO resultDTO = null;
        try {
            AccountsBackendDelegate accountsBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountsBackendDelegate.class);
            resultDTO = accountsBackendDelegate.createAccount(dto, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10294);
        }
        return resultDTO;
    }

    @Override
    public AccountsDTO updateAccount(AccountsDTO dto, Map<String, Object> headersMap) throws ApplicationException {
        AccountsDTO resultDTO = null;
        try {
            AccountsBackendDelegate accountsBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountsBackendDelegate.class);
            resultDTO = accountsBackendDelegate.updateAccount(dto, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10295);
        }
        return resultDTO;
    }

    @Override
    public List<AccountsDTO> getOrganizationAccounts(AccountsDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        List<AccountsDTO> resultDTO = null;
        try {
            AccountsBackendDelegate accountsBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountsBackendDelegate.class);
            resultDTO = accountsBackendDelegate.getOrganizationAccounts(inputDTO, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10305);
        }
        return resultDTO;
    }

    @Override
    public List<AllAccountsViewDTO> getAccountHolderDetails(AllAccountsViewDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        List<AllAccountsViewDTO> resultDTO = null;
        try {
            AccountHolderBackendDelegate accountHolderBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(AccountHolderBackendDelegate.class);
            resultDTO = accountHolderBackendDelegate.getAccountInformation(inputDTO, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10307);
        }
        return resultDTO;
    }

    @Override
    public List<AllAccountsViewDTO> getUnUsedAccountsFromArray(List<AllAccountsViewDTO> dtoList,
            Map<String, Object> headersMap) throws ApplicationException {
        List<AllAccountsViewDTO> resultDTOList = new ArrayList<>();
        try {
            StringBuilder givenAccountsListString = new StringBuilder();
            for (AllAccountsViewDTO dto : dtoList) {
                String accountId = dto.getAccountId();
                if (StringUtils.isNotBlank(accountId)) {
                    givenAccountsListString.append(accountId).append(",");
                }

            }
            if (givenAccountsListString.length() > 0) {
                givenAccountsListString.replace(givenAccountsListString.length() - 1, givenAccountsListString.length(),
                        "");
            }
            AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            String[] accountsList = accountsBusinessDelegate.getUnUsedAccountsList(givenAccountsListString.toString(),
                    headersMap);
            for (AllAccountsViewDTO dto : dtoList) {
                for (String validAccount : accountsList) {
                    if (validAccount.equals(dto.getAccountId())) {
                        resultDTOList.add(dto);
                    }
                }

            }

        } catch (ApplicationException e) {
            alert.prepareError("Error occured while shortlisting the accounts").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Error occured while shortlisting the accounts").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);
        }

        return resultDTOList;
    }

    @Override
    public boolean checkIfUnUsedAccountsExistsByMembershipId(String membershipId, Map<String, Object> headersMap)
            throws ApplicationException {
        AllAccountsViewDTO accountdto = new AllAccountsViewDTO();
        accountdto.setMembershipId(membershipId);

        AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountsBusinessDelegate.class);
        List<AllAccountsViewDTO> dtoList = accountsBusinessDelegate.getAllAccountsInformation(accountdto, headersMap);
        if (null != dtoList) {
            dtoList = accountsBusinessDelegate.getUnUsedAccountsFromArray(dtoList, headersMap);
        }

        return (null != dtoList && !dtoList.isEmpty());

    }

    /**
     * Fetches the Account holders associated with the Organization accounts NOTE : Return the account holders that are
     * not present in DBXDB
     */
    @Override
    public List<AccountsDTO> getOrganizationAccountHolderDetails(List<AccountsDTO> orgAccounts,
            Map<String, Object> headersMap) throws ApplicationException {
        List<AccountsDTO> orgAccountHolders = new ArrayList<>();
        for (AccountsDTO accountDTO : orgAccounts) {
            try {
                AccountHolderBackendDelegate accountHoldersBackendDelegate = DBPAPIAbstractFactoryImpl
                        .getBackendDelegate(AccountHolderBackendDelegate.class);
                List<MembershipOwnerDTO> accountHolders = accountHoldersBackendDelegate
                        .getOrganizationAccountHolderDetails(accountDTO, headersMap);
                accountHolders = getValidAccountHolders(accountHolders, accountDTO.getOrganizationId(), headersMap);
                accountDTO.setMembershipownerDTOList(accountHolders);
                orgAccountHolders.add(accountDTO);
            } catch (ApplicationException e) {
                throw new ApplicationException(e.getErrorCodeEnum());
            } catch (Exception e) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10307);
            }
        }
        return orgAccountHolders;
    }

    private List<MembershipOwnerDTO> getValidAccountHolders(List<MembershipOwnerDTO> orgAccountHolders, String orgId,
            Map<String, Object> headersMap) throws ApplicationException {

        List<MembershipOwnerDTO> validAccountholders = new ArrayList<>();
        List<SignatoryTypeDTO> sigTypes = getSignatoryType(orgId, headersMap);

        for (MembershipOwnerDTO childDTO : orgAccountHolders) {
            if (childDTO.getSignatoryTypes() == null) {
                childDTO.setSignatoryTypes(sigTypes);
            }
            childDTO.setIsRetailUserPresent("false");
            CustomerDTO inputDTO = new CustomerDTO();
            List<CustomerDTO> responseDTO = new ArrayList<>();
            inputDTO.setSsn(childDTO.getSsn());
            inputDTO.setDateOfBirth(childDTO.getDateOfBirth());
            inputDTO.setLastName(childDTO.getLastName());
            inputDTO.setOrganization_Id(orgId);
            try {
                UserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(UserManagementBusinessDelegate.class);
                responseDTO = businessDelegate.getCustomerDetails(inputDTO, headersMap);
                if (responseDTO.isEmpty()) {
                    validAccountholders.add(childDTO);
                }
            } catch (Exception e) {
                alert.prepareError("Exception occured while fetching the customer details").log();
            }
        }

        return validAccountholders;
    }

    /**
     * Fetches the Authorized signatory types associated with the business types of the organisation
     * 
     * @param orgId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    private List<SignatoryTypeDTO> getSignatoryType(String orgId, Map<String, Object> headersMap)
            throws ApplicationException {
        OrganisationDTO inputDTO = new OrganisationDTO();
        inputDTO.setId(orgId);
        OrganisationDTO outputDTO = new OrganisationDTO();
        try {
            OrganizationBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(OrganizationBusinessDelegate.class);
            outputDTO = businessDelegate.getOrganization(inputDTO, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10310);
        }

        BusinessSignatoryDTO businessSignatoryDTO = new BusinessSignatoryDTO();
        businessSignatoryDTO.setBusinessTypeId(outputDTO.getBusinessTypeId());

        List<SignatoryTypeDTO> sigTypes = new ArrayList<>();

        try {
            BusinessSignatoryBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(BusinessSignatoryBusinessDelegate.class);
            List<BusinessSignatoryDTO> businessSignatories = businessDelegate
                    .getBusinessSignatories(businessSignatoryDTO, headersMap);
            SignatoryTypeBusinessDelegate sigTypebusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(SignatoryTypeBusinessDelegate.class);
            for (BusinessSignatoryDTO dto : businessSignatories) {
                SignatoryTypeDTO signatoryDTO = new SignatoryTypeDTO();
                signatoryDTO.setId(dto.getSignatoryId());
                SignatoryTypeDTO childDTO = sigTypebusinessDelegate.getSignatoryTypes(signatoryDTO, headersMap);
                sigTypes.add(childDTO);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10311);
        }
        return sigTypes;

    }

    @Override
    public List<AllAccountsViewDTO> getUnUsedAccountsFromMembership(String membershipId, Map<String, Object> headersMap)
            throws ApplicationException {
        AllAccountsViewDTO accountdto = new AllAccountsViewDTO();
        accountdto.setMembershipId(membershipId);

        AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountsBusinessDelegate.class);

        List<AllAccountsViewDTO> dtoList = accountsBusinessDelegate.getAllAccountsInformation(accountdto, headersMap);
        if (null != dtoList) {
            dtoList = accountsBusinessDelegate.getUnUsedAccountsFromArray(dtoList, headersMap);
        }
        return dtoList;
    }

    @Override
    public JSONObject getCustomerCentricAccounts(JsonObject serviceKeyPayload, JsonObject masterServiceKeyPayload,
            String serviceKey, String masterServiceKey, Map<String, Object> headersMap)
            throws ApplicationException, IOException {
        final String PAYLOAD_CIF_KEY = "Cif";
        final String RESULT_DATASET_NAME = "Accounts";
        final String PAYLOAD_LEI_KEY = "legalEntityId";
        boolean isMfaServiceUpdated = false;
        String cif = "";
        String legalEntityId = "";

        JsonObject serviceKeyRequestPayloadJson = null;
        JsonObject masterServiceKeyRequestPayloadJson = null;

        JSONObject accountsJson = new JSONObject();
        JSONArray accountsArray = new JSONArray();

        CoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CoreCustomerBusinessDelegate.class);
        MFAServiceBusinessDelegate mfaServiceBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(MFAServiceBusinessDelegate.class);

        if (JSONUtil.isJsonNotNull(serviceKeyPayload) && serviceKeyPayload.isJsonObject()) {
            if (JSONUtil.hasKey(serviceKeyPayload, "requestPayload")
                    && serviceKeyPayload.get("requestPayload").isJsonObject()) {
                serviceKeyRequestPayloadJson = serviceKeyPayload.get("requestPayload").getAsJsonObject();
                cif = serviceKeyRequestPayloadJson.has(PAYLOAD_CIF_KEY)
                        ? serviceKeyRequestPayloadJson.get(PAYLOAD_CIF_KEY).getAsString()
                        : "";
                legalEntityId = serviceKeyRequestPayloadJson.has(PAYLOAD_LEI_KEY)
                        ? serviceKeyRequestPayloadJson.get(PAYLOAD_LEI_KEY).getAsString()
                        : "";

                JsonArray cifArray = new JsonArray();
                cifArray.add(cif);
                boolean status = checkIfTheCoreCustomerIsEnrolled(cif, legalEntityId, headersMap);
                if (status) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10384);
                }
                DBXResult accountsResult = coreCustomerBD.getCoreCustomerAccounts(cifArray.toString(), legalEntityId, headersMap);
                JsonObject resultJson = (JsonObject) accountsResult.getResponse();
                if (JSONUtil.isJsonNotNull(resultJson) && JSONUtil.hasKey(resultJson, "coreCustomerAccounts")
                        && JSONUtil.getJsonArrary(resultJson, "coreCustomerAccounts") != null
                        && JSONUtil.getJsonArrary(resultJson, "coreCustomerAccounts").size() > 0
                        && JSONUtil.getJsonArrary(resultJson, "coreCustomerAccounts").get(0).getAsJsonObject()
                                .has("accounts")) {
                    JsonArray array = JSONUtil.getJsonArrary(resultJson, "coreCustomerAccounts").get(0)
                            .getAsJsonObject().get("accounts").getAsJsonArray();
                    accountsArray = new JSONArray(array.toString());
                    accountsJson.put(RESULT_DATASET_NAME, new JSONArray(array.toString()));

                }
            }
            if (accountsArray.length() == 0 || !accountsJson.has(RESULT_DATASET_NAME)
                    || accountsJson.optJSONArray(RESULT_DATASET_NAME).length() == 0) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10275);
            }
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_10258);
        }

        if (StringUtils.isNotBlank(masterServiceKey)) {
            masterServiceKeyRequestPayloadJson =
                    masterServiceKeyPayload.get("requestPayload").getAsJsonObject();
            if (JSONUtil.isJsonNotNull(masterServiceKeyPayload)
                    && JSONUtil.hasKey(masterServiceKeyPayload, "Accounts")
                    && masterServiceKeyPayload.get("Accounts").isJsonArray()
                    && mfaServiceBD.validateMasterServiceKeyAndServiceKey(masterServiceKeyRequestPayloadJson,
                            serviceKeyRequestPayloadJson)) {
                JsonArray array = masterServiceKeyPayload.get("Accounts").getAsJsonArray();

                for (int i = 0; i < accountsArray.length(); i++) {
                    JSONObject newAccountJson = accountsArray.getJSONObject(i);
                    JsonObject accountJson = new JsonObject();
                    accountJson.addProperty("accountId", newAccountJson.optString("accountId"));
                    accountJson.addProperty("coreCustomerId", cif);
                    array.add(accountJson);
                }
                isMfaServiceUpdated = mfaServiceBD.updateGivenServiceKeyRecord(masterServiceKeyPayload,
                        masterServiceKey, headersMap);

            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10276);
            }

        } else {
            JsonArray array = new JsonArray();
            for (int i = 0; i < accountsArray.length(); i++) {
                JSONObject newAccountJson = accountsArray.getJSONObject(i);
                JsonObject accountJson = new JsonObject();
                accountJson.addProperty("accountId", newAccountJson.optString("accountId"));
                accountJson.addProperty("coreCustomerId", cif);
                array.add(accountJson);
            }
            serviceKeyPayload.add("Accounts", array);
            isMfaServiceUpdated =
                    mfaServiceBD.updateGivenServiceKeyRecord(serviceKeyPayload, serviceKey, headersMap);
        }

        if (!isMfaServiceUpdated) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10277);
        }

        return accountsJson;
    }

    private boolean checkIfTheCoreCustomerIsEnrolled(String coreCustomerId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException {
        ContractCoreCustomerBusinessDelegate customerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        Set<String> givenCoreCustomers = new HashSet<>();
        Set<String> unusedCoreCustomers;
        givenCoreCustomers.add(coreCustomerId);
        unusedCoreCustomers = customerBD.getValidCoreContractCustomers(givenCoreCustomers, legalEntityId, headerMap);

        return unusedCoreCustomers.isEmpty();
    }

}
