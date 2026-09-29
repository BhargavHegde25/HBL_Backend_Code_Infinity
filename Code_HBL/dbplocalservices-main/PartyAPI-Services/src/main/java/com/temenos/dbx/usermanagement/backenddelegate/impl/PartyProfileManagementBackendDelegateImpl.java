package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.StatusEnum;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.BackendIdentifiersBackendDelegateimpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.ProfileManagementBackendDelegateImpl;
import com.temenos.dbx.party.businessdelegate.impl.PartyCustomerBusinessDelegateImpl;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.businessdelegate.api.SystemConfigurationBusinessDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.AlternateIdentity;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.dto.PartyIdentifier;
import com.temenos.dbx.product.dto.PasswordLockoutSettingsDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.constants.PartyProfileDetailsConstants;
import com.temenos.dbx.usermanagement.dto.PartyDetails;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.dto.DBXResult;
import java.util.Map.Entry;

public class PartyProfileManagementBackendDelegateImpl implements ProfileManagementBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static LoggerUtil logger = new LoggerUtil(PartyProfileManagementBackendDelegateImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public DBXResult getCustomerForUserResponse(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        Map<String, String> map = new HashMap<String, String>();

        String partyId = "";
        String customerId = customerDTO.getId();

        dbxResult = new ProfileManagementBackendDelegateImpl().getCustomerForUserResponse(customerDTO, headerMap);

        if (dbxResult.getResponse() != null) {
            map = (Map<String, String>) dbxResult.getResponse();
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerId);
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
        backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                partyId = identifierDTO.getBackendId();
            }
        } catch (ApplicationException e1) {
            alert.prepareError("Error while fetching backend identifier for backend ID " + customerId).log();
        }

        PartyDTO partyDTO = new PartyDTO();
        String employments = "[]";
        String partyIdentifiers = "[]";
        try {
            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyId);

            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            partyDTO.loadFromJson(jsonObject);
            employments = jsonObject.has("employments") && jsonObject.get("employments").isJsonArray()
                    ? jsonObject.get("employments").getAsJsonArray().toString()
                    : new JsonArray().toString();
            partyIdentifiers = jsonObject.has("partyIdentifiers") && jsonObject.get("partyIdentifiers").isJsonArray()
                    ? jsonObject.get("partyIdentifiers").getAsJsonArray().toString()
                    : new JsonArray().toString();
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
            partyDTO = null;
        }

        if (partyDTO != null) {

            map.put("FirstName", partyDTO.getFirstName());
            map.put("LastName", partyDTO.getLastName());
            map.put("MiddleName", partyDTO.getMiddleName());
            map.put("Gender", partyDTO.getGender());
            map.put("DateOfBirth", partyDTO.getDateOfBirth());
            map.put("NoOfDependents", partyDTO.getNoOfDependents());
            if (StringUtils.isBlank(map.get("id"))) {
                map.put("id", partyDTO.getPartyId());
            }

            List<PartyIdentifier> identifiers = partyDTO.getPartyIdentifier();
            if (identifiers != null && identifiers.size() > 0) {
                for (PartyIdentifier identifier : identifiers) {
                    if (identifier.getType().equals(DTOConstants.SOCIAL_SECURITY_NUMBER)) {
                        map.put("Ssn", identifier.getIdentifierNumber());
                    } else if (identifier.getType().equals(DTOConstants.DRIVER_LICENSE)) {
                        map.put("DrivingLicenseNumber", identifier.getIdentifierNumber());
                    }
                }
            }

            map.put("MaritalStatus_id", partyDTO.getMaritalStatus());
            map.put("employments", employments);
            map.put("identifiers", partyIdentifiers);
            dbxResult.setResponse(map);

        }

        dbxResult.setResponse(map);

        return dbxResult;
    }

    @Override
    public DBXResult getUserResponse(CustomerDTO customerDTO, Map<String, Object> map) {

        ProfileManagementBackendDelegateImpl backendDelegateImpl = new ProfileManagementBackendDelegateImpl();
        return backendDelegateImpl.getUserResponse(customerDTO, map);
    }

    @Override
    public DBXResult createCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();

        logger = new LoggerUtil(PartyProfileManagementBackendDelegateImpl.class);

        PartyDTO partyDTO = new PartyDTO();

        partyDTO = PartyUtils.buildPartyDTO(customerDTO, partyDTO);

        String party = partyDTO.toStringJson().toString();

        diagnostic.prepareDebug("PartyDTO for create Party Service is : " + party).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_CREATE);

        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL, party, headerMap);

        String id = "";
        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if (jsonObject.has("id")) {

                id = jsonObject.get("id").getAsString();
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while parsing the save Party response: ", e).log();
            dbxResult.setDbpErrMsg("Party create Failed");
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        dbxResult.setResponse(id);

        return dbxResult;
    }

    private PartySearchDTO buildSearchPartyDTO(Map<String, String> inputMap) {
        PartySearchDTO partySearchDTO = new PartySearchDTO();
        DTOUtils.loadInputIntoDTO(partySearchDTO, inputMap, false);
        return partySearchDTO;
    }

    @Override
    public DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        JsonObject resultJsonObject = new JsonObject();
        dbxResult.setResponse(resultJsonObject);

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerDTO.getId());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
        backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());
        DBXResult backendResult = new DBXResult();
        try {
            backendResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e1) {
            alert.prepareError("Error while fetching backend identifier for customer ID " + customerDTO.getId()).log();
        }

        PartyDTO partyDTO = new PartyDTO();

        String partyId = null;
        String legalEntityId = null;

        if (backendResult.getResponse() != null) {
            BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) backendResult.getResponse();

            partyId = identifierDTO.getBackendId();
            legalEntityId = identifierDTO.getCompanyLegalUnit();
        }

        if (StringUtils.isBlank(partyId)) {
            PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
            DBXResult response = new DBXResult();
            partyDTO.setPartyId(customerDTO.getId());
            response = managementBusinessDelegate.get(partyDTO, headerMap);
            if (response.getResponse() != null) {
                JsonObject jsonObject = (JsonObject) response.getResponse();
                if (jsonObject.has(PartyConstants.partyId) && !jsonObject.get(PartyConstants.partyId).isJsonNull()) {
                    partyId = customerDTO.getId();
                }
            }
        }

        if (StringUtils.isBlank(partyId)) {

            PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
            DBXResult response = new DBXResult();
            JsonArray partyJsonArray = new JsonArray();
            Map<String, String> inputMap = new HashMap<String, String>();
            legalEntityId = customerDTO.getCompanyLegalUnit();
            inputMap.put("alternateIdentifierNumber", legalEntityId + "-" + customerDTO.getId());
            inputMap.put("alternateIdentifierType", PartyConstants.BackOfficeIdentifier);
            PartySearchDTO searchDTO = buildSearchPartyDTO(inputMap);
            headerMap = PartyUtils.addJWTAuthHeader(null, headerMap, AuthConstants.PRE_LOGIN_FLOW);
            headerMap.put("companyid", legalEntityId);
            response = managementBusinessDelegate.searchParty(searchDTO, headerMap);
            if (response.getResponse() != null) {
                JsonObject party = (JsonObject) response.getResponse();
                partyJsonArray =
                        party.has(PartyConstants.parties) && party.get(PartyConstants.parties).isJsonArray()
                                ? party.get(PartyConstants.parties).getAsJsonArray()
                                : new JsonArray();
            }

            if (partyJsonArray.size() > 0) {
                JsonObject party = partyJsonArray.get(0).isJsonObject() ? partyJsonArray.get(0).getAsJsonObject()
                        : new JsonObject();
                if (party.has(PartyConstants.partyId) && !party.get(PartyConstants.partyId).isJsonNull()) {
                    partyId = party.get(PartyConstants.partyId).getAsString();
                }
            }
        }
        
        
        if (StringUtils.isBlank(partyId)) {
            ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject, "party backendIdentifier not found");
            resultJsonObject.addProperty("errmsg", "party backendIdentifier not found");
            return dbxResult;
        }

        try {
            PartyDetails partyDetails = null;
            if (customerDTO.getSource() != null) {
                if (customerDTO.getSource().contentEquals(PartyProfileDetailsConstants.PARAM_SOURCE)) {
                    if (customerDTO.getOperation()
                            .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATE)
                            || customerDTO.getOperation()
                                    .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_UPDATE)) {
                        partyDetails = PartyUtils.buildPartyDTOforEmailPhone(customerDTO);
                        dbxResult = updatePartyProfile(partyDetails, headerMap, partyId, customerDTO);
                        return dbxResult;
                    } else if (customerDTO.getOperation()
                            .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATEADDRESS)
                            || customerDTO.getOperation()
                                    .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_UPDATEADDRESS)) {
                        partyDetails = PartyUtils.buildPartyDTOforAddress(customerDTO, partyId, headerMap);
                        dbxResult = updatePartyProfile(partyDetails, headerMap, partyId, customerDTO);
                        return dbxResult;
                    } else if (customerDTO.getOperation()
                            .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_DELETE)
                            || customerDTO.getOperation()
                                    .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_DELETEADDRESS)) {
                        dbxResult = deletePartyAddress(headerMap, partyId, customerDTO);
                        return dbxResult;
                    }

                }
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ", e).log();
            ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject, "Unable to update Party");
            resultJsonObject.addProperty("errmsg", "Unable to update Party ");
            return dbxResult;
        }

        if (StringUtils.isNotBlank(customerDTO.getOperation()) && (customerDTO.getOperation()
                .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_DELETE)
                || customerDTO.getOperation()
                        .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_DELETEADDRESS))) {
            dbxResult = deletePartyAddress(headerMap, partyId, customerDTO);
            return dbxResult;
        }

        try {
            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyId);

            DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            partyDTO.loadFromJson(jsonObject);
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
            ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject, "Unable to get Party ");
            resultJsonObject.addProperty("errmsg", "Unable to get Party ");
            return dbxResult;
        }

        try {
            partyDTO = PartyUtils.buildPartyDTO(customerDTO, partyDTO);

        } catch (Exception e) {
            alert.prepareError("Caught exception while updating party from Customer DTO: ", e).log();
            ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject, "Unable to convert Customer to Party");
            resultJsonObject.addProperty("errmsg", "Unable to convert Customer to Party");
            return dbxResult;
        }
        String party = partyDTO.toStringJson().toString();
        
		// This code is to remove Party identifiers from update party
		JsonObject partyJsonObj = new JsonParser().parse(party).getAsJsonObject();
		if (partyJsonObj.has("partyIdentifiers")) {
			JsonArray partyJsonArray = partyJsonObj.get("partyIdentifiers").getAsJsonArray();
			if (partyJsonArray != null) {
				for (int i = partyJsonArray.size() - 1; i >= 0; i--) {
					partyJsonObj.get("partyIdentifiers").getAsJsonArray().remove(i);
				}
			}
		}
		party = partyJsonObj.toString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : " + party).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, partyId);

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL,
                party, headerMap);

        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();

            if (jsonObject.has("id")) {
                resultJsonObject.addProperty("success", "success");
                resultJsonObject.addProperty("Status", "Operation successful");
                resultJsonObject.addProperty("status", "Operation successful");
            } else {
                ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject,
                        "Unable to update Party " + jsonObject.get("message").getAsString());
                resultJsonObject.addProperty("errmsg",
                        "Unable to update Party " + jsonObject.get("message").getAsString());

                return dbxResult;
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ", e).log();
            ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject, "Unable to update Party");
            resultJsonObject.addProperty("errmsg", "Unable to update Party ");
            return dbxResult;
        }

        return dbxResult;
    }

    @Override
    public DBXResult verifyCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap, String deploymentPlatform)
            throws JSONException, UnsupportedEncodingException, DBPApplicationException, MiddlewareException {

        DBXResult dbxResult = new DBXResult();
        JsonObject response = new JsonObject();

        if (StringUtils.isNotBlank(customerDTO.getDateOfBirth()) && customerDTO.getCustomerCommuncation() != null
                && customerDTO.getCustomerCommuncation().size() != 2) {
            JsonObject record = new JsonObject();
            response.add(DBPUtilitiesConstants.USR_ATTR, record);
            dbxResult.setError(ErrorCodeEnum.ERR_10023);
            return dbxResult;
        }

        ProfileManagementBackendDelegateImpl backendDelegateImpl = new ProfileManagementBackendDelegateImpl();
        try {
            dbxResult = backendDelegateImpl.verifyCustomer(customerDTO, headerMap,deploymentPlatform);
        } catch (ApplicationException e1) {
        	alert.prepareError("Exception", e1).log();        }
        JsonObject dbxResponseJsonObject = new JsonObject();
        JsonArray dbxJsonArray = new JsonArray();
        if (dbxResult.getResponse() != null) {
            dbxResponseJsonObject = (JsonObject) dbxResult.getResponse();
            if (dbxResponseJsonObject.has(DBPUtilitiesConstants.USR_ATTR)) {
                dbxJsonArray = dbxResponseJsonObject.get(DBPUtilitiesConstants.USR_ATTR).getAsJsonArray();
            }
        }

        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        String phonenumber="";
        StringBuilder queryParams = new StringBuilder();
        
        for (CustomerCommunicationDTO dto : customerDTO.getCustomerCommuncation()) {
            if (queryParams.length() > 1) {
                queryParams.append("&");
            }
            if (DBPUtilitiesConstants.COMM_TYPE_EMAIL.equalsIgnoreCase(dto.getType_id()))
                queryParams.append("emailId=" + dto.getValue());
            if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equalsIgnoreCase(dto.getType_id())) {
                phonenumber = dto.getValue();
                String[] phoneno = phonenumber.split("-");
                 queryParams.append("contactNumber=" + phoneno[1]);
            }
        }	
        if (StringUtils.isNotBlank(customerDTO.getDateOfBirth())) {
            if (queryParams.length() > 1) {
                queryParams.append("&");
            }
            queryParams.append("dateOfBirth=" + customerDTO.getDateOfBirth());
        }
        JsonArray partyJsonArray = new JsonArray();
       /* if (queryParams.length() > 1) {
            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_SEARCH5) + queryParams;
            dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
            if (dbxResult.getResponse() != null) {
                try {
                    JsonObject partyResponse =
                            new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
                    if (partyResponse.has("parties") && partyResponse.get("parties").isJsonArray())
                        partyJsonArray = partyResponse.get("parties").getAsJsonArray();
                } catch (Exception e) {
                    alert.prepareError("Caught error while parsing partysearch response ", e).log();
                }
            }
        }*/

        JsonArray jsonArray = new JsonArray();
        if (partyJsonArray.size() > 0) {
            BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
            BackendIdentifierDTO backendIdentifierDTO;
            for (int i = 0; i < partyJsonArray.size(); i++) {
                PartyDTO partyDTO = new PartyDTO();
                JsonObject partyJson = partyJsonArray.get(i).getAsJsonObject();
                partyDTO.loadFromJson(partyJson);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("DateOfBirth", partyDTO.getDateOfBirth());
                jsonObject.addProperty("FirstName",
                        (StringUtils.isNotBlank(partyDTO.getFirstName()) ? partyDTO.getFirstName() : ""));
                jsonObject.addProperty("LastName",
                        (StringUtils.isNotBlank(partyDTO.getLastName()) ? partyDTO.getLastName() : ""));
                jsonObject.addProperty("Ssn", getSSN(partyDTO.getPartyIdentifier()));
                backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO.setBackendId(partyDTO.getPartyId());
                backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
                dbxResult = backendDelegateimpl.get(backendIdentifierDTO, headerMap);
                if (dbxResult.getResponse() != null) {
                    backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                    customerDTO = new CustomerDTO();
                    customerDTO = (CustomerDTO) customerDTO.loadDTO(backendIdentifierDTO.getCustomer_id());
                    jsonObject.addProperty("Status_id", customerDTO.getStatus_id());
                    jsonObject.addProperty("CustomerTypeId", customerDTO.getCustomerType_id());
                    jsonObject.addProperty("id", customerDTO.getId());
                    jsonObject.addProperty("UserName", customerDTO.getUserName());
                    Map<String, Object> map = new HashMap<>();
                    String activationToken = UUID.randomUUID().toString();
                    map.put("id", activationToken);
                    map.put("UserName", customerDTO.getUserName());
                    map.put("linktype", HelperMethods.CREDENTIAL_TYPE.RESETPASSWORD.toString());
                    map.put("createdts", HelperMethods.getCurrentTimeStamp());
                    ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
                            URLConstants.CREDENTIAL_CHECKER_CREATE);
                    jsonObject.addProperty(MFAConstants.SECURITY_KEY, activationToken);
                    jsonArray.add(jsonObject);
                }
            }
        }
        jsonArray.addAll(dbxJsonArray);
        if (jsonArray.size() <= 0) {
            response.addProperty(DBPUtilitiesConstants.IS_USER_EXISTS, "false");
        } else {
            response.addProperty(DBPUtilitiesConstants.IS_USER_EXISTS, "true");
            response.add(DBPUtilitiesConstants.USR_ATTR, jsonArray);
        }
        dbxResult.setResponse(response);
        return dbxResult;
    }

    @Override
    public DBXResult searchCustomer(Map<String, String> configurations, MemberSearchBean memberSearchBean,
            Map<String, Object> headerMap, PasswordHistoryManagement pm){
        
        DBXResult dbxResult = new DBXResult();
        JsonObject processedResult = new JsonObject();
        JsonObject searchResults = new JsonObject();
        String IS_Integrated = Boolean.toString(PartyUtils.getIntegratedFlag());
        
        ProfileManagementBackendDelegateImpl backend = new ProfileManagementBackendDelegateImpl();
        boolean isCustomerSearch = false;
        if (StringUtils.isNotBlank(memberSearchBean.getCustomerId())) {
            memberSearchBean.setMemberId(memberSearchBean.getCustomerId());
            isCustomerSearch = true;
            memberSearchBean.setCustomerId(null);
        }
       
        if (isCustomerSearch && StringUtils.isNotBlank(memberSearchBean.getMemberId())
                && !PartyUtils.getIntegratedFlag()
                && !backend.memberPresent(memberSearchBean.getMemberId(), headerMap)) {
            dbxResult.setError(ErrorCodeEnum.ERR_10335);
            return dbxResult;
        } else if (!isCustomerSearch && StringUtils.isNotBlank(memberSearchBean.getMemberId())
                && !PartyUtils.getIntegratedFlag()
                && backend.memberPresent(memberSearchBean.getMemberId(), headerMap)) {
            dbxResult.setError(ErrorCodeEnum.ERR_10335);
            return dbxResult;
        }

    
        if (memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.APPLICANT_SEARCH)) {
            processedResult.addProperty("TotalResultsFound", 0);
            searchResults = backend.searchCustomers(headerMap, memberSearchBean.getSearchType(), memberSearchBean);
        }
        if (memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.GROUP_SEARCH)) {
            searchResults = backend.searchCustomers(headerMap, memberSearchBean.getSearchType(), memberSearchBean);
            if (searchResults.has("records1") && searchResults.get("records1").getAsJsonArray().size() > 0) {
                if (searchResults.get("records1").getAsJsonArray().get(0).getAsJsonObject().has("SearchMatchs")) {
                    processedResult.addProperty("TotalResultsFound", searchResults.get("records1").getAsJsonArray()
                            .get(0).getAsJsonObject().get("SearchMatchs").getAsInt());
                } else {
                    processedResult.addProperty("TotalResultsFound", 0);
                }
            }
        }
        if (memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.CUSTOMER_SEARCH)) {
            searchResults = backend.searchCustomers(headerMap, memberSearchBean.getSearchType(), memberSearchBean);
            if (searchResults.has("records1") && searchResults.get("records1").getAsJsonArray().size() > 0) {
                if (searchResults.get("records1").getAsJsonArray().get(0).getAsJsonObject().has("SearchMatchs")) {
                    processedResult.addProperty("TotalResultsFound", searchResults.get("records1").getAsJsonArray()
                            .get(0).getAsJsonObject().get("SearchMatchs").getAsInt());
                } else {
                    processedResult.addProperty("TotalResultsFound", "0");
                }
            }
        }
       

        JsonArray recordsArray = new JsonArray();
        int erasureStatusCount = 0;
        if (searchResults.has("records")) {

            recordsArray = searchResults.get("records").getAsJsonArray();
            if(recordsArray.size() > 0) {
                JsonArray filteredRecordsArray = new JsonArray();
                for(JsonElement recordElementItem: recordsArray){
                    JsonObject recordObjItem = (JsonObject) recordElementItem;
                    if(recordObjItem.has("Status_id")
                        && recordObjItem.get("Status_id").getAsString().equals(StatusEnum.SID_CUS_ERASURE_INPROGRESS.name())
                        || recordObjItem.get("Status_id").getAsString().equals(StatusEnum.SID_CUS_ERASURE_COMPLETED.name())){
                        erasureStatusCount++;
                    } else {
                        filteredRecordsArray.add(recordObjItem);
                    }
                }
                recordsArray = filteredRecordsArray;
            }
            processedResult.add("records", recordsArray);
          
        }
       
        if ((StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")
                && (StringUtils.isBlank(memberSearchBean.getMemberId()) || isCustomerSearch))
                && !memberSearchBean.getIsMicroServiceFlow()) {
            processedResult = searchCustomerinParty(recordsArray, configurations, memberSearchBean, headerMap,
                    isCustomerSearch, pm);
            backend.mergeResults(recordsArray, headerMap);
            backend.mergeResultsFromLeadMS(recordsArray, headerMap);
        }
        if(erasureStatusCount > 0){
            processedResult.addProperty("ErasureStatusRecordsFound", true);
        }

        processedResult.addProperty("Status", "Records returned: " + recordsArray.size());

        processedResult.addProperty("TotalResultsFound", recordsArray.size());

        processedResult.add("records", recordsArray);
       
        if (recordsArray.size() == 1
                && memberSearchBean.getSearchType().equalsIgnoreCase(DTOConstants.CUSTOMER_SEARCH)
                && !memberSearchBean.getIsMicroServiceFlow()) {

            String customerId = recordsArray.get(0).getAsJsonObject().has("id")
                    ? recordsArray.get(0).getAsJsonObject().get("id").getAsString()
                    : recordsArray.get(0).getAsJsonObject().get(InfinityConstants.primaryCustomerId).getAsString();
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(customerId);
            customerDTO.setCompanyLegalUnit(memberSearchBean.getCompanyLegalUnit());
           
			dbxResult = getBasicInformation(configurations, customerDTO, headerMap, isCustomerSearch, pm);

            if (dbxResult.getResponse() != null) {
                JsonObject jsonObject = (JsonObject) dbxResult.getResponse();

                for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    processedResult.add(entry.getKey(), entry.getValue());
                }
            }

        }
        Set<String> enrolledCustomers = new HashSet<>();
        for (JsonElement recordElement : recordsArray) {
        	backend.isAssociated(recordElement, headerMap);
            if (!Boolean.parseBoolean(
                    recordElement.getAsJsonObject().get(InfinityConstants.isProfileExist).getAsString())) {
                recordElement.getAsJsonObject().addProperty(InfinityConstants.isEnrolled, "false");
            }
            if (recordElement.getAsJsonObject().has(InfinityConstants.id)) {
    			enrolledCustomers.add(recordElement.getAsJsonObject().get(InfinityConstants.id).getAsString());
    		}
        }
        
		Map<String, Set<String>> infinitycustomerLegalEntities = CommonUtils.getCustomerLegalEntities(enrolledCustomers,
				headerMap);

		CommonUtils.generateLegalEntitiesForCustomers(recordsArray, infinitycustomerLegalEntities);
		dbxResult.setResponse(processedResult);

        if (recordsArray.size() == 0) {
            dbxResult.setError(ErrorCodeEnum.ERR_10335);
        }

        return dbxResult;
	}

	

    private String getSSN(List<PartyIdentifier> partyIdentifiers) {
        if (partyIdentifiers == null || partyIdentifiers.size() <= 0)
            return null;
        for (PartyIdentifier partyIdentifier : partyIdentifiers) {
            if (partyIdentifier.getType().equals(DTOConstants.SOCIAL_SECURITY_NUMBER)) {
                return partyIdentifier.getIdentifierNumber();
            }
        }

        return null;
    }

    @Override
    public DBXResult getBasicInformation(Map<String, String> configurations, CustomerDTO customerDTO,
            Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {

        DBXResult dbxResult = new DBXResult();
        String customerId = customerDTO.getId();
        String username = customerDTO.getUserName();
        String legalEntityId = customerDTO.getCompanyLegalUnit();
        
        JsonObject basicResult = new JsonObject();
   
        if (StringUtils.isBlank(customerId) && StringUtils.isNotBlank(legalEntityId)) {
            if (StringUtils.isBlank(username)) {
                ErrorCodeEnum.ERR_20612.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            }
        }

        if (StringUtils.isBlank(customerId) && StringUtils.isNotBlank(legalEntityId)) {
            customerDTO = (CustomerDTO) customerDTO.loadDTO();
            if (customerDTO != null) {
                customerId = customerDTO.getId();
                legalEntityId = customerDTO.getCompanyLegalUnit();
            } else {
                ErrorCodeEnum.ERR_20688.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            }
        }
        
        
       ProfileManagementBackendDelegateImpl backendDelegateImpl = new ProfileManagementBackendDelegateImpl();
        JsonObject customerViewJson = new JsonObject();

        Map<String, Object> postParametersMap = new HashMap<String, Object>();
        postParametersMap.put("_customerId", customerId);
        postParametersMap.put("_legalEntityId", legalEntityId);
        JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                URLConstants.CUSTOMER_BASIC_INFO_PROC);
        String primaryCustomerId = "";
        final String IS_Integrated = Boolean.toString(PartyUtils.getIntegratedFlag());
        if (jsonobject != null && jsonobject.has(Param.OPSTATUS) && jsonobject.get(Param.OPSTATUS).getAsInt() == 0
                && jsonobject.has("records")) {

            if (jsonobject.get("records").getAsJsonArray().size() == 0
                    && (StringUtils.isBlank(IS_Integrated) || !IS_Integrated.equalsIgnoreCase("true"))) {
                ErrorCodeEnum.ERR_20539.setErrorCode(basicResult);
                basicResult.addProperty("Status", "Failure");
                dbxResult.setResponse(basicResult);
                return dbxResult;
            } else if (jsonobject.get("records").getAsJsonArray().size() != 0) {
                customerViewJson = jsonobject.get("records").getAsJsonArray().get(0).getAsJsonObject();
            }

            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                dbxResult = getBasicInfoFromParty(customerViewJson, configurations, customerDTO, headerMap, isCustomerSearch, pm);

                basicResult = ((JsonObject) dbxResult.getResponse());

                if (basicResult.has("customerbasicinfo_view")) {
                    basicResult = basicResult.has("customerbasicinfo_view")
                            && basicResult.get("customerbasicinfo_view").isJsonObject()
                                    ? basicResult.get("customerbasicinfo_view").getAsJsonObject()
                                    : new JsonObject();
                    for (Entry<String, JsonElement> entry : basicResult.entrySet()) {
                        customerViewJson.add(entry.getKey(), entry.getValue());
                    }

                    basicResult = new JsonObject();

                    basicResult.add("customerbasicinfo_view", customerViewJson);

                    dbxResult.setResponse(basicResult);

                    return dbxResult;
                }
            }

        } else {
            ErrorCodeEnum.ERR_20689.setErrorCode(basicResult);
            basicResult.addProperty("Status", "Failure");
            dbxResult.setResponse(basicResult);
            return dbxResult;
        }

        if (customerViewJson.has(InfinityConstants.Customer_id)) {
            customerViewJson.add(InfinityConstants.id, customerViewJson.get(InfinityConstants.Customer_id));
        }

        boolean isAssociated = false;

        if (StringUtils.isBlank(IS_Integrated) || !IS_Integrated.equalsIgnoreCase("true")) {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

            backendIdentifierDTO.setBackendType(DTOConstants.CORE);

            if (isCustomerSearch) {
                backendIdentifierDTO.setBackendId(customerDTO.getId());
               
            } else {
                backendIdentifierDTO.setCustomer_id(customerDTO.getId());
               
            }
            backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());
            
            try {
                dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                        .get(backendIdentifierDTO, headerMap);
                if (dbxResult.getResponse() != null) {
                    BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                    primaryCustomerId = identifierDTO.getBackendId();
                    customerId = identifierDTO.getCustomer_id();
                } else {
                	 
                    backendIdentifierDTO = new BackendIdentifierDTO();
                    backendIdentifierDTO.setBackendType(DTOConstants.CORE);
                    backendIdentifierDTO.setBackendId(customerDTO.getId());

                    dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                            .get(backendIdentifierDTO, headerMap);
                    if (dbxResult.getResponse() != null) {
                        BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                        primaryCustomerId = identifierDTO.getBackendId();
                       
                        customerId = identifierDTO.getCustomer_id();
                       
                    }
                }
            } catch (ApplicationException e1) {
                // TODO Auto-generated catch block
                alert.prepareError("Error while fetching backend identifier for backend ID " + customerDTO.getId()).log();
            }

            if (StringUtils.isBlank(primaryCustomerId)) {
                primaryCustomerId = customerViewJson.get("id").getAsString();
            }
        }

        if (customerViewJson.has(InfinityConstants.Username)
                && !customerViewJson.get(InfinityConstants.Username).isJsonNull()
                && customerViewJson.has(InfinityConstants.id)
                && !customerViewJson.get(InfinityConstants.id).isJsonNull()) {
            if (!customerViewJson.get(InfinityConstants.Username).getAsString()
                    .equals(customerViewJson.get(InfinityConstants.id).getAsString())) {
                isAssociated = true;
            }
        }

        customerViewJson.addProperty(InfinityConstants.primaryCustomerId, primaryCustomerId);

        if (StringUtils.isNotBlank(customerId)
                && (!customerId.equals(primaryCustomerId) || (customerId.equals(primaryCustomerId)
                        && customerViewJson.has("Username") && !customerViewJson.get("Username").isJsonNull()
                        && !customerId.equals(customerViewJson.get("Username").getAsString())))) {
            // CustomerDTO userDTO = (CustomerDTO) new CustomerDTO().loadDTO(customerId);
            customerViewJson.addProperty(InfinityConstants.isProfileExist, "true");
            // customerViewJson.addProperty(InfinityConstants.isEnrolled, "" + userDTO.getIsEnrolled());
            // customerViewJson.addProperty("CustomerStatus_id", userDTO.getStatus_id());
        } else if (customerId.equals(primaryCustomerId)) {
            customerViewJson.addProperty(InfinityConstants.isProfileExist, "false");
            // customerViewJson.addProperty(InfinityConstants.isEnrolled, "" + "false");
        }

        if (!isAssociated) {
        	backendDelegateImpl.getCustomerType(customerViewJson, customerViewJson.get(InfinityConstants.id).getAsString(), headerMap, customerDTO.getCompanyLegalUnit());
            customerViewJson.remove(InfinityConstants.id);
            customerViewJson.remove(InfinityConstants.Customer_id);
        } else {
        	backendDelegateImpl.isAssociated(customerViewJson, headerMap);
            customerViewJson.getAsJsonObject().addProperty(InfinityConstants.id, customerId);
            customerViewJson.addProperty("Customer_id", customerId);
        }

        String statusId = customerViewJson.has("CustomerStatus_id")
                ? customerViewJson.get("CustomerStatus_id").getAsString()
                : "";
        String isEnrolled = customerViewJson.has("isEnrolled") ? customerViewJson.get("isEnrolled").getAsString() : "";

        CredentialCheckerDTO credentialCheckerDTO = new CredentialCheckerDTO();
        credentialCheckerDTO.setUserName(customerDTO.getUserName());
        credentialCheckerDTO.setLinktype(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
        credentialCheckerDTO = (CredentialCheckerDTO) credentialCheckerDTO.loadDTO();
        customerViewJson.addProperty(DBPUtilitiesConstants.IS_CUSTOMER_ENROLLED, isEnrolled);
        customerViewJson.addProperty(DBPUtilitiesConstants.CUSTOMER_STATUS, statusId);

        if (credentialCheckerDTO == null) {
            customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "false");
        } else if (credentialCheckerDTO != null) {
            customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "true");
        }

        customerViewJson.addProperty("isCustomerAccessiable", true);
        basicResult.add("customerbasicinfo_view", customerViewJson);

        JsonObject configuration = new JsonObject();
        configuration.addProperty("value", customerViewJson.get("accountLockoutTime").getAsString());
        basicResult.add("Configuration", configuration);
        String currentStatus;

        String lockedOnTS = "";
        if (customerViewJson.has("lockedOn")) {
            lockedOnTS = customerViewJson.get("lockedOn").getAsString();
        }
        if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {
            currentStatus = "LOCKED";
        } else if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_SUSPENDED.name())) {
            currentStatus = "SUSPENDED";
        } else if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_ACTIVE.name())) {
            currentStatus = "ACTIVE";
        } else {
            currentStatus = "NEW";
        }

        if (customerViewJson.get("CustomerStatus_id").getAsString()
                .equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {

            if (StringUtils.isNotBlank(lockedOnTS)) {
                String lockDuration = "0";

                if (customerViewJson.has("accountLockoutTime")) {
                    lockDuration = customerViewJson.get("accountLockoutTime").getAsString();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Calendar elapsedLockedOnDate = Calendar.getInstance();
                try {
                    elapsedLockedOnDate.setTime(dateFormat.parse(lockedOnTS));
                } catch (ParseException e) {

                }
                elapsedLockedOnDate.add(Calendar.MINUTE, Integer.parseInt(lockDuration));
                Calendar currentDate = Calendar.getInstance();

                if (elapsedLockedOnDate.before(currentDate)) {
                    // Time has been elapsed. Calling unlock service
                    postParametersMap = new HashMap<String, Object>();
                    postParametersMap.put("id", customerId);
                    postParametersMap.put("lockCount", "0");
                    postParametersMap.put("lockedOn", "");
                    JsonObject unlockResponse = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                            URLConstants.CUSTOMER_UPDATE);

                    basicResult.addProperty("unlockStatus", unlockResponse.toString());
                    if (unlockResponse == null || !unlockResponse.has(Param.OPSTATUS)
                            || unlockResponse.get(Param.OPSTATUS).getAsInt() != 0) {
                        ErrorCodeEnum.ERR_20538.setErrorCode(basicResult);
                        dbxResult.setResponse(basicResult);
                        return dbxResult;
                    }
                    // set updated status
                    currentStatus = "ACTIVE";
                }
            }
        }

        JsonObject statusResponse = new JsonObject();
        statusResponse.addProperty("LockedOn", lockedOnTS);
        statusResponse.addProperty("Status", currentStatus);
        customerViewJson.add("OLBCustomerFlags", statusResponse);
        if (customerViewJson.get("CustomerType_id").getAsString()
                .equalsIgnoreCase(HelperMethods.getCustomerTypes().get("Prospect"))) {
            // Add address in basic information if the customer is of type prospect

            postParametersMap = new HashMap<String, Object>();
            postParametersMap.put(DBPUtilitiesConstants.FILTER, "CustomerId eq '" + customerId + "'");
            postParametersMap.put(DBPUtilitiesConstants.SELECT,
                    "Address_id,AddressType,AddressLine1,AddressLine2,ZipCode,CityName,City_id,RegionName,Region_id,RegionCode,CountryName,Country_id,CountryCode,isPrimary");
            JsonObject readCustomerAddr = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                    URLConstants.CUSTOMER_ADDRESS_VIEW_GET);

            if (readCustomerAddr == null || !readCustomerAddr.has(Param.OPSTATUS)
                    || readCustomerAddr.get(Param.OPSTATUS).getAsInt() != 0
                    || !readCustomerAddr.has("customeraddress_view")) {
                ErrorCodeEnum.ERR_20881.setErrorCode(basicResult);

                dbxResult.setResponse(basicResult);
                return dbxResult;

            }
            JsonArray addressDataset = readCustomerAddr.get("customeraddress_view").getAsJsonArray();
            customerViewJson.add("Addresses", addressDataset);

        }

        dbxResult =

                getCustomerRequestNotificationCount(customerDTO, headerMap);
        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                customerViewJson.add(entry.getKey(), entry.getValue());
            }
        }

        basicResult.add("customerbasicinfo_view", customerViewJson);

        dbxResult.setResponse(basicResult);

        return dbxResult;
    }

    public DBXResult getCustomerRequestNotificationCount(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();
        if (customerDTO == null || StringUtils.isBlank(customerDTO.getId())) {
            // ErrorCodeEnum.ERR_20865.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }
        Map<String, Object> postParametersMap = new HashMap<String, Object>();
        postParametersMap.put(DBPUtilitiesConstants.FILTER, "customerId eq '" + customerDTO.getId() + "'");

        JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
                URLConstants.CARD_REQUEST_NOTIFICATION_COUNT_VIEW);

        if (result == null || !result.has("card_request_notification_count_view")) {
            // ErrorCodeEnum.ERR_20302.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }

        int totalRequestCount = 0, totalNotificationCount = 0;
        JsonArray countArray = result.get("card_request_notification_count_view").getAsJsonArray();
        JsonObject currRecordJSONObject = null;

        int currRequestCount;
        for (int indexVar = 0; indexVar < countArray.size(); indexVar++) {
            if (countArray.get(indexVar).isJsonObject()) {
                currRecordJSONObject = countArray.get(indexVar).getAsJsonObject();

                if (currRecordJSONObject.has("reqType")) {
                    currRequestCount = 0;
                    if (currRecordJSONObject.has("requestcount")) {
                        currRequestCount = currRecordJSONObject.get("requestcount").getAsInt();
                    }
                    if (currRecordJSONObject.has("reqType")) {
                        if (currRecordJSONObject.get("reqType").getAsString()
                                .equalsIgnoreCase(DTOConstants.REQUEST_TYPE_IDENTIFIER)) {
                            totalRequestCount += currRequestCount;
                        } else if (currRecordJSONObject.get("reqType").getAsString()
                                .equalsIgnoreCase(DTOConstants.NOTIFICATION_TYPE_IDENTIFIER)) {
                            totalNotificationCount += currRequestCount;
                        }
                    }
                }
            }

            jsonObject.addProperty("requestCount", String.valueOf(totalRequestCount));
            jsonObject.addProperty("notificationCount", String.valueOf(totalNotificationCount));

            dbxResult.setResponse(jsonObject);
        }

        return dbxResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DBXResult getCustomerDetailsToAdmin(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        String currencyCode = HelperMethods.getCurrencyCode(customerDTO.getCountryCode());

        try {
            customerDTO.setDateOfBirth(
                    HelperMethods.convertDateFormat(customerDTO.getDateOfBirth(), "yyyy-MM-dd'T'hh:mm:ss'Z'"));
        } catch (ParseException e) {
        	alert.prepareError("Exception", e).log();
        }

        JsonObject record = DTOUtils.getJsonObjectFromObject(customerDTO, true);
        record.addProperty("currencyCode", currencyCode);
        record.addProperty("dateOfBirth", record.get("DateOfBirth").getAsString());

        CommunicationBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);

        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
        communicationDTO.setCustomer_id(customerDTO.getId());

        dbxResult = backendDelegate.get(communicationDTO, headerMap);
        if (dbxResult.getResponse() != null) {
            List<CustomerCommunicationDTO> dtoList = (List<CustomerCommunicationDTO>) dbxResult.getResponse();
            if (dtoList != null && dtoList.size() > 0) {
                for (CustomerCommunicationDTO rec : dtoList) {
                    if (rec.getIsPrimary()) {
                        String type = rec.getType_id();
                        String key = "";
                        if ("COMM_TYPE_EMAIL".equalsIgnoreCase(type)) {
                            key = "Email";
                        } else {
                            key = "Phone";
                        }
                        record.addProperty(key, rec.getValue());
                    }
                }
            }
        }

        dbxResult.setResponse(record);
        return dbxResult;

    }

    @Override
    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> map) {

        DBXResult dbxResult = new DBXResult();

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerDTO.getId());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, map);
        } catch (ApplicationException e1) {
            alert.prepareError("Error while fetching backend identifier for customer ID " + customerDTO.getId()).log();
        }

        PartyDTO partyDTO = new PartyDTO();
        JsonObject jsonObject = new JsonObject();
        try {
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();

                PartyUtils.addJWTAuthHeader(map, AuthConstants.PRE_LOGIN_FLOW);
                String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                        PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, identifierDTO.getBackendId());

                DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                        partyURL, null, map);
                jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
                partyDTO.loadFromJson(jsonObject);

                try {
                    PartyUtils.updateCustomerDTOFromPartyDTO(customerDTO, partyDTO);

                    jsonObject = DTOUtils.getJsonObjectFromObject(customerDTO, true);

                } catch (Exception e) {
                    alert.prepareError("Caught exception while converting Party to Customer: ", e).log();
                    partyDTO = null;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
            partyDTO = null;
        }

        dbxResult.setResponse(jsonObject);

        return dbxResult;
    }

    @Override
    public DBXResult checkifUserEnrolled(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

        String queryParams = "?";

        if (StringUtils.isNotBlank(customerDTO.getLastName())) {
            if (queryParams.length() > 1) {
                queryParams += "&";
            }
            queryParams += "lastName=" + customerDTO.getLastName();
        }
        if (StringUtils.isNotBlank(customerDTO.getDateOfBirth())) {
            if (queryParams.length() > 1) {
                queryParams += "&";
            }
            queryParams += "dateOfBirth=" + customerDTO.getDateOfBirth();
        }
        if (StringUtils.isNotBlank(customerDTO.getSsn())) {
            if (queryParams.length() > 1) {
                queryParams += "&";
            }
            queryParams += "identifierNumber=" + customerDTO.getSsn() + "&identifierType="
                    + DTOConstants.SOCIAL_SECURITY_NUMBER;
            queryParams = queryParams.replace(" ", "%20");

        }

        JsonArray partyJsonArray = new JsonArray();
        if (queryParams.length() > 1) {

            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_SEARCH) + queryParams;

            dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);

            if (dbxResult.getResponse() != null) {
                try {
                    JsonObject partyResponse =
                            new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
                    if (partyResponse.has("parties") && partyResponse.get("parties").isJsonArray())
                        partyJsonArray = partyResponse.get("parties").getAsJsonArray();
                } catch (Exception e) {
                    alert.prepareError("Caught error while parsing partysearch response ", e).log();
                }
            }
        }

        JsonObject jsonObject = new JsonObject();

        CustomerDTO responseDTO = null;

        if (partyJsonArray.size() > 0) {

            for (int i = 0; i < partyJsonArray.size(); i++) {
                BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
                BackendIdentifierDTO backendIdentifierDTO;
                PartyDTO partyDTO = new PartyDTO();
                JsonObject partyJson = partyJsonArray.get(i).getAsJsonObject();
                partyDTO.loadFromJson(partyJson);
                backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO.setBackendId(partyDTO.getPartyId());
                backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
                dbxResult = backendDelegateimpl.get(backendIdentifierDTO, headerMap);
                backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                responseDTO = (CustomerDTO) customerDTO.loadDTO(backendIdentifierDTO.getCustomer_id());
                if (responseDTO != null) {
                    break;
                }
            }

        }

        if (responseDTO != null) {
            boolean isEnrolled = responseDTO.getIsEnrolled();
            boolean isEnrolledFromSpotlight = "1".equals(responseDTO.getIsEnrolledFromSpotlight());

            String password = responseDTO.getPassword();
            String isUserEnrolled = "";
            if (isEnrolled) {
                isUserEnrolled = "true";
                jsonObject.addProperty("result", "User Already Enrolled");
            } else if (StringUtils.isBlank(password)
                    || (StringUtils.isNotBlank(password) && !isEnrolledFromSpotlight)) {
                isUserEnrolled = "false";
                jsonObject.addProperty("result", "User Not Enrolled");
                String userId = responseDTO.getId();
                JsonObject communication = getCommunicationData(userId, headerMap);
                JsonObject requestPayload = getRequestPayload(responseDTO);
                String communicationString = communication.toString();
                String requestPayloadString = requestPayload.toString();
                jsonObject.addProperty("communication", communicationString);
                jsonObject.addProperty("requestPayload", requestPayloadString);
            }
            jsonObject.addProperty("isUserEnrolled", isUserEnrolled);
        } else {
            JsonObject requestPayload = getRequestPayload(customerDTO);
            String requestPayloadString = requestPayload.toString();
            jsonObject.addProperty("requestPayload", requestPayloadString);
            jsonObject.addProperty(DBPUtilitiesConstants.VALIDATION_ERROR, "No Record Found");
        }

        dbxResult.setResponse(jsonObject);
        return dbxResult;
    }

    private JsonObject getRequestPayload(CustomerDTO customerDTO) {
        JsonObject payload = new JsonObject();
        payload.addProperty(DBPUtilitiesConstants.C_SSN, customerDTO.getSsn());
        payload.addProperty(DBPInputConstants.CUSTOMER_LAST_NAME, customerDTO.getLastName());
        payload.addProperty(DBPUtilitiesConstants.C_DOB, customerDTO.getDateOfBirth());
        return payload;
    }

    @SuppressWarnings("unchecked")
    public JsonObject getCommunicationData(String user_id, Map<String, Object> headerMap) {

        JsonObject communication = new JsonObject();

        CommunicationBackendDelegate backendDelegateImpl =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);

        CustomerCommunicationDTO dto = new CustomerCommunicationDTO();
        dto.setCustomer_id(user_id);

        DBXResult dbxResult = backendDelegateImpl.get(dto, headerMap);

        List<CustomerCommunicationDTO> dtoList = (List<CustomerCommunicationDTO>) dbxResult.getResponse();

        JsonArray phone = new JsonArray();
        JsonArray email = new JsonArray();
        JsonObject contact = new JsonObject();

        if (dtoList != null && dtoList.size() > 0) {
            for (CustomerCommunicationDTO record : dtoList) {
                contact = new JsonObject();
                if (record.getType_id().equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
                    contact.addProperty("unmasked", record.getValue());
                    if (record.getIsPrimary()) {
                        contact.addProperty("isPrimary", "true");
                    }
                    email.add(contact);
                } else {
                    String mobile = record.getValue();
                    contact.addProperty("unmasked", mobile);
                    if (record.getIsPrimary()) {
                        contact.addProperty("isPrimary", "true");
                    }
                    phone.add(contact);
                }
            }
            communication.add("phone", phone);
            communication.add("email", email);
        }

        return communication;
    }

    @Override
    public DBXResult sendCustomerUnlockEmail(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        JsonObject jsonObject = new JsonObject();

        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (customerDTO == null) {
            jsonObject.addProperty("mailRequestSent", "false");
            ErrorCodeEnum.ERR_10192.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }

        CommunicationBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
        communicationDTO.setCustomer_id(customerDTO.getId());
        dbxResult = backendDelegate.getPrimaryCommunicationForLogin(communicationDTO, headerMap);
        String email = "";
        if (dbxResult.getResponse() != null) {
            JsonObject communicaiton = (JsonObject) dbxResult.getResponse();
            communicaiton = communicaiton.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                    ? communicaiton.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonObject()
                    : new JsonObject();
            email = communicaiton.has(DTOConstants.EMAIL) ? communicaiton.get(DTOConstants.EMAIL).getAsString() : "";
        }

        if (StringUtils.isBlank(email)) {
            jsonObject.addProperty("mailRequestSent", "false");
            ErrorCodeEnum.ERR_10193.setErrorCode(jsonObject);
            dbxResult.setResponse(jsonObject);
            return dbxResult;
        }

        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + customerDTO.getUserName();
        Map<String, Object> inputParam = new HashMap<String, Object>();
        inputParam.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject checkerResult =
                ServiceCallHelper.invokeServiceAndGetJson(inputParam, headerMap, URLConstants.CREDENTIAL_CHECKER_GET);
        if (checkerResult.has(DBPDatasetConstants.DATASET_CREDENTIALCHECKER)) {

            JsonArray existingRecords =
                    checkerResult.get(DBPDatasetConstants.DATASET_CREDENTIALCHECKER).getAsJsonArray();

            for (int i = 0; i < existingRecords.size(); i++) {

                if (existingRecords.get(i).getAsJsonObject().get("linktype").getAsString()
                        .equals(HelperMethods.CREDENTIAL_TYPE.UNLOCK.toString())) {
                    String existingToken = existingRecords.get(i).getAsJsonObject().get("id").getAsString();
                    inputParam = new HashMap<String, Object>();
                    inputParam.put("id", existingToken);
                    ServiceCallHelper.invokeServiceAndGetJson(inputParam, headerMap,
                            URLConstants.CREDENTIAL_CHECKER_DELETE);
                }
            }
        }

        String activationToken = UUID.randomUUID().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("id", activationToken);
        map.put("UserName", customerDTO.getUserName());
        map.put("linktype", HelperMethods.CREDENTIAL_TYPE.UNLOCK.toString());
        map.put("createdts", HelperMethods.getCurrentTimeStamp());
        JsonObject createCredential = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
                URLConstants.CREDENTIAL_CHECKER_CREATE);

        if (!HelperMethods.hasError(createCredential)) {

            String link;

            link = URLFinder.getServerRuntimeProperty(URLConstants.DBX_CUSTOMER_UNLOCK_LINK) + "?qp="
                    + new String(java.util.Base64.getEncoder().encode(activationToken.getBytes()));

            PasswordLockoutSettingsDTO settingsDTO =
                    (PasswordLockoutSettingsDTO) new PasswordLockoutSettingsDTO().loadDTO();
            Map<String, Object> input = new HashMap<>();
            input.put("Subscribe", "true");
            input.put("FirstName", customerDTO.getFirstName());
            input.put("EmailType", "UNLOCK_CUSTOMER");
            input.put("LastName", customerDTO.getLastName());
            JSONObject addContext = new JSONObject();
            addContext.put("unlockAccountLink", link);
            addContext.put("userName", customerDTO.getUserName());
            addContext.put("linkExpiry", String.valueOf(Math.floorDiv(settingsDTO.getRecoveryEmailLinkValidity(), 60)));
            input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
            input.put("Email", email);

            headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            HelperMethods.callApiAsync(input, headerMap, URLConstants.DBX_SEND_EMAIL_ORCH);
            jsonObject.addProperty("mailRequestSent", "true");
            dbxResult.setResponse(jsonObject);

            return dbxResult;
        }

        jsonObject.addProperty("mailRequestSent", "false");

        dbxResult.setResponse(jsonObject);

        return dbxResult;
    }

    @Override
    public DBXResult fetchCustomerIdForEnrollment(CustomerDTO customerDTO, Map<String, Object> headersMap)
            throws ApplicationException {

        DBXResult result = new DBXResult();
        CustomerDTO responseDTO = new CustomerDTO();
        Map<String, Object> inputParams = new HashMap<>();
        boolean isPresentInDBXDB = false;
        String customerIdinDB = customerDTO.getId();
        String partyId = customerDTO.getId();

        if (StringUtils.isBlank(customerDTO.getId())) {
            return result;
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerIdinDB);
        JsonObject customerJson =
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, URLConstants.CUSTOMER_GET);
        isPresentInDBXDB = JSONUtil.hasKey(customerJson, MWConstants.OPSTATUS)
                && JSONUtil.getString(customerJson, MWConstants.OPSTATUS).equalsIgnoreCase("0")
                && JSONUtil.hasKey(customerJson, DBPDatasetConstants.DATASET_CUSTOMER)
                && customerJson.get(DBPDatasetConstants.DATASET_CUSTOMER).getAsJsonArray().size() > 0;

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        String homeLegalEntity = null;
		if (customerJson != null && customerJson.has("customer") && customerJson.get("customer").isJsonArray()) {
			JsonObject customerrec = customerJson.get("customer").getAsJsonArray().get(0).getAsJsonObject();
			if (customerrec != null && customerrec.has(DBPDatasetConstants.HOME_LEGALENTITY))
				homeLegalEntity = JSONUtil.getString(customerrec, DBPDatasetConstants.HOME_LEGALENTITY);
		}
        if (isPresentInDBXDB)
        {
            backendIdentifierDTO.setCustomer_id(customerIdinDB);
        }
        else {
            backendIdentifierDTO.setBackendId(partyId);
        }
        if(StringUtils.isNotBlank(homeLegalEntity))
        {
            backendIdentifierDTO.setCompanyLegalUnit(homeLegalEntity);
        }
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
        BackendIdentifierDTO identifierDTO = new BackendIdentifierDTO();
        try {
            DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headersMap);
            identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
        } catch (ApplicationException e1) {
            alert.prepareError("Exception occured while fetching party backendidentifier ID"
                    + customerDTO.getId()).log();
            return result;
        }
        if (identifierDTO != null) {
            customerIdinDB = identifierDTO.getCustomer_id();
            partyId = identifierDTO.getBackendId();
        }
        try {
            if (!isPresentInDBXDB && identifierDTO == null) {

                /** fetch party basic info **/
                PartyDTO partyDTO = new PartyDTO();
                String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                        PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyId);

                PartyUtils.addJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);
                DBXResult Partyresponse = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                        partyURL, null, headersMap);
                JsonObject jsonObject = new JsonParser().parse((String) Partyresponse.getResponse()).getAsJsonObject();
                partyDTO.loadFromJson(jsonObject);

                SystemConfigurationBusinessDelegate systemConfigBD =
                        DBPAPIAbstractFactoryImpl.getBusinessDelegate(SystemConfigurationBusinessDelegate.class);
                Integer userNameLength = Integer
                        .parseInt(systemConfigBD.getSystemConfigurationValue("USERNAME_LENGTH", headersMap));
                String customerId = String.valueOf(HelperMethods.getNumericId(userNameLength));
                inputParams.put("id", customerId);
                inputParams.put("UserName", inputParams.get("id"));
                inputParams.put("Status_id", DBPUtilitiesConstants.CUSTOMER_STATUS_NEW);
                inputParams.put("isEnrolledFromSpotlight", "1");
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.CUSTOMER_CREATE);
                inputParams.clear();
                inputParams.put("id", UUID.randomUUID().toString());
                inputParams.put("Customer_id", customerId);
                inputParams.put("customerId", "1");
                inputParams.put("BackendId", customerDTO.getId());
                inputParams.put("BackendType", DTOConstants.PARTY);
                inputParams.put("identifier_name", "customer_id");
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.BACKENDIDENTIFIER_CREATE);
                CustomerGroupDTO customerGroupDTO = new CustomerGroupDTO();
                CustomerGroupBusinessDelegate customerGroup =
                        DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerGroupBusinessDelegate.class);
                customerGroupDTO.setCustomerId(customerId);
                customerGroupDTO.setGroupId("DEFAULT_GROUP");
                customerGroup.createCustomerGroup(customerGroupDTO, headersMap);
                responseDTO.setId(customerId);
                responseDTO.setFirstName(partyDTO.getFirstName());
                responseDTO.setLastName(partyDTO.getLastName());
                responseDTO.setUserName(customerId);
            } else {
                inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + customerIdinDB);
                if (partyId != customerIdinDB) {
                    customerJson =
                            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                                    URLConstants.CUSTOMER_GET);
                }
                CustomerDTO infintyCustomerDTO = (CustomerDTO) DTOUtils.loadJsonObjectIntoObject(
                        customerJson.get("customer").getAsJsonArray().get(0).getAsJsonObject(), CustomerDTO.class,
                        true);
                String scaenabled = EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED");
                if (StringUtils.isNotBlank(scaenabled) && !Boolean.valueOf(scaenabled)) {
                    if (infintyCustomerDTO.getIsEnrolled()) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_10748);
                    }
                }
                responseDTO.setId(infintyCustomerDTO.getId());
                responseDTO.setUserName(infintyCustomerDTO.getUserName());
                responseDTO.setFirstName(infintyCustomerDTO.getFirstName());
                responseDTO.setLastName(infintyCustomerDTO.getLastName());
                responseDTO.setIsEnrolled(infintyCustomerDTO.getIsEnrolled());
            }
			if (StringUtils.isNotBlank(homeLegalEntity))
				responseDTO.setHomeLegalEntity(homeLegalEntity);
           
            result.setResponse(responseDTO);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating Infinity Digital profile" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10741);
        }
        return result;
    }

    @Override
    public DBXResult getAddressTypes(Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        JsonObject result = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject addressType = new JsonObject();

        try {
            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_REFERENCE)
                    + PartyConstants.COMMUNICATION_TYPE;

            dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);

            if (dbxResult.getResponse() != null) {
                JsonElement element = new JsonParser().parse((String) dbxResult.getResponse());
                if (element.isJsonObject()) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    if (jsonObject.has(PartyConstants.REFERENCES)
                            && !jsonObject.get(PartyConstants.REFERENCES).isJsonNull()) {
                        jsonObject = jsonObject.get(PartyConstants.REFERENCES).getAsJsonObject();
                        if (jsonObject.has(PartyConstants.COMMUNICATION_TYPE)
                                && !jsonObject.get(PartyConstants.COMMUNICATION_TYPE).isJsonNull()) {
                            JsonArray array = jsonObject.get(PartyConstants.COMMUNICATION_TYPE).getAsJsonArray();
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject jsonObject2 = array.get(i).getAsJsonObject();
                                if (jsonObject2.has(PartyConstants.TYPE_NATURE)
                                        && !jsonObject2.get(PartyConstants.TYPE_NATURE).isJsonNull()
                                        && jsonObject2.get(PartyConstants.TYPE_NATURE).getAsString()
                                                .equals(DTOConstants.PHYSICAL)) {
                                    addressType = new JsonObject();
                                    addressType.addProperty("id", jsonObject2.get("typeName").getAsString());
                                    addressType.addProperty("Description",
                                            jsonObject2.get("description").getAsString());
                                    jsonArray.add(addressType);
                                }
                            }
                            result.add("addresstype", jsonArray);
                            dbxResult.setResponse(result);
                        }
                    }
                } else if (element.isJsonArray()) {
                    dbxResult.setResponse(element.getAsJsonArray().get(0).getAsJsonObject());
                }
            }
        } catch (Exception e) {
            alert.prepareError("Error in PartyBackendDelegateImpl-getReferences : " + e.toString()).log();
        }

        return dbxResult;
    }

    public DBXResult updatePartyProfile(PartyDetails partyDetails, Map<String, Object> headerMap, String partyId,
            CustomerDTO customerDTO) {

        DBXResult dbxResult = new DBXResult();
        DBXResult response = null;
        JsonObject resultJsonObject = new JsonObject();
        dbxResult.setResponse(resultJsonObject);

        String jsonString = null;
        JSONObject addresses = new JSONObject();
        JSONArray addressesArr = new JSONArray();
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonString = mapper.writeValueAsString(partyDetails);
        } catch (JsonProcessingException e1) {
        	alert.prepareError("Exception", e1).log();
        }

        addressesArr.put(new JSONObject(jsonString));

        addresses.put("addresses", addressesArr);

        String address = addresses.toString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : " + address).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESS, partyId);

        headerMap = PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

        if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATE) || customerDTO
                .getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATEADDRESS)) {
            response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL, address,
                    headerMap);
        } else if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_UPDATE)
                || customerDTO
                        .getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_UPDATEADDRESS)) {
            response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL, address,
                    headerMap);
        }

        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();

            if (jsonObject.has("id")) {
                resultJsonObject.addProperty("success", "success");
                resultJsonObject.addProperty("Status", "Operation successful");
                resultJsonObject.addProperty("status", "Operation successful");
            } else {
                ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject,
                        "Unable to update Party " + jsonObject.get("message").getAsString());
                resultJsonObject.addProperty("errmsg",
                        "Unable to update Party " + jsonObject.get("message").getAsString());

                return dbxResult;
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ", e).log();
            ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject, "Unable to update Party");
            resultJsonObject.addProperty("errmsg", "Unable to update Party ");
            return dbxResult;
        }
        return dbxResult;
    }

    public DBXResult deletePartyAddress(Map<String, Object> headerMap, String partyId, CustomerDTO customerDTO) {

        DBXResult dbxResult = new DBXResult();
        DBXResult response = null;
        JsonObject resultJsonObject = new JsonObject();
        dbxResult.setResponse(resultJsonObject);

        String partyURL = "";
        headerMap = PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

        if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_DELETE)) {
            partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESSDELETE, partyId)
                    + customerDTO.getCommunicationIDToBeDeleted();
        } else if (customerDTO.getOperation()
                .contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_DELETEADDRESS)) {
            partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESSDELETE, partyId)
                    + customerDTO.getAddressIDToBeDeleted();
        }
        response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.DELETE, partyURL, null,
                headerMap);

        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();

            if (jsonObject.has("id")) {
                resultJsonObject.addProperty("success", "success");
                resultJsonObject.addProperty("Status", "Operation successful");
                resultJsonObject.addProperty("status", "Operation successful");
            } else {
                ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject,
                        "Unable to update Party " + jsonObject.get("message").getAsString());
                resultJsonObject.addProperty("errmsg",
                        "Unable to update Party " + jsonObject.get("message").getAsString());

                return dbxResult;
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ", e).log();
            ErrorCodeEnum.ERR_10218.setErrorCode(resultJsonObject, "Unable to update Party");
            resultJsonObject.addProperty("errmsg", "Unable to update Party ");
            return dbxResult;
        }
        return dbxResult;
    }

    public DBXResult fetchRetailCustomerDetails(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        String taxId = customerDTO.getTaxID();
        DBXResult response = new DBXResult();
        ProfileManagementBackendDelegateImpl backendDelegateImpl = new ProfileManagementBackendDelegateImpl();
        response = backendDelegateImpl.fetchRetailCustomerDetails(customerDTO, headerMap);

        JsonObject jsonObject = response.getResponse() != null ? (JsonObject) response.getResponse() : new JsonObject();

        if (!jsonObject.has(InfinityConstants.id) || jsonObject.get(InfinityConstants.id).isJsonNull()) {
            return response;
        }

        String id = jsonObject.get(InfinityConstants.id).getAsString();

        String companyId="";
        
        if (StringUtils.isNotBlank(customerDTO.getCompanyLegalUnit())) {
			headerMap.put("companyId", customerDTO.getCompanyLegalUnit());
			companyId = customerDTO.getCompanyLegalUnit();
		}
        id = companyId + "-" + id;
        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        String queryParams = "";

        queryParams += "alternateIdentifierNumber=" + id;
        queryParams += "&";
        queryParams += "alternateIdentifierType=BackOfficeIdentifier";

        DBXResult dbxResult = new DBXResult();
        JsonObject partyJson = new JsonObject();
        JsonArray partyJsonArray = new JsonArray();
        if (queryParams.length() > 1) {
            try {
                String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                        PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_SEARCH) + queryParams;

                dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
                if (dbxResult.getResponse() != null) {
                    JsonElement partyResponse =
                            new JsonParser().parse((String) dbxResult.getResponse());
                    if (partyResponse.isJsonObject()) {
                        JsonObject partyResponseObject = partyResponse.getAsJsonObject();
                        if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
                                && partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
                            partyJsonArray = partyResponseObject.get("parties").getAsJsonArray();
                            partyJson = partyJsonArray.get(0).getAsJsonObject();
                        }
                    }
                }
            } catch (Exception e) {
                alert.prepareError("Caught error while parsing partysearch response ", e).log();
                return dbxResult;
            }
        }

        String partyId = JSONUtil.getString(partyJson, "partyId");
        if (StringUtils.isBlank(partyId)) {
           partyId = createPartyEntry(jsonObject, companyId, headerMap);
        }
        else {
            checkAndAddTaxID(partyJson, taxId, headerMap);
        }
        
        jsonObject.addProperty("partyId", partyId);

        response.setResponse(jsonObject);
        return response;

        // DBXResult response = new DBXResult();
        // PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        // String queryParams = "";
        // if (StringUtils.isNotBlank(customerDTO.getLastName())) {
        // if (queryParams.length() > 1) {
        // queryParams += "&";
        // }
        // queryParams += "lastName=" + customerDTO.getLastName();
        // }
        // if (StringUtils.isNotBlank(customerDTO.getDateOfBirth())) {
        // if (queryParams.length() > 1) {
        // queryParams += "&";
        // }
        // queryParams += "dateOfBirth=" + customerDTO.getDateOfBirth();
        // }
        // if (StringUtils.isNotBlank(customerDTO.getTaxID())) {
        // if (queryParams.length() > 1) {
        // queryParams += "&";
        // }
        // queryParams += "identifierNumber=" + customerDTO.getTaxID() + "&identifierType="
        // + DTOConstants.SOCIAL_SECURITY_NUMBER;
        // queryParams = queryParams.replace(" ", "%20");
        //
        // }
        // DBXResult dbxResult = new DBXResult();
        // JsonObject partyJson = new JsonObject();
        // JsonArray partyJsonArray = new JsonArray();
        // if (queryParams.length() > 1) {
        // try {
        // String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
        // PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_SEARCH) + queryParams;
        //
        // dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
        // if (dbxResult.getResponse() != null) {
        // JsonObject partyResponse =
        // new JsonParser().parse((String) dbxResult.getResponse()).getAsJsonObject();
        // if (partyResponse.has("parties") && partyResponse.get("parties").isJsonArray())
        // partyJsonArray = partyResponse.get("parties").getAsJsonArray();
        //
        // if (partyJsonArray.size() > 1) {
        // throw new ApplicationException(ErrorCodeEnum.ERR_10804);
        // }
        //
        // partyJson = partyJsonArray.get(0).getAsJsonObject();
        // }
        // } catch (Exception e) {
        // alert.prepareError("Caught error while parsing partysearch response ", e).log();
        // return dbxResult;
        // }
        // }
        // String coreCustomerPhone = "";
        // String coreCustomerEmail = "";
        //
        // if (partyJson.has(DTOConstants.PARTY_ADDRESS)
        // && !partyJson.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
        // List<PartyAddress> contactPoints =
        // PartyAddress.loadFromJsonArray(partyJson.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
        // List<CustomerCommunicationDTO> communicationDTOs = new ArrayList<CustomerCommunicationDTO>();
        // for (PartyAddress contactPoint : contactPoints) {
        // if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)) {
        // if (StringUtils.isNotBlank(contactPoint.getIddPrefixPhone()))
        // coreCustomerPhone = contactPoint.getIddPrefixPhone();
        // coreCustomerPhone += contactPoint.getPhoneNo();
        // } else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC)
        // && contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)) {
        // coreCustomerEmail = contactPoint.getElectronicAddress();
        // }
        // }
        // dbxResult.setResponse(communicationDTOs);
        // }
        //
        // getBackOfficeIdentifier(partyJson);
        // diagnostic.prepareDebug(partyJson.toString()).log();
        // String partyType = JSONUtil.getString(partyJson, "partyType");
        // if (StringUtils.isBlank(coreCustomerPhone) || StringUtils.isBlank(coreCustomerEmail)) {
        // throw new ApplicationException(ErrorCodeEnum.ERR_10805);
        // }
        //
        // if (!PartyConstants.PARTY_TYPE_INDIVIDUAL.equalsIgnoreCase(partyType)) {
        // throw new ApplicationException(ErrorCodeEnum.ERR_10803);
        // }
        // JsonObject coreCustomer = new JsonObject();
        // coreCustomer.addProperty("partyId", JSONUtil.getString(partyJson, "partyId"));
        // String cifstring = JSONUtil.getString(partyJson, PartyConstants.coreCustomerId);
        // String[] cifarray = cifstring.split("-");
        // coreCustomer.addProperty("id", cifarray[1]);
        // coreCustomer.addProperty("name", JSONUtil.getString(partyJson, "firstName")
        // + " " + JSONUtil.getString(partyJson, "lastName"));
        // coreCustomer.addProperty("firstName", JSONUtil.getString(partyJson, "firstName"));
        // coreCustomer.addProperty("lastName", JSONUtil.getString(partyJson, "lastName"));
        // coreCustomer.addProperty("addressLine1", JSONUtil.getString(partyJson, "streetName"));
        // coreCustomer.addProperty("addressLine2", JSONUtil.getString(partyJson, "partyId"));
        // coreCustomer.addProperty("cityName", JSONUtil.getString(partyJson, "town"));
        // coreCustomer.addProperty("country", JSONUtil.getString(partyJson, "countrySubdivision"));
        // // coreCustomer.addProperty("zipCode", JSONUtil.getString(communication, "postalOrZipCode"));
        // coreCustomer.addProperty("taxId", customerDTO.getTaxID());
        // coreCustomer.addProperty("dateOfBirth", JSONUtil.getString(partyJson, "dateOfBirth"));
        // coreCustomer.addProperty("phone", coreCustomerPhone);
        // coreCustomer.addProperty("email", coreCustomerEmail);
        // coreCustomer.addProperty("gender", JSONUtil.getString(partyJson, "gender"));
        // response.setResponse(coreCustomer);
        //
        // return response;
    }

    private String createPartyEntry(JsonObject jsonObject, String companyId, Map<String, Object> headerMap) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerType_id(HelperMethods.getCustomerTypes().get("Customer"));
        customerDTO.setFirstName(JSONUtil.getString(jsonObject, "firstName"));
        customerDTO.setLastName(JSONUtil.getString(jsonObject, "lastName"));
        customerDTO.setDateOfBirth(JSONUtil.getString(jsonObject, "dateOfBirth"));
        customerDTO.setGender(JSONUtil.getString(jsonObject, "gender"));
        customerDTO.setSsn(JSONUtil.getString(jsonObject, "taxId"));
        CustomerCommunicationDTO phoneCommunicationDTO = new CustomerCommunicationDTO();
        CustomerCommunicationDTO emailCommunicationDTO = new CustomerCommunicationDTO();

        String coreCustomerId = JSONUtil.getString(jsonObject, "id");
        String phoneNumber = JSONUtil.getString(jsonObject, "phone");
        String phoneCountryCode = "";
        if (StringUtils.isNotBlank(phoneNumber) && phoneNumber.contains("-")) {
            phoneCountryCode = phoneNumber.split("-")[0];
            phoneNumber = phoneNumber.split("-")[1];
        }

        AddressDTO address = new AddressDTO();
        address.setAddressLine1(JSONUtil.getString(jsonObject, "addressLine1"));
        address.setAddressLine2(JSONUtil.getString(jsonObject, "addressLine2"));
        address.setCityName(JSONUtil.getString(jsonObject, "cityName"));
        address.setCountry(JSONUtil.getString(jsonObject, "country"));
        address.setZipCode(JSONUtil.getString(jsonObject, "zipCode"));

        CustomerAddressDTO addressDTO = new CustomerAddressDTO();
        addressDTO.setAddressDTO(address);
        addressDTO.setCustomer_id(coreCustomerId);
        addressDTO.setAddress_id(HelperMethods.getNewId());
        addressDTO.setIsPrimary(true);
        addressDTO.setType_id(HelperMethods.getAddressTypes().get("Home"));
        customerDTO.setCustomerAddress(addressDTO);
        
        if (!phoneCountryCode.contains("+"))
            phoneCountryCode = "+" + phoneCountryCode;
        phoneCommunicationDTO.setId(HelperMethods.getNewId());
        phoneCommunicationDTO.setPhoneCountryCode(phoneCountryCode);
        phoneCommunicationDTO.setValue(phoneNumber);
        phoneCommunicationDTO.setIsPrimary(true);
        phoneCommunicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));

        emailCommunicationDTO.setId(HelperMethods.getNewId());
        emailCommunicationDTO.setIsPrimary(true);
        emailCommunicationDTO.setValue(
                jsonObject.has(InfinityConstants.email) && !jsonObject.get(InfinityConstants.email).isJsonNull()
                        ? jsonObject.get(InfinityConstants.email).getAsString()
                        : null);
        emailCommunicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));

        customerDTO.setCustomerCommuncation(phoneCommunicationDTO);
        customerDTO.setCustomerCommuncation(emailCommunicationDTO);

        PartyUserManagementBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(PartyUserManagementBackendDelegate.class);

        PartyDTO partyDTO = new PartyDTO();
        PartyUtils.buildPartyDTO(customerDTO, partyDTO);
        
        if (StringUtils.isNotBlank(coreCustomerId)) {
        	AlternateIdentity alternateIdentity = new AlternateIdentity();
            alternateIdentity.setIdentityType(PartyConstants.BackOfficeIdentifier);
            alternateIdentity.setIdentitySource(PartyConstants.TransactT24);
            alternateIdentity.setIdentityNumber(
            		companyId + "-" + coreCustomerId);
            partyDTO.setAlternateIdentities(alternateIdentity);
        }

        DBXResult dbxResult = backendDelegate.create(partyDTO, headerMap);
        if(dbxResult.getResponse() != null) {
            return (String) dbxResult.getResponse();
        }
        
        return null;
    }

    private void checkAndAddTaxID(JsonObject partyJson, String taxId, Map<String, Object> headerMap) {

        boolean already_exist = false;
        if (partyJson.has(DTOConstants.PARTYIDENTIFIERS)
                && partyJson.get(DTOConstants.PARTYIDENTIFIERS).isJsonArray()) {
            JsonArray partyIdentifiers = partyJson.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
            if (partyIdentifiers.size() > 0) {
                for (JsonElement element : partyIdentifiers) {
                    JsonObject partyIdentifier = element.getAsJsonObject();
                    if (partyIdentifier.has("type") && !partyIdentifier.get("type").isJsonNull()
                            && DTOConstants.SOCIAL_SECURITY_NUMBER.equals(partyIdentifier.get("type").getAsString())) {
                        already_exist = true;
                    }
                }
            }
        }

        PartyDTO partyDTO = new PartyDTO();
        partyDTO.setPartyId(JSONUtil.getString(partyJson, "partyId"));
        PartyIdentifier identifier = new PartyIdentifier();
        identifier.setType(DTOConstants.SOCIAL_SECURITY_NUMBER);
        identifier.setIdentifierNumber(taxId);
        partyDTO.setPartyIdentifier(identifier);

        if (!already_exist) {
            new PartyCustomerBusinessDelegateImpl().addIdentifier(partyDTO, headerMap);
        }

    }

    private void getBackOfficeIdentifier(JsonObject party) {
        if (party.has(PartyConstants.alternateIdentities)
                && party.get(PartyConstants.alternateIdentities).isJsonArray()) {
            for (JsonElement element : party.get(PartyConstants.alternateIdentities).getAsJsonArray()) {
                JsonObject alternateIdentity = element.getAsJsonObject();
                if (alternateIdentity.has(PartyConstants.identityType)
                        && !alternateIdentity.get(PartyConstants.identityType).isJsonNull()) {
                    String identitySource = alternateIdentity.get(PartyConstants.identitySource).getAsString();
                    String identityType = alternateIdentity.get(PartyConstants.identityType).getAsString();
                    if (PartyConstants.BackOfficeIdentifier.equals(identityType)
                            && PartyConstants.TransactT24.equals(identitySource)) {
                        party.add(PartyConstants.coreCustomerId, alternateIdentity.get(PartyConstants.identityNumber));
                    }
                }
            }
        }
    }
    
    public JsonObject searchCustomerinParty(JsonArray recordsArray, Map<String, String> configurations,
			MemberSearchBean memberSearchBean, Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {
		DBXResult dbxResult = new DBXResult();
		JsonObject processedResult = new JsonObject();
		
		Map<String, Object> input = new HashMap<String, Object>();
		PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
		if (StringUtils.isNotBlank(memberSearchBean.getCustomerEmail())) {
			input.put("emailId", memberSearchBean.getCustomerEmail());
		}
		if (StringUtils.isNotBlank(memberSearchBean.getCustomerName())) {
			input.put("lastName", memberSearchBean.getCustomerName());
		}
		if (StringUtils.isNotBlank(memberSearchBean.getCustomerPhone())) {
			input.put("contactNumber", memberSearchBean.getCustomerPhone());
		}
		if (StringUtils.isNotBlank(memberSearchBean.getMemberId())) {
			input.put("customerId", memberSearchBean.getMemberId());
		}
		if (StringUtils.isNotBlank(memberSearchBean.getDateOfBirth())) {
			input.put("dateOfBirth", memberSearchBean.getDateOfBirth());
		}
		JsonArray partyJsonArray = new JsonArray();

		if (StringUtils.isNotBlank(memberSearchBean.getCustomerId())) {
			input.put("customerId", memberSearchBean.getCustomerId());
		}
		
		ProfileManagementBackendDelegateImpl backend = new ProfileManagementBackendDelegateImpl();
		
		if(!input.isEmpty()) {
			String companyId="";
			
			if (StringUtils.isNotBlank(memberSearchBean.getCompanyLegalUnit())) {
				headerMap.put("companyId", memberSearchBean.getCompanyLegalUnit());
				companyId =memberSearchBean.getCompanyLegalUnit();
			}
				
	            String queryParams = "";
	            String id ="";
	            
	            String phonenumber = memberSearchBean.getCustomerPhone();
                String[] phoneno = phonenumber.split("-");

                if(!memberSearchBean.getMemberId().isEmpty()) {
	             id = companyId + "-" + memberSearchBean.getMemberId();
	            
	            queryParams += "alternateIdentifierNumber=" + id;
	    		queryParams += "&";
	    		queryParams += "alternateIdentifierType=BackOfficeIdentifier";
			} else {
				if (!memberSearchBean.getCustomerEmail().isEmpty()) {
					queryParams += "emailId=" + memberSearchBean.getCustomerEmail();
				}
				if (!memberSearchBean.getCustomerName().isEmpty()) {
					if(StringUtils.isNotBlank(queryParams))
						queryParams = queryParams+"&";
					queryParams += "lastName=" + memberSearchBean.getCustomerName();
				}
				if (!memberSearchBean.getCustomerPhone().isEmpty()) {
					if(StringUtils.isNotBlank(queryParams))
						queryParams = queryParams+"&";
					queryParams += "contactNumber=" + phoneno[1];
				}
				if (!memberSearchBean.getDateOfBirth().isEmpty()) {
					if(StringUtils.isNotBlank(queryParams))
						queryParams = queryParams+"&";
					queryParams += "dateOfBirth=" + memberSearchBean.getDateOfBirth();
				}
			}
			String partyURL = "";
			DBXResult dbxResult1 = new DBXResult();
			if (memberSearchBean.isPartyPassed() && StringUtils.isNotBlank(memberSearchBean.getMemberId())) {

				partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) + PartyURLFinder
						.getServiceUrl(PartyPropertyConstants.PARTY_GET5, memberSearchBean.getMemberId());

			} else {
				partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
						+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_SEARCH5) + queryParams;

			}
			dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);

	            if (dbxResult1.getResponse() != null) {
					JsonElement partyResponse = new JsonParser().parse((String) dbxResult1.getResponse());
					if (partyResponse.isJsonObject()) {
						JsonObject partyResponseObject = partyResponse.getAsJsonObject();
						if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
								&& partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
							partyJsonArray = partyResponseObject.get("parties").getAsJsonArray();
						}
						else if(!partyResponseObject.isJsonNull()) {
							partyJsonArray.add(partyResponseObject);
						}
					}
				}
	            
	        if (partyJsonArray.size() > 0) {
	            BackendIdentifiersBackendDelegateimpl backendDelegateimpl = new BackendIdentifiersBackendDelegateimpl();
	           	JsonArray customerDetails = new JsonArray();  	

	           	for (JsonElement jsonelement : partyJsonArray) {
	            	JsonObject jsonObject =  new JsonObject() ;
	            	 jsonObject.addProperty("DateOfBirth", JSONUtil.getString(jsonelement.getAsJsonObject(), "dateOfBirth"));
	            	 jsonObject.addProperty("FirstName", JSONUtil.getString(jsonelement.getAsJsonObject(), "firstName"));
	            	 jsonObject.addProperty("name",
	                         (jsonelement.getAsJsonObject().has("firstName") && !jsonelement.getAsJsonObject().get("firstName").isJsonNull()
	                                 && StringUtils.isNotBlank(jsonelement.getAsJsonObject().get("firstName").getAsString())
	                                         ? jsonelement.getAsJsonObject().get("firstName").getAsString()
	                                         : "")
	                                 + " "
	                                 + (jsonelement.getAsJsonObject().has("lastName") && !jsonelement.getAsJsonObject().get("lastName").isJsonNull()
	                                         && StringUtils.isNotBlank(jsonelement.getAsJsonObject().get("lastName").getAsString())
	                                                 ? jsonelement.getAsJsonObject().get("lastName").getAsString()
	                                                 : ""));
	            	 jsonObject.addProperty("LastName", JSONUtil.getString(jsonelement.getAsJsonObject(), "lastName"));
	            	 jsonObject.addProperty("Gender", JSONUtil.getString(jsonelement.getAsJsonObject(), "gender"));
	            	 jsonObject.addProperty("MaritalStatus_name", JSONUtil.getString(jsonelement.getAsJsonObject(), "maritalStatus"));

	            	if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.PARTYIDENTIFIERS)
	                         && jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).isJsonArray()
	                         && jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray().size() > 0) {
	                         customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
	                       for (JsonElement taxids : customerDetails) {
	                        	 String type = JSONUtil.getString(taxids.getAsJsonObject(), "type");
	                           if(type.equalsIgnoreCase("SOCIAL.SECURITY.NO")) {
	                             String taxId = JSONUtil.getString(taxids.getAsJsonObject(), "identifierNumber");
	                         	 jsonObject.addProperty("ssn", taxId);
	                             jsonObject.addProperty("Ssn", taxId);
	                             jsonObject.addProperty("SSN", taxId); 
	                           }
	                       }
	            	 }
	            
	            	 
	                 String employmenttype="";
	                 if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.EMPLOYMENTS)
	                         && jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).isJsonArray()
	                         && jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).getAsJsonArray().size() > 0) {
	                         customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).getAsJsonArray();
	                         for (JsonElement employments : customerDetails) {
	                        	 employmenttype = JSONUtil.getString(employments.getAsJsonObject(), "type");
	                        	 jsonObject.addProperty("EmployementStatus_name", employmenttype);
	                         }
	            	 }
	                 
	            	  if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.ALTERNATEIDENTITIES)
	                          && jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).isJsonArray()
	                          && jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray().size() > 0) {
	            		  customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray();
	            		  for (JsonElement customerInfos : customerDetails) {
	            			  String identityNumber =   JSONUtil.getString(customerInfos.getAsJsonObject(), "identityNumber");
	            			  String[] identityNumberArray = identityNumber.split("-");
	      					  if(identityNumberArray != null && identityNumberArray.length == 2) 
	      					  {
	      						 id = identityNumberArray[1];

	      					  }
	            		  }
	            	  }
	                 if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.PARTYADDRESS)
	                         && jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).isJsonArray()
	                         && jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() > 0) {
	                	 JsonArray address = new JsonArray();
	                	 address = jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray();
	                     for (JsonElement customerInfo : address) {
	                    	 if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary")) 
	                    			 && "Electronic".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))) {
	                    		 String electronicAddress = JSONUtil.getString(customerInfo.getAsJsonObject(), "electronicAddress");
	                    		 jsonObject.addProperty("PrimaryEmailAddress", electronicAddress);
	                     }
	                    	 if ("true".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary")) 
	                    			 && "Phone".equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "communicationNature"))) {
	                    		 String phonePrefix = JSONUtil.getString(customerInfo.getAsJsonObject(), "iddPrefixPhone"); 
		                    	 String phoneNo = JSONUtil.getString(customerInfo.getAsJsonObject(), "phoneNo"); 
		                    	 jsonObject.addProperty("PrimaryPhoneNumber", phonePrefix +""+ phoneNo);
		                    	 
	                     }         	 
	            	  }
	               
	                 }
	           	
	                String partyType = JSONUtil.getString(jsonelement.getAsJsonObject(), "partyType");
	                BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
	                String partyId = JSONUtil.getString(jsonelement.getAsJsonObject(), "partyId");
	                backendIdentifierDTO.setBackendId(partyId);
	                backendIdentifierDTO.setBackendType("PARTY");
	                backendIdentifierDTO.setCompanyLegalUnit(memberSearchBean.getCompanyLegalUnit());
	               
	                
	                dbxResult = backendDelegateimpl.get(backendIdentifierDTO, headerMap);
	                if (dbxResult.getResponse() != null) {
	                    backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
	                    CustomerDTO customerDTO = new CustomerDTO();
	                    customerDTO = (CustomerDTO) customerDTO.loadDTO(backendIdentifierDTO.getCustomer_id());
	                    if ((customerDTO.getLockCount() + 1) >= pm.getAccountLockoutThreshold()) {
							jsonObject.addProperty("Status_id", StatusEnum.SID_CUS_LOCKED.name());
						} else {
							jsonObject.addProperty("Status_id", customerDTO.getStatus_id());
						}
	                    jsonObject.addProperty(InfinityConstants.isEnrolled, customerDTO.getIsEnrolled().toString());
	                    jsonObject.addProperty(InfinityConstants.Username, customerDTO.getUserName());
	                    jsonObject.addProperty(InfinityConstants.isProfileExist, "true");
	                    jsonObject.addProperty(InfinityConstants.primaryCustomerId,id);
	                    jsonObject.addProperty(InfinityConstants.CustomerType_id, customerDTO.getCustomerType_id());
	                    jsonObject.addProperty(InfinityConstants.id, customerDTO.getId());
	                    backend.getCustomerType(jsonObject, customerDTO.getId(), headerMap, customerDTO.getCompanyLegalUnit());
	                    jsonObject.addProperty("legalEntityId", customerDTO.getCompanyLegalUnit());
	                    if (partyType.equalsIgnoreCase("Individual")) {
	                 	   jsonObject.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Retail"));
	                 	   jsonObject.addProperty("isBusiness", "false");
	                    } else {                       jsonObject.addProperty("isBusiness", "true");
	                    jsonObject.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Business"));
	                    }        
	                    jsonObject.addProperty(InfinityConstants.homeLegalEntity, customerDTO.getHomeLegalEntity());
	                    } else {
	                	jsonObject.addProperty(InfinityConstants.isProfileExist, "false");
	                	jsonObject.addProperty(InfinityConstants.isEnrolled, "false");
	                	jsonObject.addProperty(InfinityConstants.primaryCustomerId,id);
	                	jsonObject.remove(InfinityConstants.id);
	                   if (partyType.equalsIgnoreCase("Individual")) {
	                	   jsonObject.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Retail"));
	                	   jsonObject.addProperty("isBusiness", "false");
	                   } else {                       jsonObject.addProperty("isBusiness", "true");
	                   jsonObject.addProperty("CustomerType_id", HelperMethods.getCustomerTypes().get("Business"));
	                   }
	                   jsonObject.addProperty("Status_id", StatusEnum.SID_CUS_NEW.name());
	                   if (partyType.equalsIgnoreCase("Individual")) {
	                	   jsonObject.addProperty("CustomerTypeId", HelperMethods.getCustomerTypes().get("Retail"));
	                	   jsonObject.addProperty("isBusiness", "false");
	                   } else {                       jsonObject.addProperty("isBusiness", "true");
	                   jsonObject.addProperty("CustomerTypeId", HelperMethods.getCustomerTypes().get("Business"));
	                   }                
	                   
	                }
	                
	                jsonObject.addProperty("sectorName", "");
	                jsonObject.addProperty("branchId", memberSearchBean.getCompanyLegalUnit());
	                jsonObject.addProperty("branchName", "");
	                recordsArray.add(jsonObject);
	            }
	        }
	        }
		recordsArray = backend.sortJsonArray(memberSearchBean, recordsArray);

		processedResult.addProperty("Status", "Records returned: " + recordsArray.size());

		processedResult.addProperty("TotalResultsFound", recordsArray.size());

		processedResult.add("records", recordsArray);

		dbxResult.setResponse(processedResult);

		processedResult.addProperty("Status", "Records returned: " + recordsArray.size());

		return processedResult;
	}   
    
   
    public DBXResult getBasicInfoFromParty(JsonObject customerViewJson, Map<String, String> configurations,
			CustomerDTO customerDTO, Map<String, Object> headerMap, boolean isCustomerSearch, PasswordHistoryManagement pm) {
		DBXResult dbxResult = new DBXResult();

		final String IS_Integrated = Boolean.toString(PartyUtils.getIntegratedFlag());

		ProfileManagementBackendDelegateImpl backend = new ProfileManagementBackendDelegateImpl();

		String customerId = customerDTO.getId();
		String username = customerDTO.getUserName();
		String legalEntityId = customerDTO.getCompanyLegalUnit();
		JsonObject basicResult = new JsonObject();

		if (StringUtils.isBlank(customerId)) {
			if (StringUtils.isBlank(username)) {
				ErrorCodeEnum.ERR_20612.setErrorCode(basicResult);
				basicResult.addProperty("Status", "Failure");
				dbxResult.setResponse(basicResult);
				return dbxResult;
			}
		}

		if (StringUtils.isBlank(customerId)) {
			customerDTO = (CustomerDTO) customerDTO.loadDTO();
			if (customerDTO != null) {
				customerId = customerDTO.getId();
			} else {
				ErrorCodeEnum.ERR_20688.setErrorCode(basicResult);
				basicResult.addProperty("Status", "Failure");
				dbxResult.setResponse(basicResult);
				return dbxResult;
			}
		}

		JsonObject resultJsonObject = new JsonObject();
		dbxResult.setResponse(resultJsonObject);
		String primaryCustomerId = "";
		BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

		backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
		backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());
		if (isCustomerSearch) {
			backendIdentifierDTO.setBackendId(customerDTO.getId());
			}
		 else {
			backendIdentifierDTO.setCustomer_id(customerDTO.getId());
			}
		boolean isProfileExists = false;
		String isEnrolled = "false";

		String customerStatus = StatusEnum.SID_CUS_NEW.name();

		String CustomerType_id = "";
		String companyId = "";
		String partyType = "";
		try {
			dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
					.get(backendIdentifierDTO, headerMap);
			if (dbxResult.getResponse() != null) {
				BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
				primaryCustomerId = identifierDTO.getBackendId();
				companyId = identifierDTO.getCompanyId();
				customerId = identifierDTO.getCustomer_id();
				isProfileExists = true;
				customerDTO = (CustomerDTO) (new CustomerDTO().loadDTO(identifierDTO.getCustomer_id()));
				isEnrolled = customerDTO.getIsEnrolled() + "";
				CustomerType_id = customerDTO.getCustomerType_id();
				customerStatus = customerDTO.getStatus_id();
			} else {
				backendIdentifierDTO = new BackendIdentifierDTO();
				backendIdentifierDTO
						.setBackendType(DTOConstants.PARTY);
				backendIdentifierDTO.setBackendId(customerDTO.getId());
				backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());
				dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
						.get(backendIdentifierDTO, headerMap);
				if (dbxResult.getResponse() != null) {
					BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
					primaryCustomerId = identifierDTO.getBackendId();
					companyId = identifierDTO.getCompanyId();
					customerId = identifierDTO.getCustomer_id();
					isProfileExists = true;
					customerDTO = (CustomerDTO) (new CustomerDTO().loadDTO(identifierDTO.getCustomer_id()));
					isEnrolled = customerDTO.getIsEnrolled() + "";
					CustomerType_id = customerDTO.getCustomerType_id();
					customerStatus = customerDTO.getStatus_id();
				} else {
					customerDTO = new CustomerDTO();
					customerDTO.setId(customerId);
					customerDTO = (CustomerDTO) customerDTO.loadDTO();
					if (customerDTO != null) {
						isProfileExists = true;
						isEnrolled = customerDTO.getIsEnrolled() + "";
						customerStatus = customerDTO.getStatus_id();
						CustomerType_id = customerDTO.getCustomerType_id();
						primaryCustomerId = customerId;
					} else {
						primaryCustomerId = customerId;
					}
				}
			}
		} catch (ApplicationException e1) {
			// TODO Auto-generated catch block
			alert.prepareError("Error while fetching backend identifier for backend ID " + customerDTO.getId()).log();
		}

		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("_customerId", customerId);
		postParametersMap.put("_legalEntityId", legalEntityId);
		
		JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
				URLConstants.CUSTOMER_BASIC_INFO_PROC);

		if (jsonobject != null && jsonobject.has(Param.OPSTATUS) && jsonobject.get(Param.OPSTATUS).getAsInt() == 0
				&& jsonobject.has("records")) {

			if (jsonobject.get("records").getAsJsonArray().size() > 0) {
				customerViewJson = jsonobject.get("records").getAsJsonArray().get(0).getAsJsonObject();
			}
            
		} else {
			ErrorCodeEnum.ERR_20689.setErrorCode(basicResult);
			basicResult.addProperty("Status", "Failure");
			dbxResult.setResponse(basicResult);
			return dbxResult;
		}
		try {
			JsonArray jsonArray = new JsonArray();
			if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
				
				if (StringUtils.isNotBlank(legalEntityId)) {
					headerMap.put("companyId", legalEntityId);
					companyId = legalEntityId;
				}
				String id = companyId + "-" + primaryCustomerId;
				

				String queryParams = "";

				queryParams += "alternateIdentifierNumber=" + id;
				queryParams += "&";
				queryParams += "alternateIdentifierType=BackOfficeIdentifier";

				if(!isProfileExists) {
					PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
					String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
								+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_SEARCH5) + queryParams;
					  
			            DBXResult dbxResult1 = new DBXResult();
			            dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
						
					if (dbxResult1.getResponse() != null) {
						JsonElement partyResponse = new JsonParser().parse((String) dbxResult1.getResponse());
						if (partyResponse.isJsonObject()) {
							JsonObject partyResponseObject = partyResponse.getAsJsonObject();
							if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
									&& partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
								jsonArray = partyResponseObject.get("parties").getAsJsonArray();
							}
						}
					}

					if (jsonArray.size() > 0) {
						for (JsonElement jsonelement : jsonArray) {
							JsonObject customerJson = jsonelement.getAsJsonObject();
							customerViewJson.add("DateOfBirth", customerJson.get("dateOfBirth"));
							customerViewJson.add("FirstName", customerJson.get("firstName"));
							if (customerViewJson.has("customerName") && !customerJson.get("customerName").isJsonNull()) {
								customerViewJson.add("Name", customerJson.get("customerName"));
							} else if (customerViewJson.has("name") && !customerJson.get("name").isJsonNull()) {
								customerViewJson.add("Name", customerJson.get("name"));
							} else {
								customerViewJson.addProperty("Name",
										(customerJson.has("firstName") && !customerJson.get("firstName").isJsonNull()
												&& StringUtils.isNotBlank(customerJson.get("firstName").getAsString())
														? customerJson.get("firstName").getAsString()
														: "")
												+ " "
												+ (customerJson.has("lastName")
														&& !customerJson.get("lastName").isJsonNull()
														&& StringUtils
																.isNotBlank(customerJson.get("lastName").getAsString())
																		? customerJson.get("lastName").getAsString()
																		: ""));
							}
							customerViewJson.add("LastName", customerJson.get("lastName"));
							customerViewJson.add("Gender", customerJson.get("gender"));
							customerViewJson.add("MaritalStatus_id", customerJson.get("maritalStatus"));

							JsonArray customerDetails = new JsonArray();
							if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.ALTERNATEIDENTITIES)
									&& jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).isJsonArray() && jsonelement
											.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray().size() > 0) {
								customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray();
								for (JsonElement customerInfos : customerDetails) {

									String identityNumber = JSONUtil.getString(customerInfos.getAsJsonObject(),
											"identityNumber");
									String[] identityNumberArray = identityNumber.split("-");
									if (identityNumberArray != null && identityNumberArray.length == 2) {
										id = identityNumberArray[1];
									}
								}
							}

							if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.PARTYIDENTIFIERS)
									&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).isJsonArray()
									&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray().size() > 0) {
								customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
								for (JsonElement taxids : customerDetails) {
									String type = JSONUtil.getString(taxids.getAsJsonObject(), "type");
									if (type.equalsIgnoreCase("SOCIAL.SECURITY.NO")) {
										String taxId = JSONUtil.getString(taxids.getAsJsonObject(), "identifierNumber");
										customerViewJson.addProperty("ssn", taxId);
										customerViewJson.addProperty("Ssn", taxId);
										customerViewJson.addProperty("SSN", taxId);
									}
								}
							}

							String employmenttype = "";
							if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.EMPLOYMENTS)
									&& jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).isJsonArray()
									&& jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).getAsJsonArray().size() > 0) {
								customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.EMPLOYMENTS).getAsJsonArray();
								for (JsonElement employments : customerDetails) {
									employmenttype = JSONUtil.getString(employments.getAsJsonObject(), "type");
									customerViewJson.addProperty("EmployementStatus_id", employmenttype);
								}
							}
							if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), DTOConstants.PARTYADDRESS)
									&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).isJsonArray()
									&& jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() > 0) {
								customerDetails = jsonelement.getAsJsonObject().get(DTOConstants.PARTYADDRESS).getAsJsonArray();
								for (JsonElement customerInfo : customerDetails) {

									String electronicAddress = JSONUtil.getString(customerInfo.getAsJsonObject(),
											"electronicAddress");
									String phoneNo = JSONUtil.getString(customerInfo.getAsJsonObject(), "phoneNo");
									if ("true"
											.equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
											&& customerInfo.getAsJsonObject().has("electronicAddress")) {
										customerViewJson.addProperty("PrimaryEmailAddress", electronicAddress);
									}
									if ("true"
											.equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
											&& customerInfo.getAsJsonObject().has("phoneNo")) {
										customerViewJson.addProperty("PrimaryPhoneNumber", phoneNo);
									}
								}

							}

							customerViewJson.addProperty("Customer_id", id);
							customerViewJson.add("sectorId", customerJson.get("sectorId"));
							customerViewJson.addProperty("sectorName", "");
							customerViewJson.addProperty("branchId", legalEntityId);
							customerViewJson.addProperty("branchName", "");
							customerViewJson.addProperty("CustomerStatus_id", customerStatus);
							customerViewJson.addProperty("legalEntityId",legalEntityId);
							partyType = JSONUtil.getString(jsonelement.getAsJsonObject(), "partyType");
							
						}
					}
				}

		
				
			else {
				String id1="";
				alert.prepareError("primaryyy"+primaryCustomerId).log();
				PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
				String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
						+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, primaryCustomerId);
				DBXResult dbxResult1 = new DBXResult();
				dbxResult1 = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
				 JsonObject customerJson = new JsonParser().parse((String) dbxResult1.getResponse()).getAsJsonObject();
						customerViewJson.add("DateOfBirth", customerJson.get("dateOfBirth"));
						customerViewJson.add("FirstName", customerJson.get("firstName"));
						if (customerViewJson.has("customerName") && !customerJson.get("customerName").isJsonNull()) {
							customerViewJson.add("Name", customerJson.get("customerName"));
						} else if (customerViewJson.has("name") && !customerJson.get("name").isJsonNull()) {
							customerViewJson.add("Name", customerJson.get("name"));
						} else {
							customerViewJson.addProperty("Name",
									(customerJson.has("firstName") && !customerJson.get("firstName").isJsonNull()
											&& StringUtils.isNotBlank(customerJson.get("firstName").getAsString())
													? customerJson.get("firstName").getAsString()
													: "")
											+ " "
											+ (customerJson.has("lastName")
													&& !customerJson.get("lastName").isJsonNull()
													&& StringUtils
															.isNotBlank(customerJson.get("lastName").getAsString())
																	? customerJson.get("lastName").getAsString()
																	: ""));
						}
						customerViewJson.add("LastName", customerJson.get("lastName"));
						customerViewJson.add("Gender", customerJson.get("gender"));
						customerViewJson.add("MaritalStatus_id", customerJson.get("maritalStatus"));

						JsonArray customerDetails = new JsonArray();
						if (JSONUtil.hasKey(customerJson, DTOConstants.ALTERNATEIDENTITIES)
								&& customerJson.get(DTOConstants.ALTERNATEIDENTITIES).isJsonArray() 
								&& customerJson.get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray().size() > 0) {
							customerDetails = customerJson.get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray();
							for (JsonElement customerInfos : customerDetails) {

								String identityNumber = JSONUtil.getString(customerInfos.getAsJsonObject(),
										"identityNumber");
								String[] identityNumberArray = identityNumber.split("-");
								if (identityNumberArray != null && identityNumberArray.length == 2) {
									id1 = identityNumberArray[1];
								}
							}
						}

						if (JSONUtil.hasKey(customerJson, DTOConstants.PARTYIDENTIFIERS)
								&& customerJson.get(DTOConstants.PARTYIDENTIFIERS).isJsonArray()
								&& customerJson.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray().size() > 0) {
							customerDetails = customerJson.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
							for (JsonElement taxids : customerDetails) {
								String type = JSONUtil.getString(taxids.getAsJsonObject(), "type");
								if (type.equalsIgnoreCase("SOCIAL.SECURITY.NO")) {
									String taxId = JSONUtil.getString(taxids.getAsJsonObject(), "identifierNumber");
									customerViewJson.addProperty("ssn", taxId);
									customerViewJson.addProperty("Ssn", taxId);
									customerViewJson.addProperty("SSN", taxId);
								}
							}
						}

						String employmenttype = "";
						if (JSONUtil.hasKey(customerJson,DTOConstants.EMPLOYMENTS)
								&& customerJson.get(DTOConstants.EMPLOYMENTS).isJsonArray()
								&& customerJson.get(DTOConstants.EMPLOYMENTS).getAsJsonArray().size() > 0) {
							customerDetails = customerJson.get(DTOConstants.EMPLOYMENTS).getAsJsonArray();
							for (JsonElement employments : customerDetails) {
								employmenttype = JSONUtil.getString(employments.getAsJsonObject(), "type");
								customerViewJson.addProperty("EmployementStatus_id", employmenttype);
							}
						}
						if (JSONUtil.hasKey(customerJson, DTOConstants.PARTYADDRESS)
								&& customerJson.get(DTOConstants.PARTYADDRESS).isJsonArray()
								&& customerJson.get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() > 0) {
							customerDetails = customerJson.get(DTOConstants.PARTYADDRESS).getAsJsonArray();
							for (JsonElement customerInfo : customerDetails) {

								String electronicAddress = JSONUtil.getString(customerInfo.getAsJsonObject(),
										"electronicAddress");
								String phoneNo = JSONUtil.getString(customerInfo.getAsJsonObject(), "phoneNo");
								if ("true"
										.equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
										&& customerInfo.getAsJsonObject().has("electronicAddress")) {
									customerViewJson.addProperty("PrimaryEmailAddress", electronicAddress);
								}
								if ("true"
										.equalsIgnoreCase(JSONUtil.getString(customerInfo.getAsJsonObject(), "primary"))
										&& customerInfo.getAsJsonObject().has("phoneNo")) {
									customerViewJson.addProperty("PrimaryPhoneNumber", phoneNo);
								}
							}

						}

						customerViewJson.addProperty("Customer_id", id1);
						customerViewJson.add("sectorId", customerJson.get("sectorId"));
						customerViewJson.addProperty("sectorName", "");
						customerViewJson.addProperty("branchId", legalEntityId);
						customerViewJson.addProperty("branchName", "");
						customerViewJson.addProperty("CustomerStatus_id", customerStatus);
						customerViewJson.addProperty("legalEntityId",legalEntityId);
						partyType = JSONUtil.getString(customerJson, "partyType");
						
			
			}
			}
		} catch (Exception e) {
		}
		
		if (!customerViewJson.has(InfinityConstants.isProfileExist)) {
			customerViewJson.addProperty(InfinityConstants.isProfileExist, isProfileExists + "");
		}

		if (!customerViewJson.has(InfinityConstants.isEnrolled)) {
			customerViewJson.addProperty(InfinityConstants.isEnrolled, isEnrolled + "");
		}

		if (customerViewJson.has(InfinityConstants.Customer_id)) {
			customerViewJson.add(InfinityConstants.id, customerViewJson.get(InfinityConstants.Customer_id));
		}

		customerViewJson.addProperty("CustomerType_id", CustomerType_id);
		
		
		
		if (customerId.equals(primaryCustomerId) && !isProfileExists) {

			if (partyType.equalsIgnoreCase("Individual")) {
				customerViewJson.addProperty("CustomerType_id",
						HelperMethods.getCustomerTypes().get("Retail"));
				customerViewJson.addProperty("isBusiness", "false");
			} else {
				customerViewJson.addProperty("isBusiness", "true");
				customerViewJson.addProperty("CustomerType_id",
						HelperMethods.getCustomerTypes().get("Business"));
			}

			customerViewJson.remove(InfinityConstants.id);
			customerViewJson.remove(InfinityConstants.Customer_id);
		} else {
			backend.getCustomerType(customerViewJson, customerDTO.getId(), headerMap, customerDTO.getCompanyLegalUnit());
	        customerViewJson.addProperty("id", customerId);
			customerViewJson.addProperty("Customer_id", customerId);
		}
		try {
			if (customerViewJson.has("Customer_id")) {
				int count = customerDTO.getLockCount();
				if (pm != null && ((count + 1) >= pm.getAccountLockoutThreshold())) {
					customerViewJson.addProperty("CustomerStatus_id", StatusEnum.SID_CUS_LOCKED.name());
				} else {

					String customerForlegalentity = customerViewJson.get("Customer_id").getAsString();
					CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
					customerLegalEntityDTO.setCustomer_id(customerForlegalentity);
					customerLegalEntityDTO.setLegalEntityId(legalEntityId);
					List<CustomerLegalEntityDTO> customerLegalEntityDTOs;
					customerLegalEntityDTOs = (List<CustomerLegalEntityDTO>) customerLegalEntityDTO.loadDTO();
					if (customerLegalEntityDTOs != null && customerLegalEntityDTOs.size() > 0)
						customerLegalEntityDTO = customerLegalEntityDTOs.get(0);
					if (StringUtils.isNotBlank(customerLegalEntityDTO.getStatus_id())) {
						customerViewJson.addProperty("CustomerStatus_id", customerLegalEntityDTO.getStatus_id());
					}

				}
			}

		} catch (Exception e) {
			alert.prepareError("Error here", e).log();
		}
		try {
			if (customerViewJson.has("Customer_id")) {
				String customerForlegalentity = customerViewJson.get("Customer_id").getAsString();
				getCustomerType(customerViewJson, customerForlegalentity, headerMap, legalEntityId);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		String statusId = customerViewJson.has("CustomerStatus_id")
				&& !customerViewJson.get("CustomerStatus_id").isJsonNull()
						? customerViewJson.get("CustomerStatus_id").getAsString()
						: HelperMethods.getCustomerStatus().get("NEW");
		isEnrolled = customerViewJson.has("isEnrolled") ? customerViewJson.get("isEnrolled").getAsString() : "false";

		if (StringUtils.isNotBlank(primaryCustomerId)) {
			customerViewJson.addProperty(InfinityConstants.primaryCustomerId, primaryCustomerId);
		} else {
			customerViewJson.add(InfinityConstants.primaryCustomerId, customerViewJson.get("id"));
		}

		String isEnrolledFromSpotlight = customerViewJson.has("isEnrolledFromSpotlight")
				? customerViewJson.get("isEnrolledFromSpotlight").getAsString()
				: "1";

		customerViewJson.addProperty("isEnrolledFromSpotlight", isEnrolledFromSpotlight);

		CredentialCheckerDTO credentialCheckerDTO = null;
		
		if (customerDTO != null) {
			credentialCheckerDTO = new CredentialCheckerDTO();
			credentialCheckerDTO.setUserName(customerDTO.getUserName());
			credentialCheckerDTO.setLinktype(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
			credentialCheckerDTO = (CredentialCheckerDTO) credentialCheckerDTO.loadDTO();
		}

		customerViewJson.addProperty(DBPUtilitiesConstants.IS_CUSTOMER_ENROLLED, isEnrolled);
		customerViewJson.addProperty(DBPUtilitiesConstants.CUSTOMER_STATUS, statusId);

		if (credentialCheckerDTO == null) {
			customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "false");
		} else if (credentialCheckerDTO != null) {
			customerViewJson.addProperty(DBPUtilitiesConstants.IS_ACTIVATION_LINK_SENT, "true");
		}

		customerViewJson.addProperty("isCustomerAccessiable", true);
		basicResult.add("customerbasicinfo_view", customerViewJson);

		JsonObject configuration = new JsonObject();
		if (customerViewJson.has("accountLockoutTime")) {
			configuration.addProperty("value", customerViewJson.get("accountLockoutTime").getAsString());
		} else {
			configuration.addProperty("value", "N/A");
			customerViewJson.addProperty("accountLockoutTime", "N/A");
		}

		basicResult.add("Configuration", configuration);
		String currentStatus;

		String lockedOnTS = "";
		if (customerViewJson.has("lockedOn")) {
			lockedOnTS = customerViewJson.get("lockedOn").getAsString();
		} else {
			configuration.addProperty("value", "N/A");
			customerViewJson.addProperty("lockedOn", "N/A");
		}

		customerViewJson.addProperty("CustomerStatus_id", statusId);
		if (customerViewJson.get("CustomerStatus_id").getAsString()
				.equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {
			currentStatus = "LOCKED";
		} else if (customerViewJson.get("CustomerStatus_id").getAsString()
				.equalsIgnoreCase(StatusEnum.SID_CUS_SUSPENDED.name())) {
			currentStatus = "SUSPENDED";
		} else if (customerViewJson.get("CustomerStatus_id").getAsString()
				.equalsIgnoreCase(StatusEnum.SID_CUS_ACTIVE.name())) {
			currentStatus = "ACTIVE";
		} else if (customerViewJson.get("CustomerStatus_id").getAsString()
				.equalsIgnoreCase(StatusEnum.SID_CUS_INACTIVE.name())) {
			currentStatus = "INACTIVE";
		} else {
			currentStatus = "NEW";
		}

		if (customerViewJson.get("CustomerStatus_id").getAsString()
				.equalsIgnoreCase(StatusEnum.SID_CUS_LOCKED.name())) {

			if (StringUtils.isNotBlank(lockedOnTS)) {
				String lockDuration = "0";

				if (customerViewJson.has("accountLockoutTime")) {
					lockDuration = customerViewJson.get("accountLockoutTime").getAsString();
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Calendar elapsedLockedOnDate = Calendar.getInstance();
				try {
					elapsedLockedOnDate.setTime(dateFormat.parse(lockedOnTS));
				} catch (ParseException e) {
				}
				elapsedLockedOnDate.add(Calendar.MINUTE, Integer.parseInt(lockDuration));
				Calendar currentDate = Calendar.getInstance();

				if (elapsedLockedOnDate.before(currentDate)) {
					// Time has been elapsed. Calling unlock service
					postParametersMap = new HashMap<String, Object>();
					postParametersMap.put("id", customerId);
					postParametersMap.put("lockCount", "0");
					postParametersMap.put("lockedOn", "");
					JsonObject unlockResponse = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
							URLConstants.CUSTOMER_UPDATE);

					basicResult.addProperty("unlockStatus", unlockResponse.toString());
					if (unlockResponse == null || !unlockResponse.has(Param.OPSTATUS)
							|| unlockResponse.get(Param.OPSTATUS).getAsInt() != 0) {
						ErrorCodeEnum.ERR_20538.setErrorCode(basicResult);
						dbxResult.setResponse(basicResult);
						return dbxResult;
					}
					// set updated status
					currentStatus = "ACTIVE";
				}
			}
		}

		JsonObject statusResponse = new JsonObject();
		statusResponse.addProperty("LockedOn", lockedOnTS);
		statusResponse.addProperty("Status", currentStatus);
		customerViewJson.add("OLBCustomerFlags", statusResponse);
		if (customerViewJson.get("CustomerType_id").getAsString()
				.equalsIgnoreCase(HelperMethods.getCustomerTypes().get("Prospect"))) {
			// Add address in basic information if the customer is of type prospect

			postParametersMap = new HashMap<String, Object>();
			postParametersMap.put(DBPUtilitiesConstants.FILTER, "CustomerId eq '" + customerId + "'");
			postParametersMap.put(DBPUtilitiesConstants.SELECT,
					"Address_id,AddressType,AddressLine1,AddressLine2,ZipCode,CityName,City_id,RegionName,Region_id,RegionCode,CountryName,Country_id,CountryCode,isPrimary");
			JsonObject readCustomerAddr = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
					URLConstants.CUSTOMER_ADDRESS_VIEW_GET);

			if (readCustomerAddr == null || !readCustomerAddr.has(Param.OPSTATUS)
					|| readCustomerAddr.get(Param.OPSTATUS).getAsInt() != 0
					|| !readCustomerAddr.has("customeraddress_view")) {
				ErrorCodeEnum.ERR_20881.setErrorCode(basicResult);

				dbxResult.setResponse(basicResult);
				return dbxResult;

			}
			JsonArray addressDataset = readCustomerAddr.get("customeraddress_view").getAsJsonArray();
			customerViewJson.add("Addresses", addressDataset);

		}

		// if (HelperMethods.getBusinessUserTypes().contains(customerTypeId)) {
		// customerViewJson.add("customerbusinesstype",
		// getBusniessTypesandSignatories(customerId, headerMap));
		// }
		
		dbxResult = getCustomerRequestNotificationCount(customerDTO, headerMap);
		
		if (dbxResult.getResponse() != null) {
			JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
			for (java.util.Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				customerViewJson.add(entry.getKey(), entry.getValue());
			}
		}

		basicResult.add("customerbasicinfo_view", customerViewJson);

		dbxResult.setResponse(basicResult);

		return dbxResult;
	}
    
    public void getCustomerType(JsonObject json, String id, Map<String, Object> headerMap, String LegalEntityId) {

        String CustomerType_id = "";

        if (IntegrationTemplateURLFinder.isIntegrated) {
            String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + id + 
            		DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + LegalEntityId;
            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.CONTRACT_CUSTOMERS_GET);
            if (response.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
                JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {

                    for (JsonElement arrayElement : jsonElement.getAsJsonArray()) {
                        filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL
                                + arrayElement.getAsJsonObject().get(InfinityConstants.contractId).getAsString();
                        input = new HashMap<String, Object>();
                        input.put(DBPUtilitiesConstants.FILTER, filter);
                        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                                URLConstants.CONTRACT_GET);
                        input.clear();
                        if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT)) {
                            jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT);
                            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                                JsonArray jsonArray = jsonElement.getAsJsonArray();
                                jsonObject = jsonArray.get(0).getAsJsonObject();
                                if (StringUtils.isBlank(CustomerType_id) || !CustomerType_id
                                        .contains(jsonObject.get(InfinityConstants.serviceType).getAsString())) {
                                    if (StringUtils.isNotBlank(CustomerType_id)) {
                                        CustomerType_id += ",";
                                    }
                                    CustomerType_id += jsonObject.get(InfinityConstants.serviceType).getAsString();
                                }
                            }
                        }
                    }

                }
            }
        } else {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + id;

            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.MEMBERSHIP_GET);

            if (result.has(DBPDatasetConstants.DATASET_MEMBERSHIP)) {
                JsonArray jsonArray = result.get(DBPDatasetConstants.DATASET_MEMBERSHIP).isJsonArray()
                        ? result.get(DBPDatasetConstants.DATASET_MEMBERSHIP).getAsJsonArray()
                        : new JsonArray();
                if (jsonArray.size() > 0) {
                    result = jsonArray.get(0).getAsJsonObject();
                    if (result.has(InfinityConstants.isBusinessType)
                            && !result.get(InfinityConstants.isBusinessType).isJsonNull()) {
                        if (Boolean.parseBoolean(result.get(InfinityConstants.isBusinessType).getAsString())
                                || "1".equals(result.get(InfinityConstants.isBusinessType).getAsString())) {
                            CustomerType_id = HelperMethods.getCustomerTypes().get("Business");

                        } else {
                            CustomerType_id = HelperMethods.getCustomerTypes().get("Retail");
                        }
                    }
                }
            }
        }

        if (StringUtils.isBlank(CustomerType_id) && json.has(InfinityConstants.CustomerType_id)
                && !json.get(InfinityConstants.CustomerType_id).isJsonNull()) {
            CustomerType_id = json.get(InfinityConstants.CustomerType_id).getAsString();
        } else if (StringUtils.isBlank(CustomerType_id) && json.has(InfinityConstants.CustomerType_Id)
                && !json.get(InfinityConstants.CustomerType_Id).isJsonNull()) {
            CustomerType_id = json.get(InfinityConstants.CustomerType_Id).getAsString();
        } else if (StringUtils.isBlank(CustomerType_id) && json.has(InfinityConstants.CustomerTypeId)
                && !json.get(InfinityConstants.CustomerTypeId).isJsonNull()) {
            CustomerType_id = json.get(InfinityConstants.CustomerTypeId).getAsString();
        }
        if (CustomerType_id.contains(HelperMethods.getCustomerTypes().get("Business"))) {
            json.addProperty("isBusiness", "true");
        } else {
            json.addProperty("isBusiness", "false");
        }

        json.addProperty("CustomerType_id", CustomerType_id);

    }
    
    @Override
	public DBXResult userIdSearch(String userName, Map<String, Object> headerMap) throws ApplicationException {
		DBXResult dbxResult = new DBXResult();
		try {

			Map<String, Object> postParametersMap = new HashMap<String, Object>();
			postParametersMap.put("_userName", userName);

			JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(postParametersMap, headerMap,
					URLConstants.USERID_SEARCH_PROC);

			if (jsonobject != null && jsonobject.has(Param.OPSTATUS) && jsonobject.get(Param.OPSTATUS).getAsInt() == 0
					&& jsonobject.has("records")) {
				dbxResult.setResponse(jsonobject);

			}

		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10379);
		}
		return dbxResult;

	}

	@Override
	public DBXResult customerLegalEntitiesGet(CustomerDTO customerDTO, Map<String, Object> headerMap)
			throws ApplicationException {
		
		ProfileManagementBackendDelegateImpl impl = new ProfileManagementBackendDelegateImpl();
		return impl.customerLegalEntitiesGet(customerDTO, headerMap);
		
		
	}




}
