package com.temenos.dbx.product.contract.businessdelegate.impl;

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
import com.dbp.core.util.JSONUtils;
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
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountsBackendDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;

public class CoreCustomerBusinessDelegateImpl implements CoreCustomerBusinessDelegate {
        private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private static final String ID = "id";
    private static final String ACCOUNTS = "accounts";
    private static final String CORE_CUSTOMER_ACCOUNTS = "coreCustomerAccounts";
    private static final String CORE_CUSTOMER_ID = "coreCustomerId";
    private static final String TAXID = "taxId";
    private static final String ADDRESSLINE1 = "addressLine1";
    private static final String ADDRESSLINE2 = "addressLine2";
    private static final String CITYNAME = "cityName";
    private static final String COUNTRY = "country";
    private static final String ZIPCODE = "zipCode";
    private static final String STATE = "state";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String INDUSTRY = "industry";
    private static final String ISBUSINESS = "isBusiness";
    private static final String NAME = "name";
    private static final String SECTORID = "sectorId";
    private static final String IDTYPE_ID = "legalDocumentName";
    private static final String IDVALUE = "legalId";
    private static final String IDISSUEDATE = "legalIssueDate";
    private static final String IDEXPIRYDATE = "legalExpiredDate";

    @Override
    public DBXResult searchCoreCustomers(Map<String, String> configurations, MembershipDTO membershipDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        try {
            CoreCustomerBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
            response = backendDelegate.searchCoreCustomers(membershipDTO, headersMap);
            JsonArray coreCustomerRecords = new JsonArray();
            JsonObject json = new JsonObject();
            if (response != null && response.getResponse() != null) {
                coreCustomerRecords = (JsonArray) response.getResponse();
            }
            if (coreCustomerRecords.size() > 0) {
                coreCustomerRecords = validateCoreCustomerDetails(configurations, coreCustomerRecords, headersMap);
            }
            response = new DBXResult();
            json.add(DBPDatasetConstants.DATASET_CUSTOMERS, coreCustomerRecords);
            response.setResponse(json);
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while searching core customers"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while searching core customers "
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10757);
        }
        return response;
    }

    @Override
    public DBXResult getCoreRelativeCustomers(Map<String, String> configurations, MembershipDTO membershipDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        try {
            CoreCustomerBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
            response = backendDelegate.getCoreRelativeCustomers(membershipDTO, headersMap);
            JsonArray coreCustomerRecords = new JsonArray();
            JsonObject json = new JsonObject();
            if (response != null && response.getResponse() != null) {
                coreCustomerRecords = (JsonArray) response.getResponse();
            }
            if (coreCustomerRecords.size() > 0) {
                coreCustomerRecords = validateCoreCustomerDetails(configurations, coreCustomerRecords, headersMap);
            }
            response = new DBXResult();
            json.add(DBPDatasetConstants.DATASET_CUSTOMERS, coreCustomerRecords);
            response.setResponse(json);
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while searching core customers"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while searching core customers "
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10760);
        }
        return response;
    }

    private JsonArray validateCoreCustomerDetails(Map<String, String> configurations, JsonArray jsonarray,
            Map<String, Object> headersMap)
            throws ApplicationException {
        Set<String> customerIdList = new HashSet<>();
        for (JsonElement jsonelement : jsonarray) {
            customerIdList.add(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
        }
        try {
            ContractCoreCustomerBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
            customerIdList = backendDelegate.getValidCoreContractCustomers(customerIdList, headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while searching core customers"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while searching core customers "
                    + e.getMessage()).log();
        }
        Set<String> sectorIdList =
                HelperMethods.splitString(configurations.get(BundleConfigurationHandler.BUSINESS_SECTORID_LIST), ",");
        for (JsonElement jsonelement : jsonarray) {
            boolean isRecordValid = customerIdList.contains(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
            jsonelement.getAsJsonObject().addProperty("isAssociated", String.valueOf(!isRecordValid));
            String sectorId = JSONUtil.getString(jsonelement.getAsJsonObject(), "sectorId");
            if (StringUtils.isNotBlank(sectorId)) {
                jsonelement.getAsJsonObject().addProperty("isBusinessType", sectorIdList.contains(sectorId));
            }
            jsonelement.getAsJsonObject().addProperty("sectorId", sectorId);
        }
        return jsonarray;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DBXResult getCoreCustomerAccounts(String coreCustomerIdList, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult accountsResponse = new DBXResult();
        JsonArray customersJsonArray = new JsonParser().parse(coreCustomerIdList).getAsJsonArray();
        try {
            CoreCustomerBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
            ContractAccountsBusinessDelegate contractAccountBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAccountsBusinessDelegate.class);
            JsonObject jsonobject = new JsonObject();
            JsonArray coreCustomerAccounts = new JsonArray();
            for (int i = 0; i < customersJsonArray.size(); i++) {
                MembershipDTO membershipDTO = new MembershipDTO();
                membershipDTO.setId(customersJsonArray.get(i).getAsString());
                DBXResult response = backendDelegate.getCoreCustomerAccounts(membershipDTO, headersMap);
                if (response != null && response.getResponse() != null) {
                    List<AllAccountsViewDTO> accountsList = (List<AllAccountsViewDTO>) response.getResponse();
                    String accounts = JSONUtils.stringifyCollectionWithTypeInfo(accountsList,
                            AllAccountsViewDTO.class);
                    JsonObject customerAccounts = new JsonObject();
                    customerAccounts.addProperty(CORE_CUSTOMER_ID, customersJsonArray.get(i).getAsString());
                    DBXResult contractAccounts = contractAccountBusinessDelegate.validateAccountsContractAssociation(
                            new JsonParser().parse(accounts).getAsJsonArray(), headersMap);
                    customerAccounts.add(ACCOUNTS, (JsonArray) contractAccounts.getResponse());
                    coreCustomerAccounts.add(customerAccounts);
                }
            }
            jsonobject.add(CORE_CUSTOMER_ACCOUNTS, coreCustomerAccounts);
            accountsResponse.setResponse(jsonobject);
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while fetching core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while fetching core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10785);
        }
        return accountsResponse;
    }

    @Override
    public DBXResult getCoreCustomerAccounts(String coreCustomerIdList, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult accountsResponse = new DBXResult();
        JsonArray customersJsonArray = new JsonParser().parse(coreCustomerIdList).getAsJsonArray();
        try {
            CoreCustomerBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
            ContractAccountsBusinessDelegate contractAccountBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractAccountsBusinessDelegate.class);
            JsonObject jsonobject = new JsonObject();
            JsonArray coreCustomerAccounts = new JsonArray();
            for (int i = 0; i < customersJsonArray.size(); i++) {
                MembershipDTO membershipDTO = new MembershipDTO();
                membershipDTO.setId(customersJsonArray.get(i).getAsString());
                membershipDTO.setCompanyLegalUnit(legalEntityId);
                DBXResult response = backendDelegate.getCoreCustomerAccounts(membershipDTO, headersMap);
                if (response != null && response.getResponse() != null) {
                    List<AllAccountsViewDTO> accountsList = (List<AllAccountsViewDTO>) response.getResponse();
                    String accounts = JSONUtils.stringifyCollectionWithTypeInfo(accountsList,
                            AllAccountsViewDTO.class);
                    JsonObject customerAccounts = new JsonObject();
                    customerAccounts.addProperty(CORE_CUSTOMER_ID, customersJsonArray.get(i).getAsString());
                    DBXResult contractAccounts = contractAccountBusinessDelegate.validateAccountsContractAssociation(
                            new JsonParser().parse(accounts).getAsJsonArray(), headersMap);
                    customerAccounts.add(ACCOUNTS, (JsonArray) contractAccounts.getResponse());
                    coreCustomerAccounts.add(customerAccounts);
                }
            }
            jsonobject.add(CORE_CUSTOMER_ACCOUNTS, coreCustomerAccounts);
            accountsResponse.setResponse(jsonobject);
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while fetching core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerBusinessDelegateImpl : Exception occured while fetching core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10785);
        }
        return accountsResponse;
    } 
    
    @Override
    public boolean checkIfTheCoreCustomerIsEnrolled(String coreCustomerId, Map<String, Object> headerMap)
            throws ApplicationException {
        ContractCoreCustomerBusinessDelegate coreCusustomerBD =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
        CoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CoreCustomerBusinessDelegate.class);

        JsonArray jsonArray;
        JsonArray cifArray = new JsonArray();
        cifArray.add(coreCustomerId);
        Set<String> givenCoreCustomers = new HashSet<>();
        Set<String> unusedCoreCustomers;
        givenCoreCustomers.add(coreCustomerId);
        unusedCoreCustomers = coreCusustomerBD.getValidCoreContractCustomers(givenCoreCustomers, headerMap);
        if (unusedCoreCustomers.isEmpty()) {
            return true;
        }

        DBXResult accountsResult = coreCustomerBD.getCoreCustomerAccounts(cifArray.toString(), headerMap);
        JsonObject resultJson = (JsonObject) accountsResult.getResponse();
        if (JSONUtil.isJsonNotNull(resultJson) && JSONUtil.hasKey(resultJson, CORE_CUSTOMER_ACCOUNTS)
                && JSONUtil.getJsonArrary(resultJson, CORE_CUSTOMER_ACCOUNTS) != null
                && JSONUtil.getJsonArrary(resultJson, CORE_CUSTOMER_ACCOUNTS).size() > 0
                && JSONUtil.getJsonArrary(resultJson, CORE_CUSTOMER_ACCOUNTS).get(0).getAsJsonObject()
                        .has(ACCOUNTS)
                && JSONUtil.getJsonArrary(resultJson, CORE_CUSTOMER_ACCOUNTS).get(0).getAsJsonObject()
                        .get(ACCOUNTS).isJsonArray()) {
            jsonArray = JSONUtil.getJsonArrary(resultJson, CORE_CUSTOMER_ACCOUNTS).get(0).getAsJsonObject()
                    .get(ACCOUNTS).getAsJsonArray();
            if (jsonArray.size() > 0) {
                return false;
            } else if (jsonArray.size() == 0) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10275);
            }
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_10275);
        }

        return true;
    }

    @Override
    public List<MembershipDTO> getCoreRelativeCustomers(String membershipId, Map<String, Object> headersMap)
            throws ApplicationException {
        CoreCustomerBackendDelegate coreCsutomerBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CoreCustomerBackendDelegate.class);

        List<MembershipDTO> resultList = new ArrayList<>();
        MembershipDTO dto = new MembershipDTO();
        dto.setId(membershipId);
        DBXResult result = coreCsutomerBD.getCoreRelativeCustomers(dto, headersMap);
        if (null != result && result.getResponse() != null) {
            JsonArray relativeCustomers = (JsonArray) result.getResponse();
            for (JsonElement element : relativeCustomers) {
                JsonObject coreCustomerJson = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
                String id = coreCustomerJson.has("id") ? coreCustomerJson.get("id").getAsString() : "";
                String firstName =
                        coreCustomerJson.has("firstName") ? coreCustomerJson.get("firstName").getAsString() : "";
                String lastName =
                        coreCustomerJson.has("lastName") ? coreCustomerJson.get("lastName").getAsString() : "";
                String dateOfBirth =
                        coreCustomerJson.has("dateOfBirth") ? coreCustomerJson.get("dateOfBirth").getAsString() : "";
                String taxId =
                        coreCustomerJson.has("taxId") ? coreCustomerJson.get("taxId").getAsString() : "";
                String phone =
                        coreCustomerJson.has("phone") ? coreCustomerJson.get("phone").getAsString() : "";
                String email =
                        coreCustomerJson.has("email") ? coreCustomerJson.get("email").getAsString() : "";
                if (StringUtils.isNotBlank(id)) {
                    MembershipDTO membershipDTO = new MembershipDTO();
                    membershipDTO.setId(id);
                    membershipDTO.setFirstName(firstName);
                    membershipDTO.setLastName(lastName);
                    membershipDTO.setDateOfBirth(dateOfBirth);
                    membershipDTO.setTaxId(taxId);
                    membershipDTO.setPhone(phone);
                    membershipDTO.setEmail(email);

                    resultList.add(membershipDTO);
                }
            }
        }

        return resultList;
    }

    @Override
    public MembershipDTO getMembershipDetails(String membershipId, Map<String, Object> headesrMap)
            throws ApplicationException {
        CoreCustomerBackendDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CoreCustomerBackendDelegate.class);

        MembershipDTO membershipDTO = new MembershipDTO();
        MembershipDTO resultMembershipDTO = new MembershipDTO();
        membershipDTO.setId(membershipId);
        DBXResult customerResult =
                coreCustomerBD.searchCoreCustomers(membershipDTO, headesrMap);

        if (customerResult != null && StringUtils.isBlank(customerResult.getDbpErrCode())
                && StringUtils.isBlank(customerResult.getDbpErrMsg())
                && customerResult.getResponse() != null) {
            JsonArray customerArray = (JsonArray) customerResult.getResponse();
            JsonObject customerResponse =
                    customerArray.size() > 0 ? customerArray.get(0).getAsJsonObject()
                            : new JsonObject();
            String taxId =
                    customerResponse.has(TAXID) ? customerResponse.get(TAXID).getAsString()
                            : "";
            String addressLine1 =
                    customerResponse.has(ADDRESSLINE1)
                            ? customerResponse.get(ADDRESSLINE1).getAsString()
                            : "";
            String addressLine2 =
                    customerResponse.has(ADDRESSLINE2)
                            ? customerResponse.get(ADDRESSLINE2).getAsString()
                            : "";
            String cityName =
                    customerResponse.has(CITYNAME)
                            ? customerResponse.get(CITYNAME).getAsString()
                            : "";
            String country =
                    customerResponse.has(COUNTRY) ? customerResponse.get(COUNTRY).getAsString()
                            : "";
            String zipcode =
                    customerResponse.has(ZIPCODE) ? customerResponse.get(ZIPCODE).getAsString()
                            : "";
            String state =
                    customerResponse.has(STATE) ? customerResponse.get(STATE).getAsString()
                            : "";
            String phone =
                    customerResponse.has(PHONE) ? customerResponse.get(PHONE).getAsString()
                            : "";
            String email =
                    customerResponse.has(EMAIL) ? customerResponse.get(EMAIL).getAsString()
                            : "";
            String industry =
                    customerResponse.has(INDUSTRY) ? customerResponse.get(INDUSTRY).getAsString()
                            : "";
            String isBusiness =
                    customerResponse.has(ISBUSINESS) ? customerResponse.get(ISBUSINESS).getAsString()
                            : "";
            String name =
                    customerResponse.has(NAME) ? customerResponse.get(NAME).getAsString()
                            : "";

            String sectorId =
                    customerResponse.has(SECTORID) ? customerResponse.get(SECTORID).getAsString()
                            : "";

            String IDType_id = customerResponse.has(IDTYPE_ID)
                    ? getIdentityType().get(customerResponse.get(IDTYPE_ID).getAsString())
                    : "";

            String IDValue = customerResponse.has(IDVALUE) ? customerResponse.get(IDVALUE).getAsString() : "";

            String IDIssueDate =
                    customerResponse.has(IDISSUEDATE) ? customerResponse.get(IDISSUEDATE).getAsString() : "";

            String IDExpiryDate =
                    customerResponse.has(IDEXPIRYDATE) ? customerResponse.get(IDEXPIRYDATE).getAsString() : "";

            if (StringUtils.isBlank(isBusiness) && StringUtils.isNotBlank(sectorId)) {
                if (Integer.parseInt(sectorId) >= 2000) {
                    isBusiness = DBPUtilitiesConstants.BOOLEAN_STRING_TRUE;
                } else {
                    isBusiness = DBPUtilitiesConstants.BOOLEAN_STRING_FALSE;
                }
            }
            resultMembershipDTO.setId(membershipId);
            resultMembershipDTO.setName(name);
            resultMembershipDTO.setTaxId(taxId);
            resultMembershipDTO.setAddressLine1(addressLine1);
            resultMembershipDTO.setAddressLine2(addressLine2);
            resultMembershipDTO.setCityName(cityName);
            resultMembershipDTO.setCountry(country);
            resultMembershipDTO.setZipCode(zipcode);
            resultMembershipDTO.setState(state);
            resultMembershipDTO.setPhone(phone);
            resultMembershipDTO.setEmail(email);
            resultMembershipDTO.setIndustry(industry);
            resultMembershipDTO.setIsBusiness(isBusiness);
            resultMembershipDTO.setSectorId(sectorId);
            resultMembershipDTO.setIDType_id(IDType_id);
            resultMembershipDTO.setIDValue(IDValue);
            resultMembershipDTO.setIDIssueDate(IDIssueDate);
            resultMembershipDTO.setIDExpiryDate(IDExpiryDate);
            resultMembershipDTO.setFirstName(JSONUtil.getString(customerResponse, "firstName"));
            resultMembershipDTO.setLastName(JSONUtil.getString(customerResponse, "lastName"));

        }

        return resultMembershipDTO;
    }

    private Map<String, String> getIdentityType() {
        Map<String, String> idTypeMap = new HashMap<String, String>();
        idTypeMap.put("DRIVERS.LICENSE", "ID_DRIVING_LICENSE");
        idTypeMap.put("PASSPORT", "ID_PASSPORT");
        idTypeMap.put("MILITARY.ID", "Military ID");
        idTypeMap.put("NATIONAL.ID", "ID State");
        idTypeMap.put("FED.GOVT.ID", "Foreign Government ID");
        return idTypeMap;

    }

    @Override
    public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, Map<String, Object> headerMap)
            throws ApplicationException {
        CoreCustomerBackendDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CoreCustomerBackendDelegate.class);
        return coreCustomerBD.getMembershipDetailsByTaxid(taxID, companyName, headerMap);
    }

    @Override
    public MembershipDTO verifyGivenAuthorizerDetailsAndGetData(String firstName, String lastName, String ssn,
            String dateOfBirth, List<MembershipDTO> dtoList) {
        if (null == dtoList) {
            return null;
        }
        for (MembershipDTO dto : dtoList) {
            String authorizerFirstName = dto.getFirstName();
            String authorizerLastName = dto.getLastName();
            String authorizerDOB = dto.getDateOfBirth();
            String authorizerSsn = dto.getTaxId();

            if (firstName.equalsIgnoreCase(authorizerFirstName) && lastName.equalsIgnoreCase(authorizerLastName)
                    && ssn.equalsIgnoreCase(authorizerSsn) && dateOfBirth.equalsIgnoreCase(authorizerDOB)) {
                return dto;

            }
        }
        return null;
    }

    @Override
    public JSONObject getCompanyAccounts(JsonObject serviceKeyPayload, JsonObject masterServiceKeyPayload,
            String serviceKey, String masterServiceKey, Map<String, Object> headersMap)
            throws ApplicationException, IOException {
        final String PAYLOAD_CIF_KEY = "Cif";
        final String RESULT_DATASET_NAME = "Accounts";
        boolean isMfaServiceUpdated = false;
        String cif = "";

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

                JsonArray cifArray = new JsonArray();
                cifArray.add(cif);
                boolean status = coreCustomerBD.checkIfTheCoreCustomerIsEnrolled(cif, headersMap);
                if (status) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10384);
                }
                DBXResult accountsResult = coreCustomerBD.getCoreCustomerAccounts(cifArray.toString(), headersMap);
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

    @Override
    public Map<String, List<ContractAccountsDTO>> getAccountsWithImplicitAccountAccess(Set<String> corecustomers,
            String customerId, Map<String, Object> headersMap) throws ApplicationException {
        CoreCustomerBackendDelegate corecustomerBD =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
        AccountsBackendDelegate accountsBD =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountsBackendDelegate.class);

        Map<String, List<ContractAccountsDTO>> map = new HashMap<>();
        List<MembershipDTO> membershipList = new ArrayList<>();
        MembershipDTO membershipDTO = null;
        for (String coreCustomer : corecustomers) {
            membershipDTO = new MembershipDTO();
            membershipDTO.setId(coreCustomer);
            membershipList.add(membershipDTO);
        }

        List<AllAccountsViewDTO> allAccounts = corecustomerBD.getCoreCustomerAccounts(membershipList, headersMap);
        Map<String, String> processedAccounts = accountsBD.processNewAccounts(allAccounts, customerId, headersMap);
        for (AllAccountsViewDTO dto : allAccounts) {
            List<ContractAccountsDTO> corecustomerAccounts = new ArrayList<>();
            if (processedAccounts.containsKey(dto.getAccountId())) {
                if (map.containsKey(dto.getMembershipId())) {
                    corecustomerAccounts = map.get(dto.getMembershipId());
                }
                addToResult(corecustomerAccounts, dto, processedAccounts.get(dto.getAccountId()));
                map.put(dto.getMembershipId(), corecustomerAccounts);
            }
        }
        return map;
    }

    private void addToResult(List<ContractAccountsDTO> resultContractAccounts, AllAccountsViewDTO dto, String isNew) {
        ContractAccountsDTO contractAccountDTO = new ContractAccountsDTO();
        contractAccountDTO.setAccountId(dto.getAccountId());
        contractAccountDTO.setAccountName(dto.getAccountName());
        contractAccountDTO.setAccountType(dto.getAccountType());
        contractAccountDTO.setAccountHolderName(dto.getAccountHolderName());
        contractAccountDTO.setArrangementId(dto.getArrangementId());
        contractAccountDTO.setCoreCustomerId(dto.getMembershipId());
        contractAccountDTO.setOwnerType(dto.getOwnership());
        contractAccountDTO.setStatusDesc(dto.getAccountStatus());
        if ("1".equalsIgnoreCase(isNew))
            contractAccountDTO.setIsNew(isNew);
        resultContractAccounts.add(contractAccountDTO);
    }

    @Override
    public List<MembershipDTO> getMembershipDetails(String dateofbirth, String email, String phone,
            Map<String, Object> headerMap) throws ApplicationException {
        CoreCustomerBackendDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CoreCustomerBackendDelegate.class);

        MembershipDTO membershipDTO = new MembershipDTO();
        List<MembershipDTO> resultList = new ArrayList<>();
        MembershipDTO resultMembershipDTO;
        membershipDTO.setDateOfBirth(dateofbirth);
        membershipDTO.setEmail(email);
        DBXResult customerResult =
                coreCustomerBD.searchCoreCustomers(membershipDTO, headerMap);

        String[] array = phone.split("-");
        String phoneNumber = array.length > 1 && StringUtils.isNotBlank(array[1]) ? array[1] : phone;

        if (customerResult != null && StringUtils.isBlank(customerResult.getDbpErrCode())
                && StringUtils.isBlank(customerResult.getDbpErrMsg())
                && customerResult.getResponse() != null) {
            JsonArray customerArray = (JsonArray) customerResult.getResponse();
            for (JsonElement element : customerArray) {
                resultMembershipDTO = new MembershipDTO();

                JsonObject customerResponse = element.getAsJsonObject();
                String id =
                        customerResponse.has(ID) ? customerResponse.get(ID).getAsString()
                                : "";
                String taxId =
                        customerResponse.has(TAXID) ? customerResponse.get(TAXID).getAsString()
                                : "";
                String addressLine1 =
                        customerResponse.has(ADDRESSLINE1)
                                ? customerResponse.get(ADDRESSLINE1).getAsString()
                                : "";
                String addressLine2 =
                        customerResponse.has(ADDRESSLINE2)
                                ? customerResponse.get(ADDRESSLINE2).getAsString()
                                : "";
                String cityName =
                        customerResponse.has(CITYNAME)
                                ? customerResponse.get(CITYNAME).getAsString()
                                : "";
                String country =
                        customerResponse.has(COUNTRY) ? customerResponse.get(COUNTRY).getAsString()
                                : "";
                String zipcode =
                        customerResponse.has(ZIPCODE) ? customerResponse.get(ZIPCODE).getAsString()
                                : "";
                String state =
                        customerResponse.has(STATE) ? customerResponse.get(STATE).getAsString()
                                : "";

                String industry =
                        customerResponse.has(INDUSTRY) ? customerResponse.get(INDUSTRY).getAsString()
                                : "";
                String isBusiness =
                        customerResponse.has(ISBUSINESS) ? customerResponse.get(ISBUSINESS).getAsString()
                                : "";
                String name =
                        customerResponse.has(NAME) ? customerResponse.get(NAME).getAsString()
                                : "";

                String sectorId =
                        customerResponse.has(SECTORID) ? customerResponse.get(SECTORID).getAsString()
                                : "";

                String phoneFromSearch =
                        customerResponse.has(PHONE) ? customerResponse.get(PHONE).getAsString()
                                : "";

                if (StringUtils.isBlank(isBusiness) && StringUtils.isNotBlank(sectorId)) {
                    if (Integer.parseInt(sectorId) >= 2000) {
                        isBusiness = DBPUtilitiesConstants.BOOLEAN_STRING_TRUE;
                    } else {
                        isBusiness = DBPUtilitiesConstants.BOOLEAN_STRING_FALSE;
                    }
                }
                resultMembershipDTO.setId(id);
                resultMembershipDTO.setName(name);
                resultMembershipDTO.setTaxId(taxId);
                resultMembershipDTO.setAddressLine1(addressLine1);
                resultMembershipDTO.setAddressLine2(addressLine2);
                resultMembershipDTO.setCityName(cityName);
                resultMembershipDTO.setCountry(country);
                resultMembershipDTO.setZipCode(zipcode);
                resultMembershipDTO.setState(state);
                resultMembershipDTO.setPhone(phone);
                resultMembershipDTO.setEmail(email);
                resultMembershipDTO.setIndustry(industry);
                resultMembershipDTO.setIsBusiness(isBusiness);
                resultMembershipDTO.setSectorId(sectorId);

                if (phoneFromSearch.contains(phoneNumber)) {
                    resultList.add(resultMembershipDTO);
                }

            }
        }

        return resultList;
    }
}
